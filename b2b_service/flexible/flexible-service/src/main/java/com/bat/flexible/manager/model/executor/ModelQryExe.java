package com.bat.flexible.manager.model.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bat.flexible.api.model.ModelMaterialRelevanceServiceI;
import com.bat.flexible.api.model.dto.ModelQry;
import com.bat.flexible.api.picture.PictureModelRelevanceServiceI;
import com.bat.flexible.api.product.ProductCategoryServiceI;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeRelaConstant;
import com.bat.flexible.manager.common.constant.picture.PictureConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.anno.Cached;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.exchange.ExchangeModelRelevanceServiceI;
import com.bat.flexible.dao.distributor.dataobject.DistributorModelExcludeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeModelRelevanceDO;
import com.bat.flexible.dao.model.ModelDOMapper;
import com.bat.flexible.dao.model.co.ModelPageCO;
import com.bat.flexible.dao.model.co.ModelSimpleCO;
import com.bat.flexible.dao.model.co.ModelTreeCO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import com.bat.flexible.dao.picture.dataobject.PictureModelRelevanceDO;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.model.ModelErrorCode;

@Component
public class ModelQryExe {

    @Autowired
    private ModelDOMapper modelDOMapper;

    @Autowired
    private ProductCategoryServiceI productCategoryServiceI;

    @Autowired
    private ModelMaterialRelevanceServiceI modelMaterialRelevanceServiceI;

    @Autowired
    private PictureModelRelevanceServiceI pictureModelRelevanceServiceI;

    @Autowired
    private ExchangeModelRelevanceServiceI exchangeModelRelevanceServiceI;

    @Autowired
    private ModelTreeQryExe modelTreeQryExe;

    /**
     * 根据型号id列表查询型号列表
     * 
     * @param modelIdList
     * @return
     */
    public List<ModelDO> listByModelIdList(List<Integer> modelIdList) {
        List<ModelDO> modelDOList = modelDOMapper.listByModelIdList(modelIdList);
        if (modelDOList == null || modelDOList.size() == 0) {
            String msg = MessageUtils.get(ModelErrorCode.MODEL_NAME_ERROR_MSG_CODE) + "【"
                + JSON.toJSONString(modelIdList) + "】" + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
            throw new FlexibleCustomException(msg);
        }
        if (modelDOList.size() - modelIdList.size() == 0) {
            return modelDOList;
        }
        modelIdList.stream().forEach(modelId -> {
            Boolean flag = false;
            for (int x = 0; x < modelDOList.size(); x++) {
                if (modelDOList.get(x).getId() - modelId == 0) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                String msg = MessageUtils.get(ModelErrorCode.MODEL_NAME_ERROR_MSG_CODE) + "【" + modelId + "】"
                    + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
                throw new FlexibleCustomException(msg);
            }
        });
        return modelDOList;
    }

    public List<ModelDO> listByIdList(List<Integer> idList) {
        List<ModelDO> modelDOList = modelDOMapper.listByModelIdList(idList);
        return modelDOList;
    }

