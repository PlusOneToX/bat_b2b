package com.bat.flexible.api.taobao;

import com.bat.flexible.api.base.common.response.Response;

public interface TaoBaoServiceI {


    Response getItemSeller(Integer distributorId,Long numId,Long skuId,String sellerNick);
}
