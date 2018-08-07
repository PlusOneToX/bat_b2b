package com.bat.distributor.service.subaccount;

import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.subaccount.DistributorSubAccountAdminConfigServiceI;
import com.bat.distributor.api.subaccount.DistributorSubAccountLevelServiceI;
import com.bat.distributor.api.subaccount.dto.SubAccountAdminConfigCmd;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountAdminConfigDO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountAdminConfigCmdExe;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountAdminConfigQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistributorSubAccountAdminConfigServiceImpl implements DistributorSubAccountAdminConfigServiceI {

    @Autowired
    private DistributorSubAccountAdminConfigCmdExe distributorSubAccountAdminConfigCmdExe;

    @Autowired
    private DistributorSubAccountAdminConfigQryExe distributorSubAccountAdminConfigQryExe;

    @Autowired
    private DistributorSubAccountLevelServiceI distributorSubAccountLevelServiceI;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response create(SubAccountAdminConfigCmd subAccountAdminConfigCmd, String userId, String userName) {
        distributorSubAccountAdminConfigCmdExe.create(subAccountAdminConfigCmd,userId,userName);
        return Response.buildSuccess();
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response update(SubAccountAdminConfigCmd subAccountAdminConfigCmd, String userId, String userName) {
        distributorSubAccountAdminConfigCmdExe.update(subAccountAdminConfigCmd,userId,userName);
        return Response.buildSuccess();
    }

    /**
     * 根据分销商id查询详情
     * @param distributorId
     * @return
     */
    @Override
    public SubAccountAdminConfigCmd detailByDistributorId(Integer distributorId) {
        DistributorSubAccountAdminConfigDO subAccountAdminConfigDO = distributorSubAccountAdminConfigQryExe.getByDistributorId(distributorId);
        if(subAccountAdminConfigDO ==null){
            return null;
        }
        SubAccountAdminConfigCmd subAccountAdminConfigCmd = BeanUtils.copy(subAccountAdminConfigDO,SubAccountAdminConfigCmd.class);
        List<DistributorSubAccountLevelDO> levelDOList = distributorSubAccountLevelServiceI.listByDistributorId(distributorId);
        if(levelDOList ==null || levelDOList.size() ==0){
            return subAccountAdminConfigCmd;
        }
        List<String> stringList = levelDOList.stream().map(DistributorSubAccountLevelDO::getLevelName).collect(Collectors.toList());
        subAccountAdminConfigCmd.setLevelNameList(stringList);
        return subAccountAdminConfigCmd;
    }
}
