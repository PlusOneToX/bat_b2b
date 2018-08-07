package com.bat.system.api.globalsetting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 13:47
 */
@Data
public class ShopSettingUpdateCmd {

    @ApiModelProperty(value = "定制商品是否参与活动", required = true, example = "0")
    private String customizedAttendEventFlag;
    @ApiModelProperty(value = "直运订单是否参与活动", required = true, example = "1")
    private String directTransportationEventFlag;
    @ApiModelProperty(value = "提示语", required = true,
        example = "尊敬的客户：请您仔细核对订单明细，订单下达后将直接到仓库发货，无法中途取消；下错订单取消需要走退货流程，退款在15个工作日左右完成；追加需求请再下一单，感谢您的配合.")
    private String hint;
    @ApiModelProperty(value = "mto订单是否参与活动", required = true, example = "1")
    private String mtoAttendEventFlag;
    @ApiModelProperty(value = "上架多少天内为新品时间", required = true, example = "30")
    private String newproductTime;
    @ApiModelProperty(value = "非直发分销商采用提示语 0否 1是", required = true, example = "1")
    private String noStiffUseHint;
    @ApiModelProperty(value = "在途商品是否参与活动", required = true, example = "0")
    private String onWayAttendEventFlag;
    @ApiModelProperty(value = "直发分销商采用提示语 0否 1是", required = true, example = "1")
    private String stiffUseHint;
    @ApiModelProperty(value = "库存显示方式：0-显示实际库存，1-显示模糊库存", required = true, example = "0")
    private String stockShowFlag;
    @ApiModelProperty(value = "库存显示临界值", required = true, example = "10")
    private String stockShowNumber;

}
