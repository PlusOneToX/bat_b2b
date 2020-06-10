package com.bat.flexible.api.distributor.cooperation;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.distributor.cooperation.dto.FlexibleDistributorCooperationCmd;
import com.bat.flexible.api.distributor.cooperation.dto.FlexibleDistributorCooperationQry;

public interface FlexibleDistributorCooperationServiceI {
    Response create(FlexibleDistributorCooperationCmd flexDistributorCooperationCmd, AdminResponse currentAdmin);

    Response update(FlexibleDistributorCooperationCmd flexDistributorCooperationCmd, AdminResponse currentAdmin);

    PageInfo<FlexibleDistributorCooperationCmd> page(FlexibleDistributorCooperationQry flexDistributorCooperationQry, AdminResponse currentAdmin);

    Response listUsable();

    Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin);

    Response deleteById(Integer id, AdminResponse currentAdmin);
}
