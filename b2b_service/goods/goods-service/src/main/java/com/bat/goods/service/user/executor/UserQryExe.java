package com.bat.goods.service.user.executor;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.goods.api.user.dto.*;
import com.bat.goods.api.user.dto.data.*;
import com.bat.goods.dao.brand.BrandMapper;
import com.bat.goods.dao.goods.*;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.dao.tag.TagMapper;
import com.bat.goods.service.classify.executor.ClassifyQryExe;
import com.bat.goods.service.common.Constant;
import com.bat.goods.service.common.MobileModuleType;
import com.bat.goods.service.user.convertor.UserConvertor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alicp.jetcache.anno.Cached;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.*;
import com.bat.dubboapi.promotion.dto.data.PromotionGroupSeckillGoodsRpcDTO;
import com.bat.dubboapi.promotion.dto.data.PromotionGroupSeckillIdsByAllDistributorDTO;
import com.bat.goods.api.base.BaseIds;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.classify.dto.data.ClassifyRecommendDTO;
import com.bat.goods.api.tag.dto.data.TagsDTO;
import com.bat.goods.api.user.dto.*;
import com.bat.goods.api.user.dto.data.*;
import com.bat.goods.dao.category.CategoryMapper;
import com.bat.goods.dao.classify.ClassifyBannerRelevanceMapper;
import com.bat.goods.dao.classify.ClassifyMapper;
import com.bat.goods.dao.classify.dataobject.ClassifyBannerRelevanceDO;
import com.bat.goods.dao.classify.dataobject.ClassifyDO;
import com.bat.goods.dao.goods.*;
import com.bat.goods.dao.goods.dataobject.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/29 11:27
 */
@Component
public class UserQryExe {

    @Resource
    private ClassifyMapper classifyMapper;
    @Resource
    private UserRpcQryExe rpcQryExe;
    @Resource
    private GoodsStoreMapper storeMapper;
    @Resource
    private GoodsUserMapper userMapper;

    @Resource
    private GoodsMinMaxPriceMapper minMaxPriceMapper;

    @Resource
    private GoodsStoreMobileMapper mobileMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private GoodsDistributorCollectionMapper collectionMapper;
    @Resource
    private GoodsPromotionPriceMapper goodsPromotionPriceMapper;
    @Resource
    private BrandMapper brandMapper;
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ClassifyBannerRelevanceMapper classifyBannerRelevanceMapper;

    @Resource
    private ClassifyQryExe classifyQryExe;

    /**
     * 获取商品分类列表(不分页)
     * 
     * @param qry
     * @return
     */
    @Cached(name = "classify:", key = "#qry.cacheKey")
    public UserClassifyInfoDTO listClassify(UserClassifyListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        Map<String, Object> subQryMap = new HashMap();
        subQryMap.putAll(qryMap);
        subQryMap.put("openFlag", Constant.OPEN_YES);
        List<ClassifyDO> classifyDOList = classifyMapper.listClassify(subQryMap);
        List<UserClassifyDTO> dtos = UserConvertor.toUserClassifyDTOList(classifyDOList);
        // 组装子分类
        List<Integer> classifyIds = classifyDOList.stream().map(ClassifyDO::getId).collect(Collectors.toList());
        subQryMap.put("classifyIds", classifyIds);
        List<ClassifyDO> subClassifyDOList = classifyMapper.listSubClassifyByClassifyIds(subQryMap);
        if (!CollectionUtils.isEmpty(subClassifyDOList)) {
            Map<Integer, UserClassifyDTO> classifyDTOMap =
                dtos.stream().collect(Collectors.toMap(UserClassifyDTO::getId, userClassifyDTO -> userClassifyDTO));
            List<UserClassifyDTO> subDtos = UserConvertor.toUserClassifyDTOList(subClassifyDOList);
            subDtos.forEach(subDto -> {
                UserClassifyDTO dto = classifyDTOMap.get(subDto.getParentId());
                List<UserClassifyDTO> subClassifies = dto.getSubClassifies();
                if (subClassifies == null) {
                    subClassifies = new ArrayList<>();
                    dto.setSubClassifies(subClassifies);
                }
                subClassifies.add(subDto);
            });
        }

        // 增加banner图数据
        List<ClassifyBannerRelevanceDO> classifyBannerRelevanceDOS =
            classifyBannerRelevanceMapper.listByClassifyIds(classifyIds);
        List<UserClassifyBannerDTO> userClassifyBannerDTOS = new ArrayList<>();
        for (ClassifyBannerRelevanceDO classifyBannerRelevanceDO : classifyBannerRelevanceDOS) {
            UserClassifyBannerDTO userClassifyBannerDTO = new UserClassifyBannerDTO();
            BeanUtils.copyProperties(classifyBannerRelevanceDO, userClassifyBannerDTO);
            userClassifyBannerDTOS.add(userClassifyBannerDTO);
        }
        // 分组
        Map<Integer, List<UserClassifyBannerDTO>> bannerMap =
            userClassifyBannerDTOS.stream().collect(Collectors.groupingBy(UserClassifyBannerDTO::getClassifyId));
        for (UserClassifyDTO userClassifyDTO : dtos) {
            List<UserClassifyBannerDTO> bannerDTOS = bannerMap.get(userClassifyDTO.getId());
            if (bannerDTOS != null) {
                userClassifyDTO.setUserClassifyBannerDTOS(bannerDTOS);
            }
        }

        ClassifyRecommendDTO classifyRecommendDTO = classifyQryExe.recommendInfo(Constant.OPEN_YES);
        UserClassifyInfoDTO userClassifyInfoDTO = new UserClassifyInfoDTO();
        userClassifyInfoDTO.setRecommend(classifyRecommendDTO);
        userClassifyInfoDTO.setClassify(dtos);
        return userClassifyInfoDTO;
    }

    /**
     * 根据父级分类id获取下级分类列表(不分页)
     * 
     * @param qry
     * @return
     */
    public List<UserClassifyDTO> listSubClassify(UserSubClassifyListQry qry) {
        List<ClassifyDO> classifyDOS = classifyMapper.listClassifyByParentId(qry.getClassifyId(), Constant.OPEN_YES);
        List<UserClassifyDTO> dtos = UserConvertor.toUserClassifyDTOList(classifyDOS);
        return dtos;
    }

