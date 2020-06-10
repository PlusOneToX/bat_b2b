package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureThemeDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureThemeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PictureThemeCmdExe {

    @Autowired
    private PictureThemeDOMapper pictureThemeDOMapper;

    public PictureThemeDO create(PictureThemeDO pictureTheme) {
        pictureThemeDOMapper.insert(pictureTheme);
        return pictureTheme;
    }

    public PictureThemeDO update(PictureThemeDO pictureTheme) {
        pictureThemeDOMapper.updateByPrimaryKey(pictureTheme);
        return pictureTheme;
    }

    public void delete(Integer id) {
        pictureThemeDOMapper.deleteByPrimaryKey(id);
    }
}
