package com.bat.system.service.trainingdownloadcenter.executor;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterCreateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterUpdateCmd;
import com.bat.system.dao.trainingdownloadcenter.DownloadCenterMapper;
import com.bat.system.dao.trainingdownloadcenter.dataobject.DownloadCenterDO;
import com.bat.system.service.trainingdownloadcenter.convertor.DownloadCenterConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 17:12
 */
@Component
@Slf4j
public class DownloadCenterCmdExc {
    @Resource
    private DownloadCenterMapper downloadCenterMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean createDownloadCenter(DownloadCenterCreateCmd cmd) {
        DownloadCenterDO downloadCenterDO = DownloadCenterConvertor.toDownloadCenterDO(cmd);
        downloadCenterDO.setSort(0);
        downloadCenterMapper.updateSort(downloadCenterDO.getParentId(), 0);
        downloadCenterMapper.insert(downloadCenterDO);
        return true;
    }

    public boolean updateDownloadCenter(DownloadCenterUpdateCmd cmd) {
        DownloadCenterDO downloadCenterDO = DownloadCenterConvertor.toDownloadCenterDO(cmd);
        downloadCenterMapper.updateByPrimaryKeySelective(downloadCenterDO);
        return true;
    }

    public boolean deleteDownloadCenter(Integer id) {
        downloadCenterMapper.deleteByPrimaryKey(id);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean sortDownloadCenterUp(Integer id) {
        DownloadCenterDO downloadCenterDO = downloadCenterMapper.selectByPrimaryKey(id);
        if (downloadCenterDO == null) {
            throw SystemException.buildException(ErrorCode.B_DOWNLOAD_CENTER_NOT_EXISTS);
        }
        Integer tmp = downloadCenterDO.getSort();
        DownloadCenterDO downloadCenterDO1 = downloadCenterMapper.getPreOne(downloadCenterDO.getSort());
        downloadCenterDO.setSort(downloadCenterDO1.getSort());
        downloadCenterMapper.updateByPrimaryKeySelective(downloadCenterDO);
        downloadCenterDO1.setSort(tmp);
        downloadCenterMapper.updateByPrimaryKeySelective(downloadCenterDO1);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean sortDownloadCenterDown(Integer id) {
        DownloadCenterDO downloadCenterDO = downloadCenterMapper.selectByPrimaryKey(id);
        if (downloadCenterDO == null) {
            throw SystemException.buildException(ErrorCode.B_DOWNLOAD_CENTER_NOT_EXISTS);
        }
        Integer tmp = downloadCenterDO.getSort();
        DownloadCenterDO downloadCenterDO1 = downloadCenterMapper.getNextOne(downloadCenterDO.getSort());
        downloadCenterDO.setSort(downloadCenterDO1.getSort());
        downloadCenterMapper.updateByPrimaryKeySelective(downloadCenterDO);
        downloadCenterDO1.setSort(tmp);
        downloadCenterMapper.updateByPrimaryKeySelective(downloadCenterDO1);
        return true;
    }
}
