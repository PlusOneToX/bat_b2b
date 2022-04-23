package com.platform.modules.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.Constant;
import com.platform.common.utils.JedisUtil;
import com.platform.common.utils.RestResponse;
import com.platform.modules.app.entity.BuyGoodsVo;
import com.platform.modules.mall.entity.MallUserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @gitee https://gitee.com/fuyang_lipengjun/platform
 */
@RestController
@RequestMapping("/app/buy")
@Api(tags = "AppBuyController|商品购买")
public class AppBuyController {
    @Autowired
    JedisUtil jedisUtil;

    @ApiOperation(value = "商品添加")
    @PostMapping("/add")
    public Object addBuy(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String skuId = jsonParam.getString("skuId");
        String goodsId = jsonParam.getString("goodsId");
        Integer number = jsonParam.getInteger("number");
        String selectedText = jsonParam.getString("selectedText");

        BuyGoodsVo goodsVo = new BuyGoodsVo();
        goodsVo.setSkuId(skuId);
        goodsVo.setGoodsId(goodsId);
        goodsVo.setNumber(number);
        goodsVo.setSelectedText(selectedText);
        jedisUtil.setObject(Constant.MTM_CACHE + "goods" + loginUser.getId(), goodsVo);
        return RestResponse.success("添加成功");
    }
}
