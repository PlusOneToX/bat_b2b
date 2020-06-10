package com.bat.flexible.manager.material.convertor;

import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.goods.dto.GoodsItemSimplePageBean;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.flexible.material.dto.MaterialDTORpcQry;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.flexible.api.distributor.exclude.DistributorMaterialExcludeServiceI;
import com.bat.flexible.api.material.dto.MaterialDetailQry;
import com.bat.flexible.api.material.dto.MaterialItemQry;
import com.bat.flexible.api.material.dto.MaterialRelaSimpleDTO;
import com.bat.flexible.api.material.dto.MaterialSimpleDTO;
import com.bat.flexible.dao.distributor.dataobject.DistributorMaterialExcludeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeMaterialRelevanceDO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.utils.proxy.FlexibleProxyUtil;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.error.common.FlexibleDubboServiceErrorCode;
import com.bat.flexible.manager.error.material.MaterialErrorCode;
import com.bat.flexible.manager.material.executor.MaterialQryExe;
import com.bat.flexible.manager.product.executor.ProductCategoryQryExe;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MaterialConvertor {

    private static ProductCategoryQryExe productCategoryQryExe;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private DistributorServiceRpc distributorServiceRpc;

    @Resource
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @Autowired
    private DistributorMaterialExcludeServiceI distributorMaterialExcludeServiceI;

    @Autowired
    private MaterialQryExe materialQryExe;

    public static void validItemQuery(MaterialItemQry materialItemQry) {
        if(materialItemQry.getMaterialId() ==null && materialItemQry.getItemId() ==null && StringUtils.isBlank(materialItemQry.getItemCode())){
            throw new FlexibleCustomException(MaterialErrorCode.M_MATERIAL_ID_AND_ITEM_NULL);
        }
    }


    @Resource
    public  void setProductCategoryQryExe(ProductCategoryQryExe productCategoryQryExe) {
        MaterialConvertor.productCategoryQryExe = productCategoryQryExe;
    }

    public static List<MaterialSimpleDTO> toMaterialSimpleDTO(List<MaterialDO> materialDOList, MaterialQryExe materialQryExe){
        List<MaterialSimpleDTO> resultList = new ArrayList<>();
        if(materialDOList==null || materialDOList.size()==0){
            return resultList;
        }
        List<ProductCategoryDO> productCategoryDOList = productCategoryQryExe.listByCondition(null,null);
        Map<Integer, ProductCategoryDO> productCategoryDOMap = productCategoryDOList.stream().collect(Collectors.toMap(ProductCategoryDO::getId, productCategoryDO -> productCategoryDO));
        Map<Integer, MaterialDO> materialDOMap = new HashMap<>();
        materialDOList.stream().forEach(materialDO -> {
            MaterialSimpleDTO simpleDTO = BeanUtils.copy(materialDO,MaterialSimpleDTO.class);
            if(simpleDTO.getParentId()>0){
                //设置名称
                MaterialDO parent = materialDOMap.get(simpleDTO.getParentId());
                if(parent ==null){
                    parent = materialQryExe.getById(simpleDTO.getParentId());
                    materialDOMap.put(simpleDTO.getParentId(),parent);
                }
                simpleDTO.setParentEnglishName(parent.getEnglishName());
                simpleDTO.setParentName(parent.getName());
            }
            ProductCategoryDO productCategoryDO = productCategoryDOMap.get(simpleDTO.getCategoryId());
            simpleDTO.setCategoryName(productCategoryDO.getName());
            simpleDTO.setCategoryEnglishName(productCategoryDO.getEnglishName());
            resultList.add(simpleDTO);
        });
        return resultList;
    }

    public static MaterialDTORpcQry ToMaterialDTORpcQry(MaterialDO materialDO){
        MaterialDTORpcQry materialDTORpcQry =  BeanUtils.copy(materialDO,MaterialDTORpcQry.class);
        ProductCategoryDO productCategoryDO = productCategoryQryExe.getById(materialDO.getCategoryId());
        materialDTORpcQry.setCategoryName(productCategoryDO.getName());
        return materialDTORpcQry;
    }

    public GoodsItemSimplePageBean toGoodsItemSimplePageBeanByItemId(Integer itemId, String itemCode){
        if(itemId ==null){
            return null;
        }
        List<Integer> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);
        Response<List<GoodsItemRpcDTO>> goodsResponse = goodsServiceRpc.listGoodsItemByIds(itemIdList);
        if(goodsResponse ==null || !goodsResponse.isSuccess()){
            throw new FlexibleCustomException(FlexibleDubboServiceErrorCode.DUBBO_GOODS_SERVICE_EXCEPTION);
        }
        GoodsItemSimplePageBean goodsItemSimplePageBean = new GoodsItemSimplePageBean();
        List<GoodsItemRpcDTO> data = goodsResponse.getData();
        //需要判空
        if(data ==null || data.size()==0){
            //货品已被删除
            goodsItemSimplePageBean.setItemId(itemId);
            goodsItemSimplePageBean.setItemCode(itemCode);
        }else{
            goodsItemSimplePageBean = BeanUtils.copy(data.get(0),GoodsItemSimplePageBean.class);
            goodsItemSimplePageBean.setItemId(data.get(0).getId());
        }
        return goodsItemSimplePageBean;
    }

    public void queryDistributorExclude(Integer id, MaterialDetailQry materialDetailQry) {
        //分销商不可用
        List<DistributorMaterialExcludeDO> excludeDOList = distributorMaterialExcludeServiceI.listByMaterialIdAndDelFlag(id, FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        if(excludeDOList !=null && excludeDOList.size()>0){
            List<DistributorSimpleRelaQry> relaQryList = new ArrayList<>();
            Map<Integer, DistributorRpcDTO> map = new HashMap<>();
            excludeDOList.stream().forEach(distributorMaterialExcludeDO -> {
                DistributorSimpleRelaQry simpleRelaQry = new DistributorSimpleRelaQry();
                simpleRelaQry.setId(distributorMaterialExcludeDO.getId());
                simpleRelaQry.setDistributorId(distributorMaterialExcludeDO.getDistributorId());
                //分销商数据
                flexibleDistributorQryExe.setDistributorNameMsg(simpleRelaQry,map);
                relaQryList.add(simpleRelaQry);
            });
            materialDetailQry.setDistributorExcludeList(relaQryList);
        }
    }

    public List<MaterialRelaSimpleDTO> toMaterialRelaSimpleDTOListFromExchange(List<ExchangeMaterialRelevanceDO> relevanceDOList){
        if(relevanceDOList ==null || relevanceDOList.size()==0){
            return null;
        }
        List<MaterialRelaSimpleDTO> list = new ArrayList<>();
        Map<Integer, ProductCategoryDO> categoryDOMap = new HashMap<>();
        relevanceDOList.stream().forEach(exchangeMaterialRelevanceDO -> {
            MaterialRelaSimpleDTO relaSimpleDTO = new MaterialRelaSimpleDTO();
            relaSimpleDTO.setId(exchangeMaterialRelevanceDO.getId());
            relaSimpleDTO.setOpenFlag(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
            MaterialDO materialDO = materialQryExe.getById(exchangeMaterialRelevanceDO.getMaterialId());
            relaSimpleDTO.setName(materialDO.getName());
            relaSimpleDTO.setEnglishName(materialDO.getEnglishName());
            relaSimpleDTO.setMaterialId(materialDO.getId());
            relaSimpleDTO.setMaterialNo(materialDO.getMaterialNo());
            if(materialDO.getParentId() ==0){
                relaSimpleDTO.setCategoryEnglishName(materialDO.getEnglishName());
                relaSimpleDTO.setCategoryName(materialDO.getName());
            }else{
                MaterialDO parentDO = materialQryExe.getById(materialDO.getParentId());
                relaSimpleDTO.setParentName(parentDO.getName());
                relaSimpleDTO.setParentEnglishName(parentDO.getEnglishName());
            }
            ProductCategoryDO productCategoryDO = categoryDOMap.get(materialDO.getCategoryId());
            if(productCategoryDO ==null){
                productCategoryDO = productCategoryQryExe.getById(materialDO.getCategoryId());
                categoryDOMap.put(materialDO.getCategoryId(),productCategoryDO);
            }
            relaSimpleDTO.setCategoryName(productCategoryDO.getName());
            relaSimpleDTO.setCategoryEnglishName(productCategoryDO.getEnglishName());
            list.add(relaSimpleDTO);
        });
        return list;
    }
}
