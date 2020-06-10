package com.bat.flexible.web.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.exchange.ExchangeSpecialServiceI;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.dao.exchange.co.ExchangeSpecialDistributorCO;
import com.bat.flexible.dao.exchange.co.ExchangeSpecialPageCO;
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
@RequestMapping("/flexible/v1/web/admin/u/p/exchangeSpecial")
@Api(tags = "兑换卡营销专题后台管理后台接口")
public class ExchangeSpecialController extends BaseController {

    @Autowired
    private ExchangeSpecialServiceI exchangeSpecialServiceI;

    /**
     * 分页条件查询
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value="分页查询")
    public Response<PageInfo<List<ExchangeSpecialPageCO>>> page(ExchangeSpecialPageQry exchangeSpecialPageQry){
        PageInfo<List<ExchangeSpecialPageCO>> pageInfo = exchangeSpecialServiceI.page(exchangeSpecialPageQry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeSpecial, value = CommonLogTypeConstantDTO.ExchangeSpecialAdd)
    @PostMapping
    @ApiOperation(value="新增专题")
    public Response add(@RequestBody @Valid ExchangeSpecialCmd exchangeSpecialCmd){
        return exchangeSpecialServiceI.add(exchangeSpecialCmd,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeSpecial, value = CommonLogTypeConstantDTO.ExchangeSpecialUpdate)
    @PutMapping
    @ApiOperation(value="修改专题")
    public Response update(@RequestBody @Valid ExchangeSpecialCmd exchangeSpecialCmd){
        return exchangeSpecialServiceI.update(exchangeSpecialCmd,getCurrentAdmin());
    }

    @GetMapping("/detail")
    @ApiOperation(value="查看专题详情")
    public Response<ExchangeSpecialDetailDTO> detailById(@Valid FlexibleIdDTO flexibleIdDTO){
        return exchangeSpecialServiceI.detailById(flexibleIdDTO.getId());
    }

    /**
     * 分页条件查询专题分销商关系
     * @return
     */
    @GetMapping("/distributor/page")
    @ApiOperation(value = "分页条件查询专题分销商关系")
    public Response<PageInfo<List<ExchangeSpecialDistributorCO>>> distributorPage(ExchangeSpecialDistributorPageQry exchangeSpecialDistributorPageQry) {
        PageInfo<List<ExchangeSpecialDistributorCO>> pageInfo = exchangeSpecialServiceI.distributorPage(exchangeSpecialDistributorPageQry);
        return Response.of(pageInfo);
    }

    /**
     * 分销商关系添加
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeSpecial, value = CommonLogTypeConstantDTO.ExchangeSpecialDistributorAdd)
    @PostMapping("/distributor")
    @ApiOperation(value = "专题分销商关系添加")
    public Response distributorAdd(@RequestBody ExchangeSpecialDistributorAddCmd exchangeSpecialDistributorAddCmd) {
        return exchangeSpecialServiceI.distributorAdd(exchangeSpecialDistributorAddCmd);
    }

    /**
     * 分销商关系添加更新
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeSpecial, value = CommonLogTypeConstantDTO.ExchangeSpecialDistributorUpdate)
    @PutMapping("/distributor")
    @ApiOperation(value = "专题分销商关系更新")
    public Response distributorUpdate(@RequestBody ExchangeSpecialDistributorUpdateCmd exchangeSpecialDistributorUpdateCmd) {
        return exchangeSpecialServiceI.distributorUpdate(exchangeSpecialDistributorUpdateCmd);
    }

    @ApiOperation(value = "查看二维码")
    @PostMapping("/specialDistributor/qrCodeUrl")
    public Response<String> qrCodeUrl(@RequestBody @Valid FlexibleIdDTO flexibleIdDTO) {
        String url = exchangeSpecialServiceI.specialDistributorQrCodeUrl(flexibleIdDTO.getId());
        return Response.of(url);
    }

    @ApiOperation(value = "查看短链接")
    @PostMapping("/specialDistributor/shortLink")
    public Response<String> shortLink(@RequestBody @Valid FlexibleIdDTO flexibleIdDTO) {
        String url = exchangeSpecialServiceI.specialDistributorShortLink(flexibleIdDTO.getId());
        return Response.of(url);
    }

    @ApiOperation(value = "查看链接")
    @PostMapping("/specialDistributor/link")
    public Response<String> link(@RequestBody @Valid FlexibleIdDTO flexibleIdDTO) {
        String url = exchangeSpecialServiceI.specialDistributorLink(flexibleIdDTO.getId());
        return Response.of(url);
    }


}
