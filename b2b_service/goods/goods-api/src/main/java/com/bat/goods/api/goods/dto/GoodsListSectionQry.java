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
@ApiModel(value = "UserGoodsListSectionQry", description = "板块列表查询")
public class GoodsListSectionQry extends PageQry implements Serializable {
    @NotNull(message = "P_GOODS_USER_SECTION_ID_NULL")
    @ApiModelProperty(value = "板块id", example = "78445")
    private Integer sectionId;
    @ApiModelProperty(value = "用作缓存key，一定不能传值", required = false, example = "78445")
    private String cacheKey = "userGoodsListSection";
}
