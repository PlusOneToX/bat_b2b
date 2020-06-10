package com.bat.flexible.manager.exchange;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.exchange.*;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.api.goods.dto.GoodsItemSimplePageBean;
import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.model.dto.ModelQry;
import com.bat.flexible.api.model.dto.ModelRelaSimpleDTO;
import com.bat.flexible.api.picture.dto.PictureRelaSimpleDTO;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.manager.exchange.convertor.ExchangeCardConvertor;
import com.bat.flexible.manager.exchange.executor.ExchangeCardCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCardQryExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCardTransferCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCardTransferQryExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeInvalidLogCmdExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeOutboundRestoreLogCmdExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeSyncBackLogCmdExe;
import com.bat.flexible.manager.exchange.validator.ExchangeCardValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.distributor.wx.api.WxPlatformServiceRpc;
import com.bat.dubboapi.distributor.wx.dto.WxPlatformRpcDTO;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.thirdparty.oss.ThirdPartySystemOssServiceRpc;
import com.bat.dubboapi.thirdparty.wx.api.WxServiceRpc;
import com.bat.flexible.api.exchange.*;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.api.material.dto.MaterialRelaSimpleDTO;
import com.bat.flexible.api.material.dto.MaterialTreeQry;
import com.bat.flexible.dao.exchange.co.ExchangeCardPageCO;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.dao.material.co.MaterialTreeCO;
import com.bat.flexible.dao.model.co.ModelTreeCO;
import com.bat.flexible.manager.common.ConfigQry;
import com.bat.flexible.manager.common.config.FlexibleConfig;
import com.bat.flexible.manager.common.constant.dubbo.DistributorConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeConstant;
import com.bat.flexible.manager.common.utils.code.AESUtil;
import com.bat.flexible.manager.common.utils.proxy.FlexibleProxyUtil;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.common.FlexibleDubboServiceErrorCode;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;

