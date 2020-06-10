package com.bat.flexible.manager.index;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.index.BannerSeriesPictureRelevanceServiceI;
import com.bat.flexible.api.index.dto.page.UserBannerQry;
import com.bat.flexible.manager.common.constant.index.DistributorBannerConstants;
import com.bat.flexible.manager.index.executor.BannerSeriesPictureRelevanceCmdExe;
import com.bat.flexible.manager.index.executor.BannerSeriesPictureRelevanceQryExe;
import com.bat.flexible.dao.index.dataobject.BannerSeriesPictureRelevanceDO;
import com.bat.flexible.dao.index.dataobject.DistributorBannerDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BannerSeriesPictureRelevanceServiceImpl implements BannerSeriesPictureRelevanceServiceI {

    @Autowired
    private BannerSeriesPictureRelevanceCmdExe bannerSeriesPictureRelevanceCmdExe;

    @Autowired
    private BannerSeriesPictureRelevanceQryExe bannerSeriesPictureRelevanceQryExe;



    @Override
    public List<BannerSeriesPictureRelevanceDO> findByBannerId(Integer bannerId) {
        return bannerSeriesPictureRelevanceQryExe.findByBannerId(bannerId);
    }



    @Override
    public void create(BannerSeriesPictureRelevanceDO pictureRelevanceDO) {
        bannerSeriesPictureRelevanceCmdExe.create(pictureRelevanceDO);
    }

    @Override
    public void update(BannerSeriesPictureRelevanceDO pictureRelevanceDO) {
        bannerSeriesPictureRelevanceCmdExe.update(pictureRelevanceDO);
    }

    @Override
    public List<Integer> listPictureIdByBannerId(Integer bannerId) {
        return bannerSeriesPictureRelevanceQryExe.listPictureIdByBannerId(bannerId);
    }

    @Override
    public void deleteByBannerId(Integer bannerId) {
        bannerSeriesPictureRelevanceCmdExe.deleteByBannerId(bannerId);
    }

    @Override
    public PageInfo pagePictureByBannerId(UserBannerQry userBannerQry) {
        PageHelper.startPage(userBannerQry.getPage(),userBannerQry.getSize());
        List<CommonPicturePageCO> commonPicturePageCOList = bannerSeriesPictureRelevanceQryExe.listPictureCOByBannerIdAndSeriesId(userBannerQry.getBannerId(),
                userBannerQry.getSeriesId());
        return new PageInfo(commonPicturePageCOList);
    }

    @Override
    public void setBannerPictureRelevance(DistributorBannerDO banner, List<Integer> pictureIdList, AdminResponse currentAdmin, boolean isAdd) {
        if(banner.getType() - DistributorBannerConstants.typeSeriesTheme !=0){
            return;
        }

        if(!isAdd){
            List<BannerSeriesPictureRelevanceDO> bannerSeriesPictureRelaList =findByBannerId(banner.getId());
            for(int x=0;x<bannerSeriesPictureRelaList.size();x++){
                for(int y=0;y<pictureIdList.size();y++){
                    if(bannerSeriesPictureRelaList.get(x).getPictureId() - pictureIdList.get(y)==0){
                        //没有删除
                        bannerSeriesPictureRelaList.remove(x);
                        pictureIdList.remove(y);
                        x--;
                        y--;
                        break;
                    }
                }
            }
            //剩下就是已被删除的
            if(bannerSeriesPictureRelaList !=null && bannerSeriesPictureRelaList.size()>0){
                bannerSeriesPictureRelaList.stream().forEach(bannerSeriesPictureRela -> {
                    bannerSeriesPictureRelevanceCmdExe.deleteById(bannerSeriesPictureRela.getId());
                });
            }
        }
        if(pictureIdList !=null && pictureIdList.size()>0){
            pictureIdList.stream().forEach(pictureId -> {
                BannerSeriesPictureRelevanceDO pictureRela = new BannerSeriesPictureRelevanceDO();
                pictureRela.setPictureId(pictureId);
                pictureRela.setBannerId(banner.getId());
                pictureRela.setCreateTime(new Date());
                pictureRela.setCreateUserId(currentAdmin.getId());
                pictureRela.setCreateUserName(currentAdmin.getUserName());
                bannerSeriesPictureRelevanceCmdExe.create(pictureRela);
            });
        }
    }
}
