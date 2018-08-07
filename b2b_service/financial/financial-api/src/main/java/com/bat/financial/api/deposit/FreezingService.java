package com.bat.financial.api.deposit;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.dto.FreezableDistributorQry;
import com.bat.financial.api.deposit.dto.FreezableQry;
import com.bat.financial.api.deposit.dto.FreezingDeleteCmd;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorFreezingDTO;
import com.bat.financial.api.deposit.dto.FreezingCreateCmd;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/29 11:54
 */
public interface FreezingService {
    void listFreezableDistributor(FreezableDistributorQry qry);

    void createFreezing(FreezingCreateCmd cmd);

    void createFreezings(List<FreezingCreateCmd> cmds);

    void updateFreezing(FreezingDeleteCmd cmd);

    PageInfo<DepositDistributorFreezingDTO> listFreezing(FreezableQry qry);

    List<DepositDistributorDTO> listDepositAvailableByDistributorIds(List<Integer> id);
}
