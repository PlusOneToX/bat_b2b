package com.bat.flexible.api.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.api.exchange.dto.order.ExchangeAdminUnboundCmd;
import com.bat.flexible.dao.exchange.co.ExchangeCodePageCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.api.exchange.dto.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.InputStream;
import java.util.List;

public interface ExchangeCodeServiceI {
    PageInfo<List<ExchangeCodePageCO>> page(ExchangeCodePageQry exchangeCodePageQry);

    /**
     * 批量作废
     * @param requestList
     * @param currentAdmin
     * @return
     */
    Response invalid(List<ExchangeCodeStatusRequest> requestList, AdminResponse currentAdmin);

    /**
     * 生成兑换码
     * @param exchangeFactoryRequest
     * @param currentAdmin
     * @return
     */
    Response createCode(ExchangeFactoryRequest exchangeFactoryRequest, AdminResponse currentAdmin);

    void syncFactory(ExchangeCardDO exchangeCardDO, AdminResponse currentAdmin, Integer additionalCount);

    Response importCode(InputStream inputStream, AdminResponse currentAdmind, Short isEntity);

    /**
     * 设置excel导入的兑换码和明码
     * @param exchangeCardRequest
     */
    void checkExcelImportCode(ExchangeCardCmd exchangeCardRequest);

    /**
     * 保存excel导入的码
     * @param importBeanList
     * @param exchangeCardDO
     * @param b
     * @param codeKey
     */
    void saveExcelImportCode(List<ExchangeCodeImportBean> importBeanList, ExchangeCardDO exchangeCardDO, boolean b, String codeKey);

    HSSFWorkbook createExcelByCondition(ExchangeCodePageQry exchangeCodePageQry, AdminResponse currentUser, String currentLanguage);

    /**
     * 设置盒码
     * @param list
     * @return
     */
    Response setBoxCode(List<ExchangeBoxCodeRequest> list,Integer exchangeId);

    void cancelOrder(Integer userOrderId);

    Response txt();

	PageInfo pageOrder(ExchangeCodeOrderQry exchangeCodeOrderQry);

	HSSFWorkbook createExchangeExcelByCondition(ExchangeCodeOrderQry exchangeCodeOrderQry, AdminResponse currentUser,
			String currentLanguage);

    void createEndExchangeEvent(String time, Integer exchangeId);

    List<ExchangeCodeDO> listByOutboundNoGroupByBoxCode(String outboundNo);

    List<ExchangeCodeDO> listByBoxCode(String boxCode);

    void update(ExchangeCodeDO code);

    /**
     * 根据明码或者暗码查询
     * @param plainCode
     * @param secortCodeEncode
     * @return
     */
    ExchangeCodeDO findByPlainCodeAndSecretCode(String plainCode, String secortCodeEncode);

    /**
     * 根据盒码和明码列表查询兑换码
     * @param boxCode
     * @param plainCodeList
     * @return
     */
    List<ExchangeCodeDO> listByBoxCodeAndPlainCodeList(String boxCode, List<String> plainCodeList);

    List<ExchangeCodeDO> listByExchangeId(Integer exchangeId);

    InputStream tempDownLoad(DownLoadRequest downLoadRequest);

    /**
     * 根据兑换码id列表和材质id、型号id、查询兑换码列表
     * @param materialId
     * @param modelId
     * @param exchangeCodeIds
     * @return
     */
    List<ExchangeCodeDO> listByExchangeCodeIdListAndMaterialIdAndModelId(Integer materialId, Integer modelId, List<Integer> exchangeCodeIds);

    List<ExchangeCodeDO> listBySecretCodeList(List<String> secretCodeList);

    /**
     * 根据C端核销订单id查询兑换码列表
     * @param orderId
     * @return
     */
    List<ExchangeCodeDO> listByUserOrderId(Integer orderId);


    /**
     * 根据发卡分销商，生成已激活的电子兑换码
     * @param exchangeId
     * @param distributorId
     * @param outNum
     * @param currentAdmin
     * @return
     */
    List<ExchangeCodeDO> createEleActivateCode(Integer exchangeId,Integer distributorId,Integer outNum, AdminResponse currentAdmin);

    /**
     * 批量修改
     * @param updateList
     */
    void batchUpdate(List<ExchangeCodeDO> updateList);

    Integer quanyiExchange(ExchangeQuanyiCmd cmd);

    /**
     * 后台兑换码解绑
     * @param exchangeAdminUnboundCmd
     * @return
     */
    Response unbound(ExchangeAdminUnboundCmd exchangeAdminUnboundCmd);

}
