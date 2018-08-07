package com.bat.financial.refund.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.financial.dao.refund.RefundCustomerApplyMapper;
import com.bat.financial.dao.refund.RefundDistributorApplyMapper;
import com.bat.financial.dao.refund.dataobject.RefundCustomerApplyDO;
import com.bat.financial.refund.convertor.RefundApplyConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.financial.api.refund.dto.RefundApplyQry;
import com.bat.financial.api.refund.dto.data.RefundApplyDTO;
import com.bat.financial.api.refund.dto.data.RefundCustomerApplyDTO;
import com.bat.financial.api.refund.dto.data.RefundDistributorApplyDTO;
import com.bat.financial.common.CommonServiceImpl;
import com.bat.financial.dao.refund.dataobject.RefundDistributorApplyDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 15:59
 */
@Component
public class RefundApplyQryExc {

    @Resource
    private CommonServiceImpl commonService;

    @Resource
    private RefundDistributorApplyMapper refundDistributorApplyMapper;

    @Resource
    private RefundCustomerApplyMapper refundCustomerApplyMapper;

    public PageInfo<RefundDistributorApplyDTO> listDistributorRefundApply(RefundApplyQry qry) {
        List<Integer> distributorIds = commonService.getDistributorIds(qry.getUserId());
        PageHelper.startPage(qry.getPage(), qry.getSize());
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("distributorIds", distributorIds);
        List<RefundDistributorApplyDO> refundDistributorApplyDOS = refundDistributorApplyMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(refundDistributorApplyDOS);
        List<RefundDistributorApplyDTO> list = RefundApplyConvertor.toRefundDistributorApplyDTOList(pageInfo.getList());
        pageInfo.setList(list);
        return pageInfo;
    }

    public PageInfo<RefundCustomerApplyDTO> listCustomerRefundApply(RefundApplyQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<RefundCustomerApplyDO> refundCustomerApplyDOS = refundCustomerApplyMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(refundCustomerApplyDOS);
        List<RefundCustomerApplyDTO> list = RefundApplyConvertor.toRefundCustomerApplyDTOList(pageInfo.getList());
        pageInfo.setList(list);
        return pageInfo;
    }

    public RefundApplyDTO listRefundApply(RefundApplyQry qry) {
        List<Integer> distributorIds = commonService.getDistributorIds(qry.getUserId());
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("distributorIds", distributorIds);
        List<RefundDistributorApplyDO> refundDistributorApplyDOS = refundDistributorApplyMapper.selectByParams(map);
        List<RefundDistributorApplyDTO> refundDistributorApplyDTOS =
            RefundApplyConvertor.toRefundDistributorApplyDTOList(refundDistributorApplyDOS);
        List<RefundCustomerApplyDO> refundCustomerApplyDOS = refundCustomerApplyMapper.selectByParams(map);
        List<RefundCustomerApplyDTO> refundCustomerApplyDTOS =
            RefundApplyConvertor.toRefundCustomerApplyDTOList(refundCustomerApplyDOS);
        RefundApplyDTO dto = new RefundApplyDTO();
        dto.setRefundCustomerApplyDTOS(refundCustomerApplyDTOS);
        dto.setRefundDistributorApplyDTOS(refundDistributorApplyDTOS);
        return dto;
    }

    public RefundDistributorApplyDO getDistributorRefundApply(Integer id) {
        return refundDistributorApplyMapper.selectByPrimaryKey(id);
    }

    public RefundCustomerApplyDO getCustomerRefundApply(Integer id) {
        return refundCustomerApplyMapper.selectByPrimaryKey(id);
    }
}
