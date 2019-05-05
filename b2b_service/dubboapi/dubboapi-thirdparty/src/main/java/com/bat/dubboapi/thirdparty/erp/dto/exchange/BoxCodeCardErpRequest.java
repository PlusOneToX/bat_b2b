package com.bat.dubboapi.thirdparty.erp.dto.exchange;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BoxCodeCardErpRequest implements Serializable {

    private static final long serialVersionUID = 7173629001281352323L;
    /**
     * 盒码
     */
    private String F_HCode;


    /**
     * 物料编码、货品编码
     */
    private String F_MateID;

    /**
     * 生产日期、就是活动的开始时间
     */
    private String FCreateDate;

    /**
     * 有效日期、就是活动的结束时间
     */
    private String F_CanUseDate;

    /**
     * 材质盒码排序号
     */
    private Integer sortNo;

    /**
     * 明码列表
     */
    private List<PlainCodeERPRequest> MCards;



}
