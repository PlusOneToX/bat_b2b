package com.bat.goods.service.classify.executor;

import static com.bat.goods.service.classify.executor.ErrorCode.*;
import static com.bat.goods.service.common.Constant.*;

import javax.annotation.Resource;

import com.bat.goods.api.classify.dto.ClassifyRecommendCmd;
import com.bat.goods.dao.classify.ClassifyBannerRelevanceMapper;
import com.bat.goods.dao.classify.dataobject.ClassifyBannerRelevanceDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.classify.dto.ClassifyCmd;
import com.bat.goods.api.classify.dto.ClassifyId;
import com.bat.goods.api.classify.dto.ClassifyOpenCmd;
import com.bat.goods.dao.classify.ClassifyMapper;
import com.bat.goods.dao.classify.dataobject.ClassifyDO;
import com.bat.goods.service.classify.convertor.ClassifyConvertor;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClassifyCmdExe {

    @Resource
    private ClassifyMapper classifyMapper;

    @Resource
    private ClassifyBannerRelevanceMapper classifyBannerRelevanceMapper;


    /**
     * 创建商品分类
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheInvalidate(name = "classify:", key = "#cmd.cacheKey")
    public void createClassify(ClassifyCmd cmd) {
        ClassifyDO classifyDO = ClassifyConvertor.toClassifyDo(cmd, OPERATION_TYPE_1);
        classifyMapper.createClassify(classifyDO);
        if (cmd.getBanners() != null || cmd.getBanners().size() > 0) {
            List<ClassifyBannerRelevanceDO> classifyBannerRelevanceDOS = new ArrayList<>();
            for (ClassifyCmd.Banner banner : cmd.getBanners()) {
                ClassifyBannerRelevanceDO classifyBannerRelevanceDO = new ClassifyBannerRelevanceDO();
                BeanUtils.copyProperties(banner, classifyBannerRelevanceDO);
                classifyBannerRelevanceDO.setClassifyId(classifyDO.getId());
                classifyBannerRelevanceDO.setRecommendFlag(classifyDO.getRecommendFlag());
                classifyBannerRelevanceDOS.add(classifyBannerRelevanceDO);
            }
            if(classifyBannerRelevanceDOS.size()>0) {
                classifyBannerRelevanceMapper.insertOrUpdateList(classifyBannerRelevanceDOS);
            }
        }

    }

    /**
     * 更新商品分类
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheInvalidate(name = "classify:", key = "#cmd.cacheKey")
    public void updateClassify(ClassifyCmd cmd) {
        ClassifyDO classifyDO = ClassifyConvertor.toClassifyDo(cmd, OPERATION_TYPE_2);
        classifyMapper.updateClassify(classifyDO);
        classifyBannerRelevanceMapper.deleteByClassifyId(classifyDO.getId());
        if (cmd.getBanners() != null || cmd.getBanners().size() > 0) {
            List<ClassifyBannerRelevanceDO> classifyBannerRelevanceDOS = new ArrayList<>();
            for (ClassifyCmd.Banner banner : cmd.getBanners()) {
                ClassifyBannerRelevanceDO classifyBannerRelevanceDO = new ClassifyBannerRelevanceDO();
                BeanUtils.copyProperties(banner, classifyBannerRelevanceDO);
                classifyBannerRelevanceDO.setClassifyId(classifyDO.getId());
                classifyBannerRelevanceDO.setRecommendFlag(classifyDO.getRecommendFlag());
                classifyBannerRelevanceDOS.add(classifyBannerRelevanceDO);
            }
            if(classifyBannerRelevanceDOS.size()>0){
                classifyBannerRelevanceMapper.insertOrUpdateList(classifyBannerRelevanceDOS);
            }
        }
    }

    /**
     * 更新商品分类状态
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheInvalidate(name = "classify:", key = "#cmd.cacheKey")
    public void openClassify(ClassifyOpenCmd cmd) {
        if (cmd.getOpenFlag().equals(OPEN_NO)) {
            // 停用商品分类前，确保品类下的商品都已转移或删除
            Integer count = classifyMapper.getClassifyGoodsCount(cmd.getId());
            if (count > 0) {
                throw GoodsException.buildException(B_CLASSIFY_GOODSNOTNULL);
            }
        }
        classifyMapper.openClassify(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 删除商品分类
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteClassify(ClassifyId cmd) {
        ClassifyDO classifyDO = classifyMapper.getById(cmd.getId());
        if (classifyDO == null) {
            throw GoodsException.buildException(B_CLASSIFY_NULL);
        }
        // 停用的商品分类才允许删除
        if (!classifyDO.getOpenFlag().equals(OPEN_NO)) {
            throw GoodsException.buildException(B_CLASSIFY_DELETE_OPEN_ERROR);
        }
        classifyMapper.deleteClassify(cmd.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheInvalidate(name = "classify:", key = "#cmd.cacheKey")
    public void updateRecommendInfo(ClassifyRecommendCmd cmd) {
        classifyMapper.closeAllRecommend();
        classifyBannerRelevanceMapper.deleteRecommend();
        if (cmd.getClassifyIds().size() > 0) {
            classifyMapper.updateRecommendByIds(cmd.getClassifyIds());
        }
        if (cmd.getClassifyBanners().size() > 0) {
            List<ClassifyBannerRelevanceDO> classifyBannerRelevanceDOS = new ArrayList<>();
            for (ClassifyRecommendCmd.ClassifyBanner classifyBanner : cmd.getClassifyBanners()) {
                ClassifyBannerRelevanceDO classifyBannerRelevanceDO = new ClassifyBannerRelevanceDO();
                BeanUtils.copyProperties(classifyBanner, classifyBannerRelevanceDO);
                classifyBannerRelevanceDO.setRecommendFlag(RECOMMEND_FLAG_1);
                classifyBannerRelevanceDOS.add(classifyBannerRelevanceDO);
            }
            classifyBannerRelevanceMapper.insertOrUpdateList(classifyBannerRelevanceDOS);
        }
    }
}
