package com.bat.flexible.manager.third.validtor;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.util.excel.ExcelUtil;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import com.bat.flexible.dao.third.dataobject.ThirdSkuRelevanceDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.excel.ExcelCommonErrorCode;
import com.bat.flexible.manager.error.model.ModelMaterialRelevanceErrorCode;
import com.bat.flexible.manager.error.third.ThirdSkuRelevanceErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdSkuRelevanceValidtor {

    public static void checkImportSku(AdminResponse currentAdmin, Integer distributorId, Workbook workbook, List<ThirdSkuRelevanceDO> list, Map<String, MaterialDO> materialMap, Map<String, ModelDO> modelMap, Map<String, ModelMaterialRelevanceDO> relevanceDOMap, Map<String, String> materialCategoryMap, Map<String, String> brandMap, Map<String, String> thirdModelMap, Map<String, String> thirdMaterialMap, Map<String, String> colorMap, boolean checkSku) {
        //三方skuNo集合
        Map<String, Boolean> skuMap = new HashMap<>();
        //B2B材质和型号关联关系、判断是否重复添加
        Map<String, Boolean> customerMap = new HashMap<>();
        for (int numSheet = 0; numSheet < 1; numSheet++) {
            Sheet hssfSheet = workbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            String sheetName = hssfSheet.getSheetName();
            int lastRowNum = hssfSheet.getLastRowNum() - hssfSheet.getFirstRowNum() + 1;// 获取所有的行项目

            for (int rowNum = 2; rowNum < lastRowNum; rowNum++) {
                Row xssfRow = hssfSheet.getRow(rowNum);
                Cell rqMaterialCell = xssfRow.getCell(4);
                String materialNo = ExcelUtil.getValue(rqMaterialCell);
                String errorMsg = sheetName + MessageUtils.get(ExcelCommonErrorCode.EXCEL_ERROR_NAME_TABLE_SEQUENCE) + (rowNum + 1) +MessageUtils.get(ExcelCommonErrorCode.EXCEL_ERROR_NAME_ROWS);
                if (StringUtils.isBlank(materialNo)) {
                    throw new FlexibleCustomException(errorMsg+MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_RQ_MATERIAL_NO_NULL));
                }
                //校验B2B材质
                MaterialDO materialDO =materialMap.get(materialNo);
                if(materialDO ==null){
                    throw new FlexibleCustomException(errorMsg+MessageUtils.get(FlexibleCommonErrorCode.COMMON_MATERIAL_NO_ERROR)+"【"+materialNo+"】");
                }
                if(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(materialDO.getOpenFlag())){
                    throw new FlexibleCustomException(errorMsg+MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)+MessageUtils.get(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE)+"【"+materialNo+"】");
                }
                Cell rqModelCell = xssfRow.getCell(7);
                String modelNo = ExcelUtil.getValue(rqModelCell);
                if (StringUtils.isBlank(modelNo)) {
                    throw new FlexibleCustomException( errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_RQ_MODEL_NO_NULL));
                }
                //校验B2B型号
                ModelDO modelDO = modelMap.get(modelNo);
                if(modelDO ==null ){
                    throw new FlexibleCustomException(errorMsg+ MessageUtils.get(FlexibleCommonErrorCode.COMMON_MODEL_NO_ERROR));
                }
                if(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(modelDO.getOpenFlag())){
                    String msg = errorMsg+ MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MODEL)+
                            MessageUtils.get(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE);
                    throw new FlexibleCustomException(msg);
                }
                //校验B2B材质和型号关联关系
                String key = materialDO.getId()+"_"+modelDO.getId();
                ModelMaterialRelevanceDO relevanceDO = relevanceDOMap.get(key);
                if(relevanceDO ==null){
                    throw new FlexibleCustomException(errorMsg+ MessageUtils.get(ModelMaterialRelevanceErrorCode.M_MODEL_MATERIAL_NO_CORRELATION));
                }
                if(customerMap.containsKey(key)){
                    throw new FlexibleCustomException(errorMsg+MessageUtils.get(ThirdSkuRelevanceErrorCode.MODEL_MATERIAL_ADD_REPEAT_ERROR));
                }
                //设置重复标记、避免重复添加
                customerMap.put(key,true);
                Cell materialCategoryNameCell = xssfRow.getCell(8);
                String materialCategoryName = ExcelUtil.getValue(materialCategoryNameCell);
                if (StringUtils.isBlank(materialCategoryName)) {
                    throw new FlexibleCustomException(errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_MATERIAL_CATEGORY_NAME_NULL));
                }
                Cell materialCategoryCell = xssfRow.getCell(9);
                String materialCategoryNo = ExcelUtil.getValue(materialCategoryCell);
                if (StringUtils.isBlank(materialCategoryNo)) {
                    throw new FlexibleCustomException(errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_MATERIAL_CATEGORY_NO_NULL));
                }
                Cell brandNameCell = xssfRow.getCell(10);
                String brandName = ExcelUtil.getValue(brandNameCell);
                if (StringUtils.isBlank(brandName)) {
                    throw new FlexibleCustomException( errorMsg+MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_BRAND_NAME_NULL));
                }

                Cell brandCell = xssfRow.getCell(11);
                String brandNo = ExcelUtil.getValue(brandCell);
                if (StringUtils.isBlank(brandNo)) {
                    throw new FlexibleCustomException( errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_BRAND_NO_NULL));
                }
                Cell thirdSeriesCell = xssfRow.getCell(12);
                String thirdSeries = ExcelUtil.getValue(thirdSeriesCell);
                if (StringUtils.isBlank(thirdSeries)) {
                    throw new FlexibleCustomException( errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_BRAND_SERIES_NULL));
                }
                Cell thirdModelNameCell = xssfRow.getCell(13);
                String thirdModelName = ExcelUtil.getValue(thirdModelNameCell);
                if (StringUtils.isBlank(thirdModelName)) {
                    throw new FlexibleCustomException( errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_MODEL_NAME_NULL));
                }
                Cell thirdModelCell = xssfRow.getCell(14);
                String thirdModelNo = ExcelUtil.getValue(thirdModelCell);
                if (StringUtils.isBlank(thirdModelNo)) {
                    throw new FlexibleCustomException(errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_MODEL_NO_NULL));
                }
                Cell thirdMaterialNameCell = xssfRow.getCell(15);
                String thirdMaterialName = ExcelUtil.getValue(thirdMaterialNameCell);
                if (StringUtils.isBlank(thirdMaterialName)) {
                    throw new FlexibleCustomException(errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_MATERIAL_NAME_NULL));
                }
                Cell thirdMaterialCell = xssfRow.getCell(16);
                String thirdMaterialNo = ExcelUtil.getValue(thirdMaterialCell);
                if (StringUtils.isBlank(thirdMaterialNo)) {
                    throw new FlexibleCustomException( errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_MATERIAL_NO_NULL));
                }
                Cell thirdColorNameCell = xssfRow.getCell(17);
                String colorName = ExcelUtil.getValue(thirdColorNameCell);
                if (StringUtils.isBlank(colorName)) {
                    throw new FlexibleCustomException( errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_COLOUR_NAME_NULL));
                }
                Cell thirdColorCell = xssfRow.getCell(18);
                String colorNo = ExcelUtil.getValue(thirdColorCell);
                if (StringUtils.isBlank(colorNo)) {
                    throw new FlexibleCustomException( errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_COLOUR_NO_NULL));
                }
                Cell skuCell = xssfRow.getCell(19);
                String skuNo = ExcelUtil.getValue(skuCell);
                if (StringUtils.isBlank(skuNo)) {
                    throw new FlexibleCustomException(errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_SKU_NO_NULL));
                }
                String skuNoAppend = materialCategoryNo+brandNo+thirdModelNo+thirdMaterialNo+colorNo;
                if (!skuNo.equals(skuNoAppend)) {
                    if(checkSku) {
                        //sku组成有误
                        throw new FlexibleCustomException(errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_SKU_NO_COMPOSITION_ERROR)+skuNo+"->"+skuNoAppend);
                    }
                }
                if(skuMap.containsKey(skuNo)){
                    //重复sku
                    throw new FlexibleCustomException(errorMsg+ MessageUtils.get(ThirdSkuRelevanceErrorCode.EXCEL_IMPORT_THIRD_SKU_NO_REPEAT_ERROR)+skuNo);
                }
                skuMap.put(skuNo,true);
                ThirdSkuRelevanceDO thirdSkuRelevanceDO = getThirdSkuRelevanceDO(currentAdmin, distributorId, materialDO, modelDO, materialCategoryNo, brandNo, thirdModelNo, thirdMaterialNo, colorNo, skuNo);
                list.add(thirdSkuRelevanceDO);
                if(!materialCategoryMap.containsKey(materialCategoryNo)){
                    materialCategoryMap.put(materialCategoryNo,materialCategoryName);
                }
                //品牌
                if(!brandMap.containsKey(brandNo)){
                    brandMap.put(brandNo,brandName);
                }
                //机型
                if(!thirdModelMap.containsKey(thirdModelNo)){
                    thirdModelMap.put(thirdModelNo,thirdModelName+"_"+thirdSeries);
                }
                //壳材质
                if(!thirdMaterialMap.containsKey(thirdMaterialNo)){
                    thirdMaterialMap.put(thirdMaterialNo,thirdMaterialName);
                }
                //颜色
                if(!colorMap.containsKey(colorNo)){
                    colorMap.put(colorNo,colorName);
                }
            }
        }
    }
    private static ThirdSkuRelevanceDO getThirdSkuRelevanceDO(AdminResponse currentAdmin, Integer distributorId, MaterialDO materialDO, ModelDO modelDO, String materialCategoryNo, String brandNo, String thirdModelNo, String thirdMaterialNo, String colorNo, String skuNo) {
        ThirdSkuRelevanceDO thirdSkuRelevanceDO  = new ThirdSkuRelevanceDO();
        thirdSkuRelevanceDO.setDistributorId(distributorId);
        thirdSkuRelevanceDO.setMaterialId(materialDO.getId());
        thirdSkuRelevanceDO.setModelId(modelDO.getId());
        thirdSkuRelevanceDO.setThirdMaterialCategoryNo(materialCategoryNo);
        thirdSkuRelevanceDO.setThirdBrandNo(brandNo);
        thirdSkuRelevanceDO.setThirdModelNo(thirdModelNo);
        thirdSkuRelevanceDO.setThirdMaterialNo(thirdMaterialNo);
        thirdSkuRelevanceDO.setThirdColourNo(colorNo);
        thirdSkuRelevanceDO.setThirdSkuNo(skuNo);
        thirdSkuRelevanceDO.setOpenFlag(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
        thirdSkuRelevanceDO.setCreateTime(new Date());
        thirdSkuRelevanceDO.setCreateUserId(currentAdmin.getId());
        thirdSkuRelevanceDO.setCreateUserName(currentAdmin.getUserName());
        thirdSkuRelevanceDO.setUpdateTime(new Date());
        thirdSkuRelevanceDO.setUpdateUserId(currentAdmin.getId());
        thirdSkuRelevanceDO.setCreateUserName(currentAdmin.getUserName());
        return thirdSkuRelevanceDO;
    }
}
