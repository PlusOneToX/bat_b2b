package com.bat.system.service.globalsetting;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.globalsetting.AgreementSettingService;
import com.bat.system.api.globalsetting.dto.*;
import com.bat.system.api.globalsetting.dto.data.AgreementSettingDTO;
import com.bat.system.service.globalsetting.executor.AgreementSettingCmdExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.globalsetting.dto.*;
import com.bat.system.service.globalsetting.executor.AgreementSettingQryExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/29 10:36
 */
@Service
@Slf4j
public class AgreementSettingServiceImpl implements AgreementSettingService {

    @Resource
    private AgreementSettingQryExc agreementSettingQryExc;

    @Resource
    private AgreementSettingCmdExc agreementSettingCmdExc;

    @Override
    public AgreementSettingDTO getAgreementSettingById(Integer id) {
        return agreementSettingQryExc.getAgreementSettingById(id);
    }

    @Override
    public AgreementSettingDTO getAgreementSettingByBrandId(AgreementSettingBrandQry qry) {
        return agreementSettingQryExc.getAgreementSettingByBrandId(qry);
    }

    @Override
    public PageInfo<AgreementSettingDTO> listAgreementSetting(AgreementSettingQry qry) {
        return agreementSettingQryExc.listAgreementSetting(qry);
    }

    @Override
    public boolean deleteAgreementSettingById(Integer id) {
        return agreementSettingCmdExc.deleteAgreementSettingById(id);
    }

    @Override
    public boolean updateAgreementSetting(AgreementSettingUpdateCmd cmd) {
        return agreementSettingCmdExc.updateAgreementSetting(cmd);
    }

    @Override
    public boolean createAgreementSetting(AgreementSettingCreateCmd cmd) {
        return agreementSettingCmdExc.createAgreementSetting(cmd);
    }

    @Override
    public boolean signAgreement(AgreementDistributorCmd cmd) {
        return agreementSettingCmdExc.signAgreement(cmd);
    }

    @Override
    public boolean getAgreementSignStatus(AgreementDistributorCmd cmd) {
        return agreementSettingQryExc.getAgreementSignStatus(cmd);
    }

    @Override
    public List<Integer> checkBrandAgreementSetting(CheckBrandAgreementQry qry) {
        return agreementSettingQryExc.checkBrandAgreementSetting(qry);
    }

    @Override
    public AgreementSettingDTO getChinaUserAgreementSetting() {
        return agreementSettingQryExc.getChinaUserAgreementSetting();
    }

    @Override
    public List<AgreementSettingDTO> getAgreementSettingByDistributorId(Integer distributorId) {
        return agreementSettingQryExc.getAgreementSettingByDistributorId(distributorId);
    }
}
