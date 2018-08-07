package com.bat.financial.api.deposit;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.dto.ReceiptQry;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/10 19:42
 */
public interface ReceiptService {
    PageInfo listReceipt(ReceiptQry qry);
}
