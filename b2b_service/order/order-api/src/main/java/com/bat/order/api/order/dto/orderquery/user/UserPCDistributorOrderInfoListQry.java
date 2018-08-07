package com.bat.order.api.order.dto.orderquery.user;

import com.bat.order.api.basic.BaseSearchQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: lim
 * @description: 前台订单查询
 * @date: 2018/6/21 10:48
 */
@Data
@ApiModel(description = "订单列表")
public class UserPCDistributorOrderInfoListQry extends BaseSearchQry {

    @ApiModelProperty(value = "查看常量确定")
    private Short searchType;

    @ApiModelProperty(value = "付款状态 1.未付款，2.部分付款 3. 已付款 4.部分退款 5.退款申请中 6.已退款", example = "1")
    private Short payStatus;

    @ApiModelProperty(value = "订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成", example = "1")
    private Short orderStatus;

    @ApiModelProperty(value = "发货状态 0.全部 1.未发货2.出库中 3.部分发货 4.已发货 (如果订单状态为已确认 才判断发货状态)", example = "1")
    private Short deliverStatus;

    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成 7 待付款 8已拒绝 9出库中", example = "1")
    private Short frontOrderStatus;

    @ApiModelProperty(value = "B端客户id", example = "1")
    private Integer distributorId;

    @ApiModelProperty(value = "订单类型id", example = "1")
    private Integer orderTypeId;

    @ApiModelProperty("收货人")
    private String userName;

    @ApiModelProperty("订单主键")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    /**
     * 以下为高级查询参数
     */
    @ApiModelProperty(value = "开始时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "手机（精确查询）", example = "18995912083")
    private String mobile;

    @ApiModelProperty(value = "固定电话（精确查询）", example = "")
    private String phone;

    @ApiModelProperty(value = "地址（模糊查询）", example = "")
    private String address;

    /**
     * 柔性店铺参数
     */
    @ApiModelProperty(value = "店铺编码", example = "")
    private String shopCode;

    @ApiModelProperty(value = "店铺名称", example = "")
    private String shopName;

}
