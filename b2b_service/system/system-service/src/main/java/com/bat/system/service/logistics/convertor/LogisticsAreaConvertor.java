package com.bat.system.service.logistics.convertor;

import java.util.Date;

import com.bat.system.api.logistics.dto.LogisticsAreaCmd;
import com.bat.system.api.logistics.dto.data.LogisticsAreaDTO;
import com.bat.system.dao.logistics.dataobject.LogisticsAreaDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/19 13:36
 */
public class LogisticsAreaConvertor {
    public static LogisticsAreaDO toLogisticsAreaDO(LogisticsAreaCmd cmd) {
        LogisticsAreaDO logisticsCostDO = new LogisticsAreaDO();
        BeanUtils.copyProperties(cmd, logisticsCostDO);
        Date date = new Date();
        logisticsCostDO.setCreateTime(date);
        logisticsCostDO.setUpdateTime(date);
        return logisticsCostDO;
    }

    public static LogisticsAreaDTO toLogisticsAreaDTO(LogisticsAreaDO areaDO) {
        LogisticsAreaDTO logisticsAreaDTO = new LogisticsAreaDTO();
        BeanUtils.copyProperties(areaDO, logisticsAreaDTO);
        return logisticsAreaDTO;
    }
}
