package com.bat.goods.api.user.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.bat.goods.api.base.PageQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/27 19:49
 */
@Data
@ApiModel(value = "UserGoodsListMobileQry", description = "分销商移动端配置id商品列表查询")
public class UserGoodsListMobileQry extends PageQry implements Serializable {

    @ApiModelProperty(value = "移动端配置id", example = "78445")
    private Integer mobileId;
    @ApiModelProperty(value = "商品分类id", required = false, example = "123343")
    private Integer classifyId;
    @ApiModelProperty(value = "热门：1 活动热销(只有登录后的用户支持) 2 新品上市，不传为全部", required = false, example = "123343")
    private Short hotType;
    @ApiModelProperty(value = "排序：1 价格(只有登录后的用户支持) 2 销量(只有登录后的用户支持) 3 时间，不传为综合", required = false, example = "123343")
    private Short sortType;
    @ApiModelProperty(value = "升序降序：1 升序 2 降序", required = false, example = "1")
    private Short sortWay;
    @ApiModelProperty(value = "搜索内容，商品名称/货品编号", required = false, example = "123343")
    private String content;
    @ApiModelProperty(value = "用作缓存key，一定不能传值", required = false, example = "78445")
    private String cacheKey = "userGoodsListMobile";

    @NotNull(message = "P_GOODS_USER_APPOINT_TYPE_NULL")
    @ApiModelProperty(value = "指定类型 1.指定分类 2.指定品牌 3.指定商品 4.全部商品")
    private Integer appointType;

    @ApiModelProperty(value = "模块类型 3商品推广模块 4商品列表模块")
    private Short moduleType;

    @ApiModelProperty(value = "指定ids(分类id或者品牌ids)", example = "123343")
    private List<Integer> pointIds;

    @ApiModelProperty(value = "是否缺货（标品） 0、不缺货 1、缺货 不传表示全部 ", required = false)
    private Short underStockFlag;

}
