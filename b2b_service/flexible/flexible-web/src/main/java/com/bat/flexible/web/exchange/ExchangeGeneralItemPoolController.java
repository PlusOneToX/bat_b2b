package com.bat.flexible.web.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.goods.dto.GoodsItemSimplePageBean;
import com.bat.flexible.api.exchange.ExchangeGeneralItemPoolServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeGeneralItemQry;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 兑换卡控制器
 */
@RestController
@RequestMapping("/flexible/v1/web/admin/u")
@Api(tags = "兑换卡关联的通用版货品池后台接口")
public class ExchangeGeneralItemPoolController extends BaseController {



    @Autowired
    private ExchangeGeneralItemPoolServiceI exchangeGeneralItemPoolServiceI;




    /**
     * 分页条件查询
     * @return
     */
    @GetMapping({"p/exchangeGeneralItemPool/page","po/exchangeGeneralItemPool/page"})
    @ApiOperation(value="分页查询分销商通用兑换卡关联的货品信息")
    public Response<PageInfo<GoodsItemSimplePageBean>> page(@Valid ExchangeGeneralItemQry exchangeGeneralItemQry){
        PageInfo<GoodsItemSimplePageBean> pageInfo = exchangeGeneralItemPoolServiceI.page(exchangeGeneralItemQry);
        return Response.of(pageInfo);
    }






}
