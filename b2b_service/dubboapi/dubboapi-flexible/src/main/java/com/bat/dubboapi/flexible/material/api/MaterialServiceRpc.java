package com.bat.dubboapi.flexible.material.api;

import com.bat.dubboapi.flexible.material.dto.MaterialDTORpcQry;
import com.bat.dubboapi.flexible.common.Response;

import java.util.List;

public interface MaterialServiceRpc {

    /**
     * 根据材质id查询
     * @param materialId
     * @return
     */
    Response getByMaterialId(Integer materialId);

    /**
     * 根据货品id列表查询材质列表
     * @param itemIdList
     * @return
     */
    Response<List<MaterialDTORpcQry>> listByItemIdList(List<Integer> itemIdList);

    /**
     *
     * @return
     */
    Response<List<MaterialDTORpcQry>> listAllGroupByItemId();

    /**
     *
     * @return
     */
    Response<List<MaterialDTORpcQry>> listAll();

    /**
     * 下架材质（货品删除或者下架需要下架）
     * @param itemIdList
     * @return
     */
    Response soldOutByItem(List<Integer> itemIdList);
}
