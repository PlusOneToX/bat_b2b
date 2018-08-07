package com.bat.distributor.service.group.executor;

import static com.bat.distributor.service.group.executor.ErrorCode.B_DISTRIBUTOR_GROUP_NULL;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.group.dto.GroupId;
import com.bat.distributor.api.group.dto.GroupListQry;
import com.bat.distributor.api.group.dto.data.GroupDTO;
import com.bat.distributor.dao.group.GroupMapper;
import com.bat.distributor.dao.group.dataobject.GroupDO;
import com.bat.distributor.service.group.convertor.GroupConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Component
public class GroupQryExe {

    @Resource
    private GroupMapper mapper;

    /**
     * 查询分销商分组列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<GroupDTO> executeList(GroupListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<GroupDO> tagDOList = mapper.listGroup(qryMap);
        PageInfo pageInfo = new PageInfo(tagDOList);
        List<GroupDTO> tagDTOList = GroupConvertor.toGroupDTOList(pageInfo.getList());
        pageInfo.setList(tagDTOList);
        return pageInfo;
    }

    /**
     * 根据分销商分组ID查询分销商分组详情
     * 
     * @param qry
     * @return
     */
    public GroupDTO execute(GroupId qry) {
        GroupDO groupDO = mapper.selectByPrimaryKey(qry.getId());
        if (groupDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_GROUP_NULL);
        }
        GroupDTO dto = GroupConvertor.toGroupDTO(groupDO);
        return dto;
    }
}
