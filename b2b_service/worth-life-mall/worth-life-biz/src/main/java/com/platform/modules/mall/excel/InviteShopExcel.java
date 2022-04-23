package com.platform.modules.mall.excel;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class InviteShopExcel {
    @ApiModelProperty("邀请人用户姓名")
    @Excel(name = "推广者账号", width = 15)
    private String inviteUserName;
    @ApiModelProperty("店铺数量")
    @Excel(name = "邀请店铺数量", width = 15)
    private Integer shopCount;
}
