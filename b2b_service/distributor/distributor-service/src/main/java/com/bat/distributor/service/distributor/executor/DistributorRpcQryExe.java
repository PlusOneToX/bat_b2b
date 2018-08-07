package com.bat.distributor.service.distributor.executor;

import static com.bat.distributor.service.common.Constant.*;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.distributor.dto.data.DistributorSpecialGoodsDTO;
import com.bat.distributor.dao.category.CategoryMapper;
import com.bat.distributor.dao.category.dataobject.CategoryDO;
import com.bat.distributor.dao.distributor.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.dao.group.GroupMapper;
import com.bat.distributor.dao.group.dataobject.GroupDO;
import com.bat.distributor.dao.nextscaleprice.NextScalePriceMapper;
import com.bat.distributor.dao.nextscaleprice.NextScalePriceSpecialBrandCategoryMapper;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceFormulaDO;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceSpecialFormulaDO;
import com.bat.distributor.dao.trade.TradeMapper;
import com.bat.distributor.dao.trade.dataobject.TradeDO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.bat.distributor.dao.distributor.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.service.common.DistributorConfig;
import com.bat.distributor.service.common.MessageUtils;
import com.bat.distributor.service.distributor.convertor.DistributorConvertor;
import com.bat.dubboapi.distributor.distributor.dto.DistributorGoodsScalePriceRpcQry;
import com.bat.dubboapi.distributor.distributor.dto.data.*;
import com.bat.dubboapi.goods.brand.api.GoodsBrandServiceRpc;
import com.bat.dubboapi.goods.brand.dto.data.UserBrandRpcDTO;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.goods.scaleprice.api.ScalePriceServiceRpc;
import com.bat.dubboapi.goods.scaleprice.dto.ScalePriceRpcDTO;
import com.bat.dubboapi.system.region.api.SystemRegionServiceRpc;
import com.bat.dubboapi.system.region.dto.data.RegionRpcDTO;
import com.bat.dubboapi.system.user.api.SystemUserServiceRpc;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;
import org.springframework.util.StringUtils;

@Component
public class DistributorRpcQryExe {

    @Resource
    private DistributorBusinessMapper businessMapper;

    @Resource
    private DistributorOneScalePriceMapper scalePriceMapper;

    @DubboReference(check = false, timeout = 30000)
    private SystemUserServiceRpc userServiceRpc;
    @DubboReference(check = false, timeout = 30000)
    private GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private SystemRegionServiceRpc regionServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private GoodsBrandServiceRpc brandServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private ScalePriceServiceRpc scalePriceServiceRpc;

    @Resource
    private DistributorTreePathMapper treePathMapper;

    @Resource
    private DistributorBrandRelevanceMapper brandRelevanceMapper;
    @Resource
    private DistributorCategoryRelevanceMapper categoryRelevanceMapper;
    @Resource
    private DistributorBrandRelevanceNoMapper brandRelevanceNoMapper;
    @Resource
    private DistributorGoodsRelevanceMapper goodsRelevanceMapper;
    @Resource
    private DistributorGoodsRelevanceNoMapper goodsRelevanceNoMapper;
    @Resource
    private DistributorOneScalePriceMapper oneScalePriceMapper;
    @Resource
    NextScalePriceSpecialBrandCategoryMapper specialBrandCategoryMapper;
    @Resource
    NextScalePriceMapper nextScalePriceMapper;
    @Resource
    DistributorSpecialGoodsMapper specialGoodsMapper;
    @Resource
    private DistributorMapper distributorMapper;
    @Resource
    private DistributorAddressMapper addressMapper;
    @Resource
    private DistributorSalesAreaMapper distributorSalesAreaMapper;
    @Resource
    private DistributorPromotionRelevanceMapper promotionRelevanceMapper;
    @Resource
    private DistributorGroupSeckillRelevanceMapper groupSeckillRelevanceMapper;
    @Resource
    private DistributorExtendDataMapper extendDataMapper;
    @Resource
    private DistributorRpcMapper distributorRpcMapper;
    @Resource
    private DistributorBusinessMapper distributorBusinessMapper;
    @Resource
    private DistributorFinancialMapper financialMapper;
    @Resource
    private DistributorContactsMapper distributorContactsMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TradeMapper tradeMapper;
    @Resource
    private DistributorConfig distributorConfig;

    private static final Logger log = LoggerFactory.getLogger(DistributorRpcQryExe.class);

    /**
     * 根据业务员ids查询分销商ids列表
     * 
     * @param salesIds
     * @return
     */
    public List<Integer> listDistributorIdBySalesIdsAndOneTreeNode(List<Integer> salesIds) {
        return businessMapper.listIdBySalesIdsAndOneTreeNode(salesIds);
    }

    /**
     * 根据业务员ids查询分销商名称列表
     *
     * @param salesIds
     * @return
     */
    public List<DistributorNameRpcDTO> listDistributorNameBySalesIdAndOneTreeNode(List<Integer> salesIds) {
        List<DistributorNameDO> distributorNameDOS =
            businessMapper.listDistributorNameBySalesIdAndOneTreeNode(salesIds);
        return DistributorConvertor.toDistributorNameRpcDTOList(distributorNameDOS);

    }

    /**
     * 根据分销商价格等级ids查询分销商ids列表
     * 
     * @param ids
     * @param brandId
     * @return
     */
    public List<Integer> listDistributorIdByScalePriceIds(List<Integer> ids, Integer brandId) {
        return scalePriceMapper.listIdByScalePriceIdsAndBrandId(ids, brandId);
    }

    /**
     * 根据分销商价格等级ids查询分销商ids列表
     *
     * @param ids
     * @return
     */
    public List<Integer> listDistributorIdByScalePriceIdsTwo(List<Integer> ids) {
        return scalePriceMapper.listDistributorIdByScalePriceIdsTwo(ids);
    }

    /**
     * 根据分销商价格等级ids查询分销商ids列表
     *
     * @param ids
     * @return
     */
    public List<Integer> listDistributorIdByDefaultScalePriceIds(List<Integer> ids) {
        return scalePriceMapper.listDefaultByScalePriceIds(ids);
    }

    public List<DistributorNameRpcDTO>
        listDistributorNameByDefaultScalePriceIdsAndOneTreeNode(List<Integer> scalePriceIds) {
        List<DistributorNameDO> distributorNameDOS =
            scalePriceMapper.listDistributorNameDefaultByScalePriceIds(scalePriceIds);
        List<DistributorNameRpcDTO> list = new ArrayList<>();
        for (DistributorNameDO distributorNameDO : distributorNameDOS) {
            DistributorNameRpcDTO distributorNameRpcDTO = new DistributorNameRpcDTO();
            BeanUtils.copyProperties(distributorNameDO, distributorNameRpcDTO);
            list.add(distributorNameRpcDTO);
        }
        return list;
    }

