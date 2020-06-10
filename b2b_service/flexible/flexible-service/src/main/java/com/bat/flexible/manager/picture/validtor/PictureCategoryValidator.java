package com.bat.flexible.manager.picture.validtor;


import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.picture.dto.PictureCategoryCmd;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.picture.PictureCategoryErrorCode;
import com.bat.flexible.manager.picture.executor.PictureCategoryQryExe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 图片分类
 */
@Component
public class PictureCategoryValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureCategoryValidator.class);

    @Autowired
    private PictureCategoryQryExe pictureCategoryQryExe;

    @Autowired
    private PictureServiceI pictureServiceI;
    /**
     * 校验分类禁用、下面是否还有未禁用的子分类、该分类是否有未禁用的图片
     * @param pictureCategoryDO
     * @param openFlag
     */
    public void checkWhenDisable(PictureCategoryDO pictureCategoryDO, Short openFlag) {
        if(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES.equals(openFlag)){
            return;
        }
        //判断禁用
        List<PictureCategoryDO> sonList = pictureCategoryQryExe.listByParentIdAndOpenFlag(pictureCategoryDO.getId(), FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
        if(sonList !=null && sonList.size()>0){
            throw new FlexibleCustomException(PictureCategoryErrorCode.P_CATEGORY_DISABLE_SON_FIRST);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_NO.equals(pictureCategoryDO.getAtLastTrademark())){
            return;
        }
        List<PictureDO> pictureDOList = pictureServiceI.listByCategoryIdAndOpenFlag(pictureCategoryDO.getId(),FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
        if(pictureDOList !=null && pictureDOList.size()>0){
            throw new FlexibleCustomException(PictureCategoryErrorCode.P_CATEGORY_DISABLE_PICTURE_RELEVANCE);
        }
    }

    /**
     * 校验删除
     * @param pictureCategoryDO
     */
    public void checkWhenDelete(PictureCategoryDO pictureCategoryDO) {
        List<PictureCategoryDO> pictureCategoryDOList = pictureCategoryQryExe.listByParentIdAndOpenFlag(pictureCategoryDO.getId(),null);
        if(pictureCategoryDOList !=null && pictureCategoryDOList.size()>0){
            throw new FlexibleCustomException(PictureCategoryErrorCode.P_CATEGORY_DELETE_SON_FIRST);
        }
        //判断这个分类有没有关联图片
        List<PictureDO>  pictureDOList = pictureServiceI.listByCategoryIdAndOpenFlag(pictureCategoryDO.getId(),null);
        if(pictureDOList !=null && pictureDOList.size()>0){
            throw new FlexibleCustomException(PictureCategoryErrorCode.P_CATEGORY_DELETE_PICTURE_RELEVANCE);
        }
    }

    /**
     * 校验参数、返回父父分类
     * @param pictureCategoryCmd
     * @param isAdd
     * @return
     */
    public PictureCategoryDO checkParam(PictureCategoryCmd pictureCategoryCmd, Boolean isAdd) {
        if(isAdd && pictureCategoryCmd.getId() !=null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_MUST_BE_NULL_WHEN_ADD);
        }
        PictureCategoryDO categoryDO =null;
        if(pictureCategoryCmd.getParentId()>0){
            try {
                categoryDO= pictureCategoryQryExe.getById(pictureCategoryCmd.getParentId());
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("判断图片分类父分类是否可用异常:"+pictureCategoryCmd.getParentId(),e);
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_PARENT_ID_ERROR);
            }
            if(categoryDO.getType() - pictureCategoryCmd.getType() !=0){
                throw new FlexibleCustomException(PictureCategoryErrorCode.P_CATEGORY_TYPE_DIFFERENT_FROM_WITH_PARENT);
            }
        }
        //判断修改时、是否为最底级的分类
        if(pictureCategoryCmd.getId() !=null && FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(pictureCategoryCmd.getAtLastTrademark())){
            List<PictureCategoryDO> categoryDOList = pictureCategoryQryExe.listByParentIdAndOpenFlag(pictureCategoryCmd.getId(),null);
            if(categoryDOList !=null && categoryDOList.size()>0){
                //还有子类
                LOGGER.info(MessageUtils.get(PictureCategoryErrorCode.P_CATEGORY_NOT_BELONG_TO_LOWEST) +"{}",pictureCategoryCmd.getId());
                throw new FlexibleCustomException(PictureCategoryErrorCode.P_CATEGORY_NOT_BELONG_TO_LOWEST);
            }
        }
        return categoryDO;
    }

    /**
     * 判断是否最终分类
     * @param pictureCategoryCmd
     * @param pictureCategoryDO
     */
    public void checkAtLastTrademark(PictureCategoryCmd pictureCategoryCmd, PictureCategoryDO pictureCategoryDO) {
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(pictureCategoryDO.getAtLastTrademark()) &&
                FlexibleCommonConstant.AT_LAST_TRADEMARK_NO.equals(pictureCategoryCmd.getAtLastTrademark())){
            //之前是最终分类、修改为否、需要判断原来分类下是否有图片
            List<PictureDO> pictureDOList = pictureServiceI.listByCategoryIdAndOpenFlag(pictureCategoryDO.getId(),null);
            if(pictureDOList !=null && pictureDOList.size()>0){
                throw new FlexibleCustomException(PictureCategoryErrorCode.P_CATEGORY_UPDATE_AT_LAST_NO_FAIL_PICTURE_RELA);
            }
        }
    }
}
