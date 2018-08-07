package com.bat.order.api.order.dto.orderquery.admin;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.bat.order.api.basic.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/21 10:48
 */
@Data
@ApiModel(description = "订单列表")
public class AdminCustomerOrderInfoListQry extends BaseSearchQry {

    public AdminCustomerOrderInfoListQry() {
        super.attributeMapper.put((short)1, "setDistributorName");
        // super.attributeMapper.put((short)2, "setShopName");
        super.attributeMapper.put((short)3, "setShopCode");
        super.attributeMapper.put((short)4, "setUserName");
        super.attributeMapper.put((short)5, "setOrderNo");
        super.attributeMapper.put((short)6, "setId");
        super.attributeMapper.put((short)7, "setMobile");
        super.attributeMapper.put((short)8, "setOrderFactoryNo");
    }

    @ApiModelProperty(value = "付款状态 1.未付款，2.部分付款 3. 已付款 4.部分退款 5.退款申请中 6.已退款", example = "1")
    private Short payStatus;

    @ApiModelProperty(value = "订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成", example = "1")
    private Short orderStatus;

    @ApiModelProperty(value = "发货状态 0.全部 1.未发货2.出库中 3.部分发货 4.已发货 (如果订单状态为已确认 才判断发货状态)", example = "1")
    private Short deliverStatus;

    /**
     * 0全部 orderStatus0 deliverStatus0 订单全部 发货全部
     *
     * 1待确认 orderStatus1 deliverStatus0 订单待确认 发货全部
     *
     * 2待发货 orderStatus2 deliverStatus1 deliverStatus2 订单已确认 发货待发货 发货出库中
     *
     * 3部分发货 orderStatus2 deliverStatus3 订单已确认 发货部分发货
     *
     * 4待收货 orderStatus2 deliverStatus4 订单已确认 发货已发货
     *
     * 5已关闭 orderStatus4 deliverStatus0 订单已取消 发货全部
     *
     * 6已完成 orderStatus5 deliverStatus0 订单已完成 发货全部
     */
    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成", example = "1")
    private Short frontOrderStatus;

    @ApiModelProperty(value = "开始时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @NotNull(message = "P_SEARCH_TYPE_NULL")
    @ApiModelProperty(value = "查看常量确定", example = "4")
    private Short searchType;

    @ApiModelProperty("1分销商用户")
    private String distributorName;

    // @ApiModelProperty("2店铺名称")
    // private String shopName;

    @ApiModelProperty("3店铺编号")
    private String shopCode;

    @ApiModelProperty("4收货人")
    private String userName;

    @ApiModelProperty("5订单编号")
    private String orderNo;

    @ApiModelProperty("6订单id")
    private String id;

    @ApiModelProperty("7手机号")
    private String mobile;

    @ApiModelProperty("8工厂订单编号")
    private String orderFactoryNo;
}
