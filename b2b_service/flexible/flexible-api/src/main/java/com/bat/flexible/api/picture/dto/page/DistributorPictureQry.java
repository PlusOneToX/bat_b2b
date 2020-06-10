package com.bat.flexible.api.picture.dto.page;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DistributorPictureQry extends BasePageParamQry {


    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    /**
     * 1、普通素材 2、IP素材
     */
    @ApiModelProperty(value = "图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图 ")
    private Short type;

    @ApiModelProperty(value = "状态1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "图片分类id")
    private Integer categoryId;

    private List<Integer> categoryIdList;


}
