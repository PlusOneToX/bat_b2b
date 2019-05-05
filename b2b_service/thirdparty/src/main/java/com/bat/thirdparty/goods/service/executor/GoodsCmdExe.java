package com.bat.thirdparty.goods.service.executor;

import com.bat.thirdparty.common.base.Response;
import org.springframework.stereotype.Component;

import com.bat.thirdparty.goods.api.dto.ItemCodeCmd;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/28 10:10
 */
@Component
public class GoodsCmdExe {

    /**
     * 根据货品编码同步货品价格
     * 
     * @param cmd
     * @return
     */
    public Response syncGoodsPriceByItemCodes(ItemCodeCmd cmd) {

        return Response.buildSuccess();
    }
}
