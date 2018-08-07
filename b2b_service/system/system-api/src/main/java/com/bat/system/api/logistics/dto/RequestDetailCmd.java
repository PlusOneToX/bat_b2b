package com.bat.system.api.logistics.dto;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/26 10:02
 */
@Data
public class RequestDetailCmd {

    private Integer id;
    private Short accessType;
    private String groupName;
    /**
     * 物流编号
     */
    private String logisticsNo;

    private Short isShowFee;
}
