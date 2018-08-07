package com.bat.goods.service.attribute.convertor;

import static com.bat.goods.service.common.Constant.OPERATION_TYPE_1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.bat.goods.api.attribute.dto.AttributeCmd;
import com.bat.goods.api.attribute.dto.AttributeValueCmd;
import com.bat.goods.api.attribute.dto.data.AttributeDTO;
import com.bat.goods.api.attribute.dto.data.AttributeValueDTO;
import com.bat.goods.dao.attribute.dataobject.AttributeDO;
import com.bat.goods.dao.attribute.dataobject.AttributeValueDO;

public class AttributeConvertor {
    /**
     * 商品属性数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static AttributeDO toAttributeDO(AttributeCmd cmd, Short operationType) {
        AttributeDO attributeDO = new AttributeDO();
        BeanUtils.copyProperties(cmd, attributeDO);
        if (operationType.equals(OPERATION_TYPE_1)) {
            attributeDO.setId(null);
        }
        Date date = new Date(System.currentTimeMillis());
        attributeDO.setCreateTime(date);
        attributeDO.setUpdateTime(date);
        return attributeDO;
    }

    /**
     * 商品属性值数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static AttributeValueDO toAttributeValueDO(Integer attributeId, AttributeValueCmd cmd) {
        AttributeValueDO attributeValueDO = new AttributeValueDO();
        BeanUtils.copyProperties(cmd, attributeValueDO);
        attributeValueDO.setAttributeId(attributeId);
        return attributeValueDO;
    }

    /**
     * 商品属性值数据源DO的适配
     * 
     * @param attributeValueCmdList
     * @return
     */
    public static List<AttributeValueDO> toAttributeValueDOList(Integer attributeId,
        List<AttributeValueCmd> attributeValueCmdList) {
        List<AttributeValueDO> attributeValueDOList = new ArrayList<>();
        attributeValueCmdList.forEach(cmd -> {
            AttributeValueDO attributeValueDO = new AttributeValueDO();
            BeanUtils.copyProperties(cmd, attributeValueDO);
            attributeValueDO.setAttributeId(attributeId);
            attributeValueDOList.add(attributeValueDO);
        });
        return attributeValueDOList;
    }

    /**
     * do列表转dto列表(商品属性)
     * 
     * @param attributeDOList
     * @return
     */
    public static List<AttributeDTO> toAttributeDTOList(List<AttributeDO> attributeDOList) {
        List<AttributeDTO> attributeDTOList = new ArrayList<>();
        attributeDOList.forEach(attributeDO -> {
            AttributeDTO attributeDTO = new AttributeDTO();
            BeanUtils.copyProperties(attributeDO, attributeDTO);
            attributeDTOList.add(attributeDTO);
        });
        return attributeDTOList;
    }

    /**
     * do列表转dto列表(商品属性值)
     * 
     * @param attributeValueDOList
     * @return
     */
    public static List<AttributeValueDTO> toAttributeValueDTOList(List<AttributeValueDO> attributeValueDOList) {
        List<AttributeValueDTO> attributeValueDTOList = new ArrayList<>();
        attributeValueDOList.forEach(attributeValueDO -> {
            AttributeValueDTO attributeValueDTO = new AttributeValueDTO();
            BeanUtils.copyProperties(attributeValueDO, attributeValueDTO);
            attributeValueDTOList.add(attributeValueDTO);
        });
        return attributeValueDTOList;
    }

    /**
     * do转dto(商品属性)
     * 
     * @param attributeDO
     * @return
     */
    public static AttributeDTO toAttributeDTO(AttributeDO attributeDO) {
        AttributeDTO attributeDTO = new AttributeDTO();
        BeanUtils.copyProperties(attributeDO, attributeDTO);
        return attributeDTO;
    }

}
