package com.bat.goods.web.user;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.goods.api.base.BaseIds;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.tag.dto.data.TagsDTO;
import com.bat.goods.api.user.UserServiceI;
import com.bat.goods.api.user.dto.*;
import com.bat.goods.api.user.dto.data.*;
import com.bat.goods.web.annotation.SysLog;
import com.bat.goods.web.constants.CommonLogTypeConstantDTO;
import com.bat.goods.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.user.dto.*;
import com.bat.goods.api.user.dto.data.*;
import com.bat.goods.web.base.BaseController;
import com.bat.goods.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "商品前台接口", value = "UserController")
@RestController
@RequestMapping("/goods/v1/web/user")
public class UserController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserServiceI service;

    @ApiOperation(value = "获取商品分类列表(不分页)")
    @GetMapping(value = "/classify/list")
    public Response<List<UserClassifyDTO>> listClassify(@Valid UserClassifyListQry qry) {
        UserClassifyInfoDTO dto = service.listClassify(qry);
        return Response.of(dto.getClassify());
    }

    @ApiOperation(value = "获取商品分类列表(不分页,H5端获取)")
    @GetMapping(value = "/classify/list/h5")
    public Response<UserClassifyInfoDTO> listClassifyH5(@Valid UserClassifyListQry qry) {
        UserClassifyInfoDTO dto = service.listClassify(qry);
        return Response.of(dto);
    }

    @ApiOperation(value = "根据父级分类id获取下级分类列表(不分页)")
    @GetMapping(value = "/subclassify/list")
    public Response<List<UserClassifyDTO>> listSubClassify(@Valid UserSubClassifyListQry qry) {
        List<UserClassifyDTO> dtos = service.listSubClassify(qry);
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据商品ids获取商品价格列表")
    @PostMapping(value = "/price/goods/list")
    public Response<List<UserGoodsPriceDTO>> priceGoodsList(@RequestBody @Valid UserGoodsPriceQry qry) {
        List<UserGoodsPriceDTO> dtos = service.priceGoodsList(qry, getUserId());
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据商品货品ids获取货品价格列表")
    @PostMapping(value = "/price/item/list")
    public Response<List<UserGoodsItemPriceDTO>> priceGoodsItemList(@RequestBody @Valid UserGoodsItemPriceQry qry) {
        if(CollectionUtils.isEmpty(qry.getGoodsItemIds())){
            throw GoodsException.buildException("P_GOODS_GOODS_GOODS_ITEM_ID_NULL");
        }
        List<UserGoodsItemPriceDTO> dtos = service.priceGoodsItemList(qry, getUserId());
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据栏目id获取商品列表(分页)")
    @GetMapping(value = "/column/goods/list")
    public Response<PageInfo<UserGoodsListDTO>> columnGoodsList(@Valid UserGoodsListColumnQry qry) {
        PageInfo<UserGoodsListDTO> pageInfo = service.columnGoodsList(qry, getUserId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据板块id获取商品列表(分页)")
    @GetMapping(value = "/section/goods/list")
    public Response<PageInfo<UserGoodsListDTO>> sectionGoodsList(@Valid UserGoodsListSectionQry qry) {
        PageInfo<UserGoodsListDTO> pageInfo = service.sectionGoodsList(qry, getUserId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据移动端配置id获取商品列表(分页)")
    @GetMapping(value = "/mobile/goods/list")
    public Response<PageInfo<UserGoodsListDTO>> mobileGoodsList(@Valid UserGoodsListMobileQry qry) {
        PageInfo<UserGoodsListDTO> pageInfo = service.mobileGoodsList(qry, getUserId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据移动端配置获取商品列表(分页,指定品牌、指定商品、全部商品用此接口)")
    @PostMapping(value = "/mobile/goods/list")
    public Response<PageInfo<UserGoodsListDTO>> mobileAppointGoodsList(@RequestBody @Valid UserGoodsListMobileQry qry) {
        PageInfo<UserGoodsListDTO> pageInfo = service.mobileGoodsList(qry, getUserId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "商品列表查询(包含个性定制、收藏和新品商品筛选)")
    @GetMapping(value = "/goods/list")
    public Response<PageInfo<UserGoodsListDTO>> goodsList(@Valid UserGoodsListQry qry) {
        PageInfo<UserGoodsListDTO> dtos = service.goodsList(qry, getUserId());
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据栏目id获取商品分类列表")
    @GetMapping(value = "/column/classify/list")
    public Response<List<UserClassifyDTO>> columnClassifyList(@Valid UserClassifyListColumnQry qry) {
        List<UserClassifyDTO> dtos = service.columnClassifyList(qry);
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据板块id获取商品分类列表")
    @GetMapping(value = "/section/classify/list")
    public Response<List<UserClassifyDTO>> sectionClassifyList(@Valid UserClassifyListSectionQry qry) {
        List<UserClassifyDTO> dtos = service.sectionClassifyList(qry);
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据移动端配置id获取商品分类列表")
    @GetMapping(value = "/mobile/classify/list")
    public Response<List<UserClassifyDTO>> mobileClassifyList(@Valid UserClassifyListMobileQry qry) {
        List<UserClassifyDTO> dtos = service.mobileClassifyList(qry);
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据商品id获取商品详情")
    @GetMapping(value = "/goods")
    public Response<UserGoodsDTO> goods(@Valid UserGoodsId qry) {
        UserGoodsDTO dto = service.goods(qry);
        return Response.of(dto);
    }

    @ApiOperation(value = "根据商品id查询商品收藏状态")
    @GetMapping(value = "/goods/collection")
    public Response<Boolean> getGoodsCollection(@Valid UserGoodsId qry) {
        Boolean b = service.getGoodsCollection(qry, getUserId());
        return Response.of(b);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserCreateGoodsCollection)
    @ApiOperation(value = "商品收藏")
    @PostMapping(value = "/goods/collection")
    public Response createGoodsCollection(@RequestBody @Valid UserGoodsId cmd) {
        service.createGoodsCollection(cmd, getUserId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserDeleteGoodsCollection)
    @ApiOperation(value = "删除商品收藏(包含单个商品和清除所有)")
    @DeleteMapping(value = "/goods/collection")
    public Response deleteGoodsCollection(@Valid UserGoodsCollection cmd) {
        service.deleteGoodsCollection(cmd, getUserId());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "根据商品ids商品标签")
    @PostMapping(value = "/goods/labels")
    public Response<List<TagsDTO>> getGoodsLabelByGoodsIds(@Valid @RequestBody BaseIds qry) {
        List<TagsDTO> tagDTOS = service.getGoodsLabelByGoodsIds(qry);
        return Response.of(tagDTOS);
    }

}
