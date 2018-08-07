package com.bat.distributor.api.trade;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.trade.dto.TradeCmd;
import com.bat.distributor.api.trade.dto.TradeId;
import com.bat.distributor.api.trade.dto.TradeListQry;
import com.bat.distributor.api.trade.dto.TradeOpenCmd;
import com.bat.distributor.api.trade.dto.data.TradeDTO;

public interface TradeServiceI {
    /**
     * 添加分销商收款条件
     * 
     * @param cmd
     * @return
     */
    public void createTrade(TradeCmd cmd);

    /**
     * 更新分销商收款条件
     * 
     * @param cmd
     * @return
     */
    public void updateTrade(TradeCmd cmd);

    /**
     * 更新分销商收款条件状态
     * 
     * @param cmd
     * @return
     */
    public void openTrade(TradeOpenCmd cmd);

    /**
     * 获取分销商收款条件列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<TradeDTO> listTrade(TradeListQry qry);

    /**
     * 根据ID删除分销商收款条件
     * 
     * @param cmd
     * @return
     */
    public void deleteTrade(TradeId cmd);

    /**
     * 根据分销商收款条件id获取详情
     * 
     * @param qry
     * @return
     */
    public TradeDTO getTrade(TradeId qry);

}
