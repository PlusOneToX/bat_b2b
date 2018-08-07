package com.bat.distributor.api.distributor.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商业务信息")
public class DistributorBusinessCmd {

    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_SALES_ID")
    @ApiModelProperty(value = "业务员id", required = true, example = "13454")
    private Integer salesId;
    @ApiModelProperty(value = "商务员id", required = false, example = "13454")
    private Integer coordinatorId;
    @ApiModelProperty(value = "分销商分组ids", required = false, example = "13543")
    private String distributorGroupIds;
    @ApiModelProperty(value = "分销商分类id", required = false, example = "1456543")
    private Integer distributorCategoryId;
    @NotNull(message = "P_DISTRIBUTOR_AUTO_DELIVERY_NULL")
    @ApiModelProperty(value = "自动下推出库 1.是 0.否", required = true, example = "0")
    private Short autoDelivery;
    @NotNull(message = "P_DISTRIBUTOR_CAN_EXPORT_PRICE")
    @ApiModelProperty(value = "是否能导出报价 1.能 0.不能", required = true, example = "0")
    private Short canExportPrice;
    @NotNull(message = "P_DISTRIBUTOR_PROMOTION_SCOPE")
    @ApiModelProperty(value = "'参与活动 0-不参与活动 1-全部活动 2-指定活动类型", required = true, example = "0")
    private Short promotionScope;
    @ApiModelProperty(value = "可参与活动类型 1-营销活动 2-阶梯活动 3-拼团活动,如果多选中间用逗号隔开", required = false, example = "0")
    private String promotionTypes;
    @NotNull(message = "P_DISTRIBUTOR_RX_SHOP_SWITCH")
    @ApiModelProperty(value = "是否启用柔性店铺开关，1启用 0 不启用", required = true, example = "0")
    private Short rxShopSwitch;
    @NotNull(message = "P_DISTRIBUTOR_LOGISTICS_SMS_SWITCH")
    @ApiModelProperty(value = "发货短信提醒客户 1是 0否 默认是0", required = true, example = "0")
    private Short logisticsSmsSwitch;
    @NotNull(message = "P_DISTRIBUTOR_ON_WAY_FLAG")
    @ApiModelProperty(value = "是否支持在途库存 1是 0否 默认是1", required = true, example = "0")
    private Short onWayFlag;
    @NotNull(message = "P_DISTRIBUTOR_ONE_DEFAULT_SCALE_PRICE_NULL")
    @ApiModelProperty(value = "默认价格等级", required = true, example = "11252")
    private Integer scalePriceId;
    @ApiModelProperty(value = "默认分销模式价格等级", required = false, example = "11252")
    private Integer distributionScalePriceId;
}
