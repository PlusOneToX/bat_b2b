package com.bat.promotion.service.rebatevoucher.executor;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.dao.rebatevoucher.RebateVoucherMapper;
import com.bat.promotion.dao.rebatevoucher.RebateVoucherUsageRecordMapper;
import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherDO;
import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherStatusDO;
import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherUsageRecordDO;
import com.bat.promotion.service.check.executor.PromotionCheckCmdExe;
import com.bat.promotion.service.common.CommonErrorCode;
import com.bat.promotion.service.common.Constant;
import com.bat.promotion.service.common.rpc.PromotionRpcQryExe;
import com.bat.promotion.service.rebatevoucher.convertor.RebateVoucherConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.system.check.dto.data.CheckConfigDetailRpcDTO;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherCmd;
import com.bat.promotion.mq.dto.RebateVoucherDTO;
import com.bat.promotion.mq.dto.RebateVoucherRollBackDTO;
import com.bat.promotion.service.rebatevoucher.dto.RollBackRebateSumDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/12/31 15:38
 */
@Component
@Slf4j
public class RebateVoucherCmdExe {

    @Resource
    private RebateVoucherMapper rebateVoucherMapper;

    @Resource
    private RebateVoucherUsageRecordMapper rebateVoucherUsageRecordMapper;

    @Resource
    private PromotionRpcQryExe rpcQryExe;

    @Resource
    private RebateVoucherRpcCmdExe rpcCmdExe;

    @Resource
    private PromotionCheckCmdExe checkCmdExe;

    public void createRebateVoucher(RebateVoucherCmd cmd, String userId, String userName) {
        // Date date = new Date();
        // RebateVoucherDO rebateVoucherDO = RebateVoucherConvertor.toRebateVoucherDO(cmd, date);
        // CheckConfigDetailRpcDTO detailRpcDTO = rpcQryExe.getCheckFlows(PROMOTION_TYPE_16);
        // rebateVoucherApplyAndStatus(rebateVoucherDO, detailRpcDTO);
        // rebateVoucherMapper.insert(rebateVoucherDO);
        // if (rebateVoucherDO.getApplyStatus().equals(APPLY_STATUS_2)) {
        // // 促销活动定时任务
        // cronJobRebateVoucher(rebateVoucherDO);
        // } else if (rebateVoucherDO.getApplyStatus().equals(APPLY_STATUS_1)) {
        // checkCmdExe.promotionCheck(PROMOTION_CHECK_TYPE_4, rebateVoucherDO.getId(), detailRpcDTO, date, userId,
        // userName);
        // }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchCreateRebateVoucher(List<RebateVoucherCmd> cmds, String userId, String userName) {
        Date date = new Date();
        List<RebateVoucherDO> rebateVoucherDOS =
            RebateVoucherConvertor.toRebateVoucherDOList(cmds, date, userId, userName);
        CheckConfigDetailRpcDTO detailRpcDTO = rpcQryExe.getCheckFlows(Constant.PROMOTION_TYPE_16);
        rebateVoucherDOS.forEach(rebateVoucherDO -> rebateVoucherApplyAndStatus(rebateVoucherDO, detailRpcDTO));
        rebateVoucherMapper.batchInsert(rebateVoucherDOS);
        RebateVoucherDO rebateVoucherDO = rebateVoucherDOS.get(0);
        rebateVoucherDOS.forEach(rebateVoucherDO1 -> {
            rebateVoucherDO1.setBatchId(rebateVoucherDO.getId());
            rebateVoucherDO1.setBatchName(rebateVoucherDO.getName());
            rebateVoucherMapper.updateByPrimaryKey(rebateVoucherDO1);
        });
        if (rebateVoucherDO.getApplyStatus().equals(Constant.APPLY_STATUS_2)) {
            // 促销活动定时任务
            rebateVoucherDOS.forEach(this::cronJobRebateVoucher);
        } else if (rebateVoucherDO.getApplyStatus().equals(Constant.APPLY_STATUS_1)) {
            checkCmdExe.promotionCheck(Constant.PROMOTION_CHECK_TYPE_4, rebateVoucherDO.getId(), detailRpcDTO, date, userId,
                userName);
        }
    }

