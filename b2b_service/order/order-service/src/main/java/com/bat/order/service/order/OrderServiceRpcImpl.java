package com.bat.order.service.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.order.dao.order.dataobject.OrderFactoryNoDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.common.CommonErrorCode;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.order.executor.*;
import com.bat.order.service.order.executor.customer.UserCustomerOrderQryExe;
import com.bat.order.service.third.factory.duohong.convertor.DuoHongConvertor;
import com.bat.order.service.third.factory.feikuai.convertor.FeiKuaiConvertor;
import com.bat.order.service.third.factory.haixing.convertor.HaixingConvertor;
import com.bat.order.service.third.factory.maike.convertor.MaikeConvertor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderServiceRpc;
import com.bat.dubboapi.order.order.dto.*;
import com.bat.dubboapi.order.order.dto.data.OrderDTO;
import com.bat.dubboapi.order.order.dto.data.OrderGoodsThirdCodeRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderTradeRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderVoucherErpDTO;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderQueCmd;
import com.bat.dubboapi.order.order.dto.factory.maike.MaikeOrderAddCmd;
import com.bat.order.api.basic.ErrorCode;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderGoodsThirdCodeDO;
import com.bat.order.service.data.executor.OrderCustomerDataCmdExe;
import com.bat.order.service.data.executor.OrderDistributorDataCmdExe;
import com.bat.order.service.data.executor.OrderDistributorDataQryExe;
import com.bat.order.service.data.executor.OrderExtendDataQryExe;
import com.bat.order.service.order.executor.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/29 17:51
 */
@Slf4j
@DubboService
public class OrderServiceRpcImpl implements OrderServiceRpc {
    @Resource
    private OrderRpcExe orderRpcExe;

    @Resource
    private MaikeConvertor maikeConvertor;

    @Resource
    private DuoHongConvertor duoHongConvertor;

    @Resource
    private HaixingConvertor haixingConvertor;

    @Resource
    private FeiKuaiConvertor feiKuaiConvertor;

    @Resource
    private UserCustomerOrderQryExe userCustomerOrderQryExe;

    @Resource
    private OrderCmdExe orderCmdExe;
    @Resource
    private OrderGoodsDiyCmdExe orderGoodsDiyCmdExe;
    @Resource
    private OrderQryExe orderQryExe;

    @Resource
    private OrderCustomerDataCmdExe orderCustomerDataCmdExe;
    @Resource
    private OrderDistributorDataCmdExe orderDistributorDataCmdExe;

    @Resource
    private OrderDistributorDataQryExe orderDistributorDataQryExe;

    @Resource
    private OrderExtendDataQryExe orderExtendDataQryExe;

    @Resource
    private OrderGoodsDiyQryExe orderGoodsDiyQryExe;

    @Resource
    private MessageSendService messageSendService;

    @Override
    public Response<OrderTradeRpcDTO> orderTrade(OrderTradeRpcQry qry) {
        OrderTradeRpcDTO rpcDTO = orderRpcExe.orderTrade(qry);
        if (rpcDTO.getFlag().equals(CommonErrorCode.B_TRADE_SUCCESS)) {
            return Response.of(rpcDTO);
        }
        return Response.buildFailure(rpcDTO.getFlag(), rpcDTO.getMsg());
    }

    @Override
    public Response<List<OrderDTO>> createDistributorOrder(OrderInfoCmd cmd) {
        try {
            List<OrderDTO> orders = orderRpcExe.createDistributorOrder(cmd);
            return Response.of(orders);
        } catch (OrderException e) {
            return Response.buildFailure(e.getCode(), MessageUtils.get(e.getCode()));
        }
    }

    @Override
    public Response orderChangeByErp(ErpOrderChangeCmd cmd) {
        log.info("erp------>订单变更接口,请求参数：" + JSON.toJSONString(cmd));
        try {
            orderRpcExe.orderChangeByErp(cmd);
            return Response.buildSuccess();
        } catch (OrderException e) {
            return Response.buildFailure(e.getCode(), MessageUtils.get(e.getCode()));
        }
    }

