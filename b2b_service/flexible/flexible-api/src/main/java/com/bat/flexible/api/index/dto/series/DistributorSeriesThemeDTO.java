package com.bat.flexible.api.index.dto.series;


import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
public class DistributorSeriesThemeDTO {


    private Integer id;


    /**
     * 主题名称
     */
    @NotBlank(message = "主题名称不能为空")
    private String seriesName;




    /**
     * 图片分类id
     */
    @NotNull(message = "图库分类不能为空")
    private Integer pictureCategoryId;

    /**
     * 图片id列表
     */
    @NotEmpty(message = "图片必须要选择")
    private Set<Integer> pictureIdList;

    /**
     * 图片id列表
     */
    @NotEmpty(message = "分销商必须要选择")
    private List<DistributorSimpleRelaQry> distributorList;


}
