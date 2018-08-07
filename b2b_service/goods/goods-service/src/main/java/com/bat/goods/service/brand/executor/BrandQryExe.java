package com.bat.goods.service.brand.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.dao.brand.BrandDistributorGroupRelevanceMapper;
import com.bat.goods.dao.brand.BrandMapper;
import com.bat.goods.dao.brand.dataobject.BrandDO;
import com.bat.goods.dao.brand.dataobject.BrandDistributorGroupRelevanceDO;
import com.bat.goods.service.brand.convertor.BrandConvertor;
import com.bat.goods.service.common.CommonRpcQryExe;
import com.bat.goods.service.common.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorGroupRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorNameRpcDTO;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.brand.dto.BrandId;
import com.bat.goods.api.brand.dto.BrandListQry;
import com.bat.goods.api.brand.dto.data.BrandDTO;

@Component
public class BrandQryExe {

    @Resource
    private BrandMapper brandMapper;
    @Resource
    private CommonRpcQryExe commonRpcQryExe;
    @Resource
    private BrandDistributorGroupRelevanceMapper brandDistributorGroupRelevanceMapper;

    /**
     * 查询品牌列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<BrandDTO> executeList(BrandListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<BrandDO> brandDOList = brandMapper.listBrand(qryMap);
        PageInfo pageInfo = new PageInfo(brandDOList);
        List<BrandDTO> brandDTOList = BrandConvertor.toBrandDTOList(pageInfo.getList());
        pageInfo.setList(brandDTOList);
        return pageInfo;
    }

    /**
     * 根据品牌ID查询品牌详情
     * 
     * @param brandId
     * @return
     */
    public BrandDTO execute(BrandId brandId) {
        BrandDO brandDO = brandMapper.getById(brandId.getId());
        if (brandDO == null) {
            throw GoodsException.buildException(ErrorCode.B_BRAND_NULL);
        }
        BrandDTO brandDTO = BrandConvertor.toBrandDTO(brandDO);
        getBrandScopeIds(brandDTO);
        getBrandScopeNoIds(brandDTO);
        return brandDTO;
    }

    /**
     * 获取品牌可视关联关系
     * 
     * @param brandDTO
     */
    public void getBrandScopeIds(BrandDTO brandDTO) {
        Integer brandId = brandDTO.getId();
        if (brandDTO.getDistributorScope() == null) {
            return;
        }
        if (brandDTO.getDistributorScope().equals(Constant.SCOPE_SCALE_PRICE)) {
            brandDTO.setScalePriceIds(brandMapper.listBrandScalePriceRelevanceId(brandId));
        } else if (brandDTO.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR)) {
            List<Integer> distributorIds = brandMapper.listBrandDistributorRelevanceId(brandId);
            List<DistributorNameRpcDTO> distributorNameRpcDTOS =
                commonRpcQryExe.getDistributorNameByDistributorIds(distributorIds);
            brandDTO
                .setDistributors(BrandConvertor.toBrandDistributorScopeDTOList(distributorIds, distributorNameRpcDTOS));
        } else if (brandDTO.getDistributorScope().equals(Constant.SCOPE_DEPARTMENT)) {
            brandDTO.setDepartmentIds(brandMapper.listBrandDepartmentRelevanceId(brandId));
        } else if (brandDTO.getDistributorScope().equals(Constant.SCOPE_ADMIN)) {
            brandDTO.setAdminIds(brandMapper.listBrandAdminRelevanceId(brandId));
        } else if (brandDTO.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR_GROUP)) {
            List<Integer> distributorGroupIds =
                brandDistributorGroupRelevanceMapper.listDistributorGroupIdByBrandId(brandId);
            List<DistributorGroupRpcDTO> groupRpcDTOS =
                commonRpcQryExe.getDistributorGroupByDistributorGroupIds(distributorGroupIds);
            brandDTO
                .setDistributorGroups(BrandConvertor.toBrandDistributorGroupDTOList(distributorGroupIds, groupRpcDTOS));
        }
    }

    /**
     * 获取品牌不可视关联关系
     * 
     * @param brandDTO
     */
    public void getBrandScopeNoIds(BrandDTO brandDTO) {
        Integer brandId = brandDTO.getId();
        if (brandDTO.getDistributorScopeNo() == null) {
            return;
        }
        if (brandDTO.getDistributorScopeNo().equals(Constant.SCOPE_SCALE_PRICE)) {
            brandDTO.setScalePriceNoIds(brandMapper.listBrandScalePriceRelevanceIdNo(brandId));
        } else if (brandDTO.getDistributorScopeNo().equals(Constant.SCOPE_DISTRIBUTOR)) {
            List<Integer> distributorNoIds = brandMapper.listBrandDistributorRelevanceIdNo(brandId);
            List<DistributorNameRpcDTO> distributorNameRpcDTOS =
                commonRpcQryExe.getDistributorNameByDistributorIds(distributorNoIds);
            brandDTO.setDistributorNos(
                BrandConvertor.toBrandDistributorScopeDTOList(distributorNoIds, distributorNameRpcDTOS));
        } else if (brandDTO.getDistributorScopeNo().equals(Constant.SCOPE_DEPARTMENT)) {
            brandDTO.setDepartmentNoIds(brandMapper.listBrandDepartmentRelevanceIdNo(brandId));
        } else if (brandDTO.getDistributorScopeNo().equals(Constant.SCOPE_ADMIN)) {
            brandDTO.setAdminNoIds(brandMapper.listBrandAdminRelevanceIdNo(brandId));
        }
    }

    public List<BrandDistributorGroupRelevanceDO> listByDistributorGroupIds(String distributorGroupIdsStr) {
        List<String> distributorGroupIdList = Arrays.asList(distributorGroupIdsStr.split(","));
        List<Integer> distributorGroupIds = new ArrayList<>();
        for (String idStr : distributorGroupIdList) {
            if (StringUtils.isNotBlank(idStr)) {
                distributorGroupIds.add(Integer.valueOf(idStr));
            }
        }
        if(distributorGroupIds.size()==0){
            return new ArrayList<>();
        }
        return brandDistributorGroupRelevanceMapper.listByDistributorGroupIds(distributorGroupIds);
    }

}
