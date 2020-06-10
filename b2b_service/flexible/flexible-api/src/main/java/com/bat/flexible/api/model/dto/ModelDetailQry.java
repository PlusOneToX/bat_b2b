package com.bat.flexible.api.model.dto;

import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.dao.model.co.ModelMaterialRelevanceCO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
@ApiModel(value = "ModelCmd",description = "型号新增修改传参")
public class ModelDetailQry {

    @ApiModelProperty(value = "型号id",notes = "编辑时必传")
    private Integer id;

    @ApiModelProperty(value = "型号名称")
    private String name;

    @ApiModelProperty(value = "型号英文名称")
    private String englishName;

    @ApiModelProperty(value = "型号类别id")
    private Integer categoryId;

    @ApiModelProperty(value = "型号类别名称")
    private String categoryName;

    @ApiModelProperty(value = "型号父id",notes = "一级填0")
    private Integer parentId;

    @ApiModelProperty(value = "型号图片")
    private String image;


    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "是否最终型号",notes = "1、启用 0、禁用")
    private Short atLastTrademark;

    @ApiModelProperty(value = "入网型号")
    private String networkModel;

    @ApiModelProperty(value = "型号编码",notes = "若是最终型号、该项必填")
    private String modelNo;

    @ApiModelProperty(value = "型号描述")
    private String depict;

    @NotEmpty(message = "M_MODEL_MATERIAL_RELA_NULL")
    private List<ModelMaterialRelevanceCO> materialRelevanceDTOList;


    @ApiModelProperty(value = "分销商不可用列表")
    private List<DistributorSimpleRelaQry> distributorExcludeList;

}
