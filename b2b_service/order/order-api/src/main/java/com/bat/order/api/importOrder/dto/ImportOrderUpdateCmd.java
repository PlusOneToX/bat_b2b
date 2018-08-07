package com.bat.order.api.importOrder.dto;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2018/05/15 10:24
 */
@Data
public class ImportOrderUpdateCmd {

    @ApiModelProperty("导入订单id")
    private Integer id;

    @ApiModelProperty("订单类型")
    private String orderTypeValue;

    @ApiModelProperty("国家id")
    private Integer countryId;

    @ApiModelProperty("省份id")
    private Integer provinceId;

    @ApiModelProperty("市id")
    private Integer cityId;

    @ApiModelProperty("区/县id")
    private Integer districtId;

    /**
     * 地址
     */
    private String address;
    /**
     * 邮编
     */
    private String zipCode;
    /**
     * 收货人
     */
    private String userName;
    /**
     * 手机
     */
    private String mobile;

    /**
     * 电话
     */
    private String phone;

    @ApiModelProperty("支付方式")
    private short payWay;
    /**
     * 送货时间类型 1.工作日，2.任意时间 3.双休 4.指定日期
     */
    private short deliveryType;

    @ApiModelProperty("送货时间")
    private Date deliveryTime;

    @ApiModelProperty("配送方式id")
    private Integer distributionId;

    /**
     * 是否开具发票 0.否，1.是
     */
    private short isInvoice;
    /**
     * 发票类型 1.普通 2.增值税发票
     */
    private short invoiceType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 货品删除ids，多个以','分隔
     */
    private String goodsDelIds;

    private List<ImportOrderGoodsCmd> modifyGoodsList;
}
