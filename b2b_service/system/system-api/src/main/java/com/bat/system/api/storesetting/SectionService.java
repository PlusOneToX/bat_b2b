package com.bat.system.api.storesetting;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.storesetting.dto.SectionCreateCmd;
import com.bat.system.api.storesetting.dto.SectionQry;
import com.bat.system.api.storesetting.dto.SectionReleaseCmd;
import com.bat.system.api.storesetting.dto.SectionUpdateCmd;
import com.bat.system.api.storesetting.dto.data.SectionDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/30 15:01
 */
public interface SectionService {
    /**
     * 创建板块
     * 
     * @param cmd
     * @return
     */
    boolean createSection(SectionCreateCmd cmd);

    /**
     * 更新板块
     * 
     * @param cmd
     * @return
     */
    boolean updateSection(SectionUpdateCmd cmd);

    /**
     * 发布板块
     * 
     * @param cmd
     * @return
     */
    boolean releaseSection(SectionReleaseCmd cmd);

    /**
     * 通过id删除板块
     * 
     * @param id
     * @return
     */
    boolean deleteSectionById(Integer id);

    /**
     * 通过id获取板块
     * 
     * @param id
     * @return
     */
    SectionDTO getSectionById(Integer id);

    /**
     * 获取板块列表
     * 
     * @param qry
     * @return
     */
    PageInfo<SectionDTO> listSection(SectionQry qry);

}
