package com.bat.distributor.api.distributor.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "多级分销关联关系信息")
public class DistributorTreePathDTO {

    private Integer id;
    @ApiModelProperty(value = "父分销商id", required = true, example = "13654")
    private Integer distributorAncestorId;
    @ApiModelProperty(value = "子分销商id", required = true, example = "1665432")
    private Integer distributorDescendantId;
    @ApiModelProperty(value = "归属分销级数", required = true, example = "2")
    private Integer treeNode;
}
