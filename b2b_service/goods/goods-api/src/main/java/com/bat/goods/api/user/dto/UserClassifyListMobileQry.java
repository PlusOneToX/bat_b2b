package com.bat.goods.api.user.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/27 19:49
 */
@Data
@ApiModel(value = "UserClassifyListMobileQry", description = "分销商移动端配置id商品分类列表查询")
public class UserClassifyListMobileQry implements Serializable {
    @NotNull(message = "P_GOODS_USER_MOBILE_ID_NULL")
    @ApiModelProperty(value = "移动端配置id", example = "78445")
    private Integer mobileId;
    @ApiModelProperty(value = "用作缓存key，一定不能传值", required = false, example = "78445")
    private String cacheKey = "userGoodsListMobile";
}
