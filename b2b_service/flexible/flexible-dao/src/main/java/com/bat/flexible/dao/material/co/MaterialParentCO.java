package com.bat.flexible.dao.material.co;

import java.util.List;

public class MaterialParentCO {

    /**
     * 材质id
     */
    private Integer parentId;


    private String parentName;


    private String parentEnglishName;


    private String parentMaterialNo;

    /**
     * 描述信息
     */
    private String parentDescription;


    /**
     * 材质图片
     */
    private String parentImage;

    /**
     * 副标题
     */
    private String parentSubtitle;


    private String parentColour;

    private Short isStockOut;

    private List<MaterialSonDTO> sonDTOList;


}
