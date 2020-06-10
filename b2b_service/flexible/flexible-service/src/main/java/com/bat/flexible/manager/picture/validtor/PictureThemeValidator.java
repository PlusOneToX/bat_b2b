package com.bat.flexible.manager.picture.validtor;

import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.picture.dataobject.PictureThemeDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.picture.PictureThemeErrorCode;
import org.apache.commons.lang3.StringUtils;

public class PictureThemeValidator {

    public static final void validate(PictureThemeDO pictureThemeDO){
        if(pictureThemeDO.getDistributorId() ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL);
        }
        if(StringUtils.isBlank(pictureThemeDO.getDistributorName())){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_NAME_NULL);
        }
        if(StringUtils.isBlank(pictureThemeDO.getName())){
            throw new FlexibleCustomException(PictureThemeErrorCode.P_THEME_NAME_NULL);
        }
    }

    /**
     * 校验查询图片分类和官方主题
     * @param themeDTO
     */
    public static void validaPictureCategoryThemeQuery(ThemeDTO themeDTO, Boolean checkCategoryId){
        if(themeDTO.getDistributorId() ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL);
        }
        if(themeDTO.getPage() ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_PAGE_PAGE_NULL);
        }
        if(themeDTO.getSize() ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_PAGE_SIZE_NULL);
        }
        if(checkCategoryId !=null && checkCategoryId && themeDTO.getCategoryId() ==null){
            throw new FlexibleCustomException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_PICTURE_CATEGORY)
            +MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL));
        }
    }
}
