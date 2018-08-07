package com.bat.financial.deposit;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.financial.dao.deposit.dataobject.DepositDistributorDO;
import com.bat.financial.deposit.convertor.DepositConvertor;
import com.bat.financial.deposit.executor.DepositBalanceCmdAsyncExc;
import com.bat.financial.deposit.executor.DepositBalanceCmdExc;
import com.bat.financial.deposit.executor.DepositBalanceQryExc;
import com.bat.financial.message.event.DistributorNameChangeEvent;
import com.bat.financial.Tenant.TenantContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.deposit.DepositBalanceService;
import com.bat.financial.api.deposit.dto.BalanceInfoSyncQry;
import com.bat.financial.api.deposit.dto.DepositAvailableQry;
import com.bat.financial.api.deposit.dto.data.DepositDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;

import lombok.extern.slf4j.Slf4j;

import static com.bat.financial.common.constant.FinancialConstant.COMMON_OPERATE_REPEAT;
import static com.bat.financial.common.constant.FinancialConstant.DEPOSIT_SYNC_LOCK_KEY;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 14:57
 */
@Service
@Slf4j
public class DepositBalanceServiceImpl
    implements DepositBalanceService, ApplicationListener<DistributorNameChangeEvent> {

    @Resource
    private DepositBalanceCmdExc depositCmdExc;

    @Resource
    private DepositBalanceQryExc depositQryExc;

    @Resource
    private DepositBalanceCmdAsyncExc asyncDepositBalanceInfo;

    @CreateCache(name = DEPOSIT_SYNC_LOCK_KEY, cacheType = CacheType.BOTH)
    private Cache<String, Object> syncDepositLock;

    /**
     * 获取余额设置
     *
     * @return
     */
    @Override
    public DepositDTO getDepositSet() {
        return depositQryExc.getDeposit();
    }

    /**
     * 通过分销商id获取余额信息（单个分销商id）
     *
     * @param id
     * @return
     */
    @Override
    public DepositDistributorDTO getDepositBalanceByDistributorId(Integer id) {
        DepositDistributorDO distributorDO = depositQryExc.getDepositBalanceByDistributorId(id);
        if (distributorDO != null) {
            return DepositConvertor.toDepositDistributorDTO(distributorDO);
        }else {
            return null;
        }
    }

    /**
     * 通过分销商ids获取余额信息列表
     *
     * @param ids
     * @return
     */
    @Override
    public List<DepositDistributorDTO> listDepositAvailableByDistributorIds(List<Integer> ids) {
        return depositQryExc.listDepositAvailableByDistributorIds(ids);
    }

    /**
     * 根据后台用户获取分销商余额列表(多个分销商id)
     *
     * @param qry
     * @return
     */
    @Override
    public PageInfo<DepositDistributorDTO> listDepositAvailableByUserId(DepositAvailableQry qry) {
        return depositQryExc.listDepositAvailableByDistributorIdsAndParams(qry);
    }

    @Override
    public void updateDepositBalance(DepositDistributorDTO dto) {
        DepositDistributorDO aDo = DepositConvertor.toDepositDistributorDO(dto);
        depositCmdExc.updateDepositDistributor(aDo);
    }

    /**
     * 同步ERP分销商余额 从ERP拉取
     *
     * @param syncCmd
     */
    @Override
    public void syncDepositBalanceInfo(BalanceInfoSyncQry syncCmd) {
        AutoReleaseLock autoReleaseLock =
                syncDepositLock.tryLock(TenantContext.getTenantNo() + ":syncDepositBalanceInfo", 180, TimeUnit.SECONDS);
        if (autoReleaseLock == null) {
            // 加锁
            throw FinancialException.buildException(COMMON_OPERATE_REPEAT);
        }
        asyncDepositBalanceInfo.asyncDepositBalanceInfo(syncCmd,TenantContext.getTenantNo(),autoReleaseLock);
    }

    /**
     * 分销商修改事件
     *
     * @param distributorNameChangeEvent
     */
    @Override
    public void onApplicationEvent(DistributorNameChangeEvent distributorNameChangeEvent) {
        log.info("{} 收到事件", this);
        System.out.println(distributorNameChangeEvent);
    }

}