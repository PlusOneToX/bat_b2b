package com.bat.goods.service.goods.executor;

import static com.bat.goods.service.goods.executor.ErrorCode.B_GOODS_NUll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.goods.api.goods.dto.*;
import com.bat.goods.api.goods.dto.data.*;
import com.bat.goods.dao.goods.GoodsDistributorGroupRelevanceMapper;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.service.common.CommonRpcQryExe;
import com.bat.goods.service.common.Constant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorGroupRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorNameRpcDTO;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.material.api.MaterialServiceRpc;
import com.bat.dubboapi.flexible.material.dto.MaterialDTORpcQry;
import com.bat.goods.api.base.BaseIds;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.goods.dto.*;
import com.bat.goods.api.goods.dto.data.*;
import com.bat.goods.dao.goods.GoodsMapper;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.service.goods.convertor.GoodsConvertor;

@Component
public class GoodsQryExe {

    @Resource
    private GoodsMapper mapper;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private MaterialServiceRpc materialServiceRpc;

    @Resource
    private CommonRpcQryExe commonRpcQryExe;

    @Resource
    private GoodsDistributorGroupRelevanceMapper goodsDistributorGroupRelevanceMapper;

    /**
     * 查询商品列表
     *
     * @param qry
     * @return
     */
    public PageInfo<GoodsListDTO> executeGoodsList(GoodsListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<GoodsListDO> goodsListDOS = mapper.listGoods(qryMap);
        PageInfo pageInfo = new PageInfo(goodsListDOS);
        List<GoodsListDTO> goodsListDTOS = GoodsConvertor.toGoodsListDTOList(pageInfo.getList());
        pageInfo.setList(goodsListDTOS);
        // 组装商品分类
        if (!CollectionUtils.isEmpty(goodsListDOS)) {
            List<Integer> goodsIds = new ArrayList<>();
            goodsListDTOS.forEach(goodsListDTO -> {
                goodsIds.add(goodsListDTO.getId());
            });
            List<GoodsClassifyRelevanceDO> classifyRelevanceDOS =
                mapper.listGoodsClassifyRelevanceIdBygoodsIds(goodsIds);
            if (!CollectionUtils.isEmpty(classifyRelevanceDOS)) {
                Map<Integer, GoodsListDTO> goodsListDTOMap =
                    goodsListDTOS.stream().collect(Collectors.toMap(GoodsListDTO::getId, goodsListDTO -> goodsListDTO));
                classifyRelevanceDOS.forEach(classifyRelevanceDO -> {
                    GoodsListDTO goodsListDTO = goodsListDTOMap.get(classifyRelevanceDO.getGoodsId());
                    List<Integer> classifyIds = goodsListDTO.getClassifyIds();
                    if (classifyIds == null) {
                        classifyIds = new ArrayList<>();
                        goodsListDTO.setClassifyIds(classifyIds);
                    }
                    classifyIds.add(classifyRelevanceDO.getClassifyId());
                });
            }
        }
        return pageInfo;
    }

