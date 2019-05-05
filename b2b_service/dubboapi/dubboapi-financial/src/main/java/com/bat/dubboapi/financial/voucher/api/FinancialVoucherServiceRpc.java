package com.bat.dubboapi.financial.voucher.api;

import java.util.List;

import com.bat.dubboapi.financial.voucher.dto.data.CreateReceiveBillEntryReq;
import com.bat.dubboapi.financial.voucher.dto.data.ErpVoucherDetailsDTO;
import com.bat.dubboapi.financial.voucher.dto.data.VoucherDistributorRpcDTO;
import com.bat.dubboapi.financial.common.Response;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/11 14:28
 */
public interface FinancialVoucherServiceRpc {

    /**
     * ERP->B2B 创建收款单
     * 
     * @param dtos
     */
    Response createVouchers(List<ErpVoucherDetailsDTO> dtos);

    /**
     * 同步erp收款单结果
     * 
     * @param request
     * @return
     */
    Response updateVouchers(List<ErpVoucherDetailsDTO> request);

    /**
     * 获取组装的erp收款单列表
     *
     * @param voucherIds
     * @return
     */
    Response<List<CreateReceiveBillEntryReq>> listErpVoucherById(List<Integer> voucherIds);

    /**
     * 根据订单id 获取收款单id
     * 
     * @return
     */
    Response<VoucherDistributorRpcDTO> listVouchersByBusinessId(Integer businessId);

}
