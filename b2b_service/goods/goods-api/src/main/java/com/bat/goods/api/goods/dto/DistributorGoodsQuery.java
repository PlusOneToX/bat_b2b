package com.bat.goods.api.goods.dto;

import com.bat.goods.api.base.PageQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DistributorGoodsQuery extends PageQry {

    @ApiModelProperty(value = "分销商id")
    @NotNull(message = "D_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员 ")
    private Short distributorScope;

    @ApiModelProperty(value = "搜索关键词、商品名称、货品名称、80码")
    private String content;
}