    /**
     * 根据栏目获取商品列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserGoodsListDTO> columnGoodsList(UserGoodsListColumnQry qry, String userId) {
        // userId = "1000";
        PromotionGroupSeckillGoodsRpcDTO promotionGroupSeckillGoods = getPromotionGroupSeckillGoods(userId);
        Date newDate = new Date(System.currentTimeMillis() - (long)rpcQryExe.getNewproductTime() * 24 * 60 * 60 * 1000);
        Map<String, Object> qryMap = getGoodsListQryMap(qry, userId, promotionGroupSeckillGoods.getPromotionGoodsIds(),
            promotionGroupSeckillGoods.getGroupGoodsIds(), promotionGroupSeckillGoods.getSeckillGoodsIds(), newDate);
        PageHelper.startPage(qry.getPage(), qry.getSize());

        List<UserGoodsListDO> goodsListDOS = null;
        if (qry.getClassifyId() == null) {
            if (StringUtils.isNotBlank(userId)) {
                goodsListDOS = storeMapper.listUserGoodsListByColumn(qryMap);
            } else {
                goodsListDOS = storeMapper.listGoodsListByColumn(qryMap);
            }
        } else {
            if (StringUtils.isNotBlank(userId)) {
                goodsListDOS = storeMapper.listUserGoodsListByColumnAndClassify(qryMap);
            } else {
                goodsListDOS = storeMapper.listGoodsListByColumnAndClassify(qryMap);
            }
        }
        PageInfo pageInfo = new PageInfo(goodsListDOS);
        List<UserGoodsListDTO> userGoodsListDTOS = UserConvertor.toUserGoodsListDTOList(pageInfo.getList(), null);
        checkCollectionGoods(userGoodsListDTOS, userId, null, newDate, promotionGroupSeckillGoods);
        getGoodsTags(userGoodsListDTOS);
        pageInfo.setList(userGoodsListDTOS);
        return pageInfo;
    }

    /**
     * 根据板块获取商品列表(分页)
     *
     * @param qry
     * @return
     */
    public PageInfo<UserGoodsListDTO> sectionGoodsList(UserGoodsListSectionQry qry, String userId) {
        PromotionGroupSeckillGoodsRpcDTO promotionGroupSeckillGoods = getPromotionGroupSeckillGoods(userId);
        Date newDate = new Date(System.currentTimeMillis() - (long)rpcQryExe.getNewproductTime() * 24 * 60 * 60 * 1000);
        Map<String, Object> qryMap = getGoodsListQryMap(qry, userId, promotionGroupSeckillGoods.getPromotionGoodsIds(),
            promotionGroupSeckillGoods.getGroupGoodsIds(), promotionGroupSeckillGoods.getSeckillGoodsIds(), newDate);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<UserGoodsListDO> goodsListDOS = null;
        if (qry.getClassifyId() == null) {
            if (StringUtils.isNotBlank(userId)) {
                goodsListDOS = storeMapper.listUserGoodsListBySection(qryMap);
            } else {
                goodsListDOS = storeMapper.listGoodsListBySection(qryMap);
            }
        } else {
            if (StringUtils.isNotBlank(userId)) {
                goodsListDOS = storeMapper.listUserGoodsListBySectionAndClassify(qryMap);
            } else {
                goodsListDOS = storeMapper.listGoodsListBySectionAndClassify(qryMap);
            }
        }
        PageInfo pageInfo = new PageInfo(goodsListDOS);
        List<UserGoodsListDTO> userGoodsListDTOS = UserConvertor.toUserGoodsListDTOList(pageInfo.getList(), null);
        checkCollectionGoods(userGoodsListDTOS, userId, null, newDate, promotionGroupSeckillGoods);
        getGoodsTags(userGoodsListDTOS);
        pageInfo.setList(userGoodsListDTOS);
        return pageInfo;
    }

    /**
     * 根据移动端配置id获取商品列表(分页)
     *
     * @param qry
     * @return
     */
    public PageInfo<UserGoodsListDTO> mobileGoodsList(UserGoodsListMobileQry qry, String userId) {
        if (qry.getMobileId() == null && qry.getAppointType() == MobileModuleType.APPOINT_ITEM) {
            throw GoodsException.buildException(ErrorCode.SYSTEM_EXCEPTION);
        }
        if (qry.getModuleType() == null && qry.getAppointType() == MobileModuleType.APPOINT_ITEM) {
            throw GoodsException.buildException(ErrorCode.SYSTEM_EXCEPTION);
        }
        if (qry.getAppointType() == MobileModuleType.APPOINT_TYPE_CATEGORY
            || qry.getAppointType() == MobileModuleType.APPOINT_BRAND) {
            if (qry.getPointIds() == null || qry.getPointIds().size() == 0) {
                throw GoodsException.buildException(ErrorCode.SYSTEM_EXCEPTION);
            }
        }
        List<UserGoodsListDO> goodsListDOS = null;
        PromotionGroupSeckillGoodsRpcDTO promotionGroupSeckillGoods = getPromotionGroupSeckillGoods(userId);
        Date newDate = new Date(System.currentTimeMillis() - (long)rpcQryExe.getNewproductTime() * 24 * 60 * 60 * 1000);
        Map<String, Object> qryMap = getGoodsListQryMap(qry, userId, promotionGroupSeckillGoods.getPromotionGoodsIds(),
            promotionGroupSeckillGoods.getGroupGoodsIds(), promotionGroupSeckillGoods.getSeckillGoodsIds(), newDate);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        if (qry.getClassifyId() == null) {
            if (StringUtils.isNotBlank(userId)) {
                goodsListDOS = mobileMapper.listUserGoodsListByMobile(qryMap);
            } else {
                goodsListDOS = mobileMapper.listGoodsListByMobile(qryMap);
            }
        } else {
            if (StringUtils.isNotBlank(userId)) {
                goodsListDOS = mobileMapper.listUserGoodsListByMobileAndClassify(qryMap);
            } else {
                goodsListDOS = mobileMapper.listGoodsListByMobileAndClassify(qryMap);
            }
        }
        PageHelper.startPage(qry.getPage(), qry.getSize());
        PageInfo pageInfo = new PageInfo(goodsListDOS);
        List<UserGoodsListDTO> userGoodsListDTOS = UserConvertor.toUserGoodsListDTOList(pageInfo.getList(), null);
        checkCollectionGoods(userGoodsListDTOS, userId, null, newDate, promotionGroupSeckillGoods);
        getGoodsTags(userGoodsListDTOS);
        pageInfo.setList(userGoodsListDTOS);
        return pageInfo;
    }

