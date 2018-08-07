package com.bat.distributor.service.group;

// package by domain, not by duty

import javax.annotation.Resource;

import com.bat.distributor.api.group.GroupServiceI;
import com.bat.distributor.api.group.dto.GroupCmd;
import com.bat.distributor.api.group.dto.GroupId;
import com.bat.distributor.api.group.dto.GroupListQry;
import com.bat.distributor.api.group.dto.GroupOpenCmd;
import com.bat.distributor.api.group.dto.data.GroupDTO;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.service.group.executor.GroupCmdExe;
import com.bat.distributor.service.group.executor.GroupQryExe;

@Service
public class GroupServiceImpl implements GroupServiceI {

    @Resource
    private GroupCmdExe cmdExe;

    @Resource
    private GroupQryExe qryExe;

    @Override
    public void createGroup(GroupCmd cmd) {
        cmdExe.createGroup(cmd);
    }

    @Override
    public void updateGroup(GroupCmd cmd) {
        cmdExe.updateGroup(cmd);
    }

    @Override
    public void openGroup(GroupOpenCmd cmd) {
        cmdExe.openGroup(cmd);
    }

    @Override
    public PageInfo<GroupDTO> listGroup(GroupListQry qry) {
        return qryExe.executeList(qry);
    }

    @Override
    public void deleteGroup(GroupId cmd) {
        cmdExe.deleteGroup(cmd);
    }

    @Override
    public GroupDTO getGroup(GroupId qry) {
        return qryExe.execute(qry);
    }

}