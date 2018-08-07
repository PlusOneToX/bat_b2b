package com.bat.goods.api.user.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/27 19:49
 */
@Data
@ApiModel(value = "UserClassifyIdQry", description = "子分类列表查询(父类id传0时获取顶级分类)")
public class UserSubClassifyListQry implements Serializable {
    @ApiModelProperty(value = "父级分类id，如传0时获取顶级分类", required = true, example = "78445")
    private Integer classifyId;
}
