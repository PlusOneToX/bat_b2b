package com.bat.thirdparty.erp.controller;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.log.annotation.SysLog;
import com.bat.thirdparty.log.constants.CommonLogTypeConstantDTO;
import com.bat.thirdparty.log.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bat.dubboapi.flexible.model.dto.ErpGoodsCustomInfoListCmd;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderRequest;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderStatusRequest;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.dubboapi.warehouse.stock.dto.ErpStockChangeCmd;
import com.bat.thirdparty.erp.api.ErpServiceI;
import com.bat.thirdparty.erp.api.request.AccountBalanceChangeRequest;
import com.bat.thirdparty.erp.api.request.ErpOrderChangeRequest;
import com.bat.thirdparty.erp.api.request.ErpOrderCheckRequest;
import com.bat.thirdparty.erp.api.request.ErpVoucherDetailsRequest;
import com.bat.thirdparty.erp.api.response.ErpResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "ERP集成接口")
@RequestMapping(value = "/thirdparty/v1/web/erp")
public class ErpController {

    private static final Logger logger = LoggerFactory.getLogger(ErpController.class);

    @Autowired
    private ErpServiceI service;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Erp, value = CommonLogTypeConstantDTO.ErpOrderChange)
    @ApiOperation(value = "ERP订单变更")
    @PostMapping(value = "/order/change")
    public ErpResponse orderChange(@RequestBody ErpOrderChangeRequest request) {
        try {
            service.orderChange(request);
            return ErpResponse.buildSuccess();
        } catch (ThirdPartyException e) {
            return ErpResponse.buildFailure(e.getMsg());
        }
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Erp, value = CommonLogTypeConstantDTO.ErpOrderCheck)
    @ApiOperation(value = "ERP订单状态变更(审核)")
    @PostMapping(value = "/order/check")
    public ErpResponse orderCheck(@RequestBody ErpOrderCheckRequest request) {
        try {
            service.orderCheckByErp(request);
            return ErpResponse.buildSuccess();
        } catch (ThirdPartyException e) {
            return ErpResponse.buildFailure(e.getMsg());
        }
    }
    /**
     * @api {POST} /erp/u/order/check 销售订单审核
     * @apiName erp_u_order_check_post
     * @apiGroup erp_order
     *
     * @apiParam {String} orderNo erp订单编号
     * @apiParam {Number = 1 未确认(反审核), 2 确认(审核通过), 5 取消(作废,关闭)} orderStatus 订单确认
     * @apiVersion 1.0.0
     * @apiUse headerAndAuthorization
     *
     * @apiSuccessExample {json} 结果描述: { "code": 0, //int 成功为0,为其他查看errorcode组 "msg": "成功", //string 成功为"",失败则为错误描述 }
     */
    /*@RequestMapping(value = "/erp/u/order/check", method = RequestMethod.POST, consumes = BaseController.Consumes, produces = BaseController.Produces)
    @ResponseBody
    public ResponseBaseBean orderCheck(@RequestBody ErpOrderCheckRequest request) throws Exception {
        return service.orderCheck(request);
    }*/

    /**
     * @api {POST} /erp/u/deliverOrder 销售出库
     * @apiName erp_u_deliverOrder_post
     * @apiGroup erp_order
     *
     * @apiParam {String} deliverOrderNo 出库单号
     * @apiParam {String} expressNo 快递单号
     * @apiParam {Number} expressType 快递类型
     * @apiParam {Number} DocumentStatus 单据状态 1.创建，2.已审核，3.取消，4.删除 5.提交
     * @apiParam {DeliverOrderDetails[]} deliverOrderDetails erp出库明细
     *
     * @apiParam (DeliverOrderDetails) {String} orderNo erp订单编号
     * @apiParam (DeliverOrderDetails) {String} itemNo 物料编号
     * @apiParam (DeliverOrderDetails) {String} no Erp仓库id
     * @apiParam (DeliverOrderDetails) {number} num 数量
     * @apiVersion 1.0.0
     * @apiUse headerAndAuthorization
     *
     * @apiSuccessExample {json} 结果描述: { "code": 0, //int 成功为0,为其他查看errorcode组 "msg": "成功", //string 成功为"",失败则为错误描述
     *                    "deliverDetails": [ { "orderNo": "2314", "deliverId": "1000001" } ] }
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Erp,
        value = CommonLogTypeConstantDTO.ErpCreateDeliverOrder)
    @PostMapping(value = "/order/deliverOrder")
    @ApiOperation(value = "销售出库,仅同步销售出库单、不解锁库存（重构后）")
    public com.bat.dubboapi.order.common.ResponseBaseBean
        deliverOrder(@RequestBody ErpDeliverOrderRequest request) {
        return service.deliverOrder(request);
    }

    /**
     * @api {POST} /erp/u/vouchers erp收款单
     * @apiName erp_u_vouchers_post
     * @apiGroup erp_order
     *
     * @apiParam {VoucherDetails[]} voucherDetails 收款明细
     *
     * @apiParam (VoucherDetails) {String} orderNo erp的销售单据编号
     * @apiParam (VoucherDetails) {String} voucherNo erp的收款编号
     * @apiParam (VoucherDetails) {number} amount 收款金额
     * @apiParam (VoucherDetails) {number} voucherTime 收款时间戳，毫秒
     * @apiVersion 1.0.0
     * @apiUse headerAndAuthorization
     *
     * @apiSuccessExample {json} 结果描述: { "code": 0, //int 成功为0,为其他查看errorcode组 "msg": "成功", //string 成功为"",失败则为错误描述
     *                    "voucherDetails": [ { "voucherNo": "2314", "voucherId": "1000001" } ] }
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Erp, value = CommonLogTypeConstantDTO.ErpCreateVouchers)
    @PostMapping(value = "/vouchers")
    @ResponseBody
    public ResponseBaseBean createVouchers(@RequestBody ErpVoucherDetailsRequest request) {
        return service.createVouchers(request);
    }

    /**
     * @api {POST} /erp/u/warehouse/stock/change 库存变更接口
     * @apiName erp_u_warehouse_stock_change_post
     * @apiGroup erp_order
     *
     * @apiParam {String} billNo 单据编号
     * @apiParam {String} stockBillType 单据类型
     * @apiParam {changeType} stockBillType 库存变化类型 1.增加，2.减少
     * @apiParam {StockBillDetail[]} stockBillDetails 单据明细
     *
     * @apiParam (StockBillDetail) {String} itemNo 物料编码
     * @apiParam (StockBillDetail) {String} warehouseNo 仓库
     * @apiParam (StockBillDetail) {number} num 数量
     * @apiVersion 1.0.0
     * @apiUse headerAndAuthorization
     *
     * @apiUse successResponse
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Erp,
        value = CommonLogTypeConstantDTO.ErpWarehouseStockChange)
    @PostMapping(value = "/warehouse/stock/change")
    @ApiOperation(value = "库存变更接口（订单解锁和非B2B订单货品同步库存）")
    public ResponseBaseBean warehouseStockChange(@RequestBody ErpStockChangeCmd erpStockChangeCmd) {
        return service.warehouseStockChange(erpStockChangeCmd);
    }

    /**
     * @api {POST} /erp/u/account/balance/change 余额变更接口
     * @apiName erp_u_account_balance_change_post
     * @apiGroup erp_order
     *
     * @apiParam {int} erp_distributor_id erp分销商id
     * @apiParam {changeType} changeType 余额变化类型 1.增加，2.减少
     * @apiParam {number} amount 变化金额
     * @apiVersion 1.0.0
     * @apiUse headerAndAuthorization
     *
     * @apiUse successResponse
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Erp,
        value = CommonLogTypeConstantDTO.ErpAccountBalanceChange)
    @PostMapping(value = "/account/balance/change")
    @ResponseBody
    public ResponseBaseBean accountBalanceChange(@RequestBody AccountBalanceChangeRequest request) {
        return service.accountBalanceChange(request);
    }

    /**
     * @api {POST} /erp/u/deliverOrder/status 销售出库单状态变更接口
     * @apiName erp_u_deliverOrder_status_post
     * @apiGroup erp_order
     *
     * @apiParam {String} deliverOrderNo 出库单号
     * @apiParam {Number} status 单据状态 1.创建，2.已审核，3.取消，4.删除 ，5.提交
     *
     * @apiVersion 1.0.0
     * @apiUse headerAndAuthorization
     * @apiUse successResponse
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Erp,
        value = CommonLogTypeConstantDTO.ErpChangeDeliverOrder)
    @PostMapping(value = "/order/deliverOrder/status")
    @ApiOperation(value = "销售出库单状态变更")
    public ResponseBaseBean changeDeliverOrderStatus(@RequestBody ErpDeliverOrderStatusRequest request) {
        return service.changeDeliverOrderStatus(request);
    }

    /**
     * ERP 设置型号和材质的组合关系是否缺货
     * 
     * @param erpGoodsCustomInfoListCmd
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Erp,
        value = CommonLogTypeConstantDTO.ErpGoodsCustomInfoIsStockOut)
    @PostMapping(value = "/sync/goodsCustomInfoIsStockOut")
    @ApiOperation(value = "ERP设置型号和材质关系是否缺货")
    public ResponseBaseBean
        syncMaterialAndModelRelaStockOutStatus(@RequestBody ErpGoodsCustomInfoListCmd erpGoodsCustomInfoListCmd) {
        return service.syncMaterialAndModelRelaStockOutStatus(erpGoodsCustomInfoListCmd);
    }
}
