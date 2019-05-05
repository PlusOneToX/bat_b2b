package com.bat.promotion.web.check;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.promotion.web.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.check.PromotionCheckServiceI;
import com.bat.promotion.api.check.dto.PromotionCheckCmd;
import com.bat.promotion.api.check.dto.PromotionCheckListQry;
import com.bat.promotion.api.check.dto.data.PromotionCheckDTO;
import com.bat.promotion.api.check.dto.data.PromotionCheckListDTO;
import com.bat.promotion.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "活动审批后台接口", value = "CheckController")
@RestController
@RequestMapping("/promotion/v1/web/admin/promotion/check")
public class CheckController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(CheckController.class);

    @Resource
    private PromotionCheckServiceI service;

    @ApiOperation(value = "促销活动审批列表")
    @GetMapping(value = "/list")
    public Response<PageInfo<PromotionCheckListDTO>> listPromotionCheck(@Valid PromotionCheckListQry qry) {
        PageInfo<PromotionCheckListDTO> pageInfo = service.listPromotionCheck(qry, getUserId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "促销活动审批详情")
    @GetMapping()
    public Response<PromotionCheckDTO> getPromotionCheck(@Valid BaseId qry) {
        PromotionCheckDTO checkDTO = service.getPromotionCheck(qry);
        return Response.of(checkDTO);
    }

    @ApiOperation(value = "促销活动审批")
    @PutMapping()
    public Response checkPromotion(@RequestBody @Valid PromotionCheckCmd cmd) {
        service.checkPromotion(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

}
