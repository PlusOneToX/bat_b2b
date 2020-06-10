package com.bat.flexible.dao.model;

import com.bat.flexible.dao.model.co.ModelPageCO;
import com.bat.flexible.dao.model.co.ModelSimpleCO;
import com.bat.flexible.dao.model.co.ModelTreeCO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ModelDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ModelDO record);

    int insertSelective(ModelDO record);

    ModelDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ModelDO record);

    int updateByPrimaryKeyWithBLOBs(ModelDO record);

    int updateByPrimaryKey(ModelDO record);


    List<ModelDO> listByModelIdList(@Param("modelIdList") List<Integer> modelIdList);


    void setSequenceAddByCategoryIdAndParentId(@Param("categoryId")Integer categoryId,@Param("parentId") Integer parentId,@Param("sortNoAdd")Integer sortNoAdd,
                                               @Param("sequenceStart")Integer sequenceStart,@Param("sequenceEnd")Integer sequenceEnd);

    ModelDO findEffectByUpOrDown(@Param("parentId") Integer parentId,@Param("flag") Boolean flag,@Param("sequence") Integer sequence);

    List<ModelDO> listByParentId(@Param("parentId")Integer parentId);

    ModelDO getByModelNo(@Param("modelNo")String modelNo);

    List<ModelPageCO> treeByCondition(@Param("parentId") Integer parentId, @Param("categoryId")  Integer categoryId);

    List<ModelTreeCO> tree(@Param("categoryId") Integer categoryId, @Param("distributorId") Integer distributorId, @Param("materialId") Integer materialId,
                           @Param("pictureId") Integer pictureId);

    List<ModelTreeCO> treeByParentIdAndCategoryIdAndOpenFlag(@Param("parentId") Integer parentId, @Param("categoryId")Integer categoryId,@Param("openFlagParam")Short openFlagParam
                                                                );

    List<ModelDO> listAll();

    List<ModelDO> listAllCanUseParentsByDistributorIds(@Param("distributorIds")List<Integer> distributorIds);

    List<ModelSimpleCO> listSimpleCO(@Param("content") String content, @Param("atLastTrademark") Short atLastTrademark, @Param("modelIdList") List<Integer> modelIdList);

    List<ModelDO> listByCondition(@Param("atLastTrademark") Short atLastTrademark,@Param("openFlag")Short openFlag);

    ModelDO getOneByNetworkModel(@Param("networkModel") String networkModel);

    List<ModelDO> listByParentIdsAndDistributorIdsAndMaterialId(@Param("parents")List<Integer> parents, @Param("distributorIds")List<Integer> distributorIds);

    List<ModelDO> findByIdsAndMaterialIdAndPictureId(@Param("ids")List<Integer> ids, @Param("materialId")Integer materialId, @Param("pictureId")Integer pictureId);
}