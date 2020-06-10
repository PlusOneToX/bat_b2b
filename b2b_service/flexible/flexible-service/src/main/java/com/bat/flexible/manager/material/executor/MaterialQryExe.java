package com.bat.flexible.manager.material.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bat.flexible.api.model.ModelMaterialRelevanceServiceI;
import com.bat.flexible.api.product.ProductCategoryServiceI;
import com.bat.flexible.api.util.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.anno.Cached;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.exchange.ExchangeMaterialRelevanceServiceI;
import com.bat.flexible.api.material.dto.MaterialTreeAdminQry;
import com.bat.flexible.api.material.dto.MaterialTreeQry;
import com.bat.flexible.dao.distributor.dataobject.DistributorMaterialExcludeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeMaterialRelevanceDO;
import com.bat.flexible.dao.material.MaterialDOMapper;
import com.bat.flexible.dao.material.co.MaterialPageCO;
import com.bat.flexible.dao.material.co.MaterialSimpleTreeCO;
import com.bat.flexible.dao.material.co.MaterialTreeCO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import com.bat.flexible.dao.picture.dataobject.PictureMaterialRelevanceDO;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.material.MaterialErrorCode;

@Component
public class MaterialQryExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialQryExe.class);

    @Autowired
    private MaterialDOMapper materialDOMapper;

    @Autowired
    private ProductCategoryServiceI productCategoryServiceI;

    @Autowired
    private ModelMaterialRelevanceServiceI modelMaterialRelevanceServiceI;

    @Autowired
    private MaterialTreeQryExe materialTreeQryExe;

    @Autowired
    private ExchangeMaterialRelevanceServiceI exchangeMaterialRelevanceServiceI;

    public List<MaterialDO> listByMaterialIdList(List<Integer> materialIdList) {
        List<MaterialDO> materialDOList = materialDOMapper.listByMaterialIdList(materialIdList);
        if (materialDOList == null || materialDOList.size() == 0) {
            String msg = MessageUtils.get(MaterialErrorCode.MATERIAL_NAME_ERROR_MSG_CODE) + "【"
                + JSON.toJSONString(materialIdList) + "】" + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
            throw new FlexibleCustomException(msg);
        }
        if (materialIdList.size() - materialDOList.size() == 0) {
            return materialDOList;
        }
        materialIdList.stream().forEach(materialId -> {
            Boolean flag = false;
            for (int x = 0; x < materialDOList.size(); x++) {
                if (materialDOList.get(x).getId() - materialId == 0) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                String msg = MessageUtils.get(MaterialErrorCode.MATERIAL_NAME_ERROR_MSG_CODE) + "【" + materialId + "】"
                    + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
                throw new FlexibleCustomException(msg);
            }
        });
        return materialDOList;
    }

    @Cached(name = FlexibleKeyConstant.MATERIAL_DO_PRE, key = "#id")
    public MaterialDO getById(Integer id) {
        if (id == null) {
            throw new FlexibleCustomException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL));
        }
        MaterialDO materialDO = materialDOMapper.selectByPrimaryKey(id);
        if (materialDO == null) {
            LOGGER.info(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR) + id);
            throw new FlexibleCustomException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR));
        }
        if (FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(materialDO.getDelFlag())) {
            LOGGER.info(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY) + id);
            throw new FlexibleCustomException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY));
        }
        return materialDO;
    }

    /* @Cached(name = FlexibleKeyConstant.MATERIAL_DO_PRE,key = "#id")
    public MaterialDO findById(Integer id) {
        System.out.println("进来");
        MaterialDO aDo = materialDOCache.get(id);
        return materialDOMapper.selectByPrimaryKey(id);
    }
    */

    /**
     *
     * @param materialNo
     *            材质编码
     * @param checkUsable
     *            是否需要判断材质编码是否准确、删除
     * @return
     */
    public MaterialDO getByMaterialNo(String materialNo, Boolean checkUsable) {
        if (StringUtils.isBlank(materialNo)) {
            throw new FlexibleCustomException(MaterialErrorCode.MATERIAL_NO_NULL);
        }
        if (checkUsable == null) {
            checkUsable = true;
        }
        MaterialDO materialDO = materialDOMapper.getByMaterialNo(materialNo);
        if (checkUsable && materialDO == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        if (checkUsable && FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(materialDO.getDelFlag())) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY);
        }
        return materialDO;
    }

    public MaterialDO findEffectByUpOrDown(Integer parentId, Boolean flag, Integer sequence) {
        return materialDOMapper.findEffectByUpOrDown(parentId, flag, sequence);
    }

    public List<MaterialDO> listByParentId(Integer parentId) {
        return materialDOMapper.listByParentId(parentId);
    }

    public Integer findMaxByParentId(Integer parentId) {
        return materialDOMapper.findMaxByParentId(parentId);
    }

    public List<MaterialPageCO> treeByCondition(Integer categoryId, Short openFlag, String content, Integer parentId) {
        List<MaterialPageCO> list = materialTreeQryExe.treeAllByCategoryId(categoryId);
        // 过滤状态
        list = materialTreeQryExe.filterOpenFlagRecurise(list, openFlag);
        // 过滤父id
        list = filterAdminParentIdRecurise(list, parentId);
        // 过滤搜索关键词
        list = filterContentRecurise(list, content);

        if (list != null && list.size() > 0) {
            // 处理分类名称
            list.stream().forEach(materialPageCO -> {
                ProductCategoryDO productCategoryDO = productCategoryServiceI.getById(materialPageCO.getCategoryId());
                materialPageCO.setCategoryName(productCategoryDO.getName());
                materialPageCO.setCategoryEnglishName(productCategoryDO.getEnglishName());
            });
        }
        return list;
    }

    private List<MaterialPageCO> filterContentRecurise(List<MaterialPageCO> list, String content) {
        if (StringUtils.isBlank(content)) {
            return list;
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int x = 0; x < list.size(); x++) {
            MaterialPageCO materialPageCO = list.get(x);
            List<MaterialPageCO> childrenList = materialPageCO.getChildrenList();

            if ((materialPageCO.getName().indexOf(content) > -1
                || (StringUtils.isNotBlank(materialPageCO.getEnglishName())
                    && materialPageCO.getEnglishName().indexOf(content) > -1)
                || (StringUtils.isNotBlank(materialPageCO.getMaterialNo())
                    && materialPageCO.getMaterialNo().indexOf(content) > -1))
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

    private List<MaterialPageCO> filterAdminParentIdRecurise(List<MaterialPageCO> list, Integer parentId) {
        if (parentId == FlexibleCommonConstant.COMMON_PARENT_ID) {
            // 返回的列表、就是0的子类
            return list;
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int x = 0; x < list.size(); x++) {
            MaterialPageCO materialPageCO = list.get(x);
            List<MaterialPageCO> childrenList = materialPageCO.getChildrenList();
            if (materialPageCO.getId() - parentId == 0) {
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
                childrenList = filterAdminParentIdRecurise(childrenList, parentId);
                if (childrenList != null && childrenList.size() > 0) {
                    // 找到了
                    return childrenList;
                }
            }
        }
        return list;
    }

    /**
     * 前台树结构查询
     * 
     * @param materialTreeQry
     * @param excludeDOList
     * @param relevanceDOList
     * @return
     */
    public List<MaterialTreeCO> tree(MaterialTreeQry materialTreeQry, List<DistributorMaterialExcludeDO> excludeDOList,
        List<PictureMaterialRelevanceDO> relevanceDOList) {
        // 指定可用的材质id(各种指定的交集)
        Map<Integer, Integer> assignMap = new HashMap<>();
        // 排斥的
        Map<Integer, Integer> exludeMap = new HashMap<>();

        // 定义标记、是否指定了材质
        Boolean assignMaterialFlag = false;
        /* if(materialTreeQry.getMaterialIdList() != null && materialTreeQry.getMaterialIdList().size()>0){
           for(int x=0 ; x <  materialTreeQry.getMaterialIdList().size() ;x++){
               assignMap.put(materialTreeQry.getMaterialIdList().get(x),1);
           }
        }*/
        if (excludeDOList != null && excludeDOList.size() > 0) {
            // 设置材质不可用
            for (int x = 0; x < excludeDOList.size(); x++) {
                exludeMap.put(excludeDOList.get(x).getMaterialId(), 1);
            }
        }
        // 1、处理兑换卡（指定）
        if (materialTreeQry.getExchangeId() != null) {
            assignMaterialFlag = true;
            List<ExchangeMaterialRelevanceDO> exchangeMaterialRelevanceDOList =
                exchangeMaterialRelevanceServiceI.listByExchangeId(materialTreeQry.getExchangeId());
            for (int x = 0; x < exchangeMaterialRelevanceDOList.size(); x++) {
                Integer materialId = exchangeMaterialRelevanceDOList.get(x).getMaterialId();
                // materialTreeQry.getMaterialIdList().add(materialId);
                // 指定了材质、需要将指定材质列表设置为0
                assignMap.put(materialId, 1);
            }
        }
        // 2、处理图片关联
        if (relevanceDOList != null && relevanceDOList.size() > 0) {
            // 图片关联了材质
            if (assignMaterialFlag) {
                // 取交集
                Map<Integer, Integer> newAssignMap = new HashMap<>();
                for (int x = 0; x < relevanceDOList.size(); x++) {
                    if (assignMap.containsKey(relevanceDOList.get(x).getMaterialId())) {
                        // 有值
                        newAssignMap.put(relevanceDOList.get(x).getMaterialId(), 1);
                    }
                }
                // 替换
                assignMap = newAssignMap;
                if (assignMap.size() == 0) {
                    return null;
                }
            } else {
                for (int x = 0; x < relevanceDOList.size(); x++) {
                    assignMap.put(relevanceDOList.get(x).getMaterialId(), 1);
                }
            }
            assignMaterialFlag = true;
        }
        // 指定材质
        /* if (materialTreeQry.getMaterialId() !=null){
            //指定材质
            assignMap.put(materialTreeQry.getMaterialId(),1);
        }*/
        //
        if (materialTreeQry.getItemId() != null) {
            // 指定了货品id
            MaterialDO materialDO = materialDOMapper.getByItemId(materialTreeQry.getItemId());
            if (materialDO == null) {
                return null;
            }
            if (assignMap.size() > 0 && !assignMap.containsKey(materialDO.getId())) {
                // 图片指定了材质、但指定货品id关联的材质不在图片指定材质列表、返回null
                return null;
            }
            // 重置指定材质列表（itemId不为空表示只能选这一个材质）
            assignMap = new HashMap<>();
            // 添加指定材质id
            assignMap.put(materialDO.getId(), 1);
        }

        // 根据产品类型获取材质树
        List<MaterialTreeCO> treeCOList = materialTreeQryExe.treeUsableCOByCategoryId(materialTreeQry.getCategoryId());

        Map<String, ModelMaterialRelevanceDO> map = new HashMap<>();
        if (materialTreeQry.getModelId() != null) {
            List<ModelMaterialRelevanceDO> modelMaterialRelevanceDOList = modelMaterialRelevanceServiceI
                .listByCondition(null, materialTreeQry.getModelId(), FlexibleCommonConstant.COMMON_OPEN_FLAG_YES, null);
            if (modelMaterialRelevanceDOList == null || modelMaterialRelevanceDOList.size() == 0) {
                return null;
            }
            if (assignMaterialFlag) {
                // 前面进行了指定、需要取交集
                Map<Integer, Integer> newAssignMap = new HashMap<>();
                for (int x = 0; x < modelMaterialRelevanceDOList.size(); x++) {
                    ModelMaterialRelevanceDO modelMaterialRelevanceDO = modelMaterialRelevanceDOList.get(x);
                    if (assignMap.containsKey(modelMaterialRelevanceDO.getMaterialId())) {
                        // 取交集
                        newAssignMap.put(modelMaterialRelevanceDO.getMaterialId(), 1);
                        map.put(modelMaterialRelevanceDO.getMaterialId() + "_" + modelMaterialRelevanceDO.getModelId(),
                            modelMaterialRelevanceDO);
                    }
                }
                assignMap = newAssignMap;
                if (assignMap.size() == 0) {
                    return null;
                }
            } else {
                for (int x = 0; x < modelMaterialRelevanceDOList.size(); x++) {
                    ModelMaterialRelevanceDO modelMaterialRelevanceDO = modelMaterialRelevanceDOList.get(x);
                    // 排除前面指定了材质id列表的（兑换码指定材质已在上一步做添加）
                    assignMap.put(modelMaterialRelevanceDO.getMaterialId(), 1);
                    map.put(modelMaterialRelevanceDO.getMaterialId() + "_" + modelMaterialRelevanceDO.getModelId(),
                        modelMaterialRelevanceDO);
                }
            }
        }
        // 处理指定材质的
        removeMaterialWithPicture(treeCOList, assignMap, exludeMap);
        if (treeCOList == null || treeCOList.size() == 0 || materialTreeQry.getModelId() == null) {
            return treeCOList;
        }

        recursiveSetModelAndMaterialParam(treeCOList, map, materialTreeQry.getModelId());

        return treeCOList;
    }

    /**
     *
     * @param treeCOList
     * @param parentMap
     * @param assignMaterialMap
     *            指定可用材质集合
     */
    private void recursiveSetParentMsg(List<MaterialTreeCO> treeCOList, Map<Integer, String> parentMap,
        Map<Integer, Integer> assignMaterialMap, Map<Integer, Integer> excludeMap) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return;
        }
        for (int x = 0; x < treeCOList.size(); x++) {
            MaterialTreeCO materialTreeCO = treeCOList.get(x);
            List<MaterialTreeCO> childrenList = materialTreeCO.getChildrenList();
            if (childrenList != null && childrenList.size() > 0) {
                Integer parentId = materialTreeCO.getParentId();
                if (parentId > 0) {
                    // 去掉0
                    String value = parentMap.get(parentId);
                    if (StringUtils.isNotBlank(value)) {
                        // 将之前的父id拼接
                        value = value + "-" + parentId;
                    } else {
                        value = String.valueOf(parentId);
                    }
                    parentMap.put(materialTreeCO.getMaterialId(), value);
                }
                recursiveSetParentMsg(childrenList, parentMap, assignMaterialMap, excludeMap);
            } else {
                // 最低级
                if (assignMaterialMap.containsKey(materialTreeCO.getMaterialId())
                    && !excludeMap.containsKey(materialTreeCO.getMaterialId())) {
                    // 含有这个材质
                    String parentStr = parentMap.get(materialTreeCO.getParentId());
                    if (StringUtils.isNotBlank(parentStr)) {
                        // 加上当前父id
                        parentStr = parentStr + "-" + materialTreeCO.getParentId();
                        String[] parentArray = parentStr.split("-");
                        for (int z = 0; z < parentArray.length; z++) {
                            Integer key = Integer.parseInt(parentArray[z]);
                            if (!excludeMap.containsKey(key)) {
                                assignMaterialMap.put(key, 1);
                            } else {
                                // 最顶级节点在前面、排斥下属节点之后不要继续遍历
                                break;
                            }
                        }
                    } else {
                        // 二级材质 第一级是0
                        assignMaterialMap.put(materialTreeCO.getParentId(), 1);
                    }
                    // 加上当前的
                    assignMaterialMap.put(materialTreeCO.getMaterialId(), 1);
                }
            }
        }
        return;
    }

    /**
     *
     * @param treeCOList
     * @param assignMap
     *            指定可用材质id
     * @param excludeMap
     *            排斥
     */
    private void removeMaterialWithPicture(List<MaterialTreeCO> treeCOList, Map<Integer, Integer> assignMap,
        Map<Integer, Integer> excludeMap) {
        // 先设置型号的父id、key:最低级上一级id、value、上一级id的父id -隔开 （）
        Map<Integer, String> parentMap = new HashMap<>();
        // 移除排斥
        recuriseExclude(excludeMap, treeCOList);
        // 处理指定

        // 处理父节点信息(指定了材质)
        if (assignMap != null && assignMap.size() > 0) {
            recursiveSetParentMsg(treeCOList, parentMap, assignMap, excludeMap);
            recursiveRemoveAssign(treeCOList, assignMap);
        }
        // 递归删掉材质
        recursiveRemove(treeCOList, assignMap);
    }

    /**
     * 递归移除非指定的
     * 
     * @param treeCOList
     * @param usableMap
     * @return
     */
    private List<MaterialTreeCO> recursiveRemoveAssign(List<MaterialTreeCO> treeCOList,
        Map<Integer, Integer> usableMap) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return treeCOList;
        }
        if (usableMap == null || usableMap.size() == 0) {
            return treeCOList;
        }
        // List<ModelTreeCO> allList = new ArrayList<>();
        for (int x = 0; x < treeCOList.size(); x++) {
            MaterialTreeCO materialTreeCO = treeCOList.get(x);
            Integer materialId = usableMap.get(materialTreeCO.getMaterialId());
            List<MaterialTreeCO> childrenList = materialTreeCO.getChildrenList();
            if (materialId == null && (childrenList == null || childrenList.size() == 0)) {
                // 删除
                treeCOList.remove(x);
                x--;
                continue;
            }

            if (childrenList != null && childrenList.size() > 0) {
                childrenList = recursiveRemoveAssign(childrenList, usableMap);
                if (childrenList == null || childrenList.size() == 0) {
                    // 子类都不符合、删除
                    treeCOList.remove(x);
                    x--;
                }
            }
        }

        return treeCOList;
    }

    private void recuriseExclude(Map<Integer, Integer> excludeMap, List<MaterialTreeCO> treeCOList) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return;
        }
        if (excludeMap == null || excludeMap.size() == 0) {
            return;
        }
        // List<ModelTreeCO> allList = new ArrayList<>();
        for (int x = 0; x < treeCOList.size(); x++) {
            MaterialTreeCO materialTreeCO = treeCOList.get(x);
            if (excludeMap.containsKey(materialTreeCO.getMaterialId())) {
                // 不可用
                treeCOList.remove(x);
                x--;
                continue;
            }
            List<MaterialTreeCO> childrenList = materialTreeCO.getChildrenList();

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

    /**
     * 移除没有指定材质列表里面的材质节点
     * 
     * @param treeCOList
     *            材质树
     * @param assignMaterialMap
     *            指定材质id
     * @return
     */
    private List<MaterialTreeCO> recursiveRemove(List<MaterialTreeCO> treeCOList,
        Map<Integer, Integer> assignMaterialMap) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return treeCOList;
        }
        // List<ModelTreeCO> allList = new ArrayList<>();
        for (int x = 0; x < treeCOList.size(); x++) {
            MaterialTreeCO materialTreeCO = treeCOList.get(x);
            Integer materialId = assignMaterialMap.get(materialTreeCO.getMaterialId());
            List<MaterialTreeCO> childrenList = materialTreeCO.getChildrenList();
            if (materialId == null && assignMaterialMap != null && assignMaterialMap.size() > 0) {
                // 删除
                treeCOList.remove(x);
                x--;
                continue;
            }
            if (materialId == null && (assignMaterialMap == null || assignMaterialMap.size() == 0)
                && (childrenList == null || childrenList.size() == 0)
                && FlexibleCommonConstant.AT_LAST_TRADEMARK_NO.equals(materialTreeCO.getAtLastTrademark())) {
                // 删除
                treeCOList.remove(x);
                x--;
                continue;
            }
            if (childrenList != null && childrenList.size() > 0) {
                recursiveRemove(childrenList, assignMaterialMap);
            }
        }

        return treeCOList;
    }

    private void recursiveSetModelAndMaterialParam(List<MaterialTreeCO> treeCOList,
        Map<String, ModelMaterialRelevanceDO> map, Integer modelId) {
        if (treeCOList == null || treeCOList.size() == 0) {
            return;
        }
        List<MaterialTreeCO> coList = new ArrayList<>();
        for (int x = 0; x < treeCOList.size(); x++) {
            MaterialTreeCO materialTreeCO = treeCOList.get(x);
            List<MaterialTreeCO> childrenList = materialTreeCO.getChildrenList();
            if (childrenList == null || childrenList.size() == 0) {
                // 最低级
                String key = materialTreeCO.getMaterialId() + "_" + modelId;
                ModelMaterialRelevanceDO relevanceDO = map.get(key);
                if (relevanceDO != null) {
                    materialTreeCO.setLength(relevanceDO.getLength());
                    materialTreeCO.setCrosscuttingWhite(relevanceDO.getCrosscuttingWhite());
                    materialTreeCO.setCrosscuttingWidth(relevanceDO.getCrosscuttingWidth());
                    materialTreeCO.setCutType(relevanceDO.getCutType());
                    materialTreeCO.setFloorImage(relevanceDO.getFloorImage());
                    materialTreeCO.setOutImage(relevanceDO.getOutImage());
                    materialTreeCO.setSlittingHeight(relevanceDO.getSlittingHeight());
                    materialTreeCO.setSlittingWhite(relevanceDO.getSlittingWhite());
                    materialTreeCO.setUnderStockFlag(relevanceDO.getUnderStockFlag());
                    materialTreeCO.setWidth(relevanceDO.getWidth());
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
            recursiveSetModelAndMaterialParam(coList, map, modelId);
        } else {
            return;
        }

    }

    public List<MaterialDO> listAll() {
        return materialDOMapper.listAll();
    }

    public List<MaterialDO> listByCondition(Short atLastTrademark, Integer categoryId, Integer parentId, Short openFlag,
        String content) {
        return materialDOMapper.listByCondition(atLastTrademark, categoryId, parentId, openFlag, content);
    }

    public List<MaterialSimpleTreeCO> treeAdmin(MaterialTreeAdminQry materialTreeAdminQry) {
        return materialDOMapper.treeAdmin(materialTreeAdminQry.getParentId(), materialTreeAdminQry.getOpenFlag(),

            materialTreeAdminQry.getCategoryId(), materialTreeAdminQry.getAtLastTrademark());
    }

    public MaterialDO getByItemId(Integer itemId) {
        return materialDOMapper.getByItemId(itemId);
    }

    public List<MaterialDO> listByItemIdList(List<Integer> itemIdList) {
        return materialDOMapper.listByItemIdList(itemIdList);
    }

    /**
     * 查询材质关联的所有货品id列表
     * 
     * @return
     */
    public List<MaterialDO> listAllGroupByItemId() {
        return materialDOMapper.listAllGroupByItemId();
    }

    public MaterialDO getByItemCode(String itemCode) {
        return materialDOMapper.getByItemCode(itemCode);
    }

    public List<MaterialDO> findCanUseParentsByDistributorIds(List<Integer> distributorIds) {
        return materialDOMapper.findCanUseParentsByDistributorIds(distributorIds);
    }

    public List<MaterialDO> listByParentIdsAndDistributorIds(List<Integer> parentIds, List<Integer> distributorIds) {
        if (parentIds.size() == 0) {
            return new ArrayList<>();
        }
        return materialDOMapper.listByParentIds(parentIds, distributorIds);
    }

    public List<MaterialDO> findByIdsAndModelIdAndPictureId(List<Integer> ids, Integer modelId, Integer pictureId) {
        if(ids.size()==0){
            return new ArrayList<>();
        }
        return materialDOMapper.findByIdsAndModelIdAndPictureId(ids, modelId, pictureId);
    }
}
