package com.bat.system.service.globalsetting.executor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.globalsetting.dto.AgreementDistributorCmd;
import com.bat.system.api.globalsetting.dto.AgreementSettingCreateCmd;
import com.bat.system.api.globalsetting.dto.AgreementSettingUpdateCmd;
import com.bat.system.dao.globalsetting.AgreementDistributorMapper;
import com.bat.system.dao.globalsetting.AgreementSettingBrandMapper;
import com.bat.system.dao.globalsetting.AgreementSettingMapper;
import com.bat.system.dao.globalsetting.dataobject.AgreementDistributorDO;
import com.bat.system.dao.globalsetting.dataobject.AgreementSettingBrandDO;
import com.bat.system.dao.globalsetting.dataobject.AgreementSettingDO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.system.service.globalsetting.constant.AgreementType;
import com.bat.system.service.globalsetting.convertor.AgreementSettingConvertor;
import com.bat.system.service.organization.executor.ErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 关于协议去重这块 有点复杂
 * @date: 2018/4/10 16:53
 */
@Component
@Slf4j
public class AgreementSettingCmdExc {

    @Resource
    private AgreementSettingMapper agreementSettingMapper;

    @Resource
    private AgreementDistributorMapper agreementDistributorMapper;

    /**
     * 说明 AgreementSettingBrand 是之前没有的表，就是把AgreementSetting 中的brandId 拆分开来，借助了唯一索引的功能（其实感觉不用拆开）
     */
    @Resource
    private AgreementSettingBrandMapper agreementSettingBrandMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean createAgreementSetting(AgreementSettingCreateCmd cmd) {
        AgreementSettingDO aDo = AgreementSettingConvertor.toAgreementSettingDO(cmd);
        // 用户协议 国内国外各只能有一份
        if (cmd.getType() == AgreementType.USER_AGREEMENT) {
            List<AgreementSettingDO> agreementSettingDOS =
                agreementSettingMapper.listByAgreementAreaAndType(cmd.getAgreementArea(), cmd.getType());
            agreementSettingDOS.forEach(agreementSettingDO -> {
                agreementSettingMapper.deleteByPrimaryKey(agreementSettingDO.getId());
            });
        }
        agreementSettingMapper.insert(aDo);

        if (cmd.getType().equals(AgreementType.BRAND_AGREEMENT)) {
            String[] split = cmd.getBrandId().split(",");
            List<String> branIds = Arrays.asList(split);
            agreementSettingBrandMapper.deleteByBrandIdsAndArea(branIds, cmd.getAgreementArea());
            for (String branId : branIds) {
                try {
                    AgreementSettingBrandDO brandDO = new AgreementSettingBrandDO();
                    brandDO.setAgreementArea(cmd.getAgreementArea());
                    brandDO.setAgreementSettingId(aDo.getId());
                    brandDO.setBrandId(Integer.valueOf(branId));
                    agreementSettingBrandMapper.insert(brandDO);
                } catch (DuplicateKeyException e) {
                    final String localizedMessage = e.getLocalizedMessage();
                    log.error("品牌：{}", branId);
                    log.error(e.getLocalizedMessage());
                    if (localizedMessage.contains("Duplicate entry")
                        && localizedMessage.contains("uk_agreement_area_brand")) {
                        throw SystemException.buildException(ErrorCode.B_AGREEMENT_EXISTS);
                    }
                }
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAgreementSettingById(Integer id) {
        agreementSettingMapper.deleteByPrimaryKey(id);
        agreementSettingBrandMapper.deleteByAgreementSettingId(id);
        return true;
    }

    public boolean updateAgreementSetting(AgreementSettingUpdateCmd cmd) {
        AgreementSettingDO aDo = AgreementSettingConvertor.toAgreementSettingDO(cmd);
        agreementSettingMapper.updateByPrimaryKeySelective(aDo);
        return true;
    }

    public boolean signAgreement(AgreementDistributorCmd cmd) {
        AgreementDistributorDO aDo = new AgreementDistributorDO();
        aDo.setAgreementId(cmd.getAgreementId());
        aDo.setDistributorId(cmd.getDistributorId());
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        agreementDistributorMapper.insert(aDo);
        return true;
    }
}
