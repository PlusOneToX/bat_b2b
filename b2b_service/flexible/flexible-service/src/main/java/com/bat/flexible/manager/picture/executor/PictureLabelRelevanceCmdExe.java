package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureLabelRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureLabelRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureLabelRelevanceCmdExe {

    @Autowired
    private PictureLabelRelevanceDOMapper pictureLabelRelevanceDOMapper;

    public void create(PictureLabelRelevanceDO referencesDO) {
        pictureLabelRelevanceDOMapper.insert(referencesDO);
    }

    public void deleteById(Integer id) {
        pictureLabelRelevanceDOMapper.deleteByPrimaryKey(id);
    }

    public void update(PictureLabelRelevanceDO pictureLabelReferencesDO) {
        pictureLabelRelevanceDOMapper.updateByPrimaryKey(pictureLabelReferencesDO);
    }


    public void batchUpate(List<PictureLabelRelevanceDO> relevanceDOList) {
        pictureLabelRelevanceDOMapper.batchUpdate(relevanceDOList);
    }
}
