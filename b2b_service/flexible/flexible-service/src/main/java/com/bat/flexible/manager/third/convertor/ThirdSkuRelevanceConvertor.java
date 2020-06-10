package com.bat.flexible.manager.third.convertor;

import com.bat.flexible.manager.common.constant.excel.ThirdSkuExcelConstant;
import com.bat.flexible.dao.third.co.ThirdSkuRelevancePageCO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class ThirdSkuRelevanceConvertor {

    public static HSSFWorkbook export(List<ThirdSkuRelevancePageCO> list){
        HSSFWorkbook workbook = new HSSFWorkbook();

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
        cellRangeAddress = new CellRangeAddress(0, 0, 19,20 );
        hssfSheet.addMergedRegion(cellRangeAddress);
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
            row.createCell(1).setCellValue(list.get(x).getCategoryName());
            row.createCell(2).setCellValue(list.get(x).getParentMaterialName());
            row.createCell(3).setCellValue(list.get(x).getMaterialName());
            row.createCell(4).setCellValue(list.get(x).getMaterialNo());
            row.createCell(5).setCellValue(list.get(x).getParentModelName());
            row.createCell(6).setCellValue(list.get(x).getModelName());
            row.createCell(7).setCellValue(list.get(x).getModelNo());
            row.createCell(8).setCellValue(list.get(x).getMaterialCategoryName());
            row.createCell(9).setCellValue(list.get(x).getMaterialCategoryNo());
            row.createCell(10).setCellValue(list.get(x).getThirdBrandName());
            row.createCell(11).setCellValue(list.get(x).getThirdBrandNo());
            row.createCell(12).setCellValue(list.get(x).getSeries());
            row.createCell(13).setCellValue(list.get(x).getThirdModelName());
            row.createCell(14).setCellValue(list.get(x).getThirdModelNo());
            row.createCell(15).setCellValue(list.get(x).getThirdMaterialName());
            row.createCell(16).setCellValue(list.get(x).getThirdMaterialNo());
            row.createCell(17).setCellValue(list.get(x).getColourName());
            row.createCell(18).setCellValue(list.get(x).getColourNo());
            row.createCell(19).setCellValue(list.get(x).getThirdSkuNo());
        }
        workbook.setSheetName(0,"材质型号三方校对");

        return workbook;
    }
}
