package com.bat.system.api.globalsetting.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 13:47
 */
@Data
@ApiModel(value = "ShopSettingDTO")
public class ShopSettingDTO {
    @ApiModelProperty(value = "定制商品是否参与活动")
    private Integer customizedAttendEventFlag;
    @ApiModelProperty(value = "直运订单是否参与活动")
    private Integer directTransportationEventFlag;
    @ApiModelProperty(value = "提示语")
    private String hint;
    @ApiModelProperty(value = "mto订单是否参与活动")
    private Integer mtoAttendEventFlag;
    @ApiModelProperty(value = "上架多少天内为新品时间")
    private Integer newproductTime;
    @ApiModelProperty(value = "非直发分销商采用提示语 0否 1是")
    private Integer noStiffUseHint;
    @ApiModelProperty(value = "在途商品是否参与活动")
    private Integer onWayAttendEventFlag;
    @ApiModelProperty(value = "直发分销商采用提示语 0否 1是")
    private Integer stiffUseHint;
    @ApiModelProperty(value = "库存显示方式：0-显示实际库存，1-显示模糊库存")
    private Integer stockShowFlag;
    @ApiModelProperty(value = "库存显示临界值")
    private Integer stockShowNumber;

}
