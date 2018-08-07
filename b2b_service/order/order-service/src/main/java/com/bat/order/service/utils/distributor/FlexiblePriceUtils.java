package com.bat.order.service.utils.distributor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bat.order.service.common.error.OrderInfoErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorCustomPriceServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorCustomerPriceDTO;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.order.api.common.exception.OrderException;

/**
 * 柔性价格工具类
 */
@Component
public class FlexiblePriceUtils {

    private static DistributorCustomPriceServiceRpc distributorCustomPriceServiceRpc;

    private static GoodsServiceRpc goodsServiceRpc;

    @DubboReference(retries = 0, timeout = 5000, check = false)
    public void setDistributorCustomPriceServiceRpc(DistributorCustomPriceServiceRpc distributorCustomPriceServiceRpc) {
        FlexiblePriceUtils.distributorCustomPriceServiceRpc = distributorCustomPriceServiceRpc;
    }

    @DubboReference(retries = 0, timeout = 5000, check = false)
    public void setGoodsServiceRpc(GoodsServiceRpc goodsServiceRpc) {
        FlexiblePriceUtils.goodsServiceRpc = goodsServiceRpc;
    }

    /**
     * 获取定制价格
     * 
     * @param distributorId
     *            分销商id
     * @param itemId
     *            货品id
     * @param itemCode
     *            货品编码 （用于查询外部接口）
     * @param customerId
     *            C端客户id（该值不为空表示查询C端价格）
     * @return
     */
    public static BigDecimal getPriceByCondition(Integer distributorId, Integer itemId, String itemCode,
        Integer customerId) {
        BigDecimal price = null;
        if (customerId != null) {
            Response<DistributorCustomerPriceDTO> response =
                distributorCustomPriceServiceRpc.getByDistributorIdAndItemId(distributorId, itemId);
            price = response.getData().getPrice();
            if (price == null) {
                // 远程获取用户C端价格、尚未完成
            }
        } else {
            // 分销商角色下单、获取建议零售价
            List<Integer> itemIdList = new ArrayList<>();
            itemIdList.add(itemId);
            com.bat.dubboapi.goods.common.Response<List<GoodsItemPriceRpcDTO>> listResponse =
                goodsServiceRpc.listGoodsItemRetailPrice(itemIdList);
            List<GoodsItemPriceRpcDTO> goodsItemPriceRpcDTOS = listResponse.getData();
            if (goodsItemPriceRpcDTOS == null || goodsItemPriceRpcDTOS.size() == 0) {
                // 尚未设置结算金额
                throw OrderException.buildException(OrderInfoErrorCode.ORDER_ITEM_RETAIL_PRICE_NULL);
            }
            return goodsItemPriceRpcDTOS.get(0).getRetailPrice();
        }

        return price;
    }

}
