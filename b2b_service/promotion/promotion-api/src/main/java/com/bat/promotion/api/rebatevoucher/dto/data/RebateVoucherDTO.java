package com.bat.promotion.api.rebatevoucher.dto.data;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/12/31 16:15
 */
@Data
@ApiModel(description = "返利代金券信息")
public class RebateVoucherDTO {

    @ApiModelProperty(value = "返利代金券id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "分销商id", example = "2491")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称", example = "test01")
    private String distributorName;

    @ApiModelProperty(value = "代金券券名", example = "九月返利")
    private String name;

    @ApiModelProperty(value = "代金券编号", required = false, example = "九月返利")
    private String voucherNo;

    @ApiModelProperty(value = "面值", example = "200")
    private BigDecimal faceValue;

    @ApiModelProperty(value = "余额", example = "200")
    private BigDecimal balance;

    @ApiModelProperty(value = "申请状态 0草稿 1待审核 2审核通过(可用) 3审核拒绝", example = "2")
    private Short applyStatus;

    @ApiModelProperty(value = "冻结状态 10未冻结(可用) 11冻结", example = "10")
    private Short freezeStatus;

    @ApiModelProperty(value = "代金券状态（汇总） 0草稿 1待审核 3审核拒绝 4待生效 5 可用 7已过期 9已用完 11已冻结", example = "5")
    private Short voucherStatus;

    @ApiModelProperty(value = "开始时间", example = "2019-12-31 00:00:00")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2019-12-31 23:59:59")
    private Date endTime;

    @ApiModelProperty(value = "排序号", example = "1")
    private Integer sort;

    @ApiModelProperty(value = "备注", example = "")
    private String remark;

    @ApiModelProperty(value = "创建时间", example = "2019-12-31 00:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", example = "2019-12-31 23:59:59")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "批量id(一起导入的第一个代金券id)", example = "")
    private Integer batchId;

    @ApiModelProperty(value = "批量名称(一起导入的第一个代金券名称)", example = "")
    private String batchName;

    @ApiModelProperty(value = "创建用户id", example = "")
    private Integer createUserId;

    @ApiModelProperty(value = "创建用户名称", example = "")
    private String createUserName;

    @ApiModelProperty(value = "审核用户id", example = "")
    private Integer checkUserId;

    @ApiModelProperty(value = "审核用户名称", example = "")
    private String checkUserName;

    @ApiModelProperty(value = "审核时间", example = "2019-12-31 23:59:59")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
}
