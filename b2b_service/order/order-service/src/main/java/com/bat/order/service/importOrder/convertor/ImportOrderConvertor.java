package com.bat.order.service.importOrder.convertor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.order.api.importOrder.dto.ImportOrderDTO;
import com.bat.order.dao.importOrder.dataobject.ImportOrderDO;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2018/05/12 15:08
 */
public class ImportOrderConvertor {

    public static ImportOrderDTO toImportOrderDTO(ImportOrderDO aDo) {
        ImportOrderDTO dto = new ImportOrderDTO();
        BeanUtils.copyProperties(aDo, dto);
        // private List<Long> brands;
        // private Short goodsType;
        return dto;
    }

    public static List<ImportOrderDTO> toImportOrderListDTO(List<ImportOrderDO> list) {
        return list.stream().map(ImportOrderConvertor::toImportOrderDTO).collect(Collectors.toList());
    }
}
