package com.bat.financial.api.pay;

import com.bat.financial.api.pay.dto.*;
import com.bat.financial.api.pay.dto.data.CreateTradeRespDTO;
import com.bat.financial.api.pay.dto.data.QueryRefundDTO;
import com.bat.financial.api.pay.dto.data.QueryTradeRespDTO;
import com.bat.financial.api.pay.dto.data.RefundTradeRespDTO;
import com.bat.financial.api.pay.notify.CreateNotifyReq;
import com.bat.financial.api.pay.notify.CreateNotifyResp;
import com.bat.financial.api.pay.notify.RefundNotifyReq;
import com.bat.financial.api.pay.notify.RefundNotifyResp;
import com.bat.financial.api.pay.dto.*;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 15:04
 */
public interface PayService {

    CreateTradeRespDTO createTrade(CreateTradeCmd cmd);

    QueryTradeRespDTO queryTrade(QueryTradeQry qry);

    boolean closeTrade(CloseTradeCmd cmd);

    RefundTradeRespDTO refundTrade(RefundTradeCmd cmd);

    QueryRefundDTO queryRefund(QueryRefundCmd cmd);

    CreateNotifyResp createTradeNotify(CreateNotifyReq notify);

    RefundNotifyResp refundTradeNotify(RefundNotifyReq req);
}
