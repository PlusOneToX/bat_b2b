package com.bat.goods.api.user.dto.data;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/30 16:42
 */
@Data
@ApiModel(description = "前台商品(SPU)列表信息")
public class UserGoodsListDTO {
    private Integer id;
    @ApiModelProperty(value = "商品名称", example = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品英文名称", example = "eng")
    private String goodsNameEn;
    @ApiModelProperty(value = "商品编码", example = "G175F8DCBF82")
    private String goodsNo;
    @ApiModelProperty(value = "商品类型 1-普通 2-定制", example = "1")
    private Short goodsType;
    @ApiModelProperty(value = "定制类型 0-标准定制 1-DIY定制", example = "1")
    private Short diyType;
    @ApiModelProperty(value = "商品SPU中文图片地址1", example = "http://bat")
    private String imageUrl1;
    @ApiModelProperty(value = "商品SPU英文图片地址1", example = "http://bat")
    private String imageUrl1en;
    @ApiModelProperty(value = "销量(只有登录的用户有返回值)", example = "100")
    private Integer saleNum;
    @ApiModelProperty(value = "是否已收藏 0-否 1-是", example = "123343")
    private Short collectionFlag = 0;
    @ApiModelProperty(value = "是否新品 0-否 1-是", example = "0")
    private Short newFlag = 0;
    @ApiModelProperty(value = "是否促销 0-否 1-是", example = "0")
    private Short promotionFlag = 0;
    @ApiModelProperty(value = "活动类型，1 普通活动，2 阶梯活动(promotionFlag为1时有值)", example = "1")
    private Short promoType;
    @ApiModelProperty(value = "是否拼团 0-否 1-是", example = "0")
    private Short groupFlag = 0;
    @ApiModelProperty(value = "是否秒杀 0-否 1-是", example = "0")
    private Short seckillFlag = 0;
    @ApiModelProperty(value = "上架时间", example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date saleTime;
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
    @ApiModelProperty("在库 1:有库存，0：无库存")
    private Short underStockFlag;
    @ApiModelProperty("在途 1:有库存，0：无库存")
    private Short whetherOutOfStockInTransit;
    @ApiModelProperty("1:活动，0：普通")
    private Integer activity;
    @ApiModelProperty("1:新品，0：非新品")
    private Integer xinpin;

    @ApiModelProperty("1有货、0无货")
    private Integer stock;

}
