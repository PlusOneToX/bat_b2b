package com.bat.system.api.trainingdownloadcenter;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterCreateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterMenuQry;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterQry;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterUpdateCmd;
import com.bat.system.api.trainingdownloadcenter.dto.data.TrainingCenterDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 15:36
 */
public interface TrainingCenterService {
    /**
     * 通过id获取培训中心
     * 
     * @param id
     * @return
     */
    TrainingCenterDTO getTrainingCenterById(Integer id);

    /**
     * 根据父id获取培训中心
     * 
     * @param qry
     * @return
     */
    PageInfo<TrainingCenterDTO> listTrainingCenterByParentId(TrainingCenterQry qry);

    /**
     * 通过父id获取培训中心列表
     * 
     * @param qry
     * @return
     */
    List<TrainingCenterDTO> listTrainingCenterMenuByParentId(TrainingCenterMenuQry qry);

    /**
     * 创建培训中心
     * 
     * @param cmd
     * @return
     */
    boolean createTrainingCenter(TrainingCenterCreateCmd cmd);

    /**
     * 更新培训中心
     * 
     * @param cmd
     * @return
     */
    boolean updateTrainingCenter(TrainingCenterUpdateCmd cmd);

    /**
     * 删除培训中心
     * 
     * @param id
     * @return
     */
    boolean deleteTrainingCenter(Integer id);

    /**
     * 培训中心上移
     * 
     * @param id
     * @return
     */
    boolean sortTrainingCenterUp(Integer id);

    /**
     * 培训中心下移
     * 
     * @param id
     * @return
     */
    boolean sortTrainingCenterDown(Integer id);
}
