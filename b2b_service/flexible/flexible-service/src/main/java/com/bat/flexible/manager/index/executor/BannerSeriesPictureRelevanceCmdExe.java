package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.BannerSeriesPictureRelevanceDOMapper;
import com.bat.flexible.dao.index.dataobject.BannerSeriesPictureRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BannerSeriesPictureRelevanceCmdExe {

    @Autowired
    private BannerSeriesPictureRelevanceDOMapper bannerSeriesPictureRelevanceDOMapper;

    public void deleteById(Integer id) {
        bannerSeriesPictureRelevanceDOMapper.deleteByPrimaryKey(id);
    }

    public void create(BannerSeriesPictureRelevanceDO pictureRelevanceDO) {
        bannerSeriesPictureRelevanceDOMapper.insert(pictureRelevanceDO);
    }

    public void update(BannerSeriesPictureRelevanceDO pictureRelevanceDO) {
        bannerSeriesPictureRelevanceDOMapper.updateByPrimaryKey(pictureRelevanceDO);
    }

    public void deleteByBannerId(Integer bannerId) {
        bannerSeriesPictureRelevanceDOMapper.deleteByBannerId(bannerId);
    }
}
