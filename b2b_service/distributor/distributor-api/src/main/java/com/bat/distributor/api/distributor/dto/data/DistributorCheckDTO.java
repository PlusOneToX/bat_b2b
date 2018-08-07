package com.bat.distributor.api.distributor.dto.data;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商资料审批详情")
public class DistributorCheckDTO {

    @ApiModelProperty(value = "审批单号", example = "bat2018")
    private Integer id;
    @ApiModelProperty(value = "审批状态 0, 审批中 1,审批通过，2审批未通过 ", example = "0")
    private Short checkStatus;
    @ApiModelProperty(value = "审批类型: 1 新增 2 修改", example = "1")
    private Short checkType;
    @ApiModelProperty(value = "当前审批人id", example = "bat2018")
    private Integer checkUserId;
    @ApiModelProperty(value = "当前审批人名称", example = "bat2018")
    private String checkUserName;
    @ApiModelProperty(value = "发起人id", example = "bat2018")
    private Integer userId;
    @ApiModelProperty(value = "发起人", example = "bat2018")
    private String userName;
    @ApiModelProperty(value = "创建时间或发起时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "审批流程")
    private List<DistributorCheckFlowDTO> checkFlows;

    @ApiModelProperty(value = "分销商详情(审批类型为1时有值)")
    private DistributorDTO distributor;

    @ApiModelProperty(value = "分销商修改列表(审批类型为2时有值)")
    private List<DistributorCheckContentDTO> checkContents;
}
