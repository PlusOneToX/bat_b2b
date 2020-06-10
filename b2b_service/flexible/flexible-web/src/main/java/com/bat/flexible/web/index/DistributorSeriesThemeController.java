package com.bat.flexible.web.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.DistributorSeriesThemeServiceI;
import com.bat.flexible.api.index.dto.page.SeriesThemePageQry;
import com.bat.flexible.api.index.dto.series.DistributorSeriesThemeDTO;
import com.bat.flexible.api.index.dto.series.DistributorSeriesThemeQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.dao.index.co.SeriesThemePageCO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 分销商主题系列控制器
 */
@RestController
@RequestMapping("/flexible/v1/web/admin/u/p/distributorSeriesTheme")
@Api(tags = "主题系列展示后台接口")
public class DistributorSeriesThemeController extends BaseController {

    @Autowired
    private DistributorSeriesThemeServiceI distributorSeriesThemeServiceI;


    /**
     * 后台分页
     * @param seriesThemePageRequest
     * @return
     */
    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<SeriesThemePageCO>> page(SeriesThemePageQry seriesThemePageRequest){
        PageInfo<SeriesThemePageCO> pageInfo = distributorSeriesThemeServiceI.page(seriesThemePageRequest);
        return Response.of(pageInfo);
    }

    /**
     * 新增接口
     * @param distributorSeriesThemeDTO
     * @param result
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.DistributorSeriesTheme, value = CommonLogTypeConstantDTO.DistributorSeriesThemeAdd)
    @PostMapping
    @ApiOperation(value = "新增")
    public Response create(@Valid @RequestBody DistributorSeriesThemeDTO distributorSeriesThemeDTO, BindingResult result){

        distributorSeriesThemeDTO.setId(null);
        return distributorSeriesThemeServiceI.create(distributorSeriesThemeDTO,getCurrentAdmin());
    }

    /**
     * 编辑接口
     * @param distributorSeriesThemeDTO
     * @param result
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.DistributorSeriesTheme, value = CommonLogTypeConstantDTO.DistributorSeriesThemeUpdate)
    @PutMapping
    @ApiOperation(value = "编辑")
    public Response update(@Valid @RequestBody DistributorSeriesThemeDTO distributorSeriesThemeDTO, BindingResult result){

        return distributorSeriesThemeServiceI.update(distributorSeriesThemeDTO,getCurrentAdmin());
    }

    /**
     * 查询详情
     * @return
     */
    @GetMapping(value = "/detail")
    @ApiOperation(value = "查询详情")
    public Response detail(@Valid FlexibleIdDTO flexibleIdDTO){

        return distributorSeriesThemeServiceI.detail(flexibleIdDTO.getId());
    }
    /**
     * 查询主题列表
     * @param distributorSeriesThemeRequest
     * @return
     */
    @GetMapping(value = "/listSimpleByCondition")
    @ApiOperation(value = "查询主题列表")
    public Response listSimpleByCondition(DistributorSeriesThemeQry distributorSeriesThemeRequest){
        return distributorSeriesThemeServiceI.listSimpleByCondition(distributorSeriesThemeRequest);
    }

    /**
     * 列表查询、不分页
     * @param seriesThemePageRequest
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "列表查询、不分页")
    public Response list(SeriesThemePageQry seriesThemePageRequest){
        return distributorSeriesThemeServiceI.list(seriesThemePageRequest);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.DistributorSeriesTheme, value = CommonLogTypeConstantDTO.DistributorSeriesThemeDelete)
    @DeleteMapping
    @ApiOperation(value = "删除")
    public Response delete(@Valid FlexibleIdDTO flexibleIdDTO){

        return distributorSeriesThemeServiceI.delete(flexibleIdDTO.getId());
    }
}
