package com.bat.system.service.storesetting.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.storesetting.dto.SectionQry;
import com.bat.system.api.storesetting.dto.data.SectionDTO;
import com.bat.system.dao.storesetting.SectionMapper;
import com.bat.system.dao.storesetting.dataobject.SectionDO;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.system.service.storesetting.convertor.SectionConvertor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class SectionQryExc {
    @Resource
    private SectionMapper sectionMapper;

    public SectionDTO getSectionById(Integer id) {
        SectionDO sectionDO = sectionMapper.selectByPrimaryKey(id);
        return sectionDO == null ? null : SectionConvertor.toSectionDTO(sectionDO);
    }

    public PageInfo<SectionDTO> listSection(SectionQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<SectionDO> sectionDOS = sectionMapper.listByParams(map);
        PageInfo pageInfo = new PageInfo(sectionDOS);
        List<SectionDTO> toRoleDTOList = SectionConvertor.toSectionDTOList(pageInfo.getList());
        pageInfo.setList(toRoleDTOList);
        return pageInfo;
    }
}
