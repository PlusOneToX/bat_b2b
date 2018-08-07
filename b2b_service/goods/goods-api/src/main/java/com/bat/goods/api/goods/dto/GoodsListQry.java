package com.bat.goods.api.goods.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品列表查询")
public class GoodsListQry {
    @ApiModelProperty(value = "搜索内容类型，1 商品名称 2 商品编号 3 货品编号 4 条形码", required = false, example = "1")
    private Short contentType;
    @ApiModelProperty(value = "搜索内容，商品名称/商品编号/货品编号", required = false, example = "123343")
    private String content;
    @ApiModelProperty(value = "商品分类id", required = false, example = "123343")
    private Integer classifyId;
    @ApiModelProperty(value = "商品品牌id", required = false, example = "123343")
    private Integer brandId;
    @ApiModelProperty(value = "商品品类id", required = false, example = "123343")
    private Integer categoryId;
    @ApiModelProperty(value = "商品类型，1 普通 2 定制", required = false, example = "1")
    private Short goodsType;
    @ApiModelProperty(value = "上架状态，1未上架，2审批中，3已上架", required = false, example = "3")
    private Short saleStatus;
    @ApiModelProperty(value = "冻结状态，1未冻结，2冻结", required = false, example = "1")
    private Short freezeStatus;
    @NotNull(message = "P_GOODS_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;
    @NotNull(message = "P_GOODS_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
