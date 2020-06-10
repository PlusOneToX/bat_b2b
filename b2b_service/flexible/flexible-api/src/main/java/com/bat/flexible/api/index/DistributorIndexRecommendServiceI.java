package com.bat.flexible.api.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.index.dto.page.IndexRecommendPageQry;
import com.bat.flexible.api.index.dto.recommend.DistributorPictureIdListDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;

public interface DistributorIndexRecommendServiceI {
    Response add(DistributorPictureIdListDTO distributorPictureIdListDTO, AdminResponse currentAdmin);

    Response update(DistributorPictureIdListDTO distributorPictureIdListDTO, AdminResponse currentAdmin);


    Response detail(Integer id);



  //  List<DistributorInfo> listDistributorIndexRecommendAndSeriesThemeByPictureId(Long pictureId);

    PageInfo<CommonPicturePageCO> pageByDistributorIdNew(IndexRecommendPageQry pageRequest);

    DistributorIndexRecommendDO getById(Integer indexRecommendId);

    void update(DistributorIndexRecommendDO distributorIndexRecommend);

    /**
     * 删除
     * @param id
     */
    void delete(Integer id);
}
