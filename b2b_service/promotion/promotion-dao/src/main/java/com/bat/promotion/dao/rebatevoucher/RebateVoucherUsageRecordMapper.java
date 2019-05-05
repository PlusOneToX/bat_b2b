package com.bat.promotion.dao.rebatevoucher;

import java.util.List;
import java.util.Map;

import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherUsageRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RebateVoucherUsageRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RebateVoucherUsageRecordDO record);

    int insertSelective(RebateVoucherUsageRecordDO record);

    RebateVoucherUsageRecordDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RebateVoucherUsageRecordDO record);

    int updateByPrimaryKey(RebateVoucherUsageRecordDO record);

    int batchInsert(@Param("records") List<RebateVoucherUsageRecordDO> recordDOS);

    List<RebateVoucherUsageRecordDO> selectByParams(@Param("params") Map<String,Object> map);

    List<RebateVoucherUsageRecordDO> listByOrderId(Integer orderId);

    void deleteByPrimaryKeys(@Param("ids") List<Integer> ids);
}