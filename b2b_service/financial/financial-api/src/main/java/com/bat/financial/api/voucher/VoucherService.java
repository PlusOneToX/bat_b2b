package com.bat.financial.api.voucher;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.voucher.dto.VoucherQry;
import com.bat.financial.api.voucher.dto.data.OrderVoucherDTO;
import com.bat.financial.api.voucher.dto.data.VoucherDistributorDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/1 10:37
 */
public interface VoucherService {
    /**
     * 获取收款单
     * 
     * @param qry
     * @return
     */
    PageInfo<VoucherDistributorDTO> listVoucher(VoucherQry qry);

    /**
     * 创建收款单
     *
     * @param orderVoucherDTO
     */
    void createVoucher(OrderVoucherDTO orderVoucherDTO);

    /**
     * 获取收款单详情
     * 
     * @param id
     * @return
     */
    VoucherDistributorDTO getVoucher(Integer id);

    /**
     * 获取收款单详情
     * 
     * @param qry
     * @return
     */
    List<VoucherDistributorDTO> getVoucher(VoucherQry qry);
}
