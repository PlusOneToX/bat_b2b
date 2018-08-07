package com.bat.distributor.service.salesarea.convertor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bat.distributor.api.salesarea.dto.SalesAreaCmd;
import com.bat.distributor.api.salesarea.dto.data.SalesAreaDTO;
import com.bat.distributor.dao.salesarea.dataobject.SalesAreaDO;
import org.springframework.beans.BeanUtils;

public class SalesAreaConvertor {
    /**
     * 分销商销售区域数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static SalesAreaDO toSalesAreaDo(SalesAreaCmd cmd) {
        SalesAreaDO salesAreaDO = new SalesAreaDO();
        BeanUtils.copyProperties(cmd, salesAreaDO);
        Date date = new Date(System.currentTimeMillis());
        salesAreaDO.setCreateTime(date);
        salesAreaDO.setUpdateTime(date);
        return salesAreaDO;
    }

    /**
     * do列表转dto列表
     * 
     * @param doList
     * @return
     */
    public static List<SalesAreaDTO> toSalesAreaDTOList(List<SalesAreaDO> doList) {
        List<SalesAreaDTO> dtoList = new ArrayList<>();
        doList.forEach(salesAreaDO -> {
            SalesAreaDTO dto = new SalesAreaDTO();
            BeanUtils.copyProperties(salesAreaDO, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    /**
     * do转dto
     * 
     * @param salesAreaDO
     * @return
     */
    public static SalesAreaDTO toSalesAreaDTO(SalesAreaDO salesAreaDO) {
        SalesAreaDTO dto = new SalesAreaDTO();
        BeanUtils.copyProperties(salesAreaDO, dto);
        return dto;
    }

}
