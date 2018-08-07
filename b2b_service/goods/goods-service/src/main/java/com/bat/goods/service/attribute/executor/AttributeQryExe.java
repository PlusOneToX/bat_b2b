package com.bat.goods.service.attribute.executor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.service.attribute.convertor.AttributeConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.goods.api.attribute.dto.AttributeId;
import com.bat.goods.api.attribute.dto.AttributeListQry;
import com.bat.goods.api.attribute.dto.AttributeValueListQry;
import com.bat.goods.api.attribute.dto.data.AttributeDTO;
import com.bat.goods.api.attribute.dto.data.AttributeValueDTO;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.dao.attribute.AttributeMapper;
import com.bat.goods.dao.attribute.dataobject.AttributeDO;
import com.bat.goods.dao.attribute.dataobject.AttributeValueDO;

@Component
public class AttributeQryExe {

    @Resource
    private AttributeMapper attributeMapper;

    /**
     * 查询商品属性列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<AttributeDTO> executeList(AttributeListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<AttributeDO> attributeDOList = attributeMapper.listAttribute(qryMap);
        PageInfo pageInfo = new PageInfo(attributeDOList);
        List<AttributeDTO> tagDTOList = AttributeConvertor.toAttributeDTOList(pageInfo.getList());
        pageInfo.setList(tagDTOList);
        return pageInfo;
    }

    /**
     * 根据商品属性ID查询商品属性详情
     * 
     * @param attributeId
     * @return
     */
    public AttributeDTO execute(AttributeId attributeId) {
        AttributeDO attributeDO = attributeMapper.getById(attributeId.getId());
        if (attributeDO == null) {
            throw GoodsException.buildException(ErrorCode.B_ATTRIBUTE_NULL);
        }
        AttributeDTO attributeDTO = AttributeConvertor.toAttributeDTO(attributeDO);
        List<AttributeValueDO> attributeValueDOList = attributeMapper.listAllAttributeValue(attributeDO.getId());
        if (!CollectionUtils.isEmpty(attributeValueDOList)) {
            List<AttributeValueDTO> attributeValueDTOList =
                AttributeConvertor.toAttributeValueDTOList(attributeValueDOList);
            attributeDTO.setValues(attributeValueDTOList);
        }
        return attributeDTO;
    }

    /**
     * 根据商品属性ID查询商品属性值列表
     * 
     * @param qry
     * @return
     */
    public PageInfo<AttributeValueDTO> executeAttributeValues(AttributeValueListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<AttributeValueDO> attributeValueDOList = attributeMapper.listAttributeValue(qry.getId());
        PageInfo pageInfo = new PageInfo(attributeValueDOList);
        List<AttributeValueDTO> attributeValueDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            attributeValueDTOList = AttributeConvertor.toAttributeValueDTOList(pageInfo.getList());
            pageInfo.setList(attributeValueDTOList);
        }
        return pageInfo;
    }

    /**
     * 根据商品属性ID查询所有的商品属性值
     * 
     * @param qry
     * @return
     */
    public List<AttributeValueDTO> executeAllAttributeValues(AttributeId qry) {
        List<AttributeValueDO> attributeValueDOList = attributeMapper.listAllAttributeValue(qry.getId());
        List<AttributeValueDTO> attributeValueDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(attributeValueDOList)) {
            attributeValueDTOList = AttributeConvertor.toAttributeValueDTOList(attributeValueDOList);
        }
        return attributeValueDTOList;
    }
}
