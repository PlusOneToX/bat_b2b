package com.bat.financial.pay.executor;

import javax.annotation.Resource;

import com.bat.financial.dao.pay.PayTradeQrRelationMapper;
import com.bat.financial.dao.pay.dataobject.PayTradeQrRelationDO;
import org.springframework.stereotype.Component;

import com.bat.financial.api.pay.dto.CreateTradeCmd;
import com.bat.financial.api.pay.dto.data.CreateTradeRespDTO;
import com.bat.financial.pay.constant.PayStatus;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/1/6 12:26
 */
@Component
public class PayTradeQrRelationCmdExe {

    @Resource
    private PayTradeQrRelationMapper mapper;

    public void createRelation(CreateTradeCmd cmd, CreateTradeRespDTO.AlipayCreateTradeRespDTO respDTO) {
        try {
            PayTradeQrRelationDO aDo = new PayTradeQrRelationDO();
            aDo.setOutTradeNo(respDTO.getOutTradeNo());
            aDo.setQrCode(respDTO.getCodeUrl());
            aDo.setPayMethod(cmd.getPayMethod());
            aDo.setStatus(PayStatus.WAIT_PAYMENT);
            mapper.insert(aDo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
