package com.bat.flexible.dao.third.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
public class ThirdSkuRelaExcelCO {



    private Integer id;

    /**
     * 材质名称
     */
    private String materialName;

    /**
     * 材质编号
     */
    private String materialNo;

    private String categoryName;

    private String parentMaterialName;


    private String materialCategoryName;


    private String  materialCategoryNo;

    /**
     * 型号归属品牌
     */
    private String parentModelName;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 型号编码
     */
    private String modelNo;

    private String thirdMaterialName;

    private String thirdMaterialNo;

    private String colourNo;

    private String colourName;

    private String thirdModelNo;


    private String thirdModelName;

    private String series;

    private String brandNo;

    private String brandName;

    private  String thirdSkuNo;


}
