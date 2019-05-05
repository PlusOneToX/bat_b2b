package com.bat.dubboapi.goods.classify.api;

import com.bat.dubboapi.goods.classify.dto.data.ClassifyRpcDTO;
import com.bat.dubboapi.goods.common.Response;

import java.util.List;

/**
 * 沙漠
 */
public interface GoodsClassifyServiceRpc {

    /**
     * 根据分类ids查询分类
     *
     * @return
     */
    Response<List<ClassifyRpcDTO>> listByIds(List<Integer> ids);


}
