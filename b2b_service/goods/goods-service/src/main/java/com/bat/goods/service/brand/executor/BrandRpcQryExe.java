package com.bat.goods.service.brand.executor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.dao.brand.BrandMapper;
import com.bat.goods.dao.brand.dataobject.BrandDO;
import com.bat.goods.dao.brand.dataobject.UserBrandDO;
import com.bat.goods.service.brand.convertor.BrandConvertor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.goods.brand.dto.UserBrandListRpcQry;
import com.bat.dubboapi.goods.brand.dto.data.UserBrandRpcDTO;

@Component
public class BrandRpcQryExe {

    @Resource
    private BrandMapper brandMapper;

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    /**
     * 根据分销商id获取品牌列表
     * 
     * @param qry
     * @return
     */
    public List<UserBrandRpcDTO> listBrand(UserBrandListRpcQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        List<UserBrandDO> brandDOS = brandMapper.listBrandByDistributorId(qryMap);
        List<UserBrandRpcDTO> dtos = BrandConvertor.toUserBrandRpcDTOList(brandDOS);
        return dtos;
    }

    /**
     * 根据品牌id获取品牌信息
     * 
     * @param brandId
     * @return
     */
    public UserBrandRpcDTO getBrand(Integer brandId) {
        BrandDO brandDO = brandMapper.getById(brandId);
        return BrandConvertor.toUserBrandRpcDTO(brandDO);
    }

    /**
     * 根据品牌id获取品牌信息
     *
     * @param brandIds
     * @return
     */
    public List<UserBrandRpcDTO> getBrandByIds(List<Integer> brandIds) {
        List<BrandDO> brandDOS = brandMapper.listBrandByBrandIds(brandIds);
        List<UserBrandRpcDTO> userBrandRpcDTOS = new ArrayList<>();
        for (BrandDO brandDO : brandDOS) {
            UserBrandRpcDTO userBrandRpcDTO = new UserBrandRpcDTO();
            BeanUtils.copyProperties(brandDO, userBrandRpcDTO);
            userBrandRpcDTOS.add(userBrandRpcDTO);
        }
        return userBrandRpcDTOS;
    }

}
