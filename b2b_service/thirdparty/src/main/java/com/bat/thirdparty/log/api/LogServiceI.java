package com.bat.thirdparty.log.api;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.log.api.dto.*;
import com.bat.thirdparty.log.api.dto.*;

public interface LogServiceI {

    PageInfo findPageCommonLogByParams(LogQry qry);

    PageInfo findPageDistributorLogByParams(DistributorLogQry qry);

    PageInfo findPageOrderLogByParams(OrderLogQry qry);

    PageInfo findPageVoucherLogByParams(VoucherLogQry qry);

    PageInfo findPageRefundLogByParams(RefundLogQry qry);

    PageInfo findPageOrderDeliverBillLogByParams(OrderDeliverBillLogQry qry);

    PageInfo findPageWithdrawApplyLogByParams(WithdrawApplyLogQry qry);
}
