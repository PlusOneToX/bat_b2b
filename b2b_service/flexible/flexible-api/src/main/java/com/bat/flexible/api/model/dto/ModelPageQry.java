package com.bat.flexible.api.model.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class ModelPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "产品类型id")
    private Integer categoryId=1;

    @ApiModelProperty(value = "型号名称、型号编码")
    private String content;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;
    
    @ApiModelProperty(value = "型号父id、0为顶级")
    private Integer parentId=0;

    @ApiModelProperty(value = "是否最终类型 1、是 0、否")
    private Short atLastTrademark;

    @ApiModelProperty(value = "材质id列表")
    private List<Integer> materialIdList;
}
