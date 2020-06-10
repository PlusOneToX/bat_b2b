package com.bat.flexible.manager.common.utils.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class POIExcelUtils {

    /**
     *
     * @param sheet
     * @param row
     * @param workbook
     * @param index list索引

     */
    public static void setPictureUrl(HSSFSheet sheet, HSSFRow row, Workbook workbook,Integer index,String fielUrl,
                                     Integer columnIndex
                                     ) throws IOException {
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        URL url = new URL(fielUrl);
        URLConnection urlConnection = url.openConnection();
        
        InputStream inputStream = urlConnection.getInputStream();
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        ImageIO.write(bufferedImage,"jpg",byteArrayOut);
        sheet.setColumnWidth(columnIndex, 28 * 256);
        row.setHeightInPoints(188);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                /*
                  该构造函数有8个参数 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离 后四个参数，前两个表示图片左上角所在的cellNum和
                  rowNum，后两个参数对应的表示图片右下角所在的cellNum和 rowNum， excel中的cellNum和rowNum的index都是从0开始的
                 */
        Integer endColumn = columnIndex+1;
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, columnIndex.shortValue(), index+1, endColumn.shortValue(), index + 1+1);;
        patriarch.createPicture(anchor,
                workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
        byteArrayOut.close();
    }

}
