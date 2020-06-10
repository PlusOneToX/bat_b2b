package com.bat.flexible.manager.picture;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.label.dto.LabelRelaSimpleDTO;
import com.bat.flexible.api.model.dto.ModelRelaSimpleDTO;
import com.bat.flexible.api.picture.*;
import com.bat.flexible.api.picture.dto.*;
import com.bat.flexible.api.picture.dto.page.DistributorPictureQry;
import com.bat.flexible.api.picture.dto.page.PicturePageQry;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.dao.picture.co.*;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.picture.executor.PictureCategoryQryExe;
import com.bat.flexible.manager.picture.executor.PictureCmdExe;
import com.bat.flexible.manager.picture.executor.PictureQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.flexible.api.distributor.exclude.DistributorPictureExcludeServiceI;
import com.bat.flexible.api.exchange.ExchangePictureRelevanceServiceI;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.api.material.dto.MaterialRelaSimpleDTO;
import com.bat.flexible.api.picture.*;
import com.bat.flexible.api.picture.dto.*;
import com.bat.flexible.api.product.dto.ProductCategoryRelaDTO;
import com.bat.flexible.dao.exchange.dataobject.ExchangePictureRelevanceDO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.picture.co.*;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.picture.PictureConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.picture.PictureErrorCode;
import com.bat.flexible.manager.error.picture.PictureTemplateEditErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 图片业务层
 */
