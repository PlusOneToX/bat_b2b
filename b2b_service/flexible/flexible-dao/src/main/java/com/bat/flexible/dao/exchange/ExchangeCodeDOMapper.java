package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.co.ExchangeCodePageCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ExchangeCodeDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeCodeDO record);

    int insertSelective(ExchangeCodeDO record);

    ExchangeCodeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeCodeDO record);

    int updateByPrimaryKey(ExchangeCodeDO record);

    List<ExchangeCodeDO> listByExchangeId(@Param("exchangeId")Integer exchangeId);

    void createEndExchangeEvent(@Param("time") String time, @Param("exchangeId") Integer exchangeId);

    List<ExchangeCodeDO> listByOutboundNoGroupByBoxCode(@Param("outboundNo") String outboundNo);

    List<ExchangeCodeDO> listByBoxCode(@Param("boxCode") String boxCode);

    ExchangeCodeDO findByPlainCodeAndSecretCode(@Param("plainCode") String plainCode,@Param("secretCode") String secretCode);

    List<ExchangeCodeDO> listByBoxCodeAndPlainCodeList(@Param("boxCode") String boxCode, @Param("plainCodeList") List<String> plainCodeList);

    void updateExchangeFactoryId(@Param("exchangeId") Integer exchangeId,@Param("factoryId") Integer factoryId);

    ExchangeCodeDO findMaxByRiseValue(@Param("riseValue") String riseValue);

    List<ExchangeCodeDO> findByUserOrderIdAndStatusOrderByExchangeIdAsc(@Param("userOrderId") Integer userOrderId,@Param("status") Short status);

    List<ExchangeCodeDO> countByBoxCodeAndItemId(@Param("itemId") Integer itemId);

    List<ExchangeCodeDO> countByBoxCodeIsNotNull(@Param("itemId") Integer itemId);

    List<ExchangeCodePageCO> listCOByCondition(@Param("exchangeFactoryId") Integer exchangeFactoryId, @Param("exchangeId") Integer exchangeId,
                                               @Param("exchangeWay") Short exchangeWay, @Param("status") Short status, @Param("content") String content, @Param("exchangeExportId")Integer exchangeExportId);

    List<ExchangeCodeDO> listByExchangeCodeIdListAndMaterialIdAndModelId(@Param("materialId") Integer materialId,
                                                                         @Param("modelId") Integer modelId,@Param("exchangeCodeIdList") List<Integer> exchangeCodeIdList);

    List<ExchangeCodeDO> listByOutboundNoGroupByDistributorOrderId(@Param("outboundNo") String outboundNo);

    List<ExchangeCodeDO> listBySecretCodeList(@Param("secretCodeList") List<String> secretCodeList);

    List<ExchangeCodeDO> listByUserOrderId(@Param("orderId") Integer orderId);

    void batchCreate(@Param("exchangeCodeList") List<ExchangeCodeDO> exchangeCodeList);

    void batchCreateContainKey(@Param("exchangeCodeList")List<ExchangeCodeDO> exchangeCodeList);

    /**
     * 拿来处理脏数据
     * @return
     */
    List<ExchangeCodeDO> listAll2();

    /**
     * 拿来处理迁移的数据、不要调用
     * @param oldId
     * @param newId
     */
    void updateId(@Param("oldId") Integer oldId, @Param("newId") Integer newId);

    void batchUpdate(@Param("updateList") List<ExchangeCodeDO> updateList);

    List<ExchangeCodeDO> listByIds(@Param("ids")List<Integer> ids);

    List<ExchangeCodeDO> listByExportId(@Param("exchangeExportId")Integer exchangeExportId);

    /**
     * 后台兑换码解绑
     * @param record
     * @return
     */
    int unboundExchange(ExchangeCodeDO record);
    //后台兑换码解绑子属性 判断兑换卡是否被领取
    ExchangeCodeDO selectExchangeUserRecordById(ExchangeCodeDO record);
}