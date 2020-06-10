package com.bat.flexible.api.picture.dto.page;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class PicturePageQry extends BasePageParamQry {

    @ApiModelProperty(value = "图片分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图")
    private Short type;

    @ApiModelProperty(value = "图片编号/名称")
    private String content;

    @ApiModelProperty(value = "材质id列表")
    private List<Integer> materialIdList;

    @ApiModelProperty(value = "型号id列表")
    private List<Integer> modelIdList;
    
}
