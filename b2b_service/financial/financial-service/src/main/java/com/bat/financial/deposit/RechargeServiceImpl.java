package com.bat.financial.deposit;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.bat.financial.deposit.executor.RechargeCmdExc;
import com.bat.financial.deposit.executor.RechargeQryExc;
import com.bat.financial.pay.constant.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.financial.api.deposit.RechargeService;
import com.bat.financial.api.deposit.dto.DepositReChargeCreateCmd;
import com.bat.financial.api.deposit.dto.DepositReChargeQry;
import com.bat.financial.api.deposit.dto.data.PayCallBackDTO;
import com.bat.financial.api.pay.PayService;
import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.api.pay.constant.TradeMode;
import com.bat.financial.api.pay.dto.CreateTradeCmd;
import com.bat.financial.api.pay.dto.QueryTradeQry;
import com.bat.financial.api.pay.dto.data.CreateTradeRespDTO;
import com.bat.financial.api.pay.dto.data.QueryTradeRespDTO;
import com.bat.financial.common.CommonServiceImpl;
import com.bat.financial.pay.constant.*;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 14:57
 */
@Service
@Slf4j
public class RechargeServiceImpl implements RechargeService {

    @Resource
    private RechargeCmdExc rechargeCmdExc;

    @Resource
    private RechargeQryExc queryRechargeStatus;

    @Resource(name = "WxPayV3ServiceImpl")
    private PayService wxpayV3Service;

    @Resource(name = "AliPayServiceImpl")
    private PayService alipayService;

    @Resource
    private CommonServiceImpl commonService;

    @Override
    public CreateTradeRespDTO createRecharge(DepositReChargeCreateCmd cmd) {
        CreateTradeCmd cmd1 = new CreateTradeCmd();
        cmd1.setOrganizationId(commonService.getOrganizationId(cmd.getDistributorId()));
        cmd1.setCustomerFlag(CustomerFlag.IS_NOT_CUSTOMER);
        cmd1.setAmount(cmd.getAmount());
        cmd1.setDescription("B2B充值");
        cmd1.setTitle("B2B充值");
        cmd1.setBusinessType(BillsBusinessType.RECHARGE);
        cmd1.setOrderId(null);
        cmd1.setPayerId(cmd.getDistributorId());
        if (StringUtils.isBlank(cmd.getDistributorName())) {
            DistributorRpcDTO distributorInfo = commonService.getDistributorInfo(cmd.getDistributorId());
            cmd1.setPayerName(distributorInfo.getName());
        }
        cmd1.setTradeMode(TradeMode.PLATFORM);
        if (cmd.getPayWay() == PayWay.WXPAY) {
            cmd1.setPayChannel(PayChannel.WXPAY_V3.name());
            cmd1.setPayMethod(PayChannel.WXPAY_NATIVE.name());
            return wxpayV3Service.createTrade(cmd1);
        } else if (cmd.getPayWay() == PayWay.ALIPAY) {
            cmd1.setPayChannel(PayChannel.ALIPAY.name());
            cmd1.setPayMethod(PayChannel.ALIPAY_FACE_TO_FACE.name());
            return alipayService.createTrade(cmd1);
        }
        return null;
    }

    @Override
    public boolean updateRecharge(PayCallBackDTO payCallBackDTO) {
        Integer distributorId = payCallBackDTO.getDistributorId();
        BigDecimal rechargeAmount = payCallBackDTO.getAmount();
        log.info("接收充值回调事件：{} 充值了 {} 元", distributorId, rechargeAmount);
        return rechargeCmdExc.updateRecharge(payCallBackDTO);
    }

    @Override
    public Boolean queryRechargeStatus(DepositReChargeQry qry) {
        // 先直接查询数据库 看凭证状态有没有已支付，没有在调api查询
        boolean payStatus = queryRechargeStatus.queryRechargeStatus(qry);
        if (payStatus) {
            return true;
        }
        QueryTradeQry qry1 = new QueryTradeQry();
        qry1.setOrganizationId(commonService.getOrganizationId(qry.getDistributorId()));
        qry1.setOutTradeNo(qry.getOutTradeNo());
        qry1.setCustomerFlag(qry.getCustomerFlag());
        try {
            if (qry.getPayWay() == PayWay.WXPAY) {
                qry1.setPayChannel(PayChannel.WXPAY_V3.name());
                qry1.setPayMethod(PayChannel.WXPAY_NATIVE.name());
                QueryTradeRespDTO queryTradeRespDTO = wxpayV3Service.queryTrade(qry1);
                return WxPayReturnCode.SUCCESS.equals(queryTradeRespDTO.getWxResp().getTradeState());
            } else if (qry.getPayWay() == PayWay.ALIPAY) {
                qry1.setPayChannel(PayChannel.ALIPAY.name());
                qry1.setPayMethod(PayChannel.ALIPAY_FACE_TO_FACE.name());
                QueryTradeRespDTO queryTradeRespDTO = alipayService.queryTrade(qry1);
                return queryTradeRespDTO.getAlipayResp().getAlipayTradeQueryResponse().getTradeStatus()
                    .equals(TradeStatus.AliPayTradeState.TRADE_SUCCESS);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
