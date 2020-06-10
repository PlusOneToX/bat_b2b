package com.bat.flexible.web.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.ExchangeShareCmd;
import com.bat.flexible.api.exchange.dto.ExchangeShareDetailDTO;
import com.bat.flexible.api.exchange.dto.ExchangeSharePageQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.exchange.ExchangeShareServiceI;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.dao.exchange.co.ExchangeSharePageCO;
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
@RequestMapping("/flexible/v1/web/admin/u/p/exchangeShare")
@Api(tags = "兑换卡转发活动配置后台管理后台接口")
public class ExchangeShareController extends BaseController {

    @Autowired
    private ExchangeShareServiceI exchangeShareServiceI;

    /**
     * 分页条件查询
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value="分页查询")
    public Response<PageInfo<List<ExchangeSharePageCO>>> page(ExchangeSharePageQry exchangeSharePageQry) {
        PageInfo<List<ExchangeSharePageCO>> pageInfo = exchangeShareServiceI.page(exchangeSharePageQry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeShare, value = CommonLogTypeConstantDTO.ExchangeShareAdd)
    @PostMapping
    @ApiOperation(value="新增转发活动配置")
    public Response add(@RequestBody ExchangeShareCmd exchangeShareCmd){
        return exchangeShareServiceI.add(exchangeShareCmd);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeShare, value = CommonLogTypeConstantDTO.ExchangeShareUpdate)
    @PutMapping
    @ApiOperation(value="更新转发活动配置")
    public Response update(@RequestBody ExchangeShareCmd exchangeShareCmd){
        return exchangeShareServiceI.update(exchangeShareCmd);
    }

    @GetMapping("/detail")
    @ApiOperation(value="查看转发活动详情")
    public Response<ExchangeShareDetailDTO> detailById(@Valid FlexibleIdDTO flexibleIdDTO){
        return exchangeShareServiceI.detailById(flexibleIdDTO.getId());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeShare, value = CommonLogTypeConstantDTO.ExchangeShareDelete)
    @DeleteMapping
    @ApiOperation(value = "删除转发活动配置")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO) {
        exchangeShareServiceI.delete(flexibleIdDTO.getId());
        return Response.buildSuccess();
    }

}
