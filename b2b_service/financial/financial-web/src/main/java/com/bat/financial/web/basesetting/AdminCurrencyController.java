package com.bat.financial.web.basesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.basesetting.CurrencyService;
import com.bat.financial.api.basesetting.dto.CurrencyCreateCmd;
import com.bat.financial.api.basesetting.dto.CurrencyId;
import com.bat.financial.api.basesetting.dto.CurrencyQry;
import com.bat.financial.api.basesetting.dto.CurrencyUpdateCmd;
import com.bat.financial.api.basesetting.dto.data.CurrencyDTO;
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

@Api(tags = "币别后台接口", value = "AdminCurrencyController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/currency")
public class AdminCurrencyController extends BaseController {

    @Resource
    private CurrencyService currencyService;

    // @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminCurrency,value =
    // CommonLogTypeConstantDTO.AdminCurrencyById)
    @GetMapping()
    @ApiOperation(value = "查询币别")
    public Response<CurrencyDTO> getCurrencyById(@Valid CurrencyId id) {
        return Response.of(currencyService.getCurrencyById(id.getId()));
    }

    // @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.AdminCurrency,value =
    // CommonLogTypeConstantDTO.AdminCurrencyList)
    @GetMapping("/list")
    @ApiOperation(value = "查询币别(分页)")
    public Response<PageInfo<CurrencyDTO>> listCurrency(@Valid CurrencyQry qry) {
        return Response.of(currencyService.listCurrency(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCurrency,
        value = CommonLogTypeConstantDTO.AdminCurrencyAdd)
    @PostMapping()
    @ApiOperation(value = "添加币别")
    public Response createCurrency(@RequestBody @Valid CurrencyCreateCmd cmd) {
        currencyService.createCurrency(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCurrency,
        value = CommonLogTypeConstantDTO.AdminCurrencyUpdate)
    @PutMapping()
    @ApiOperation(value = "更新币别")
    public Response updateCurrency(@RequestBody @Valid CurrencyUpdateCmd cmd) {
        currencyService.updateCurrency(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCurrency,
        value = CommonLogTypeConstantDTO.AdminCurrencyDeleteById)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除币别")
    public Response deleteCurrency(@RequestBody @Valid CurrencyId id) {
        currencyService.deleteCurrencyById(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCurrency,
        value = CommonLogTypeConstantDTO.AdminCurrencySync)
    @PutMapping("/po/sync")
    @ApiOperation(value = "同步币别")
    public Response syncCurrency() {
        return Response.of(currencyService.syncCurrency());
    }
}
