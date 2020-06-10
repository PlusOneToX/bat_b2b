package com.bat.flexible.manager.exchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.bat.flexible.manager.exchange.executor.ExchangeCardTransferQryExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCodeQryExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCodeTransferRecordCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCodeTransferRecordQryExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeUserCmdExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeUserQryExe;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.distributor.customer.api.CustomerServiceRpc;
import com.bat.dubboapi.distributor.customer.dto.data.CustomerRpcDTO;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.api.exchange.ExchangeTransferServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeCodeTransferRecordDTO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardTransferDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeTransferRecordDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeUserDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeCodeConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;

@Service
public class ExchangeTransferServiceImpl implements ExchangeTransferServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeTransferServiceImpl.class);

    @Autowired
    private ExchangeCodeUserQryExe exchangeCodeUserQryExe;

    @Autowired
    private ExchangeCodeTransferRecordCmdExe exchangeCodeTransferRecordCmdExe;

    @Autowired
    private ExchangeCodeTransferRecordQryExe exchangeCodeTransferRecordQryExe;

    @Autowired
    private ExchangeCodeQryExe exchangeCodeQryExe;

    @Autowired
    private ExchangeCardTransferQryExe exchangeCardTransferQryExe;

    @Autowired
    private ExchangeCodeUserCmdExe exchangeCodeUserCmdExe;

    @DubboReference(check = false, timeout = 5000)
    private CustomerServiceRpc customerServiceRpc;

    @CreateCache(name = FlexibleKeyConstant.EXCHANGE_TRANSFER_PRE)
    private Cache<String, Integer> exchangeTransferCache;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExchangeCodeTransferRecordDTO sendOut(Integer userId, String userName, Integer exchangeCodeId) {
        if (userId <= 0) {
            throw new FlexibleCustomException("用户未登录");
        }
        ExchangeCardTransferDO exchangeCardTransferDO = checkAndGetData(userId, exchangeCodeId);
        ExchangeCodeTransferRecordDTO exchangeCodeTransferRecordDTO = new ExchangeCodeTransferRecordDTO();
        BeanUtils.copyProperties(exchangeCardTransferDO, exchangeCodeTransferRecordDTO);
        ExchangeCodeTransferRecordDO exchangeCodeTransferRecordDO =
            exchangeCodeTransferRecordQryExe.selectByCondition(exchangeCodeId, userId, FlexibleCommonConstant.FLAG_NO);
        if (exchangeCodeTransferRecordDO == null) {
            exchangeCodeTransferRecordDO = new ExchangeCodeTransferRecordDO();
            exchangeCodeTransferRecordDO.setExchangeId(exchangeCardTransferDO.getExchangeId());
            exchangeCodeTransferRecordDO.setExchangeCodeId(exchangeCodeId);
            exchangeCodeTransferRecordDO.setFromUserId(userId);
            exchangeCodeTransferRecordDO.setCreateTime(new Date());
            exchangeCodeTransferRecordDO.setReceiveFlag(FlexibleCommonConstant.FLAG_NO);
            exchangeCodeTransferRecordCmdExe.insert(exchangeCodeTransferRecordDO);
        }
        BeanUtils.copyProperties(exchangeCodeTransferRecordDO, exchangeCodeTransferRecordDTO);
        List<Integer> userIds = new ArrayList<>();
        userIds.add(exchangeCodeTransferRecordDO.getFromUserId());
        List<CustomerRpcDTO> customers = customerServiceRpc.listByIds(userIds).getData();
        if (customers.size() > 0) {
            exchangeCodeTransferRecordDTO.setNickName(customers.get(0).getNikeName());
            exchangeCodeTransferRecordDTO.setHeadImg(customers.get(0).getHeadPortrait());
        }
        exchangeCodeTransferRecordDTO.setUserName(userName);
        return exchangeCodeTransferRecordDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void receive(Integer userId, String userName, Integer id) {
        if (userId <= 0) {
            throw new FlexibleCustomException("用户未登录");
        }
        // 分布式锁
        AutoReleaseLock autoReleaseLock =
            exchangeTransferCache.tryLock(TenantContext.getTenantNo() + ":" + id, 5, TimeUnit.MINUTES);
        if (autoReleaseLock == null) {
            // 加锁
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_SYSTEM_ERROR);
        }
        try {
            ExchangeCodeTransferRecordDO exchangeCodeTransferRecordDO = exchangeCodeTransferRecordQryExe.selectById(id);
            if (exchangeCodeTransferRecordDO == null) {
                throw new FlexibleCustomException("找不到记录");
            }
            if (exchangeCodeTransferRecordDO.getReceiveFlag() != FlexibleCommonConstant.FLAG_NO
                || exchangeCodeTransferRecordDO.getReceiveTime() != null
                || exchangeCodeTransferRecordDO.getToUserId() != null) {
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_HAS_RECEICE);
            }
            Date date = new Date();
            // 进行检查
            checkAndGetData(exchangeCodeTransferRecordDO.getFromUserId(),
                exchangeCodeTransferRecordDO.getExchangeCodeId());
            // 找出发起用户关联记录并删除
            ExchangeCodeUserDO exchangeCodeUserDO =
                exchangeCodeUserQryExe.getByExchangeCodeId(exchangeCodeTransferRecordDO.getExchangeCodeId());
            exchangeCodeUserCmdExe.deleteById(exchangeCodeUserDO.getId());
            exchangeCodeTransferRecordDO.setReceiveFlag(FlexibleCommonConstant.FLAG_YES);
            exchangeCodeTransferRecordDO.setReceiveTime(date);
            exchangeCodeTransferRecordDO.setToUserId(userId);
            // 更新领取状态
            exchangeCodeTransferRecordCmdExe.update(exchangeCodeTransferRecordDO);
            exchangeCodeUserDO = new ExchangeCodeUserDO();
            exchangeCodeUserDO.setExchangeCodeId(exchangeCodeTransferRecordDO.getExchangeCodeId());
            exchangeCodeUserDO.setUserId(userId);
            exchangeCodeUserDO.setCreateTime(date);
            // 绑定记录
            exchangeCodeUserCmdExe.create(exchangeCodeUserDO);
        } finally {
            autoReleaseLock.close();
        }
    }

    @Override
    public ExchangeCodeTransferRecordDTO detail(Integer id) {
        ExchangeCodeTransferRecordDTO exchangeCodeTransferRecordDTO = new ExchangeCodeTransferRecordDTO();
        ExchangeCodeTransferRecordDO exchangeCodeTransferRecordDO = exchangeCodeTransferRecordQryExe.selectById(id);
        if (exchangeCodeTransferRecordDO == null) {
            throw new FlexibleCustomException("找不到记录");
        }
        ExchangeCardTransferDO exchangeCardTransferDO =
            exchangeCardTransferQryExe.selectByExchangeId(exchangeCodeTransferRecordDO.getExchangeId());
        if (exchangeCardTransferDO == null) {
            throw new FlexibleCustomException("找不到记录");
        }
        BeanUtils.copyProperties(exchangeCardTransferDO, exchangeCodeTransferRecordDTO);
        BeanUtils.copyProperties(exchangeCodeTransferRecordDO, exchangeCodeTransferRecordDTO);
        List<Integer> userIds = new ArrayList<>();
        userIds.add(exchangeCodeTransferRecordDO.getFromUserId());
        List<CustomerRpcDTO> customers = customerServiceRpc.listByIds(userIds).getData();
        if (customers.size() > 0) {
            exchangeCodeTransferRecordDTO.setNickName(customers.get(0).getNikeName());
            exchangeCodeTransferRecordDTO.setHeadImg(customers.get(0).getHeadPortrait());
        }
        return exchangeCodeTransferRecordDTO;
    }

    /**
     * 检查兑换码是否可以转发与领取并获取转发信息
     */
    private ExchangeCardTransferDO checkAndGetData(Integer fromUserId, Integer exchangeCodeId) {
        ExchangeCodeUserDO exchangeCodeUserDO = exchangeCodeUserQryExe.getByExchangeCodeId(exchangeCodeId);
        if (exchangeCodeUserDO == null) {
            throw new FlexibleCustomException("找不到兑换卡");
        }
        if (exchangeCodeUserDO.getUserId() != fromUserId.intValue()) {
            throw new FlexibleCustomException("不是本人绑定的兑换卡");
        }
        ExchangeCodeDO exchangeCodeDO = exchangeCodeQryExe.getById(exchangeCodeId);
        if (exchangeCodeDO == null) {
            throw new FlexibleCustomException("找不到兑换卡");
        }
        if (exchangeCodeDO.getStatus() != ExchangeCodeConstant.StatusUnUse.shortValue()) {
            throw new FlexibleCustomException("不是未使用状态不能转赠");
        }
        ExchangeCardTransferDO exchangeCardTransferDO =
            exchangeCardTransferQryExe.selectByExchangeId(exchangeCodeDO.getExchangeId());
        if (exchangeCardTransferDO == null) {
            throw new FlexibleCustomException("找不到记录");
        }
        return exchangeCardTransferDO;
    }

}
