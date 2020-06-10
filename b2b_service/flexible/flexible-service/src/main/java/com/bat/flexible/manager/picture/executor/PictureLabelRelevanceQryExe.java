package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureLabelRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureLabelRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureLabelRelevanceQryExe {

    @Autowired
    private PictureLabelRelevanceDOMapper pictureLabelRelevanceDOMapper;



    public List<PictureLabelRelevanceDO> listByLabelIdAndPictureId(Integer labelId, Integer pictureId) {
        return pictureLabelRelevanceDOMapper.listByLabelIdAndPictureId(labelId,pictureId);
    }
}
