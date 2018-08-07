package com.bat.distributor.web.trade;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.trade.TradeServiceI;
import com.bat.distributor.api.trade.dto.TradeCmd;
import com.bat.distributor.api.trade.dto.TradeId;
import com.bat.distributor.api.trade.dto.TradeListQry;
import com.bat.distributor.api.trade.dto.TradeOpenCmd;
import com.bat.distributor.api.trade.dto.data.TradeDTO;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "分销商收款条件后台接口", value = "AdminTradeController")
@RestController
@RequestMapping("/distributor/v1/web/admin/trade")
public class AdminTradeController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminTradeController.class);

    @Resource
    private TradeServiceI service;

    @ApiOperation(value = "根据搜索条件查找分销商收款条件列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<TradeDTO>> listTrade(@Valid TradeListQry qry) {
        PageInfo<TradeDTO> pageInfo = service.listTrade(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找分销商收款条件列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<TradeDTO>> listTradePo(@Valid TradeListQry qry) {
        PageInfo<TradeDTO> pageInfo = service.listTrade(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据分销商收款条件id获取分销商收款条件详情")
    @GetMapping()
    public Response<TradeDTO> getTrade(@Valid TradeId qry) {
        TradeDTO dto = service.getTrade(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTrade, value = CommonLogTypeConstantDTO.AdminTradeAdd)
    @ApiOperation(value = "添加分销商收款条件")
    @PostMapping()
    public Response createTrade(@RequestBody @Valid TradeCmd cmd) {
        service.createTrade(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTrade, value = CommonLogTypeConstantDTO.AdminTradeUpdate)
    @ApiOperation(value = "更新分销商收款条件")
    @PutMapping()
    public Response updateTrade(@RequestBody @Valid TradeCmd cmd) {
        service.updateTrade(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTrade, value = CommonLogTypeConstantDTO.AdminTradeUpdateStatus)
    @ApiOperation(value = "更新分销商收款条件状态")
    @PutMapping(value = "/open")
    public Response openTrade(@RequestBody @Valid TradeOpenCmd cmd) {
        service.openTrade(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTrade, value = CommonLogTypeConstantDTO.AdminTradeDeleteById)
    @ApiOperation(value = "根据分销商收款条件id删除分销商收款条件")
    @DeleteMapping()
    public Response deleteTrade(@RequestBody @Valid TradeId cmd) {
        service.deleteTrade(cmd);
        return Response.buildSuccess();
    }

}
