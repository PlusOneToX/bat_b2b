package com.bat.goods.service.goods.executor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.goods.dao.brand.BrandMapper;
import com.bat.goods.dao.goods.*;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.service.common.Constant;
import com.bat.goods.service.common.GoodsConfig;
import com.bat.goods.service.goods.convertor.GoodsConvertor;
import com.bat.goods.service.scaleprice.executor.ScalePriceQryExe;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorGoodsControlRpcDTO;
import com.bat.dubboapi.goods.goods.dto.*;
import com.bat.dubboapi.goods.goods.dto.data.*;
import com.bat.dubboapi.system.user.api.SystemUserServiceRpc;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyGoodsServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.goods.GoodsItemErpListRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.goods.GoodsItemPriceErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemErpRpcDTO;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.goods.dto.GoodsItemListErpQry;
import com.bat.goods.api.goods.dto.data.GoodsItemErpDTO;
import com.bat.goods.api.user.dto.UserGoodsItemId;
import com.bat.goods.api.user.dto.data.UserGoodsItemPriceDTO;
import com.bat.goods.api.utils.CharUtils;
import com.bat.goods.dao.category.CategoryMapper;
import com.bat.goods.dao.goods.*;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.dao.scaleprice.dataobject.ScalePriceDO;
import com.bat.goods.service.user.executor.UserQryExe;

@Component
public class GoodsRpcQryExe {

    @DubboReference(check = false, timeout = 30000)
    private ThirdPartyGoodsServiceErpRpc goodsServiceErpRpc;

    @Resource
    private ScalePriceQryExe scalePriceQryExe;

    @Resource
    private GoodsRpcMapper goodsRpcMapper;

    @DubboReference(check = false, timeout = 30000)
    private SystemUserServiceRpc userServiceRpc;

    @Resource
    private GoodsStoreMapper goodsStoreMapper;

    @Resource
    private GoodsStoreMobileMapper mobileMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @Resource
    private GoodsUserMapper userMapper;

    @Resource
    private GoodsConfig config;

    @Resource
    private UserQryExe userQryExe;

    /**
     * 获取erp货品列表（调用第三方接口服务）
     *
     * @param qry
     * @return
     */
    public PageInfo<GoodsItemErpDTO> listGoodsItemErp(GoodsItemListErpQry qry, String userId) {
        GoodsItemErpListRpcQry rpcQry = new GoodsItemErpListRpcQry();
        BeanUtils.copyProperties(qry, rpcQry);
        rpcQry.setOrgId(String.valueOf(100));
        if (StringUtils.isNotBlank(userId)) {
            com.bat.dubboapi.system.common.Response<UserRpcDTO> response =
                userServiceRpc.getUserById(Integer.valueOf(userId));
            if (response.isSuccess()) {
                UserRpcDTO userRpcDTO = response.getData();
                if (userRpcDTO != null && userRpcDTO.getOrganizationDTO() != null
                    && StringUtils.isNotBlank(userRpcDTO.getOrganizationDTO().getErpOrganizationId())) {
                    rpcQry.setOrgId(userRpcDTO.getOrganizationDTO().getErpOrganizationId());
                }
            }
        }
        /** 获取等级价格erp字段列表 */
        List<ScalePriceDO> scalePriceDOS = scalePriceQryExe.executeFieldList(Constant.OPEN_YES);
        rpcQry.setErpPriceFields(GoodsConvertor.toErpPriceFieldKeyListRpcQry(scalePriceDOS));
        Response<List<GoodsItemErpRpcDTO>> rpcResponse = goodsServiceErpRpc.listGoodsItemErp(rpcQry);
        if (rpcResponse.isSuccess()) {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageSize(qry.getPage());
            pageInfo.setSize(qry.getSize());
            List<GoodsItemErpRpcDTO> erpRpcDTOList = rpcResponse.getData();
            if (!CollectionUtils.isEmpty(erpRpcDTOList)) {
                List<String> itemCodes =
                    erpRpcDTOList.stream().map(GoodsItemErpRpcDTO::getItemCode).collect(Collectors.toList());
                List<UserGoodsNameDO> goodsNameDOS = goodsRpcMapper.listGoodsNameByItemCodes(itemCodes);
                List<GoodsItemErpDTO> dtos = GoodsConvertor.toGoodsItemErpDTO(erpRpcDTOList, goodsNameDOS);
                pageInfo.setList(dtos);
            }
            return pageInfo;
        } else {
            throw GoodsException.buildException(rpcResponse.getErrCode(), rpcResponse.getErrMessage());
        }
    }

