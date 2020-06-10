package com.bat.flexible.dao.model;

import com.bat.flexible.dao.model.co.ModelMaterialRelevanceCO;
import com.bat.flexible.dao.model.co.ModelSkuExportQry;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModelMaterialRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ModelMaterialRelevanceDO record);

    int insertSelective(ModelMaterialRelevanceDO record);

    ModelMaterialRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ModelMaterialRelevanceDO record);

    int updateByPrimaryKey(ModelMaterialRelevanceDO record);

    /**
     * 根据id列表查询
     *
     * @param idList
     * @return
     */
    List<ModelMaterialRelevanceDO> listByIdList(@Param("idList") List<Integer> idList);


    List<ModelMaterialRelevanceDO> listByCondition(@Param("materialId") Integer materialId, @Param("modelId") Integer modelId,
                                                   @Param("openFlag") Short openFlag, @Param("bomCode") String bomCode);

    List<ModelMaterialRelevanceCO> listCOByCondition(@Param("productCategoryId") Integer productCategoryId, @Param("materialId") Integer materialId,
                                                     @Param("openFlag") Short openFlag, @Param("content") String content);

    List<ModelMaterialRelevanceDO> listByMaterialIdListAndOpenFlag(@Param("materialIdList") List<Integer> materialIdList, @Param("openFlag") Short openFlag);

    /**
     * 查询所有的excel数据
     *
     * @return
     */
    List<ModelSkuExportQry> listExcelCO();
}