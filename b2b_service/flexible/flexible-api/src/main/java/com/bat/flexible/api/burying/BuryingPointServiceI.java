package com.bat.flexible.api.burying;

import com.bat.flexible.api.burying.dto.BuryingPointCmd;
import com.bat.flexible.api.base.common.response.Response;

public interface BuryingPointServiceI {
    /**
     * 新增埋点
     * @param buryingPointCmd
     * @return
     */
    Response create(BuryingPointCmd buryingPointCmd);
}
