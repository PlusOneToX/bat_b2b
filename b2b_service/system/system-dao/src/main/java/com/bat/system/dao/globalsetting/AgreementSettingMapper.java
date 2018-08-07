package com.bat.system.dao.globalsetting;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bat.system.dao.globalsetting.dataobject.AgreementSettingDO;

public interface AgreementSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AgreementSettingDO record);

    int insertSelective(AgreementSettingDO record);

    AgreementSettingDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AgreementSettingDO record);

    int updateByPrimaryKeyWithBLOBs(AgreementSettingDO record);

    int updateByPrimaryKey(AgreementSettingDO record);

    List<AgreementSettingDO> listByAgreementAreaAndType(@Param("agreementArea") Short agreementArea,
        @Param("type") Short type);

    AgreementSettingDO getChinaUserAgreementSetting();

    List<AgreementSettingDO> listByPrimaryKey(@Param("agreementIds")List<Integer> agreementIds);
}