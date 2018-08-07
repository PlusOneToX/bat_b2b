package com.bat.distributor.service.distributor.validator;

import static com.bat.distributor.service.common.CommonErrorCode.*;
import static com.bat.distributor.service.common.Constant.*;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.distributor.dto.DistributorCmd;
import com.bat.distributor.api.distributor.dto.DistributorContactsCmd;
import com.bat.distributor.dao.distributor.DistributorMapper;
import com.bat.distributor.dao.distributor.dataobject.DistributorDO;
import com.bat.distributor.dao.platform.PlatformMapper;
import com.bat.distributor.dao.platform.dataobject.PlatformDO;
import com.bat.distributor.service.distributor.executor.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/20 16:15
 */
@Component
public class DistributorValidator {

    @Resource
    private DistributorMapper distributorMapper;
    @Resource
    private PlatformMapper platformMapper;

    /**
     * 检查分销商数据准确性
     * 
     * @param cmd
     */
    public void checkDistributor(DistributorCmd cmd) {
        /** 至少有一个账号拥有者联系人，且只能有一个账号拥有者联系人 */
        List<DistributorContactsCmd> contacts = cmd.getContacts();
        if (!CollectionUtils.isEmpty(contacts)) {
            long count = contacts.stream().filter(contact -> contact.getOwnerFlag().equals(OWNER_FLAG_1)).count();
            if (count != 1) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_CONTACT_OWNER_ERROR);
            }
        }
    }

    /**
     * 校验分销商
     *
     * @param distributorId
     */
    public DistributorDO checkDistributor(String distributorId, String platform) {
        if (StringUtils.isBlank(platform)) {
            throw DistributorException.buildException(B_DISTRIBUTOR_PLATFORM_NULL);
        }
        PlatformDO platformDO = platformMapper.selectByPlatformNo(platform);
        if (platformDO == null || platformDO.getOpenFlag().equals(OPEN_NO)) {
            throw DistributorException.buildException(B_DISTRIBUTOR_PLATFORM_NULL_ERROR);
        }
        DistributorDO distributorDO = checkDistributor(distributorId);
        return distributorDO;
    }

    /**
     * 校验分销商
     * 
     * @param distributorId
     */
    public DistributorDO checkDistributor(String distributorId) {
        if (StringUtils.isBlank(distributorId)) {
            throw DistributorException.buildException(B_DISTRIBUTOR_NULL);
        }
        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(Integer.valueOf(distributorId));
        if (distributorDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_NULL);
        }
        if (!distributorDO.getApplyStatus().equals(APPLY_STATUS_2)
            || distributorDO.getFreezeStatus().equals(FREEZE_STATUS_2)) {
            throw DistributorException.buildException(B_DISTRIBUTOR_STATUS_ERROR);
        }
        return distributorDO;
    }

}
