package com.bat.order.api.deliver.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 发货单列表
 */
@Data
@ApiModel(value = "AdminOrderDeliverBillDTO", description = "发货单列表")
public class AdminOrderDeliverBillListDTO {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("配送id")
    private Integer distributionId;

    @ApiModelProperty("配送方式编码")
    private String distributionCode;

    @ApiModelProperty("配送方式名称")
    private String distributionName;

    @ApiModelProperty("发货状态 1待确认,2.已确认,3.已取消")
    private Short deliverStatus;

    @ApiModelProperty("物流编号")
    private String logisticsNo;

    @ApiModelProperty("物流状态：2-在途中,3-签收,4-问题件")
    private String logisticsStatus;

    @ApiModelProperty("erp出库单号")
    private String deliverErpNo;

    @ApiModelProperty("erp采购单号")
    private String deliverStkNo;

    @ApiModelProperty("是否需要推送第三方,1 为是需要推送，0为不需要推送")
    private Short push;

    @ApiModelProperty("同步给第三方成功与否: 1为同步成功，0为同步失败")
    private Short pushStatus;

    @ApiModelProperty("发货时间")
    private Date deliverTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("收货人姓名")
    private String userName;

    @ApiModelProperty("下单时间")
    private Date orderCreateTime;

    @ApiModelProperty("同步到erp错误信息")
    private String erpErrorLog="";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(Integer distributionId) {
        this.distributionId = distributionId;
    }

    public String getDistributionCode() {
        return distributionCode;
    }

    public void setDistributionCode(String distributionCode) {
        this.distributionCode = distributionCode;
    }

    public String getDistributionName() {
        return distributionName;
    }

    public void setDistributionName(String distributionName) {
        this.distributionName = distributionName;
    }

    public Short getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(Short deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(String logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    public String getDeliverErpNo() {
        return deliverErpNo;
    }

    public void setDeliverErpNo(String deliverErpNo) {
        this.deliverErpNo = deliverErpNo;
    }

    public String getDeliverStkNo() {
        return deliverStkNo;
    }

    public void setDeliverStkNo(String deliverStkNo) {
        this.deliverStkNo = deliverStkNo;
    }

    public Short getPush() {
        return push;
    }

    public void setPush(Short push) {
        this.push = push;
    }

    public Short getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Short pushStatus) {
        this.pushStatus = pushStatus;
    }

    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getErpErrorLog() {
        return erpErrorLog;
    }

    public void setErpErrorLog(String erpErrorLog) {
        this.erpErrorLog = erpErrorLog;
    }
}
