package com.bat.flexible.api.exchange;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.ExchangeCodeUserCmd;
import com.bat.flexible.api.exchange.dto.ExchangeCodeUserDetailDTO;
import com.bat.flexible.api.exchange.dto.ExchangeCodeUserPageQry;
import com.bat.flexible.api.exchange.dto.order.ExchangePreAddCmd;
import com.bat.flexible.api.exchange.dto.order.ExchangePreReturnDTO;
import com.bat.flexible.api.exchange.dto.order.ExchangeUnboundCmd;

public interface ExchangeCodeUserServiceI {
    /**
     * 分页用户查询兑换码卡包列表
     * @param exchangeCodeUserPageQry
     * @return
     */
    PageInfo page(ExchangeCodeUserPageQry exchangeCodeUserPageQry);

    /**
     * 当前兑换嘛详情
     * @param id
     * @return
     */
    ExchangeCodeUserDetailDTO findByExchangeCodeId(Integer id);

    /**
     * 添加
     * @param exchangeCodeUserCmd
     * @return
     */
    Response create(ExchangeCodeUserCmd exchangeCodeUserCmd);

    /**
     * 查询C端用户的兑换码数量
     * @param exchangeCodeUserPageQry
     * @return
     */
    Integer count(ExchangeCodeUserPageQry exchangeCodeUserPageQry);

    /**
     * 兑换码订单预添加
     * @param exchangePreAddCmd
     * @return
     */
    ExchangePreReturnDTO orderPreAdd(ExchangePreAddCmd exchangePreAddCmd);

    /**
     * 前台用户进行兑换码解绑
     * @param exchangeUnboundCmd
     * @return
     */
    Response unbound(ExchangeUnboundCmd exchangeUnboundCmd);
}
