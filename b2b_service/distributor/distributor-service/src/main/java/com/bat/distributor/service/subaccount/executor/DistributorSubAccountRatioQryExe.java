package com.bat.distributor.service.subaccount.executor;

import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.api.subaccount.dto.SubAccountLevelRatioDTO;
import com.bat.distributor.dao.subaccount.DistributorSubAccountRatioDOMapper;
import com.bat.distributor.dao.subaccount.co.SubAccountLevelRatioCO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountRatioDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributorSubAccountRatioQryExe {

    @Autowired
    private DistributorSubAccountRatioDOMapper distributorSubAccountRatioDOMapper;

    public List<DistributorSubAccountRatioDO> listBySubAccountConfigId(Integer subAccountConfigId) {
        return distributorSubAccountRatioDOMapper.listBySubAccountConfigId(subAccountConfigId);
    }

    public List<SubAccountLevelRatioDTO> listLevelRatioBySubAccountConfigId(Integer subAccountConfigId) {
        List<SubAccountLevelRatioCO> subAccountLevelRatioCOList = distributorSubAccountRatioDOMapper.listLevelRatioBySubAccountConfigId(subAccountConfigId);
        return BeanUtils.copyList(subAccountLevelRatioCOList,SubAccountLevelRatioDTO.class);
    }



    public List<DistributorSubAccountRatioDO> listBySubAccountConfigIdOrderByLevelSequenceAsc(Integer subAccountConfigId) {
        return distributorSubAccountRatioDOMapper.listBySubAccountConfigIdOrderByLevelSequenceAsc(subAccountConfigId);
    }
}
