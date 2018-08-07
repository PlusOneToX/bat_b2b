package com.bat.financial.pay.convertor;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.api.pay.dto.wechat.*;
import com.bat.financial.common.constant.subaccount.OrderSubAccountConstant;
import com.bat.financial.common.constant.subaccount.SubAccountTypeEnum;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountBillDO;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountDO;
import com.bat.financial.subaccount.executor.OrderSubAccountBillCmdExe;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.subaccount.DistributorSubAccountSalemanServiceRpc;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountSalemanRpcDTOQry;
import com.bat.dubboapi.distributor.wx.api.WxPlatformServiceRpc;
import com.bat.dubboapi.distributor.wx.dto.WxPlatformRpcDTO;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.common.CommonService;
import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.api.pay.constant.TradeMode;
import com.bat.financial.api.pay.dto.CreateTradeCmd;
import com.bat.financial.api.subaccount.dto.WechatPaySubAccountCmd;
import com.bat.financial.api.subaccount.dto.WechatPaySubAccountReceiverCmd;
import com.bat.financial.pay.data.WxConfig;
import com.bat.financial.pay.executor.ErrorCode;
import com.bat.financial.pay.utils.DateUtils;
import com.bat.financial.pay.utils.TradeNoUtils;

import static com.bat.financial.common.error.subaccount.SubAccountErrorCode.ORDER_SUB_ACCOUNT_BILL_SALE_MAN_ID_NULL;

@Component
public class WxPayConvertor {

    @Value("${wechat.notify_url_create_partner}")
    private String notifyUrlWechatCreatePartner;

    @DubboReference(check = false, timeout = 30000)
    private WxPlatformServiceRpc wxPlatformServiceRpc;

    @DubboReference(check = false, timeout = 30000)
    private DistributorSubAccountSalemanServiceRpc distributorSubAccountSalemanServiceRpc;

    @Resource
    private CommonService commonService;

    @Autowired
    private OrderSubAccountBillCmdExe orderSubAccountBillCmdExe;

