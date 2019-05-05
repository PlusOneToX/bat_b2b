package com.bat.thirdparty.factory.api;

import com.bat.thirdparty.common.base.Response;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderQueCmd;

public interface FactoryExpandService {

    Response synchronizedLogisticsByOrderID(FactoryOrderQueCmd data);

    Response factoryTrackingNumber(FactoryOrderQueCmd data, String expressNo);
}
