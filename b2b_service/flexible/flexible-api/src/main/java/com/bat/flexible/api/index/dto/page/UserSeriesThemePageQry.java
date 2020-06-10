package com.bat.flexible.api.index.dto.page;


import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserSeriesThemePageQry extends BasePageParamQry {

    @ApiModelProperty(value = "系列id")
    @NotNull(message = "INDEX_BANNER_SERIES_ID_NULL")
    private Integer seriesId;


}
