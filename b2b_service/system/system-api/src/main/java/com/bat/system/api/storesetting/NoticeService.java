package com.bat.system.api.storesetting;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.storesetting.dto.NoticeCreateCmd;
import com.bat.system.api.storesetting.dto.NoticeQry;
import com.bat.system.api.storesetting.dto.NoticeReleaseCmd;
import com.bat.system.api.storesetting.dto.NoticeUpdateCmd;
import com.bat.system.api.storesetting.dto.data.NoticeDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/29 11:46
 */
public interface NoticeService {
    /**
     * 创建公告
     * 
     * @param cmd
     * @return
     */
    boolean createNotice(NoticeCreateCmd cmd);

    /**
     * 更新公告
     * 
     * @param cmd
     * @return
     */
    boolean updateNotice(NoticeUpdateCmd cmd);

    /**
     * 发布公告
     * 
     * @param cmd
     * @return
     */
    boolean releaseNotice(NoticeReleaseCmd cmd);

    /**
     * 通过id删除公告
     * 
     * @param id
     * @return
     */
    boolean deleteNoticeById(Integer id);

    /**
     * 通过id获取公告
     * 
     * @param id
     * @return
     */
    NoticeDTO getNoticeById(Integer id);

    /**
     * 获取公告列表
     * 
     * @param qry
     * @return
     */
    PageInfo<NoticeDTO> listNotice(NoticeQry qry);

}
