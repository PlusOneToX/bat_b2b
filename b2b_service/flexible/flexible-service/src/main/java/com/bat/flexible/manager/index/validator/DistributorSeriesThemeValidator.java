package com.bat.flexible.manager.index.validator;

import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DistributorSeriesThemeValidator {


    /**
     * 校验分销商id、名称、公司名称信息
     * @param distributorSimpleRelaQryList
     * @return
     */
    public static List<Integer> validDistributor(List<DistributorSimpleRelaQry> distributorSimpleRelaQryList){
        List<Integer> distributorIdList = new ArrayList<>();
        if(distributorSimpleRelaQryList ==null || distributorSimpleRelaQryList.size()==0){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL);
        }
        distributorSimpleRelaQryList.stream().forEach(distributorSimpleRelaQry -> {
            if(distributorSimpleRelaQry.getDistributorId() ==null){
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL);
            }
            if(StringUtils.isBlank(distributorSimpleRelaQry.getDistributorCompanyName())){
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_COMPANY_NAME_NULL);
            }
            if(StringUtils.isBlank(distributorSimpleRelaQry.getDistributorName())){
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_NAME_NULL);
            }
            distributorIdList.add(distributorSimpleRelaQry.getDistributorId());
        });
        return distributorIdList;
    }


}
