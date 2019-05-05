package com.bat.thirdparty.financial.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bat.thirdparty.common.ThirdKeyConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.erp.api.request.CreateRefundBillRequest;
import com.bat.thirdparty.erp.api.response.CreateRefundBillResponse;
import com.bat.thirdparty.erp.service.ThirdPartyFinancialServiceErpRpcImpl;
import com.bat.thirdparty.erp.service.executor.ErrorCode;
import com.bat.thirdparty.erp.service.executor.FinancialErpCmdExc;
import com.bat.thirdparty.message.service.MessageSendService;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.financial.refund.api.FinancialRefundServiceRpc;
import com.bat.dubboapi.financial.refund.dto.data.ErpRefundBillDetailsDTO;
import com.bat.dubboapi.financial.refund.dto.data.RefundBillSyncDTO;
import com.bat.dubboapi.financial.voucher.api.FinancialVoucherServiceRpc;
import com.bat.dubboapi.financial.voucher.dto.data.CreateReceiveBillEntryReq;
import com.bat.dubboapi.financial.voucher.dto.data.CreateReceiveBillEntryResp;
import com.bat.dubboapi.financial.voucher.dto.data.ErpVoucherDetailsDTO;
import com.bat.dubboapi.financial.voucher.dto.data.ReceiveBillEntry;
import com.bat.thirdparty.financial.api.AdminFinancialServiceI;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/7/17 20:02
 */
@Service
@Slf4j
public class AdminFinancialServiceImpl implements AdminFinancialServiceI {

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private FinancialVoucherServiceRpc financialVoucherServiceRpc;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private FinancialRefundServiceRpc financialRefundServiceRpc;

    @Resource
    private ThirdPartyFinancialServiceErpRpcImpl thirdPartyFinancialServiceErpRpc;

    @Resource
    private FinancialErpCmdExc financialErpCmdExc;

    @Resource
    private MessageSendService messageSendService;

    @Resource
    private HttpServletRequest request;

    @CreateCache(name = ThirdKeyConstant.VOUCHER_SYNC_ERP_PRE)
    private Cache<String, Integer> financialSyncErpCache;

    /**
     * 财务 ERP 两边数据接口情况都可能出现问题，所以才有这么多判断
     * 
     * @param ids
     * @return
     */
    @Override
    public Response syncVouchersToERP(List<Integer> ids) {
        // 处理日志
        log.info("开始同步收款单：收款单号ids:{}", ids);
        Date date = new Date();
        Response<List<CreateReceiveBillEntryReq>> listResponse = financialVoucherServiceRpc.listErpVoucherById(ids);
        if (listResponse.isSuccess()) {
            List<CreateReceiveBillEntryReq> listResponseData = listResponse.getData();
            log.info("从财务服务获取数据成功 :{}", listResponseData);
            if (CollectionUtils.isEmpty(listResponseData)) {
                return Response.buildFailure(ErrorCode.B_ERP_SYNC_VOUCHER_REQUEST_ERROR,
                    MessageUtils.get(ErrorCode.B_ERP_SYNC_VOUCHER_REQUEST_ERROR));
            }
            // 同步ERP
            List<ErpVoucherDetailsDTO> collect = syncErp(ids, date, listResponseData);
            log.info("待同步回财务的集合：{}", collect);
            if (!CollectionUtils.isEmpty(collect)) {
                Response response = financialVoucherServiceRpc.updateVouchers(collect);
                if (response.isSuccess()) {
                    log.info("更新财务数据成功");
                    return Response.buildSuccess();
                } else {
                    log.error("更新出错");
                    throw ThirdPartyException.buildException(ErrorCode.B_ERP_SYNC_VOUCHER_RESPONSE_ERROR);
                }
            } else {
                log.error("待同步回财务的集合 为空集合");
                throw ThirdPartyException.buildException(ErrorCode.B_ERP_SYNC_VOUCHER_RESPONSE_ERROR);
            }
        } else {
            log.error("erp 获取同步数据出错");
            throw ThirdPartyException.buildException(ErrorCode.B_ERP_SYNC_VOUCHER_REQUEST_ERROR);
        }
    }

