package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.co.PictureCategoryPageCO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.dao.picture.co.PictureCategorySimpleTreeCO;
import com.bat.flexible.dao.picture.co.PictureCategoryTreeCO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PictureCategoryDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureCategoryDO record);

    int insertSelective(PictureCategoryDO record);

    PictureCategoryDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureCategoryDO record);

    int updateByPrimaryKeyWithBLOBs(PictureCategoryDO record);

    int updateByPrimaryKey(PictureCategoryDO record);

    void updateSequenceAddByParentId(@Param("parentId") Integer parentId, @Param("sequenceAdd") BigDecimal sequenceAdd, @Param("sequenceStart")BigDecimal sequenceStart);

    List<PictureCategoryDO> listByParentIdAndOpenFlag(@Param("parentId")Integer parentId,@Param("openFlag")Short openFlag);

    List<PictureCategoryPageCO> treeCOByParentId(@Param("parentId")Integer parentId);

    PictureCategoryDO findEffectByUpOrDown(@Param("parentId")Integer parentId,@Param("flag") Boolean flag,@Param("sequence")BigDecimal sequence);

    List<PictureCategoryDO> listByCondition(@Param("openFlag")Short openFlag);

    /**
     * 根据父id获取树图片分类列表
     * @param parentId
     * @return
     */
    List<PictureCategoryTreeCO> treeByParentId(@Param("parentId")Integer parentId);

    /**
     *
     * @param openFlag
     * @param parentId
     * @return
     */
    List<PictureCategorySimpleTreeCO> treeSimple(@Param("openFlag") Short openFlag,@Param("parentId") Integer parentId);

    List<PictureCategoryDO> listByPictureThemeId(@Param("themeId") Integer themeId);

    List<PictureCategoryDO> listByTypeAndDistributorIdsAndMaterialIdAndModelId(@Param("type") Short type,
                                                                               @Param("distributorIds") List<Integer> distributorIds, @Param("isSanXing") boolean isSanXing,
                                                                               @Param("countryId") Integer countryId, @Param("materialId") Integer materialId, @Param("modelId") Integer modelId);

    List<PictureCategoryDO> listByIdList(@Param("categoryIdList") List<Integer> categoryIdList);

    List<PictureCategoryDO> listByParentId(@Param("parentId") Integer parentId);
}