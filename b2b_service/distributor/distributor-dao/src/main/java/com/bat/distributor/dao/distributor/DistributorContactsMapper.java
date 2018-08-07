package com.bat.distributor.dao.distributor;

import com.bat.distributor.dao.distributor.dataobject.DistributorContactsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorContactsMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByIds(@Param("ids") List<Integer> ids);

    int insert(DistributorContactsDO record);

    void insertList(List<DistributorContactsDO> doList);

    DistributorContactsDO selectByPrimaryKey(Integer id);

    List<DistributorContactsDO> listByDistributorId(Integer distributorId);

    List<DistributorContactsDO> selectAll();

    int updateByPrimaryKey(DistributorContactsDO record);

    void updateList(List<DistributorContactsDO> records);

    DistributorContactsDO selectByPhoneOrEmail(String phoneOrEmail);

    List<DistributorContactsDO> selectByDistributorIdAndOwnerFlag(@Param("distributorId") Integer distributorId,
        @Param("ownerFlag") Short ownerFlag);

    void updatePassword(@Param("id") Integer id, @Param("password") String password);

    List<DistributorContactsDO> listByOpenIdAndFreezeStatus(@Param("openId") String openId,@Param("freezeStatus") Short freezeStatus);

    Integer queryWhetherToFreeze(@Param("distributorId") Integer distributorId);

    void deleteByDistributorId(Integer distributorId);
}