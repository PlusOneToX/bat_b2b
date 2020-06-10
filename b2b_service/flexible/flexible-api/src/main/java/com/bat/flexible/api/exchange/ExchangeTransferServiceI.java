package com.bat.flexible.api.exchange;


import com.bat.flexible.api.exchange.dto.ExchangeCodeTransferRecordDTO;

public interface ExchangeTransferServiceI {

    ExchangeCodeTransferRecordDTO sendOut(Integer userId, String userName, Integer exchangeCodeId);

    void receive(Integer userId, String userName, Integer id);

    ExchangeCodeTransferRecordDTO detail(Integer id);
}
