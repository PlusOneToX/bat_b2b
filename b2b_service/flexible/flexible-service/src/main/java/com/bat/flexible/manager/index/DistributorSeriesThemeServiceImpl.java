package com.bat.flexible.manager.index;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.index.DistributorBannerServiceI;
import com.bat.flexible.api.index.DistributorSeriesThemeRelevanceServiceI;
import com.bat.flexible.api.index.DistributorSeriesThemeServiceI;
import com.bat.flexible.api.index.SeriesPictureRelevanceServiceI;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.index.dto.page.SeriesThemePageQry;
import com.bat.flexible.api.index.dto.series.DistributorSeriesThemeDTO;
import com.bat.flexible.api.index.dto.series.DistributorSeriesThemeDetailDTO;
import com.bat.flexible.api.index.dto.series.DistributorSeriesThemeListDTO;
import com.bat.flexible.api.index.dto.series.DistributorSeriesThemeQry;
import com.bat.flexible.api.picture.PictureCategoryServiceI;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.picture.PictureTemplateEditServiceI;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.manager.common.constant.index.SeriesThemeConstant;
import com.bat.flexible.manager.common.constant.picture.PictureConstant;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.index.executor.DistributorSeriesThemeCmdExe;
import com.bat.flexible.manager.index.executor.DistributorSeriesThemeQryExe;
import com.bat.flexible.manager.index.validator.DistributorSeriesThemeValidator;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.flexible.api.exchange.ExchangePictureRelevanceServiceI;
import com.bat.flexible.dao.exchange.dataobject.ExchangePictureRelevanceDO;
import com.bat.flexible.dao.index.co.DistributorSeriesThemeSimpleCO;
import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import com.bat.flexible.dao.index.co.SeriesThemePageCO;
import com.bat.flexible.dao.index.dataobject.DistributorBannerDO;
import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeDO;
import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeRelevanceDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.dao.picture.co.PictureTemplateEditCmd;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.manager.error.index.DistributorSeriesThemeErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DistributorSeriesThemeServiceImpl implements DistributorSeriesThemeServiceI {


    @Autowired
    private SeriesPictureRelevanceServiceI seriesPictureRelevanceServiceI;

    @Autowired
    private DistributorSeriesThemeCmdExe distributorSeriesThemeCmdExe;

    @Autowired
    private DistributorSeriesThemeQryExe distributorSeriesThemeQryExe;

    @Autowired
    private PictureServiceI pictureServiceI;

    @Autowired
    private PictureCategoryServiceI pictureCategoryServiceI;


    @Autowired
    private DistributorBannerServiceI distributorBannerServiceI;

    @Autowired
    private DistributorSeriesThemeRelevanceServiceI distributorSeriesThemeRelevanceServiceI;


/*
    @Autowired
    private PictureTheme pictureThemeDataManager;*/

    @Autowired
    private PictureTemplateEditServiceI pictureTemplateEditServiceI;

    @DubboReference(check = false,timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @Autowired
    private ExchangePictureRelevanceServiceI exchangePictureRelevanceServiceI;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributorSeriesThemeServiceImpl.class);

    /**
     * 新增
     * @param distributorSeriesThemeDTO
     * @param currentAdmin
     * @return
     */
    @Override
    @Transactional
    public Response create(DistributorSeriesThemeDTO distributorSeriesThemeDTO, AdminResponse currentAdmin) {
        DistributorSeriesThemeDO seriesTheme = new DistributorSeriesThemeDO();
        seriesTheme.setCategoryId(distributorSeriesThemeDTO.getPictureCategoryId());
        seriesTheme.setThemeName(distributorSeriesThemeDTO.getSeriesName());
        setSortNo(seriesTheme);
        //设置操作人信息
        setAdminUserMsg(currentAdmin, seriesTheme);
        List<Integer> pictureIdList = new ArrayList<>(distributorSeriesThemeDTO.getPictureIdList());
        //校验分销商参数
        List<Integer> distributorIdList = DistributorSeriesThemeValidator.validDistributor(distributorSeriesThemeDTO.getDistributorList());
        pictureServiceI.checkDistributorPicture(distributorIdList,pictureIdList);
        distributorSeriesThemeCmdExe.create(seriesTheme);
        List<Integer> pictureList = new ArrayList<>(distributorSeriesThemeDTO.getPictureIdList());
        seriesPictureRelevanceServiceI.save(pictureList,seriesTheme.getId(),true,currentAdmin);

        distributorSeriesThemeRelevanceServiceI.add(seriesTheme.getId(),distributorSeriesThemeDTO.getDistributorList(),currentAdmin);
        return Response.buildSuccess();
    }

    private void setSortNo(DistributorSeriesThemeDO seriesTheme) {

        DistributorSeriesThemeDO max = distributorSeriesThemeQryExe.findMaxSortNo();
        seriesTheme.setSortNo(max==null?1:max.getSortNo()+1);
    }


    /**
     * 编辑
     * @param distributorSeriesThemeDTO
     * @param currentAdmin
     * @return
     */
    @Override
    @Transactional
    public Response update(DistributorSeriesThemeDTO distributorSeriesThemeDTO, AdminResponse currentAdmin) {
        DistributorSeriesThemeDO seriesTheme = distributorSeriesThemeQryExe.getById(distributorSeriesThemeDTO.getId());
        seriesTheme.setCategoryId(distributorSeriesThemeDTO.getPictureCategoryId());
        seriesTheme.setThemeName(distributorSeriesThemeDTO.getSeriesName());
        //设置操作人信息
        setAdminUserMsg(currentAdmin, seriesTheme);
        List<Integer> pictureList = new ArrayList<>(distributorSeriesThemeDTO.getPictureIdList());
        //校验分销商参数
        List<Integer> distributorIdList = DistributorSeriesThemeValidator.validDistributor(distributorSeriesThemeDTO.getDistributorList());
        pictureServiceI.checkDistributorPicture(distributorIdList,pictureList);
        distributorSeriesThemeCmdExe.update(seriesTheme);

        seriesPictureRelevanceServiceI.save(pictureList,seriesTheme.getId(),false,currentAdmin);

        distributorSeriesThemeRelevanceServiceI.save(seriesTheme.getId(),distributorSeriesThemeDTO.getDistributorList(),currentAdmin);
        return Response.buildSuccess();
    }

    @Override
    public PageInfo<SeriesThemePageCO> page(SeriesThemePageQry seriesThemePageQry) {
        PageHelper.startPage(seriesThemePageQry.getPage(),seriesThemePageQry.getSize());
        List<SeriesThemePageCO> list = distributorSeriesThemeQryExe.listCOByCondition(seriesThemePageQry.getThemeId(),
                seriesThemePageQry.getPictureCategoryId(),seriesThemePageQry.getContent());
        return new PageInfo(list);
    }

    @Override
    public Response detail(Integer id) {
        DistributorSeriesThemeDO seriesTheme = distributorSeriesThemeQryExe.getById(id);
        DistributorSeriesThemeDetailDTO themeDTO = BeanUtils.copy(seriesTheme,DistributorSeriesThemeDetailDTO.class);
        themeDTO.setSortNo(seriesTheme.getSortNo()>2? SeriesThemeConstant.SortNoTypeRandom:seriesTheme.getSortNo());
        themeDTO.setPictureCategoryId(seriesTheme.getCategoryId());
        themeDTO.setSeriesName(seriesTheme.getThemeName());

        PictureCategoryDO pictureCategory = pictureCategoryServiceI.getById(seriesTheme.getCategoryId());
        if(pictureCategory.getParentId()>0 ){
            PictureCategoryDO  parentCategory = pictureCategoryServiceI.getById(pictureCategory.getParentId());
            themeDTO.setParentCategoryId(parentCategory.getId());
        }
        List<IndexRecommendRelaCO> dtoList = seriesPictureRelevanceServiceI.listCOByIndexSeriesId(id);
        themeDTO.setPictureList(dtoList);
        List<DistributorSimpleRelaQry> distributorSimpleDTOList = distributorSeriesThemeRelevanceServiceI.listDistributorSimpleMsgBySeriesThemeId(id);
        themeDTO.setDistributorList(distributorSimpleDTOList);
        return Response.of(themeDTO);
    }

    @Override
    public Response listSimpleByCondition(DistributorSeriesThemeQry distributorSeriesThemeQry) {
        List<DistributorSeriesThemeSimpleCO>  list = distributorSeriesThemeQryExe.listSimpleCOByDistributorIdAndContent(distributorSeriesThemeQry.getDistributorId(),
                distributorSeriesThemeQry.getContent());
        return Response.of(list);
    }

    @Override
    public Response list(SeriesThemePageQry seriesThemePageQry) {

       /* List<SeriesThemePageDTO> list = distributorSeriesThemeQryExe.list(seriesThemePageRequest);
        return Response.of(list);*/
        List<SeriesThemePageCO> list = distributorSeriesThemeQryExe.listCOByCondition(seriesThemePageQry.getThemeId(),
                seriesThemePageQry.getPictureCategoryId(),seriesThemePageQry.getContent());
        return Response.of(list);
    }

    @Override
    public Response listByDistributorId(Integer distributorId, Integer seriesNum, Integer pictureNum) {
        List<Integer> distributorIds = flexibleDistributorQryExe.getDistributorTreePaths(distributorId);
        List<DistributorSeriesThemeListDTO> list = distributorSeriesThemeQryExe.listDTOByDistributorIds(distributorIds, seriesNum, pictureNum);

        return Response.of(list);
        //return null;
    }



    @Transactional
    @Override
    public Response delete(Integer id) {
        DistributorSeriesThemeDO seriesTheme = distributorSeriesThemeQryExe.getById(id);
        List<DistributorBannerDO> bannerList = distributorBannerServiceI.listBySeriesId(id);
        if(bannerList !=null && bannerList.size()>0){
            throw new FlexibleCustomException(DistributorSeriesThemeErrorCode.INDEX_SERIES_DEL_FAIL_BY_RELA_BANNER);
        }
        distributorSeriesThemeCmdExe.deleteById(id);
        seriesPictureRelevanceServiceI.deleteBySeriesId(id);
        distributorSeriesThemeRelevanceServiceI.deleteBySeriesThemeId(id);
        return Response.buildSuccess();
    }

    /**
     * 校验分销商主题系列权限
     * @param distributorSimpleRelaQryList
     * @param seriesId
     */
    @Override
    public void checkDistributorSeriesTheme(List<DistributorSimpleRelaQry> distributorSimpleRelaQryList, Integer seriesId) {
        if(seriesId ==null){
            return;
        }
        List<Integer> distributorIdList = new ArrayList<>();
        distributorSimpleRelaQryList.stream().forEach(distributorSimpleRelaQry -> {
            distributorIdList.add(distributorSimpleRelaQry.getDistributorId());
        });
        List<DistributorSeriesThemeRelevanceDO> relaList = distributorSeriesThemeRelevanceServiceI.findByDistributorIdList(distributorIdList,seriesId);
        if(relaList ==null || relaList.size()==0){
            //String msg = "所有分销商没有这个主题系列权限";
            throw new FlexibleCustomException(DistributorSeriesThemeErrorCode.INDEX_DISTRIBUTOR_SERIES_THEME_PERMISSION_ERROR);
        }
        distributorIdList.stream().forEach(distributorId -> {
            Boolean flag = false;

            for(int x=0;x<relaList.size();x++){
                if(distributorId - relaList.get(x).getDistributorId() ==0){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                //分销商没有这个主题系列
                com.bat.dubboapi.distributor.common.Response<DistributorRpcDTO> distributorRpcDTOResponse = distributorServiceRpc.distributorById(distributorId);
                DistributorRpcDTO distributorRpcDTO = distributorRpcDTOResponse.getData();
                String msg = "【"+distributorRpcDTO.getName()+"】"+ MessageUtils.get(DistributorSeriesThemeErrorCode.INDEX_DISTRIBUTOR_SERIES_THEME_PERMISSION_ERROR);
                throw new FlexibleCustomException(msg);
            }
        });
    }

    /**
     * 设置操作人信息
     * @param currentAdmin
     * @param seriesTheme
     */
    private void setAdminUserMsg(AdminResponse currentAdmin, DistributorSeriesThemeDO seriesTheme) {
        if(seriesTheme.getId() ==null){
            seriesTheme.setCreateTime(new Date());
            seriesTheme.setCreateUserId(currentAdmin.getId());
            seriesTheme.setCreateUserName(currentAdmin.getUserName());
        }
        seriesTheme.setUpdateTime(new Date());
        seriesTheme.setUpdateUserId(currentAdmin.getId());
        seriesTheme.setUpdateUserName(currentAdmin.getUserName());
    }

    @Override
    public Response tabList(ThemeDTO themeDTO) {
        if(themeDTO.getDistributorId()==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL);
        }
        themeDTO.setDistributorIds(flexibleDistributorQryExe.getDistributorTreePaths(themeDTO.getDistributorId()));
        List<DistributorSeriesThemeDO> distributorSeriesThemes = distributorSeriesThemeQryExe.listByDistributorIds(themeDTO.getDistributorIds());
        Iterator<DistributorSeriesThemeDO> iterator = distributorSeriesThemes.iterator();

        while (iterator.hasNext()) {
            DistributorSeriesThemeDO distributorSeriesTheme = iterator.next();
            themeDTO.setThemeId(distributorSeriesTheme.getId());
            Integer count = pictureListCount(themeDTO);
            if (count == 0) {
                //移除没有数据的导航
                iterator.remove();
            }
        }

        return Response.of(distributorSeriesThemes);
    }

    @Override
    public PageInfo pictureList(ThemeDTO themeDTO) {
       /* if(themeDTO.getDistributorId()==null|| themeDTO.getThemeId()==null|| themeDTO.getPage()==null|| themeDTO.getSize()==null){
          return new ResponseBaseBean();
        }
        PageInfo pageInfo = pictureDataManager.list(themeDTO);
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

        Integer countryId =null;
        try {
            countryId = flexibleDistributorQryExe.getCountryIdByDistributorId(themeDTO.getDistributorId());
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            throw new FlexibleCustomException(e.getMessage());
        }
        PageHelper.startPage(themeDTO.getPage(),themeDTO.getSize());
        List<CommonPicturePageCO> list = distributorSeriesThemeQryExe.listCommonPictureCOByCondition(themeDTO,exchangePictureRelevanceDO,countryId);
        LOGGER.info("查询到的数据:{}", JSONObject.toJSONString(list));
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
        return new PageInfo(list);
    }

    @Override
    public Integer pictureListCount(ThemeDTO themeDTO) {
        ExchangePictureRelevanceDO exchangePictureRelevanceDO = null;
        if(themeDTO.getExchangeId() !=null){
            exchangePictureRelevanceDO = exchangePictureRelevanceServiceI.findOneByExchangeId(themeDTO.getExchangeId());
        }
        Integer countryId =null;
        try {
            countryId = flexibleDistributorQryExe.getCountryIdByDistributorId(themeDTO.getDistributorId());
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            throw new FlexibleCustomException(e.getMessage());
        }
        return distributorSeriesThemeQryExe.listCommonPictureCOByConditionCount(themeDTO,exchangePictureRelevanceDO,countryId);
    }

    @Override
    public Response detailTabList(ThemeDTO themeDTO) {
       if(themeDTO.getDistributorId()==null){
           throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL);
        }
        themeDTO.setDistributorIds(flexibleDistributorQryExe.getDistributorTreePaths(themeDTO.getDistributorId()));
        /*
        List<PictureCategory> pictureCategories = pictureCategoryDataManager.detailTabList(themeDTO);
        ResponseBaseBean response = new ResponseBaseBean();
        response.setData(pictureCategories);*/
        Integer countryId= null;
        try {
            countryId = flexibleDistributorQryExe.getCountryIdByDistributorId(themeDTO.getDistributorId());
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            throw new FlexibleCustomException(e.getMessage());
        }

        List<PictureCategoryDO> list = distributorSeriesThemeQryExe.listPictureCategoryByCondition(themeDTO,countryId);
        return Response.of(list);
    }








    @Override
    public DistributorSeriesThemeDO getById(Integer seriesId) {
        return distributorSeriesThemeQryExe.getById(seriesId);
    }

    @Transactional
    @Override
    public void updateDO(DistributorSeriesThemeDO seriesTheme) {
        distributorSeriesThemeCmdExe.update(seriesTheme);
    }
}
