package com.bat.financial.subaccount.convertor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bat.financial.api.subaccount.dto.OrderSubAccountBillDTO;
import com.bat.financial.common.constant.subaccount.OrderSubAccountConstant;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountBillDO;

@Component
public class SubAccountConvertor {

    public static List<OrderSubAccountBillDTO>
        toOrderSubAccountBillDTOList(List<OrderSubAccountBillDO> orderSubAccountBillDOList) {
        if (orderSubAccountBillDOList == null || orderSubAccountBillDOList.size() == 0) {
            return null;
        }
        List<OrderSubAccountBillDTO> resultList = new ArrayList<>();
        orderSubAccountBillDOList.stream().forEach(orderSubAccountBillDO -> {
            OrderSubAccountBillDTO billDTO = new OrderSubAccountBillDTO();
            BeanUtils.copyProperties(orderSubAccountBillDO, billDTO);
            resultList.add(billDTO);
        });
        return resultList;
    }

    /**
     * 判断流水是否都分账了 true、没有全部分账、false、全都分账
     * 
     * @param billDOList
     * @return
     */
    public static Boolean checkExistUnSubAccount(List<OrderSubAccountBillDO> billDOList) {
        return billDOList.stream().anyMatch(orderSubAccountBillDO -> OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_UN
            .equals(orderSubAccountBillDO.getStatus()));
    }
}
