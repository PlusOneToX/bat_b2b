package com.bat.flexible.manager.order;

import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.manager.order.executor.FlexibleOrderQryExe;
import com.bat.flexible.manager.order.validator.FlexibleOrderValidator;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.order.api.FlexibleOrderServiceRpc;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnCodeQry;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnIdQry;
import com.bat.dubboapi.flexible.order.dto.OrderGoodsDiyParamDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class FlexibleOrderServiceRpcImpl  implements FlexibleOrderServiceRpc {

    @Autowired
    private FlexibleOrderQryExe flexibleOrderQryExe;

    @Autowired
    private FlexibleOrderValidator flexibleOrderValidator;

    /**
     * 参数校验、基于ID
     * @param orderDetailBaseOnIdQryList
     * @return
     */
    @Override
    public Response<List<OrderGoodsDiyParamDTO>> validThirdOrderParamBaseId(List<OrderDetailBaseOnIdQry> orderDetailBaseOnIdQryList) {
        List<OrderGoodsDiyParamDTO> list = null;
        try {
            list = flexibleOrderValidator.validThirdOrderParamBaseId(orderDetailBaseOnIdQryList);
        } catch (FlexibleCustomException e ) {
            e.printStackTrace();
            return Response.buildFailure(e.getMessage(), StringUtils.isNotBlank(MessageUtils.get(e.getMessage()))?MessageUtils.get(e.getMessage()):e.getMessage() );
        }catch (FlexibleDubboApiException e){
            e.printStackTrace();
            return Response.buildFailure(e.getCode(),StringUtils.isNotBlank(MessageUtils.get(e.getMsg()))?MessageUtils.get(e.getMsg()):e.getMsg());
        }
        return  Response.of(list);
    }

    /**
     * 参数校验、基于编码
     * @param orderDetailBaseOnCodeQryList
     * @return
     */
    @Override
    public Response<List<OrderGoodsDiyParamDTO>> validThirdOrderParamBaseOnCode(List<OrderDetailBaseOnCodeQry> orderDetailBaseOnCodeQryList) {
        List<OrderGoodsDiyParamDTO> list = null;
        try {
            list = flexibleOrderValidator.validThirdOrderParamBaseOnCode(orderDetailBaseOnCodeQryList);
        } catch (FlexibleCustomException e ) {
            e.printStackTrace();
            return Response.buildFailure(e.getMessage(), StringUtils.isNotBlank(MessageUtils.get(e.getMessage()))?MessageUtils.get(e.getMessage()):e.getMessage() );
        }catch (FlexibleDubboApiException e){
            e.printStackTrace();
            return Response.buildFailure(e.getCode(),StringUtils.isNotBlank(MessageUtils.get(e.getMsg()))?MessageUtils.get(e.getMsg()):e.getMsg());
        }
        return  Response.of(list);
    }

}
