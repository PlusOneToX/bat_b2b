package com.bat.financial.api.subaccount;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.subaccount.dto.OrderSubAccountPageDTO;
import com.bat.financial.api.subaccount.dto.OrderSubAccountPageQry;
import com.bat.financial.api.subaccount.dto.WechatPaySubAccountCmd;

public interface OrderSubAccountServiceI {


    void test(WechatPaySubAccountCmd cmd);

    PageInfo<OrderSubAccountPageDTO> page(OrderSubAccountPageQry orderSubAccountPageQry);


}
