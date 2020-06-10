package com.bat.flexible.web.picture;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.picture.PictureCategoryServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/flexible/v1/web/user/u/pictureCategory")
@Api(tags = "图片分类前台接口")
public class UserPictureCategoryController {

    @Autowired
    private PictureCategoryServiceI pictureCategoryServiceI;

    /**
     * 图库详情(获取贴图栏目)
     * @return
     */
    @GetMapping(value = "/detail/tab/mapp/list")
    @ApiOperation(value = "图库详情(获取贴图栏目)")
    public Response detailTabMappList(ThemeDTO themeDTO) {
        return pictureCategoryServiceI.detailTabMappList(themeDTO);
    }
}
