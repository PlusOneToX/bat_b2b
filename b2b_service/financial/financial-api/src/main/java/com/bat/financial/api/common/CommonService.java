package com.bat.financial.api.common;

import java.util.Map;

import com.bat.financial.api.common.dto.BaseBillEntity;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.system.organization.dto.data.OrganizationRpcDTO;
import com.bat.financial.api.pay.PayService;
import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.api.pay.dto.CreateTradeCmd;
import com.bat.financial.api.pay.dto.QueryTradeQry;
import com.bat.financial.api.pay.dto.RefundTradeCmd;

public interface CommonService {
    /**
     * 获取映射关系
     * 
     * @return
     */
    Map<PayChannel, PayService> getMap();

    /**
     * 根据分销商id 获取分销商信息
     * 
     * @param distributorId
     * @return
     */
    DistributorRpcDTO getDistributorInfo(Integer distributorId);

    OrganizationRpcDTO getOrganizationInfo(Integer distributorId);

    OrganizationRpcDTO getOrganizationInfoBySaleId(Integer sealId);

    void preCreateTrade(CreateTradeCmd cmd);

    void preQueryTrade(QueryTradeQry qry);

    void preRefundTrade(RefundTradeCmd cmd);

    /**
     * 根据payMethod 获取 payChannel
     * 
     * @param payMethod
     * @param tradeMode
     * @param distributorId
     * @param organizationId
     * @return
     */
    String getPayChannel(String payMethod, Short tradeMode, Integer distributorId, Integer organizationId);

    /**
     * 计算余额 以及更新信息 互相补充
     * 
     * @param distributorDTO
     * @param bookDTO
     */
    void calcBalanceAndBook(DepositDistributorDTO distributorDTO, DepositDistributorSubsidiaryBookDTO bookDTO);

    /**
     * 组装
     * 
     * private String FSETTLETYPEID = ""; //结算方式,固定
     * 
     * 
     * private String FACCOUNTID = ""; //收款的银行账号，用于测试的账号：10863000000410707
     * 
     * @param organizationId
     * @param payWay
     * @param outTradeNo
     * @return
     */
    BaseBillEntity getEntity(Integer organizationId, Short payWay, String outTradeNo);

    /**
     * 根据租户编码获取后端接口主机域名
     * 
     * @param tenantNo
     * @return
     */
    String getHostByTenantNo(String tenantNo);
}
