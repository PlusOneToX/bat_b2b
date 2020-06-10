package com.bat.flexible.api.picture.dto.diy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PictureModelMaterialDiyQry {

    @ApiModelProperty(value = "型号id")
    @NotNull(message = "COMMON_MODEL_ID_NULL")
    private Integer modelId;

    @ApiModelProperty(value = "材质id")
    @NotNull(message = "COMMON_MATERIAL_ID_NULL")
    private Integer materialId;

    @ApiModelProperty(value = "图片id")
    @NotNull(message = "COMMON_PICTURE_ID_NULL")
    private Integer pictureId;
}
