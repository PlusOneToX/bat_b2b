package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorExtendDataDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorExtendDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorExtendDataDO record);

    DistributorExtendDataDO selectByPrimaryKey(Integer id);

    DistributorExtendDataDO getByDistributorId(Integer distributorId);

    List<DistributorExtendDataDO> listByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    List<DistributorExtendDataDO> selectAll();

    int updateByPrimaryKey(DistributorExtendDataDO record);

    void updateDistributionQrUrlByDistributorId(@Param("distributorId") Integer distributorId,
        @Param("distributionQrUrl") String distributionQrUrl);

    void updateLanguageAndCurrencyType(@Param("distributorId") Integer distributorId,
        @Param("language") String language, @Param("currencyType") String currencyType);

    List<DistributorExtendDataDO> listAvailableByErpNos(@Param("erpNos") List<String> erpNos);

    void deleteByDistributorId(Integer distributorId);
}