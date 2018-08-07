package com.bat.system.service.storesetting;

import javax.annotation.Resource;

import com.bat.system.api.storesetting.SectionService;
import com.bat.system.api.storesetting.dto.SectionCreateCmd;
import com.bat.system.api.storesetting.dto.SectionQry;
import com.bat.system.api.storesetting.dto.SectionReleaseCmd;
import com.bat.system.api.storesetting.dto.SectionUpdateCmd;
import com.bat.system.api.storesetting.dto.data.SectionDTO;
import com.bat.system.service.storesetting.executor.SectionCmdExc;
import com.bat.system.service.storesetting.executor.SectionQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 17:13
 */
@Service
public class SectionServiceImpl implements SectionService {

    @Resource
    private SectionQryExc sectionQryExc;

    @Resource
    private SectionCmdExc sectionCmdExc;

    @Override
    public PageInfo<SectionDTO> listSection(SectionQry qry) {
        return sectionQryExc.listSection(qry);
    }

    @Override
    public SectionDTO getSectionById(Integer id) {
        return sectionQryExc.getSectionById(id);
    }

    @Override
    public boolean createSection(SectionCreateCmd cmd) {
        return sectionCmdExc.createSection(cmd);
    }

    @Override
    public boolean updateSection(SectionUpdateCmd cmd) {
        return sectionCmdExc.updateSection(cmd);
    }

    @Override
    public boolean releaseSection(SectionReleaseCmd cmd) {
        return sectionCmdExc.releaseSection(cmd);
    }

    @Override
    public boolean deleteSectionById(Integer id) {
        return sectionCmdExc.deleteSectionById(id);
    }
}