    /**
     * 根据分销商分组id查询分销商ids列表
     * 
     * @param distributorGroupIds
     * @return
     */
    public List<Integer> listIdByDistributorGroupIdsAndOneTreeNode(List<Integer> distributorGroupIds) {
        if (distributorGroupIds == null || distributorGroupIds.size() == 0) {
            return new ArrayList<>();
        }
        return businessMapper.listIdByDistributorGroupIdsAndOneTreeNode(distributorGroupIds);
    }

    /**
     * 根据分销商分组id查询分销商名称列表
     *
     * @param distributorGroupIds
     * @return
     */
    public List<DistributorNameRpcDTO> listNameByDistributorGroupIdsAndOneTreeNode(List<Integer> distributorGroupIds) {
        List<DistributorNameRpcDTO> list = new ArrayList<>();
        List<DistributorNameDO> distributorNameDOS =
            businessMapper.listNameByDistributorGroupIdsAndOneTreeNode(distributorGroupIds);
        for (DistributorNameDO distributorNameDO : distributorNameDOS) {
            DistributorNameRpcDTO distributorNameRpcDTO = new DistributorNameRpcDTO();
            BeanUtils.copyProperties(distributorNameDO, distributorNameRpcDTO);
            list.add(distributorNameRpcDTO);
        }
        return list;
    }

    /**
     * 根据部门ids查询分销商ids(一级分销商)列表
     * 
     * @param departmentIds
     * @return
     */
    public List<Integer> listDistributorIdByDepartmentIds(List<Integer> departmentIds) {
        com.bat.dubboapi.system.common.Response<List<UserRpcDTO>> response =
            userServiceRpc.getUserByDepartmentIds(departmentIds);
        List<Integer> ids = new ArrayList<>();
        if (response.isSuccess()) {
            List<UserRpcDTO> userRpcDTOS = response.getData();
            if (!CollectionUtils.isEmpty(userRpcDTOS)) {
                List<Integer> adminIds = userRpcDTOS.stream().map(UserRpcDTO::getId).collect(Collectors.toList());
                ids.addAll(businessMapper.listIdBySalesIdsAndOneTreeNode(adminIds));
            }
        }
        return ids;
    }

    public List<DistributorNameRpcDTO> listDistributorNameByDepartmentIdsAndOneTreeNode(List<Integer> departmentIds) {
        com.bat.dubboapi.system.common.Response<List<UserRpcDTO>> response =
            userServiceRpc.getUserByDepartmentIds(departmentIds);
        List<DistributorNameRpcDTO> distributorNames = new ArrayList<>();
        if (response.isSuccess()) {
            List<UserRpcDTO> userRpcDTOS = response.getData();
            if (!CollectionUtils.isEmpty(userRpcDTOS)) {
                List<Integer> adminIds = userRpcDTOS.stream().map(UserRpcDTO::getId).collect(Collectors.toList());
                List<DistributorNameDO> distributorNameDOS =
                    businessMapper.listDistributorNameBySalesIdAndOneTreeNode(adminIds);
                for (DistributorNameDO distributorNameDO : distributorNameDOS) {
                    DistributorNameRpcDTO distributorNameRpcDTO = new DistributorNameRpcDTO();
                    BeanUtils.copyProperties(distributorNameDO, distributorNameRpcDTO);
                    distributorNames.add(distributorNameRpcDTO);
                }
            }
        }
        return distributorNames;
    }

