package com.bat.goods.web.scaleprice;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.goods.api.scaleprice.ScalePriceServiceI;
import com.bat.goods.api.scaleprice.dto.ScalePriceCmd;
import com.bat.goods.api.scaleprice.dto.ScalePriceId;
import com.bat.goods.api.scaleprice.dto.ScalePriceListQry;
import com.bat.goods.api.scaleprice.dto.ScalePriceOpenCmd;
import com.bat.goods.api.scaleprice.dto.data.ScalePriceDTO;
import com.bat.goods.web.base.BaseController;
import com.bat.goods.web.base.Response;
import com.bat.goods.web.annotation.SysLog;
import com.bat.goods.web.constants.CommonLogTypeConstantDTO;
import com.bat.goods.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "价格等级后台接口", value = "AdminScalePriceController")
@RestController
@RequestMapping("/goods/v1/web/admin/scaleprice")
public class AdminScalePriceController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminScalePriceController.class);

    @Resource
    private ScalePriceServiceI scalePriceService;

    @ApiOperation(value = "根据搜索条件查找价格等级列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<ScalePriceDTO>> listScalePrice(@Valid ScalePriceListQry qry) {
        PageInfo<ScalePriceDTO> pageInfo = scalePriceService.listScalePrice(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找价格等级列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<ScalePriceDTO>> listScalePricePo(@Valid ScalePriceListQry qry) {
        PageInfo<ScalePriceDTO> pageInfo = scalePriceService.listScalePrice(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据价格等级id获取价格等级详情")
    @GetMapping()
    public Response<ScalePriceDTO> getScalePrice(@Valid ScalePriceId qry) {
        ScalePriceDTO dto = scalePriceService.getScalePrice(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminScalePrice, value = CommonLogTypeConstantDTO.AdminScalePriceAdd)
    @ApiOperation(value = "添加价格等级")
    @PostMapping()
    public Response createScalePrice(@RequestBody @Valid ScalePriceCmd cmd) {
        scalePriceService.createScalePrice(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminScalePrice, value = CommonLogTypeConstantDTO.AdminScalePriceUpdate)
    @ApiOperation(value = "更新价格等级")
    @PutMapping()
    public Response updateScalePrice(@RequestBody @Valid ScalePriceCmd cmd) {
        scalePriceService.updateScalePrice(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminScalePrice, value = CommonLogTypeConstantDTO.AdminScalePriceUpdateStatus)
    @ApiOperation(value = "更新价格等级状态")
    @PutMapping(value = "/open")
    public Response openScalePrice(@RequestBody @Valid ScalePriceOpenCmd cmd) {
        scalePriceService.openScalePrice(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminScalePrice, value = CommonLogTypeConstantDTO.AdminScalePriceDelete)
    @ApiOperation(value = "根据价格等级id删除价格等级")
    @DeleteMapping()
    public Response deleteScalePrice(@RequestBody @Valid ScalePriceId cmd) {
        scalePriceService.deleteScalePrice(cmd);
        return Response.buildSuccess();
    }

}
