package com.bat.promotion.api.rebatevoucher.dto;

import java.util.Date;

import com.bat.promotion.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 19:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "返利代金券列表查询")
public class RebateVoucherListQry extends BaseSearchQry {
    public RebateVoucherListQry() {
        super.attributeMapper.put((short)1, "setName");
        super.attributeMapper.put((short)2, "setVoucherNo");
        super.attributeMapper.put((short)3, "setDistributorId");
        super.attributeMapper.put((short)4, "setDistributorName");
    }
    @ApiModelProperty(value = "1.代金券券名", example = "九月返利")
    private String name;
    @ApiModelProperty(value = "2.代金券券号", example = "九月返利")
    private String voucherNo;
    @ApiModelProperty(value = "3.分销商Id", example = "0")
    private Integer distributorId;
    @ApiModelProperty(value = "4.分销商用户名(模糊搜索)", example = "0")
    private String distributorName;
    @ApiModelProperty(value = "代金券状态（汇总 单个状态） 0草稿 1待审核 3审核拒绝 4待生效 5 可用 7已过期 9已用完 11已冻结", example = "1")
    private Short voucherStatus;
    @ApiModelProperty(value = "代金券状态（汇总 查询逗号分割） 0草稿 1待审核 3审核拒绝 4待生效 5 可用 7已过期 9已用完 11已冻结", example = "1")
    private String voucherStatusStr;
    @ApiModelProperty(value = "开始时间", example = "2019-12-31 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value = "结束时间", example = "2019-12-31 23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value = "排序类型 1 正序 2 倒序", example = "1")
    private Short sortType = 2;
    @ApiModelProperty(value = "排序字段 1 创建时间 2 更新时间 3 开始时间 4 结束时间", example = "1")
    private Short sortColumn = 1;
}
