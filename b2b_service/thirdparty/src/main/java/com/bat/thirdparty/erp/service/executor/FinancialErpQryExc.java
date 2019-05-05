package com.bat.thirdparty.erp.service.executor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.thirdparty.erp.k3cloudwebapiclient.K3CloudApiClient;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.thirdparty.erp.dto.financial.BalanceInfoRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.BalanceInfoDTO;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyRateErpRpcDTO;
import com.bat.thirdparty.erp.api.request.BalanceRequest;
import com.bat.thirdparty.erp.api.request.BillQueryRequest;
import com.bat.thirdparty.erp.api.response.BalanceInfoResponse;
import com.bat.thirdparty.erp.manager.ErpDataManager;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/17 14:51
 */
@Component
@Slf4j
public class FinancialErpQryExc {
    @Resource
    private ErpDataManager erpDataManager;

    public List<CurrencyErpRpcDTO> listCurrency() throws Exception {
        K3CloudApiClient client = erpDataManager.login();
        BillQueryRequest request = new BillQueryRequest();
        request.setFormId("BD_Currency");
        request.setFieldKeys("FNumber,FName,FCODE,FIsShowCSymbol,FAMOUNTDIGITS,FForbidStatus");
        String json = JSONObject.toJSONString(request);
        List<List<Object>> result = client.executeBillQuery(json);
        List<CurrencyErpRpcDTO> list = new ArrayList<>();
        if (result != null && !result.isEmpty()) {
            for (List<Object> objects : result) {
                CurrencyErpRpcDTO currencyErpRpcDTO = new CurrencyErpRpcDTO();
                currencyErpRpcDTO.setErpNo(objects.get(0) != null ? objects.get(0).toString() : null);
                currencyErpRpcDTO.setName(objects.get(1) != null ? objects.get(1).toString() : null);
                currencyErpRpcDTO.setCurrencyCode(objects.get(2) != null ? objects.get(2).toString() : null);
                currencyErpRpcDTO
                    .setMoneyAccuracy(objects.get(4) != null ? Short.valueOf(objects.get(4).toString()) : null);
                currencyErpRpcDTO.setOpenFlag(
                    objects.get(5) != null ? objects.get(5).toString().equals("A") ? (short)1 : (short)0 : null);
                list.add(currencyErpRpcDTO);
            }
            return list;
        }
        return list;
    }

    public List<CurrencyRateErpRpcDTO> listCurrencyRate() throws Exception {
        K3CloudApiClient client = erpDataManager.login();
        BillQueryRequest request = new BillQueryRequest();
        request.setFormId("BD_Rate");
        request.setFieldKeys(
            "FRateID,FExchangeRate,FReverseExRate,FRATETYPEID.FName,FCyForID.FCODE,FCyToID.FCODE,FBegDate,FEndDate,FDocumentStatus");
        StringBuilder filter = new StringBuilder();
        // 固定汇率
        filter.append("FRATETYPEID.FNumber = 'HLTX01_SYS'");
        request.setFilterString(filter.toString());
        String json = JSONObject.toJSONString(request);
        List<List<Object>> result = client.executeBillQuery(json);
        List<CurrencyRateErpRpcDTO> list = new ArrayList<>();
        if (result != null && !result.isEmpty()) {
            for (List<Object> objects : result) {
                CurrencyRateErpRpcDTO currencyRateErpRpcDTO = new CurrencyRateErpRpcDTO();
                currencyRateErpRpcDTO
                    .setRateId(objects.get(0) != null ? Integer.valueOf(objects.get(0).toString()) : null);
                currencyRateErpRpcDTO
                    .setExchangeRate(objects.get(1) != null ? Double.valueOf(objects.get(1).toString()) : null);
                currencyRateErpRpcDTO
                    .setReverseExRate(objects.get(2) != null ? Double.valueOf(objects.get(2).toString()) : null);
                currencyRateErpRpcDTO.setCyForid(objects.get(4) != null ? objects.get(4).toString() : null);
                currencyRateErpRpcDTO.setCyToid(objects.get(5) != null ? objects.get(5).toString() : null);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currencyRateErpRpcDTO.setBegDate(
                    objects.get(6) != null ? dateFormat.parse(objects.get(6).toString().replace("T", " ")) : null);
                currencyRateErpRpcDTO.setEndDate(
                    objects.get(7) != null ? dateFormat.parse(objects.get(7).toString().replace("T", " ")) : null);
                currencyRateErpRpcDTO.setDocumentStatus(objects.get(8) != null ? objects.get(8).toString() : null);
                list.add(currencyRateErpRpcDTO);
            }
            return list;
        }
        return list;
    }

    public List<BalanceInfoDTO> listBalance(BalanceInfoRpcQry qry) {
        BalanceRequest request = new BalanceRequest();
        List<String> fcustIdList = new ArrayList<>();
        if (qry.getFCUSTID() != null && qry.getFCUSTID().size() > 0) {
            fcustIdList.addAll(request.getFCUSTID());
        }
        request.setFCUSTID(fcustIdList);
        request.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
        log.info("third party sync balance start");
        BalanceInfoResponse balance = erpDataManager.findBalance(request);
        log.info("third party listBalance: {}", balance);
        return balance.getData().stream().map(balanceInfo -> {
            BalanceInfoDTO dto = new BalanceInfoDTO();
            dto.setFCUSTID(balanceInfo.getFCUSTID());
            dto.setFZHYE(balanceInfo.getFZHYE());
            return dto;
        }).collect(Collectors.toList());
    }
}
