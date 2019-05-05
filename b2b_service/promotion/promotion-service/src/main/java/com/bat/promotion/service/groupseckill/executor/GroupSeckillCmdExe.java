package com.bat.promotion.service.groupseckill.executor;

import static com.bat.promotion.service.common.CommonErrorCode.B_PROMOTION_TIME_ERROR;
import static com.bat.promotion.service.common.CommonErrorCode.B_PROMOTION_UPDATE_ERROR;
import static com.bat.promotion.service.common.Constant.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.api.groupseckill.dto.*;
import com.bat.promotion.dao.groupseckill.*;
import com.bat.promotion.service.groupseckill.convertor.GroupSeckillConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.system.check.dto.data.CheckConfigDetailRpcDTO;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.groupseckill.dto.*;
import com.bat.promotion.dao.groupseckill.*;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDO;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillGoodsDO;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillStatusDO;
import com.bat.promotion.mq.dto.GoodsSaleDTO;
import com.bat.promotion.service.check.executor.PromotionCheckCmdExe;
import com.bat.promotion.service.common.PromotionConfig;
import com.bat.promotion.service.common.rpc.PromotionRpcQryExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/21 14:35
 */
@Component
public class GroupSeckillCmdExe {

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
    private GroupSeckillRpcCmdExe rpcCmdExe;
    @Resource
    private PromotionConfig config;
    @Resource
    private PromotionRpcQryExe rpcQryExe;
    @Resource
    private PromotionCheckCmdExe checkCmdExe;

    /**
     * 新增拼团秒杀活动
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void createGroupSeckill(GroupSeckillCmd cmd, String userId, String userName) {
        Date date = new Date(System.currentTimeMillis());
        GroupSeckillDO groupSeckillDO = GroupSeckillConvertor.toGroupSeckillDO(cmd, date);
        CheckConfigDetailRpcDTO detailRpcDTO = rpcQryExe.getCheckFlows(PROMOTION_TYPE_15);
        groupSeckillApplyAndStatus(groupSeckillDO, detailRpcDTO);
        groupSeckillMapper.insert(groupSeckillDO);
        saveGroupSeckillScope(groupSeckillDO.getId(), cmd, OPERATION_TYPE_1);
        saveGroupSeckillGoods(groupSeckillDO.getId(), cmd, OPERATION_TYPE_1);
        if (groupSeckillDO.getApplyStatus().equals(APPLY_STATUS_2)) {
            // 商品价格更新
            if (groupSeckillDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_1)) {
                rpcCmdExe.goodsPromotionPriceByGroupSeckill(groupSeckillDO.getId(), cmd);
            }
            // 拼团秒杀定时任务
            cronJobGroupSeckill(groupSeckillDO);
        } else if (groupSeckillDO.getApplyStatus().equals(APPLY_STATUS_1)) {
            checkCmdExe.promotionCheck(PROMOTION_CHECK_TYPE_2, groupSeckillDO.getId(), detailRpcDTO, date, userId,
                userName);
        }
    }

    /**
     * 拼团秒杀定时任务
     *
     * @param groupSeckillDO
     */
    private void cronJobGroupSeckill(GroupSeckillDO groupSeckillDO) {
        try {
            rpcCmdExe.groupSeckillAddXxlJob(groupSeckillDO);
        } catch (ParseException e) {
            throw PromotionException.buildException(B_PROMOTION_TIME_ERROR);
        }
    }

    /**
     * 拼团秒杀活动审批和状态处理
     */
    private void groupSeckillApplyAndStatus(GroupSeckillDO groupSeckillDO, CheckConfigDetailRpcDTO detailRpcDTO) {
        if (groupSeckillDO.getApplyStatus() != null && groupSeckillDO.getApplyStatus().equals(APPLY_STATUS_0)) {
            groupSeckillDO.setGroupSeckillStatus(GROUP_SECKILL_STATUS_0);
        } else {
            if (detailRpcDTO != null && detailRpcDTO.getOpenFlag().equals(FLAG_1)) {
                groupSeckillDO.setApplyStatus(APPLY_STATUS_1);
                groupSeckillDO.setGroupSeckillStatus(GROUP_SECKILL_STATUS_0);
            } else {
                groupSeckillDO.setApplyStatus(APPLY_STATUS_2);
                // 拼团秒杀进行状态(如果开始时间与创建时间间隔10秒情况，活动直接开始)
                if (groupSeckillDO.getStartTime().getTime()
                    - groupSeckillDO.getCreateTime().getTime() < config.getIntervalTime() * 1000) {
                    groupSeckillDO.setGroupSeckillStatus(GROUP_SECKILL_STATUS_1);
                } else {
                    groupSeckillDO.setGroupSeckillStatus(GROUP_SECKILL_STATUS_0);
                }
            }
        }
    }