    /**
     * 查询货品(SKU)列表
     *
     * @param qry
     * @return
     */
    public PageInfo<GoodsItemListDTO> executeGoodsItemList(GoodsItemListQry qry) {
        List<MaterialDTORpcQry> materialDTORpcQryList = null;
        if (Constant.GOODS_TYPE_CUSTOM.equals(qry.getGoodsType()) && qry.getRelevanceMaterialFlag() != null
            && qry.getRelevanceMaterialFlag().equals((short)1)) {
            // 定制加材质名称
            Response<List<MaterialDTORpcQry>> materialResponse = materialServiceRpc.listAllGroupByItemId();
            materialDTORpcQryList = materialResponse.getData();
            List<Integer> itemIdList =
                materialDTORpcQryList.stream().map(MaterialDTORpcQry::getItemId).collect(Collectors.toList());
            // 指定货品id列表
            qry.setItemIdList(itemIdList);
        }
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<GoodsItemListDO> goodsItemListDOS = mapper.listGoodsItem(qryMap);
        List<GoodsItemSpecsColorListDO> specsColorListDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsItemListDOS)) {
            List<Integer> itemIds = goodsItemListDOS.stream().map(GoodsItemListDO::getId).collect(Collectors.toList());
            specsColorListDOS = mapper.listGoodsItemSpecsColorListByGoodsItemIds(itemIds);
        }
        PageInfo pageInfo = new PageInfo(goodsItemListDOS);
        List<GoodsItemListDTO> goodsItemListDTOS =
            GoodsConvertor.toGoodsItemListDTOList(pageInfo.getList(), specsColorListDOS, materialDTORpcQryList);
        pageInfo.setList(goodsItemListDTOS);
        return pageInfo;
    }

    /**
     * 商品ID查询商品详情
     *
     * @param qry
     * @return
     */
    public GoodsDTO executeGoods(GoodsId qry) {
        GoodsDO goodsDO = mapper.getGoodsById(qry.getId());
        if (goodsDO == null) {
            throw GoodsException.buildException(B_GOODS_NUll);
        }
        GoodsDTO goodsDTO = GoodsConvertor.toGoodsDTO(goodsDO);
        getGoodsRelevance(goodsDTO);
        getGoodsScopeIds(goodsDTO);
        getGoodsScopeNoIds(goodsDTO);
        getGoodsItems(goodsDTO);
        return goodsDTO;
    }

    /**
     * 根据商品获取商品里的货品（SKU）信息
     *
     * @param goodsDTO
     */
    private void getGoodsItems(GoodsDTO goodsDTO) {
        List<GoodsItemDO> goodsItemDOS = mapper.listGoodsItemByGoodsId(goodsDTO.getId());
        List<GoodsItemDTO> goodsItemDTOS = GoodsConvertor.toGoodsItemDTOList(goodsItemDOS);
        List<Integer> goodsItems = goodsItemDTOS.stream().map(GoodsItemDTO::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsItems)) {
            List<GoodsItemBoxDO> goodsItemBoxDOS = mapper.listGoodsItemBoxByGoodsItemIds(goodsItems);
            List<GoodsItemScalePriceDO> scalePriceDOS = mapper.listGoodsItemScalePriceByGoodsItemIds(goodsItems);
            List<GoodsItemSpecsColorListDO> specsColorDOS =
                mapper.listGoodsItemSpecsColorListByGoodsItemIds(goodsItems);
            goodsItemDTOS.forEach(goodsItemDTO -> {
                List<GoodsItemBoxDTO> boxDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(goodsItemBoxDOS)) {
                    goodsItemBoxDOS.forEach(goodsItemBoxDO -> {
                        if (goodsItemBoxDO.getGoodsItemId().equals(goodsItemDTO.getId())) {
                            boxDTOS.add(GoodsConvertor.toGoodsItemBoxDTO(goodsItemBoxDO));
                        }
                    });
                }
                goodsItemDTO.setBoxs(boxDTOS);
                List<GoodsItemScalePriceDTO> scalePriceDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(scalePriceDOS)) {
                    scalePriceDOS.forEach(scalePriceDO -> {
                        if (scalePriceDO.getGoodsItemId().equals(goodsItemDTO.getId())) {
                            scalePriceDTOS.add(GoodsConvertor.toGoodsItemScalePriceDTO(scalePriceDO));
                        }
                    });
                }
                goodsItemDTO.setScalePrices(scalePriceDTOS);
                if (!CollectionUtils.isEmpty(specsColorDOS)) {
                    specsColorDOS.forEach(specsColorDO -> {
                        if (specsColorDO.getGoodsItemId().equals(goodsItemDTO.getId())
                            && specsColorDO.getAttributeType().equals(Constant.ATTRIBUTE_TYPE_1)) {
                            goodsItemDTO.setSpecsId(specsColorDO.getAttributeValueId());
                            goodsItemDTO.setSpecsName(specsColorDO.getAttributeValueName());
                        } else if (specsColorDO.getGoodsItemId().equals(goodsItemDTO.getId())
                            && specsColorDO.getAttributeType().equals(Constant.ATTRIBUTE_TYPE_2)) {
                            goodsItemDTO.setColorId(specsColorDO.getAttributeValueId());
                            goodsItemDTO.setColorName(specsColorDO.getAttributeValueName());
                        }
                    });
                }
            });
        }
        goodsDTO.setGoodsItems(goodsItemDTOS);
    }

    /**
     * 获取商品关联关系（品牌、品类、商品属性、商品标签等信息）
     *
     * @param goodsDTO
     */
    private void getGoodsRelevance(GoodsDTO goodsDTO) {
        Integer goodsId = goodsDTO.getId();
        goodsDTO.setClassifyIds(mapper.listGoodsClassifyRelevanceId(goodsId));
        goodsDTO.setTagIds(GoodsConvertor.toGoodsTagDTO(mapper.listGoodsTagByGoodsId(goodsId)));
        goodsDTO.setParamIds(GoodsConvertor.toGoodsParamDTO(mapper.listGoodsParamByGoodsId(goodsId)));
    }

    /**
     * 获取商品可视关联关系
     *
     * @param goodsDTO
     */
    public void getGoodsScopeIds(GoodsDTO goodsDTO) {
        Integer goodsId = goodsDTO.getId();
        if (goodsDTO.getDistributorScope() == null) {
            return;
        }
        if (goodsDTO.getDistributorScope().equals(Constant.SCOPE_SCALE_PRICE)) {
            goodsDTO.setScalePriceIds(mapper.listGoodsScalePriceRelevanceId(goodsId));
        } else if (goodsDTO.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR)) {
            List<Integer> distributorIds = mapper.listGoodsDistributorRelevanceId(goodsId);
            List<DistributorNameRpcDTO> distributorNameRpcDTOS =
                commonRpcQryExe.getDistributorNameByDistributorIds(distributorIds);
            goodsDTO
                .setDistributors(GoodsConvertor.toGoodsDistributorScopeDTOList(distributorIds, distributorNameRpcDTOS));
        } else if (goodsDTO.getDistributorScope().equals(Constant.SCOPE_DEPARTMENT)) {
            goodsDTO.setDepartmentIds(mapper.listGoodsDepartmentRelevanceId(goodsId));
        } else if (goodsDTO.getDistributorScope().equals(Constant.SCOPE_ADMIN)) {
            goodsDTO.setAdminIds(mapper.listGoodsAdminRelevanceId(goodsId));
        } else if (goodsDTO.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR_GROUP)) {
            List<Integer> distributorGroupIds =
                goodsDistributorGroupRelevanceMapper.listDistributorGroupIdByGoodsId(goodsId);
            List<DistributorGroupRpcDTO> groupRpcDTOS =
                commonRpcQryExe.getDistributorGroupByDistributorGroupIds(distributorGroupIds);
            goodsDTO
                .setDistributorGroups(GoodsConvertor.toGoodsDistributorGroupDTOList(distributorGroupIds, groupRpcDTOS));
        }
    }

    /**
     * 获取商品不可视关联关系
     *
     * @param goodsDTO
     */
    public void getGoodsScopeNoIds(GoodsDTO goodsDTO) {
        Integer goodsId = goodsDTO.getId();
        if (goodsDTO.getDistributorScopeNo() == null) {
            return;
        }
        if (goodsDTO.getDistributorScopeNo().equals(Constant.SCOPE_SCALE_PRICE)) {
            goodsDTO.setScalePriceNoIds(mapper.listGoodsScalePriceRelevanceIdNo(goodsId));
        } else if (goodsDTO.getDistributorScopeNo().equals(Constant.SCOPE_DISTRIBUTOR)) {
            List<Integer> distributorNoIds = mapper.listGoodsDistributorRelevanceIdNo(goodsId);
            List<DistributorNameRpcDTO> distributorNameRpcDTOS =
                commonRpcQryExe.getDistributorNameByDistributorIds(distributorNoIds);
            goodsDTO.setDistributorNos(
                GoodsConvertor.toGoodsDistributorScopeDTOList(distributorNoIds, distributorNameRpcDTOS));
        } else if (goodsDTO.getDistributorScopeNo().equals(Constant.SCOPE_DEPARTMENT)) {
            goodsDTO.setDepartmentNoIds(mapper.listGoodsDepartmentRelevanceIdNo(goodsId));
        } else if (goodsDTO.getDistributorScopeNo().equals(Constant.SCOPE_ADMIN)) {
            goodsDTO.setAdminNoIds(mapper.listGoodsAdminRelevanceIdNo(goodsId));
        }
    }

    /**
     * 获取货品(SKU)装箱信息列表
     * 
     * @param qry
     * @return
     */
    public List<GoodsItemBoxDTO> listGoodsItemBox(GoodsItemId qry) {
        List<GoodsItemBoxDO> goodsItemBoxDOS = mapper.listGoodsItemBoxByGoodsItemId(qry.getId());
        return GoodsConvertor.toGoodsItemBoxDTOList(goodsItemBoxDOS);
    }

    /**
     * 根据栏目id获取商品列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<GoodsListStoreDTO> columnGoodsList(GoodsListColumnQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<GoodsListStoreDO> goodsListDOS = mapper.listGoodsStoreColumn(qry.getColumnId());
        PageInfo pageInfo = new PageInfo(goodsListDOS);
        List<GoodsListStoreDTO> dtos = GoodsConvertor.toGoodsListStoreDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 根据板块id获取商品列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<GoodsListStoreDTO> sectionGoodsList(GoodsListSectionQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<GoodsListStoreDO> goodsListDOS = mapper.listGoodsStoreSection(qry.getSectionId());
        PageInfo pageInfo = new PageInfo(goodsListDOS);
        List<GoodsListStoreDTO> dtos = GoodsConvertor.toGoodsListStoreDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 根据移动端配置id获取商品列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<GoodsListStoreDTO> mobileGoodsList(GoodsListMobileQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<GoodsListStoreDO> goodsListDOS = mapper.listGoodsStoreMobile(qry.getMobileId(), qry.getModuleType());
        PageInfo pageInfo = new PageInfo(goodsListDOS);
        List<GoodsListStoreDTO> dtos = GoodsConvertor.toGoodsListStoreDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 根据分类ids获取商品列表
     * 
     * @param qry
     * @return
     */
    public List<GoodsListDTO> goodsListByClassifyIds(BaseIds qry) {
        List<GoodsListDO> goodsListDOS = mapper.listGoodsByClassifyIds(qry.getIds());
        return GoodsConvertor.toGoodsListDTOList(goodsListDOS);
    }

    /**
     * 根据商品ids获取商品列表
     * 
     * @param qry
     * @return
     */
    public List<GoodsListDTO> goodsListByIds(BaseIds qry) {
        List<GoodsListDO> goodsListDOS = mapper.listGoodsByIds(qry.getIds());
        return GoodsConvertor.toGoodsListDTOList(goodsListDOS);
    }

    public List<GoodsItemDO> listAllItem() {
        return mapper.listAllItem();
    }

    /**
     * 分页获取指定分销商的货品列表
     * 
     * @param goodsQuery
     * @return
     */
    public PageInfo<GoodsItemSimpleDTO> pageAssignByDistributorId(DistributorGoodsQuery goodsQuery) {
        PageHelper.startPage(goodsQuery.getPage(), goodsQuery.getSize());
        List<GoodsItemSimpleDO> list =
            mapper.listAssignByDistributorId(goodsQuery.getDistributorId(), goodsQuery.getContent());
        PageInfo pageInfo = new PageInfo<>(list);
        List<GoodsItemSimpleDTO> resultList = GoodsConvertor.toGoodsItemSimpleDTO(list, null);
        pageInfo.setList(resultList);
        return pageInfo;
    }

    public PageInfo<GoodsItemSimpleDTO> pageByGoodsTypeAndDiyType(DiyGoodsPageQry diyGoodsPageQry) {
        List<Integer> itemIdList = null;
        List<MaterialDTORpcQry> materialDTORpcQryList = null;
        if (Constant.GOODS_TYPE_GENERAL.equals(diyGoodsPageQry.getGoodsType())) {
            Response<List<MaterialDTORpcQry>> materialResponse = materialServiceRpc.listAllGroupByItemId();
            materialDTORpcQryList = materialResponse.getData();
            itemIdList = materialDTORpcQryList.stream().map(MaterialDTORpcQry::getItemId).collect(Collectors.toList());
        }
        PageHelper.startPage(diyGoodsPageQry.getPage(), diyGoodsPageQry.getSize());
        List<GoodsItemSimpleDO> list = mapper.listByGoodsTypeAndDiyTypeAndItemIdList(diyGoodsPageQry.getGoodsType(),
            diyGoodsPageQry.getDiyType(), itemIdList, diyGoodsPageQry.getContent());
        PageInfo pageInfo = new PageInfo<>(list);
        List<GoodsItemSimpleDTO> resultList = GoodsConvertor.toGoodsItemSimpleDTO(list, materialDTORpcQryList);
        pageInfo.setList(resultList);
        return pageInfo;
    }

    /**
     * 根据货品ids获取货品上下架状态
     * 
     * @param itemIds
     * @return
     */
    public List<GoodsItemStatusDO> listGoodsItemStatusByItemIdsAndLifeCycle(List<Integer> itemIds, Short lifeCycle) {
        return mapper.listGoodsItemStatusByItemIdsAndLifeCycle(itemIds, lifeCycle);
    }
}
