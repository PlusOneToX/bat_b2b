package com.bat.flexible.manager.distributor.cooperation;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.distributor.cooperation.FlexibleDistributorCooperationServiceI;
import com.bat.flexible.api.distributor.cooperation.dto.FlexibleDistributorCooperationCmd;
import com.bat.flexible.api.distributor.cooperation.dto.FlexibleDistributorCooperationQry;
import com.bat.flexible.dao.distributor.dataobject.FlexibleDistributorCooperationDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.distributor.cooperation.executor.FlexibleDistributorCooperationCmdExe;
import com.bat.flexible.manager.distributor.cooperation.executor.FlexibleDistributorCooperationQryExe;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FlexibleDistributorCooperationServiceImpl implements FlexibleDistributorCooperationServiceI {

    @Autowired
    private FlexibleDistributorCooperationQryExe flexibleDistributorCooperationQryExe;

    @Autowired
    private FlexibleDistributorCooperationCmdExe flexibleDistributorCooperationCmdExe;

    @Override
    @Transactional
    public Response create(FlexibleDistributorCooperationCmd flexibleDistributorCooperationCmd, AdminResponse currentAdmin) {
        FlexibleDistributorCooperationDO flexibleDistributorCooperationDO = BeanUtils.copy(flexibleDistributorCooperationCmd,FlexibleDistributorCooperationDO.class);
        setAdminMsg(flexibleDistributorCooperationDO,currentAdmin);
        flexibleDistributorCooperationDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        //设置排序号
        Integer max=flexibleDistributorCooperationQryExe.findMaxSequence();
        flexibleDistributorCooperationDO.setSequence(max++);
        flexibleDistributorCooperationCmdExe.create(flexibleDistributorCooperationDO);
        return Response.buildSuccess();
    }

    private void setAdminMsg(FlexibleDistributorCooperationDO flexibleDistributorCooperationDO, AdminResponse currentAdmin) {
        if(flexibleDistributorCooperationDO.getId() ==null){
            flexibleDistributorCooperationDO.setCreateTime(new Date());
            flexibleDistributorCooperationDO.setCreateUserId(currentAdmin.getId());
            flexibleDistributorCooperationDO.setCreateUserName(currentAdmin.getUserName());
        }
        flexibleDistributorCooperationDO.setUpdateTime(new Date());
        flexibleDistributorCooperationDO.setUpdateUserId(currentAdmin.getId());
        flexibleDistributorCooperationDO.setUpdateUserName(currentAdmin.getUserName());
    }

    @Override
    @Transactional
    public Response update(FlexibleDistributorCooperationCmd flexDistributorCooperationCmd, AdminResponse currentAdmin) {
        FlexibleDistributorCooperationDO cooperationDO = flexibleDistributorCooperationQryExe.getById(flexDistributorCooperationCmd.getId());
        cooperationDO.setCooperationType(flexDistributorCooperationCmd.getCooperationType());
        cooperationDO.setDefaultChoose(flexDistributorCooperationCmd.getDefaultChoose());
        cooperationDO.setOpenFlag(flexDistributorCooperationCmd.getOpenFlag());
        cooperationDO.setRemark(flexDistributorCooperationCmd.getRemark());
        setAdminMsg(cooperationDO,currentAdmin);
        flexibleDistributorCooperationCmdExe.update(cooperationDO);
        return Response.buildSuccess();
    }

    @Override
    public PageInfo<FlexibleDistributorCooperationCmd> page(FlexibleDistributorCooperationQry flexibleDistributorCooperationQry, AdminResponse currentAdmin) {
        PageHelper.startPage(flexibleDistributorCooperationQry.getPage(),flexibleDistributorCooperationQry.getSize());
        List<FlexibleDistributorCooperationCmd> cmdList = queryByCondtion(flexibleDistributorCooperationQry.getOpenFlag(),flexibleDistributorCooperationQry.getContent());
        return new PageInfo<>(cmdList);
    }

    private List<FlexibleDistributorCooperationCmd> queryByCondtion(Short openFlag, String content) {
        List<FlexibleDistributorCooperationDO> doList = flexibleDistributorCooperationQryExe.listByOpenFlagAndContent(openFlag, content);
        List<FlexibleDistributorCooperationCmd> cmdList = new ArrayList<>();
        if(doList !=null && doList.size()>0){
            doList.stream().forEach(flexibleDistributorCooperationDO -> {
                FlexibleDistributorCooperationCmd cooperationCmd = BeanUtils.copy(flexibleDistributorCooperationDO,FlexibleDistributorCooperationCmd.class);
                cmdList.add(cooperationCmd);
            });
        }
        return cmdList;
    }

    @Override
    public Response listUsable() {
        List<FlexibleDistributorCooperationCmd> cmdList = queryByCondtion(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES,null);
        return Response.of(cmdList);
    }

    @Override
    public Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin) {
        FlexibleDistributorCooperationDO cooperationDO = flexibleDistributorCooperationQryExe.getById(flexibleUpdateStatusDTO.getId());
        if(cooperationDO.getOpenFlag() - flexibleUpdateStatusDTO.getOpenFlag() ==0){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        cooperationDO.setOpenFlag(flexibleUpdateStatusDTO.getOpenFlag());
        setAdminMsg(cooperationDO,currentAdmin);
        flexibleDistributorCooperationCmdExe.update(cooperationDO);
        return Response.buildSuccess();
    }

    @Override
    public Response deleteById(Integer id, AdminResponse currentAdmin) {
        FlexibleDistributorCooperationDO cooperationDO = flexibleDistributorCooperationQryExe.getById(id);
        cooperationDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
        setAdminMsg(cooperationDO,currentAdmin);
        flexibleDistributorCooperationCmdExe.update(cooperationDO);
        return Response.buildSuccess();
    }
}
