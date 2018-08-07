package com.bat.goods.api.user.dto.data;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "前台商品SPU信息封装")
public class UserGoodsDTO {

    private Integer id;
    @ApiModelProperty(value = "商品名称", example = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品英文名称", example = "eng")
    private String goodsNameEn;
    @ApiModelProperty(value = "商品编码", example = "G175F8DCBF82")
    private String goodsNo;
    @ApiModelProperty(value = "商品简介", example = "商品简介")
    private String introduce;
    @ApiModelProperty(value = "商品英文简介", example = "商品英文简介")
    private String introduceEn;
    @ApiModelProperty(value = "上架状态，1未上架，2审批中，3已上架", example = "3")
    private String saleStatus;
    @ApiModelProperty(value = "上架时间", example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date saleTime;
    @ApiModelProperty(value = "商品类型 1-普通 2-定制", example = "1")
    private Short goodsType;
    @ApiModelProperty(value = "定制类型 0-标准定制 1-DIY定制", example = "1")
    private Short diyType;
    @ApiModelProperty(value = "商品SPU中文图片地址1", example = "http://bat")
    private String imageUrl1;
    @ApiModelProperty(value = "商品SPU中文图片地址2", example = "http://bat")
    private String imageUrl2;
    @ApiModelProperty(value = "商品SPU中文图片地址3", example = "http://bat")
    private String imageUrl3;
    @ApiModelProperty(value = "商品SPU中文图片地址4", example = "http://bat")
    private String imageUrl4;
    @ApiModelProperty(value = "商品SPU中文图片地址5", example = "http://bat")
    private String imageUrl5;
    @ApiModelProperty(value = "商品SPU中文图片地址6", example = "http://bat")
    private String imageUrl6;
    @ApiModelProperty(value = "商品SPU英文图片地址1", example = "http://bat")
    private String imageUrl1en;
    @ApiModelProperty(value = "商品SPU英文图片地址2", example = "http://bat")
    private String imageUrl2en;
    @ApiModelProperty(value = "商品SPU英文图片地址3", example = "http://bat")
    private String imageUrl3en;
    @ApiModelProperty(value = "商品SPU英文图片地址4", example = "http://bat")
    private String imageUrl4en;
    @ApiModelProperty(value = "商品SPU英文图片地址5", example = "http://bat")
    private String imageUrl5en;
    @ApiModelProperty(value = "商品SPU英文图片地址6", example = "http://bat")
    private String imageUrl6en;
    @ApiModelProperty(value = "中文商品详情", example = "带格式的文本")
    private String contentUrl;
    @ApiModelProperty(value = "英文商品详情", example = "带格式的文本")
    private String contentUrlEn;
    @ApiModelProperty(value = "销量(只有登录的用户有返回值)", example = "100")
    private Integer saleNum;

    @ApiModelProperty(value = "商品品牌id", example = "10010")
    private Integer brandId;
    @ApiModelProperty(value = "商品品类id", example = "10010")
    private Integer categoryId;
    @ApiModelProperty(value = "商品品牌名称", example = "10010")
    private String brandName;
    @ApiModelProperty(value = "商品品类名称", example = "10010")
    private String categoryName;
    @ApiModelProperty(value = "商品品牌英文名称", example = "10010")
    private String brandNameEn;
    @ApiModelProperty(value = "商品品类英文名称", example = "10010")
    private String categoryNameEn;

    @ApiModelProperty(value = "商品标签列表")
    private List<UserTagDTO> tags;
    @ApiModelProperty(value = "商品参数列表")
    private List<UserParamDTO> params;

    @ApiModelProperty("货品(SKU)列表")
    private List<UserGoodsItemDTO> goodsItems;

}
