package com.bat.goods.api.goods.dto;

import com.bat.goods.api.base.PageQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/27 19:49
 */
@Data
@ApiModel(value = "UserGoodsListMobilesQry", description = "移动端配置ids商品列表查询")
public class GoodsListMobilesQry extends PageQry implements Serializable {
    @NotNull(message = "P_GOODS_USER_MOBILE_ID_NULL")
    @ApiModelProperty(value = "移动端配置ids", example = "78445")
    private List<Integer> mobileIds;

    @NotNull(message = "P_MOBILE_MODULE_TYPE_NULL")
    @ApiModelProperty(value = "模块类型 3商品推广模块 4商品列表模块")
    private Short moduleType;

    @ApiModelProperty(value = "用作缓存key，一定不能传值", required = false, example = "78445")
    private String cacheKey = "userGoodsListMobile";
}
