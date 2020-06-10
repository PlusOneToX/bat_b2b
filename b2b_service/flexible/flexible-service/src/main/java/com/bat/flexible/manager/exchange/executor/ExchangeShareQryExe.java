package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.ExchangeShareDetailDTO;
import com.bat.flexible.dao.exchange.ExchangeShareDistributorMapper;
import com.bat.flexible.dao.exchange.ExchangeShareMapper;
import com.bat.flexible.dao.exchange.co.ExchangeShareDistributorCO;
import com.bat.flexible.dao.exchange.co.ExchangeSharePageCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeShareDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeShareDistributorDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExchangeShareQryExe {

    @Autowired
    private ExchangeShareMapper exchangeShareMapper;

    @Autowired
    private ExchangeShareDistributorMapper exchangeShareDistributorMapper;

    public List<ExchangeSharePageCO> listCOByCondition(Short activityPlatform, String preferName,Short seat) {
        List<ExchangeSharePageCO> list = exchangeShareMapper.listCOByCondition(activityPlatform, preferName,seat);
        return list;
    }

    public Response<ExchangeShareDetailDTO> detailById(Integer id) {
        ExchangeShareDO exchangeShareDO = exchangeShareMapper.selectByPrimaryKey(id);
        ExchangeShareDetailDTO exchangeShareDetailDTO = new ExchangeShareDetailDTO();
        BeanUtils.copyProperties(exchangeShareDO, exchangeShareDetailDTO);
        short type = FlexibleCommonConstant.COMMON_OPEN_FLAG_YES;
        //指定全部
        if (exchangeShareDO.getDistributorVisualType() == FlexibleCommonConstant.ALL_TYPE) {
            type = FlexibleCommonConstant.COMMON_OPEN_FLAG_NO;
        }
        List<ExchangeShareDistributorDO> exchangeShareDistributorDOS=exchangeShareDistributorMapper.listByExchangeShareIdAndType(exchangeShareDO.getId(),type);
        List<ExchangeShareDistributorCO> exchangeShareDistributorCOS=new ArrayList<>();
        for(ExchangeShareDistributorDO exchangeShareDistributorDO:exchangeShareDistributorDOS){
            ExchangeShareDistributorCO exchangeShareDistributorCO=new ExchangeShareDistributorCO();
            BeanUtils.copyProperties(exchangeShareDistributorDO,exchangeShareDistributorCO);
            exchangeShareDistributorCOS.add(exchangeShareDistributorCO);
        }
        exchangeShareDetailDTO.setDistributor(exchangeShareDistributorCOS);
        return Response.of(exchangeShareDetailDTO);
    }

    /**
     * 找到合适的活动
     * @param activityPlatform
     * @param seat
     */
    public ExchangeShareDO findSuitable(Short activityPlatform, Short seat) {
        return exchangeShareMapper.findSuitable(activityPlatform, seat);
    }

    public List<ExchangeShareDistributorDO> listByExchangeShareIdAndType(Integer exchangeShareId, short type) {
        List<ExchangeShareDistributorDO> exchangeShareDistributorDOS = exchangeShareDistributorMapper.listByExchangeShareIdAndType(exchangeShareId, type);
        return exchangeShareDistributorDOS;
    }
}
