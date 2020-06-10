package com.bat.flexible.manager.index.executor;

import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.index.SeriesPictureRelevanceDOMapper;
import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import com.bat.flexible.dao.index.dataobject.SeriesPictureRelevanceDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeriesPictureRelevanceQryExe {

    @Autowired
    private SeriesPictureRelevanceDOMapper seriesPictureRelevanceDOMapper;

    public List<SeriesPictureRelevanceDO> listBySeriesId(Integer seriesId) {
        return seriesPictureRelevanceDOMapper.listBySeriesId(seriesId);
    }

    public SeriesPictureRelevanceDO findMaxSortNo(Integer seriesId) {
        return seriesPictureRelevanceDOMapper.findMaxSortNo(seriesId);
    }

    public SeriesPictureRelevanceDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        SeriesPictureRelevanceDO seriesPictureRelevanceDO = seriesPictureRelevanceDOMapper.selectByPrimaryKey(id);
        if(seriesPictureRelevanceDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        return seriesPictureRelevanceDO;
    }

    public List<CommonPicturePageCO> listCommonPictureCOBySeriesId(Integer seriesId) {
        return seriesPictureRelevanceDOMapper.listCommonPictureCOBySeriesId(seriesId);
    }

    public SeriesPictureRelevanceDO findEffect(Integer sortNo, Boolean upFlag, Integer seriesId) {
        return seriesPictureRelevanceDOMapper.findEffect(sortNo,upFlag,seriesId);
    }

    public List<IndexRecommendRelaCO> listCOBySeriesId(Integer seriesId) {
        return seriesPictureRelevanceDOMapper.listCOBySeriesId(seriesId);
    }
}
