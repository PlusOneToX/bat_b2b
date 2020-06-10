package com.bat.flexible.api.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.index.dto.page.UserSeriesThemePageQry;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;

import java.util.List;

public interface SeriesPictureRelevanceServiceI {

    /**
     * 绑定图片和主题系列关联
     * @param pictureList
     * @param id
     * @param isAdd true、首次新增 false、修改
     * @param currentAdmin
     */
    void save(List<Integer> pictureList, Integer id, boolean isAdd, AdminResponse currentAdmin);



    Response updateSortNo(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin);

    Response delete(List<Integer> list);

    void deleteBySeriesId(Integer seriesId);

    List<IndexRecommendRelaCO> listCOByIndexSeriesId(Integer id);

    /**
     * 根据系列id分页查询
     * @param userSeriesThemePageQry
     * @return
     */
    PageInfo<CommonPicturePageCO> pageBySeriesId(UserSeriesThemePageQry userSeriesThemePageQry);
}
