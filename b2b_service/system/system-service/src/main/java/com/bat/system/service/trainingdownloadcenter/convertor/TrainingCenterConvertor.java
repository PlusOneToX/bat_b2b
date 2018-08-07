package com.bat.system.service.trainingdownloadcenter.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterCreateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterUpdateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.data.TrainingCenterDTO;
import com.bat.system.dao.trainingdownloadcenter.dataobject.TrainingCenterDO;
import org.springframework.beans.BeanUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 16:15
 */
public class TrainingCenterConvertor {
    public static TrainingCenterDTO toTrainingCenterDTO(TrainingCenterDO trainingCenterDO) {
        TrainingCenterDTO trainingCenterDTO = new TrainingCenterDTO();
        BeanUtils.copyProperties(trainingCenterDO, trainingCenterDTO);
        return trainingCenterDTO;
    }

    public static List<TrainingCenterDTO> toTrainingCenterDTOList(List<TrainingCenterDO> trainingCenterDOList) {
        return trainingCenterDOList.stream().map(trainingCenterDO -> {
            TrainingCenterDTO trainingCenterDTO = new TrainingCenterDTO();
            BeanUtils.copyProperties(trainingCenterDO, trainingCenterDTO);
            return trainingCenterDTO;
        }).collect(Collectors.toList());
    }

    public static TrainingCenterDO toTrainingCenterDO(TrainingCenterCreateCmd cmd) {
        TrainingCenterDO trainingCenterDO = new TrainingCenterDO();
        BeanUtils.copyProperties(cmd, trainingCenterDO);
        Date date = new Date();
        trainingCenterDO.setStatus((short)1);
        trainingCenterDO.setCreateTime(date);
        trainingCenterDO.setUpdateTime(date);
        return trainingCenterDO;
    }

    public static TrainingCenterDO toTrainingCenterDO(TrainingCenterUpdateCmd cmd) {
        TrainingCenterDO trainingCenterDO = new TrainingCenterDO();
        BeanUtils.copyProperties(cmd, trainingCenterDO);
        Date date = new Date();
        trainingCenterDO.setStatus((short)1);
        trainingCenterDO.setUpdateTime(date);
        return trainingCenterDO;
    }
}