@Service
public class ExchangeCardServiceImpl implements ExchangeCardServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeCardServiceImpl.class);

    @Autowired
    private ExchangeCardCmdExe exchangeCardCmdExe;

    @Autowired
    private ExchangeCardQryExe exchangeCardQryExe;

    @Autowired
    private ExchangeCardTransferCmdExe exchangeCardTransferCmdExe;

    @Autowired
    private ExchangeCardTransferQryExe exchangeCardTransferQryExe;

    @Autowired
    private ExchangeModelRelevanceServiceI exchangeModelRelevanceServiceI;

    @Autowired
    private ExchangePictureRelevanceServiceI exchangePictureRelevanceServiceI;

    @Autowired
    private ExchangeEntityRuleServiceI exchangeEntityRuleServiceI;

    @Autowired
    private ExchangeFactoryServiceI exchangeFactoryServiceI;

    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;

    @Autowired
    private ExchangeMaterialRelevanceServiceI exchangeMaterialRelevanceServiceI;

    @DubboReference(check = false, timeout = 10000, retries = 1)
    private ThirdPartySystemOssServiceRpc thirdPartySystemOssServiceRpc;

    @Autowired
    private ExchangeRefundOrderServiceI exchangeRefundOrderServiceI;
    @Autowired
    private ExchangeCodeInvalidLogCmdExe exchangeCodeInvalidLogCmdExe;

    @Autowired
    private ExchangeCardValidator exchangeCardValidator;

    @Autowired
    private ExchangeCodeOutboundRestoreLogCmdExe exchangeCodeOutboundRestoreLogCmdExe;

    @Autowired
    private ExchangeCodeSyncBackLogCmdExe exchangeCodeSyncBackLogCmdExe;

    @Autowired
    private ExchangeDistributorRelevanceServiceI exchangeDistributorRelevanceServiceI;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private GoodsServiceRpc goodsServiceRpc;

    @Autowired
    private ModelServiceI modelServiceI;

    @Autowired
    private MaterialServiceI materialServiceI;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private WxServiceRpc wxServiceRpc;

    @Resource
    private ConfigQry configQry;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private WxPlatformServiceRpc wxPlatformServiceRpc;

    @Override
    @Transactional
    public Response add(ExchangeCardCmd exchangeCardCmd, AdminResponse currentAdmin) {
        LOGGER.info("新增兑换卡、参数{}", JSON.toJSONString(exchangeCardCmd));
        // 使用兑换商城、只能部分可用
        if (exchangeCardCmd.getIsUseMall() - ExchangeConstant.IsMallYes == 0) {
            // 兑换商城、全部可用（这个功能目前没有上的、现在固定是全部可用）
            exchangeCardCmd.setGoodsScope(ExchangeConstant.GoodsScopeAll);
        }
        // 校验参数
        ExchangeCardValidator.validParam(exchangeCardCmd, true);
        ExchangeCardDO exchangeCardDO = BeanUtils.copy(exchangeCardCmd, ExchangeCardDO.class);
        exchangeCardDO.setStatus(ExchangeConstant.StatusInit);
        exchangeCardDO.setSaleQuantity(0);
        exchangeCardDO.setExchangeQuantity(0);
        setAdminMsg(exchangeCardDO, currentAdmin);

        exchangeCardCmdExe.create(exchangeCardDO);
        //检查权益
        checkQuanyi(exchangeCardCmd);

        if (exchangeCardCmd.getTransfer() != null) {
            ExchangeCardTransferDO exchangeCardTransferDO = new ExchangeCardTransferDO();
            org.springframework.beans.BeanUtils.copyProperties(exchangeCardCmd.getTransfer(), exchangeCardTransferDO);
            exchangeCardTransferDO.setExchangeId(exchangeCardDO.getId());
            exchangeCardTransferCmdExe.insert(exchangeCardTransferDO);
        }
        // 设置兑换码使用兑换商城
        setMallRela(exchangeCardCmd, exchangeCardDO.getId(), currentAdmin, true);
        // 设置是否生成实物卡
        exchangeEntityRuleServiceI.setEntityRule(exchangeCardCmd, exchangeCardDO.getId(), currentAdmin, true);
        // 设置excel导入的兑换码和明码
        exchangeCodeServiceI.saveExcelImportCode(exchangeCardCmd.getImportBeanList(), exchangeCardDO, true,
            exchangeCardCmd.getCodeKey());
        // 设置兑换卡和分销商的关联关系
        exchangeDistributorRelevanceServiceI.saveRelevance(true, exchangeCardDO.getDistributorScope(),
            exchangeCardCmd.getDistributorList(), exchangeCardDO.getId(), currentAdmin);
        return Response.buildSuccess();
    }

    /**
     * 权益检查
     * @param exchangeCardCmd
     */
    private void checkQuanyi(ExchangeCardCmd exchangeCardCmd){
        if(exchangeCardCmd.getIsEntity()!=ExchangeConstant.IsEntityNo.shortValue()){
            return;
        }
        if (exchangeCardCmd.getExchangeWay() == ExchangeConstant.ExchangeWayExchange2.shortValue()) {
            //指定分销商
            if (exchangeCardCmd.getDistributorScope() == ExchangeConstant.EXCHANGE_DISTRIBUTOR_SCOPE_ASSIGN.shortValue()) {
                for (DistributorSimpleRelaQry distributorSimpleRelaQry : exchangeCardCmd.getDistributorList()) {
                    for (Integer materialId : exchangeCardCmd.getMaterialIdList()) {
                        int count = exchangeCardQryExe.countQuanyiByDistributorIdAndMaterialId(distributorSimpleRelaQry.getDistributorId(), materialId);
                        if (count >= 2) {
                            throw new FlexibleCustomException("当前分销商id:"+distributorSimpleRelaQry.getDistributorId()+",当前材质id:"+materialId+",在当前时间内已有重复权益，请保持现时活动时间内有且只有一个权益");
                        }
                    }
                }
            }
            if (exchangeCardCmd.getDistributorScope() == ExchangeConstant.EXCHANGE_DISTRIBUTOR_SCOPE_ALL.shortValue()) {
                for (Integer materialId : exchangeCardCmd.getMaterialIdList()) {
                    int count = exchangeCardQryExe.countQuanyiByDistributorIdAndMaterialId(null, materialId);
                    if (count >= 2) {
                        throw new FlexibleCustomException("当前材质id:"+materialId+",当前时间内可能使一些分销商重复权益，请保持现时活动时间内有且只有一个权益");

                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public Response update(ExchangeCardCmd exchangeCardCmd, AdminResponse currentAdmin) {
        LOGGER.info("编辑兑换卡、参数{}", JSON.toJSONString(exchangeCardCmd));
        // 使用兑换商城、只能部分可用
        if (exchangeCardCmd.getIsUseMall() - ExchangeConstant.IsMallYes == 0) {
            // 兑换商城、只能部分可用
            exchangeCardCmd.setGoodsScope(ExchangeConstant.GoodsScopeAll);
        }
        // 校验参数
        ExchangeCardValidator.validParam(exchangeCardCmd, false);
        ExchangeCardDO exchangeCardDO = exchangeCardQryExe.getById(exchangeCardCmd.getId());
        /*if(exchangeCard.getStatus() !=ExchangeConstant.StatusInit){
            throw new BaseException(ExchangeErrorConstant.EditForbinWithOutInit.getCode(),ExchangeErrorConstant.EditForbinWithOutInit.getMsg());
        }*/
        if (exchangeCardCmd.getIsUseMall() - ExchangeConstant.IsMallYes == 0
            && exchangeCardDO.getStatus() - ExchangeConstant.StatusInit != 0
            && exchangeCardDO.getStatus() - ExchangeConstant.StatusEnd != 0) {
            List<Integer> materialIdList = null;
            if (exchangeCardCmd.getMaterialIdList() != null) {
                materialIdList = new ArrayList<>(exchangeCardCmd.getMaterialIdList());
            }
            List<Integer> modelIdList = null;
            if (exchangeCardCmd.getModelIdList() != null) {
                modelIdList = new ArrayList<>(exchangeCardCmd.getModelIdList());
            }
            List<Integer> pictureIdList = null;
            if (exchangeCardCmd.getPictureIdList() != null) {
                pictureIdList = new ArrayList<>(exchangeCardCmd.getPictureIdList());
            }
            // 非确定状态需要判断物料
            if (exchangeCardCmd.getIsConfirm() == null
                || exchangeCardCmd.getIsConfirm() - ExchangeConstant.IS_CONFIRM_YES != 0) {
                Boolean flag = exchangeCardValidator.checkItem(exchangeCardDO, materialIdList, modelIdList,
                    pictureIdList, true, exchangeCardCmd.getModelUseType(), exchangeCardCmd.getPictureUseType());
                if (flag) {
                    // 校验通过
                    exchangeCardCmd.setIsConfirm(ExchangeConstant.IS_CONFIRM_YES);
                }
            }
            // 判断是否确定修改
            if (exchangeCardCmd.getIsConfirm() != null
                && exchangeCardCmd.getIsConfirm() - ExchangeConstant.IS_CONFIRM_YES == 0) {
                // 确认修改、将该物料的关联的活动（未开始和进行中、暂停的材质、型号、图片关联全部覆盖）
                List<ExchangeCardDO> cardList =
                    exchangeCardQryExe.listByItemIdAndStatusList(exchangeCardDO.getItemId(), null);
                List<Integer> finalMaterialIdList = materialIdList;
                List<Integer> finalModelIdList = modelIdList;
                List<Integer> finalPictureIdList = pictureIdList;
                cardList.stream().forEach(card -> {
                    if (card.getStatus() - ExchangeConstant.StatusInit != 0
                        && card.getStatus() - ExchangeConstant.StatusEnd != 0) {
                        // 非初始化和结束的全部修改
                        List<ExchangeMaterialRelevanceDO> materialRelaList =
                            exchangeMaterialRelevanceServiceI.listByExchangeId(card.getId());
                        // 新对旧、判断新增
                        for (int x = 0; x < finalMaterialIdList.size(); x++) {
                            Boolean isNew = true;
                            for (int y = 0; y < materialRelaList.size(); y++) {
                                if (finalMaterialIdList.get(x) - materialRelaList.get(y).getMaterialId() == 0) {
                                    isNew = false;
                                    break;
                                }
                            }
                            if (isNew) {
                                // 新增一个材质
                                ExchangeMaterialRelevanceDO rela = new ExchangeMaterialRelevanceDO();
                                rela.setCreateTime(new Date());
                                rela.setExchangeId(card.getId());
                                rela.setMaterialId(finalMaterialIdList.get(x));
                                rela.setCreateUserName(currentAdmin.getUserName());
                                rela.setCreateUserId(currentAdmin.getId());
                                exchangeMaterialRelevanceServiceI.create(rela);
                            }
                        }
                        // 旧对新、判读删除
                        for (int x = 0; x < materialRelaList.size(); x++) {
                            Boolean isDelete = true;
                            for (int y = 0; y < finalMaterialIdList.size(); y++) {
                                if (materialRelaList.get(x).getMaterialId() - finalMaterialIdList.get(y) == 0) {
                                    isDelete = false;
                                    break;
                                }
                            }
                            if (isDelete) {
                                // 已删除
                                exchangeMaterialRelevanceServiceI.deleteById(materialRelaList.get(x).getId());
                            }
                        }
                        // 处理型号
                        List<ExchangeModelRelevanceDO> modelRelaList =
                            exchangeModelRelevanceServiceI.listByExchangeId(card.getId());

                        // 只有3种情况需要处理 1、原来是全部可用（后面修改是部分可用） 2、原来和现在都是部分可用 3、原来是部分可用、修改成全部可用
                        if (modelRelaList.get(0).getType() - ExchangeConstant.ExchangeModelUserTypeAll != 0
                            || exchangeCardCmd.getModelUseType() - ExchangeConstant.ExchangeModelUserTypeAll != 0) {
                            // 新对旧、判断是否要添加
                            if (finalModelIdList != null && finalModelIdList.size() > 0) {
                                for (int x = 0; x < finalModelIdList.size(); x++) {
                                    Boolean isNew = true;
                                    if (modelRelaList.get(0).getType()
                                        - ExchangeConstant.ExchangeModelUserTypeSome == 0) {
                                        for (int y = 0; y < modelRelaList.size(); y++) {
                                            if (finalModelIdList.get(x) - modelRelaList.get(y).getModelId() == 0) {
                                                isNew = false;
                                                break;
                                            }
                                        }
                                    }
                                    if (isNew) {
                                        addModelRela(currentAdmin, finalModelIdList.get(x), card.getId(),
                                            ExchangeConstant.ExchangeModelUserTypeSome);
                                    }
                                }
                            } else {
                                // 前面是部分可用、后面改成全部可用
                                addModelRela(currentAdmin, null, card.getId(),
                                    ExchangeConstant.ExchangeModelUserTypeAll);
                            }
                            // 旧对新、判断是否删除
                            for (int x = 0; x < modelRelaList.size(); x++) {
                                Boolean isDelete = true;
                                if (finalModelIdList != null && finalModelIdList.size() > 0
                                    && modelRelaList.get(0).getType()
                                        - ExchangeConstant.ExchangeModelUserTypeSome == 0) {
                                    for (int y = 0; y < finalModelIdList.size(); y++) {
                                        if (modelRelaList.get(x).getModelId() - finalModelIdList.get(y) == 0) {
                                            isDelete = false;
                                            break;
                                        }
                                    }
                                }
                                if (isDelete) {
                                    exchangeModelRelevanceServiceI.deleteById(modelRelaList.get(x).getId());
                                }
                            }
                        }

                        // 处理图片 1、原来是全部可用（后面修改是部分可用） 2、原来和现在都是部分可用 3、原来是部分可用、修改成全部可用
                        List<ExchangePictureRelevanceDO> pictureRelaList =
                            exchangePictureRelevanceServiceI.listByExchangeId(card.getId());
                        if (exchangeCardCmd.getPictureUseType() - ExchangeConstant.ExchangePictureUserTypeAll != 0
                            || pictureRelaList.get(0).getType() - ExchangeConstant.ExchangePictureUserTypeAll != 0) {
                            if (finalPictureIdList != null && finalPictureIdList.size() > 0) {
                                for (int x = 0; x < finalPictureIdList.size(); x++) {
                                    Boolean isNew = true;
                                    if (pictureRelaList.get(0).getType()
                                        - ExchangeConstant.ExchangePictureUserTypeSome == 0) {
                                        // 之前是部分可用
                                        for (int y = 0; y < pictureRelaList.size(); y++) {
                                            if (finalPictureIdList.get(x)
                                                - pictureRelaList.get(y).getPictureId() == 0) {
                                                isNew = false;
                                                break;
                                            }
                                        }
                                    }
                                    if (isNew) {
                                        addPictureRela(currentAdmin, finalPictureIdList.get(x), card.getId(),
                                            ExchangeConstant.ExchangePictureUserTypeSome);
                                    }
                                }
                            } else {
                                addPictureRela(currentAdmin, null, card.getId(),
                                    ExchangeConstant.ExchangePictureUserTypeAll);
                            }
                            // 旧对新
                            for (int x = 0; x < pictureRelaList.size(); x++) {
                                Boolean isDelete = true;
                                if (finalPictureIdList != null && finalPictureIdList.size() > 0
                                    && pictureRelaList.get(0).getType()
                                        - ExchangeConstant.ExchangePictureUserTypeSome == 0) {
                                    for (int y = 0; y < finalPictureIdList.size(); y++) {
                                        if (pictureRelaList.get(x).getPictureId() - finalPictureIdList.get(y) == 0) {
                                            isDelete = false;
                                            break;
                                        }
                                    }
                                }
                                if (isDelete) {
                                    exchangePictureRelevanceServiceI.deleteById(pictureRelaList.get(x).getId());
                                }
                            }
                        }
                    }
                });
            }
        }
        Boolean isFirst = exchangeCardValidator.validImportCodeIsFirst(exchangeCardCmd, exchangeCardDO);
        // 初始化状态才允许修改
        ExchangeCardConvertor.updateByStatusInit(exchangeCardDO, exchangeCardCmd);
        setAdminMsg(exchangeCardDO, currentAdmin);
        exchangeCardCmdExe.update(exchangeCardDO);
        //检查权益
        checkQuanyi(exchangeCardCmd);
        // 非初始化草稿状态不允许修改下面信息
        if (exchangeCardDO.getStatus() - ExchangeConstant.StatusInit == 0) {
            // 设置兑换码使用兑换商城
            setMallRela(exchangeCardCmd, exchangeCardDO.getId(), currentAdmin, false);
            // 设置是否生成实物卡
            exchangeEntityRuleServiceI.setEntityRule(exchangeCardCmd, exchangeCardDO.getId(), currentAdmin, false);
            // 设置excel导入的兑换码和明码
            exchangeCodeServiceI.saveExcelImportCode(exchangeCardCmd.getImportBeanList(), exchangeCardDO, isFirst,
                exchangeCardCmd.getCodeKey());
            // 设置兑换卡和分销商的关联关系
            exchangeDistributorRelevanceServiceI.saveRelevance(true, exchangeCardDO.getDistributorScope(),
                exchangeCardCmd.getDistributorList(), exchangeCardDO.getId(), currentAdmin);
        }
        // 转赠配置
        if (exchangeCardCmd.getTransfer() != null) {
            ExchangeCardTransferDO exchangeCardTransferDO = new ExchangeCardTransferDO();
            org.springframework.beans.BeanUtils.copyProperties(exchangeCardCmd.getTransfer(), exchangeCardTransferDO);
            exchangeCardTransferDO.setExchangeId(exchangeCardDO.getId());
            if (exchangeCardCmd.getTransfer().getId() == null) {
                exchangeCardTransferCmdExe.insert(exchangeCardTransferDO);
            } else {
                exchangeCardTransferCmdExe.update(exchangeCardTransferDO);
            }
        }
        return Response.buildSuccess();
    }

    /**
     * 图片和兑换活动关联
     *
     * @param currentAdmin
     * @param pictureId
     * @param exchangeId
     * @param type
     */
    private void addPictureRela(AdminResponse currentAdmin, Integer pictureId, Integer exchangeId, Short type) {
        ExchangePictureRelevanceDO rela = new ExchangePictureRelevanceDO();
        rela.setPictureId(pictureId);
        rela.setExchangeId(exchangeId);
        rela.setCreateTime(new Date());
        rela.setCreateUserId(currentAdmin.getId());
        rela.setCreateUserName(currentAdmin.getUserName());
        rela.setType(type);
        exchangePictureRelevanceServiceI.create(rela);
    }

    /**
     * 型号和兑换活动关联
     *
     * @param currentAdmin
     * @param modelId
     * @param exchangeId
     * @param type
     */
    private void addModelRela(AdminResponse currentAdmin, Integer modelId, Integer exchangeId, Short type) {
        ExchangeModelRelevanceDO rela = new ExchangeModelRelevanceDO();
        rela.setModelId(modelId);
        rela.setType(type);
        rela.setCreateTime(new Date());
        rela.setCreateUserId(currentAdmin.getId());
        rela.setCreateUserName(currentAdmin.getUserName());
        rela.setExchangeId(exchangeId);
        exchangeModelRelevanceServiceI.create(rela);
    }

    /**
     * 查看兑换码详情
     *
     * @param id
     * @return
     */
    @Override
    public Response<ExchangeCardDetailDTO> detailById(Integer id) {
        ExchangeCardDO exchangeCardDO = exchangeCardQryExe.getById(id);
        ExchangeCardDetailDTO exchangeCardDetailDTO = BeanUtils.copy(exchangeCardDO, ExchangeCardDetailDTO.class);
        // 设置是否实体卡
        if (exchangeCardDO.getIsEntity() - ExchangeConstant.IsEntityYes == 0) {
            ExchangeEntityRuleDO exchangeEntityRule =
                exchangeEntityRuleServiceI.getByExchangeId(exchangeCardDO.getId());
            exchangeCardDetailDTO.setCardType(exchangeEntityRule.getCardType());
            exchangeCardDetailDTO.setRuleType(exchangeEntityRule.getRuleType());
            exchangeCardDetailDTO.setRiseValue(exchangeEntityRule.getRiseValue());
            exchangeCardDetailDTO.setFloatValue(exchangeEntityRule.getFloatValue());
            exchangeCardDetailDTO.setRandomValue(exchangeEntityRule.getRandomValue());
            exchangeCardDetailDTO.setIsSyncFactory(exchangeEntityRule.getIsSyncFactory());
            exchangeCardDetailDTO.setBoxNum(exchangeEntityRule.getBoxNum());
        }
        // 获取转赠配置
        ExchangeCardTransferDO exchangeCardTransferDO = exchangeCardTransferQryExe.selectByExchangeId(id);
        if (exchangeCardTransferDO != null) {
            exchangeCardDetailDTO.setReceiveText(exchangeCardTransferDO.getReceiveText());
            exchangeCardDetailDTO.setReceiveImg(exchangeCardTransferDO.getReceiveImg());
            exchangeCardDetailDTO.setTransferText(exchangeCardTransferDO.getTransferText());
            exchangeCardDetailDTO.setTransferImg(exchangeCardTransferDO.getTransferImg());
            exchangeCardDetailDTO.setExchangeCardTransferId(exchangeCardTransferDO.getId());
            exchangeCardDetailDTO.setSwitchFlag(exchangeCardTransferDO.getSwitchFlag());
        }
        //设置兑换商城和 设置商品关联
        if(exchangeCardDO.getIsUseMall() - ExchangeConstant.IsMallYes ==0 || exchangeCardDO.getGoodsScope() - ExchangeConstant.GoodsScopeAssign ==0){
            //设置商品关联
            List<MaterialRelaSimpleDTO> materialPageBeanList = exchangeMaterialRelevanceServiceI.listDTOByExchangeId(id);
            exchangeCardDetailDTO.setMaterialPageBeanList(materialPageBeanList);

            List<ModelRelaSimpleDTO> modelBeanList = exchangeModelRelevanceServiceI.listDTOByExchangeId(id);
            exchangeCardDetailDTO.setModelBeanList(modelBeanList);
            if (modelBeanList == null || modelBeanList.size() == 0) {
                exchangeCardDetailDTO.setModelUseType(ExchangeConstant.ExchangeModelUserTypeAll);
            } else {
                exchangeCardDetailDTO.setModelUseType(ExchangeConstant.ExchangeModelUserTypeSome);
            }
            List<PictureRelaSimpleDTO> pictureBeanList = exchangePictureRelevanceServiceI.listDTOByExchangeId(id);
            exchangeCardDetailDTO.setPictureBeanList(pictureBeanList);
            if (pictureBeanList == null || pictureBeanList.size() == 0) {
                exchangeCardDetailDTO.setPictureUseType(ExchangeConstant.ExchangePictureUserTypeAll);
            } else {
                exchangeCardDetailDTO.setPictureUseType(ExchangeConstant.ExchangePictureUserTypeSome);
            }
        }
        if (exchangeCardDO.getItemId() != null) {
            List<Integer> itemIdList = new ArrayList<>();
            itemIdList.add(exchangeCardDO.getItemId());
            List<GoodsItemSimplePageBean> simplePageBeanList = new ArrayList<>();
            com.bat.dubboapi.goods.common.Response<List<GoodsItemRpcDTO>> itemListResponse =
                goodsServiceRpc.listGoodsItemByIds(itemIdList);
            GoodsItemRpcDTO goodsItemRpcDTO = itemListResponse.getData().get(0);
            GoodsItemSimplePageBean goodsItemSimplePageBean =
                BeanUtils.copy(goodsItemRpcDTO, GoodsItemSimplePageBean.class);
            goodsItemSimplePageBean.setItemId(goodsItemRpcDTO.getId());
            simplePageBeanList.add(goodsItemSimplePageBean);
            exchangeCardDetailDTO.setChooseCardItemList(simplePageBeanList);
        }
        if (ExchangeConstant.EXCHANGE_DISTRIBUTOR_SCOPE_ASSIGN.equals(exchangeCardDO.getDistributorScope())) {
            List<ExchangeDistributorRelevanceDO> distributorRelevanceDOList =
                exchangeDistributorRelevanceServiceI.listByExchangeIdAndDistributorId(exchangeCardDO.getId(), null);
            List<DistributorSimpleRelaQry> relaQryList =
                FlexibleProxyUtil.toDistributorSimpleRelaQryList(distributorRelevanceDOList);
            exchangeCardDetailDTO.setDistributorList(relaQryList);
        }

        return Response.of(exchangeCardDetailDTO);
    }

    /**
     * 修改状态
     *
     * @param exchangeCardStatusRequest
     * @param currentAdmin
     * @return
     */
    @Override
    @Transactional
    public Response updateStatus(ExchangeCardStatusCmd exchangeCardStatusRequest, AdminResponse currentAdmin) {
        ExchangeCardDO exchangeCardDO = exchangeCardQryExe.getById(exchangeCardStatusRequest.getId());
        if (exchangeCardDO.getStatus() - exchangeCardStatusRequest.getStatus() == 0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        if (exchangeCardStatusRequest.getStatus() - ExchangeConstant.StatusUnStart == 0
            && exchangeCardDO.getEndTime() < System.currentTimeMillis()) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_ISSUE_FAIL_ENDTIME_LESS_THAN_NOW);
        }
        if (exchangeCardStatusRequest.getStatus() - ExchangeConstant.StatusStop == 0
            && exchangeCardDO.getStatus() - ExchangeConstant.StatusStarting != 0) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_STOP_FAIL_STATUS_NOT_STARTING);
        }
        exchangeCardDO.setStatus(exchangeCardStatusRequest.getStatus());
        if (exchangeCardStatusRequest.getStatus() - ExchangeConstant.StatusUnStart == 0
            && exchangeCardDO.getStartTime() <= System.currentTimeMillis()) {
            // 发布时状态改为进行中
            exchangeCardDO.setStatus(ExchangeConstant.StatusStarting);
        }
        // 设置操作人信息
        setAdminMsg(exchangeCardDO, currentAdmin);

        exchangeCardCmdExe.update(exchangeCardDO);

        //检查权益
        List<MaterialRelaSimpleDTO> materialPageBeanList = exchangeMaterialRelevanceServiceI.listDTOByExchangeId(exchangeCardDO.getId());
        if(materialPageBeanList==null){
            materialPageBeanList=new ArrayList<>();
        }
        Set<Integer> materialIds = materialPageBeanList.stream().map(MaterialRelaSimpleDTO::getMaterialId).collect(Collectors.toSet());
        List<DistributorSimpleRelaQry> relaQryList = new ArrayList<>();
        if (ExchangeConstant.EXCHANGE_DISTRIBUTOR_SCOPE_ASSIGN.equals(exchangeCardDO.getDistributorScope())) {
            List<ExchangeDistributorRelevanceDO> distributorRelevanceDOList =
                    exchangeDistributorRelevanceServiceI.listByExchangeIdAndDistributorId(exchangeCardDO.getId(), null);
            relaQryList =
                    FlexibleProxyUtil.toDistributorSimpleRelaQryList(distributorRelevanceDOList);
        }
        ExchangeCardCmd exchangeCardCmd = new ExchangeCardCmd();
        exchangeCardCmd.setExchangeWay(exchangeCardDO.getExchangeWay());
        exchangeCardCmd.setDistributorScope(exchangeCardDO.getDistributorScope());
        exchangeCardCmd.setDistributorList(relaQryList);
        exchangeCardCmd.setMaterialIdList(materialIds);
        exchangeCardCmd.setIsEntity(exchangeCardDO.getIsEntity());
        //checkQuanyi(exchangeCardCmd);
        // 发布状态同步至工厂
        if (exchangeCardStatusRequest.getStatus() - ExchangeConstant.StatusUnStart == 0) {
            // 校验对应卡片是否重复
            exchangeCardValidator.checkItem(exchangeCardDO, null, null, null, false, null, null);

            exchangeCodeServiceI.syncFactory(exchangeCardDO, currentAdmin, null);
        }
        if (exchangeCardStatusRequest.getStatus() - ExchangeConstant.StatusUnStart == 0) {
            // 发布之后（未到开始时间）、开启定时器监控
            startExchangeEvent(exchangeCardDO.getStartTime(), exchangeCardDO.getEndTime(), exchangeCardDO.getId());
        }
        return Response.buildSuccess();
    }

    @Override
    public PageInfo<List<ExchangeCardPageCO>> page(ExchangeCardPageQry exchangeCardPageQry) {
        PageHelper.startPage(exchangeCardPageQry.getPage(), exchangeCardPageQry.getSize());
        List<ExchangeCardPageCO> list = exchangeCardQryExe.listCOByCondition(exchangeCardPageQry.getType(),
            exchangeCardPageQry.getStatus(), exchangeCardPageQry.getExchangeWay(), exchangeCardPageQry.getCodeName(),
            exchangeCardPageQry.getIsEntity());
        return new PageInfo(list);
    }

    @Override
    public Response qrCode(Integer id, AdminResponse currentAdmin, ExchangeCardDO exchangeCardDO) {
        if (exchangeCardDO == null) {
            exchangeCardDO = exchangeCardQryExe.getById(id);
            if (exchangeCardDO.getIsUseMall() - ExchangeConstant.IsMallNo == 0) {
                throw new FlexibleCustomException(
                    ExchangeCardErrorCode.EXCHANGE_CARD_QR_CODE_NULL_BECAUSE_NOT_BELONG_MALL);
            }
            if (StringUtils.isNotBlank(exchangeCardDO.getQrCodeUrl())) {
                return Response.of(exchangeCardDO.getQrCodeUrl());
            }
        }
        // InputStream inputStream = WechatUtil.getminiqrQr("exchangeId="+exchangeCardDO.getId());
        // 获取ppid
        // 平台类型 1、公众号、2小程序
        FlexibleConfig flexibleConfig=configQry.getTenantExchangeDistributorId();
        com.bat.dubboapi.distributor.common.Response<List<WxPlatformRpcDTO>> distributorResponse =
            wxPlatformServiceRpc.listByDistributorIdAndType(flexibleConfig.getExchangeDistributorId(),
                DistributorConstant.DISTRIBUTOR_WECHAT_TYPE_MINI_PROGRAM);
        if (distributorResponse == null || !distributorResponse.isSuccess()) {
            throw new FlexibleCustomException(FlexibleDubboServiceErrorCode.DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION);
        }
        List<WxPlatformRpcDTO> platformRpcDTOList = distributorResponse.getData();
        if (platformRpcDTOList == null || platformRpcDTOList.size() == 0) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_QR_CODE_FAIL_DISTRIBUTOR_RELA_WECHAT_NULL);
        }
        com.bat.dubboapi.thirdparty.common.Response<String> response = wxServiceRpc.createWechatProgramQrCode(platformRpcDTOList.get(0).getAppId(), "exchangeId=" + exchangeCardDO.getId(), null,
                System.currentTimeMillis() + ".jpg", "exchange/qr/",null);
        if(response ==null || !response.isSuccess() || StringUtils.isBlank(response.getData())){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_QR_CODE_FAIL);
        }
        exchangeCardDO.setQrCodeUrl(response.getData());
        setAdminMsg(exchangeCardDO, currentAdmin);
        exchangeCardCmdExe.update(exchangeCardDO);
        return Response.of(response.getData());
    }

    /**
     * 同步至工厂
     *
     * @return
     */
    /* @Override
    @Transactional
    public ResponseBaseBean syncFactory(ExchangeSyncFactoryRequest factoryRequest, AdminResponse currentAdmin) throws IOException {
        ExchangeCard exchangeCard = exchangeCardDataManager.findById(factoryRequest.getId());
        if(exchangeCard.getIsEntity() - ExchangeConstant.IsEntityNo ==0){
            throw new BaseException(ExchangeErrorConstant.SyncFactoryFailByIsEntityNo.getCode(),ExchangeErrorConstant.SyncFactoryFailByIsEntityNo.getMsg());
        }
        ExchangeEntityRule exchangeEntityRule = exchangeEntityRuleDBManager.findByExchangeId(exchangeCard.getId());
        if(exchangeEntityRule.getIsSyncFactory() - ExchangeConstant.isSyncFactoryNo==0){
            throw new BaseException(ExchangeErrorConstant.ExchangeNotNeedSyncFactoryError.getCode(),ExchangeErrorConstant.ExchangeNotNeedSyncFactoryError.getMsg());
        }
        if(StringUtils.isBlank(exchangeCard.getQrCodeUrl())){
            ResponseBaseBean responseBaseBean = qrCode(null,currentAdmin,exchangeCard);
            if(responseBaseBean.getData() !=null){
                exchangeCard.setQrCodeUrl((String) responseBaseBean.getData());
            }else{
                throw new BaseException(ExchangeErrorConstant.SyncFactoryFailByQrCodeUrlNull.getCode(),ExchangeErrorConstant.SyncFactoryFailByQrCodeUrlNull.getMsg());
            }
        }
        List<ExchangeFactory> exchangeFactoryList = exchangeFactoryDBManager.findByExchangeIdAndStatus(factoryRequest.getId(), ExchangeCodeConstant.SyncFactoryStatusUnSync);
        if(exchangeFactoryList==null || exchangeFactoryList.size()==0){
            throw new BaseException(ExchangeErrorConstant.SyncFactoryFailByDataNull.getCode(),ExchangeErrorConstant.SyncFactoryFailByDataNull.getMsg());
        }

        List<Long> factoryIdList = new ArrayList<>();
        for(int x=0;x<exchangeFactoryList.size();x++){
            ExchangeFactory exchangeFactory = exchangeFactoryList.get(x);
            Long count = exchangeFactory.getAddQuantity() - (exchangeFactory.getInvalidQuantityInit()==null?0L:exchangeFactory.getInvalidQuantityInit());
            if(exchangeFactory.getSynchronizedQuantity() !=null){
                count = count-exchangeFactory.getSynchronizedQuantity();
            }
            //过滤掉待同步为0的数量
            if(count>0){
                factoryIdList.add(exchangeFactory.getId());
            }else{
                //去掉无用的同步工厂
                exchangeFactoryList.remove(x);
                x--;
            }
        }
        List<ExchangeCode> codeList = exchangeCodeDataManager.listUnSyncByExchangeId(factoryRequest.getId(),factoryIdList);
        if(codeList ==null || codeList.size()==0){
            throw new BaseException(ExchangeErrorConstant.SyncFactoryFailByNoCode.getCode(),ExchangeErrorConstant.SyncFactoryFailByNoCode.getMsg());
        }

        Map<Long,Long> map = getFactoryQuantity(codeList);

        GoodsItem goodsItem = goodsItemDBManager.findById(exchangeCard.getItemId());
        exchangeFactoryList.stream().forEach(exchangeFactory -> {
            String content = getContent(codeList,exchangeFactory.getId());
           */
    /* try {
       File file = new File("d:/a.txt");
       file.createNewFile();
       BufferedWriter out = new BufferedWriter(new FileWriter(file));
       out.write(content); // \r\n即为换行
       out.flush(); // 把缓存区内容压入文件
       out.close(); // 最后记得关闭文件
    } catch (IOException e) {
       e.printStackTrace();
    }*//*

            InputStream inputStream = new ByteArrayInputStream(content.getBytes());
            String url = ossUtil.uploadObjectToOSS("EXCHANGE_"+exchangeFactory.getId()+".txt",currentAdmin.getId()+"",inputStream);
            LOGGER.info("生成的路径："+url);

            exchangeFactory.setStatus(ExchangeCodeConstant.SyncFactoryStatusRunning);
            exchangeFactory.setFileUrl(url);
            exchangeFactory.setUpdateTime(System.currentTimeMillis());
            exchangeFactory.setUpdateUserName(currentAdmin.getName());
            exchangeFactory.setUpdateUserId(currentAdmin.getId());
            exchangeFactory.setSynchronizedQuantity(map.get(exchangeFactory.getId()));
            exchangeFactory.setPositiveUrl(factoryRequest.getPositiveUrl());
            exchangeFactory.setReverseUrl(factoryRequest.getReverseUrl());
            exchangeFactory.setBatchOrderNo(OrderNoUtils.getExchangeSyncFactoryNo());
            //调用接口、同步至工厂
            ExchangeYunChuangRequest yunChuangRequest = getYunChuangSyncFactory(exchangeCard, goodsItem, exchangeFactory, url,exchangeCard.getQrCodeUrl());
            YunChuangExchangeResponse yunChuangExchangeResponse = HttpUtil.postYunChuang(YunChuangConfig.yunChuangSrmUrl +YunChuangConfig.syncYunChuangCodeUrl,
                 YunChuangConfig.yunChuangSrmAppId,YunChuangConfig.yunChuangSrmAppKey,
                 yunChuangRequest, YunChuangExchangeResponse.class);
            if(!yunChuangExchangeResponse.getSuccess()){
             throw new BaseException(ExchangeErrorConstant.ExchangeCodeSyncFactoryFail.getCode(),yunChuangExchangeResponse.getMsg());
            }
            exchangeFactory.setStatus(ExchangeCodeConstant.SyncFactoryStatusSuccess);
            exchangeFactoryDBManager.saveAndFlush(exchangeFactory);
            });

            return ResponseBaseBean.responseBean();
            }*/

    /*  private ExchangeYunChuangRequest getYunChuangSyncFactory(ExchangeCard exchangeCard, GoodsItem goodsItem, ExchangeFactory exchangeFactory, String url,String qrCodeUrl) {
        ExchangeYunChuangRequest yunChuangRequest = new ExchangeYunChuangRequest();
        yunChuangRequest.setCodetxturl(url);
        String endDateStr = DateUtil.getTime(exchangeCard.getEndTime(),"yyyy-MM-dd");
        yunChuangRequest.setDeadline(endDateStr);
        yunChuangRequest.setStarttime(DateUtil.getTime(exchangeCard.getStartTime(),"yyyy-MM-dd"));
        yunChuangRequest.setQuantiy(exchangeFactory.getAddQuantity().intValue());
        yunChuangRequest.setSkuno(goodsItem.getBarCode());
        yunChuangRequest.setSkuname(goodsItem.getItemName());
        yunChuangRequest.setImgurl(exchangeFactory.getPositiveUrl()+","+exchangeFactory.getReverseUrl()+","+qrCodeUrl);
        yunChuangRequest.setBatchno(exchangeFactory.getBatchOrderNo());
        yunChuangRequest.setModel(exchangeCard.getModelNo());
        yunChuangRequest.setAppid(YunChuangConfig.yunChuangSrmAppId);
        return yunChuangRequest;
    }*/

    @Override
    @Transactional
    public Response syncExchangeToErp(Integer exchangeId) {
        ExchangeCardDO exchangeCard = exchangeCardQryExe.getById(exchangeId);
        startExchangeEvent(exchangeCard.getStartTime(), exchangeCard.getEndTime(), exchangeId);
        ExchangeEntityRuleDO exchangeEntityRuleDO = exchangeEntityRuleServiceI.getByExchangeId(exchangeId);
        exchangeEntityRuleServiceI.syncErp(exchangeCard.getId(), exchangeEntityRuleDO);
        return Response.buildSuccess();
    }

    @Override
    public Response getDefaultExchangeIdNew(Integer exchangeId) {
        DefaultExchangeIdNewResponse response = new DefaultExchangeIdNewResponse();
        ExchangeCardDO exchangeCardDO = exchangeCardQryExe.getDefaultExchange(exchangeId);
        if (exchangeCardDO != null) {
            response.setExchangeId(exchangeCardDO.getId());
            if(ExchangeConstant.EXCHANGE_DISTRIBUTOR_SCOPE_ASSIGN.equals(exchangeCardDO.getDistributorScope())){
                //指定分销商、返回一个分销商id
                List<ExchangeDistributorRelevanceDO> distributorRelevanceDOList = exchangeDistributorRelevanceServiceI.listByExchangeIdAndDistributorId(exchangeCardDO.getId(),null);
                response.setDistributorId(distributorRelevanceDOList.get(0).getDistributorId());
            }
        }
        if (response.getExchangeId() != null) {
            LOGGER.info("=================获取默认的兑换卡id是：" + response.getExchangeId().toString() + "==========原来是===="
                + exchangeId);
        } else {
            LOGGER.info("=================获取默认的兑换卡id为空==============" + exchangeId);
        }
        return Response.of(response);
    }

    @Override
    public ExchangeCardDO getById(Integer exchangeId) {
        return exchangeCardQryExe.getById(exchangeId);
    }

    @Override
    public void updateDO(ExchangeCardDO exchangeCardDO) {
        exchangeCardCmdExe.update(exchangeCardDO);
    }

    /**
     * 根据兑换卡活动id查询材质和型号参数
     *
     * @param exchangeId
     * @return
     */
    @Override
    public ExchangeCardParamDTO listModelAndMaterial(Integer exchangeId) {
        ModelQry modelQry = new ModelQry();
        modelQry.setExchangeId(exchangeId);
        // 先找到材质
        List<ExchangeMaterialRelevanceDO> exchangeMaterialRelevanceDOList =
            exchangeMaterialRelevanceServiceI.listByExchangeId(exchangeId);
        modelQry.setMaterialId(exchangeMaterialRelevanceDOList.get(0).getMaterialId());
        List<ModelTreeCO> modelTreeCOList = modelServiceI.tree(modelQry);
        if (modelTreeCOList == null || modelTreeCOList.size() == 0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_MODEL_DATA_NULL);
        }
        Integer modelId = modelTreeCOList.get(0).getChildrenList().get(0).getModelId();
        MaterialTreeQry materialTreeQry = new MaterialTreeQry();
        materialTreeQry.setExchangeId(exchangeId);
        materialTreeQry.setModelId(modelId);
        List<MaterialTreeCO> materialTreeCOList = materialServiceI.tree(materialTreeQry);
        if (materialTreeCOList == null || materialTreeCOList.size() == 0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_MATERIAL_DATA_NULL);
        }
        ExchangeCardParamDTO exchangeCardParamDTO = new ExchangeCardParamDTO();
        exchangeCardParamDTO.setMaterialList(materialTreeCOList);
        exchangeCardParamDTO.setModelList(modelTreeCOList);
        /*   for(ModelParentDTO modelParentDTO:modelParentDTOList){
            for(ModelListDTO modelListDTO:modelParentDTO.getModelBeanList()){
                modelListDTO.setIsStockOut((short)0);
                modelListDTO.setCanUse((byte)1);
            }

        }*/
        // exchangeCardParamDTO.setPictureCategoryDTOList(pictureCategoryDTOList);
        return exchangeCardParamDTO;
    }

    // 根据货品id和分销商id查询是否是兑换码
    @Override
    public ExchangeCardDO checkIsExchangeByItemIdAndDistributorId(Integer itemId, Integer distributorId) {
        // 找出未开始、或者进行中的兑换卡活动列表
        List<Short> statusList = new ArrayList<>();
        statusList.add(ExchangeConstant.StatusUnStart);
        statusList.add(ExchangeConstant.StatusStarting);
        List<ExchangeCardDO> exchangeCardDOList = exchangeCardQryExe.listByItemIdAndStatusList(itemId, statusList);
        if (exchangeCardDOList == null || exchangeCardDOList.size() == 0) {
            // 不属于兑换卡活动
            return null;
        }
        Collections.sort(exchangeCardDOList, new Comparator<ExchangeCardDO>() {
            @Override
            public int compare(ExchangeCardDO o1, ExchangeCardDO o2) {
                return o2.getStatus() - o1.getStatus();
            }
        });
        for (int x = 0; x < exchangeCardDOList.size(); x++) {
            ExchangeCardDO exchangeCardDO = exchangeCardDOList.get(x);
            if (ExchangeConstant.EXCHANGE_DISTRIBUTOR_SCOPE_ALL.equals(exchangeCardDO.getDistributorScope())) {
                // 全部分销商可用、直接返回
                return exchangeCardDO;
            } else {
                // 判断这个分销商是否可用
                List<ExchangeDistributorRelevanceDO> list = exchangeDistributorRelevanceServiceI
                    .listByExchangeIdAndDistributorId(exchangeCardDO.getId(), distributorId);
                if (list != null && list.size() > 0) {
                    // 指定分销商可用、直接返回true
                    return exchangeCardDO;
                }
            }
        }
        return null;
    }

    @Override
    public List<ExchangeCardDO> listByIdList(List<Integer> exchangeIdList) {
        return exchangeCardQryExe.listByIdList(exchangeIdList);
    }

    @Override
    public ExchangeCardDO findQuanyiByDistributorIdAndMaterialId(Integer distributorId, Integer materialId) {
        return exchangeCardQryExe.findQuanyiByDistributorIdAndMaterialId( distributorId,  materialId) ;
    }

    /**
     * 获取同步到工厂的数量
     *
     * @param codeList
     * @return
     */
    private Map<Integer, Integer> getFactoryQuantity(List<ExchangeCodeDO> codeList) {
        Map<Integer, Integer> map = new HashMap<>();
        codeList.stream().forEach(exchangeCode -> {
            if (exchangeCode.getExchangeFactoryId() != null) {
                Integer count = 1;
                if (map.containsKey(exchangeCode.getExchangeFactoryId())) {
                    count = map.get(exchangeCode.getExchangeFactoryId()) + 1;
                }
                map.put(exchangeCode.getExchangeFactoryId(), count);
            }
        });
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Long> entry = (Map.Entry<Long, Long>)iterator.next();
            if (entry.getValue() % 10 != 0) {
                // 同步到工厂的数量必须要被10整除
                throw new FlexibleCustomException(
                    MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_BOX_CODE_NUM_ALIQUOT_TEN) + "批次id"
                        + entry.getKey() + "是" + entry.getValue() + "个，需要作废是" + entry.getValue() % 10 + "个再同步");
            }
        }

        return map;
    }

    /**
     * 获取txt内容
     *
     * @param codeList
     * @param exchangeFactoryId
     * @return
     */
    private String getContent(List<ExchangeCodeDO> codeList, Long exchangeFactoryId) {
        StringBuilder builder = new StringBuilder();
        codeList.stream().forEach(exchangeCode -> {
            if (exchangeCode.getExchangeFactoryId() - exchangeFactoryId == 0) {
                // builder.append(exchangeFactoryId+"\t");
                String secretCode = exchangeCode.getSecretCode();
                secretCode = AESUtil.decrypt(secretCode);
                // builder.append(exchangeCode.getPlainCode()+"\t"+secretCode+"\t"+qrCodeUrl+"\t"+itemName+"\r\n");
                builder.append(exchangeCode.getPlainCode() + "\t" + secretCode + "\r\n");
            }
        });
        return builder.toString();
    }

    /**
     * 设置兑换码使用兑换商城
     *
     * @param exchangeCardCmd
     * @param exchangeId
     *            兑换码活动id
     * @param currentAdmin
     * @param isNew
     */
    private void setMallRela(ExchangeCardCmd exchangeCardCmd, Integer exchangeId, AdminResponse currentAdmin,
        boolean isNew) {
        if (exchangeCardCmd.getIsUseMall() - ExchangeConstant.IsMallNo == 0) {
            return;
        }
        // 设置材质
        exchangeMaterialRelevanceServiceI.saveRelevance(isNew, exchangeId,
            new ArrayList<>(exchangeCardCmd.getMaterialIdList()), currentAdmin);
        // 设置型号
        exchangeModelRelevanceServiceI.saveRelevance(isNew, exchangeId,
            new ArrayList<>(exchangeCardCmd.getModelIdList()), currentAdmin);
        // 设置图片
        exchangePictureRelevanceServiceI.saveRelevance(isNew, exchangeId,
            new ArrayList<>(exchangeCardCmd.getPictureIdList()), currentAdmin);
    }

    /**
     * 设置操作人信息
     *
     * @param exchangeCardDO
     * @param currentAdmin
     */
    private void setAdminMsg(ExchangeCardDO exchangeCardDO, AdminResponse currentAdmin) {
        if (exchangeCardDO.getId() == null) {
            exchangeCardDO.setCreateUserId(currentAdmin.getId());
            exchangeCardDO.setCreateUserName(currentAdmin.getUserName());
            exchangeCardDO.setCreateTime(new Date());
        }
        exchangeCardDO.setUpdateTime(new Date());
        exchangeCardDO.setUpdateUserId(currentAdmin.getId());
        exchangeCardDO.setUpdateUserName(currentAdmin.getUserName());
    }

    /**
     * 开启定时任务
     *
     * @param startTime
     * @param endTime
     * @param exchangeId
     *            兑换卡活动id
     */
    public void startExchangeEvent(Long startTime, Long endTime, Integer exchangeId) {
        Long currentTime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 使用了默认的格式创建了一个日期格式化对象。
        if (exchangeId == null) {
            return;
        }
        if (currentTime < startTime) {// 开始时间晚于当前时间
            Date date = new Date(startTime);
            String time = dateFormat.format(date);
            exchangeCardCmdExe.createStartExchangeEvent(time, exchangeId);
        }
        if (currentTime < endTime) {// 结束时间晚于当前时间
            Date date = new Date(endTime);
            String time = dateFormat.format(date);
            exchangeCardCmdExe.createEndExchangeEvent(time, exchangeId);
            exchangeCodeServiceI.createEndExchangeEvent(time, exchangeId);
        }
    }

}
