package com.bat.flexible.web.exchange;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.exchange.ExchangeCodeUserServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeCodeUserCmd;
import com.bat.flexible.api.exchange.dto.ExchangeCodeUserDetailDTO;
import com.bat.flexible.api.exchange.dto.ExchangeCodeUserPageQry;
import com.bat.flexible.api.exchange.dto.order.ExchangePreAddCmd;
import com.bat.flexible.api.exchange.dto.order.ExchangePreReturnDTO;
import com.bat.flexible.api.exchange.dto.order.ExchangeUnboundCmd;
import com.bat.flexible.dao.exchange.co.ExchangeCodeUserCO;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/flexible/v1/web/user/exchangeCodeUser")
@Api(tags = "兑换卡卡包前台接口")
public class ExchangeCodeUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeCodeUserController.class);

    @Autowired
    private ExchangeCodeUserServiceI exchangeCodeUserServiceI;

    /**
     * 兑换码列表/code/list
     * @return
     */
    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询C端用户的兑换码列表")
    public Response<PageInfo<ExchangeCodeUserCO>> page(@Valid ExchangeCodeUserPageQry exchangeCodeUserPageQry) {
        PageInfo<ExchangeCodeUserCO> pageInfo = exchangeCodeUserServiceI.page(exchangeCodeUserPageQry);
        return Response.of(pageInfo);
    }

    /**
     * 查看兑换码详情
     * @param flexibleIdDTO
     * @return
     */
    @GetMapping(value = "/findByExchangeCodeId")
    @ApiOperation(value = "查看兑换码详情")
    public Response<ExchangeCodeUserDetailDTO> findByExchangeCodeId(@Valid FlexibleIdDTO flexibleIdDTO) {
        ExchangeCodeUserDetailDTO exchangeCodeUserDetailDTO = exchangeCodeUserServiceI.findByExchangeCodeId(flexibleIdDTO.getId());
        return Response.of(exchangeCodeUserDetailDTO);
    }

    /**
     * 将兑换码添加到卡包
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCodeUser, value = CommonLogTypeConstantDTO.ExchangeCodeUserAdd)
    @PostMapping
    @ApiOperation(value = "添加卡包")
    public Response add (@RequestBody ExchangeCodeUserCmd exchangeCodeUserCmd){
        return exchangeCodeUserServiceI.create(exchangeCodeUserCmd);
    }

    @GetMapping(value = "/count")
    @ApiOperation(value = "条件查询C端用户的兑换码数量")
    public Response count( ExchangeCodeUserPageQry exchangeCodeUserPageQry) {
        Integer count = exchangeCodeUserServiceI.count(exchangeCodeUserPageQry);
        return Response.of(count);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCodeUser, value = CommonLogTypeConstantDTO.ExchangeCodeUserOrderPreAdd)
    @PostMapping(value = "/order/pre/add")
    @ApiOperation(value = "兑换码订单预添加")
    public Response orderPreAdd(@Valid @RequestBody ExchangePreAddCmd exchangePreAddCmd) {
        ExchangePreReturnDTO exchangePreReturnDTO = exchangeCodeUserServiceI.orderPreAdd(exchangePreAddCmd);
        return Response.of(exchangePreReturnDTO);
    }

    /**
     * 前台用户进行兑换码解绑
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeUnbound, value = CommonLogTypeConstantDTO.ExchangeUnbound)
    @DeleteMapping(value = "/unboundExchange")
    @ApiOperation(value = "前台用户进行兑换码解绑")
    public Response unboundExchange(@Valid @RequestBody ExchangeUnboundCmd exchangeUnboundCmd) {
        LOGGER.info("前台用户进行兑换码解绑");
        LOGGER.info(String.valueOf(exchangeUnboundCmd));
        return exchangeCodeUserServiceI.unbound(exchangeUnboundCmd);
    }

}
