package com.bat.thirdparty.msgcenter.controller;

import com.bat.thirdparty.common.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/user/customer/msgcenter")
@Api(tags = "消息中心C端用户前台接口")
public class UserCustomerMsgCenterController extends BaseController {
}