package com.bat.flexible.manager.burying.executor;

import com.bat.flexible.dao.burying.BuryingPointDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuryingPointQryExe {

    @Autowired
    private BuryingPointDOMapper buryingPointDOMapper;
}
