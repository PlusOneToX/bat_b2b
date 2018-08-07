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
@ApiModel(value = "UserClassifyListColumnQry", description = "分销栏目商品分类列表查询")
public class UserClassifyListColumnQry implements Serializable {
//    @NotNull(message = "P_GOODS_USER_COLUMN_ID_NULL")
    @ApiModelProperty(value = "栏目id", example = "78445")
    private Integer columnId;
    @ApiModelProperty(value = "用作缓存key，一定不能传值", required = false, example = "78445")
    private String cacheKey = "userGoodsListColumn";
}
