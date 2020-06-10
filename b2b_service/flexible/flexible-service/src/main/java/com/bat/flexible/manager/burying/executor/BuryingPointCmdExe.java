package com.bat.flexible.manager.burying.executor;

import com.bat.flexible.dao.burying.BuryingPointDOMapper;
import com.bat.flexible.dao.burying.dataobject.BuryingPointDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuryingPointCmdExe {

    @Autowired
    private BuryingPointDOMapper buryingPointDOMapper;

    public void create(BuryingPointDO buryingPointDO) {
        buryingPointDOMapper.insert(buryingPointDO);
    }
}
