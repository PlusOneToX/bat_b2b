package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.picture.PictureThemeDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureThemeDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureThemeQryExe {

    @Autowired
    private PictureThemeDOMapper pictureThemeDOMapper;

    public PictureThemeDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        PictureThemeDO pictureThemeDO = pictureThemeDOMapper.selectByPrimaryKey(id);
        if(pictureThemeDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        return pictureThemeDO;
    }

    public List<PictureThemeDO> listByOpenFlagAndContent(Short openFlag, String content) {
        return pictureThemeDOMapper.listByOpenFlagAndContent(openFlag,content);
    }
}
