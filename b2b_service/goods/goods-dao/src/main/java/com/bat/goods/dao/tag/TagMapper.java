package com.bat.goods.dao.tag;

import java.util.List;
import java.util.Map;

import com.bat.goods.dao.tag.dataobject.TagDO;
import com.bat.goods.dao.goods.dataobject.GoodsTagDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TagMapper {

    Integer createTag(TagDO goodsTagDO);

    List<TagDO> listTag(Map map);

    Integer listCount(@Param("content") String content);

    TagDO getById(@Param("id") Integer id);

    Integer updateTag(TagDO goodsTagDO);

    void deleteTag(@Param("id") Integer id);

    Integer getTagGoodsCount(@Param("tagId") Integer tagId);

    void openTag(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    List<GoodsTagDO> listByGoodsIds(@Param("goodsIds")List<Integer> goodsIds);
}
