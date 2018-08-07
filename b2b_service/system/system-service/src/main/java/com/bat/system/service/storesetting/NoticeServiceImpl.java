package com.bat.system.service.storesetting;

import javax.annotation.Resource;

import com.bat.system.api.storesetting.NoticeService;
import com.bat.system.api.storesetting.dto.NoticeCreateCmd;
import com.bat.system.api.storesetting.dto.NoticeQry;
import com.bat.system.api.storesetting.dto.NoticeReleaseCmd;
import com.bat.system.api.storesetting.dto.NoticeUpdateCmd;
import com.bat.system.api.storesetting.dto.data.NoticeDTO;
import com.bat.system.service.storesetting.executor.NoticeCmdExc;
import com.bat.system.service.storesetting.executor.NoticeQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 17:13
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private NoticeQryExc noticeQryExc;;

    @Resource
    private NoticeCmdExc noticeCmdExc;

    @Override
    public PageInfo<NoticeDTO> listNotice(NoticeQry qry) {
        return noticeQryExc.listNotice(qry);
    }

    @Override
    public NoticeDTO getNoticeById(Integer id) {
        return noticeQryExc.getNoticeById(id);
    }

    @Override
    public boolean createNotice(NoticeCreateCmd cmd) {
        return noticeCmdExc.createNotice(cmd);
    }

    @Override
    public boolean updateNotice(NoticeUpdateCmd cmd) {
        return noticeCmdExc.updateNotice(cmd);
    }

    @Override
    public boolean releaseNotice(NoticeReleaseCmd cmd) {
        return noticeCmdExc.releaseNotice(cmd);
    }

    @Override
    public boolean deleteNoticeById(Integer id) {
        return noticeCmdExc.deleteNoticeById(id);
    }

}
