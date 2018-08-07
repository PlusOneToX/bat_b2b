package com.bat.system.api.promotion.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/26 19:43
 */
@Data
@ApiModel(value = "GoodsPromotionCmd", description = "商品推广列表")
public class GoodsPromotionCmd {

    private Integer id;

    @ApiModelProperty("推广商品名称")
    private String extensionGoodsName;

    @ApiModelProperty("推广开始时间")
    private Date extensionStartTime;

    @ApiModelProperty("推广结束时间")
    private Date extensionEndTime;

    @ApiModelProperty("状态:0、未开始；1、进行中；2、已结束；3、作废")
    private Integer goodsPromotionStatus;

}
