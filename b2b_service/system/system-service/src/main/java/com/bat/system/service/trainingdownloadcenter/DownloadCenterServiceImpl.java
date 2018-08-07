package com.bat.system.service.trainingdownloadcenter;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.trainingdownloadcenter.DownloadCenterService;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterCreateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterMenuQry;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterQry;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterUpdateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.data.DownloadCenterDTO;
import com.bat.system.service.trainingdownloadcenter.executor.DownloadCenterCmdExc;
import com.bat.system.service.trainingdownloadcenter.executor.DownloadCenterQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 15:50
 */
@Service
@Slf4j
public class DownloadCenterServiceImpl implements DownloadCenterService {

    @Resource
    private DownloadCenterQryExc downLoadCenterQryExc;

    @Resource
    private DownloadCenterCmdExc downLoadCenterCmdExc;

    @Override
    public DownloadCenterDTO getDownloadCenterById(Integer id) {
        return downLoadCenterQryExc.getDownloadCenterById(id);
    }

    @Override
    public PageInfo<DownloadCenterDTO> listDownloadCenterByParentId(DownloadCenterQry qry) {
        return downLoadCenterQryExc.listDownloadCenterByParentId(qry);
    }

    @Override
    public List<DownloadCenterDTO> listDownloadCenterMenuByParentId(DownloadCenterMenuQry qry) {
        return downLoadCenterQryExc.listDownloadCenterMenuByParentId(qry);
    }

    @Override
    public boolean createDownloadCenter(DownloadCenterCreateCmd cmd) {
        return downLoadCenterCmdExc.createDownloadCenter(cmd);
    }

    @Override
    public boolean updateDownloadCenter(DownloadCenterUpdateCmd cmd) {
        return downLoadCenterCmdExc.updateDownloadCenter(cmd);
    }

    @Override
    public boolean deleteDownloadCenter(Integer id) {
        return downLoadCenterCmdExc.deleteDownloadCenter(id);
    }

    @Override
    public boolean sortDownloadCenterUp(Integer id) {
        return downLoadCenterCmdExc.sortDownloadCenterUp(id);
    }

    @Override
    public boolean sortDownloadCenterDown(Integer id) {
        return downLoadCenterCmdExc.sortDownloadCenterDown(id);
    }
}
