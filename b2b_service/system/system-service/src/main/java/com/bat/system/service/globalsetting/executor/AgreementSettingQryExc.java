package com.bat.system.service.globalsetting.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.globalsetting.dto.AgreementDistributorCmd;
import com.bat.system.api.globalsetting.dto.AgreementSettingBrandQry;
import com.bat.system.api.globalsetting.dto.AgreementSettingQry;
import com.bat.system.api.globalsetting.dto.CheckBrandAgreementQry;
import com.bat.system.api.globalsetting.dto.data.AgreementSettingDTO;
import com.bat.system.dao.globalsetting.AgreementDistributorMapper;
import com.bat.system.dao.globalsetting.AgreementSettingBrandMapper;
import com.bat.system.dao.globalsetting.AgreementSettingMapper;
import com.bat.system.dao.globalsetting.dataobject.AgreementDistributorDO;
import com.bat.system.dao.globalsetting.dataobject.AgreementSettingBrandDO;
import com.bat.system.dao.globalsetting.dataobject.AgreementSettingDO;
import com.bat.system.service.globalsetting.convertor.AgreementSettingConvertor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class AgreementSettingQryExc {
    @Resource
    private AgreementSettingMapper agreementSettingMapper;

    @Resource
    private AgreementDistributorMapper agreementDistributorMapper;

    /**
     * 说明 AgreementSettingBrand 是之前没有的表，就是把AgreementSetting 中的brandId 拆分开来，借助了唯一索引的功能（其实感觉不用拆开）
     */
    @Resource
    private AgreementSettingBrandMapper agreementSettingBrandMapper;

    public AgreementSettingDTO getAgreementSettingById(Integer id) {
        AgreementSettingDO aDo = agreementSettingMapper.selectByPrimaryKey(id);
        return AgreementSettingConvertor.toAgreementSettingDTO(aDo);
    }

    public PageInfo<AgreementSettingDTO> listAgreementSetting(AgreementSettingQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<AgreementSettingDO> agreementSettingDOS =
            agreementSettingMapper.listByAgreementAreaAndType(qry.getAgreementArea(), qry.getType());
        PageInfo pageInfo = new PageInfo(agreementSettingDOS);
        List<AgreementSettingDTO> toRoleDTOList =
            AgreementSettingConvertor.toAgreementSettingDTOList(pageInfo.getList());
        pageInfo.setList(toRoleDTOList);
        return pageInfo;
    }

    public List<Integer> checkBrandAgreementSetting(CheckBrandAgreementQry qry) {
        List<AgreementSettingBrandDO> agreementSettingBrandDOS =
            agreementSettingBrandMapper.listByArea(qry.getAgreementArea());
        return agreementSettingBrandDOS.stream().map(AgreementSettingBrandDO::getBrandId).collect(Collectors.toList());
    }

    public AgreementSettingDTO getAgreementSettingByBrandId(AgreementSettingBrandQry qry) {
        AgreementSettingBrandDO brandDO =
            agreementSettingBrandMapper.getByBrandIdArea(qry.getBrandId(), qry.getAgreementArea());
        if (brandDO == null) {
            // throw SystemException.buildException(ErrorCode.B_BASE_SETTING_AGREEMENT_SETTING_NULL);
            return null;
        }
        AgreementSettingDO aDo = agreementSettingMapper.selectByPrimaryKey(brandDO.getAgreementSettingId());
        if (aDo.getStatus() == (short)1) {
            return AgreementSettingConvertor.toAgreementSettingDTO(aDo);
        } else {
            throw SystemException.buildException(ErrorCode.B_BASE_SETTING_AGREEMENT_SETTING_NULL);
        }
    }

    public AgreementSettingDTO getChinaUserAgreementSetting() {
        AgreementSettingDO aDo = agreementSettingMapper.getChinaUserAgreementSetting();
        if (aDo.getStatus() == (short)1) {
            return AgreementSettingConvertor.toAgreementSettingDTO(aDo);
        } else {
            throw SystemException.buildException(ErrorCode.B_BASE_SETTING_AGREEMENT_SETTING_NULL);
        }
    }

    public boolean getAgreementSignStatus(AgreementDistributorCmd cmd) {
        AgreementDistributorDO aDo =
            agreementDistributorMapper.getByAgreementIdAndDistributorId(cmd.getAgreementId(), cmd.getDistributorId());
        return aDo != null;
    }

    public List<AgreementSettingDTO> getAgreementSettingByDistributorId(Integer distributorId) {
        List<AgreementDistributorDO> agreementDistributorDOS =
            agreementDistributorMapper.listByDistributorId(distributorId);
        List<AgreementSettingDTO> dtoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(agreementDistributorDOS)) {
            return dtoList;
        }
        List<Integer> agreementIds = agreementDistributorDOS.stream().map(AgreementDistributorDO::getAgreementId)
            .distinct().collect(Collectors.toList());
        List<AgreementSettingDO> agreementSettingDOS = agreementSettingMapper.listByPrimaryKey(agreementIds);
        return AgreementSettingConvertor.toAgreementSettingDTOList(agreementSettingDOS);
    }
}