    /**
     * 获取前台商品列表查询条件（过滤分销商可视范围）
     * 
     * @param obj
     * @param userId
     * @return
     */
    private Map<String, Object> getGoodsListQryMapTwo(Object obj, String userId, List<Integer> promotionGoodsIds,
        List<Integer> groupGoodsIds, List<Integer> geckillGoodsIds, Date newDate) {
        BeanMap beanMap = BeanMap.create(obj);
        Map<String, Object> qryMap = new HashMap();
        qryMap.putAll(beanMap);
        // 新品计算时间
        if (qryMap.get("hotType") != null && Short.valueOf(qryMap.get("hotType").toString()).equals(Constant.HOT_TYPE_2)) {
            qryMap.put("newDate", newDate);
        }
        // 不是游客情况，需获取分销商商品可视范围（非全部可视部分）
        if (StringUtils.isNotBlank(userId)) {
            List<Integer> allBrandIds = brandMapper.listByAllDistributor();
            DistributorGoodsControlRpcDTO goodsRange = rpcQryExe.getDistributorGoodsRange(Integer.valueOf(userId));
            List<Integer> brandIds = goodsRange.getBrandIds();
            List<Integer> noBrandIds = goodsRange.getNoBrandIds();
            if (brandIds == null) {
                brandIds = new ArrayList<>();
            }
            if (!CollectionUtils.isEmpty(allBrandIds)) {
                brandIds.addAll(allBrandIds);
            }
            // 剔除不可视品牌
            if (!CollectionUtils.isEmpty(brandIds) && !CollectionUtils.isEmpty(noBrandIds)) {
                brandIds =
                    brandIds.stream().filter(brandId -> !noBrandIds.contains(brandId)).collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(brandIds)) {
                qryMap.put("brandIds", brandIds);
            }
            List<Integer> categoryIds = goodsRange.getCategoryIds();
            List<Integer> noCategoryIds = goodsRange.getNoCategoryIds();
            List<Integer> allCategoryIds = categoryMapper.listByAllDistributor();
            if (categoryIds == null) {
                categoryIds = new ArrayList<>();
            }
            if (!CollectionUtils.isEmpty(allCategoryIds)) {
                categoryIds.addAll(allCategoryIds);
            }
            // 剔除不可视品类
            if (!CollectionUtils.isEmpty(categoryIds) && !CollectionUtils.isEmpty(noCategoryIds)) {
                categoryIds = categoryIds.stream().filter(categoryId -> !noCategoryIds.contains(categoryId))
                    .collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(categoryIds)) {
                qryMap.put("categoryIds", goodsRange.getCategoryIds());
            }
            List<Integer> goodsIds = goodsRange.getGoodsIds();
            List<Integer> noGoodsIds = goodsRange.getNoGoodsIds();
            if (!CollectionUtils.isEmpty(goodsIds)) {
                qryMap.put("goodsIds", goodsIds);
            }
            // 剔除不可视商品
            if (!CollectionUtils.isEmpty(noGoodsIds)) {
                qryMap.put("noGoodsIds", noGoodsIds);
            }
            List<Integer> promotionGroupGeckillGoodsIds = new ArrayList<>();
            // 判断是否只取活动数据
            // if (qryMap.get("hotType") != null && Short.valueOf(qryMap.get("hotType").toString()).equals(HOT_TYPE_1))
            // {
            if (!CollectionUtils.isEmpty(promotionGoodsIds)) {
                promotionGroupGeckillGoodsIds.addAll(promotionGoodsIds);
            }
            if (!CollectionUtils.isEmpty(groupGoodsIds)) {
                promotionGroupGeckillGoodsIds.addAll(groupGoodsIds);
            }
            if (!CollectionUtils.isEmpty(geckillGoodsIds)) {
                promotionGroupGeckillGoodsIds.addAll(geckillGoodsIds);
            }
            if (!CollectionUtils.isEmpty(promotionGroupGeckillGoodsIds)) {
                qryMap.put("promotionGroupGeckillGoodsIds", promotionGroupGeckillGoodsIds);
            }
            // }
            // 判断商品是否有活动
            // StringBuffer promotionGroupGeckillGoodsIdsStr = new StringBuffer();
            // for (int i = 0; i < promotionGroupGeckillGoodsIds.size(); i++) {
            // if (i == 0) {
            // promotionGroupGeckillGoodsIdsStr.append(promotionGroupGeckillGoodsIds.get(i));
            // } else {
            // promotionGroupGeckillGoodsIdsStr.append("," + promotionGroupGeckillGoodsIds.get(i));
            // }
            // }
            // if (ObjectUtils.isNotEmpty(promotionGroupGeckillGoodsIdsStr)) {
            // qryMap.put("activityStr", promotionGroupGeckillGoodsIdsStr.toString());
            // }
        }
        return qryMap;
    }

