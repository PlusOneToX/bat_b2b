package com.bat.promotion.web.user;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.user.dto.customer.*;
import com.bat.promotion.web.annotation.SysLog;
import com.bat.promotion.web.base.BaseController;
import com.bat.promotion.web.constants.CommonLogTypeConstantDTO;
import com.bat.promotion.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.promotion.api.base.BaseIds;
import com.bat.promotion.api.user.UserCustomerServiceI;
import com.bat.promotion.api.user.dto.customer.*;
import com.bat.promotion.api.user.dto.customer.data.UserCustomerCouponCountDTO;
import com.bat.promotion.api.user.dto.customer.data.UserCustomerCouponDTO;
import com.bat.promotion.web.base.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/26 17:19
 */
@Api(tags = "C端客户活动前台接口", value = "UserCustomerController")
@RestController
@RequestMapping("/promotion/v1/web/user/customer")
public class UserCustomerController extends BaseController {

    @Resource
    private UserCustomerServiceI service;

    @ApiOperation(value = "优惠券列表(分页)")
    @GetMapping(value = "/coupon/list")
    public Response<PageInfo<UserCustomerCouponDTO>> couponList(@Valid UserCustomerCouponListQry qry) {
        PageInfo<UserCustomerCouponDTO> pageInfo = service.couponList(qry, getUserId(), getDistributorId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "优惠券汇总")
    @GetMapping(value = "/coupon/count")
    public Response<UserCustomerCouponCountDTO> couponCount() {
        UserCustomerCouponCountDTO dto = service.couponCount(getUserId(), getDistributorId());
        return Response.of(dto);
    }

    @ApiOperation(value = "根据商品列表获取优惠券列表(分页)")
    @PostMapping(value = "/coupon/goods/list")
    public Response<PageInfo<UserCustomerCouponDTO>>
        couponGoodsList(@RequestBody @Valid UserCustomerGoodsCouponListQry qry) {
        PageInfo<UserCustomerCouponDTO> pageInfo = service.couponGoodsList(qry, getUserId(), getDistributorId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据优惠券类型获取优惠券列表(不分页)")
    @GetMapping(value = "/coupon/special/list")
    public Response<List<UserCustomerCouponDTO>> couponSpecialList(@Valid UserCustomerSpecialCouponListQry qry) {
        List<UserCustomerCouponDTO> dtos = service.couponSpecialList(qry, getUserId(), getDistributorId());
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据商品列表获取可用优惠券列表数量")
    @PostMapping(value = "/coupon/goods/enable/count")
    public Response<Integer> couponGoodsEnableCount(@RequestBody @Valid UserCustomerGoodsEnableCouponQry qry) {
        Integer count = service.couponGoodsEnableCount(qry, getUserId(), getDistributorId());
        return Response.of(count);
    }

    @ApiOperation(value = "根据商品列表获取最优优惠券")
    @PostMapping(value = "/coupon/goods/optimal")
    public Response<UserCustomerCouponDTO>
        couponGoodsOptimal(@RequestBody @Valid UserCustomerGoodsOptimalCouponQry qry) {
        UserCustomerCouponDTO dto = service.couponGoodsOptimal(qry, getUserId(), getDistributorId());
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserCustomer,
        value = CommonLogTypeConstantDTO.UserCustomerReceiveCoupon)
    @ApiOperation(value = "用户主动领取优惠券")
    @PostMapping(value = "/coupon/receive")
    public Response receiveCoupon(@RequestBody @Valid BaseIds cmd) {
        service.receiveCoupon(cmd, getUserId(), getUserName(), getDistributorId(), getPlatform(), getOpenId());
        return Response.buildSuccess();
    }

}
