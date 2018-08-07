package com.bat.order.web.order;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bat.order.web.annotation.SysLog;
import com.bat.order.web.base.BaseController;
import com.bat.order.web.constants.CommonLogTypeConstantDTO;
import com.bat.order.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.deliver.dto.data.OrderDeliverBillDetailDTO;
import com.bat.order.api.order.UserDistributorOrderServiceI;
import com.bat.order.api.order.constants.SearchType;
import com.bat.order.api.order.dto.common.OrderCancelCmd;
import com.bat.order.api.order.dto.common.OrderOneMoreDTO;
import com.bat.order.api.order.dto.distributor.OrderCheckCmd;
import com.bat.order.api.order.dto.distributor.OrderInfoCmd;
import com.bat.order.api.order.dto.orderquery.common.OrderDeliverDetailDTO;
import com.bat.order.api.order.dto.orderquery.user.*;
import com.bat.order.api.utils.excel.ExcelUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "分销客户订单前台接口")
@RequestMapping(value = "/order/v1/web/user/distributor/order")
public class UserDistributorOrderController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDistributorOrderController.class);

    @Autowired
    private UserDistributorOrderServiceI service;

    @Resource
    private ExcelUtils excelUtils;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserDistributor,
        value = CommonLogTypeConstantDTO.UserDistributorCreate)
    @ApiOperation(value = "分销下单接口")
    @PostMapping()
    public Response<BaseIds> createOrder(@Valid @RequestBody OrderInfoCmd cmd) {
        String userId = getUserId();
        if (cmd.getDistributorId() != null) {
            userId = cmd.getDistributorId() + "";
        }
        BaseIds ids = service.createOrder(cmd, userId, getUserName(), getContactId(), getContactName(), getPlatform(),
            getLanguage());
        return Response.of(ids);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserDistributor,
        value = CommonLogTypeConstantDTO.UserDistributorFindCheck)
    @ApiOperation(value = "分销订单审核接口(支持批量审核)")
    @PutMapping("/check/order")
    public Response checkOrder(@Valid @RequestBody OrderCheckCmd cmd) {
        service.checkOrder(cmd, getUserId());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "分销获取再来一单数据")
    @GetMapping("/one/more")
    public Response<List<OrderOneMoreDTO>> oneMoreOrder(@Valid BaseId qry) {
        List<OrderOneMoreDTO> dtos = service.oneMoreOrder(qry, getUserId());
        return Response.of(dtos);
    }

    @ApiOperation(value = "获取订单下单结果(单个)")
    @GetMapping()
    public Response<UserPCDistributorOrderResultDTO.UserDistributorOrderItemResultDTO>
        getOrderInfoById(@Valid BaseId id) {
        return Response.of(service.getOrderInfoById(id.getId()));
    }

    @ApiOperation(value = "获取订单下单结果（多个）")
    @GetMapping("/ids")
    public Response<UserPCDistributorOrderResultDTO> getOrderInfoByIds(@Valid BaseIds ids) {
        return Response.of(service.getOrderInfoByIds(ids.getIds()));
    }

    /**
     * @param qry
     * @return
     */
    @ApiOperation(value = "获取当前分销订单列表")
    @GetMapping("/pc/list")
    public Response<PageInfo<UserPCDistributorOrderInfoListDTO>>
        listPCOrderInfoByDistributorId(@Valid UserPCDistributorOrderInfoListQry qry) {
        qry.setSearchType(SearchType.USER_PC_ORDER_LIST);
        return Response.of(service.listPCOrderInfoByDistributorId(qry));
    }

    /**
     * 
     * @param qry
     * @return
     */
    @ApiOperation(value = "获取柔性店铺订单列表")
    @GetMapping("/pc/shop/list")
    public Response<PageInfo<UserPCDistributorOrderInfoListDTO>>
        listPCShopOrderInfoByDistributorId(@Valid UserPCDistributorOrderInfoListQry qry) {
        qry.setSearchType(SearchType.USER_PC_SHOP_ORDER_LIST);
        return Response.of(service.listPCShopOrderInfoByDistributorId(qry));
    }

    /**
     *
     * @param qry
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserDistributor,
        value = CommonLogTypeConstantDTO.UserDistributorShopOrderExport)
    @ApiOperation(value = "获取柔性店铺订单导出")
    @GetMapping("/pc/shop/export")
    public void pcShopOrderInfoByDistributorIdExport(@Valid UserPCDistributorOrderInfoListQry qry,
        HttpServletResponse response) throws IOException {
        qry.setSearchType(SearchType.USER_PC_SHOP_ORDER_LIST);
        List<UserPCDistributorOrderInfoExportDTO> list = service.pcShopOrderInfoByDistributorIdExport(qry);
        excelUtils.getExporter().exportExcel(UserPCDistributorOrderInfoExportDTO.class, list, response);
    }

    /**
     * 目前下单结果详情 与 订单列表详情 一样
     * 
     * @param qry
     * @return
     */
    @ApiOperation(value = "获取订单详情")
    @GetMapping("/pc/detail")
    public Response<UserPCDistributorOrderDetailDTO>
        getPCOrderDetailInfoById(@Valid UserPCDistributorOrderDetailQry qry) {
        return Response.of(service.getPCOrderDetailInfoById(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserDistributor,
        value = CommonLogTypeConstantDTO.UserDistributorPcDeliverBillDetail)
    @ApiOperation(value = "获取订单发货单详情")
    @GetMapping("/pc/deliverBillDetail")
    public Response<List<OrderDeliverBillDetailDTO>>
        getPCOrderDeliverBillDetailById(@Valid UserPCDistributorOrderDeliverBillDetailQry qry) {
        return Response.of(service.getPCOrderDeliverBillDetailById(qry));
    }

    /**
     * @param qry
     * @return
     */
    @ApiOperation(value = "获取当前分销订单列表")
    @GetMapping("/mb/list")
    public Response<PageInfo<UserMBDistributorOrderInfoListDTO>>
        listMBOrderInfoByDistributorId(@Valid UserMBDistributorOrderInfoListQry qry) {
        qry.setSearchType(SearchType.USER_MB_DISTRIBUTOR_ORDER_LIST);
        return Response.of(service.listMBOrderInfoByDistributorId(qry));
    }

    /**
     * @param qry
     * @return
     */
    @ApiOperation(value = "获取当前分销订单列表数量")
    @GetMapping("/mb/count")
    public Response<List<UserMBOrderInfoCountDTO>>
        listMBOrderInfoCountByDistributorId(@Valid UserMBOrderInfoCountQry qry) {
        qry.setSearchType(SearchType.USER_MB_DISTRIBUTOR_ORDER_LIST);
        return Response.of(service.listMBOrderInfoCountByDistributorId(qry));
    }

    /**
     * 主要逻辑是获取当前分销商 下一级的订单
     *
     * @param qry
     * @return
     */
    @ApiOperation(value = "移动端获取下级分销订单列表")
    @GetMapping("/mb/next/list")
    public Response<PageInfo<UserMBDistributorOrderInfoListDTO>>
        listMBNextDistributorOrderInfoByDistributorId(@Valid UserMBDistributorOrderInfoListQry qry) {
        qry.setSearchType(SearchType.USER_MB_NEXT_DISTRIBUTOR_ORDER_LIST);
        PageInfo<UserMBDistributorOrderInfoListDTO> dtoPageInfo =
            service.listMBNextDistributorOrderInfoByDistributorId(qry);
        return Response.of(dtoPageInfo);
    }

    @ApiOperation(value = "移动端获取下级分销订单总数")
    @GetMapping("/mb/next/count")
    public Response<Integer>
        countMBNextDistributorOrderInfoByDistributorId(@Valid UserMBDistributorOrderInfoCountQry qry) {
        qry.setSearchType(SearchType.USER_MB_NEXT_DISTRIBUTOR_ORDER_LIST);
        Integer count = service.countMBNextDistributorOrderInfoByDistributorId(qry);
        return Response.of(count);
    }

    /**
     * 与pc端一致
     *
     * @param qry
     * @return
     */
    @ApiOperation(value = "获取订单详情")
    @GetMapping("/mb/detail")
    public Response<UserPCDistributorOrderDetailDTO>
        getMBOrderDetailInfoById(@Valid UserPCDistributorOrderDetailQry qry) {
        return Response.of(service.getPCOrderDetailInfoById(qry));
    }

    /**
     * 与pc端一致
     *
     * @param qry
     *            借用
     * 
     * @see #getMBOrderDetailInfoById(UserPCDistributorOrderDetailQry qry) 的参数
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserDistributor,
        value = CommonLogTypeConstantDTO.UserDistributorPcDeliverBillDetail)
    @ApiOperation(value = "移动端根据订单号获取获取订单发货单详情")
    @GetMapping("/mb/deliverBillDetail")
    public Response<List<OrderDeliverDetailDTO>>
        getMBOrderDeliverBillDetailByOrderId(@Valid UserPCDistributorOrderDetailQry qry) {
        return Response.of(service.getMBOrderDeliverBillDetailByOrderId(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserDistributor,
        value = CommonLogTypeConstantDTO.UserDistributorOrderCancel)
    @ApiOperation(value = "根据订单id取消订单")
    @PutMapping("/cancel")
    public Response orderCancel(@Valid @RequestBody OrderCancelCmd cmd) {
        service.orderCancel(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "分销端获取订单失效时间（单位分钟）")
    @GetMapping("/time")
    public Response<Integer> getLoseTime() {
        Integer time = service.getLoseTime();
        return Response.of(time);
    }

}
