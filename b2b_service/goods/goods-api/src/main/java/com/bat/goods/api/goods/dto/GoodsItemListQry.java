package com.bat.goods.api.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "货品列表查询")
public class GoodsItemListQry {
    @ApiModelProperty(value = "搜索内容，货品名称/货品编号/货品条形码", required = false, example = "123343")
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


    @ApiModelProperty(value = "是否关联材质，1 关联(查询的货品都是已关联材质) 0、不关联（不处理货品和材质的关联关系）", required = false, example = "1")
    private Short relevanceMaterialFlag;
    /**
     * 指定itemIdList
     */
    private List<Integer> itemIdList;
}
