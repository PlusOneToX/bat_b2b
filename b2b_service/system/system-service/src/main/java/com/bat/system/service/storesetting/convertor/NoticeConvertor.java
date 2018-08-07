package com.bat.system.service.storesetting.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.storesetting.dto.NoticeCreateCmd;
import com.bat.system.api.storesetting.dto.NoticeUpdateCmd;
import com.bat.system.api.storesetting.dto.data.NoticeDTO;
import com.bat.system.dao.storesetting.dataobject.NoticeDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:34
 */
public class NoticeConvertor {

    public static NoticeDO toNoticeDO(NoticeCreateCmd cmd) {
        NoticeDO noticeDO = new NoticeDO();
        BeanUtils.copyProperties(cmd, noticeDO);
        Date date = new Date();
        noticeDO.setCreateTime(date);
        noticeDO.setUpdateTime(date);
        if (noticeDO.getReleaseStatus() == 1) {
            noticeDO.setReleaseTime(date);
        }
        return noticeDO;
    }

    public static NoticeDO toNoticeDO(NoticeUpdateCmd cmd) {
        NoticeDO noticeDO = new NoticeDO();
        BeanUtils.copyProperties(cmd, noticeDO);
        return noticeDO;
    }

    public static NoticeDTO toNoticeDTO(NoticeDO noticeDO) {
        NoticeDTO noticeDTO = new NoticeDTO();
        BeanUtils.copyProperties(noticeDO, noticeDTO);
        return noticeDTO;
    }

    public static List<NoticeDTO> toNoticeDTOList(List<NoticeDO> noticeDOList) {
        return noticeDOList.stream().map(noticeDO -> {
            NoticeDTO noticeDTO = new NoticeDTO();
            BeanUtils.copyProperties(noticeDO, noticeDTO);
            return noticeDTO;
        }).collect(Collectors.toList());
    }
}
