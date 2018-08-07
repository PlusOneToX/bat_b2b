package com.bat.system.api.region.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:50
 */
@Data
@ApiModel(value = "AssumeRoleDTO")
public class RegionDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "区域名称")
    private String regionName;
    @ApiModelProperty(value = "父id")
    private Integer parentId;
    @ApiModelProperty(value = "区域级数 国家/省/市/区 1/2/3/4")
    private Short level;
    @ApiModelProperty(value = "是否有下一级")
    private Short haveNext;
    @ApiModelProperty(value = "区域英文名称")
    private String regionNameEn;
    @ApiModelProperty(value = "ERP编码")
    private String erpCode;
    @ApiModelProperty(value = "是否开启 0未开启 1开启")
    private Short openFlag;
}
