package com.bat.promotion.service.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/2 18:46
 */
public class CommonUtils {

    private static final char[] codeSequenceChar = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
        'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static String GOODSPIC = "商品图";
    public static String GOODSPACKAGEPIC = "包装图";
    public static String GOODSPICEN = "GRAPHIC";
    public static String GOODSPACKAGEPICEN = "PACKING";
    public static String GOODSName = "货品名称";
    public static String GOODSNameEn = "DESCRIPTION";

    public static String getCronByDate(Date date) {
        StringBuilder builder = new StringBuilder();
        String[] str1 = new SimpleDateFormat("HH:mm:ss").format(date).toString().split(":");
        builder.append(String.valueOf(str1[2]));
        builder.append(" ");
        builder.append(String.valueOf(str1[1]));
        builder.append(" ");
        builder.append(String.valueOf(str1[0]));
        builder.append(" ");
        String[] str2 = new SimpleDateFormat("yyyy-MM-dd").format(date).toString().split("-");
        builder.append(String.valueOf(str2[2]));
        builder.append(" ");
        builder.append(String.valueOf(str2[1]));
        builder.append(" ");
        builder.append("?");
        builder.append(" ");
        builder.append(String.valueOf(str2[0]));
        return builder.toString();
    }

    /**
     * 获取当天最后一刻
     * 
     * @return
     * @throws ParseException
     */
    public static long getTodayEndTime() throws ParseException {
        String endTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd 23:59:59");
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return form.parse(endTime).getTime();
    }

    /**
     * 获取未来24小时时间
     * 
     * @return
     */
    public static Date get24Time() {
        long time = System.currentTimeMillis() + 24 * 60 * 60 * 1000L;
        return new Date(time);
    }

    public static String getRandom() {
        StringBuffer randomCodeRes = new StringBuffer();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 随机产生，验证码由几个数字、几个字母组成
        int charNum = 12;
        for (int i = 0; i < charNum; i++) {
            // 得到随机产生的验证码字母。
            String strRand = String.valueOf(codeSequenceChar[random.nextInt(codeSequenceChar.length)]);
            // 将产生的六个随机数组合在一起。
            randomCodeRes.append(strRand);
        }
        return randomCodeRes.toString();
    }

    public static String getRebateVoucherNo(Date date) {
        String dataStr = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        return getRebateVoucherNo(dataStr);
    }

    public static String getRebateVoucherNo(String date) {
        String format = String.format("%04d", new Random().nextInt(9999));
        return date + format;
    }

    public static String getRebateVoucherNo(String date, Integer num) {
        if (num > 10000) {
            num -= 10000;
        }
        String format = String.format("%04d", num);
        return date + format;
    }

    /**
     * 导出excel表格设计和组装
     * 
     * @param titles
     * @param rows
     * @param maps
     * @param uploadPath
     * @return
     */
    public static HSSFWorkbook excelOut(List<String> titles, int rows, List<Map<String, Object>> maps,
        String uploadPath) {

        FileOutputStream fileOut = null;
        BufferedImage bufferImg = null;
        HSSFWorkbook wb = null;
        try {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            // 创建工作sheet
            wb = new HSSFWorkbook();
            String name = "活动";
            HSSFSheet sheet = wb.createSheet(name);
            HSSFFont font = wb.createFont();
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setFontName("宋体"); // 设置字体
            font.setFontHeightInPoints((short)10); // 设置字号
            font.setBold(true); // 设置字体样式 正常显示
            // 设置单元格内容水平垂直居中
            HSSFCellStyle titleStyle = wb.createCellStyle();
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            // 4.设置单元格背景色
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);// 填充单元格
            titleStyle.setFillForegroundColor((short)17);// 设置单元格背景色
            titleStyle.setFont(font);
            // 指定当单元格内容显示不下时自动换行
            // 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            // HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            int num = 0;
            HSSFRow row0 = sheet.createRow(num++);
            row0.setHeightInPoints(25);
            if (titles.size() == 0) {
                return null;
            }
            HSSFCell cell = null;
            // 第一行、标题行列
            for (int i = 0; i < titles.size(); i++) {
                cell = row0.createCell(i);// 第一个单元格
                cell.setCellValue(titles.get(i));// 设定值
                cell.setCellStyle(titleStyle);
                sheet.setColumnWidth(i, 9000);
            }

            HSSFRow row = null;
            HSSFCell cellRow = null;
            HSSFClientAnchor anchor = null;
            HSSFCellStyle style = wb.createCellStyle();
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setWrapText(true);

            for (int i = num; i <= rows; i++) {
                int cellColumn = 0;
                // 创建行
                row = sheet.createRow(i);
                // 设置默认行高
                row.setHeightInPoints(25);
                // 行数据处理
                Map<String, Object> stringObjectMap = maps.get(i - 1);
                for (String value : titles) {
                    // 行单元格
                    cellRow = row.createCell(cellColumn);
                    cellRow.setCellStyle(style);
                    // 如果行数据中有图片时候的处理
                    if (value.equals(GOODSPIC) || value.equals(GOODSPACKAGEPIC) || value.equals(GOODSPICEN)
                        || value.equals(GOODSPACKAGEPICEN)) {
                        Object o = stringObjectMap.get(value);
                        File file = null;
                        if (o instanceof File) {
                            file = (File)stringObjectMap.get(value);
                        }
                        if (file == null) {
                            cellRow.setCellValue("");
                        } else {
                            row.setHeightInPoints(150);
                            cellRow = row.createCell(cellColumn);
                            cellRow.setCellStyle(style);
                            sheet.setColumnWidth(cellColumn, 6000);
                            bufferImg = ImageIO.read(file);
                            byteArrayOut.reset();
                            int index = file.getPath().lastIndexOf(".");
                            String suffix = file.getPath().substring(index + 1);
                            ImageIO.write(bufferImg, suffix, byteArrayOut);
                            anchor =
                                new HSSFClientAnchor(60, 30, 1000, 200, (short)cellColumn, i, (short)cellColumn, i);
                            anchor.setAnchorType(ClientAnchor.AnchorType.byId(3));
                            // patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(),
                            // HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                        cellColumn++;
                        continue;
                    }
                    if (value.equals(GOODSName) || value.equals(GOODSNameEn)) {
                        sheet.setColumnWidth(cellColumn, 10000);
                    }
                    String cellValue = "";
                    if (stringObjectMap.get(value) != null) {
                        cellValue = stringObjectMap.get(value).toString();
                    }
                    if (cellValue.equals("")) {
                        cellValue = "";
                    }
                    cellRow.setCellValue(cellValue);
                    cellColumn++;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return wb;
    }

}
