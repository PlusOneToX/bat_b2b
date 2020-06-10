package com.bat.flexible.manager.index.executor;

import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.index.dto.series.DistributorSeriesThemeListDTO;
import com.bat.flexible.manager.common.constant.exchange.ExchangeRelaConstant;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.exchange.dataobject.ExchangePictureRelevanceDO;
import com.bat.flexible.dao.index.DistributorSeriesThemeDOMapper;
import com.bat.flexible.dao.index.co.DistributorSeriesThemePictureCO;
import com.bat.flexible.dao.index.co.DistributorSeriesThemeSimpleCO;
import com.bat.flexible.dao.index.co.SeriesThemePageCO;
import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DistributorSeriesThemeQryExe {

    @Autowired
    private DistributorSeriesThemeDOMapper distributorSeriesThemeDOMapper;

    @Value("${distributor.id.sanxing}")
    private Integer sanxingDistributorId;

    public DistributorSeriesThemeDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        DistributorSeriesThemeDO distributorSeriesThemeDO = distributorSeriesThemeDOMapper.selectByPrimaryKey(id);
        if(distributorSeriesThemeDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        return distributorSeriesThemeDO;
    }

    public DistributorSeriesThemeDO findMaxSortNo() {
        return distributorSeriesThemeDOMapper.findMaxSortNo();
    }


    public List<DistributorSeriesThemeDO> listByDistributorIds(List<Integer> distributorIds) {
        return distributorSeriesThemeDOMapper.listByDistributorIds(distributorIds);
    }

    public List<DistributorSeriesThemeListDTO> listDTOByDistributorIds(List<Integer> distributorIds, Integer seriesNum, Integer pictureNum) {
        //图片数量需要减一
        pictureNum--;
        List<DistributorSeriesThemePictureCO> list = distributorSeriesThemeDOMapper.pageCOByDistributorIds(distributorIds,seriesNum,pictureNum);
        if(list ==null || list.size()==0){
            return null;
        }
        List<DistributorSeriesThemeListDTO> resuList = new ArrayList<>();
        for(int x=0;x<list.size();x++){
            DistributorSeriesThemeListDTO themeListDTO = new DistributorSeriesThemeListDTO();
            themeListDTO.setCategoryImage(list.get(x).getCategoryImage());
            themeListDTO.setSeriesId(list.get(x).getSeriesId());
            themeListDTO.setThemeName(list.get(x).getThemeName());
            List<CommonPicturePageCO> pictureDTOList = new ArrayList<>();
            CommonPicturePageCO pictureDTO = new CommonPicturePageCO();
            pictureDTO.setEnglishName(list.get(x).getEnglishName());
            pictureDTO.setPictureId(list.get(x).getPictureId());
            pictureDTO.setThumbnail(list.get(x).getThumbnail());
            pictureDTO.setPictureName(list.get(x).getPictureName());
            pictureDTO.setId(list.get(x).getId());
            pictureDTO.setOriginImage(list.get(x).getOriginImage());
            pictureDTOList.add(pictureDTO);
            for(int y=x+1;y<list.size();y++){
                if(list.get(y).getSeriesId() - list.get(x).getSeriesId() ==0){
                    pictureDTO = new CommonPicturePageCO();
                    pictureDTO.setEnglishName(list.get(y).getEnglishName());
                    pictureDTO.setPictureId(list.get(y).getPictureId());
                    pictureDTO.setThumbnail(list.get(y).getThumbnail());
                    pictureDTO.setPictureName(list.get(y).getPictureName());
                    pictureDTO.setId(list.get(y).getId());
                    pictureDTO.setOriginImage(list.get(y).getOriginImage());
                    pictureDTOList.add(pictureDTO);
                    x++;
                    if(x-list.size()==-1){
                        themeListDTO.setPictureDTOList(pictureDTOList);
                        resuList.add(themeListDTO);
                        break;
                    }
                }else {
                    //新增
                    themeListDTO.setPictureDTOList(pictureDTOList);
                    resuList.add(themeListDTO);
                    break;
                }
            }
        }
        return resuList;
    }

    public List<CommonPicturePageCO> listCommonPictureCOByCondition(ThemeDTO themeDTO, ExchangePictureRelevanceDO exchangePictureRelevanceDO, Integer countryId) {
        //判断是否是
        boolean isSanXing=false;
        if(themeDTO.getDistributorId()==sanxingDistributorId.intValue()){
            isSanXing=true;
        }
        List<CommonPicturePageCO> list = new ArrayList<>();
        if(themeDTO.getExchangeId()!=null) {
            //存在兑换卡
            if (ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_ALL.equals(exchangePictureRelevanceDO.getType())) {
                list = distributorSeriesThemeDOMapper.listExchangeAllPictureByCondition(themeDTO.getThemeId(),isSanXing,themeDTO.getDistributorIds(),
                       themeDTO.getExchangeId(),themeDTO.getMaterialId(),themeDTO.getModelId() ,countryId);
            } else {
                list = distributorSeriesThemeDOMapper.listAssignExchangePictureByCondition(themeDTO.getThemeId(),isSanXing,themeDTO.getDistributorIds(),
                        themeDTO.getExchangeId(),themeDTO.getMaterialId(),themeDTO.getModelId(),countryId );
            }
        }else{
            //没有兑换卡
            list = distributorSeriesThemeDOMapper.listPictureWithOutExchange(themeDTO.getThemeId(),isSanXing,themeDTO.getDistributorIds(),
                    themeDTO.getMaterialId(),themeDTO.getModelId(),countryId);
        }
        return list;
    }

    public Integer listCommonPictureCOByConditionCount(ThemeDTO themeDTO, ExchangePictureRelevanceDO exchangePictureRelevanceDO,Integer countryId) {
        //判断是否是
        boolean isSanXing=false;
        if(themeDTO.getDistributorId()==sanxingDistributorId.intValue()){
            isSanXing=true;
        }
        Integer list;
        if(themeDTO.getExchangeId()!=null) {
            //存在兑换卡
            if (ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_ALL.equals(exchangePictureRelevanceDO.getType())) {
                list = distributorSeriesThemeDOMapper.listExchangeAllPictureByConditionCount(themeDTO.getThemeId(),isSanXing,themeDTO.getDistributorIds(),
                        themeDTO.getExchangeId(),themeDTO.getMaterialId(),themeDTO.getModelId() ,countryId);
            } else {
                list = distributorSeriesThemeDOMapper.listAssignExchangePictureByConditionCount(themeDTO.getThemeId(),isSanXing,themeDTO.getDistributorIds(),
                        themeDTO.getExchangeId(),themeDTO.getMaterialId(),themeDTO.getModelId(),countryId );
            }
        }else{
            //没有兑换卡
            list = distributorSeriesThemeDOMapper.listPictureWithOutExchangeCount(themeDTO.getThemeId(),isSanXing,themeDTO.getDistributorIds(),
                    themeDTO.getMaterialId(),themeDTO.getModelId(),countryId);
        }
        return list;
    }

    public List<PictureCategoryDO> listPictureCategoryByCondition(ThemeDTO themeDTO,Integer countryId) {
        //判断是否是
        boolean isSanXing=false;
        if(themeDTO.getDistributorId()==sanxingDistributorId.intValue()){
            isSanXing=true;
        }
        return distributorSeriesThemeDOMapper.listPictureCategoryByCondtion(themeDTO.getThemeId(),isSanXing,themeDTO.getDistributorIds(),themeDTO.getMaterialId(),
                themeDTO.getModelId(),countryId);
    }

    public List<SeriesThemePageCO> listCOByCondition(Integer themeId, Integer pictureCategoryId, String content) {
        return distributorSeriesThemeDOMapper.listCOByCondition(themeId,pictureCategoryId,content);
    }

    public List<DistributorSeriesThemeSimpleCO> listSimpleCOByDistributorIdAndContent(Integer distributorId, String content) {
        return distributorSeriesThemeDOMapper.listSimpleCOByDistributorIdAndContent(distributorId,content);
    }
}
