package com.bat.thirdparty.erp.service;

import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.erp.service.executor.ErrorCode;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyGoodsServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.goods.GoodsItemErpListRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.goods.GoodsItemPriceErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemBoxRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemPriceRpcDTO;
import com.bat.thirdparty.erp.service.executor.GoodsExe;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/17 10:53
 */
@DubboService
public class ThirdPartyGoodsServiceErpRpcImpl implements ThirdPartyGoodsServiceErpRpc {

    @Resource
    GoodsExe qryExe;

    /**
     * 获取erp货品信息列表
     *
     * @param qry
     * @return
     */
    @Override
    public Response<List<GoodsItemErpRpcDTO>> listGoodsItemErp(GoodsItemErpListRpcQry qry) {
        try {
            List<GoodsItemErpRpcDTO> goodsItemErpRpcDTOList = qryExe.listGoodsItemErp(qry);
            return Response.of(goodsItemErpRpcDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_ERP_GOODS_ITEM_ERROR, MessageUtils.get(ErrorCode.B_ERP_GOODS_ITEM_ERROR));
        }
    }

    @Override
    public Response<List<GoodsItemPriceRpcDTO>> listGoodsItemPriceErp(GoodsItemPriceErpRpcQry qry) {
        try {
            List<GoodsItemPriceRpcDTO> rpcDTOS = qryExe.listGoodsItemPriceErp(qry);
            return Response.of(rpcDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_ERP_GOODS_PRICE_ERROR, MessageUtils.get(ErrorCode.B_ERP_GOODS_PRICE_ERROR));
        }
    }

    @Override
    public Response<List<GoodsItemErpRpcDTO>> listGoodsItemErp(List<Integer> itemErpIds) {
        try {
            List<GoodsItemErpRpcDTO> itemErpRpcDTOS = qryExe.listGoodsItemErp(itemErpIds);
            return Response.of(itemErpRpcDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_ERP_GOODS_ITEM_ERROR, MessageUtils.get(ErrorCode.B_ERP_GOODS_ITEM_ERROR));
        }
    }

    @Override
    public Response<List<GoodsItemBoxRpcDTO>> listGoodsItemBoxErp(List<Integer> itemErpIds) {
        try {
            List<GoodsItemBoxRpcDTO> goodsItemBoxRpcDTOS = qryExe.listGoodsItemBoxErp(itemErpIds);
            return Response.of(goodsItemBoxRpcDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_ERP_GOODS_ITEM_BOX_ERROR, MessageUtils.get(ErrorCode.B_ERP_GOODS_ITEM_BOX_ERROR));
        }
    }
}
