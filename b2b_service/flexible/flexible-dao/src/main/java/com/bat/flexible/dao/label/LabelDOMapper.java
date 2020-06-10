package com.bat.flexible.dao.label;

import com.bat.flexible.dao.label.co.LabelPageListQry;
import com.bat.flexible.dao.label.dataobject.LabelDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LabelDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LabelDO record);

    int insertSelective(LabelDO record);

    LabelDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LabelDO record);

    int updateByPrimaryKey(LabelDO record);

    List<LabelDO> listByLabelIdList(@Param("labelIdList") List<Integer> labelIdList);


    List<LabelPageListQry> listByCondition(@Param("type") Short type, @Param("openFlag") Short openFlag, @Param("content") String content);

    /**
     * 查询可用标签列表
     * @param distributorIds
     * @param categoryId
     * @param distributorScope
     * @return
     */
    List<LabelDO> listUsableDiyLabelByCondition(@Param("distributorIds") List<Integer> distributorIds,@Param("categoryId") Integer categoryId,
                                          @Param("distributorScope") Short distributorScope, @Param("pictureId") Integer pictureId);


    /**
     * 查询可用标签列表(优先查分销商指定)
     * @param distributorIds
     * @param categoryId
     * @param distributorScope
     * @return
     */
    List<LabelDO> listUsableDiyLabelByConditionAndDistributorIds(@Param("distributorIds") List<Integer> distributorIds,@Param("categoryId") Integer categoryId,
                                                @Param("distributorScope") Short distributorScope, @Param("pictureId") Integer pictureId);


}