package com.bat.flexible.dao.material.co;

import com.bat.flexible.dao.model.co.ModelMaterialRelevanceBaseCO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel
public class MaterialTreeCO  extends ModelMaterialRelevanceBaseCO implements Serializable {

    private static final long serialVersionUID = 4876047168618550321L;
    /**
     * 材质id
     */
    @ApiModelProperty(value = "材质id")
    private Integer materialId;


    @ApiModelProperty(value = "材质父id")
    private Integer parentId;

    @ApiModelProperty(value = "材质名称")
    private String name;

    @ApiModelProperty(value = "材质英文")
    private String englishName;

    @ApiModelProperty(value = "材质编码")
    private String materialNo;

    @ApiModelProperty(value = "描述信息")
    private String description;

    @ApiModelProperty(value = "材质图片")
    private String image;

    @ApiModelProperty(value = "副标题")
    private String subtitle;

    @ApiModelProperty(value = "颜色")
    private String colour;

    @ApiModelProperty(value = "工厂编码")
    private String manufactor;

    @ApiModelProperty(value = "货品id")
    private Integer itemId;

    @ApiModelProperty(value = "货品编码")
    private String itemCode;

    @ApiModelProperty(value = "是否允许用户上传图片 1、是 0、否（最终材质才有）")
    private Short allowUploadFlag;

    @ApiModelProperty(value = "材质价格")
    private BigDecimal price;

    @ApiModelProperty(value = "是否为最终材质 1、是 0、否")
    private Short atLastTrademark;

    @ApiModelProperty(value = "是否强制铺满出血位 1、是 0、否")
    private Short mandatoryCoveredBleedFlag;

    @ApiModelProperty(value = "子级列表")
    private List<MaterialTreeCO> childrenList;

    @ApiModelProperty(value = "是否有效 0无效 1有效")
    private Short validFlag=1;

    @ApiModelProperty(value = "无效类型 1型号无效 2图片无效")
    private Short validType = 1;

    public Short getValidFlag() {
        if (validFlag == null) {
            return 1;
        }
        return validFlag;
    }

    public Short getValidType() {
        if(validType==null){
            return 1;
        }
        return validType;
    }
}
