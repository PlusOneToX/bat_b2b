package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureTemplateEditDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureTemplateEditDO;
import com.bat.flexible.dao.picture.co.PictureTemplateEditCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureTemplateEditQryExe {

    @Autowired
    private PictureTemplateEditDOMapper pictureTemplateEditDOMapper;

    public List<PictureTemplateEditDO> listPictureId(Integer pictureId) {
        return pictureTemplateEditDOMapper.listByPictureId(pictureId);
    }

    public List<PictureTemplateEditCmd> listSimpleByPictureId(Integer pictureId) {
        return pictureTemplateEditDOMapper.listSimpleByPictureId(pictureId);
    }
}
