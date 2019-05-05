package com.bat.thirdparty.erp.convertor;

import java.util.ArrayList;

import com.bat.thirdparty.common.ThirdCommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.distributor.distributor.dto.DistributorErpRpcCmd;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorAddressRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorContactsRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorERPRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorFinancialRpcDO;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;
import com.bat.thirdparty.erp.api.request.DistributorERPCheckRequest;
import com.bat.thirdparty.erp.api.request.DistributorERPRequest;
import com.bat.thirdparty.erp.api.request.dto.DistributorERPEntry;
import com.bat.thirdparty.erp.dao.dataobject.CountryComparisonDO;

/**
 * 订单ERP
 */
@Component
public class ErpDistributorConvertor {

    public static DistributorERPRequest toDistributorERPRequest(DistributorERPRpcDTO distributor, UserRpcDTO sales,
        CountryComparisonDO country) {
        DistributorERPRequest request = new DistributorERPRequest();
        DistributorERPEntry model = new DistributorERPEntry();
        request.setModel(model);
        String erpOrganizationId = sales.getOrganizationDTO().getErpOrganizationId();
        model.setFCreateOrgId(new DistributorERPEntry.ERPCreateOrg(erpOrganizationId));
        model.setFUseOrgId(new DistributorERPEntry.ERPFUseOrgId(erpOrganizationId));
        if (distributor.getErpId() != null) {
            model.setFCUSTID(String.valueOf(distributor.getErpId()));
        }
        model.setFName(distributor.getCompanyName());
        model.setFNumber(distributor.getErpNo());
        model.setFIsTrade(true);
        model.setFSELLER(new DistributorERPEntry.ERPSeller(sales.getErpUserNo()));
        String erpDepartmentId = sales.getDepartmentDTO().getErpDepartmentId();
        model.setFSALDEPTID(new DistributorERPEntry.ERPSellDepId(erpDepartmentId));
        model.setFGroup(new DistributorERPEntry.ERPGroup(distributor.getErpGroupNo()));
        model.setFCustTypeId(new DistributorERPEntry.ERPCusType(distributor.getErpCategoryNo()));
        model.setF_PAEZ_KHGS("B2B");
        // 自动下推出库 1.是 0.否
        if (distributor.getAutoDelivery() != null && distributor.getAutoDelivery().equals(ThirdCommonConstant.AUTO_DELIVERY_1)) {
            model.setF_SOZF(true);
        } else {
            model.setF_SOZF(false);
        }
        DistributorFinancialRpcDO financial = distributor.getFinancial();
        // 结算方式类型，1为立即支付，2为期间结算
        if (financial.getPayWay() != null && financial.getPayWay().equals(ThirdCommonConstant.PAY_WAY_1)) {
            model.setF_PAEZ_YETOB2B("1");
        } else {
            model.setF_PAEZ_YETOB2B("0");
        }
        // ERP余额是否同步 1.是 0.否
        model.setF_PAEZ_YEB2BView(String.valueOf(financial.getErpBalanceFlag()));
        model.setFRECCONDITIONID(new DistributorERPEntry.ERPFrecondition(financial.getErpSettleAccountNo()));
        if (country != null && StringUtils.isNotBlank(country.getErpCountryCode())) {
            model.setFCOUNTRY(new DistributorERPEntry.ERPCountry(country.getErpCountryCode()));
        } else {
            model.setFCOUNTRY(new DistributorERPEntry.ERPCountry("CHN"));
        }
        DistributorAddressRpcDTO address = distributor.getAddress();
        model.setFZIP(address.getZipCode());
        model.setFADDRESS(address.getAddress());
        DistributorContactsRpcDTO contacts = distributor.getContacts();
        model.setF_PAEZ_Text(contacts.getName());
        model.setFTEL(contacts.getPhone());
        // 结算币别 货币类型 1-人民币 2-美元
        if (financial.getCoinType() != null && financial.getCoinType().equals(ThirdCommonConstant.COIN_TYPE_2)) {
            model.setFTRADINGCURRID(new DistributorERPEntry.ERPTrade("PRE007"));
        } else {
            model.setFTRADINGCURRID(new DistributorERPEntry.ERPTrade("PRE001"));
        }
        model.setFtaxRate(new DistributorERPEntry.ErpFtaxRate("SL02_SYS"));
        if (financial != null && financial.getTaxType() != null && financial.getTaxType().equals(ThirdCommonConstant.TAX_TYPE_1)) {
            model.setFTaxType(new DistributorERPEntry.ErpFtaxType("SFL02_SYS"));
        } else {
            model.setFTaxType(new DistributorERPEntry.ErpFtaxType("SFL01_SYS"));
        }
        model.setFINVOICETITLE(financial.getInvoiceTitleName());
        model.setFTAXREGISTERCODE(financial.getTaxpayerNumber());
        model.setFINVOICEBANKNAME(financial.getRegisteredBankDepositName());
        model.setFINVOICEBANKACCOUNT(financial.getRegisteredBankAccount());
        model.setFINVOICETEL(financial.getRegisteredPhone());
        model.setFINVOICEADDRESS(financial.getRegisteredAddress());
        return request;
    }

    public static DistributorERPCheckRequest toDistributorERPCheckRequest(String orgId, Integer erpDistributorId) {
        DistributorERPCheckRequest checkRequest = new DistributorERPCheckRequest();
        checkRequest.setCreateOrgId(orgId);
        checkRequest.setNumbers(new ArrayList<String>());
        checkRequest.setIds(String.valueOf(erpDistributorId));
        return checkRequest;
    }

    public static DistributorErpRpcCmd toDistributorErpRpcCmd(Integer distributorId, Integer erpId, String erpNo) {
        DistributorErpRpcCmd erpRpcCmd = new DistributorErpRpcCmd();
        erpRpcCmd.setDistributorId(distributorId);
        erpRpcCmd.setErpId(erpId);
        erpRpcCmd.setErpNo(erpNo);
        return erpRpcCmd;
    }

}
