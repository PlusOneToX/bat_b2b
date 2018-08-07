package com.bat.system.service.storesetting.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.storesetting.dto.ColumnCreateCmd;
import com.bat.system.api.storesetting.dto.ColumnUpdateCmd;
import com.bat.system.api.storesetting.dto.data.ColumnDTO;
import com.bat.system.dao.storesetting.dataobject.ColumnDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class ColumnConvertor {

    public static ColumnDO toColumnDO(ColumnCreateCmd cmd) {
        ColumnDO columnDO = new ColumnDO();
        BeanUtils.copyProperties(cmd, columnDO);
        Date date = new Date();
        columnDO.setCreateTime(date);
        columnDO.setUpdateTime(date);
        return columnDO;
    }

    public static ColumnDO toColumnDO(ColumnUpdateCmd cmd) {
        ColumnDO columnDO = new ColumnDO();
        BeanUtils.copyProperties(cmd, columnDO);
        columnDO.setUpdateTime(new Date());
        return columnDO;
    }

    public static ColumnDTO toColumnDTO(ColumnDO regionDO) {
        ColumnDTO columnDTO = new ColumnDTO();
        BeanUtils.copyProperties(regionDO, columnDTO);
        return columnDTO;
    }

    public static List<ColumnDTO> toColumnDTOList(List<ColumnDO> regionDOList) {
        return regionDOList.stream().map(regionDO -> {
            ColumnDTO columnDTO = new ColumnDTO();
            BeanUtils.copyProperties(regionDO, columnDTO);
            return columnDTO;
        }).collect(Collectors.toList());
    }
}
