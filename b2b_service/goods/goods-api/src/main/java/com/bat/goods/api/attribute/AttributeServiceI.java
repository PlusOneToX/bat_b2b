package com.bat.goods.api.attribute;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.attribute.dto.*;
import com.bat.goods.api.attribute.dto.*;
import com.bat.goods.api.attribute.dto.data.AttributeDTO;
import com.bat.goods.api.attribute.dto.data.AttributeValueDTO;

public interface AttributeServiceI {
    /**
     * 添加商品属性
     * 
     * @param cmd
     * @return
     */
    public void createAttribute(AttributeCmd cmd);

    /**
     * 更新商品属性
     * 
     * @param cmd
     * @return
     */
    public void updateAttribute(AttributeCmd cmd);

    /**
     * 更新商品属性状态
     * 
     * @param cmd
     * @return
     */
    public void openAttribute(AttributeOpenCmd cmd);

    /**
     * 获取商品属性列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<AttributeDTO> listAttribute(AttributeListQry qry);

    /**
     * 根据ID删除商品属性
     * 
     * @param cmd
     * @return
     */
    public void deleteAttribute(AttributeId cmd);

    /**
     * 根据商品属性id获取详情
     * 
     * @param id
     * @return
     */
    public AttributeDTO getAttribute(AttributeId id);

    /**
     * 根据商品属性id获取商品属性值列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<AttributeValueDTO> listAttributeValue(AttributeValueListQry qry);

    /**
     * 根据商品属性id获取所有的商品属性值
     * 
     * @param id
     * @return
     */
    public List<AttributeValueDTO> listAllAttributeValue(AttributeId id);

}
