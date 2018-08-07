package com.bat.system.service.logistics.convertor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.logistics.dto.LogisticsCreateCmd;
import com.bat.system.api.logistics.dto.LogisticsUpdateCmd;
import com.bat.system.api.logistics.dto.data.LogisticsDTO;
import com.bat.system.api.logistics.dto.data.LogisticsThirdMappingDTO;
import com.bat.system.dao.logistics.dataobject.LogisticsDO;
import com.bat.system.dao.logistics.dataobject.LogisticsThirdMappingDO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class LogisticsConvertor {

    public static LogisticsDO toLogisticsDO(LogisticsCreateCmd cmd) {
        if (cmd != null) {
            LogisticsDO logisticsDO = new LogisticsDO();
            BeanUtils.copyProperties(cmd, logisticsDO);
            if (cmd.getMaxWeight() == null) {
                logisticsDO.setMaxWeight(0.00);
            }
            if (cmd.getMaxVolume() == null) {
                logisticsDO.setMaxVolume(0.00);
            }
            Date date = new Date();
            logisticsDO.setCreateTime(date);
            logisticsDO.setUpdateTime(date);
            return logisticsDO;
        }
        return null;
    }

    public static LogisticsDO toLogisticsDO(LogisticsUpdateCmd cmd) {
        if (cmd != null) {
            LogisticsDO logisticsDO = new LogisticsDO();
            BeanUtils.copyProperties(cmd, logisticsDO);
            if (cmd.getMaxWeight() == null) {
                logisticsDO.setMaxWeight(0.00);
            }
            if (cmd.getMaxVolume() == null) {
                logisticsDO.setMaxVolume(0.00);
            }
            Date date = new Date();
            logisticsDO.setUpdateTime(date);
            return logisticsDO;
        }
        return null;
    }

    public static LogisticsDTO toLogisticsDTO(LogisticsDO logisticsDO) {
        if (logisticsDO != null) {
            LogisticsDTO organizationDTO = new LogisticsDTO();
            BeanUtils.copyProperties(logisticsDO, organizationDTO);
            return organizationDTO;
        }
        return null;
    }

    public static List<LogisticsDTO> toLogisticsDTOList(List<? extends LogisticsDO> logisticsDOList) {
        if (CollectionUtils.isNotEmpty(logisticsDOList)) {
            return logisticsDOList.stream().map(LogisticsConvertor::toLogisticsDTO).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static LogisticsThirdMappingDTO toLogisticsThirdMappingMapperDTO(LogisticsThirdMappingDO mappingDO) {
        if (mappingDO != null) {
            LogisticsThirdMappingDTO dto = new LogisticsThirdMappingDTO();
            BeanUtils.copyProperties(mappingDO, dto);
            return dto;
        } else {
            return null;
        }
    }
}
