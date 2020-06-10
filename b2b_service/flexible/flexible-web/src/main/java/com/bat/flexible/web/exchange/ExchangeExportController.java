package com.bat.flexible.web.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.exchange.ExchangeExportServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeExportCmd;
import com.bat.flexible.api.exchange.dto.ExchangeExportPageQry;
import com.bat.flexible.dao.exchange.co.ExchangeExportPageCO;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 兑换卡营销专题
 */
@RestController
@RequestMapping("/flexible/v1/web/admin/u/p/exchangeExport")
@Api(tags = "兑换卡导出后台管理后台接口")
public class ExchangeExportController extends BaseController {

    @Autowired
    private ExchangeExportServiceI exchangeExportServiceI;

    @GetMapping("/page")
    @ApiOperation(value="分页查询")
    public Response<PageInfo<List<ExchangeExportPageCO>>> page(ExchangeExportPageQry exchangeExportPageQry) {
        PageInfo<List<ExchangeExportPageCO>> pageInfo = exchangeExportServiceI.page(exchangeExportPageQry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeExport, value = CommonLogTypeConstantDTO.ExchangeExportAdd)
    @PostMapping
    @ApiOperation(value="新增兑换卡导出")
    public Response add(@RequestBody ExchangeExportCmd exchangeExportCmd){
        return exchangeExportServiceI.add(exchangeExportCmd);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeExport, value = CommonLogTypeConstantDTO.ExchangeExportUpdate)
    @PutMapping
    @ApiOperation(value="更新兑换卡导出")
    public Response update(@RequestBody ExchangeExportCmd exchangeExportCmd){
        return exchangeExportServiceI.update(exchangeExportCmd);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeExport, value = CommonLogTypeConstantDTO.ExchangeExportOut)
    @PostMapping("/out")
    @ApiOperation(value="兑换卡导出")
    public Response<String> export(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){
        return exchangeExportServiceI.export(flexibleIdDTO.getId(),getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeExport, value = CommonLogTypeConstantDTO.ExchangeExportDelete)
    @DeleteMapping
    @ApiOperation(value = "兑换卡导出删除")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO) {
        exchangeExportServiceI.delete(flexibleIdDTO.getId());
        return Response.buildSuccess();
    }

}
