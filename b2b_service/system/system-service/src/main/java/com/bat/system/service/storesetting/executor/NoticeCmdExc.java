package com.bat.system.service.storesetting.executor;

import java.util.Date;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.storesetting.dto.NoticeCreateCmd;
import com.bat.system.api.storesetting.dto.NoticeReleaseCmd;
import com.bat.system.api.storesetting.dto.NoticeUpdateCmd;
import com.bat.system.dao.storesetting.NoticeMapper;
import com.bat.system.dao.storesetting.dataobject.NoticeDO;
import com.bat.system.service.storesetting.convertor.NoticeConvertor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/29 11:49
 */
@Component
@Slf4j
public class NoticeCmdExc {

    @Resource
    private NoticeMapper noticeMapper;

    public boolean createNotice(NoticeCreateCmd cmd) {
        NoticeDO bannerDO = NoticeConvertor.toNoticeDO(cmd);
        noticeMapper.insert(bannerDO);
        return true;

    }

    public boolean updateNotice(NoticeUpdateCmd cmd) {
        NoticeDO noticeDO = noticeMapper.selectByPrimaryKey(cmd.getId());
        if (noticeDO == null) {
            throw SystemException.buildException(ErrorCode.B_NOTICE_ID_NOT_EXISTS);
        }
        NoticeDO noticeDO1 = NoticeConvertor.toNoticeDO(cmd);
        Date date = new Date();
        // 取消发布
        if (noticeDO1.getReleaseStatus() == 0 && noticeDO.getReleaseStatus() == 1) {
            noticeDO1.setCancelTime(date);
        } else if (noticeDO1.getReleaseStatus() == 1 && noticeDO.getReleaseStatus() == 0) {
            // 发布
            noticeDO1.setReleaseTime(date);
        }
        noticeDO1.setUpdateTime(date);
        noticeMapper.updateByPrimaryKeySelective(noticeDO1);
        return true;
    }

    public boolean releaseNotice(NoticeReleaseCmd cmd) {
        NoticeDO noticeDO = noticeMapper.selectByPrimaryKey(cmd.getId());
        if (noticeDO == null) {
            throw SystemException.buildException(ErrorCode.B_NOTICE_ID_NOT_EXISTS);
        }
        Date date = new Date();
        // 取消发布
        if (cmd.getReleaseStatus() == 0 && noticeDO.getReleaseStatus() == 1) {
            noticeDO.setCancelTime(date);
        } else if (cmd.getReleaseStatus() == 1 && noticeDO.getReleaseStatus() == 0) {
            // 发布
            noticeDO.setReleaseTime(date);
        }
        noticeDO.setReleaseStatus(cmd.getReleaseStatus());
        noticeDO.setUpdateTime(date);
        noticeMapper.updateByPrimaryKeySelective(noticeDO);
        return true;
    }

    public boolean deleteNoticeById(Integer id) {
        if (noticeMapper.selectByPrimaryKey(id) == null) {
            throw SystemException.buildException(ErrorCode.B_BANNER_ID_NOT_EXISTS);
        }
        noticeMapper.deleteByPrimaryKey(id);
        return true;
    }
}
