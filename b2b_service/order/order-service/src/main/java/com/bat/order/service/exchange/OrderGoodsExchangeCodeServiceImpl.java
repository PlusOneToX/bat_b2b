package com.bat.order.service.exchange;

import java.util.List;
import java.util.stream.Collectors;

import com.bat.order.service.exchange.executor.OrderGoodsExchangeCodeQryExe;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.exchange.ExchangeCodeServiceRpc;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodePageMsgDTO;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderStatusRequest;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.exchange.api.OrderGoodsExchangeCodeServiceI;
import com.bat.order.api.exchange.dto.ExchangeCodePageQry;
import com.bat.order.api.exchange.dto.OrderGoodsExchangeCodeListDTO;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeListDO;
import com.bat.order.service.common.error.DubboServiceErrorCode;
import com.bat.order.service.order.convertor.OrderGoodsExchangeCodeConvertor;

@Service
public class OrderGoodsExchangeCodeServiceImpl implements OrderGoodsExchangeCodeServiceI {

    @Autowired
    private OrderGoodsExchangeCodeQryExe orderGoodsExchangeCodeQryExe;

    @DubboReference(check = false, timeout = 7000, retries = 0)
    private ExchangeCodeServiceRpc exchangeCodeServiceRpc;

    /**
     * 分页查询兑换码兑换数据
     * 
     * @param exchangeCodePageQry
     * @return
     */
    @Override
    public PageInfo<OrderGoodsExchangeCodeListDTO> page(ExchangeCodePageQry exchangeCodePageQry) {
        PageHelper.startPage(exchangeCodePageQry.getPage(), exchangeCodePageQry.getSize());
        List<OrderGoodsExchangeCodeListDO> list = orderGoodsExchangeCodeQryExe.listDOByCondition(
            exchangeCodePageQry.getStartTime(), exchangeCodePageQry.getEndTime(), exchangeCodePageQry.getOrderStatus(),
            exchangeCodePageQry.getContent());
        PageInfo pageInfo = new PageInfo(list);
        List<OrderGoodsExchangeCodeListDTO> resultList = BeanUtils.copyList(list, OrderGoodsExchangeCodeListDTO.class);
        if (resultList != null && resultList.size() > 0) {
            List<String> secretCodeList =
                resultList.stream().map(OrderGoodsExchangeCodeListDTO::getSecretCode).collect(Collectors.toList());
            Response<List<ExchangeCodePageMsgDTO>> response =
                exchangeCodeServiceRpc.listExchangePageMsgBySecretCodeList(secretCodeList);
            if (response == null) {
                throw OrderException.buildException(DubboServiceErrorCode.DUBBO_FLEXIBLE_SERVICE_EXCEPTION);
            }
            if (!response.isSuccess()) {
                throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
            }
            List<ExchangeCodePageMsgDTO> codePageMsgDTOList = response.getData();
            OrderGoodsExchangeCodeConvertor.setExchangeCodeMsg(codePageMsgDTOList, resultList);
            pageInfo.setList(resultList);
        }
        return pageInfo;
    }

    @Autowired
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;

    @Override
    public void test() {
        String json =
            "{\"deliverOrderNo\":\"XSCKD1002108000020\",\"expressNo\":\" \",\"expressType\":\" \",\"status\":4}";
        ErpDeliverOrderStatusRequest erpDeliverOrderStatusRequest =
            JSONObject.parseObject(json, ErpDeliverOrderStatusRequest.class);
        orderDeliveryServiceRpc.changeDeliverOrderStatus(erpDeliverOrderStatusRequest);

    }
}
