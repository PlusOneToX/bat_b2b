package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureMateriaRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureMaterialRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PictureMaterialReferencesCmdExe {

    @Autowired
    private PictureMateriaRelevanceDOMapper pictureMaterialReferencesDOMapper;

    @Transactional
    public void update(PictureMaterialRelevanceDO pictureMaterialRelevanceDO) {
        pictureMaterialReferencesDOMapper.updateByPrimaryKey(pictureMaterialRelevanceDO);
    }

    @Transactional
    public void create(PictureMaterialRelevanceDO relevanceDO) {
        pictureMaterialReferencesDOMapper.insert(relevanceDO);
    }

    public void batchUpdate(List<PictureMaterialRelevanceDO> updateList) {
        pictureMaterialReferencesDOMapper.batchUpdate(updateList);
    }
}
