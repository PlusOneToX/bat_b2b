package com.bat.flexible.dao.picture.dataobject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PictureModelMaterialDiyDO implements Serializable {
    private static final long serialVersionUID = 2087258410499959547L;
    private Integer id;

    @ApiModelProperty(value = "图片id")
    private Integer pictureId;

    @ApiModelProperty(value = "材质id")
    private Integer materialId;

    @ApiModelProperty(value = "型号id")
    private Integer modelId;

    @ApiModelProperty(value = "生产图")
    private String generateImage;

    @ApiModelProperty(value = "预览图")
    private String previewImage;

    private Date createTime;
}