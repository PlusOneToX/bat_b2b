package com.bat.flexible.manager.picture;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.picture.PictureCategoryServiceI;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.picture.PictureThemeServiceI;
import com.bat.flexible.api.picture.dto.theme.PictureCategoryThemeDTO;
import com.bat.flexible.api.picture.dto.theme.PictureThemeDTO;
import com.bat.flexible.api.picture.dto.theme.PictureThemePageQry;
import com.bat.flexible.api.picture.dto.theme.PictureThemeRelaRequest;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.picture.executor.PictureCategoryThemeRelevanceCmdExe;
import com.bat.flexible.manager.picture.executor.PictureCategoryThemeRelevanceQryExe;
import com.bat.flexible.manager.picture.executor.PictureThemeCmdExe;
import com.bat.flexible.manager.picture.executor.PictureThemeQryExe;
import com.bat.flexible.manager.picture.validtor.PictureThemeValidator;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryThemeRelevanceDO;
import com.bat.flexible.dao.picture.dataobject.PictureThemeDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 沙漠
 */
@Service
@Scope(value = "singleton")
public class PictureThemeServiceImpl implements PictureThemeServiceI {

    @Autowired
    private PictureThemeCmdExe pictureThemeCmdExe;

    @Autowired
    private PictureThemeQryExe pictureThemeQryExe;

    @Autowired
    private PictureCategoryThemeRelevanceCmdExe pictureCategoryThemeRelevanceCmdExe;

    @Autowired
    private PictureCategoryThemeRelevanceQryExe pictureCategoryThemeRelevanceQryExe;



    @Autowired
    private PictureCategoryServiceI pictureCategoryServiceI;

