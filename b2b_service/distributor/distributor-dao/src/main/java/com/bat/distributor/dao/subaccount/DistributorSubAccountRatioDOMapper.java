package com.bat.distributor.dao.subaccount;

import com.bat.distributor.dao.subaccount.co.SubAccountLevelRatioCO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountRatioDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorSubAccountRatioDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorSubAccountRatioDO record);

    int insertSelective(DistributorSubAccountRatioDO record);

    DistributorSubAccountRatioDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorSubAccountRatioDO record);

    int updateByPrimaryKey(DistributorSubAccountRatioDO record);

    void batchCreate(@Param("ratioList") List<DistributorSubAccountRatioDO> ratioDOList);

    List<DistributorSubAccountRatioDO> listBySubAccountConfigId(@Param("subAccountConfigId") Integer subAccountConfigId);

    void batchUpdate(@Param("updateList") List<DistributorSubAccountRatioDO> updateList);

    List<SubAccountLevelRatioCO> listLevelRatioBySubAccountConfigId(@Param("subAccountConfigId")Integer subAccountConfigId);

    void deleteByLevelId(@Param("levelId") Integer levelId,@Param("userId") Integer userId, @Param("userName") String userName);

    List<DistributorSubAccountRatioDO> listBySubAccountConfigIdOrderByLevelSequenceAsc(@Param("subAccountConfigId")Integer subAccountConfigId);
}