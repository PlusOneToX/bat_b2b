package com.bat.thirdparty.order.dao.log;

import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrderBusinessLogDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderBusinessLogDO record);

    int insertSelective(OrderBusinessLogDO record);

    OrderBusinessLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderBusinessLogDO record);

    int updateByPrimaryKey(OrderBusinessLogDO record);



    List<OrderBusinessLogDO> listByCondtion(@Param("logType")Short logType,@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("status") Short status
            , @Param("content") String content,@Param("otherOrderNo") String otherOrderNo,@Param("searchType")Short searchType,@Param("platformList")List<String>platformList
            ,Integer distributorId);

    /**
     * 查询第三方推单到B2B列表
     * @param startTime
     * @param endTime
     * @param status
     * @param content
     * @param otherOrderNo
     * @param searchType
     * @param platform
     * @return
     */
    List<OrderBusinessLogDO> listReceiveOrderByCondition(@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("status") Short status
            , @Param("content") String content,@Param("otherOrderNo") String otherOrderNo,@Param("searchType")Short searchType,@Param("platform")String platform,@Param("distributorId") Integer distributorId);


    void deleteByOtherOrderNoAndLogTypeAndDistributorId(@Param("otherOrderNo") String otherOrderNo, @Param("logType") Short logType,@Param("distributorId")Integer distributorId,
                                                        @Param("id")Integer id);
}