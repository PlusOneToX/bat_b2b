package com.bat.thirdparty.erp.service.executor;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.erp.convertor.ErpDistributorConvertor;
import com.bat.thirdparty.erp.dao.dataobject.CountryComparisonDO;
import com.bat.thirdparty.erp.k3cloudwebapiclient.K3CloudApiClient;
import com.bat.thirdparty.erp.manager.ErpDataManager;
import com.bat.thirdparty.message.service.MessageSendService;
import com.bat.dubboapi.system.region.api.SystemRegionServiceRpc;
import com.bat.dubboapi.system.region.dto.data.RegionRpcDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.DistributorErpRpcCmd;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorERPRpcDTO;
import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.user.api.SystemUserServiceRpc;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;
import com.bat.thirdparty.erp.api.request.DistributorERPCheckRequest;
import com.bat.thirdparty.erp.api.request.DistributorERPRequest;
import com.bat.thirdparty.erp.api.response.DistributorERPResponse;
import com.bat.thirdparty.erp.api.response.dto.DistributorERPEntity;
import com.bat.thirdparty.erp.dao.CountryComparisonMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/20 9:37
 */
@Slf4j
@Component
public class DistributorExe {

    @DubboReference(check = false, timeout = 10000)
    private SystemUserServiceRpc userServiceRpc;
    @Resource
    private CountryComparisonMapper countryComparisonMapper;
    @Resource
    private ErpDataManager erpDataManager;
    @DubboReference(check = false, timeout = 10000)
    private DistributorServiceRpc distributorServiceRpc;

    @Resource
    private MessageSendService messageSendService;

    @Resource
    private HttpServletRequest request;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private SystemRegionServiceRpc systemRegionServiceRpc;

    /**
     * 客户同步erp
     * 
     * @return
     * @throws Exception
     */
    public void syncDistributorToERP(Integer distributorId) throws Exception {
        com.bat.dubboapi.distributor.common.Response<DistributorERPRpcDTO> erpRpcDTOResponse =
            distributorServiceRpc.getDistributorERPData(distributorId);
        if (!erpRpcDTOResponse.isSuccess()) {
            throw ThirdPartyException.buildException(erpRpcDTOResponse.getErrCode(), erpRpcDTOResponse.getErrMessage());
        }
        DistributorERPRpcDTO distributor = erpRpcDTOResponse.getData();
        Response<UserRpcDTO> userRpcDTOResponse = userServiceRpc.getUserById(distributor.getSalesId());
        if (!userRpcDTOResponse.isSuccess()) {
            throw ThirdPartyException.buildException(userRpcDTOResponse.getErrCode(),
                userRpcDTOResponse.getErrMessage());
        }
        UserRpcDTO sales = userRpcDTOResponse.getData();
        K3CloudApiClient apiClient = erpDataManager.login();
        DistributorERPCheckRequest checkRequest = null;
        // 分销商修改，需要调用反审接口（分销商已存在情况）
        if (distributor.getErpId() != null && distributor.getErpId() != 0) {
            checkRequest = ErpDistributorConvertor.toDistributorERPCheckRequest(
                sales.getOrganizationDTO().getErpOrganizationId(), distributor.getErpId());
            String checkResponseJson = apiClient.unAudit("BD_Customer", JSONObject.toJSONString(checkRequest));
            // 判断反审核是否成功
            checkErpResponse(checkResponseJson, distributorId, "分销商同步erp反审核");
        }
        // 新建或修改客户
        CountryComparisonDO country = null;
        Response<RegionRpcDTO> regionById = systemRegionServiceRpc.getRegionById(distributor.getAddress().getCountryId());
        if(regionById.isSuccess()){
            RegionRpcDTO data = regionById.getData();
            if(data!=null&&StringUtils.isNotBlank(regionById.getData().getErpCode())){
                country =new CountryComparisonDO();
                country.setErpCountryCode(regionById.getData().getErpCode());
            }
        }
        DistributorERPRequest request = ErpDistributorConvertor.toDistributorERPRequest(distributor, sales, country);
        String responseJson = apiClient.save("BD_Customer", JSONObject.toJSONString(request));
        // 判断新建修改是否成功
        DistributorERPResponse erpResponse = checkErpResponse(responseJson, distributorId, "分销商同步erp新建修改");
        List<DistributorERPEntity> successEntitys = erpResponse.getResult().getResponseStatus().getSuccessEntitys();
        DistributorERPEntity erpEntity = successEntitys.get(0);
        // 客户提交
        checkRequest = ErpDistributorConvertor
            .toDistributorERPCheckRequest(sales.getOrganizationDTO().getErpOrganizationId(), erpEntity.getId());
        String checkResponseJson = apiClient.submit("BD_Customer", JSONObject.toJSONString(checkRequest));
        // 判断提交是否成功
        checkErpResponse(checkResponseJson, distributorId, "分销商同步erp提交");
        // 客户审核
        checkResponseJson = apiClient.audit("BD_Customer", JSONObject.toJSONString(checkRequest));
        // 判断审核是否成功
        checkErpResponse(checkResponseJson, distributorId, "分销商同步erp审核");
        if (distributor.getErpId() == null) {
            // 更新分销商erp数据
            DistributorErpRpcCmd erpRpcCmd =
                ErpDistributorConvertor.toDistributorErpRpcCmd(distributorId, erpEntity.getId(), erpEntity.getNumber());
            distributorServiceRpc.updateDistributorErpData(erpRpcCmd);
        }
    }

    /**
     * 判断erp同步结果是否成功
     * 
     * @param responseJson
     * @return
     */
    private DistributorERPResponse checkErpResponse(String responseJson, Integer distributorId, String operateType) {
        String operateDes = "同步成功";
        boolean isFail1 = false;
        boolean isFail2 = false;
        DistributorERPResponse response = JSON.parseObject(responseJson, DistributorERPResponse.class);
        if (response == null || response.getResult() == null || response.getResult().getResponseStatus() == null) {
            operateDes = "同步erp异常：返回值null";
            isFail1 = true;
        }
        DistributorERPResponse.DistributorResponseStatus statusUnAudit = response.getResult().getResponseStatus();
        if (!statusUnAudit.isIsSuccess()) {
            operateDes = "同步erp异常:" + statusUnAudit.toString();
            isFail2 = true;

        }
        messageSendService.distributorLogPackage(distributorId, operateType, operateDes,
            JSONObject.toJSONString(responseJson));
        if (isFail1) {
            throw ThirdPartyException.buildException(ErrorCode.B_ERP_SYNC_DISTRIBUTOR_NULL_ERROR,
                MessageUtils.get("B_ERP_SYNC_DISTRIBUTOR_NULL_ERROR"));
        }
        if (isFail2) {
            throw ThirdPartyException.buildException(ErrorCode.B_ERP_SYNC_DISTRIBUTOR_NULL_ERROR,
                "同步erp异常:" + statusUnAudit.toString());
        }
        return response;
    }

}
