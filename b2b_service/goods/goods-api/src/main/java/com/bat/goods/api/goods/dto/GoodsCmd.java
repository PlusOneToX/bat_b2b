package com.bat.goods.api.goods.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GoodsCmd", description = "商品信息")
public class GoodsCmd {

    private Integer id;
    @NotBlank(message = "P_GOODS_NAME_NULL")
    @ApiModelProperty(value = "商品名称", required = true, example = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品英文名称", required = false, example = "eng"
    private String goodsNameEn;
    @ApiModelProperty(value = "关键字", required = false, example = "G175F8DCBF82")
    private String keywords;
    @ApiModelProperty(value = "商品简介", required = false, example = "商品简介")
    private String introduce;
    @ApiModelProperty(value = "商品英文简介", required = false, example = "商品英文简介")
    private String introduceEn;
    @NotNull(message = "P_GOODS_SALE_STATUS")
    @ApiModelProperty(value = "上架状态，1未上架，2审批中，3已上架", required = true, example = "3")
    private Short saleStatus;
    @NotNull(message = "P_GOODS_TYPE_NULL")
    @ApiModelProperty(value = "商品类型 1-普通 2-定制", required = true, example = "1")
    private Short goodsType;
    @ApiModelProperty(value = "定制类型 0-标准定制 1-DIY定制(当商品类型为2时，此字段必填)", required = false, example = "1")
    private Short diyType;
    @NotBlank(message = "P_GOODS_IMAGE_NULL")
    @ApiModelProperty(value = "商品SPU中文图片地址1", required = true, example = "http://bat")
    private String imageUrl1;
    @ApiModelProperty(value = "商品SPU中文图片地址2", required = false, example = "http://bat")
    private String imageUrl2;
    @ApiModelProperty(value = "商品SPU中文图片地址3", required = false, example = "http://bat")
    private String imageUrl3;
    @ApiModelProperty(value = "商品SPU中文图片地址4", required = false, example = "http://bat")
    private String imageUrl4;
    @ApiModelProperty(value = "商品SPU中文图片地址5", required = false, example = "http://bat")
    private String imageUrl5;
    @ApiModelProperty(value = "商品SPU中文图片地址6", required = false, example = "http://bat")
    private String imageUrl6;
    @ApiModelProperty(value = "商品SPU英文图片地址1", required = false, example = "http://bat")
    private String imageUrl1en;
    @ApiModelProperty(value = "商品SPU英文图片地址2", required = false, example = "http://bat")
    private String imageUrl2en;
    @ApiModelProperty(value = "商品SPU英文图片地址3", required = false, example = "http://bat")
    private String imageUrl3en;
    @ApiModelProperty(value = "商品SPU英文图片地址4", required = false, example = "http://bat")
    private String imageUrl4en;
    @ApiModelProperty(value = "商品SPU英文图片地址5", required = false, example = "http://bat")
    private String imageUrl5en;
    @ApiModelProperty(value = "商品SPU英文图片地址6", required = false, example = "http://bat")
    private String imageUrl6en;
    @ApiModelProperty(value = "中文商品详情", required = false, example = "带格式的文本")
    private String contentUrl;
    @ApiModelProperty(value = "英文商品详情", required = false, example = "带格式的文本")
    private String contentUrlEn;
    @NotNull(message = "P_GOODS_DISTRIBUTOR_SCOPE_NULL")
    @ApiModelProperty(value = "分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员,6.指定分销商分组", required = true,
        example = "1")
    private Short distributorScope;
    @NotNull(message = "P_GOODS_DISTRIBUTOR_SCOPE_NO_NULL")
    @ApiModelProperty(value = "分销商不可视范围,0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", required = true, example = "0")
    private Short distributorScopeNo;

    @NotNull(message = "P_GOODS_BRAND_NULL")
    @ApiModelProperty(value = "商品品牌id", required = true, example = "10010")
    private Integer brandId;
    @ApiModelProperty(value = "商品品类id", required = false, example = "10010")
    private Integer categoryId;

    @NotNull(message = "P_GOODS_CLASSIFY_NULL")
    @ApiModelProperty(value = "商品分类id", required = true)
    private List<Integer> classifyIds;
    @ApiModelProperty(value = "可视，分销商等级id列表", required = false)
    private List<Integer> scalePriceIds;
    @ApiModelProperty(value = "可视，销售部门id列表", required = false)
    private List<Integer> departmentIds;
    @ApiModelProperty(value = "可视，业务员id列表", required = false)
    private List<Integer> adminIds;
    @ApiModelProperty(value = "可视，分销商id列表", required = false)
    private List<Integer> distributorIds;
    @ApiModelProperty(value = "分销商分组id列表，可视范围为6时需传值", required = false)
    private List<Integer> distributorGroupIds;
    @ApiModelProperty(value = "不可视，分销商等级id列表", required = false)
    private List<Integer> scalePriceNoIds;
    @ApiModelProperty(value = "不可视，销售部门id列表", required = false)
    private List<Integer> departmentNoIds;
    @ApiModelProperty(value = "不可视，业务员id列表", required = false)
    private List<Integer> adminNoIds;
    @ApiModelProperty(value = "不可视，分销商id列表", required = false)
    private List<Integer> distributorNoIds;

    @ApiModelProperty(value = "商品标签id列表", required = false)
    private List<Integer> tagIds;
    @ApiModelProperty(value = "商品参数id列表", required = false)
    private List<Integer> paramIds;

    @Valid
    @NotNull(message = "P_GOODS_SKU_NULL")
    @ApiModelProperty(value = "货品(SKU)列表", required = true)
    private List<GoodsItemCmd> goodsItems;

}
