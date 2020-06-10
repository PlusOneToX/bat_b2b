package com.bat.flexible.dao.material;


import com.bat.flexible.dao.material.co.MaterialPageCO;
import com.bat.flexible.dao.material.co.MaterialSimpleTreeCO;
import com.bat.flexible.dao.material.co.MaterialTreeCO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface MaterialDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MaterialDO record);

    int insertSelective(MaterialDO record);

   // @Cached(name = "FLEXIBLE_MATERIAL_DO_",key = "#id")
    MaterialDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MaterialDO record);

    int updateByPrimaryKeyWithBLOBs(MaterialDO record);

    int updateByPrimaryKey(MaterialDO record);
    List<MaterialDO> listByMaterialIdList(@Param("materialIdList") List<Integer> materialIdList);

    MaterialDO getByMaterialNo(@Param("materialNo") String materialNo);

    MaterialDO findEffectByUpOrDown(@Param("parentId") Integer parentId,@Param("flag") Boolean flag,@Param("sequence") Integer sequence);

    List<MaterialDO> listByParentId(@Param("parentId")Integer parentId);

    Integer findMaxByParentId(@Param("parentId")Integer parentId);

    List<MaterialPageCO> treeByCondition(@Param("categoryId") Integer categoryId, @Param("parentId") Integer parentId);

    List<MaterialTreeCO> treeUsableCOByParentIdAndCategoryId(@Param("parentId") Integer parentId, @Param("categoryId") Integer categoryId);

    List<MaterialDO> listAll();

    List<MaterialDO> listByCondition(@Param("atLastTrademark") Short atLastTrademark,@Param("categoryId") Integer categoryId,@Param("parentId") Integer parentId,
                                     @Param("openFlag") Short openFlag,@Param("content") String content);

    List<MaterialSimpleTreeCO> treeAdmin(@Param("parentId") Integer parentId, @Param("openFlag") Short openFlag, @Param("categoryId") Integer categoryId,
                                         @Param("atLastTrademark") Short atLastTrademark);

    MaterialDO getByItemId(@Param("itemId") Integer itemId);

    List<MaterialDO> listByItemIdList(@Param("itemIdList") List<Integer> itemIdList);

    List<MaterialDO> listAllGroupByItemId();

    MaterialDO getByItemCode(@Param("itemCode") String itemCode);

    List<MaterialDO> findCanUseParentsByDistributorIds(@Param("distributorIds")List<Integer> distributorIds);

 List<MaterialDO> listByParentIds(@Param("parentIds")List<Integer> parentIds,@Param("distributorIds") List<Integer> distributorIds);

 List<MaterialDO> findByIdsAndModelIdAndPictureId(@Param("ids")List<Integer> ids, @Param("modelId")Integer modelId, @Param("pictureId")Integer pictureId);
}