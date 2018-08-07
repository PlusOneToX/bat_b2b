package com.bat.distributor.service.group.executor;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.group.dto.GroupCmd;
import com.bat.distributor.api.group.dto.GroupId;
import com.bat.distributor.api.group.dto.GroupOpenCmd;
import com.bat.distributor.dao.group.GroupMapper;
import com.bat.distributor.dao.group.dataobject.GroupDO;
import com.bat.distributor.service.common.Constant;
import com.bat.distributor.service.common.MessageUtils;
import com.bat.distributor.service.group.convertor.GroupConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class GroupCmdExe {

    @Resource
    private GroupMapper mapper;

    /**
     * 创建分销商分组
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createGroup(GroupCmd cmd) {
        GroupDO groupDO = GroupConvertor.toGroupDo(cmd);
        mapper.insert(groupDO);
    }

    /**
     * 更新分销商分组
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGroup(GroupCmd cmd) {
        GroupDO groupDO = GroupConvertor.toGroupDo(cmd);
        mapper.updateByPrimaryKey(groupDO);
    }

    /**
     * 更新分销商分组状态
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void openGroup(GroupOpenCmd cmd) {
        if (cmd.getOpenFlag().equals(Constant.OPEN_NO)) {
            // 停用分销商类别前，确保分销商类别下的分销商都已转移或删除
            List<Integer> distributorIds = mapper.getGroupDistributorIds(cmd.getId());
            if (distributorIds.size() > 0) {
                throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_GROUP_DISTRIBUTORNOTNUL,
                        MessageUtils.get(ErrorCode.B_DISTRIBUTOR_GROUP_DISTRIBUTORNOTNUL)+distributorIds);
            }
        }
        mapper.openGroup(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 根据Id删除分销商分组
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteGroup(GroupId cmd) {
        GroupDO groupDO = mapper.selectByPrimaryKey(cmd.getId());
        if (groupDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_GROUP_NULL);
        }
        // 停用的分销商类别才允许删除
        if (!groupDO.getOpenFlag().equals(Constant.OPEN_NO)) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_GROUP_DELETE_OPEN_ERROR);
        }
        mapper.deleteByPrimaryKey(cmd.getId());
    }

}
