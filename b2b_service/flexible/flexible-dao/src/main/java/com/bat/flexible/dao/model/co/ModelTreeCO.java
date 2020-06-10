package com.bat.flexible.dao.model.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;
import java.util.List;

@Data
public class ModelTreeCO extends ModelMaterialRelevanceBaseCO implements Serializable {

    private static final long serialVersionUID = -8425219176733593495L;
    @ApiModelProperty(value = "型号id")
    private Integer modelId;

    @ApiModelProperty(value = "父型号id")
    private Integer parentId;

    @ApiModelProperty(value = "型号名称")
    private String name;

    @ApiModelProperty(value = "英文名称")
    private String englishName;

    @ApiModelProperty(value = "型号编码")
    private String modelNo;

    @ApiModelProperty(value = "产品类型id")
    private Integer categoryId;

    @ApiModelProperty(value = "是否最终型号")
    private Short atLastTrademark;

    @ApiModelProperty(value = "传参的状态 1、是 、0、否",notes = "不能拿表字段open_flag作为值、要不然传空过来、子节点会过滤掉父节点")
    private Short openFlagParam;

    @ApiModelProperty(value = "子型号列表")
    private List<ModelTreeCO> childrenList;

    @ApiModelProperty(value = "是否有效 0无效 1有效")
    private Short validFlag;

    @ApiModelProperty(value = "无效类型 1材质无效 2图片无效")
    private Short validType;

}
