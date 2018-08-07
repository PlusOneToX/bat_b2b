package com.bat.goods.api.goods.dto;

import java.io.Serializable;

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
@ApiModel(value = "UserGoodsListColumnQry", description = "栏目列表查询")
public class GoodsListColumnQry extends PageQry implements Serializable {
    @NotNull(message = "P_GOODS_USER_COLUMN_ID_NULL")
    @ApiModelProperty(value = "栏目id", example = "78445")
    private Integer columnId;
    @ApiModelProperty(value = "用作缓存key，一定不能传值", required = false, example = "78445")
    private String cacheKey = "userGoodsListColumn";
}
