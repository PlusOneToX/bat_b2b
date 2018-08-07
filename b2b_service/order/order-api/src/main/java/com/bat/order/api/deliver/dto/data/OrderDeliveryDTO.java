package com.bat.order.api.deliver.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: 订单物流信息
 * @date: 2018/6/22 12:42
 */
@Data
public class OrderDeliveryDTO {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("配送方式id")
    private Integer distributionId;
    @ApiModelProperty("配送方式名称")
    private String distributionName;
    @ApiModelProperty("国家id")
    private Integer countryId;
    @ApiModelProperty("省份id")
    private Integer provinceId;
    @ApiModelProperty("城市id")
    private Integer cityId;
    @ApiModelProperty("区/县id")
    private Integer districtId;
    @ApiModelProperty("国家名称")
    private String countryName;
    @ApiModelProperty("省份名称")
    private String provinceName;
    @ApiModelProperty("城市名称")
    private String cityName;
    @ApiModelProperty("区县名称")
    private String districtName;
    @ApiModelProperty("街道地址")
    private String address;
    @ApiModelProperty("邮政编码")
    private String zipCode;
    @ApiModelProperty("收货人姓名")
    private String userName;
    @ApiModelProperty("手机")
    private String mobile;
    @ApiModelProperty("固定电话")
    private String phone;
    @ApiModelProperty("送货时间类型 1.工作日，2.任意时间 3.双休 4.指定日期")
    private Short deliveryType;
    @ApiModelProperty("送货时间")
    private Date deliveryTime;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
