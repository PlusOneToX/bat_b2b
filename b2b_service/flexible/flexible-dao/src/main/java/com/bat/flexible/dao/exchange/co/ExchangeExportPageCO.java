package com.bat.flexible.dao.exchange.co;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExchangeExportPageCO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "发卡分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "导出记录单名称")
    private String exportName;

    @ApiModelProperty(value = "兑换卡活动id")
    private Integer exchangeId;

    @ApiModelProperty(value = "出库数量")
    private Integer outNum;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "导出文件url")
    private String fileUrl;

    @ApiModelProperty(value = "是否已审核 0否 1是")
    private Short examineFlag;

    @ApiModelProperty(value = "已使用数量")
    private Integer hasUseNum;

    @ApiModelProperty(value = "是否生成实体卡 1、是 0、否")
    private Short isEntity;

    @ApiModelProperty(value = "快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）")
    private Short mailSetting;

}
