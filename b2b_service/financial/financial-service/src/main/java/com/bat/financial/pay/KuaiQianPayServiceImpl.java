package com.bat.financial.pay;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import com.bat.financial.api.pay.dto.*;
import com.bat.financial.common.CommonServiceImpl;
import com.bat.financial.dao.pay.dataobject.PayBillsDO;
import com.bat.financial.dao.platformaccount.dataobject.AccountKuaiQianDO;
import com.bat.financial.pay.constant.PayStatus;
import com.bat.financial.pay.data.kuaiqian.KuaiQianQueryTradeReqDTO;
import com.bat.financial.pay.executor.ErrorCode;
import com.bat.financial.pay.executor.PayCmdExc;
import com.bat.financial.pay.utils.KuaiQianUtils;
import com.bat.financial.pay.utils.Pkipair;
import com.bat.financial.pay.utils.TradeNoUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.pay.PayService;
import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.api.pay.dto.data.CreateTradeRespDTO;
import com.bat.financial.api.pay.dto.data.QueryRefundDTO;
import com.bat.financial.api.pay.dto.data.QueryTradeRespDTO;
import com.bat.financial.api.pay.dto.data.RefundTradeRespDTO;
import com.bat.financial.api.pay.notify.CreateNotifyReq;
import com.bat.financial.api.pay.notify.CreateNotifyResp;
import com.bat.financial.api.pay.notify.RefundNotifyReq;
import com.bat.financial.api.pay.notify.RefundNotifyResp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 17:26
 */
@Service("KuaiQianPayServiceImpl")
@Slf4j
public class KuaiQianPayServiceImpl implements PayService {

    @Value("${kuaiqian.gateway_pay}")
    private String gatewayPay;

    @Value("${kuaiqian.gateway_order_query}")
    private String gatewayOrderQuery;

    @Value("${kuaiqian.notify_url_create}")
    private String notifyUrl;

    @Value("${kuaiqian.sign_file}")
    private String filePath;

    @Value("${kuaiqian.check_file}")
    private String checkFile;

    @Resource
    private PayCmdExc payCmdExc;

    @Resource
    private CommonServiceImpl commonService;

