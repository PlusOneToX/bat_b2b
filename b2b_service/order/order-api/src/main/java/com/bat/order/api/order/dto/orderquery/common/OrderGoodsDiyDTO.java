package com.bat.order.api.order.dto.orderquery.common;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author: lim
 * @description: 定制商品补充信息
 * @date: 2018/6/22 12:47
 */
@Data
public class OrderGoodsDiyDTO implements Cloneable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单明细id")
    private Integer orderGoodsId;
    @ApiModelProperty("产品类型id")
    private Integer categoryId;
    @ApiModelProperty("产品类型名称")
    private String categoryName;
    @ApiModelProperty("材质id")
    private Integer materialId;
    @ApiModelProperty("材料名称")
    private String materialName;
    @ApiModelProperty("型号id")
    private Integer modelId;
    @ApiModelProperty("型号名称")
    private String modelName;
    @ApiModelProperty("品牌id")
    private Integer brandId;
    @ApiModelProperty("品牌名称")
    private String brandName;
    @ApiModelProperty("图片id(网络图为0)")
    private Integer pictureId;
    @ApiModelProperty("标签id")
    private Integer labelId;
    @ApiModelProperty("标签存放路径")
    private String labelUrl;
    @ApiModelProperty("生产图URL地址")
    private String generateImage;
    @ApiModelProperty("预览图URL地址")
    private String previewImage;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("兑换卡归属订单号")
    private Integer exchangeOrderId;

    @ApiModelProperty("发卡分销商公司名")
    private String distributorCompanyName;

    @ApiModelProperty("业务员姓名")
    private String salesName;

    @SneakyThrows
    @Override
    public OrderGoodsDiyDTO clone() {
        return (OrderGoodsDiyDTO)super.clone();
    }
}
