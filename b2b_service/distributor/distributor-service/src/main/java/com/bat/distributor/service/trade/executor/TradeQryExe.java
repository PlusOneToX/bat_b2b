package com.bat.distributor.service.trade.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.trade.dto.TradeId;
import com.bat.distributor.api.trade.dto.TradeListQry;
import com.bat.distributor.api.trade.dto.data.TradeDTO;
import com.bat.distributor.dao.trade.TradeMapper;
import com.bat.distributor.dao.trade.dataobject.TradeDO;
import com.bat.distributor.service.trade.convertor.TradeConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Component
public class TradeQryExe {

    @Resource
    private TradeMapper mapper;

    /**
     * 查询分销商收款条件列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<TradeDTO> executeList(TradeListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<TradeDO> tradeDOList = mapper.listTrade(qryMap);
        PageInfo pageInfo = new PageInfo(tradeDOList);
        List<TradeDTO> toTradeDTOList = TradeConvertor.toTradeDTOList(pageInfo.getList());
        pageInfo.setList(toTradeDTOList);
        return pageInfo;
    }

    /**
     * 根据分销商收款条件ID查询分销商收款条件详情
     * 
     * @param qry
     * @return
     */
    public TradeDTO execute(TradeId qry) {
        TradeDO tradeDO = mapper.selectByPrimaryKey(qry.getId());
        if (tradeDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_TRADE_NULL);
        }
        TradeDTO dto = TradeConvertor.toTradeDTO(tradeDO);
        return dto;
    }

    public TradeDO getByDistributorId(Integer distributorId) {
        return mapper.getByDistributorId(distributorId);
    }
}
