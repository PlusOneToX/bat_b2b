package com.bat.flexible.web.picture;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleIdListDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.picture.PictureCategoryServiceI;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.picture.dto.PictureDTO;
import com.bat.flexible.api.picture.dto.PictureThemeUrlDTO;
import com.bat.flexible.api.picture.dto.PictureTreeQry;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.dao.picture.co.PictureCategoryPageCO;
import com.bat.flexible.dao.picture.co.PictureCategorySimpleTreeCO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/flexible/v1/web/user/u/picture")
@Api(tags = "图片前台接口")
public class UserPictureController {

    @Autowired
    private PictureServiceI pictureServiceI;

    @Autowired
    private PictureCategoryServiceI pictureCategoryServiceI;


    @GetMapping(value ="/tree")
    @ApiOperation(value = "条件查询图片树")
    public Response<List<PictureCategorySimpleTreeCO>> tree(PictureTreeQry pictureTreeQry){
        System.out.println(pictureTreeQry);
        List<PictureCategorySimpleTreeCO> list = pictureServiceI.tree(pictureTreeQry);
        return Response.of(list);
    }

    @GetMapping(value = "/detail/picture/category")
    @ApiOperation(value = "图库详情根据分类id等信息找到图片二级分类")
    public Response<List<PictureCategoryPageCO>> detailPictureCategory(ThemeDTO themeDTO) {
        List<PictureCategoryPageCO> pictureCategorys = pictureCategoryServiceI.detailPictureCategory(themeDTO.getCategoryId());
        return Response.of(pictureCategorys);
    }

    /**
     * 图库详情根据分类id等信息找到图片
     * @return
     */
    @GetMapping(value = "/detail/picture/list")
    @ApiOperation(value = "图库详情根据分类id等信息找到图片(分页)")
    public Response<PageInfo<CommonPicturePageCO>>  detailPictureList(ThemeDTO themeDTO) {
        PageInfo<CommonPicturePageCO> pageInfo = pictureServiceI.detailPictureList(themeDTO);
        return Response.of(pageInfo);
    }


    @GetMapping(value = "/detail/picture/find/one")
    @ApiOperation(value = "根据图片id找到图片")
    public Response<PictureDTO> detailPictureFindOne(@Valid FlexibleIdDTO flexibleIdDTO) {
        PictureDTO pictureDTO = pictureServiceI.detailPictureFindOne(flexibleIdDTO.getId());
        return Response.of(pictureDTO);
    }

    /**
     * 根据id列表查询主题列表
     * @return
     */
    @GetMapping(value = "/listSanxingThemeUrl")
    @ApiOperation(value = "根据id列表查询主题列表")
    public Response<List<PictureThemeUrlDTO>>  listSanxingThemeUrl(@Valid FlexibleIdListDTO flexibleIdListDTO) {
        List<PictureThemeUrlDTO> list = pictureServiceI.listThemeUrlByIdList(flexibleIdListDTO.getIdList());
        return Response.of(list);
    }

}
