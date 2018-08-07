package com.bat.financial.voucher.executor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.financial.voucher.dto.data.CreateReceiveBillEntryReq;
import com.bat.dubboapi.financial.voucher.dto.data.ReceiveBillEntry;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderVoucherErpDTO;
import com.bat.dubboapi.system.organization.dto.data.OrganizationRpcDTO;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.base.MessageUtils;
import com.bat.financial.api.common.dto.BaseBillEntity;
import com.bat.financial.api.voucher.dto.data.OrderVoucherDTO;
import com.bat.financial.common.CommonServiceImpl;
import com.bat.financial.common.constant.CounterpartyType;
import com.bat.financial.dao.pay.PayBillsCustomerMapper;
import com.bat.financial.dao.pay.PayBillsDistributorMapper;
import com.bat.financial.dao.pay.dataobject.PayBillsDO;
import com.bat.financial.dao.voucher.VoucherDistributorMapper;
import com.bat.financial.dao.voucher.dataobject.VoucherDistributorDO;
import com.bat.financial.message.MessageSendService;
import com.bat.financial.pay.constant.BillsBusinessType;
import com.bat.financial.pay.constant.CustomerFlag;
import com.bat.financial.voucher.constant.VoucherStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/17 23:44
 */
@Component
@Slf4j
public class VoucherCmdExc {

    @Resource
    private MessageSendService messageSendService;

    @Resource
    private VoucherDistributorMapper voucherDistributorMapper;

    @Resource
    private CommonServiceImpl commonService;

    @Resource
    private PayBillsCustomerMapper payBillsCustomerMapper;

    @Resource
    private PayBillsDistributorMapper payBillsDistributorMapper;

    @Resource
    private MessageSendService sendService;

    @Resource
    private HttpServletRequest request;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private OrderServiceRpc orderServiceRpc;

    public void updateVoucher(VoucherDistributorDO voucherById) {
        voucherDistributorMapper.updateByPrimaryKey(voucherById);
        if (StringUtils.isNotBlank(voucherById.getBusinessId())) {
            String[] strArray = voucherById.getBusinessId().split(",");
            for (int i = 0; i < strArray.length; i++) {
                messageSendService.voucherLogPackage(voucherById.getId(), Integer.valueOf(strArray[i]), "更新收款单信息",
                    "更新成功", JSONObject.toJSONString(voucherById));
            }
        }
    }

    public void createVoucher(OrderVoucherDTO orderVoucherDTO) {
        VoucherDistributorDO aDo = new VoucherDistributorDO();
        aDo.setDistributorId(orderVoucherDTO.getDistributorId());
        aDo.setDistributorName(orderVoucherDTO.getDistributorName());
        aDo.setCompanyName(orderVoucherDTO.getCompanyName());
        Short counterpartyType = orderVoucherDTO.getCounterpartyType();
        PayBillsDO aDo1 = null;
        if (counterpartyType.equals(CounterpartyType.CUSTOMER)) {
            aDo1 = payBillsCustomerMapper.getByOutTradeNo(orderVoucherDTO.getOutTradeNo());
            aDo.setCustomerFlag(CustomerFlag.IS_CUSTOMER);
        } else if (counterpartyType.equals(CounterpartyType.DISTRIBUTOR)) {
            aDo1 = payBillsDistributorMapper.getByOutTradeNo(orderVoucherDTO.getOutTradeNo());
            aDo.setCustomerFlag(CustomerFlag.IS_NOT_CUSTOMER);
        }
        if (aDo1 == null) {
            throw FinancialException.buildException(ErrorCode.B_PAY_BILLS_NULL);
        }
        // 如果有实际收款金额使用实际收款金额 同步ERP
        if (aDo1.getReceiptAmount() != null && aDo1.getReceiptAmount().compareTo(BigDecimal.ZERO) != 0) {
            aDo.setAmount(aDo1.getReceiptAmount());
        } else {
            aDo.setAmount(aDo1.getTotalFee());
        }
        aDo.setPayWay(aDo1.getPayType());
        aDo.setCurrencyType(orderVoucherDTO.getCurrencyType());
        aDo.setBusinessType(aDo1.getBusinessType());
        aDo.setBusinessId(aDo1.getBusinessId());
        aDo.setOutTradeNo(orderVoucherDTO.getOutTradeNo());
        aDo.setVoucherStatus(VoucherStatus.UNCONFIRMED);
        aDo.setRemark(null);
        aDo.setVoucherErpNo(null);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        try {
            voucherDistributorMapper.insert(aDo);
            if (StringUtils.isNotBlank(aDo.getBusinessId())) {
                String[] strArray = aDo.getBusinessId().split(",");
                for (int i = 0; i < strArray.length; i++) {
                    sendService.voucherLogPackage(aDo.getId(), Integer.valueOf(strArray[i]), "收款单生成", "创建成功",
                        JSONObject.toJSONString(aDo));
                }
            }

        } catch (DuplicateKeyException e) {
            final String localizedMessage = e.getLocalizedMessage();
            if (localizedMessage.contains("Duplicate entry") && localizedMessage.contains("uk_out_trade_no")) {
                throw FinancialException.buildException(ErrorCode.B_VOUCHER_IS_EXISTS);
            }
        }
    }

    public void createVoucher(VoucherDistributorDO aDo) {
        try {
            voucherDistributorMapper.insert(aDo);
        } catch (DuplicateKeyException e) {
            final String localizedMessage = e.getLocalizedMessage();
            if (localizedMessage.contains("Duplicate entry") && localizedMessage.contains("uk_out_trade_no")) {
                throw FinancialException.buildException(ErrorCode.B_VOUCHER_IS_EXISTS);
            }
        }
    }

