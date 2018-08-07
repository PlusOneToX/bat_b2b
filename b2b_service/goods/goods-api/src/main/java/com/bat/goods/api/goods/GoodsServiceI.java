package com.bat.goods.api.goods;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.goods.dto.*;
import com.bat.goods.api.goods.dto.data.*;
import com.bat.goods.api.base.BaseIds;
import com.bat.goods.api.goods.dto.*;
import com.bat.goods.api.goods.dto.data.*;
import com.bat.goods.dao.goods.dataobject.GoodsItemDO;

public interface GoodsServiceI {
    /**
     * 添加新商品
     * 
     * @param cmd
     * @return
     */
    void createGoods(GoodsCmd cmd);

    /**
     * 更新商品
     * 
     * @param cmd
     * @return
     */
    void updateGoods(GoodsCmd cmd);

    /**
     * 查询商品（SPU）列表（分页）
     * 
     * @param qry
     * @return
     */
    PageInfo<GoodsListDTO> listGoods(GoodsListQry qry);

    /**
     * 查询货品（SKU）列表（分页）
     *
     * @param qry
     * @return
     */
    PageInfo<GoodsItemListDTO> listGoodsItem(GoodsItemListQry qry);

    /**
     * 根据ID删除商品
     * 
     * @param cmd
     * @return
     */
    void deleteGoods(GoodsId cmd);

    /**
     * 根据商品id获取详情
     * 
     * @param qry
     * @return
     */
    GoodsDTO getGoods(GoodsId qry);

    /**
     * 上下架商品
     *
     * @param cmd
     * @return
     */
    void saleStatusGoods(GoodsSaleStatusCmd cmd);

    /**
     * 上下架货品（SKU）
     *
     * @param cmd
     * @return
     */
    void saleStatusGoodsItem(GoodsItemSaleStatusCmd cmd);

    /**
     * 冻结解冻商品
     *
     * @param cmd
     * @return
     */
    void freezeStatusGoods(GoodsFreezeStatusCmd cmd);

    /**
     * 查找erp货品列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<GoodsItemErpDTO> listGoodsItemErp(GoodsItemListErpQry qry, String userId);

    /**
     * 设置是否按装箱数量售卖
     * 
     * @param cmds
     */
    void updateGoodsItemBoxs(List<GoodsItemBoxCmd> cmds);

    /**
     * 获取货品(SKU)装箱信息列表
     * 
     * @param qry
     * @return
     */
    List<GoodsItemBoxDTO> listGoodsItemBox(GoodsItemId qry);

    /**
     * 根据栏目id获取商品列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<GoodsListStoreDTO> columnGoodsList(GoodsListColumnQry qry);

    /**
     * 根据板块id获取商品列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<GoodsListStoreDTO> sectionGoodsList(GoodsListSectionQry qry);

    /**
     * 根据移动端配置id获取商品列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<GoodsListStoreDTO> mobileGoodsList(GoodsListMobileQry qry);

    /**
     * 根据移动端配置ids获取商品列表
     *
     * @param qry
     * @return
     */
    List<GoodsListStoresDTO> mobileGoodsList(GoodsListMobilesQry qry);

    /**
     * 根据分类ids获取商品列表
     * 
     * @param qry
     * @return
     */
    List<GoodsListDTO> goodsListByClassifyIds(BaseIds qry);

    /**
     * 根据商品ids获取商品列表
     * 
     * @param qry
     * @return
     */
    List<GoodsListDTO> goodsListByIds(BaseIds qry);

    List<GoodsItemDO> listAllItem();

    /**
     * 分页查询分销商指定关联的货品列表
     * 
     * @param goodsQuery
     * @return
     */
    PageInfo<GoodsItemSimpleDTO> pageAssignByDistributorId(DistributorGoodsQuery goodsQuery);

    /**
     * 分页查询货品列表（定制）
     * 
     * @param diyGoodsPageQry
     * @return
     */
    PageInfo<GoodsItemSimpleDTO> pageByGoodsTypeAndDiyType(DiyGoodsPageQry diyGoodsPageQry);

    /**
     * 同步商品价格
     * 
     * @param cmd
     */
    void syncBatchGoodsPrice(BaseIds cmd);

    /**
     * 全量同步商品价格
     *
     * @param
     */
    void syncAllGoodsPrice();

    /**
     * 同步货品信息
     *
     * @param cmd
     */
    void syncBatchGoodsItem(BaseIds cmd);

    /**
     * 全量同步货品信息
     *
     * @param
     */
    void syncAllGoodsItem();

    /**
     * 同步货品装箱信息
     *
     * @param cmd
     */
    void syncBatchGoodsItemBox(BaseIds cmd);

    /**
     * 全量同步货品装箱信息
     *
     * @param
     */
    void syncAllGoodsItemBox();

    /**
     * 刷新商品最小最大价格
     */
    void allGoodsPrice();

    /**
     * 刷新商品可视范围
     */
    void allGoodsScope();
}
