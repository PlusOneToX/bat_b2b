package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsDepartmentRelevanceDO {
  private Integer id;
  private Integer departmentId;
  private Integer goodsId;
}