    /**
     * 获取商品列表查询条件（过滤分销商可视范围）
     *
     * @param obj
     * @param userId
     * @return
     */
    private Map<String, Object> getGoodsListQryMap(Object obj, String userId, List<Integer> promotionGoodsIds,
        List<Integer> groupGoodsIds, List<Integer> geckillGoodsIds, Date newDate) {
        BeanMap beanMap = BeanMap.create(obj);
        Map<String, Object> qryMap = new HashMap();
        qryMap.putAll(beanMap);
        // 新品计算时间(newFlag是用来判断首页展示的五条新品的)
        if ((qryMap.get("hotType") != null && Short.valueOf(qryMap.get("hotType").toString()).equals(Constant.HOT_TYPE_2))
            || (qryMap.get("newFlag") != null && qryMap.get("newFlag").toString().equals("1"))) {
            qryMap.put("newDate", newDate);
        }
        // 不是游客情况，需获取分销商商品可视范围（非全部可视部分）
        if (StringUtils.isNotBlank(userId)) {
            List<Integer> allBrandIds = brandMapper.listByAllDistributor();
            DistributorGoodsControlRpcDTO goodsRange = rpcQryExe.getDistributorGoodsRange(Integer.valueOf(userId));
            List<Integer> brandIds = goodsRange.getBrandIds();
            List<Integer> noBrandIds = goodsRange.getNoBrandIds();
            if (brandIds == null) {
                brandIds = new ArrayList<>();
            }
            if (!CollectionUtils.isEmpty(allBrandIds)) {
                brandIds.addAll(allBrandIds);
            }
            // 剔除不可视品牌
            if (!CollectionUtils.isEmpty(brandIds) && !CollectionUtils.isEmpty(noBrandIds)) {
                brandIds =
                    brandIds.stream().filter(brandId -> !noBrandIds.contains(brandId)).collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(brandIds)) {
                qryMap.put("brandIds", brandIds);
            }
            List<Integer> categoryIds = goodsRange.getCategoryIds();
            List<Integer> noCategoryIds = goodsRange.getNoCategoryIds();
            List<Integer> allCategoryIds = categoryMapper.listByAllDistributor();
            if (categoryIds == null) {
                categoryIds = new ArrayList<>();
            }
            if (!CollectionUtils.isEmpty(allCategoryIds)) {
                categoryIds.addAll(allCategoryIds);
            }
            // 剔除不可视品类
            if (!CollectionUtils.isEmpty(categoryIds) && !CollectionUtils.isEmpty(noCategoryIds)) {
                categoryIds = categoryIds.stream().filter(categoryId -> !noCategoryIds.contains(categoryId))
                    .collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(categoryIds)) {
                qryMap.put("categoryIds", goodsRange.getCategoryIds());
            }
            List<Integer> goodsIds = goodsRange.getGoodsIds();
            List<Integer> noGoodsIds = goodsRange.getNoGoodsIds();
            if (!CollectionUtils.isEmpty(goodsIds)) {
                qryMap.put("goodsIds", goodsIds);
            }
            // 剔除不可视商品
            if (!CollectionUtils.isEmpty(noGoodsIds)) {
                qryMap.put("noGoodsIds", noGoodsIds);
            }
            List<Integer> promotionGroupGeckillGoodsIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(promotionGoodsIds)) {
                promotionGroupGeckillGoodsIds.addAll(promotionGoodsIds);
            }
            if (!CollectionUtils.isEmpty(groupGoodsIds)) {
                promotionGroupGeckillGoodsIds.addAll(groupGoodsIds);
            }
            if (!CollectionUtils.isEmpty(geckillGoodsIds)) {
                promotionGroupGeckillGoodsIds.addAll(geckillGoodsIds);
            }
            if (!CollectionUtils.isEmpty(promotionGroupGeckillGoodsIds)) {
                qryMap.put("promotionGroupGeckillGoodsIds", promotionGroupGeckillGoodsIds);
            }

            // 用户的仓库id
            DistributorBusinessRpcDTO distributorBusinessData =
                rpcQryExe.getDistributorBusinessData(Integer.parseInt(userId));
            List<Integer> salesAreaIds = distributorBusinessData.getSalesAreaIds();
            List<Integer> warehouseIdList = rpcQryExe.listWarehouseIdListByAreaIdList(salesAreaIds);
            // VMI库存放在前面
            warehouseIdList.add(0, 0);
            StringBuilder warehouseIdArr = new StringBuilder();
            for (int i = 0; i < warehouseIdList.size(); i++) {
                warehouseIdArr.append(warehouseIdList.get(i));
                if (i < warehouseIdList.size() - 1) {
                    warehouseIdArr.append("_");
                }
            }
            qryMap.put("warehouseIdArr", warehouseIdArr.toString());

            // 查询用户是否直发、是否在途
            DistributorBusinessRpcDTO distributorBusinessRpcDTO = rpcQryExe.distributorBusiness(userId);
            // 是否直发
            Short autoDelivery = distributorBusinessRpcDTO.getAutoDelivery();
            // 是否在途
            Short onWayFlag = distributorBusinessRpcDTO.getOnWayFlag();

            // if (autoDelivery.equals(AUTODELIVERY_1) && onWayFlag.equals(ONWAYFLAG_1)) {
            // // 支持直发客户在途商品的库存=在途+在库，不支持直发客户的商品=在库
            // qryMap.put("straightHair", 1);
            // } else {
            // if (onWayFlag.equals(ONWAYFLAG_1)) {
            // // 如果是在途（商品库存=在途+在库）
            // qryMap.put("onWayFlag", 1);
            // } else {
            // // 在库 （商品库存=在库）
            // qryMap.put("onWayFlag", 0);
            // }
            // }

            if (autoDelivery.equals(Constant.AUTODELIVERY_1) && onWayFlag.equals(Constant.ONWAYFLAG_1)) {
                // 直发在途
                qryMap.put("straightHairOnTheWay", 1);
            } else if (autoDelivery.equals(Constant.AUTODELIVERY_0) && onWayFlag.equals(Constant.ONWAYFLAG_1)) {
                // 非直发在途
                qryMap.put("nonStraightHairInTransit", 1);
            } else {
                // 直发非直发 的非在途
                qryMap.put("notInTransit", 1);
            }

        }
        return qryMap;
    }

    /**
     * 根据商品ids获取商品价格列表
     * 
     * @param qry
     * @param userId
     * @return
     */
    public List<UserGoodsPriceDTO> priceGoodsList(UserGoodsPriceQry qry, String userId) {
        if (StringUtils.isBlank(userId)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_USER_ID_NULL);
        }
        Integer distributorId = Integer.valueOf(userId);
        List<Integer> goodsIds = qry.getGoodsIds();
        if (CollectionUtils.isEmpty(goodsIds)) {
            throw GoodsException.buildException(ErrorCode.P_GOODS_IDS_NULL);
        }
        // 获取商品品牌品类关系
        List<GoodsBrandCategoryDO> goodsBrandCategoryDOS =
            userMapper.listGoodsBrandCategoryByGoodsIds(qry.getGoodsIds());
        Map<Integer, GoodsBrandCategoryDO> brandCategoryDOMap = goodsBrandCategoryDOS.stream()
            .collect(Collectors.toMap(GoodsBrandCategoryDO::getGoodsId, brandCategoryDO -> brandCategoryDO));
        // 获取分销商价格等级（包括多级分销情况,包含商品特价）
        DistributorScalePriceControlRpcDTO distributorScalePrice =
            rpcQryExe.getDistributorScalePriceByGoodsIds(distributorId, goodsIds);
        // 一级分销商等级价格
        List<OneScalePriceRpcDTO> oneScalePrices = distributorScalePrice.getOneScalePrices();
        if (CollectionUtils.isEmpty(oneScalePrices)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_USER_SCALE_PRICE_NULL);
        }
        // 考虑多级分销情况
        List<Integer> scalePriceIds = new ArrayList<>();
        if (distributorScalePrice.getTreeNode() == 1) {
            scalePriceIds
                .addAll(oneScalePrices.stream().map(OneScalePriceRpcDTO::getScalePriceId).collect(Collectors.toList()));
        } else {
            scalePriceIds.addAll(oneScalePrices.stream().map(OneScalePriceRpcDTO::getDistributionScalePriceId)
                .collect(Collectors.toList()));
        }
        List<GoodsMinMaxPriceDO> minMaxPriceDOS =
            minMaxPriceMapper.listByGoodsIdsAndScalePriceIds(qry.getGoodsIds(), scalePriceIds);
        // 获取分销商活动商品
        List<GoodsPromotionPriceDO> promotionPriceDOS = new ArrayList<>();
        DistributorPromitonGroupSeckillRpcDTO promitonGroupSeckillRpcDTO =
            rpcQryExe.getDistributorPromotionGroupSeckill(distributorId);
        PromotionGroupSeckillIdsByAllDistributorDTO allDistributorDTO =
            rpcQryExe.promotionGroupSeckillIdsByAllDistributor();
        List<Integer> promotionIds = promitonGroupSeckillRpcDTO.getPromotionIds();
        List<Integer> groupSeckilIds = promitonGroupSeckillRpcDTO.getGroupSeckilIds();
        if (allDistributorDTO != null) {
            if (!CollectionUtils.isEmpty(promotionIds)
                && !CollectionUtils.isEmpty(allDistributorDTO.getPromotionIds())) {
                promotionIds.addAll(allDistributorDTO.getPromotionIds());
            } else {
                promotionIds = allDistributorDTO.getPromotionIds();
            }
            if (!CollectionUtils.isEmpty(groupSeckilIds)
                && !CollectionUtils.isEmpty(allDistributorDTO.getGroupSeckillIds())) {
                groupSeckilIds.addAll(allDistributorDTO.getGroupSeckillIds());
            } else {
                groupSeckilIds = allDistributorDTO.getGroupSeckillIds();
            }
        }
        if (!CollectionUtils.isEmpty(promotionIds)) {// 获取促销活动商品
            List<GoodsPromotionPriceDO> priceDOS =
                goodsPromotionPriceMapper.listByGoodsIdsAndPromotionIds(qry.getGoodsIds(), promotionIds);
            promotionPriceDOS.addAll(priceDOS);
        }
        if (!CollectionUtils.isEmpty(groupSeckilIds)) {// 获取拼团秒杀活动商品
            List<GoodsPromotionPriceDO> priceDOS =
                goodsPromotionPriceMapper.listByGoodsIdsAndGroupSeckillIds(qry.getGoodsIds(), groupSeckilIds);
            promotionPriceDOS.addAll(priceDOS);
        }
        List<DistributorSpecialGoodsRpcDTO> specials = distributorScalePrice.getSpecials();
        // 处理商品特价情况
        List<GoodsItemScalePriceDO> goodsItemScalePriceDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(specials)) {
            List<Integer> itemIds =
                specials.stream().map(DistributorSpecialGoodsRpcDTO::getGoodsItemId).collect(Collectors.toList());
            List<GoodsItemScalePriceDO> list =
                goodsMapper.listGoodsItemScalePriceByGoodsItemIdsAndGoodsItemGradeId(itemIds, scalePriceIds);
            if (!CollectionUtils.isEmpty(list)) {
                goodsItemScalePriceDOS.addAll(list);
            }
        }
        // 获取商品价格(一级分销商价格或一级默认分销等级价格)
        List<UserGoodsPriceDTO> dtos = UserConvertor.toUserGoodsPriceDTOList(distributorScalePrice, brandCategoryDOMap,
            qry.getGoodsIds(), oneScalePrices, minMaxPriceDOS, promotionPriceDOS, goodsItemScalePriceDOS);
        // 计算多级分销价格等级
        goodsNextArithmetic(dtos, distributorScalePrice, brandCategoryDOMap);
        return dtos;
    }

    /**
     * 计算多级分销价格等级
     * 
     * @param dtos
     * @param distributorScalePrice
     * @param brandCategoryDOMap
     */
    private void goodsNextArithmetic(List<UserGoodsPriceDTO> dtos,
        DistributorScalePriceControlRpcDTO distributorScalePrice,
        Map<Integer, GoodsBrandCategoryDO> brandCategoryDOMap) {
        List<NextScalePriceRpcDTO> nextScalePrices = distributorScalePrice.getNextScalePrices();
        if (!CollectionUtils.isEmpty(nextScalePrices)) {
            dtos.forEach(dto -> {
                List<Short> arithmeticTypes = new ArrayList<>();
                List<BigDecimal> arithmeticNums = new ArrayList<>();
                GoodsBrandCategoryDO goodsBrandCategoryDO = brandCategoryDOMap.get(dto.getGoodsId());
                getNextArithmetic(arithmeticTypes, arithmeticNums, nextScalePrices, goodsBrandCategoryDO);
                // 计算多级分销商品价格
                if (!CollectionUtils.isEmpty(arithmeticTypes) && dto.getMaxPrice() != null) {
                    arithmeticTypes.forEach(arithmeticType -> {
                        BigDecimal arithmeticNum = arithmeticNums.get(arithmeticTypes.indexOf(arithmeticType));
                        if (arithmeticType.equals(Constant.ARITHMETIC_TYPE_1)) {
                            if (dto.getMinPrice() != null) {
                                dto.setMinPrice(dto.getMinPrice().multiply(arithmeticNum));
                            }
                            if (dto.getMaxPrice() != null) {
                                dto.setMaxPrice(dto.getMaxPrice().multiply(arithmeticNum));
                            }
                            if (dto.getPromotionMinPrice() != null) {
                                dto.setPromotionMinPrice(dto.getPromotionMinPrice().multiply(arithmeticNum));
                            }
                            if (dto.getPromotionMaxPrice() != null) {
                                dto.setPromotionMaxPrice(dto.getPromotionMaxPrice().multiply(arithmeticNum));
                            }
                        } else if (arithmeticType.equals(Constant.ARITHMETIC_TYPE_2)) {
                            if (dto.getMinPrice() != null) {
                                dto.setMinPrice(dto.getMinPrice().add(arithmeticNum));
                            }
                            if (dto.getMaxPrice() != null) {
                                dto.setMaxPrice(dto.getMaxPrice().add(arithmeticNum));
                            }
                            if (dto.getPromotionMinPrice() != null) {
                                dto.setPromotionMinPrice(dto.getPromotionMinPrice().add(arithmeticNum));
                            }
                            if (dto.getPromotionMaxPrice() != null) {
                                dto.setPromotionMaxPrice(dto.getPromotionMaxPrice().add(arithmeticNum));
                            }
                        } else if (arithmeticType.equals(Constant.ARITHMETIC_TYPE_3)) {
                            if (dto.getMinPrice() != null) {
                                dto.setMinPrice(dto.getMinPrice().divide(arithmeticNum));
                            }
                            if (dto.getMaxPrice() != null) {
                                dto.setMaxPrice(dto.getMaxPrice().divide(arithmeticNum));
                            }
                            if (dto.getPromotionMinPrice() != null) {
                                dto.setPromotionMinPrice(dto.getPromotionMinPrice().divide(arithmeticNum));
                            }
                            if (dto.getPromotionMaxPrice() != null) {
                                dto.setPromotionMaxPrice(dto.getPromotionMaxPrice().divide(arithmeticNum));
                            }
                        } else if (arithmeticType.equals(Constant.ARITHMETIC_TYPE_4)) {
                            if (dto.getMinPrice() != null) {
                                dto.setMinPrice(dto.getMinPrice().subtract(arithmeticNum));
                            }
                            if (dto.getMaxPrice() != null) {
                                dto.setMaxPrice(dto.getMaxPrice().subtract(arithmeticNum));
                            }
                            if (dto.getPromotionMinPrice() != null) {
                                dto.setPromotionMinPrice(dto.getPromotionMinPrice().subtract(arithmeticNum));
                            }
                            if (dto.getPromotionMaxPrice() != null) {
                                dto.setPromotionMaxPrice(dto.getPromotionMaxPrice().subtract(arithmeticNum));
                            }
                        }
                        if (dto.getMinPrice() != null) {
                            dto.setMinPrice(dto.getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        if (dto.getMaxPrice() != null) {
                            dto.setMaxPrice(dto.getMaxPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        if (dto.getPromotionMinPrice() != null) {
                            dto.setPromotionMinPrice(dto.getPromotionMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        if (dto.getPromotionMaxPrice() != null) {
                            dto.setPromotionMaxPrice(dto.getPromotionMaxPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    });
                }
            });
        }
    }

    /**
     * 获取多级分销商计算公式
     * 
     * @return
     */
    private void getNextArithmetic(List<Short> arithmeticTypes, List<BigDecimal> arithmeticNums,
        List<NextScalePriceRpcDTO> nextScalePrices, GoodsBrandCategoryDO goodsBrandCategoryDO) {
        nextScalePrices.forEach(nextScalePrice -> {
            List<NextScalePriceSpecialRpcDTO> nextScalePriceSpecials = nextScalePrice.getNextScalePriceSpecials();
            // 有特殊公式情况
            Optional<NextScalePriceSpecialRpcDTO> specialOptional = null;
            if (!CollectionUtils.isEmpty(nextScalePriceSpecials)) {
                if (goodsBrandCategoryDO.getCategoryId() != null) {
                    specialOptional = nextScalePriceSpecials.stream()
                        .filter(nextScalePriceSpecial -> nextScalePriceSpecial.getBrandId() != null
                            && nextScalePriceSpecial.getBrandId().equals(goodsBrandCategoryDO.getBrandId())
                            && nextScalePriceSpecial.getCategoryId() != null
                            && nextScalePriceSpecial.getCategoryId().equals(goodsBrandCategoryDO.getCategoryId()))
                        .findFirst();
                }
                if (specialOptional == null || !specialOptional.isPresent()) {
                    specialOptional = nextScalePriceSpecials.stream()
                        .filter(nextScalePriceSpecial -> nextScalePriceSpecial.getBrandId() != null
                            && nextScalePriceSpecial.getBrandId().equals(goodsBrandCategoryDO.getBrandId())
                            && (nextScalePriceSpecial.getCategoryId() == null
                                || nextScalePriceSpecial.getCategoryId() == 0))
                        .findFirst();
                }
            }
            if (specialOptional != null && specialOptional.isPresent()) {
                NextScalePriceSpecialRpcDTO specialRpcDTO = specialOptional.get();
                if (specialRpcDTO != null && specialRpcDTO.getArithmeticType() != null) {
                    arithmeticTypes.add(specialOptional.get().getArithmeticType());
                    arithmeticNums.add(specialOptional.get().getArithmeticNum());
                }
            } else if (nextScalePrice.getArithmeticType() != null) {
                arithmeticTypes.add(nextScalePrice.getArithmeticType());
                arithmeticNums.add(nextScalePrice.getArithmeticNum());
            }
        });
    }

    /**
     * 根据货品ids获取货品价格列表
     * 
     * @param qry
     * @param userId
     * @return
     */
    public List<UserGoodsItemPriceDTO> priceGoodsItemList(UserGoodsItemPriceQry qry, String userId) {
        // userId = "1000";
        if (StringUtils.isBlank(userId)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_USER_ID_NULL);
        }
        Integer distributorId = Integer.valueOf(userId);
        return priceGoodsItemList(qry.getGoodsItemIds(), distributorId, Constant.RETAIL_PRICE_FLAG_1);
    }

    /**
     * 根据货品ids和分销商id获取货品价格列表
     * 
     * @param goodsItems
     * @param distributorId
     * @return
     */
    public List<UserGoodsItemPriceDTO> priceGoodsItemList(List<UserGoodsItemId> goodsItems, Integer distributorId,
                                                          Short retailPriceFlag) {
        List<Integer> goodsIds = goodsItems.stream().map(UserGoodsItemId::getGoodsId).collect(Collectors.toList());
        List<Integer> itemIds = goodsItems.stream().map(UserGoodsItemId::getItemId).collect(Collectors.toList());
        // 获取商品品牌品类关系
        List<GoodsBrandCategoryDO> goodsBrandCategoryDOS = userMapper.listGoodsBrandCategoryByGoodsIds(goodsIds);
        Map<Integer, GoodsBrandCategoryDO> brandCategoryDOMap = goodsBrandCategoryDOS.stream()
            .collect(Collectors.toMap(GoodsBrandCategoryDO::getGoodsId, brandCategoryDO -> brandCategoryDO));
        // 获取分销商价格等级和特价（包括多级分销情况）
        DistributorScalePriceControlRpcDTO distributorScalePrice =
            rpcQryExe.getDistributorScalePrice(distributorId, itemIds);
        List<OneScalePriceRpcDTO> oneScalePrices = distributorScalePrice.getOneScalePrices();
        if (CollectionUtils.isEmpty(oneScalePrices)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_USER_SCALE_PRICE_NULL);
        }
        // 考虑多级分销情况
        List<Integer> scalePriceIds = new ArrayList<>();
        if (distributorScalePrice.getTreeNode() == 1) {
            scalePriceIds
                .addAll(oneScalePrices.stream().map(OneScalePriceRpcDTO::getScalePriceId).collect(Collectors.toList()));
        } else {
            scalePriceIds.addAll(oneScalePrices.stream().map(OneScalePriceRpcDTO::getDistributionScalePriceId)
                .collect(Collectors.toList()));
        }
        // 获取货品等级价格
        List<GoodsItemScalePriceDO> scalePriceDOS =
            userMapper.listGoodsItemScalePriceByItemIdsAndScalePriceIds(itemIds, scalePriceIds);
        // 获取货品零售价
        List<GoodsItemScalePriceDO> retailPriceDOS = new ArrayList<>();
        if (retailPriceFlag != null && retailPriceFlag.equals(Constant.RETAIL_PRICE_FLAG_1)) {
            retailPriceDOS.addAll(userMapper.listGoodsItemRetailPriceByItemIds(itemIds, Constant.RETAIL_FLAG_1));
        }
        // 获取货品价格(一级分销商价格)
        List<UserGoodsItemPriceDTO> dtos = UserConvertor.toUserGoodsItemPriceDTOList(distributorScalePrice, goodsItems,
            brandCategoryDOMap, scalePriceDOS, retailPriceDOS, oneScalePrices, distributorScalePrice.getSpecials());
        // 获取多级分销商价格等级
        goodsItemNextArithmetic(dtos, distributorScalePrice, brandCategoryDOMap);
        return dtos;
    }

    /**
     * 计算多级分销价格等级
     *
     * @param dtos
     * @param distributorScalePrice
     * @param brandCategoryDOMap
     */
    private void goodsItemNextArithmetic(List<UserGoodsItemPriceDTO> dtos,
        DistributorScalePriceControlRpcDTO distributorScalePrice,
        Map<Integer, GoodsBrandCategoryDO> brandCategoryDOMap) {
        List<NextScalePriceRpcDTO> nextScalePrices = distributorScalePrice.getNextScalePrices();
        if (!CollectionUtils.isEmpty(nextScalePrices)) {
            dtos.forEach(dto -> {
                List<Short> arithmeticTypes = new ArrayList<>();
                List<BigDecimal> arithmeticNums = new ArrayList<>();
                GoodsBrandCategoryDO goodsBrandCategoryDO = brandCategoryDOMap.get(dto.getGoodsId());
                getNextArithmetic(arithmeticTypes, arithmeticNums, nextScalePrices, goodsBrandCategoryDO);
                // 计算多级分销货品价格
                if (!CollectionUtils.isEmpty(arithmeticTypes) && dto.getSalePrice() != null) {
                    arithmeticTypes.forEach(arithmeticType -> {
                        BigDecimal arithmeticNum = arithmeticNums.get(arithmeticTypes.indexOf(arithmeticType));
                        if (arithmeticType.equals(Constant.ARITHMETIC_TYPE_1)) {
                            dto.setSalePrice(dto.getSalePrice().multiply(arithmeticNum));
                        } else if (arithmeticType.equals(Constant.ARITHMETIC_TYPE_2)) {
                            dto.setSalePrice(dto.getSalePrice().add(arithmeticNum));
                        } else if (arithmeticType.equals(Constant.ARITHMETIC_TYPE_3)) {
                            dto.setSalePrice(dto.getSalePrice().divide(arithmeticNum));
                        } else if (arithmeticType.equals(Constant.ARITHMETIC_TYPE_4)) {
                            dto.setSalePrice(dto.getSalePrice().subtract(arithmeticNum));
                        }
                        dto.setSalePrice(dto.getSalePrice().setScale(2, BigDecimal.ROUND_HALF_UP));
                    });
                }
            });
        }
    }

    /**
     * 商品列表查询(包含个性定制和新品商品筛选)
     * 
     * @param qry
     * @param userId
     * @return
     */
    public PageInfo<UserGoodsListDTO> goodsList(UserGoodsListQry qry, String userId) {
        // userId = "1000";
        if (qry.getCollectionFlag() != null && StringUtils.isBlank(userId)) {
            throw GoodsException.buildException(ErrorCode.B_USER_LOGIN_ERROR);
        }
        // 组建判断是不是新品的时间
        Date newDate = new Date(System.currentTimeMillis() - (long)rpcQryExe.getNewproductTime() * 24 * 60 * 60 * 1000);
        // 获取该用户的获取活动促销拼团秒杀商品
        PromotionGroupSeckillGoodsRpcDTO promotionGroupSeckillGoods = getPromotionGroupSeckillGoods(userId);

        Map<String, Object> qryMap = getGoodsListQryMap(qry, userId, promotionGroupSeckillGoods.getPromotionGoodsIds(),
            promotionGroupSeckillGoods.getGroupGoodsIds(), promotionGroupSeckillGoods.getSeckillGoodsIds(), newDate);
        if (qry.getCollectionFlag() != null && qry.getCollectionFlag().equals(Constant.COLLECTION_FLAG_1)) {
            qryMap.put("distributorId", Integer.valueOf(userId));
        }
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<UserGoodsListDO> goodsListDOS = null;
        if (qry.getClassifyId() == null) {
            if (StringUtils.isNotBlank(userId)) {
                goodsListDOS = userMapper.listUserGoodsList(qryMap);
            } else {
                goodsListDOS = userMapper.listGoodsList(qryMap);
            }
        } else {
            if (StringUtils.isNotBlank(userId)) {
                goodsListDOS = userMapper.listUserGoodsListByClassify(qryMap);
            } else {
                goodsListDOS = userMapper.listGoodsListByClassify(qryMap);
            }
        }

        PageInfo pageInfo = new PageInfo(goodsListDOS);
        List<UserGoodsListDTO> userGoodsListDTOS =
            UserConvertor.toUserGoodsListDTOList(pageInfo.getList(), qry.getCollectionFlag());
        checkCollectionGoods(userGoodsListDTOS, userId, qry.getCollectionFlag(), newDate, promotionGroupSeckillGoods);
        getGoodsTags(userGoodsListDTOS);
        pageInfo.setList(userGoodsListDTOS);
        return pageInfo;
    }

    /**
     * 获取促销拼团秒杀商品
     * 
     * @param userId
     * @return
     */
    private PromotionGroupSeckillGoodsRpcDTO getPromotionGroupSeckillGoods(String userId) {
        PromotionGroupSeckillGoodsRpcDTO promotionGroupSeckillGoods = new PromotionGroupSeckillGoodsRpcDTO();
        if (StringUtils.isNotBlank(userId)) {
            // 根据分销商id获取顶层分销商的推广活动与拼团秒杀活动的id
            DistributorPromitonGroupSeckillRpcDTO promotionGroupSeckill =
                rpcQryExe.getDistributorPromotionGroupSeckill(Integer.valueOf(userId));
            // 根据推广活动与拼团秒杀活动的信息去查询
            promotionGroupSeckillGoods = rpcQryExe.getPromotionGroupSeckillGoods(
                promotionGroupSeckill.getPromotionIds(), promotionGroupSeckill.getGroupSeckilIds());
        }
        return promotionGroupSeckillGoods;
    }

    /**
     * 获取商品标签
     * 
     * @param userGoodsListDTOS
     */
    private void getGoodsTags(List<UserGoodsListDTO> userGoodsListDTOS) {
        if (!CollectionUtils.isEmpty(userGoodsListDTOS)) {
            List<Integer> goodsIds =
                userGoodsListDTOS.stream().map(UserGoodsListDTO::getId).collect(Collectors.toList());
            List<GoodsTagDO> goodsTagDOS = goodsMapper.listGoodsTagByGoodsIds(goodsIds);
            if (!CollectionUtils.isEmpty(goodsTagDOS)) {
                UserConvertor.toUserGoodsListTagDTOList(userGoodsListDTOS, goodsTagDOS);
            }
        }
    }

    /**
     * 组装商品列表数据
     * 
     * @param userGoodsListDTOS
     * @param userId
     * @param collectionFlag
     * @param newDate
     * @param promotionGroupSeckillGoods
     */
    private void checkCollectionGoods(List<UserGoodsListDTO> userGoodsListDTOS, String userId, Short collectionFlag,
        Date newDate, PromotionGroupSeckillGoodsRpcDTO promotionGroupSeckillGoods) {
        List<Integer> collectionIds = new ArrayList<>();
        if ((collectionFlag == null || (collectionFlag != null && collectionFlag.equals(Constant.COLLECTION_FLAG_0)))
            && StringUtils.isNotBlank(userId) && !CollectionUtils.isEmpty(userGoodsListDTOS)) {
            List<Integer> goodsIds =
                userGoodsListDTOS.stream().map(UserGoodsListDTO::getId).collect(Collectors.toList());
            collectionIds.addAll(collectionMapper.listByGoodsIdsAndDistributorId(goodsIds, Integer.valueOf(userId)));
        }
        List<Integer> promotionGoodsIds = promotionGroupSeckillGoods.getPromotionGoodsIds();
        List<Integer> promotionStepGoodsIds = promotionGroupSeckillGoods.getPromotionStepGoodsIds();
        List<Integer> groupGoodsIds = promotionGroupSeckillGoods.getGroupGoodsIds();
        List<Integer> seckillGoodsIds = promotionGroupSeckillGoods.getSeckillGoodsIds();
        userGoodsListDTOS.forEach(dto -> {
            if (collectionIds.contains(dto.getId())) {// 是否收藏
                dto.setCollectionFlag(Constant.COLLECTION_FLAG_1);
            }
            if (dto.getSaleTime() != null && dto.getSaleTime().getTime() >= newDate.getTime()) {// 是否新品
                dto.setNewFlag(Constant.NEW_FLAG_1);
            }
            if (!CollectionUtils.isEmpty(promotionGoodsIds) && promotionGoodsIds.contains(dto.getId())) {// 是否促销活动(普通)
                dto.setPromotionFlag(Constant.PROMOTION_FLAG_1);
                dto.setPromoType(Constant.PROMO_TYPE_1);
            }
            if (!CollectionUtils.isEmpty(promotionStepGoodsIds) && promotionStepGoodsIds.contains(dto.getId())) {// 是否促销活动(阶梯)
                dto.setPromotionFlag(Constant.PROMOTION_FLAG_1);
                dto.setPromoType(Constant.PROMO_TYPE_2);
            }
            if (!CollectionUtils.isEmpty(groupGoodsIds) && groupGoodsIds.contains(dto.getId())) {// 是否拼团活动
                dto.setGroupFlag(Constant.GROUP_FLAG_1);
            }
            if (!CollectionUtils.isEmpty(seckillGoodsIds) && seckillGoodsIds.contains(dto.getId())) {// 是否秒杀活动
                dto.setSeckillFlag(Constant.SECKILL_FLAG_1);
            }
        });

    }

    /**
     * 根据栏目id获取商品分类列表
     * 
     * @param qry
     * @return
     */
    public List<UserClassifyDTO> columnClassifyList(UserClassifyListColumnQry qry) {
        List<ClassifyDO> classifyDOS = userMapper.listClassifyByColumnId(qry.getColumnId());
        return UserConvertor.toUserClassifyDTOList(classifyDOS);
    }

    /**
     * 根据板块id获取商品分类列表
     * 
     * @param qry
     * @return
     */
    public List<UserClassifyDTO> sectionClassifyList(UserClassifyListSectionQry qry) {
        List<ClassifyDO> classifyDOS = userMapper.listClassifyBySectionId(qry.getSectionId());
        return UserConvertor.toUserClassifyDTOList(classifyDOS);
    }

    /**
     * 根据移动端配置id获取商品分类列表
     * 
     * @param qry
     * @return
     */
    public List<UserClassifyDTO> mobileClassifyList(UserClassifyListMobileQry qry) {
        List<ClassifyDO> classifyDOS = userMapper.listClassifyByMobileId(qry.getMobileId());
        return UserConvertor.toUserClassifyDTOList(classifyDOS);
    }

    /**
     * 根据商品id获取商品详情
     * 
     * @param qry
     * @return
     */
    public UserGoodsDTO goods(UserGoodsId qry) {
        // 商品基本信息
        UserGoodsDO userGoodsDO = userMapper.selectUserGoodsByGoodsId(qry.getId());
        if (userGoodsDO == null) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_USER_GOODS_NULL);
        }
        UserGoodsDTO goodsDTO = UserConvertor.toUserGoodsDTO(userGoodsDO);
        // 商品标签信息
        List<GoodsTagDO> userTagDOS = userMapper.listUserTagByGoodsId(qry.getId());
        List<UserTagDTO> tagDTOS = UserConvertor.toUserTagDTOList(userTagDOS);
        goodsDTO.setTags(tagDTOS);
        // 商品参数信息
        List<GoodsParamDO> userParamDOS = userMapper.listUserParamByGoodsId(qry.getId());
        List<UserParamDTO> paramDTOS = UserConvertor.toUserParamDTOList(userParamDOS);
        goodsDTO.setParams(paramDTOS);
        // 货品（SKU）信息
        List<UserGoodsItemDO> userGoodsItemDOS =
            userMapper.listUserGoodsItemByGoodsIdAndItemId(qry.getId(), qry.getItemId());
        if (!CollectionUtils.isEmpty(userGoodsItemDOS)) {
            List<Integer> goodsItemIds =
                userGoodsItemDOS.stream().map(UserGoodsItemDO::getId).collect(Collectors.toList());
            // 货品装箱信息
            List<GoodsItemBoxDO> goodsItemBoxDOS = goodsMapper.listGoodsItemBoxByGoodsItemIds(goodsItemIds);
            // 货品规格颜色
            List<GoodsItemSpecsColorListDO> specsColorListDOS =
                goodsMapper.listGoodsItemSpecsColorListByGoodsItemIds(goodsItemIds);
            List<UserGoodsItemDTO> goodsItemDTOS =
                UserConvertor.toUserGoodsItemDTO(userGoodsItemDOS, goodsItemBoxDOS, specsColorListDOS);
            goodsDTO.setGoodsItems(goodsItemDTOS);
        }
        return goodsDTO;
    }

    /**
     * 根据商品id查询商品收藏状态
     * 
     * @param qry
     * @param userId
     * @return
     */
    public Boolean getGoodsCollection(UserGoodsId qry, String userId) {
        if (StringUtils.isBlank(userId)) {
            throw GoodsException.buildException(ErrorCode.B_USER_LOGIN_ERROR);
        }
        Integer goodsId = collectionMapper.listByGoodsIdAndDistributorId(qry.getId(), Integer.valueOf(userId));
        if (goodsId != null) {
            return true;
        }
        return false;
    }

    public List<TagsDTO> getGoodsLabelByGoodsIds(BaseIds qry) {
        List<TagsDTO> list = new ArrayList<>();
        if (qry.getIds().size() == 0) {
            return list;
        }
        List<GoodsTagDO> tagDOS = tagMapper.listByGoodsIds(qry.getIds());
        Map<Integer, List<GoodsTagDO>> group = tagDOS.stream().collect(Collectors.groupingBy(GoodsTagDO::getGoodsId));
        for (Map.Entry<Integer, List<GoodsTagDO>> entryUser : group.entrySet()) {
            Integer key = entryUser.getKey();
            List<GoodsTagDO> goodsTagDOS = entryUser.getValue();
            TagsDTO tagDTO = new TagsDTO();
            tagDTO.setGoodsId(key);
            List<TagsDTO.Tag> tagList = new ArrayList<>();
            for (GoodsTagDO tagDO : goodsTagDOS) {
                TagsDTO.Tag tag = new TagsDTO.Tag();
                BeanUtils.copyProperties(tagDO, tag);
                tagList.add(tag);
            }
            tagDTO.setTags(tagList);
            list.add(tagDTO);
        }
        return list;
    }
}
