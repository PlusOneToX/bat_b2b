package com.bat.dubboapi.promotion.api;

import com.bat.dubboapi.promotion.common.Response;
import com.bat.dubboapi.promotion.dto.CouponCustomerRpcQry;
import com.bat.dubboapi.promotion.dto.GoodsItemPromotionPriceRpcQry;
import com.bat.dubboapi.promotion.dto.data.CouponCustomerRpcDTO;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;

import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/25 19:59
 */
public interface PromotionServiceCustomerRpc {
    /**
     * C端客户计算商品活动价格
     *
     * @param qry
     * @return
     */
    Response<List<GoodsItemPromotionPriceRpcDTO>> goodsItemPromotionPrice(GoodsItemPromotionPriceRpcQry qry);

    /**
     * 根据客户id获取C端客户已领取优惠券
     *
     * @param customerId
     * @return
     */
    Response<List<CouponCustomerRpcDTO>> listCouponCustomerByCustomerId(Integer customerId);

    /**
     *
     * @param rpcQry
     * @return
     */
    Response<List<CouponCustomerRpcDTO>> listCouponCustomer(CouponCustomerRpcQry rpcQry);
}
