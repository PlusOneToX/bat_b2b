package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureTemplateEditDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureTemplateEditDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PictureTemplateEditCmdExe {

    @Autowired
    private PictureTemplateEditDOMapper pictureTemplateEditDOMapper;

    public void update(PictureTemplateEditDO edit) {
        pictureTemplateEditDOMapper.updateByPrimaryKey(edit);
    }

    public void create(PictureTemplateEditDO edit) {
        pictureTemplateEditDOMapper.insert(edit);
    }

    public void delete(Integer id) {
        pictureTemplateEditDOMapper.deleteByPrimaryKey(id);
    }
}