@Service
public class PictureServiceImpl implements PictureServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureServiceImpl.class);

    @Autowired
    private PictureCmdExe pictureCmdExe;

    @Autowired
    private PictureQryExe pictureQryExe;

    @Autowired
    private PictureCategoryQryExe pictureCategoryQryExe;

    @Autowired
    private PictureLabelDistributorScopeServiceI pictureLabelDistributorScopeServiceI;

    @Autowired
    private PictureProductCategoryRelevanceServiceI pictureProductCategoryRelevanceServiceI;

    @Autowired
    private PictureLabelRelevanceServiceI pictureLabelRelevanceServiceI;

    @Autowired
    private PictureModelRelevanceServiceI pictureModelRelevanceServiceI;

    @Autowired
    private PictureMaterialRelevanceServiceI pictureMaterialRelevanceServiceI;

    @Autowired
    private PictureDistributorRelevanceServiceI pictureDistributorRelevanceServiceI;

    @Autowired
    private PictureTemplateEditServiceI pictureTemplateEditServiceI;


    @Autowired
    private DistributorPictureExcludeServiceI distributorPictureExcludeServiceI;

    @Autowired
    private MaterialServiceI materialServiceI;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private DistributorServiceRpc distributorServiceRpc;

    @Autowired
    private ExchangePictureRelevanceServiceI exchangePictureRelevanceServiceI;

    @Autowired
    private PictureCategoryServiceI pictureCategoryServiceI;

    @Autowired
    private PictureModelMaterialDiyServiceI pictureModelMaterialDiyServiceI;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @Value("${distributor.id.sanxing}")
    private Integer sanxingDistributorId;

    @Transactional
    @Override
    public Response create(PictureCmd pictureCmd, AdminResponse adminResponse) {
        //校验参数
        checkParam(pictureCmd);
        PictureDO pictureDO = BeanUtils.copy(pictureCmd,PictureDO.class);

        //设置后台操作人信息
        setAdminMsg(pictureDO,adminResponse);
        Integer max = pictureQryExe.findMaxByCategoryId(pictureDO.getCategoryId());
        pictureDO.setSequence(max==null?1:max+1);
        pictureDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        pictureCmdExe.create(pictureDO);
        //设置图片的关联关系
        savePictureRela(pictureDO.getId(),pictureCmd,true,adminResponse);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response update(PictureCmd pictureCmd, AdminResponse currentAdmin) {
        LOGGER.info("修改图片："+ JSON.toJSONString(pictureCmd));
        //校验参数
        checkParam(pictureCmd);
        PictureDO pictureDO = pictureQryExe.getById(pictureCmd.getId());
        pictureDO.setCategoryId(pictureCmd.getCategoryId());
        pictureDO.setCode(pictureCmd.getCode());
        pictureDO.setName(pictureCmd.getName());
        pictureDO.setEnglishName(pictureCmd.getEnglishName());
        pictureDO.setType(pictureCmd.getType());
        pictureDO.setOriginImage(pictureCmd.getOriginImage());
        pictureDO.setThumbnail(pictureCmd.getThumbnail());
        pictureDO.setDescription(pictureCmd.getDescription());
        pictureDO.setModelScope(pictureCmd.getModelScope());
        pictureDO.setResellerScope(pictureCmd.getResellerScope());
        pictureDO.setThemeUrl(pictureCmd.getThemeUrl());
        pictureDO.setTemplateCenterX(pictureCmd.getTemplateCenterX());
        pictureDO.setTemplateCenterY(pictureCmd.getTemplateCenterY());
        pictureDO.setCopyrightCost(pictureCmd.getCopyrightCost());
        pictureDO.setOpenFlag(pictureCmd.getOpenFlag());
        pictureDO.setBackgroundPreviewUrl(pictureCmd.getBackgroundPreviewUrl());
        pictureDO.setNoCameraVacancyPreview(pictureCmd.getNoCameraVacancyPreview());
        setAdminMsg(pictureDO,currentAdmin);
        pictureCmdExe.update(pictureDO);
        savePictureRela(pictureDO.getId(),pictureCmd,false,currentAdmin);
        return Response.buildSuccess();
    }

    @Override
    public Response<PictureQry> detailById(Integer id) {
        PictureDO pictureDO = pictureQryExe.getById(id);
        PictureQry pictureQry = BeanUtils.copy(pictureDO,PictureQry.class);
        //查询关联关系
        queryRela(pictureQry);
        return Response.of(pictureQry);
    }

    @Override
    public List<PictureDO> listByPictureIdList(List<Integer> pictureIdList) {
        return pictureQryExe.listByPictureIdList(pictureIdList);
    }

    /**
     * 根据图片分类Id查询图片列表
     * @param categoryId
     * @return
     */
    @Override
    public List<PictureDO> listByCategoryIdAndOpenFlag(Integer categoryId,Short openFlag) {
        return pictureQryExe.listByCategoryIdAndOpenFlag(categoryId,openFlag);
    }

    @Override
    @Transactional
    public Response upOrDown(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin) {
        PictureDO pictureDO = pictureQryExe.getById(flexibleUpOrDownDTO.getId());
        PictureDO effect = pictureQryExe.findEffectByUpOrDown(pictureDO.getCategoryId(),flexibleUpOrDownDTO.getFlag(),pictureDO.getSequence());
        if(effect==null && flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_UP_TOP_ERROR);
        }
        if(effect==null && !flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DOWN_LOWEST_ERROR);
        }
        //对调排序号
        Integer temp = pictureDO.getSequence();
        pictureDO.setSequence(effect.getSequence());
        effect.setSequence(temp);
        setAdminMsg(pictureDO,currentAdmin);
        setAdminMsg(effect,currentAdmin);
        pictureCmdExe.update(pictureDO);
        pictureCmdExe.update(effect);
        return Response.buildSuccess();
    }

    /**
     * 启用禁用
     * @param flexibleUpdateStatusDTO
     * @param currentAdmin
     * @return
     */
    @Override
    @Transactional
    public Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin) {
        PictureDO pictureDO = pictureQryExe.getById(flexibleUpdateStatusDTO.getId());
        if(pictureDO.getOpenFlag()-flexibleUpdateStatusDTO.getOpenFlag()==0){
            throw  new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        pictureDO.setOpenFlag(flexibleUpdateStatusDTO.getOpenFlag());
        setAdminMsg(pictureDO,currentAdmin);
        pictureCmdExe.update(pictureDO);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response deleteById(Integer id, AdminResponse currentAdmin) {
        PictureDO pictureDO = pictureQryExe.getById(id);
        if(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES.equals(pictureDO.getOpenFlag())){
            //先下架才能删除
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DELETE_AFTER_DISABLE_ERROR);
        }
        setAdminMsg(pictureDO,currentAdmin);
        pictureDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
        pictureCmdExe.update(pictureDO);
        //删除关联关系
        deleteRela(id,currentAdmin);

        return Response.buildSuccess();
    }

    /**
     * 删除关联关系
     * @param id
     * @param currentAdmin
     */
    private void deleteRela(Integer id, AdminResponse currentAdmin) {
        pictureMaterialRelevanceServiceI.delete(null,id,currentAdmin);
        pictureProductCategoryRelevanceServiceI.delete(null,id);
        pictureLabelRelevanceServiceI.delete(null,id,currentAdmin);
        pictureModelRelevanceServiceI.delete(null,id,currentAdmin);
        pictureModelMaterialDiyServiceI.delete(null,null,id);
    }

    @Override
    @Transactional
    public Response top(Integer id, AdminResponse currentAdmin) {
        PictureDO pictureDO = pictureQryExe.getById(id);
        if(pictureDO.getSequence()==1){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_TOP_ALREADY_TOPEST_ERROR);
        }
        //将同一分类的图片降序（大于这个图片序号的）
        pictureCmdExe.updateSequenceAddByCategoryId(pictureDO.getCategoryId(),1,null,pictureDO.getSequence());
        pictureDO.setSequence(1);
        setAdminMsg(pictureDO,currentAdmin);
        pictureCmdExe.update(pictureDO);
        return Response.buildSuccess();
    }

    @Override
    public PictureDO getById(Integer pictureId) {
        return pictureQryExe.getById(pictureId);
    }

    @Override
    public PageInfo<PicturePageCO> page(PicturePageQry picturePageQry) {
        PageHelper.startPage(picturePageQry.getPage(),picturePageQry.getSize());
        List<PicturePageCO> list = pictureQryExe.listCOByCondition(picturePageQry.getCategoryId(),picturePageQry.getOpenFlag(),
                                picturePageQry.getContent(),picturePageQry.getType(),picturePageQry.getMaterialIdList(),picturePageQry.getModelIdList());
        return new PageInfo<>(list);
    }

    @Override
    public List<PictureCategorySimpleTreeCO> tree(PictureTreeQry pictureTreeQry) {
        //获取该分销商所有上级以及本身
        pictureTreeQry.setDistributorIds(flexibleDistributorQryExe.getDistributorTreePaths(pictureTreeQry.getDistributorId()));
        //通过材质编号查询材质ID
        if(StringUtils.isNotBlank(pictureTreeQry.getMaterialNo())){
            MaterialDO materialDO = materialServiceI.getByMaterialNo(pictureTreeQry.getMaterialNo());
            pictureTreeQry.setMaterialId(materialDO.getId());
        }
        return pictureQryExe.tree(pictureTreeQry);
    }

    /**
     * 查询关联关系
     * @param pictureQry
     */
    private void queryRela(PictureQry pictureQry) {
        //设置材质
        List<MaterialRelaSimpleDTO> materialRelaSimpleDTOList = pictureMaterialRelevanceServiceI.listSimpleByPictureIdAndDelFlag(pictureQry.getId(), FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        pictureQry.setMaterialRelaList(materialRelaSimpleDTOList);
        //设置型号范围
        queryModelScopeRela(pictureQry);
        //查询标签
        queryLabelRela(pictureQry);
        //设置分销商关系
        queryDistributorRela(pictureQry);
        //设置模板
        queryTemplate(pictureQry);
    }

    private void queryTemplate(PictureQry pictureQry) {
        if(!PictureConstant.PICTURE_TYPE_TEMPLATE.equals(pictureQry.getType())){
            return;
        }
        List<PictureTemplateEditCmd> templateEditList = pictureTemplateEditServiceI.listByPictureId(pictureQry.getId());
        pictureQry.setTemplateEditList(templateEditList);
    }

    private void queryDistributorRela(PictureQry pictureQry) {
        //分销商不可用
        List<DistributorSimpleRelaQry> excludeList = distributorPictureExcludeServiceI.listByPictureIdAndDelFlag(pictureQry.getId(),FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        pictureQry.setDistributorExcludeList(excludeList);
        //分销商适用
        if(!PictureConstant.RESELLER_SCOPE_DISTRIBUTOR_PERSONAL_ASSIGN.equals(pictureQry.getResellerScope())){
            return;
        }
        List<DistributorSimpleRelaQry> relaQryList = pictureDistributorRelevanceServiceI.listByPictureIdAndDelFlag(pictureQry.getId(),FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        pictureQry.setDistributorApplyList(relaQryList);
    }

    /**
     * 标签关联
     * @param pictureQry
     */
    private void queryLabelRela(PictureQry pictureQry) {
        List<LabelRelaSimpleDTO> relaSimpleDTOList = pictureLabelRelevanceServiceI.listLabelRelaByPictureIdAndDelFlag(pictureQry.getId());
        pictureQry.setLabelRelaList(relaSimpleDTOList);
    }

    private void queryModelScopeRela(PictureQry pictureQry) {
        if(PictureConstant.MODEL_SCOPE_MODEL_ALL.equals(pictureQry.getModelScope())){
            return;
        }
        if(PictureConstant.MODEL_SCOPE_MODEL_ASSIGN.equals(pictureQry.getModelScope())){
            //指定型号
            List<ModelRelaSimpleDTO> relaSimpleDTOList = pictureModelRelevanceServiceI.listModelRelaByPictureIdAndDelFlag(pictureQry.getId(),FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
            pictureQry.setModelApplyList(relaSimpleDTOList);
        }
        if(PictureConstant.MODEL_SCOPE_MODEL_CATEGORY.equals(pictureQry.getModelScope())){
            //指定型号类型
            List<ProductCategoryRelaDTO> categoryRelaDTOList = pictureProductCategoryRelevanceServiceI.listByPictureId(pictureQry.getId());
            pictureQry.setProductCategoryRelaList(categoryRelaDTOList);
        }
    }

    /**
     * 参数校验
     * @param pictureCmd
     */
    private void checkParam(PictureCmd pictureCmd) {
        //检查模板列表参数
        checkTemplateParam(pictureCmd.getType(),pictureCmd.getTemplateEditList(),pictureCmd.getTemplateCenterX(),pictureCmd.getTemplateCenterY());
        //检查型号关系
        checkModelRelaParam(pictureCmd.getModelScope(),pictureCmd.getProductCategoryIdList(),pictureCmd.getModelIdApplyList());
        //检查分销商关系
        checkoutDistributorRelaParam(pictureCmd.getResellerScope(),pictureCmd.getDistributorIdApplyList());
    }

    /**
     * 检查分销商参数
     * @param resellerScope
     * @param distributorIdApplyList
     */
    private void checkoutDistributorRelaParam(Short resellerScope, List<Integer> distributorIdApplyList) {
        if(!PictureConstant.RESELLER_SCOPE_DISTRIBUTOR_PERSONAL_ASSIGN.equals(resellerScope)){
            return;
        }
        if(distributorIdApplyList ==null || distributorIdApplyList.size()==0){
            //分销商id不能为空
            throw new FlexibleCustomException(PictureErrorCode.P_DISTRIBUTOR_LIST_NULL);
        }

    }

    /**
     * 检查型号关联参数
     * @param modelScope
     * @param productCategoryIdList
     * @param modelIdApplyList
     */
    private void checkModelRelaParam(Short modelScope, List<Integer> productCategoryIdList, List<Integer> modelIdApplyList) {
        if(PictureConstant.MODEL_SCOPE_MODEL_ALL.equals(modelScope)){
            //型号全部适用
            return;
        }
        if(PictureConstant.MODEL_SCOPE_MODEL_CATEGORY.equals(modelScope) && (productCategoryIdList==null || productCategoryIdList.size()==0)){
            //指定型号分类为空
            throw new FlexibleCustomException(PictureErrorCode.P_MODEL_CATEGORY_LIST_NULL);
        }
        if(PictureConstant.MODEL_SCOPE_MODEL_ASSIGN.equals(modelScope) && (modelIdApplyList==null || modelIdApplyList.size()==0)){
            //指定型号为空
            throw new FlexibleCustomException(PictureErrorCode.P_MODEL_LIST_NULL);
        }
    }

    /**
     * 检查模板参数
     * @param type
     * @param templateEditList
     * @param templateCenterX
     * @param templateCenterY
     */
    private void checkTemplateParam(Short type, List<PictureTemplateEditCmd> templateEditList,BigDecimal templateCenterX,BigDecimal templateCenterY) {
        if(!PictureConstant.PICTURE_TYPE_TEMPLATE.equals(type) && templateEditList !=null && templateEditList.size() >0){
            //非模板类型、且模板列表不为空
            throw new FlexibleCustomException(PictureErrorCode.P_TEMPLATE_LIST_MUST_NULL_WITHOUT_TEMPLATE);
        }
        if(!PictureConstant.PICTURE_TYPE_TEMPLATE.equals(type)){
            return;
        }
        if(templateEditList==null || templateEditList.size()==0){
            //图片为模板、，模板列表不能为空
            throw new FlexibleCustomException(PictureErrorCode.P_TEMPLATE_LIST_NULL);
        }
        if(templateCenterX ==null){
            throw new FlexibleCustomException(PictureErrorCode.P_TEMPLATE_LIST_NULL);
        }
        if(templateCenterY ==null){
            throw new FlexibleCustomException(PictureErrorCode.P_TEMPLATE_LIST_NULL);
        }
        templateEditList.stream().forEach(pictureTemplateEditCmd -> {
            if(pictureTemplateEditCmd.getEditCenterX()==null){
                //可编辑区域X轴不能为空
                throw new FlexibleCustomException(PictureTemplateEditErrorCode.P_TEMPLATE_EDIT_CENTER_X_NULL);
            }
            if(pictureTemplateEditCmd.getEditCenterX().compareTo(BigDecimal.ZERO)==-1){
                //可编辑区域X轴不能小于0
                throw new FlexibleCustomException(PictureTemplateEditErrorCode.P_TEMPLATE_EDIT_CENTER_X_LESS_THEN_ZERO);
            }
            if(pictureTemplateEditCmd.getEditCenterY()==null){
                //可编辑区域Y轴不能为空
                throw new FlexibleCustomException(PictureTemplateEditErrorCode.P_TEMPLATE_EDIT_CENTER_Y_NULL);
            }
            if(pictureTemplateEditCmd.getEditCenterY().compareTo(BigDecimal.ZERO)==-1){
                //可编辑区域Y轴不能小于0
                throw new FlexibleCustomException(PictureTemplateEditErrorCode.P_TEMPLATE_EDIT_CENTER_Y_LESS_THEN_ZERO);
            }
            if(pictureTemplateEditCmd.getWidth()==null){
                //可编辑区域宽度不能为空
                throw new FlexibleCustomException(PictureTemplateEditErrorCode.P_TEMPLATE_WIDTH_NULL);
            }
            if(pictureTemplateEditCmd.getWidth().compareTo(BigDecimal.ZERO)==-1){
                //可编辑区域宽度不能小于0
                throw new FlexibleCustomException(PictureTemplateEditErrorCode.P_TEMPLATE_WIDTH_LESS_THEN_ZERO);
            }
            if(pictureTemplateEditCmd.getLength()==null){
                //可编辑区域长度不能为空
                throw new FlexibleCustomException(PictureTemplateEditErrorCode.P_TEMPLATE_LENGTH_NULL);
            }
            if(pictureTemplateEditCmd.getLength().compareTo(BigDecimal.ZERO)==-1){
                //可编辑区域长度不能小于0
                throw new FlexibleCustomException(PictureTemplateEditErrorCode.P_TEMPLATE_LENGTH_LESS_THEN_ZERO);
            }
        });
    }

    /**
     * 设置图片的关联关系
     * @param pictureId
     * @param pictureCmd
     * @param isAdd 是否新增图片
     * @param adminResponse
     */
    private void savePictureRela(Integer pictureId, PictureCmd pictureCmd, Boolean isAdd, AdminResponse adminResponse) {
       // pictureLabelDistributorScopeServiceI.savePictureRela(pictureId,pictureCmd.getDistributorIdApplyList())pi
        //处理图片和标签的关联关系
        pictureLabelRelevanceServiceI.saveRela(true,Lists.newArrayList(pictureId),pictureCmd.getLabelIdList(),isAdd,adminResponse);
        //处理图片和型号类别的关联关系
        pictureProductCategoryRelevanceServiceI.saveRela(pictureId,pictureCmd.getModelScope(),isAdd,adminResponse,pictureCmd.getProductCategoryIdList());
        //处理图片和型号的关联关系
        pictureModelRelevanceServiceI.saveRela(pictureId,pictureCmd.getModelScope(),isAdd,adminResponse,pictureCmd.getModelIdApplyList());
        //处理图片和材质的关联关系
        pictureMaterialRelevanceServiceI.saveRela(true, Lists.newArrayList(pictureId),pictureCmd.getMaterialIdList(),adminResponse,isAdd);
        //处理图片和分销商关联关系
        pictureDistributorRelevanceServiceI.saveRela(pictureId,pictureCmd.getDistributorIdApplyList(),isAdd,adminResponse,pictureCmd.getResellerScope());
        //处理模板
        pictureTemplateEditServiceI.saveTemplate(pictureId,pictureCmd.getTemplateEditList(),pictureCmd.getType(),isAdd,adminResponse);
        //处理分销商不适用
        distributorPictureExcludeServiceI.save(pictureId,pictureCmd.getDistributorIdExcludeList(),isAdd,adminResponse);
    }

    /**
     * 设置后台操作人信息
     * @param pictureDO
     * @param adminResponse
     */
    private void setAdminMsg(PictureDO pictureDO, AdminResponse adminResponse) {
        if(pictureDO.getId() ==null){
            pictureDO.setCreateTime(new Date());
            pictureDO.setCreateUserId(adminResponse.getId());
            pictureDO.setCreateUserName(adminResponse.getUserName());
        }
        pictureDO.setUpdateTime(new Date());
        pictureDO.setUpdateUserId(adminResponse.getId());
        pictureDO.setUpdateUserName(adminResponse.getUserName());
    }

    /**
     * 校验分销商图片权限
     * @param distributorIdList
     * @param pictureIdList
     */
    @Override
    public void checkDistributorPicture(List<Integer> distributorIdList, List<Integer> pictureIdList) {
        if(distributorIdList ==null || distributorIdList.size()==0){
            throw new FlexibleCustomException(PictureErrorCode.P_DISTRIBUTOR_LIST_NULL);
        }
        if(pictureIdList ==null || pictureIdList.size()==0){
            throw new FlexibleCustomException(PictureErrorCode.P_PICTURE_LIST_NULL);
        }
        distributorIdList.stream().forEach(distributorId -> {


            Integer countryId = null;
            try {
                countryId = flexibleDistributorQryExe.getCountryIdByDistributorId(distributorId);
            } catch (FlexibleDubboApiException e) {
                e.printStackTrace();
                throw new FlexibleCustomException(e.getMessage());
            }
            List<PictureDO> pictureList = pictureQryExe.checkDistributorPicture(distributorId,pictureIdList,countryId);
            if(pictureList ==null || pictureList.size() ==0 ){
                com.bat.dubboapi.distributor.common.Response<DistributorRpcDTO> distributorRpcDTOResponse = distributorServiceRpc.distributorById(distributorId);
                DistributorRpcDTO distributorRpcDTO = distributorRpcDTOResponse.getData();
                throw new FlexibleCustomException("分销商【"+distributorRpcDTO.getName()+"】尚未关联图片" );
            }
            for(int x=0;x<pictureIdList.size();x++){
                Boolean flag = false;
                for(int y=0;y<pictureList.size();y++){
                    if(pictureIdList.get(x) - pictureList.get(y).getId() ==0){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    PictureDO picture = pictureQryExe.getById(pictureIdList.get(x));
                    com.bat.dubboapi.distributor.common.Response<DistributorRpcDTO> distributorRpcDTOResponse = distributorServiceRpc.distributorById(distributorId);
                    DistributorRpcDTO distributorRpcDTO = distributorRpcDTOResponse.getData();
                    String msg = "分销商【"+distributorRpcDTO.getName()+"】无图片【"+picture.getName()+"】权限";
                    throw new FlexibleCustomException(msg);
                }
            }
        });
    }

    @Override
    public PageInfo<CommonPicturePageCO> detailPictureList(ThemeDTO themeDTO) {
       /* if(themeDTO.getDistributorId()==null|| themeDTO.getCategoryId()==null|| themeDTO.getPage()==null|| themeDTO.getSize()==null){
            return new ResponseBaseBean();
        }
        PageInfo pageInfo = pictureDataManager.detailList(themeDTO);
        List<Picture> list=pageInfo.getList();
        for (Picture picture : list) {
            //处理模板
            if (picture.getType() == PictureConstant.TypeTemplate.intValue()) {
                List<PictureTemplateEdit> templateEditList = pictureTemplateEditRepository.findByPictureId(picture.getId().longValue());
                picture.setTemplateEditList(templateEditList);
            }
        }
        ResponsePageBaseBean responsePageBaseBean = new ResponsePageBaseBean();
        responsePageBaseBean.setPageInfo(pageInfo);*/
        themeDTO.setDistributorIds(flexibleDistributorQryExe.getDistributorTreePaths(themeDTO.getDistributorId()));
        ExchangePictureRelevanceDO exchangePictureRelevanceDO = null;
        if(themeDTO.getExchangeId() !=null){
            exchangePictureRelevanceDO = exchangePictureRelevanceServiceI.findOneByExchangeId(themeDTO.getExchangeId());
        }

        Integer countryId = null;
        try {
            countryId = flexibleDistributorQryExe.getCountryIdByDistributorId(themeDTO.getDistributorId());
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            throw new FlexibleCustomException(e.getMessage());
        }
        List<Integer>pictureCategoryIdList = null;
        if( themeDTO.getCategoryId() !=0){
            //图片分类id列表为空、且图片分类id不为空、查询图片分类下面的子列表
            List<PictureCategoryDO> pictureCategoryDOList = pictureCategoryServiceI.listByParentIdAndOpenFlag(themeDTO.getCategoryId(),FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
            pictureCategoryIdList = pictureCategoryDOList.stream().map(pictureCategory -> pictureCategory.getId()).collect(Collectors.toList());
            pictureCategoryIdList.add(themeDTO.getCategoryId());
        }
        if (themeDTO.getSecondCategoryId() != null) {
            pictureCategoryIdList = new ArrayList<>();
            pictureCategoryIdList.add(themeDTO.getSecondCategoryId());
        }
        PageHelper.startPage(themeDTO.getPage(),themeDTO.getSize());
        List<CommonPicturePageCO> list = pictureQryExe.listCommonPictureCOByCondition(themeDTO.getDistributorId(),themeDTO.getDistributorIds(),themeDTO.getMaterialId(),
                themeDTO.getModelId(),themeDTO.getExchangeId(),exchangePictureRelevanceDO,countryId,pictureCategoryIdList,themeDTO.getType());
        if(list ==null || list.size()==0){
            return new PageInfo<>(list);
        }
        List<PictureTemplateEditCmd> editCmdList = pictureTemplateEditServiceI.listByPictureId(null);
        if(editCmdList ==null || editCmdList.size()==0){
            return new PageInfo(list);
        }
        Map<Integer, List<PictureTemplateEditCmd>> editCmdMap = editCmdList.stream().collect(Collectors.toMap(PictureTemplateEditCmd::getPictureId, pictureTemplateEditCmd -> Lists.newArrayList(pictureTemplateEditCmd),
                (List<PictureTemplateEditCmd> oldList,List<PictureTemplateEditCmd> newList)->{
                    oldList.addAll(newList);
                    return oldList;
                }));
        list.stream().forEach(commonPicturePageCO -> {
            if(PictureConstant.PICTURE_TYPE_TEMPLATE.equals(commonPicturePageCO.getType())){
                List<PictureTemplateEditCmd> pictureTemplateEditCmdList = editCmdMap.get(commonPicturePageCO.getPictureId());
                commonPicturePageCO.setTemplateEditList(pictureTemplateEditCmdList);
            }
        });
        return new PageInfo<>(list);
    }

    @Override
    public PictureDTO detailPictureFindOne(Integer id) {
        PictureDO pictureDO = pictureQryExe.getById(id);
        if (pictureDO == null) {
            return null;
        }
        PictureDTO pictureDTO = new PictureDTO();
        org.springframework.beans.BeanUtils.copyProperties(pictureDO, pictureDTO);
        pictureDTO.setPictureName(pictureDO.getName());
        if (pictureDO.getCategoryId() != null) {
            PictureCategoryDO pictureCategoryDO = pictureCategoryQryExe.getById(pictureDO.getCategoryId());
            if (pictureCategoryDO != null) {
                pictureDTO.setCategoryName(pictureCategoryDO.getName());
            }
        }
        return pictureDTO;
    }

    @Override
    public PageInfo<DistributorPictureCO> pagePictureByDistributor(DistributorPictureQry distributorPictureQry) {
        if(distributorPictureQry.getCategoryId() !=null){
            List<PictureCategoryDO> pictureCategoryDOList = pictureCategoryServiceI.listByParentIdAndOpenFlag(distributorPictureQry.getCategoryId(),FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
            List<Integer> pictureCategoryIds = pictureCategoryDOList.stream().map(pictureCategory -> pictureCategory.getId()).collect(Collectors.toList());
            pictureCategoryIds.add(distributorPictureQry.getCategoryId());
            distributorPictureQry.setCategoryIdList(pictureCategoryIds);
        }

        Integer countryId = null;
        try {
            countryId = flexibleDistributorQryExe.getCountryIdByDistributorId(distributorPictureQry.getDistributorId());
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            throw new FlexibleCustomException(e.getMessage());
        }
        PageHelper.startPage(distributorPictureQry.getPage(),distributorPictureQry.getSize());
        List<DistributorPictureCO> list = pictureQryExe.listDistributorByCondition(distributorPictureQry.getDistributorId(),distributorPictureQry.getOpenFlag(),
                distributorPictureQry.getCategoryIdList(),distributorPictureQry.getType(),distributorPictureQry.getDistributorId()==sanxingDistributorId.intValue(),countryId);
        return new PageInfo<>(list);
    }

    /**
     * 根据id列表查询主题列表
     * @param idList
     * @return
     */
    @Override
    public List<PictureThemeUrlDTO> listThemeUrlByIdList(List<Integer> idList) {
        List<PictureDO> list = pictureQryExe.listByPictureIdList(idList);
        return BeanUtils.copyList(list,PictureThemeUrlDTO.class);
    }

    @Override
    public List<PictureDO> listByCondition(Short openFlag, Short type) {
        return pictureQryExe.listByCondition(openFlag,type);
    }

    @Override
    public PictureDO getByCode(String code) {
        return pictureQryExe.getByCode(code);
    }


}
