package com.bat.goods.api.user;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.user.dto.*;
import com.bat.goods.api.user.dto.data.*;
import com.bat.goods.api.base.BaseIds;
import com.bat.goods.api.tag.dto.data.TagsDTO;
import com.bat.goods.api.user.dto.*;
import com.bat.goods.api.user.dto.data.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/27 18:49
 */
public interface UserServiceI {
    /**
     * 获取商品分类列表(不分页)
     * 
     * @param qry
     * @return
     */
    UserClassifyInfoDTO listClassify(UserClassifyListQry qry);

    /**
     * 根据父级分类id获取下级分类列表(不分页)
     *
     * @param qry
     * @return
     */
    List<UserClassifyDTO> listSubClassify(UserSubClassifyListQry qry);

    /**
     * 根据商品ids获取商品价格列表
     * 
     * @param userId
     * @return
     */
    List<UserGoodsPriceDTO> priceGoodsList(UserGoodsPriceQry qry, String userId);

    /**
     * 根据货品ids获取货品价格列表
     * 
     * @param qry
     * @param userId
     * @return
     */
    List<UserGoodsItemPriceDTO> priceGoodsItemList(UserGoodsItemPriceQry qry, String userId);

    /**
     * 根据栏目获取商品列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<UserGoodsListDTO> columnGoodsList(UserGoodsListColumnQry qry, String userId);

    /**
     * 根据板块获取商品列表(分页)
     *
     * @param qry
     * @return
     */
    PageInfo<UserGoodsListDTO> sectionGoodsList(UserGoodsListSectionQry qry, String userId);

    /**
     * 根据移动端配置id获取商品列表(分页)
     *
     * @param qry
     * @return
     */
    PageInfo<UserGoodsListDTO> mobileGoodsList(UserGoodsListMobileQry qry, String userId);

    /**
     * 商品列表查询
     * 
     * @param qry
     * @return
     */
    PageInfo<UserGoodsListDTO> goodsList(UserGoodsListQry qry, String userId);

    /**
     * 根据栏目id获取商品分类列表(不分页)
     * 
     * @param qry
     * @return
     */
    List<UserClassifyDTO> columnClassifyList(UserClassifyListColumnQry qry);

    /**
     * 根据板块id获取商品分类列表(不分页)
     * 
     * @param qry
     * @return
     */
    List<UserClassifyDTO> sectionClassifyList(UserClassifyListSectionQry qry);

    /**
     * 根据移动端配置id获取商品分类列表(不分页)
     * 
     * @param qry
     * @return
     */
    List<UserClassifyDTO> mobileClassifyList(UserClassifyListMobileQry qry);

    /**
     * 根据商品id获取商品详情
     * 
     * @param qry
     * @return
     */
    UserGoodsDTO goods(UserGoodsId qry);

    /**
     * 根据商品id查询商品收藏状态
     * 
     * @param qry
     * @param userId
     * @return
     */
    Boolean getGoodsCollection(UserGoodsId qry, String userId);

    /**
     * 商品收藏
     * 
     * @param cmd
     */
    void createGoodsCollection(UserGoodsId cmd, String userId);

    /**
     * 商品收藏删除
     * 
     * @param cmd
     */
    void deleteGoodsCollection(UserGoodsCollection cmd, String userId);

    List<TagsDTO> getGoodsLabelByGoodsIds(BaseIds qry);
}