    /**
     * 组装收款单
     *
     * @param voucherDistributorDO
     * @return
     */
    public CreateReceiveBillEntryReq getCreateReceiveBillEntryRequest(VoucherDistributorDO voucherDistributorDO) {
        // 分销商信息
        DistributorRpcDTO distributorInfo = commonService.getDistributorInfo(voucherDistributorDO.getDistributorId());
        // 业务员所属组织信息
        OrganizationRpcDTO organizationRpcDTO = commonService.getOrganizationInfoBySaleId(distributorInfo.getSalesId());
        CreateReceiveBillEntryReq request = new CreateReceiveBillEntryReq();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String outTradeNo = voucherDistributorDO.getOutTradeNo();
        PayBillsDO byOutTradeNo = payBillsCustomerMapper.getByOutTradeNo(outTradeNo);
        if (byOutTradeNo == null) {
            byOutTradeNo = payBillsDistributorMapper.getByOutTradeNo(outTradeNo);
        }
        if (byOutTradeNo == null) {
            log.error("outTradeNo 对应的支付凭证不存在");
            throw FinancialException.buildException(ErrorCode.B_PAY_BILLS_NULL,
                MessageUtils.get(ErrorCode.B_PAY_BILLS_NULL));
        }
        request.setFDATE(ft.format(byOutTradeNo.getPayTime()));
        request.setFPAYORGID(organizationRpcDTO.getErpOrganizationId());
        request.setFSETTLEORGID(organizationRpcDTO.getErpOrganizationId());
        request.setFCONTACTUNIT(distributorInfo.getErpNo());
        request.setFPAYUNIT(distributorInfo.getErpNo());
        request.setFB2B_BILLNO(voucherDistributorDO.getId() + "");
        // 订单收款单 组装
        List<ReceiveBillEntry> receiveBillEntries = creatReceiveBillEntrys(voucherDistributorDO, organizationRpcDTO);
        request.setReceiveBillEntryDetail(receiveBillEntries);
        return request;

    }

    /**
     * 组装明细
     *
     * @return
     */
    private List<ReceiveBillEntry> creatReceiveBillEntrys(VoucherDistributorDO voucherDistributorDO,
        OrganizationRpcDTO organizationRpcDTO) {
        // 明细
        List<ReceiveBillEntry> billEntryList = new ArrayList<>();
        if (voucherDistributorDO.getBusinessType() == BillsBusinessType.ORDER) {
            // TODO 多订单id
            String businessId = voucherDistributorDO.getBusinessId();
            if (StringUtils.isNotBlank(businessId)) {
                List<Integer> orderIds =
                    Arrays.stream(businessId.split(",")).map(Integer::valueOf).collect(Collectors.toList());
                Response<List<OrderVoucherErpDTO>> listResponse = orderServiceRpc.orderVoucherErp(orderIds);
                if (listResponse.isSuccess()) {
                    List<OrderVoucherErpDTO> data = listResponse.getData();
                    boolean b = data.stream()
                        .allMatch(orderVoucherErpDTO -> StringUtils.isNotBlank(orderVoucherErpDTO.getOrderErpNo()));
                    if (b) {
                        data.forEach(orderVoucherErpDTO -> {
                            ReceiveBillEntry entry = new ReceiveBillEntry();
                            BaseBillEntity entity = commonService.getEntity(organizationRpcDTO.getId(),
                                voucherDistributorDO.getPayWay(), voucherDistributorDO.getOutTradeNo());
                            entry.setFACCOUNTID(entity.getFACCOUNTID());
                            entry.setFSETTLETYPEID(entity.getFSETTLETYPEID());
                            entry.setFRECEIVEITEM(orderVoucherErpDTO.getOrderErpNo());
                            entry.setFRECTOTALAMOUNTFOR(orderVoucherErpDTO.getPaidAmount().doubleValue());
                            billEntryList.add(entry);
                        });
                    } else {
                        log.error("data:{}", data);
                        log.error("该收款单对应的订单，没有全部同步ERP，请先同步ERP 且订单为已确认（已审核）");
                        throw FinancialException.buildException(ErrorCode.B_VOUCHER_CORRESPONDING_ORDER_NOT_SYNC_ERP,
                            MessageUtils.get(ErrorCode.B_VOUCHER_CORRESPONDING_ORDER_NOT_SYNC_ERP));
                    }
                }
            } else {
                log.error("同步订单收款单时 业务id为空");
                throw FinancialException.buildException(ErrorCode.B_VOUCHER_SYNC_ORDER_BUSINESS_ID_NULL,
                    MessageUtils.get(ErrorCode.B_VOUCHER_SYNC_ORDER_BUSINESS_ID_NULL));
            }
        } else if (voucherDistributorDO.getBusinessType() == BillsBusinessType.RECHARGE) {
            ReceiveBillEntry entry = new ReceiveBillEntry();
            BaseBillEntity entity = commonService.getEntity(organizationRpcDTO.getId(),
                voucherDistributorDO.getPayWay(), voucherDistributorDO.getOutTradeNo());
            entry.setFACCOUNTID(entity.getFACCOUNTID());
            entry.setFSETTLETYPEID(entity.getFSETTLETYPEID());
            entry.setFRECEIVEITEMTYPE((byte)2);
            entry.setFRECTOTALAMOUNTFOR(voucherDistributorDO.getAmount().doubleValue());
            billEntryList.add(entry);
        }
        return billEntryList;
    }
}
