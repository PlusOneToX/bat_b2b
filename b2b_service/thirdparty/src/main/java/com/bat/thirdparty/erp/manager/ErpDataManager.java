package com.bat.thirdparty.erp.manager;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.bat.thirdparty.common.ThirdKeyConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.erp.api.request.*;
import com.bat.thirdparty.erp.api.response.*;
import com.bat.thirdparty.erp.k3cloudwebapiclient.K3CloudApiClient;
import com.bat.thirdparty.erp.service.executor.ErpConfigExe;
import com.bat.thirdparty.erp.service.executor.ErrorCode;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.exchange.ExchangeCodeInvalidCodeDTO;
import com.bat.dubboapi.thirdparty.erp.dto.exchange.ExchangeCodeSyncERPRequest;
import com.bat.thirdparty.erp.api.request.*;
import com.bat.thirdparty.erp.api.request.dto.order.CreateSaleOrderQry;
import com.bat.thirdparty.erp.api.response.*;

@Component
public class ErpDataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpDataManager.class);

    @Resource
    private ErpConfigExe erpConfigExe;

    @Resource
    HttpUtil httpUtil;

    @CreateCache(name = ThirdKeyConstant.ERP_LOGIN_TOKEN, expire = 1100)
    private Cache<String, String> erpLoginTokenCache;

    @CreateCache(name = ThirdKeyConstant.ERP_LOGIN_KEY)
    private Cache<String, Integer> erpLoginMethodCache;

    // HttpHeaders headers = new HttpHeaders();

    // @PostConstruct
    // public void init() {
    // headers.add("Content-Type", "application/json");
    // headers.add("Accept", "application/json");
    // }

    /********************************** 扩展接口 ***********************************************/
    // public String getBase() {
    // erpConfigExe.getPlatformTenantErpConfig();
    // erpConfigExe.getConfig();
    // headers.remove("platform");
    // headers.add("platform", config.getPlatform());
    // return config.getBaseUrl() + config.getBaseExtUrl();
    // }

    public HttpHeaders getHeaders(PlatformTenantErpRpcDTO erpConfig) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.remove("platform");
        headers.add("platform", erpConfig.getPlatform());
        return headers;
    }

    public ValidateSystemResponse validateSystem() {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        ValidateSystemRequest request = new ValidateSystemRequest();
        request.setUSERNAME(erpConfig.getUserName());
        request.setPASSWORD(erpConfig.getPassword());
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, ValidateSystemResponse.class);
    }

    /**
     * 扩展接口登录获取token接口（含缓存数据，有效期20分钟，cacheKey固定值传"AXI-token"）
     *
     * @param cacheKey
     *            token缓存key固定为“AXI-token”
     * @return
     */
    // @Cached(name = "ERP-", key = "#cacheKey", expire = 1100)
    public synchronized String getToken(String cacheKey) {
        String token = erpLoginTokenCache.get(TenantContext.getTenantNo() + ":" + cacheKey);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        // 获取分布式erp登录锁
        AutoReleaseLock autoReleaseLock =
            erpLoginMethodCache.tryLock(TenantContext.getTenantNo() + ":" + "ERP_LOGIN", 60, TimeUnit.SECONDS);
        if (autoReleaseLock != null) {
            // 获取锁成功，进行erp登录
            try {
                ValidateSystemResponse response = validateSystem();
                if (response != null && response.isSuccess()) {
                    // 塞到缓存
                    LOGGER.info("重新获取erp token");
                    erpLoginTokenCache.put(TenantContext.getTenantNo() + ":" + cacheKey, response.getAccessToken(),
                        1100, TimeUnit.SECONDS);
                    return response.getAccessToken();
                }
                throw ThirdPartyException.buildException(ErrorCode.B_ERP_LOGIN_ERROR);
            } catch (Exception e) {
                e.printStackTrace();
                throw ThirdPartyException.buildException(ErrorCode.B_ERP_LOGIN_ERROR);
            } finally {
                // 释放锁
                if (autoReleaseLock != null) {
                    autoReleaseLock.close();
                }
            }
        } else {
            // 获取失败睡眠1秒后再次获取
            try {
                Thread.sleep(1000);
                return getToken(cacheKey);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw ThirdPartyException.buildException(ErrorCode.B_ERP_LOGIN_ERROR);
            }
        }
    }

    /**
     * 删除erp token缓存(cacheKey固定值传"AXI-token")
     *
     * @param cacheKey
     */
    // @CacheInvalidate(name = "ERP-", key = "#cacheKey")
    public void deleteCachedToken(String cacheKey) {
        LOGGER.info("删除erp token");
        erpLoginTokenCache.remove(TenantContext.getTenantNo() + ":" + cacheKey);
    }

    // 库存同步
    public InquiryInventoryResponse findErpStock(InquiryInventoryRequest request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        LOGGER.info("同步ERP库存：" + erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath());
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, InquiryInventoryResponse.class);
    }

    // 余额同步
    public BalanceInfoResponse findBalance(BalanceRequest request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, BalanceInfoResponse.class);
    }

    // 新建订单
    public CreateSaleOrderResponse createSaleOrder(CreateSaleOrderQry request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, CreateSaleOrderResponse.class);
    }

    // 取消订单（作废订单）
    public InvalidSaleOrderResponse invalidSaleOrder(InvalidSaleOrderRequest request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, InvalidSaleOrderResponse.class);
    }

    // 关闭订单
    public CloseSaleOrderResponse closeSaleOrder(CloseSaleOrderRequest request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
                HttpMethod.POST, getHeaders(erpConfig), request, CloseSaleOrderResponse.class);
    }

    // 创建收款单
    public CreateReceiveBillEntryResponse createReceiveBill(CreateReceiveBillEntryRequest request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, CreateReceiveBillEntryResponse.class);
    }

    // 创建退款单
    public CreateRefundBillResponse createRefundBill(CreateRefundBillRequest request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, CreateRefundBillResponse.class);
    }

    // 创建预测单
    public CreatePlnForeCastResponse createPlnForeCast(CreatePlnForeCastRequest request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, CreatePlnForeCastResponse.class);
    }

    // 同步盒码明码到erp
    public ErpSyncResponse syncBoxCodeAndPlainCodeToErp(ExchangeCodeSyncERPRequest request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, ErpSyncResponse.class);
    }

    // 同步盒码明码到erp
    public BaseResponse syncExchangeCodeInvalid(ExchangeCodeInvalidCodeDTO request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, BaseResponse.class);
    }

    public SysPushtSTKInStockResponse sysPushtSTKInStock(SysPushtSTKInStockRequest request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, SysPushtSTKInStockResponse.class);
    }

    public SysPUSHOUTSTOCKResponse sysPUSHOUTSTOCK(SysPUSHOUTSTOCKRequest request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, SysPUSHOUTSTOCKResponse.class);
    }

    /**
     * 同步标品直发订单的出库单到ERP(触发ERP进行库存变更)
     * 
     * @param request
     * @return
     */
    public SysPUSHOUTSTOCKResponse syncGeneralOrderOutbound(SyncGeneralOutboundCmd request) {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        return httpUtil.requestFor(erpConfig.getBaseUrl() + erpConfig.getBaseExtUrl() + request.getPath(),
            HttpMethod.POST, getHeaders(erpConfig), request, SysPUSHOUTSTOCKResponse.class);
    }

    /********************************** 标准接口 ***********************************************/
    public K3CloudApiClient login() throws Exception {
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        K3CloudApiClient client = new K3CloudApiClient(erpConfig.getBaseUrl());
        Boolean result = client.login(erpConfig.getDbId(), erpConfig.getUserName(), erpConfig.getPassword(),
            Integer.valueOf(erpConfig.getLang()));
        if (result) {
            return client;
        } else {
            LOGGER.info("erp登录失败");
            return null;
        }
    }

    public static void main(String[] args) {
        Integer a = test();
        System.out.println(a);
    }

    private static Integer test() {
        try {
            Integer index = 0;
            return index;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(333);
        } finally {
            System.out.println(22);
        }

        return 2;

    }

}
