package com.bat.promotion.dao.rebatevoucher;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherDO;
import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherStatusDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RebateVoucherMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RebateVoucherDO record);

    int batchInsert(@Param("records") List<RebateVoucherDO> records);

    int batchUpdate(@Param("records") List<RebateVoucherDO> records);

    int insertSelective(RebateVoucherDO record);

    RebateVoucherDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RebateVoucherDO record);

    int updateByPrimaryKey(RebateVoucherDO record);

    List<RebateVoucherDO> selectByParams(@Param("params") Map<String, Object> map);

    void updateListRebateVoucherStatus(List<RebateVoucherStatusDO> statusDOS);

    List<RebateVoucherDO> listRebateVoucherByTime(Date time);

    List<RebateVoucherDO> listRebateVoucherByBatchId(Integer batchId);

    List<RebateVoucherDO> listRebateVoucherByPrimaryKeysAndDistributorId(
        @Param("rebateVoucherIds") List<Integer> rebateVoucherIds, @Param("distributorId") Integer distributorId,
        @Param("time") Date date);

    List<RebateVoucherDO> listRebateVoucherByPrimaryKeys(@Param("rebateVoucherIds")List<Integer> rebateVoucherIds);
}