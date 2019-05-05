package com.bat.promotion.web.coupon;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.promotion.api.coupon.dto.*;
import com.bat.promotion.web.annotation.SysLog;
import com.bat.promotion.web.base.BaseController;
import com.bat.promotion.web.constants.CommonLogTypeConstantDTO;
import com.bat.promotion.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.coupon.CouponServiceI;
import com.bat.promotion.api.coupon.dto.*;
import com.bat.promotion.api.coupon.dto.data.CouponCustomerDTO;
import com.bat.promotion.api.coupon.dto.data.CouponDTO;
import com.bat.promotion.api.coupon.dto.data.CouponListDTO;
import com.bat.promotion.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "优惠券后台接口", value = "CouponController")
@RestController
@RequestMapping("/promotion/v1/web/admin/coupon")
public class CouponController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Resource
    private CouponServiceI service;

    @ApiOperation(value = "根据搜索条件查找优惠券列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<CouponListDTO>> listCoupon(@Valid CouponListQry qry) {
        PageInfo<CouponListDTO> pageInfo = service.listCoupon(qry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Coupon, value = CommonLogTypeConstantDTO.CouponAdd)
    @ApiOperation(value = "新增优惠券活动")
    @PostMapping()
    public Response createCoupon(@RequestBody @Valid CouponCmd cmd) {
        service.createCoupon(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Coupon, value = CommonLogTypeConstantDTO.CouponUpdate)
    @ApiOperation(value = "修改优惠券活动(草稿或未发布状态的优惠券修改)")
    @PutMapping()
    public Response updateCoupon(@RequestBody @Valid CouponCmd cmd) {
        service.updateCoupon(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Coupon, value = CommonLogTypeConstantDTO.CouponUpdateCount)
    @ApiOperation(value = "修改优惠券发放总数量和限购数量")
    @PutMapping(value = "/count")
    public Response updateCouponCount(@RequestBody @Valid CouponCountCmd cmd) {
        service.updateCouponCount(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "根据id查询优惠券详情")
    @GetMapping()
    public Response<CouponDTO> getCoupon(@Valid BaseId qry) {
        CouponDTO dto = service.getCoupon(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Coupon, value = CommonLogTypeConstantDTO.CouponDelete)
    @ApiOperation(value = "根据id删除优惠券")
    @DeleteMapping()
    public Response deleteCoupon(@RequestBody @Valid BaseId cmd) {
        service.deleteCoupon(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Coupon,
        value = CommonLogTypeConstantDTO.CouponUpdateStatus)
    @ApiOperation(value = "根据优惠券id变更状态")
    @PutMapping(value = "/status")
    public Response updateCouponStatus(@RequestBody @Valid CouponStatusCmd cmd) {
        service.updateCouponStatus(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "获取券码列表")
    @GetMapping(value = "/no")
    public Response listCouponNo(@Valid CouponCustomerListQry qry) {
        PageInfo<CouponCustomerDTO> pageInfo = service.listCouponNo(qry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Coupon,
        value = CommonLogTypeConstantDTO.CouponUpdateStatusCustomer)
    @ApiOperation(value = "根据券码变更客户优惠券状态")
    @PutMapping(value = "/customer/status")
    public Response updateCustomerCouponStatus(@RequestBody @Valid CouponCustomerStatusCmd cmd) {
        service.updateCustomerCouponStatus(cmd);
        return Response.buildSuccess();
    }

}
