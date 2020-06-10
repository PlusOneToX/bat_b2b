package com.bat.flexible.manager.label.executor;

import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.dao.label.LabelDOMapper;
import com.bat.flexible.dao.label.dataobject.LabelDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LabelCmdExe {

    @Autowired
    private LabelDOMapper labelDOMapper;

    public void create(LabelDO labelDO) {
        if(labelDO.getDelFlag() ==null){
            labelDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        }
        labelDOMapper.insert(labelDO);
    }

    public void update(LabelDO labelDO) {
        labelDOMapper.updateByPrimaryKey(labelDO);
    }
}
