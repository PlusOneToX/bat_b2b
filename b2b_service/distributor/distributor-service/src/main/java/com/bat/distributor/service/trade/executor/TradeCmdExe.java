package com.bat.distributor.service.trade.executor;

import static com.bat.distributor.service.common.Constant.OPEN_NO;
import static com.bat.distributor.service.trade.executor.ErrorCode.*;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.trade.dto.TradeCmd;
import com.bat.distributor.api.trade.dto.TradeId;
import com.bat.distributor.api.trade.dto.TradeOpenCmd;
import com.bat.distributor.dao.trade.TradeMapper;
import com.bat.distributor.dao.trade.dataobject.TradeDO;
import com.bat.distributor.service.trade.convertor.TradeConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TradeCmdExe {

    @Resource
    private TradeMapper mapper;

    /**
     * 创建分销商收款条件
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createTrade(TradeCmd cmd) {
        TradeDO tradeDO = TradeConvertor.toTradeDo(cmd);
        mapper.insert(tradeDO);
    }

    /**
     * 更新分销商收款条件
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateTrade(TradeCmd cmd) {
        TradeDO tradeDO = TradeConvertor.toTradeDo(cmd);
        mapper.updateByPrimaryKey(tradeDO);
    }

    /**
     * 更新分销商收款条件状态
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void openTrade(TradeOpenCmd cmd) {
        if (cmd.getOpenFlag().equals(OPEN_NO)) {
            // 停用分销商收款条件前，确保分销商收款条件下的分销商都已转移或删除
            Integer count = mapper.getTradeDistributorsCount(cmd.getId());
            if (count > 0) {
                throw DistributorException.buildException(B_DISTRIBUTOR_TRADE_DISTRIBUTORNOTNUL);
            }
        }
        mapper.openTrade(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 根据Id删除分销商收款条件
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteTrade(TradeId cmd) {
        TradeDO tradeDO = mapper.selectByPrimaryKey(cmd.getId());
        if (tradeDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_TRADE_NULL);
        }
        // 停用的分销商收款条件才允许删除
        if (!tradeDO.getOpenFlag().equals(OPEN_NO)) {
            throw DistributorException.buildException(B_DISTRIBUTOR_TRADE_DELETE_OPEN_ERROR);
        }
        mapper.deleteByPrimaryKey(cmd.getId());
    }

}
