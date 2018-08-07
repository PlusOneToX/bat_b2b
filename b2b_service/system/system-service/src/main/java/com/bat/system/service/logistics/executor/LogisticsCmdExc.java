package com.bat.system.service.logistics.executor;

import static com.bat.system.service.common.CommonConstant.OVERSEAS_NAME;
import static com.bat.system.service.common.CommonConstant.OVERSEAS_NAME_EN;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.logistics.dto.LogisticsCreateCmd;
import com.bat.system.api.logistics.dto.LogisticsUpdateCmd;
import com.bat.system.api.logistics.dto.data.LogisticsAreaDetailDTO;
import com.bat.system.dao.logistics.*;
import com.bat.system.dao.logistics.dataobject.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.system.dao.logistics.*;
import com.bat.system.dao.logistics.dataobject.*;
import com.bat.system.service.common.utils.CommonCheckUtils;
import com.bat.system.service.logistics.constant.DistributorScopeConsts;
import com.bat.system.service.logistics.convertor.LogisticsAreaConvertor;
import com.bat.system.service.logistics.convertor.LogisticsConvertor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class LogisticsCmdExc {

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
    private CheckUtils checkUtils;

    @Transactional(rollbackFor = Exception.class)
    public boolean createLogistics(LogisticsCreateCmd cmd) {
        BeanMap map = BeanMap.create(cmd);
        // 校验参数
        CheckUtils.checkBillingMethod(map);
        CheckUtils.checkLogisticsFactoryId(map);
        checkUtils.checkLogisticsCost(cmd);
        CommonCheckUtils.checkDistributorScope(map);
        LogisticsDO logisticsDO = LogisticsConvertor.toLogisticsDO(cmd);
        logisticsMapper.insert(logisticsDO);
        saveLogisticsArea(cmd, logisticsDO);
        saveDistributorScope(map, logisticsDO);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateLogistics(LogisticsUpdateCmd cmd) {
        if (logisticsMapper.selectByPrimaryKey(cmd.getId()) == null) {
            throw SystemException.buildException(ErrorCode.B_LOGISTICS_ID_EXISTS);
        }
        // 校验参数
        BeanMap map = BeanMap.create(cmd);
        CheckUtils.checkBillingMethod(map);
        CheckUtils.checkLogisticsFactoryId(map);
        checkUtils.checkLogisticsCost(cmd);
        CommonCheckUtils.checkDistributorScope(map);
        LogisticsDO logisticsDO = LogisticsConvertor.toLogisticsDO(cmd);
        logisticsMapper.updateByPrimaryKeySelective(logisticsDO);
        // 更新：先删除 后新增
        deleteRelationTable(logisticsDO.getId());
        LogisticsCreateCmd logisticsCreateCmd = new LogisticsCreateCmd();
        BeanUtils.copyProperties(cmd, logisticsCreateCmd);
        saveLogisticsArea(logisticsCreateCmd, logisticsDO);
        saveDistributorScope(map, logisticsDO);
        return true;
    }

    /**
     * 存储地区费用类型
     *
     * @param cmd
     * @param logisticsDO
     */
    private void saveLogisticsArea(LogisticsCreateCmd cmd, LogisticsDO logisticsDO) {
        List<LogisticsAreaDO> dos = new ArrayList<>();
        cmd.getLogisticsCost().forEach(logisticsAreaCmd -> {
            List<LogisticsAreaDetailDTO> areaList = logisticsAreaCmd.getAreaList();
            if (areaList.size() == 0) {
                LogisticsAreaDetailDTO dto = new LogisticsAreaDetailDTO();
                dto.setCountryId(null);
                dto.setProvinceId(null);
                dto.setCityId(null);
                dto.setDistrictId(null);
                areaList.add(dto);
            }
            // 后台更新 处理海外地址
            areaList.removeIf(
                next -> OVERSEAS_NAME.equals(next.getRegionName()) && OVERSEAS_NAME_EN.equals(next.getRegionNameEn()));
            // 前端传错值了
            areaList.forEach(dto -> {
                if (dto.getCityId() != null && dto.getCityId() <= 192 && dto.getCountryId() != null
                    && dto.getCountryId() == 0) {
                    dto.setCountryId(dto.getCityId());
                    dto.setCityId(0);
                }
            });
            String uuid = UUID.randomUUID().toString().replace("-", "");
            List<LogisticsAreaDO> logisticsAreaDOS = areaList.stream().map(logisticsAreaDetailDTO -> {
                LogisticsAreaDO logisticsAreaDO = LogisticsAreaConvertor.toLogisticsAreaDO(logisticsAreaCmd);
                logisticsAreaDO.setLogisticsId(logisticsDO.getId());
                logisticsAreaDO.setCountryId(logisticsAreaDetailDTO.getCountryId());
                logisticsAreaDO.setProvinceId(logisticsAreaDetailDTO.getProvinceId());
                logisticsAreaDO.setCityId(logisticsAreaDetailDTO.getCityId());
                // 只存省市 不存地区
                // logisticsAreaDO.setDistrictId(logisticsAreaDetailDTO.getDistrictId());
                logisticsAreaDO.setGroupId(uuid);
                logisticsAreaDO.setFormulaFlag(logisticsAreaCmd.getFormulaFlag());
                logisticsAreaDO.setFormula(logisticsAreaCmd.getFormula());
                logisticsAreaDO.setFirstWeightCost(logisticsAreaCmd.getFirstWeightCost());
                logisticsAreaDO.setFirstVolumeCost(logisticsAreaCmd.getFirstVolumeCost());
                logisticsAreaDO.setAdditionalVolumeCost(logisticsAreaCmd.getAdditionalVolumeCost());
                logisticsAreaDO.setAdditionalWeightCost(logisticsAreaCmd.getAdditionalWeightCost());
                logisticsAreaDO.setDefaultFlag(logisticsAreaCmd.getDefaultFlag());
                // logisticsAreaMapper.insert(logisticsAreaDO);
                return logisticsAreaDO;
            }).collect(Collectors.toList());
            dos.addAll(logisticsAreaDOS);
        });
        if (CollectionUtils.isNotEmpty(dos)) {
            logisticsAreaMapper.insertList(dos);
        }

    }

    private void saveDistributorScope(Map<String, Object> map, LogisticsDO logisticsDO) {
        // 可视范围
        switch ((short)map.get("distributorScope")) {
            // 全部分销商
            case DistributorScopeConsts.ALL_DISTRIBUTOR:
                break;
            // 指定的分销商等级
            case DistributorScopeConsts.APPOINT_DISTRIBUTOR_GRADE:
                List<Integer> gradeIds = (ArrayList)map.get("gradeIds");
                gradeIds.forEach(s -> {
                    LogisticsDistributorGradeDO aDo = new LogisticsDistributorGradeDO();
                    aDo.setLogisticsId(logisticsDO.getId());
                    aDo.setDistributorGradeId(s);
                    logisticsDistributorGradeMapper.insert(aDo);
                });
                break;
            // 指定的销售部门
            case DistributorScopeConsts.APPOINT_DEPARTMENT:
                List<Integer> departmentIds = (ArrayList)map.get("departmentIds");
                departmentIds.forEach(s -> {
                    LogisticsDepartmentDO aDo = new LogisticsDepartmentDO();
                    aDo.setLogisticsId(logisticsDO.getId());
                    aDo.setDepartmentId(s);
                    logisticsDepartmentMapper.insert(aDo);
                });
                break;
            // 指定的业务员（后台用户）
            case DistributorScopeConsts.APPOINT_USER:
                List<Integer> userIds = (ArrayList)map.get("userIds");
                userIds.forEach(s -> {
                    LogisticsUserDO aDo = new LogisticsUserDO();
                    aDo.setLogisticsId(logisticsDO.getId());
                    aDo.setUserId(s);
                    logisticsUserMapper.insert(aDo);
                });
                break;
            // 指定的分销商
            case DistributorScopeConsts.APPOINT_DISTRIBUTOR:
                List<Integer> distributorIds = (ArrayList)map.get("distributorIds");
                distributorIds.forEach(s -> {
                    LogisticsDistributorDO aDo = new LogisticsDistributorDO();
                    aDo.setLogisticsId(logisticsDO.getId());
                    aDo.setDistributorId(s);
                    logisticsDistributorMapper.insert(aDo);
                });
                break;
            default:
                break;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLogisticsById(Integer id) {
        deleteRelationTable(id);
        logisticsMapper.deleteByPrimaryKey(id);
        return true;
    }

    private void deleteRelationTable(Integer id) {
        logisticsAreaMapper.deleteByLogisticsId(id);
        logisticsDistributorGradeMapper.deleteByLogisticsId(id);
        logisticsDistributorMapper.deleteByLogisticsId(id);
        logisticsDepartmentMapper.deleteByLogisticsId(id);
        logisticsUserMapper.deleteByLogisticsId(id);
    }

}
