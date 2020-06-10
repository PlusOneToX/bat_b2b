package com.bat.flexible.manager.third;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdListDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.model.ModelMaterialRelevanceServiceI;
import com.bat.flexible.api.third.ThirdSkuNoNameInfoServiceI;
import com.bat.flexible.api.third.ThirdSkuRelevanceServiceI;
import com.bat.flexible.api.third.dto.page.ThirdSkuRelevancePageQry;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.excel.ThirdSkuExcelConstant;
import com.bat.flexible.manager.material.executor.MaterialQryExe;
import com.bat.flexible.manager.product.executor.ProductCategoryQryExe;
import com.bat.flexible.manager.third.convertor.ThirdSkuRelevanceConvertor;
import com.bat.flexible.manager.third.validtor.ThirdSkuRelevanceValidtor;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import com.bat.flexible.dao.third.co.ThirdSkuRelevancePageCO;
import com.bat.flexible.dao.third.dataobject.ThirdSkuNoNameInfoDO;
import com.bat.flexible.dao.third.dataobject.ThirdSkuRelevanceDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.third.ThirdSkuRelevanceErrorCode;
import com.bat.flexible.manager.model.executor.ModelQryExe;
import com.bat.flexible.manager.third.executor.ThirdSkuRelevanceCmdExe;
import com.bat.flexible.manager.third.executor.ThirdSkuRelevanceQryExe;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 第三方sku关联业务层
 */
