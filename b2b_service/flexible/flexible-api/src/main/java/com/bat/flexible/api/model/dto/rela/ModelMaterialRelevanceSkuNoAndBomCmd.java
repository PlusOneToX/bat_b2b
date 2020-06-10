package com.bat.flexible.api.model.dto.rela;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ModelMaterialRelevanceSkuNoAndBomCmd {

    @ApiModelProperty(value = "型号材质关联关系主键id")
    @NotNull(message = "FLEXIBLE_ID_NULL")
    private Integer id;


    @ApiModelProperty(value = "第三方SKU编码（工厂）")
    @NotBlank(message = "M_THIRD_SKU_NULL")
    private String thirdSku;


    @ApiModelProperty(value = "BOM编码")
    @NotBlank(message = "M_BOM_CODE_NULL")
    private String bomCode;

}
