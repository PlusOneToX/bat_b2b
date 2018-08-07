package com.bat.warehouse.dao.warehouse.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class WarehouseCO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "仓库主键id")
    private Integer id;

    /**
     * 仓库名称
     */
    @ApiModelProperty(value = "仓库名称")
    private String name;


    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String warehouseNo;

    /**
     * 所属区域Id
     */
    @ApiModelProperty(value = "销售区域id")
    private String areaId;

    /**
     * 所属区域
     */
    @ApiModelProperty(value = "所属区域")
    private String areaName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 集成类型 1、集成 0、不集成
     */
    @ApiModelProperty(value = "集成类型 1、集成 0、不集成")
    private Byte syncType;


    /**
     * 参与存销比计算 1、是 0、否
     */
    @ApiModelProperty(value = "参与存销比计算 1、是 0、否")
    private Byte calculationType;

    /**
     * 是否平台仓 1、是 0、否
     */
    @ApiModelProperty(value = "是否平台仓 ",notes = "1、是 0、否")
    private Byte isPlatform;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private String createUserName;

    private Date updateTime;


    private String updateUserName;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Integer sortNo;

    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    private Short openFlag;
}
