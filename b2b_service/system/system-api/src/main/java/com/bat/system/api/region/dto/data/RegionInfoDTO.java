package com.bat.system.api.region.dto.data;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/26 11:31
 */
@Data
@ApiModel(value = "AssumeRoleDTO")
public class RegionInfoDTO implements Serializable {
    @ApiModelProperty(value = "accessKeyId")
    private Integer id;
    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String regionName;
    /**
     * 区域英文名称
     */
    @ApiModelProperty(value = "区域英文名称")
    private String regionNameEn;
    @ApiModelProperty(value = "sonRegions")
    private List<RegionInfoDTO> sonRegions;
}