    /**
     * 组装工厂参数
     * 
     * @param orderId
     * @return
     */
    @Override
    public Response<MaikeOrderAddCmd> assemblyParamToFactory(Integer orderId) {
        try {
            MaikeOrderAddCmd maikeOrderAddCmd = maikeConvertor.toMaikeOrderAddCmd(orderId);
            return Response.of(maikeOrderAddCmd);
        } catch (OrderException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(), e.getMsg());
        }
    }

    /**
     * 组装工厂参数V2 通用
     * 
     * @param orderId
     * @return
     */
    @Override
    public Response<FactoryOrderAddCmd> assemblyParamToFactoryV2(Integer orderId) {
        // 查询订单是不是 定制订单 工厂是哪个工厂 用对应工厂的封装参数
        List<OrderGoodsDiyDO> orderGoodsDiyDOS = orderGoodsDiyQryExe.listByOrderId(orderId);
        OrderGoodsDiyDO orderGoodsDiyDO = orderGoodsDiyDOS.stream()
            .filter(aDo -> StringUtils.isNotBlank(aDo.getManufactors())).findFirst().orElse(null);
        String factoryCode;
        if (orderGoodsDiyDO != null) {
            factoryCode = orderGoodsDiyDO.getManufactors().toUpperCase();
        } else {
            return Response.buildFailure(OrderInfoErrorCode.MANUFACTORS_CODE_NOT_EXIST, MessageUtils.get(OrderInfoErrorCode.MANUFACTORS_CODE_NOT_EXIST));
        }
        try {
            FactoryOrderAddCmd addCmd = new FactoryOrderAddCmd();
            if (factoryCode.equals(FactoryEnum.MK.name())) {
                addCmd.setFactoryEnum(FactoryEnum.MK);
                addCmd.setMaikeOrderAddCmd(maikeConvertor.toMaikeOrderAddCmd(orderId));
            } else if (factoryCode.startsWith(FactoryEnum.DH.name())) {
                // 如果乱填 是有可能为空
                addCmd.setFactoryEnum(FactoryEnum.valueOf(factoryCode));
                addCmd.setDuoHongOrderAddCmd(duoHongConvertor.toDuoHongOrderAddCmd(orderId, factoryCode));
            } else if (factoryCode.equals(FactoryEnum.LHW.name())) {
                addCmd.setFactoryEnum(FactoryEnum.LHW);
                addCmd.setHaiXingOrderAddCmd(haixingConvertor.toHaixingOrderAddCmd(orderId, factoryCode));
            }else if(factoryCode.equals(FactoryEnum.KDS_FK.name())){
                addCmd.setFactoryEnum(FactoryEnum.KDS_FK);
                addCmd.setFeiKuaiOrderAddCmd(feiKuaiConvertor.toFeiKuaiOrderAddCmd(orderId,factoryCode));
            }
            return Response.of(addCmd);
        } catch (OrderException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.P_NOTNULL, e.getMessage());
        }
    }

    @Override
    public Response<List<OrderGoodsThirdCodeRpcDTO>> thirdWillWriteOffOrder() {
        List<OrderGoodsThirdCodeDO> orderGoodsThirdCodeDOS = userCustomerOrderQryExe.listDeliverWriteOff();
        List<OrderGoodsThirdCodeRpcDTO> dtos =
            BeanUtils.copyList(orderGoodsThirdCodeDOS, OrderGoodsThirdCodeRpcDTO.class);
        if (dtos == null) {
            dtos = new ArrayList<>();
        }
        return Response.of(dtos);
    }

    @Override
    public Response writeOffThirdCodeOrders(ThirdCodeOrderCmd cmd) {
        userCustomerOrderQryExe.writeOffThirdCodeOrders(cmd.getIds());
        return Response.buildSuccess();
    }

    @Override
    public Response orderCancelByThirdparty(OrderCancelCmd cmd) {
        try {
            orderCmdExe.cancelOrderByThirdparty(cmd.getOrderId(), cmd.getOrderNo(), cmd.getOrderThirdpartyNo(),
                cmd.getDistributorId(), cmd.getRemark());
            return Response.buildSuccess();
        } catch (OrderException e) {
            return Response.buildFailure(e.getCode(), e.getMsg());
        }
    }

    @Override
    public Response orderCancelByXXLJob() {
        try {
            orderCmdExe.orderCancelByXXLJob();
            return Response.buildSuccess();
        } catch (OrderException e) {
            return Response.buildFailure(e.getCode(), e.getMsg());
        }
    }

    @Override
    public Response orderCheckByErp(OrderCheckCmd cmd) {
        try {
            orderCmdExe.orderCheckByErp(cmd.getOrderErpNo(), cmd.getOrderStatus(), null);
            return Response.buildSuccess();
        } catch (OrderException e) {
            try {
                OrderExtendDataDO orderExtendDataDO = orderExtendDataQryExe.getByOrderErpNo(cmd.getOrderErpNo());
                messageSendService.oredrLogPackage(orderExtendDataDO.getOrderId(), "ERP订单状态变更",
                    "变更出现异常:" + JSONObject.toJSONString(e), JSONObject.toJSONString(cmd), "erp");
            } catch (Exception f) {
                log.error("日志发送异常:{}", f.getMessage());
            }
            return Response.buildFailure(e.getCode(), e.getMsg());
        }
    }

    @Override
    public Response orderGoodsDiyLabel(List<OrderGoodsDiyLabelCmd> cmds) {
        orderGoodsDiyCmdExe.orderGoodsDiyLabel(cmds);
        return Response.buildSuccess();
    }

    @Override
    public Response<List<OrderVoucherErpDTO>> orderVoucherErp(List<Integer> orderIds) {
        List<OrderVoucherErpDTO> erpDTOS = orderQryExe.orderVoucherErp(orderIds);
        return Response.of(erpDTOS);
    }

    @Override
    public Response orderPayStatus(OrderPayStatusCmd cmd) {
        if (cmd.getReceiverType().equals(Constant.RECEIVER_TYPE_1)) {
            orderDistributorDataCmdExe.orderPayStatus(cmd);
        } else {
            orderCustomerDataCmdExe.orderPayStatus(cmd);
        }
        return Response.buildSuccess();
    }

    @Override
    public Response<List<Integer>> getNotErpOrderIds(Date startTime) {
        List<Integer> orderIds = orderDistributorDataQryExe.getNotErpOrderIds(startTime);
        return Response.of(orderIds);
    }

    /**
     * 根据订单Id查询订单工厂信息
     *
     * @return
     */
    @Override
    public Response<FactoryOrderQueCmd> orderFactoryInformationByOrderId(Integer orderId) {
        // 查询订单是不是定制商品以及是哪个工厂定制的
        List<OrderGoodsDiyDO> orderGoodsDiyDOS = orderGoodsDiyQryExe.listByOrderId(orderId);
        OrderGoodsDiyDO orderGoodsDiyDO = orderGoodsDiyDOS.stream()
            .filter(aDo -> StringUtils.isNotBlank(aDo.getManufactors())).findFirst().orElse(null);
        String factoryCode;
        if (orderGoodsDiyDO != null) {
            factoryCode = orderGoodsDiyDO.getManufactors().toUpperCase();
        } else {
            return Response.buildFailure(OrderInfoErrorCode.MANUFACTORS_CODE_NOT_EXIST, MessageUtils.get(OrderInfoErrorCode.MANUFACTORS_CODE_NOT_EXIST));
        }
        try {
            // 根据bat订单id查询工厂订单编号
            String orderFactoryNo = orderExtendDataQryExe.queryOrderFactoryNoByOrderId(orderId);
            OrderInfoDO order = orderQryExe.getById(orderId);
            FactoryOrderQueCmd queCmd = new FactoryOrderQueCmd();
            queCmd.setOrderNo(order.getOrderNo());
            queCmd.setFactoryEnum(FactoryEnum.valueOf(factoryCode));
            if (ObjectUtils.isNotEmpty(orderFactoryNo)) {
                queCmd.setOrderFactoryNo(orderFactoryNo);
            }
            if (factoryCode.equals(FactoryEnum.LHW.name())) {
                queCmd.setHaiXingOrderQueCmd(haixingConvertor.toHaixingOrderQueCmd(orderId, factoryCode));
            }
            return Response.of(queCmd);
        } catch (OrderException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.P_NOTNULL, e.getMessage());
        }
    }

    @Override
    public Response<List<FactoryOrderQueCmd>> orderFactoryInformationByFactoryCode(String factoryCode,Date startTime) {
        List<FactoryOrderQueCmd> queCmds = new ArrayList<>();
        List<OrderFactoryNoDO> orderFactoryNoDOS = orderQryExe.orderDiyByFactoryCodeAndNoLogistics(factoryCode,startTime);
        if (!CollectionUtils.isEmpty(orderFactoryNoDOS)){
            orderFactoryNoDOS.forEach(orderFactoryNoDO ->{
                FactoryOrderQueCmd queCmd = new FactoryOrderQueCmd();
                queCmd.setOrderNo(orderFactoryNoDO.getOrderNo());
                queCmd.setOrderFactoryNo(orderFactoryNoDO.getOrderFactoryNo());
                queCmd.setFactoryEnum(FactoryEnum.valueOf(factoryCode));
                queCmds.add(queCmd);
            });
        }
        return Response.of(queCmds);
    }

    @Override
    public void updateShoppingCartItemStatus(Integer goodsId, Integer cartItemStatus) {
        orderQryExe.updateShoppingCartItemStatus(goodsId, cartItemStatus);
    }
}