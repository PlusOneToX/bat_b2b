package com.bat.distributor.service.distributor.validator;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.distributor.dto.DistributorCustomPriceListCmd;
import com.bat.distributor.service.distributor.executor.DistributorCustomPriceErrorCode;
import com.bat.distributor.service.distributor.executor.DistributorCustomPriceQryExe;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorCustomerPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DistributorCustomPriceValidtor {

    @Autowired
    private DistributorCustomPriceQryExe distributorCustomPriceQryExe;

    public static void checkParam(List<DistributorCustomPriceListCmd> customPriceListCmdList , Boolean isAdd){
        if((customPriceListCmdList ==null || customPriceListCmdList.size()==0)&& isAdd){
            throw DistributorException.buildException(DistributorCustomPriceErrorCode.D_DISTRIBUTOR_ITEM_PRICE_LIST_WHEN_ADD_NULL);
        }
        if(customPriceListCmdList !=null && customPriceListCmdList.size()>0){
            customPriceListCmdList.stream().forEach(distributorCustomPriceListCmd -> {
                if(distributorCustomPriceListCmd.getPrice() ==null){
                    throw DistributorException.buildException(DistributorCustomPriceErrorCode.D_CUSTOM_PRICE_NULL);
                }
                if(distributorCustomPriceListCmd.getPrice().compareTo(BigDecimal.ZERO) <1){
                    throw DistributorException.buildException(DistributorCustomPriceErrorCode.D_CUSTOM_PRICE_MUST_GREATER_THEN_ONE);
                }
                if(distributorCustomPriceListCmd.getItemId() ==null){
                    throw DistributorException.buildException(DistributorCustomPriceErrorCode.D_ITEM_ID_NULL);
                }
            });
        }
    }

    public void validDistributorItemExist(Integer distributorId,Integer itemId){
        // 判断分销商和货品是否已添加
        DistributorCustomerPriceDTO dto =
                distributorCustomPriceQryExe.listByDistributorIdAndItemId(distributorId, itemId);
        if (dto == null) {
            throw DistributorException
                    .buildException(DistributorCustomPriceErrorCode.D_CUSTOM_PRICE_ITEM_ALREADY_SET);
        }
    }

}
