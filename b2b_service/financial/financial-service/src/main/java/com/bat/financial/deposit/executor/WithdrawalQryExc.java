package com.bat.financial.deposit.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.financial.deposit.convertor.FreezingConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.dto.WithdrawalQry;
import com.bat.financial.api.deposit.dto.data.WithdrawDepositsDistributorApplyDTO;
import com.bat.financial.common.CommonServiceImpl;
import com.bat.financial.dao.deposit.WithdrawDepositsDistributorApplyMapper;
import com.bat.financial.dao.deposit.dataobject.WithdrawDepositsDistributorApplyDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/9 21:52
 */
@Component
public class WithdrawalQryExc {

    @Resource
    private CommonServiceImpl commonService;

    @Resource
    private WithdrawDepositsDistributorApplyMapper withdrawMapper;

    public PageInfo<WithdrawDepositsDistributorApplyDTO> listWithdrawal(WithdrawalQry qry) {
        List<Integer> distributorIds = commonService.getDistributorIds(qry.getUserId());
        PageHelper.startPage(qry.getPage(), qry.getSize());
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("distributorIds", distributorIds);
        List<WithdrawDepositsDistributorApplyDO> dos = withdrawMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(dos);
        List<WithdrawDepositsDistributorApplyDTO> applyDTOS =
            FreezingConvertor.toWithdrawDepositsDistributorApplyDTOList(pageInfo.getList());
        pageInfo.setList(applyDTOS);
        return pageInfo;
    }
}
