package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PictureCmdExe {

    @Autowired
    private PictureDOMapper pictureDOMapper;

    public void create(PictureDO pictureDO) {
        pictureDOMapper.insert(pictureDO);
    }

    public void update(PictureDO pictureDO) {
        pictureDOMapper.updateByPrimaryKey(pictureDO);
    }


    public void updateSequenceAddByCategoryId(Integer categoryId, Integer sequenceAdd, Integer sequenceStart, Integer sequenceEnd) {
        pictureDOMapper.updateSequenceAddByCategoryId(categoryId,sequenceAdd,sequenceStart,sequenceEnd);
    }
}
