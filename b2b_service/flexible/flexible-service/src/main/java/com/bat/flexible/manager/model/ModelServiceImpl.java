package com.bat.flexible.manager.model;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.model.ModelMaterialRelevanceServiceI;
import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.model.dto.ModelCmd;
import com.bat.flexible.api.model.dto.ModelDetailQry;
import com.bat.flexible.api.model.dto.ModelPageQry;
import com.bat.flexible.api.model.dto.ModelQry;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.product.ProductCategoryServiceI;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.model.ModelConstant;
import com.bat.flexible.manager.common.utils.proxy.FlexibleProxyUtil;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemCountDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.dubboapi.warehouse.warehouse.api.WarehouseServiceRpc;
import com.bat.flexible.api.distributor.exclude.DistributorModelExcludeServiceI;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.dao.distributor.dataobject.DistributorModelExcludeDO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.co.ModelMaterialRelevanceCO;
import com.bat.flexible.dao.model.co.ModelPageCO;
import com.bat.flexible.dao.model.co.ModelSimpleCO;
import com.bat.flexible.dao.model.co.ModelTreeCO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.model.ModelErrorCode;
import com.bat.flexible.manager.model.executor.ModelCmdExe;
import com.bat.flexible.manager.model.executor.ModelQryExe;
import com.bat.flexible.manager.model.validtor.ModelValidtor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ModelServiceImpl implements ModelServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelServiceImpl.class);

    @Autowired
    private ModelQryExe modelQryExe;

    @Autowired
    private ModelCmdExe modelCmdExe;

    @Autowired
    private ModelMaterialRelevanceServiceI modelMaterialRelevanceServiceI;

    @Autowired
    private DistributorModelExcludeServiceI distributorModelExcludeServiceI;


    @Autowired
    private ProductCategoryServiceI productCategoryServiceI;

    @Autowired
    private PictureServiceI pictureServiceI;

    @Autowired
    private ModelValidtor modelValidtor;

    @Autowired
    private MaterialServiceI materialServiceI;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private DistributorServiceRpc distributorServiceRpc;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @Override
    public List<ModelDO> listByModelIdList(List<Integer> modelIdList) {
        return modelQryExe.listByModelIdList(modelIdList);
    }

    @Override
    public List<ModelDO> listByIdList(List<Integer> idList) {
        if (idList == null || idList.size() == 0) {
            return new ArrayList<>();
        }
        return modelQryExe.listByIdList(idList);
    }

    /**
     * 新增型号
     * @param modelCmd
     * @param currentAdmin
     * @return
     */
    @Transactional
    @Override
    public Response create(ModelCmd modelCmd, AdminResponse currentAdmin) {
        LOGGER.info("创建型号："+ JSON.toJSONString(modelCmd));
        modelValidtor.checkParam(modelCmd,true);
        ModelDO modelDO = BeanUtils.copy(modelCmd,ModelDO.class);

        setAdminMsg(modelDO,currentAdmin);
        modelDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        //排在第一
        modelCmdExe.setSequenceAddByCategoryIdAndParentId(modelDO.getCategoryId(),modelDO.getParentId(),1,0,null);
        //设置最小序号
        modelDO.setSequence(1);
        modelCmdExe.create(modelDO);
        //处理材质和型号的关联关系
       if(modelCmd.getMaterialRelevanceDTOList() !=null && modelCmd.getMaterialRelevanceDTOList().size()>0){
           modelCmd.getMaterialRelevanceDTOList().stream().forEach(modelMaterialRelevanceDTO -> {
               modelMaterialRelevanceDTO.setModelId(modelDO.getId());
           });
       }
        modelMaterialRelevanceServiceI.saveModelMaterialRele(true,modelCmd.getMaterialRelevanceDTOList(),currentAdmin,true);
        //处理型号和分销商不可用
        distributorModelExcludeServiceI.saveModelRela(modelDO.getId(),modelCmd.getDistributorIdList(),currentAdmin,true);
        return Response.buildSuccess();
    }

    @Transactional
    @Override
    public Response update(ModelCmd modelCmd, AdminResponse currentAdmin) {
        LOGGER.info("修改型号："+ JSON.toJSONString(modelCmd));
        ModelDO modelDO = modelQryExe.getById(modelCmd.getId());
        //参数校验
        modelValidtor.checkParam(modelCmd,false);
        modelDO.setName(modelCmd.getName());
        modelDO.setEnglishName(modelCmd.getEnglishName());
        modelDO.setCategoryId(modelCmd.getCategoryId());
        modelDO.setParentId(modelCmd.getParentId());
        modelDO.setImage(modelCmd.getImage());
        modelDO.setDepict(modelCmd.getDepict());
        modelDO.setOpenFlag(modelCmd.getOpenFlag());
        modelDO.setAtLastTrademark(modelCmd.getAtLastTrademark());
        modelDO.setModelNo(modelCmd.getModelNo());
        modelDO.setNetworkModel(modelCmd.getNetworkModel());
        setAdminMsg(modelDO,currentAdmin);
        modelCmdExe.update(modelDO,true);
        //处理材质和型号的关联关系
        modelCmd.getMaterialRelevanceDTOList().stream().forEach(modelMaterialRelevanceDTO -> {
            modelMaterialRelevanceDTO.setModelId(modelDO.getId());
        });
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(modelCmd.getAtLastTrademark())){
            modelMaterialRelevanceServiceI.saveModelMaterialRele(true,modelCmd.getMaterialRelevanceDTOList(),currentAdmin,false);
        }
        //处理型号和分销商不可用
        distributorModelExcludeServiceI.saveModelRela(modelDO.getId(),modelCmd.getDistributorIdList(),currentAdmin,false);
        return Response.buildSuccess();
    }

    @Override
    public Response<ModelDetailQry> detailById(Integer id) {
        ModelDO modelDO = modelQryExe.getById(id);
        ModelDetailQry modelDetailQry = BeanUtils.copy(modelDO,ModelDetailQry.class);

        ProductCategoryDO productCategoryDO = productCategoryServiceI.getById(modelDetailQry.getCategoryId());
        modelDetailQry.setCategoryName(productCategoryDO.getName());
        List<ModelMaterialRelevanceCO> materialRelevanceCOList = modelMaterialRelevanceServiceI.listCOByModelIdAndMaterialId(modelDO.getId(),null);
        modelDetailQry.setMaterialRelevanceDTOList(materialRelevanceCOList);
        //分销商不可用
        List<DistributorModelExcludeDO> excludeDOList = distributorModelExcludeServiceI.listByModelId(id);
        if(excludeDOList !=null && excludeDOList.size()>0){
            List<DistributorSimpleRelaQry> distributorSimpleRelaQryList = new ArrayList<>();
            Map<Integer, DistributorRpcDTO> map = new HashMap<>();
            excludeDOList.stream().forEach(distributorModelExcludeDO -> {
                DistributorSimpleRelaQry simpleRelaQry = new DistributorSimpleRelaQry();
                simpleRelaQry.setId(distributorModelExcludeDO.getId());
                simpleRelaQry.setDistributorId(distributorModelExcludeDO.getDistributorId());
                //设置分销商信息
                flexibleDistributorQryExe.setDistributorNameMsg(simpleRelaQry,map);
                distributorSimpleRelaQryList.add(simpleRelaQry);
            });
            modelDetailQry.setDistributorExcludeList(distributorSimpleRelaQryList);
        }
        return Response.of(modelDetailQry);
    }

    @Override
    @Transactional
    public Response upOrDown(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin) {
        ModelDO modelDO = modelQryExe.getById(flexibleUpOrDownDTO.getId());
        ModelDO effect = modelQryExe.findEffectByUpOrDown(modelDO.getParentId(),flexibleUpOrDownDTO.getFlag(),modelDO.getSequence());
        if(effect==null && flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_UP_TOP_ERROR);
        }
        if(effect==null && !flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DOWN_LOWEST_ERROR);
        }
        //对调排序号
        Integer temp = modelDO.getSequence();
        modelDO.setSequence(effect.getSequence());
        effect.setSequence(temp);
        setAdminMsg(modelDO,currentAdmin);
        setAdminMsg(effect,currentAdmin);
        modelCmdExe.update(modelDO,false);
        //最后一条修改缓存
        modelCmdExe.update(effect,true);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin) {
        ModelDO modelDO = modelQryExe.getById(flexibleUpdateStatusDTO.getId());
        if(modelDO.getOpenFlag()-flexibleUpdateStatusDTO.getOpenFlag()==0){
            throw  new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        modelDO.setOpenFlag(flexibleUpdateStatusDTO.getOpenFlag());
        setAdminMsg(modelDO,currentAdmin);
        modelCmdExe.update(modelDO,true);
        return Response.buildSuccess();
    }

    @Override
    public Response deleteById(Integer id, AdminResponse currentAdmin) {
        ModelDO modelDO = modelQryExe.getById(id);
        //判断下面还有没有子分类
        List<ModelDO> list = modelQryExe.listByParentId(id);
        if(list !=null && list.size()>0){
            throw new FlexibleCustomException(ModelErrorCode.M_MODEL_DELETE_SON_FIRST);
        }
        setAdminMsg(modelDO,currentAdmin);
        modelDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
        modelCmdExe.update(modelDO,true);
        return Response.buildSuccess();
    }

    @Override
    public Response top(Integer id, AdminResponse currentAdmin) {
        ModelDO modelDO = modelQryExe.getById(id);
        if(modelDO.getSequence()==1){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_TOP_ALREADY_TOPEST_ERROR);
        }
        modelCmdExe.setSequenceAddByCategoryIdAndParentId(null,modelDO.getParentId(),1,null,modelDO.getSequence());
        modelDO.setSequence(1);
        setAdminMsg(modelDO,currentAdmin);
        modelCmdExe.update(modelDO,true);
        return Response.buildSuccess();
    }

    @Override
    public ModelDO getById(Integer modelId) {
        return modelQryExe.getById(modelId);
    }

    @Override
    public PageInfo<ModelPageCO> page(ModelPageQry modelPageQry) {
        if (modelPageQry.getParentId() ==null){
            modelPageQry.setParentId(FlexibleCommonConstant.COMMON_PARENT_ID);
        }
        List<ModelMaterialRelevanceDO> modelMaterialRelevanceDOList = null;
        if(modelPageQry.getMaterialIdList() != null && modelPageQry.getMaterialIdList().size()>0){
            modelMaterialRelevanceDOList = modelMaterialRelevanceServiceI.listByMaterialIdListAndOpenFlag(modelPageQry.getMaterialIdList(),modelPageQry.getOpenFlag());
        }
        List<ModelPageCO> list = modelQryExe.treeByCondition(modelPageQry.getParentId(),modelPageQry.getCategoryId(),modelPageQry.getContent(),modelPageQry.getOpenFlag(),modelPageQry.getAtLastTrademark(),
                modelMaterialRelevanceDOList);
        PageInfo<ModelPageCO> pageInfo = new PageInfo<>();
        pageInfo.setTotal(0);
        if(list !=null && list.size()>0){
            pageInfo.setTotal(list.size());
            //截取列表
            Integer startIndex =(modelPageQry.getPage()-1)*modelPageQry.getSize();

            if(startIndex >=list.size()){
                //已过了最后一页
                list=new ArrayList<>();
            }else if(modelPageQry.getPage()*modelPageQry.getSize() >list.size()){
                list = list.subList((modelPageQry.getPage()-1)*modelPageQry.getSize(),list.size());
                pageInfo.setIsLastPage(true);
            }else{
                list = list.subList((modelPageQry.getPage()-1)*modelPageQry.getSize(),(modelPageQry.getPage()*modelPageQry.getSize()));
                pageInfo.setIsLastPage(false);
            }

            pageInfo.setList(list);
        }
        return pageInfo;

    }

    @Override
    public List<ModelTreeCO> tree(ModelQry modelQry) {
        modelQry.setDistributorIds(flexibleDistributorQryExe.getDistributorTreePaths(modelQry.getDistributorId()));
        List<DistributorModelExcludeDO> excludeDOList = null;
        if(modelQry.getDistributorId() !=null){
            excludeDOList = distributorModelExcludeServiceI.listByDistributorIds(modelQry.getDistributorIds());
            
        }
        PictureDO pictureDO =null;
        if(modelQry.getPictureId() !=null && modelQry.getPictureId() >0){
            pictureDO = pictureServiceI.getById(modelQry.getPictureId()); 
        }
        if(modelQry.getMaterialId() ==null && modelQry.getItemId() !=null){
            MaterialDO materialDO = materialServiceI.getByItemId(modelQry.getItemId());
            modelQry.setMaterialId(materialDO.getId());
        }
        if(StringUtils.isNotBlank(modelQry.getMaterialNo())){
            MaterialDO materialDO = materialServiceI.getByMaterialNo(modelQry.getMaterialNo());
            modelQry.setMaterialId(materialDO.getId());
        }
        return modelQryExe.tree(modelQry,pictureDO,excludeDOList);
    }

    @Override
    public List<ModelTreeCO> treeNew(ModelQry modelQry) {
        //查出可用的机型
        List<ModelTreeCO> modelTrees = tree(modelQry);
        if (modelTrees == null) {
            modelTrees = new ArrayList<>();
        }
        //加上可用标志
        for (ModelTreeCO modelTree : modelTrees) {
            for (ModelTreeCO childModelTree : modelTree.getChildrenList()) {
                childModelTree.setValidFlag(FlexibleCommonConstant.FLAG_YES);
            }
        }
        //获取可用的父级id集合
        List<Integer> parentModelIds = modelTrees.stream().map(ModelTreeCO::getModelId).collect(Collectors.toList());
        //查出当前分销商所有可用并启用的父级型号
        List<ModelDO> allParentModels = modelQryExe.listAllCanUseParentsByDistributorIds(modelQry.getDistributorIds());
        for (ModelDO model : allParentModels) {
            //添加缺少的父级机型
            if (!parentModelIds.contains(model.getId())) {
                ModelTreeCO modelTree = new ModelTreeCO();
                org.springframework.beans.BeanUtils.copyProperties(model, modelTree);
                modelTree.setChildrenList(new ArrayList<>());
                modelTree.setValidFlag(FlexibleCommonConstant.FLAG_NO);
                //默认为材质原因
                modelTree.setValidType(ModelConstant.MODEL_VALID_TYPE_MATERIAL);
                if (modelQry.getMaterialId() == null && modelQry.getPictureId() != null) {
                    modelTree.setValidType(ModelConstant.MODEL_VALID_TYPE_PICTURE);
                }
                modelTree.setModelId(model.getId());
                modelTrees.add(modelTree);
            }
        }

        //查出当前分销商所有可用并启用的父级型号id集合
        List<Integer> allParentModelIds = allParentModels.stream().map(ModelDO::getId).collect(Collectors.toList());
        //查出当前分销商所有支持并启用的子级型号
        List<ModelDO> allChildModels = modelQryExe.listByParentIdsAndDistributorIdsAndMaterialId(allParentModelIds, modelQry.getDistributorIds(),modelQry.getMaterialId());
        //根据父级进行分组
        Map<Integer, List<ModelDO>> modelGroup = allChildModels.stream().collect(Collectors.groupingBy(ModelDO::getParentId));

        //处理子级
        for (ModelTreeCO modelTree : modelTrees) {
            if (modelTree.getChildrenList() == null) {
                modelTree.setChildrenList(new ArrayList<>());
            }
            //从分组获取子级型号
            List<ModelDO> childModels = modelGroup.get(modelTree.getModelId());
            if (childModels == null) {
                continue;
            }
            //获取当前子级id集合
            List<Integer> childModelIds = modelTree.getChildrenList().stream().map(ModelTreeCO::getModelId).collect(Collectors.toList());
            for (ModelDO model : childModels) {
                //添加缺少的子级机型
                if (!childModelIds.contains(model.getId())) {
                    ModelTreeCO childModelTree = new ModelTreeCO();
                    org.springframework.beans.BeanUtils.copyProperties(model, childModelTree);
                    childModelTree.setChildrenList(new ArrayList<>());
                    childModelTree.setValidFlag(FlexibleCommonConstant.FLAG_NO);
                    //默认为材质原因
                    childModelTree.setValidType(ModelConstant.MODEL_VALID_TYPE_MATERIAL);
                    if (modelQry.getMaterialId() == null && modelQry.getPictureId() != null) {
                        childModelTree.setValidType(ModelConstant.MODEL_VALID_TYPE_PICTURE);
                    }
                    childModelTree.setModelId(model.getId());
                    modelTree.getChildrenList().add(childModelTree);
                }
            }
        }
        //将没有子机型的过滤
        modelTrees.removeIf(modelTreeCO -> modelTreeCO.getChildrenList().size() == 0);

        if (modelQry.getMaterialId() != null) {
            //定义不可用子级型号id集合
            List<Integer> childModelIds = new ArrayList<>();
            for (ModelTreeCO modelTree : modelTrees) {
                for (ModelTreeCO childModelTree : modelTree.getChildrenList()) {
                    //if (childModelTree.getValidFlag() == FlexibleCommonConstant.FLAG_NO) {
                        childModelIds.add(childModelTree.getModelId());
                   // }
                }
            }
            //查出当前材质支持的型号
            List<ModelDO> modelsByMaterial = modelQryExe.findByIdsAndMaterialIdAndPictureId(childModelIds, modelQry.getMaterialId(), null);
            List<Integer> modelIdsByMaterial = modelsByMaterial.stream().map(ModelDO::getId).collect(Collectors.toList());

            //查出当前图片支持的型号id集合
            List<Integer> modelIdsByPicture = null;
            for (ModelTreeCO modelTree : modelTrees) {
                for (ModelTreeCO childModelTree : modelTree.getChildrenList()) {
                  //  if(childModelTree.getValidFlag()==FlexibleCommonConstant.FLAG_NO){
                        //记录处理标志
                        boolean hasDeal = false;
                        //查看是否材质不适用
                        if (!modelIdsByMaterial.contains(childModelTree.getModelId())) {
                            childModelTree.setValidType(ModelConstant.MODEL_VALID_TYPE_MATERIAL);
                            childModelTree.setValidFlag(FlexibleCommonConstant.FLAG_NO);
                            hasDeal = true;
                        }
                        if (!hasDeal&&modelQry.getPictureId() != null) {
                            if(childModelTree.getValidFlag()==FlexibleCommonConstant.FLAG_NO) {
                                //图片支持的型号id为空则初始化
                                List<ModelDO> modelsByPicture = modelQryExe.findByIdsAndMaterialIdAndPictureId(childModelIds, null, modelQry.getPictureId());
                                if (modelIdsByPicture == null) {
                                    modelIdsByPicture = modelsByPicture.stream().map(ModelDO::getId).collect(Collectors.toList());
                                }
                                //查看是否图片不适用
                                if (!modelIdsByPicture.contains(childModelTree.getModelId())) {
                                    childModelTree.setValidType(ModelConstant.MODEL_VALID_TYPE_PICTURE);
                                }
                            }
                        }
                  //  }
                }
            }
        }
        return modelTrees;
    }

    @Override
    public List<ModelDO> listAll() {
        return modelQryExe.listAll();
    }

    /**
     *
     * @param modelPageQry
     * @return
     */
    @Override
    public PageInfo<ModelSimpleCO> pageSimpleCO(ModelPageQry modelPageQry) {
        //根据父id、递归拿到所有的子节点id
        LOGGER.info("开始获取子型号列表");
        List<Integer> modelIdList = modelQryExe.getSonIdList(modelPageQry.getParentId(),modelPageQry.getCategoryId(),modelPageQry.getOpenFlag());
        List<ModelSimpleCO> list = null;
        if(modelIdList ==null || modelIdList.size()==0){
            //查不到
            list = new ArrayList<>();
            PageInfo<ModelSimpleCO> pageInfo = new PageInfo<>(list);
            return pageInfo;
        }
        LOGGER.info("开始分页查询");
        PageHelper.startPage(modelPageQry.getPage(),modelPageQry.getSize());
        list = modelQryExe.listSimpleCO(
                modelPageQry.getContent(),modelPageQry.getAtLastTrademark(),modelIdList);
        LOGGER.info("分页查询完毕");
        if(list == null ){
            //要不然下一步会报空指针
          list = new ArrayList<>();
        }
        return new PageInfo<>(list);
    }

    @DubboReference(check = false,timeout = 1000000,retries = 0)
    private WarehouseServiceRpc warehouseServiceRpc;

    @DubboReference(check = false,timeout = 1000000,retries = 0)
    private WarehouseStockServiceRpc  warehouseStockServiceRpc;

    private static Integer index=0;

    private synchronized static Integer getIndex(){
        return index++;
    }

    @Override
    public Response test() {
        /*Integer i = getIndex();
        LOGGER.info(i+"开始处理:"+new Date());
        warehouseServiceRpc.test(i);*/
        List<GoodsItemCountDTO> itemCountList= new ArrayList<>();
        GoodsItemCountDTO goodsItemCountDTO = new GoodsItemCountDTO();
        goodsItemCountDTO.setItemId(969);
        goodsItemCountDTO.setInStockCount(100);
        goodsItemCountDTO.setOnWayCount(0);
        goodsItemCountDTO.setSupportOversoldFlag(false);
        itemCountList.add(goodsItemCountDTO);
        GoodsItemCountDTO goodsItemCountDTO2 = new GoodsItemCountDTO();
        goodsItemCountDTO2.setItemId(12537);
        goodsItemCountDTO2.setInStockCount(100);
        goodsItemCountDTO2.setOnWayCount(0);
        goodsItemCountDTO2.setSupportOversoldFlag(false);
        itemCountList.add(goodsItemCountDTO2);
        List<Integer> areaList = new ArrayList<>();
        areaList.add(9);
        areaList.add(11);
        areaList.add(0);
        com.bat.dubboapi.warehouse.common.Response<List<ItemStockLockDTO>> response = warehouseStockServiceRpc.summaryLockStock(itemCountList, areaList, 2601, null);
        List<ItemStockLockDTO> data = response.getData();
        LOGGER.info("返回库存锁库："+JSON.toJSONString(data));
        return Response.of(response);
    }

    @Override
    public ModelDO getOneByNetworkModel(String networkModel) {

        return modelQryExe.getOneByNetworkModel(networkModel);
    }

    private void setAdminMsg(ModelDO modelDO, AdminResponse currentAdmin) {
        if(modelDO.getId() ==null){
            modelDO.setCreateTime(new Date());
            modelDO.setCreateUserId(currentAdmin.getId());
            modelDO.setCreateUserName(currentAdmin.getUserName());
        }
        modelDO.setUpdateTime(new Date());
        modelDO.setUpdateUserId(currentAdmin.getId());
        modelDO.setUpdateUserName(currentAdmin.getUserName());
    }


}
