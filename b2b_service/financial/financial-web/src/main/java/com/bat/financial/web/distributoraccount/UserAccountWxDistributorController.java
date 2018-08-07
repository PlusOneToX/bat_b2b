package com.bat.financial.web.distributoraccount;

import com.bat.financial.api.distributoraccount.AccountWxDistributorService;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 9:50
 */
@Api(tags = "分销商账户-微信支付账户列表前台接口", value = "UserAccountWxDistributorController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/user/distributor/accountWx")
public class UserAccountWxDistributorController extends BaseController {

    @Resource
    private AccountWxDistributorService accountWxDistributorService;



    @GetMapping("/getSubAccountRatioByDistributor")
    @ApiOperation(value = "查询分销商服务商的分账比例")
    public Response<BigDecimal> getSubAccountRatioByDistributor() {
        BigDecimal ratio = accountWxDistributorService.getSubAccountRatioByDistributor(Integer.parseInt(getUserId()));
        return Response.of(ratio);
    }

}