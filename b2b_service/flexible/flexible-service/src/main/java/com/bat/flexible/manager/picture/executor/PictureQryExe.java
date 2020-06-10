package com.bat.flexible.manager.picture.executor;

import com.google.common.collect.Lists;
import com.bat.flexible.api.picture.*;
import com.bat.flexible.api.picture.dto.PictureTreeQry;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.dao.picture.co.*;
import com.bat.flexible.dao.picture.dataobject.*;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.picture.convertor.PictureConvertor;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.flexible.api.distributor.exclude.DistributorPictureExcludeServiceI;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.api.picture.*;
import com.bat.flexible.dao.distributor.dataobject.DistributorPictureExcludeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangePictureRelevanceDO;
import com.bat.flexible.dao.picture.PictureDOMapper;
import com.bat.flexible.dao.picture.co.*;
import com.bat.flexible.dao.picture.dataobject.*;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeRelaConstant;
import com.bat.flexible.manager.common.constant.picture.PictureConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class PictureQryExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureQryExe.class);

    @Autowired
    private PictureDOMapper pictureDOMapper;

    @Autowired
    private PictureCategoryTreeQryExe pictureCategoryTreeQryExe;

    @Autowired
    private DistributorPictureExcludeServiceI distributorPictureExcludeServiceI;

    @Autowired
    private PictureMaterialRelevanceServiceI pictureMaterialRelevanceServiceI;


    @Autowired
    private PictureModelRelevanceServiceI pictureModelRelevanceServiceI;

    @Autowired
    private PictureProductCategoryRelevanceServiceI pictureProductCategoryRelevanceServiceI;

    @Autowired
    private PictureDistributorRelevanceServiceI pictureDistributorRelevanceServiceI;

    @Autowired
    private MaterialServiceI materialServiceI;

    @Autowired
    private PictureTemplateEditServiceI pictureTemplateEditServiceI;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @Value("${country.china.id}")
    private Integer chinaCountryId;

    @Value("${distributor.id.sanxing}")
    private Integer sanxingDistributorId;

    public PictureDO getById(Integer id) {
        if (id == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        PictureDO pictureDO = pictureDOMapper.selectByPrimaryKey(id);
        if (pictureDO == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        if (FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(pictureDO.getDelFlag())) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY);
        }
        return pictureDO;
    }

    public Integer findMaxByCategoryId(Integer categoryId) {
        return pictureDOMapper.findMaxByCategoryId(categoryId);
    }

    public List<PictureDO> listByPictureIdList(List<Integer> pictureIdList) {
        if (pictureIdList == null || pictureIdList.size() == 0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_LIST_NULL);
        }
        List<PictureDO> pictureDOList = pictureDOMapper.listByPictureIdList(pictureIdList);
        if (pictureDOList == null || pictureDOList.size() == 0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        if (pictureDOList.size() - pictureIdList.size() != 0) {
            for (int x = 0; x < pictureIdList.size(); x++) {
                Boolean flag = false;
                for (int y = 0; y < pictureDOList.size(); y++) {
                    if (pictureIdList.get(x) - pictureDOList.get(y).getId() == 0) {
                        flag = true;
                        if (pictureDOList.get(x).getDelFlag() - FlexibleCommonConstant.COMMON_DEL_FLAG_YES == 0) {
                            String msg = MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_PICTURE) + "【" + pictureIdList.get(x) + "】" +
                                    MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY);
                            throw new FlexibleCustomException(msg);
                        }
                        break;
                    }
                }
                if (!flag) {
                    String msg = MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_PICTURE) + "【" + pictureIdList.get(x) + "】" +
                            MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
                    throw new FlexibleCustomException(msg);
                }
            }
        }
        return pictureDOList;
    }

    public List<PictureDO> listByCategoryIdAndOpenFlag(Integer categoryId, Short openFlag) {
        return pictureDOMapper.listByCategoryIdAndOpenFlag(categoryId, openFlag);
    }

    public PictureDO findEffectByUpOrDown(Integer categoryId, Boolean flag, Integer sequence) {
        return pictureDOMapper.findEffectByUpOrDown(categoryId, flag, sequence);
    }

    public List<PicturePageCO> listCOByCondition(Integer categoryId, Short openFlag, String content, Short type,
                                                 List<Integer> materialIdList, List<Integer> modelIdList) {
        List<PicturePageCO> list = pictureDOMapper.listCOByCondition(categoryId, openFlag, content, type, materialIdList, modelIdList);
        PictureConvertor.setMaterialNameByIds(list, materialServiceI);
        return list;
    }

    public List<PictureCategorySimpleTreeCO> tree(PictureTreeQry pictureTreeQry) {

        //查询启用的图库分类
        List<PictureCategorySimpleTreeCO> treeCOList = pictureCategoryTreeQryExe.treeSimple(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
        if (treeCOList == null || treeCOList.size() == 0) {
            return treeCOList;
        }

        //通过启用状态和图库类型查询数据
        List<PictureDO> pictureDOList = pictureDOMapper.listByConditionType(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES, pictureTreeQry.getType(), pictureTreeQry.getTypeList());
        if (pictureDOList == null || pictureDOList.size() == 0) {
            return treeCOList;
        }

        //判断分销商、型号材质、产品类型
        removeByDistributorIdsAndMaterialIdAndModelId(pictureTreeQry.getDistributorIds(), pictureTreeQry.getMaterialId(), pictureTreeQry.getModelId(), pictureDOList, pictureTreeQry.getProductCategoryId(), pictureTreeQry.getDistributorId());
        if (pictureDOList == null || pictureDOList.size() == 0) {
            return treeCOList;
        }
        //设置模板
        List<PictureTemplateEditCmd> editCmdList = pictureTemplateEditServiceI.listByPictureId(null);
        Map<Integer, List<PictureTemplateEditCmd>> templateMap = new HashMap<>();
        if (editCmdList != null && editCmdList.size() > 0) {
            templateMap = editCmdList.stream().collect(Collectors.toMap(PictureTemplateEditCmd::getPictureId, edit -> Lists.newArrayList(edit),
                    (List<PictureTemplateEditCmd> oldList, List<PictureTemplateEditCmd> newList) -> {
                        oldList.addAll(newList);
                        return oldList;
                    }));
        }
        //图片集合 、key为分类id、value为图片列表
        Map<Integer, List<PictureDO>> pictureDOMap = pictureDOList.stream().collect(Collectors.toMap(PictureDO::getCategoryId, pictureDO -> Lists.newArrayList(pictureDO),
                (List<PictureDO> oldList, List<PictureDO> newList) -> {

                    oldList.addAll(newList);
                    return oldList;
                }
        ));

        //递归图片分类
        recurisePictureCategory(treeCOList, pictureDOMap, templateMap);
        return treeCOList;
    }

    /**
     * 递归处理图片分类
     *
     * @param treeCOList
     * @param pictureDOMap
     */
    private void recurisePictureCategory(List<PictureCategorySimpleTreeCO> treeCOList, Map<Integer, List<PictureDO>> pictureDOMap, Map<Integer, List<PictureTemplateEditCmd>> templateMap) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return;
        }
        if (pictureDOMap == null || pictureDOMap.size() == 0) {
            return;
        }
        for (int x = 0; x < treeCOList.size(); x++) {
            PictureCategorySimpleTreeCO treeCO = treeCOList.get(x);
            if (FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(treeCO.getAtLastTrademark())) {
                //最终分类
                List<PictureDO> list = pictureDOMap.get(treeCO.getId());
                if (list != null && list.size() > 0) {
                    List<PictureCO> coList = new ArrayList<>();
                    list.stream().forEach(pictureDO -> {
                        PictureCO pictureCO = BeanUtils.copy(pictureDO, PictureCO.class);

                        if (PictureConstant.PICTURE_TYPE_TEMPLATE.equals(pictureDO.getType())) {
                            pictureCO.setTemplateEditList(templateMap.get(pictureCO.getId()));
                        }
                        coList.add(pictureCO);
                    });
                    treeCO.setPictureList(coList);
                } else {
                    treeCOList.remove(x);
                    x--;
                }
            } else {
                List<PictureCategorySimpleTreeCO> sonList = treeCO.getChildrenList();
                if (sonList != null && sonList.size() > 0) {
                    recurisePictureCategory(sonList, pictureDOMap, templateMap);
                    //递归完判断该节点是否还有满足条件的子节点
                    if (sonList == null || sonList.size() == 0) {
                        treeCOList.remove(x);
                        x--;
                    }
                } else {
                    treeCOList.remove(x);
                    x--;
                }
            }
        }
    }

    private void removeByDistributorIdsAndMaterialIdAndModelId(List<Integer> distributorIds, Integer materialId, Integer modelId, List<PictureDO> pictureDOList,
                                                               Integer productCategoryId, Integer distributorId) {
        //处理分销商
        removeByDistributorId(distributorIds, pictureDOList, distributorId);
        //移除材质不符合的
        removeByMaterialId(pictureDOList, materialId);
        //移除型号不符合的
        removeByModelId(pictureDOList, modelId);
        //移除产品类型不符合的
        removeByProductCategory(pictureDOList, productCategoryId);
    }

    private void removeByDistributorId(List<Integer> distributorIds, List<PictureDO> pictureDOList, Integer distributorId) {
        if (distributorId == null) {
            return;
        }
        if (pictureDOList == null || pictureDOList.size() == 0) {
            return;
        }
        List<DistributorPictureExcludeDO> pictureExcludeDOList = distributorPictureExcludeServiceI.listByDistributorId(distributorIds);
        if (pictureExcludeDOList != null && pictureExcludeDOList.size() > 0) {
            for (int x = 0; x < pictureDOList.size(); x++) {
                for (int y = 0; y < pictureExcludeDOList.size(); y++) {
                    if (pictureDOList.get(x).getId() - pictureExcludeDOList.get(y).getPictureId() == 0) {
                        //排斥不可用
                        pictureDOList.remove(x);
                        x--;
                        break;
                    }
                }
            }
        }
        if (pictureDOList.size() == 0) {
            return;
        }
        //处理国内国外
        Integer countryId = chinaCountryId;

        try {
            countryId = flexibleDistributorQryExe.getCountryIdByDistributorId(distributorId);
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            throw new FlexibleCustomException(e.getMessage());
        }
        //指定分销商
        List<PictureDistributorRelevanceDO> relevanceDOList = pictureDistributorRelevanceServiceI.listByCondition(null, distributorIds);
        Map<Integer, Integer> map = relevanceDOList.stream().collect(Collectors.toMap(PictureDistributorRelevanceDO::getPictureId, p -> p.getDistributorId(), (k1, k2) -> k1));
        for (int x = 0; x < pictureDOList.size(); x++) {
            PictureDO pictureDO = pictureDOList.get(x);
            Boolean flag = false;
            if (PictureConstant.RESELLER_SCOPE_DISTRIBUTOR_INTERNAL_ASSIGN.equals(pictureDO.getResellerScope()) && !countryId.equals(chinaCountryId)) {
                flag = true;
            }
            if (PictureConstant.RESELLER_SCOPE_DISTRIBUTOR_FOREIGN_ASSIGN.equals(pictureDO.getResellerScope()) && countryId.equals(chinaCountryId)) {
                flag = true;
            }
            if (PictureConstant.RESELLER_SCOPE_DISTRIBUTOR_PERSONAL_ASSIGN.equals(pictureDO.getResellerScope()) && (
                    map.size() == 0 || !map.containsKey(pictureDO.getId()))) {
                //指定分销商不存在
                flag = true;
            }
            //处理
            if (sanxingDistributorId.equals(distributorId) && (
                    map.size() == 0 || !map.containsKey(pictureDO.getId()))) {
                flag = true;
            }
            if (flag) {
                pictureDOList.remove(x);
                x--;
            }
        }
    }

    private void removeByProductCategory(List<PictureDO> pictureDOList, Integer productCategoryId) {
        if (pictureDOList == null || pictureDOList.size() == 0) {
            return;
        }
        if (productCategoryId == null) {
            return;
        }
        List<Integer> pictureIdList = pictureDOList.stream().map(PictureDO::getId).collect(Collectors.toList());
        List<PictureProductCategoryRelevanceDO> relevanceDOList = pictureProductCategoryRelevanceServiceI.listByCondition(null, null, pictureIdList);
        if (relevanceDOList == null || relevanceDOList.size() == 0) {
            return;
        }
        Map<String, Integer> map = new HashMap<>();
        relevanceDOList.stream().forEach(pictureProductCategoryRelevanceDO -> {
            map.put(pictureProductCategoryRelevanceDO.getPictureId() + "_" + pictureProductCategoryRelevanceDO.getCategoryId(), pictureProductCategoryRelevanceDO.getCategoryId());
        });
        //移除指定了产品类型可用又不在map里面的图片
        for (int x = 0; x < pictureDOList.size(); x++) {
            if (!PictureConstant.MODEL_SCOPE_MODEL_CATEGORY.equals(pictureDOList.get(x).getModelScope())) {
                //非指定型号
                continue;
            }
            //不包含就删除
            if (!map.containsKey(pictureDOList.get(x).getId() + "_" + productCategoryId)) {
                pictureDOList.remove(x);
                x--;
            }
        }
    }

    private void removeByModelId(List<PictureDO> pictureDOList, Integer modelId) {
        //判断列表是否还大于0
        if (pictureDOList == null || pictureDOList.size() == 0) {
            return;
        }
        if (modelId == null) {
            return;
        }
        List<Integer> pictureIdList = pictureDOList.stream().map(PictureDO::getId).collect(Collectors.toList());
        //找出当前图片id列表对应的图片型号
        List<PictureModelRelevanceDO> relevanceDOList = pictureModelRelevanceServiceI.listByCondition(null, null, pictureIdList);
        if (relevanceDOList == null || relevanceDOList.size() == 0) {
            //图片都没设置型号关联
            return;
        }

        Map<String, Integer> modelPictureMap = new HashMap<>();
        relevanceDOList.stream().forEach(pictureModelRelevanceDO -> {
            modelPictureMap.put(pictureModelRelevanceDO.getPictureId() + "_" + pictureModelRelevanceDO.getModelId(), pictureModelRelevanceDO.getModelId());
        });
        for (int x = 0; x < pictureDOList.size(); x++) {
            if (!PictureConstant.MODEL_SCOPE_MODEL_ASSIGN.equals(pictureDOList.get(x).getModelScope())) {
                //非指定型号
                continue;
            }
            if (!modelPictureMap.containsKey(pictureDOList.get(x).getId() + "_" + modelId)) {
                pictureDOList.remove(x);
                x--;
            }
        }
    }

    /**
     * 材质和图片的关联关系
     *
     * @param pictureDOList
     * @param materialId
     */
    private void removeByMaterialId(List<PictureDO> pictureDOList, Integer materialId) {
        //判断列表是否还大于0
        if (pictureDOList == null || pictureDOList.size() == 0) {
            return;
        }
        if (materialId == null) {
            return;
        }
        List<PictureMaterialRelevanceDO> relevanceDOList = pictureMaterialRelevanceServiceI.listByPictureIdAndMaterialId(null, materialId);
        if (relevanceDOList == null || relevanceDOList.size() == 0) {
            //没有关联关系、直接返回
            pictureDOList = null;
            return;
        }
        Map<Integer, Integer> materialPictureMap = relevanceDOList.stream().collect(Collectors.toMap(PictureMaterialRelevanceDO::getPictureId, p -> p.getId()));
        pictureDOList.removeIf(pictureDO -> !materialPictureMap.containsKey(pictureDO.getId()));
    }


    public List<PictureDO> checkDistributorPicture(Integer distributorId, List<Integer> pictureIdList, Integer countryId) {
        return pictureDOMapper.listByDistributorIdAndPictureIdListAndCountryId(distributorId, pictureIdList, countryId);
    }

    public List<CommonPicturePageCO> listCommonPictureCOByCondition(Integer distributorId, List<Integer> distributorIds, Integer materialId, Integer modelId, Integer exchangeId, ExchangePictureRelevanceDO exchangePictureRelevanceDO,
                                                                    Integer countryId, List<Integer> pictureCategoryIdList, Short type) {
        //判断是否是
        boolean isSanXing = false;
        if (distributorId == sanxingDistributorId.intValue()) {
            isSanXing = true;
        }
        List<CommonPicturePageCO> list = new ArrayList<>();
        if (exchangeId != null) {
            //存在兑换卡
            return pictureDOMapper.listExchangePictureByCondition(isSanXing, exchangeId, materialId, distributorIds, modelId, pictureCategoryIdList, countryId,
                    ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_ALL.equals(exchangePictureRelevanceDO.getType()), type);
           /* if (ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_ALL.equals(exchangePictureRelevanceDO.getType())) {


            }else{
                builder.append("SELECT a.* FROM picture a LEFT JOIN picture_reseller_references b on a.id=b.picture_id");
                builder.append(" LEFT JOIN picture_category c on c.id=a.category_id");
                builder.append(" LEFT JOIN exchange_picture_rela d on d.picture_id=a.id");
                if (themeDTO.getMaterialId() != null) {
                    builder.append(" LEFT JOIN picture_goods_references e ON e.picture_id = a.id ");
                }
                if (themeDTO.getModelId() != null) {
                    builder.append(" LEFT JOIN picture_model_references f ON a.id = f.picture_id");
                }
                builder.append(" WHERE a.category_id in :categoryIds and a.status=1 and c.status=1");
                if(isSanXing){
                    builder.append(" and a.reseller_scope=4 and b.reseller_id=:distributorId");
                }else {
                    builder.append(" and (a.reseller_scope=1  or (a.reseller_scope=2 and :countryId=1) or (a.reseller_scope=3 and :countryId>1) or (a.reseller_scope=4 and b.reseller_id=:distributorId))");
                }
                builder.append(" and d.exchange_id=:exchangeId");
                if (themeDTO.getMaterialId() != null) {
                    builder.append(" and e.material_id=:materialId");
                }
                if (themeDTO.getModelId() != null) {
                    builder.append(" AND ( a.model_scope = 1 OR ( a.model_scope = 2 AND f.`status` = 1 AND f.model_id =:modelId ) ) ");
                }
                //排除掉不可用的
                builder.append(" and NOT EXISTS(select 1 from distributor_diy_picture_remove ddpr where ddpr.distributor_id =:distributorId and ddpr.picture_id = a.id)");
                builder.append(" ORDER BY a.sequence");
                list = pictureDOMapper.listAssignExchangePictureByCondition(isSanXing,exchangeId,materialId,distributorId,modelId,pictureCategoryIdList,countryId);
            }
            map.put("exchangeId", themeDTO.getExchangeId());*/
        } else {
           /* builder.append("SELECT a.* FROM picture a LEFT JOIN picture_reseller_references b on a.id=b.picture_id");
            builder.append(" LEFT JOIN picture_category c on c.id=a.category_id");
            if (themeDTO.getMaterialId() != null) {
                builder.append(" LEFT JOIN picture_goods_references d ON d.picture_id = a.id ");
            }
            if (themeDTO.getModelId() != null) {
                builder.append(" LEFT JOIN picture_model_references e ON a.id = e.picture_id");
            }
            builder.append(" WHERE a.category_id in :categoryIds and a.status=1 and c.status=1");
            if(isSanXing){
                builder.append(" and a.reseller_scope=4 and b.reseller_id=:distributorId");
            }else {
                builder.append(" and (a.reseller_scope=1  or (a.reseller_scope=2 and :countryId=1) or (a.reseller_scope=3 and :countryId>1) or (a.reseller_scope=4 and b.reseller_id=:distributorId))");
            }
            if (themeDTO.getMaterialId() != null) {
                builder.append(" and d.material_id=:materialId");
            }
            if (themeDTO.getModelId() != null) {
                builder.append(" AND ( a.model_scope = 1 OR ( a.model_scope = 2 AND e.`status` = 1 AND e.model_id =:modelId ) ) ");
            }
            //排除掉不可用的
            builder.append(" and NOT EXISTS(select 1 from distributor_diy_picture_remove ddpr where ddpr.distributor_id =:distributorId and ddpr.picture_id = a.id)");
            builder.append(" ORDER BY a.sequence");*/
            return pictureDOMapper.listExchangePictureByCondition(isSanXing, exchangeId, materialId, distributorIds, modelId, pictureCategoryIdList, countryId,
                    null, type);
        }
    }

    public List<DistributorPictureCO> listDistributorByCondition(Integer distributorId, Short openFlag, List<Integer> categoryIdList, Short type, Boolean isSanXing,
                                                                 Integer countryId) {

        return pictureDOMapper.listDistributorByCondition(distributorId, openFlag, categoryIdList, type, isSanXing, countryId);
    }

    public List<PictureDO> listByCondition(Short openFlag, Short type) {
        return pictureDOMapper.listByCondition(openFlag, type);
    }


    public PictureDO getByCode(String code) {
        return pictureDOMapper.getByCode(code);
    }
}
