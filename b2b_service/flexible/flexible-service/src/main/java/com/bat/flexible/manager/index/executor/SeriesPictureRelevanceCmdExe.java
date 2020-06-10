package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.SeriesPictureRelevanceDOMapper;
import com.bat.flexible.dao.index.dataobject.SeriesPictureRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeriesPictureRelevanceCmdExe {

    @Autowired
    private SeriesPictureRelevanceDOMapper seriesPictureRelevanceDOMapper;

    public void delete(Integer id) {
        seriesPictureRelevanceDOMapper.deleteByPrimaryKey(id);
    }

    public void create(SeriesPictureRelevanceDO seriesPictureRelevanceDO) {
        seriesPictureRelevanceDOMapper.insert(seriesPictureRelevanceDO);
    }

    public void update(SeriesPictureRelevanceDO pictureRela) {
        seriesPictureRelevanceDOMapper.updateByPrimaryKey(pictureRela);
    }

    public void deleteBySeriesId(Integer seriesId) {
        seriesPictureRelevanceDOMapper.deleteBySeriesId(seriesId);
    }
}
