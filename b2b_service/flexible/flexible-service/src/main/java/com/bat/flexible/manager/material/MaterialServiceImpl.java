package com.bat.flexible.manager.material;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.material.dto.*;
import com.bat.flexible.api.model.ModelMaterialRelevanceServiceI;
import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.model.dto.rela.ModelMaterialRelevanceDTO;
import com.bat.flexible.api.picture.PictureMaterialRelevanceServiceI;
import com.bat.flexible.api.picture.dto.PictureRelaSimpleDTO;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.material.convertor.MaterialConvertor;
import com.bat.flexible.manager.material.executor.MaterialCmdExe;
import com.bat.flexible.manager.material.executor.MaterialQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.distributor.distributor.api.DistributorCustomPriceServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorCustomerPriceDTO;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.flexible.api.distributor.exclude.DistributorMaterialExcludeServiceI;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.api.material.dto.*;
import com.bat.flexible.dao.distributor.dataobject.DistributorMaterialExcludeDO;
import com.bat.flexible.dao.material.co.MaterialPageCO;
import com.bat.flexible.dao.material.co.MaterialSimpleTreeCO;
import com.bat.flexible.dao.material.co.MaterialTreeCO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.co.ModelMaterialRelevanceCO;
import com.bat.flexible.dao.model.co.ModelSimpleCO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.picture.dataobject.PictureMaterialRelevanceDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.material.MaterialConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.material.MaterialErrorCode;
import com.bat.flexible.manager.message.MessageService;
import com.bat.flexible.manager.picture.convertor.PictureRelevanceConvertor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialServiceImpl.class);

    @Autowired
    private MaterialQryExe materialQryExe;

    @Autowired
    private MaterialCmdExe materialCmdExe;

    @Autowired
    private ModelServiceI modelServiceI;

    @Autowired
    private ModelMaterialRelevanceServiceI modelMaterialRelevanceServiceI;

    @Autowired
    private DistributorMaterialExcludeServiceI distributorMaterialExcludeServiceI;

    @Autowired
    private PictureMaterialRelevanceServiceI pictureMaterialRelevanceServiceI;

    @DubboReference(check = false,timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false,timeout = 5000)
    private DistributorCustomPriceServiceRpc distributorCustomPriceServiceRpc;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MaterialConvertor materialConvertor;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @Value("${distributor.id.sanxing}")
    private Integer sanxingDistributorId;

    //炫彩肤感壳材质id
    @Value("${material.skin.id}")
    private Integer materialSkinId;

    //真彩创意壳材质id
    @Value("${material.true.color.id}")
    private Integer materialTrueColorId;

    //亮透彩印软壳材质id
    @Value("${material.soft.shell.id}")
    private Integer materialSoftShellId;

    //晶透浮雕壳材质id
    @Value("${material.relief.id}")
    private Integer materialReliefId;


    @Override
    public List<MaterialDO> listByMaterialIdList(List<Integer> materialIdList) {
        return materialQryExe.listByMaterialIdList(materialIdList);
    }

    @Override
    @Transactional
    public Response create(MaterialSaveCmd materialSaveCmd, AdminResponse currentAdmin) {
        LOGGER.info("新增材质："+ JSON.toJSONString(materialSaveCmd));
        //参数校验
        checkParam(materialSaveCmd,true);
        MaterialDO materialDO = BeanUtils.copy(materialSaveCmd,MaterialDO.class);
        setAdminMsg(materialDO,currentAdmin);
        materialDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        //设置排序号
        Integer max = materialQryExe.findMaxByParentId(materialDO.getParentId());
        materialDO.setSequence(max+1);
        materialCmdExe.create(materialDO);
        if(materialSaveCmd.getMaterialRelevanceDTOList() !=null &&materialSaveCmd.getMaterialRelevanceDTOList().size()>0){
            for(ModelMaterialRelevanceDTO dto:materialSaveCmd.getMaterialRelevanceDTOList()){
                dto.setMaterialId(materialDO.getId());
            }
        }
        //设置材质和型号的关联关系
        modelMaterialRelevanceServiceI.saveModelMaterialRele(false,materialSaveCmd.getMaterialRelevanceDTOList(),currentAdmin,true);
        //设置分销商不可用关系
        distributorMaterialExcludeServiceI.save(materialDO.getId(),materialSaveCmd.getDistributorIdList(),currentAdmin,true);
        //材质和图片关联关系
        pictureMaterialRelevanceServiceI.saveRela(false, materialSaveCmd.getPictureIdList(),Lists.newArrayList(materialDO.getId()),currentAdmin,true);
        return Response.buildSuccess();
    }

    /**
     * 参数校验
     * @param materialSaveCmd
     * @param isAdd
     */
    private void checkParam(MaterialSaveCmd materialSaveCmd,Boolean isAdd) {
        if(materialSaveCmd.getId() !=null && isAdd){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_MUST_BE_NULL_WHEN_ADD);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(materialSaveCmd.getAtLastTrademark()) && StringUtils.isBlank(materialSaveCmd.getMaterialNo())){
            //最低级材质编码不能为空
            throw new FlexibleCustomException(MaterialErrorCode.MATERIAL_NO_NULL);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(materialSaveCmd.getAtLastTrademark()) && StringUtils.isBlank(materialSaveCmd.getManufactor())){
            //最低级材质工厂不能为空
            throw new FlexibleCustomException(MaterialErrorCode.MANUFACTOR_NULL);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(materialSaveCmd.getAtLastTrademark()) && StringUtils.isBlank(materialSaveCmd.getSubtitle())){
            //最低级材质副标题不能为空
            throw new FlexibleCustomException(MaterialErrorCode.SUBTITLE_NULL);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(materialSaveCmd.getAtLastTrademark()) && materialSaveCmd.getAllowUploadFlag()==null){
            //最低级材质是否允许上传图片不能为空
            throw new FlexibleCustomException(MaterialErrorCode.ALLOW_UPDATE_PICTURE_FLAG_NULL);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(materialSaveCmd.getAtLastTrademark()) && materialSaveCmd.getItemId()==null){
            //最低级材质货品id不能为空
            throw new FlexibleCustomException(MaterialErrorCode.M_MATERIAL_RELEVANCE_ITEM_ID_NULL);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(materialSaveCmd.getAtLastTrademark()) && StringUtils.isBlank(materialSaveCmd.getItemCode())){
            //最低级材质货品编码不能为空
            throw new FlexibleCustomException(MaterialErrorCode.M_MATERIAL_RELEVANCE_ITEM_CODE_NULL);
        }
        if(StringUtils.isNotBlank(materialSaveCmd.getMaterialNo())){
            //判断材质编码是否重复
            MaterialDO materialDO = materialQryExe.getByMaterialNo(materialSaveCmd.getMaterialNo(),false);
            if(materialDO !=null && ( isAdd || (!isAdd && materialDO.getId() - materialSaveCmd.getId() !=0))){
                throw new FlexibleCustomException(MaterialErrorCode.MATERIAL_NO_ONLY);
            }
        }
        if(materialSaveCmd.getItemId() !=null){
            MaterialDO materialDO = materialQryExe.getByItemId(materialSaveCmd.getItemId());
            if (isAdd && materialDO != null || (!isAdd && materialDO != null && materialDO.getId() - materialSaveCmd.getId() != 0)) {
                throw new FlexibleCustomException(MaterialErrorCode.M_ITEM_RELEVANCE_MATERIAL_REPEAT);
            }
        }
        //校验材质的关联列表
        modelMaterialRelevanceServiceI.checkModelMaterialRelevanceList(false,materialSaveCmd.getMaterialRelevanceDTOList(),isAdd,materialSaveCmd.getId());
    }



    /**
     * 修改材质
     * @param materialSaveCmd
     * @param currentAdmin
     * @return
     */
    @Override
    public Response update(MaterialSaveCmd materialSaveCmd, AdminResponse currentAdmin) {
        LOGGER.info("修改材质："+ JSON.toJSONString(materialSaveCmd));
        MaterialDO materialDO = materialQryExe.getById(materialSaveCmd.getId());
        //是否原来是最终材质、后续改为非最终材质(需要清除关联关系)
        Boolean updateFlag = false;
        Integer itemId = materialDO.getItemId();
        //参数校验
        checkParam(materialSaveCmd,false);
        BeanUtils.copyAndCover(materialSaveCmd,materialDO);
        setAdminMsg(materialDO,currentAdmin);
        materialCmdExe.update(materialDO);

        //设置分销商不可用关系
        distributorMaterialExcludeServiceI.save(materialDO.getId(),materialSaveCmd.getDistributorIdList(),currentAdmin,false);
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(materialDO.getAtLastTrademark())){
            //设置材质和型号的关联关系
            modelMaterialRelevanceServiceI.saveModelMaterialRele(false,materialSaveCmd.getMaterialRelevanceDTOList(),currentAdmin,false);
            //材质和图片关联关系
            pictureMaterialRelevanceServiceI.saveRela(false, materialSaveCmd.getPictureIdList(),Lists.newArrayList(materialDO.getId()),currentAdmin,false);
        }
        if( (itemId !=null && materialSaveCmd.getItemId() !=null && itemId - materialSaveCmd.getItemId() !=0) || (itemId !=null && materialSaveCmd.getItemId() ==null)
        ){
            //货品被修改、同步修改分销商C端价格
            distributorCustomPriceServiceRpc.updateItemIdByMaterialRelevanceItemIdChange(itemId,materialSaveCmd.getItemId(),currentAdmin.getId(),currentAdmin.getUserName());
        }
        return Response.buildSuccess();
    }

    @Override
    public Response<MaterialDetailQry> detailById(Integer id) {

        MaterialDO materialDO = materialQryExe.getById(id);

        MaterialDetailQry materialDetailQry = BeanUtils.copy(materialDO,MaterialDetailQry.class);

        List<ModelMaterialRelevanceCO> materialRelevanceCOList = modelMaterialRelevanceServiceI.listCOByModelIdAndMaterialId(null,id);

        if(materialRelevanceCOList==null){
            materialRelevanceCOList=new ArrayList<>();
        }
        Set<Integer> modelParentIds = materialRelevanceCOList.stream().map(ModelMaterialRelevanceCO::getModelParentId).collect(Collectors.toSet());
        List<Integer> modelParentIdList = new ArrayList<>(modelParentIds);
        List<ModelDO> parentModels = modelServiceI.listByIdList(modelParentIdList);
        List<ModelSimpleCO> parentModelCOS = new ArrayList<>();
        for (ModelDO modelDO : parentModels) {
            ModelSimpleCO modelSimpleCO = new ModelSimpleCO();
            org.springframework.beans.BeanUtils.copyProperties(modelDO, modelSimpleCO);
            parentModelCOS.add(modelSimpleCO);
        }
        materialDetailQry.setParentModel(parentModelCOS);
        materialDetailQry.setMaterialRelevanceDTOList(materialRelevanceCOList);
        //分销商不可用
        materialConvertor.queryDistributorExclude(id, materialDetailQry);

        //设置材质和图片的关联关系
        List<PictureMaterialRelevanceDO> pictureMaterialList = pictureMaterialRelevanceServiceI.listByPictureIdAndMaterialId(null,id);

        List<PictureRelaSimpleDTO> pictureRelaSimpleDTOList = PictureRelevanceConvertor.toPictureRelaSimpleDTOListFromMaterial(pictureMaterialList);

        materialDetailQry.setPictureRelaList(pictureRelaSimpleDTOList);
        materialDetailQry.setItemSimplePageBean(materialConvertor.toGoodsItemSimplePageBeanByItemId(materialDO.getItemId(),materialDO.getItemCode()));
        return Response.of(materialDetailQry);
    }

    @Override
    @Transactional
    public Response upOrDown(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin) {
        MaterialDO materialDO = materialQryExe.getById(flexibleUpOrDownDTO.getId());
        MaterialDO effect = materialQryExe.findEffectByUpOrDown(materialDO.getParentId(),flexibleUpOrDownDTO.getFlag(),materialDO.getSequence());
        if(effect==null && flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_UP_TOP_ERROR);
        }
        if(effect==null && !flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DOWN_LOWEST_ERROR);
        }
        //对调排序号
        Integer temp = materialDO.getSequence();
        materialDO.setSequence(effect.getSequence());
        effect.setSequence(temp);
        setAdminMsg(materialDO,currentAdmin);
        setAdminMsg(effect,currentAdmin);
        materialCmdExe.update(materialDO);
        materialCmdExe.update(effect);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin) {
        MaterialDO materialDO = materialQryExe.getById(flexibleUpdateStatusDTO.getId());
        materialDO.setOpenFlag(flexibleUpdateStatusDTO.getOpenFlag());
        setAdminMsg(materialDO,currentAdmin);
        materialCmdExe.update(materialDO);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response deleteById(Integer id, AdminResponse currentAdmin) {
        MaterialDO materialDO = materialQryExe.getById(id);
        if(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES.equals(materialDO.getOpenFlag())){
            //先下架才能删除
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DELETE_AFTER_DISABLE_ERROR);
        }
        List<MaterialDO> materialDOList = materialQryExe.listByParentId(materialDO.getId());
        if(materialDOList !=null && materialDOList.size()>0){
            throw new FlexibleCustomException(MaterialErrorCode.M_MATERIAL_DELETE_MUST_DELETE_SON_FIRST);
        }
        materialDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
        setAdminMsg(materialDO,currentAdmin);
        materialCmdExe.update(materialDO);
        //需要删除关联关系
        pictureMaterialRelevanceServiceI.delete(id,null,currentAdmin);
        //删除型号关系
        modelMaterialRelevanceServiceI.deleteByMaterialIdAndModelId(id,null,currentAdmin);
        return Response.buildSuccess();
    }

    @Override
    public MaterialDO getById(Integer materialId) {
        return materialQryExe.getById(materialId);
    }

    @Override
    public PageInfo<MaterialPageCO> page(MaterialPageQry materialPageQry) {

        List<MaterialPageCO> list = materialQryExe.treeByCondition(materialPageQry.getCategoryId(),materialPageQry.getOpenFlag(),materialPageQry.getContent(),
                materialPageQry.getParentId());
        PageInfo<MaterialPageCO> pageInfo = new PageInfo<>();
        pageInfo.setTotal(0);
        if(list !=null && list.size()>0){
            pageInfo.setTotal(list.size());
            //截取列表
            Integer startIndex =(materialPageQry.getPage()-1)*materialPageQry.getSize();

            if(startIndex >=list.size()){
                //已过了最后一页
                list=new ArrayList<>();
            }else if(materialPageQry.getPage()*materialPageQry.getSize() >list.size()){
                list = list.subList((materialPageQry.getPage()-1)* materialPageQry.getSize(),list.size());
                pageInfo.setIsLastPage(true);
            }else{
                list = list.subList((materialPageQry.getPage()-1) * materialPageQry.getSize(),(materialPageQry.getPage()*materialPageQry.getSize()));
                pageInfo.setIsLastPage(false);
            }

            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    public List<MaterialTreeCO> tree(MaterialTreeQry materialTreeQry) {
        materialTreeQry.setDistributorIds(flexibleDistributorQryExe.getDistributorTreePaths(materialTreeQry.getDistributorId()));
        List<DistributorMaterialExcludeDO> excludeDOList = null;
        if(materialTreeQry.getDistributorId() !=null){
            excludeDOList = distributorMaterialExcludeServiceI.listByDistributorIds(materialTreeQry.getDistributorIds());
        }
        List<PictureMaterialRelevanceDO> relevanceDOList = null;
        if(materialTreeQry.getPictureId() !=null && materialTreeQry.getPictureId() >0){
            relevanceDOList = pictureMaterialRelevanceServiceI.listByPictureIdAndMaterialId(materialTreeQry.getPictureId(), null);
        }
        return materialQryExe.tree(materialTreeQry,excludeDOList,relevanceDOList);
    }

    @Override
    public void swapTree(List<MaterialTreeCO> list, Integer distributorId) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (distributorId == null || distributorId != sanxingDistributorId.intValue()) {
            return;
        }
        //初始化下标
        int index=0;
        //炫彩肤感壳
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaterialId() == materialSkinId.intValue()) {
                if (i != index) {
                    Collections.swap(list, index, i);
                }
                index=index+1;
                break;
            }
        }
        //真彩创意壳
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaterialId() == materialTrueColorId.intValue()) {
                if (i != index) {
                    Collections.swap(list, index, i);
                }
                index=index+1;
                break;
            }
        }
        //晶透浮雕壳
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaterialId() == materialReliefId.intValue()) {
                if (i != index) {
                    Collections.swap(list, index, i);
                }
                index=index+1;
                break;
            }
        }
        //亮透彩印软壳
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaterialId() == materialSoftShellId.intValue()) {
                if (i != index) {
                    Collections.swap(list, index, i);
                }
                break;
            }
        }


    }

    @Override
    public List<MaterialTreeCO> treeNew(MaterialTreeQry materialTreeQry) {
        //查出正常可用的材质数
        List<MaterialTreeCO> materialTrees = tree(materialTreeQry);
        if (materialTrees == null) {
            materialTrees = new ArrayList<>();
        }
        //加加上可用标志
        for (MaterialTreeCO materialTree : materialTrees) {
            for (MaterialTreeCO childMaterialTree : materialTree.getChildrenList()) {
                childMaterialTree.setValidFlag(FlexibleCommonConstant.FLAG_YES);
            }
        }
        //获取可用的父级id集合
        List<Integer> parentMaterialIds = materialTrees.stream().map(MaterialTreeCO::getMaterialId).collect(Collectors.toList());
        //查出当前分销商所有可用并启用的父级材质
        List<MaterialDO> allParentMaterials = materialQryExe.findCanUseParentsByDistributorIds(materialTreeQry.getDistributorIds());

        for (MaterialDO material : allParentMaterials) {
            //补上缺少的材质；并标志为不适用
            if (!parentMaterialIds.contains(material.getId())) {
                MaterialTreeCO materialTree = new MaterialTreeCO();
                org.springframework.beans.BeanUtils.copyProperties(material, materialTree);
                materialTree.setChildrenList(new ArrayList<>());
                materialTree.setValidFlag(FlexibleCommonConstant.FLAG_NO);
                //默认为型号原因
                materialTree.setValidType(MaterialConstant.MATERIAL_VALID_TYPE_MODEL);
                if (materialTreeQry.getModelId() == null && materialTreeQry.getPictureId() != null) {
                    materialTree.setValidType(MaterialConstant.MATERIAL_VALID_TYPE_PICTURE);
                }
                materialTree.setMaterialId(material.getId());
                materialTrees.add(materialTree);
            }
        }

        //查出当前分销商所有可用并启用的父级材质id集合
        List<Integer> allParentMaterialIds = allParentMaterials.stream().map(MaterialDO::getId).collect(Collectors.toList());
        //查出当前分销商所有支持并启用的子级材质
        List<MaterialDO> allChildMaterials = materialQryExe.listByParentIdsAndDistributorIds(allParentMaterialIds, materialTreeQry.getDistributorIds());
        //根据父级进行分组
        Map<Integer, List<MaterialDO>> materialGroup = allChildMaterials.stream().collect(Collectors.groupingBy(MaterialDO::getParentId));

        //处理子级
        for (MaterialTreeCO materialTreeCO : materialTrees) {
            if (materialTreeCO.getChildrenList() == null) {
                materialTreeCO.setChildrenList(new ArrayList<>());
            }
            //从分组获取子级材质
            List<MaterialDO> childMaterials = materialGroup.get(materialTreeCO.getMaterialId());
            if (childMaterials == null) {
                continue;
            }
            //获取当前子级id集合
            List<Integer> childMaterialIds = materialTreeCO.getChildrenList().stream().map(MaterialTreeCO::getMaterialId).collect(Collectors.toList());

            for (MaterialDO materialDO : childMaterials) {
                //添加缺少的子级,并做不适用标志
                if (!childMaterialIds.contains(materialDO.getId())) {
                    MaterialTreeCO childMaterialTree = new MaterialTreeCO();
                    org.springframework.beans.BeanUtils.copyProperties(materialDO, childMaterialTree);
                    childMaterialTree.setValidFlag(FlexibleCommonConstant.FLAG_NO);
                    childMaterialTree.setMaterialId(materialDO.getId());
                    //默认为机型原因
                    childMaterialTree.setValidType(MaterialConstant.MATERIAL_VALID_TYPE_MODEL);
                    if (materialTreeQry.getModelId() == null && materialTreeQry.getPictureId() != null) {
                        childMaterialTree.setValidType(MaterialConstant.MATERIAL_VALID_TYPE_PICTURE);
                    }
                    materialTreeCO.getChildrenList().add(childMaterialTree);
                }
            }
        }
        //将没有子材质的过滤
        materialTrees.removeIf(modelTreeCO -> modelTreeCO.getChildrenList().size() == 0);

        //当材质和型号都传参时候;判断是材质不支持还是型号不支持
        if (materialTreeQry.getModelId() != null && materialTreeQry.getPictureId() != null) {
            //定义不可用子级材质id集合
            List<Integer> childMaterialIds = new ArrayList<>();
            for (MaterialTreeCO materialTree : materialTrees) {
                for(MaterialTreeCO childMaterialTree:materialTree.getChildrenList()){
                    if(childMaterialTree.getValidFlag()==FlexibleCommonConstant.FLAG_NO){
                        childMaterialIds.add(childMaterialTree.getMaterialId());
                    }
                }
            }
            //查出当前型号支持的材质
            List<MaterialDO> materialsByModel = materialQryExe.findByIdsAndModelIdAndPictureId(childMaterialIds, materialTreeQry.getModelId(), null);
            List<Integer> materialIdsByModel = materialsByModel.stream().map(MaterialDO::getId).collect(Collectors.toList());
            //查出当前图片支持的材质id
            List<Integer> materialIdsByPicture = null;
            for (MaterialTreeCO modelTreeCO : materialTrees) {
                for (MaterialTreeCO childModelTree : modelTreeCO.getChildrenList()) {
                    if(childModelTree.getValidFlag()==FlexibleCommonConstant.FLAG_NO) {
                        //记录处理标志
                        boolean hasDeal = false;
                        //查看是否型号不适用
                        if (!materialIdsByModel.contains(childModelTree.getMaterialId())) {
                            childModelTree.setValidType(MaterialConstant.MATERIAL_VALID_TYPE_MODEL);
                            hasDeal = true;
                        }
                        if (!hasDeal) {
                            List<MaterialDO> materialsByPicture = materialQryExe.findByIdsAndModelIdAndPictureId(childMaterialIds, null, materialTreeQry.getPictureId());
                            //图片支持的材质id为空则初始化
                            if (materialIdsByPicture == null) {
                                materialIdsByPicture = materialsByPicture.stream().map(MaterialDO::getId).collect(Collectors.toList());
                            }
                            //查看是否图片不适用
                            if (!materialIdsByPicture.contains(childModelTree.getMaterialId())) {
                                childModelTree.setValidType(MaterialConstant.MATERIAL_VALID_TYPE_PICTURE);
                            }

                        }
                    }
                }
            }
        }
        for (MaterialTreeCO modelTreeCO : materialTrees) {
            if (modelTreeCO.getChildrenList() != null && modelTreeCO.getChildrenList().size() == 1) {
                MaterialTreeCO childMaterialTreeCO = modelTreeCO.getChildrenList().get(0);
                modelTreeCO.setValidFlag(childMaterialTreeCO.getValidFlag());
                modelTreeCO.setValidType(childMaterialTreeCO.getValidType());
            }
        }
        return materialTrees;
    }

    @Override
    public List<MaterialDO> listAll() {
        return materialQryExe.listAll();
    }

    @Override
    public MaterialDO getByMaterialNo(String materialNo) {
        return materialQryExe.getByMaterialNo(materialNo,true);
    }



    @Override
    public Response<List<MaterialSimpleTreeCO>> treeAdmin(MaterialTreeAdminQry materialTreeAdminQry) {
        if(materialTreeAdminQry.getParentId() ==null){
            materialTreeAdminQry.setParentId(FlexibleCommonConstant.COMMON_PARENT_ID);
        }
        List<MaterialSimpleTreeCO> treeCOList = materialQryExe.treeAdmin(materialTreeAdminQry);
        return Response.of(treeCOList);
    }

    @Override
    public MaterialDO getByItemId(Integer itemId) {
        return materialQryExe.getByItemId(itemId);
    }

    /**
     * 获取材质对应的货品信息
     * @param materialItemQry
     * @return
     */
    @Override
    public GoodsItemRpcQry getItemMsgForMaterial(MaterialItemQry materialItemQry) {
        //判断三个参数
        MaterialConvertor.validItemQuery(materialItemQry);
        if(StringUtils.isBlank(materialItemQry.getItemCode()) && materialItemQry.getItemId() ==null && materialItemQry.getMaterialId() !=null){
            //只传了材质id、货品信息没有传
            MaterialDO materialDO = materialQryExe.getById(materialItemQry.getMaterialId());
            materialItemQry.setItemId(materialDO.getItemId());
        }
        GoodsItemRpcDTO goodsItemRpcDTO= null;
        if(materialItemQry.getItemId() !=null){
            List<Integer> itemIdList = new ArrayList<>();
            itemIdList.add(materialItemQry.getItemId());
            LOGGER.info("查询："+JSON.toJSONString(itemIdList));
            com.bat.dubboapi.goods.common.Response<List<GoodsItemRpcDTO>> itemResponse = goodsServiceRpc.listGoodsItemByIds(itemIdList);
            List<GoodsItemRpcDTO> data = itemResponse.getData();
            if(data ==null || data.size() ==0){
                throw new FlexibleCustomException(MaterialErrorCode.G_ITEM_ID_ERROR);
            }
            goodsItemRpcDTO = data.get(0);
        }else{
            List<String> itemCodeList = new ArrayList<>();
            itemCodeList.add(materialItemQry.getItemCode());
            com.bat.dubboapi.goods.common.Response<List<GoodsItemRpcDTO>> itemResponse = goodsServiceRpc.listGoodsItemByCodes(itemCodeList);
            List<GoodsItemRpcDTO> data = itemResponse.getData();
            if(data ==null || data.size() ==0){
                throw new FlexibleCustomException(MaterialErrorCode.G_ITEM_CODE_ERROR);
            }
            goodsItemRpcDTO = data.get(0);
        }
        return BeanUtils.copy(goodsItemRpcDTO, GoodsItemRpcQry.class);
    }

    @Override
    public Response getPriceByCondition(Integer distributorId,Integer materialId,String orderSource) {
        LOGGER.info("获取价格以及sku信息：分销商Id:"+distributorId+",材质id："+materialId+",orderSource:"+orderSource);
        MaterialDO materialDO = materialQryExe.getById(materialId);

        com.bat.dubboapi.distributor.common.Response<DistributorCustomerPriceDTO> distributorCustomPriceResp = distributorCustomPriceServiceRpc.getByDistributorIdAndItemId(distributorId, materialDO.getItemId());
        DistributorCustomerPriceDTO  distributorCustomerPriceDTO= distributorCustomPriceResp.getData();
        LOGGER.info("获取分销商C端价格distributorId{},itemId{},返回{}",distributorId,materialDO.getItemId(),JSON.toJSONString(distributorCustomPriceResp));
        if(distributorCustomerPriceDTO !=null){
            return Response.of(distributorCustomerPriceDTO.getPrice());
        }
        /*DistributorUrl distributorUrl = distributorUrlDBManager.findByDistributorAndOrderSourceAndType(distributorId, orderSource, TYPE4);// 4代表定制价格获取
        if(distributorId == 752 || distributorId == 1762 || (distributorUrl !=null && distributorUrl.getDevelopSource() !=null && distributorUrl.getDevelopSource() -
                DistributorConfigConstants.DevelopSourceJiuJi==0)){

            HttpClientBuilder builder = HttpClientBuilder.create();
            CloseableHttpClient httpClient = builder.build();
            HttpGet get = new HttpGet();
            StringBuilder stringBuilder=new StringBuilder();
            if(distributorUrl != null){
                stringBuilder.append(distributorUrl.getUrl()).append(itemCode);
            }else {
                if(distributorId == 752) {
                    stringBuilder.append(hostname).append(path).append(itemCode);
                }else if(distributorId == 1762){
                    stringBuilder.append(jiaHaiHostname).append(path).append(itemCode);
                }
            }
            String url=stringBuilder.toString();
            get.setURI(URI.create(url));
            try {
                CloseableHttpResponse response = httpClient.execute(get);
                if (response.getStatusLine().getStatusCode()==200){
                    HttpEntity entity = response.getEntity();
                    String json = EntityUtils.toString(entity, Charset.defaultCharset());
                    PriceModel priceModel = JSON.parseObject(json, PriceModel.class);
                    if (priceModel.getCode()==0){
                        BigDecimal data = priceModel.getData();
                        map.put("price",data);
                    }else {
                        logger.info("获取价格失败:"+priceModel.getMsg());
                        throw new ApiException("获取价格失败:"+priceModel.getMsg());
                    }
                }else {
                    logger.info("请求接口发生异常");
                    throw new ApiException("请求接口发生异常");
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("请求接口发生异常");
                throw new ApiException("请求接口发生异常");
            }
        } */

        return Response.buildFailure("无法获取到分销商价格","无法获取到分销商价格");
    }

    /**
     * 后台查询材质列表（非树形）
     * @param materialTreeAdminQry
     * @return
     */
    @Override
    public PageInfo<MaterialSimpleDTO> pageWithoutTree(MaterialTreeAdminQry materialTreeAdminQry) {
        PageHelper.startPage(materialTreeAdminQry.getPage(),materialTreeAdminQry.getSize());
        List<MaterialDO> materialDOList = materialQryExe.listByCondition(materialTreeAdminQry.getAtLastTrademark(),materialTreeAdminQry.getCategoryId(),
                materialTreeAdminQry.getParentId(),materialTreeAdminQry.getOpenFlag(),materialTreeAdminQry.getContent());
        PageInfo pageInfo = new PageInfo(materialDOList);
        List<MaterialSimpleDTO> simpleDTOList = MaterialConvertor.toMaterialSimpleDTO(materialDOList,materialQryExe);
        pageInfo.setList(simpleDTOList);

        return pageInfo;
    }

    @Override
    public List<MaterialSimpleDTO> listWithoutTree(MaterialTreeAdminQry materialTreeAdminQry) {
        List<MaterialDO> materialDOList = materialQryExe.listByCondition(materialTreeAdminQry.getAtLastTrademark(),materialTreeAdminQry.getCategoryId(),
                materialTreeAdminQry.getParentId(),materialTreeAdminQry.getOpenFlag(),materialTreeAdminQry.getContent());
        return  MaterialConvertor.toMaterialSimpleDTO(materialDOList,materialQryExe);
    }

    @Override
    public void test() {
        messageService.test();
    }

    /**
     * 根据货品编码查询材质
     * @param itemCode
     * @return
     */
    @Override
    public MaterialDO getByItemCode(String itemCode) {
        return materialQryExe.getByItemCode(itemCode);
    }


    /**
     * 设置操作人信息
     * @param materialDO
     * @param currentAdmin
     */
    private void setAdminMsg(MaterialDO materialDO, AdminResponse currentAdmin) {
        if(materialDO.getId() ==null){
            materialDO.setCreateTime(new Date());
            materialDO.setCreateUserId(currentAdmin.getId());
            materialDO.setCreateUserName(currentAdmin.getUserName());
        }
        materialDO.setUpdateTime(new Date());
        materialDO.setUpdateUserId(currentAdmin.getId());
        materialDO.setUpdateUserName(currentAdmin.getUserName());
    }
}
