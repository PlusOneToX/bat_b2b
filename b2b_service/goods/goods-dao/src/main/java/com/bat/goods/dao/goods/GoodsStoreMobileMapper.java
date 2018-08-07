package com.bat.goods.dao.goods;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.goods.dataobject.GoodsStoreMobileDO;
import com.bat.goods.dao.goods.dataobject.UserGoodsListDO;

@Mapper
public interface GoodsStoreMobileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsStoreMobileDO record);

    GoodsStoreMobileDO selectByPrimaryKey(Integer id);

    List<GoodsStoreMobileDO> selectAll();

    int updateByPrimaryKey(GoodsStoreMobileDO record);

    void createGoodsStoreMobileList(List<GoodsStoreMobileDO> goodsStoreMobileDOS);

    void updateGoodsStoreMobileList(List<GoodsStoreMobileDO> goodsStoreMobileDOS);

    void updateGoodsStoreMobileSort(Integer sort);

    List<GoodsStoreMobileDO> listGoodsStoreMobile(@Param("mobileId") Integer mobileId);

    void deleteGoodsStoreMobileList(List<GoodsStoreMobileDO> goodsStoreMobileDOS);

    void deleteGoodsStoreMobileByMobileId(@Param("mobileId") Integer mobileId);

    List<UserGoodsListDO> listUserGoodsListByMobile(Map map);

    List<UserGoodsListDO> listUserGoodsListByMobileAndClassify(Map map);

    List<UserGoodsListDO> listGoodsListByMobile(Map map);

    List<UserGoodsListDO> listGoodsListByMobileAndClassify(Map map);
}