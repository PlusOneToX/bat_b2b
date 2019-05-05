package com.bat.promotion.service.common.rpc;

import java.util.List;

import javax.annotation.Resource;

import com.bat.dubboapi.promotion.dto.CouponCustomerRpcQry;
import com.bat.dubboapi.promotion.dto.data.CouponCustomerRpcDTO;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.promotion.api.PromotionServiceCustomerRpc;
import com.bat.dubboapi.promotion.common.Response;
import com.bat.dubboapi.promotion.dto.GoodsItemPromotionPriceRpcQry;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/26 13:31
 */
@DubboService
public class PromotionCustomerServiceRpcImpl implements PromotionServiceCustomerRpc {

    @Resource
    private PromotionCustomerRpcQryExe rpcQryExe;

    @Override
    public Response<List<GoodsItemPromotionPriceRpcDTO>> goodsItemPromotionPrice(GoodsItemPromotionPriceRpcQry qry) {
        return rpcQryExe.goodsItemPromotionPrice(qry);
    }

    @Override
    public Response<List<CouponCustomerRpcDTO>> listCouponCustomerByCustomerId(Integer customerId) {
        return rpcQryExe.listCouponCustomerByCustomerId(customerId);
    }

    @Override
    public Response<List<CouponCustomerRpcDTO>> listCouponCustomer(CouponCustomerRpcQry rpcQry) {
        return rpcQryExe.listCouponCustomer(rpcQry);
    }
}
