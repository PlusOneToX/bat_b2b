package com.bat.thirdparty.msgcenter.api.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class DistributorMsgTemplateQry {

    @ApiModelProperty("通知类型集合:1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知")
    private List<Short> types;

    public List<Short> getTypes() {
        return types;
    }

    public void setTypes(List<Short> types) {
        this.types = types;
    }
}
