package com.bat.financial.dao.voucher;

import java.util.List;
import java.util.Map;

import com.bat.financial.dao.voucher.dataobject.VoucherDistributorDO;
import org.apache.ibatis.annotations.Param;

public interface VoucherDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VoucherDistributorDO record);

    int insertSelective(VoucherDistributorDO record);

    VoucherDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VoucherDistributorDO record);

    int updateByPrimaryKey(VoucherDistributorDO record);

    List<VoucherDistributorDO> selectByParams(@Param("params") Map<String, Object> map);

    List<VoucherDistributorDO> listVoucherByIds(@Param("voucherIds") List<Integer> voucherIds);

    // List<VoucherDistributorDOExtend> selectByParams(@Param("params") Map<String, Object> map);
}