package com.bat.distributor.service.subaccount.executor;

import java.util.List;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.dao.subaccount.DistributorSubAccountSalemanDOMapper;
import com.bat.distributor.dao.subaccount.co.SubAccountSalemanPageCO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountSalemanDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.distributor.service.common.CommonErrorCode;
import com.bat.distributor.service.common.DistributorCommonConstant;
import com.bat.distributor.service.common.MessageUtils;

@Component
public class DistributorSubAccountSalemanQryExe {

    @Autowired
    DistributorSubAccountSalemanDOMapper distributorSubAccountSalemanDOMapper;

    @Autowired
    private DistributorSubAccountLevelQryExe distributorSubAccountLevelQryExe;

    public List<SubAccountSalemanPageCO> listCOByCondition(Short searchType, String content, Integer distributorId,
                                                           Integer levelId) {
        return distributorSubAccountSalemanDOMapper.listCOByCondition(searchType, content, distributorId, levelId);
    }

    /**
     * 条件查询分销商业务员
     * 
     * @param distributorId
     * @param mobile
     * @param openFlag
     * @return
     */
    public List<DistributorSubAccountSalemanDO> listByCondition(Integer distributorId, String mobile, Short openFlag,
                                                                Integer sonLevelId) {
        Integer levelId = null;
        if (sonLevelId != null) {
            DistributorSubAccountLevelDO levelDO = distributorSubAccountLevelQryExe.findParentLevel(sonLevelId);
            if (levelDO == null) {
                // 找不到上级
                return null;
            }
            levelId = levelDO.getId();
        }
        List<DistributorSubAccountSalemanDO> dos =
            distributorSubAccountSalemanDOMapper.listByCondition(distributorId, mobile, openFlag, levelId);
        dos.forEach(distributorSubAccountSalemanDO -> {
            DistributorSubAccountLevelDO byId =
                distributorSubAccountLevelQryExe.getById(distributorSubAccountSalemanDO.getLevelId());
            if (byId != null && byId.getDeleteFlag().equals((short)0)) {
                distributorSubAccountSalemanDO.setSequence(byId.getSequence());
            }
        });
        return dos;
    }

    public DistributorSubAccountSalemanDO getById(Integer id) {
        if (id == null) {
            throw DistributorException.buildException(CommonErrorCode.D_COMMON_ID_NULL);
        }
        DistributorSubAccountSalemanDO distributorSubAccountSalemanDO =
            distributorSubAccountSalemanDOMapper.selectByPrimaryKey(id);
        if (distributorSubAccountSalemanDO == null) {
            throw DistributorException.buildException(CommonErrorCode.D_COMMON_ID_ERROR,
                MessageUtils.get(CommonErrorCode.D_COMMON_ID_ERROR) + "【" + id + "】");
        }
        if (DistributorCommonConstant.COMMON_DELETE_FLAG_YES.equals(distributorSubAccountSalemanDO.getDeleteFlag())) {
            throw DistributorException.buildException(CommonErrorCode.D_COMMON_DATA_DEL_ALREADY);
        }
        return distributorSubAccountSalemanDO;
    }

}