    /**
     * 获取分销商特价商品名称等信息
     * 
     * @param specialGoodsDTOS
     */
    public void listSpecialGoodsDTO(List<DistributorSpecialGoodsDTO> specialGoodsDTOS) {
        List<Integer> goodsItemIds =
            specialGoodsDTOS.stream().map(DistributorSpecialGoodsDTO::getGoodsItemId).collect(Collectors.toList());
        Response<List<GoodsItemRpcDTO>> listResponse = goodsServiceRpc.listGoodsItemByIds(goodsItemIds);
        if (!listResponse.isSuccess()) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_SPECIAL_GOODS_GET_ERROR);
        }
        Map<Integer, GoodsItemRpcDTO> goodsItemRpcDTOMap = listResponse.getData().stream()
            .collect(Collectors.toMap(GoodsItemRpcDTO::getId, goodsItemRpcDTO -> goodsItemRpcDTO));
        specialGoodsDTOS.forEach(specialGoodsDTO -> {
            GoodsItemRpcDTO dto = goodsItemRpcDTOMap.get(specialGoodsDTO.getGoodsItemId());
            if (dto != null) {
                Integer specialGoodsId = specialGoodsDTO.getId();
                BeanUtils.copyProperties(dto, specialGoodsDTO);
                specialGoodsDTO.setGoodsItemId(dto.getId());
                specialGoodsDTO.setId(specialGoodsId);
            }
        });
    }

    /**
     * 根据获取ids获取货品信息
     * 
     * @param goodsItemIds
     * @return
     */
    public List<GoodsItemRpcDTO> listGoodsItem(List<Integer> goodsItemIds) {
        Response<List<GoodsItemRpcDTO>> listResponse = goodsServiceRpc.listGoodsItemByIds(goodsItemIds);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        } else {
            return null;
        }
    }

    /**
     * 获取分销商商品可视范围数据
     * 
     * @param distributorId
     * @return
     */
    // @Cached(name = "distributorGoodsControl.", key = "#distributorId")
    public DistributorGoodsControlRpcDTO distributorGoodsControl(Integer distributorId) {
        DistributorGoodsControlRpcDTO distributorGoodsControlRpcDTO = new DistributorGoodsControlRpcDTO();
        Integer firstDistributorId = null;
        //查询该分销商的所有上级记录
        List<DistributorTreePathDO> distributorTreePathDOS =
            treePathMapper.listByDistributorDescendantId(distributorId);
        List<Integer> brandIds = new ArrayList<>();
        List<Integer> categoryIds = new ArrayList<>();
        List<Integer> goodsIds = new ArrayList<>();
        List<Integer> noBrandIds = new ArrayList<>();
        List<Integer> noCategoryIds = new ArrayList<>();
        List<Integer> noGoodsIds = new ArrayList<>();
        /** 如果为空情况说明是一级分销商 */
        if (CollectionUtils.isEmpty(distributorTreePathDOS)) {
            firstDistributorId = distributorId;
        } else {
            firstDistributorId = distributorTreePathDOS.stream()
                    .max(Comparator.comparing(DistributorTreePathDO::getTreeNode)).get().getDistributorAncestorId();
        }
        /** 获取品牌、品类和商品可视范围 */
        //一级分销商可视品牌
        DistributorBrandRelevanceDO brandRelevanceDO = brandRelevanceMapper.selectByDistributorId(firstDistributorId);
        if (brandRelevanceDO != null) {
            brandIds.addAll(Arrays
                .stream(
                    brandRelevanceDO.getBrandIds().substring(1, brandRelevanceDO.getBrandIds().length() - 1).split(","))
                .mapToInt(Integer::parseInt).boxed().distinct().collect(Collectors.toList()));
        }
        //一级分销商可视品类
        DistributorCategoryRelevanceDO categoryRelevanceDO =
            categoryRelevanceMapper.selectByDistributorId(firstDistributorId);
        if (categoryRelevanceDO != null) {
            categoryIds.addAll(Arrays
                .stream(categoryRelevanceDO.getCategoryIds()
                    .substring(1, categoryRelevanceDO.getCategoryIds().length() - 1).split(","))
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
        }
        //一级分销商可视商品表
        DistributorGoodsRelevanceDO goodsRelevanceDO = goodsRelevanceMapper.selectByDistributorId(firstDistributorId);
        if (goodsRelevanceDO != null) {
            goodsIds.addAll(Arrays
                .stream(
                    goodsRelevanceDO.getGoodsIds().substring(1, goodsRelevanceDO.getGoodsIds().length() - 1).split(","))
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
        }

        //查询全部可视商品
        List<Integer> goodsIdList=goodsServiceRpc.allVisibleProducts().getData();
        //查询全部可视品牌
        List<Integer> brandsIdList=goodsServiceRpc.allVisibleBrands().getData();

        /** 剔除品牌和商品不可视范围 */
        if (!firstDistributorId.equals(distributorId)) {
            //该分销商不可视品牌
            DistributorBrandRelevanceNoDO brandRelevanceNoDO =
                brandRelevanceNoMapper.selectByDistributorId(distributorId);
            if (brandRelevanceNoDO != null) {
                noBrandIds =
                    Arrays
                        .stream(brandRelevanceNoDO.getBrandIds()
                            .substring(1, brandRelevanceNoDO.getBrandIds().length() - 1).split(","))
                        .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                Arrays.stream(brandRelevanceNoDO.getBrandIds()
                    .substring(1, brandRelevanceNoDO.getBrandIds().length() - 1).split(",")).mapToInt(Integer::parseInt)
                    .boxed().collect(Collectors.toList());
            }
            //该分销商不可视商品
            DistributorGoodsRelevanceNoDO goodsRelevanceNoDO =
                goodsRelevanceNoMapper.selectByDistributorId(distributorId);
            if (goodsRelevanceNoDO != null) {
                noGoodsIds =
                    Arrays
                        .stream(goodsRelevanceNoDO.getGoodsIds()
                            .substring(1, goodsRelevanceNoDO.getGoodsIds().length() - 1).split(","))
                        .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                Arrays.stream(goodsRelevanceNoDO.getGoodsIds()
                    .substring(1, goodsRelevanceNoDO.getGoodsIds().length() - 1).split(",")).mapToInt(Integer::parseInt)
                    .boxed().collect(Collectors.toList());
            }
        }
        distributorGoodsControlRpcDTO.setBrandIds(brandIds);
        distributorGoodsControlRpcDTO.setCategoryIds(categoryIds);
        distributorGoodsControlRpcDTO.setGoodsIds(goodsIds);
        distributorGoodsControlRpcDTO.setNoBrandIds(noBrandIds);
        distributorGoodsControlRpcDTO.setNoCategoryIds(noCategoryIds);
        distributorGoodsControlRpcDTO.setNoGoodsIds(noGoodsIds);
        distributorGoodsControlRpcDTO.setAllVisibleProducts(goodsIdList);
        distributorGoodsControlRpcDTO.setAllVisibleBrands(brandsIdList);
        return distributorGoodsControlRpcDTO;
    }

    /**
     * 获取一级分销商品牌品类可视范围数据
     * 
     * @param distributorId
     * @return
     */
    public DistributorBrandCategoryControlRpcDTO distributorBrandCategoryControl(Integer distributorId) {
        DistributorBrandCategoryControlRpcDTO controlRpcDTO = new DistributorBrandCategoryControlRpcDTO();
        DistributorBrandRelevanceDO brandRelevanceDO = brandRelevanceMapper.selectByDistributorId(distributorId);
        if (brandRelevanceDO != null) {
            controlRpcDTO.setBrandIds(Arrays
                .stream(
                    brandRelevanceDO.getBrandIds().substring(1, brandRelevanceDO.getBrandIds().length() - 1).split(","))
                .mapToInt(Integer::parseInt).boxed().distinct().collect(Collectors.toList()));
        }
        DistributorCategoryRelevanceDO categoryRelevanceDO =
            categoryRelevanceMapper.selectByDistributorId(distributorId);
        if (categoryRelevanceDO != null) {
            controlRpcDTO.setCategoryIds(Arrays
                .stream(categoryRelevanceDO.getCategoryIds()
                    .substring(1, categoryRelevanceDO.getCategoryIds().length() - 1).split(","))
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
        }
        return controlRpcDTO;
    }

    /**
     * 根据品牌id获取可视分销商范围
     * 
     * @param brandId
     * @return
     */
    public List<Integer> getDistributorIdsByBrandId(Integer brandId) {
        List<Integer> distributorIds =
            brandRelevanceMapper.listDistributorIdsByBrandId("," + String.valueOf(brandId) + ",");
        return distributorIds;
    }

    /**
     * 根据品类id获取可视分销商范围
     * 
     * @param categoryId
     * @return
     */
    public List<Integer> getDistributorIdsByCategoryId(Integer categoryId) {
        List<Integer> distributorIds =
            categoryRelevanceMapper.listDistributorIdsByCategoryId("," + String.valueOf(categoryId) + ",");
        return distributorIds;
    }

    /**
     * 获取分销商所有价格等级数据(货品)
     * 
     * @param distributorId
     * @return
     */
    public DistributorScalePriceControlRpcDTO distributorScalePriceControl(Integer distributorId,
        List<Integer> itemIds) {
        DistributorScalePriceControlRpcDTO scalePriceControlRpcDTO = distributorScalePriceControl(distributorId);
        // 获取分销商特价
        if (!CollectionUtils.isEmpty(itemIds)) {
            List<DistributorSpecialGoodsDO> specialGoodsDOS =
                specialGoodsMapper.listByDistributorIdAndGoodsItemIds(distributorId, itemIds);
            if (!CollectionUtils.isEmpty(specialGoodsDOS)) {
                scalePriceControlRpcDTO
                    .setSpecials(DistributorConvertor.toDistributorSpecialGoodsRpcDTO(specialGoodsDOS));
            }
        }
        return scalePriceControlRpcDTO;
    }

    /**
     * 获取分销商所有价格等级数据(商品)
     *
     * @param distributorId
     * @return
     */
    public DistributorScalePriceControlRpcDTO distributorScalePriceControlByGoodsIds(Integer distributorId,
        List<Integer> goodsIds) {
        DistributorScalePriceControlRpcDTO scalePriceControlRpcDTO = distributorScalePriceControl(distributorId);
        // 获取分销商特价(暂时用不到)
        if (!CollectionUtils.isEmpty(goodsIds)) {
            List<DistributorSpecialGoodsDO> specialGoodsDOS =
                specialGoodsMapper.listByDistributorIdAndGoodsIds(distributorId, goodsIds);
            if (!CollectionUtils.isEmpty(specialGoodsDOS)) {
                scalePriceControlRpcDTO
                    .setSpecials(DistributorConvertor.toDistributorSpecialGoodsRpcDTO(specialGoodsDOS));
            }
        }
        return scalePriceControlRpcDTO;
    }

    /**
     * 获取分销商所有价格等级数据
     * 
     * @return
     */
    public DistributorScalePriceControlRpcDTO distributorScalePriceControl(Integer distributorId) {
        DistributorScalePriceControlRpcDTO scalePriceControlRpcDTO = new DistributorScalePriceControlRpcDTO();
        List<OneScalePriceRpcDTO> oneScalePriceRpcDTOS = new ArrayList<>();
        Integer firstDistributorId = null;
        List<DistributorTreePathDO> distributorTreePathDOS =
            treePathMapper.listByDistributorDescendantId(distributorId);
        /** 如果为空情况说明是一级分销商 */
        if (CollectionUtils.isEmpty(distributorTreePathDOS)) {
            scalePriceControlRpcDTO.setTreeNode(1);
            firstDistributorId = distributorId;
        } else {
            DistributorTreePathDO maxTreePathDO =
                distributorTreePathDOS.stream().max(Comparator.comparing(DistributorTreePathDO::getTreeNode)).get();
            firstDistributorId = maxTreePathDO.getDistributorAncestorId();
            scalePriceControlRpcDTO.setTreeNode(maxTreePathDO.getTreeNode() + 1);
        }
        /** 获取分销商价格等级 */
        /** 获取一级分销商价格等级 */
        List<DistributorOneScalePriceDO> scalePriceDOS = oneScalePriceMapper.listByDistributorId(firstDistributorId);
        oneScalePriceRpcDTOS = DistributorConvertor.toOneScalePriceRpcDTOList(scalePriceDOS);
        scalePriceControlRpcDTO.setOneScalePrices(oneScalePriceRpcDTOS);
        /** 获取多级分销商价格等级 */
        if (!firstDistributorId.equals(distributorId)) {
            List<Integer> nextDistributorIds = distributorTreePathDOS.stream()
                .map(DistributorTreePathDO::getDistributorAncestorId).collect(Collectors.toList());
            if (nextDistributorIds == null) {
                nextDistributorIds = new ArrayList<>();
            }
            nextDistributorIds.add(distributorId);
            List<NextScalePriceFormulaDO> nextScalePriceFormulaDOS =
                nextScalePriceMapper.listByNextDistributorIds(nextDistributorIds);
            if (!CollectionUtils.isEmpty(nextScalePriceFormulaDOS)) {
                List<Integer> nextScalePriceIds = nextScalePriceFormulaDOS.stream()
                    .filter(nextScalePriceDO -> nextScalePriceDO.getSpecialFlag().equals(SPECIAL_FLAG_1))
                    .map(NextScalePriceFormulaDO::getId).collect(Collectors.toList());
                List<NextScalePriceSpecialFormulaDO> scalePriceSpecialDOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(nextScalePriceIds)) {
                    scalePriceSpecialDOS = specialBrandCategoryMapper.listByNextScalePriceIds(nextScalePriceIds);
                }
                List<NextScalePriceRpcDTO> dtos =
                    DistributorConvertor.toNextScalePriceRpcDTOList(nextScalePriceFormulaDOS, scalePriceSpecialDOS);
                scalePriceControlRpcDTO.setNextScalePrices(dtos);
            }
        }
        return scalePriceControlRpcDTO;
    }

    /**
     * 获取分销商对应商品价格等级数据
     * 
     * @return
     */
    public DistributorGoodsScalePriceControlRpcDTO
        distributorGoodsScalePriceControl(DistributorGoodsScalePriceRpcQry qry) {
        Integer distributorId = qry.getDistributorId();
        DistributorGoodsScalePriceControlRpcDTO goodsScalePriceControlRpcDTO =
            new DistributorGoodsScalePriceControlRpcDTO();
        Integer firstDistributorId = null;
        List<DistributorTreePathDO> distributorTreePathDOS =
            treePathMapper.listByDistributorDescendantId(distributorId);
        /** 如果为空情况说明是一级分销商 */
        if (CollectionUtils.isEmpty(distributorTreePathDOS)) {
            goodsScalePriceControlRpcDTO.setTreeNode(1);
            firstDistributorId = distributorId;
        } else {
            DistributorTreePathDO maxTreePathDO =
                distributorTreePathDOS.stream().max(Comparator.comparing(DistributorTreePathDO::getTreeNode)).get();
            firstDistributorId = maxTreePathDO.getDistributorAncestorId();
            goodsScalePriceControlRpcDTO.setTreeNode(maxTreePathDO.getTreeNode() + 1);
        }
        /** 获取分销商价格等级 */
        /** 获取一级分销商价格等级 */
        List<DistributorOneScalePriceDO> scalePriceDOS = oneScalePriceMapper.listByDistributorId(firstDistributorId);
        List<DistributorOneScalePriceDO> oneScalePriceDOS = new ArrayList<>();
        qry.getGoodsScalePrices().forEach(goodsScalePrice -> {
            DistributorOneScalePriceDO oneScalePriceDO = null;
            if (goodsScalePrice.getCategoryId() != null) {
                Optional<DistributorOneScalePriceDO> optional = scalePriceDOS.stream()
                    .filter(scalePriceDO -> scalePriceDO.getBrandId() != null
                        && scalePriceDO.getBrandId().equals(goodsScalePrice.getBrandId())
                        && scalePriceDO.getCategoryId() != null
                        && scalePriceDO.getCategoryId().equals(goodsScalePrice.getCategoryId()))
                    .findFirst();
                if (optional != null && optional.isPresent()) {
                    oneScalePriceDO = optional.get();
                }
            }
            if (oneScalePriceDO == null) {
                Optional<DistributorOneScalePriceDO> optional = scalePriceDOS.stream()
                    .filter(scalePriceDO -> scalePriceDO.getBrandId() != null
                        && scalePriceDO.getBrandId().equals(goodsScalePrice.getBrandId())
                        && (scalePriceDO.getCategoryId() == null || scalePriceDO.getCategoryId() == 0))
                    .findFirst();
                if (optional != null && optional.isPresent()) {
                    oneScalePriceDO = optional.get();
                }
            }
            if (oneScalePriceDO == null) {
                Optional<
                    DistributorOneScalePriceDO> optional =
                        scalePriceDOS.stream()
                            .filter(scalePriceDO -> (scalePriceDO.getBrandId() == 0 || scalePriceDO.getBrandId() == 0)
                                && (scalePriceDO.getCategoryId() == null || scalePriceDO.getCategoryId() == 0))
                            .findFirst();
                if (optional != null && optional.isPresent()) {
                    oneScalePriceDO = optional.get();
                    oneScalePriceDOS.add(oneScalePriceDO);
                }
            }
        });
        goodsScalePriceControlRpcDTO
            .setOneScalePrices(DistributorConvertor.toOneScalePriceRpcDTOList(oneScalePriceDOS));
        /** 获取多级分销商价格等级 */
        if (!firstDistributorId.equals(distributorId)) {
            List<Integer> nextDistributorIds = distributorTreePathDOS.stream()
                .map(DistributorTreePathDO::getDistributorAncestorId).collect(Collectors.toList());
            if (nextDistributorIds == null) {
                nextDistributorIds = new ArrayList<>();
            }
            nextDistributorIds.add(distributorId);
            List<NextScalePriceFormulaDO> nextScalePriceFormulaDOS =
                nextScalePriceMapper.listByNextDistributorIds(nextDistributorIds);
            if (!CollectionUtils.isEmpty(nextScalePriceFormulaDOS)) {
                List<Integer> nextScalePriceIds = nextScalePriceFormulaDOS.stream()
                    .filter(nextScalePriceDO -> nextScalePriceDO.getSpecialFlag().equals(SPECIAL_FLAG_1))
                    .map(NextScalePriceFormulaDO::getId).collect(Collectors.toList());
                List<NextScalePriceSpecialFormulaDO> scalePriceSpecialDOS =
                    specialBrandCategoryMapper.listByNextScalePriceIds(nextScalePriceIds);
                List<NextScalePriceRpcDTO> dtos =
                    DistributorConvertor.toNextScalePriceRpcDTOList(nextScalePriceFormulaDOS, scalePriceSpecialDOS);
                goodsScalePriceControlRpcDTO.setNextScalePrices(dtos);
            }
        }
        /** 获取一级分销商特价商品 */
        List<DistributorSpecialGoodsDO> specialGoodsDOS =
            specialGoodsMapper.listByDistributorIdAndGoodsItemIds(distributorId, qry.getGoodsItemIds());
        if (!CollectionUtils.isEmpty(specialGoodsDOS)) {
            goodsScalePriceControlRpcDTO
                .setSpecialPrices(DistributorConvertor.toDistributorSpecialGoodsRpcDTO(specialGoodsDOS));
        }
        return goodsScalePriceControlRpcDTO;
    }

    /**
     * 根据分销商Ids查询分销商基本信息
     * 
     * @param ids
     * @return
     */
    public List<DistributorRpcDTO> distributorByIds(List<Integer> ids) {
        List<DistributorRpcDO> distributorDOS = distributorRpcMapper.listDistributorRpcByIds(ids);
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(distributorDOS.stream().map(DistributorRpcDO::getSalesId).collect(Collectors.toList()));
        userIds.addAll(distributorDOS.stream().map(DistributorRpcDO::getCoordinatorId).collect(Collectors.toList()));
        Map<Integer, UserRpcDTO> userRpcDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(userIds)) {
            userIds = userIds.stream().distinct().collect(Collectors.toList());
            com.bat.dubboapi.system.common.Response<List<UserRpcDTO>> response =
                userServiceRpc.getUserByIds(userIds);
            if (response.isSuccess()) {
                List<UserRpcDTO> userRpcDTOS = response.getData();
                if (!CollectionUtils.isEmpty(userRpcDTOS)) {
                    userRpcDTOMap.putAll(
                        userRpcDTOS.stream().collect(Collectors.toMap(UserRpcDTO::getId, userRpcDTO -> userRpcDTO)));
                }
            }
        }
        return DistributorConvertor.toDistributorRpcDTOList(distributorDOS, userRpcDTOMap);
    }

    /**
     * 根据分销商Id查询分销商基本信息
     * 
     * @param id
     * @return
     */
    public DistributorRpcDTO distributorById(Integer id) {
        log.info("请求的分销商id{}", id);
        DistributorRpcDO distributorDO = distributorRpcMapper.selectDistributorRpcById(id);
        log.info("得到的分销商信息{}", JSONObject.toJSONString(distributorDO));
        List<Integer> userIds = new ArrayList<>();
        if (distributorDO.getSalesId() != null) {
            userIds.add(distributorDO.getSalesId());
        }
        if (distributorDO.getCoordinatorId() != null) {
            userIds.add(distributorDO.getCoordinatorId());
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            userIds = userIds.stream().distinct().collect(Collectors.toList());
        }
        Map<Integer, UserRpcDTO> userRpcDTOMap = new HashMap<>();
        // 业务员获取
        if (!CollectionUtils.isEmpty(userIds)) {
            userIds = userIds.stream().distinct().collect(Collectors.toList());
            com.bat.dubboapi.system.common.Response<List<UserRpcDTO>> response =
                userServiceRpc.getUserByIds(userIds);
            if (response.isSuccess()) {
                List<UserRpcDTO> userRpcDTOS = response.getData();
                if (!CollectionUtils.isEmpty(userRpcDTOS)) {
                    userRpcDTOMap.putAll(
                        userRpcDTOS.stream().collect(Collectors.toMap(UserRpcDTO::getId, userRpcDTO -> userRpcDTO)));
                }
            }
        }

        // 获取上级分销商数据
        DistributorDO ancestorDistributorDO = null;
        DistributorExtendDataDO ancestorExtendDataDO = null;
        List<DistributorSalesAreaDO> salesAreaDOS = distributorSalesAreaMapper.listByDistributorId(id);
        if (distributorDO.getTreeNode() > 1) {
            List<DistributorTreePathDO> treePathDOS =
                treePathMapper.listByDistributorDescendantId(distributorDO.getId());
            DistributorTreePathDO minTreePathDO =
                treePathDOS.stream().min(Comparator.comparing(DistributorTreePathDO::getTreeNode)).get();
            ancestorDistributorDO = distributorMapper.selectByPrimaryKey(minTreePathDO.getDistributorAncestorId());
            ancestorExtendDataDO = extendDataMapper.getByDistributorId(minTreePathDO.getDistributorAncestorId());
            if (distributorDO.getErpFlag().equals(ERP_FLAG_0) || distributorDO.getOrderTypeId() == null) {
                // 销售区域根据最上层分销商
                DistributorTreePathDO maxTreePathDO =
                    treePathDOS.stream().max(Comparator.comparing(DistributorTreePathDO::getTreeNode)).get();
                salesAreaDOS = distributorSalesAreaMapper.listByDistributorId(maxTreePathDO.getDistributorAncestorId());
                distributorDO.setOrderTypeId(
                    distributorRpcMapper.selectOrderTypeIdByDistributorId(maxTreePathDO.getDistributorAncestorId()));
            }
        }
        return DistributorConvertor.toDistributorRpcDTO(distributorDO, userRpcDTOMap, salesAreaDOS,
            ancestorDistributorDO, ancestorExtendDataDO);
    }

    /**
     * 根据分销商Id查询分销商基本信息(C端客户归属分销商信息查询)
     *
     * @param id
     * @return
     */
    public DistributorRpcDTO customerDistributorById(Integer id) {
        DistributorRpcDO distributorDO = distributorRpcMapper.selectDistributorRpcById(id);
        if (distributorDO == null) {
            return null;
        }
        // 销售区域获取
        List<DistributorSalesAreaDO> salesAreaDOS = distributorSalesAreaMapper.listByDistributorId(id);
        return DistributorConvertor.toDistributorRpcDTO(distributorDO, salesAreaDOS);
    }

    /**
     * 根据分销商Id查询分销商地址国家
     * 
     * @param id
     * @return
     */
    public Integer distributorCountryById(Integer id) {
        List<DistributorAddressDO> addressDOS = addressMapper.listByDistributorIdAndAddressType(id, ADDRESS_TYPE_1);
        if (!CollectionUtils.isEmpty(addressDOS)) {
            return addressDOS.get(0).getCountryId();
        } else {
            return distributorConfig.getCountryChina();
        }
    }

    /**
     * 根据分销商id获取业务数据
     * 
     * @param id
     * @return
     */
    public DistributorBusinessRpcDTO getDistributorBusiness(Integer id) {
        // 一级分销商id
        Integer firstDistributorId = null;
        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(id);
        if (distributorDO.getTreeNode() == 1) {
            firstDistributorId = id;
        } else {
            List<DistributorTreePathDO> distributorTreePathDOS = treePathMapper.listByDistributorDescendantId(id);
            Optional<DistributorTreePathDO> first = distributorTreePathDOS.stream()
                .filter(distributorTreePathDO -> distributorTreePathDO.getTreeNode() == 1).findFirst();
            if (first != null && first.isPresent()) {
                firstDistributorId = first.get().getDistributorAncestorId();
            }
        }
        DistributorBusinessDO businessDO = businessMapper.getByDistributorId(firstDistributorId);
        List<DistributorSalesAreaDO> salesAreaDOS = distributorSalesAreaMapper.listByDistributorId(firstDistributorId);
        return DistributorConvertor.toDistributorBusinessRpcDTO(businessDO, distributorDO, salesAreaDOS);
    }

    /**
     * 根据分销商id获取活动id列表
     * 
     * @param id
     * @return
     */
    public DistributorPromitonGroupSeckillRpcDTO getDistributorPromotionGroupSeckill(Integer id) {
        DistributorPromitonGroupSeckillRpcDTO rpcDTO = new DistributorPromitonGroupSeckillRpcDTO();
        // 如果多级分销情况，需查看父级分销商是否支持活动同步

        //根据分销商id查询出分销商的所有层级关系
        List<DistributorTreePathDO> distributorTreePathDOS = treePathMapper.listByDistributorDescendantId(id);
        DistributorBusinessDO distributorBusinessDO = null;
        rpcDTO.setDistributionPromotionFlag(DISTRIBUTION_PROMOTION_FLAG_1);
        if (!CollectionUtils.isEmpty(distributorTreePathDOS)) {
            //从所有层级关系中取出顶层分销商
            DistributorTreePathDO maxTreePathDO =
                distributorTreePathDOS.stream().max(Comparator.comparing(DistributorTreePathDO::getTreeNode)).get();

            //查询顶层分销商的业务信息
            distributorBusinessDO =
                distributorBusinessMapper.getByDistributorId(maxTreePathDO.getDistributorAncestorId());

            if (!distributorBusinessDO.getPromotionScope().equals(PROMOTION_SCOPE_0)) {
                //取出该分销商的所有上级分销商
                List<Integer> distributorAncestorIds = distributorTreePathDOS.stream()
                    .map(DistributorTreePathDO::getDistributorAncestorId).collect(Collectors.toList());

                //将取出来的分销商的分销拓展信息查出来
                List<DistributorExtendDataDO> extendDataDOS =
                    extendDataMapper.listByDistributorIds(distributorAncestorIds);

                //设置上级分销活动是否同步下级
                extendDataDOS.forEach(extendDataDO -> {
                    if (extendDataDO.getDistributionPromotionFlag().equals(DISTRIBUTION_PROMOTION_FLAG_0)) {
                        rpcDTO.setDistributionPromotionFlag(DISTRIBUTION_PROMOTION_FLAG_0);
                    }
                });
            }
        } else {
            distributorBusinessDO = distributorBusinessMapper.getByDistributorId(id);
        }
        // 是否参与活动暂时只支持最顶级分销商配置
        rpcDTO.setPromotionScope(distributorBusinessDO.getPromotionScope());
        // 不参与活动情况直接返回
        if (distributorBusinessDO.getPromotionScope() != null
            && distributorBusinessDO.getPromotionScope().equals(PROMOTION_SCOPE_0)) {
            return rpcDTO;
        } else {
            rpcDTO.setPromotionTypes(distributorBusinessDO.getPromotionTypes());
        }
        if (rpcDTO.getDistributionPromotionFlag().equals(DISTRIBUTION_PROMOTION_FLAG_0)) {
            return rpcDTO;
        }
        if (rpcDTO.getPromotionScope().equals(PROMOTION_SCOPE_1)
            || rpcDTO.getPromotionTypes().contains(PROMOTION_TYPE_1)
            || rpcDTO.getPromotionTypes().contains(PROMOTION_TYPE_2)) {
            //查询顶层分销商全部促销活动
            DistributorPromotionRelevanceDO promotionRelevanceDO =
                promotionRelevanceMapper.selectByDistributorId(distributorBusinessDO.getDistributorId());
            if (promotionRelevanceDO != null) {
                //过滤所有促销活动id
                List<Integer> promotionIds = Arrays
                    .stream(promotionRelevanceDO.getPromotionIds()
                        .substring(1, promotionRelevanceDO.getPromotionIds().length() - 1).split(","))
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                rpcDTO.setPromotionIds(promotionIds);
            }
        }
        if (rpcDTO.getPromotionScope().equals(PROMOTION_SCOPE_1)
            || rpcDTO.getPromotionTypes().contains(PROMOTION_TYPE_3)) {
            //顶层分销商全部拼团秒杀活动可视
            DistributorGroupSeckillRelevanceDO groupSeckillRelevanceDO =
                groupSeckillRelevanceMapper.selectByDistributorId(distributorBusinessDO.getDistributorId());
            if (groupSeckillRelevanceDO != null) {
                //过滤拼团秒杀活动id
                List<Integer> groupSeckillIds = Arrays
                    .stream(groupSeckillRelevanceDO.getGroupSeckillIds()
                        .substring(1, groupSeckillRelevanceDO.getGroupSeckillIds().length() - 1).split(","))
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                rpcDTO.setGroupSeckilIds(groupSeckillIds);
            }
        }
        return rpcDTO;
    }

    /**
     * 根据业务员ids查找业务员管理的分销商ids
     * 
     * @param salesIds
     * @return
     */
    public List<Integer> getDistributorIdsBySalesIds(List<Integer> salesIds) {
        return businessMapper.listIdBySalesIds(salesIds);
    }

    /**
     * 根据分销商erp内码获取分销商信息
     * 
     * @param erpIds
     * @return
     */
    public List<DistributorNextNameRpcDTO> getDistributorNextByErpIds(List<Integer> erpIds) {
        List<DistributorErpIdRpcDO> erpIdRpcDOS = distributorRpcMapper.listDistributorErpIdRpcByErpIds(erpIds);
        return listDistributorErpIdRpcDO(erpIdRpcDOS);
    }

    /**
     * 根据分销商erp内码获取分销商基本信息
     * 
     * @param erpId
     * @return
     */
    public DistributorInfoRpcDTO getDistributorInfoByErpId(Integer erpId) {
        DistributorInfoDO distributorInfoDO = distributorRpcMapper.selectDistributorInfoDOByErpId(erpId);
        if (distributorInfoDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_NULL, MessageUtils.get(ErrorCode.B_DISTRIBUTOR_NULL));
        }
        DistributorInfoRpcDTO rpcDTO = new DistributorInfoRpcDTO();
        BeanUtils.copyProperties(distributorInfoDO, rpcDTO);
        return rpcDTO;
    }

    /**
     * 根据分销商ids获取分销商信息
     * 
     * @param distributorIds
     * @return
     */
    public List<DistributorNextNameRpcDTO> getDistributorNextByDistributorIds(List<Integer> distributorIds) {
        List<DistributorErpIdRpcDO> erpIdRpcDOS =
            distributorRpcMapper.listDistributorErpIdRpcByDistributorIds(distributorIds);
        return listDistributorErpIdRpcDO(erpIdRpcDOS);
    }

    /**
     * 根据分销商ids获取分销商名称
     * 
     * @param distributorIds
     * @return
     */
    public List<DistributorNameRpcDTO> getDistributorNameByDistributorIds(List<Integer> distributorIds) {
        List<DistributorNameDO> distributorNameDOS =
            distributorRpcMapper.listDistributorNameRpcByDistributorIds(distributorIds);
        return DistributorConvertor.toDistributorNameRpcDTOList(distributorNameDOS);
    }

    /**
     * 获取所有分销商名称
     *
     * @return
     */
    public List<DistributorNameRpcDTO> getAllDistributorNameOneTreeNode() {
        List<DistributorNameDO> distributorNameDOS = distributorRpcMapper.getAllDistributorNameOneTreeNode();
        return DistributorConvertor.toDistributorNameRpcDTOList(distributorNameDOS);
    }

    /**
     * 获取分销商信息（包含下级分销商基本信息）
     * 
     * @param erpIdRpcDOS
     * @return
     */
    private List<DistributorNextNameRpcDTO> listDistributorErpIdRpcDO(List<DistributorErpIdRpcDO> erpIdRpcDOS) {
        List<Integer> distributorIds =
            erpIdRpcDOS.stream().filter(erpIdRpcDO -> erpIdRpcDO.getTreeNode().intValue() > 1)
                .map(DistributorErpIdRpcDO::getId).collect(Collectors.toList());
        Map<Integer, DistributorTreePathDO> treePathDOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(distributorIds)) {
            List<DistributorTreePathDO> distributorTreePathDOS =
                treePathMapper.listByDistributorDescendantIds(distributorIds);
            treePathDOMap = distributorTreePathDOS.stream()
                .collect(Collectors.groupingBy(DistributorTreePathDO::getDistributorDescendantId,
                    Collectors.collectingAndThen(
                        Collectors
                            .reducing((c1, c2) -> c1.getTreeNode().intValue() > c2.getTreeNode().intValue() ? c1 : c2),
                        Optional::get)));
        }
        List<DistributorIdsDO> distributorIdsDOS = new ArrayList<>();
        if (treePathDOMap != null && treePathDOMap.size() > 0) {
            List<Integer> distributorAncestorIds = treePathDOMap.values().stream()
                .map(DistributorTreePathDO::getDistributorAncestorId).collect(Collectors.toList());
            distributorIdsDOS = distributorMapper.distributorIds(distributorAncestorIds);
        }
        return DistributorConvertor.toDistributorNextNameRpcDTOList(erpIdRpcDOS, treePathDOMap, distributorIdsDOS);
    }

    public List<GoodsItemPriceRpcDTO> listGoodsItemRetailPrice(List<Integer> itemIds) {
        Response<List<GoodsItemPriceRpcDTO>> listResponse = goodsServiceRpc.listGoodsItemRetailPrice(itemIds);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        }
        return null;
    }

    /**
     * 根据分销商id获取分销商扩展信息
     * 
     * @param distributorId
     * @return
     */
    public DistributorExtendDataRpcDTO getDistributorExtendData(Integer distributorId) {
        DistributorExtendDataDO extendDataDO = extendDataMapper.getByDistributorId(distributorId);
        return DistributorConvertor.toDistributorExtendDataRpcDTO(extendDataDO);
    }

    /**
     * 根据分销商id获取同步erp相关数据
     * 
     * @param distributorId
     * @return
     */
    public DistributorERPRpcDTO getDistributorERPData(Integer distributorId) {
        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(distributorId);
        DistributorExtendDataDO extendDataDO = extendDataMapper.getByDistributorId(distributorId);
        DistributorBusinessDO businessDO = businessMapper.getByDistributorId(distributorId);
        GroupDO groupDO = null;
        // 分销商分组改为多选；取第一个同步erp
        if (!StringUtils.isEmpty(businessDO.getDistributorGroupIds())) {
            String[] split = businessDO.getDistributorGroupIds().substring(1,businessDO.getDistributorGroupIds().length() - 1).split(",");
            groupDO = groupMapper.selectByPrimaryKey(Integer.valueOf(split[0]));
        }
        CategoryDO categoryDO = null;
        if (businessDO.getDistributorCategoryId() != null) {
            categoryDO = categoryMapper.selectByPrimaryKey(businessDO.getDistributorCategoryId());
        }
        DistributorFinancialDO financialDO = financialMapper.getByDistributorId(distributorId);
        TradeDO tradeDO = tradeMapper.selectByPrimaryKey(financialDO.getTradeId());
        List<DistributorAddressDO> addressDOS =
            addressMapper.listByDistributorIdAndAddressType(distributorId, ADDRESS_TYPE_1);
        List<DistributorContactsDO> contactsDOS =
            distributorContactsMapper.selectByDistributorIdAndOwnerFlag(distributorId, OWNER_FLAG_1);
        DistributorERPRpcDTO erpRpcDTO = DistributorConvertor.toDistributorERPRpcDTO(distributorDO, extendDataDO,
            businessDO, groupDO, categoryDO, financialDO, tradeDO, addressDOS, contactsDOS);
        return erpRpcDTO;
    }

    /**
     * 根据分销商分组ids获取分销商分组数据
     * 
     * @param distributorGroupIds
     * @return
     */
    public List<DistributorGroupRpcDTO> getDistributorGroupByDistributorGroupIds(List<Integer> distributorGroupIds) {
        List<GroupDO> groupDOS = groupMapper.listByDistributorIds(distributorGroupIds);
        return DistributorConvertor.toDistributorGroupRpcDTOList(groupDOS);
    }

    /**
     * 根据分销商id获取一级分销商信息
     * 
     * @param distributorId
     * @return
     */
    public List<DistributorTreePathRpcDTO> getDistributorTreePaths(Integer distributorId) {
        List<DistributorTreePathRpcDTO> distributorTreePathRpcDTOS = new ArrayList<>();
        List<DistributorTreePathDO> treePathDOS = treePathMapper.listByDistributorDescendantId(distributorId);
        for (DistributorTreePathDO distributorTreePathDO : treePathDOS) {
            DistributorTreePathRpcDTO distributorTreePathRpcDTO = new DistributorTreePathRpcDTO();
            BeanUtils.copyProperties(distributorTreePathDO, distributorTreePathRpcDTO);
            distributorTreePathRpcDTOS.add(distributorTreePathRpcDTO);
        }
        return distributorTreePathRpcDTOS;
    }

    /**
     * 根据国家、省、市、区id获取名称
     * 
     * @param regionId
     * @return
     */
    public String getRegionById(Integer regionId) {
        com.bat.dubboapi.system.common.Response<RegionRpcDTO> response = regionServiceRpc.getRegionById(regionId);
        if (response.isSuccess() && response.getData() != null) {
            return response.getData().getRegionName();
        } else {
            return null;
        }
    }

    /**
     * 根据品牌id获取品牌名称
     * 
     * @param brandId
     * @return
     */
    public String getBrandById(Integer brandId) {
        Response<UserBrandRpcDTO> response = brandServiceRpc.getBrandByBrandId(brandId);
        if (response.isSuccess() && response.getData() != null) {
            return response.getData().getName();
        } else {
            return null;
        }
    }

    /**
     * 根据品牌id获取品牌名称
     * 
     * @return
     */
    public String getScalePriceByScalePriceId(Integer scalePriceId) {
        Response<ScalePriceRpcDTO> response = scalePriceServiceRpc.getScalePriceByScalePriceId(scalePriceId);
        if (response.isSuccess() && response.getData() != null) {
            return response.getData().getName();
        } else {
            return null;
        }
    }

    public UpperDistributorRpcDTO getUpperDistributorId(Integer distributorId) {
        UpperDistributorRpcDTO upperDistributorRpcDTO = new UpperDistributorRpcDTO();
        UpperDistributorDO upperDistributorDO = distributorMapper.getUpperDistributorId(distributorId);
        if (upperDistributorDO == null) {
            return null;
        }
        BeanUtils.copyProperties(upperDistributorDO, upperDistributorRpcDTO);
        return upperDistributorRpcDTO;
    }

    /**
     * 获取上级分销商
     *
     * @param distributorId
     * @return
     */
    public DistributorTreePathDO getAncestorDistributorId(Integer distributorId) {
        return treePathMapper.selectByDistributorDescendantIdAndTreeNode(distributorId, 1);
    }

    public Integer goodsPromotionMaximumDistributor(Integer id) {
        // 根据分销商id查询所有祖先
        List<DistributorTreePathDO> distributorTreePathDOS = treePathMapper.listByDistributorDescendantId(id);

        if (!CollectionUtils.isEmpty(distributorTreePathDOS)) {
            // 拿出最大的那个祖先，也就是一级分销商
            DistributorTreePathDO maxTreePathDO =
                distributorTreePathDOS.stream().max(Comparator.comparing(DistributorTreePathDO::getTreeNode)).get();
            return maxTreePathDO.getDistributorAncestorId();
        } else {
            return null;
        }
    }

    public DistributorPayWayRpcDTO distributorPaymentWayById(Integer distributorId) {
        DistributorExtendDataDO dataDO = distributorMapper.distributorPaymentWayById(distributorId);
        DistributorPayWayRpcDTO dto = new DistributorPayWayRpcDTO();
        BeanUtils.copyProperties(dataDO,dto);
        return dto;
    }
}
