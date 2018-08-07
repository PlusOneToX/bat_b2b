package com.bat.goods.web.goods;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.goods.api.base.BaseIds;
import com.bat.goods.api.goods.GoodsServiceI;
import com.bat.goods.api.goods.dto.*;
import com.bat.goods.api.goods.dto.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.bat.goods.api.goods.dto.*;
import com.bat.goods.api.goods.dto.data.*;
import com.bat.goods.dao.goods.dataobject.GoodsItemDO;
import com.bat.goods.web.annotation.SysLog;
import com.bat.goods.web.base.BaseController;
import com.bat.goods.web.base.Response;
import com.bat.goods.web.constants.CommonLogTypeConstantDTO;
import com.bat.goods.web.constants.CommonLogTypeTitleConstantDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "商品后台接口", value = "AdminGoodsController")
@RestController
@RequestMapping("/goods/v1/web/admin/goods")
public class GoodsController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private GoodsServiceI service;

    @ApiOperation(value = "根据搜索条件查找商品列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<GoodsListDTO>> listGoods(@Valid GoodsListQry qry) {
        PageInfo<GoodsListDTO> pageInfo = service.listGoods(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找商品列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<GoodsListDTO>> listGoodsPo(@Valid GoodsListQry qry) {
        PageInfo<GoodsListDTO> pageInfo = service.listGoods(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找货品列表(分页)")
    @GetMapping(value = "/item/list")
    public Response<PageInfo<GoodsItemListDTO>> listGoodsItem(@Valid GoodsItemListQry qry) {
        PageInfo<GoodsItemListDTO> pageInfo = service.listGoodsItem(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找货品列表(分页,不受权限控制接口)")
    @GetMapping(value = "po/item/list")
    public Response<PageInfo<GoodsItemListDTO>> listGoodsItemPo(@Valid GoodsItemListQry qry) {
        PageInfo<GoodsItemListDTO> pageInfo = service.listGoodsItem(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找erp货品列表(分页)")
    @GetMapping(value = "/item/erp/list")
    public Response<PageInfo<GoodsItemErpDTO>> listGoodsItemErp(@Valid GoodsItemListErpQry qry) {
        PageInfo<GoodsItemErpDTO> pageInfo = service.listGoodsItemErp(qry, getUserId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据商品id获取商品详情")
    @GetMapping()
    public Response<GoodsDTO> getGoods(@Valid GoodsId qry) {
        GoodsDTO dto = service.getGoods(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods, value = CommonLogTypeConstantDTO.GoodsAdd)
    @ApiOperation(value = "新增商品")
    @PostMapping()
    public Response createGoods(@RequestBody @Valid GoodsCmd cmd) {
        service.createGoods(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods, value = CommonLogTypeConstantDTO.GoodsUpdate)
    @ApiOperation(value = "更新商品")
    @PutMapping()
    public Response updateGoods(@RequestBody @Valid GoodsCmd cmd) {
        service.updateGoods(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods, value = CommonLogTypeConstantDTO.GoodsSalestatus)
    @ApiOperation(value = "上下架商品（SPU）")
    @PutMapping(value = "/salestatus")
    public Response saleStatusGoods(@RequestBody @Valid GoodsSaleStatusCmd cmd) {
        service.saleStatusGoods(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods,
        value = CommonLogTypeConstantDTO.GoodsItemSalestatus)
    @ApiOperation(value = "上下架货品（SKU）")
    @PutMapping(value = "/item/salestatus")
    public Response saleStatusGoodsItem(@RequestBody @Valid GoodsItemSaleStatusCmd cmd) {
        service.saleStatusGoodsItem(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods, value = CommonLogTypeConstantDTO.GoodsFreezestatus)
    @ApiOperation(value = "冻结解冻商品（SPU）")
    @PutMapping(value = "/freezestatus")
    public Response freezeStatusGoods(@RequestBody @Valid GoodsFreezeStatusCmd cmd) {
        service.freezeStatusGoods(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods, value = CommonLogTypeConstantDTO.GoodsDelete)
    @ApiOperation(value = "根据商品id删除商品")
    @DeleteMapping()
    public Response deleteGoods(@RequestBody @Valid GoodsId cmd) {
        service.deleteGoods(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods, value = CommonLogTypeConstantDTO.GoodsUpdateItemBox)
    @ApiOperation(value = "修改货品(SKU)装箱信息")
    @PutMapping(value = "/item/box")
    public Response updateGoodsItemBoxs(@RequestBody @Valid List<GoodsItemBoxCmd> cmds) {
        service.updateGoodsItemBoxs(cmds);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "获取货品(SKU)装箱信息列表")
    @GetMapping(value = "/item/box")
    public Response<List<GoodsItemBoxDTO>> listGoodsItemBox(@Valid GoodsItemId qry) {
        List<GoodsItemBoxDTO> dtos = service.listGoodsItemBox(qry);
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据栏目id获取商品列表(分页)")
    @GetMapping(value = "/list/column")
    public Response<PageInfo<GoodsListStoreDTO>> columnGoodsList(@Valid GoodsListColumnQry qry) {
        PageInfo<GoodsListStoreDTO> pageInfo = service.columnGoodsList(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据板块id获取商品列表(分页)")
    @GetMapping(value = "/list/section")
    public Response<PageInfo<GoodsListStoreDTO>> sectionGoodsList(@Valid GoodsListSectionQry qry) {
        PageInfo<GoodsListStoreDTO> pageInfo = service.sectionGoodsList(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据移动端配置id获取商品列表(分页)")
    @GetMapping(value = "/list/mobile")
    public Response<PageInfo<GoodsListStoreDTO>> mobileGoodsList(@Valid GoodsListMobileQry qry) {
        PageInfo<GoodsListStoreDTO> pageInfo = service.mobileGoodsList(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据移动端配置ids获取商品列表")
    @GetMapping(value = "/list/mobiles")
    public Response<List<GoodsListStoresDTO>> mobileGoodsList(@Valid GoodsListMobilesQry qry) {
        List<GoodsListStoresDTO> list = service.mobileGoodsList(qry);
        return Response.of(list);
    }

    @ApiOperation(value = "根据分类ids获取商品列表")
    @PostMapping(value = "/po/list/classifyIds")
    public Response<List<GoodsListDTO>> goodsListByClassifyIds(@RequestBody @Valid BaseIds qry) {
        List<GoodsListDTO> dtos = service.goodsListByClassifyIds(qry);
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据商品ids获取商品列表")
    @PostMapping(value = "/po/list/ids")
    public Response<List<GoodsListDTO>> goodsListByIds(@RequestBody @Valid BaseIds qry) {
        List<GoodsListDTO> dtos = service.goodsListByIds(qry);
        return Response.of(dtos);
    }

    @GetMapping(value = "/po/listAllItem")
    public JSONArray listAll() {
        List<GoodsItemDO> list = service.listAllItem();
        return JSON.parseArray(JSON.toJSONString(list));
    }

    @GetMapping(value = "/po/pageAssignByDistributorId")
    @ApiOperation(value = "分页查询分销商指定的货品列表")
    public Response<PageInfo<GoodsItemSimpleDTO>> pageAssignByDistributorId(@Valid DistributorGoodsQuery goodsQuery) {
        PageInfo<GoodsItemSimpleDTO> pageInfo = service.pageAssignByDistributorId(goodsQuery);
        return Response.of(pageInfo);
    }

    @GetMapping(value = "/po/pageDiyItem")
    @ApiOperation(value = "分页查询货品列表（定制）")
    public Response<PageInfo<GoodsItemSimpleDTO>> pageDiyItem(@Valid DiyGoodsPageQry diyGoodsPageQry) {
        PageInfo<GoodsItemSimpleDTO> pageInfo = service.pageByGoodsTypeAndDiyType(diyGoodsPageQry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods,
        value = CommonLogTypeConstantDTO.GoodsUpdateItemPrice)
    @ApiOperation(value = "同步商品价格")
    @PutMapping(value = "/sync/batch/price")
    public Response syncBatchGoodsPrice(@Valid @RequestBody BaseIds cmd) {
        service.syncBatchGoodsPrice(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods,
        value = CommonLogTypeConstantDTO.GoodsUpdateItemPrice)
    @ApiOperation(value = "全量同步商品价格")
    @PutMapping(value = "/sync/all/price")
    public Response syncAllGoodsPrice() {
        service.syncAllGoodsPrice();
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods, value = CommonLogTypeConstantDTO.GoodsUpdateItemErp)
    @ApiOperation(value = "同步货品信息")
    @PutMapping(value = "/sync/batch/item")
    public Response syncBatchGoodsItem(@Valid @RequestBody BaseIds cmd) {
        service.syncBatchGoodsItem(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods, value = CommonLogTypeConstantDTO.GoodsUpdateItemErp)
    @ApiOperation(value = "全量同步货品信息")
    @PutMapping(value = "/sync/all/item")
    public Response syncAllGoodsItem() {
        service.syncAllGoodsItem();
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods,
        value = CommonLogTypeConstantDTO.GoodsUpdateItemBoxErp)
    @ApiOperation(value = "同步货品装箱信息")
    @PutMapping(value = "/sync/batch/item/box")
    public Response syncBatchGoodsItemBox(@Valid @RequestBody BaseIds cmd) {
        service.syncBatchGoodsItemBox(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Goods,
        value = CommonLogTypeConstantDTO.GoodsUpdateItemBoxErp)
    @ApiOperation(value = "全量同步货品装箱信息")
    @PutMapping(value = "/sync/all/item/box")
    public Response syncAllGoodsItemBox() {
        service.syncAllGoodsItemBox();
        return Response.buildSuccess();
    }

    @ApiOperation(value = "刷新商品最小最大价格")
    @PutMapping(value = "/all/goods/price")
    public Response allGoodsPrice() {
        service.allGoodsPrice();
        return Response.buildSuccess();
    }

    @ApiOperation(value = "刷新商品可视范围")
    @PutMapping(value = "/all/goods/scope")
    public Response allGoodsScope() {
        service.allGoodsScope();
        return Response.buildSuccess();
    }

}
