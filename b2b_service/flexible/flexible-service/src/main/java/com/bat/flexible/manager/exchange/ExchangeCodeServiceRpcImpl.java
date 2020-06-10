package com.bat.flexible.manager.exchange;

import com.alibaba.fastjson.JSON;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.manager.exchange.executor.ExchangeCodeCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCodeQryExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeInvalidLogCmdExe;
import com.bat.flexible.manager.exchange.validator.ExchangeCodeValidator;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.exchange.ExchangeCodeServiceRpc;
import com.bat.dubboapi.flexible.exchange.dto.*;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyExchangeServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.exchange.ExchangeCodeInvalidCodeDTO;
import com.bat.dubboapi.thirdparty.quanyi.api.ThirdQuanyiServiceRpc;
import com.bat.flexible.api.exchange.ExchangeCardServiceI;
import com.bat.flexible.api.exchange.ExchangeCodeServiceI;
import com.bat.flexible.api.exchange.ExchangeFactoryServiceI;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeInvalidLogDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeFactoryDO;
import com.bat.flexible.manager.common.constant.exchange.ExchangeCodeConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeConstant;
import com.bat.flexible.manager.common.utils.code.AESUtil;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.stream.Collectors;

@DubboService
public class ExchangeCodeServiceRpcImpl implements ExchangeCodeServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeCodeServiceRpcImpl.class);

    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;

    @Autowired
    private ExchangeCardServiceI exchangeCardServiceI;

    @Autowired
    private ExchangeCodeQryExe exchangeCodeQryExe;

    @DubboReference(check = false,timeout = 1000000,retries = 0)
    private ThirdQuanyiServiceRpc thirdQuanyiServiceRpc;

    @Autowired
    private ExchangeCodeCmdExe exchangeCodeCmdExe;

    @Autowired
    private ExchangeCodeInvalidLogCmdExe exchangeCodeInvalidLogCmdExe;

    @Autowired
    private ExchangeFactoryServiceI exchangeFactoryServiceI;

    @DubboReference(check = false,timeout = 1000000,retries = 0)
    private ThirdPartyExchangeServiceErpRpc thirdPartyExchangeServiceErpRpc;

    @Override
    public Response<List<ExchangeCodeB2BOrderDTORpcQry>> listB2BOrderMsgBySecretCodeList(List<String> secretCodeList) {
        LOGGER.info("根据暗码列表查询兑换码B端订单信息、参数{}",JSON.toJSONString(secretCodeList));
        if (secretCodeList ==null || secretCodeList.size()==0){
            return Response.of(null);
        }

        List<ExchangeCodeDO> exchangeCodeDOList = exchangeCodeServiceI.listBySecretCodeList(secretCodeList);
        if(exchangeCodeDOList ==null || exchangeCodeDOList.size() -secretCodeList.size() !=0){
            return Response.buildFailure(ExchangeCardErrorCode.EXCHANGE_CARD_SECRET_CODE_ERROR, MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_SECRET_CODE_ERROR));
        }
        List<ExchangeCodeB2BOrderDTORpcQry> list = BeanUtils.copyList(exchangeCodeDOList,ExchangeCodeB2BOrderDTORpcQry.class);
        List<Integer> exchangeIdList = list.stream().map(ExchangeCodeB2BOrderDTORpcQry::getExchangeId).collect(Collectors.toList());
        List<ExchangeCardDO> exchangeCardDOList = exchangeCardServiceI.listByIdList(exchangeIdList);
        Map<Integer, ExchangeCardDO> exchangeCardDOMap = exchangeCardDOList.stream().collect(Collectors.toMap(ExchangeCardDO::getId, exchangeCardDO -> exchangeCardDO));

        list.stream().forEach(exchangeCodeB2BOrderDTORpcQry -> {
            //解密
            exchangeCodeB2BOrderDTORpcQry.setSecretCode(AESUtil.decrypt(exchangeCodeB2BOrderDTORpcQry.getSecretCode()));
            ExchangeCardDO exchangeCardDO=exchangeCardDOMap.get(exchangeCodeB2BOrderDTORpcQry.getExchangeId());
            if(exchangeCardDO!=null){
                exchangeCodeB2BOrderDTORpcQry.setExchangeWay(exchangeCardDO.getExchangeWay());
            }
        });
        LOGGER.info("根据暗码列表查询兑换码B端订单信息、返回{}",JSON.toJSONString(list));
        return Response.of(list);
    }

    @Override
    public Response<List<ExchangeCodeDecryptRpcDTO>> decryptSecretCodes(List<String> encryptSecretCodes) {
        LOGGER.info("已加密暗码参数:{}", JSON.toJSONString(encryptSecretCodes));
        if (encryptSecretCodes == null || encryptSecretCodes.size() == 0) {
            return Response.of(new ArrayList<>());
        }

        List<ExchangeCodeDecryptRpcDTO> list = new ArrayList<>();
        for (String encryptSecretCode : encryptSecretCodes) {
            ExchangeCodeDecryptRpcDTO exchangeCodeDecrypt = new ExchangeCodeDecryptRpcDTO();
            exchangeCodeDecrypt.setEncryptSecretCode(encryptSecretCode);
            exchangeCodeDecrypt.setDecryptSecretCode(AESUtil.decrypt(encryptSecretCode));
            list.add(exchangeCodeDecrypt);
        }

        LOGGER.info("解密处理后的数据:{}", JSON.toJSONString(list));
        return Response.of(list);
    }

    /**
     * 设置核销订单信息
     * @param list 核销兑换码列表
     * @return
     */
    @Transactional
    @Override
    public Response setUseOrderMsg(List<ExchangeCodeUseCmd> list) {
        LOGGER.info("核销订单、设置兑换码核销信息：{}", JSON.toJSON(list));
        if(list ==null || list.size()==0){
            return Response.buildSuccess();
        }
        try {
            list.stream().forEach( exchangeCodeUseCmd -> {
                List<String> secretCodeList = exchangeCodeUseCmd.getSecretCodeList();
                secretCodeList.stream().forEach(secretCode -> {
                    ExchangeCodeDO exchangeCodeDO = exchangeCodeServiceI.findByPlainCodeAndSecretCode(null,secretCode);
                    ExchangeCodeValidator.validExchangeCodeUse(exchangeCodeDO,secretCode);
                    exchangeCodeDO.setStatus(ExchangeCodeConstant.StatusUsed);
                    exchangeCodeDO.setUserId(exchangeCodeUseCmd.getUserId().longValue());
                    exchangeCodeDO.setUserName(exchangeCodeUseCmd.getUserName());
                    exchangeCodeDO.setUserOrderId(exchangeCodeUseCmd.getOrderId());
                    exchangeCodeDO.setUserOrderGoodsId(exchangeCodeUseCmd.getOrderGoodId());
                    exchangeCodeDO.setUpdateTime(new Date());
                    exchangeCodeDO.setUseTime(new Date());
                    exchangeCodeServiceI.update(exchangeCodeDO);
                    //处理权益逻辑
                  com.bat.dubboapi.thirdparty.common.Response response= thirdQuanyiServiceRpc.destroy(exchangeCodeDO.getId(),exchangeCodeUseCmd.getOrderId());
                  if(!response.isSuccess()){
                     throw   FlexibleDubboApiException.buildException(ExchangeCardErrorCode.EXCHANGE_CODE_USE_EXCEPTION,MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CODE_USE_EXCEPTION));
                  }

                });
            });
            LOGGER.info("核销订单成功、设置兑换码核销信息：{}", JSON.toJSON(list));
            return Response.buildSuccess();
        } catch (FlexibleDubboApiException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            LOGGER.info("核销订单失败、设置兑换码核销信息：{}", JSON.toJSON(list));
            return Response.buildFailure(e.getCode(),e.getMsg());
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            LOGGER.info("核销订单失败、设置兑换码核销信息：{}", JSON.toJSON(list));
            return Response.buildFailure(ExchangeCardErrorCode.EXCHANGE_CODE_USE_EXCEPTION,MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CODE_USE_EXCEPTION));
        }
    }

    /**
     * 根据核销订单查询兑换码列表
     * @param orderId 核销订单id
     * @param secretCodeFlag 是否将暗码解密 true解密 false 不
     */
    @Override
    public Response<List<ExchangeCodeDTORpcQry>> listByUserOrderId(Integer orderId,Boolean secretCodeFlag) {
        List<ExchangeCodeDO> list = exchangeCodeServiceI.listByUserOrderId(orderId);
        List<ExchangeCodeDTORpcQry> exchangeCodeDTORpcQryList = BeanUtils.copyList(list, ExchangeCodeDTORpcQry.class);
        if(secretCodeFlag && exchangeCodeDTORpcQryList !=null && exchangeCodeDTORpcQryList.size()>0 ){
            //解密
            exchangeCodeDTORpcQryList.stream().forEach(exchangeCodeDTORpcQry -> {
                exchangeCodeDTORpcQry.setSecretCode(AESUtil.decrypt(exchangeCodeDTORpcQry.getSecretCode()));
            });
        }
        return Response.of(exchangeCodeDTORpcQryList);
    }

    /**
     * 根据暗码列表（未加密）查询B兑换码分页参数（返回暗码已解密）
     * @param secretCodeList
     * @return
     */
    @Override
    public Response<List<ExchangeCodePageMsgDTO>> listExchangePageMsgBySecretCodeList(List<String> secretCodeList) {
        List<ExchangeCodeDO> exchangeCodeDOList = exchangeCodeServiceI.listBySecretCodeList(secretCodeList);
        if(exchangeCodeDOList ==null /*|| exchangeCodeDOList.size() -secretCodeList.size() !=0*/){
            return Response.buildFailure(ExchangeCardErrorCode.EXCHANGE_CARD_SECRET_CODE_ERROR, MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_SECRET_CODE_ERROR));
        }
        List<ExchangeCodePageMsgDTO> list  = BeanUtils.copyList(exchangeCodeDOList,ExchangeCodePageMsgDTO.class);
        List<Integer> exchangeIdList = list.stream().map(ExchangeCodePageMsgDTO::getExchangeId).collect(Collectors.toList());
        List<ExchangeCardDO> exchangeCardDOList = exchangeCardServiceI.listByIdList(exchangeIdList);
        Map<Integer, ExchangeCardDO> exchangeCardDOMap = exchangeCardDOList.stream().collect(Collectors.toMap(ExchangeCardDO::getId, exchangeCardDO -> exchangeCardDO));
        list.stream().forEach(exchangeCodePageMsgDTO -> {
            ExchangeCardDO exchangeCardDO = exchangeCardDOMap.get(exchangeCodePageMsgDTO.getExchangeId());
            exchangeCodePageMsgDTO.setCodeName(exchangeCardDO.getCodeName());
            //解密
            exchangeCodePageMsgDTO.setSecretCode(AESUtil.decrypt(exchangeCodePageMsgDTO.getSecretCode()));
        });
        return Response.of(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response invalid(ExchangeCodeStatusCmd cmd) {
        try {
            List<ExchangeCodeInvalidCodeDTO> list = new ArrayList<>();
            Map<Integer, Integer> map = new HashMap<>();
            ExchangeCodeDO exchangeCodeDO = exchangeCodeQryExe.getById(cmd.getId());
            if (exchangeCodeDO.getStatus() != null && exchangeCodeDO.getStatus() - ExchangeCodeConstant.StatusUsed == 0) {
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_INVALID_FAIL_BY_USE);
            }
            if (exchangeCodeDO.getStatus() != null && exchangeCodeDO.getStatus() - ExchangeCodeConstant.StatusInvalid == 0) {
               return Response.buildSuccess();
            }
            if (exchangeCodeDO.getStatus() == null || exchangeCodeDO.getStatus() != ExchangeCodeConstant.StatusUnUse.shortValue()) {
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_INVALID_FAIL_BY_ONLY_NOT_USE);
            }
            exchangeCodeDO.setStatus(ExchangeCodeConstant.StatusInvalid);
            exchangeCodeDO.setUpdateUserId(cmd.getDistributorId());
            exchangeCodeDO.setUpdateUserName(cmd.getDistributorName());
            exchangeCodeDO.setUpdateTime(new Date());
            exchangeCodeCmdExe.update(exchangeCodeDO);
            AdminResponse currentAdmin = new AdminResponse();
            currentAdmin.setId(cmd.getDistributorId());
            currentAdmin.setUserName(cmd.getDistributorName());
            //作废记录
            createInvalidLog(currentAdmin, cmd.getReason(), ExchangeConstant.ExchangeCodeInvalidTypeB2B, exchangeCodeDO);
            if (exchangeCodeDO.getStatus() - ExchangeCodeConstant.StatusInit != 0) {
                Integer count = map.get(exchangeCodeDO.getExchangeId());
                if (count == null) {
                    count = 0;
                }
                count++;
                map.put(exchangeCodeDO.getExchangeId(), count);
            } else {
                //初始状态、作废数量加1
                ExchangeCardDO exchangeCardDO = exchangeCardServiceI.getById(exchangeCodeDO.getExchangeId());
                exchangeCardDO.setInvalidCount(exchangeCardDO.getInvalidCount() == null ? 1 : exchangeCardDO.getInvalidCount() + 1);
                exchangeCardServiceI.updateDO(exchangeCardDO);
                //待同步到工厂数量加1
                ExchangeFactoryDO exchangeFactoryDO = exchangeFactoryServiceI.getById(exchangeCodeDO.getExchangeFactoryId());
                exchangeFactoryDO.setInvalidQuantityInit(exchangeFactoryDO.getInvalidQuantityInit() == null ? 1 : exchangeFactoryDO.getInvalidQuantityInit() + 1);
                exchangeFactoryServiceI.update(exchangeFactoryDO);
            }
            if (StringUtils.isNotBlank(exchangeCodeDO.getBoxCode())) {
                //还没生成盒码才需要同步至erp
                Boolean flag = false;
                if (list != null && list.size() > 0) {
                    for (int x = 0; x < list.size(); x++) {
                        if (list.get(x).getF_HCode().equals(exchangeCodeDO.getBoxCode())) {
                            list.get(x).getF_MCode().add(exchangeCodeDO.getPlainCode());
                            //已存在盒码
                            flag = true;
                            break;
                        }
                    }
                }
                //新增
                if (!flag) {
                    ExchangeCodeInvalidCodeDTO codeInvalidCodeDTO = getExchangeCodeInvalidCodeDTO(currentAdmin.getUserName(), cmd.getReason(), exchangeCodeDO.getPlainCode());
                    LOGGER.info("b2b--> erp明码失效、参数为：" + JSON.toJSONString(codeInvalidCodeDTO));
                    com.bat.dubboapi.thirdparty.common.Response response = thirdPartyExchangeServiceErpRpc.syncExchangeCodeInvalidToErp(codeInvalidCodeDTO);
                    LOGGER.info("b2b--> erp明码失效、响应为：" + JSON.toJSONString(response));
                    if (!response.isSuccess()) {
                        throw new FlexibleCustomException(MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_SYNC_INVALID_ERP_FAIL) + response.getErrMessage());
                    }
                }
            }

            //处理退货
            if (map != null && map.size() > 0) {
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator.next();
                    ExchangeCardDO exchangeCardDO = exchangeCardServiceI.getById(entry.getKey());
                    exchangeCardDO.setRefundQuantity(exchangeCardDO.getRefundQuantity() == null ? entry.getValue() : exchangeCardDO.getRefundQuantity() + entry.getValue());
                    exchangeCardServiceI.updateDO(exchangeCardDO);
                }
            }
            return Response.buildSuccess();
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Response.buildFailure(FlexibleCommonErrorCode.COMMON_DEAL_ERROR,MessageUtils.get(FlexibleCommonErrorCode.COMMON_DEAL_ERROR));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response unInvalid(ExchangeCodeStatusCmd cmd) {
        try {
            ExchangeCodeDO exchangeCodeDO = exchangeCodeQryExe.getById(cmd.getId());
            if (exchangeCodeDO.getStatus() != null && exchangeCodeDO.getStatus() - ExchangeCodeConstant.StatusUsed == 0) {
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_INVALID_FAIL_BY_USE);
            }
            if (exchangeCodeDO.getStatus() != null && exchangeCodeDO.getStatus() - ExchangeCodeConstant.StatusUnUse == 0) {
               return Response.buildSuccess();
            }
            if (exchangeCodeDO.getStatus() == null || exchangeCodeDO.getStatus() != ExchangeCodeConstant.StatusInvalid.shortValue()) {
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_UN_INVALID_FAIL_BY_ONLY_INVALID);
            }
            exchangeCodeDO.setStatus(ExchangeCodeConstant.StatusUnUse);
            exchangeCodeDO.setUpdateUserId(cmd.getDistributorId());
            exchangeCodeDO.setUpdateUserName(cmd.getDistributorName());
            exchangeCodeDO.setUpdateTime(new Date());
            exchangeCodeCmdExe.update(exchangeCodeDO);
            AdminResponse currentAdmin = new AdminResponse();
            currentAdmin.setId(cmd.getDistributorId());
            currentAdmin.setUserName(cmd.getDistributorName());
            ExchangeCardDO exchangeCardDO = exchangeCardServiceI.getById(exchangeCodeDO.getExchangeId());
            if(exchangeCardDO.getRefundQuantity()==null||exchangeCardDO.getRefundQuantity()<=0){
                exchangeCardDO.setRefundQuantity(0);
            }else{
                exchangeCardDO.setRefundQuantity(exchangeCardDO.getRefundQuantity()-1);
            }
            exchangeCardServiceI.updateDO(exchangeCardDO);
            return Response.buildSuccess();
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.buildFailure("兑换卡撤销作废失败！","兑换卡撤销作废失败");
        }
    }

    /**
     * 生成作废记录
     * @param currentAdmin 操作人信息
     * @param reason 作废原因
     * @param type 类型
     * @param exchangeCode
     */
    private void createInvalidLog(AdminResponse currentAdmin, String reason, Short type, ExchangeCodeDO exchangeCode) {
        ExchangeCodeInvalidLogDO invalidLog = new ExchangeCodeInvalidLogDO();
        invalidLog.setCodeId(exchangeCode.getId());
        invalidLog.setExchangeId(exchangeCode.getExchangeId());
        invalidLog.setCreateUserId(currentAdmin.getId());
        invalidLog.setCreateTime(new Date());
        invalidLog.setCreateUserName(currentAdmin.getUserName());
        invalidLog.setReason(reason);
        invalidLog.setType(type);
        exchangeCodeInvalidLogCmdExe.create(invalidLog);
    }

    /**
     * 获取作废明码盒码参数
     * @param name 作废人
     * @param reason 作废原因
     * @param plainCode 明码
     * @return
     */
    private ExchangeCodeInvalidCodeDTO getExchangeCodeInvalidCodeDTO(String name, String reason, String plainCode) {
        ExchangeCodeInvalidCodeDTO codeInvalidCodeDTO = new ExchangeCodeInvalidCodeDTO();
        //盒码明码二选一
        //codeInvalidCodeDTO.setF_HCode(exchangeCode.getBoxCode());
        codeInvalidCodeDTO.setF_PAEZ_REMARK(reason);
        List<String> plainCodeList = new ArrayList<>();
        plainCodeList.add(plainCode);
        codeInvalidCodeDTO.setF_MCode(plainCodeList);
        codeInvalidCodeDTO.setCancelTime(new Date());
        codeInvalidCodeDTO.setF_CancelUser(name);
        return codeInvalidCodeDTO;
    }
}