    /**
     * 保存商品
     * 
     * @param groupSeckillId
     * @param cmd
     * @param operationType
     */
    public void saveGroupSeckillGoods(Integer groupSeckillId, GroupSeckillCmd cmd, Short operationType) {
        if (!operationType.equals(OPERATION_TYPE_1)) {
            groupSeckillGoodsMapper.deleteByGroupSeckillId(groupSeckillId);
        }
        List<GroupSeckillGoodsCmd> goodsCmds = cmd.getGoods();
        if (!CollectionUtils.isEmpty(goodsCmds)) {
            List<GroupSeckillGoodsDO> goodsDOS =
                GroupSeckillConvertor.toGroupSeckillGoodsDOList(groupSeckillId, goodsCmds);
            groupSeckillGoodsMapper.insertList(goodsDOS);
        }
    }

    /**
     * 保存拼团促销活动可视范围
     * 
     * @param groupSeckillId
     * @param cmd
     * @param operationType
     */
    private void saveGroupSeckillScope(Integer groupSeckillId, GroupSeckillCmd cmd, Short operationType) {
        // 非新增时，需先删除历史关系
        if (!operationType.equals(OPERATION_TYPE_1)) {
            deleteGroupSeckillScope(groupSeckillId);
        }
        // 可视关系
        List list = GroupSeckillConvertor.toGroupSeckillRelevance(groupSeckillId, cmd);
        if (list != null && list.size() > 0) {
            Short distributorScope = cmd.getDistributorScope();
            if (distributorScope.equals(SCOPE_SCALE_PRICE)) {
                scalePriceRelevanceMapper.createScalePriceRelevanceList(list);
            } else if (distributorScope.equals(SCOPE_DISTRIBUTOR)) {
                distributorRelevanceMapper.createDistributorRelevanceList(list);
            } else if (distributorScope.equals(SCOPE_DEPARTMENT)) {
                departmentRelevanceMapper.createDepartmentRelevanceList(list);
            } else if (distributorScope.equals(SCOPE_ADMIN)) {
                adminRelevanceMapper.createAdminRelevanceList(list);
            }
        }
        // 不可视关系
        List listNo = GroupSeckillConvertor.toGroupSeckillRelevanceNO(groupSeckillId, cmd);
        if (listNo != null && listNo.size() > 0) {
            Short distributorScopeNo = cmd.getDistributorScopeNo();
            if (distributorScopeNo.equals(SCOPE_SCALE_PRICE)) {
                scalePriceRelevanceNoMapper.createScalePriceRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(SCOPE_DISTRIBUTOR)) {
                distributorRelevanceNoMapper.createDistributorRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(SCOPE_DEPARTMENT)) {
                departmentRelevanceNoMapper.createDepartmentRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(SCOPE_ADMIN)) {
                adminRelevanceNoMapper.createAdminRelevanceNoList(listNo);
            }
        }
        // 更新分销商促销活动可视数据
        rpcCmdExe.distributorGroupSeckillRelevance(groupSeckillId, cmd);
    }

    /**
     * 删除拼团秒杀活动可视范围
     *
     * @param groupSeckillId
     */
    private void deleteGroupSeckillScope(Integer groupSeckillId) {
        scalePriceRelevanceMapper.deleteByGroupSeckillId(groupSeckillId);
        scalePriceRelevanceNoMapper.deleteByGroupSeckillId(groupSeckillId);
        departmentRelevanceMapper.deleteByGroupSeckillId(groupSeckillId);
        departmentRelevanceNoMapper.deleteByGroupSeckillId(groupSeckillId);
        adminRelevanceMapper.deleteByGroupSeckillId(groupSeckillId);
        adminRelevanceNoMapper.deleteByGroupSeckillId(groupSeckillId);
        distributorRelevanceMapper.deleteByGroupSeckillId(groupSeckillId);
        distributorRelevanceNoMapper.deleteByGroupSeckillId(groupSeckillId);
    }

