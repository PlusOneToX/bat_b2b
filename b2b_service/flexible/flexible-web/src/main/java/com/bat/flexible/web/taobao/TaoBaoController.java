package com.bat.flexible.web.taobao;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.taobao.TaoBaoServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/flexible/v1/web/user/u/taoBao")
@Api(tags = "淘宝聚石塔接口")
public class TaoBaoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaoBaoController.class);

    @Autowired
    private TaoBaoServiceI taoBaoServiceI;

    @GetMapping("/item/seller")
    @ApiOperation(value = "聚石塔sku查询")
    public Response getItemSeller(@RequestParam(value = "distributorId", required = false) Integer distributorId,
                                  @RequestParam(value = "seriesNum", required = false) Long numId,
                                  @RequestParam(value = "skuId", required = false) Long skuId,
                                  @RequestParam(value = "sellerNick", required = false) String sellerNick) {
        LOGGER.info("getItemSeller distributorId:{},seriesNum,{},skuId:{},sellerNick{}", distributorId, numId, skuId, sellerNick);
        if (numId == null) {
            numId = skuId;
        }
        if (distributorId == null) {
            distributorId = 2775;
        }
        return taoBaoServiceI.getItemSeller(distributorId, numId, skuId, sellerNick);
    }
}
