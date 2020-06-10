package com.bat.flexible.web.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.DistributorSeriesThemeServiceI;
import com.bat.flexible.api.index.SeriesPictureRelevanceServiceI;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.index.dto.page.UserSeriesThemePageQry;
import com.bat.flexible.api.index.dto.series.DistributorSeriesQry;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * 分销商banner移动端控制器
 */
@RestController
@RequestMapping("/flexible/v1/web/user/distributorSeriesTheme")
@Api(tags = "用户端分销商首页系列接口")
public class UserDistributorSeriesThemeController extends BaseController {

    @Autowired
    private DistributorSeriesThemeServiceI distributorSeriesThemeServiceI;

    @Autowired
    private SeriesPictureRelevanceServiceI seriesPictureRelevanceServiceI;

    @GetMapping(value = "/listByDistributorId")
    @ApiOperation(value = "查询分销商首页系列列表")
    public Response listByDistributorId(@Valid DistributorSeriesQry distributorSeriesQry){
        return distributorSeriesThemeServiceI.listByDistributorId(distributorSeriesQry.getDistributorId(),distributorSeriesQry.getSeriesNum(),
                distributorSeriesQry.getPictureNum());
    }

    /**
     * tab栏目列表
     * @return
     */
    @GetMapping(value = "/tab/list")
    @ApiOperation(value = "tab栏目列表")
    public Response tabList(ThemeDTO themeDTO) {
        return distributorSeriesThemeServiceI.tabList(themeDTO);
    }
    /**
     * 根据主题等信息找到图片
     * @return
     */
    @GetMapping(value = "/picture/list")
    @ApiOperation(value = "根据主题等信息找到图片")
    public Response pictureList(ThemeDTO themeDTO) {
        PageInfo pageInfo = distributorSeriesThemeServiceI.pictureList(themeDTO);
        return Response.of(pageInfo);
    }
    /**
     * 图库详情tab栏目列表
     * @return
     */
    @GetMapping(value = "/detail/tab/list")
    @ApiOperation(value = "图库详情tab栏目列表")
    public Response detailTabList(ThemeDTO themeDTO) {
        return distributorSeriesThemeServiceI.detailTabList(themeDTO);
    }







    @GetMapping(value = "/pageBySeriesId")
    @ApiOperation(value = "根据系列id分页查询")
    public Response<PageInfo<CommonPicturePageCO>> pageBySeriesId(@Valid UserSeriesThemePageQry userSeriesThemePageQry){
        PageInfo<CommonPicturePageCO> pageInfo = seriesPictureRelevanceServiceI.pageBySeriesId(userSeriesThemePageQry);
        return Response.of(pageInfo);
    }





}
