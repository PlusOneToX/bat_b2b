package com.bat.promotion.service.groupseckill.executor;

import static com.bat.promotion.service.common.Constant.*;
import static com.bat.promotion.service.groupseckill.executor.ErrorCode.B_PROMOTION_GROUP_SECKILL_NULL;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.dao.groupseckill.*;
import com.bat.promotion.service.groupseckill.convertor.GroupSeckillConvertor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.groupseckill.dto.GroupSeckillListQry;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillDTO;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillGoodsDTO;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillListDTO;
import com.bat.promotion.dao.groupseckill.*;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDO;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDistributorRelevanceDO;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillGoodsDO;
import com.bat.promotion.service.common.rpc.PromotionRpcQryExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/21 14:35
 */
@Component
public class GroupSeckillQryExe {

    @Resource
    private GroupSeckillMapper groupSeckillMapper;
    @Resource
    private GroupSeckillScalePriceRelevanceMapper scalePriceRelevanceMapper;
    @Resource
    private GroupSeckillScalePriceRelevanceNoMapper scalePriceRelevanceNoMapper;
    @Resource
    private GroupSeckillDepartmentRelevanceMapper departmentRelevanceMapper;
    @Resource
    private GroupSeckillDepartmentRelevanceNoMapper departmentRelevanceNoMapper;
    @Resource
    private GroupSeckillAdminRelevanceMapper adminRelevanceMapper;
    @Resource
    private GroupSeckillAdminRelevanceNoMapper adminRelevanceNoMapper;
    @Resource
    private GroupSeckillDistributorRelevanceMapper distributorRelevanceMapper;
    @Resource
    private GroupSeckillDistributorRelevanceNoMapper distributorRelevanceNoMapper;
    @Resource
    private GroupSeckillGoodsMapper groupSeckillGoodsMapper;
    @Resource
    private PromotionRpcQryExe rpcQryExe;

    /**
     * 根据搜索条件查找拼团秒杀列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<GroupSeckillListDTO> listGroupSeckill(GroupSeckillListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<GroupSeckillDO> groupSeckillDOS = null;
        // 搜索内容类型，1 拼团秒杀活动名称 3 货品编号 4 分销商
        if (StringUtils.isNotBlank(qry.getContent()) && qry.getContentType() != null
            && qry.getContentType().equals(CONTENT_TYPE_3)) {
            groupSeckillDOS = groupSeckillMapper.listGroupSeckillByGoods(qryMap);
        } else if (StringUtils.isNotBlank(qry.getContent()) && qry.getContentType() != null
            && qry.getContentType().equals(CONTENT_TYPE_4)) {
            groupSeckillDOS = groupSeckillMapper.listGroupSeckillByDistributorName(qryMap);
        } else {
            groupSeckillDOS = groupSeckillMapper.listGroupSeckill(qryMap);
        }
        PageInfo pageInfo = new PageInfo(groupSeckillDOS);
        List<GroupSeckillListDTO> dtos = GroupSeckillConvertor.toGroupSeckillListDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 根据id查询拼团秒杀活动详情
     * 
     * @param qry
     * @return
     */
    public GroupSeckillDTO getGroupSeckill(BaseId qry) {
        return getGroupSeckill(qry.getId());
    }

    public GroupSeckillDTO getGroupSeckill(Integer id) {
        GroupSeckillDO groupSeckillDO = groupSeckillMapper.selectByPrimaryKey(id);
        if (groupSeckillDO == null) {
            throw PromotionException.buildException(B_PROMOTION_GROUP_SECKILL_NULL);
        }
        GroupSeckillDTO dto = GroupSeckillConvertor.toGroupSeckillDTO(groupSeckillDO);
        getGroupSeckillScope(dto);
        getGroupSeckillScopeNo(dto);
        getGroupSeckillGoods(dto);
        return dto;
    }

    private void getGroupSeckillGoods(GroupSeckillDTO dto) {
        List<GroupSeckillGoodsDO> goodsDOS = groupSeckillGoodsMapper.listByGroupSeckillId(dto.getId());
        if (!CollectionUtils.isEmpty(goodsDOS)) {
            List<Integer> goodsItemIds =
                goodsDOS.stream().map(GroupSeckillGoodsDO::getItemId).collect(Collectors.toList());
            List<GoodsItemRpcDTO> itemRpcDTOS = rpcQryExe.listGoodsItem(goodsItemIds);
            List<GroupSeckillGoodsDTO> dtos = GroupSeckillConvertor.toGroupSeckillGoodsDTOList(goodsDOS, itemRpcDTOS);
            dto.setGoods(dtos);
        }
    }

