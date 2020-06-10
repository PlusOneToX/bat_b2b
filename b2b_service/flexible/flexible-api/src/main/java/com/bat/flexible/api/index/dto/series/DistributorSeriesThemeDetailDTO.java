package com.bat.flexible.api.index.dto.series;


import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import lombok.Data;

import java.util.List;

@Data
public class DistributorSeriesThemeDetailDTO {


    private Integer id;


    /**
     * 主题名称
     */
    private String seriesName;

    /**
     *
     */
    private Integer sortNo;


    /**
     * 图片分类id
     */
    private Integer pictureCategoryId;

    /**
     * 图片一级分类id(pictureCategoryId存在两级时)
     */
    private Integer parentCategoryId;

    /**
     * 图片id列表
     */
    private List<IndexRecommendRelaCO> pictureList;


    private List<DistributorSimpleRelaQry> distributorList;


}
