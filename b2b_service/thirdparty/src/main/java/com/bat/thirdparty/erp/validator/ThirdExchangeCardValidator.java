package com.bat.thirdparty.erp.validator;

import com.bat.thirdparty.common.base.ThirdPartyBaseException;
import com.bat.thirdparty.common.error.flexible.exchange.ExchangeErpErrorConstant;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeBoxPlainCodeDTO;
import com.bat.dubboapi.flexible.exchange.dto.ItemBoxCodeRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 兑换卡ERP参数校验
 */
public class ThirdExchangeCardValidator {

    /**
     * 校验出库
     * @param requestList
     */
    public static void validErpBindingBoxCode( List<ItemBoxCodeRequest> requestList){
        for(int x=0;x<requestList.size();x++){
            ItemBoxCodeRequest itemBoxCodeRequest = requestList.get(x);
            if(itemBoxCodeRequest.getBoxCodeList()==null || itemBoxCodeRequest.getBoxCodeList().size()==0){
                throw new ThirdPartyBaseException(ExchangeErpErrorConstant.BoxCodeNullError.getCode(),"第"+(x+1)+"行"+ ExchangeErpErrorConstant.BoxCodeNullError.getMsg());
            }
            if(StringUtils.isBlank(itemBoxCodeRequest.getItemCode())){
                throw new   ThirdPartyBaseException(ExchangeErpErrorConstant.ItemCodeNullError.getCode(),"第"+(x+1)+"行"+ExchangeErpErrorConstant.ItemCodeNullError.getMsg());
            }
            if(StringUtils.isBlank(itemBoxCodeRequest.getOrderNo())){
                throw new   ThirdPartyBaseException(ExchangeErpErrorConstant.OrderNoNull.getCode(),"第"+(x+1)+"行"+ExchangeErpErrorConstant.OrderNoNull.getMsg());
            }
        }
    }

    /**
     * 校验ERP退换
     * @param list
     */
    public static void validErpRefund(List<ExchangeBoxPlainCodeDTO> list){

        list.stream().forEach(exchangeBoxPlainCodeDTO -> {
            if(StringUtils.isBlank(exchangeBoxPlainCodeDTO.getBoxCode()) && (exchangeBoxPlainCodeDTO.getPlainCodeList()==null ||
                    exchangeBoxPlainCodeDTO.getPlainCodeList().size()==0)){
                throw new ThirdPartyBaseException(ExchangeErpErrorConstant.BoxCodeAndPlainCodeNull.getCode(),ExchangeErpErrorConstant.BoxCodeAndPlainCodeNull.getMsg());
            }
        });
    }
}
