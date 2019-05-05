package com.bat.dubboapi.flexible.model.api;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.model.dto.ModelDTORpcQry;

public interface ModelServiceRpc {

    /**
     * 根据型号id或者型号编码获取型号信息
     * @param modelId
     * @param modelNo
     * @return
     */
    Response<ModelDTORpcQry> getByModelIdOrModelNo(Integer modelId, String modelNo);
}
