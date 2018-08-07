package com.bat.system.dao.globalsetting;

import java.util.List;

import com.bat.system.dao.globalsetting.dataobject.AgreementSettingBrandDO;
import org.apache.ibatis.annotations.Param;

public interface AgreementSettingBrandMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AgreementSettingBrandDO record);

    int insertSelective(AgreementSettingBrandDO record);

    AgreementSettingBrandDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AgreementSettingBrandDO record);

    int updateByPrimaryKey(AgreementSettingBrandDO record);

    List<AgreementSettingBrandDO> listByArea(@Param("agreementArea") Short agreementArea);

    AgreementSettingBrandDO getByBrandIdArea(@Param("brandId") Integer brandId,
        @Param("agreementArea") Short agreementArea);

    void deleteByAgreementSettingId(Integer id);

    void deleteByBrandIdsAndArea(@Param("brandId") List<String> branIds, @Param("agreementArea") Short agreementArea);
}