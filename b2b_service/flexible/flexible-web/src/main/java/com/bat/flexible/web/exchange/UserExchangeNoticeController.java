package com.bat.flexible.web.exchange;


import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.ExchangeNoticeServiceI;
import com.bat.flexible.dao.exchange.dataobject.ExchangeNoticeDO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/flexible/v1/web/user/exchangeNotice")
@Api(tags = "兑换卡通知前台接口")
public class UserExchangeNoticeController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserExchangeNoticeController.class);

    @Autowired
    private ExchangeNoticeServiceI exchangeNoticeServiceI;

    @GetMapping(value = "/list")
    @ApiOperation(value = "查询兑换卡通知列表")
    public Response<List<ExchangeNoticeDO>> noticeList() {
        List<ExchangeNoticeDO> exchangeNotices = exchangeNoticeServiceI.noticeList();
        return Response.of(exchangeNotices);
    }


}
