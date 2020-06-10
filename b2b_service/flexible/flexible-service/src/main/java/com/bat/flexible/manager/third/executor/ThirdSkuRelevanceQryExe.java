package com.bat.flexible.manager.third.executor;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.product.ProductCategoryServiceI;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.common.constant.third.ThirdSkuNoNameInfoConstant;
import com.bat.flexible.manager.common.utils.proxy.FlexibleProxyUtil;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import com.bat.flexible.dao.third.ThirdSkuRelevanceDOMapper;
import com.bat.flexible.dao.third.co.ThirdSkuRelevancePageCO;
import com.bat.flexible.dao.third.dataobject.ThirdSkuNoNameInfoDO;
import com.bat.flexible.dao.third.dataobject.ThirdSkuRelevanceDO;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ThirdSkuRelevanceQryExe {

    @Autowired
    private ThirdSkuRelevanceDOMapper thirdSkuRelevanceDOMapper;

    @Autowired
    private ThirdSkuNoNameInfoQryExe thirdSkuNoNameInfoQryExe;

    @Autowired
    private MaterialServiceI materialServiceI;

    @Autowired
    private ModelServiceI modelServiceI;

    @Autowired
    private ProductCategoryServiceI productCategoryServiceI;

    @Resource
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private DistributorServiceRpc distributorServiceRpc;

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdSkuRelevanceQryExe.class);


    public PageInfo<ThirdSkuRelevancePageCO> listCOByDistributorIdAndOpenFlag(Integer distributorId ,Short openFlag) {
        List<ThirdSkuRelevanceDO> list = thirdSkuRelevanceDOMapper.listByDistributorIdAndOpenFlag(distributorId, openFlag);
        PageInfo pageInfo = new PageInfo(list);
        //处理其他系统的信息
        List<ThirdSkuRelevancePageCO> resultList = new ArrayList<>();
        if(list.size() == 0){
            return pageInfo;
        }
        List<ThirdSkuNoNameInfoDO> infoDOList = thirdSkuNoNameInfoQryExe.listByDistributorId(distributorId);
        Map<String,ThirdSkuNoNameInfoDO> infoDOMap = new HashMap<>();
        for (ThirdSkuNoNameInfoDO thirdSkuNoNameInfoDO : infoDOList) {
            //拼接
            String key = thirdSkuNoNameInfoDO.getThirdNo() + "_" + thirdSkuNoNameInfoDO.getType();
            infoDOMap.put(key, thirdSkuNoNameInfoDO);
        }
        //查询所有的材质
        List<MaterialDO> materialDOList = materialServiceI.listAll();
        //材质map
        Map<Integer,MaterialDO> materialMap = new HashMap<>();
        materialDOList.forEach(materialDO -> {
            materialMap.put(materialDO.getId(),materialDO);
        });
        //产品类型
        List<ProductCategoryDO> productCategoryDOList = productCategoryServiceI.listByCondition(null);
        Map<Integer, String> productCategoryMap = new HashMap<>();
        productCategoryDOList.forEach(productCategoryDO -> {
            productCategoryMap.put(productCategoryDO.getId(),productCategoryDO.getName());
        });
        //型号map
        Map<Integer, ModelDO> modelMap = new HashMap<>();
        List<ModelDO> modelDOList = modelServiceI.listAll();
        modelDOList.forEach(modelDO -> {
            modelMap.put(modelDO.getId(),modelDO);
        });
        Map<Integer, DistributorRpcDTO> map = new HashMap<>();
        list.forEach(thirdSkuRelevanceDO -> {
            try {
                ThirdSkuRelevancePageCO relevancePageCO = BeanUtils.copy(thirdSkuRelevanceDO, ThirdSkuRelevancePageCO.class);
                //设置分销商名称
                flexibleDistributorQryExe.setDistributorNameMsg(relevancePageCO, map);
                //设置物料分类名称
                ThirdSkuNoNameInfoDO nameInfoDO = infoDOMap.get(thirdSkuRelevanceDO.getThirdMaterialCategoryNo() + "_" + ThirdSkuNoNameInfoConstant.TYPE_SKU_CATEGORY);
                if (nameInfoDO != null) {
                    relevancePageCO.setCategoryName(nameInfoDO.getThirdName());
                }
                //设置品牌名称
                nameInfoDO = infoDOMap.get(thirdSkuRelevanceDO.getThirdBrandNo() + "_" + ThirdSkuNoNameInfoConstant.TYPE_BRAND);
                relevancePageCO.setThirdBrandName(nameInfoDO.getThirdName());

                nameInfoDO = infoDOMap.get(thirdSkuRelevanceDO.getThirdBrandNo() + "_" + ThirdSkuNoNameInfoConstant.TYPE_BRAND);
                relevancePageCO.setThirdBrandNo(nameInfoDO.getThirdNo());

                //设置扩展字段
                nameInfoDO = infoDOMap.get(thirdSkuRelevanceDO.getThirdModelNo() + "_" + ThirdSkuNoNameInfoConstant.TYPE_MODEL);
                if (nameInfoDO != null) {
                    relevancePageCO.setThirdModelName(nameInfoDO.getThirdName());
                    relevancePageCO.setSeries(nameInfoDO.getThirdExtName());
                }

                //设置材质名称
                nameInfoDO = infoDOMap.get(thirdSkuRelevanceDO.getThirdMaterialNo() + "_" + ThirdSkuNoNameInfoConstant.TYPE_MATERIAL);
                relevancePageCO.setThirdMaterialName(nameInfoDO.getThirdName());

                //设置颜色
                nameInfoDO = infoDOMap.get(thirdSkuRelevanceDO.getThirdColourNo() + "_" + ThirdSkuNoNameInfoConstant.TYPE_COLOUR);
                relevancePageCO.setColourName(nameInfoDO.getThirdName());

                //设置材质
                MaterialDO materialDO = materialMap.get(thirdSkuRelevanceDO.getMaterialId());
                if (materialDO != null) {
                    relevancePageCO.setMaterialName(materialDO.getName());
                    relevancePageCO.setMaterialNo(materialDO.getMaterialNo());
                    MaterialDO parentMaterialDO = materialMap.get(materialDO.getParentId());
                    if (parentMaterialDO != null) {
                        relevancePageCO.setParentMaterialName(parentMaterialDO.getName());
                    }
                }

                //设置产品类型
                relevancePageCO.setMaterialCategoryName(productCategoryMap.get(relevancePageCO.getCategoryId()));
                //设置型号名称
                ModelDO modelDO = modelMap.get(thirdSkuRelevanceDO.getModelId());
                if (modelDO != null) {
                    relevancePageCO.setModelName(modelDO.getName());
                    relevancePageCO.setModelNo(modelDO.getModelNo());
                    //归属父型号
                    ModelDO parentModelDO = modelMap.get(modelDO.getParentId());
                    if (parentModelDO != null) {
                        relevancePageCO.setParentModelName(parentModelDO.getName());
                    }
                }
                resultList.add(relevancePageCO);
            } catch (Exception e) {
                LOGGER.error("组装数据出现异常:{}",e);
            }
        });
        pageInfo.setList(resultList);
        return pageInfo;
    }

    public ThirdSkuRelevanceDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        ThirdSkuRelevanceDO relevanceDO = thirdSkuRelevanceDOMapper.selectByPrimaryKey(id);
        if(relevanceDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        return relevanceDO;
    }

    public List<ThirdSkuRelevanceDO> listByCondition(Integer distributorId,Short openFlag) {
        return thirdSkuRelevanceDOMapper.listByDistributorIdAndOpenFlag(distributorId,openFlag);
    }

    /**
     * 根据分销商id和第三方sku编码查询
     * @param distributorId
     * @param sku
     * @return
     */
    public ThirdSkuRelevanceDO getByDistributorIdAndThirdSkuNo(Integer distributorId, String sku) {
        return thirdSkuRelevanceDOMapper.getByDistributorIdAndThirdSkuNo(distributorId,sku);
    }
}
