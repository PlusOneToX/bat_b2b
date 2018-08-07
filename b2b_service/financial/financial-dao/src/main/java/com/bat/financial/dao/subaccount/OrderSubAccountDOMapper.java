package com.bat.financial.dao.subaccount;

import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrderSubAccountDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderSubAccountDO record);

    int insertSelective(OrderSubAccountDO record);

    OrderSubAccountDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderSubAccountDO record);

    int updateByPrimaryKey(OrderSubAccountDO record);

    List<OrderSubAccountDO> pageByCondition(@Param("distributorId") Integer distributorId, @Param("status") Short status,@Param("orderNo") String orderNo,
                                            @Param("subAccountFailFlag") Short subAccountFailFlag, @Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime, @Param("contentType") Short contentType,@Param("content") String content);

    List<OrderSubAccountDO> listByTransactionId(@Param("transactionId") String transactionId);

    OrderSubAccountDO getByOrderId(@Param("orderId") Integer orderId);

    List<OrderSubAccountDO> listSubAccountUsable();
}