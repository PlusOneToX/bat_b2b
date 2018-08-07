package com.bat.financial.api.voucher.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.bat.financial.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/5 18:51
 */
@Data
@ApiModel(value = "VoucherQry", description = "收款单查询")
public class VoucherQry extends BaseSearchQry {

    public VoucherQry() {
        super.attributeMapper.put((short)1, "setDistributorName");
        super.attributeMapper.put((short)2, "setId");
        super.attributeMapper.put((short)3, "setVoucherErpNo");
        super.attributeMapper.put((short)4, "setOutTradeNo");
    }

    @NotNull(message = "P_VOUCHER_USER_NULL")
    @ApiModelProperty(value = "当前用户", required = true, example = "1")
    private Integer userId;

    @ApiModelProperty(value = "收款单状态,1待确认,2已确认,3已取消", example = "1")
    private Short voucherStatus;

    @ApiModelProperty(value = "付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付", example = "1")
    private Short payWay;

    @ApiModelProperty(value = "业务类型 1订单收款2在线充值收款", example = "1")
    private Short businessType;

    @ApiModelProperty(value = "业务id 订单id", example = "1")
    private String businessId;

    @ApiModelProperty(value = "是否同步erp 0未同步 1已同步", example = "0")
    private Short syncErpFlag;

    @ApiModelProperty(value = "开始时间", example = "2018-06-01 18:22:40")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2018-06-05 18:22:40")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "1.分销商用户名(模糊搜索)", example = "0")
    private String distributorName;

    @ApiModelProperty(value = "2.收款单编号(精确搜索)", example = "0")
    private String id;

    @ApiModelProperty(value = "3.ERP收款单编号(精确搜索)", example = "0")
    private String voucherErpNo;

    @ApiModelProperty(value = "4.支付凭证ID(精确搜索)", example = "0")
    private String outTradeNo;

}
