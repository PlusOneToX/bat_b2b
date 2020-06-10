package com.bat.flexible.api.model;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.model.dto.rela.ModelMaterialRelevanceDTO;
import com.bat.flexible.api.model.dto.rela.ModelMaterialRelevancePageQry;
import com.bat.flexible.api.model.dto.rela.ModelMaterialRelevanceSkuNoAndBomCmd;
import com.bat.flexible.dao.model.co.ModelMaterialRelevanceCO;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

public interface ModelMaterialRelevanceServiceI {
    /**
     * @param fromModel 操作来源 来源型号true 来源材质false
     * @param materialRelevanceDTOList
     * @param currentAdmin
     * @param isAdd
     */
    void saveModelMaterialRele(Boolean fromModel, List<ModelMaterialRelevanceDTO> materialRelevanceDTOList, AdminResponse currentAdmin, Boolean isAdd);

    /**
     * 根据型号和材质id查询关联关系
     * @param modelId
     * @param materialId
     * @return
     */
    List<ModelMaterialRelevanceCO> listCOByModelIdAndMaterialId(Integer modelId, Integer materialId);

    /**
     *
     * @param modelMaterialRelevancePageQry
     * @return
     */
    PageInfo<ModelMaterialRelevanceCO> page(ModelMaterialRelevancePageQry modelMaterialRelevancePageQry);

    /**
     * 修改sku编码和bom编码
     * @param relevanceSkuNoAndBomCmd
     * @param currentAdmin
     * @return
     */
    Response updateSkuAndBomCode(ModelMaterialRelevanceSkuNoAndBomCmd relevanceSkuNoAndBomCmd, AdminResponse currentAdmin);

    /**
     * 启用禁用
     * @param flexibleUpdateStatusDTO
     * @param currentAdmin
     * @return
     */
    Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin);

    /**
     * 删除
     * @param id
     * @param currentAdmin
     * @return
     */
    Response deleteById(Integer id, AdminResponse currentAdmin);

    /**
     * 校验型号和材质的关联列表参数
     * @param fromModel
     * @param materialRelevanceDTOList
     * @param isAdd
     * @param id
     */
    void checkModelMaterialRelevanceList(Boolean fromModel, List<ModelMaterialRelevanceDTO> materialRelevanceDTOList, Boolean isAdd, Integer id);


    /**
     * 根据材质、型号id、状态查询
     * @param materialId
     * @param modelId
     * @param openFlag
     * @return
     */
    List<ModelMaterialRelevanceDO> listByCondition(Integer materialId, Integer modelId, Short openFlag,String bomCode);

    /**
     * 根据型号id和材质id查询
     * @param modelId 型号id
     * @param materialId 材质id
     * @param needUsable true 不校验状态 false 要判断可用
     * @return
     */
    Response getByModelIdAndMaterialId(Integer modelId, Integer materialId,boolean needUsable);

    /**
     * 根据材质id列表查询关联关系
     * @param materialIdList
     * @return
     */
    List<ModelMaterialRelevanceDO> listByMaterialIdListAndOpenFlag(List<Integer> materialIdList,Short openFlag);

    /**
     * 导出型号材质关系
     * @return
     */
    HSSFWorkbook exportExcel();

    /**
     * 删除材质和型号的关联关系
     * @param materialId 材质id
     * @param modelId 型号id
     * @param currentAdmin
     */
    void deleteByMaterialIdAndModelId(Integer materialId, Integer modelId, AdminResponse currentAdmin);
}
