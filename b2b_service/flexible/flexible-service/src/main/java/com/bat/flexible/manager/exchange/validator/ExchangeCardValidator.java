package com.bat.flexible.manager.exchange.validator;

import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.exchange.*;
import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeOrderDTO;
import com.bat.flexible.api.exchange.*;
import com.bat.flexible.api.exchange.dto.ExchangeCardCmd;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import com.bat.flexible.manager.common.constant.exchange.ExchangeConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeRxErrorConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;
import com.bat.flexible.manager.exchange.executor.ExchangeCardQryExe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExchangeCardValidator {


    private static ExchangeCodeServiceI exchangeCodeServiceI;

    @Resource
    public  void setExchangeCodeServiceI(ExchangeCodeServiceI exchangeCodeServiceI) {
        ExchangeCardValidator.exchangeCodeServiceI = exchangeCodeServiceI;
    }


    @Autowired
    private ExchangeCardQryExe exchangeCardQryExe;

    @Autowired
    private ModelServiceI modelServiceI;

    @Autowired
    private ExchangeMaterialRelevanceServiceI exchangeMaterialRelevanceServiceI;

    @Autowired
    private MaterialServiceI materialServiceI;

    @Autowired
    private ExchangePictureRelevanceServiceI exchangePictureRelevanceServiceI;

    @Autowired
    private PictureServiceI pictureServiceI;

    @Autowired
    private ExchangeModelRelevanceServiceI exchangeModelRelevanceServiceI;

    @Autowired
    private ExchangeEntityRuleServiceI exchangeEntityRuleServiceI;
    /**
     * 校验参数
     */
    public static void validParam(ExchangeCardCmd exchangeCardRequest, Boolean isNew) {
        if(isNew && exchangeCardRequest.getId() !=null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_MUST_BE_NULL_WHEN_ADD);
        }
        if(!isNew && exchangeCardRequest.getId() ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        if(exchangeCardRequest.getEndTime() <=exchangeCardRequest.getStartTime()){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_ENDTIME_LESSTHEN_STARTTIME);
        }
        if(exchangeCardRequest.getEndTime() <=System.currentTimeMillis()){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_ENDTIME_LESSTHEN_NOW);
        }
        if(exchangeCardRequest.getIsEntity()- ExchangeConstant.IsEntityYes == 0 && exchangeCardRequest.getCardType() ==null){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CARD_TYPE_NULL_WHEN_ENTITY);
        }
        if(exchangeCardRequest.getIsEntity()- ExchangeConstant.IsEntityYes ==0 && exchangeCardRequest.getCardType() ==null){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_RULE_TYPE_NULL_WHEN_ENTITY);
        }
        if(exchangeCardRequest.getIsUseMall() - ExchangeConstant.IsMallYes ==0 && exchangeCardRequest.getMallType() ==null){
            throw  new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_MALL_TYPE_NULL_CHOOSE_EXCHANGE);
        }
        if(exchangeCardRequest.getIsUseMall() - ExchangeConstant.IsMallYes ==0 && exchangeCardRequest.getModelUseType() ==null){
            throw  new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_MODEL_USE_TYPE_NULL);
        }
        if(exchangeCardRequest.getIsUseMall() - ExchangeConstant.IsMallYes ==0 && exchangeCardRequest.getPictureUseType() ==null){
            throw  new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_PICTURE_USE_TYPE_NULL);
        }

        if(exchangeCardRequest.getIsUseMall() - ExchangeConstant.IsMallYes ==0 && (exchangeCardRequest.getModelIdList() ==null || exchangeCardRequest.getModelIdList().size()==0)
                && exchangeCardRequest.getModelUseType()-ExchangeConstant.ExchangeModelUserTypeSome==0
        ){
            throw  new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_MODEL_USE_TYPE_NULL);
        }

        if(exchangeCardRequest.getIsUseMall() - ExchangeConstant.IsMallYes ==0 &&
                exchangeCardRequest.getPictureUseType() !=null && exchangeCardRequest.getPictureUseType() - ExchangeConstant.ExchangePictureUserTypeSome==0 &&
                (exchangeCardRequest.getPictureIdList() ==null || exchangeCardRequest.getPictureIdList().size()==0)){
            throw  new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_PICTURE_NULL_CHOOSE_MALL);
        }
        if (exchangeCardRequest.getIsUseMall() - ExchangeConstant.IsMallYes == 0 && exchangeCardRequest.getItemId() == null &&
                exchangeCardRequest.getMallType() - ExchangeConstant.MallTypeExchangeDiy == 0) {
            if (exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes == 0) {
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CARD_NULL_CHOOSE_MALL);
            }
        }
        if(exchangeCardRequest.getIsUseMall() - ExchangeConstant.IsMallYes ==0 && StringUtils.isBlank(exchangeCardRequest.getModelNo()) &&
                exchangeCardRequest.getMallType() - ExchangeConstant.MallTypeExchangeDiy ==0){
            throw  new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_MODEL_NO_NULL_CHOOSE_MALL);
        }
        if(exchangeCardRequest.getOrderUseThreshold() !=null && exchangeCardRequest.getOrderUseThreshold().compareTo(BigDecimal.ZERO)==-1){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_ORDER_THRESHOLD_LESSTHEN_ZERO);
        }
        if(exchangeCardRequest.getLimitQuantity() !=null && exchangeCardRequest.getLimitQuantity()< 1){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_USER_LIMIT_QUANTITY_LESS_THEN_ONE);
        }
        if(exchangeCardRequest.getGoodsScope() - ExchangeConstant.GoodsScopeAssign==0){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_NO_SUPPORT_ASSIGN_ITEM);
        }
        if(exchangeCardRequest.getRuleType() !=null && exchangeCardRequest.getRuleType() - ExchangeConstant.RuleTypeSystemRandom ==0 && exchangeCardRequest.getRandomValue()==null){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_RANDOM_VALUE_NULL_CHOOSE_RANDOM);
        }
        if(exchangeCardRequest.getRuleType() !=null && exchangeCardRequest.getRuleType() - ExchangeConstant.RuleTypeSystemRandom ==0 && exchangeCardRequest.getRandomValue()!=null
                && exchangeCardRequest.getRandomValue() !=6 && exchangeCardRequest.getRandomValue() !=7
        ){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_RANDOM_NUM_LENGTH_ONLY_SIX_SEVEN);
        }
        if(exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes ==0 &&
                exchangeCardRequest.getCardType() - ExchangeConstant.CardTypeSystem==0 &&
                exchangeCardRequest.getRuleType() - ExchangeConstant.RuleTypeRule ==0
                && StringUtils.isBlank(exchangeCardRequest.getRiseValue())){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_RISE_VALUE_NULL_CHOOSE_RULE);
        }
        if(exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes ==0 && exchangeCardRequest.getCardType() - ExchangeConstant.CardTypeSystem==0 &&
                exchangeCardRequest.getRuleType() - ExchangeConstant.RuleTypeRule ==0
                && StringUtils.isBlank(exchangeCardRequest.getFloatValue()) ){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_FLOAT_VALUE_NULL_CHOOSE_RULE);
        }
        if( exchangeCardRequest.getIsEntity()- ExchangeConstant.IsEntityNo==0 && exchangeCardRequest.getSource()==null){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_SOURCE_NULL_WHEN_VIRTUAL);
        }
        if( ((exchangeCardRequest.getIsEntity()- ExchangeConstant.IsEntityNo==0 && exchangeCardRequest.getSource()-ExchangeConstant.SourceSystem==0)
                || (exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes==0 && exchangeCardRequest.getCardType() - ExchangeConstant.CardTypeSystem==0))
                && exchangeCardRequest.getCodeQuantity() ==null
        ){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_QUANTITY_NULL);
        }

        if(StringUtils.isNotBlank(exchangeCardRequest.getRiseValue())){
            exchangeCardRequest.setRiseValue(exchangeCardRequest.getRiseValue().toUpperCase());
        }
        //清除型号id列表
        if(exchangeCardRequest.getModelUseType() !=null && exchangeCardRequest.getModelUseType() - ExchangeConstant.ExchangeModelUserTypeAll==0
                && exchangeCardRequest.getModelIdList() !=null && exchangeCardRequest.getModelIdList().size()>0
        ){
            exchangeCardRequest.setModelIdList(null);
        }
        //清除图片id列表
        if(exchangeCardRequest.getPictureUseType() !=null && exchangeCardRequest.getPictureUseType() - ExchangeConstant.ExchangePictureUserTypeAll==0
                && exchangeCardRequest.getPictureIdList() !=null && exchangeCardRequest.getPictureIdList().size()>0
        ){
            exchangeCardRequest.setPictureIdList(null);
        }
        //判断实体卡时 是否同步工厂
        if(exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes==0 && exchangeCardRequest.getIsSyncFactory()==null){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_IS_SYNC_FACTORY_NULL_CHOOSE_ENTITY);
        }
        //判断实体卡时 盒装数量
        if(exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes==0 && exchangeCardRequest.getBoxNum()==null){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_BOX_NUM_NULL_CHOOSE_ENTITY);
        }
        //判断实体卡时 盒装数量是否小于1
        if(exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes==0 && exchangeCardRequest.getBoxNum()<1){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_BOX_NUM_LESS_THAN_ZERO_CHOOSE_ENTITY);
        }
        //分销商范围校验
        validDistributorScope(exchangeCardRequest);
        if(ExchangeConstant.EXCHANGE_MAIL_TYPE_GENERAL.equals(exchangeCardRequest.getMailSetting())){
            exchangeCardRequest.setMailFee(BigDecimal.ZERO);
        }
        exchangeCodeServiceI.checkExcelImportCode(exchangeCardRequest);

    }

    //分销商范围校验
    private static void validDistributorScope(ExchangeCardCmd exchangeCardRequest) {
        List<DistributorSimpleRelaQry> distributorList = exchangeCardRequest.getDistributorList();
        if(ExchangeConstant.EXCHANGE_DISTRIBUTOR_SCOPE_ASSIGN.equals(exchangeCardRequest.getDistributorScope())
        && (distributorList == null || distributorList.size()==0)){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_DISTRIBUTOR_LIST_NULL_WHEN_SCOPE_ASSIGN);
        }
        if(ExchangeConstant.EXCHANGE_DISTRIBUTOR_SCOPE_ALL.equals(exchangeCardRequest.getDistributorScope())){
            distributorList =null;
        }
        if(ExchangeConstant.EXCHANGE_DISTRIBUTOR_SCOPE_ASSIGN.equals(exchangeCardRequest.getDistributorScope())){
            distributorList.stream().forEach(distributorSimpleRelaQry -> {
                if(distributorSimpleRelaQry.getDistributorId() ==null){
                    throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL);
                }
                if( StringUtils.isBlank(distributorSimpleRelaQry.getDistributorName())){
                    throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_NAME_NULL);
                }
                if( StringUtils.isBlank(distributorSimpleRelaQry.getDistributorCompanyName())){
                    throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_COMPANY_NAME_NULL);
                }
            });
        }
    }

    /**
     *
     * @param exchangeCardDO 兑换卡对象
     * @param materialIdList 新的材质id列表
     * @param modelIdList 新的型号id列表
     * @param pictureIdList 新的图片id列表
     * @param isNeedConfirm
     * @param modelUseType
     * @param pictureUseType
     */
    public Boolean checkItem(ExchangeCardDO exchangeCardDO, List<Integer> materialIdList, List<Integer> modelIdList, List<Integer> pictureIdList, Boolean isNeedConfirm,
                             Short modelUseType, Short pictureUseType) {
        if(exchangeCardDO.getItemId() !=null){
            List<Short> statusList = new ArrayList<>();
            statusList.add(ExchangeConstant.StatusUnStart);
            statusList.add(ExchangeConstant.StatusStarting);
            List<ExchangeCardDO> cardList = exchangeCardQryExe.listByItemIdAndStatusList(exchangeCardDO.getItemId(),statusList);
            if(cardList ==null || cardList.size()==0){
                return true;
            }
           /* if(cardList !=null && cardList.size()>0){
                //去掉初始化状态和已结束的
                for(int x=0;x<cardList.size();x++){
                    if((cardList.get(x).getStatus() - ExchangeConstant.StatusInit ==0) || (cardList.get(x).getStatus() - ExchangeConstant.StatusEnd==0)
                            || exchangeCardDO.getId() - cardList.get(x).getId()==0
                    ){
                        cardList.remove(x);
                        x--;
                    }
                }
            }else{
                //没有重复的兑换卡活动
                return true;
            }*/
           /* if(cardList !=null && cardList.size()>1){
                throw new BaseException(ExchangeErrorConstant.ItemAssociatedExchanegMoreThenOne.getCode(),ExchangeErrorConstant.ItemAssociatedExchanegMoreThenOne.getMsg());
            }
            if(cardList !=null && cardList.size()==1 && cardList.get(0).getId()-exchangeCard.getId() !=0){
                throw new BaseException(ExchangeErrorConstant.ItemAssociatedExchanegMoreThenOne.getCode(),ExchangeErrorConstant.ItemAssociatedExchanegMoreThenOne.getMsg());
            }*/
            if(cardList !=null && cardList.size()>0){
                //校验材质、型号、图片关系是否一致、保证进行中的、未开始的一致
                if(materialIdList ==null || materialIdList.size()==0){
                    List<ExchangeMaterialRelevanceDO> thisMaterialRelaList = exchangeMaterialRelevanceServiceI.listByExchangeId(exchangeCardDO.getId());
                    materialIdList = new ArrayList<>();
                    for(int i=0;i<thisMaterialRelaList.size();i++){
                        materialIdList.add(thisMaterialRelaList.get(i).getMaterialId());
                    }
                }
                String errorMsg = "";
                List<ExchangeMaterialRelevanceDO> oldMaterialRelaList = exchangeMaterialRelevanceServiceI.listByExchangeId(cardList.get(0).getId());
                if(oldMaterialRelaList !=null && oldMaterialRelaList.size()>0 && materialIdList !=null && materialIdList.size()>0){
                    //对比两个材质是否一致
                    //新对旧
                    String msg ="材质【";
                    for(int x=0;x<materialIdList.size();x++){
                        Boolean isNew = true;
                        for(int y=0;y<oldMaterialRelaList.size();y++){
                            if(materialIdList.get(x) - oldMaterialRelaList.get(y).getMaterialId()==0){
                                isNew = false;
                                break;
                            }
                        }
                        if(isNew){
                            MaterialDO materialDO = materialServiceI.getById(materialIdList.get(x));
                            msg+=materialDO.getName()+"、";
                        }
                    }
                    if(msg.length()>3){
                        //存在新增
                        if(isNeedConfirm){
                            msg = msg.substring(0,msg.length()-1)+"】与该卡片对应其他兑换卡活动的材质不匹配；";
                        }else{
                            msg = msg.substring(0,msg.length()-1)+"】与该卡片对应的物料不匹配、需要移除；";
                        }
                    }
                    String deleteMsg ="材质【";
                    for(int x=0;x<oldMaterialRelaList.size();x++){
                        Boolean isDelete = true;
                        for(int y=0;y<materialIdList.size();y++){
                            if(oldMaterialRelaList.get(x).getMaterialId() !=null && oldMaterialRelaList.get(x).getMaterialId() - materialIdList.get(y)==0){
                                isDelete = false;
                                break;
                            }
                        }
                        if(isDelete && oldMaterialRelaList.get(x).getMaterialId() !=null){
                            MaterialDO materialDO = materialServiceI.getById(oldMaterialRelaList.get(x).getMaterialId());
                            deleteMsg+=materialDO.getName()+"、";
                        }
                    }
                    if(deleteMsg.length()>3){
                        if(isNeedConfirm){
                            deleteMsg = deleteMsg.substring(0,deleteMsg.length()-1)+"】与该卡片对应其他兑换卡活动的材质不匹配；";
                        }else{
                            deleteMsg = deleteMsg.substring(0,deleteMsg.length()-1)+"】与该卡片对应的物料不匹配、需要增加该材质或者更换物料；";
                        }
                        if(msg.length()>3){
                            msg+=deleteMsg;
                        }else{
                            msg = deleteMsg;
                        }
                    }
                    if(msg.length()>3){
                        errorMsg+=msg;
                    }
                }

                //设置型号校验标志
                Boolean modelFlag = true;
                //校验型号
                //判断是否是下面情况：原来是全部可用、后期改成部分可用
                if(modelIdList !=null && modelIdList.size() >0){

                    List<ExchangeModelRelevanceDO> thisModelRelaList = exchangeModelRelevanceServiceI.listByExchangeId(exchangeCardDO.getId());
                    if(thisModelRelaList.get(0).getType() - ExchangeConstant.ExchangeModelUserTypeAll==0){
                        if(errorMsg.length()>0){
                            errorMsg+=",且";
                        }
                        errorMsg+="该物料原来对应的兑换卡活动是型号全部可用；";
                        modelFlag=false;
                    }
                }
                if(modelIdList ==null || modelIdList.size()==0){
                    modelIdList = new ArrayList<>();
                    List<ExchangeModelRelevanceDO> thisModelRelaList = exchangeModelRelevanceServiceI.listByExchangeId(exchangeCardDO.getId());
                    if(thisModelRelaList.get(0).getType() - ExchangeConstant.ExchangeModelUserTypeSome==0 && modelUseType !=null && modelUseType-ExchangeConstant.ExchangeModelUserTypeAll==0){
                        //之前是部分可用、后面改成全部可用
                        if(errorMsg.length()>0){
                            errorMsg+=",且";
                        }
                        errorMsg+="该物料原来对应的兑换卡活动是型号部分可用；";
                        modelFlag=false;
                    }
                    if(modelFlag && thisModelRelaList.get(0).getType() - ExchangeConstant.ExchangeModelUserTypeSome==0){
                        for(int i=0; i<thisModelRelaList.size();i++){
                            modelIdList.add(thisModelRelaList.get(i).getModelId());
                        }
                    }
                }


                List<ExchangeModelRelevanceDO> oldModelRelaList = exchangeModelRelevanceServiceI.listByExchangeId(cardList.get(0).getId());
                if(modelUseType==null){
                    //判断原来是部分可用、现在是全部可用
                    if(oldModelRelaList.get(0).getType() - ExchangeConstant.ExchangeModelUserTypeSome==0 && (modelIdList==null || modelIdList.size()==0)){
                        if(errorMsg.length()>0){
                            errorMsg+="\r\n";
                        }
                        //该物料绑定的兑换卡之前是部分可用、后面改成全部可用
                        if(errorMsg.length()>0){
                            errorMsg+=",且";
                        }
                        errorMsg+="该物料原来对应的兑换卡活动是型号部分可用；";
                        modelFlag=false;
                    }

                }
                if(modelFlag){
                    if(modelIdList !=null && modelIdList.size()>0 && oldModelRelaList !=null && oldModelRelaList.size()>0){
                        String msg = "型号【";
                        //新对旧
                        for(int x=0;x<modelIdList.size();x++){
                            Boolean isNew = true;
                            for(int y=0;y<oldModelRelaList.size();y++){
                                //原来是部分可用
                                if(oldModelRelaList.get(y).getType() - ExchangeConstant.ExchangeModelUserTypeSome==0 &&
                                        modelIdList.get(x) - oldModelRelaList.get(y).getModelId() ==0){
                                    isNew = false;
                                    break;
                                }
                            }
                            if(isNew){
                                ModelDO modelDO = modelServiceI.getById(modelIdList.get(x).intValue());
                                msg+=modelDO.getName()+"、";
                            }
                        }
                        if(msg.length()>3){
                            //存在新增
                            if(isNeedConfirm){
                                msg = msg.substring(0,msg.length()-1)+"】与该卡片物料对应其他兑换卡活动的图片不匹配；";
                            }else{
                                msg = msg.substring(0,msg.length()-1)+"】与该卡片对应的物料不匹配、需要移除；";
                            }
                        }
                        String deleteMsg ="型号【";
                        for(int x=0;x<oldModelRelaList.size();x++){
                            Boolean isDelete = true;
                            for(int y=0;y<modelIdList.size();y++){
                                if(oldModelRelaList.get(x).getType() - ExchangeConstant.ExchangeModelUserTypeSome==0 &&
                                        oldModelRelaList.get(x).getModelId() - modelIdList.get(y) ==0){
                                    isDelete = false;
                                    break;
                                }
                            }
                            if( oldModelRelaList.get(x).getType() - ExchangeConstant.ExchangeModelUserTypeSome==0 && isDelete){
                                ModelDO modelDO = modelServiceI.getById(oldModelRelaList.get(x).getModelId().intValue());
                                deleteMsg+=modelDO.getName()+"、";
                            }
                        }
                        if(deleteMsg.length()>3){
                            if(isNeedConfirm){
                                deleteMsg = deleteMsg.substring(0,deleteMsg.length()-1)+"】与该卡片物料对应的其他兑换卡活动的型号不匹配；";
                            }else {
                                deleteMsg = deleteMsg.substring(0,deleteMsg.length()-1)+"】与该卡片对应的物料不匹配、需要增加该型号或者更换物料；";
                            }
                            if(msg.length()>3){
                                msg+=deleteMsg;
                            }else{
                                msg = deleteMsg;
                            }
                        }
                        if(msg.length()>3){
                            if(errorMsg.length()>0){
                                errorMsg+="\r\n";
                            }
                            errorMsg+=msg;
                        }
                    }
                }
                //设置图片校验标志
                Boolean pictureFlag = true;
                //校验是否是下面情况 ：原来是全部可用、后期改成部分可用
                if(pictureIdList !=null && pictureIdList.size() >0){
                    List<ExchangePictureRelevanceDO> thisPictureRelaList = exchangePictureRelevanceServiceI.listByExchangeId(exchangeCardDO.getId());
                    if(thisPictureRelaList.get(0).getType() - ExchangeConstant.ExchangePictureUserTypeAll==0 ){
                        //之前是部分可用、后面改成全部可用
                        if(errorMsg.length()>0){
                            errorMsg+="\r\n";
                        }
                        if(errorMsg.length()>0){
                            errorMsg+=",且";
                        }
                        errorMsg+="该物料原来对应的兑换卡活动是图片全部可用；";
                        pictureFlag=false;
                    }
                }
                if(pictureIdList ==null || pictureIdList.size()==0){
                    pictureIdList = new ArrayList<>();
                    List<ExchangePictureRelevanceDO> thisPictureRelaList = exchangePictureRelevanceServiceI.listByExchangeId(exchangeCardDO.getId());
                    if(thisPictureRelaList.get(0).getType() - ExchangeConstant.ExchangePictureUserTypeSome==0 && pictureUseType !=null &&
                            pictureUseType-ExchangeConstant.ExchangePictureUserTypeAll==0){
                        //之前是部分可用、后面改成全部可用
                        if(errorMsg.length()>0){
                            errorMsg+="\r\n";
                        }
                        if(errorMsg.length()>0){
                            errorMsg+="且";
                        }
                        errorMsg+="该物料原来对应的兑换卡活动是图片部分可用；";
                        pictureFlag=false;
                    }
                    if(thisPictureRelaList.get(0).getType() - ExchangeConstant.ExchangePictureUserTypeSome==0){
                        for(int i=0;i<thisPictureRelaList.size();i++){
                            pictureIdList.add(thisPictureRelaList.get(i).getPictureId());
                        }
                    }
                }

                List<ExchangePictureRelevanceDO> oldPictureRelaList = exchangePictureRelevanceServiceI.listByExchangeId(cardList.get(0).getId());
                if(pictureUseType==null){
                    if(oldPictureRelaList.get(0).getType() - ExchangeConstant.ExchangePictureUserTypeSome==0 && (pictureIdList==null || pictureIdList.size()==0)){
                        //该物料绑定的兑换卡之前是部分可用、后面改成全部可用
                        if(errorMsg.length()>0){
                            errorMsg+="且";
                        }
                        errorMsg+="该物料原来对应的兑换卡活动图片是部分可用；";
                        pictureFlag=false;
                    }
                }
                if(pictureFlag){
                    if(pictureIdList !=null && pictureIdList.size()>0 && oldPictureRelaList !=null && oldPictureRelaList.size()>0){
                        String msg ="图片【";
                        for(int x=0;x<pictureIdList.size();x++){
                            Boolean isNew = true;
                            for(int y=0;y<oldPictureRelaList.size();y++){
                                //原来是部分可用
                                if(oldPictureRelaList.get(y).getType() - ExchangeConstant.ExchangePictureUserTypeSome ==0 &&
                                        pictureIdList.get(x) - oldPictureRelaList.get(y).getPictureId()==0){
                                    isNew = false;
                                    break;
                                }
                            }
                            if(isNew){
                                PictureDO pictureDO = pictureServiceI.getById(pictureIdList.get(x));
                                msg+=pictureDO.getName()+"、";
                            }
                        }
                        if(msg.length()>3){
                            if(isNeedConfirm){
                                msg = msg.substring(0,msg.length()-1)+"】与该卡片物料对应其他兑换卡活动的图片不匹配；";
                            }else{
                                msg = msg.substring(0,msg.length()-1)+"】与该卡片对应的物料不匹配、需要移除；";
                            }
                        }
                        String deleteMsg = "图片【";
                        for(int x=0;x<oldPictureRelaList.size();x++){
                            Boolean isDelete = true;
                            for(int y=0;y<pictureIdList.size();y++){
                                if(oldPictureRelaList.get(x).getType() - ExchangeConstant.ExchangePictureUserTypeSome ==0 &&
                                        oldPictureRelaList.get(x).getPictureId() - pictureIdList.get(y)==0){
                                    isDelete = false;
                                    break;
                                }
                            }
                            if(oldPictureRelaList.get(x).getType() - ExchangeConstant.ExchangePictureUserTypeSome ==0 && isDelete){
                                PictureDO pictureDO = pictureServiceI.getById(oldPictureRelaList.get(x).getPictureId());
                                deleteMsg+=pictureDO.getName()+"、";
                            }
                        }
                        if(deleteMsg.length()>3){
                            if(isNeedConfirm){
                                deleteMsg = deleteMsg.substring(0,deleteMsg.length()-1)+"】与该卡片对应其他兑换卡活动的图片不匹配；";
                            }else {
                                deleteMsg = deleteMsg.substring(0,deleteMsg.length()-1)+"】与该卡片对应的物料不匹配、需要移除；";
                            }
                            if(msg.length()>3){
                                msg+=deleteMsg;
                            }else{
                                msg = deleteMsg;
                            }
                        }
                        if(msg.length()>3){
                            if(errorMsg.length()>0){
                                errorMsg+="\r\n";
                            }
                            errorMsg+=msg;
                        }
                    }
                }
                if(errorMsg.length()>0){
                    throw new FlexibleCustomException(errorMsg.substring(0,errorMsg.length()-1));
                }
            }
        }
        return true;
    }

    /**
     * 判断导入的兑换码是否第一次
     * @param exchangeCardRequest
     * @param exchangeCardDO
     * @return
     */
    public Boolean validImportCodeIsFirst(ExchangeCardCmd exchangeCardRequest, ExchangeCardDO exchangeCardDO) {

        if (exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityNo == 0 && exchangeCardRequest.getSource() - ExchangeConstant.SourceImport == 0 &&
                StringUtils.isBlank(exchangeCardRequest.getCodeKey()) && exchangeCardRequest.getId()==null
        ) {
            //非实体卡、选中人工导入、必须要上传excel
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_MUST_IMPORT_CODE_BEFORE_CHOOSE_IMPORT);
        }
        /*if (exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes == 0 && exchangeCardRequest.getCardType() - ExchangeConstant.CardTypeImport == 0 &&
                StringUtils.isBlank(exchangeCardRequest.getCodeKey())
        ) {
            //非实体卡、选中人工导入、必须要上传excel
            throw new BaseException(ExchangeErrorConstant.ExchangeCodeNotNullByCardTypeImport.getCode(),ExchangeErrorConstant.ExchangeCodeNotNullByCardTypeImport.getMsg());
        }*/

        Boolean isFirst = true;
        if(exchangeCardDO.getIsEntity()- ExchangeConstant.IsEntityYes==0 ){
            ExchangeEntityRuleDO rule = exchangeEntityRuleServiceI.getByExchangeId(exchangeCardDO.getId());
            if( rule !=null && rule.getCardType() - ExchangeConstant.CardTypeImport ==0 && (
                    exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes==0 && exchangeCardRequest.getCardType()-ExchangeConstant.CardTypeImport==0
                            && exchangeCardRequest.getImportBeanList() !=null && exchangeCardRequest.getImportBeanList().size()>0
            )){
                //1、原来的实体卡是人工导入、、后面还是人工导入、且重新导入了数据 12、后面非人工导入、改为系统生成 121 、后面非实体卡
                isFirst = false;
            }
            if( rule !=null && rule.getCardType() - ExchangeConstant.CardTypeImport ==0 && (
                    (exchangeCardRequest.getIsEntity() -ExchangeConstant.IsEntityYes ==0 && exchangeCardRequest.getCardType()-ExchangeConstant.CardTypeSystem ==0)
            )){
                //1、原来的实体卡是人工导入、、后面非人工导入、改为系统生成 121
                isFirst = false;
            }
            if( rule !=null && rule.getCardType() - ExchangeConstant.CardTypeImport ==0 && exchangeCardRequest.getIsEntity() -ExchangeConstant.IsEntityNo==0 ){
                //1、原来的实体卡是人工导入、后面非实体卡
                isFirst = false;
            }
            if(exchangeCardRequest.getCardType() - ExchangeConstant.CardTypeImport == 0 && rule.getCardType() -ExchangeConstant.CardTypeSystem==0 &&
                    (exchangeCardRequest.getImportBeanList() ==null || exchangeCardRequest.getImportBeanList().size()==0)
            ){
                //实体卡、原来是系统生成、后面编辑修改为人工导入、必须要上传excel
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_MUST_IMPORT_CODE_BEFORE_WHEN_CARD_TYPE_IMPORT);
            }
            if (exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityNo == 0 && exchangeCardRequest.getSource() - ExchangeConstant.SourceImport == 0 &&
                    (exchangeCardRequest.getImportBeanList() ==null || exchangeCardRequest.getImportBeanList().size()==0)
            ) {
                //实体卡、原来是系统生成、后面编辑修改为非实体卡（人工导入）、必须要上传excel
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_MUST_IMPORT_CODE_BEFORE_CHOOSE_IMPORT);
            }

        }else{
            if(exchangeCardDO.getSource() - ExchangeConstant.SourceImport ==0 && exchangeCardRequest.getSource() - ExchangeConstant.SourceImport !=0){
                //原来来源是人工导入、后面改成系统生成
                isFirst=false;
            }
            if(exchangeCardDO.getSource() - ExchangeConstant.SourceImport ==0 && exchangeCardRequest.getSource() - ExchangeConstant.SourceImport ==0
                    && exchangeCardRequest.getImportBeanList() !=null && exchangeCardRequest.getImportBeanList().size()>0
            ){
                //原来来源是人工导入、后面还是人工导入、重新导入了列表
                isFirst=false;
            }
            if(exchangeCardDO.getSource() - ExchangeConstant.SourceImport ==0 && exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes ==0){
                //原来是人工导入改为实体卡
                isFirst=false;
            }
            if(exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityYes ==0 &&  exchangeCardRequest.getCardType() - ExchangeConstant.CardTypeImport == 0
                    && (exchangeCardRequest.getImportBeanList() ==null || exchangeCardRequest.getImportBeanList().size()==0)
            ){
                //非实体卡、后面编辑修改为实体卡、且人工导入、必须要上传excel
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_MUST_IMPORT_CODE_BEFORE_WHEN_CARD_TYPE_IMPORT);
            }
            if (exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityNo == 0 && exchangeCardDO.getSource() - ExchangeConstant.SourceImport != 0
                    && exchangeCardRequest.getSource() -ExchangeConstant.SourceImport==0 &&
                    (exchangeCardRequest.getImportBeanList() ==null || exchangeCardRequest.getImportBeanList().size()==0)
            ) {
                //非实体卡、原来是系统生成、后面编辑修改为人工导入、必须要上传excel
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_MUST_IMPORT_CODE_BEFORE_CHOOSE_IMPORT);
            }

        }
        return isFirst;
    }

    /**
     * 校验用户下单传的兑换码参数列表
     * @param exchangeCodeOrderDTOList
     */
    public static void validUserExchange(List<ExchangeCodeOrderDTO> exchangeCodeOrderDTOList){
        for(int x=0;x<exchangeCodeOrderDTOList.size();x++){
            ExchangeCodeOrderDTO codeOrderDTO = exchangeCodeOrderDTOList.get(x);
            if(codeOrderDTO.getMaterialId()==null){
                throw  FlexibleDubboApiException.buildException("111","第"+(x+1)+"个"+ ExchangeRxErrorConstant.MaterialIdNullError.getMsg());
            }
            if(codeOrderDTO.getModelId()==null){
                throw  FlexibleDubboApiException.buildException("111","第"+(x+1)+"个"+ExchangeRxErrorConstant.ModelIdNullError.getMsg());
            }
            if(codeOrderDTO.getPictureId()==null){
                throw  FlexibleDubboApiException.buildException("111","第"+(x+1)+"个"+ExchangeRxErrorConstant.PictureIdNullError.getMsg());
            }
            if(codeOrderDTO.getSecretCodeList()==null || codeOrderDTO.getSecretCodeList().size()==0){
                throw  FlexibleDubboApiException.buildException("111","第"+(x+1)+"个"+ExchangeRxErrorConstant.SecretCodeNullError.getMsg());
            }
        }
    }
}
