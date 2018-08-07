package com.bat.warehouse.api.warehouse.dto;

import com.bat.warehouse.api.base.dto.BasePageParamQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "WarehousePageQry",description = "仓库列表分页查询")
public class WarehousePageQry extends BasePageParamQry {

    @ApiModelProperty(value = "仓库名称/所属区域",required = false)
    private String content;

    @ApiModelProperty(value = "状态1、启用 0、禁用",notes = "1、删除 0、否",required = false)
    private Short openFlag;

}
