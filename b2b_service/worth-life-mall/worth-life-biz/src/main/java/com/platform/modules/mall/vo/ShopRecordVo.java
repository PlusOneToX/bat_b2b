package com.platform.modules.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ShopRecordVo {
    @ApiModelProperty("用户昵称")
    private String nikeName;
    @ApiModelProperty("店铺名称")
    private String name;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("用户id")
    private String userId;



    
}
