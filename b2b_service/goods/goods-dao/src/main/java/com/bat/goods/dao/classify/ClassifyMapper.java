package com.bat.goods.dao.classify;

import java.util.List;
import java.util.Map;

import com.bat.goods.dao.classify.dataobject.ClassifyDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ClassifyMapper {

    Integer createClassify(ClassifyDO classifyDO);

    List<ClassifyDO> listClassify(Map map);

    List<ClassifyDO> listClassifyByParentId(@Param("parentId") Integer parentId, @Param("openFlag") Short openFlag);

    List<ClassifyDO> listSubClassifyByClassifyIds(Map map);

    Integer listCount(@Param("content") String content);

    List<ClassifyDO> listSubClassify(Map map);

    ClassifyDO getById(@Param("id") Integer id);

    Integer updateClassify(ClassifyDO classifyDO);

    void openClassify(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    void deleteClassify(@Param("id") Integer id);

    Integer getSubClassifyCount(@Param("id") Integer classifyId);

    Integer getClassifyGoodsCount(@Param("classifyId") Integer classifyId);

    void changeClassifyOpen(@Param("id") Integer id);

    List<ClassifyDO> listRecommend(@Param("openFlag") Short openFlag);

    void closeAllRecommend();

    void updateRecommendByIds(@Param("classifyIds") List<Integer> classifyIds);

    List<ClassifyDO> listByIds(@Param("classifyIds")List<Integer> classifyIds);
}
