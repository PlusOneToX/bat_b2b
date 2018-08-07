package com.bat.financial.voucher;

import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.voucher.executor.VoucherCmdExc;
import com.bat.financial.voucher.executor.VoucherQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.voucher.VoucherService;
import com.bat.financial.api.voucher.dto.VoucherQry;
import com.bat.financial.api.voucher.dto.data.OrderVoucherDTO;
import com.bat.financial.api.voucher.dto.data.VoucherDistributorDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/1 10:38
 */
@Service
public class VoucherServiceImpl implements VoucherService {

    @Resource
    private VoucherQryExc voucherQryExc;

    @Resource
    private VoucherCmdExc voucherCmdExc;

    @Override
    public PageInfo<VoucherDistributorDTO> listVoucher(VoucherQry qry) {
        return voucherQryExc.listVoucherByDistributorId(qry);
    }

    @Override
    public void createVoucher(OrderVoucherDTO orderVoucherDTO) {
        voucherCmdExc.createVoucher(orderVoucherDTO);
    }

    @Override
    public VoucherDistributorDTO getVoucher(Integer id) {
        return voucherQryExc.getVoucherDTO(id);
    }

    @Override
    public List<VoucherDistributorDTO> getVoucher(VoucherQry qry) {
        return voucherQryExc.getVoucherDTO(qry);
    }

}
