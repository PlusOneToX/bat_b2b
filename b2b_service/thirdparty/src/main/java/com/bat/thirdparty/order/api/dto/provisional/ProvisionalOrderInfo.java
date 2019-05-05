package com.bat.thirdparty.order.api.dto.provisional;


import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnIdQry;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author 刘思颖
 * @date 2019/06/9
 * @time 15:31
 */
@Data
public class ProvisionalOrderInfo implements Serializable {
    private static final long serialVersionUID = 4605601208519925740L;
    /**
     * 分销商名称
     */
    private Integer distributorId;
    /**
     *  订单来源
     */
    private String orderSource;




    @Valid
    @NotBlank(message = "T_ORDER_DETAIL_LIST_NULL")
    private OrderDetailBaseOnIdQry orderDetail;


}
