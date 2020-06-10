package com.bat.flexible.manager.material.validator;

import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.common.FlexibleDubboServiceErrorCode;
import com.bat.flexible.manager.error.material.MaterialErrorCode;
import com.bat.flexible.manager.material.executor.MaterialQryExe;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MaterialValidtor {

    @Autowired
    private MaterialQryExe materialQryExe;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private GoodsServiceRpc goodsServiceRpc;


    /**
     * 校验材质是否最终可用
     * @param materialId
     */
    public MaterialDO validMaterialIsLast(Integer materialId,MaterialDO materialDO){
        if(materialDO ==null){
            materialDO = materialQryExe.getById(materialId);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_NO.equals(materialDO.getAtLastTrademark())){
            throw new FlexibleCustomException(MaterialErrorCode.M_MATERIAL_NOT_LAST_TRADEMARK);
        }
        if(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(materialDO.getOpenFlag())){
            String msg = MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)+MessageUtils.get(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE);
            throw new FlexibleCustomException(msg);
        }
        return materialDO;
    }

    public void validItemShelves(MaterialDO materialDO){
        if(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(materialDO.getOpenFlag())){
            //下架操作不做判断
            return;
        }
        if(materialDO.getItemId() ==null){
            //非最终材质
            return;
        }
        Response<Short> itemResp = goodsServiceRpc.getSaleStatusByItemId(materialDO.getItemId());
        if(itemResp ==null || !itemResp.isSuccess()){
            throw new FlexibleCustomException(FlexibleDubboServiceErrorCode.DUBBO_GOODS_SERVICE_EXCEPTION);
        }
        //根据货品id查询货品上架状态 1、未上架 2、审判中 3、已上架 null表示已被删除
        Short saleStatus = itemResp.getData();
        if(saleStatus ==null){
            throw new FlexibleCustomException(MaterialErrorCode.G_ITEM_HAS_DELETE_ERROR);
        }
        if(saleStatus !=3){
            throw new FlexibleCustomException(MaterialErrorCode.G_ITEM_HAS_NOT_SHELVES_ERROR);
        }
    }

}
