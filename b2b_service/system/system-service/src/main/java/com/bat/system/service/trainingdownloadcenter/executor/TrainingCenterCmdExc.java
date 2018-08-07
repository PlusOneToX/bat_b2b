package com.bat.system.service.trainingdownloadcenter.executor;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterCreateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterUpdateCmd;
import com.bat.system.dao.trainingdownloadcenter.TrainingCenterMapper;
import com.bat.system.dao.trainingdownloadcenter.dataobject.TrainingCenterDO;
import com.bat.system.service.trainingdownloadcenter.convertor.TrainingCenterConvertor;
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
public class TrainingCenterCmdExc {
    @Resource
    private TrainingCenterMapper trainingCenterMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean createTrainingCenter(TrainingCenterCreateCmd cmd) {
        TrainingCenterDO trainingCenterDO = TrainingCenterConvertor.toTrainingCenterDO(cmd);
        trainingCenterDO.setSort(0);
        trainingCenterMapper.updateSort(trainingCenterDO.getParentId(), 0);
        trainingCenterMapper.insert(trainingCenterDO);
        return true;
    }

    public boolean updateTrainingCenter(TrainingCenterUpdateCmd cmd) {
        TrainingCenterDO trainingCenterDO = TrainingCenterConvertor.toTrainingCenterDO(cmd);
        trainingCenterMapper.updateByPrimaryKeySelective(trainingCenterDO);
        return true;
    }

    public boolean deleteTrainingCenter(Integer id) {
        trainingCenterMapper.deleteByPrimaryKey(id);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean sortTrainingCenterUp(Integer id) {
        TrainingCenterDO trainingCenterDO = trainingCenterMapper.selectByPrimaryKey(id);
        if (trainingCenterDO == null) {
            throw SystemException.buildException(ErrorCode.B_DOWNLOAD_CENTER_NOT_EXISTS);
        }
        Integer tmp = trainingCenterDO.getSort();
        TrainingCenterDO trainingCenterDO1 = trainingCenterMapper.getPreOne(trainingCenterDO.getSort());
        trainingCenterDO.setSort(trainingCenterDO1.getSort());
        trainingCenterMapper.updateByPrimaryKeySelective(trainingCenterDO);
        trainingCenterDO1.setSort(tmp);
        trainingCenterMapper.updateByPrimaryKeySelective(trainingCenterDO1);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean sortTrainingCenterDown(Integer id) {
        TrainingCenterDO trainingCenterDO = trainingCenterMapper.selectByPrimaryKey(id);
        if (trainingCenterDO == null) {
            throw SystemException.buildException(ErrorCode.B_DOWNLOAD_CENTER_NOT_EXISTS);
        }
        Integer tmp = trainingCenterDO.getSort();
        TrainingCenterDO trainingCenterDO1 = trainingCenterMapper.getNextOne(trainingCenterDO.getSort());
        trainingCenterDO.setSort(trainingCenterDO1.getSort());
        trainingCenterMapper.updateByPrimaryKeySelective(trainingCenterDO);
        trainingCenterDO1.setSort(tmp);
        trainingCenterMapper.updateByPrimaryKeySelective(trainingCenterDO1);
        return true;
    }
}
