package com.bat.thirdparty.message.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.bat.thirdparty.message.dto.VoucherDistributorDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/16 20:20
 */
public class VoucherSyncEvent extends ApplicationEvent {

    private List<VoucherDistributorDO> voucherDistributorDOS;

    public List<VoucherDistributorDO> getVoucherDistributorDOS() {
        return voucherDistributorDOS;
    }

    public void setVoucherDistributorDOS(List<VoucherDistributorDO> voucherDistributorDOS) {
        this.voucherDistributorDOS = voucherDistributorDOS;
    }

    public VoucherSyncEvent(Object source) {
        super(source);
    }
}
