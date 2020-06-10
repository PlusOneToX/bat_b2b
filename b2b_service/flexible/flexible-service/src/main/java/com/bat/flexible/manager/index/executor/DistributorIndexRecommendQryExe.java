package com.bat.flexible.manager.index.executor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.flexible.api.exchange.ExchangePictureRelevanceServiceI;
import com.bat.flexible.dao.exchange.dataobject.ExchangePictureRelevanceDO;
import com.bat.flexible.dao.index.DistributorIndexRecommendDOMapper;
import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DistributorIndexRecommendQryExe {

    @Autowired
    private DistributorIndexRecommendDOMapper distributorIndexRecommendDOMapper;

    @Autowired
    private ExchangePictureRelevanceServiceI exchangePictureRelevanceServiceI;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @Value("${distributor.id.sanxing}")
    private Integer sanxingDistributorId;

    public DistributorIndexRecommendDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        DistributorIndexRecommendDO distributorIndexRecommendDO = distributorIndexRecommendDOMapper.selectByPrimaryKey(id);
        if(distributorIndexRecommendDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        return distributorIndexRecommendDO;
    }

    public PageInfo<CommonPicturePageCO> listCOByCondition(Integer exchangeId, Integer distributorId,List<Integer> distributorIds, Integer materialId, Integer modelId,
                                                       Integer page,Integer size) {

        Integer countryId = null;
        try {
            countryId = flexibleDistributorQryExe.getCountryIdByDistributorId(distributorId);
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            throw new FlexibleCustomException(e.getMessage());
        }

        List<CommonPicturePageCO> list = new ArrayList<>();
        if(countryId==null){
            return new PageInfo(list);
        }
        //判断是否是
        boolean isSanXing=false;
        if(distributorId==sanxingDistributorId.intValue()){
            isSanXing=true;
        }

        if(exchangeId !=null) {
            ExchangePictureRelevanceDO pictureRela = exchangePictureRelevanceServiceI.findOneByExchangeId(exchangeId);
            if (pictureRela == null) {
                return new PageInfo(list);
            }
            PageHelper.startPage(page,size);
            //存在兑换卡
            if (pictureRela.getType() == 1) {
               //全部可用
                list =distributorIndexRecommendDOMapper.listAllExchangePictureCOByCondition(isSanXing,countryId,materialId,modelId,distributorIds,exchangeId);
            } else {
                //指定
                list =distributorIndexRecommendDOMapper.listAssginExchangePictureCO(isSanXing,countryId,materialId,modelId,distributorIds,exchangeId);
            }

        }else{
           //没有兑换卡
            PageHelper.startPage(page,size);
            list =distributorIndexRecommendDOMapper.listPictureCOByCondition(isSanXing,countryId,materialId,modelId,distributorIds);
        }
        return new PageInfo(list);
    }
}
