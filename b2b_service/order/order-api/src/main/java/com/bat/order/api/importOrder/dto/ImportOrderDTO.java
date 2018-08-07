package com.bat.order.api.importOrder.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/12 14:22
 */
@Data
public class ImportOrderDTO {

    private Integer id;

    @ApiModelProperty("erp分销商内码")
    private String erpDistributorNo;

    @ApiModelProperty("分销商id")
    private Integer distributorId;

    @ApiModelProperty("分销商名称")
    private String distributorName;

    @ApiModelProperty("订单类型")
    private String orderTypeValue;

    @ApiModelProperty("订单类型名称")
    private String orderTypeName;

    private Integer countryId;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    @ApiModelProperty("国家名称")
    private String countryName;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("区,县名称")
    private String districtName;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("邮编")
    private String zipCode;

    @ApiModelProperty("收货人")
    private String userName;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("电话")
    private String phone;

    private Short payWay;

    private Boolean deliveryType;

    @ApiModelProperty("送货时间")
    private Date deliveryTime;

    private Integer distributionId;

    @ApiModelProperty("配送名称")
    private String distributionName;

    private Short invoiceType;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("下单状态 1:已下单 0:未下单")
    private Short handleFlag;

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("erp订单编号")
    private String orderNo;

    @ApiModelProperty("订单下单时间")
    private Date orderCreateTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("货品总数量")
    private Integer countSum;

    @ApiModelProperty("货品总价")
    private BigDecimal amountSum;

    private Short isInvoice;

    @ApiModelProperty("操作人ID")
    private Integer operateUid;

    @ApiModelProperty("订单币种 CNY ,USD")
    private String currencyType;

    @ApiModelProperty("订单汇率")
    private BigDecimal currencyRates;

    @ApiModelProperty("'提交状态 1.未提交，2.提交中，3.提交失败';")
    private Short submitStatus = 1;

    private String orderSplitFlag;

    private String manufactor;

    @ApiModelProperty("错误提示")
    private String remind;

    /**
     * 关联的品牌
     */
    private List<Integer> brands;

    /**
     * 订单里面商品类型
     */
    private Short goodsType;

    // private List<PromotionRuleCart> rules = new ArrayList<>();
    // private GoodsGradeDiscountRuleCart gradeRule;
    // private ImportOrderDetailDiyBean newDiy;
}
