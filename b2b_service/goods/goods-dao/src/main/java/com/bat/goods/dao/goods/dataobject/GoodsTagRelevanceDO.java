package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsTagRelevanceDO {
  private Integer id;
  private Integer tagId;
  private Integer goodsId;
}
