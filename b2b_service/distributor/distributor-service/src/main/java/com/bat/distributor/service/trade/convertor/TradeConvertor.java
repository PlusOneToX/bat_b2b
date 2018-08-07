package com.bat.distributor.service.trade.convertor;

import com.bat.distributor.api.trade.dto.TradeCmd;
import com.bat.distributor.api.trade.dto.data.TradeDTO;
import com.bat.distributor.dao.trade.dataobject.TradeDO;
import com.bat.dubboapi.distributor.trade.dto.DistributorTradeRpcQryDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradeConvertor {
    /**
     * 分销商收款条件数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static TradeDO toTradeDo(TradeCmd cmd) {
        TradeDO tradeDO = new TradeDO();
        BeanUtils.copyProperties(cmd, tradeDO);
        Date date = new Date(System.currentTimeMillis());
        tradeDO.setCreateTime(date);
        tradeDO.setUpdateTime(date);
        return tradeDO;
    }

    /**
     * do列表转dto列表
     * 
     * @param doList
     * @return
     */
    public static List<TradeDTO> toTradeDTOList(List<TradeDO> doList) {
        List<TradeDTO> dtoList = new ArrayList<>();
        doList.forEach(tradeDO -> {
            TradeDTO dto = new TradeDTO();
            BeanUtils.copyProperties(tradeDO, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    /**
     * do转dto
     * 
     * @param tradeDO
     * @return
     */
    public static TradeDTO toTradeDTO(TradeDO tradeDO) {
        TradeDTO dto = new TradeDTO();
        BeanUtils.copyProperties(tradeDO, dto);
        return dto;
    }

    /**
     * 转换为RPC参数
     * @param tradeDTO
     * @return
     */
    public static DistributorTradeRpcQryDTO toDistributorTradeRpcQryDTO(TradeDTO tradeDTO) {
        DistributorTradeRpcQryDTO tradeRpcQryDTO = new DistributorTradeRpcQryDTO();
        BeanUtils.copyProperties(tradeDTO,tradeRpcQryDTO);
        return tradeRpcQryDTO;
    }

    /**
     * 转换为RPC对象 DO转
     * @param tradeDO
     * @return
     */
    public static DistributorTradeRpcQryDTO toDistributorTradeRpcQryDTOByDO(TradeDO tradeDO) {
        DistributorTradeRpcQryDTO tradeRpcQryDTO = new DistributorTradeRpcQryDTO();
        BeanUtils.copyProperties(tradeDO,tradeRpcQryDTO);
        return tradeRpcQryDTO;
    }
}
