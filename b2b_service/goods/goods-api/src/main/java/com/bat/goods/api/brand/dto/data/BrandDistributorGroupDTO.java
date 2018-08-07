package com.bat.goods.api.brand.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/5/19 17:30
 */
@Data
@ApiModel(description = "品牌可视范围分销商分组")
public class BrandDistributorGroupDTO {
    @ApiModelProperty(value = "分销商分组id", example = "123456")
    private Integer distributorGroupId;
    @ApiModelProperty(value = "分销商分组名称", example = "分销商分组名称")
    private String name;
    @ApiModelProperty(value = "ERP分销商分组编号", example = "ERP分销商分组编号")
    private String erpGroupNo;
}
