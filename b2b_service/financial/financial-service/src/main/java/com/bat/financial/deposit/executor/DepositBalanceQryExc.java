package com.bat.financial.deposit.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.financial.deposit.convertor.DepositConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.dto.DepositAvailableQry;
import com.bat.financial.api.deposit.dto.data.DepositDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.common.CommonServiceImpl;
import com.bat.financial.dao.deposit.DepositDistributorMapper;
import com.bat.financial.dao.deposit.DepositMapper;
import com.bat.financial.dao.deposit.dataobject.DepositDO;
import com.bat.financial.dao.deposit.dataobject.DepositDistributorDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 15:01
 */
@Component
@Slf4j
public class DepositBalanceQryExc {

    @Resource
    private DepositMapper depositMapper;

    @Resource
    private DepositBalanceCmdExc depositBalanceCmdExc;

    @Resource
    private DepositDistributorMapper depositDistributorMapper;

    @Resource
    private CommonServiceImpl commonService;

    public DepositDTO getDeposit() {
        DepositDO depositDO = depositMapper.selectByPrimaryKey(1);
        return DepositConvertor.toDepositDTO(depositDO);
    }

    /**
     * 根据分销商id 获取分销商余额信息 没有账户新增账户
     *
     * @param id
     * @return
     */
    public DepositDistributorDO getDepositBalanceByDistributorId(Integer id) {
        DepositDistributorDO aDo = depositDistributorMapper.getByDistributorId(id);
        if (aDo == null) {
            return depositBalanceCmdExc
                .createNewDepositDistributorAndReturn(depositBalanceCmdExc.initNewDepositDistributor(id));
        }
        return aDo;
    }

    /**
     * 根据主键id 获取分销商余额信息 没有账户新增账户
     * 
     * @param id
     * @return
     */
    public DepositDistributorDO getDepositAvailableByPrimaryKey(Integer id) {
        DepositDistributorDO aDo = depositDistributorMapper.selectByPrimaryKey(id);
        if (aDo == null) {
            return depositBalanceCmdExc
                .createNewDepositDistributorAndReturn(depositBalanceCmdExc.initNewDepositDistributor(id));
        }
        return aDo;
    }

    /**
     * 根据分销商ids 以及筛选条件 获取分销商余额信息
     *
     * @param qry
     * @return
     */
    public PageInfo<DepositDistributorDTO> listDepositAvailableByDistributorIdsAndParams(DepositAvailableQry qry) {
        // List<Integer> distributorIds = commonService.getDistributorIds(qry.getUserId());
        PageHelper.startPage(qry.getPage(), qry.getSize());
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("distributorIds", null);
        List<DepositDistributorDO> depositDistributorDOS = depositDistributorMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(depositDistributorDOS);
        List<DepositDistributorDTO> depositDistributorDTOS =
            DepositConvertor.toDepositDistributorDTOList(depositDistributorDOS);
        pageInfo.setList(depositDistributorDTOS);
        return pageInfo;
    }

    /**
     * 根据分销商ids 获取分销商余额信息 不筛选 不分页
     *
     * @param ids
     * @return
     */
    public List<DepositDistributorDTO> listDepositAvailableByDistributorIds(List<Integer> ids) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("distributorIds", ids);
        List<DepositDistributorDO> depositDistributorDOS = depositDistributorMapper.selectByParams(map);
        return DepositConvertor.toDepositDistributorDTOList(depositDistributorDOS);
    }

}