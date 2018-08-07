package com.bat.system.service.trainingdownloadcenter.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterCreateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterUpdateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.data.DownloadCenterDTO;
import com.bat.system.dao.trainingdownloadcenter.dataobject.DownloadCenterDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 16:15
 */
public class DownloadCenterConvertor {
    public static DownloadCenterDTO toDownloadCenterDTO(DownloadCenterDO downloadCenterDO) {
        DownloadCenterDTO downloadCenterDTO = new DownloadCenterDTO();
        BeanUtils.copyProperties(downloadCenterDO, downloadCenterDTO);
        return downloadCenterDTO;
    }

    public static List<DownloadCenterDTO> toDownloadCenterDTOList(List<DownloadCenterDO> downloadCenterDOList) {
        return downloadCenterDOList.stream().map(downloadCenterDO -> {
            DownloadCenterDTO downloadCenterDTO = new DownloadCenterDTO();
            BeanUtils.copyProperties(downloadCenterDO, downloadCenterDTO);
            return downloadCenterDTO;
        }).collect(Collectors.toList());
    }

    public static DownloadCenterDO toDownloadCenterDO(DownloadCenterCreateCmd cmd) {
        DownloadCenterDO downloadCenterDO = new DownloadCenterDO();
        BeanUtils.copyProperties(cmd, downloadCenterDO);
        Date date = new Date();
        downloadCenterDO.setStatus((short)1);
        downloadCenterDO.setCreateTime(date);
        downloadCenterDO.setUpdateTime(date);
        return downloadCenterDO;
    }

    public static DownloadCenterDO toDownloadCenterDO(DownloadCenterUpdateCmd cmd) {
        DownloadCenterDO downloadCenterDO = new DownloadCenterDO();
        BeanUtils.copyProperties(cmd, downloadCenterDO);
        Date date = new Date();
        downloadCenterDO.setStatus((short)1);
        downloadCenterDO.setUpdateTime(date);
        return downloadCenterDO;
    }
}