    private List<ErpVoucherDetailsDTO> syncErp(List<Integer> ids, Date date,
        List<CreateReceiveBillEntryReq> listResponseData) {
        List<ErpVoucherDetailsDTO> responseList = new ArrayList<>();
        for (CreateReceiveBillEntryReq listResponseDatum : listResponseData) {
            Integer integer = Integer.valueOf(listResponseDatum.getFB2B_BILLNO());
            // 分布式锁
            AutoReleaseLock autoReleaseLock = financialSyncErpCache
                .tryLock(TenantContext.getTenantNo() + ":" + String.valueOf(integer), 30, TimeUnit.MINUTES);
            if (autoReleaseLock == null) {
                // 加锁
                throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_REPEAT);
            }
            String b2bId = null;
            String logResult = null;
            try {
                List<ReceiveBillEntry> receiveBillEntryDetail = listResponseDatum.getReceiveBillEntryDetail();
                if (CollectionUtils.isEmpty(receiveBillEntryDetail)) {
                    log.info("收款单：{} 所对应行项目为空，跳过循环", listResponseDatum.getFB2B_BILLNO());
                    continue;
                }
                b2bId = listResponseDatum.getFB2B_BILLNO();
                log.info("开始同步 id:{} 具体信息：createReceiveBillEntryRequest：{}", b2bId, listResponseDatum);
                com.bat.dubboapi.thirdparty.common.Response<CreateReceiveBillEntryResp> voucher =
                    thirdPartyFinancialServiceErpRpc.syncVoucher(listResponseDatum);
                ErpVoucherDetailsDTO dto = new ErpVoucherDetailsDTO();
                logResult = "同步失败";
                if (voucher.isSuccess()) {
                    CreateReceiveBillEntryResp data = voucher.getData();
                    String voucherNo = data.getData();
                    if (StringUtils.isNotBlank(voucherNo) && !"null".equals(voucherNo)) {
                        log.info("同步 id:{} 成功。返回ERP 销售单号：{}", b2bId, data.getData());
                        logResult = "erp同步收款单成功:" + data.getData();
                        dto.setOrderNo(b2bId);
                        dto.setVoucherNo(data.getData());
                        responseList.add(dto);
                    } else {
                        log.info("同步 id:{} 失败。返回ERP 销售单号：{}", b2bId, data.getMessage());
                        logResult = "同步失败：" + data.getMessage();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                financialSyncErpCache.remove(TenantContext.getTenantNo() + ":" + String.valueOf(integer));
                autoReleaseLock.close();
            }
            sendVoucherLogMsg(ids, date, b2bId, logResult);
        }
        List<ErpVoucherDetailsDTO> collect = responseList.stream()
            .filter(erpVoucherDetailsDTO -> erpVoucherDetailsDTO.getVoucherNo() != null).collect(Collectors.toList());
        return collect;
    }

    private void sendVoucherLogMsg(List<Integer> ids, Date date, String b2bId, String logResult) {
        try {
            messageSendService.voucherLogPackage(null, Integer.valueOf(b2bId), "收款单同步到erp", logResult, ids.toString(),
                date);
        } catch (Exception e) {
            log.error("记录收款单日志出现异常:{}");
        }
    }

    private void sendRefundLogMsg(Integer refundId, String b2bId, String logResult) {
        try {
            messageSendService.refundLogPackage(refundId, Integer.valueOf(b2bId), "收款单同步到erp", logResult, null);
        } catch (Exception e) {
            log.error("记录收款单日志出现异常:{}");
        }
    }

    @Override
    public Response syncRefundBillToErp(RefundBillSyncDTO dto) {
        if (dto.getRefundBillReq() == null) {
            log.info("没有数据，通过id 从财务获取同步数据");
            Response<RefundBillSyncDTO> billReqResponse = financialRefundServiceRpc.listErpRefundBillById(dto.getId());
            if (billReqResponse.isSuccess()) {
                dto = billReqResponse.getData();
                log.info("获取成功：{}", dto);
            } else {
                sendRefundLogMsg(dto.getId(), null, "从财务获取同步数据出错:" + billReqResponse.getErrMessage());
                log.info("获取失败：code{},msg:{}", billReqResponse.getErrCode(), billReqResponse.getErrMessage());
                return billReqResponse;
            }
        } else {
            log.info("有数据直接拼装 同步");
        }
        // 变量经常丢失 可能是DUBBO 序列化的问题，但是收款单没有问题
        if (dto.getRefundBillReq().getFB2B_BILLNO() == null) {
            dto.getRefundBillReq().setFB2B_BILLNO(String.valueOf(dto.getId()));
        }
        log.info("同步信息：id:{},refundBillReq:{}", dto.getId(), dto.getRefundBillReq());
        CreateRefundBillRequest request =
            new CreateRefundBillRequest();
        BeanUtils.copyProperties(dto.getRefundBillReq(), request);
        CreateRefundBillResponse createRefundBillResponse = financialErpCmdExc.syncRefundBill(request);
        log.info("同步结果：{}", createRefundBillResponse);
        // sendRefundLogMsg(dto.getId(), null, "从财务获取同步数据出错");
        if (createRefundBillResponse.getData() != null) {
            ErpRefundBillDetailsDTO detailsDTO = new ErpRefundBillDetailsDTO();
            detailsDTO.setRefundId(dto.getId() + "");
            detailsDTO.setRefundBillNo(createRefundBillResponse.getData());
            Response response = financialRefundServiceRpc.updateRefund(detailsDTO);
            if (response.isSuccess()) {
                log.info("更新退款单成功");
                sendRefundLogMsg(dto.getId(), null, "从ERP到财务同步成功!!!");
                return response;
            } else {
                log.info("更新退款单失败");
                sendRefundLogMsg(dto.getId(), null, "从ERP到财务同步失败：" + response.getErrMessage());
                return response;
            }
        } else {
            log.info("同步失败:{}", createRefundBillResponse.getMessage());
            sendRefundLogMsg(dto.getId(), null, "同步erp失败: " + createRefundBillResponse.getMessage());
            return Response.buildFailure(ErrorCode.B_ERP_SYNC_VOUCHER_ERROR, createRefundBillResponse.getMessage());
        }
    }
}
