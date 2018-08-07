package com.bat.order.service.order.dto;

import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/3/11 10:38
 */
@Data
public class CancelOrderContext {
    private Integer orderId;
    private String remark;
    private String operatorId;
    private String operatorName;
    private CancelOrderEnum source;
    public enum CancelOrderEnum{
        ERP("ERP"),THIRD("第三方"),B2B("B2B"),TIMER("定时器");
        private String name;
        CancelOrderEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}


