package com.bat.flexible.manager.common.utils.proxy;

import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Component
public class FlexibleProxyUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlexibleProxyUtil.class);


    /**
     * 有分销商id、设置分销商名称和所属公司名称
     * @param object
     * @param distributorMap 分销商的信息
     */
    public static void setDistributorNameMsg(Object object, Map<Integer, DistributorRpcDTO> distributorMap, DistributorServiceRpc distributorServiceRpc){
        try {
            if(object ==null){
                return;
            }
            if(distributorServiceRpc ==null){
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
            LOGGER.error("给分销商设置名称和公司名称出现异常",object);
        }
    }


    /**
     *  有分销商id、设置分销商名称和所属公司名称
     * @param sourcelist
     */
    public static List<DistributorSimpleRelaQry> toDistributorSimpleRelaQryList(Collection sourcelist){
        if(sourcelist ==null || sourcelist.size()==0){
            return null;
        }
        List<DistributorSimpleRelaQry> list = new ArrayList<>();
        sourcelist.stream().forEach(object -> {
            try {
                DistributorSimpleRelaQry simpleRelaQry = BeanUtils.copy(object,DistributorSimpleRelaQry.class);
                Class clazz = object.getClass();


                Field distributorCompanyNameField = null;
                try {
                    distributorCompanyNameField = clazz.getDeclaredField("companyName");
                } catch (NoSuchFieldException e) {
                    //不打印、没有该字段
                }
                if(distributorCompanyNameField==null){
                    return;
                }
                distributorCompanyNameField.setAccessible(true);
                Object companyNameObj = distributorCompanyNameField.get(object);
                simpleRelaQry.setDistributorCompanyName(String.valueOf(companyNameObj));
                list.add(simpleRelaQry);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("给分销商设置名称和公司名称出现异常",object);
            }
        });
        return list;


    }
}
