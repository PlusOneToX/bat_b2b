package com.bat.promotion.service.rebatevoucher;

import static com.bat.promotion.api.base.ErrorCode.SYSTEM_EXCEPTION;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.api.rebatevoucher.RebateVoucherService;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherCmd;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherListQry;
import com.bat.promotion.service.rebatevoucher.dto.RebateVoucherExcelDTO;
import com.bat.promotion.service.rebatevoucher.executor.RebateVoucherCmdExe;
import com.bat.promotion.service.rebatevoucher.executor.RebateVoucherQryExe;
import com.bat.promotion.service.rebatevoucher.executor.RebateVoucherRpcCmdExe;
import com.bat.promotion.service.rebatevoucher.validator.RebateVoucherValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherUsageRecordListQry;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherDTO;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherUsageRecordDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/12/31 15:38
 */
@Service
@Slf4j
public class RebateVoucherServiceImpl implements RebateVoucherService {

    @Resource
    private RebateVoucherCmdExe rebateVoucherCmdExe;

    @Resource
    private RebateVoucherQryExe rebateVoucherQryExe;

    @Value("${oss.excel.admin-import-rebatevoucher}")
    public String adminImportRebateVoucherUrl;

    @Resource
    private RebateVoucherRpcCmdExe rpcCmdExe;

    @Override
    public void createRebateVoucher(RebateVoucherCmd cmd, String userId, String userName) {
        rebateVoucherCmdExe.createRebateVoucher(cmd, userId, userName);
    }

    @Override
    public void batchCreateRebateVoucher(List<RebateVoucherCmd> cmds, String userId, String userName) {
        rebateVoucherCmdExe.batchCreateRebateVoucher(cmds, userId, userName);
    }

    @Override
    public PageInfo<RebateVoucherDTO> listRebateVoucher(RebateVoucherListQry qry) {
        return rebateVoucherQryExe.listRebateVoucher(qry);
    }

    @Override
    public void updateRebateVoucher(RebateVoucherCmd cmd) {
        rebateVoucherCmdExe.updateRebateVoucher(cmd);
    }

    @Override
    public PageInfo<RebateVoucherUsageRecordDTO> listRebateVoucherUsageRecord(RebateVoucherUsageRecordListQry qry) {
        return rebateVoucherQryExe.listRebateVoucherUsageRecord(qry);
    }

    @Override
    public BigDecimal getAvailableRebateVoucherBalanceSum(Integer distributorId) {
        return rebateVoucherQryExe.getAvailableRebateVoucherBalanceSum(distributorId);
    }

    @Override
    public String getTempUrl() {
        return adminImportRebateVoucherUrl;
    }

    @Override
    public void importRebateVoucher(InputStream inputStream, String userId, String userName) {
        List<RebateVoucherExcelDTO> rebateVoucherExcelDTOS = RebateVoucherValidator.analysisExcel(inputStream);
        rpcCmdExe.listDistributorIdByErpNos(rebateVoucherExcelDTOS);
        List<Integer> distributorIds = rebateVoucherExcelDTOS.stream().map(RebateVoucherExcelDTO::getDistributorId)
            .distinct().collect(Collectors.toList());
        Map<Integer, DistributorRpcDTO> distributorMap = rpcCmdExe.listDistributorInfo(distributorIds).stream()
            .collect(Collectors.toMap(DistributorRpcDTO::getId, k -> k));
        List<RebateVoucherCmd> collect = new ArrayList<>();
        for (int i = 0; i < rebateVoucherExcelDTOS.size(); i++) {
            RebateVoucherExcelDTO rebateVoucherExcelDTO = rebateVoucherExcelDTOS.get(i);
            RebateVoucherCmd cmd = new RebateVoucherCmd();
            cmd.setDistributorId(rebateVoucherExcelDTO.getDistributorId());
            if (distributorMap.get(cmd.getDistributorId()) != null) {
                cmd.setDistributorName(distributorMap.get(cmd.getDistributorId()).getName());
            } else {
                int lineNum = i + 2;
                String msg = "第{0}行，分销商 ERPNO{1},B2BID:{2}不可用";
                String format = MessageFormat.format(msg, lineNum, rebateVoucherExcelDTO.getErpDistributorNo(),
                    rebateVoucherExcelDTO.getDistributorId());
                log.error(msg);
                throw PromotionException.buildException(SYSTEM_EXCEPTION, format);
            }
            cmd.setFaceValue(rebateVoucherExcelDTO.getFaceValue());
            cmd.setName(rebateVoucherExcelDTO.getName());
            cmd.setStartTime(rebateVoucherExcelDTO.getStartTime());
            cmd.setEndTime(rebateVoucherExcelDTO.getEndTime());
            cmd.setRemark(rebateVoucherExcelDTO.getRemark());
            collect.add(cmd);
        }
        batchCreateRebateVoucher(collect, userId, userName);
    }
}
