package com.bat.system.service.logistics;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.bat.system.api.logistics.LogisticsService;
import com.bat.system.api.logistics.dto.*;
import com.bat.system.api.logistics.dto.data.FormulaCheckDTO;
import com.bat.system.api.logistics.dto.data.LogisticsDTO;
import com.bat.system.api.logistics.dto.data.LogisticsQryDTO;
import com.bat.system.api.user.dto.data.UserDTO;
import com.bat.system.service.logistics.executor.LogisticsCmdExc;
import com.bat.system.service.logistics.executor.LogisticsQryExc;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.system.api.logistics.dto.*;
import com.bat.system.service.common.CommonServiceImpl;
import com.bat.system.service.user.executor.UserQryExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:54
 */
@Service
@Slf4j
public class LogisticsServiceImpl implements LogisticsService {

    @Resource
    private LogisticsQryExc logisticsQryExc;

    @Resource
    private LogisticsCmdExc logisticsCmdExc;

    @Resource
    private CommonServiceImpl commonServiceImpl;

    @Resource
    private UserQryExc userQryExc;

    @Override
    public boolean createLogistics(LogisticsCreateCmd cmd) {
        return logisticsCmdExc.createLogistics(cmd);
    }

    @Override
    public LogisticsDTO getLogisticsById(Integer id) {
        return logisticsQryExc.getLogisticsById(id);
    }

    @Override
    public PageInfo<LogisticsDTO> listLogistics(LogisticsQry qry) {
        return logisticsQryExc.listLogistics(qry);
    }

    @Override
    public boolean deleteLogisticsById(Integer id) {
        return logisticsCmdExc.deleteLogisticsById(id);
    }

    @Override
    public boolean updateLogistics(LogisticsUpdateCmd cmd) {
        return logisticsCmdExc.updateLogistics(cmd);
    }

    @Override
    public FormulaCheckDTO verificationDistributionFormula(FormulaCheckCmd cmd) {
        return logisticsQryExc.verificationDistributionFormula(cmd);
    }

    /**
     * 计算单个物流
     * 
     * @param qry
     * @return
     */
    @Override
    public List<LogisticsQryDTO> listLogisticsCalculation(LogisticsCalculationQry qry) {
        log.error("获取配送方式请求参数:{}", JSONObject.toJSONString(qry));
        // 获取分销商所属业务员
        DistributorRpcDTO distributorInfo = commonServiceImpl.getDistributorInfo(qry.getDistributorId());
        Integer userId = distributorInfo.getSalesId();
        qry.setUserId(userId);
        // 获取业务员所属部门
        UserDTO userById = userQryExc.getUserById(userId);
        qry.setDepartmentId(userById.getDepartmentId());
        List<LogisticsQryDTO> logisticsQryDTOS = logisticsQryExc.listLogisticsCalculation(qry);
        if (logisticsQryDTOS.size() == 0) {
            log.error("当前取不到配送方式信息;当前传参:{}", JSONObject.toJSONString(qry));
        }
        return logisticsQryDTOS;
    }

    /**
     * 物流拆分计算物流费，并汇总 排序
     * 
     * @param qrys
     * @return
     */
    @Override
    public List<LogisticsQryDTO> listLogisticsCalculations(List<LogisticsCalculationQry> qrys) {
        // 获取分销商所属业务员
        DistributorRpcDTO distributorInfo = commonServiceImpl.getDistributorInfo(qrys.get(0).getDistributorId());
        Integer userId = distributorInfo.getSalesId();
        // 获取业务员所属部门
        UserDTO userById = userQryExc.getUserById(userId);
        // Map<Integer, LogisticsQryDTO> map = new HashMap<>(16);
        // 计算多次 累加求值（可以复用代码 但是性能有问题 里面的排序是用不到了）
        List<List<LogisticsQryDTO>> logisticsDTOs = qrys.stream().map(qry -> {
            qry.setUserId(userId);
            qry.setDepartmentId(userById.getDepartmentId());
            return logisticsQryExc.listLogisticsCalculation(qry);
        }).collect(Collectors.toList());
        // 深拷贝
        if (!CollectionUtils.isEmpty(logisticsDTOs)) {
            List<LogisticsQryDTO> dtos =
                logisticsDTOs.get(0).stream().map(LogisticsQryDTO::clone).collect(Collectors.toList());
            // 取交集
            logisticsDTOs.forEach(dtos::retainAll);
            // 有效的物流集合id
            List<Integer> ids = dtos.stream().map(LogisticsQryDTO::getId).collect(Collectors.toList());
            Map<Integer, LogisticsQryDTO> map = new HashMap<>(16);
            logisticsDTOs.forEach(logisticsQryDTOS -> {
                Map<Integer, LogisticsQryDTO> collect = logisticsQryDTOS.stream()
                    .collect(Collectors.toMap(LogisticsQryDTO::getId, logisticsQryDTO -> logisticsQryDTO));
                collect.forEach((id, logisticsQryDTO) -> {
                    // 有效的物流才参与求和
                    if (ids.contains(id)) {
                        LogisticsQryDTO dto = map.get(id);
                        if (dto == null) {
                            map.put(logisticsQryDTO.getId(), logisticsQryDTO);
                        } else {
                            dto.setCost(dto.getCost().add(logisticsQryDTO.getCost()));
                        }
                    }
                });
            });
            ArrayList<LogisticsQryDTO> collect1 = new ArrayList<>(map.values());
            collect1.sort(Comparator.comparing(LogisticsQryDTO::getCost, BigDecimal::compareTo));
            if (collect1.get(0) != null && collect1.get(0).getId().equals(34)
                && "到付".equals(collect1.get(0).getName())) {
                collect1.add(collect1.get(0));
                collect1.remove(0);
            }
            return collect1;
        }
        return new ArrayList<>();
    }

}