    /**
     * 修改拼团秒杀活动(草稿状态的拼团秒杀活动修改)
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGroupSeckill(GroupSeckillCmd cmd, String userId, String userName) {
        if (cmd.getId() == null) {
            throw PromotionException.buildException(ErrorCode.P_PROMOTION_ID_NULL);
        }
        GroupSeckillDO beforeGroupSeckillDO = groupSeckillMapper.selectByPrimaryKey(cmd.getId());
        if (beforeGroupSeckillDO == null) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_GROUP_SECKILL_NULL);
        }
        if (!beforeGroupSeckillDO.getApplyStatus().equals(APPLY_STATUS_0)) {
            throw PromotionException.buildException(B_PROMOTION_UPDATE_ERROR);
        }
        Date date = new Date(System.currentTimeMillis());
        GroupSeckillDO afterGroupSeckillDO = GroupSeckillConvertor.toGroupSeckillDO(cmd, date);
        afterGroupSeckillDO.setCreateTime(beforeGroupSeckillDO.getCreateTime());
        // 判断是否需要审批
        CheckConfigDetailRpcDTO detailRpcDTO = rpcQryExe.getCheckFlows(PROMOTION_TYPE_15);
        groupSeckillApplyAndStatus(afterGroupSeckillDO, detailRpcDTO);
        groupSeckillMapper.updateByPrimaryKey(afterGroupSeckillDO);
        saveGroupSeckillScope(afterGroupSeckillDO.getId(), cmd, OPERATION_TYPE_2);
        saveGroupSeckillGoods(afterGroupSeckillDO.getId(), cmd, OPERATION_TYPE_2);
        if (afterGroupSeckillDO.getApplyStatus().equals(APPLY_STATUS_2)) {
            // 商品价格更新
            if (afterGroupSeckillDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_1)) {
                rpcCmdExe.goodsPromotionPriceByGroupSeckill(afterGroupSeckillDO.getId(), cmd);
            }
            // 拼团秒杀定时任务
            cronJobGroupSeckill(afterGroupSeckillDO);
        } else if (afterGroupSeckillDO.getApplyStatus().equals(APPLY_STATUS_1)) {
            checkCmdExe.promotionCheck(PROMOTION_CHECK_TYPE_2, afterGroupSeckillDO.getId(), detailRpcDTO, date, userId,
                userName);
        }
    }

    /**
     * 根据id删除拼团秒杀活动
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteGroupSeckill(BaseId cmd) {
        GroupSeckillDO groupSeckillDO = groupSeckillMapper.selectByPrimaryKey(cmd.getId());
        if (groupSeckillDO == null) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_GROUP_SECKILL_NULL);
        }
        // 只有草稿和申请失败的促销活动可以删除
        if (!groupSeckillDO.getApplyStatus().equals(APPLY_STATUS_0)
            && !groupSeckillDO.getApplyStatus().equals(APPLY_STATUS_3)
            && !groupSeckillDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_0)) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_DELETE_ERROR);
        }
        deleteGroupSeckillScope(cmd.getId());
        groupSeckillGoodsMapper.deleteByGroupSeckillId(cmd.getId());
        groupSeckillMapper.deleteByPrimaryKey(cmd.getId());
    }

    /**
     * 根据拼团秒杀活动id变更状态
     * 
     * @param cmd
     */
    public void updateGroupSeckillStatus(GroupSeckillStatusCmd cmd) {
        List<Integer> ids = new ArrayList<>();
        ids.add(cmd.getId());
        updateListGroupSeckillStatus(ids, cmd.getGroupSeckillStatus());
    }

    /**
     * 更新拼团秒杀活动
     * 
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateListGroupSeckillStatus(List<Integer> ids, Short groupSeckillStatus) {
        List<GroupSeckillStatusDO> statusDOS =
            GroupSeckillConvertor.toGroupSeckillStatusDOList(ids, groupSeckillStatus);
        if (!CollectionUtils.isEmpty(statusDOS)) {
            groupSeckillMapper.updateListGroupSeckillStatus(statusDOS);
            statusDOS.forEach(statusDO -> {
                GroupSeckillDO groupSeckillDO = groupSeckillMapper.selectByPrimaryKey(statusDO.getId());
                if (groupSeckillDO != null) {
                    if (statusDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_1)) {// 拼团秒杀开始，更新商品最小价格
                        changeGoodsPromotionPrice(groupSeckillDO);
                    } else if (!statusDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_1)) {// 拼团秒杀非开始时，删除商品最小价格
                        rpcCmdExe.deletePromotionPriceByGroupSeckillId(statusDO.getId());
                        // 活动结束，删除活动数据
                        if (statusDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_3)
                            || statusDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_4)) {
                            rpcCmdExe.deleteGroupSeckillRelevanceByGroupSeckillId(statusDO.getId());
                        }
                    }
                }
            });
        }
    }

    /**
     * 根据拼团秒杀活动id更新商品列表价格
     * 
     * @param groupSeckillId
     */
    public void changeGoodsPromotionPrice(Integer groupSeckillId) {
        GroupSeckillDO groupSeckillDO = groupSeckillMapper.selectByPrimaryKey(groupSeckillId);
        changeGoodsPromotionPrice(groupSeckillDO);
    }