    /**
     * 返利代金券的审批和状态处理
     *
     * @param rebateVoucherDO
     */
    private void rebateVoucherApplyAndStatus(RebateVoucherDO rebateVoucherDO, CheckConfigDetailRpcDTO detailRpcDTO) {
        if (rebateVoucherDO.getApplyStatus() != null && rebateVoucherDO.getApplyStatus().equals(Constant.APPLY_STATUS_0)) {
            rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_0);
        } else {
            if (detailRpcDTO != null && detailRpcDTO.getOpenFlag().equals(Constant.FLAG_1)) {
                rebateVoucherDO.setApplyStatus(Constant.APPLY_STATUS_1);
                rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_1);
            } else {
                rebateVoucherDO.setApplyStatus(Constant.APPLY_STATUS_2);
                Date date = new Date();
                if (date.before(rebateVoucherDO.getStartTime())) {
                    // 待生效
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_4);
                } else if (date.after(rebateVoucherDO.getStartTime()) && date.before(rebateVoucherDO.getEndTime())) {
                    // 在有效期中
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_5);
                } else if (date.after(rebateVoucherDO.getEndTime())) {
                    // 直接过期
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_7);
                }
            }
        }
    }

    /**
     * 返利代金券定时任务
     *
     * @param rebateVoucherDO
     */
    public void cronJobRebateVoucher(RebateVoucherDO rebateVoucherDO) {
        try {
            rpcCmdExe.rebateVoucherAddXxlJob(rebateVoucherDO);
        } catch (ParseException e) {
            throw PromotionException.buildException(CommonErrorCode.B_PROMOTION_TIME_ERROR);
        }
    }

    /**
     * 定时器过来的 更新代金券 开启或者过期(别的入口慎用，没有校验其他状态)
     * 
     * @param ids
     * @param rebateVoucherStatus
     */
    public void updateListRebateVoucherStatus(List<Integer> ids, Short rebateVoucherStatus) {
        List<RebateVoucherStatusDO> statusDOS =
            RebateVoucherConvertor.toRebateVoucherStatusDOList(ids, rebateVoucherStatus);
        if (!CollectionUtils.isEmpty(statusDOS)) {
            rebateVoucherMapper.updateListRebateVoucherStatus(statusDOS);
        }
    }

    public void updateRebateVoucher(RebateVoucherCmd cmd) {
        if (cmd.getId() == null) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_REBATE_VOUCHER_ID_NULL);
        }
        RebateVoucherDO rebateVoucherDO = rebateVoucherMapper.selectByPrimaryKey(cmd.getId());
        if (rebateVoucherDO == null) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_REBATE_VOUCHER_ID_NOT_EXISTS);
        }
        BeanUtil.copyProperties(cmd, rebateVoucherDO,
            CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        freezeStatusChange(cmd, rebateVoucherDO);
        rebateVoucherDO.setUpdateTime(new Date());
        rebateVoucherMapper.updateByPrimaryKey(rebateVoucherDO);
    }

    private void freezeStatusChange(RebateVoucherCmd cmd, RebateVoucherDO rebateVoucherDO) {
        if (cmd.getFreezeStatus() != null) {
            // 可用 待生效 才能冻结
            boolean allowFreezeStatus = rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_4)
                || rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_5);
            if (cmd.getFreezeStatus().equals(Constant.REBATE_VOUCHER_FREEZE_STATUS_10)) {
                // 解冻 后要判断代金券回到待生效还是可用 还是已过期
                Date date = new Date();
                if (date.before(rebateVoucherDO.getStartTime())) {
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_4);
                } else if (date.after(rebateVoucherDO.getStartTime()) && date.before(rebateVoucherDO.getEndTime())) {
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_5);
                } else if (date.after(rebateVoucherDO.getEndTime())) {
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_7);
                }
            } else if (cmd.getFreezeStatus().equals(Constant.REBATE_VOUCHER_FREEZE_STATUS_11)) {
                if (allowFreezeStatus) {
                    // 冻结
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_11);
                } else {
                    throw PromotionException.buildException("只有待生效 可用状态下的返利金券 才能冻结");
                }
            }
        }
    }

    /**
     * 扣减代金券 抵扣金额
     * 
     * @param rebateVoucherDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void deductionRebateVoucher(RebateVoucherDTO rebateVoucherDTO) {
        List<Integer> rebateVoucherIds = rebateVoucherDTO.getRebateVoucherIds();
        List<RebateVoucherDO> rebateVoucherDOS = rebateVoucherMapper.listRebateVoucherByPrimaryKeys(rebateVoucherIds);
        List<RebateVoucherUsageRecordDO> recordDOS = new ArrayList<>();
        // 余额从大到小
        rebateVoucherDOS.sort(Comparator.comparing(RebateVoucherDO::getBalance).reversed());
        List<RebateVoucherDTO.RebateVoucherItemDTO> voucherItems = rebateVoucherDTO.getVoucherItems();
        // 抵扣金额从大到小
        // 注意 使用记录 负数为使用 正数为退还
        voucherItems
            .sort(Comparator.comparing(RebateVoucherDTO.RebateVoucherItemDTO::getRebateVoucherAmount).reversed());
        for (RebateVoucherDTO.RebateVoucherItemDTO voucherItem : voucherItems) {
            for (RebateVoucherDO rebateVoucherDO : rebateVoucherDOS) {
                // 券抵了 还不够 或者 刚好 抵扣100元 余额 80元 还剩20没有抵扣
                BigDecimal subtract = voucherItem.getRebateVoucherAmount().subtract(rebateVoucherDO.getBalance());
                if (subtract.compareTo(BigDecimal.ZERO) >= 0) {
                    RebateVoucherUsageRecordDO recordDO = new RebateVoucherUsageRecordDO();
                    recordDO.setUseAmount(rebateVoucherDO.getBalance().negate());

                    voucherItem.setRebateVoucherAmount(subtract);
                    rebateVoucherDO.setBalance(BigDecimal.ZERO);
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_9);
                    rebateVoucherDO.setUpdateTime(voucherItem.getCreateTime());
                    recordDO.setRebateVoucherId(rebateVoucherDO.getId());
                    recordDO.setRebateVoucherNo(rebateVoucherDO.getVoucherNo());
                    recordDO.setBalance(rebateVoucherDO.getBalance());
                    recordDO.setOrderId(voucherItem.getOrderId());
                    recordDO.setOrderNo(voucherItem.getOrderNo());
                    recordDO.setUseTime(rebateVoucherDO.getUpdateTime());
                    recordDOS.add(recordDO);
                    if (subtract.compareTo(BigDecimal.ZERO) == 0) {
                        // 这个券 刚好把这个订单抵完 券也用完了
                        break;
                    }
                } else if (subtract.compareTo(BigDecimal.ZERO) < 0) {
                    // 这个订单抵扣完了 但是券没有用完 抵扣100元 余额 120元 -20 余额还剩20
                    RebateVoucherUsageRecordDO recordDO = new RebateVoucherUsageRecordDO();
                    recordDO.setUseAmount(voucherItem.getRebateVoucherAmount().negate());

                    voucherItem.setRebateVoucherAmount(BigDecimal.ZERO);
                    rebateVoucherDO.setBalance(subtract.abs());
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_5);
                    rebateVoucherDO.setUpdateTime(voucherItem.getCreateTime());
                    recordDO.setRebateVoucherId(rebateVoucherDO.getId());
                    recordDO.setRebateVoucherNo(rebateVoucherDO.getVoucherNo());
                    recordDO.setBalance(rebateVoucherDO.getBalance());
                    recordDO.setOrderId(voucherItem.getOrderId());
                    recordDO.setOrderNo(voucherItem.getOrderNo());
                    recordDO.setUseTime(rebateVoucherDO.getUpdateTime());
                    recordDOS.add(recordDO);
                    break;
                }
            }
        }
        // 更新代金券
        rebateVoucherMapper.batchUpdate(rebateVoucherDOS);
        // 添加使用记录
        rebateVoucherUsageRecordMapper.batchInsert(recordDOS);
    }

    /**
     * 取消订单 代金券回退
     *
     * @param rebateVoucherRollBackDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void rollBackRebateVoucher(RebateVoucherRollBackDTO rebateVoucherRollBackDTO) {
        Integer orderId = rebateVoucherRollBackDTO.getOrderId();
        BigDecimal rollBackAmount = rebateVoucherRollBackDTO.getRollBackAmount();
        if (rollBackAmount == null) {
            allRollBackRebateVoucher(orderId);
        } else {
            partRollBackRebateVoucher(orderId, rollBackAmount);
        }
    }

    /**
     * 全部回退逻辑 简单点 当初怎么扣的 现在怎么退
     * 
     * @param orderId
     */
    private void allRollBackRebateVoucher(Integer orderId) {
        log.info("订单：{} 开始全额返还代金券",orderId);
        List<RebateVoucherUsageRecordDO> recordDOS = rebateVoucherUsageRecordMapper.listByOrderId(orderId);
        // 一个订单 只会取消一次
        List<RebateVoucherUsageRecordDO> deductionRecords = recordDOS.stream()
            .filter(
                rebateVoucherUsageRecordDO -> rebateVoucherUsageRecordDO.getUseAmount().compareTo(BigDecimal.ZERO) < 0)
            .collect(Collectors.toList());
        List<RebateVoucherUsageRecordDO> rollbackRecords = recordDOS.stream()
            .filter(
                rebateVoucherUsageRecordDO -> rebateVoucherUsageRecordDO.getUseAmount().compareTo(BigDecimal.ZERO) > 0)
            .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(rollbackRecords)) {
            // 中间出现了 退款记录 删除退款记录
            log.info("订单：{} 中间出现了 退款记录,记录有偏差", orderId);
            List<Integer> rollbackRecordIds = rollbackRecords.stream().map(RebateVoucherUsageRecordDO::getId).collect(Collectors.toList());
            rebateVoucherUsageRecordMapper.deleteByPrimaryKeys(rollbackRecordIds);
        }
        Date date = new Date();
        List<RebateVoucherUsageRecordDO> collect = deductionRecords.stream().map(rebateVoucherUsageRecordDO -> {
            RebateVoucherUsageRecordDO aDo = new RebateVoucherUsageRecordDO();
            aDo.setRebateVoucherId(rebateVoucherUsageRecordDO.getRebateVoucherId());
            aDo.setRebateVoucherNo(rebateVoucherUsageRecordDO.getRebateVoucherNo());
            aDo.setOrderId(rebateVoucherUsageRecordDO.getOrderId());
            aDo.setOrderNo(rebateVoucherUsageRecordDO.getOrderNo());
            BigDecimal rollbackAmount = rebateVoucherUsageRecordDO.getUseAmount().negate();
            aDo.setUseAmount(rebateVoucherUsageRecordDO.getUseAmount().negate());
            RebateVoucherDO rebateVoucherDO = rebateVoucherMapper.selectByPrimaryKey(rebateVoucherUsageRecordDO.getRebateVoucherId());
            BigDecimal balance = rebateVoucherDO.getBalance().add(rollbackAmount);
            aDo.setBalance(balance);
            rebateVoucherDO.setBalance(balance);
            rebateVoucherDO.setUpdateTime(date);
            // 已用完 可以转成 可用 或者已过期
            if (rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_9)) {
                if (date.before(rebateVoucherDO.getEndTime())) {
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_5);
                } else {
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_7);
                }
            }
            rebateVoucherMapper.updateByPrimaryKey(rebateVoucherDO);
            aDo.setUseTime(date);
            return aDo;
        }).collect(Collectors.toList());
        rebateVoucherUsageRecordMapper.batchInsert(collect);
        // 修改 代金券的资料

    }

    /**
     * 部分回退
     * 
     * @param orderId
     * @param rollBackAmount
     */
    private void partRollBackRebateVoucher(Integer orderId, BigDecimal rollBackAmount) {
        log.info("订单：{} 开始部分返还代金券，返还金额：{}",orderId,rollBackAmount);
        List<RebateVoucherUsageRecordDO> recordDOS = rebateVoucherUsageRecordMapper.listByOrderId(orderId);
        // 该订单的抵扣记录 负数为抵扣 正数为退还
        // 汇总抵扣与退款 查看当前券还能返还多少
        Map<Integer, List<RebateVoucherUsageRecordDO>> rebateVoucherUsageRecordMaps =
            recordDOS.stream().collect(Collectors.groupingBy(RebateVoucherUsageRecordDO::getRebateVoucherId));
        List<RollBackRebateSumDTO> rollBackList = new ArrayList<>();
        for (Map.Entry<Integer, List<RebateVoucherUsageRecordDO>> entry : rebateVoucherUsageRecordMaps.entrySet()) {
            Integer rebateVoucherId = entry.getKey();
            RebateVoucherUsageRecordDO recordDO = entry.getValue().get(0);
            List<RebateVoucherUsageRecordDO> rebateVoucherUsageRecordDOS = entry.getValue();
            BigDecimal orElse = rebateVoucherUsageRecordDOS.stream().map(RebateVoucherUsageRecordDO::getUseAmount)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            if (orElse.compareTo(BigDecimal.ZERO) > 0) {
                log.error("该订单:{} 该券:{} 退款金额大于抵扣金额，历史返还数据有问题", orderId, rebateVoucherId);
                continue;
            }
            if (orElse.compareTo(BigDecimal.ZERO) == 0) {
                log.info("该订单:{} 该券:{} 已经退完了", orderId, rebateVoucherId);
                continue;
            }
            RebateVoucherDO rebateVoucherDO = rebateVoucherMapper.selectByPrimaryKey(rebateVoucherId);
            BigDecimal subtract = rebateVoucherDO.getFaceValue().subtract(rebateVoucherDO.getBalance());
            if (orElse.abs().compareTo(subtract) > 0) {
                log.error("该订单:{} 该券:{} 理论可退金额大于 面额与余额的差值(即如果退还 会导致余额超过面值)", orderId, rebateVoucherId);
                continue;
            }
            RollBackRebateSumDTO sumDTO = new RollBackRebateSumDTO();
            sumDTO.setRebateVoucherId(rebateVoucherId);
            sumDTO.setRebateVoucherNo(recordDO.getRebateVoucherNo());
            sumDTO.setBalance(rebateVoucherDO.getBalance());
            sumDTO.setUseAmount(orElse);
            sumDTO.setOrderId(recordDO.getOrderId());
            sumDTO.setOrderNo(recordDO.getOrderNo());
            sumDTO.setEndTime(rebateVoucherDO.getEndTime());
            sumDTO.setVoucherStatus(rebateVoucherDO.getVoucherStatus());
            rollBackList.add(sumDTO);
        }
        // 按到期时间 倒叙排序
        Date date = new Date();
        // 可用的 和已用完的 代金券
        List<RollBackRebateSumDTO> rollBackList1 = rollBackList.stream()
            .filter(rollBackRebateSumDTO -> rollBackRebateSumDTO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_5)
                || rollBackRebateSumDTO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_9))
            .sorted(Comparator.comparing(RollBackRebateSumDTO::getEndTime)).collect(Collectors.toList());

        // 已冻结的代金券
        List<RollBackRebateSumDTO> rollBackList2 = rollBackList.stream()
            .filter(rollBackRebateSumDTO -> rollBackRebateSumDTO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_11))
            .sorted(Comparator.comparing(RollBackRebateSumDTO::getEndTime)).collect(Collectors.toList());

        // 已过期的代金券
        List<RollBackRebateSumDTO> rollBackList3 = rollBackList.stream()
            .filter(rollBackRebateSumDTO -> rollBackRebateSumDTO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_7))
            .sorted(Comparator.comparing(RollBackRebateSumDTO::getEndTime)).collect(Collectors.toList());

        // 先返还 可用 再返还冻结 最后返还过期
        List<RebateVoucherUsageRecordDO> list = new ArrayList<>();

        rollBackAmount = doRollbackAmount(rollBackList1, date, rollBackAmount, list);

        rollBackAmount = doRollbackAmount(rollBackList2, date, rollBackAmount, list);

        doRollbackAmount(rollBackList3, date, rollBackAmount, list);

        rebateVoucherUsageRecordMapper.batchInsert(list);
    }

    /**
     * 部分回退 具体回退逻辑
     * 
     * @param rollBackList1
     * @param date
     * @param rollBackAmount
     * @param list
     * @return
     */
    private BigDecimal doRollbackAmount(List<RollBackRebateSumDTO> rollBackList1, Date date, BigDecimal rollBackAmount,
        List<RebateVoucherUsageRecordDO> list) {
        if (CollectionUtils.isEmpty(rollBackList1)) {
            return rollBackAmount;
        }
        for (RollBackRebateSumDTO sumDTO : rollBackList1) {
            RebateVoucherUsageRecordDO recordDO = new RebateVoucherUsageRecordDO();
            recordDO.setRebateVoucherId(sumDTO.getRebateVoucherId());
            recordDO.setRebateVoucherNo(sumDTO.getRebateVoucherNo());
            recordDO.setUseTime(date);
            recordDO.setOrderId(sumDTO.getOrderId());
            recordDO.setOrderNo(sumDTO.getOrderNo());
            recordDO.setUseAmount(rollBackAmount);
            recordDO.setBalance(sumDTO.getBalance().add(rollBackAmount));
            // 正常来讲 sumDTO.getUseAmount()是能抵扣的最大额度 这地方是负数
            BigDecimal maximum = sumDTO.getUseAmount().negate();
            // 扣减后的结果 小于等于0 等于当前券可以返还
            BigDecimal subtract = rollBackAmount.subtract(maximum);
            if (subtract.compareTo(BigDecimal.ZERO) <= 0) {
                // 抵扣完了
                rollBackAmount = BigDecimal.ZERO;
            } else {
                // 大于0的话 表示当前券 一张券 返还不完
                rollBackAmount = subtract;
            }
            list.add(recordDO);
            RebateVoucherDO rebateVoucherDO = rebateVoucherMapper.selectByPrimaryKey(sumDTO.getRebateVoucherId());
            rebateVoucherDO.setBalance(recordDO.getBalance());
            rebateVoucherDO.setUpdateTime(date);
            // 已用完 可以转成 可用 或者已过期
            if (rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_9)) {
                if (date.before(rebateVoucherDO.getEndTime())) {
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_5);
                } else {
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_7);
                }
            }
            rebateVoucherMapper.updateByPrimaryKey(rebateVoucherDO);
            if (rollBackAmount.compareTo(BigDecimal.ZERO) <= 0) {
                // 已经退完 券可抵金额没有抵完 或者刚好 正数
                break;
            }
        }
        return rollBackAmount;
    }
}
