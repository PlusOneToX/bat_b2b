package com.bat.financial.voucher.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.financial.voucher.convertor.VoucherConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.financial.api.voucher.dto.VoucherQry;
import com.bat.financial.api.voucher.dto.data.VoucherDistributorDTO;
import com.bat.financial.common.CommonServiceImpl;
import com.bat.financial.dao.voucher.VoucherDistributorMapper;
import com.bat.financial.dao.voucher.dataobject.VoucherDistributorDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/5 19:29
 */
@Component
@Slf4j
public class VoucherQryExc {

    @Resource
    private CommonServiceImpl commonService;

    @Resource
    private VoucherDistributorMapper voucherDistributorMapper;

    public PageInfo<VoucherDistributorDTO> listVoucherByDistributorId(VoucherQry qry) {
//        List<Integer> distributorIds = commonService.getDistributorIds(qry.getUserId());
        PageHelper.startPage(qry.getPage(), qry.getSize());
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
//        map.put("distributorIds", distributorIds);
        map.put("startTime", qry.getStartTime());
        map.put("endTime", qry.getEndTime());
        List<VoucherDistributorDO> voucherDistributorDOS = voucherDistributorMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(voucherDistributorDOS);
        List<VoucherDistributorDTO> toCurrencyRateDTOList =
            VoucherConvertor.toVoucherDistributorDTOList(pageInfo.getList());
        pageInfo.setList(toCurrencyRateDTOList);
        return pageInfo;
    }

    public VoucherDistributorDTO getVoucherDTO(Integer id) {
        VoucherDistributorDO voucherDistributorDO = voucherDistributorMapper.selectByPrimaryKey(id);
        return VoucherConvertor.toVoucherDistributorDTO(voucherDistributorDO);
    }

    public VoucherDistributorDO getVoucherDOById(Integer id) {
        return voucherDistributorMapper.selectByPrimaryKey(id);
    }

    public List<VoucherDistributorDTO> getVoucherDTO(VoucherQry qry) {
        BeanMap map = BeanMap.create(qry);
        List<VoucherDistributorDO> voucherDistributorDOS = voucherDistributorMapper.selectByParams(map);
        List<VoucherDistributorDTO> toCurrencyRateDTOList =
            VoucherConvertor.toVoucherDistributorDTOList(voucherDistributorDOS);
        return toCurrencyRateDTOList;
    }

    public List<VoucherDistributorDO> listVoucherById(List<Integer> voucherIds) {
        return voucherDistributorMapper.listVoucherByIds(voucherIds);
    }
}