    /**
     * 获取拼团秒杀活动可视范围数据
     *
     * @param dto
     */
    private void getGroupSeckillScope(GroupSeckillDTO dto) {
        Integer groupSeckillId = dto.getId();
        if (dto.getDistributorScope() == null) {
            return;
        }
        if (dto.getDistributorScope().equals(SCOPE_SCALE_PRICE)) {
            dto.setScalePriceIds(scalePriceRelevanceMapper.listScalePriceRelevanceIdByGroupSeckillId(groupSeckillId));
        } else if (dto.getDistributorScope().equals(SCOPE_DISTRIBUTOR)) {
            List<GroupSeckillDistributorRelevanceDO> distributorRelevanceDOS =
                distributorRelevanceMapper.listDistributorRelevanceByGroupSeckillId(groupSeckillId);
            dto.setDistributors(GroupSeckillConvertor.toGroupSeckillDistributorScopeDTOList(distributorRelevanceDOS));
        } else if (dto.getDistributorScope().equals(SCOPE_DEPARTMENT)) {
            dto.setDepartmentIds(departmentRelevanceMapper.listDepartmentRelevanceIdByGroupSeckillId(groupSeckillId));
        } else if (dto.getDistributorScope().equals(SCOPE_ADMIN)) {
            dto.setAdminIds(adminRelevanceMapper.listAdminRelevanceIdByGroupSeckillId(groupSeckillId));
        }
    }

    /**
     * 获取拼团秒杀活动不可视范围数据
     *
     * @param dto
     */
    private void getGroupSeckillScopeNo(GroupSeckillDTO dto) {
        Integer groupSeckillId = dto.getId();
        if (dto.getDistributorScopeNo() == null) {
            return;
        }
        if (dto.getDistributorScopeNo().equals(SCOPE_SCALE_PRICE)) {
            dto.setScalePriceNoIds(
                scalePriceRelevanceNoMapper.listScalePriceRelevanceNoIdByGroupSeckillId(groupSeckillId));
        } else if (dto.getDistributorScopeNo().equals(SCOPE_DISTRIBUTOR)) {
            List<Integer> distributorIds =
                distributorRelevanceNoMapper.listDistributorRelevanceNoIdByGroupSeckillId(groupSeckillId);
            List<DistributorRpcDTO> rpcDTOS = rpcQryExe.listDistributorNameRpcDTO(distributorIds);
            dto.setDistributorNos(GroupSeckillConvertor.toGroupSeckillDistributorScopeDTOListForRpc(rpcDTOS));
        } else if (dto.getDistributorScopeNo().equals(SCOPE_DEPARTMENT)) {
            dto.setDepartmentNoIds(
                departmentRelevanceNoMapper.listDepartmentRelevanceNoIdByGroupSeckillId(groupSeckillId));
        } else if (dto.getDistributorScopeNo().equals(SCOPE_ADMIN)) {
            dto.setAdminNoIds(adminRelevanceNoMapper.listAdminRelevanceNoIdByGroupSeckillId(groupSeckillId));
        }
    }

    /**
     * 计算拼团秒杀活动价格
     * 
     * @param goodsSpellGroupPrices
     * @return
     */
    public List<GoodsItemPromotionPriceRpcDTO> goodsSpellGroupPrices(List<GoodsItemPriceRpcQry> goodsSpellGroupPrices,
        Date time) throws PromotionException {
        List<Integer> groupSeckillIds = goodsSpellGroupPrices.stream().map(GoodsItemPriceRpcQry::getSpellGroupId)
            .distinct().collect(Collectors.toList());
        List<GroupSeckillDO> groupSeckillDOS = groupSeckillMapper.listGroupSeckillByIds(groupSeckillIds);
        // groupSeckillDOS.forEach(groupSeckillDO -> {
        // if (time.getTime() > groupSeckillDO.getEndTime().getTime()) {
        // throw PromotionException.buildException(B_PROMOTION_EXPIRE_ERROR);
        // }
        // });
        List<Integer> itemIds =
            goodsSpellGroupPrices.stream().map(GoodsItemPriceRpcQry::getItemId).distinct().collect(Collectors.toList());
        // 获取商品对应拼团秒杀规则
        List<GroupSeckillGoodsDO> groupSeckillGoodsDOS =
            groupSeckillGoodsMapper.listByGroupSeckillIdsAndItemIds(groupSeckillIds, itemIds);
        List<GoodsItemPromotionPriceRpcDTO> rpcDTOS = GroupSeckillConvertor
            .toGoodsItemPromotionPriceRpcDTOList(groupSeckillDOS, goodsSpellGroupPrices, groupSeckillGoodsDOS, time);
        // TODO 注意已拼和已秒数量更新（考虑高并发情况）
        // groupSeckillGoodsMapper.updateRealSumList(groupSeckillGoodsDOS);
        return rpcDTOS;
    }
}
