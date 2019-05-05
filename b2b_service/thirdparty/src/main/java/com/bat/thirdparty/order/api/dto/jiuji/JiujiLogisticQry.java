package com.bat.thirdparty.order.api.dto.jiuji;

import lombok.Data;

/**
 * 物流回调查询接口
 */
@Data
public class JiujiLogisticQry {


    private Integer distributorId;


    private String otherOrderNo;


    private String userId;

    private String expressNo;


}
