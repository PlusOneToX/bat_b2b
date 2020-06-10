package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.BannerSeriesPictureRelevanceDOMapper;
import com.bat.flexible.dao.index.dataobject.BannerSeriesPictureRelevanceDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BannerSeriesPictureRelevanceQryExe {

    @Autowired
    private BannerSeriesPictureRelevanceDOMapper bannerSeriesPictureRelevanceDOMapper;

    public List<BannerSeriesPictureRelevanceDO> findByBannerId(Integer bannerId) {
        return bannerSeriesPictureRelevanceDOMapper.listByBannerId(bannerId);
    }

    public List<Integer> listPictureIdByBannerId(Integer bannerId) {
        return bannerSeriesPictureRelevanceDOMapper.listPictureIdByBannerId(bannerId);
    }

    public List<CommonPicturePageCO> listPictureCOByBannerIdAndSeriesId(Integer bannerId, Integer seriesId) {
        return bannerSeriesPictureRelevanceDOMapper.listPictureCOByBannerIdAndSeriesId(bannerId,seriesId);
    }
}
