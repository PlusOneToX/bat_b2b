package com.bat.flexible.dao.model.dataobject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ModelMaterialRelevanceDO implements Serializable {
    private static final long serialVersionUID = 7653612885319592635L;

    @ApiModelProperty(value = "关联主键id")
    private Integer id;

    @ApiModelProperty(value = "型号id")
    private Integer modelId;

    @ApiModelProperty(value = "材质id")
    private Integer materialId;

    @ApiModelProperty(value = "长度")
    private BigDecimal length;

    @ApiModelProperty(value = "宽度")
    private BigDecimal width;

    @ApiModelProperty(value = "高度")
    private BigDecimal height;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "外框图")
    private String outImage;

    @ApiModelProperty(value = "底图")
    private String floorImage;

    @ApiModelProperty(value = "状态值 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "第三方sku编码")
    private String thirdSku;

    @ApiModelProperty(value = "提醒信息")
    private String warnMsg;

    @ApiModelProperty(value = "BOM编码")
    private String bomCode;

    @ApiModelProperty(value = "切割类型 1纵切 2横切")
    private Short cutType;

    @ApiModelProperty(value = "纵切高度")
    private Integer slittingHeight;

    @ApiModelProperty(value = "纵切高度留白")
    private Integer slittingWhite;

    @ApiModelProperty(value = "横切宽度")
    private Integer crosscuttingWidth;

    @ApiModelProperty(value = "横切宽度留白")
    private Integer crosscuttingWhite;

    @ApiModelProperty(value = "是否缺货 0否 1是")
    private Short underStockFlag;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    @ApiModelProperty(value = "是否删除 1、是 0、否、默认是0")
    private Short delFlag;

    @ApiModelProperty(value = "上边框")
    private BigDecimal topFrame;

    @ApiModelProperty(value = "下边框")
    private BigDecimal underFrame;

    @ApiModelProperty(value = "左边框")
    private BigDecimal leftFrame;

    @ApiModelProperty(value = "右边框")
    private BigDecimal rightFrame;


}