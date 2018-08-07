package com.bat.financial.web.basesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.basesetting.CurrencyRateService;
import com.bat.financial.api.basesetting.dto.CurrencyRateCreateCmd;
import com.bat.financial.api.basesetting.dto.CurrencyRateId;
import com.bat.financial.api.basesetting.dto.CurrencyRateListQry;
import com.bat.financial.api.basesetting.dto.CurrencyRateUpdateCmd;
import com.bat.financial.api.basesetting.dto.data.CurrencyRateDTO;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description:
 * @date: 2018/4/28 11:08
 */

@Api(tags = "汇率设置后台接口", value = "AdminCurrencyRateController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/currencyRate")
public class AdminCurrencyRateController extends BaseController {

    @Resource
    private CurrencyRateService currencyRateService;

    // @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminCurrencyRate,value =
    // CommonLogTypeConstantDTO.AdminCurrencyRateById)
    @GetMapping()
    @ApiOperation(value = "查询汇率")
    public Response<CurrencyRateDTO> getCurrencyRateById(@Valid CurrencyRateId id) {
        return Response.of(currencyRateService.getCurrencyRateById(id.getId()));
    }

    // @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminCurrencyRate,value =
    // CommonLogTypeConstantDTO.AdminCurrencyRateList)
    @GetMapping("/list")
    @ApiOperation(value = "查询汇率(分页)")
    public Response<PageInfo<CurrencyRateDTO>> listCurrencyRate(@Valid CurrencyRateListQry qry) {
        return Response.of(currencyRateService.listCurrencyRate(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCurrencyRate,
        value = CommonLogTypeConstantDTO.AdminCurrencyRateAdd)
    @PostMapping()
    @ApiOperation(value = "添加汇率")
    public Response createCurrencyRate(@RequestBody @Valid CurrencyRateCreateCmd cmd) {
        currencyRateService.createCurrencyRate(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCurrencyRate,
        value = CommonLogTypeConstantDTO.AdminCurrencyRateUpdate)
    @PutMapping()
    @ApiOperation(value = "更新汇率")
    public Response updateCurrencyRate(@RequestBody @Valid CurrencyRateUpdateCmd cmd) {
        currencyRateService.updateCurrencyRate(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCurrencyRate,
        value = CommonLogTypeConstantDTO.AdminCurrencyRateDeleteById)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除汇率")
    public Response deleteCurrencyRate(@RequestBody @Valid CurrencyRateId id) {
        currencyRateService.deleteCurrencyRateById(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCurrencyRate,
        value = CommonLogTypeConstantDTO.AdminCurrencyRateSync)
    @PutMapping("/po/sync")
    @ApiOperation(value = "从ERP同步汇率")
    public Response syncCurrencyRate() {
        return Response.of(currencyRateService.syncCurrencyRate());
    }

}
