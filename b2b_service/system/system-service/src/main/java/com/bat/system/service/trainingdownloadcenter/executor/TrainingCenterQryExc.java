package com.bat.system.service.trainingdownloadcenter.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterMenuQry;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterQry;
import com.bat.system.api.trainingdownloadcenter.dto.data.TrainingCenterDTO;
import com.bat.system.dao.trainingdownloadcenter.TrainingCenterMapper;
import com.bat.system.dao.trainingdownloadcenter.dataobject.TrainingCenterDO;
import com.bat.system.service.trainingdownloadcenter.convertor.TrainingCenterConvertor;
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
public class TrainingCenterQryExc {
    @Resource
    private TrainingCenterMapper trainingCenterMapper;

    public TrainingCenterDTO getTrainingCenterById(Integer id) {
        TrainingCenterDO trainingCenterDO = trainingCenterMapper.selectByPrimaryKey(id);
        return trainingCenterDO == null ? null : TrainingCenterConvertor.toTrainingCenterDTO(trainingCenterDO);
    }

    public PageInfo<TrainingCenterDTO> listTrainingCenterByParentId(TrainingCenterQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<TrainingCenterDO> trainingCenterDOS = trainingCenterMapper.listByParentIdRecursion(qry.getParentId());
        PageInfo pageInfo = new PageInfo(trainingCenterDOS);
        List<TrainingCenterDTO> toRoleDTOList = TrainingCenterConvertor.toTrainingCenterDTOList(pageInfo.getList());
        pageInfo.setList(toRoleDTOList);
        return pageInfo;
    }

    public List<TrainingCenterDTO> listTrainingCenterMenuByParentId(TrainingCenterMenuQry qry) {
        List<TrainingCenterDO> trainingCenterDOS = trainingCenterMapper.listByParentId(qry.getParentId());
        List<TrainingCenterDTO> trainingCenterDTOS = TrainingCenterConvertor.toTrainingCenterDTOList(trainingCenterDOS);
        return trainingCenterDTOS;
    }
}
