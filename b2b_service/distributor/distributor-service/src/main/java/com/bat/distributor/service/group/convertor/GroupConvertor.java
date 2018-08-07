package com.bat.distributor.service.group.convertor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bat.distributor.api.group.dto.GroupCmd;
import com.bat.distributor.api.group.dto.data.GroupDTO;
import com.bat.distributor.dao.group.dataobject.GroupDO;
import org.springframework.beans.BeanUtils;

public class GroupConvertor {
    /**
     * 分销商分组数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static GroupDO toGroupDo(GroupCmd cmd) {
        GroupDO groupDO = new GroupDO();
        BeanUtils.copyProperties(cmd, groupDO);
        Date date = new Date(System.currentTimeMillis());
        groupDO.setCreateTime(date);
        groupDO.setUpdateTime(date);
        return groupDO;
    }

    /**
     * do列表转dto列表
     * 
     * @param doList
     * @return
     */
    public static List<GroupDTO> toGroupDTOList(List<GroupDO> doList) {
        List<GroupDTO> dtoList = new ArrayList<>();
        doList.forEach(groupDO -> {
            GroupDTO dto = new GroupDTO();
            BeanUtils.copyProperties(groupDO, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    /**
     * do转dto
     * 
     * @param groupDO
     * @return
     */
    public static GroupDTO toGroupDTO(GroupDO groupDO) {
        GroupDTO dto = new GroupDTO();
        BeanUtils.copyProperties(groupDO, dto);
        return dto;
    }

}
