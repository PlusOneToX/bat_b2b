package com.bat.flexible.api.picture;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.picture.dto.*;
import com.bat.flexible.api.picture.dto.page.DistributorPictureQry;
import com.bat.flexible.api.picture.dto.page.PicturePageQry;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.picture.dto.*;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.dao.picture.co.DistributorPictureCO;
import com.bat.flexible.dao.picture.co.PictureCategorySimpleTreeCO;
import com.bat.flexible.dao.picture.co.PicturePageCO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;

import java.util.List;

public interface PictureServiceI {
    /**
     * 新增图片
     * @param pictureCmd
     * @param adminResponse
     * @return
     */
    Response create(PictureCmd pictureCmd, AdminResponse adminResponse);

    /**
     * 编辑图片
     * @param pictureCmd
     * @param currentAdmin
     * @return
     */
    Response update(PictureCmd pictureCmd, AdminResponse currentAdmin);

    /**
     * 查看图片详情
     * @param id
     * @return
     */
    Response<PictureQry> detailById(Integer id);

    List<PictureDO> listByPictureIdList(List<Integer> pictureIdList);

    /**
     * 根据图片分类Id查询图片列表
     * @param categoryId
     * @param openFlag
     * @return
     */
    List<PictureDO> listByCategoryIdAndOpenFlag(Integer categoryId,Short openFlag);

    /**
     * 上移下移
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

    /**
     * 置顶
     * @param id
     * @param currentAdmin
     * @return
     */
    Response top(Integer id, AdminResponse currentAdmin);

    PictureDO getById(Integer pictureId);

    /**
     * 分页
     * @param picturePageQry
     * @return
     */
    PageInfo<PicturePageCO> page(PicturePageQry picturePageQry);

    List<PictureCategorySimpleTreeCO> tree(PictureTreeQry pictureTreeQry);

    void checkDistributorPicture(List<Integer> distributorIdList, List<Integer> pictureIdList);

    /**
     * 图库详情根据分类id等信息找到图片
      * @param themeDTO
     * @return
     */
    PageInfo<CommonPicturePageCO> detailPictureList(ThemeDTO themeDTO);

    /**
     * 根据分销商查询图片信息
     * @param distributorPictureQry
     * @return
     */
    PageInfo<DistributorPictureCO> pagePictureByDistributor(DistributorPictureQry distributorPictureQry);

    /**
     * 根据id列表查询主题列表
     * @param idList
     * @return
     */
    List<PictureThemeUrlDTO> listThemeUrlByIdList(List<Integer> idList);

    /**
     * 根据类型和状态查询
     * @param openFlag
     * @param type
     * @return
     */
    List<PictureDO> listByCondition(Short openFlag, Short type);

    /**
     * 根据图片编码查询
     * @param code
     * @return
     */
    PictureDO getByCode(String code);

    /**
     * 根据图片id获取
     * @param id
     * @return
     */
    PictureDTO detailPictureFindOne(Integer id);
}
