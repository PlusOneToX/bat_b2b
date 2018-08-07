package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

@Data
public class GoodsItemDataDO {
  private Integer id;
  private Integer goodsItemId;
  private String series;
  private String appliedDevice;
  private String modelEn;
  private String seriesEn;
  private String appliedDeviceEn;
  private Integer purchaseCycle;
  private String planStrategy;
}
