package com.bat.flexible.manager.index;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.DistributorBannerServiceI;
import com.bat.flexible.api.index.DistributorSeriesThemeRelevanceServiceI;
import com.bat.flexible.api.index.DistributorSeriesThemeServiceI;
import com.bat.flexible.api.index.SeriesPictureRelevanceServiceI;
import com.bat.flexible.api.index.dto.page.UserSeriesThemePageQry;
import com.bat.flexible.manager.index.executor.SeriesPictureRelevanceCmdExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import com.bat.flexible.dao.index.dataobject.DistributorBannerDO;
import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeDO;
import com.bat.flexible.dao.index.dataobject.SeriesPictureRelevanceDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.index.DistributorSeriesThemeErrorCode;
import com.bat.flexible.manager.index.executor.SeriesPictureRelevanceQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SeriesPictureRelevanceServiceImpl implements SeriesPictureRelevanceServiceI {

    @Autowired
    private SeriesPictureRelevanceCmdExe seriesPictureRelevanceCmdExe;

    @Autowired
    private SeriesPictureRelevanceQryExe seriesPictureRelevanceQryExe;



    @Autowired
    private DistributorSeriesThemeServiceI distributorSeriesThemeServiceI;

    @Autowired
    private DistributorBannerServiceI distributorBannerServiceI;

    @Autowired
    private DistributorSeriesThemeRelevanceServiceI distributorSeriesThemeRelevanceServiceI;

    @Override
    public void save(List<Integer> pictureList, Integer seriesId, boolean isAdd, AdminResponse currentAdmin) {
        if (!isAdd){
            List<SeriesPictureRelevanceDO> editListSeriesPictureRelaList = seriesPictureRelevanceQryExe.listBySeriesId(seriesId);
            if(editListSeriesPictureRelaList !=null && editListSeriesPictureRelaList.size()>0){
                for(int x=0;x<editListSeriesPictureRelaList.size();x++){
                    for(int y=0;y<pictureList.size();y++){
                        if(editListSeriesPictureRelaList.get(x).getPictureId() - pictureList.get(y)==0){
                            //没有删除
                            editListSeriesPictureRelaList.remove(x);
                            pictureList.remove(y);
                            x--;
                            y--;
                            break;
                        }
                    }
                }
                //删除已经被删除的
                if(editListSeriesPictureRelaList !=null && editListSeriesPictureRelaList.size()>0){
                    editListSeriesPictureRelaList.stream().forEach(seriesPictureRela -> {
                        seriesPictureRelevanceCmdExe.delete(seriesPictureRela.getId());
                    });
                }
            }
        }
        if(pictureList !=null && pictureList.size()>0){
           Integer sortNo =1;
           if(!isAdd){
               //编辑查询最大序号
               SeriesPictureRelevanceDO max = seriesPictureRelevanceQryExe.findMaxSortNo(seriesId);
               if(max !=null){
                   sortNo = max.getSortNo()+1;
               }
           }
           for(int x=0;x<pictureList.size();x++){
               SeriesPictureRelevanceDO seriesPictureRelevanceDO = new SeriesPictureRelevanceDO();
               seriesPictureRelevanceDO.setPictureId(pictureList.get(x));
               seriesPictureRelevanceDO.setSeriesId(seriesId);
               seriesPictureRelevanceDO.setSortNo(sortNo+x);
               setAdminMsg(currentAdmin, seriesPictureRelevanceDO);
               seriesPictureRelevanceCmdExe.create(seriesPictureRelevanceDO);
           }
        }
    }


    /**
     * 上移下移
     * @return
     */
    @Override
    @Transactional
    public Response updateSortNo(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin) {
        SeriesPictureRelevanceDO pictureRela = seriesPictureRelevanceQryExe.getById(flexibleUpOrDownDTO.getId());

        //查询受影响的数据（上移影响上一个序号、下移影响下一个数据）
        SeriesPictureRelevanceDO effectPicture = seriesPictureRelevanceQryExe.findEffect(pictureRela.getSortNo(),flexibleUpOrDownDTO.getFlag(),pictureRela.getSeriesId());
        if(effectPicture==null && flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_UP_TOP_ERROR);
        }
        if(effectPicture==null && !flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DOWN_LOWEST_ERROR);
        }
        Integer temp = effectPicture.getSortNo();
        effectPicture.setSortNo(pictureRela.getSortNo());
        pictureRela.setSortNo(temp);
        setAdminMsg(currentAdmin,effectPicture);
        setAdminMsg(currentAdmin,pictureRela);
        seriesPictureRelevanceCmdExe.update(pictureRela);
        seriesPictureRelevanceCmdExe.update(effectPicture);
        DistributorSeriesThemeDO seriesTheme = distributorSeriesThemeServiceI.getById(pictureRela.getSeriesId());
        seriesTheme.setUpdateUserName(currentAdmin.getUserName());
        seriesTheme.setUpdateTime(new Date());
        seriesTheme.setUpdateUserId(currentAdmin.getId());
        distributorSeriesThemeServiceI.updateDO(seriesTheme);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response delete(List<Integer> list) {
        list.stream().forEach(id -> {
            SeriesPictureRelevanceDO pictureRela = seriesPictureRelevanceQryExe.getById(id);

            seriesPictureRelevanceCmdExe.delete(id);
            List<SeriesPictureRelevanceDO> pictureRelaList = seriesPictureRelevanceQryExe.listBySeriesId(pictureRela.getSeriesId());
            if(pictureRelaList ==null || pictureRelaList.size()==0){
                List<DistributorBannerDO> bannerList = distributorBannerServiceI.listBySeriesId(pictureRela.getSeriesId());
                if(bannerList !=null && bannerList.size()>0){
                    throw new FlexibleCustomException(DistributorSeriesThemeErrorCode.INDEX_SERIES_DEL_FAIL_BY_RELA_BANNER);
                }
                distributorSeriesThemeServiceI.delete(pictureRela.getSeriesId());
                distributorSeriesThemeRelevanceServiceI.deleteBySeriesThemeId(pictureRela.getSeriesId());
            }
        });
        return Response.buildSuccess();
    }

    @Override
    public void deleteBySeriesId(Integer seriesId) {
        seriesPictureRelevanceCmdExe.deleteBySeriesId(seriesId);
    }

    @Override
    public List<IndexRecommendRelaCO> listCOByIndexSeriesId(Integer seriesId) {
        return seriesPictureRelevanceQryExe.listCOBySeriesId(seriesId);
    }

    @Override
    public PageInfo<CommonPicturePageCO> pageBySeriesId(UserSeriesThemePageQry userSeriesThemePageQry) {
        PageHelper.startPage(userSeriesThemePageQry.getPage(),userSeriesThemePageQry.getSize());
        List<CommonPicturePageCO> list = seriesPictureRelevanceQryExe.listCommonPictureCOBySeriesId(userSeriesThemePageQry.getSeriesId());
        return new PageInfo<>(list);
    }

    private void setAdminMsg(AdminResponse currentAdmin, SeriesPictureRelevanceDO pictureRela) {
        if(pictureRela.getId() ==null){
            pictureRela.setCreateTime(new Date());
            pictureRela.setCreateUserId(currentAdmin.getId());
            pictureRela.setCreateUserName(currentAdmin.getUserName());
        }
        pictureRela.setUpdateTime(new Date());
        pictureRela.setUpdateUserId(currentAdmin.getId());
        pictureRela.setUpdateUserName(currentAdmin.getUserName());
    }
}
