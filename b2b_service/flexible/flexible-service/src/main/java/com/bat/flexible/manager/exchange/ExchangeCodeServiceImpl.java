package com.bat.flexible.manager.exchange;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.*;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.util.excel.ExcelUtil;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.manager.common.utils.DateUtil;
import com.bat.flexible.manager.exchange.convertor.ExchangeCardConvertor;
import com.bat.flexible.manager.exchange.executor.ExchangeCodeCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCodeQryExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeInvalidLogCmdExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeRestoreLogCmdExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeSyncBackLogCmdExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeSyncBackLogQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.flexible.common.BaseOpenApiException;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyExchangeServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.exchange.BoxCodeCardErpRequest;
import com.bat.dubboapi.thirdparty.erp.dto.exchange.ExchangeCodeInvalidCodeDTO;
import com.bat.dubboapi.thirdparty.erp.dto.exchange.ExchangeCodeSyncERPRequest;
import com.bat.dubboapi.thirdparty.erp.dto.exchange.PlainCodeERPRequest;
import com.bat.dubboapi.thirdparty.oss.ThirdPartySystemOssServiceRpc;
import com.bat.dubboapi.thirdparty.quanyi.api.ThirdQuanyiServiceRpc;
import com.bat.dubboapi.thirdparty.quanyi.dto.ThirdQuanyiRpcCmd;
import com.bat.dubboapi.thirdparty.quanyi.dto.ThirdQuanyiRpcDTO;
import com.bat.flexible.api.base.common.parameter.BaseData;
import com.bat.flexible.api.exchange.*;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.api.exchange.dto.order.ExchangeAdminUnboundCmd;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.dao.exchange.co.ExchangeCodePageCO;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeCodeConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeErpErrorConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeExcelConstant;
import com.bat.flexible.manager.common.utils.code.AESUtil;
import com.bat.flexible.manager.common.utils.code.NumUtils;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ExchangeCodeServiceImpl implements ExchangeCodeServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeCodeServiceImpl.class);

    @Autowired
    private ExchangeCodeCmdExe exchangeCodeCmdExe;

    @Autowired
    private ExchangeCodeQryExe exchangeCodeQryExe;

    @Autowired
    private ExchangeCodeInvalidLogCmdExe exchangeCodeInvalidLogCmdExe;

    @Autowired
    private ExchangeCardServiceI exchangeCardServiceI;

    @Autowired
    private ExchangeEntityRuleServiceI exchangeEntityRuleServiceI;

    @Autowired
    private ExchangeFactoryServiceI exchangeFactoryServiceI;


    @Autowired
    private ExchangeCodeRestoreLogCmdExe exchangeCodeRestoreLogCmdExe;


    @Autowired
    private ExchangeCodeSyncBackLogCmdExe exchangeCodeSyncBackLogCmdExe;

    //保存excel导入的明码暗码
    private Cache<String,String> codeCaches;

    @DubboReference(check = false,timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;

    @Autowired
    private ExchangeCodeSyncBackLogQryExe exchangeCodeSyncBackLogQryExe;

    @Value("${importExchangeCode.tmp.virtual:import/exchange_virtual.xls}")
    private String ExcelTempExchangeVirtual;

    @Value("${importExchangeCode.tmp.entity:import/exchange_entity.xls}")
    private String ExcelTempExchangeEntity;

    @DubboReference(check = false,timeout = 10000,retries = 1)
    private ThirdPartySystemOssServiceRpc  thirdPartySystemOssServiceRpc;

    @DubboReference(check = false,timeout = 1000000,retries = 0)
    private ThirdPartyExchangeServiceErpRpc thirdPartyExchangeServiceErpRpc;

    @DubboReference(check = false,timeout = 1000000,retries = 0)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false,timeout = 1000000,retries = 0)
    private ThirdQuanyiServiceRpc thirdQuanyiServiceRpc;

    @Autowired
    private MaterialServiceI materialServiceI;




    @Autowired
    private ExchangeCodeUserServiceI exchangeCodeUserServiceI;

    @Override
    public PageInfo<List<ExchangeCodePageCO>> page(ExchangeCodePageQry exchangeCodePageQry) {

        PageHelper.startPage(exchangeCodePageQry.getPage(),exchangeCodePageQry.getSize());
        List<ExchangeCodePageCO> list  = exchangeCodeQryExe.listCOByCondition(exchangeCodePageQry.getExchangeFactoryId(),
                exchangeCodePageQry.getExchangeId(),exchangeCodePageQry.getExchangeWay(),exchangeCodePageQry.getStatus(),exchangeCodePageQry.getContent(),exchangeCodePageQry.getExchangeExportId());
        //解密
        if(list !=null && list.size()>0 && exchangeCodePageQry.getEncodeFlag()){
            list.stream().forEach(o -> {
                ExchangeCodePageCO codePageBean = (ExchangeCodePageCO) o;
                if(StringUtils.isNotBlank(codePageBean.getSecretCode())){
                    codePageBean.setSecretCode(AESUtil.decrypt(codePageBean.getSecretCode()));
                }
            });
        }
        return new PageInfo(list);
    }

    /**
     * 批量作废
     * @param requestList
     * @param currentAdmin
     * @return
     */
    @Override
    @Transactional
    public Response invalid(List<ExchangeCodeStatusRequest> requestList, AdminResponse currentAdmin) {
        List<ExchangeCodeInvalidCodeDTO> list = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        requestList.stream().forEach(exchangeCodeStatusRequest -> {
            ExchangeCodeDO exchangeCodeDO = exchangeCodeQryExe.getById(exchangeCodeStatusRequest.getId());

            if( exchangeCodeDO.getStatus() !=null && exchangeCodeDO.getStatus() - ExchangeCodeConstant.StatusUsed ==0){
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_INVALID_FAIL_BY_USE);
            }
            if( exchangeCodeDO.getStatus() !=null && exchangeCodeDO.getStatus() - ExchangeCodeConstant.StatusInvalid ==0){
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_INVALID_FAIL_BY_INVALID);
            }
            exchangeCodeDO.setStatus(ExchangeCodeConstant.StatusInvalid);
            exchangeCodeDO.setUpdateUserId(currentAdmin.getId());
            exchangeCodeDO.setUpdateUserName(currentAdmin.getUserName());
            exchangeCodeDO.setUpdateTime(new Date());
            exchangeCodeCmdExe.update(exchangeCodeDO);
            //作废记录
            createInvalidLog(currentAdmin, exchangeCodeStatusRequest.getReason(), ExchangeConstant.ExchangeCodeInvalidTypeB2B, exchangeCodeDO);
            if(exchangeCodeDO.getStatus() - ExchangeCodeConstant.StatusInit !=0){
                Integer count = map.get(exchangeCodeDO.getExchangeId());
                if(count ==null){
                    count=0;
                }
                count++;
                map.put(exchangeCodeDO.getExchangeId(), count);
            }else{
                //初始状态、作废数量加1
                ExchangeCardDO exchangeCardDO = exchangeCardServiceI.getById(exchangeCodeDO.getExchangeId());
                exchangeCardDO.setInvalidCount(exchangeCardDO.getInvalidCount()==null?1:exchangeCardDO.getInvalidCount()+1);
                exchangeCardServiceI.updateDO(exchangeCardDO);
                //待同步到工厂数量加1
                ExchangeFactoryDO exchangeFactoryDO = exchangeFactoryServiceI.getById(exchangeCodeDO.getExchangeFactoryId());
                exchangeFactoryDO.setInvalidQuantityInit(exchangeFactoryDO.getInvalidQuantityInit()==null?1:exchangeFactoryDO.getInvalidQuantityInit()+1);
                exchangeFactoryServiceI.update(exchangeFactoryDO);
            }
            if(StringUtils.isNotBlank(exchangeCodeDO.getBoxCode())){
                //还没生成盒码才需要同步至erp
                Boolean flag = false;
                if(list !=null && list.size()>0){
                    for (int x=0;x<list.size();x++){
                        if(list.get(x).getF_HCode().equals(exchangeCodeDO.getBoxCode())){
                            list.get(x).getF_MCode().add(exchangeCodeDO.getPlainCode());
                            //已存在盒码
                            flag = true;
                            break;
                        }
                    }
                }
                //新增
                if(!flag){

                    ExchangeCodeInvalidCodeDTO codeInvalidCodeDTO = getExchangeCodeInvalidCodeDTO(currentAdmin.getUserName(), exchangeCodeStatusRequest.getReason(), exchangeCodeDO.getPlainCode());
                    LOGGER.info("b2b--> erp明码失效、参数为："+JSON.toJSONString(codeInvalidCodeDTO));
                    com.bat.dubboapi.thirdparty.common.Response response = thirdPartyExchangeServiceErpRpc.syncExchangeCodeInvalidToErp(codeInvalidCodeDTO);
                    LOGGER.info("b2b--> erp明码失效、响应为："+JSON.toJSONString(response));
                    if(!response.isSuccess()){
                        throw new FlexibleCustomException(MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_SYNC_INVALID_ERP_FAIL)+response.getErrMessage());
                    }
                }
            }
        });
        //处理退货
        if(map !=null && map.size()>0){
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()){
                Entry<Integer,Integer> entry =(Entry<Integer,Integer>)iterator.next();
                ExchangeCardDO exchangeCardDO = exchangeCardServiceI.getById(entry.getKey());
                exchangeCardDO.setRefundQuantity(exchangeCardDO.getRefundQuantity()==null?entry.getValue():exchangeCardDO.getRefundQuantity()+entry.getValue());
                exchangeCardServiceI.updateDO(exchangeCardDO);
            }
        }
        return Response.buildSuccess();
    }

    /**
     * 生成作废记录
     * @param currentAdmin 操作人信息
     * @param reason 作废原因
     * @param type 类型
     * @param exchangeCode
     */
    private void createInvalidLog(AdminResponse currentAdmin, String reason,Short type, ExchangeCodeDO exchangeCode) {
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

    /**
     * 生成兑换码
     * @param exchangeFactoryRequest
     * @param currentAdmin
     * @return
     */
    @Override
    @Transactional
    public Response createCode(ExchangeFactoryRequest exchangeFactoryRequest, AdminResponse currentAdmin) {
        ExchangeCardDO exchangeCardDO = exchangeCardServiceI.getById(exchangeFactoryRequest.getExchangeId());
        if(exchangeCardDO.getStatus() - ExchangeConstant.StatusEnd ==0){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CREATE_CODE_FAIL_BY_ACTIVITY_END);
        }
        switch (exchangeCardDO.getIsEntity()){
            case 1:
                //实体卡
                syncFactory(exchangeCardDO,currentAdmin,exchangeFactoryRequest.getCount());
                break;
            case 0:
                List<ExchangeCodeDO> exchangeCodeDOList = new ArrayList<>();
                for(int x=0;x<exchangeFactoryRequest.getCount();x++){
                    String code = NumUtils.getRandomNum();
                    ExchangeCodeDO exchangeCodeDO = new ExchangeCodeDO();
                    exchangeCodeDO.setStatus(ExchangeCodeConstant.StatusInit);
                    exchangeCodeDO.setPlainCode(code);
                    exchangeCodeDO.setSecretCode(AESUtil.encrypt(NumUtils.getRandomNum()));
                    exchangeCodeDO.setExchangeId(exchangeCardDO.getId());
                    ExchangeCardConvertor.setExchangeCodeOperateMsg(currentAdmin,exchangeCodeDO);
                    exchangeCodeDOList.add(exchangeCodeDO);
                }
                exchangeCodeCmdExe.batchCreate(exchangeCodeDOList);
                exchangeCardDO.setCodeQuantity(exchangeCardDO.getCodeQuantity()+exchangeFactoryRequest.getCount());
                exchangeCardServiceI.updateDO(exchangeCardDO);
                break;
        }
        return Response.buildSuccess();
    }

    /**
     * 根据发卡分销商，生成已激活的电子兑换码
     * @param exchangeId
     * @param distributorId
     * @param outNum
     * @param currentAdmin
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExchangeCodeDO> createEleActivateCode(Integer exchangeId,Integer distributorId,Integer outNum,AdminResponse currentAdmin) {
        ExchangeCardDO exchangeCardDO = exchangeCardServiceI.getById(exchangeId);
        if (exchangeCardDO.getStatus() - ExchangeConstant.StatusEnd == 0) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CREATE_CODE_FAIL_BY_ACTIVITY_END);
        }
        if (exchangeCardDO.getIsEntity() == FlexibleCommonConstant.FLAG_YES) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_ENTITY_CANT_EXPORT);
        }
        if (outNum <= 0) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_EXPORT_NUM_ERROR);
        }
        DistributorRpcDTO distributorRpcDTO = distributorServiceRpc.distributorById(distributorId).getData();
        if (distributorRpcDTO == null) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_NO_RECORD);
        }

        List<ExchangeCodeDO> exchangeCodeDOList = new ArrayList<>();
        for (int x = 0; x < outNum; x++) {
            String code = NumUtils.getRandomNum();
            ExchangeCodeDO exchangeCodeDO = new ExchangeCodeDO();
            exchangeCodeDO.setStatus(ExchangeCodeConstant.StatusUnUse);
            exchangeCodeDO.setPlainCode("dz"+code);
            exchangeCodeDO.setSecretCode(AESUtil.encrypt(NumUtils.getRandomNum()));
            exchangeCodeDO.setExchangeId(exchangeCardDO.getId());
            exchangeCodeDO.setDistributorId(distributorId);
            exchangeCodeDO.setDistributorCompanyName(distributorRpcDTO.getCompanyName());
            exchangeCodeDO.setDistributorName(distributorRpcDTO.getName());
            ExchangeCardConvertor.setExchangeCodeOperateMsg(currentAdmin, exchangeCodeDO);
            exchangeCodeDOList.add(exchangeCodeDO);
        }
        exchangeCodeCmdExe.batchCreate(exchangeCodeDOList);
        exchangeCardDO.setCodeQuantity(exchangeCardDO.getCodeQuantity() + outNum);
        exchangeCardServiceI.updateDO(exchangeCardDO);
        return exchangeCodeDOList;
    }

    @Transactional
    @Override
    public void batchUpdate(List<ExchangeCodeDO> updateList) {
        exchangeCodeCmdExe.batchUpdate(updateList);
    }

    @Transactional
    @Override
    public Integer quanyiExchange(ExchangeQuanyiCmd cmd) {
        LOGGER.info("开始根据权益领取兑换卡:{}",JSONObject.toJSONString(cmd));
        //获取第三方编码以及分销商ID获取对应材质
        com.bat.dubboapi.thirdparty.common.Response<ThirdQuanyiRpcDTO> thirdQuanyiResponse = thirdQuanyiServiceRpc.findByDistributorIdAndThirdCode(cmd.getDistributorId(), cmd.getThirdCode());
        if (!thirdQuanyiResponse.isSuccess()||thirdQuanyiResponse.getData() == null) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_QUANYI_NOT_ESIST);
        }
        ThirdQuanyiRpcDTO thirdQuanyi=thirdQuanyiResponse.getData();
        LOGGER.info("得到的权益信息:{}",JSONObject.toJSONString(thirdQuanyi));
        if(thirdQuanyi.getExchangeCodeId()!=null){
            return thirdQuanyi.getExchangeCodeId();
        }
        if (thirdQuanyi.getCancelFlag() != null && thirdQuanyi.getCancelFlag() == FlexibleCommonConstant.FLAG_YES) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_QUANYI_SYSTEM_CANCEL);
        }
        ExchangeCardDO exchangeCardDO = exchangeCardServiceI.findQuanyiByDistributorIdAndMaterialId(cmd.getDistributorId(), thirdQuanyi.getMaterialId());
        if (exchangeCardDO == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DEAL_ERROR);
        }
        com.bat.dubboapi.distributor.common.Response<DistributorRpcDTO> distributorRpcDTOResponse=  distributorServiceRpc.distributorById(cmd.getDistributorId());
        if(!distributorRpcDTOResponse.isSuccess()||distributorRpcDTOResponse.getData()==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DEAL_ERROR);
        }
        DistributorRpcDTO distributorRpcDTO=distributorRpcDTOResponse.getData();

        MaterialDO materialDO = materialServiceI.getById(thirdQuanyi.getMaterialId());
        if (materialDO.getItemId() == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DEAL_ERROR);
        }
        //查询货品信息
        List<Integer> itemIdList = new ArrayList<>();
        itemIdList.add(materialDO.getItemId());
        com.bat.dubboapi.goods.common.Response<List<GoodsItemRpcDTO>> itemResponse = goodsServiceRpc.listGoodsItemByIds(itemIdList);
        if (!itemResponse.isSuccess() || itemResponse.getData() == null || itemResponse.getData().size() != 1) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DEAL_ERROR);
        }
        GoodsItemRpcDTO item = itemResponse.getData().get(0);
        LOGGER.info("获取到兑换货品信息:{}",JSONObject.toJSONString(item));
        GoodsItemPriceRpcQry qry = new GoodsItemPriceRpcQry();
        GoodsItemRpc goodsItemRpc = new GoodsItemRpc();
        goodsItemRpc.setItemId(item.getId());
        goodsItemRpc.setGoodsId(item.getGoodsId());
        List<GoodsItemRpc> goodsItemRpcs = new ArrayList<>();
        goodsItemRpcs.add(goodsItemRpc);
        qry.setGoodsItems(goodsItemRpcs);
        qry.setDistributorId(distributorRpcDTO.getId());
        com.bat.dubboapi.goods.common.Response<List<GoodsItemPriceRpcDTO>> goodsItemPriceResponse = goodsServiceRpc.listDistributorGoodsItemPrice(qry);
        if (!goodsItemPriceResponse.isSuccess() || goodsItemPriceResponse.getData() == null||goodsItemPriceResponse.getData().size()!=1) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DEAL_ERROR);
        }
        GoodsItemPriceRpcDTO goodsItemPriceRpcDTO= goodsItemPriceResponse.getData().get(0);
        if(goodsItemPriceRpcDTO.getSalePrice()==null||goodsItemPriceRpcDTO.getSalePrice().compareTo(BigDecimal.ZERO)<=0){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DEAL_ERROR);
        }
        LOGGER.info("获取到兑换货品价格信息:{}",JSONObject.toJSONString(goodsItemPriceRpcDTO));
        //生成兑换卡
        String code = NumUtils.getRandomNum();
        ExchangeCodeDO exchangeCodeDO = new ExchangeCodeDO();
        exchangeCodeDO.setStatus(ExchangeCodeConstant.StatusUnUse);
        exchangeCodeDO.setPlainCode("qy"+code);
        exchangeCodeDO.setSecretCode(AESUtil.encrypt(NumUtils.getRandomNum()));
        exchangeCodeDO.setExchangeId(exchangeCardDO.getId());
        exchangeCodeDO.setDistributorId(cmd.getDistributorId());
        exchangeCodeDO.setDistributorName(distributorRpcDTO.getName());
        exchangeCodeDO.setDistributorCompanyName(distributorRpcDTO.getCompanyName());
        exchangeCodeDO.setDistributorQuanyiPrice(goodsItemPriceRpcDTO.getSalePrice());
        AdminResponse currentAdmin = new AdminResponse();
        currentAdmin.setId(cmd.getDistributorId());
        currentAdmin.setUserName(distributorRpcDTO.getName());
        ExchangeCardConvertor.setExchangeCodeOperateMsg(currentAdmin, exchangeCodeDO);
        exchangeCodeCmdExe.create(exchangeCodeDO);
        LOGGER.info("生成的兑换码:{}",JSONObject.toJSONString(exchangeCodeDO));
        exchangeCardDO.setCodeQuantity(exchangeCardDO.getCodeQuantity() + 1);
        exchangeCardServiceI.updateDO(exchangeCardDO);
        //进行兑换卡绑定
        ExchangeCodeUserCmd exchangeCodeUserCmd=new ExchangeCodeUserCmd();
        exchangeCodeUserCmd.setSecretCode( AESUtil.decrypt(exchangeCodeDO.getSecretCode()));
        exchangeCodeUserCmd.setUserId(cmd.getUserId());
        LOGGER.info("兑换信息进行绑定:{}",JSONObject.toJSONString(exchangeCodeUserCmd));
        exchangeCodeUserServiceI.create(exchangeCodeUserCmd);

        //进行签到处理
        ThirdQuanyiRpcCmd thirdQuanyiRpcCmd=new ThirdQuanyiRpcCmd();
        BeanUtils.copyProperties(thirdQuanyi,thirdQuanyiRpcCmd);
        thirdQuanyiRpcCmd.setCustomerId(cmd.getUserId());
        thirdQuanyiRpcCmd.setCustomerNo(cmd.getCustomerNo());
        thirdQuanyiRpcCmd.setExchangeId(exchangeCardDO.getId());
        thirdQuanyiRpcCmd.setExchangeName(exchangeCardDO.getCodeName());
        thirdQuanyiRpcCmd.setExchangeCodeId(exchangeCodeDO.getId());
        thirdQuanyiRpcCmd.setPlainCode(exchangeCodeDO.getPlainCode());
        thirdQuanyiRpcCmd.setSecretCode(exchangeCodeDO.getSecretCode());
        LOGGER.info("开始权益签到:{}",JSONObject.toJSONString(thirdQuanyiRpcCmd));
       com.bat.dubboapi.thirdparty.common.Response response= thirdQuanyiServiceRpc.signIn(thirdQuanyiRpcCmd);
       if(!response.isSuccess()){
           LOGGER.info("权益签到失败:{}",JSONObject.toJSONString(response));
           throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DEAL_ERROR);
       }else{
           return exchangeCodeDO.getId();
       }

    }


    /**
     *
     * @param exchangeCardDO
     * @param currentAdmin
     * @param additionalCount 追加数量 null表示新增
     */
    @Override
    public void syncFactory(ExchangeCardDO exchangeCardDO, AdminResponse currentAdmin,Integer additionalCount) {
       /* if(status - ExchangeConstant.StatusUnStart !=0){
            return;
        }
        if(exchangeCard.getIsEntity() - ExchangeConstant.IsEntityNo ==0){
            return;
        }*/
        if(exchangeCardDO.getIsEntity() - ExchangeConstant.IsEntityNo ==0){
            return;
        }
        ExchangeEntityRuleDO exchangeEntityRule = exchangeEntityRuleServiceI.getByExchangeId(exchangeCardDO.getId());
        if(exchangeEntityRule ==null){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_ENTITY_RULE_NULL);
        }
        //发布同步至工厂
        ExchangeFactoryDO factory = new ExchangeFactoryDO();
        factory.setAddQuantity(additionalCount==null || additionalCount==0?exchangeCardDO.getCodeQuantity():additionalCount);
        factory.setExchangeId(exchangeCardDO.getId());
        factory.setStatus(ExchangeCodeConstant.SyncFactoryStatusUnSync);
        if(exchangeEntityRule.getIsSyncFactory() - ExchangeConstant.isSyncFactoryNo==0){
            //不需要同步
            factory.setStatus(ExchangeCodeConstant.SyncFactoryStatusSuccess);
        }
        //默认是云创
        factory.setFactory(ExchangeConstant.FactoryYunChuang);
        factory.setCreateTime(new Date());
        factory.setCreateUserId(currentAdmin.getId());
        factory.setCreateUserName(currentAdmin.getUserName());
        factory.setUpdateUserId(currentAdmin.getId());
        factory.setUpdateUserName(currentAdmin.getUserName());
        factory.setUpdateTime(new Date());
        factory.setSynchronizedQuantity(0);
        factory.setInvalidQuantityInit(0);
        exchangeFactoryServiceI.create(factory);
        if(additionalCount ==null && exchangeEntityRule.getCardType() - ExchangeConstant.CardTypeImport==0){
            //新增的时候excel导入、直接生成了、需要设置工厂同步id（之前保存该值为null）
            exchangeCodeCmdExe.updateExchangeFactoryId(exchangeCardDO.getId(),factory.getId());
            exchangeEntityRuleServiceI.syncErp(exchangeCardDO.getId(),exchangeEntityRule);
            return;
        }
        if(exchangeEntityRule.getRuleType() - ExchangeConstant.RuleTypeRule==0){
            //按照规则生成
            createCodeByRule(exchangeCardDO, currentAdmin, additionalCount, exchangeEntityRule,factory.getId());
        }else {
            //随机生成
            createCodeByRandom(exchangeCardDO, currentAdmin, additionalCount, exchangeEntityRule,factory.getId());
        }

        if(additionalCount !=null && additionalCount>0){
            //首次发布不加数量
            exchangeCardDO.setCodeQuantity(exchangeCardDO.getCodeQuantity()+factory.getAddQuantity());
        }
        exchangeCardDO.setUpdateTime(new Date());
        exchangeCardDO.setUpdateUserName(currentAdmin.getUserName());
        exchangeCardDO.setUpdateUserId(currentAdmin.getId());
        exchangeCardServiceI.updateDO(exchangeCardDO);
        exchangeEntityRuleServiceI.syncErp(exchangeCardDO.getId(),exchangeEntityRule);
    }


    @Override
    public Response importCode(InputStream inputStream, AdminResponse currentAdmind, Short isEntity) {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (Exception ex) {
            throw  new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_EXCEL_IMPORT_ERROE);
        }
        List<ExchangeCodeImportBean>  list = new ArrayList<>();
        for (int numSheet = 0; numSheet < 1; numSheet++) {
            Sheet hssfSheet = workbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            String sheetName = hssfSheet.getSheetName();
            int lastRowNum = hssfSheet.getLastRowNum() - hssfSheet.getFirstRowNum() + 1;// 获取所有的行项目

            for (int rowNum = 2; rowNum < lastRowNum; rowNum++) {
                Row xssfRow = hssfSheet.getRow(rowNum);
                Cell secortCell = xssfRow.getCell(0);
                String secort = ExcelUtil.getValue(secortCell);
                if (StringUtils.isBlank(secort)) {
                    throw new FlexibleCustomException(sheetName + "表第" + (rowNum + 1) + MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_IMPORT_SECRET_NULL));
                }
                ExchangeCodeImportBean exchangeCodeImportBean = new ExchangeCodeImportBean();
                exchangeCodeImportBean.setSecretCode(secort);
                if(isEntity - ExchangeConstant.IsEntityYes ==0){
                    Cell plainCell = xssfRow.getCell(1);
                    String plain = ExcelUtil.getValue(plainCell);
                    if(StringUtils.isBlank(plain)){
                        throw new FlexibleCustomException( sheetName + "表第" + (rowNum + 1) + MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_IMPORT_SECRET_NULL));
                    }
                    exchangeCodeImportBean.setPlainCode(plain);
                }
                list.add(exchangeCodeImportBean);

            }
        }
        if(list ==null || list.size()==0){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_EXCEL_IMPORT_DATA_NULL);
        }
        String key = String.valueOf(System.currentTimeMillis());
        //stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(list),360, TimeUnit.MINUTES);
        codeCaches.put(key,JSON.toJSONString(list),360,TimeUnit.MINUTES);
        return Response.of(key);
    }

    /**
     * 设置excel导入的兑换码和明码
     * @param exchangeCardRequest
     */
    @Override
    public void checkExcelImportCode(ExchangeCardCmd exchangeCardRequest) {
        if (exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityNo == 0 && exchangeCardRequest.getSource() - ExchangeConstant.SourceImport == 0 &&
                StringUtils.isBlank(exchangeCardRequest.getCodeKey()) && exchangeCardRequest.getId()==null
        ) {
            //非实体卡、选中人工导入、必须要上传excel
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_MUST_IMPORT_CODE_BEFORE_CHOOSE_IMPORT);
        }
        if (exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes == 0 && exchangeCardRequest.getCardType() - ExchangeConstant.CardTypeImport == 0 &&
                StringUtils.isBlank(exchangeCardRequest.getCodeKey()) && exchangeCardRequest.getId()==null
        ) {
            //非实体卡、选中人工导入、必须要上传excel
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_MUST_IMPORT_CODE_BEFORE_WHEN_CARD_TYPE_IMPORT);
        }
        if(StringUtils.isBlank(exchangeCardRequest.getCodeKey()) || (exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes==0 && exchangeCardRequest.getCardType() - ExchangeConstant.CardTypeImport !=0)
        || (exchangeCardRequest.getSource() - ExchangeConstant.SourceImport !=0 && exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityNo==0)
        ){
            //非导入
            return;
        }
       // String keyValue = stringRedisTemplate.opsForValue().get(exchangeCardRequest.getCodeKey());
        String keyValue = codeCaches.get(exchangeCardRequest.getCodeKey());
        List<ExchangeCodeImportBean> list = JSONObject.parseArray(keyValue,ExchangeCodeImportBean.class);
        if(list ==null || list.size() ==0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_EXCEL_IMPORT_DATA_NULL);
        }
        if(StringUtils.isBlank(list.get(0).getPlainCode()) && exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes==0){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_PLAIN_CODE_NULL_BY_IMPORT);
        }
        if(StringUtils.isNotBlank(list.get(0).getPlainCode()) && exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityNo==0){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_VIRTUAL_NOT_NEED_IMPORT_PLAIN_CODE);
        }
        exchangeCardRequest.setImportBeanList(list);
        Integer count =list.size();
        exchangeCardRequest.setCodeQuantity(count);
    }

    /**
     *  @param importBeanList
     * @param exchangeCardDO
     * @param isFirst
     * @param codeKey
     */
    @Override
    public void saveExcelImportCode(List<ExchangeCodeImportBean> importBeanList, ExchangeCardDO exchangeCardDO, boolean isFirst, String codeKey) {
        if(importBeanList !=null && importBeanList.size()>0){
            //加密
            importBeanList.stream().forEach(exchangeCodeImportBean -> {
                exchangeCodeImportBean.setSecretCode(AESUtil.encrypt(exchangeCodeImportBean.getSecretCode()));
            });
        }

        if(!isFirst ){
            //先删除原来的
            List<ExchangeCodeDO> codeList = exchangeCodeQryExe.listByExchangeId(exchangeCardDO.getId());
            if(codeList !=null && codeList.size()>0){
                for(int x=0;x<codeList.size();x++){
                    ExchangeCodeDO exchangeCodeDO  = codeList.get(x);
                    if(exchangeCodeDO.getStatus() - ExchangeCodeConstant.StatusInit ==0){
                        Boolean isDelete = true;
                        if(importBeanList !=null && importBeanList.size()>0){
                            for(int y=0;y<importBeanList.size();y++){
                                if( (StringUtils.isNotBlank(exchangeCodeDO.getPlainCode())
                                        && StringUtils.isBlank(importBeanList.get(y).getPlainCode())) && exchangeCodeDO.getSecretCode().equals(importBeanList.get(y).getSecretCode())  ){
                                    //原来有明码、后面编辑又没有明码
                                    throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_PLAIN_CODE_NOT_NULL_BEFORE);
                                }
                                if((StringUtils.isBlank(exchangeCodeDO.getPlainCode())
                                        && StringUtils.isNotBlank(importBeanList.get(y).getPlainCode())
                                ) && exchangeCodeDO.getSecretCode().equals(importBeanList.get(y).getSecretCode())  ){
                                    //原来没有明码、后面编辑又有明码
                                    throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_PLAIN_CODE_NULL_BEFORE);
                                }


                                if(exchangeCodeDO.getSecretCode().equals(importBeanList.get(y).getSecretCode())
                                || (StringUtils.isNotBlank(importBeanList.get(y).getPlainCode()) && StringUtils.isNotBlank(exchangeCodeDO.getPlainCode())
                                && importBeanList.get(y).getPlainCode().equals(exchangeCodeDO.getPlainCode()))
                                ){
                                    //编辑没有清掉
                                    isDelete = false;
                                    importBeanList.remove(y);
                                    if(!exchangeCodeDO.getSecretCode().equals(importBeanList.get(y).getSecretCode())){
                                        throw new FlexibleCustomException(exchangeCodeDO.getPlainCode()+"对应的"+ MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_FORBIN_UPDATE));
                                    }
                                    if(StringUtils.isNotBlank(exchangeCodeDO.getPlainCode()) && StringUtils.isNotBlank(importBeanList.get(y).getPlainCode()) && !exchangeCodeDO.getPlainCode().equals(importBeanList.get(y).getPlainCode())){
                                        throw new FlexibleCustomException(importBeanList.get(y).getPlainCode()+MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_PLAIN_CODE_FORBIN_UPDATE));
                                    }
                                    break;
                                }

                            }
                        }
                       if(isDelete){
                           //直接删除
                           exchangeCodeCmdExe.deleteById(exchangeCodeDO.getId());
                       }
                    }
                }
            }
        }
        if(importBeanList ==null || importBeanList.size()==0){
            return;
        }

        importBeanList.stream().forEach(exchangeCodeImportBean -> {
            try {
                ExchangeCodeDO exchangeCodeDO = new ExchangeCodeDO();
                exchangeCodeDO.setExchangeId(exchangeCardDO.getId());
                exchangeCodeDO.setSecretCode(exchangeCodeImportBean.getSecretCode());
                exchangeCodeDO.setPlainCode(exchangeCodeImportBean.getPlainCode());
                exchangeCodeDO.setStatus(ExchangeCodeConstant.StatusInit);
                exchangeCodeDO.setCreateTime(new Date());
                exchangeCodeDO.setCreateUserId(exchangeCardDO.getUpdateUserId());
                exchangeCodeDO.setCreateUserName(exchangeCardDO.getUpdateUserName());
                exchangeCodeDO.setUpdateTime(new Date());
                exchangeCodeDO.setUpdateUserId(exchangeCardDO.getUpdateUserId());
                exchangeCodeDO.setUpdateUserName(exchangeCardDO.getUpdateUserName());
                exchangeCodeCmdExe.create(exchangeCodeDO);
            } catch (Exception e) {
                e.printStackTrace();
                String msg ="、明码：【"+exchangeCodeImportBean.getPlainCode()+"】和暗码【"+exchangeCodeImportBean.getSecretCode()+"】";
                throw new FlexibleCustomException(MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_SAVE_FAIL_BY_CODE_REPEAT)+msg);
            }
        });
       if(StringUtils.isNotBlank(codeKey)){
           codeCaches.remove(codeKey);
       }
    }

    @Override
    public HSSFWorkbook createExcelByCondition(ExchangeCodePageQry exchangeCodePageQry, AdminResponse currentUser, String currentLanguage) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<ExchangeCodePageCO> allList = exchangeCodeQryExe.listCOByCondition(exchangeCodePageQry.getExchangeFactoryId(),exchangeCodePageQry.getExchangeId(),
                exchangeCodePageQry.getExchangeWay(),exchangeCodePageQry.getStatus(),exchangeCodePageQry.getContent(),null);
        if(allList ==null || allList.size() ==0){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_EXPORT_FAIL_BY_CODE_NULL);
        }
        //按照65536数量来切割成多个列表
        List<List<ExchangeCodePageCO>> groupList = new ArrayList<>();
        List<ExchangeCodePageCO> sonList = null;
        for(int x=0;x<allList.size();x++){
            //前面2行留空
            if(x%(65536-2)==0){
                if(x>0){
                    groupList.add(sonList);
                }
                sonList = new ArrayList<>();
            }
            sonList.add(allList.get(x));
        }
        if(sonList !=null && sonList.size()>0){
            groupList.add(sonList);
        }
        Long index = 0L;
        for(int z=0;z<groupList.size();z++){
            List<ExchangeCodePageCO> list = groupList.get(z);
            HSSFSheet hssfSheet = workbook.createSheet("兑换券码导出表格第"+(z+1)+"个");
            HSSFFont hssfFont = workbook.createFont();
            hssfFont.setColor(IndexedColors.BLACK.getIndex());
            //设置字体
            hssfFont.setFontName("宋体");
            //设置字号
            hssfFont.setFontHeightInPoints((short)10);
            //设置单元格内容水平垂直居中
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            //4.设置单元格背景色
            titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充单元格
            titleStyle.setFillForegroundColor(HSSFColor.GREEN.index);//设置单元格背景色
            titleStyle.setFont(hssfFont);

            HSSFRow row2 = hssfSheet.createRow(1);
            row2.setHeightInPoints(25);
            HSSFCell hssfCell = row2.createCell(0);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.SerialNumberZH:ExchangeExcelConstant.SerialNumberEN);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row2.createCell(1);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.ExchangeCodeZH:ExchangeExcelConstant.ExchangeCodeEN);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row2.createCell(2);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.PlainCodeZH:ExchangeExcelConstant.PlainCodeEN);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row2.createCell(3);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.BoxCodeZH:ExchangeExcelConstant.BoxCodeEN);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row2.createCell(4);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.ActivityNameZH:ExchangeExcelConstant.ActivityNameEN);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row2.createCell(5);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.ExchangeWayZH:ExchangeExcelConstant.ExchangeWayEN);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row2.createCell(6);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.CreateTimeZH:ExchangeExcelConstant.CreateTimeEN);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row2.createCell(7);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.CodeStatusZH:ExchangeExcelConstant.CodeStatusEN);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row2.createCell(8);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.DistributorZH:ExchangeExcelConstant.DistributorEN);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row2.createCell(9);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.UserNameZH:ExchangeExcelConstant.UserNameEN);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row2.createCell(10);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.UseOrderIDZH:ExchangeExcelConstant.UseOrderIDEN);
            hssfCell.setCellStyle(titleStyle);

            for(int x=0;x<list.size();x++){
                index++;
                HSSFRow row = hssfSheet.createRow(x+2);
                for(int i=0;i<11;i++){
                    hssfCell = row.createCell(i);
                    hssfCell.setCellStyle(titleStyle);
                }
                row.createCell(0).setCellValue(index);
                if(StringUtils.isNotBlank(list.get(x).getSecretCode())){
                    String secretCode = AESUtil.decrypt(list.get(x).getSecretCode());
                    row.createCell(1).setCellValue(secretCode);
                }else{
                    row.createCell(1).setCellValue("");
                }
                if(StringUtils.isNotBlank(list.get(x).getPlainCode())){
                    row.createCell(2).setCellValue(list.get(x).getPlainCode());
                }else{
                    row.createCell(2).setCellValue("");
                }
                if(StringUtils.isNotBlank(list.get(x).getBoxCode())){
                    row.createCell(3).setCellValue(list.get(x).getBoxCode());
                }else {
                    row.createCell(3).setCellValue("");
                }
                row.createCell(4).setCellValue(list.get(x).getCardName());
                row.createCell(5).setCellValue(list.get(x).getExchangeWay().equals(ExchangeConstant.ExchangeWayExchange1)?"兑换":"其他");
                Date date = list.get(x).getCreateTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String sd = sdf.format(date); // 时间戳转换日期
                row.createCell(6).setCellValue(sd);
                //核销状态 0、未激活、1、未使用 2、已核销 3、已过期 4、已作废
                if(list.get(x).getStatus() - ExchangeCodeConstant.StatusInit==0){
                    row.createCell(7).setCellValue(ExchangeCodeConstant.StatusInitName);
                }
                if(list.get(x).getStatus() - ExchangeCodeConstant.StatusUnUse==0){
                    row.createCell(7).setCellValue(ExchangeCodeConstant.StatusUnUseName);
                }
                if(list.get(x).getStatus() - ExchangeCodeConstant.StatusUsed==0){
                    row.createCell(7).setCellValue(ExchangeCodeConstant.StatusUsedName);
                }
                if(list.get(x).getStatus() - ExchangeCodeConstant.StatusEnd==0){
                    row.createCell(7).setCellValue(ExchangeCodeConstant.StatusEndName);
                }
                if(list.get(x).getStatus() - ExchangeCodeConstant.StatusInvalid==0){
                    row.createCell(7).setCellValue(ExchangeCodeConstant.StatusInvalidName);
                }

                if(StringUtils.isNotBlank(list.get(x).getDistributorName())){
                    row.createCell(8).setCellValue(list.get(x).getDistributorName());
                }else{
                    row.createCell(8).setCellValue("");
                }
                if(StringUtils.isNotBlank(list.get(x).getUserName())){
                    row.createCell(9).setCellValue(list.get(x).getUserName());
                }else {
                    row.createCell(9).setCellValue("");
                }
                if(list.get(x).getUserOrderId() !=null){
                    row.createCell(10).setCellValue(list.get(x).getUserOrderId());
                }else {
                    row.createCell(10).setCellValue("");
                }

            }
          //  workbook.setSheetName(0,"兑换码导出");
        }
        return workbook;
    }


    @Override
    @Transactional
    public Response setBoxCode(List<ExchangeBoxCodeRequest> list,Integer exchangeId) {
       //先判断盒码是否已回传、erp不允许盒码重复传
       /* Set<String> boxCodeSet = new HashSet<>();
        list.stream().forEach(exchangeBoxCodeRequest -> {
            boxCodeSet.add(exchangeBoxCodeRequest.getBoxCode());
        });
        boxCodeSet.stream().forEach(s -> {
            List<ExchangeCodeDO> exchangeCodeList = exchangeCodeQryExe.listByBoxCode(s);
            if(exchangeCodeList !=null && exchangeCodeList.size()>0){
                throw new BaseOpenApiException(ExchangeOpenErrorConstant.BoxCodeSendAgainError.getCode(),ExchangeOpenErrorConstant.BoxCodeSendAgainError.getMsg());
            }
        });*/
        LOGGER.info("开始处理同步ERP兑换码");
        //exchange_factory表主键
        Set<Integer> set = new HashSet<>();
        Map<Integer,LinkedHashMap<String,List<String>>> map = new LinkedHashMap<>();
        List<ExchangeCodeSyncBackLogDO> syncBackLogList = new ArrayList<>();
        Map<String, Integer> exchangeCodeMap = new HashMap<>();
        list.stream().forEach(exchangeBoxCodeRequest -> {
           /* String secretCode = null;
            if(StringUtils.isNotBlank(exchangeBoxCodeRequest.getSecretCode())){
                secretCode = AESUtil.encrypt(exchangeBoxCodeRequest.getSecretCode());
            }
            ExchangeCodeDO exchangeCodeDO = exchangeCodeQryExe.findByPlainCodeAndSecretCode(exchangeBoxCodeRequest.getPlainCode(),secretCode);
            if(exchangeCodeDO ==null){
                String msg = "明码："+exchangeBoxCodeRequest.getPlainCode()+"暗码："+exchangeBoxCodeRequest.getSecretCode();
                throw new BaseOpenApiException(ExchangeOpenErrorConstant.PlainCodeErrorOrSecretCodeError.getCode(),msg+ExchangeOpenErrorConstant.PlainCodeErrorOrSecretCodeError.getMsg());
            }
            //设置盒码
            exchangeCodeDO.setBoxCode(exchangeBoxCodeRequest.getBoxCode());*/
            set.add(exchangeBoxCodeRequest.getExchangeFactoryId());
           // exchangeCodeCmdExe.update(exchangeCodeDO);
            exchangeCodeMap.put(exchangeBoxCodeRequest.getPlainCode(),exchangeBoxCodeRequest.getExchangeCodeId());
            //同步给erp
            LinkedHashMap<String,List<String>> boxMap = map.get(exchangeId);

            if( boxMap !=null){
                //明码
               List<String> plainCodeList = boxMap.get(exchangeBoxCodeRequest.getBoxCode());
               if(plainCodeList==null || plainCodeList.size()==0){
                  plainCodeList = new ArrayList<>();
               }
                plainCodeList.add(exchangeBoxCodeRequest.getPlainCode());
               boxMap.put(exchangeBoxCodeRequest.getBoxCode(),plainCodeList);
            }else{
                boxMap = new LinkedHashMap<>();
                List<String> plainCodeList = new ArrayList<>();
                plainCodeList.add(exchangeBoxCodeRequest.getPlainCode());
                boxMap.put(exchangeBoxCodeRequest.getBoxCode(),plainCodeList);
            }
            map.put(exchangeId,boxMap);
        });
        List<Integer> factoryIdList = new ArrayList<>(set);
        factoryIdList.stream().forEach(id -> {
            ExchangeFactoryDO exchangeFactoryDO = exchangeFactoryServiceI.getById(id);
            exchangeFactoryDO.setStatus(ExchangeCodeConstant.SyncFactoryStatusSuccess);
            exchangeFactoryServiceI.update(exchangeFactoryDO);
        });
        //同步给erp
        Iterator<Entry<Integer,LinkedHashMap<String,List<String>>>> iterator = map.entrySet().iterator();
        List<BoxCodeCardErpRequest> erpRequestList = new ArrayList<>();
        //记录存放的货品id同步到ERP的数量
        Map<Integer, Integer> boxNumMap = new HashMap<>();
        Map<Integer, Integer> plainCodeNumMap = new HashMap<>();
        while (iterator.hasNext()){
            Entry<Integer,LinkedHashMap<String,List<String>>> entry= iterator.next();
            ExchangeCardDO exchangeCardDO = exchangeCardServiceI.getById(entry.getKey());
            //查询货品信息
            List<Integer> itemIdList = new ArrayList<>();
            itemIdList.add(exchangeCardDO.getItemId());
            com.bat.dubboapi.goods.common.Response<List<GoodsItemRpcDTO>> itemResponse = goodsServiceRpc.listGoodsItemByIds(itemIdList);
            GoodsItemRpcDTO goodsItemRpcDTO = itemResponse.getData().get(0);
            //获取当前材质的生成的盒码数量
            Integer boxCount =boxNumMap.get(exchangeCardDO.getItemId());
            Integer plainCodeNum = plainCodeNumMap.get(exchangeCardDO.getItemId());
            ExchangeCodeSyncBackLogDO backLog = exchangeCodeSyncBackLogQryExe.countByByItemId(exchangeCardDO.getItemId());
            if(boxCount ==null){
                 boxCount = backLog.getAlreadySyncBoxCount();
                 plainCodeNum = backLog.getAlreadySyncPlainCodeCount();
            }

            AtomicReference<Integer> plainCodeCount = new AtomicReference<>(plainCodeNum);
            Map<String,List<String>> boxMap =entry.getValue();
            Iterator<Entry<String,List<String>>> entryIterator = boxMap.entrySet().iterator();
            while (entryIterator.hasNext()){
                Entry<String,List<String>> plainEntity = entryIterator.next();
                BoxCodeCardErpRequest erpRequest = new BoxCodeCardErpRequest();
                erpRequest.setF_MateID(goodsItemRpcDTO.getItemCode());
                erpRequest.setF_HCode(plainEntity.getKey());

                erpRequest.setFCreateDate(DateUtil.getTime(exchangeCardDO.getStartTime(),"yyyy-MM-dd HH:mm"));
                erpRequest.setF_CanUseDate(DateUtil.getTime(exchangeCardDO.getEndTime(),"yyyy-MM-dd HH:mm"));
                boxCount++;
                erpRequest.setSortNo(boxCount);
                List<String> plainList = plainEntity.getValue();
                //处理明码列表
                List<PlainCodeERPRequest> plainCodeERPRequestList = new ArrayList<>();
                plainList.stream().forEach(s -> {
                    PlainCodeERPRequest plainCodeERPRequest = new PlainCodeERPRequest();
                    plainCodeERPRequest.setF_MCode(s);
                    plainCodeCount.getAndSet(plainCodeCount.get() + 1);
                    plainCodeERPRequest.setSortNo(plainCodeCount.get());
                    plainCodeERPRequestList.add(plainCodeERPRequest);
                    ExchangeCodeSyncBackLogDO log = new ExchangeCodeSyncBackLogDO();
                    log.setCreateTime(new Date());
                    log.setExchangeId(exchangeCardDO.getId());
                    //先存兑换活动id、后续改回来
                    log.setItemId(exchangeCardDO.getItemId());
                    log.setExchangeCodeId(exchangeCodeMap.get(s));
                    log.setAlreadySyncPlainCodeCount(plainCodeERPRequest.getSortNo());
                    log.setAlreadySyncBoxCount(erpRequest.getSortNo());
                    syncBackLogList.add(log);
                });
                erpRequest.setMCards(plainCodeERPRequestList);
                erpRequestList.add(erpRequest);
            }
            boxNumMap.put(exchangeCardDO.getItemId(),boxCount);
            plainCodeNumMap.put(exchangeCardDO.getItemId(),plainCodeCount.get());

        }
        try {
            //分段List、分批同步到erp
            List<List<BoxCodeCardErpRequest>> allSyncList= new ArrayList<>();
            List<BoxCodeCardErpRequest> syncList =new ArrayList<>();
            for(int x=0;x<erpRequestList.size();x++){
                if(x > 0 && x%100==0){
                    allSyncList.add(syncList);
                    syncList = new ArrayList<>();
                }
                syncList.add(erpRequestList.get(x));
            }
            allSyncList.add(syncList);

            for(int x=0;x<allSyncList.size();x++){
                List<BoxCodeCardErpRequest> erpList = allSyncList.get(x);
                ExchangeCodeSyncERPRequest exchangeCodeSyncERPRequest = new ExchangeCodeSyncERPRequest();
                exchangeCodeSyncERPRequest.setHCodes(erpList);
                LOGGER.info("第"+(x+1)+"批b2b--> erp同步盒码、明码、参数为："+JSON.toJSONString(exchangeCodeSyncERPRequest));
                // String response = apiClient.save("",JSON.toJSONString(exchangeCodeSyncERPRequest));
                com.bat.dubboapi.thirdparty.common.Response response = thirdPartyExchangeServiceErpRpc.syncBoxCodeAndPlainCodeToErp(exchangeCodeSyncERPRequest);
                LOGGER.info("第"+(x+1)+"批b2b--> erp同步盒码、明码、响应为："+JSON.toJSONString(response));
                if(!response.isSuccess()){
                    throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_SYNC_ERP_FAIL);
                }
            }
            //持久化到数据库
            exchangeCodeSyncBackLogCmdExe.createList(syncBackLogList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_SYNC_ERP_FAIL);
        }
        return Response.buildSuccess();
    }

    @Transactional
    @Override
    public void cancelOrder(Integer userOrderId) {
        LOGGER.info("取消订单，还原兑换码状态、订单id为："+userOrderId);
        List<ExchangeCodeDO> codeList = exchangeCodeQryExe.findByUserOrderIdAndStatusOrderByExchangeIdAsc(userOrderId,ExchangeCodeConstant.StatusUsed);
        if(codeList == null || codeList.size()==0){
            throw new BaseOpenApiException(ExchangeErpErrorConstant.CancelFailWithoutExchangeCode.getCode(),ExchangeErpErrorConstant.CancelFailWithoutExchangeCode.getMsg());
        }
        ExchangeCardDO exchangeCard = null;
        for(int x=0;x<codeList.size();x++){
            ExchangeCodeDO exchangeCode = codeList.get(x);
            if(exchangeCard ==null || exchangeCode.getExchangeId() -exchangeCard.getId() !=0){
                if(exchangeCard !=null){
                    exchangeCardServiceI.updateDO(exchangeCard);
                }
                //兑换卡对象为空或者改变时、查询
                exchangeCard = exchangeCardServiceI.getById(exchangeCode.getExchangeId());
            }
            //修改为未使用
            exchangeCode.setStatus(ExchangeCodeConstant.StatusUnUse);
            if(exchangeCard.getStatus()  -ExchangeConstant.StatusEnd==0){
                exchangeCode.setStatus(ExchangeCodeConstant.StatusEnd);
            }
            //还原兑换码状态
            restoreExchangeCode(exchangeCode);
            //兑换数量-1
            exchangeCard.setExchangeQuantity(exchangeCard.getExchangeQuantity()-1);
        }
    }

    private void restoreExchangeCode(ExchangeCodeDO exchangeCodeDO) {
        addRestoreLog(exchangeCodeDO);
        //还原为空
        exchangeCodeDO.setUserId(null);

        exchangeCodeDO.setUseTime(null);
        exchangeCodeDO.setUserOrderId(null);
        exchangeCodeDO.setUserThirdOrderNo(null);
        exchangeCodeDO.setUserOrderGoodsId(null);
        exchangeCodeDO.setUserName(null);
        exchangeCodeCmdExe.update(exchangeCodeDO);
    }

    private void addRestoreLog(ExchangeCodeDO exchangeCodeDO) {
        ExchangeCodeRestoreLogDO exchangeCodeRestoreLog = new ExchangeCodeRestoreLogDO();
        exchangeCodeRestoreLog.setOldUserId(exchangeCodeDO.getUserId());
        exchangeCodeRestoreLog.setOldUserOrderNo(exchangeCodeDO.getUserThirdOrderNo());
        exchangeCodeRestoreLog.setUseTime(exchangeCodeDO.getUseTime());
        exchangeCodeRestoreLog.setOldOrderGoodsId(exchangeCodeDO.getUserOrderGoodsId());
        exchangeCodeRestoreLog.setOldOrderId(exchangeCodeDO.getUserOrderId());
        exchangeCodeRestoreLog.setExchangeCodeId(exchangeCodeDO.getId());
        exchangeCodeRestoreLog.setCreateTime(new Date());
        exchangeCodeRestoreLogCmdExe.create(exchangeCodeRestoreLog);
    }

    //系统随机生成
    private void createCodeByRandom(ExchangeCardDO exchangeCardDO, AdminResponse currentAdmin, Integer additionalCount, ExchangeEntityRuleDO exchangeEntityRuleDO, Integer exchangeFactoryId) {
        Integer createCount = exchangeCardDO.getCodeQuantity();
        if(additionalCount !=null && additionalCount >0){
            createCount = additionalCount;
        }
        //剩余数量
        //String count =stringRedisTemplate.opsForValue().get(ExchangeConstant.ExchangeRandomLeng+"_"+exchangeEntityRuleDO.getRandomValue());
        String count =codeCaches.get(ExchangeConstant.ExchangeRandomLeng+"_"+exchangeEntityRuleDO.getRandomValue());
        if(Integer.parseInt(count) <createCount){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_RANDOM_INSUFFICIENT_ERROR);
        }
        JSONArray jsonArray = NumUtils.getRandomPool(exchangeEntityRuleDO.getRandomValue());
        List<ExchangeCodeDO> codeDOList = new ArrayList<>();
        //获取的
        for(int x=0;x<createCount;x++){
            String number = NumUtils.getDiffereNumber(exchangeEntityRuleDO.getRandomValue(),jsonArray,x);
            ExchangeCodeDO exchangeCode = ExchangeCardConvertor.createExchangeCode(exchangeCardDO, currentAdmin, "", number, exchangeFactoryId, null);
            codeDOList.add(exchangeCode);
        }
        exchangeCodeCmdExe.batchCreate(codeDOList);
        //截取长度
        Integer listNum = jsonArray.size()-createCount.intValue();
        //剩余
        System.out.println("剩余"+listNum);
        List<Object> resultList = jsonArray.subList(0,listNum);
        System.out.println("最后长度"+resultList.size());
        NumUtils.updateRandomPool(exchangeEntityRuleDO.getRandomValue(),resultList);
       // stringRedisTemplate.opsForValue().set(ExchangeConstant.ExchangeRandomLeng+"_"+exchangeEntityRuleDO.getRandomValue(),String.valueOf(listNum));
        codeCaches.put(ExchangeConstant.ExchangeRandomLeng+"_"+exchangeEntityRuleDO.getRandomValue(),String.valueOf(listNum));
    }

    private void createCodeByRule(ExchangeCardDO exchangeCardDO, AdminResponse currentAdmin, Integer additionalCount, ExchangeEntityRuleDO exchangeEntityRule,
                                  Integer exchangeFactoryId) {
        //生成明码和暗码
        Long num= 1L;
        Long max = (additionalCount==null || additionalCount==0?exchangeCardDO.getCodeQuantity():additionalCount)*Long.parseLong(exchangeEntityRule.getFloatValue());
        Integer numLength = String.valueOf(max).length();
        String rise = exchangeEntityRule.getRiseValue();
        //获取浮动值的00长度
        Integer length = exchangeEntityRule.getFloatValue().length()-String.valueOf(Long.parseLong(exchangeEntityRule.getFloatValue())).length();
        /*if(additionalCount == null || additionalCount ==0){
            numLength = numLength+length-1;
        }*/
        if(additionalCount == null || additionalCount ==0){
            numLength = exchangeEntityRule.getFloatValue().length();
        }
        //判断抬头值是否已被使用过
        ExchangeCodeDO maxExchangeCode = exchangeCodeQryExe.findMaxByRiseValue(exchangeEntityRule.getRiseValue());
        if(maxExchangeCode !=null && additionalCount==null){
            //直接后面叠加、修改additionalCount的值
            additionalCount = exchangeCardDO.getCodeQuantity();
        }
        if(additionalCount !=null && additionalCount>0){
            if(maxExchangeCode !=null){
                String code = maxExchangeCode.getPlainCode().replace(exchangeEntityRule.getRiseValue(),"");
                num = Long.parseLong(code);//获取上一个最大值

                max = num+(additionalCount*Long.parseLong(exchangeEntityRule.getFloatValue()));
                num = num+Long.parseLong(exchangeEntityRule.getFloatValue());
                length =String.valueOf(max).length()-code.length();
                numLength = String.valueOf(max).length()-length;
            }
        }
        List<ExchangeCodeDO> codeDOList = new ArrayList<>();
        while (num<max+1){
            if(numLength - String.valueOf(num).length()>0){
                String a="";
                for(int x = 0;x<numLength-String.valueOf(num).length();x++){
                    a=a+"0";
                }
                ExchangeCodeDO exchangeCodeDO = ExchangeCardConvertor.createExchangeCode(exchangeCardDO, currentAdmin, rise, a + num, exchangeFactoryId, exchangeEntityRule);
                codeDOList.add(exchangeCodeDO);
            }else{
                ExchangeCodeDO exchangeCodeDO = ExchangeCardConvertor.createExchangeCode(exchangeCardDO, currentAdmin, rise, String.valueOf(num), exchangeFactoryId,exchangeEntityRule);
                codeDOList.add(exchangeCodeDO);
            }
            num+=Long.parseLong(exchangeEntityRule.getFloatValue());
        }
        if(codeDOList.size()>0){
            exchangeCodeCmdExe.batchCreate(codeDOList);
            LOGGER.info("生成的兑换码列表:{}",JSON.toJSONString(codeDOList));
        }
    }

    public static void create(Long additionalCount,Long codeQuantity,String floatValue,String rise,String lastCode){
        Long num= 1L;

        Long max = codeQuantity*Long.parseLong(floatValue);
        Integer numLength = String.valueOf(max).length();

        //获取浮动值的00长度
        Integer length = floatValue.length()-String.valueOf(Long.parseLong(floatValue)).length();
        if(additionalCount ==null || additionalCount==0){
            numLength = numLength+length-1;
        }
        if(additionalCount !=null && additionalCount>0){
            String code = lastCode.replace(rise,"");
            num = Long.parseLong(code);//获取上一个最大值
            max =(num+Long.parseLong(floatValue))+(additionalCount*Long.parseLong(floatValue));
            num=num+Long.parseLong(floatValue);
            length = String.valueOf(max).length() - code.length();
            numLength=String.valueOf(max).length()-length;

        }

        //是否进位
        while (num<max+1){
            if(numLength - String.valueOf(num).length()>0){
                String a="";
                for(int x = 0;x<numLength-String.valueOf(num).length();x++){
                    a=a+"0";
                }
                System.out.println("=="+rise+a+num);
            }else{
               /* if(isOUt){
                    rise = rise.substring(0,rise.length()-1);
                    isOUt = false;
                }*/
                System.out.println("--"+rise+num);
            }
            num+=Long.parseLong(floatValue);
        }
    }

    @Override
    public Response txt() {
        List<String> resultList = new ArrayList<>();
        readAllFile("D:/我的工作/兑换卡/工厂给回的装箱数据/分箱数据/分箱数据",resultList);
        System.out.println(resultList.size());
        List<Map<String, String>> list = new ArrayList<>();
        ExchangeBoxCodeOpenRequest exchangeBoxCodeOpenRequest = new ExchangeBoxCodeOpenRequest();
        List<ExchangeBoxCodeRequest> boxCodeList = new ArrayList();
        List<Map<String,Integer>> boxCodeErrorList = new ArrayList();
        Map<String,Integer> codeMap = new HashMap();
        Integer count = 0;
        Set<String> boxCodeFirstMap = new HashSet<>();
        for(int x = 0; x<resultList.size();x++){
            String fileUrl = resultList.get(x); 
            if(fileUrl.contains(".txt")){
                //含有txt文件
                try {
                    String[] arr = fileUrl.split("\\\\");
                    String code = arr[arr.length-1].replace(".txt","");
                    File file = new File(fileUrl);
                    FileInputStream in = new FileInputStream(file);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String s = null;
                    while ((s =bufferedReader.readLine())!=null){
                        //LOGGER.info("--"+s+"=="+s.length());
                        count++;
                        s=s.substring(s.indexOf("P"));
                        ExchangeCodeDO exchangeCode = exchangeCodeQryExe.findByPlainCodeAndSecretCode(s,null);
                        if(StringUtils.isBlank(exchangeCode.getBoxCode())){
                            Map<String, String> map = new HashMap<>();
                            map.put("boxCode",code);
                            map.put("plainCode",s);
                            list.add(map);
                            ExchangeBoxCodeRequest exchangeBoxCodeRequest = new ExchangeBoxCodeRequest();
                            exchangeBoxCodeRequest.setBoxCode(code);
                            exchangeBoxCodeRequest.setPlainCode(s);
                            boxCodeList.add(exchangeBoxCodeRequest);
                        }
                        boxCodeFirstMap.add(code.substring(0, 1));
                    }
                    Integer num = 1;
                    if(codeMap.get(code) !=null){
                        num=codeMap.get(code)+1;
                    }
                    codeMap.put(code, num);
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new FlexibleCustomException("txt明码异常");
                }

            }
        }
        System.out.println(count);
        System.out.println(list.size());
        Iterator<Entry<String,Integer>> iterator = codeMap.entrySet().iterator();
        while(iterator.hasNext()){
            Entry<String,Integer> entry = iterator.next();
            if(entry.getValue()>1){
                Map<String,Integer> map = new HashMap();
                map.put(entry.getKey(), entry.getValue());
                boxCodeErrorList.add(map);
            }
        }
        System.out.println("错误的盒码："+JSON.toJSONString(boxCodeErrorList));
        String param = "{\"boxCodeList\":"+JSON.toJSONString(list)+"}";
        System.out.println(param);
        LOGGER.info("返回的盒码开头值："+JSON.toJSONString(boxCodeFirstMap));
        return Response.buildSuccess();
    }
    /**
     * @Author：
     * @Description：获取某个目录下所有直接下级文件，不包括目录下的子目录的下的文件，所以不用递归获取
     * @Date：
     */
    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
                //文件名，不包含路径
                //String fileName = tempList[i].getName();
            }
            if (tempList[i].isDirectory()) {
                //这里就不递归了，
            }
        }
        return files;
    }

    //读取一个文件夹下所有文件及子文件夹下的所有文件
    public static File[]  readAllFile(String filePath, List<String> resultList) {
        File f = null;
        f = new File(filePath);
        File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。
        List<File> list = new ArrayList<File>();
        for (File file : files) {
            if(file.isDirectory()) {
                //如何当前路劲是文件夹，则循环读取这个文件夹下的所有文件
                readAllFile(file.getAbsolutePath(), resultList);
            } else {
                list.add(file);
            }
        }
        for(File file : files) {
            System.out.println(file.getAbsolutePath());
            resultList.add(file.getAbsolutePath());
        }
        return files;
    }

    //读取一个文件夹下的所有文件夹和文件
    public static void ReadFile(String filePath) {
        File f = null;
        f = new File(filePath);
        File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。
        List<File> list = new ArrayList<File>();
        for (File file : files) {
            list.add(file);
        }
        for(File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }

    @Override
    public PageInfo pageOrder(ExchangeCodeOrderQry exchangeCodeOrderQry) {
        PageHelper.startPage(exchangeCodeOrderQry.getPage(),exchangeCodeOrderQry.getSize());
       /* List<ExchangeCodeOrderPageCO> orderPageCOList = exchangeCodeQryExe.listCodeCOByCondition(exchangeCodeOrderQry.getStartTime(),
                exchangeCodeOrderQry.getEndTime(),exchangeCodeOrderQry.getStatus(),exchangeCodeOrderQry.get)*/

        return new PageInfo();
    }

    @Override
    public HSSFWorkbook createExchangeExcelByCondition(ExchangeCodeOrderQry exchangeCodeOrderQry,
            AdminResponse currentUser, String currentLanguage) {
        
                HSSFWorkbook workbook = new HSSFWorkbook();
                /*List<ExchangeCodeOrderPageDTO> list = exchangeCodeDataManager.listDistributorExchangeByCondition(exchangeCodeOrderQry);
                if(list ==null || list.size() ==0){
                    throw new BaseException(ExchangeErrorConstant.CodeExportFailByCodeNull.getCode(),ExchangeErrorConstant.CodeExportFailByCodeNull.getMsg());
                }
                HSSFSheet hssfSheet = workbook.createSheet("兑换券码订单导出表格");
                HSSFFont hssfFont = workbook.createFont();
                hssfFont.setColor(IndexedColors.BLACK.getIndex());
                //设置字体
                hssfFont.setFontName("宋体");
                //设置字号
                hssfFont.setFontHeightInPoints((short)10);
                //设置单元格内容水平垂直居中
                HSSFCellStyle titleStyle = workbook.createCellStyle();
                titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                //4.设置单元格背景色
                titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充单元格
                titleStyle.setFillForegroundColor(HSSFColor.GREEN.index);//设置单元格背景色
                titleStyle.setFont(hssfFont);
        
                HSSFRow row2 = hssfSheet.createRow(1);
                row2.setHeightInPoints(25);
                HSSFCell hssfCell = row2.createCell(0);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.SerialNumberZH:ExchangeCodeExcelConstant.SerialNumberEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(1);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.DistributorZH:ExchangeCodeExcelConstant.DistributorEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(2);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.DistributorOrderIdZH:ExchangeCodeExcelConstant.DistributorOrderIdEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(3);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.ReceiveNameZH:ExchangeCodeExcelConstant.ReceiveNameEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(4);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.ReceiveMobileZH:ExchangeCodeExcelConstant.ReceiveMobileEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(5);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.ActivityNameZH:ExchangeCodeExcelConstant.ActivityNameEN);
                hssfCell.setCellStyle(titleStyle);
         
                hssfCell = row2.createCell(6);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.PlainCodeZH:ExchangeCodeExcelConstant.PlainCodeEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(7);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.BoxCodeZH:ExchangeCodeExcelConstant.BoxCodeEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(8);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.ExchangeFactoryIdZH:ExchangeCodeExcelConstant.ExchangeFactoryIdEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(9);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.UseOrderIDZH:ExchangeCodeExcelConstant.UseOrderIDEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(10);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.OrderStatusZH:ExchangeCodeExcelConstant.OrderStatusEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(11);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.PayWayZH:ExchangeCodeExcelConstant.PayWayEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(12);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.UseTimeZH:ExchangeCodeExcelConstant.UseTimeEN);
                hssfCell.setCellStyle(titleStyle);
                hssfCell = row2.createCell(13);
                hssfCell.setCellValue("zh".equals(currentLanguage)? ExchangeCodeExcelConstant.CodeStatusZH:ExchangeCodeExcelConstant.CodeStatusEN);
                hssfCell.setCellStyle(titleStyle);
                for(int x=0;x<list.size();x++){
                    HSSFRow row = hssfSheet.createRow(x+2);
                    for(int i=0;i<11;i++){
                        hssfCell = row.createCell(i);
                        hssfCell.setCellStyle(titleStyle);
                    }
                    row.createCell(0).setCellValue(x+1);
                    row.createCell(1).setCellValue(list.get(x).getDistributorName());
                    row.createCell(2).setCellValue(list.get(x).getDistributorOrderId());
                    if(StringUtils.isNotBlank(list.get(x).getReceiveName())){
                        row.createCell(3).setCellValue(list.get(x).getReceiveName());
                    }else{
                        row.createCell(3).setCellValue("");
                    }
                    if(StringUtils.isNotBlank(list.get(x).getReceiveMobile())){
                        row.createCell(4).setCellValue(list.get(x).getReceiveMobile());
                    }else{
                        row.createCell(4).setCellValue("");
                    }
                    row.createCell(5).setCellValue(list.get(x).getActivityName());
                    if(StringUtils.isNotBlank(list.get(x).getPlainCode())){
                        row.createCell(6).setCellValue(list.get(x).getPlainCode());
                    }else{
                        row.createCell(6).setCellValue("");
                    }
                    if(StringUtils.isNotBlank(list.get(x).getBoxCode())){
                        row.createCell(7).setCellValue(list.get(x).getBoxCode());
                    }else {
                        row.createCell(7).setCellValue("");
                    }
                    row.createCell(8).setCellValue(list.get(x).getExchangeFactoryId());
                    row.createCell(9).setCellValue(list.get(x).getUserOrderId());
                 
                    if(list.get(x).getOrderStatus() - OrderConstant.OrderWaitConfirm ==0){
                        row.createCell(10).setCellValue(OrderConstant.OrderStatusNameWaitConfirm);
                    }
                    if(list.get(x).getOrderStatus() - OrderConstant.OrderFinishConfirm ==0){
                        row.createCell(10).setCellValue(OrderConstant.OrderStatusNameFinishConfirm);
                    }
                    if(list.get(x).getOrderStatus() - OrderConstant.PartDeliver ==0){
                        row.createCell(10).setCellValue(OrderConstant.OrderStatusNamePartDeliver);
                    }
                    if(list.get(x).getOrderStatus() - OrderConstant.AllDeliver ==0){
                        row.createCell(10).setCellValue(OrderConstant.OrderStatusNameAllDeliver);
                    }
                    if(list.get(x).getOrderStatus() - OrderConstant.OrderInvalid ==0){
                        row.createCell(10).setCellValue(OrderConstant.OrderStatusNameOrderInvalid);
                    }
                    if(list.get(x).getOrderStatus() - OrderConstant.OrderComplete ==0){
                        row.createCell(10).setCellValue(OrderConstant.OrderStatusNameOrderComplete);
                    }
                    if(list.get(x).getPayWay() - OrderConstant.AliPayType ==0){
                        row.createCell(11).setCellValue(OrderConstant.PayWayNameAliPayType);
                    }      
                    if(list.get(x).getPayWay() - OrderConstant.WxPayType ==0){
                        row.createCell(11).setCellValue(OrderConstant.PayWayNameWxPayType);
                    } 
                    if(list.get(x).getPayWay() - OrderConstant.IntervalType==0){
                        row.createCell(11).setCellValue(OrderConstant.PayWayNameIntervalType);
                    } 
                    if(list.get(x).getPayWay() - OrderConstant.CompanyTransferType ==0){
                        row.createCell(11).setCellValue(OrderConstant.PayWayNameCompanyTransferType);
                    } 
                    if(list.get(x).getPayWay() - OrderConstant.BalanceType ==0){
                        row.createCell(11).setCellValue(OrderConstant.PayWayNameBalanceType);
                    } 
                    if(list.get(x).getPayWay() - OrderConstant.KuaiQianPay ==0){
                        row.createCell(11).setCellValue(OrderConstant.PayWayNameKuaiQianPay);
                    } 
                  
                    Date date = new Date(list.get(x).getUseTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    String sd = sdf.format(date); // 时间戳转换日期
                    row.createCell(12).setCellValue(sd);
                    //核销状态 0、未激活、1、未使用 2、已核销 3、已过期 4、已作废
                    if(list.get(x).getStatus() - ExchangeCodeConstant.StatusInit==0){
                        row.createCell(13).setCellValue(ExchangeCodeConstant.StatusInitName);
                    }
                    if(list.get(x).getStatus() - ExchangeCodeConstant.StatusUnUse==0){
                        row.createCell(13).setCellValue(ExchangeCodeConstant.StatusUnUseName);
                    }
                    if(list.get(x).getStatus() - ExchangeCodeConstant.StatusUsed==0){
                        row.createCell(13).setCellValue(ExchangeCodeConstant.StatusUsedName);
                    }
                    if(list.get(x).getStatus() - ExchangeCodeConstant.StatusEnd==0){
                        row.createCell(13).setCellValue(ExchangeCodeConstant.StatusEndName);
                    }
                    if(list.get(x).getStatus() - ExchangeCodeConstant.StatusInvalid==0){
                        row.createCell(13).setCellValue(ExchangeCodeConstant.StatusInvalidName);
                    }
        
                }*/
                workbook.setSheetName(0,"兑换码导出");
                return workbook;
    }

    @Override
    public void createEndExchangeEvent(String time, Integer exchangeId) {
        exchangeCodeCmdExe.createEndExchangeEvent(time,exchangeId);
    }

    @Override
    public List<ExchangeCodeDO> listByOutboundNoGroupByBoxCode(String outboundNo) {
        return exchangeCodeQryExe.listByOutboundNoGroupByBoxCode(outboundNo);
    }

    @Override
    public List<ExchangeCodeDO> listByBoxCode(String boxCode) {
        return exchangeCodeQryExe.listByBoxCode(boxCode);
    }

    @Override
    public void update(ExchangeCodeDO code) {
        exchangeCodeCmdExe.update(code);
    }

    /**
     * 根据明码或者暗码查询
     * @param plainCode
     * @param secretCode
     * @return
     */
    @Override
    public ExchangeCodeDO findByPlainCodeAndSecretCode(String plainCode, String secretCode) {
        if(StringUtils.isNotBlank(secretCode)){
            secretCode = AESUtil.encrypt(secretCode);
        }
        return exchangeCodeQryExe.findByPlainCodeAndSecretCode(plainCode,secretCode);
    }

    @Override
    public List<ExchangeCodeDO> listByBoxCodeAndPlainCodeList(String boxCode, List<String> plainCodeList) {
        return exchangeCodeQryExe.listByBoxCodeAndPlainCodeList(boxCode,plainCodeList);
    }

    @Override
    public List<ExchangeCodeDO> listByExchangeId(Integer exchangeId) {
        return exchangeCodeQryExe.listByExchangeId(exchangeId);
    }

    @Override
    public InputStream tempDownLoad(DownLoadRequest downLoadRequest) {
        String  fileName = ExchangeConstant.ExcelTempExchangeEntityFileName;
        String ossKey = ExcelTempExchangeEntity;
        if (ExchangeConstant.ExcelTempExchangeVirtual.equals(downLoadRequest.getType())) {
            fileName = ExchangeConstant.ExcelTempExchangeVirtualFileName;
            ossKey = ExcelTempExchangeVirtual;
        }
        try {
            InputStream inputStream = thirdPartySystemOssServiceRpc.getExcelTemp(ossKey, fileName);
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ExchangeCodeDO> listByExchangeCodeIdListAndMaterialIdAndModelId(Integer materialId, Integer modelId, List<Integer> exchangeCodeIdList) {
        return exchangeCodeQryExe.listByExchangeCodeIdListAndMaterialIdAndModelId(materialId,modelId,exchangeCodeIdList);
    }

    @Override
    public List<ExchangeCodeDO> listBySecretCodeList(List<String> secretCodeList) {
        for(int x=0;x<secretCodeList.size();x++){
           secretCodeList.set(x,AESUtil.encrypt(secretCodeList.get(x)));
        }
        return exchangeCodeQryExe.listBySecretCodeList(secretCodeList);
    }

    @Override
    public List<ExchangeCodeDO> listByUserOrderId(Integer orderId) {
        return exchangeCodeQryExe.listByUserOrderId(orderId);
    }

    /**
     * 后台兑换码解绑
     * @param exchangeAdminUnboundCmd
     * @return
     */
    public Response unbound(ExchangeAdminUnboundCmd exchangeAdminUnboundCmd) {
        try{
            LOGGER.info("兑换码原数据：" + exchangeAdminUnboundCmd);
            ExchangeCodeDO exchangeCodeDO = new ExchangeCodeDO();
            exchangeCodeDO.setId(exchangeAdminUnboundCmd.getId());

            //判断兑换卡是否被领取
            ExchangeCodeDO data = exchangeCodeCmdExe.selectExchangeUserRecordById(exchangeCodeDO);
            if(data == null){
                LOGGER.info("该兑换卡暂未使用，无须解绑");
                return Response.buildFailure(BaseData.WARN,"该兑换卡暂未使用，无须解绑");
            }else{
                exchangeCodeCmdExe.unbound(exchangeCodeDO);
                return Response.buildSuccess();
            }
        }catch (Exception e){
            LOGGER.error("后台兑换码解绑异常");
            return Response.buildFailure(BaseData.FAIL,"后台兑换码解绑异常");
        }
    }

}