@Service
public class ThirdSkuRelevanceServiceImpl implements ThirdSkuRelevanceServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdSkuRelevanceServiceImpl.class);

    @Autowired
    private ThirdSkuRelevanceQryExe thirdSkuRelevanceQryExe;


    @Autowired
    private ThirdSkuRelevanceCmdExe thirdSkuRelevanceCmdExe;

    @Autowired
    private ThirdSkuNoNameInfoServiceI thirdSkuNoNameInfoServiceI;

    @Autowired
    private MaterialQryExe materialQryExe;

    @Autowired
    private ModelQryExe modelQryExe;

    @Autowired
    private ModelMaterialRelevanceServiceI modelMaterialRelevanceServiceI;

    @Autowired
    private ProductCategoryQryExe productCategoryQryExe;

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @Value("${suning.distributor.id}")
    private Integer suNingDistributorId;

    @Override
    public PageInfo<ThirdSkuRelevancePageCO> page(ThirdSkuRelevancePageQry thirdSkuRelevancePageQry) {
        if(thirdSkuRelevancePageQry.getDistributorId()==null){
            return new PageInfo<>(new ArrayList<>());
        }
        PageHelper.startPage(thirdSkuRelevancePageQry.getPage(),thirdSkuRelevancePageQry.getSize());
        return thirdSkuRelevanceQryExe.listCOByDistributorIdAndOpenFlag(thirdSkuRelevancePageQry.getDistributorId(),thirdSkuRelevancePageQry.getOpenFlag());
    }

    @Override
    @Transactional
    public Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin) {
        ThirdSkuRelevanceDO relevanceDO = thirdSkuRelevanceQryExe.getById(flexibleUpdateStatusDTO.getId());
        if(relevanceDO.getOpenFlag()-flexibleUpdateStatusDTO.getOpenFlag()==0){
            throw  new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        relevanceDO.setOpenFlag(flexibleUpdateStatusDTO.getOpenFlag());
        setAdminMsg(relevanceDO,currentAdmin);
        thirdSkuRelevanceCmdExe.update(relevanceDO);
        return Response.buildSuccess();
    }

    /**
     * 批量删除
     * @param flexibleIdListDTO
     * @param currentAdmin
     * @return
     */
    @Override
    @Transactional
    public Response deteleBatch(FlexibleIdListDTO flexibleIdListDTO, AdminResponse currentAdmin) {
        flexibleIdListDTO.getIdList().stream().forEach(id -> {
            ThirdSkuRelevanceDO relevanceDO = thirdSkuRelevanceQryExe.getById(id);
            thirdSkuRelevanceCmdExe.deleteById(id);
        });
        return Response.buildSuccess();
    }

    @Override
    public HSSFWorkbook export(ThirdSkuRelevancePageQry thirdSkuRelevancePageQry) {
        if(thirdSkuRelevancePageQry.getDistributorId()==null){
            return ThirdSkuRelevanceConvertor.export(new ArrayList<>());
        }
        PageInfo<ThirdSkuRelevancePageCO> list = thirdSkuRelevanceQryExe.listCOByDistributorIdAndOpenFlag(thirdSkuRelevancePageQry.getDistributorId(),thirdSkuRelevancePageQry.getOpenFlag());
        return ThirdSkuRelevanceConvertor.export(list.getList());
    }

    private void setAdminMsg(ThirdSkuRelevanceDO relevanceDO, AdminResponse currentAdmin) {
        if(relevanceDO.getId() ==null){
            relevanceDO.setCreateTime(new Date());
            relevanceDO.setCreateUserId(currentAdmin.getId());
            relevanceDO.setCreateUserName(currentAdmin.getUserName());
        }
        relevanceDO.setUpdateTime(new Date());
        relevanceDO.setUpdateUserId(currentAdmin.getId());
        relevanceDO.setUpdateUserName(currentAdmin.getUserName());
    }

    @Override
    @Transactional
    public Map<String, Object> deleteByDistributorId(Integer distributorId, AdminResponse currentAdmin) {
        if(distributorId ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL);
        }
        List<ThirdSkuRelevanceDO> relaList = thirdSkuRelevanceQryExe.listByCondition(distributorId,null);
        if(relaList !=null){
            Map<String, Object> map = new HashMap<>();
            map.put("thirdSkuRelaList",relaList);
            List<ThirdSkuNoNameInfoDO> thirdSkuNoNameInfoList = thirdSkuNoNameInfoServiceI.listByDistributorId(distributorId);
            map.put("thirdSkuNoNameInfoList",thirdSkuNoNameInfoList);
            LOGGER.error("重新导入第三方sku关联关系、删除原来的sku关联关系：操作人信息："+currentAdmin.getUserName());
            thirdSkuRelevanceCmdExe.deleteByDistributorId(distributorId);
            thirdSkuNoNameInfoServiceI.deleteByDistributorId(distributorId);
            return map;
        }
        return null;
    }

    @Override
    @Transactional
    public Response importSku(InputStream inputStream, AdminResponse currentAdmin, Integer distributorId) {
        if (distributorId == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL);
        }
        com.bat.dubboapi.distributor.common.Response<DistributorRpcDTO> distributorRpcDTOResponse = distributorServiceRpc.distributorById(distributorId);
        if (!distributorRpcDTOResponse.isSuccess() || distributorRpcDTOResponse.getData() == null) {
            throw new FlexibleCustomException(ThirdSkuRelevanceErrorCode.DISTRIBUTOR_NOT_EXIST);
        }
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (Exception ex) {
            LOGGER.error(MessageUtils.get(FlexibleCommonErrorCode.COMMON_EXCEL_PARSING_ERROR));
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_EXCEL_PARSING_ERROR);
        }

        List<ThirdSkuRelevanceDO> list = new ArrayList<>();
        List<MaterialDO> materialList = materialQryExe.listByCondition(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES,null,null,null,null);
        Map<String,MaterialDO> materialMap = materialList.stream().collect(Collectors.toMap(MaterialDO::getMaterialNo, material -> material ));
        List<ModelDO> modelList = modelQryExe.listByCondition(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES,null);
        Map<String, ModelDO> modelMap = modelList.stream().collect(Collectors.toMap(ModelDO::getModelNo, model -> model));
        List<ModelMaterialRelevanceDO> relevanceDOList =modelMaterialRelevanceServiceI.listByCondition(null,null,FlexibleCommonConstant.COMMON_OPEN_FLAG_YES,null);
        if(relevanceDOList ==null || relevanceDOList.size()==0){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DATA_NULL);
        }
        Map<String, ModelMaterialRelevanceDO> relevanceDOMap = new HashMap<>();
        relevanceDOList.stream().forEach(goodsCustomInfo -> {
            String key = goodsCustomInfo.getMaterialId()+"_"+goodsCustomInfo.getModelId();
            relevanceDOMap.put(key, goodsCustomInfo);
        });

        //物料
        Map<String, String> materialCategoryMap = new HashMap<>();
        //品牌编号名称
        Map<String, String> brandMap = new HashMap<>();
        //机型
        Map<String, String> thirdModelMap = new HashMap<>();
        //壳材质
        Map<String, String> thirdMaterialMap = new HashMap<>();
        //颜色
        Map<String, String> colorMap = new HashMap<>();

        boolean skuCheck=true;
        if(distributorId==suNingDistributorId.intValue()){
            skuCheck=false;
        }
        ThirdSkuRelevanceValidtor.checkImportSku(currentAdmin, distributorId, workbook, list, materialMap, modelMap, relevanceDOMap,  materialCategoryMap, brandMap, thirdModelMap, thirdMaterialMap, colorMap,skuCheck);
        if(list ==null || list.size()==0){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_EXCEL_IMPORT_DATA_NULL);
        }
        list.stream().forEach(thirdSkuRelevanceDO -> {
            setAdminMsg(thirdSkuRelevanceDO,currentAdmin);
            thirdSkuRelevanceDO.setOpenFlag(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
            thirdSkuRelevanceCmdExe.create(thirdSkuRelevanceDO);
        });
        thirdSkuNoNameInfoServiceI.add(materialCategoryMap,brandMap,thirdModelMap,thirdMaterialMap,colorMap,currentAdmin,distributorId);
        return Response.buildSuccess();
    }





    @Override
    public void insertAgain(Map<String, Object> map) {
        if(map ==null){
            return;
        }
        List<ThirdSkuRelevanceDO> relaList = (List<ThirdSkuRelevanceDO>) map.get("thirdSkuRelaList");
        if(relaList !=null && relaList.size()>0){
            //去掉id
            relaList.stream().forEach(thirdSkuRela -> {
                thirdSkuRela.setId(null);
                thirdSkuRelevanceCmdExe.create(thirdSkuRela);
            });

        }
        List<ThirdSkuNoNameInfoDO> thirdSkuNoNameInfoList = (List<ThirdSkuNoNameInfoDO>) map.get("thirdSkuNoNameInfoList");
        if(thirdSkuNoNameInfoList !=null && thirdSkuNoNameInfoList.size()>0){
            thirdSkuNoNameInfoList.stream().forEach(thirdSkuNoNameInfo -> {
                thirdSkuNoNameInfo.setId(null);
                thirdSkuNoNameInfoServiceI.create(thirdSkuNoNameInfo);
            });

        }
    }

    @Override
    public HSSFWorkbook downLoadExcel() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<ModelMaterialRelevanceDO> list = modelMaterialRelevanceServiceI.listByCondition(null,null,FlexibleCommonConstant.COMMON_OPEN_FLAG_YES,null);
        if(list ==null || list.size() ==0){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DATA_NULL);
        }
        //查询所有可用的材质列表
        List<MaterialDO> materialDOList = materialQryExe.listAll();
        //查询所有可用型号
        List<ModelDO> modelDOList = modelQryExe.listByCondition(null,null);
        List<ProductCategoryDO> productCategoryDOList = productCategoryQryExe.listByCondition(null,FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
        Map<Integer, MaterialDO> materialDOMap = materialDOList.stream().collect(Collectors.toMap(MaterialDO::getId, materialDO -> materialDO));
        Map<Integer, ModelDO> modelDOMap = modelDOList.stream().collect(Collectors.toMap(ModelDO::getId, modelDO -> modelDO));
        Map<Integer, ProductCategoryDO> productCategoryDOMap = productCategoryDOList.stream().collect(Collectors.toMap(ProductCategoryDO::getId, productCategoryDO -> productCategoryDO));

        HSSFSheet hssfSheet = workbook.createSheet("bat材质型号关联关系汇总");
        HSSFFont hssfFont = workbook.createFont();
        hssfFont.setColor(IndexedColors.BLACK.getIndex());
        //设置字体
        hssfFont.setFontName("宋体");
        //设置字号
        hssfFont.setFontHeightInPoints((short)10);
        //设置单元格内容水平垂直居中
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //4.设置单元格背景色
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充单元格
        titleStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);//设置单元格背景色
        titleStyle.setFont(hssfFont);


        HSSFCellStyle thirdStyle = workbook.createCellStyle();
        thirdStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        thirdStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //4.设置单元格背景色
        thirdStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充单元格
        thirdStyle.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);//设置单元格背景色
        thirdStyle.setFont(hssfFont);

        HSSFRow row1 = hssfSheet.createRow(0);
        row1.setHeightInPoints(25);
        HSSFCell hssfCell = row1.createCell(0);
        hssfCell.setCellValue(ThirdSkuExcelConstant.batMsg);
        hssfCell.setCellStyle(titleStyle);
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0,7 );
        hssfSheet.addMergedRegion(cellRangeAddress);
        hssfCell = row1.createCell(8);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdMaterialCategory);
        hssfCell.setCellStyle(thirdStyle);
        cellRangeAddress = new CellRangeAddress(0, 0, 8,9 );
        hssfSheet.addMergedRegion(cellRangeAddress);
        hssfCell = row1.createCell(10);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdBrand);
        hssfCell.setCellStyle(thirdStyle);
        cellRangeAddress = new CellRangeAddress(0, 0, 10,11 );
        hssfSheet.addMergedRegion(cellRangeAddress);
        hssfCell = row1.createCell(12);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdModel);
        hssfCell.setCellStyle(thirdStyle);
        cellRangeAddress = new CellRangeAddress(0, 0, 12,14 );
        hssfSheet.addMergedRegion(cellRangeAddress);
        hssfCell = row1.createCell(15);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdMaterial);
        hssfCell.setCellStyle(thirdStyle);
        cellRangeAddress = new CellRangeAddress(0, 0, 15,16 );
        hssfSheet.addMergedRegion(cellRangeAddress);
        hssfCell = row1.createCell(17);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdColor);
        hssfCell.setCellStyle(thirdStyle);
        cellRangeAddress = new CellRangeAddress(0, 0, 17,18 );
        hssfSheet.addMergedRegion(cellRangeAddress);
        hssfCell = row1.createCell(19);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdSkuMsg);
        hssfCell.setCellStyle(thirdStyle);
       /* cellRangeAddress = new CellRangeAddress(0, 0, 19,19 );
        hssfSheet.addMergedRegion(cellRangeAddress);*/
        HSSFRow row2 = hssfSheet.createRow(1);
        //自适应列宽度
        hssfSheet.autoSizeColumn(1,true);
        row2.setHeightInPoints(25);
        hssfCell = row2.createCell(0);
        hssfCell.setCellValue(ThirdSkuExcelConstant.SerialNoName);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row2.createCell(1);
        hssfCell.setCellValue(ThirdSkuExcelConstant.MaterialCategoryName);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row2.createCell(2);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ParentMaterialName);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row2.createCell(3);
        hssfCell.setCellValue(ThirdSkuExcelConstant.MaterialName);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row2.createCell(4);
        hssfCell.setCellValue(ThirdSkuExcelConstant.MaterialNo);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row2.createCell(5);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ParentModelName);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row2.createCell(6);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ModelName);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row2.createCell(7);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ModelNo);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row2.createCell(8);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdMaterialCategoryName);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(9);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdMaterialCategoryNo);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(10);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdBrandName);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(11);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdBrandNo);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(12);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdSerial);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(13);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdModelName);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(14);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdModelNo);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(15);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdMaterialName);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(16);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdMaterialNo);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(17);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdColorName);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(18);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdColorNo);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(19);
        hssfCell.setCellValue(ThirdSkuExcelConstant.ThirdSku);
        hssfCell.setCellStyle(thirdStyle);
        hssfCell = row2.createCell(20);
        hssfCell.setCellValue(ThirdSkuExcelConstant.thirdRemark);
        hssfCell.setCellStyle(thirdStyle);
        for(int x=0;x<list.size();x++){
            HSSFRow row = hssfSheet.createRow(x+2);
            for(int i=0;i<4;i++){
                hssfCell = row.createCell(i);
                hssfCell.setCellStyle(titleStyle);
            }
            row.createCell(0).setCellValue(x+1);
            ModelMaterialRelevanceDO relevanceDO = list.get(x);
            MaterialDO materialDO = materialDOMap.get(relevanceDO.getMaterialId());
            MaterialDO parentMaterialDO = materialDOMap.get(materialDO.getParentId());
            if(parentMaterialDO ==null){
                //只有一级
                parentMaterialDO = materialDO;
            }
            ModelDO modelDO = modelDOMap.get(relevanceDO.getModelId());
            ModelDO parentModelDO = modelDOMap.get(modelDO.getParentId());
            if(parentModelDO ==null){
                //只有一级
                parentModelDO = modelDO;
            }
            row.createCell(1).setCellValue(productCategoryDOMap.get(materialDO.getCategoryId()).getName());
            row.createCell(2).setCellValue(parentMaterialDO.getName());
            row.createCell(3).setCellValue(materialDO.getName());
            row.createCell(4).setCellValue(materialDO.getMaterialNo());
            row.createCell(5).setCellValue(parentModelDO.getName());
            row.createCell(6).setCellValue(modelDO.getName());
            row.createCell(7).setCellValue(modelDO.getModelNo());

        }
        workbook.setSheetName(0,"材质型号三方校对");

        return workbook;
    }

    /**
     * 根据分销商id和第三方sku编码查询
     * @param distributorId
     * @param sku
     * @return
     */
    @Override
    public ThirdSkuRelevanceDO getByDistributorIdAndThirdSkuNo(Integer distributorId, String sku) {
        return thirdSkuRelevanceQryExe.getByDistributorIdAndThirdSkuNo(distributorId,sku);
    }
}
