package com.bat.goods.service.attribute.executor;

import static com.bat.goods.service.common.Constant.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.service.attribute.convertor.AttributeConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.goods.api.attribute.dto.AttributeCmd;
import com.bat.goods.api.attribute.dto.AttributeId;
import com.bat.goods.api.attribute.dto.AttributeOpenCmd;
import com.bat.goods.api.attribute.dto.AttributeValueCmd;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.dao.attribute.AttributeMapper;
import com.bat.goods.dao.attribute.dataobject.AttributeDO;
import com.bat.goods.dao.attribute.dataobject.AttributeValueDO;

@Component
public class AttributeCmdExe {

    @Resource
    private AttributeMapper attributeMapper;

    /**
     * 创建商品属性
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createAttribute(AttributeCmd cmd) {
        AttributeDO attributeDO = AttributeConvertor.toAttributeDO(cmd, OPERATION_TYPE_1);
        attributeMapper.createAttribute(attributeDO);
        List<AttributeValueCmd> values = cmd.getValues();
        if (!CollectionUtils.isEmpty(values)) {
            List<AttributeValueDO> attributeValueDOList =
                AttributeConvertor.toAttributeValueDOList(attributeDO.getId(), values);
            attributeMapper.createAttributeValues(attributeValueDOList);
        }
    }

    /**
     * 更新商品属性
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAttribute(AttributeCmd cmd) {
        AttributeDO attributeDO = AttributeConvertor.toAttributeDO(cmd, OPERATION_TYPE_2);
        attributeMapper.updateAttribute(attributeDO);
        List<AttributeValueCmd> values = cmd.getValues();
        if (!CollectionUtils.isEmpty(values)) {
            List<Integer> delIds = new ArrayList<>();
            List<AttributeValueDO> addValueDos = new ArrayList<>();
            List<AttributeValueDO> updateValueDos = new ArrayList<>();
            values.forEach(value -> {
                if (value.getOperationType().equals(OPERATION_TYPE_1)) {
                    addValueDos.add(AttributeConvertor.toAttributeValueDO(attributeDO.getId(), value));
                } else if (value.getOperationType().equals(OPERATION_TYPE_2)) {
                    updateValueDos.add(AttributeConvertor.toAttributeValueDO(attributeDO.getId(), value));
                } else if (value.getOperationType().equals(OPERATION_TYPE_3)) {
                    delIds.add(value.getId());
                }
            });
            if (!CollectionUtils.isEmpty(delIds)) {
                attributeMapper.deleteAttributeValuesByIds(delIds);
            }
            if (!CollectionUtils.isEmpty(addValueDos)) {
                attributeMapper.createAttributeValues(addValueDos);
            }
            if (!CollectionUtils.isEmpty(updateValueDos)) {
                attributeMapper.updateAttributeValues(updateValueDos);
            }
        }
    }

    /**
     * 更新商品属性状态
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void openAttribute(AttributeOpenCmd cmd) {
        attributeMapper.openAttribute(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 删除商品属性
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttribute(AttributeId cmd) {
        AttributeDO attributeDO = attributeMapper.getById(cmd.getId());
        if (attributeDO == null) {
            throw GoodsException.buildException(ErrorCode.B_ATTRIBUTE_NULL);
        }
        // 停用的商品属性才允许删除
        if (!attributeDO.getOpenFlag().equals(OPEN_NO)) {
            throw GoodsException.buildException(ErrorCode.B_ATTRIBUTE_DELETE_OPEN_ERROR);
        }
        attributeMapper.deleteAttributeValuesByAttributeId(cmd.getId());
        attributeMapper.deleteAttribute(cmd.getId());
    }

}
