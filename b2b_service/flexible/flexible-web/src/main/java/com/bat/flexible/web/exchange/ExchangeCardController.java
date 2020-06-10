package com.bat.flexible.web.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.exchange.ExchangeCardServiceI;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.dao.exchange.co.ExchangeCardPageCO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 兑换卡控制器
 */
@RestController
@RequestMapping("/flexible/v1/web/admin/u")
@Api(tags = "兑换卡后台管理后台接口")
public class ExchangeCardController extends BaseController {



    @Autowired
    private ExchangeCardServiceI exchangeCardServiceI;


    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCard, value = CommonLogTypeConstantDTO.ExchangeCardAdd)
    @PostMapping(value = "/p/exchangeCard")
    @ApiOperation(value="新增兑换卡")
    public Response add(@RequestBody @Valid ExchangeCardCmd exchangeCardCmd){
        return exchangeCardServiceI.add(exchangeCardCmd,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCard, value = CommonLogTypeConstantDTO.ExchangeCardUpdate)
    @PutMapping(value = "/p/exchangeCard")
    @ApiOperation(value="修改兑换卡")
    public Response update(@RequestBody @Valid ExchangeCardCmd exchangeCardCmd, BindingResult result){

        return exchangeCardServiceI.update(exchangeCardCmd,getCurrentAdmin());
    }


    @GetMapping("/p/exchangeCard/detail")
    @ApiOperation(value="查看兑换卡详情")
    public Response<ExchangeCardDetailDTO> detailById(@Valid FlexibleIdDTO flexibleIdDTO){

        return exchangeCardServiceI.detailById(flexibleIdDTO.getId());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCard, value = CommonLogTypeConstantDTO.ExchangeCardUpdateStatus)
    @PutMapping("/p/exchangeCard/updateStatus")
    @ApiOperation(value = "修改状态")
    public Response updateStatus(@Valid @RequestBody ExchangeCardStatusCmd exchangeCardStatusCmd){

        return exchangeCardServiceI.updateStatus(exchangeCardStatusCmd,getCurrentAdmin());
    }

    /**
     * 分页条件查询
     * @return
     */
    @GetMapping(value = {"/p/exchangeCard/page","/po/exchangeCard/page"})
    @ApiOperation(value="分页查询")
    public Response<PageInfo<List<ExchangeCardPageCO>>> page( ExchangeCardPageQry exchangeCardPageQry){
        PageInfo<List<ExchangeCardPageCO>> pageInfo = exchangeCardServiceI.page(exchangeCardPageQry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCard, value = CommonLogTypeConstantDTO.ExchangeCardQrCode)
    @PutMapping("/p/exchangeCard/qrCode")
    @ApiOperation(value = "生成二维码")
    public Response qrCode(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){

       return exchangeCardServiceI.qrCode(flexibleIdDTO.getId(),getCurrentAdmin(),null);
    }

    /**
     * 同步至工厂
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCard, value = CommonLogTypeConstantDTO.ExchangeCardSyncFactory)
    @PostMapping(value = "/p/exchangeCard/syncFactory")
    public Response syncFactory(@Valid @RequestBody ExchangeSyncFactoryRequest factoryRequest, BindingResult result) throws IOException {
        return null;
       // return exchangeCardServiceI.syncFactory(factoryRequest,getCurrentAdmin());
    }




}
