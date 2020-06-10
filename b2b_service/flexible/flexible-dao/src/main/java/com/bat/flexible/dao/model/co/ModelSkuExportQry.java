package com.bat.flexible.dao.model.co;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ModelSkuExportQry {


    private Integer id;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 材质名称
     */
    private String materialName;

    /**
     * 型号编码
     */
    private String modelNo;

    /**
     * 入网型号
     */
    private String networkModel;

    /**
     * 长
     */
    private BigDecimal length;

    /**
     * 宽
     */
    private BigDecimal width;

    /**
     * 高
     */
    private BigDecimal height;

    /**
     * 第三方sku
     */
    private String thirdSku;

    /**
     * bom编码
     */
    private String bomCode;



}
