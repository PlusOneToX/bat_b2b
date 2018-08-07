package com.bat.goods.api.goods.dto.data;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品SPU信息封装")
public class GoodsDTO {

    private Integer id;
    @ApiModelProperty(value = "商品名称", example = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品英文名称", example = "eng")
    private String goodsNameEn;
    @ApiModelProperty(value = "商品编码", example = "G175F8DCBF82")
    private String goodsNo;
    @ApiModelProperty(value = "关键字", example = "G175F8DCBF82")
    private String keywords;
    @ApiModelProperty(value = "商品简介", example = "商品简介")
    private String introduce;
    @ApiModelProperty(value = "商品英文简介", example = "商品英文简介")
    private String introduceEn;
    @ApiModelProperty(value = "上架状态，1未上架，2审批中，3已上架", example = "3")
    private String saleStatus;
    @ApiModelProperty(value = "上架时间", example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date saleTime;
    @ApiModelProperty(value = "冻结状态，1未冻结，2冻结", example = "1")
    private Short freezeStatus;
    @ApiModelProperty(value = "冻结时间", example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date freezeTime;
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
    @ApiModelProperty(value = "分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", example = "1")
    private Short distributorScope;
    @ApiModelProperty(value = "分销商不可视范围,0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", example = "0")
    private Short distributorScopeNo;
    @ApiModelProperty(value = "创建时间", example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "更新时间", example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "商品品牌id", example = "10010")
    private Integer brandId;
    @ApiModelProperty(value = "商品品类id", example = "10010")
    private Integer categoryId;

    @ApiModelProperty(value = "商品分类id")
    private List<Integer> classifyIds;
    @ApiModelProperty(value = "可视，分销商等级id列表")
    private List<Integer> scalePriceIds;
    @ApiModelProperty(value = "可视，销售部门id列表")
    private List<Integer> departmentIds;
    @ApiModelProperty(value = "可视，业务员id列表")
    private List<Integer> adminIds;
    @ApiModelProperty(value = "可视，分销商id列表")
    private List<GoodsDistributorScopeDTO> distributors;
    @ApiModelProperty(value = "可视，分销商分组id列表")
    private List<GoodsDistributorGroupDTO> distributorGroups;
    @ApiModelProperty(value = "不可视，分销商等级id列表")
    private List<Integer> scalePriceNoIds;
    @ApiModelProperty(value = "不可视，销售部门id列表")
    private List<Integer> departmentNoIds;
    @ApiModelProperty(value = "不可视，业务员id列表")
    private List<Integer> adminNoIds;
    @ApiModelProperty(value = "不可视，分销商id列表")
    private List<GoodsDistributorScopeDTO> distributorNos;

    @ApiModelProperty(value = "商品标签列表")
    private List<GoodsTagDTO> tagIds;
    @ApiModelProperty(value = "商品参数列表")
    private List<GoodsParamDTO> paramIds;

    @ApiModelProperty("货品(SKU)列表")
    private List<GoodsItemDTO> goodsItems;

}
