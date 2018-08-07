package com.bat.distributor.api.group;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.group.dto.GroupCmd;
import com.bat.distributor.api.group.dto.GroupId;
import com.bat.distributor.api.group.dto.GroupListQry;
import com.bat.distributor.api.group.dto.GroupOpenCmd;
import com.bat.distributor.api.group.dto.data.GroupDTO;

public interface GroupServiceI {
    /**
     * 添加分销商分组
     * 
     * @param cmd
     * @return
     */
    public void createGroup(GroupCmd cmd);

    /**
     * 更新分销商分组
     * 
     * @param cmd
     * @return
     */
    public void updateGroup(GroupCmd cmd);

    /**
     * 更新分销商分组状态
     * 
     * @param cmd
     * @return
     */
    public void openGroup(GroupOpenCmd cmd);

    /**
     * 获取分销商分组列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<GroupDTO> listGroup(GroupListQry qry);

    /**
     * 根据ID删除分销商分组
     * 
     * @param cmd
     * @return
     */
    public void deleteGroup(GroupId cmd);

    /**
     * 根据分销商分组id获取详情
     * 
     * @param qry
     * @return
     */
    public GroupDTO getGroup(GroupId qry);

}
