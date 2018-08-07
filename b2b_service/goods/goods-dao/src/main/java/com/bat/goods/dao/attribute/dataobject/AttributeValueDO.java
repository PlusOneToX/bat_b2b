package com.bat.goods.dao.attribute.dataobject;

import lombok.Data;

@Data
public class AttributeValueDO {
  private Integer id;
  private String name;
  private String nameEn;
  private Integer attributeId;
  private Integer sort;
}
