package com.bat.system.api.promotion.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/27 9:04
 */
@Data
@ApiModel(value = "GoodsPromotionCreateCmd", description = "添加或修改商品推广")
public class GoodsPromotionCreateCmd {

    @ApiModelProperty("添加的时候不用传id，修改的时候传")
    private Integer id;

    @ApiModelProperty("推广商品名称")
    @NotNull(message = "P_EXTENSION_GOODS_NAME")
    private String extensionGoodsName;

    @ApiModelProperty("国外pc推广商品图片")
    private String pcEnExtensionImgUrl;

    @ApiModelProperty("国外pc推广商品跳转链接")
    private String pcEnExtensionGoodsUrl;

    @ApiModelProperty("国内pc推广商品图片")
    private String pcCnExtensionImgUrl;

    @ApiModelProperty("国内pc推广商品跳转链接")
    private String pcCnExtensionGoodsUrl;

    @ApiModelProperty("国外移动端推广商品图片")
    private String moEnExtensionImgUrl;

    @ApiModelProperty("国外移动端推广商品跳转链接")
    private String moEnExtensionGoodsUrl;

    @ApiModelProperty("国内移动端推广商品图片")
    private String moCnExtensionImgUrl;

    @ApiModelProperty("国内移动端推广商品跳转链接")
    private String moCnExtensionGoodsUrl;

    @ApiModelProperty("推广开始时间")
    @NotNull(message = "P_EXTENSION_START_TIME")
    private Date extensionStartTime;

    @ApiModelProperty("推广结束时间")
    @NotNull(message = "P_EXTENSION_END_TIME")
    private Date extensionEndTime;

    @ApiModelProperty(value = "分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员,6.指定分销商分组", required = true,
        example = "1")
    @NotNull(message = "P_GOODS_DISTRIBUTOR_SCOPE_NULL")
    private Short distributorScope;

    @ApiModelProperty(value = "可视，分销商等级id列表", required = false)
    private List<Integer> scalePriceIds;

    @ApiModelProperty(value = "可视，销售部门id列表", required = false)
    private List<Integer> departmentIds;

    @ApiModelProperty(value = "可视，业务员id列表", required = false)
    private List<Integer> adminIds;

    @ApiModelProperty(value = "可视，分销商id列表", required = false)
    private List<Integer> distributorIds;

}