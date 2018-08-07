package com.bat.distributor.api.electricity;

import com.bat.distributor.api.base.Response;

public interface DistributorElectricityRelationMappingServiceI {
    Response getBySellerNick(String sellerNick);
}
