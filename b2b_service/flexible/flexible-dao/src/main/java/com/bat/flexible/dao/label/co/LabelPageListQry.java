package com.bat.flexible.dao.label.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "LabelPageListQry",description = "标签分页")
public class LabelPageListQry {

    @ApiModelProperty(value = "标签id")
    private Integer id;

    @ApiModelProperty(value = "标签名称")
    private String name;

    @ApiModelProperty(value = "类型",notes = "1、普通标签，2、定制标签")
    private Short type;

    @ApiModelProperty(value = "产品名称位置")
    private String productNamePosition;

    @ApiModelProperty(value = "英文名称位置")
    private String enNamePosition;

    @ApiModelProperty(value = "产品型号位置")
    private String modelPosition;

    @ApiModelProperty(value="条形码位置")
    private String barCodePosition;

    @ApiModelProperty(value = "是否启用",notes = "1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "适用分销商范围",notes = "1为全部，2 为国内， 3为国外，4 为指定")
    private Short scope;

    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "最后修改人")
    private String updateUserName;


}
