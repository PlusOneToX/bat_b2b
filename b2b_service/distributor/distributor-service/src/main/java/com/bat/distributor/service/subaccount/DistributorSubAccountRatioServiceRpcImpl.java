package com.bat.distributor.service.subaccount;

import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountRatioDO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountUserConfigDO;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountLevelQryExe;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountUserConfigQryExe;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountRatioQryExe;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.subaccount.DistributorSubAccountRatioServiceRpc;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountRatioRpcDTOQry;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DubboService
public class DistributorSubAccountRatioServiceRpcImpl implements DistributorSubAccountRatioServiceRpc {

    @Autowired
    private DistributorSubAccountRatioQryExe distributorSubAccountRatioQryExe;

    @Autowired
    private DistributorSubAccountLevelQryExe distributorSubAccountLevelQryExe;

    @Autowired
    private DistributorSubAccountUserConfigQryExe distributorSubAccountUserConfigQryExe;
    /**
     * 根据分销商分账配置ID查询分账比例列表、按照等级顺序
     * @param subAccountConfigId
     * @return
     */
    @Override
    public Response<List<DistributorSubAccountRatioRpcDTOQry>> listBySubAccountConfigIdOrderByLevelSequenceAsc(Integer subAccountConfigId) {
        List<DistributorSubAccountRatioDO> ratioDOList = distributorSubAccountRatioQryExe.listBySubAccountConfigIdOrderByLevelSequenceAsc(subAccountConfigId);
        List<DistributorSubAccountRatioRpcDTOQry> rpcDTOQryList = BeanUtils.copyList(ratioDOList, DistributorSubAccountRatioRpcDTOQry.class);
        if(rpcDTOQryList !=null && rpcDTOQryList.size()>0){
            //设置等级名称
            DistributorSubAccountUserConfigDO accountUserConfigDO = distributorSubAccountUserConfigQryExe.getById(subAccountConfigId);
            List<DistributorSubAccountLevelDO> levelDOList = distributorSubAccountLevelQryExe.listByDistributorId(accountUserConfigDO.getDistributorId());
            Map<Integer, DistributorSubAccountLevelDO> levelDOMap = levelDOList.stream().collect(Collectors.toMap(DistributorSubAccountLevelDO::getId,
                    distributorSubAccountLevelDO -> distributorSubAccountLevelDO));
            rpcDTOQryList.stream().forEach(distributorSubAccountRatioRpcDTOQry -> {
                DistributorSubAccountLevelDO levelDO = levelDOMap.get(distributorSubAccountRatioRpcDTOQry.getLevelId());
                if(levelDO !=null){
                    distributorSubAccountRatioRpcDTOQry.setLevelName(levelDO.getLevelName());
                }
            });
        }
        return Response.of(rpcDTOQryList);
    }
}
