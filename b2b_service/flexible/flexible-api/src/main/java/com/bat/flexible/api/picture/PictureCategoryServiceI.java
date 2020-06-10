package com.bat.flexible.api.picture;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.picture.dto.page.PictureCategoryPageQry;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.picture.dto.PictureCategoryCmd;
import com.bat.flexible.api.picture.dto.PictureCategoryDetailQry;
import com.bat.flexible.dao.picture.co.PictureCategoryPageCO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;

import java.util.List;


public interface PictureCategoryServiceI {
    /**
     * 新增图片分类
     * @param pictureCategoryCmd
     * @param currentAdmin
     * @return
     */
    Response create(PictureCategoryCmd pictureCategoryCmd, AdminResponse currentAdmin);

    /**
     * 修改图片分类
     * @param pictureCategoryCmd
     * @param currentAdmin
     * @return
     */
    Response update(PictureCategoryCmd pictureCategoryCmd, AdminResponse currentAdmin);

    Response<PictureCategoryDetailQry> detailById(Integer id);

    /**
     * 分页查询图片分类
     * @param categoryPageQry
     * @return
     */
    Response<PageInfo<PictureCategoryPageCO>> page(PictureCategoryPageQry categoryPageQry);

    /**
     * 上下移动
     * @param flexibleUpOrDownDTO
     * @param currentAdmin
     * @return
     */
    Response upOrDown(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin);

    /**
     * 启用禁用
     * @param flexibleUpdateStatusDTO
     * @param currentAdmin
     * @return
     */
    Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin);

    /**
     * 删除
     * @param id
     * @param currentAdmin
     * @return
     */
    Response deleteById(Integer id, AdminResponse currentAdmin);

    PictureCategoryDO getById(Integer categoryId);

    List<PictureCategoryDO> listByParentIdAndOpenFlag(Integer parentId,Short openFlag);

    List<PictureCategoryDO> listByPictureThemeId(Integer themeId);

    Response detailTabMappList(ThemeDTO themeDTO);

    /**
     * 根据id列表查询
     * @param categoryIds
     * @return
     */
    List<PictureCategoryDO> listByIdList(List<Integer> categoryIds);


    List<PictureCategoryDO> listByCondition(Short openFlag);

    /**
     * 根据一级分类找到二级分类
     * @param categoryId
     * @return
     */
    List<PictureCategoryPageCO> detailPictureCategory(Integer categoryId);
}
