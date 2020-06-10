package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureCategoryThemeRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryThemeRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureCategoryThemeRelevanceCmdExe {

    @Autowired
    private PictureCategoryThemeRelevanceDOMapper pictureCategoryThemeRelevanceDOMapper;

    public void deleteById(Integer id) {
        pictureCategoryThemeRelevanceDOMapper.deleteById(id);
    }

    public void saveList(List<PictureCategoryThemeRelevanceDO> pictureCategoryThemeRelas) {
        for(int x=0;x<pictureCategoryThemeRelas.size();x++){
            PictureCategoryThemeRelevanceDO pictureCategoryThemeRelevanceDO = pictureCategoryThemeRelas.get(x);
            pictureCategoryThemeRelevanceDO.setSequence(x+1);
            pictureCategoryThemeRelevanceDOMapper.insert(pictureCategoryThemeRelevanceDO);
        }
    }

    public void create(PictureCategoryThemeRelevanceDO pictureCategoryThemeRelevanceDO) {
        pictureCategoryThemeRelevanceDOMapper.insert(pictureCategoryThemeRelevanceDO);
    }
}
