package com.bat.promotion.service.groupseckill;

import java.util.List;

import javax.annotation.Resource;

import com.bat.promotion.api.groupseckill.dto.*;
import com.bat.promotion.service.groupseckill.executor.GroupSeckillCmdExe;
import com.bat.promotion.service.groupseckill.executor.GroupSeckillQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.groupseckill.GroupSeckillServiceI;
import com.bat.promotion.api.groupseckill.dto.*;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillDTO;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillListDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/21 14:33
 */
@Service
public class GroupSeckillServiceImpl implements GroupSeckillServiceI {

    @Resource
    private GroupSeckillCmdExe cmdExe;
    @Resource
    private GroupSeckillQryExe qryExe;

    @Override
    public PageInfo<GroupSeckillListDTO> listGroupSeckill(GroupSeckillListQry qry) {
        return qryExe.listGroupSeckill(qry);
    }

    @Override
    public void createGroupSeckill(GroupSeckillCmd cmd, String userId, String userName) {
        cmdExe.createGroupSeckill(cmd, userId, userName);
    }

    @Override
    public void updateGroupSeckill(GroupSeckillCmd cmd, String userId, String userName) {
        cmdExe.updateGroupSeckill(cmd, userId, userName);
    }

    @Override
    public GroupSeckillDTO getGroupSeckill(BaseId qry) {
        return qryExe.getGroupSeckill(qry);
    }

    @Override
    public void deleteGroupSeckill(BaseId cmd) {
        cmdExe.deleteGroupSeckill(cmd);
    }

    @Override
    public void updateGroupSeckillStatus(GroupSeckillStatusCmd cmd) {
        cmdExe.updateGroupSeckillStatus(cmd);
    }

    @Override
    public void updateGroupSeckillSort(List<GroupSeckillSortCmd> cmds) {
        cmdExe.updateGroupSeckillSort(cmds);
    }

    @Override
    public void updateGroupSeckillGoodsSort(List<GroupSeckillGoodsSortCmd> cmds) {
        cmdExe.updateGroupSeckillGoodsSort(cmds);
    }
}
