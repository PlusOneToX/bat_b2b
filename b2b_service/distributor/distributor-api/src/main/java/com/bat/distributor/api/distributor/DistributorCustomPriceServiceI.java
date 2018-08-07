package com.bat.distributor.api.distributor;

import com.bat.distributor.api.distributor.dto.data.DistributorCustomPriceDTO;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.distributor.dto.DistributorCustomPriceCmd;

import java.util.List;

public interface DistributorCustomPriceServiceI {
    Response saveList(DistributorCustomPriceCmd distributorCustomPriceCmd, String userId, String userName);

    Response<List<DistributorCustomPriceDTO>> listDTOByDistributorId(Integer distributorId);
}
