package com.bat.system.dao.globalsetting;

import org.apache.ibatis.annotations.Param;

import com.bat.system.dao.globalsetting.dataobject.AgreementDistributorDO;

import java.util.List;

public interface AgreementDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AgreementDistributorDO record);

    int insertSelective(AgreementDistributorDO record);

    AgreementDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AgreementDistributorDO record);

    int updateByPrimaryKey(AgreementDistributorDO record);

    AgreementDistributorDO getByAgreementIdAndDistributorId(@Param("agreementId") Integer agreementId,
        @Param("distributorId") Integer distributorId);

    List<AgreementDistributorDO> listByDistributorId(@Param("distributorId") Integer distributorId);
}