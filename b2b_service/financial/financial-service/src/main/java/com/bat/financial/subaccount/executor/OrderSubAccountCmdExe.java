package com.bat.financial.subaccount.executor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.subaccount.DistributorSubAccountAdminConfigServiceRpc;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountAdminConfigRpcDTOQry;
import com.bat.dubboapi.financial.subaccount.dto.OrderSubAccountCmd;
import com.bat.dubboapi.financial.subaccount.dto.SubAccountReceiveCmd;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.subaccount.dto.OrderSubAccountIdCmd;
import com.bat.financial.common.constant.FinancialConstant;
import com.bat.financial.common.constant.subaccount.OrderSubAccountConstant;
import com.bat.financial.common.constant.third.DistributorConstant;
import com.bat.financial.dao.pay.PayBillsCustomerMapper;
import com.bat.financial.dao.pay.dataobject.PayBillsCustomerDO;
import com.bat.financial.dao.subaccount.OrderSubAccountDOMapper;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountBillDO;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountDO;
import com.bat.financial.message.MessageSendService;
import com.bat.financial.pay.utils.TradeNoUtils;

@Component
public class OrderSubAccountCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSubAccountCmdExe.class);

    @Autowired
    private OrderSubAccountDOMapper orderSubAccountDOMapper;

    @Autowired
    private OrderSubAccountBillCmdExe orderSubAccountBillCmdExe;

    @Autowired
    private PayBillsCustomerMapper payBillsCustomerMapper;

    @CreateCache(name = FinancialConstant.ORDER_SUB_ACCOUNT_CREATE_LOCK_KEY)
    private Cache<String, Integer> financialOrderSubAccountCache;

    @DubboReference(check = false, timeout = 6000, retries = 0)
    private DistributorSubAccountAdminConfigServiceRpc distributorSubAccountAdminConfigServiceRpc;

    @Autowired
    private MessageSendService messageSendService;

    @Transactional(rollbackFor = Exception.class)
    public void dealwithOrderSubAccount(OrderSubAccountCmd orderSubAccountCmd) {
        AutoReleaseLock autoReleaseLock = financialOrderSubAccountCache.tryLock(
            TenantContext.getTenantNo() + ":" + String.valueOf(orderSubAccountCmd.getOrderId()), 5, TimeUnit.MINUTES);
        if (autoReleaseLock == null) {
            LOGGER.info(orderSubAccountCmd.getOrderId() + "正在执行分账");
            throw FinancialException.buildException("数据正在执行处理、请勿频繁操作");
        }

        try {

            if (orderSubAccountCmd == null) {
                LOGGER.info("分销订单列表为空、不处理{}", JSON.toJSONString(orderSubAccountCmd));
                return;
            }
            PayBillsCustomerDO payBillsCustomerDO =
                payBillsCustomerMapper.getByOutTradeNo(orderSubAccountCmd.getOutTradeNo());

            OrderSubAccountDO accountDO = orderSubAccountDOMapper.getByOrderId(orderSubAccountCmd.getOrderId());
            if (accountDO != null) {
                LOGGER.info("该分销订单已生成分账数据、不处理{}", JSON.toJSONString(orderSubAccountCmd));
                return;
            }
            // 判断是否马上分账
            Response<DistributorSubAccountAdminConfigRpcDTOQry> response = distributorSubAccountAdminConfigServiceRpc
                .getAdminSubAccountConfigByDistributorId(orderSubAccountCmd.getDistributorId());
            if (!response.isSuccess()) {
                LOGGER.error("查询分销商后台时效配置、出现异常{}", JSON.toJSONString(response));
                throw FinancialException.buildException(response.getErrCode(), response.getErrMessage());
            }
            DistributorSubAccountAdminConfigRpcDTOQry adminConfigRpcDTOQry = response.getData();
            List<OrderSubAccountBillDO> list = new ArrayList<>();

            OrderSubAccountDO orderSubAccountDO = new OrderSubAccountDO();
            BeanUtils.copyProperties(orderSubAccountCmd, orderSubAccountDO);
            orderSubAccountDO.setTransactionId(payBillsCustomerDO.getOnlineTradeNo());
            // 默认是待分账
            orderSubAccountDO.setStatus(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_UN);
            orderSubAccountDO.setCreateTime(new Date());
            orderSubAccountDO.setUpdateTime(new Date());
            // 设置分账单号
            orderSubAccountDO.setLastTransactionId(TradeNoUtils.getCreateTradeNo("wxsub", (short)1));
            // 子商户号
            orderSubAccountDO.setSubMchid(payBillsCustomerDO.getMchid());
            if (DistributorConstant.DISTRIBUTOR_SUB_ACCOUNT_AGING_TYPE_REALTIME
                .equals(adminConfigRpcDTOQry.getAgingType())) {
                // 实时
                orderSubAccountDO.setPlanTime(new Date());
            } else {
                // 计算小时后时间
                BigDecimal delayTime = adminConfigRpcDTOQry.getDelayTime();
                long currentTimeMillis = System.currentTimeMillis();
                Integer time = 1000 * 60 * 60;
                long longValue =
                    delayTime.multiply(new BigDecimal(String.valueOf(time))).longValue() + currentTimeMillis;
                orderSubAccountDO.setPlanTime(new Date(longValue));
            }
            // 默认是没有失败
            orderSubAccountDO.setSubAccountFailFlag(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_EXIST_FAIL_FLAG_NO);
            orderSubAccountDOMapper.insert(orderSubAccountDO);
            OrderSubAccountIdCmd orderSubAccountIdCmd = new OrderSubAccountIdCmd();
            orderSubAccountIdCmd.setId(orderSubAccountDO.getId());
            orderSubAccountIdCmd.setOrderId(orderSubAccountDO.getOrderId());
            orderSubAccountIdCmd.setAppId(payBillsCustomerDO.getAppId());

            List<SubAccountReceiveCmd> receiveCmdList = orderSubAccountCmd.getReceiveCmdList();
            receiveCmdList.stream().forEach(subAccountReceiveCmd -> {
                OrderSubAccountBillDO accountBillDO = new OrderSubAccountBillDO();
                accountBillDO.setOrderSubAccountId(orderSubAccountDO.getId());
                accountBillDO.setLevelId(subAccountReceiveCmd.getLevelId());
                accountBillDO.setLevelName(subAccountReceiveCmd.getLevelName());
                accountBillDO.setSalemanId(subAccountReceiveCmd.getSalemanId());
                accountBillDO.setSalemanName(subAccountReceiveCmd.getSalemanName());
                accountBillDO.setMaxSubAccountAmount(subAccountReceiveCmd.getAmount());
                // 实际分账金额默认就是最大分账金额
                accountBillDO.setActualSubAccountAmount(subAccountReceiveCmd.getAmount());
                accountBillDO.setRatio(subAccountReceiveCmd.getRatio());
                accountBillDO.setStatus(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_UN);
                accountBillDO.setOpenId(subAccountReceiveCmd.getOpenId());
                accountBillDO.setMerchantNumber(subAccountReceiveCmd.getMerchantNumber());
                accountBillDO.setSalemanId(subAccountReceiveCmd.getSalemanId());
                accountBillDO.setSalemanName(subAccountReceiveCmd.getSalemanName());
                accountBillDO.setCreateTime(new Date());
                accountBillDO.setUpdateTime(new Date());
                list.add(accountBillDO);
            });
            orderSubAccountBillCmdExe.batchCreate(list);
            LOGGER.info("返回的id列表{}", JSON.toJSONString(list));
            List<Integer> billIdList = list.stream().map(OrderSubAccountBillDO::getId).collect(Collectors.toList());
            orderSubAccountIdCmd.setBillIdList(billIdList);

            if (DistributorConstant.DISTRIBUTOR_SUB_ACCOUNT_AGING_TYPE_REALTIME
                .equals(adminConfigRpcDTOQry.getAgingType())) {
                // 实时
                messageSendService.subAccountToWxPartner(orderSubAccountIdCmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("保存分账结果异常、参数为{},异常{}", JSON.toJSONString(orderSubAccountCmd), e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw FinancialException.buildException("分账异常");
        } finally {
            autoReleaseLock.close();
        }
    }

    public void update(OrderSubAccountDO orderSubAccountDO) {
        orderSubAccountDOMapper.updateByPrimaryKey(orderSubAccountDO);
    }
}
