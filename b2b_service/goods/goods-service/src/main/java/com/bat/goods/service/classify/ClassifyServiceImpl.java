package com.bat.goods.service.classify;

// package by domain, not by duty

import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.api.classify.dto.*;
import com.bat.goods.service.classify.executor.ClassifyCmdExe;
import com.bat.goods.api.classify.dto.data.ClassifyRecommendDTO;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.classify.ClassifyServiceI;
import com.bat.goods.api.classify.dto.*;
import com.bat.goods.api.classify.dto.data.ClassifyDTO;
import com.bat.goods.service.classify.executor.ClassifyQryExe;

@Service
public class ClassifyServiceImpl implements ClassifyServiceI {

    @Resource
    private ClassifyCmdExe classifyCmdExe;

    @Resource
    private ClassifyQryExe classifyQryExe;

    @Override
    public void createClassify(ClassifyCmd cmd) {
        classifyCmdExe.createClassify(cmd);
    }

    @Override
    public void updateClassify(ClassifyCmd cmd) {
        classifyCmdExe.updateClassify(cmd);
    }

    @Override
    public void openClassify(ClassifyOpenCmd cmd) {
        classifyCmdExe.openClassify(cmd);
    }

    @Override
    public PageInfo<ClassifyDTO> listClassify(ClassifyListQry classifyListQry) {
        return classifyQryExe.executeList(classifyListQry);
    }

    @Override
    public List<ClassifyDTO> listSubClassify(ClassifySubListQry classifySubListQry) {
        return classifyQryExe.listSubClassify(classifySubListQry);
    }

    @Override
    public void deleteClassify(ClassifyId cmd) {
        classifyCmdExe.deleteClassify(cmd);
    }

    @Override
    public ClassifyDTO getClassify(ClassifyId classifyId) {
        return classifyQryExe.execute(classifyId);
    }

    @Override
    public ClassifyRecommendDTO recommendInfo() {
        return classifyQryExe.recommendInfo(null);
    }

    @Override
    public void updateRecommendInfo(ClassifyRecommendCmd cmd) {
        classifyCmdExe.updateRecommendInfo(cmd);
    }

}