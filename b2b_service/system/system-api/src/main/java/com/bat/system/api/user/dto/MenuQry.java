package com.bat.system.api.user.dto;

import com.bat.system.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:49
 */
@Data
@ApiModel(value = "MenuQry", description = "菜单查询")
public class MenuQry extends BaseSearchQry {

}
