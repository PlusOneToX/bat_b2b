package com.bat.financial.web.basesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.basesetting.CurrencyRateService;
import com.bat.financial.api.basesetting.dto.CurrencyRateListQry;
import com.bat.financial.api.basesetting.dto.CurrencyRateQry;
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

@Api(tags = "汇率设置前台接口", value = "UserCurrencyRateController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/user/currencyRate")
public class UserCurrencyRateController extends BaseController {

    @Resource
    private CurrencyRateService currencyRateService;

    @GetMapping()
    @ApiOperation(value = "查询汇率")
    public Response<CurrencyRateDTO> getCurrencyRateById(@Valid CurrencyRateQry qry) {
        return Response.of(currencyRateService.getCurrencyRate(qry));
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询汇率(分页)")
    public Response<PageInfo<CurrencyRateDTO>> listCurrencyRate(@Valid CurrencyRateListQry qry) {
        return Response.of(currencyRateService.listCurrencyRate(qry));
    }

}
