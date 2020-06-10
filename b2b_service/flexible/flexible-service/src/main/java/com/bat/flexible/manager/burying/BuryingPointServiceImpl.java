package com.bat.flexible.manager.burying;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.burying.BuryingPointServiceI;
import com.bat.flexible.api.burying.dto.BuryingPointCmd;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.burying.executor.BuryingPointCmdExe;
import com.bat.flexible.dao.burying.dataobject.BuryingPointDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BuryingPointServiceImpl implements BuryingPointServiceI {

    @Autowired
    private BuryingPointCmdExe buryingPointCmdExe;


    @Override
    public Response create(BuryingPointCmd buryingPointCmd) {
        BuryingPointDO buryingPointDO = BeanUtils.copy(buryingPointCmd,BuryingPointDO.class);
        buryingPointDO.setCreateTime(new Date());
        buryingPointCmdExe.create(buryingPointDO);
        return Response.buildSuccess();
    }
}
