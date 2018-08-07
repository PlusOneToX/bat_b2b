package com.bat.system.service.logistics.executor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.system.api.base.SystemException;
import com.bat.system.api.logistics.dto.FormulaCheckCmd;
import com.bat.system.api.logistics.dto.LogisticsCalculationQry;
import com.bat.system.api.logistics.dto.LogisticsQry;
import com.bat.system.api.logistics.dto.data.*;
import com.bat.system.dao.logistics.*;
import com.bat.system.dao.logistics.dataobject.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.system.api.logistics.dto.data.*;
import com.bat.system.dao.logistics.*;
import com.bat.system.dao.logistics.dataobject.*;
import com.bat.system.service.logistics.constant.BillingMethodConsts;
import com.bat.system.service.logistics.constant.DistributorScopeConsts;
import com.bat.system.service.logistics.constant.FormulaFlagConsts;
import com.bat.system.service.logistics.convertor.LogisticsAreaConvertor;
import com.bat.system.service.logistics.convertor.LogisticsConvertor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class LogisticsQryExc {
    @Resource
    private LogisticsMapper logisticsMapper;

    @Resource
    private LogisticsAreaMapper logisticsAreaMapper;

    @Resource
    private LogisticsDistributorGradeMapper logisticsDistributorGradeMapper;

    @Resource
    private LogisticsDepartmentMapper logisticsDepartmentMapper;

    @Resource
    private LogisticsUserMapper logisticsUserMapper;

    @Resource
    private LogisticsDistributorMapper logisticsDistributorMapper;

    @Resource
    private LogisticsThirdMappingMapper logisticsThirdMappingMapper;

    private ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

    public PageInfo<LogisticsDTO> listLogistics(LogisticsQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<LogisticsDOExtend> logisticsDOList = logisticsMapper.selectExtendByParams(map);
        PageInfo pageInfo = new PageInfo(logisticsDOList);
        List<LogisticsDTO> toLogisticsDTOList = LogisticsConvertor.toLogisticsDTOList(pageInfo.getList());
        pageInfo.setList(toLogisticsDTOList);
        return pageInfo;
    }

    public List<LogisticsDTO> listLogisticsFromRpcByParams(LogisticsRpcQry qry) {
        BeanMap map = BeanMap.create(qry);
        List<LogisticsDOExtend> logisticsDOList = logisticsMapper.selectExtendByParams(map);
        List<LogisticsDTO> toLogisticsDTOList = LogisticsConvertor.toLogisticsDTOList(logisticsDOList);
        return toLogisticsDTOList;
    }

    public LogisticsDTO getLogisticsById(Integer id) {
        LogisticsDO logisticsDO = logisticsMapper.selectByPrimaryKey(id);
        if (logisticsDO != null) {
            LogisticsDTO logisticsDTO = LogisticsConvertor.toLogisticsDTO(logisticsDO);
            // 可视范围
            switch (logisticsDO.getDistributorScope()) {
                // 指定的分销商等级
                case DistributorScopeConsts.APPOINT_DISTRIBUTOR_GRADE:
                    List<Integer> collect = logisticsDistributorGradeMapper.listByLogisticsId(logisticsDO.getId())
                        .stream().map(LogisticsDistributorGradeDO::getDistributorGradeId).collect(Collectors.toList());
                    logisticsDTO.setGradeIds(collect);
                    break;
                // 指定的销售部门
                case DistributorScopeConsts.APPOINT_DEPARTMENT:
                    List<Integer> collect1 = logisticsDepartmentMapper.listByLogisticsId(logisticsDO.getId()).stream()
                        .map(LogisticsDepartmentDO::getDepartmentId).collect(Collectors.toList());
                    logisticsDTO.setDepartmentIds(collect1);
                    break;
                // 指定的业务员（后台用户）
                case DistributorScopeConsts.APPOINT_USER:
                    List<Integer> collect2 = logisticsUserMapper.listByLogisticsId(logisticsDO.getId()).stream()
                        .map(LogisticsUserDO::getUserId).collect(Collectors.toList());
                    logisticsDTO.setUserIds(collect2);
                    break;
                // 指定的分销商
                case DistributorScopeConsts.APPOINT_DISTRIBUTOR:
                    List<Integer> collect3 = logisticsDistributorMapper.listByLogisticsId(logisticsDO.getId()).stream()
                        .map(LogisticsDistributorDO::getDistributorId).collect(Collectors.toList());
                    logisticsDTO.setDistributorIds(collect3);
                    break;
                case DistributorScopeConsts.ALL_DISTRIBUTOR:
                default:
                    break;
            }
            // 地区费用类型 0 同一设置 1 指定配送地区和费用
            logisticsDTO.setLogisticsCost(getAreaDTOByLogisticsId(logisticsDO));
            return logisticsDTO;
        }
        return null;
    }

    /**
     * 根据配送表获取配送计费
     *
     * @param logisticsDO
     * @return
     */
    private List<LogisticsAreaDTO> getAreaDTOByLogisticsId(LogisticsDO logisticsDO) {
        List<LogisticsAreaDO> areaDOList = logisticsAreaMapper.listByLogisticsIdGroupByGroupId(logisticsDO.getId());
        List<LogisticsAreaDTO> collect = areaDOList.stream().map(logisticsAreaDO -> {

            LogisticsAreaDTO logisticsAreaDTO = LogisticsAreaConvertor.toLogisticsAreaDTO(logisticsAreaDO);
            List<LogisticsAreaDO> logisticsAreaDOS =
                logisticsAreaMapper.listByLogisticsIdAndGroupId(logisticsDO.getId(), logisticsAreaDO.getGroupId());
            if (CollectionUtils.isNotEmpty(logisticsAreaDOS)) {
                List<LogisticsAreaDetailDTO> dtos = logisticsAreaDOS.stream().map(areaDO -> {
                    LogisticsAreaDetailDTO logisticsAreaDetailDTO = new LogisticsAreaDetailDTO();
                    logisticsAreaDetailDTO.setCountryId(areaDO.getCountryId());
                    logisticsAreaDetailDTO.setProvinceId(areaDO.getProvinceId());
                    logisticsAreaDetailDTO.setCityId(areaDO.getCityId());
                    return logisticsAreaDetailDTO;
                }).collect(Collectors.toList());
                logisticsAreaDTO.setAreaList(dtos);
            }
            return logisticsAreaDTO;

        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 验证公司
     *
     * @param cmd
     * @return
     */
    @SneakyThrows
    public FormulaCheckDTO verificationDistributionFormula(FormulaCheckCmd cmd) {
        String cost = fetchFormulaResult(cmd.getFormula(), cmd.getWeight(), cmd.getPrice(), cmd.getVolume());
        FormulaCheckDTO dto = new FormulaCheckDTO();
        dto.setCost(cost);
        return dto;
    }

    // 计算公式的结果
    @SneakyThrows
    private String fetchFormulaResult(String formula, Double weight, Double price, Double volume) {
        if (price < 0) {
            price = 0D;
        }
        formula = formula.toLowerCase();
        if (formula.contains("w")) {
            if (weight == null) {
                throw SystemException.buildException(ErrorCode.B_LOGISTICS_WEIGHT_NULL);
            }
            formula = formula.replace("w", weight.toString());
        }
        if (formula.contains("p")) {
            if (price == null) {
                throw SystemException.buildException(ErrorCode.B_LOGISTICS_PRICE_NULL);
            }
            formula = formula.replace("p", price.toString());
        }
        if (formula.contains("v")) {
            if (volume == null) {
                throw SystemException.buildException(ErrorCode.B_LOGISTICS_VOLUME_NULL);
            }
            formula = formula.replace("v", volume.toString());
        }
        formula = whileDoFormula(formula);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(Double.valueOf(jse.eval(formula).toString()));
    }

    /**
     * 递归循环计算
     *
     * @param formula
     * @return
     */
    private String whileDoFormula(String formula) throws Exception {
        List<String> status1 = extractContent(formula, '[', ']');
        for (String s : status1) {
            String str = s;
            if (s.contains("[") || s.contains("{")) {
                str = whileDoFormula(str);
                formula = formula.replace(s, String.valueOf(str));
                s = str;
            }
            BigDecimal result = new BigDecimal(jse.eval(s).toString()).setScale(6,BigDecimal.ROUND_HALF_UP);
            if (result.compareTo(new BigDecimal(0)) <= 0) {
                result = new BigDecimal(0);
            } else if (result.compareTo(new BigDecimal(0)) > 0) {
                result = result.setScale( 0, BigDecimal.ROUND_UP);
            }
            formula = formula.replace(s, result.stripTrailingZeros().toPlainString());
        }
        formula = formula.replace('[', ' ');
        formula = formula.replace(']', ' ');
        List<String> status2 = extractContent(formula, '{', '}');
        for (String s : status2) {
            String str = s;
            if (s.contains("[") || s.contains("{")) {
                str = whileDoFormula(str);
                formula = formula.replace(s, String.valueOf(str));
                s = str;
            }
            BigDecimal result = new BigDecimal(jse.eval(s).toString()).setScale(6,BigDecimal.ROUND_HALF_UP);
            if (result.compareTo(new BigDecimal(0)) < 0) {
                result = new BigDecimal(0);
            } else if (result.compareTo(new BigDecimal(0)) == 0) {
                result = new BigDecimal(0.5);
            } else if (result.compareTo(new BigDecimal(0)) > 0) {
                result = new BigDecimal(1);
            }
            formula = formula.replace(s, result.stripTrailingZeros().toPlainString());
        }
        formula = formula.replace('{', ' ');
        formula = formula.replace('}', ' ');
        return formula;
    }

    /**
     * []: 当[]中的数值 > 0 时， 整体向上取整数，如[7+2.2] = 10; 当[]中的数值 = 0 时， 整体值取0，如[0] = 0; 当[]中的数值 < 0 时， 整体值取0，如[0] = 0; 多用于给出倍数
     * {}: 当{}中的数值 >0 时，整体值取1，如{23565}=1,{0.000001}=1 当{}中的数值 =0 时，整体值取0.5，如{0}=0.5 当{}中的数值 <0
     * 时，整体值取0，如{-2255}=0,{-0.002}=0
     * 多用于给出状态值 截取公式中带有[],及{}中的内容并根据上面列出的条件判断取值
     */
    private List<String> extractContent(String content, char patter1, char patter2) {
        List<String> list = new ArrayList<>();
        int start = 0;
        int startFlag = 0;
        int endFlag = 0;
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == patter1) {
                startFlag++;
                if (startFlag == endFlag + 1) {
                    start = i;
                }
            } else if (content.charAt(i) == patter2) {
                endFlag++;
                if (endFlag == startFlag) {
                    list.add(content.substring(start + 1, i));
                }
            }
        }
        return list;
    }

    /**
     * 根据省市区 以及物流id查询物流费用
     *
     * @param qry
     * @return
     */
    public LogisticsQryDTO getLogisticsCalculationById(LogisticsCalculationQry qry) {
        Map<String, Object> map = new HashMap(BeanMap.create(qry));
        map.put("enable", 1);
        List<LogisticsDOExtend> logisticsDOS = logisticsMapper.selectExtendByParams(map);
        if (logisticsDOS != null && logisticsDOS.size() == 1) {
            LogisticsDOExtend logisticsDO1 = logisticsDOS.get(0);
            return calculatingCosts(logisticsDO1, qry);
        }
        return null;
    }

    /**
     * 根据地区和购买商品获取配送方式和计算配送费用
     *
     * @param qry
     * @return
     */
    public List<LogisticsQryDTO> listLogisticsCalculation(LogisticsCalculationQry qry) {
        Map<String, Object> map = new HashMap(BeanMap.create(qry));
        map.put("enable", 1);
        List<LogisticsDOExtend> logisticsDOS = logisticsMapper.selectExtendByParams(map);
        // 物流过滤
        List<LogisticsDOExtend> collect = getLogisticsDOStream(qry, logisticsDOS);
        return calculatingCosts(collect, qry);
    }

    /**
     * 计算一批物流费用 并排序
     *
     * @param collect
     */
    private List<LogisticsQryDTO> calculatingCosts(List<LogisticsDOExtend> collect, LogisticsCalculationQry qry) {
        List<LogisticsQryDTO> collect1 = collect.stream().map(logisticsDO -> calculatingCosts(logisticsDO, qry))
            .filter(Objects::nonNull).sorted(Comparator.comparing(LogisticsQryDTO::getCost, BigDecimal::compareTo))
            .collect(Collectors.toList());
        // 把到付 排到最后一个
        if (CollectionUtils.isNotEmpty(collect1)) {
            if (collect1.get(0) != null && collect1.get(0).getId().equals(34)
                && "到付".equals(collect1.get(0).getName())) {
                collect1.add(collect1.get(0));
                collect1.remove(0);
            }
        }
        return collect1;
    }

    /**
     * 极端单个物流费用
     * 
     * @param logisticsDO
     * @param qry
     * @return
     */
    LogisticsQryDTO calculatingCosts(LogisticsDOExtend logisticsDO, LogisticsCalculationQry qry) {
        LogisticsQryDTO dto = new LogisticsQryDTO();
        dto.setId(logisticsDO.getId());
        dto.setName(logisticsDO.getName());
        dto.setDescription(logisticsDO.getDescription());
        BigDecimal price = null;
        // Short appointAreaFlag = logisticsDO.getAppointAreaFlag();
        log.info("id:{},name:{}", logisticsDO.getId(), logisticsDO.getName());
        if (FormulaFlagConsts.NOT_USE_FORMULA == logisticsDO.getFormulaFlag()) {
            log.info("\t非公式计算===========================================");
            // 非公式计费
            price = defaultCost(qry, logisticsDO);
            if (price.compareTo(BigDecimal.valueOf(logisticsDO.getMinCost())) < 0) {
                price = BigDecimal.valueOf(logisticsDO.getMinCost());
            }
            dto.setCost(price);
            return dto;
        } else {
            try {
                log.info("\t公式计算=============================================");
                log.info("\t公式计算:{}", logisticsDO.getFormula());
                log.info("\t公式计算参数: w:{} p:{} v:{}", qry.getWeight(), qry.getPrice().doubleValue(), qry.getVolume());
                // 公式计费
                price = new BigDecimal(fetchFormulaResult(logisticsDO.getFormula(), qry.getWeight(),
                    qry.getPrice().doubleValue(), qry.getVolume()));
                log.info("\t公式计算结果: {}", price);
                if (price.compareTo(BigDecimal.valueOf(logisticsDO.getMinCost())) < 0) {
                    price = BigDecimal.valueOf(logisticsDO.getMinCost());
                }
                dto.setCost(price);
                return dto;
            } catch (Exception e) {
                log.info("计算结果异常，将不展示给前端选择");
                e.printStackTrace();
                return null;
            }
        }
    }

    private BigDecimal defaultCost(LogisticsCalculationQry qry, LogisticsDOExtend logisticsDO) {
        log.info("\tlogistics_id :{} 开始计算物流费", logisticsDO.getLogisticsId());
        AtomicReference<BigDecimal> price = new AtomicReference<>();
        if (BillingMethodConsts.BILLING_METHOD_WEIGHT == logisticsDO.getBillingMethod()) {
            if (qry.getWeight() == null) {
                throw SystemException.buildException(ErrorCode.B_LOGISTICS_WEIGHT_NULL);
            }
            // 重量// 单位克
            Double weight = qry.getWeight();
            // 单位千克
            BigDecimal weightKg = BigDecimal.valueOf(weight).divide(BigDecimal.valueOf(1000), RoundingMode.HALF_UP);
            BigDecimal firstWeight = BigDecimal.valueOf(logisticsDO.getFirstWeight());
            BigDecimal additionalWeight = BigDecimal.valueOf(logisticsDO.getAdditionalWeight());
            BigDecimal firstWeightCost = BigDecimal.valueOf(logisticsDO.getFirstWeightCost());
            BigDecimal additionalWeightCost = BigDecimal.valueOf(logisticsDO.getAdditionalWeightCost());
            // 不满首重按首重计算
            price.set(firstWeightCost);
            if (weightKg.compareTo(firstWeight) > 0) {
                log.info("\t首重费用：{}", price.get());
                BigDecimal subtract = weightKg.subtract(firstWeight);
                log.info("\t续重重量：{}", subtract);
                BigDecimal divide = BigDecimal.ZERO;
                if (additionalWeight.compareTo(BigDecimal.ZERO) != 0) {
                    divide = subtract.divide(additionalWeight, 0, RoundingMode.UP);
                }
                log.info("\t续重重量 单位，向上取整：{}", divide);
                BigDecimal multiply = divide.multiply(additionalWeightCost);
                log.info("\t续重重量 金额：{}", multiply);
                BigDecimal add = price.get().add(multiply);
                log.info("\t总费用 金额：{}", add);
                price.set(add);
            } else {
                log.info("\t物品重量：{} 没有达到首重：{}，按首重费用：{}计算。", weightKg, firstWeight, firstWeightCost);
            }
        } else if (BillingMethodConsts.BILLING_METHOD_VOLUME == logisticsDO.getBillingMethod()) {
            if (qry.getVolume() == null) {
                throw SystemException.buildException(ErrorCode.B_LOGISTICS_VOLUME_NULL);
            }
            // 体积
            Double volume = qry.getVolume();
            BigDecimal volumeM = BigDecimal.valueOf(volume).divide(BigDecimal.valueOf(1000), RoundingMode.HALF_UP);
            BigDecimal firstVolume = BigDecimal.valueOf(logisticsDO.getFirstVolume());
            BigDecimal additionalVolume = BigDecimal.valueOf(logisticsDO.getAdditionalVolume());
            BigDecimal firstVolumeCost = BigDecimal.valueOf(logisticsDO.getFirstVolumeCost());
            BigDecimal additionalVolumeCost = BigDecimal.valueOf(logisticsDO.getAdditionalVolumeCost());
            price.set(firstVolumeCost);
            if (volumeM.compareTo(firstVolume) > 0) {
                log.info("\t首体积费用：{}", price.get());
                BigDecimal subtract = volumeM.subtract(firstVolume);
                log.info("\t续体积体积量：{}", subtract);
                BigDecimal divide = BigDecimal.ZERO;
                if (additionalVolume.compareTo(BigDecimal.ZERO) != 0) {
                    divide = subtract.divide(additionalVolume, 0, RoundingMode.UP);
                }
                log.info("\t续体积体积量 单位，向上取整：{}", divide);
                BigDecimal multiply = divide.multiply(additionalVolumeCost);
                log.info("\t续体积体积量 金额：{}", multiply);
                BigDecimal add = price.get().add(multiply);
                log.info("\t总费用 金额：{}", add);
                price.set(add);
            } else {
                log.info("\t物品体积：{} 没有达到首体积：{}，按首体积费用：{}计算。", volumeM, firstVolume, firstVolumeCost);
            }
        }
        return price.get();
    }

    /**
     * 处理重量体积 动态sql 不好处理
     * 
     * 处理启用默认 与精确 地区下 的重复问题
     *
     * @param qry
     * @return
     */
    private List<LogisticsDOExtend> getLogisticsDOStream(LogisticsCalculationQry qry,
        List<LogisticsDOExtend> logisticsDOExtends) {
        List<LogisticsDOExtend> collect = logisticsDOExtends.stream().filter(logisticsDO -> {
            if (qry.getBillingMethod() == BillingMethodConsts.BILLING_METHOD_WEIGHT) {
                // 重量
                if (logisticsDO.getMinWeight() == null) {
                    logisticsDO.setMinWeight(0.00);
                }
                if (logisticsDO.getMaxWeight() == null || logisticsDO.getMaxWeight() == 0.00) {
                    return qry.getWeight() >= logisticsDO.getMinWeight() * 1000;
                } else {
                    return qry.getWeight() >= logisticsDO.getMinWeight() * 1000
                        && qry.getWeight() <= logisticsDO.getMaxWeight() * 1000;
                }
            } else if (qry.getBillingMethod() == BillingMethodConsts.BILLING_METHOD_VOLUME) {
                // 体积
                if (logisticsDO.getMinVolume() == null) {
                    logisticsDO.setMinVolume(0.00);
                }
                if (logisticsDO.getMaxVolume() == null || logisticsDO.getMaxVolume() == 0.00) {
                    return qry.getVolume() >= logisticsDO.getMinVolume() * 1000;
                } else {
                    return qry.getVolume() >= logisticsDO.getMinVolume() * 1000
                        && qry.getVolume() <= logisticsDO.getMaxVolume() * 1000;
                }
            }
            return true;
        }).collect(Collectors.toList());
        return collect;
    }

    public LogisticsThirdMappingDTO getThirdLogisticsByThirdTypeAndLogisticsId(Short thirdType, Integer logisticsId) {
        LogisticsThirdMappingDO mappingDO =
            logisticsThirdMappingMapper.getThirdLogisticsByThirdTypeAndLogisticsId(thirdType, logisticsId);
        return LogisticsConvertor.toLogisticsThirdMappingMapperDTO(mappingDO);
    }

    /**
     * 根据配送方式id获取配送方式
     * @param id
     * @return
     */
    public LogisticsRpcDTO getLogisticsRpcDTOById(Integer id) {
        LogisticsDO logisticsDO = logisticsMapper.selectByPrimaryKey(id);
        LogisticsRpcDTO rpcDTO = new LogisticsRpcDTO();
        BeanUtils.copyProperties(logisticsDO,rpcDTO);
        return rpcDTO;
    }

    // public List<LogisticsDO> getLogisticsByManufactor(String manufactors) {
    // return logisticsMapper.listByManufactor(manufactors);
    // }
    //
    // public LogisticsDTO getLogisticsByIdExcludeScope(Integer id) {
    // LogisticsDO logisticsDO = logisticsMapper.selectByPrimaryKey(id);
    // return LogisticsConvertor.toLogisticsDTO(logisticsDO);
    // }
    //
    // public List<LogisticsDTO> listLogisticsByKDNCodeAndDistributorId(String code, Integer distributorId) {
    // List<LogisticsDO> logisticsDOS = logisticsMapper.listByKDNCodeAndDistributorId(code, distributorId);
    // return LogisticsConvertor.toLogisticsDTOList(logisticsDOS);
    // }
    //
    // public List<LogisticsDTO> listLogisticsByName(String name) {
    // List<LogisticsDO> logisticsDOS = logisticsMapper.listLogisticsByName(name);
    // return LogisticsConvertor.toLogisticsDTOList(logisticsDOS);
    // }
    //
    // public List<LogisticsDTO> listLogisticsByLogisticsErpId(String logisticsErpId) {
    // List<LogisticsDO> logisticsDOS = logisticsMapper.listLogisticsByLogisticsErpId(logisticsErpId);
    // return LogisticsConvertor.toLogisticsDTOList(logisticsDOS);
    // }
    //
    // public Response<List<LogisticsRpcDTO>> listLogisticsByDistributorIdAndOtherParams(LogisticsRpcQry qry) {
    // return null;
    // }
}