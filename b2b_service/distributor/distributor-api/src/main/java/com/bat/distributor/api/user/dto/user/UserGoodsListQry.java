package com.bat.distributor.api.user.dto.user;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/27 19:49
 */
@Data
@ApiModel(value = "UserGoodsListQry", description = "分销商商品列表查询")
public class UserGoodsListQry extends UserId {
    @ApiModelProperty(value = "商品名称", required = false, example = "bat")
    private String content;
    @ApiModelProperty(value = "品牌ids", required = false)
    private List<Integer> brandIds;
    @ApiModelProperty(value = "品类ids", required = false)
    private List<Integer> categoryIds;
    @NotNull(message = "P_DISTRIBUTOR_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;
    @NotNull(message = "P_DISTRIBUTOR_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
