package com.bat.distributor.dao.subaccount;

import com.bat.distributor.dao.subaccount.co.SubAccountSalemanPageCO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountSalemanDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorSubAccountSalemanDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorSubAccountSalemanDO record);

    int insertSelective(DistributorSubAccountSalemanDO record);

    DistributorSubAccountSalemanDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorSubAccountSalemanDO record);

    int updateByPrimaryKey(DistributorSubAccountSalemanDO record);

    void updateLevelIdNull(@Param("levelId") Integer levelId,@Param("userId") Integer userId,@Param("userName") String userName);

    List<SubAccountSalemanPageCO> listCOByCondition(@Param("searchType") Short searchType, @Param("content") String content, @Param("distributorId")Integer distributorId,
                                                    @Param("levelId")Integer levelId);

    List<DistributorSubAccountSalemanDO> listByCondition(@Param("distributorId") Integer distributorId,
                                                         @Param("mobile") String mobile,@Param("openFlag") Short openFlag,
                                                         @Param("levelId")Integer levelId);

    void batchCreate(@Param("list") List<DistributorSubAccountSalemanDO> list);
}