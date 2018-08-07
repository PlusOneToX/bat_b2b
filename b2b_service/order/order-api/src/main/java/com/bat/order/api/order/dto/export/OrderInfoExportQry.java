package com.bat.order.api.order.dto.export;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderInfoExportQry {

    @ApiModelProperty(value = "是否参加活动 true参加 false 不参加 ")
    private Boolean activityFlag;

    @ApiModelProperty(value = "指定分销商Id、不传表示全部")
    private Integer distributorId;

    @ApiModelProperty(value = "订单来源列表、平台编码")
    private List<String> orderSourceList;

    @ApiModelProperty(value = "订单类型id列表")
    private List<Integer> orderTypeIdList;

    @ApiModelProperty(value = "订单状态列表、数组 订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成 、美式逗号隔开")
    private List<Short> orderStatusList;

    @ApiModelProperty(value = "下单开始时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "下单结束时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "发货状态 1.未发货2.出库中 3.部分发货 4.已发货 ")
    private List<Short> deliverStatus;

    @ApiModelProperty(value = "付款状态 1.未付款，2.部分付款 3. 已付款 4.部分退款 5.退款申请中 6.已退款")
    private List<Short> payStatusList;

    @ApiModelProperty(value = "货品编码")
    private String itemCode;

    @ApiModelProperty(
        value = "导出字段、全部传空、具体如下(括号内中问注释不要传)：CREATE_TIME（下单日期）、ORDER_NO（B2B订单号）、ORDER_TYPE_ID（订单类型）、ORDER_STATUS（订单状态）、CURRENCY_TYPE（订单币种）"
            + "CURRENCY_RATES（订单汇率）、PAY_STATUS（付款状态）、DELIVER_STATUS（发货状态）、PAY_WAY（支付方式）、REMARK（订单备注）、ORDER_ERP_NO（ERP订单号）、ORDER_FACTORY_NO（工厂订单号）、"
            + "ORDER_THIRDPARTY_NO（第三方订单号）、ERP_OUTBOUND_NO（ERP出库单号）、ORDER_DELIVER_BILL_ID（B2B发货单号）、DISTRIBUTOR_ID（分销商ID(B2B编码)）、"
            + "DISTRIBUTOR_ERP_NO（分销商ID(ERP客户编码)）、DISTRIBUTOR_NAME（客户名称）、DEPARTMENT_NAME（部门名称）、SALES_NAME（销售员）、RECEIVER（收货人）、"
            + "RECEIVE_ADDRESS（收货地址）、CONTACT_MOBILE（联系电话）、DISTRIBUTION_NAME（配送方式）、LOGISTICS_NO（物流单号）、ITEM_CODE（货品编码）、ITEM_NAME（货品名称）、"
            + "ITEM_COUNT（销售数量）、DELIVER_COUNT（已发货数）、MATERIAL_NAME（材质）、MODEL_NAME（型号）、SALE_PRICE（价格（含税单价））、ACTUAL_PRICE（折后价（折后含税单价））、RETAIL_PRICE(零售价)、"
            + "SALE_PRICE_SUM（总额（折前含税总额））、ACTUAL_PRICE_SUM（总额（折后含税总额））、USER_ACTUAL_PRICE（C端下单总额（实付金额））、PROMOTION_ID（促销活动编码）、SHOP_NAME（门店名称）、SHOP_CODE(门店编码)")
    private List<String> fieldList;

}
