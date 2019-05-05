package com.bat.thirdparty.distributor.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.thirdparty.common.base.BaseController;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.log.annotation.SysLog;
import com.bat.thirdparty.log.constants.CommonLogTypeConstantDTO;
import com.bat.thirdparty.log.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bat.thirdparty.distributor.api.SamsungServiceI;
import com.bat.thirdparty.distributor.api.dto.SamsungAccountQry;
import com.bat.thirdparty.distributor.api.dto.SamsungCheckTokenQry;
import com.bat.thirdparty.distributor.api.dto.SamsungVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/8 11:36
 */
@Api(tags = "客户接口")
@RequestMapping(value = "/thirdparty")
@RestController
public class SamsungController extends BaseController {

    @Resource
    private SamsungServiceI service;

    @ApiOperation(value = "账号判断是否资质领券")
    @GetMapping("/v1/web/samsung/coupon/qualified")
    public Response<Short> couponQualified(@Valid SamsungAccountQry qry) {
        Short status = service.couponQualified(qry);
        return Response.of(status);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Samsung,
        value = CommonLogTypeConstantDTO.SamsungIsReceived)
    @ApiOperation(value = "账号判断是否已领券(侧调用)")
    @PostMapping(value = {"/samsung/coupon/isreceived", "/v1/web/samsung/coupon/isreceived"})
    public ResponseEntity<SamsungVo<Short>> isreceived(@Valid @RequestBody SamsungAccountQry qry) {
        SamsungVo<Short> vo = new SamsungVo<>();
        Short status;
        try {
            status = service.couponReceived(qry);
            vo.setMessage("success");
            vo.setStatus(200);
            vo.setData(status);
        } catch (ThirdPartyException e) {
            vo.setMessage(e.getMsg());
            vo.setStatus(20001);
        } catch (Exception e) {
            vo.setMessage("网络问题，请稍后重试");
            vo.setStatus(20001);
        }
        return ResponseEntity.ok(vo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Samsung,
        value = CommonLogTypeConstantDTO.SamsungCheckToken)
    @ApiOperation(value = "token验证")
    @PostMapping("/v1/web/samsung/check/token")
    public Response<String> checkToken(@RequestBody @Valid SamsungCheckTokenQry qry) {
        String token = service.checkToken(qry);
        return Response.of(token);
    }

}
