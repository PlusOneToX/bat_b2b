package com.bat.flexible.api.picture.dto.diy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PictureModelMaterialDiyCmd extends PictureModelMaterialDiyQry{



    @ApiModelProperty(value = "生产图")
    @NotNull(message = "P_PICTURE_GENERATE_IMAGE_NULL")
    private String generateImage;

    @ApiModelProperty(value = "预览图")
    @NotBlank(message = "P_PICTURE_PREVIEW_IMAGE_NULL")
    private String previewImage;
}
