package com.bat.order.service.utils.goods;

import java.util.ArrayList;
import java.util.List;

import com.bat.order.service.common.error.GoodsErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.order.api.common.exception.OrderException;

@Component
public class GoodsServiceRpcUtils {

    private static GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false, retries = 0, timeout = 5000)
    public void setGoodsServiceRpc(GoodsServiceRpc goodsServiceRpc) {
        GoodsServiceRpcUtils.goodsServiceRpc = goodsServiceRpc;
    }

    /**
     * 根据货品id或者货品编码查询商品详情
     * 
     * @param itemId
     *            货品id
     * @param itemCode
     *            物料编码
     * @return
     */
    public static GoodsItemRpcDTO getGoodsItemRpcByItemIdOrItemCode(Integer itemId, String itemCode) {
        if (itemId == null && StringUtils.isBlank(itemCode)) {
            throw OrderException.buildException(GoodsErrorCode.GOODS_ITEM_ID_AND_ITEM_CODE_ALL_NULL);
        }
        Response<List<GoodsItemRpcDTO>> response = null;
        if (itemId != null) {
            List<Integer> itemIdList = new ArrayList<>();
            itemIdList.add(itemId);
            response = goodsServiceRpc.listGoodsItemByIds(itemIdList);

        } else {
            List<String> itemCodeList = new ArrayList<>();
            itemCodeList.add(itemCode);
            response = goodsServiceRpc.listGoodsItemByCodes(itemCodeList);
        }
        List<GoodsItemRpcDTO> list = response.getData();
        if (list == null || list.size() == 0) {
            if (itemId != null) {
                throw OrderException.buildException(GoodsErrorCode.GOODS_ITEM_ID_ERROR);
            } else {
                throw OrderException.buildException(GoodsErrorCode.GOODS_ITEM_CODE_ERROR);
            }
        }
        return list.get(0);
    }

}
