package com.bat.financial.deposit.executor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.financial.deposit.constant.ChangeType;
import com.bat.financial.deposit.convertor.DepositConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.financial.api.deposit.dto.DepositDetailAdminQry;
import com.bat.financial.api.deposit.dto.DepositDetailSummaryUserQry;
import com.bat.financial.api.deposit.dto.DepositDetailUserQry;
import com.bat.financial.api.deposit.dto.data.DepositDetailSummaryDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.common.CommonServiceImpl;
import com.bat.financial.dao.deposit.DepositDistributorSubsidiaryBookMapper;
import com.bat.financial.dao.deposit.dataobject.DepositDistributorSubsidiaryBookDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 15:01
 */
@Component
@Slf4j
public class DepositDetailQryExc {

    @Resource
    private CommonServiceImpl commonService;

    @Resource
    private DepositDistributorSubsidiaryBookMapper depositDistributorSubsidiaryBookMapper;

    /**
     * 根据分销商id获取余额明细即资产明细
     *
     * @param qry
     * @return
     */
    public PageInfo<DepositDistributorSubsidiaryBookDTO>
        listDepositDetailByDistributorIdAndParams(DepositDetailUserQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
        // 单业务员
        map.put("distributorIds", Collections.singletonList(qry.getDistributorId()));
        map.put("startTime", qry.getStartTime());
        map.put("endTime", qry.getEndTime());
        List<DepositDistributorSubsidiaryBookDO> depositDistributorSubsidiaryBookDOS =
            depositDistributorSubsidiaryBookMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(depositDistributorSubsidiaryBookDOS);
        List<DepositDistributorSubsidiaryBookDTO> list =
            DepositConvertor.toDepositDistributorSubsidiaryBookDOList(pageInfo.getList());
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * 根据分销商dis 获取分销商余额明细
     * 
     * @param qry
     * @return
     */
    public PageInfo<DepositDistributorSubsidiaryBookDTO>
        listDepositDetailByDistributorIdsAndParams(DepositDetailAdminQry qry) {
        // 多业务员
        if (qry.getUserId() == null && qry.getDistributorId() != null) {
            DistributorRpcDTO distributorInfo = commonService.getDistributorInfo(qry.getDistributorId());
            qry.setUserId(distributorInfo.getSalesId());
        }
        List<Integer> distributorIds = commonService.getDistributorIds(qry.getUserId());
        PageHelper.startPage(qry.getPage(), qry.getSize());
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("distributorIds", distributorIds);
        List<DepositDistributorSubsidiaryBookDO> depositDistributorSubsidiaryBookDOS =
            depositDistributorSubsidiaryBookMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(depositDistributorSubsidiaryBookDOS);
        List<DepositDistributorSubsidiaryBookDTO> list =
            DepositConvertor.toDepositDistributorSubsidiaryBookDOList(pageInfo.getList());
        pageInfo.setList(list);
        return pageInfo;
    }

    public DepositDetailSummaryDTO listDepositDetailSummary(DepositDetailSummaryUserQry qry) {
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
        DepositDetailSummaryDTO dto = new DepositDetailSummaryDTO();
        // 单业务员
        map.put("distributorIds", Collections.singletonList(qry.getDistributorId()));
        map.put("startTime", qry.getStartTime());
        map.put("endTime", qry.getEndTime());
        List<DepositDistributorSubsidiaryBookDO> bookDOS = depositDistributorSubsidiaryBookMapper.selectByParams(map);
        // 汇总数据
        bookDOS.forEach(aDo -> {
            if (aDo.getChangeType() == ChangeType.INCREASE) {
                // 总收入
                dto.setTotalRevenue(dto.getTotalRevenue().add(aDo.getAmount()));
            } else if (aDo.getChangeType() == ChangeType.DECREASE) {
                // 总支出
                dto.setTotalExpenditure(dto.getTotalExpenditure().add(aDo.getAmount()));
            }
        });
        return dto;
    }
}
