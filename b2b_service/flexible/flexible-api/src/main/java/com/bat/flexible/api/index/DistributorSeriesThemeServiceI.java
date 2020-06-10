package com.bat.flexible.api.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.index.dto.series.DistributorSeriesThemeDTO;
import com.bat.flexible.api.index.dto.series.DistributorSeriesThemeQry;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.index.dto.page.SeriesThemePageQry;
import com.bat.flexible.dao.index.co.SeriesThemePageCO;
import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeDO;

import java.util.List;

public interface DistributorSeriesThemeServiceI {
    /**
     * 新增
     * @param distributorSeriesThemeDTO
     * @param currentAdmin
     * @return
     */
    Response create(DistributorSeriesThemeDTO distributorSeriesThemeDTO, AdminResponse currentAdmin);

    /**
     *
     * @param distributorSeriesThemeDTO
     * @param currentAdmin
     * @return
     */
    Response update(DistributorSeriesThemeDTO distributorSeriesThemeDTO, AdminResponse currentAdmin);

    PageInfo<SeriesThemePageCO> page(SeriesThemePageQry seriesThemePageRequest);

    Response detail(Integer id);

    Response listSimpleByCondition(DistributorSeriesThemeQry distributorSeriesThemeRequest);

    Response list(SeriesThemePageQry seriesThemePageRequest);

    Response listByDistributorId(Integer distributorId, Integer seriesNum, Integer pictureNum);


    Response delete(Integer id);

    /**
     * 校验分销商主题系列权限
     * @param distributorSimpleRelaQryList
     * @param seriesId
     */
    void checkDistributorSeriesTheme(List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, Integer seriesId);

    Response tabList(ThemeDTO themeDTO);

    PageInfo pictureList(ThemeDTO themeDTO);

    Integer pictureListCount(ThemeDTO themeDTO);

    Response detailTabList(ThemeDTO themeDTO);



    DistributorSeriesThemeDO getById(Integer seriesId);

    void updateDO(DistributorSeriesThemeDO seriesTheme);


}
