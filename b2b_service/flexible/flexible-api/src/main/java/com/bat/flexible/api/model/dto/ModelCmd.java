package com.bat.flexible.api.model.dto;


import com.bat.flexible.api.model.dto.rela.ModelMaterialRelevanceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@ApiModel(value = "ModelCmd",description = "型号新增修改传参")
public class ModelCmd {

    @ApiModelProperty(value = "型号id",notes = "编辑时必传")
    private Integer id;

    @ApiModelProperty(value = "型号名称")
    @NotBlank(message = "M_MODEL_NAME_NULL")
    private String name;

    @ApiModelProperty(value = "型号英文名称")
    private String englishName;

    @ApiModelProperty(value = "产品类别id")
    @NotNull(message = "P_PRODUCT_CATEGORY_ID_NULL")
    private Integer categoryId;

    @ApiModelProperty(value = "型号父id",notes = "一级填0")
    @NotNull(message = "M_MODEL_PARENT_ID_NULL")
    private Integer parentId;

    @ApiModelProperty(value = "型号图片")
    private String image;


    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    @NotNull(message = "COMMON_OPEN_FLAG_NULL")
    private Short openFlag;

    @ApiModelProperty(value = "是否最终型号",notes = "1、启用 0、禁用")
    @NotNull(message = "M_MODEL_AT_LAST_NULL")
    private Short atLastTrademark;

    @ApiModelProperty(value = "入网型号")
    private String networkModel;

    @ApiModelProperty(value = "型号编码",notes = "若是最终型号、该项必填")
    private String modelNo;

    @ApiModelProperty(value = "型号描述")
    private String depict;

    @ApiModelProperty(value = "型号材质关联列表、若是最终型号、列表必填")
    private List<ModelMaterialRelevanceDTO> materialRelevanceDTOList;


    @ApiModelProperty(value = "分销商不可用id列表")
    private List<Integer> distributorIdList;

}
