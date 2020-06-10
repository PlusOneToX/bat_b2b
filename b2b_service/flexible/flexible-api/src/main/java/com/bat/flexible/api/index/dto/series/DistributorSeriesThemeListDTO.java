package com.bat.flexible.api.index.dto.series;

import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import lombok.Data;

import java.util.List;

@Data
public class DistributorSeriesThemeListDTO {


    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 分类图片
     */
    private String categoryImage;


    private Integer seriesId;


    private List<CommonPicturePageCO> pictureDTOList;



}
