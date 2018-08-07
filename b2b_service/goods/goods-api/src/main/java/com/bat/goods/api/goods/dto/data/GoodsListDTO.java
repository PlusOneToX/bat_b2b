package com.bat.goods.api.goods.dto.data;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品SPU(简易)信息")
public class GoodsListDTO {

    private Integer id;
    @ApiModelProperty(value = "商品名称", requi商品名称red = true, example = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品英文名称", required = true, example = "eng")
    private String goodsNameEn;
    @ApiModelProperty(value = "商品编码", required = true, example = "G175F8DCBF82")
    private String goodsNo;
    @ApiModelProperty(value = "商品简介", required = true, example = "商品简介")
    private String introduce;
    @ApiModelProperty(value = "商品英文简介", required = true, example = "商品英文简介")
    private String introduceEn;
    @ApiModelProperty(value = "上架状态，1未上架，2审批中，3已上架", required = true, example = "3")
    private String saleStatus;
    @ApiModelProperty(value = "上架时间", required = true, example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date saleTime;
    @ApiModelProperty(value = "冻结状态，1未冻结，2冻结", required = true, example = "1")
    private Short freezeStatus;
    @ApiModelProperty(value = "冻结时间", required = true, example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date freezeTime;
    @ApiModelProperty(value = "商品类型 1-普通 2-定制", required = true, example = "1")
    private Short goodsType;
    @ApiModelProperty(value = "定制类型 0-标准定制 1-DIY定制", required = true, example = "1")
    private Short diyType;
    @ApiModelProperty(value = "分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", required = true, example = "1")
    private Short distributorScope;
    @ApiModelProperty(value = "分销商不可视范围,0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", required = true, example = "0")
    private Short distributorScopeNo;
    @ApiModelProperty(value = "创建时间", required = true, example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "更新时间", required = true, example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "商品品牌id", required = true, example = "10010")
    private Integer brandId;
    @ApiModelProperty(value = "商品品类id", required = true, example = "10010")
    private Integer categoryId;

    @ApiModelProperty(value = "商品分类id", required = true)
    private List<Integer> classifyIds;

}
