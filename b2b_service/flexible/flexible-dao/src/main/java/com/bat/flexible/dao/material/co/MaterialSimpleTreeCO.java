package com.bat.flexible.dao.material.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class MaterialSimpleTreeCO  {

    /**
     * 材质id
     */
    @ApiModelProperty(value = "材质id")
    private Integer materialId;


    @ApiModelProperty(value = "材质父id")
    private Integer parentId;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "材质名称")
    private String name;

    @ApiModelProperty(value = "材质英文")
    private String englishName;

    @ApiModelProperty(value = "材质编码")
    private String materialNo;


    @ApiModelProperty(value = "是否为最终材质 1、是 0、否")
    private Integer atLastTrademark;

    /**
     * 这个参数拿来查树的、不能拿上面的atLastTrademark来做参数
     */
    private Integer atLastTrademarkQuery;

    @ApiModelProperty(value = "子级列表")
    private List<MaterialSimpleTreeCO> childrenList;
}
