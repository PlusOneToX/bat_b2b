package com.bat.order.service.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bat.order.service.common.error.OrderCommonErrorCode;
import com.bat.order.service.order.executor.OrderGoodsDiyCmdExe;
import com.bat.order.service.order.executor.OrderGoodsDiyQryExe;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.flexible.label.api.LabelServiceRpc;
import com.bat.dubboapi.flexible.label.dto.OrderDiyLabelDTO;
import com.bat.dubboapi.flexible.label.dto.OrderGoodsDiySimpleDTO;
import com.bat.dubboapi.flexible.label.dto.OrderLableCmd;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderGoodsDiyServiceRpc;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsDiyRpcDTO;
import com.bat.dubboapi.order.order.dto.info.OrderIdListQry;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.data.OrderDistributorDataServiceI;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;

@DubboService
public class OrderGoodsDiyServiceRpcImpl implements OrderGoodsDiyServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderGoodsDiyServiceRpcImpl.class);

    @DubboReference(check = false, timeout = 30000, retries = 0)
    private LabelServiceRpc labelServiceRpc;

    @Autowired
    private OrderDistributorDataServiceI orderDistributorDataServiceI;

    @Autowired
    private OrderGoodsDiyQryExe orderGoodsDiyQryExe;

    @Autowired
    private OrderGoodsDiyCmdExe orderGoodsDiyCmdExe;

    /**
     * 根据订单id查询定制订单详情
     * 
     * @param orderId
     * @return
     */
    @Override
    public Response<List<OrderGoodsDiyRpcDTO>> listByOrderId(Integer orderId) {
        List<OrderGoodsDiyDO> orderGoodsDiyDOList = orderGoodsDiyQryExe.listByOrderId(orderId);
        return Response.of(BeanUtils.copyList(orderGoodsDiyDOList, OrderGoodsDiyRpcDTO.class));
    }

    @Override
    public Response<List<OrderGoodsDiyRpcDTO>> findLatelyNullLabel() {
        List<OrderGoodsDiyDO> orderGoodsDiyDOList = orderGoodsDiyQryExe.findLatelyNullLabel();
        return Response.of(BeanUtils.copyList(orderGoodsDiyDOList, OrderGoodsDiyRpcDTO.class));
    }

    @Override
    public Response createLable(Integer orderGoodsDiyId) {
        OrderGoodsDiyDO orderGoodsDiyDO = orderGoodsDiyQryExe.getById(orderGoodsDiyId);
        if (StringUtils.isNotBlank(orderGoodsDiyDO.getLabelUrl())) {
            return Response.buildFailure(OrderCommonErrorCode.COMMON_OPERATE_REPEAT,
                MessageUtils.get(OrderCommonErrorCode.COMMON_OPERATE_REPEAT));
        }
        OrderLableCmd orderLableCmd = new OrderLableCmd();
        orderLableCmd.setOrderId(orderGoodsDiyDO.getOrderId());
        // 取第一个节点的、非同步ERP节点
        OrderDistributorDataDO orderDistributorDataDO =
            orderDistributorDataServiceI.getFirstTreeNode(orderGoodsDiyDO.getOrderId());
        orderLableCmd.setDistributorId(orderDistributorDataDO.getDistributorId());
        OrderGoodsDiySimpleDTO orderGoodsDiySimpleDTO = BeanUtils.copy(orderGoodsDiyDO, OrderGoodsDiySimpleDTO.class);
        List<OrderGoodsDiySimpleDTO> diySimpleDTOList = new ArrayList<>();
        diySimpleDTOList.add(orderGoodsDiySimpleDTO);
        orderLableCmd.setDiySimpleDTOList(diySimpleDTOList);
        com.bat.dubboapi.flexible.common.Response<List<OrderDiyLabelDTO>> labelResponse =
            labelServiceRpc.createOrderDiyLabel(orderGoodsDiyDO.getManufactors(), orderLableCmd);
        LOGGER.info("工厂：{}，调用柔性服务生成标签、返回{}", orderGoodsDiyDO.getManufactors(), JSON.toJSONString(labelResponse));
        if (labelResponse != null && labelResponse.isSuccess()) {
            List<OrderDiyLabelDTO> labelDTOS = labelResponse.getData();
            orderGoodsDiyDO.setLabelId(labelResponse.getData().get(0).getLabelId());
            orderGoodsDiyDO.setLabelUrl(labelResponse.getData().get(0).getLabelUrl());
            orderGoodsDiyDO.setUpdateTime(new Date());
            orderGoodsDiyCmdExe.update(orderGoodsDiyDO);
        }
        return BeanUtils.copy(labelResponse, Response.class);
    }

    /**
     * 根据工厂编码查询未同步工厂订单id列表
     * 
     * @param manufactor
     * @return
     */
    @Override
    public Response<OrderIdListQry> listUnSyncFactoryByManufactor(String manufactor) {
        List<Integer> orderIdList = orderGoodsDiyQryExe.listUnSyncFactory(manufactor);
        OrderIdListQry orderIdListQry = new OrderIdListQry();
        orderIdListQry.setIdList(orderIdList);
        return Response.of(orderIdListQry);
    }

    /**
     * 根据工厂编码查询需要同步的订单
     *
     * @param manufactor
     * @return
     */
    @Override
    public Response<OrderIdListQry> needToSyncOrders(String manufactor) {
        List<Integer> orderIdList = orderGoodsDiyQryExe.needToSyncOrders(manufactor);
        OrderIdListQry orderIdListQry = new OrderIdListQry();
        orderIdListQry.setIdList(orderIdList);
        return Response.of(orderIdListQry);
    }

    @Override
    public Response<OrderIdListQry> listUnSyncFactoryByStartTime(Date startTime) {
        List<Integer> orderIdList = orderGoodsDiyQryExe.listUnSyncFactoryByStartTime(startTime);
        OrderIdListQry orderIdListQry = new OrderIdListQry();
        orderIdListQry.setIdList(orderIdList);
        return Response.of(orderIdListQry);
    }
}
