package com.bat.system.service.trainingdownloadcenter.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterMenuQry;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterQry;
import com.bat.system.api.trainingdownloadcenter.dto.data.DownloadCenterDTO;
import com.bat.system.dao.trainingdownloadcenter.DownloadCenterMapper;
import com.bat.system.dao.trainingdownloadcenter.dataobject.DownloadCenterDO;
import com.bat.system.service.trainingdownloadcenter.convertor.DownloadCenterConvertor;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class DownloadCenterQryExc {
    @Resource
    private DownloadCenterMapper downloadCenterMapper;

    public DownloadCenterDTO getDownloadCenterById(Integer id) {
        DownloadCenterDO downloadCenterDO = downloadCenterMapper.selectByPrimaryKey(id);
        return downloadCenterDO == null ? null : DownloadCenterConvertor.toDownloadCenterDTO(downloadCenterDO);
    }

    public PageInfo<DownloadCenterDTO> listDownloadCenterByParentId(DownloadCenterQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<DownloadCenterDO> downloadCenterDOS = downloadCenterMapper.listByParentIdRecursion(qry.getParentId());
        PageInfo pageInfo = new PageInfo(downloadCenterDOS);
        List<DownloadCenterDTO> toRoleDTOList = DownloadCenterConvertor.toDownloadCenterDTOList(pageInfo.getList());
        pageInfo.setList(toRoleDTOList);
        return pageInfo;
    }

    public List<DownloadCenterDTO> listDownloadCenterMenuByParentId(DownloadCenterMenuQry qry) {
        List<DownloadCenterDO> downloadCenterDOS = downloadCenterMapper.listByParentId(qry.getParentId());
        List<DownloadCenterDTO> downloadCenterDTOS = DownloadCenterConvertor.toDownloadCenterDTOList(downloadCenterDOS);
        return downloadCenterDTOS;
    }
}
