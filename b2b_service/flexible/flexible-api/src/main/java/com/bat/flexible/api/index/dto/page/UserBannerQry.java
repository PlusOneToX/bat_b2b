package com.bat.flexible.api.index.dto.page;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserBannerQry extends BasePageParamQry {

    @NotNull(message = "bannerId不能为空")
    private Integer bannerId;

    @NotNull(message = "seriesId不能为空")
    private Integer seriesId;


}
