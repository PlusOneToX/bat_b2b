package com.bat.flexible.api.picture.dto.page;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PictureCategoryPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图")
    private Short type;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "是否最终分类 1、是 0、否")
    private Short atLastTrademark;

    @ApiModelProperty(value = "父节点、默认是0")
    private Integer parentId=0;

    @ApiModelProperty(value = "搜索")
    private String content;
}
