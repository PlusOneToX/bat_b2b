package com.bat.goods.dao.goods;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.goods.dataobject.GoodsStoreColumnDO;
import com.bat.goods.dao.goods.dataobject.GoodsStoreSectionDO;
import com.bat.goods.dao.goods.dataobject.UserGoodsListDO;

@Mapper
public interface GoodsStoreMapper {

    void createGoodsStoreColumnList(List<GoodsStoreColumnDO> goodsStoreColumnDOS);

    void createGoodsStoreSectionList(List<GoodsStoreSectionDO> goodsStoreSectionDOS);

    void updateGoodsStoreColumnList(List<GoodsStoreColumnDO> goodsStoreColumnDOS);

    void updateGoodsStoreSectionList(List<GoodsStoreSectionDO> goodsStoreSectionDOS);

    void updateGoodsStoreColumnSort(Integer sort);

    void updateGoodsStoreSectionSort(Integer sort);

    List<GoodsStoreColumnDO> listGoodsStoreColumn(@Param("columnId") Integer columnId);

    List<GoodsStoreSectionDO> listGoodsStoreSection(@Param("sectionId") Integer sectionId);

    void deleteGoodsStoreColumnList(List<GoodsStoreColumnDO> goodsStoreColumnDOS);

    void deleteGoodsStoreSectionList(List<GoodsStoreSectionDO> goodsStoreSectionDOS);

    void deleteGoodsStoreColumnByColumnId(@Param("columnId") Integer columnId);

    void deleteGoodsStoreSectionBySectionId(@Param("sectionId") Integer sectionId);

    List<UserGoodsListDO> listUserGoodsListByColumn(Map map);

    List<UserGoodsListDO> listUserGoodsListByColumnAndClassify(Map map);

    List<UserGoodsListDO> listUserGoodsListBySection(Map map);

    List<UserGoodsListDO> listUserGoodsListBySectionAndClassify(Map map);

    List<UserGoodsListDO> listGoodsListByColumn(Map map);

    List<UserGoodsListDO> listGoodsListByColumnAndClassify(Map map);

    List<UserGoodsListDO> listGoodsListBySection(Map map);

    List<UserGoodsListDO> listGoodsListBySectionAndClassify(Map map);
}
