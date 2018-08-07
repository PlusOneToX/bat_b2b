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
public class AdminExceptionOrderInfoListQry extends BaseSearchQry {

    public AdminExceptionOrderInfoListQry() {
        super.attributeMapper.put((short)1, "setOrderNo");
        super.attributeMapper.put((short)2, "setDistributorName");
        super.attributeMapper.put((short)3, "setId");
    }

    @ApiModelProperty(value = "付款状态 1.未付款，2.部分付款 3. 已付款 4.部分退款 5.退款申请中 6.已退款", example = "1")
    private Short payStatus;

    @ApiModelProperty(value = "订单类型id", example = "1")
    private Integer orderTypeId;

    @ApiModelProperty(value = "付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付", example = "1")
    private Short payWay;

    @ApiModelProperty(value = "订单来源：分销平台编码", example = "1")
    private String orderSource;

    @ApiModelProperty(value = "开始时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @NotNull(message = "P_SEARCH_TYPE_NULL")
    @ApiModelProperty(value = "查看常量确定", example = "6")
    private Short searchType;

    @ApiModelProperty("1分销商用户")
    private String distributorName;

    @ApiModelProperty("2订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单异常类型 1.erp同步失败订单,2.工厂同步失败订单,3.长时间未发货订单", example = "1")
    private Short errorType;

    @ApiModelProperty("3订单id")
    private String id;

}
