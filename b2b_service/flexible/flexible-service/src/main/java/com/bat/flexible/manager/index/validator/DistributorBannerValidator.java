package com.bat.flexible.manager.index.validator;

import com.bat.flexible.manager.common.constant.index.DistributorBannerConstants;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.index.dataobject.DistributorBannerDO;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;
import com.bat.flexible.manager.error.index.DistributorBannerErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class DistributorBannerValidator {

    /**
     * 参数校验
     * @param banner
     * @param pictureIdList
     */
    public static void validStatus(DistributorBannerDO banner, Set<Integer> pictureIdList) {
        if(banner.getEndTime() <System.currentTimeMillis()){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_ENDTIME_LESSTHEN_NOW);
        }
        if(banner.getEndTime() <= banner.getStartTime()){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_ENDTIME_LESSTHEN_STARTTIME);
        }
        if(banner.getSeriesId() ==null && banner.getType() - DistributorBannerConstants.typeSeriesTheme==0){
            throw new FlexibleCustomException(DistributorBannerErrorCode.INDEX_BANNER_SERIES_ID_NULL);
        }
        if((pictureIdList ==null || pictureIdList.size()==0) && banner.getType() - DistributorBannerConstants.typeSeriesTheme==0){
            throw new FlexibleCustomException(DistributorBannerErrorCode.INDEX_BANNER_SERIES_PICTURE_LIST_NULL);
        }
        if(StringUtils.isBlank(banner.getExternalLink()) && banner.getType() - DistributorBannerConstants.typeSeriesActivityLink==0){
            throw new FlexibleCustomException(DistributorBannerErrorCode.INDEX_BANNER_EXTERNAL_LINK_NULL);
        }
        banner.setStatus(DistributorBannerConstants.statusUnStart);
        if(banner.getStartTime() <System.currentTimeMillis()){
            banner.setStatus(DistributorBannerConstants.statusStarting);
        }
        if(banner.getType() -DistributorBannerConstants.typeSeriesTheme !=0){
            //设置主题系列为空
            banner.setSeriesId(null);
        }
    }
}
