package com.bat.flexible.manager.sql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.data.SqlDataServiceI;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.dao.index.dataobject.*;
import com.bat.flexible.dao.picture.dataobject.*;
import com.bat.flexible.manager.burying.executor.BuryingPointCmdExe;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeConstant;
import com.bat.flexible.manager.common.constant.label.LabelConstant;
import com.bat.flexible.manager.distributor.exclude.executor.DistributorMaterialExcludeCmdExe;
import com.bat.flexible.manager.distributor.exclude.executor.DistributorModelExcludeCmdExe;
import com.bat.flexible.manager.distributor.exclude.executor.DistributorPictureExcludeCmdExe;
import com.bat.flexible.manager.exchange.executor.*;
import com.bat.flexible.manager.exchange.executor.code.*;
import com.bat.flexible.manager.exchange.executor.relavance.ExchangeMaterialRelevanceCmdExe;
import com.bat.flexible.manager.exchange.executor.relavance.ExchangeModelRelevanceCmdExe;
import com.bat.flexible.manager.exchange.executor.relavance.ExchangePictureRelevanceCmdExe;
import com.bat.flexible.manager.font.executor.FontCmdExe;
import com.bat.flexible.manager.index.executor.*;
import com.bat.flexible.manager.label.executor.LabelCmdExe;
import com.bat.flexible.manager.label.executor.LabelDistributorRelevanceCmdExe;
import com.bat.flexible.manager.label.executor.LabelQryExe;
import com.bat.flexible.manager.material.executor.MaterialCmdExe;
import com.bat.flexible.manager.model.executor.ModelCmdExe;
import com.bat.flexible.manager.model.executor.ModelMaterialRelevanceCmdExe;
import com.bat.flexible.manager.picture.executor.*;
import com.bat.flexible.manager.shop.executor.ShopCmdExe;
import com.bat.flexible.manager.third.executor.ThirdCourierContrastCmdExe;
import com.bat.flexible.manager.third.executor.ThirdSkuNoNameInfoCmdExe;
import com.bat.flexible.manager.third.executor.ThirdSkuRelevanceCmdExe;
import com.bat.dubboapi.distributor.distributor.api.DistributorCustomPriceServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.order.local.api.OrderDetailPictureLocalServiceRpc;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.dao.burying.dataobject.BuryingPointDO;
import com.bat.flexible.dao.distributor.dataobject.DistributorMaterialExcludeDO;
import com.bat.flexible.dao.distributor.dataobject.DistributorModelExcludeDO;
import com.bat.flexible.dao.distributor.dataobject.DistributorPictureExcludeDO;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.dao.font.dataobject.FontDO;
import com.bat.flexible.dao.index.dataobject.*;
import com.bat.flexible.dao.label.dataobject.LabelDO;
import com.bat.flexible.dao.label.dataobject.LabelDistributorRelevanceDO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import com.bat.flexible.dao.picture.dataobject.*;
import com.bat.flexible.dao.shop.dataobject.ShopDO;
import com.bat.flexible.dao.third.dataobject.ThirdCourierContrastDO;
import com.bat.flexible.dao.third.dataobject.ThirdSkuNoNameInfoDO;
import com.bat.flexible.dao.third.dataobject.ThirdSkuRelevanceDO;
import com.bat.flexible.manager.exchange.executor.*;
import com.bat.flexible.manager.exchange.executor.code.*;
import com.bat.flexible.manager.index.executor.*;
import com.bat.flexible.manager.picture.executor.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SqlDataServiceImpl implements SqlDataServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlDataServiceImpl.class);

    @Autowired
    private MaterialCmdExe materialCmdExe;

    @Autowired
    private ModelCmdExe modelCmdExe;

    @Autowired
    private ModelMaterialRelevanceCmdExe modelMaterialRelevanceCmdExe;

    @Autowired
    private PictureCmdExe pictureCmdExe;

    @Autowired
    private PictureCatergoryCmdExe pictureCatergoryCmdExe;

    @Autowired
    private LabelCmdExe labelCmdExe;

    @Autowired
    private LabelQryExe labelQryExe;

    @Autowired
    private PictureMaterialReferencesCmdExe pictureMaterialReferencesCmdExe;

    @Autowired
    private PictureModelReferencesCmdExe pictureModelReferencesCmdExe;

    @Autowired
    private PictureDistributorRelevanceCmdExe pictureDistributorRelevanceCmdExe;

    @Autowired
    private PictureLabelRelevanceCmdExe pictureLabelRelevanceCmdExe;

    @Autowired
    private PictureLabelRelevanceQryExe pictureLabelRelevanceQryExe;

    @Autowired
    private PictureTemplateEditCmdExe pictureTemplateEditCmdExe;

    @Autowired
    private LabelDistributorRelevanceCmdExe labelDistributorRelevanceCmdExe;

    @Autowired
    private ExchangeCardCmdExe exchangeCardCmdExe;

    @Autowired
    private ExchangeCodeCmdExe exchangeCodeCmdExe;

    @Autowired
    private ExchangeCodeQryExe exchangeCodeQryExe;

    @Autowired
    private ExchangeCodeInvalidLogCmdExe exchangeCodeInvalidLogCmdExe;

    @Autowired
    private ExchangeCodeOutboundRestoreLogCmdExe exchangeCodeOutboundRestoreLogCmdExe;

    @Autowired
    private ExchangeCodeRestoreLogCmdExe exchangeCodeRestoreLogCmdExe;

    @Autowired
    private ExchangeCodeSyncBackLogCmdExe exchangeCodeSyncBackLogCmdExe;

    @Autowired
    private ExchangeCodeUserCmdExe exchangeCodeUserCmdExe;

    @Autowired
    private ExchangeEntityRuleCmdExe exchangeEntityRuleCmdExe;

    @Autowired
    private ExchangeFactoryCmdExe exchangeFactoryCmdExe;

    @Autowired
    private ExchangeMaterialRelevanceCmdExe exchangeMaterialRelevanceCmdExe;

    @Autowired
    private ExchangeModelRelevanceCmdExe exchangeModelRelevanceCmdExe;

    @Autowired
    private ExchangePictureRelevanceCmdExe exchangePictureRelevanceCmdExe;

    @Autowired
    private ExchangeRefundOrderCmdExe exchangeRefundOrderCmdExe;

    @Autowired
    private ExchangeNoticeCmdExe exchangeNoticeCmdExe;

    @Autowired
    private BannerSeriesPictureRelevanceCmdExe bannerSeriesPictureRelevanceCmdExe;

    @Autowired
    private DistributorBannerCmdExe distributorBannerCmdExe;

    @Autowired
    private DistributorBannerRelevanceCmdExe distributorBannerRelevanceCmdExe;

    @Autowired
    private DistributorIndexRecommendCmdExe distributorIndexRecommendCmdExe;

    @Autowired
    private DistributorIndexRecommendRelevanceCmdExe distributorIndexRecommendRelevanceCmdExe;

    @Autowired
    private DistributorSeriesThemeCmdExe distributorSeriesThemeCmdExe;

    @Autowired
    private DistributorSeriesThemeRelevanceCmdExe distributorSeriesThemeRelevanceCmdExe;

    @Autowired
    private IndexRecommendPictureRelevanceCmdExe indexRecommendPictureRelevanceCmdExe;

    @Autowired
    private SeriesPictureRelevanceCmdExe seriesPictureRelevanceCmdExe;

    @Autowired
    private PictureThemeCmdExe pictureThemeCmdExe;

    @Autowired
    private PictureCategoryThemeRelevanceCmdExe pictureCategoryThemeRelevanceCmdExe;

    @Autowired
    private BuryingPointCmdExe buryingPointCmdExe;

    @Autowired
    private DistributorMaterialExcludeCmdExe distributorMaterialExcludeCmdExe;

    @Autowired
    private DistributorModelExcludeCmdExe distributorModelExcludeCmdExe;

    @Autowired
    private DistributorPictureExcludeCmdExe distributorPictureExcludeCmdExe;

    @Autowired
    private FontCmdExe fontCmdExe;

    @Autowired
    private PictureModelMaterialDiyCmdExe pictureModelMaterialDiyCmdExe;

    @Autowired
    private PictureProductCategoryReferencesCmdExe pictureProductCategoryReferencesCmdExe;

    @Autowired
    private ShopCmdExe shopCmdExe;

    @Autowired
    private ThirdCourierContrastCmdExe thirdCourierContrastCmdExe;

    @Autowired
    private ThirdSkuNoNameInfoCmdExe thirdSkuNoNameInfoCmdExe;

    @Autowired
    private ThirdSkuRelevanceCmdExe thirdSkuRelevanceCmdExe;

    @DubboReference(check = false,timeout = 9000,retries = 0)
    private DistributorCustomPriceServiceRpc distributorCustomPriceServiceRpc;

    @DubboReference(check = false,timeout = 3000000,retries = 0)
    private OrderDetailPictureLocalServiceRpc orderDetailPictureLocalServiceRpc;

    private static final String hoseName="http://47.56.89.60:9999/open/sql/export/";

    @Transactional
    @Override
    public Response importSql(Integer index) {
        LOGGER.info("当前租户:{}", TenantContext.getTenantNo());
        JSONArray jsonArray = null;
        if(index ==1){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的材质列表："+ JSON.toJSONString(jsonArray));
            for (int x=0;x<jsonArray.size();x++){
                JSONObject  jsonObject= jsonArray.getJSONObject(x);
                MaterialDO materialDO = JSONObject.parseObject(jsonObject.toJSONString(), MaterialDO.class);
                materialDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                materialDO.setCategoryId(1);
                materialDO.setAllowUploadFlag(jsonObject.getShort("banSelf"));
                materialDO.setMandatoryCoveredBleedFlag(jsonObject.getShort("isAllPlace"));
                if(materialDO.getMandatoryCoveredBleedFlag() ==null){
                    materialDO.setMandatoryCoveredBleedFlag((short)0);
                }
                materialDO.setOpenFlag(jsonObject.getShort("status"));
                materialDO.setParentId(jsonObject.getInteger("pid"));
                materialDO.setAtLastTrademark(jsonObject.getShort("trademark"));
                materialDO.setCreateUserId(-1);
                materialCmdExe.create(materialDO);
            }
        }
        if(index ==2){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的型号列表："+ JSON.toJSONString(jsonArray));
            for (int x=0;x<jsonArray.size();x++){
                JSONObject  jsonObject= jsonArray.getJSONObject(x);
                ModelDO modelDO = JSONObject.parseObject(jsonObject.toJSONString(), ModelDO.class);
                modelDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                modelDO.setCategoryId(1);

                modelDO.setOpenFlag(jsonObject.getShort("status"));
                modelDO.setAtLastTrademark(jsonObject.getShort("trademark"));
                modelDO.setCreateUserId(-1);
                modelCmdExe.create(modelDO);
            }
        }
        if(index ==3){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的型号材质关联列表："+ JSON.toJSONString(jsonArray));
            for (int x=0;x<jsonArray.size();x++){
                JSONObject  jsonObject= jsonArray.getJSONObject(x);
                ModelMaterialRelevanceDO modelMaterialRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), ModelMaterialRelevanceDO.class);
                modelMaterialRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);

                modelMaterialRelevanceDO.setOpenFlag(jsonObject.getShort("status"));
                modelMaterialRelevanceDO.setUnderFrame(jsonObject.getBigDecimal("bottomFrame"));
                modelMaterialRelevanceDO.setCreateUserId(-1);
                modelMaterialRelevanceDO.setUnderStockFlag(jsonObject.getShort("isStockOut"));
                if(modelMaterialRelevanceDO.getUnderStockFlag() ==null){
                    modelMaterialRelevanceDO.setUnderStockFlag((short)0);
                }
                modelMaterialRelevanceCmdExe.create(modelMaterialRelevanceDO);
            }
        }
        if(index ==4){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的图片列表："+ JSON.toJSONString(jsonArray));
            for (int x=0;x<jsonArray.size();x++){
                JSONObject  jsonObject= jsonArray.getJSONObject(x);
                PictureDO pictureDO = JSONObject.parseObject(jsonObject.toJSONString(), PictureDO.class);
                pictureDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);

                pictureDO.setOpenFlag(jsonObject.getShort("status"));
                pictureDO.setCopyrightCost(BigDecimal.ZERO);
                pictureDO.setCreateUserId(-1);
                pictureDO.setBackgroundPreviewUrl(jsonObject.getString("backColorImg"));
                pictureDO.setNoCameraVacancyPreview(jsonObject.getString("noCameraImg"));
                pictureCmdExe.create(pictureDO);
            }
        }

       if(index ==5){
           jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
           LOGGER.info("查询回来的图片分类列表："+ JSON.toJSONString(jsonArray));
           for (int x=0;x<jsonArray.size();x++){
               JSONObject  jsonObject= jsonArray.getJSONObject(x);
               PictureCategoryDO pictureCategoryDO = JSONObject.parseObject(jsonObject.toJSONString(), PictureCategoryDO.class);
               pictureCategoryDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);

               pictureCategoryDO.setOpenFlag(jsonObject.getShort("status"));

               pictureCategoryDO.setCreateUserId(-1);
               Short whetherParent = jsonObject.getShort("whetherParent");
               pictureCategoryDO.setAtLastTrademark(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES);
               if(whetherParent==1){
                   //之前1才是父
                   pictureCategoryDO.setAtLastTrademark(FlexibleCommonConstant.AT_LAST_TRADEMARK_NO);
               }
               if(pictureCategoryDO.getAtLastTrademark() ==null){
                   pictureCategoryDO.setAtLastTrademark(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES);
               }
               pictureCatergoryCmdExe.create(pictureCategoryDO);
           }
       }

       if(index ==6){
           jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
           LOGGER.info("查询回来的标签列表："+ JSON.toJSONString(jsonArray));
           for (int x=0;x<jsonArray.size();x++){
               JSONObject  jsonObject= jsonArray.getJSONObject(x);
               LabelDO labelDO = JSONObject.parseObject(jsonObject.toJSONString(), LabelDO.class);
               labelDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
               labelDO.setBarCodePositionX(jsonObject.getBigDecimal("barCodePositionLeft"));
               labelDO.setBarCodePositionY(jsonObject.getBigDecimal("barCodePositionTop"));
               labelDO.setBarCodePositionWidth(jsonObject.getBigDecimal("barCodeWidth"));
               labelDO.setBarCodePositionHeight(jsonObject.getBigDecimal("barCodeHeight"));
               labelDO.setOpenFlag(jsonObject.getShort("status"));
               //产品名称
               labelDO.setProductNamePositionX(jsonObject.getBigDecimal("namePositionLeft"));
               labelDO.setProductNamePositionY(jsonObject.getBigDecimal("namePositionTop"));
               labelDO.setProductNamePositionWidth(jsonObject.getBigDecimal("nameWidth"));
               labelDO.setProductNamePositionHeight(jsonObject.getBigDecimal("nameHeight"));
               labelDO.setProductNamePositionHeight(jsonObject.getBigDecimal("nameHeight"));
               labelDO.setProductNamePositionFont(jsonObject.getInteger("nameFont"));
               labelDO.setProductNamePositionFontSize(jsonObject.getInteger("nameFontSize"));
               //型号内容
               labelDO.setModelPositionX(jsonObject.getBigDecimal("modelPositionLeft"));
               labelDO.setModelPositionY(jsonObject.getBigDecimal("modelPositionTop"));
               labelDO.setModelPositionWidth(jsonObject.getBigDecimal("modelPositionWidth"));
               labelDO.setModelPositionHeight(jsonObject.getBigDecimal("modelPositionHeight"));
               labelDO.setModelPositionFont(jsonObject.getInteger("modelFont"));
               labelDO.setModelPositionFontSize(jsonObject.getInteger("modelFontSize"));
               labelDO.setCategoryId(1);
               //英文名称
               labelDO.setEnNamePositionX(jsonObject.getBigDecimal("enNamePositionLeft"));
               labelDO.setEnNamePositionY(jsonObject.getBigDecimal("enNamePositionTop"));
               labelDO.setEnNamePositionWidth(jsonObject.getBigDecimal("enNameWidth"));
               labelDO.setEnNamePositionHeight(jsonObject.getBigDecimal("enNameHeight"));
               labelDO.setEnNamePositionFont(jsonObject.getInteger("enNameFont"));
               labelDO.setEnNamePositionFontSize(jsonObject.getInteger("enNameFontSize"));
               //材质名称
               labelDO.setStuffNamePositionX(jsonObject.getBigDecimal("stuffNamePositionLeft"));
               labelDO.setStuffNamePositionY(jsonObject.getBigDecimal("stuffNamePositionTop"));
               labelDO.setStuffNamePositionWidth(jsonObject.getBigDecimal("stuffNameWidth"));
               labelDO.setStuffNamePositionHeight(jsonObject.getBigDecimal("stuffNameHeight"));
               labelDO.setStuffNamePositionFont(jsonObject.getInteger("stuffNameFont"));
               labelDO.setStuffNamePositionFontSize(jsonObject.getInteger("stuffNameFontSize"));
               //材质名称英文
               labelDO.setStuffEnNamePositionX(jsonObject.getBigDecimal("stuffEnNamePositionLeft"));
               labelDO.setStuffEnNamePositionY(jsonObject.getBigDecimal("stuffEnNamePositionTop"));
               labelDO.setStuffEnNamePositionWidth(jsonObject.getBigDecimal("stuffEnNameWidth"));
               labelDO.setStuffEnNamePositionHeight(jsonObject.getBigDecimal("stuffEnNameHeight"));
               labelDO.setStuffEnNamePositionFont(jsonObject.getInteger("stuffEnNameFont"));
               labelDO.setStuffEnNamePositionFontSize(jsonObject.getInteger("stuffEnNameFontSize"));

               labelDO.setCreateUserId(-1);
               if(LabelConstant.TYPE_FLEXIBEL.equals(labelDO.getType())){
                   //先设置为不关联用户上传图片(下一步会根据图片为0来反设置)
                   labelDO.setRelevanceUserUpload(LabelConstant.RELEVANCE_USER_UPLOAD_NO);
               }
               labelCmdExe.create(labelDO);
           }
       }
       if(index ==7){
           jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
           LOGGER.info("查询回来的图片材质关联列表："+ JSON.toJSONString(jsonArray));
           //图片和材质的关联关系
           for (int x=0;x<jsonArray.size();x++){
               JSONObject  jsonObject= jsonArray.getJSONObject(x);
               PictureMaterialRelevanceDO pictureMaterialRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), PictureMaterialRelevanceDO.class);
               pictureMaterialRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
               pictureMaterialRelevanceDO.setCreateUserId(-1);
                pictureMaterialReferencesCmdExe.create(pictureMaterialRelevanceDO);
           }
       }
        //图片和型号的关联关系
        if(index ==8){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的图片型号关联列表："+ JSON.toJSONString(jsonArray));
            for (int x=0;x<jsonArray.size();x++){
                JSONObject  jsonObject= jsonArray.getJSONObject(x);
                PictureModelRelevanceDO pictureModelRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), PictureModelRelevanceDO.class);
                pictureModelRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                if(jsonObject.getShort("status") ==0){
                    pictureModelRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
                }
                pictureModelRelevanceDO.setCreateUserId(-1);
                 pictureModelReferencesCmdExe.create(pictureModelRelevanceDO);
            }
        }
        //图片和分销商的关联关系
        if(index ==9){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的图片分销商关联列表："+ JSON.toJSONString(jsonArray));
            for (int x=0;x<jsonArray.size();x++){
                JSONObject  jsonObject= jsonArray.getJSONObject(x);
                PictureDistributorRelevanceDO pictureDistributorRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), PictureDistributorRelevanceDO.class);
                pictureDistributorRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                if(jsonObject.getShort("status") ==0){
                    pictureDistributorRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
                }
                Long createTime = jsonObject.getLong("createTime");
                pictureDistributorRelevanceDO.setCreateUserId(-1);
                pictureDistributorRelevanceDO.setCreateTime(new Date(createTime));
                pictureDistributorRelevanceDO.setDistributorId(jsonObject.getInteger("resellerId"));
                pictureDistributorRelevanceCmdExe.create(pictureDistributorRelevanceDO);
            }
        }
        //图片模板可编辑区域
       if(index ==10){
           jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
           LOGGER.info("查询回来的图片模板可编辑区域列表："+ JSON.toJSONString(jsonArray));
           for (int x=0;x<jsonArray.size();x++){
               JSONObject  jsonObject= jsonArray.getJSONObject(x);
               PictureTemplateEditDO pictureTemplateEditDO = JSONObject.parseObject(jsonObject.toJSONString(), PictureTemplateEditDO.class);
               Long createTime = jsonObject.getLong("createTime");
               pictureTemplateEditDO.setCreateTime(new Date(createTime));
               Long updateTime = jsonObject.getLong("updateTime");
               pictureTemplateEditDO.setUpdateTime(new Date(updateTime));
               pictureTemplateEditCmdExe.create(pictureTemplateEditDO);
           }
       }

        //分销商和标签的关联关系
        if(index ==11){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的分销商标签关联列表："+ JSON.toJSONString(jsonArray));
            for (int x=0;x<jsonArray.size();x++){
                JSONObject  jsonObject= jsonArray.getJSONObject(x);
                LabelDistributorRelevanceDO labelDistributorRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), LabelDistributorRelevanceDO.class);
                labelDistributorRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                labelDistributorRelevanceDO.setCreateUserId(jsonObject.getInteger("creatorId"));
                Long time = jsonObject.getLong("createTime");
                labelDistributorRelevanceDO.setCreateTime(new Date(time));
                //labelDistributorRelevanceCmdExe.create(labelDistributorRelevanceDO);
            }
        }
        //图片和标签的关联关系
      if(index ==12){
          jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
          LOGGER.info("查询回来的图片标签关联列表："+ JSON.toJSONString(jsonArray));
          for (int x=0;x<jsonArray.size();x++){
              JSONObject  jsonObject= jsonArray.getJSONObject(x);
              PictureLabelRelevanceDO pictureLabelRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), PictureLabelRelevanceDO.class);
              pictureLabelRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
              if(jsonObject.getShort("status")==0){
                  pictureLabelRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
              }
              pictureLabelRelevanceDO.setCreateUserId(jsonObject.getInteger("creatorId"));
              pictureLabelRelevanceDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
              pictureLabelRelevanceCmdExe.create(pictureLabelRelevanceDO);
          }
          //处理标签是否关联0
          List<PictureLabelRelevanceDO> pictureLabelRelevanceDOList = pictureLabelRelevanceQryExe.listByLabelIdAndPictureId(null,0);
          if(pictureLabelRelevanceDOList ==null || pictureLabelRelevanceDOList.size()==0){
              return Response.buildSuccess() ;
          }
          pictureLabelRelevanceDOList.stream().forEach(pictureLabelRelevanceDO -> {
              LabelDO labelDO = labelQryExe.getById(pictureLabelRelevanceDO.getLabelId());
              labelDO.setRelevanceUserUpload(LabelConstant.RELEVANCE_USER_UPLOAD_YES);
              labelCmdExe.update(labelDO);
          });
      }

       if(index ==13){
           jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
           LOGGER.info("查询回来的兑换卡列表："+ JSON.toJSONString(jsonArray));
           for (int x=0;x<jsonArray.size();x++){
               JSONObject  jsonObject= jsonArray.getJSONObject(x);
               ExchangeCardDO exchangeCardDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeCardDO.class);

               exchangeCardDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
               exchangeCardDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
               exchangeCardDO.setDistributorScope(ExchangeConstant.EXCHANGE_DISTRIBUTOR_SCOPE_ALL);
               if(exchangeCardDO.getMailSetting()==null){
                   exchangeCardDO.setMailSetting((short)1);
               }
               exchangeCardCmdExe.create(exchangeCardDO);
           }
       }
       if(index==14){
           //jsonArray = HttpUtil.getJson("http://localhost:9997/open/sql/export/14",  JSONArray.class);
           String result = HttpUtil.doGet(hoseName+index);
           jsonArray = JSON.parseArray(result);
           LOGGER.info("查询回来的兑换码列表："+ JSON.toJSONString(jsonArray));
           Map<Integer, DistributorRpcDTO> allDistributor = getAllDistributor();
           List<ExchangeCodeDO> exchangeCodeDOList = new ArrayList<>();
           for (int x=0;x<jsonArray.size();x++){
               JSONObject  jsonObject= jsonArray.getJSONObject(x);
               ExchangeCodeDO exchangeCodeDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeCodeDO.class);
               exchangeCodeDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
               exchangeCodeDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
               if(exchangeCodeDO.getDistributorId() !=null){
                   DistributorRpcDTO distributorRpcDTO = allDistributor.get(exchangeCodeDO.getDistributorId());
                   if(distributorRpcDTO !=null){
                       exchangeCodeDO.setDistributorName(distributorRpcDTO.getName());
                       exchangeCodeDO.setDistributorCompanyName(distributorRpcDTO.getCompanyName());
                   }
               }
               //exchangeCodeCmdExe.create(exchangeCodeDO);
               exchangeCodeDOList.add(exchangeCodeDO);
               if(exchangeCodeDOList.size()==200){
                   exchangeCodeCmdExe.batchCreateContainKey(exchangeCodeDOList);
                   exchangeCodeDOList.clear();
               }
           }
            if(exchangeCodeDOList !=null && exchangeCodeDOList.size()>0){
                exchangeCodeCmdExe.batchCreateContainKey(exchangeCodeDOList);
            }
       }
        if(index ==15){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的兑换码作废列表："+ JSON.toJSONString(jsonArray));
            for (int x=0;x<jsonArray.size();x++){
                JSONObject  jsonObject= jsonArray.getJSONObject(x);
                ExchangeCodeInvalidLogDO exchangeCodeInvalidLogDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeCodeInvalidLogDO.class);
                exchangeCodeInvalidLogDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                exchangeCodeInvalidLogCmdExe.create(exchangeCodeInvalidLogDO);
            }
        }
        if(index ==16){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的兑换码出库重置列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    ExchangeCodeOutboundRestoreLogDO exchangeCodeOutboundRestoreLogDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeCodeOutboundRestoreLogDO.class);
                    exchangeCodeOutboundRestoreLogDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    exchangeCodeOutboundRestoreLogCmdExe.create(exchangeCodeOutboundRestoreLogDO);
                }
            }
        }
      if(index==17){
          jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
          LOGGER.info("查询回来的兑换码核销重置列表："+ JSON.toJSONString(jsonArray));
          if(jsonArray !=null && jsonArray.size()>0){
              for (int x=0;x<jsonArray.size();x++){
                  JSONObject  jsonObject= jsonArray.getJSONObject(x);
                  ExchangeCodeRestoreLogDO ExchangeCodeRestoreLogDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeCodeRestoreLogDO.class);
                  ExchangeCodeRestoreLogDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                  ExchangeCodeRestoreLogDO.setUseTime(new Date(jsonObject.getLong("useTime")));
                  exchangeCodeRestoreLogCmdExe.create(ExchangeCodeRestoreLogDO);
              }
          }
      }
        if(index==18){
           // jsonArray = HttpUtil.getJson("http://localhost:9997/open/sql/export/18",  JSONArray.class);
            String result = HttpUtil.doGet(hoseName+index);
            jsonArray = JSON.parseArray(result);
            LOGGER.info("查询回来的同步盒码回B2B记录批次表列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                List<ExchangeCodeSyncBackLogDO> codeSyncBackLogDOList = new ArrayList<>();
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    ExchangeCodeSyncBackLogDO exchangeCodeSyncBackLogDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeCodeSyncBackLogDO.class);
                    exchangeCodeSyncBackLogDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    codeSyncBackLogDOList.add(exchangeCodeSyncBackLogDO);
                }
                exchangeCodeSyncBackLogCmdExe.createList(codeSyncBackLogDOList);
            }
        }
        if(index ==19){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的c端用户与兑换码关联关系列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    ExchangeCodeUserDO exchangeCodeUserDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeCodeUserDO.class);
                    exchangeCodeUserDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    exchangeCodeUserCmdExe.create(exchangeCodeUserDO);
                }
            }
        }
       if(index ==20){
           jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
           LOGGER.info("查询回来的兑换卡实体卡生成规则列表："+ JSON.toJSONString(jsonArray));
           if(jsonArray !=null && jsonArray.size()>0){
               for (int x=0;x<jsonArray.size();x++){
                   JSONObject  jsonObject= jsonArray.getJSONObject(x);
                   ExchangeEntityRuleDO exchangeEntityRuleDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeEntityRuleDO.class);
                   exchangeEntityRuleDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                   exchangeEntityRuleDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                   exchangeEntityRuleCmdExe.create(exchangeEntityRuleDO);
               }
           }
       }
        if(index==21){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的兑换卡工厂同步表列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    ExchangeFactoryDO exchangeFactoryDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeFactoryDO.class);
                    exchangeFactoryDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    exchangeFactoryDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    exchangeFactoryCmdExe.create(exchangeFactoryDO);
                }
            }
        }
        if(index==22){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的兑换卡材质关联表列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    ExchangeMaterialRelevanceDO exchangeMaterialRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeMaterialRelevanceDO.class);
                    exchangeMaterialRelevanceDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    exchangeMaterialRelevanceCmdExe.create(exchangeMaterialRelevanceDO);
                }
            }
        }
       if(index==23){
           jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
           LOGGER.info("查询回来的兑换卡型号关联表列表："+ JSON.toJSONString(jsonArray));
           if(jsonArray !=null && jsonArray.size()>0){
               for (int x=0;x<jsonArray.size();x++){
                   JSONObject  jsonObject= jsonArray.getJSONObject(x);
                   ExchangeModelRelevanceDO exchangeModelRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeModelRelevanceDO.class);
                   exchangeModelRelevanceDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                   exchangeModelRelevanceCmdExe.create(exchangeModelRelevanceDO);
               }
           }
       }
       if(index ==24){
           jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
           LOGGER.info("查询回来的兑换卡商城公告列表："+ JSON.toJSONString(jsonArray));
           if(jsonArray !=null && jsonArray.size()>0){
               for (int x=0;x<jsonArray.size();x++){
                   JSONObject  jsonObject= jsonArray.getJSONObject(x);
                   ExchangeNoticeDO exchangeNoticeDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeNoticeDO.class);
                   exchangeNoticeDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                   exchangeNoticeCmdExe.create(exchangeNoticeDO);
               }
           }
       }
       if(index ==25){
           jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
           LOGGER.info("查询回来的兑换卡图片关联表列表："+ JSON.toJSONString(jsonArray));
           if(jsonArray !=null && jsonArray.size()>0){
               for (int x=0;x<jsonArray.size();x++){
                   JSONObject  jsonObject= jsonArray.getJSONObject(x);
                   ExchangePictureRelevanceDO exchangePictureRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangePictureRelevanceDO.class);
                   exchangePictureRelevanceDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                   exchangePictureRelevanceCmdExe.create(exchangePictureRelevanceDO);
               }
           }
       }
        if(index ==26){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的兑换卡退款订单关联表列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    ExchangeRefundOrderDO exchangeRefundOrderDO = JSONObject.parseObject(jsonObject.toJSONString(), ExchangeRefundOrderDO.class);
                    exchangeRefundOrderDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    exchangeRefundOrderCmdExe.create(exchangeRefundOrderDO);
                }
            }
        }
        //banner_series_picture_relevance
        if(index ==27){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的分销商首页banner和主题系列图片关联列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    BannerSeriesPictureRelevanceDO bannerSeriesPictureRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), BannerSeriesPictureRelevanceDO.class);
                    bannerSeriesPictureRelevanceDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    bannerSeriesPictureRelevanceCmdExe.create(bannerSeriesPictureRelevanceDO);
                }
            }
        }
        //distributor_banner
        if(index ==28){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的分销商banner列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    DistributorBannerDO distributorBannerDO = JSONObject.parseObject(jsonObject.toJSONString(), DistributorBannerDO.class);
                    distributorBannerDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    distributorBannerDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    distributorBannerCmdExe.create(distributorBannerDO);
                }
            }
        }
        //distributor_banner_relevance
        if(index ==29){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的banner分销商关联表列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                Map<Integer, DistributorRpcDTO> distributorRpcDTOMap = getAllDistributor();
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    DistributorBannerRelevanceDO distributorBannerRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), DistributorBannerRelevanceDO.class);
                    distributorBannerRelevanceDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    DistributorRpcDTO distributorRpcDTO = distributorRpcDTOMap.get(distributorBannerRelevanceDO.getDistributorId());
                    if(distributorRpcDTO!=null) {
                        distributorBannerRelevanceDO.setCompanyName(distributorRpcDTO.getCompanyName());
                        distributorBannerRelevanceDO.setDistributorName(distributorRpcDTO.getName());
                    }
                    distributorBannerRelevanceCmdExe.create(distributorBannerRelevanceDO);
                }
            }
        }
        //distributor_index_recommend
        if(index ==30){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的DistributorIndexRecommendDO列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    DistributorIndexRecommendDO distributorIndexRecommendDO = JSONObject.parseObject(jsonObject.toJSONString(), DistributorIndexRecommendDO.class);
                    distributorIndexRecommendDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    distributorIndexRecommendDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    distributorIndexRecommendCmdExe.create(distributorIndexRecommendDO);
                }
            }
        }
        //distributor_index_recommend_relevance
        if(index ==31){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的DistributorIndexRecommendRelevanceDO列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                Map<Integer, DistributorRpcDTO> distributorRpcDTOMap = getAllDistributor();
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    DistributorIndexRecommendRelevanceDO recommendRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), DistributorIndexRecommendRelevanceDO.class);
                    recommendRelevanceDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    DistributorRpcDTO distributorRpcDTO = distributorRpcDTOMap.get(recommendRelevanceDO.getDistributorId());
                    recommendRelevanceDO.setCompanyName(distributorRpcDTO.getCompanyName());
                    recommendRelevanceDO.setDistributorName(distributorRpcDTO.getName());
                    distributorIndexRecommendRelevanceCmdExe.create(recommendRelevanceDO);
                }
            }
        }
        //distributor_series_theme
        if(index ==32){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的distributor_series_theme列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    DistributorSeriesThemeDO distributorSeriesThemeDO = JSONObject.parseObject(jsonObject.toJSONString(), DistributorSeriesThemeDO.class);
                    distributorSeriesThemeDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    distributorSeriesThemeDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    distributorSeriesThemeCmdExe.create(distributorSeriesThemeDO);
                }
            }
        }
        //distributor_series_theme_relevance
        if(index ==33){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的distributor_series_theme列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                Map<Integer, DistributorRpcDTO> allDistributor = getAllDistributor();
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    DistributorSeriesThemeRelevanceDO distributorSeriesThemeRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), DistributorSeriesThemeRelevanceDO.class);
                    distributorSeriesThemeRelevanceDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    DistributorRpcDTO distributorRpcDTO = allDistributor.get(distributorSeriesThemeRelevanceDO.getDistributorId());
                    if(distributorRpcDTO!=null) {
                        distributorSeriesThemeRelevanceDO.setCompanyName(distributorRpcDTO.getCompanyName());
                        distributorSeriesThemeRelevanceDO.setDistributorName(distributorRpcDTO.getName());
                    }
                    distributorSeriesThemeRelevanceCmdExe.create(distributorSeriesThemeRelevanceDO);
                }
            }
        }
        //series_picture_relevance
        if(index ==34){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的index_recommend_picture_relevance列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    IndexRecommendPictureRelevanceDO indexRecommendPictureRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), IndexRecommendPictureRelevanceDO.class);
                    indexRecommendPictureRelevanceDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    indexRecommendPictureRelevanceDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    indexRecommendPictureRelevanceCmdExe.create(indexRecommendPictureRelevanceDO);
                }
            }
        }
        if(index ==35){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的distributor_series_theme列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    SeriesPictureRelevanceDO seriesPictureRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), SeriesPictureRelevanceDO.class);
                    seriesPictureRelevanceDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    seriesPictureRelevanceDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    seriesPictureRelevanceCmdExe.create(seriesPictureRelevanceDO);
                }
            }
        }
        if(index ==36){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的picture_theme列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                Map<Integer, DistributorRpcDTO> distributorRpcDTOMap = getAllDistributor();
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    PictureThemeDO pictureThemeDO = JSONObject.parseObject(jsonObject.toJSONString(), PictureThemeDO.class);
                    pictureThemeDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    pictureThemeDO.setOpenFlag(jsonObject.getShort("status"));
                    DistributorRpcDTO distributorRpcDTO = distributorRpcDTOMap.get(pictureThemeDO.getDistributorId());
                    pictureThemeDO.setDistributorName(distributorRpcDTO.getName());
                    pictureThemeCmdExe.create(pictureThemeDO);
                }
            }
        }
        if(index ==37){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的picture_category_theme_relevance列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    PictureCategoryThemeRelevanceDO pictureCategoryThemeRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), PictureCategoryThemeRelevanceDO.class);
                    pictureCategoryThemeRelevanceDO.setSequence(jsonObject.getInteger("sort"));
                    pictureCategoryThemeRelevanceCmdExe.create(pictureCategoryThemeRelevanceDO);
                }
            }
        }
        if(index ==38){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的burying_point列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    BuryingPointDO buryingPointDO = JSONObject.parseObject(jsonObject.toJSONString(), BuryingPointDO.class);
                    buryingPointDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    buryingPointCmdExe.create(buryingPointDO);
                }
            }
        }
        if(index ==39){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的distributor_material_exclude列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    DistributorMaterialExcludeDO materialExcludeDO = JSONObject.parseObject(jsonObject.toJSONString(), DistributorMaterialExcludeDO.class);
                    materialExcludeDO.setCreateUserId(-1);
                    materialExcludeDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                    distributorMaterialExcludeCmdExe.create(materialExcludeDO);
                }
            }
        }
        if(index ==40){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的distributor_model_exclude列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    DistributorModelExcludeDO distributorModelExcludeDO = JSONObject.parseObject(jsonObject.toJSONString(), DistributorModelExcludeDO.class);
                    distributorModelExcludeDO.setCreateUserId(-1);
                    distributorModelExcludeCmdExe.create(distributorModelExcludeDO);
                }
            }
        }
        if(index ==41){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的distributor_picture_exclude列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    DistributorPictureExcludeDO distributorPictureExcludeDO = JSONObject.parseObject(jsonObject.toJSONString(), DistributorPictureExcludeDO.class);
                    distributorPictureExcludeDO.setCreateUserId(-1);
                    distributorPictureExcludeDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                    distributorPictureExcludeCmdExe.create(distributorPictureExcludeDO);
                }
            }
        }
        if(index ==42){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的font列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    FontDO fontDO = JSONObject.parseObject(jsonObject.toJSONString(), FontDO.class);
                    fontDO.setOpenFlag(jsonObject.getShort("status"));
                    fontDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                    fontDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    fontDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    fontCmdExe.create(fontDO);
                }
            }
        }
        if(index ==43){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的picture_model_material_diy列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    PictureModelMaterialDiyDO pictureModelMaterialDiyDO = JSONObject.parseObject(jsonObject.toJSONString(), PictureModelMaterialDiyDO.class);
                    pictureModelMaterialDiyDO.setPreviewImage(jsonObject.getString("image"));
                    pictureModelMaterialDiyDO.setCreateTime(new Date());
                    pictureModelMaterialDiyCmdExe.create(pictureModelMaterialDiyDO);
                }
            }
        }
        if(index ==44){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的picture_model_material_diy列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    PictureProductCategoryRelevanceDO pictureProductCategoryRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), PictureProductCategoryRelevanceDO.class);
                    pictureProductCategoryReferencesCmdExe.create(pictureProductCategoryRelevanceDO);
                }
            }
        }
        if(index ==45){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的picture_model_material_diy列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    ShopDO shopDO = JSONObject.parseObject(jsonObject.toJSONString(), ShopDO.class);
                    shopDO.setDistributorId(jsonObject.getInteger("b2bId"));
                    shopDO.setCompanyName(jsonObject.getString("distributorCompanyName"));
                    shopDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                    shopDO.setOpenFlag(jsonObject.getShort("shopStatus"));
                    shopDO.setQrUrl(jsonObject.getString("qrurl"));
                    shopDO.setSource((short)1);
                    shopDO.setExtendParam(jsonObject.getString("thirdUrl"));
                    shopDO.setCreateUserId(jsonObject.getInteger("createUser"));
                    shopDO.setUpdateUserId(jsonObject.getInteger("updateUser"));
                    shopDO.setCreateTime(new Date(jsonObject.getLong("createTime")));
                    shopDO.setUpdateTime(new Date(jsonObject.getLong("updateTime")));
                    shopCmdExe.create(shopDO);
                }
            }
        }
        if(index ==46){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的picture_model_material_diy列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    ThirdCourierContrastDO thirdCourierContrastDO = JSONObject.parseObject(jsonObject.toJSONString(), ThirdCourierContrastDO.class);
                    thirdCourierContrastCmdExe.create(thirdCourierContrastDO);
                }
            }
        }
        if(index ==47){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的picture_model_material_diy列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    ThirdSkuNoNameInfoDO thirdSkuNoNameInfoDO = JSONObject.parseObject(jsonObject.toJSONString(), ThirdSkuNoNameInfoDO.class);
                    thirdSkuNoNameInfoCmdExe.create(thirdSkuNoNameInfoDO);
                }
            }
        }
        if(index ==48){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的picture_model_material_diy列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                for (int x=0;x<jsonArray.size();x++){
                    JSONObject  jsonObject= jsonArray.getJSONObject(x);
                    ThirdSkuRelevanceDO thirdSkuRelevanceDO = JSONObject.parseObject(jsonObject.toJSONString(), ThirdSkuRelevanceDO.class);
                    thirdSkuRelevanceCmdExe.create(thirdSkuRelevanceDO);
                }
            }
        }
        if(index ==91){
            List<ExchangeCodeDO> exchangeCodeDOList = exchangeCodeQryExe.listByBoxCodeAndPlainCodeList(null,null);
            List<ExchangeCodeDO> copyCodeDOList = exchangeCodeQryExe.listAll2();
            Map<String, ExchangeCodeDO> exchangeCodeDOMap = copyCodeDOList.stream().collect(Collectors.toMap(ExchangeCodeDO::getPlainCode, exchangeCodeDO -> exchangeCodeDO));
            exchangeCodeDOList.stream().forEach(exchangeCodeDO -> {
                ExchangeCodeDO codeDO = exchangeCodeDOMap.get(exchangeCodeDO.getPlainCode());
                exchangeCodeCmdExe.updateId(exchangeCodeDO.getId(),codeDO.getId());
            });

        }

        if(index ==2001){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的distributor_custom_price列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
              distributorCustomPriceServiceRpc.importData(jsonArray.toJSONString());
            }
        }

        if(index ==3001){
            jsonArray = HttpUtil.getJson(hoseName+index,  JSONArray.class);
            LOGGER.info("查询回来的order_detail_picture_local列表："+ JSON.toJSONString(jsonArray));
            if(jsonArray !=null && jsonArray.size()>0){
                orderDetailPictureLocalServiceRpc.importData(jsonArray.toJSONString());
            }
        }
        return Response.of(jsonArray);
    }

    public static Map<Integer, DistributorRpcDTO> getAllDistributor(){
        Map<Integer, DistributorRpcDTO> map = new HashMap<>();
        JSONArray jsonArray = HttpUtil.getJson(hoseName+0,  JSONArray.class);
        LOGGER.info("查询回来的分销商列表："+ JSON.toJSONString(jsonArray));
        if(jsonArray !=null && jsonArray.size()>0){
            for (int x=0;x<jsonArray.size();x++){
                JSONObject  jsonObject= jsonArray.getJSONObject(x);
                DistributorRpcDTO distributorRpcDTO = JSONObject.parseObject(jsonObject.toJSONString(), DistributorRpcDTO.class);
                map.put(distributorRpcDTO.getId(),distributorRpcDTO);
            }
        }
        return map;
    }
}
