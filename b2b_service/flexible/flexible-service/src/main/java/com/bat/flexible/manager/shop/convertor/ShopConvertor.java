package com.bat.flexible.manager.shop.convertor;

import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.excel.ShopExcelConstant;
import com.bat.flexible.manager.common.utils.excel.POIExcelUtils;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.shop.co.ShopPageCO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShopConvertor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopConvertor.class);



    public static HSSFWorkbook toHSSFWorkbook(List<ShopPageCO> resultList) {
        try {
            // 创建Excel文件(Workbook)
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 创建工作表(Sheet)
            HSSFSheet sheet = workbook.createSheet("店铺信息");
            sheet.setDefaultColumnWidth(15);
            // 创建文件字段
            HSSFRow row0 = sheet.createRow(0);
            HSSFFont hssfFont = workbook.createFont();
            //设置单元格内容水平垂直居中
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            //4.设置单元格背景色
            titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充单元格
            titleStyle.setFillForegroundColor(HSSFColor.GREEN.index);//设置单元格背景色
            titleStyle.setFont(hssfFont);
            //设置excel第一行
            HSSFCell hssfCell = row0.createCell(0);
            hssfCell.setCellValue(ShopExcelConstant.SerialNumberZH);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row0.createCell(1);
            hssfCell.setCellValue(ShopExcelConstant.DistributorNameZH);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row0.createCell(2);
            hssfCell.setCellValue(ShopExcelConstant.DistributorCompanyNameZH);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row0.createCell(3);
            hssfCell.setCellValue(ShopExcelConstant.ShopNameZH);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row0.createCell(4);
            hssfCell.setCellValue(ShopExcelConstant.ShopCodeZH);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row0.createCell(5);
            hssfCell.setCellValue(ShopExcelConstant.RemarkZH);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row0.createCell(6);
            hssfCell.setCellValue(ShopExcelConstant.StatusZH);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row0.createCell(7);
            hssfCell.setCellValue(ShopExcelConstant.WechatURLZH);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row0.createCell(8);
            hssfCell.setCellValue(ShopExcelConstant.ShopQRCodeZH);
            hssfCell.setCellStyle(titleStyle);
            hssfCell = row0.createCell(9);
            hssfCell.setCellValue(ShopExcelConstant.ThirdShopQRCodeZH);
            hssfCell.setCellStyle(titleStyle);
            for (int i = 0; i < resultList.size(); i++) {

                ShopPageCO shop = resultList.get(i );
                HSSFRow row = sheet.createRow(i+1);
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(i+1);
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(shop.getDistributorName());
                HSSFCell cell2 = row.createCell(2);
                cell2.setCellValue(shop.getDistributorCompanyName());
                HSSFCell cell3 = row.createCell(3);
                cell3.setCellValue(shop.getShopName());
                HSSFCell cell4 = row.createCell(4);
                cell4.setCellValue(shop.getShopCode());
                HSSFCell cell5 = row.createCell(5);
                cell5.setCellValue(shop.getRemark());
                HSSFCell cell6 = row.createCell(6);
                cell6.setCellValue(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES.equals(shop.getOpenFlag())?
                        FlexibleCommonConstant.COMMON_OPEN_FLAG_YES_NAME:FlexibleCommonConstant.COMMON_OPEN_FLAG_NO_NAME);
                HSSFCell cell7 = row.createCell(7);
                cell7.setCellValue(shop.getUrl());
                HSSFCell cell8 = row.createCell(8);
                cell8.setCellValue(shop.getQrUrl());
                if(StringUtils.isNotBlank(shop.getQrUrl())){
                    try {
                        POIExcelUtils.setPictureUrl(sheet, row, workbook, i, shop.getQrUrl(), 8);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                cell = row.createCell(9);
                cell.setCellValue(shop.getThirdQrUrl());
                if(StringUtils.isNotBlank(shop.getThirdQrUrl())){
                    try {
                        POIExcelUtils.setPictureUrl(sheet, row, workbook, i, shop.getThirdQrUrl(), 9);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_EXCEL_EXPORT_ERROE);
        }

    }

}
