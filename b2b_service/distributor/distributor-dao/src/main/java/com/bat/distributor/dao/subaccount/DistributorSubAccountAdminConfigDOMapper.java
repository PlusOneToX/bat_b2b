package com.bat.distributor.dao.subaccount;

import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountAdminConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorSubAccountAdminConfigDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorSubAccountAdminConfigDO record);

    int insertSelective(DistributorSubAccountAdminConfigDO record);

    DistributorSubAccountAdminConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorSubAccountAdminConfigDO record);

    int updateByPrimaryKey(DistributorSubAccountAdminConfigDO record);

    DistributorSubAccountAdminConfigDO getByDistributorId(@Param("distributorId") Integer distributorId);
}