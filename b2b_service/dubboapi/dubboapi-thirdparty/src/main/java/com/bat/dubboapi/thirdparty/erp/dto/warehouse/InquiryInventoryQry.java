package com.bat.dubboapi.thirdparty.erp.dto.warehouse;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InquiryInventoryQry implements Serializable {

    private static final long serialVersionUID = 1694370073770601828L;
    /**
     * 时间、天 (默认是60天)
     */
    private Integer Datetime=60;

    /**
     * 仓库编号
     */
    private String warehouseNo;

    /**
     * 页面大小
     */
    private Integer size = 1000;

    /**
     * 页码
     */
    private Integer page;

    /**
     * erp id来查询
     */
    private List<Long> itemErpIdList;


}