    @Autowired
    private PictureServiceI pictureServiceI;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @Override
    public PictureThemeDO create(PictureThemeDO pictureTheme) {
        PictureThemeValidator.validate(pictureTheme);
        pictureTheme.setOpenFlag(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
        pictureTheme.setCreateTime(new Date());
        return pictureThemeCmdExe.create(pictureTheme);
    }

    @Override
    public PageInfo<PictureThemeDO> page(PictureThemePageQry pictureThemePageQry) {
        PageHelper.startPage(pictureThemePageQry.getPage(),pictureThemePageQry.getSize());
        List<PictureThemeDO> list = pictureThemeQryExe.listByOpenFlagAndContent(pictureThemePageQry.getOpenFlag(),pictureThemePageQry.getContent());
        return new PageInfo(list);
    }

    @Override
    public PictureThemeDTO getById(Integer id) {
        PictureThemeDTO pictureThemeResponse = new PictureThemeDTO();
        PictureThemeDO pictureTheme = pictureThemeQryExe.getById(id);
        if (pictureTheme == null) {
            return pictureThemeResponse;
        }
        pictureThemeResponse.setPictureTheme(pictureTheme);
        List<PictureCategoryDO> pictureCategorys = pictureCategoryServiceI.listByPictureThemeId(id);
        pictureThemeResponse.setPictureCategorys(pictureCategorys);
        return pictureThemeResponse;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Response relation(PictureThemeRelaRequest pictureThemeRelaRequest) {
        if (pictureThemeRelaRequest.getId() == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        pictureCategoryThemeRelevanceCmdExe.deleteById(pictureThemeRelaRequest.getId());
        pictureCategoryThemeRelevanceCmdExe.saveList(pictureThemeRelaRequest.getPictureCategoryThemeRelas());
        return Response.buildSuccess();
    }

    @Override
    public PictureThemeDO update(PictureThemeDO pictureTheme) {
        PictureThemeValidator.validate(pictureTheme);
        PictureThemeDO obj = pictureThemeQryExe.getById(pictureTheme.getId());
        pictureTheme.setCreateTime(obj.getCreateTime());
        return pictureThemeCmdExe.update(pictureTheme);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Response delete(Integer id) {
        PictureThemeDO pictureTheme = pictureThemeQryExe.getById(id);
        if (FlexibleCommonConstant.COMMON_OPEN_FLAG_YES.equals(pictureTheme.getOpenFlag())) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DELETE_AFTER_DISABLE_ERROR);
        }
        pictureThemeCmdExe.delete(id);
        pictureCategoryThemeRelevanceCmdExe.deleteById(id);
        return Response.buildSuccess();
    }

    @Override
    public Response pictureCategoryThemeList(ThemeDTO themeDTO) {
        themeDTO.setDistributorIds(flexibleDistributorQryExe.getDistributorTreePaths(themeDTO.getDistributorId()));
        //校验参数
        PictureThemeValidator.validaPictureCategoryThemeQuery(themeDTO,false);
        //找出该分销商拥有的主题分类
        List<PictureCategoryThemeRelevanceDO> pictureCategoryThemeRelas = pictureCategoryThemeRelevanceQryExe.listByDistributorIdsAndCategoryType(themeDTO.getDistributorIds(),
                themeDTO.getType());
        if (pictureCategoryThemeRelas.size() == 0) {
            return Response.buildSuccess();
        }
        List<Integer> categoryIds = pictureCategoryThemeRelas.stream().map(a -> a.getCategoryId().intValue()).collect(Collectors.toList());
        List<PictureCategoryDO> pictureCategorys = pictureCategoryServiceI.listByIdList(categoryIds);
        if (pictureCategorys.size() == 0) {
            return Response.buildSuccess();
        }
        Map<Integer, PictureCategoryDO> categoryMap = pictureCategorys.stream().collect(Collectors.toMap(PictureCategoryDO::getId, a -> a, (k1, k2) -> k1));
        List<PictureCategoryThemeDTO> pictureCategoryThemeDTOList = new ArrayList<>();
        for (PictureCategoryThemeRelevanceDO pictureCategoryThemeRelevanceDO : pictureCategoryThemeRelas) {
            PictureCategoryThemeDTO categoryThemeDTO = new PictureCategoryThemeDTO();
            themeDTO.setCategoryId(pictureCategoryThemeRelevanceDO.getCategoryId());
            //PageInfo pageInfo = pictureDataManager.findCategoryThemeData(themeDTO);
            PageInfo<CommonPicturePageCO> pageInfo = pictureServiceI.detailPictureList(themeDTO);

            categoryThemeDTO.setPageInfo(pageInfo);
            PictureCategoryDO pictureCategoryDO = categoryMap.get(pictureCategoryThemeRelevanceDO.getCategoryId());
            //添加类别名称
            if (pictureCategoryDO != null) {
                categoryThemeDTO.setThemeName(pictureCategoryDO.getName());
                categoryThemeDTO.setThemeImg(pictureCategoryDO.getImage());
                categoryThemeDTO.setCategoryId(pictureCategoryDO.getId());
                categoryThemeDTO.setType(pictureCategoryDO.getType());
            }
            //存在数据则加入返回列表
            if (pageInfo.getList() != null && pageInfo.getList().size() > 0) {
                pictureCategoryThemeDTOList.add(categoryThemeDTO);
            }
        }
        return Response.of(pictureCategoryThemeDTOList);
    }

    @Override
    public Response pictureCategoryThemeDetail(ThemeDTO themeDTO) {
        PictureThemeValidator.validaPictureCategoryThemeQuery(themeDTO,true);
        PictureCategoryDO pictureCategoryDO = pictureCategoryServiceI.getById(themeDTO.getCategoryId());
        PictureCategoryThemeDTO categoryThemeDTO = new PictureCategoryThemeDTO();
        PageInfo<CommonPicturePageCO> pageInfo = pictureServiceI.detailPictureList(themeDTO);
      //  PageInfo pageInfo = pictureDataManager.findCategoryThemeData(themeDTO);

        categoryThemeDTO.setPageInfo(pageInfo);
        //添加类别名称
        categoryThemeDTO.setThemeName(pictureCategoryDO.getName());
        categoryThemeDTO.setThemeImg(pictureCategoryDO.getImage());
        return Response.of(categoryThemeDTO);

    }
}