    @Cached(name = FlexibleKeyConstant.MODEL_DO_PRE, key = "#id")
    public ModelDO getById(Integer id) {
        if (id == null) {
            throw new FlexibleCustomException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MODEL)
                + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL));
        }
        ModelDO modelDO = modelDOMapper.selectByPrimaryKey(id);
        if (modelDO == null) {
            throw new FlexibleCustomException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MODEL)
                + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR));
        }
        if (FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(modelDO.getDelFlag())) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY);
        }
        return modelDO;
    }

    public ModelDO findEffectByUpOrDown(Integer parentId, Boolean flag, Integer sequence) {
        return modelDOMapper.findEffectByUpOrDown(parentId, flag, sequence);
    }

    public List<ModelDO> listByParentId(Integer parentId) {
        return modelDOMapper.listByParentId(parentId);
    }

    public List<ModelDO> listByParentIdsAndDistributorIdsAndMaterialId(List<Integer> parentIds, List<Integer> distributorIds, Integer materialId) {
        if(parentIds.size()==0){
            return new ArrayList<>();
        }
        List<ModelDO> modelDOS = modelDOMapper.listByParentIdsAndDistributorIdsAndMaterialId(parentIds,distributorIds);
        return modelDOS;
    }

    /**
     *
     * @param modelNo
     *            型号编码
     * @param checkUsable
     *            检查是否可用
     * @return
     */
    public ModelDO getByModelNo(String modelNo, Boolean checkUsable) {
        if (StringUtils.isBlank(modelNo)) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        if (checkUsable == null) {
            checkUsable = true;
        }
        ModelDO modelDO = modelDOMapper.getByModelNo(modelNo);
        if (checkUsable && modelDO == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_MODEL_NO_ERROR);
        }
        if (checkUsable && FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(modelDO.getDelFlag())) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY);
        }
        return modelDO;
    }

    public List<ModelPageCO> treeByCondition(Integer parentId, Integer categoryId, String content, Short openFlag,
        Short atLastTrademark, List<ModelMaterialRelevanceDO> modelMaterialRelevanceDOList) {
        //
        List<ModelPageCO> list = modelTreeQryExe.treeAll(categoryId);
        // List<ModelPageCO> list = modelDOMapper.treeByCondition(parentId,categoryId,content,openFlag,atLastTrademark);
        if (list == null || list.size() == 0) {
            return list;
        }
        // 递归处理parentId
        list = filterParentId(parentId, list);

        // 递归处理openFlag
        list = filterOpenFlagRecurise(list, openFlag);
        // 递归处理最终分类
        if (atLastTrademark != null && FlexibleCommonConstant.AT_LAST_TRADEMARK_NO.equals(atLastTrademark)) {
            list = filterAtLastTrademarkRecurise(list, atLastTrademark);
        }
        if (atLastTrademark != null && FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(atLastTrademark)) {
            // 查询最终分类、只返回一级、不要父节点
            List<ModelPageCO> sonList = new ArrayList<>();
            filterSonListRecurise(list, sonList);
            // 替换
            list = sonList;
        }

        // 过滤content
        list = filterContentRecurise(list, content);
        if (list == null || list.size() == 0) {
            return list;
        }
        // 指定型号
        Map<Integer, Integer> assignModelMap = new HashMap<>();
        if (modelMaterialRelevanceDOList != null && modelMaterialRelevanceDOList.size() > 0) {
            // 查询指定的型号
            for (int x = 0; x < modelMaterialRelevanceDOList.size(); x++) {
                assignModelMap.put(modelMaterialRelevanceDOList.get(x).getModelId(), 1);
            }
        }
        // 过滤指定型号
        list = filterAssignRecurise(list, assignModelMap);
        if (list == null || list.size() == 0) {
            return list;
        }
        List<ProductCategoryDO> productCategoryDOList = productCategoryServiceI.listByCondition(null);
        if (productCategoryDOList == null || productCategoryDOList.size() == 0) {
            return list;
        }
        Map<Integer, ProductCategoryDO> map = productCategoryDOList.stream()
            .collect(Collectors.toMap(ProductCategoryDO::getId, productCategoryDO -> productCategoryDO));
        // 递归设置产品类型名称和型号父类名称
        recursiveSetProductNameAndParentName(list, map, null, null);
        return list;
    }

    // 查询最终子类
    private void filterSonListRecurise(List<ModelPageCO> list, List<ModelPageCO> sonList) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int x = 0; x < list.size(); x++) {
            ModelPageCO modelPageCO = list.get(x);
            if (FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(modelPageCO.getAtLastTrademark())) {
                sonList.add(modelPageCO);
            } else {
                // 父类
                List<ModelPageCO> childrenList = modelPageCO.getChildrenList();
                if (childrenList == null || childrenList.size() == 0) {
                    list.remove(x);
                    x--;
                } else {
                    // 存在子级分类列表
                    filterSonListRecurise(childrenList, sonList);
                }
            }
        }
    }

    private void recursiveSetProductNameAndParentName(List<ModelPageCO> list, Map<Integer, ProductCategoryDO> map,
        String parentName, String parentEnglishName) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int x = 0; x < list.size(); x++) {
            ModelPageCO modelPageCO = list.get(x);
            ProductCategoryDO productCategoryDO = map.get(modelPageCO.getCategoryId());
            modelPageCO.setCategoryEnglishName(productCategoryDO.getEnglishName());
            modelPageCO.setCategoryName(productCategoryDO.getName());
            // 父节点大于0、
            if (StringUtils.isBlank(parentName) && modelPageCO.getParentId() > 0) {
                //
                ModelDO modelDO = modelDOMapper.selectByPrimaryKey(modelPageCO.getParentId());
                parentName = modelDO.getName();
                parentEnglishName = modelDO.getEnglishName();
            }
            modelPageCO.setParentName(parentName);
            modelPageCO.setParentEnglishName(parentEnglishName);
            List<ModelPageCO> childrenList = modelPageCO.getChildrenList();
            if (childrenList != null && childrenList.size() > 0) {
                // 递归设置子类的
                recursiveSetProductNameAndParentName(childrenList, map, modelPageCO.getName(),
                    modelPageCO.getEnglishName());
            }
        }
    }

    public List<ModelTreeCO> tree(ModelQry modelQry, PictureDO pictureDO,
                                  List<DistributorModelExcludeDO> excludeDOList) {

        List<ModelTreeCO> treeCOList = null;
        // 指定型号的
        Map<Integer, Integer> assignModelMap = new HashMap<>();
        // 指定排斥的
        Map<Integer, Integer> excludeMap = new HashMap<>();
        // 设置标记、是否进行了指定型号
        Boolean assignModelFlag = false;
        if (excludeDOList != null && excludeDOList.size() > 0) {
            for (int x = 0; x < excludeDOList.size(); x++) {
                excludeMap.put(excludeDOList.get(x).getModelId(), 1);
            }
        }
        Map<String, ModelMaterialRelevanceDO> map = new HashMap<>();
        // 1、处理指定材质的（顺序请勿更改）
        if (modelQry.getMaterialId() != null) {
            List<ModelMaterialRelevanceDO> relevanceDOList = modelMaterialRelevanceServiceI
                .listByCondition(modelQry.getMaterialId(), null, FlexibleCommonConstant.COMMON_OPEN_FLAG_YES, null);
            if (relevanceDOList == null || relevanceDOList.size() == 0) {
                return null;
            }
            for (int x = 0; x < relevanceDOList.size(); x++) {
                ModelMaterialRelevanceDO modelMaterialRelevanceDO = relevanceDOList.get(x);
                assignModelMap.put(modelMaterialRelevanceDO.getModelId(), modelMaterialRelevanceDO.getModelId());
                map.put(modelMaterialRelevanceDO.getMaterialId() + "_" + modelMaterialRelevanceDO.getModelId(),
                    modelMaterialRelevanceDO);
            }
            // 材质指定了型号
            assignModelFlag = true;
        }
        // 2、处理图片指定
        if (pictureDO != null && PictureConstant.MODEL_SCOPE_MODEL_ASSIGN.equals(pictureDO.getModelScope())) {
            List<PictureModelRelevanceDO> pictureModelRelevanceDOList =
                pictureModelRelevanceServiceI.listByCondition(pictureDO.getId(), null, null);
            if (pictureModelRelevanceDOList == null || pictureModelRelevanceDOList.size() == 0) {
                return treeCOList;
            }
            if (assignModelFlag) {
                // 取交集
                Map<Integer, Integer> newAssignMap = new HashMap<>();
                for (int x = 0; x < pictureModelRelevanceDOList.size(); x++) {
                    if (assignModelMap.containsKey(pictureModelRelevanceDOList.get(x).getModelId())) {
                        // 交集
                        newAssignMap.put(pictureModelRelevanceDOList.get(x).getModelId(), 1);
                    }
                }
                assignModelMap = newAssignMap;
                if (assignModelMap.size() == 0) {
                    return null;
                }
            } else {
                assignModelMap = pictureModelRelevanceDOList.stream().collect(
                    Collectors.toMap(PictureModelRelevanceDO::getModelId, PictureModelRelevanceDO::getModelId));
            }
            // 指定型号范围
            assignModelFlag = true;
        }
        // 3、处理兑换卡指定
        if (modelQry.getExchangeId() != null) {
            List<ExchangeModelRelevanceDO> exchangeModelRelevanceDOList =
                exchangeModelRelevanceServiceI.listByExchangeId(modelQry.getExchangeId());
            if (ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_SOME
                .equals(exchangeModelRelevanceDOList.get(0).getType())) {
                // 指定型号可用
                if (assignModelFlag) {
                    // 取交集
                    Map<Integer, Integer> newAssignModelMap = new HashMap<>();
                    for (int x = 0; x < exchangeModelRelevanceDOList.size(); x++) {
                        Integer modelId = exchangeModelRelevanceDOList.get(x).getModelId();
                        if (assignModelMap.containsKey(modelId)) {
                            newAssignModelMap.put(modelId, 1);
                        }
                    }
                    assignModelMap = newAssignModelMap;
                    if (assignModelMap.size() == 0) {
                        return null;
                    }
                } else {
                    for (int x = 0; x < exchangeModelRelevanceDOList.size(); x++) {
                        Integer modelId = exchangeModelRelevanceDOList.get(x).getModelId();
                        assignModelMap.put(modelId, modelId);
                    }
                }
                assignModelFlag = true;
            }
        }
        treeCOList = modelTreeQryExe.treeByCategoryIdAndOpenFlag(modelQry.getCategoryId(),
            FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);

        // 剔除不可用
        removeModelWithPicture(treeCOList, assignModelMap, excludeMap);
        if (treeCOList == null || treeCOList.size() == 0 || modelQry.getMaterialId() == null) {
            return treeCOList;
        }
        if (StringUtils.isNotBlank(modelQry.getSearch())) {
            // 移除不符合的搜索
            removeModelBySearch(treeCOList, modelQry.getSearch());
        }
        recursiveSetModelAndMaterialParam(treeCOList, map, modelQry.getMaterialId());
        // 处理不同平台排序优先级

        return treeCOList;
    }

    private void removeModelBySearch(List<ModelTreeCO> treeCOList, String search) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return;
        }
        // List<ModelTreeCO> allList = new ArrayList<>();
        for (int x = 0; x < treeCOList.size(); x++) {
            ModelTreeCO modelTreeCO = treeCOList.get(x);
            List<ModelTreeCO> childrenList = modelTreeCO.getChildrenList();
            if (childrenList == null || childrenList.size() == 0) {
                // 没有下一级
                if (modelTreeCO.getName().indexOf(search) == -1 && modelTreeCO.getEnglishName().indexOf(search) == -1
                    && (modelTreeCO.getModelNo() == null || (StringUtils.isNotBlank(modelTreeCO.getModelNo())
                        && modelTreeCO.getModelNo().indexOf(search) == -1))) {
                    // 不符合搜索关键词、删除
                    treeCOList.remove(x);
                    x--;
                }
            }
            if (childrenList != null && childrenList.size() > 0) {
                removeModelBySearch(childrenList, search);
                if (childrenList == null || childrenList.size() == 0) {
                    // 子节点都不符合、删除父节点
                    treeCOList.remove(x);
                    x--;
                }
            }
        }
        return;
    }

    private void removeModelWithPicture(List<ModelTreeCO> treeCOList, Map<Integer, Integer> assignModelMap,
        Map<Integer, Integer> excludeMap) {
        // 先设置型号的父id、key:最低级上一级id、value、上一级id的父id -隔开 （）
        Map<Integer, String> parentMap = new HashMap<>();
        // 父几点下面有几个子节点
        // Map<Integer, Integer> usableMap = new HashMap<>();
        // 处理父节点信息
        // 移除不可用
        recuriseExclude(excludeMap, treeCOList);
        if (assignModelMap != null && assignModelMap.size() > 0) {
            recursiveSetParentMsg(treeCOList, parentMap, assignModelMap, excludeMap);
            // 递归删掉型号
            recursiveRemove(treeCOList, assignModelMap);
        }

    }

    private void recuriseExclude(Map<Integer, Integer> excludeMap, List<ModelTreeCO> treeCOList) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return;
        }
        if (excludeMap == null || excludeMap.size() == 0) {
            return;
        }
        // List<ModelTreeCO> allList = new ArrayList<>();
        for (int x = 0; x < treeCOList.size(); x++) {
            ModelTreeCO modelTreeCO = treeCOList.get(x);
            if (excludeMap.containsKey(modelTreeCO.getModelId())) {
                // 不可用
                treeCOList.remove(x);
                x--;
                continue;
            }
            List<ModelTreeCO> childrenList = modelTreeCO.getChildrenList();

            if (childrenList != null && childrenList.size() > 0) {
                recuriseExclude(excludeMap, childrenList);
                if (childrenList == null || childrenList.size() == 0) {
                    // 子类都不符合、删除
                    treeCOList.remove(x);
                    x--;
                }
            }
        }

    }

    private List<ModelTreeCO> recursiveRemove(List<ModelTreeCO> treeCOList, Map<Integer, Integer> usableMap) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return treeCOList;
        }
        if (usableMap == null || usableMap.size() == 0) {
            return treeCOList;
        }
        // List<ModelTreeCO> allList = new ArrayList<>();
        for (int x = 0; x < treeCOList.size(); x++) {
            ModelTreeCO modelTreeCO = treeCOList.get(x);
            Integer modelId = usableMap.get(modelTreeCO.getModelId());
            List<ModelTreeCO> childrenList = modelTreeCO.getChildrenList();
            if (modelId == null && (childrenList == null || childrenList.size() == 0)) {
                // 删除
                treeCOList.remove(x);
                x--;
                continue;
            }

            if (childrenList != null && childrenList.size() > 0) {
                childrenList = recursiveRemove(childrenList, usableMap);
                if (childrenList == null || childrenList.size() == 0) {
                    // 子类都不符合、删除
                    treeCOList.remove(x);
                    x--;
                }
            }
        }

        return treeCOList;
    }

    /**
     *
     * @param treeCOList
     * @param parentMap
     * @param assignModelMap
     *            指定/可用的map
     * @param excludeMap
     *            指定不可用的map
     */
    private void recursiveSetParentMsg(List<ModelTreeCO> treeCOList, Map<Integer, String> parentMap,
        Map<Integer, Integer> assignModelMap, Map<Integer, Integer> excludeMap) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return;
        }
        List<ModelTreeCO> allList = new ArrayList<>();
        for (int x = 0; x < treeCOList.size(); x++) {
            ModelTreeCO modelTreeCO = treeCOList.get(x);
            List<ModelTreeCO> childrenList = modelTreeCO.getChildrenList();
            if (childrenList != null && childrenList.size() > 0) {
                allList.addAll(childrenList);

                // 设置子节点数量
                /*Integer sonCount = usableMap.get(modelTreeCO.getModelId());
                if(sonCount ==null){
                    sonCount=0;
                }
                //子节点加1
                sonCount++;*/
                // 当前型号的父id
                Integer parentId = modelTreeCO.getParentId();
                if (parentId > 0) {
                    // 去掉0
                    String value = parentMap.get(parentId);
                    if (StringUtils.isNotBlank(value)) {
                        // 将之前的父id拼接
                        value = value + "-" + parentId;
                    } else {
                        value = String.valueOf(parentId);
                    }
                    parentMap.put(modelTreeCO.getModelId(), value);
                }
            } else {
                // 最低级
                if (assignModelMap.containsKey(modelTreeCO.getModelId())
                    && !excludeMap.containsKey(modelTreeCO.getModelId())) {
                    // 含有这个型号(且不排斥)
                    String parentStr = parentMap.get(modelTreeCO.getParentId());
                    // 加上当前父id
                    if (StringUtils.isNotBlank(parentStr)) {
                        parentStr = parentStr + "-" + modelTreeCO.getParentId();
                    } else {
                        parentStr = String.valueOf(modelTreeCO.getParentId());
                    }
                    String[] parentArray = parentStr.split("-");
                    for (int z = 0; z < parentArray.length; z++) {
                        Integer key = Integer.parseInt(parentArray[z]);
                        if (!excludeMap.containsKey(key)) {
                            // 没有排斥
                            assignModelMap.put(key, 1);
                        } else {
                            // 挑出循环(父节点最大在前面)
                            break;
                        }
                    }
                    // 加上当前的
                    assignModelMap.put(modelTreeCO.getModelId(), 1);
                }
            }
        }
        if (allList != null && allList.size() > 0) {
            recursiveSetParentMsg(allList, parentMap, assignModelMap, excludeMap);
        } else {
            return;
        }

    }

    private void recursiveSetModelAndMaterialParam(List<ModelTreeCO> treeCOList,
        Map<String, ModelMaterialRelevanceDO> map, Integer materialId) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return;
        }
        List<ModelTreeCO> coList = new ArrayList<>();
        for (int x = 0; x < treeCOList.size(); x++) {
            ModelTreeCO modelTreeCO = treeCOList.get(x);
            List<ModelTreeCO> childrenList = modelTreeCO.getChildrenList();
            if (childrenList == null || childrenList.size() == 0) {
                // 最低级
                String key = materialId + "_" + modelTreeCO.getModelId();
                ModelMaterialRelevanceDO relevanceDO = map.get(key);
                if (relevanceDO != null) {
                    modelTreeCO.setLength(relevanceDO.getLength());
                    modelTreeCO.setCrosscuttingWhite(relevanceDO.getCrosscuttingWhite());
                    modelTreeCO.setCrosscuttingWidth(relevanceDO.getCrosscuttingWidth());
                    modelTreeCO.setCutType(relevanceDO.getCutType());
                    modelTreeCO.setFloorImage(relevanceDO.getFloorImage());
                    modelTreeCO.setOutImage(relevanceDO.getOutImage());
                    modelTreeCO.setSlittingHeight(relevanceDO.getSlittingHeight());
                    modelTreeCO.setSlittingWhite(relevanceDO.getSlittingWhite());
                    modelTreeCO.setUnderStockFlag(relevanceDO.getUnderStockFlag());
                    modelTreeCO.setWidth(relevanceDO.getWidth());
                } else {
                    // 没有关联关系
                    treeCOList.remove(x);
                    x--;
                }
            } else {
                // 添加到当前级别下
                coList.addAll(childrenList);
            }
        }
        if (coList != null && coList.size() > 0) {
            recursiveSetModelAndMaterialParam(coList, map, materialId);
        } else {
            return;
        }

    }

    public List<ModelDO> listAll() {
        return modelDOMapper.listAll();
    }

    public List<ModelSimpleCO> listSimpleCO(String content, Short atLastTrademark, List<Integer> modelIdList) {
        List<ModelSimpleCO> list = modelDOMapper.listSimpleCO(content, atLastTrademark, modelIdList);
        if (list == null || list.size() == 0) {
            return null;
        }
        List<ProductCategoryDO> productCategoryDOList = productCategoryServiceI.listByCondition(null);
        if (productCategoryDOList == null || productCategoryDOList.size() == 0) {
            return list;
        }
        Map<Integer, ProductCategoryDO> map = productCategoryDOList.stream()
            .collect(Collectors.toMap(ProductCategoryDO::getId, productCategoryDO -> productCategoryDO));
        list.stream().forEach(modelSimpleDTO -> {
            ProductCategoryDO productCategoryDO = map.get(modelSimpleDTO.getCategoryId());
            modelSimpleDTO.setCategoryName(productCategoryDO.getName());
            modelSimpleDTO.setCategoryEnglishName(productCategoryDO.getEnglishName());
        });
        return list;
    }

    public List<Integer> getSonIdList(Integer parentId, Integer categoryId, Short openFlag) {
        // 最终型号的型号id列表
        List<Integer> modelIdList = new ArrayList<>();
        List<ModelPageCO> modelPageCOList = modelTreeQryExe.treeAll(categoryId);
        modelPageCOList = filterParentId(parentId, modelPageCOList);
        modelPageCOList = filterOpenFlagRecurise(modelPageCOList, openFlag);
        if (modelPageCOList == null || modelPageCOList.size() == 0) {
            return modelIdList;
        }
        recursiveGetLastModelId(modelPageCOList, modelIdList);
        return modelIdList;
    }

    private void recursiveGetLastModelId(List<ModelPageCO> treeCOList, List<Integer> modelIdList) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return;
        }
        for (int x = 0; x < treeCOList.size(); x++) {
            ModelPageCO modelPageCO = treeCOList.get(x);
            List<ModelPageCO> childList = modelPageCO.getChildrenList();
            if (childList == null || childList.size() == 0) {
                modelIdList.add(modelPageCO.getId());
            } else {
                // 递归计算
                recursiveGetLastModelId(childList, modelIdList);
            }
        }
        return;
    }

    public List<ModelDO> listByCondition(Short atLastTrademark, Short openFlag) {
        return modelDOMapper.listByCondition(atLastTrademark, openFlag);
    }

    public ModelDO getOneByNetworkModel(String networkModel) {
        if (StringUtils.isBlank(networkModel)) {
            throw new FlexibleCustomException(ModelErrorCode.M_MODEL_NETWORK_MODEL_NULL);
        }
        return modelDOMapper.getOneByNetworkModel(networkModel);
    }

    /**
     * 根据parentId过滤
     * 
     * @param parentId
     * @param list
     * @return
     */
    private List<ModelPageCO> filterParentId(Integer parentId, List<ModelPageCO> list) {
        if (parentId == FlexibleCommonConstant.COMMON_PARENT_ID) {
            // 返回的列表、就是0的子类
            return list;
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int x = 0; x < list.size(); x++) {
            ModelPageCO modelPageCO = list.get(x);
            List<ModelPageCO> childrenList = modelPageCO.getChildrenList();
            if (modelPageCO.getId() - parentId == 0) {
                // 找到父节点的子类列表、直接返回子列表
                list = childrenList;
                return childrenList;
            }
            // 该分类id非父类id、查看子类列表是否存在
            if (childrenList == null || childrenList.size() == 0) {
                // 没有下一级
                list.remove(x);
                x--;
            } else {
                // 查看子类是否存在
                childrenList = filterParentId(parentId, childrenList);
                if (childrenList != null && childrenList.size() > 0) {
                    // 找到了
                    return childrenList;
                }
            }
        }
        return list;
    }

    private List<ModelPageCO> filterOpenFlagRecurise(List<ModelPageCO> list, Short openFlag) {
        if (openFlag == null) {
            return list;
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int x = 0; x < list.size(); x++) {
            ModelPageCO modelPageCO = list.get(x);
            List<ModelPageCO> childrenList = modelPageCO.getChildrenList();

            if (modelPageCO.getOpenFlag() - openFlag == 0) {
                // 按照状态 和类型来判断、都满足、不删除
                continue;
            }
            // 该分类id非父类id、查看子类列表是否存在
            if (childrenList == null || childrenList.size() == 0) {
                // 没有下一级
                list.remove(x);
                x--;
            } else {
                // 过滤子类列表
                childrenList = filterOpenFlagRecurise(childrenList, openFlag);
                if (childrenList == null || childrenList.size() == 0) {
                    // 子类列表都不满足条件、剔除该节点
                    list.remove(x);
                    x--;
                }
            }
        }
        return list;
    }

    // 过滤搜索关键词
    private List<ModelPageCO> filterContentRecurise(List<ModelPageCO> list, String content) {
        if (StringUtils.isBlank(content)) {
            return list;
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int x = 0; x < list.size(); x++) {
            ModelPageCO modelPageCO = list.get(x);
            List<ModelPageCO> childrenList = modelPageCO.getChildrenList();

            if ((modelPageCO.getName().indexOf(content) > -1
                || (StringUtils.isNotBlank(modelPageCO.getEnglishName())
                    && modelPageCO.getEnglishName().indexOf(content) > -1)
                || (StringUtils.isNotBlank(modelPageCO.getModelNo()) && modelPageCO.getModelNo().indexOf(content) > -1))
                && (childrenList == null || childrenList.size() == 0)) {
                // 模糊匹配名称、英文名称、编码
                continue;
            }
            // 该分类id非父类id、查看子类列表是否存在
            if (childrenList == null || childrenList.size() == 0) {
                // 没有下一级
                list.remove(x);
                x--;
            } else {
                // 过滤子类列表
                childrenList = filterContentRecurise(childrenList, content);
                if (childrenList == null || childrenList.size() == 0) {
                    // 子类列表都不满足条件、剔除该节点
                    list.remove(x);
                    x--;
                }
            }
        }
        return list;
    }

    private List<ModelPageCO> filterAtLastTrademarkRecurise(List<ModelPageCO> list, Short atLastTrademark) {
        if (atLastTrademark == null) {
            return list;
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int x = 0; x < list.size(); x++) {
            ModelPageCO modelPageCO = list.get(x);
            List<ModelPageCO> childrenList = modelPageCO.getChildrenList();

            if (FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(atLastTrademark)
                && modelPageCO.getAtLastTrademark().equals(atLastTrademark)) {
                // 模糊是否最终分类(只能处理最终分类的、)
                continue;
            }
            // 该分类id非父类id、查看子类列表是否存在
            if (childrenList == null || childrenList.size() == 0) {
                // 没有下一级
                /* list.remove(x);
                x--;*/
            } else {
                // 过滤子类列表
                childrenList = filterAtLastTrademarkRecurise(childrenList, atLastTrademark);
                if (FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(atLastTrademark)
                    && (childrenList == null || childrenList.size() == 0)) {
                    // 子类列表都不满足条件、剔除该节点
                    list.remove(x);
                    x--;
                }
            }
        }
        return list;
    }

    /**
     * 过滤指定型号
     * 
     * @param list
     * @param assignModelMap
     * @return
     */
    private List<ModelPageCO> filterAssignRecurise(List<ModelPageCO> list, Map<Integer, Integer> assignModelMap) {
        if (assignModelMap == null || assignModelMap.size() == 0) {
            return list;
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int x = 0; x < list.size(); x++) {
            ModelPageCO modelPageCO = list.get(x);
            List<ModelPageCO> childrenList = modelPageCO.getChildrenList();
            if (assignModelMap.containsKey(modelPageCO.getId())) {
                // 指定了该型号
                continue;
            }
            // 查看子类列表是否存在
            if (childrenList == null || childrenList.size() == 0) {
                // 没有下一级、且没有指定
                list.remove(x);
                x--;
            } else {
                // 过滤子类列表
                childrenList = filterAssignRecurise(childrenList, assignModelMap);
                if (childrenList == null || childrenList.size() == 0) {
                    // 子类列表都不满足条件、剔除该节点
                    list.remove(x);
                    x--;
                }
            }
        }
        return list;
    }

    public List<ModelDO> listAllCanUseParentsByDistributorIds(List<Integer> distributorIds) {
        return modelDOMapper.listAllCanUseParentsByDistributorIds(distributorIds);
    }


    public List<ModelDO> findByIdsAndMaterialIdAndPictureId(List<Integer> ids, Integer materialId, Integer pictureId) {
        if(ids.size()==0){
            return new ArrayList<>();
        }
        List<ModelDO> modelDOS = modelDOMapper.findByIdsAndMaterialIdAndPictureId(ids, materialId, pictureId);
        return modelDOS;
    }
}
