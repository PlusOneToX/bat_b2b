package com.bat.goods.api.user.dto;

import java.io.Serializable;

import com.bat.goods.api.base.PageQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/27 19:49
 */
@Data
@ApiModel(value = "UserGoodsListQry", description = "商品列表查询(包含个性定制和新品商品筛选)")
public class UserGoodsListQry extends PageQry implements Serializable {
    @ApiModelProperty(value = "商品分类id", required = false, example = "123343")
    private Integer classifyId;
    @ApiModelProperty(value = "是否筛选收藏 0-否 1-是", required = false, example = "123343")
    private Short collectionFlag;
    @ApiModelProperty(value = "商品类型 1-普通 2-定制,不传查询所有商品", required = false, example = "123343")
    private Short goodsType;
    @ApiModelProperty(value = "热门：1 活动热销(只有登录后的用户支持) 2 新品上市，不传为全部", required = false, example = "123343")
    private Short hotType;
    @ApiModelProperty(value = "排序：1 价格(只有登录后的用户支持) 2 销量(只有登录后的用户支持) 3 时间，不传为综合", required = false, example = "123343")
    private Short sortType;
    @ApiModelProperty(value = "升序降序：1 升序 2 降序", required = false, example = "1")
    private Short sortWay;
    @ApiModelProperty(value = "搜索内容，商品名称/货品编号", required = false, example = "123343")
    private String content;
    @ApiModelProperty(value = "用作缓存key，一定不能传值", required = false, example = "78445")
    private String cacheKey = "userGoodsList";

    @ApiModelProperty(value = "是否缺货（标品） 0、不缺货 1、缺货 不传表示全部 ", required = false)
    private Short underStockFlag;

    @ApiModelProperty(value = "首页新品")
    private Integer newFlag;
}