    public PageInfo<UserGoodsRpcDTO> listGoods(UserGoodsListRpcQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<UserGoodsNameDO> userGoodsRpcDOS = goodsRpcMapper.listGoodsNameByDistributorId(qryMap);
        PageInfo pageInfo = new PageInfo(userGoodsRpcDOS);
        List<UserGoodsRpcDTO> dtos = GoodsConvertor.toUserGoodsRpcDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 根据栏目id获取商品关系列表（分页）
     *
     * @param qry
     * @return
     */
    public PageInfo<GoodsStoreColumnRpcDTO> listGoodsStoreColumn(GoodsStoreListRpcQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<GoodsStoreColumnDO> storeColumnDOS = goodsStoreMapper.listGoodsStoreColumn(qry.getId());
        PageInfo pageInfo = new PageInfo(storeColumnDOS);
        List<GoodsStoreColumnRpcDTO> dtos = GoodsConvertor.toGoodsStoreColumnRpcDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 根据板块id获取商品关系列表（分页）
     *
     * @param qry
     * @return
     */
    public PageInfo<GoodsStoreSectionRpcDTO> listGoodsStoreSection(GoodsStoreListRpcQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<GoodsStoreSectionDO> storeSectionDOS = goodsStoreMapper.listGoodsStoreSection(qry.getId());
        PageInfo pageInfo = new PageInfo(storeSectionDOS);
        List<GoodsStoreSectionRpcDTO> dtos = GoodsConvertor.toGoodsStoreSectionRpcDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 根据板块id获取商品关系列表（分页）
     *
     * @param qry
     * @return
     */
    public PageInfo<GoodsStoreMobileRpcDTO> listGoodsStoreMobile(GoodsStoreListRpcQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<GoodsStoreMobileDO> storeMobileDOS = mobileMapper.listGoodsStoreMobile(qry.getId());
        PageInfo pageInfo = new PageInfo(storeMobileDOS);
        List<GoodsStoreMobileRpcDTO> dtos = GoodsConvertor.toGoodsStoreMobileRpcDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 根据货品ids获取货品信息
     *
     * @param goodsItemIds
     * @return
     */
    public List<GoodsItemRpcDTO> listGoodsItem(List<Integer> goodsItemIds) {
        if (CollectionUtils.isEmpty(goodsItemIds)) {
            return null;
        }
        List<GoodsItemListDO> goodsItemListDOS = goodsRpcMapper.listGoodsItemByIds(goodsItemIds);
        List<GoodsItemSpecsColorListDO> specsColorListDOS =
            goodsMapper.listGoodsItemSpecsColorListByGoodsItemIds(goodsItemIds);
        return GoodsConvertor.toGoodsItemRpcDTOList(goodsItemListDOS, specsColorListDOS);
    }

    /**
     * 根据货品codes获取货品信息
     *
     * @param codes
     * @return
     */
    public List<GoodsItemRpcDTO> listGoodsItemByCodes(List<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return null;
        }
        List<GoodsItemListDO> goodsItemListDOS = goodsRpcMapper.listGoodsItemByCodes(codes);
        List<Integer> goodsItemIds = goodsItemListDOS.stream().map(GoodsItemListDO::getId).collect(Collectors.toList());
        List<GoodsItemSpecsColorListDO> specsColorListDOS = null;
        if (!CollectionUtils.isEmpty(goodsItemIds)) {
            specsColorListDOS = goodsMapper.listGoodsItemSpecsColorListByGoodsItemIds(goodsItemIds);
        }
        return GoodsConvertor.toGoodsItemRpcDTOList(goodsItemListDOS, specsColorListDOS);
    }

    public GoodsItemRpcDTO goodsItemByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        GoodsItemListDO goodsItemListDO = goodsRpcMapper.listGoodsItemByCode(code);
        List<GoodsItemSpecsColorListDO> specsColorListDOS = null;
        if (goodsItemListDO != null) {
            specsColorListDOS = goodsMapper.listGoodsItemSpecsColorListByGoodsItemId(goodsItemListDO.getId());
        }
        return GoodsConvertor.toGoodsItemRpcDTO(goodsItemListDO, specsColorListDOS);
    }

    /**
     * 获取商品基本信息
     *
     * @param goodsIds
     * @return
     */
    public List<GoodsRpcDTO> listGoods(List<Integer> goodsIds) {
        if (CollectionUtils.isEmpty(goodsIds)) {
            return null;
        }
        List<GoodsListDO> goodsListDOS = goodsRpcMapper.listGoodsByIds(goodsIds);
        return GoodsConvertor.toGoodsRpcDTOList(goodsListDOS);
    }

    /**
     * 根据货品ids获取直发客户是否支持在途
     *
     * @param goodsItemIds
     * @return
     */
    public List<GoodsItemOnwaySaleFlagRpcDTO> listGoodsItemOnwaySaleFlag(List<Integer> goodsItemIds) {
        List<GoodsItemOnwaySaleFlagDO> onwaySaleFlagDOS = goodsRpcMapper.listGoodsItemOnwaySaleFlagByIds(goodsItemIds);
        return GoodsConvertor.toGoodsItemOnwaySaleFlagRpcDTOList(onwaySaleFlagDOS);
    }

    /**
     * 根据商品ids或货品ids获取货品列表（分页）
     *
     * @param qry
     * @return
     */
    public PageInfo<GoodsItemRpcDTO> listGoodsItem(UserGoodsItemListRpcQry qry) {
        com.bat.dubboapi.distributor.common.Response<DistributorGoodsControlRpcDTO> response =
            distributorServiceRpc.distributorGoodsControl(qry.getDistributorId());
        if (response.isSuccess()) {
            DistributorGoodsControlRpcDTO data = response.getData();
            BeanMap beanMap = BeanMap.create(qry);
            Map<String, Object> qryMap = new HashMap();
            qryMap.putAll(beanMap);
            List<Integer> brandIds = brandMapper.listByAllDistributor();
            if (brandIds == null) {
                brandIds = new ArrayList<>();
            }
            if (!CollectionUtils.isEmpty(data.getBrandIds())) {
                brandIds.addAll(data.getBrandIds());
            }
            // 剔除不可视品牌
            List<Integer> noBrandIds = data.getNoBrandIds();
            if (!CollectionUtils.isEmpty(brandIds) && !CollectionUtils.isEmpty(noBrandIds)) {
                brandIds =
                    brandIds.stream().filter(brandId -> !noBrandIds.contains(brandId)).collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(brandIds)) {
                qryMap.put("brandIds", brandIds);
            }
            List<Integer> categoryIds = categoryMapper.listByAllDistributor();
            if (categoryIds == null) {
                categoryIds = new ArrayList<>();
            }
            if (!CollectionUtils.isEmpty(data.getCategoryIds())) {
                categoryIds.addAll(data.getCategoryIds());
            }
            // 剔除不可视品类
            List<Integer> noCategoryIds = data.getNoCategoryIds();
            if (!CollectionUtils.isEmpty(categoryIds) && !CollectionUtils.isEmpty(noCategoryIds)) {
                categoryIds = categoryIds.stream().filter(categoryId -> !noCategoryIds.contains(categoryId))
                    .collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(categoryIds)) {
                qryMap.put("categoryIds", categoryIds);
            }
            if (!CollectionUtils.isEmpty(data.getGoodsIds())) {
                qryMap.put("goodsIdsScope", data.getGoodsIds());
            }
            List<Integer> noGoodsIds = data.getNoGoodsIds();
            // 剔除不可视商品
            if (!CollectionUtils.isEmpty(noGoodsIds)) {
                qryMap.put("noGoodsIds", noGoodsIds);
            }
            PageHelper.startPage(qry.getPage(), qry.getSize());
            List<GoodsItemListDO> goodsItemListDOS = goodsRpcMapper.listDistributorGoodsItem(qryMap);
            PageInfo pageInfo = new PageInfo(goodsItemListDOS);
            List<Integer> goodsItemIds =
                goodsItemListDOS.stream().map(GoodsItemListDO::getId).collect(Collectors.toList());
            List<GoodsItemSpecsColorListDO> specsColorListDOS = null;
            if (!CollectionUtils.isEmpty(goodsItemIds)) {
                specsColorListDOS = goodsMapper.listGoodsItemSpecsColorListByGoodsItemIds(goodsItemIds);
            }
            List<GoodsItemRpcDTO> itemRpcDTOS =
                GoodsConvertor.toGoodsItemRpcDTOList(pageInfo.getList(), specsColorListDOS);
            pageInfo.setList(itemRpcDTOS);
            return pageInfo;
        } else {
            throw GoodsException.buildException(ErrorCode.B_GOODS_DISTRIBUTOR_CONTROL_NULL);
        }
    }

    /**
     * 根据分销商id和商品ids查询商品列表（分页，有序（顺序按商品ids顺序））
     *
     * @param qry
     * @return
     */
    public PageInfo<UserGoodsRpcDTO> listDistributorGoods(UserGoodsSortListRpcQry qry) {
        com.bat.dubboapi.distributor.common.Response<DistributorGoodsControlRpcDTO> response =
            distributorServiceRpc.distributorGoodsControl(qry.getDistributorId());
        if (response.isSuccess()) {
            DistributorGoodsControlRpcDTO data = response.getData();
            BeanMap beanMap = BeanMap.create(qry);
            Map<String, Object> qryMap = new HashMap();
            qryMap.putAll(beanMap);
            List<Integer> brandIds = brandMapper.listByAllDistributor();
            if (brandIds == null) {
                brandIds = new ArrayList<>();
            }
            if (!CollectionUtils.isEmpty(data.getBrandIds())) {
                brandIds.addAll(data.getBrandIds());
            }
            // 剔除不可视品牌
            List<Integer> noBrandIds = data.getNoBrandIds();
            if (!CollectionUtils.isEmpty(brandIds) && !CollectionUtils.isEmpty(noBrandIds)) {
                brandIds =
                    brandIds.stream().filter(brandId -> !noBrandIds.contains(brandId)).collect(Collectors.toList());
            }
            System.out.println("可视品牌：" + brandIds.toString());
            if (!CollectionUtils.isEmpty(brandIds)) {
                qryMap.put("brandIds", brandIds);
            }
            List<Integer> categoryIds = categoryMapper.listByAllDistributor();
            if (categoryIds == null) {
                categoryIds = new ArrayList<>();
            }
            if (!CollectionUtils.isEmpty(data.getCategoryIds())) {
                categoryIds.addAll(data.getCategoryIds());
            }
            // 剔除不可视品类
            List<Integer> noCategoryIds = data.getNoCategoryIds();
            if (!CollectionUtils.isEmpty(categoryIds) && !CollectionUtils.isEmpty(noCategoryIds)) {
                categoryIds = categoryIds.stream().filter(categoryId -> !noCategoryIds.contains(categoryId))
                    .collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(categoryIds)) {
                qryMap.put("categoryIds", categoryIds);
            }
            if (!CollectionUtils.isEmpty(data.getGoodsIds())) {
                qryMap.put("goodsIdsScope", data.getGoodsIds());
            }
            // 剔除不可视商品
            List<Integer> noGoodsIds = data.getNoGoodsIds();
            if (!CollectionUtils.isEmpty(noGoodsIds)) {
                qryMap.put("noGoodsIds", noGoodsIds);
            }
            PageHelper.startPage(qry.getPage(), qry.getSize());
            List<UserGoodsRpcDO> userGoodsRpcDOS = goodsRpcMapper.listDistributorGoods(qryMap);
            PageInfo pageInfo = new PageInfo(userGoodsRpcDOS);
            List<UserGoodsRpcDTO> rpcDTOS = GoodsConvertor.toUserGoodsRpcDTOList(pageInfo.getList());
            pageInfo.setList(rpcDTOS);
            return pageInfo;
        } else {
            throw GoodsException.buildException(ErrorCode.B_GOODS_DISTRIBUTOR_CONTROL_NULL);
        }
    }

    /**
     * 根据分销商获取货品价格列表
     *
     * @param qry
     * @return
     */
    public List<GoodsItemPriceRpcDTO> listDistributorGoodsItemPrice(GoodsItemPriceRpcQry qry) {
        List<UserGoodsItemId> goodsItems = new ArrayList<>();
        qry.getGoodsItems().forEach(goodsItemRpc -> {
            UserGoodsItemId goodsItem = new UserGoodsItemId();
            BeanUtils.copyProperties(goodsItemRpc, goodsItem);
            goodsItems.add(goodsItem);
        });
        List<UserGoodsItemPriceDTO> priceDTOS =
            userQryExe.priceGoodsItemList(goodsItems, qry.getDistributorId(), qry.getRetailPriceFlag());
        return GoodsConvertor.toGoodsItemPriceRpcDTOList(priceDTOS);
    }

    /**
     * 根据货品建议零售价
     *
     * @param itemIds
     * @return
     */
    public List<GoodsItemPriceRpcDTO> listGoodsItemRetailPrice(List<Integer> itemIds) {
        List<GoodsItemScalePriceDO> retailPriceDOS =
            userMapper.listGoodsItemRetailPriceByItemIds(itemIds, Constant.RETAIL_FLAG_1);
        return GoodsConvertor.toGoodsItemPriceRpcDTORetailList(retailPriceDOS);
    }

    /**
     * 根据货品ids获取装箱规格
     *
     * @param itemIds
     * @return
     */
    public List<GoodsItemBoxRpcDTO> listGoodsItemBoxList(List<Integer> itemIds) {
        List<GoodsItemBoxDO> goodsItemBoxDOS = goodsMapper.listGoodsItemBoxByGoodsItemIds(itemIds);
        return GoodsConvertor.toGoodsItemBoxRpcDTOList(goodsItemBoxDOS);
    }

    public GoodsItemRpcDTO goodsItemByItemId(Integer itemId) {
        GoodsItemDO goodsItemDO = goodsMapper.getGoodsItemById(itemId);
        return GoodsConvertor.toGoodsItemRpcDTOByOne(goodsItemDO);
    }

    /**
     * 同步商品价格
     *
     * @param goodsItemSyncDOS
     * @return
     */
    public List<com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemPriceRpcDTO>
        listGoodsItemPriceErp(List<GoodsItemSyncDO> goodsItemSyncDOS) {
        GoodsItemPriceErpRpcQry rpcQry = new GoodsItemPriceErpRpcQry();
        /** 获取等级价格erp字段列表 */
        List<ScalePriceDO> scalePriceDOS = scalePriceQryExe.executeFieldList(Constant.OPEN_YES);
        rpcQry.setErpPriceFields(GoodsConvertor.toErpPriceFieldKeyListRpcQry(scalePriceDOS));
        List<Integer> itemErpIds =
            goodsItemSyncDOS.stream().map(GoodsItemSyncDO::getItemErpId).collect(Collectors.toList());
        rpcQry.setItemErpIds(itemErpIds);
        Response<List<com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemPriceRpcDTO>> response =
            goodsServiceErpRpc.listGoodsItemPriceErp(rpcQry);
        if (response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 根据itemErpIds获取更新货品信息
     *
     * @param itemErpIds
     * @return
     */
    public List<GoodsItemErpRpcDTO> listGoodsItemErp(List<Integer> itemErpIds) {
        Response<List<GoodsItemErpRpcDTO>> response = goodsServiceErpRpc.listGoodsItemErp(itemErpIds);
        if (response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    public List<com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemBoxRpcDTO>
        listGoodsItemBoxErp(List<Integer> itemErpIds) {
        Response<List<com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemBoxRpcDTO>> response =
            goodsServiceErpRpc.listGoodsItemBoxErp(itemErpIds);
        if (response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 根据分销商刷新分销商商品可视范围
     *
     * @param distributorId
     * @param scaleRpcs
     * @param adminId
     * @param departmentId
     * @return
     */
    public List<Integer> getVisibleGoodsIdsByDistributor(Integer distributorId, List<BrandScaleRpc> scaleRpcs,
        Integer adminId, Integer departmentId, List<Integer> brandIds, String distributorGroupIds) {
        List<Integer> goodsIds = new ArrayList<>();
        List<Integer> distributorIdGoodsIds = goodsRpcMapper.listGoodsIdByDistributorId(distributorId, brandIds);
        if (!CollectionUtils.isEmpty(distributorIdGoodsIds)) {
            goodsIds.addAll(distributorIdGoodsIds);
        }
        List<Integer> adminIdGoodsIds = goodsRpcMapper.listGoodsIdByAdminId(adminId, brandIds);
        if (!CollectionUtils.isEmpty(adminIdGoodsIds)) {
            goodsIds.addAll(adminIdGoodsIds);
        }
        List<Integer> departmentIdGoodsIds = goodsRpcMapper.listGoodsIdByDepartmentId(departmentId, brandIds);
        if (!CollectionUtils.isEmpty(departmentIdGoodsIds)) {
            goodsIds.addAll(departmentIdGoodsIds);
        }
        List<Integer> distributorGroupIdGoodsIds = new ArrayList<>();
        List<Integer> distributorGroupIdList = CharUtils.cuttingGetListFromStr(distributorGroupIds);
        if (distributorGroupIdList.size() > 0) {
            // 根据分销商分组id列表查出支持的商品id
            distributorGroupIdGoodsIds =
                goodsRpcMapper.listGoodsIdByDistributorGroupId(distributorGroupIdList, brandIds);
        }
        if (!CollectionUtils.isEmpty(distributorGroupIdGoodsIds)) {
            goodsIds.addAll(distributorGroupIdGoodsIds);
        }
        scaleRpcs.forEach(scaleRpc -> {
            BrandScaleDO brandScaleDO = new BrandScaleDO();
            BeanUtils.copyProperties(scaleRpc, brandScaleDO);
            List<Integer> brandScaleGoodsIds = goodsRpcMapper.listGoodsIdByBrandScale(brandScaleDO.getScalePriceId(),
                brandScaleDO.getBrandId(), brandIds);
            if (!CollectionUtils.isEmpty(brandScaleGoodsIds)) {
                goodsIds.addAll(brandScaleGoodsIds);
            }
        });
        return goodsIds.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 根据品牌id获取可视分销商范围
     *
     * @param brandId
     * @return
     */
    public List<Integer> getDistributorIdsByBrandId(Integer brandId) {
        com.bat.dubboapi.distributor.common.Response<List<Integer>> response =
            distributorServiceRpc.getDistributorIdsByBrandId(brandId);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw GoodsException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 根据商品编码获取商品id
     *
     * @param goodsNo
     * @return
     */
    public Integer getGoodsIdByGoodsNo(String goodsNo) {
        Integer goodsId = goodsMapper.getGoodsIdByGoodsNo(goodsNo);
        return goodsId;
    }

    /**
     * 根据货品编码获取货品id
     *
     * @param itemCodeOrBarCode
     * @return
     */
    public List<GoodsItemRpc> getGoodsItemIdsByItemCodeOrBarCode(String itemCodeOrBarCode) {
        List<GoodsItemIdDO> goodsItemIdDOS = goodsMapper.getGoodsItemIdsByItemCodeOrBarCode(itemCodeOrBarCode);
        List<GoodsItemRpc> goodsItemRpcs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsItemIdDOS)) {
            goodsItemIdDOS.forEach(goodsItemIdDO -> {
                GoodsItemRpc itemRpc = new GoodsItemRpc();
                itemRpc.setGoodsId(goodsItemIdDO.getGoodsId());
                itemRpc.setItemId(goodsItemIdDO.getId());
                itemRpc.setGoodsNo(goodsItemIdDO.getGoodsNo());
                itemRpc.setItemCode(goodsItemIdDO.getItemCode());
                goodsItemRpcs.add(itemRpc);
            });
        }
        return goodsItemRpcs;
    }

    public List<StopGoodsItemRpcDTO> listLifeCycleStopGoodsItem(Short saleStatus) {
        List<GoodsItemDO> goodsItemDOS = goodsMapper.listLifeCycleStopGoodsItem(saleStatus);
        List<StopGoodsItemRpcDTO> stopGoodsItemRpcDTOS = new ArrayList<>();
        for (GoodsItemDO goodsItemDO : goodsItemDOS) {
            StopGoodsItemRpcDTO stopGoodsItemRpcDTO = new StopGoodsItemRpcDTO();
            BeanUtils.copyProperties(goodsItemDO, stopGoodsItemRpcDTO);
            stopGoodsItemRpcDTOS.add(stopGoodsItemRpcDTO);
        }
        return stopGoodsItemRpcDTOS;

    }

    /**
     * 获取所有货品ids
     * 
     * @return
     */
    public List<Integer> getAllItemIds() {
        return goodsMapper.allItemIds();
    }

    /**
     * 获取所有货品基本信息
     * 
     * @return
     */
    public List<GoodsItemRpc> getAllGoodsItem() {
        List<GoodsItemIdDO> allGoodsItemId = goodsMapper.getAllGoodsItemId();
        List<GoodsItemRpc> goodsItemRpcs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allGoodsItemId)) {
            allGoodsItemId.forEach(goodsItemIdDO -> {
                GoodsItemRpc itemRpc = new GoodsItemRpc();
                itemRpc.setGoodsId(goodsItemIdDO.getGoodsId());
                itemRpc.setItemId(goodsItemIdDO.getId());
                itemRpc.setItemErpId(goodsItemIdDO.getItemErpId());
                itemRpc.setGoodsNo(goodsItemIdDO.getGoodsNo());
                itemRpc.setItemCode(goodsItemIdDO.getItemCode());
                itemRpc.setItemName(goodsItemIdDO.getItemName());
                goodsItemRpcs.add(itemRpc);
            });
        }
        return goodsItemRpcs;
    }

    public List<Integer> allVisibleProducts() {
        return goodsMapper.allVisibleProducts();
    }

    public List<GoodsAreVisibleRpcDTO> goodsAreVisible(List<Integer> orderGoodsList) {
        List<GoodsDO> goodsDOList = goodsMapper.goodsAreVisible(orderGoodsList);

        List<GoodsAreVisibleRpcDTO> goodsAreVisibleRpcDTOS = new ArrayList<>();

        goodsDOList.forEach(x -> {
            GoodsAreVisibleRpcDTO areVisibleRpcDTO = new GoodsAreVisibleRpcDTO();
            areVisibleRpcDTO.setGoodsId(x.getId());
            areVisibleRpcDTO.setBrandId(x.getBrandId());
            areVisibleRpcDTO.setDistributorScope(x.getDistributorScope() * 1);
            goodsAreVisibleRpcDTOS.add(areVisibleRpcDTO);
        });
        return goodsAreVisibleRpcDTOS;
    }

    public List<Integer> allVisibleBrands() {
        return goodsMapper.allVisibleBrands();
    }

    public BigDecimal queryRetailPriceByItemId(Integer itemId) {
        return goodsMapper.queryRetailPriceByItemId(itemId);
    }

    public List<Integer> goodsAreVisibleByDepId(Integer departmentID) {
        List<Integer> goodsIdList = goodsMapper.goodsAreVisibleByDepId(departmentID);
        return goodsIdList;
    }
}
