package com.bat.system.service.trainingdownloadcenter;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.trainingdownloadcenter.TrainingCenterService;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterCreateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterMenuQry;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterQry;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterUpdateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.data.TrainingCenterDTO;
import com.bat.system.service.trainingdownloadcenter.executor.TrainingCenterCmdExc;
import com.bat.system.service.trainingdownloadcenter.executor.TrainingCenterQryExc;
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
public class TrainingCenterServiceImpl implements TrainingCenterService {

    @Resource
    private TrainingCenterQryExc downLoadCenterQryExc;

    @Resource
    private TrainingCenterCmdExc downLoadCenterCmdExc;

    @Override
    public TrainingCenterDTO getTrainingCenterById(Integer id) {
        return downLoadCenterQryExc.getTrainingCenterById(id);
    }

    @Override
    public PageInfo<TrainingCenterDTO> listTrainingCenterByParentId(TrainingCenterQry qry) {
        return downLoadCenterQryExc.listTrainingCenterByParentId(qry);
    }

    @Override
    public List<TrainingCenterDTO> listTrainingCenterMenuByParentId(TrainingCenterMenuQry qry) {
        return downLoadCenterQryExc.listTrainingCenterMenuByParentId(qry);
    }

    @Override
    public boolean createTrainingCenter(TrainingCenterCreateCmd cmd) {
        return downLoadCenterCmdExc.createTrainingCenter(cmd);
    }

    @Override
    public boolean updateTrainingCenter(TrainingCenterUpdateCmd cmd) {
        return downLoadCenterCmdExc.updateTrainingCenter(cmd);
    }

    @Override
    public boolean deleteTrainingCenter(Integer id) {
        return downLoadCenterCmdExc.deleteTrainingCenter(id);
    }

    @Override
    public boolean sortTrainingCenterUp(Integer id) {
        return downLoadCenterCmdExc.sortTrainingCenterUp(id);
    }

    @Override
    public boolean sortTrainingCenterDown(Integer id) {
        return downLoadCenterCmdExc.sortTrainingCenterDown(id);
    }
}
