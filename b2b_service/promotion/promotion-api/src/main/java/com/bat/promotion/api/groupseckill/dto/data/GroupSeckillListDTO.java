package com.bat.promotion.api.groupseckill.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "拼团秒杀活动列表信息")
public class GroupSeckillListDTO {
    @ApiModelProperty(value = "拼团秒杀活动id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "拼团秒杀活动名称", example = "拼团秒杀活动名称")
    private String name;
    @ApiModelProperty(value = "拼团秒杀活动描述", example = "促销活动描述")
    private String groupSeckillDesc;
    @ApiModelProperty(value = "拼团秒杀：1拼团 2秒杀", example = "1")
    private Short groupSeckillType;
    @ApiModelProperty(value = "开始时间", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value = "结束时间", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value = "是否提前展示：1 准时2 提前", example = "1")
    private Short advanceFlag;
    @ApiModelProperty(value = "申请状态：0草稿 1申请中 2申请通过 3申请失败", example = "1")
    private Short applyStatus;
    @ApiModelProperty(value = "拼团秒杀状态： 0、未开始、1、进行中 2、已暂停 3、已过期 4 提前结束", example = "1")
    private Short groupSeckillStatus;
    @ApiModelProperty(value = "排序号", example = "1")
    private Integer sort;
    @ApiModelProperty(value = "分销商可视范围：1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", example = "1")
    private Short distributorScope;
    @ApiModelProperty(value = "分销商不可视范围：0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", example = "0")
    private Short distributorScopeNo;
}
