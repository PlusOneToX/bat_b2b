package com.bat.dubboapi.order.order.api;

import java.util.Date;
import java.util.List;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.dto.*;
import com.bat.dubboapi.order.order.dto.data.OrderDTO;
import com.bat.dubboapi.order.order.dto.data.OrderGoodsThirdCodeRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderTradeRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderVoucherErpDTO;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderQueCmd;
import com.bat.dubboapi.order.order.dto.factory.maike.MaikeOrderAddCmd;

public interface OrderServiceRpc {
    /**
     * 根据订单ids获取订单调起支付相关信息
     *
     * @param qry
     * @return
     */
    Response<OrderTradeRpcDTO> orderTrade(OrderTradeRpcQry qry);

    /**
     * 第三方接口服务同步分销订单
     *
     * @return
     */
    Response<List<OrderDTO>> createDistributorOrder(OrderInfoCmd cmd);

    /**
     * erp订单变更
     *
     * @return
     */
    Response orderChangeByErp(ErpOrderChangeCmd cmd);

    /**
     * 获取即将要核销的第三方兑换订单（已签收但未核销）
     *
     * @return
     */
    Response<List<OrderGoodsThirdCodeRpcDTO>> thirdWillWriteOffOrder();

    /**
     * 根据订单id查询同步工厂参数
     *
     * @param orderId
     * @return
     */
    Response<MaikeOrderAddCmd> assemblyParamToFactory(Integer orderId);

    /**
     * 根据订单id查询同步工厂参数(通用)
     *
     * @param orderId
     * @return
     */
    Response<FactoryOrderAddCmd> assemblyParamToFactoryV2(Integer orderId);

    /**
     * 对第三方兑换订单进行核销
     *
     * @param cmd
     * @return
     */
    Response writeOffThirdCodeOrders(ThirdCodeOrderCmd cmd);

    /**
     * 第三方系统取消订单
     *
     * @param cmd
     * @return
     */
    Response orderCancelByThirdparty(OrderCancelCmd cmd);

    /**
     * 定时任务取消订单
     * 
     * @return
     */
    Response orderCancelByXXLJob();

    /**
     * erp系统取消订单
     *
     * @param cmd
     * @return
     */
    Response orderCheckByErp(OrderCheckCmd cmd);

    /**
     * 订单定制信息明细标签存放路径更新
     *
     * @param cmds
     * @return
     */
    Response orderGoodsDiyLabel(List<OrderGoodsDiyLabelCmd> cmds);

    /**
     * 根据订单ids获取同步订单收款单相关信息
     *
     * @param orderIds
     * @return
     */
    Response<List<OrderVoucherErpDTO>> orderVoucherErp(List<Integer> orderIds);

    /**
     * 订单付款状态更新
     *
     * @param cmd
     * @return
     */
    Response orderPayStatus(OrderPayStatusCmd cmd);

    /**
     * 获取未同步erp订单id列表
     *
     * @return
     */
    Response<List<Integer>> getNotErpOrderIds(Date startTime);

    /**
     * 根据订单Id查询订单工厂信息
     *
     * @return
     */
    Response<FactoryOrderQueCmd> orderFactoryInformationByOrderId(Integer orderId);

    /**
     * 根据工厂编号查询订单工厂信息
     * @param factoryCode
     * @return
     */
    Response<List<FactoryOrderQueCmd>> orderFactoryInformationByFactoryCode(String factoryCode,Date startTime);

    /**
     * <h2>修改购物车商品的状态</h2>
     */
    void updateShoppingCartItemStatus(Integer goodsId, Integer cartItemStatus);

}
