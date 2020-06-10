package com.bat.flexible.manager.distributor.dubbo.executor;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorTreePathRpcDTO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.common.FlexibleDubboServiceErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FlexibleDistributorQryExe {

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private DistributorServiceRpc distributorServiceRpc;

    public Integer getCountryIdByDistributorId(Integer distributorId){

        //获取分销商是否国内还是国外、需要查询分销商接口
        com.bat.dubboapi.distributor.common.Response<Integer> distributorResp = distributorServiceRpc.distributorCountryById(distributorId);
        if(distributorResp == null ){
            throw FlexibleDubboApiException.buildException(FlexibleDubboServiceErrorCode.DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION);
        }
        if(!distributorResp.isSuccess() ){
            throw FlexibleDubboApiException.buildException(distributorResp.getErrCode(),distributorResp.getErrMessage());
        }
        Integer countryId = distributorResp.getData();
        if(countryId ==null){
            throw FlexibleDubboApiException.buildException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_COUNTRY_ID_NULL, MessageUtils.get(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_COUNTRY_ID_NULL));
        }
        return countryId;
    }

    /**
     * 获取该分销商所有上级以及本身
     * @return
     */
    public List<Integer> getDistributorTreePaths(Integer distributorId) {
        List<Integer> distributorIds=new ArrayList<>();
        if (distributorId == null) {
            return distributorIds;
        }
        Response<List<DistributorTreePathRpcDTO>> response = distributorServiceRpc.getDistributorTreePaths(distributorId);
        if (response == null) {
            throw FlexibleDubboApiException.buildException(FlexibleDubboServiceErrorCode.DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION);
        }
        List<DistributorTreePathRpcDTO> distributorTreePaths = response.getData();
        if (distributorTreePaths == null) {
            throw FlexibleDubboApiException.buildException(FlexibleCommonErrorCode.COMMON_DEAL_ERROR);
        }
        distributorIds = distributorTreePaths.stream().map(DistributorTreePathRpcDTO::getDistributorAncestorId).collect(Collectors.toList());
        distributorIds.add(distributorId);
        return distributorIds;
    }

    /**
     * 有分销商id、设置分销商名称和所属公司名称
     * @param object
     * @param distributorMap 分销商的信息
     */
    public void setDistributorNameMsg(Object object, Map<Integer, DistributorRpcDTO> distributorMap){
        try {
            if(object ==null){
                return;
            }
            Class clazz = object.getClass();
            Field field = clazz.getDeclaredField("distributorId");
            if(field ==null){
                return;
            }
            if(distributorMap==null){
                distributorMap = new HashMap<>();
            }
            field.setAccessible(true);
            Object distributorIdObj =  field.get(object);
            Field distributorNameField = null;
            Field distributorCompanyNameField = null;
            try {
                distributorNameField = clazz.getDeclaredField("distributorName");
                distributorCompanyNameField = clazz.getDeclaredField("distributorCompanyName");
            } catch (NoSuchFieldException e) {
                //不打印、没有该字段
            }
            if(distributorNameField ==null && distributorCompanyNameField==null){
                return;
            }
            Integer distributorId = Integer.parseInt(String.valueOf(distributorIdObj));
            if(distributorNameField !=null){
                //设置公司名称
                DistributorRpcDTO rpcDTO = distributorMap.get(distributorId);
                if(rpcDTO ==null){
                    Response<DistributorRpcDTO> rpcDTOResponse = distributorServiceRpc.distributorById(distributorId);
                    rpcDTO = rpcDTOResponse.getData();
                    distributorMap.put(distributorId,rpcDTO);
                }
                distributorNameField.setAccessible(true);
                distributorNameField.set(object,rpcDTO.getName());
            }
            if(distributorCompanyNameField !=null){
                //设置公司名称
                DistributorRpcDTO rpcDTO = distributorMap.get(distributorId);
                if(rpcDTO ==null){
                    Response<DistributorRpcDTO> rpcDTOResponse = distributorServiceRpc.distributorById(distributorId);
                    rpcDTO = rpcDTOResponse.getData();
                    distributorMap.put(distributorId,rpcDTO);
                }
                distributorCompanyNameField.setAccessible(true);
                distributorCompanyNameField.set(object,rpcDTO.getCompanyName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
