package com.bat.distributor.service.subaccount.convertor;

import com.bat.distributor.api.subaccount.dto.SubAccountLevelRatioDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SubAccountConvertor {

    public static BigDecimal sumRatio(List<SubAccountLevelRatioDTO> levelRatioList) {
        BigDecimal sum = BigDecimal.ZERO;
        for(int x=0;x<levelRatioList.size();x++){
            SubAccountLevelRatioDTO subAccountLevelRatioDTO = levelRatioList.get(x);
            sum = sum.add(subAccountLevelRatioDTO.getRatio());
        }
        return sum;
    }
}
