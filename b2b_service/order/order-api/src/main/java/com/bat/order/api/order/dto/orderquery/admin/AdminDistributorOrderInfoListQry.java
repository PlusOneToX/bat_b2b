package com.bat.order.api.order.dto.orderquery.admin;

import com.bat.order.api.basic.BaseSearchQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/21 10:48
 */
@Data
@ApiModel(description = "订单列表")
public class AdminDistributorOrderInfoListQry extends BaseSearchQry {

    public AdminDistributorOrderInfoListQry() {
        super.attributeMapper.put((short)1, "setOrderNo");
        super.attributeMapper.put((short)2, "setOrderErpNo");
        super.attributeMapper.put((short)3, "setUserName");
        super.attributeMapper.put((short)4, "setDistributorName");
        super.attributeMapper.put((short)5, "setPayAmount");
        super.attributeMapper.put((short)6, "setId");
        super.attributeMapper.put((short)7, "setOrderThirdPartyNo");
        super.attributeMapper.put((short)8, "setMobile");
        super.attributeMapper.put((short)9, "setLogisticsNo");
        super.attributeMapper.put((short)10, "setOrderFactoryNo");
    }

    @ApiModelProperty(value = "库存类别 1在库 2在途 3预售(mto)  4 在途+在库 5、委外", example = "1")
    private Short stockType;

    @ApiModelProperty(value = "是否直发 自动下推出库 1.是 0.否", example = "1")
    private Short autoDelivery;

    @ApiModelProperty(value = "订单类型id", example = "1")
    private Integer orderTypeId;

    @ApiModelProperty(value = "付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付", example = "1")
    private Short payWay;

    @ApiModelProperty(value = "订单来源：分销平台编码", example = "1")
    private String orderSource;

    @ApiModelProperty(value = "付款状态 1.未付款，2.部分付款 3. 已付款 4.部分退款 5.退款申请中 6.已退款", example = "1")
    private Short payStatus;

    @ApiModelProperty(value = "订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成", example = "1")
    private Short orderStatus;

    @ApiModelProperty(value = "发货状态 0.全部 1.未发货2.出库中 3.部分发货 4.已发货 (如果订单状态为已确认 才判断发货状态)", example = "1")
    private Short deliverStatus;

    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成 7待付款 8 已拒绝 9出库中 ", example = "1")
    private Short frontOrderStatus;

    @ApiModelProperty(value = "是否同步erp 0.全部 1.已同步2.未同步", example = "1")
    private Short syncErpFlag;

    @ApiModelProperty(value = "开始时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "分销级数", example = "1")
    private Short treeNode;

    @NotNull(message = "P_SEARCH_TYPE_NULL")
    @ApiModelProperty(value = "查看常量确定", example = "1")
    private Short searchType;

    @ApiModelProperty("1订单编号")
    private String orderNo;

    @ApiModelProperty("2ERP订单号")
    private String orderErpNo;

    @ApiModelProperty("3收货人")
    private String userName;

    @ApiModelProperty("4分销商用户")
    private String distributorName;

    @ApiModelProperty("5订单总额")
    private BigDecimal payAmount;

    @ApiModelProperty("6订单id")
    private String id;

    @ApiModelProperty("7第三方编码")
    private String orderThirdPartyNo;

    @ApiModelProperty("8手机号")
    private String mobile;

    @ApiModelProperty("9快递单号")
    private String logisticsNo;

    @ApiModelProperty("10工厂订单编号")
    private String orderFactoryNo;

}
