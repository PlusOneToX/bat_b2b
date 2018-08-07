package com.bat.system.api.region.dto.data;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/15 22:40
 */
@Data
public class RegionComparisonDTO {
    private Integer id;

    private Integer regionId;

    private String regionName;

    private String anotherName;

    private Integer parentId;
}
