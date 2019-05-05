package com.bat.thirdparty.erp.api;

import com.bat.thirdparty.common.base.BaseId;
import com.bat.thirdparty.erp.api.request.AccountBalanceChangeRequest;
import com.bat.thirdparty.erp.api.request.ErpOrderChangeRequest;
import com.bat.thirdparty.erp.api.request.ErpOrderCheckRequest;
import com.bat.thirdparty.erp.api.request.ErpVoucherDetailsRequest;
import com.bat.dubboapi.flexible.model.dto.ErpGoodsCustomInfoListCmd;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderRequest;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderStatusRequest;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.dubboapi.warehouse.stock.dto.ErpStockChangeCmd;

public interface ErpServiceI {

    /**
     * ERP库存变更接口 （B2B订单解锁、非B2B订单物料同步仓库库存）
     * 
     * @param erpStockChangeCmd
     * @return
     */
    ResponseBaseBean warehouseStockChange(ErpStockChangeCmd erpStockChangeCmd);

    /**
     * ERP订单变更
     * 
     * @param request
     */
    void orderChange(ErpOrderChangeRequest request);

    /**
     * ERP订单状态变更(包含1 未确认(反审核), 2 确认(审核通过), 5 取消(作废,关闭))
     * 
     * @param request
     */
    void orderCheckByErp(ErpOrderCheckRequest request);

    /**
     * 余额变更接口
     *
     * @param request
     * @return
     */
    ResponseBaseBean accountBalanceChange(AccountBalanceChangeRequest request);

    /**
     * ERP创建收款单
     *
     * @param request
     * @return
     */
    ResponseBaseBean createVouchers(ErpVoucherDetailsRequest request);

    /**
     * ERP分销商同步
     *
     * @param cmd
     */
    void distributorSync(BaseId cmd) throws Exception;

    /**
     * ERP同步出库单到B2B
     * 
     * @param erpDeliverOrderRequest
     * @return
     */
    com.bat.dubboapi.order.common.ResponseBaseBean deliverOrder(ErpDeliverOrderRequest erpDeliverOrderRequest);

    /**
     * ERP销售单出库单状态变更
     * 
     * @param request
     * @return
     */
    ResponseBaseBean changeDeliverOrderStatus(ErpDeliverOrderStatusRequest request);

    /**
     * ERP设置材质和型号关系是否缺货
     * 
     * @param erpGoodsCustomInfoListCmd
     * @return
     */
    ResponseBaseBean syncMaterialAndModelRelaStockOutStatus(ErpGoodsCustomInfoListCmd erpGoodsCustomInfoListCmd);

}
