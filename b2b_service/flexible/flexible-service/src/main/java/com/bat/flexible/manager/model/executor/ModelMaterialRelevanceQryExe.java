package com.bat.flexible.manager.model.executor;

import com.alicp.jetcache.anno.Cached;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.model.ModelMaterialRelevanceDOMapper;
import com.bat.flexible.dao.model.co.ModelMaterialRelevanceCO;
import com.bat.flexible.dao.model.co.ModelSkuExportQry;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.model.ModelMaterialRelevanceErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModelMaterialRelevanceQryExe {


    private static final Logger LOGGER = LoggerFactory.getLogger(ModelMaterialRelevanceQryExe.class);

    @Autowired
    private ModelMaterialRelevanceDOMapper modelMaterialRelevanceDOMapper;

    /**
     * 根据id列表查询
     *
     * @param idList
     * @return
     */
    public List<ModelMaterialRelevanceDO> listByIdList(List<Integer> idList) {
        if (idList == null || idList.size() == 0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_LIST_NULL);
        }
        List<ModelMaterialRelevanceDO> relevanceDOList = modelMaterialRelevanceDOMapper.listByIdList(idList);
        if (relevanceDOList == null || relevanceDOList.size() == 0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        if (relevanceDOList.size() - idList.size() == 0) {
            return relevanceDOList;
        }
        for (int x = 0; x < idList.size(); x++) {
            Boolean flag = false;
            for (int y = 0; y < relevanceDOList.size(); y++) {
                if (idList.get(x) - relevanceDOList.get(y).getId() == 0) {
                    flag = true;
                    if (FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(relevanceDOList.get(y).getDelFlag())) {
                        throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY);
                    }
                    break;
                }
            }
            if (!flag) {
                LOGGER.error("id查询无数据{}", idList.get(x));
                throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
            }
        }
        return null;
    }

    public List<ModelMaterialRelevanceDO> listByCondition(Integer materialId, Integer modelId, Short openFlag, String bomCode) {
        return modelMaterialRelevanceDOMapper.listByCondition(materialId, modelId, openFlag, bomCode);
    }


    public List<ModelMaterialRelevanceCO> listCOByCondition(Integer categoryId, Integer materialId, Short openFlag, String content) {
        return modelMaterialRelevanceDOMapper.listCOByCondition(categoryId, materialId, openFlag, content);
    }

    public ModelMaterialRelevanceDO getById(Integer id) {
        if (id == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        ModelMaterialRelevanceDO modelMaterialRelevanceDO = modelMaterialRelevanceDOMapper.selectByPrimaryKey(id);
        if (modelMaterialRelevanceDO == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        if (FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(modelMaterialRelevanceDO.getDelFlag())) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY);
        }
        return modelMaterialRelevanceDO;
    }

    @Cached(name = FlexibleKeyConstant.PICTURE_MODEL_MATERIAL_RELEVANCE_DO_PRE, key = "#modelId+'_'+#materialId")
    public ModelMaterialRelevanceDO getByModelIdAndMaterialId(Integer modelId, Integer materialId, boolean needUsable) {
        List<ModelMaterialRelevanceDO> relevanceDOList = modelMaterialRelevanceDOMapper.listByCondition(materialId, modelId, null, null);
        if (relevanceDOList == null || relevanceDOList.size() == 0) {
            throw new FlexibleCustomException(ModelMaterialRelevanceErrorCode.M_MODEL_MATERIAL_NO_CORRELATION);
        }
        if (!needUsable && FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(relevanceDOList.get(0).getOpenFlag())) {
            //需要校验可用
            throw new FlexibleCustomException(ModelMaterialRelevanceErrorCode.M_MODEL_MATERIAL_STATUS_DISABLED);
        }
        return relevanceDOList.get(0);
    }


    public List<ModelMaterialRelevanceDO> listByMaterialIdListAndOpenFlag(List<Integer> materialIdList, Short openFlag) {
        return modelMaterialRelevanceDOMapper.listByMaterialIdListAndOpenFlag(materialIdList, openFlag);
    }

    /**
     * 查询所有的excel数据
     *
     * @return
     */
    public List<ModelSkuExportQry> listExcelCO() {
        return modelMaterialRelevanceDOMapper.listExcelCO();
    }
}
