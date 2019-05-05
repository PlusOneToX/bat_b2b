package com.bat.promotion.service.rebatevoucher.executor;

import static com.bat.promotion.api.base.ErrorCode.SYSTEM_EXCEPTION;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.dao.rebatevoucher.RebateVoucherMapper;
import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherDO;
import com.bat.promotion.service.common.CommonUtils;
import com.bat.promotion.service.common.Constant;
import com.bat.promotion.service.common.PromotionConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorExtendDataServiceRpc;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorExtendDataRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.thirdparty.xxljob.api.XxlJobServiceRpc;
import com.bat.dubboapi.thirdparty.xxljob.dto.XxlJobRpcCmd;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.service.rebatevoucher.dto.RebateVoucherExcelDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/12/31 15:38
 */
@Component
@Slf4j
public class RebateVoucherRpcCmdExe {

    @Resource
    private PromotionConfig promotionConfig;

    @DubboReference(check = false, timeout = 30000)
    private XxlJobServiceRpc xxlJobServiceRpc;

    @DubboReference(check = false, timeout = 30000)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 30000)
    private DistributorExtendDataServiceRpc distributorExtendDataServiceRpc;

    @Resource
    private RebateVoucherMapper rebateVoucherMapper;

    @Resource
    private RebateVoucherCmdExe rebateVoucherCmdExe;

    @Resource
    private PromotionConfig config;

    /**
     * 返利代金券定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    public void rebateVoucherStartStop() {
        Date time = CommonUtils.get24Time();
        List<RebateVoucherDO> rebateVoucherDOS = rebateVoucherMapper.listRebateVoucherByTime(time);
        if (!CollectionUtils.isEmpty(rebateVoucherDOS)) {
            rebateVoucherAddXxlJob(rebateVoucherDOS);
        }
    }

    /**
     * 创建返利代金券定时任务
     *
     * @param rebateVoucherDO
     */
    public void rebateVoucherAddXxlJob(RebateVoucherDO rebateVoucherDO) throws ParseException {
        List<XxlJobRpcCmd> cmds = new ArrayList<>();
        // 获取当天最后时刻
        long time = CommonUtils.getTodayEndTime();
        // 创建活动开始定时任务
        if (rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_4)
            && rebateVoucherDO.getStartTime().getTime() < time) {
            cmds.add(getXxlJobRpcCmd(rebateVoucherDO, promotionConfig.getRebateVoucherStartJobHandler(),
                rebateVoucherDO.getStartTime()));
        }
        // 创建活动结束定时任务
        // 待生效
        boolean flag1 = rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_4);
        // 可用
        boolean flag2 = rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_5);
        // 已冻结 审核通过后 在未到结束时间之前被冻结的，如果超出有效期，则自动变成已过期
        boolean flag3 = rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_11);
        if ((flag1 || flag2 || flag3) && rebateVoucherDO.getEndTime().getTime() < time) {
            cmds.add(getXxlJobRpcCmd(rebateVoucherDO, promotionConfig.getRebateVoucherStopJobHandler(),
                rebateVoucherDO.getEndTime()));
        }
        if (!CollectionUtils.isEmpty(cmds)) {
            xxlJobServiceRpc.xxlJobAdd(cmds);
        }
    }

    private void rebateVoucherAddXxlJob(List<RebateVoucherDO> rebateVoucherDOS) {
        List<XxlJobRpcCmd> cmds = new ArrayList<>();
        List<Integer> startIds = new ArrayList<>();
        List<Integer> stopIds = new ArrayList<>();
        long time = System.currentTimeMillis();
        if (!CollectionUtils.isEmpty(rebateVoucherDOS)) {
            rebateVoucherDOS.forEach(rebateVoucherDO -> {
                // 判断活动开始
                if (rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_4)
                    && rebateVoucherDO.getStartTime().getTime() < time + config.getIntervalTime()) {
                    startIds.add(rebateVoucherDO.getId());
                } else if (rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_4)) {
                    cmds.add(getXxlJobRpcCmd(rebateVoucherDO, promotionConfig.getRebateVoucherStartJobHandler(),
                        rebateVoucherDO.getStartTime()));
                }
                // 判断活动结束
                if (rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_5)
                    && rebateVoucherDO.getEndTime().getTime() < time + config.getIntervalTime()) {
                    stopIds.add(rebateVoucherDO.getId());
                } else if (rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_5)) {
                    cmds.add(getXxlJobRpcCmd(rebateVoucherDO, promotionConfig.getRebateVoucherStopJobHandler(),
                        rebateVoucherDO.getEndTime()));
                }
            });
        }
        if (!CollectionUtils.isEmpty(startIds)) {
            rebateVoucherCmdExe.updateListRebateVoucherStatus(startIds, Constant.REBATE_VOUCHER_STATUS_5);
        }
        if (!CollectionUtils.isEmpty(stopIds)) {
            rebateVoucherCmdExe.updateListRebateVoucherStatus(stopIds, Constant.REBATE_VOUCHER_STATUS_7);
        }
        if (!CollectionUtils.isEmpty(cmds)) {
            xxlJobServiceRpc.xxlJobAdd(cmds);
        }
    }

    private XxlJobRpcCmd getXxlJobRpcCmd(RebateVoucherDO rebateVoucherDO, String jobHandler, Date item) {
        XxlJobRpcCmd cmd = new XxlJobRpcCmd();
        cmd.setJobGroup(promotionConfig.getJobGroup());
        cmd.setAuthor(promotionConfig.getAuthor());
        cmd.setExecutorHandler(jobHandler);
        cmd.setScheduleConf(CommonUtils.getCronByDate(item));
        cmd.setExecutorParam(String.valueOf(rebateVoucherDO.getId()));
        cmd.setJobDesc(rebateVoucherDO.getName() + rebateVoucherDO.getId());
        return cmd;
    }

    public void listDistributorIdByErpNos(List<RebateVoucherExcelDTO> list) {
        List<String> erpNos = list.stream().map(RebateVoucherExcelDTO::getErpDistributorNo).distinct()
            .filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(erpNos)) {
            return;
        }
        Response<List<DistributorExtendDataRpcDTO>> listResponse =
            distributorExtendDataServiceRpc.listAvailableByErpNos(erpNos);
        if (!listResponse.isSuccess()) {
            throw PromotionException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
        Map<String, DistributorExtendDataRpcDTO> erpMaps = listResponse.getData().stream()
            .collect(Collectors.toMap(DistributorExtendDataRpcDTO::getErpNo, k -> k, (t, t2) -> {
                log.info("分销商代金券导入 出现重复项,ERP_NO:{},t:{},t2:{}", t.getErpNo(), t.getDistributorId(),
                    t2.getDistributorId());
                return t;
            }));
        for (int i = 0; i < list.size(); i++) {
            RebateVoucherExcelDTO rebateVoucherExcelDTO = list.get(i);
            if (StringUtils.isNotBlank(rebateVoucherExcelDTO.getErpDistributorNo())) {
                DistributorExtendDataRpcDTO rpcDTO = erpMaps.get(rebateVoucherExcelDTO.getErpDistributorNo());
                if (rpcDTO != null) {
                    rebateVoucherExcelDTO.setDistributorId(rpcDTO.getDistributorId());
                }else{
                    if(rebateVoucherExcelDTO.getDistributorId()==null){
                        int lineNum = i+2;
                        String msg= "第{0}行，在B2B编码为空的情况下，分销商编码查询不到可用分销商";
                        String format = MessageFormat.format(msg, lineNum);
                        log.error(msg);
                        throw PromotionException.buildException(SYSTEM_EXCEPTION, format);
                    }
                }
            }
        }
    }

    public List<DistributorRpcDTO> listDistributorInfo(List<Integer> ids) {
        Response<List<DistributorRpcDTO>> listResponse = distributorServiceRpc.distributorByIds(ids);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        }
        return new ArrayList<>();
    }

}