    /**
     * 转换为微信服务商统一下单参数
     * 
     * @param cmd
     * @param date
     * @return
     */
    public WxPayPartnerCreateOrderCmd toWxPayPartnerCreateOrder(CreateTradeCmd cmd, WxConfig wxConfig, Date date) {
        WxPayPartnerCreateOrderCmd createOrderCmd = new WxPayPartnerCreateOrderCmd();
        // 预先设置 后面可能会替换
        createOrderCmd.setSp_appid(cmd.getAppId());
        createOrderCmd.setSp_mchid(wxConfig.getAccountNo());
        createOrderCmd.setSub_mchid(wxConfig.getSubMchid());
        createOrderCmd.setDescription(cmd.getDescription());
        // 加上组织id 是否C端回调时选择正确配置
        // 获取租户主机域名
        String host = commonService.getHostByTenantNo(TenantContext.getTenantNo());
        String notifyUrl = "https://" + host + notifyUrlWechatCreatePartner + "/" + cmd.getTradeMode() + "/"
            + cmd.getAppType() + "/" + cmd.getCustomerFlag() + "/";
        if (cmd.getTradeMode().equals(TradeMode.PLATFORM)) {
            notifyUrl += cmd.getOrganizationId();
        } else if (cmd.getTradeMode().equals(TradeMode.DISTRIBUTOR_SELF)
            || cmd.getTradeMode().equals(TradeMode.DISTRIBUTOR_SUPERIOR)) {
            notifyUrl += cmd.getPayeeId();
        }
        // 拼接租户编码
        notifyUrl = notifyUrl + "/" + TenantContext.getTenantNo();
        createOrderCmd.setNotify_url(notifyUrl);
        createOrderCmd.setOut_trade_no(TradeNoUtils.getCreateTradeNo("wx", cmd.getCustomerFlag()));
        BigDecimal hundred = new BigDecimal("100");
        WxPayAmount amount = new WxPayAmount();
        amount.setCurrency("CNY");
        amount.setTotal(cmd.getAmount().multiply(hundred).intValue());
        createOrderCmd.setAmount(amount);
        WxPaySettleInfo settleInfo = new WxPaySettleInfo();
        settleInfo.setProfit_sharing(true);
        createOrderCmd.setSettle_info(settleInfo);

        WxPaySceneInfo sceneInfo = new WxPaySceneInfo();
        try {
            sceneInfo.setPayer_client_ip(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        createOrderCmd.setScene_info(sceneInfo);
        if (PayChannel.WXPAY_PARTNER_JSAPI.name().equals(cmd.getPayChannel())) {
            if (StringUtils.isBlank(cmd.getPlatformUserId())) {
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_PAYER_NULL);
            }
            WxPayPayer wxPayPayer = new WxPayPayer();
            wxPayPayer.setSp_openid(cmd.getPlatformUserId());
            createOrderCmd.setPayer(wxPayPayer);
        }
        try {
            Response<WxPlatformRpcDTO> miniProgramAuthorize = wxPlatformServiceRpc.getMiniProgramAuthorize();
            if (miniProgramAuthorize.isSuccess()) {
                WxPlatformRpcDTO data = miniProgramAuthorize.getData();
                // 如果当前appid 和 小程序appid(服务商appid) 不一致。则判断为子商户appid
                if (StringUtils.isNotBlank(data.getAppId()) && !data.getAppId().equals(wxConfig.getAppId())) {
                    // 前端传的其实是 子商户appid 以及 对应的openid
                    createOrderCmd.setSub_appid(cmd.getAppId());
                    createOrderCmd.getPayer().setSub_openid(createOrderCmd.getPayer().getSp_openid());
                    createOrderCmd.getPayer().setSp_openid(null);
                    createOrderCmd.setSp_appid(data.getAppId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        createOrderCmd.setTime_expire(DateUtils.getTimeExpire(date, PayChannel.WXPAY_PARTNER_JSAPI.name()));
        return createOrderCmd;
    }

    /**
     * 组装微信分账参数
     * 
     * @param orderSubAccountDO
     * @param billDOList
     * @return
     */
    public WechatPaySubAccountCmd toWechatPaySubAccountCmd(OrderSubAccountDO orderSubAccountDO,
                                                           List<OrderSubAccountBillDO> billDOList, String appId) {
        WechatPaySubAccountCmd cmd = new WechatPaySubAccountCmd();
        cmd.setAppid(appId);
        cmd.setSub_mchid(orderSubAccountDO.getSubMchid());
        cmd.setTransaction_id(orderSubAccountDO.getTransactionId());
        cmd.setOut_order_no(orderSubAccountDO.getLastTransactionId());
        cmd.setUnfreeze_unsplit(false);
        List<WechatPaySubAccountReceiverCmd> receivers = new ArrayList<>();
        Date date = new Date();
        for (OrderSubAccountBillDO orderSubAccountBillDO : billDOList) {
            BigDecimal decimal = orderSubAccountBillDO.getActualSubAccountAmount().multiply(new BigDecimal("100"));
            int account = decimal.intValue();
            // 分配0元的接收方 不参与 分账 理论上也不该有
            if (account != 0) {
                WechatPaySubAccountReceiverCmd receiverCmd = new WechatPaySubAccountReceiverCmd();
                receiverCmd.setAmount(account);
                if (StringUtils.isNotBlank(orderSubAccountBillDO.getMerchantNumber())) {
                    // 含有商户号
                    receiverCmd.setType(SubAccountTypeEnum.MERCHANT_ID.name());
                    receiverCmd.setAccount(orderSubAccountBillDO.getMerchantNumber());
                } else if (StringUtils.isNotBlank(orderSubAccountBillDO.getOpenId())) {
                    receiverCmd.setType(SubAccountTypeEnum.PERSONAL_OPENID.name());
                    receiverCmd.setAccount(orderSubAccountBillDO.getOpenId());
                } else {
                    if (orderSubAccountBillDO.getSalemanId() == null) {
                        throw FinancialException.buildException(ORDER_SUB_ACCOUNT_BILL_SALE_MAN_ID_NULL);
                    }
                    // 如果都为空 从分销商获取数据
                    Response<DistributorSubAccountSalemanRpcDTOQry> response = distributorSubAccountSalemanServiceRpc
                        .getSubAccountSalemanById(orderSubAccountBillDO.getSalemanId());
                    if (!response.isSuccess()) {
                        throw FinancialException.buildException(response.getErrCode(), response.getErrMessage());
                    }
                    DistributorSubAccountSalemanRpcDTOQry data = response.getData();
                    if (StringUtils.isNotBlank(data.getMerchantNumber())) {
                        // 含有商户号
                        receiverCmd.setType(SubAccountTypeEnum.MERCHANT_ID.name());
                        receiverCmd.setAccount(data.getMerchantNumber());
                        orderSubAccountBillDO.setMerchantNumber(data.getMerchantNumber());
                    } else if (StringUtils.isNotBlank(data.getOpenId())) {
                        receiverCmd.setType(SubAccountTypeEnum.PERSONAL_OPENID.name());
                        receiverCmd.setAccount(data.getOpenId());
                        orderSubAccountBillDO.setOpenId(data.getOpenId());
                    } else {
                        throw FinancialException.buildException(ORDER_SUB_ACCOUNT_BILL_SALE_MAN_ID_NULL);
                    }
                    orderSubAccountBillDO.setUpdateTime(date);
                    orderSubAccountBillCmdExe.update(orderSubAccountBillDO);
                }
                receiverCmd.setDescription("订单" + orderSubAccountDO.getOrderNo() + "的分账");
                receivers.add(receiverCmd);
            }else{
                orderSubAccountBillDO.setSuccessFlag(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_BILL_SUCCESS_FLAG_SUCCESS);
                orderSubAccountBillDO.setStatus(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_ALL);
                orderSubAccountBillDO.setFailReason(null);
                orderSubAccountBillDO.setSuccessTime(date);
                orderSubAccountBillCmdExe.update(orderSubAccountBillDO);
            }
        }
        cmd.setReceivers(receivers);
        return cmd;
    }
}
