package com.bat.flexible.api.picture.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class PictureTreeQry {

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商id")
    private List<Integer> distributorIds;

    @ApiModelProperty(value = "材质编码")
    private String materialNo;

    @ApiModelProperty(value = "材质id")
    private Integer materialId;

    @ApiModelProperty(value = "型号id")
    private Integer modelId;

    @ApiModelProperty(value = "产品类型id")
    private Integer productCategoryId;

    @ApiModelProperty(value = "图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图")
    private Short type;

    @ApiModelProperty(value = "图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图")
    private List<Integer> typeList;
}
