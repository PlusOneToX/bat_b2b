package com.bat.dubboapi.flexible.model.api;

import java.util.List;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.model.dto.ErpGoodsCustomInfoListCmd;
import com.bat.dubboapi.flexible.model.dto.ModelMaterialRelevanceDTORpcQry;
import com.bat.dubboapi.flexible.model.dto.OrderGoodDiyQry;

public interface ModelMaterialRelevanceServiceRpc {

    /**
     * 根据型号id和材质id查询关联关系
     * 
     * @param modelId
     *            型号id
     * @param materialId
     *            材质id
     * @return
     */
    Response<ModelMaterialRelevanceDTORpcQry> getByModelIdAndMaterialId(Integer modelId, Integer materialId);

    /**
     * ERP同步型号和材质关联关系的是否缺货状态
     * 
     * @param erpGoodsCustomInfoListCmd
     * @return
     */
    Response syncMaterialAndModelRelaStockOutStatus(ErpGoodsCustomInfoListCmd erpGoodsCustomInfoListCmd);

    /**
     * 根据型号id和材质id查询关联关系 批量
     * 
     * @param orderGoodDiyQries
     * @return
     */
    Response<List<OrderGoodDiyQry>> listByModelIdAndMaterialId(List<OrderGoodDiyQry> orderGoodDiyQries);
}
