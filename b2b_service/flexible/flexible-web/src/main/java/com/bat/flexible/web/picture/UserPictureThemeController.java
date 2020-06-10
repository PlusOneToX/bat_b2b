package com.bat.flexible.web.picture;


import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.picture.PictureThemeServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/flexible/v1/web/user/pictureTheme")
@Api(tags = "官方主题前台接口")
public class UserPictureThemeController {

    @Autowired
    private PictureThemeServiceI pictureThemeServiceI;

    /**
     * 获取图片分类主题
     * @return
     */
    @GetMapping(value = "/picture/category/theme/list")
    @ApiOperation(value = "获取图片分类主题")
    public Response pictureCategoryThemeList(ThemeDTO themeDTO) {
        return pictureThemeServiceI.pictureCategoryThemeList(themeDTO);
    }

    /**
     * 获取图片分类主题详情
     * @return
     */
    @GetMapping(value = "/picture/category/theme/detail")
    @ApiOperation(value = "获取图片分类主题详情")
    public Response pictureCategoryThemeDetail(ThemeDTO themeDTO) {
        return pictureThemeServiceI.pictureCategoryThemeDetail(themeDTO);
    }
}
