package com.bat.distributor.service.subaccount.executor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.bat.distributor.api.subaccount.DistributorSubAccountLevelServiceI;
import com.bat.distributor.api.subaccount.DistributorSubAccountRatioServiceI;
import com.bat.distributor.api.subaccount.dto.SubAccountUserConfigCmd;
import com.bat.distributor.api.subaccount.dto.SubAccountUserUpdateCmd;
import com.bat.distributor.dao.subaccount.DistributorSubAccountUserConfigDOMapper;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountUserConfigDO;
import com.bat.distributor.service.subaccount.convertor.SubAccountConvertor;
import com.bat.distributor.service.subaccount.validator.SubAccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.distributor.service.common.DistributorCommonConstant;

@Component
public class DistributorSubAccountUserConfigCmdExe {

    @Autowired
    private DistributorSubAccountUserConfigDOMapper distributorSubAccountUserConfigDOMapper;

    @Autowired
    private DistributorSubAccountRatioServiceI distributorSubAccountRatioServiceI;

    @Autowired
    private DistributorSubAccountLevelServiceI distributorSubAccountLevelServiceI;

    @Autowired
    private SubAccountValidator subAccountValidator;

    @Transactional(rollbackFor = Exception.class)
    public void create(SubAccountUserConfigCmd subAccountUserConfigCmd, String userId, String userName) {

        List<DistributorSubAccountLevelDO> levelDOList =
            distributorSubAccountLevelServiceI.listByDistributorId(Integer.parseInt(userId));

        SubAccountValidator.validateRatio(subAccountUserConfigCmd.getLevelRatioList(), levelDOList);
        // 判断分账比例和是否小于微信支付账户

        BigDecimal sum = SubAccountConvertor.sumRatio(subAccountUserConfigCmd.getLevelRatioList());
        subAccountValidator.validateRatioSum(sum, Integer.parseInt(userId), subAccountUserConfigCmd.getAmountType());
        DistributorSubAccountUserConfigDO userConfigDO = new DistributorSubAccountUserConfigDO();
        userConfigDO.setDistributorId(Integer.parseInt(userId));
        userConfigDO.setAmountType(subAccountUserConfigCmd.getAmountType());
        userConfigDO.setName(subAccountUserConfigCmd.getName());
        setAdminMsg(userConfigDO, userId, userName);
        userConfigDO.setDeleteFlag(DistributorCommonConstant.COMMON_DELETE_FLAG_NO);
        distributorSubAccountUserConfigDOMapper.insert(userConfigDO);
        distributorSubAccountRatioServiceI.create(userConfigDO.getId(), userId, userName,
            subAccountUserConfigCmd.getLevelRatioList());
    }

    private void setAdminMsg(DistributorSubAccountUserConfigDO userConfigDO, String userId, String userName) {
        if (userConfigDO.getId() == null) {
            userConfigDO.setCreateTime(new Date());
            userConfigDO.setCreateUserId(Integer.parseInt(userId));
            userConfigDO.setCreateUserName(userName);
        }
        userConfigDO.setUpdateTime(new Date());
        userConfigDO.setUpdateUserId(Integer.parseInt(userId));
        userConfigDO.setUpdateUserName(userName);
    }

    public void update(SubAccountUserUpdateCmd subAccountUserUpdateCmd, String userId, String userName) {
        DistributorSubAccountUserConfigDO userConfigDO =
            distributorSubAccountUserConfigDOMapper.selectByPrimaryKey(subAccountUserUpdateCmd.getId());
        List<DistributorSubAccountLevelDO> levelDOList =
            distributorSubAccountLevelServiceI.listByDistributorId(userConfigDO.getDistributorId());

        SubAccountValidator.validateRatio(subAccountUserUpdateCmd.getLevelRatioList(), levelDOList);
        // 判断分账比例和是否小于微信支付账户

        BigDecimal sum = SubAccountConvertor.sumRatio(subAccountUserUpdateCmd.getLevelRatioList());
        subAccountValidator.validateRatioSum(sum, Integer.parseInt(userId), subAccountUserUpdateCmd.getAmountType());
        userConfigDO.setAmountType(subAccountUserUpdateCmd.getAmountType());
        userConfigDO.setName(subAccountUserUpdateCmd.getName());
        setAdminMsg(userConfigDO, userId, userName);
        userConfigDO.setDeleteFlag(DistributorCommonConstant.COMMON_DELETE_FLAG_NO);
        distributorSubAccountUserConfigDOMapper.updateByPrimaryKey(userConfigDO);
        distributorSubAccountRatioServiceI.update(userConfigDO.getId(), userId, userName,
            subAccountUserUpdateCmd.getLevelRatioList());
    }

    public void updateDO(DistributorSubAccountUserConfigDO userConfigDO) {
        distributorSubAccountUserConfigDOMapper.updateByPrimaryKey(userConfigDO);
    }
}
