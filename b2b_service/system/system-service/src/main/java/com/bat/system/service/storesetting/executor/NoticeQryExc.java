package com.bat.system.service.storesetting.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.storesetting.dto.NoticeQry;
import com.bat.system.api.storesetting.dto.data.NoticeDTO;
import com.bat.system.dao.storesetting.NoticeMapper;
import com.bat.system.dao.storesetting.dataobject.NoticeDO;
import com.bat.system.service.storesetting.convertor.NoticeConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/29 13:46
 */
@Component
@Slf4j
public class NoticeQryExc {

    @Resource
    private NoticeMapper noticeMapper;

    public NoticeDTO getNoticeById(Integer id) {
        NoticeDO noticeDO = noticeMapper.selectByPrimaryKey(id);
        return noticeDO == null ? null : NoticeConvertor.toNoticeDTO(noticeDO);
    }

    public PageInfo<NoticeDTO> listNotice(NoticeQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<NoticeDO> noticeDOS = noticeMapper.listByParams(map);
        PageInfo pageInfo = new PageInfo(noticeDOS);
        List<NoticeDTO> noticeDTOList = NoticeConvertor.toNoticeDTOList(pageInfo.getList());
        pageInfo.setList(noticeDTOList);
        return pageInfo;
    }

}
