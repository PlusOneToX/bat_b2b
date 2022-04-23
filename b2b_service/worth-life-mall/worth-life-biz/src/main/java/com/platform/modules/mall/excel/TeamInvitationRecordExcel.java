package com.platform.modules.mall.excel;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TeamInvitationRecordExcel {

    @Excel(name = "序号", width = 15)
    @TableId
    private String id;

    @Excel(name = "用户账号", width = 15)
    private String userAccountNumber;

    @Excel(name = "等级名称", width = 15)
    private String levelName;

    @Excel(name = "直推人数", width = 15)
    private Integer directCount;

    @Excel(name = "间推人数", width = 15)
    private Integer indirectCount;
}
