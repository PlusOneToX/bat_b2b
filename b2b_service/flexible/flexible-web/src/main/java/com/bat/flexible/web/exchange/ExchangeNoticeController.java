package com.bat.flexible.web.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.exchange.ExchangeNoticeServiceI;
import com.bat.flexible.dao.exchange.dataobject.ExchangeNoticeDO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/flexible/v1/web/admin/u/p/exchangeNotice")
@Api(tags = "兑换卡通知后台接口")
public class ExchangeNoticeController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeNoticeController.class);

    @Autowired
    private ExchangeNoticeServiceI exchangeNoticeServiceI;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<List<ExchangeNoticeDO>>> list(BasePageParamQry basePageParamQry) {
        PageInfo<List<ExchangeNoticeDO>> pageInfo = exchangeNoticeServiceI.page(basePageParamQry);
        return Response.of(pageInfo);
    }

    @GetMapping
    @ApiOperation(value = "根据id查询详情")
    public Response get(@Valid FlexibleIdDTO flexibleIdDTO) {
        ExchangeNoticeDO exchangeNotice=exchangeNoticeServiceI.getById(flexibleIdDTO.getId());
        return Response.of(exchangeNotice);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeNotice, value = CommonLogTypeConstantDTO.ExchangeNoticeAdd)
    @PostMapping
    @ApiOperation(value = "新增")
    public Response create(@RequestBody ExchangeNoticeDO exchangeNotice) {
        exchangeNotice = exchangeNoticeServiceI.create(exchangeNotice);
        return Response.of(exchangeNotice);
    }


    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeNotice, value = CommonLogTypeConstantDTO.ExchangeNoticeUpdate)
    @PutMapping
    @ApiOperation(value = "修改")
    public Response update(@RequestBody ExchangeNoticeDO exchangeNotice) {
        exchangeNotice = exchangeNoticeServiceI.update(exchangeNotice);
        return Response.of(exchangeNotice);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeNotice, value = CommonLogTypeConstantDTO.ExchangeNoticeDelete)
    @DeleteMapping
    @ApiOperation(value = "删除")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO) {
        exchangeNoticeServiceI.delete(flexibleIdDTO.getId());
        return Response.buildSuccess();
    }

}
