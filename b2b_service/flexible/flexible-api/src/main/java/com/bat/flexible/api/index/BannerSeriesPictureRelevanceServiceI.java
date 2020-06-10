package com.bat.flexible.api.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.index.dto.page.UserBannerQry;
import com.bat.flexible.dao.index.dataobject.BannerSeriesPictureRelevanceDO;
import com.bat.flexible.dao.index.dataobject.DistributorBannerDO;

import java.util.List;

public interface BannerSeriesPictureRelevanceServiceI {
    List<BannerSeriesPictureRelevanceDO> findByBannerId(Integer bannerId);



    void create(BannerSeriesPictureRelevanceDO pictureRela);

    void update(BannerSeriesPictureRelevanceDO pictureRela);

    List<Integer> listPictureIdByBannerId(Integer bannerId);

    void deleteByBannerId(Integer bannerId);

    PageInfo pagePictureByBannerId(UserBannerQry userBannerQry);

    void setBannerPictureRelevance(DistributorBannerDO banner, List<Integer> pictureIdList, AdminResponse currentAdmin, boolean isAdd);
}