    /**
     * 根据拼团秒杀活动更新商品列表价格
     * 
     * @param groupSeckillDO
     */
    public void changeGoodsPromotionPrice(GroupSeckillDO groupSeckillDO) {
        List<GroupSeckillGoodsDO> goodsDOS = groupSeckillGoodsMapper.listByGroupSeckillId(groupSeckillDO.getId());
        rpcCmdExe.goodsPromotionPriceByGroupSeckill(groupSeckillDO, goodsDOS);
    }

    /**
     * 拼团秒杀活动排序更新
     * 
     * @param cmds
     */
    public void updateGroupSeckillSort(List<GroupSeckillSortCmd> cmds) {
        if (!CollectionUtils.isEmpty(cmds)) {
            groupSeckillMapper.updateGroupSeckillSort(GroupSeckillConvertor.toGroupSeckillSortDOList(cmds));
        }
    }

    /**
     * 拼团秒杀商品排序更新
     * 
     * @param cmds
     */
    public void updateGroupSeckillGoodsSort(List<GroupSeckillGoodsSortCmd> cmds) {
        if (!CollectionUtils.isEmpty(cmds)) {
            groupSeckillGoodsMapper
                .updateGroupSeckillGoodsSort(GroupSeckillConvertor.toGroupSeckillGoodsSortDOList(cmds));
        }
    }

    /**
     * 刷新活动数据
     *
     * @param groupSeckillDO
     */
    public void updateGroupSeckill(GroupSeckillDO groupSeckillDO) {
        long timeMillis = System.currentTimeMillis();
        // 拼团秒杀进行状态(如果开始时间与创建时间间隔10秒情况，活动直接开始)
        if (groupSeckillDO.getStartTime().getTime() - timeMillis < config.getIntervalTime() * 1000) {
            groupSeckillDO.setGroupSeckillStatus(GROUP_SECKILL_STATUS_1);
        } else {
            groupSeckillDO.setGroupSeckillStatus(GROUP_SECKILL_STATUS_0);
        }
        // 商品价格更新
        if (groupSeckillDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_1)) {
            changeGoodsPromotionPrice(groupSeckillDO);
        }
        // 拼团秒杀定时任务
        cronJobGroupSeckill(groupSeckillDO);
        groupSeckillMapper.updateByPrimaryKey(groupSeckillDO);
    }

    /**
     * 更新拼团秒杀已拼数量
     * 
     * @param saleDTOS
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRealSumList(List<GoodsSaleDTO> saleDTOS) {
        if (!CollectionUtils.isEmpty(saleDTOS)) {
            List<GoodsSaleDTO> filterSaleDTOS = saleDTOS.stream().filter(saleDTO -> saleDTO.getSpellGroupId() != null
                && saleDTO.getSaleNum() != null && saleDTO.getSaleNum() > 0).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(filterSaleDTOS)) {
                List<Integer> itemIds =
                    filterSaleDTOS.stream().map(GoodsSaleDTO::getItemId).collect(Collectors.toList());
                List<Integer> spellGroupIds =
                    filterSaleDTOS.stream().map(GoodsSaleDTO::getSpellGroupId).collect(Collectors.toList());
                List<GroupSeckillGoodsDO> goodsDOS =
                    groupSeckillGoodsMapper.listByGroupSeckillIdsAndItemIds(spellGroupIds, itemIds);
                List<GroupSeckillGoodsDO> changeGoodsDOS =
                    GroupSeckillConvertor.toGroupSeckillGoodsDOList(saleDTOS, goodsDOS);
                // TODO 注意已拼和已秒数量更新（暂未考虑高并发情况）
                groupSeckillGoodsMapper.updateRealSumList(changeGoodsDOS);
            }
        }
    }
}
