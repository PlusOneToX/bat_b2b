package com.bat.goods.dao.attribute;

import java.util.List;
import java.util.Map;

import com.bat.goods.dao.attribute.dataobject.AttributeDO;
import com.bat.goods.dao.attribute.dataobject.AttributeValueDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttributeMapper {

    Integer createAttribute(AttributeDO attributeDO);

    void createAttributeValues(List<AttributeValueDO> attributeValueDOS);

    Integer updateAttribute(AttributeDO attributeDO);

    void updateAttributeValues(List<AttributeValueDO> attributeValueDOS);

    void openAttribute(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    List<AttributeDO> listAttribute(Map map);

    Integer listCount(Map map);

    Integer listValueCount(@Param("attributeId") Integer attributeId);

    AttributeDO getById(@Param("id") Integer id);

    List<AttributeValueDO> listAttributeValue(@Param("attributeId") Integer attributeId);

    List<AttributeValueDO> listAllAttributeValue(@Param("attributeId") Integer attributeId);

    void deleteAttribute(@Param("id") Integer id);

    void deleteAttributeValuesByAttributeId(@Param("attributeId") Integer attributeId);

    void deleteAttributeValuesByIds(@Param("ids") List<Integer> ids);

}
