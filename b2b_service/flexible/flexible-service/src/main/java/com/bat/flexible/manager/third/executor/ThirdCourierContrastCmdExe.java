package com.bat.flexible.manager.third.executor;

import com.bat.flexible.dao.third.ThirdCourierContrastDOMapper;
import com.bat.flexible.dao.third.dataobject.ThirdCourierContrastDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThirdCourierContrastCmdExe {

    @Autowired
    private ThirdCourierContrastDOMapper thirdCourierContrastDOMapper;


    public void create(ThirdCourierContrastDO thirdCourierContrastDO) {
        thirdCourierContrastDOMapper.insert(thirdCourierContrastDO);
    }
}
