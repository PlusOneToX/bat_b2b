package com.bat.system.api.trainingdownloadcenter;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterCreateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterMenuQry;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterQry;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterUpdateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.data.DownloadCenterDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 15:36
 */
public interface DownloadCenterService {
    /**
     * 通过id获取下载中心
     * 
     * @param id
     * @return
     */
    DownloadCenterDTO getDownloadCenterById(Integer id);

    /**
     * 通过父id获取下载中心列表
     * 
     * @param qry
     * @return
     */
    PageInfo<DownloadCenterDTO> listDownloadCenterByParentId(DownloadCenterQry qry);

    /**
     * 通过父id获取下载中心列表菜单
     * 
     * @param qry
     * @return
     */
    List<DownloadCenterDTO> listDownloadCenterMenuByParentId(DownloadCenterMenuQry qry);

    /**
     * 创建下载中心
     * 
     * @param cmd
     * @return
     */
    boolean createDownloadCenter(DownloadCenterCreateCmd cmd);

    /**
     * 更新下载中心
     * 
     * @param cmd
     * @return
     */
    boolean updateDownloadCenter(DownloadCenterUpdateCmd cmd);

    /**
     * 删除下载中心
     * 
     * @param id
     * @return
     */
    boolean deleteDownloadCenter(Integer id);

    /**
     * 下载中心上移
     * 
     * @param id
     * @return
     */
    boolean sortDownloadCenterUp(Integer id);

    /**
     * 下载中心下移
     * 
     * @param id
     * @return
     */
    boolean sortDownloadCenterDown(Integer id);
}
