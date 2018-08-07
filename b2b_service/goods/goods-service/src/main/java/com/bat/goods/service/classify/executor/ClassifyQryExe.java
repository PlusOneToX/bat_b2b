package com.bat.goods.service.classify.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.goods.service.classify.convertor.ClassifyConvertor;
import com.bat.goods.api.classify.dto.data.ClassifyRecommendDTO;
import com.bat.goods.api.user.dto.data.UserClassifyBannerDTO;
import com.bat.goods.dao.classify.ClassifyBannerRelevanceMapper;
import com.bat.goods.dao.classify.dataobject.ClassifyBannerRelevanceDO;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.classify.dto.ClassifyId;
import com.bat.goods.api.classify.dto.ClassifyListQry;
import com.bat.goods.api.classify.dto.ClassifySubListQry;
import com.bat.goods.api.classify.dto.data.ClassifyDTO;
import com.bat.goods.dao.classify.ClassifyMapper;
import com.bat.goods.dao.classify.dataobject.ClassifyDO;

@Component
public class ClassifyQryExe {

    @Resource
    private ClassifyMapper classifyMapper;

    @Resource
    private ClassifyBannerRelevanceMapper classifyBannerRelevanceMapper;

    /**
     * 查询商品分类列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<ClassifyDTO> executeList(ClassifyListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<ClassifyDO> classifyDOList = classifyMapper.listClassify(qryMap);
        PageInfo pageInfo = new PageInfo(classifyDOList);
        List<ClassifyDTO> classifyDTOList = ClassifyConvertor.toClassifyDTOList(pageInfo.getList());
        pageInfo.setList(classifyDTOList);
        if(classifyDTOList.size()==0){
            return pageInfo;
        }
        // 组装子分类
        List<Integer> classifyIds = classifyDOList.stream().map(ClassifyDO::getId).collect(Collectors.toList());
        Map<String, Object> subQryMap = new HashMap();
        subQryMap.putAll(qryMap);
        subQryMap.put("classifyIds", classifyIds);
        List<ClassifyDO> subClassifyDOList = classifyMapper.listSubClassifyByClassifyIds(subQryMap);
        if (!CollectionUtils.isEmpty(subClassifyDOList)) {
            Map<Integer, ClassifyDTO> classifyDTOMap =
                classifyDTOList.stream().collect(Collectors.toMap(ClassifyDTO::getId, classifyDTO -> classifyDTO));
            List<ClassifyDTO> subClassifyDTOList = ClassifyConvertor.toClassifyDTOList(subClassifyDOList);
            subClassifyDTOList.forEach(subClassifyDTO -> {
                ClassifyDTO classifyDTO = classifyDTOMap.get(subClassifyDTO.getParentId());
                List<ClassifyDTO> subClassifies = classifyDTO.getSubClassifies();
                if (subClassifies == null) {
                    subClassifies = new ArrayList<>();
                    classifyDTO.setSubClassifies(subClassifies);
                }
                subClassifies.add(subClassifyDTO);
            });
        }
        return pageInfo;
    }

    /**
     * 根据商品分类ID查询商品分类详情
     * 
     * @param classifyId
     * @return
     */
    public ClassifyDTO execute(ClassifyId classifyId) {
        ClassifyDO classifyDO = classifyMapper.getById(classifyId.getId());
        if (classifyDO == null) {
            throw GoodsException.buildException(ErrorCode.B_CLASSIFY_NULL);
        }
        ClassifyDTO dto = ClassifyConvertor.toClassifyDTO(classifyDO);
        // 组装子分类
        List<Integer> classifyIds = new ArrayList<>();
        classifyIds.add(classifyDO.getId());
                //增加banner图数据
        List<ClassifyBannerRelevanceDO> classifyBannerRelevanceDOS = classifyBannerRelevanceMapper.listByClassifyIds(classifyIds);
        List<UserClassifyBannerDTO> userClassifyBannerDTOS = new ArrayList<>();
        for (ClassifyBannerRelevanceDO classifyBannerRelevanceDO : classifyBannerRelevanceDOS) {
            UserClassifyBannerDTO userClassifyBannerDTO = new UserClassifyBannerDTO();
            BeanUtils.copyProperties(classifyBannerRelevanceDO, userClassifyBannerDTO);
            userClassifyBannerDTOS.add(userClassifyBannerDTO);
        }
        dto.setBanners(userClassifyBannerDTOS);
        return dto;
    }

    /**
     * 根据商品分类ID查询子分类列表
     * 
     * @param qry
     * @return
     */
    public List<ClassifyDTO> listSubClassify(ClassifySubListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        List<ClassifyDO> classifyDOList = classifyMapper.listSubClassify(qryMap);
        List<ClassifyDTO> classifyDTOList = ClassifyConvertor.toClassifyDTOList(classifyDOList);
        return classifyDTOList;
    }

    public ClassifyRecommendDTO recommendInfo(Short openFlag) {
        ClassifyRecommendDTO classifyRecommendDTO = new ClassifyRecommendDTO();
        List<ClassifyDO> classifyDOS = classifyMapper.listRecommend(openFlag);
        List<ClassifyRecommendDTO.Classify> classifies = new ArrayList<>();
        for (ClassifyDO classifyDO : classifyDOS) {
            ClassifyRecommendDTO.Classify classify = new ClassifyRecommendDTO.Classify();
            BeanUtils.copyProperties(classifyDO, classify);
            classifies.add(classify);
        }
        List<ClassifyBannerRelevanceDO> classifyBannerRelevanceDOS = classifyBannerRelevanceMapper.listRecommend();
        List<ClassifyRecommendDTO.ClassifyBanner> classifyBanners = new ArrayList<>();
        for (ClassifyBannerRelevanceDO classifyBannerRelevanceDO : classifyBannerRelevanceDOS) {
            ClassifyRecommendDTO.ClassifyBanner classifyBanner = new ClassifyRecommendDTO.ClassifyBanner();
            BeanUtils.copyProperties(classifyBannerRelevanceDO, classifyBanner);
            classifyBanners.add(classifyBanner);
        }
        classifyRecommendDTO.setClassifies(classifies);
        classifyRecommendDTO.setClassifyBanners(classifyBanners);
        return classifyRecommendDTO;
    }
}