    @SneakyThrows
    @Override
    public CreateTradeRespDTO createTrade(CreateTradeCmd cmd) {
        if (PayChannel.valueOf(cmd.getPayMethod().toUpperCase()) == PayChannel.KUAIQIAN_MERCHANT) {
            ObjectMapper objectMapper = new ObjectMapper();
            CreateTradeRespDTO dto = new CreateTradeRespDTO();
            CreateTradeRespDTO.KuaiQianCreateTradeRespDTO respDTO = new CreateTradeRespDTO.KuaiQianCreateTradeRespDTO();
            // closeOldTrade(cmd);
            AccountKuaiQianDO kuaiQianConfig = payCmdExc.getKuaiQianConfig(cmd.getOrganizationId());
            // 人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
            String merchantAcctId = kuaiQianConfig.getMerchanAcctId();
            // 编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
            String inputCharset = "1";
            // 接收支付结果的页面地址，该参数一般置为空即可。
            String pageUrl = "";
            // 服务器接收支付结果的后台地址，该参数务必填写，不能为空。
            // 加上机构 以及是否C端标志
            // 获取租户主机域名
            String host = commonService.getHostByTenantNo(TenantContext.getTenantNo());
            String bgUrl = "https://" + host + notifyUrl + "/" + cmd.getOrganizationId() + "/" + cmd.getCustomerFlag();
            // 拼接租户编码
            bgUrl = bgUrl + "/" + TenantContext.getTenantNo();
            // 网关版本，固定值：v2.0,该参数必填。
            String version = "v2.0";
            // 语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
            String language = "1";
            // 签名类型,该值为4，代表PKI加密方式,该参数必填。
            String signType = "4";
            // 支付人姓名,可以为空。
            String payerName = "";
            // 支付人联系类型，1 代表电子邮件方式；2 代表手机联系方式。可以为空。
            String payerContactType = "";
            // 支付人联系方式，与payerContactType设置对应，payerContactType为1，则填写邮箱地址；payerContactType为2，则填写手机号码。可以为空。
            String payerContact = "";
            // 指定付款人，可以为空
            String payerIdType = "3";
            // 付款人标识，可以为空
            String payerId = "";
            // 付款人IP，可以为空
            String payerIP = "";
            // 商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
            String orderId = TradeNoUtils.getCreateTradeNo("kq", cmd.getCustomerFlag());
            // 订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
            String orderAmount = (int)(cmd.getAmount().multiply(BigDecimal.valueOf(100L)).doubleValue()) + "";
            // 订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101，不能为空。
            String orderTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
            // 快钱时间戳，格式：yyyyMMddHHmmss，如：20071117020101， 可以为空
            String orderTimestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());;
            // 商品名称，可以为空。
            String productName = "分销平台订单支付";
            // 商品数量，可以为空。
            String productNum = "";
            // 商品代码，可以为空。
            String productId = "";
            // 商品描述，可以为空。
            String productDesc = cmd.getDescription();
            // 扩展字段1，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
            String ext1 = cmd.getRedirectUrl();
            // 扩展自段2，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
            String ext2 = "";
            // 支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10-1或10-2，必填。
            String payType = "00";
            // 银行代码，如果payType为00，该值可以为空；如果payType为10-1或10-2，该值必须填写，具体请参考银行列表。
            String bankId = "";
            // 分期期数，分期支付的时候此参数不可为空，按照与快钱签订协议 期数进行上送。非必填
            String period = "";
            // 发卡机构 字符串，固定值99BILL：支持预付卡和快运通。99BILL_EAP：支持快运通，包括采购卡 非必填
            String cardIssuer = "";
            // 卡号 整形数字 提交给快钱的支付卡号，页面上不可修改 仅当 payType 为 15（信用卡支付）、17（储值卡支付）时有效 非必填
            String cardNum = "";
            // 线下汇款类型 1 代表银行柜台汇款 2 代表邮局汇款 当 payType=13 且 submitType=01时（后台提交的线下支付方式）时不可空 非必填
            String remitType = "";
            // 同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。 非必填
            String redoFlag = "0";
            // 快钱合作伙伴的帐户号，即商户编号，可为空。 非必填
            String pid = "";
            // 提交方式 00:代表前台提交 01：代表后台提交 为空默认为前台提交 非必填
            String submitType = "";
            // 交易超时时间：正整数，0~2592000（30 天）单位为秒默认为空，为空表示交易无超时时间订单超时计算规则：订单支付成功时间 减去 订单提交时间（orderTime） 大于
            // 订单超时时间（orderTimeOut）超时成功订 单，快钱会自动发起退款 非必填
            String orderTimeOut = "";
            // 附加信息类型 字符串 ，与 extDataContent 配合使用 。固定值为 NB2（微信实名 、二维码有效时长 和分账时使用 ） 非必填
            String extDataType = "";
            // 附加信息 XML 与 extDataType 匹配使用 。二维码有效时长 key 为timePeriod，二维码实名认证key 为 realNameCertification，平台分账 Key 为
            // sharingInfo ，微信支付宝扫码支付是否限制信用卡 key 为 limitPay 非必填
            String extDataContent = "";
            // 分账标识 分账标识，为空代表不分账；1为分账；如快钱后台没有开通平台分账功能，此参数传 1 无效 非必填
            String sharingFlag = "";
            // 手续费收取方式 当 sharingFlag 为 1，不可为空。0: 主收款方承担手续费 非必填
            String feeMode = "";
            // 手续费主手款方 当 feeMode 为 0，不可为空。用来承担快钱手续费的快钱账户，此处为平台快钱账户。 非必填
            String feepayer = "";
            // 分账明细 sharingFlag=1 时此字段不能为空； 分账明细数据格式： flag^sharingContact ^ amount ^shar-ingSyncFlag^memo 多条分账明细数据之间采用符号
            // “|”进行 分隔。 【其中： flag：必传，固定选择值 ： 2 2 为useId（平台二级商户在平台的唯一标识）； sharingContact：必传，分账方（或称平台合作商户）的 useId；
            // amount：必传，对应分账方的分账金额，单位为分； sharingSyncFlag，分账模式，必传。固定值为 0 或者 1或者 T+N（1<=N<=99）。 0------准同步分账明细，即 T+1
            // 分账；1----异步分账，平台调用分账确认接口后立即分账；T+N 分账-----为按计划分账，代表 T+N 分账memo：可为空，备注字段。 如果feePayer 不为空，则sharingContact
            // 必须包含feePayer，且对应的 amount 需要大于交易手续费金额 amount 的总和需要等于 orderAmount非 非必填
            String sharingData = "";
            // signMsg 签名字符串 不可空，生成加密签名串
            StringBuilder signMsgVal = null;
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "inputCharset", inputCharset);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "pageUrl", pageUrl);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "bgUrl", bgUrl);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "version", version);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "language", language);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "signType", signType);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "merchantAcctId", merchantAcctId);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "payerName", payerName);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "payerContactType", payerContactType);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "payerContact", payerContact);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "payerIdType", payerIdType);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "payerId", payerId);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "payerIP", payerIP);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "orderId", orderId);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "orderAmount", orderAmount);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "orderTime", orderTime);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "orderTimestamp", orderTimestamp);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "productName", productName);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "productNum", productNum);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "productId", productId);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "productDesc", productDesc);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "ext1", ext1);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "ext2", ext2);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "payType", payType);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "bankId", bankId);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "period", period);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "cardIssuer", cardIssuer);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "cardNum", cardNum);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "remitType", remitType);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "cardNum", cardNum);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "redoFlag", redoFlag);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "pid", pid);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "submitType", submitType);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "orderTimeOut", orderTimeOut);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "extDataType", extDataType);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "extDataContent", extDataContent);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "sharingFlag", sharingFlag);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "feeMode", feeMode);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "feepayer", feepayer);
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "sharingData", sharingData);
            String signMsg = Pkipair.signMsg(signMsgVal.toString(), kuaiQianConfig.getSignFileUrl(),
                kuaiQianConfig.getSignPwd(), kuaiQianConfig.getSignPrivateKey());
            signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "signMsg", URLEncoder.encode(signMsg, "utf-8"));
            // 中文参数一定要url编码
            String h5Url = gatewayPay + signMsgVal;
            h5Url = KuaiQianUtils.encodeParam(h5Url, "productName", productName);
            h5Url = KuaiQianUtils.encodeParam(h5Url, "productDesc", productDesc);
            h5Url = KuaiQianUtils.encodeParam(h5Url, "ext1", ext1);
            log.info("快钱生成的url:{}", h5Url);
            respDTO.setH5Url(h5Url);
            respDTO.setOutTradeNo(orderId);
            dto.setKuaiQianReap(respDTO);
            // 存储支付凭证 待支付
            payCmdExc.createPayBills(cmd, orderId);
            return dto;
        } else {
            // 暂不支持快捷支付
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_NOT_SUPPORT);
        }
    }

    /**
     * 确认旧订单 并处理
     *
     * @return
     */
    private void confirmOldTrade(CreateTradeCmd cmd) {
        if (StringUtils.isNotBlank(cmd.getOrderId())) {
            PayBillsDO payBillByOrderId =
                payCmdExc.getPayBillByOrderId(cmd.getCustomerFlag(), cmd.getOrderId().split(",")[0], cmd.getPayerId());
            // 存在旧的交易
            if (payBillByOrderId != null) {
                if (payBillByOrderId.getPayStatus().equals(PayStatus.COMPLETE_PAYMENT)) {
                    throw FinancialException.buildException(ErrorCode.B_TRADE_HAS_BEEN_PAID);
                } else if (payBillByOrderId.getPayStatus().equals(PayStatus.WAIT_PAYMENT)) {
                    // String outTradeNo = payBillByOrderId.getOutTradeNo();
                    // log.info("存在旧的交易，商户订单号：{}", outTradeNo);
                    // CloseTradeCmd closeTradeCmd = new CloseTradeCmd();
                    // BeanUtils.copyProperties(cmd, closeTradeCmd);
                    // closeTradeCmd.setOutTradeNo(outTradeNo);
                    // threadPoolTaskScheduler.submit(() -> closeTrade(closeTradeCmd));
                }
            }
        }
    }

    @SneakyThrows
    @Override
    public QueryTradeRespDTO queryTrade(QueryTradeQry cmd) {
        // throw SystemException.buildException(ErrorCode.B_NOT_SUPPORT);
        AccountKuaiQianDO kuaiQianConfig = payCmdExc.getKuaiQianConfig(cmd.getOrganizationId());
        // 人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
        String merchantAcctId = kuaiQianConfig.getMerchanAcctId();
        // 编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
        String inputCharset = "1";
        // 网关版本，固定值：v2.0,该参数必填。
        String version = "v2.0";
        // 签名类型,数字串，2 代表PKI 加密方式 ？？？与创建时不同 创建时为4 必填
        String signType = "2";
        // 查询方式 固定选择值：0、1 0 按商户订单号单笔查询（只 返回成功订单） 1 按交易结束时间批量查询 （只返回成功订单） 必填
        String queryType = "0";
        // 查询模式 固定值：1 1 代表简单查询（返回基本订单信息）必填
        String queryMode = "1";
        // 交易开始时间，格式：yyyyMMddHHmmss，如：20071117020101。 非必填
        String startTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        // 交易结束时间，格式：yyyyMMddHHmmss，如：20071117020101。 非必填
        String endTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        // 请求记录集页码。 数字串在查询结果数据总量很大时，快钱会将支付结果分多次返回。本参数表示商户需要得到的记录集页码。默认为1，表示第1 页
        String requestPage = "1";
        // 商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
        String orderId = cmd.getOutTradeNo();
        // 查询秘钥
        String key = "27YKWKBKHT2IZSQ4";// XIXMFISFG7RGDKQN

        // signMsg 签名字符串 不可空，生成加密签名串
        StringBuilder signMsgVal = null;
        signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "inputCharset", inputCharset);
        signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "version", version);
        signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "signType", signType);
        signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "merchantAcctId", merchantAcctId);
        signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "queryType", queryType);
        signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "queryMode", queryMode);
        signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "startTime", "");
        signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "endTime", "");
        signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "requestPage", requestPage);
        signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "orderId", orderId);
        signMsgVal = KuaiQianUtils.appendParam(signMsgVal, "key", key);
        log.info("快钱查询加密串：{}", signMsgVal.toString());
        String signMsg = Pkipair.signMsg(signMsgVal.toString(), kuaiQianConfig.getSignFileUrl(),
            kuaiQianConfig.getSignPwd(), kuaiQianConfig.getSignPrivateKey());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(gatewayOrderQuery);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        KuaiQianQueryTradeReqDTO dto1 = new KuaiQianQueryTradeReqDTO();
        dto1.setInputCharset(inputCharset);
        dto1.setVersion(version);
        dto1.setSignType(signType);
        dto1.setMerchantAcctId(merchantAcctId);
        dto1.setQueryType(queryType);
        dto1.setQueryMode(queryMode);
        // dto1.setStartTime(startTime);
        // dto1.setEndTime(endTime);
        dto1.setOrderId(cmd.getOutTradeNo());
        dto1.setRequestPage(requestPage);
        dto1.setSignMsg(signMsg);
        String jsonParams = JSON.toJSONString(dto1);
        log.info("快钱查询请求参数：{}", jsonParams);
        httpPost.setEntity(new StringEntity(jsonParams, "utf-8"));
        CloseableHttpResponse response = client.execute(httpPost);
        ObjectMapper objectMapper = new ObjectMapper();
        String s1 = EntityUtils.toString(response.getEntity());
        log.info("快钱查询返回值：{}", s1);
        QueryTradeRespDTO.KuaiQianQueryTradeRespDTO kuaiQianQueryTradeRespDTO =
            objectMapper.readValue(s1, QueryTradeRespDTO.KuaiQianQueryTradeRespDTO.class);
        if (StringUtils.isNotBlank(kuaiQianQueryTradeRespDTO.getErrCode())) {
            log.error("查询订单异常，异常编码：{}", kuaiQianQueryTradeRespDTO.getErrCode());
            throw FinancialException.buildException(ErrorCode.B_QUERY_TRADE_ERROR,
                "查询快钱订单异常，异常编码:{}" + kuaiQianQueryTradeRespDTO.getErrCode());
        } else {
            // 只会返回成功订单
            // 存储支付凭证 已支付或取消支付（支付失败）
            PayBillsDO aDo = new PayBillsDO();
            QueryTradeRespDTO.KuaiQianQueryTradeRespDTO.OrderDetailDTO orderDetailDTO =
                kuaiQianQueryTradeRespDTO.getOrderDetail().get(0);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            aDo.setPayTime(format.parse(orderDetailDTO.getOrderTime()));
            aDo.setPayStatus(PayStatus.COMPLETE_PAYMENT);
            aDo.setOutTradeNo(orderDetailDTO.getOrderId());
            aDo.setOnlineTradeNo(orderDetailDTO.getDealId());
            /**
             * 快钱使用的人民币网关 只支持人民币
             */
            aDo.setCurrencyCode("CNY");
            payCmdExc.updatePayBills(cmd.getCustomerFlag(), aDo);
        }
        QueryTradeRespDTO dto = new QueryTradeRespDTO();
        dto.setKuaiQianResp(kuaiQianQueryTradeRespDTO);
        return dto;
    }

    @SneakyThrows
    @Override
    public CreateNotifyResp createTradeNotify(CreateNotifyReq notify) {
        AccountKuaiQianDO kuaiQianConfig = payCmdExc.getKuaiQianConfig(notify.getOrganizationId());
        StringBuilder merchantSignMsgVal;
        merchantSignMsgVal = KuaiQianUtils.appendParam(null, "merchantAcctId", notify.getKuaiQianReq().getMerchantAcctId());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "version", notify.getKuaiQianReq().getVersion());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "language", notify.getKuaiQianReq().getLanguage());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "signType", notify.getKuaiQianReq().getSignType());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "payType", notify.getKuaiQianReq().getPayType());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "bankId", notify.getKuaiQianReq().getBankId());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "orderId", notify.getKuaiQianReq().getOrderId());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "orderTime", notify.getKuaiQianReq().getOrderTime());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "orderAmount", notify.getKuaiQianReq().getOrderAmount());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "bindCard", "");
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "bindMobile", "");
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "dealId", notify.getKuaiQianReq().getDealId());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "bankDealId", notify.getKuaiQianReq().getBankDealId());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "dealTime", notify.getKuaiQianReq().getDealTime());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "payAmount", notify.getKuaiQianReq().getPayAmount());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "fee", notify.getKuaiQianReq().getFee());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "ext1", notify.getKuaiQianReq().getExt1());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "ext2", notify.getKuaiQianReq().getExt2());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "payResult", notify.getKuaiQianReq().getPayResult());
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "aggregatePay", "");
        merchantSignMsgVal = KuaiQianUtils.appendParam(merchantSignMsgVal, "errCode", notify.getKuaiQianReq().getErrCode());
        log.info("merchantSignMsgVal={}", merchantSignMsgVal);
        log.info("SignMsg={}", notify.getKuaiQianReq().getSignMsg());
        boolean b = Pkipair.enCodeByCer(kuaiQianConfig.getCheckSignFileUrl(), merchantSignMsgVal.toString(),
            notify.getKuaiQianReq().getSignMsg());
        CreateNotifyResp resp = new CreateNotifyResp();
        CreateNotifyResp.KuaiQianCreateNotifyResp resp1 = new CreateNotifyResp.KuaiQianCreateNotifyResp();
        String redirectUrl = notify.getKuaiQianReq().getExt1();
        if (!b) {
            log.error("快钱验签失败");
            resp1.setMessage("<result>0</result><redirecturl>" + redirectUrl + "</redirecturl>");
        } else {
            // 付款成功
            PayBillsDO aDo = new PayBillsDO();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            aDo.setPayTime(format.parse(notify.getKuaiQianReq().getOrderTime()));
            aDo.setOutTradeNo(notify.getKuaiQianReq().getOrderId());
            aDo.setOnlineTradeNo(notify.getKuaiQianReq().getDealId());
            BigDecimal decimal = new BigDecimal(notify.getKuaiQianReq().getOrderAmount());
            BigDecimal divide = decimal.divide(BigDecimal.valueOf(100L), 2, BigDecimal.ROUND_HALF_UP);
            aDo.setTotalFee(divide);
            if ("10".equals(notify.getKuaiQianReq().getPayResult())) {
                // 存储支付凭证 已支付或取消支付（支付失败）
                aDo.setPayStatus(PayStatus.COMPLETE_PAYMENT);
            } else {
                // 存储支付凭证 已支付或取消支付（支付失败）
                aDo.setPayStatus(PayStatus.CANCEL_PAYMENT);
            }
            /**
             * 快钱使用的人民币网关 只支持人民币
             */
            aDo.setCurrencyCode("CNY");
            payCmdExc.updatePayBills(notify.getCustomerFlag(), aDo);
        }
        resp1.setMessage("<result>1</result><redirecturl>" + redirectUrl + "</redirecturl>");
        resp.setKuaiQianResp(resp1);
        return resp;
    }

    @Override
    public boolean closeTrade(CloseTradeCmd cmd) {
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }

    @Override
    public RefundTradeRespDTO refundTrade(RefundTradeCmd cmd) {
        // 需要联系快钱销售那边 可能需要签合同之类的没有实现
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }

    @Override
    public QueryRefundDTO queryRefund(QueryRefundCmd cmd) {
        // 需要联系快钱销售那边 可能需要签合同之类的没有实现
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }

    @Override
    public RefundNotifyResp refundTradeNotify(RefundNotifyReq req) {
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }

}
