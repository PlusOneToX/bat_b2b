package com.bat.flexible.manager.common.utils.code;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BarCodeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BarCodeUtils.class);

    public static void addCode(PdfPage page, String fileName, String mine, String content, float left, float bottom, float width, float height) {
        try {
            LOGGER.info("addCode生成条形码{},fileName{},mine{},content{}",page,fileName,mine,content);
            createEAN13(fileName, mine, content);
            ImageData png = ImageDataFactory.create(fileName);
            Image picture = new Image(png);
            picture.setWidth(width);
            picture.setHeight(height);
            Rectangle rectangle = new Rectangle(0, 0);
            new Canvas(new PdfCanvas(page), rectangle, true).add(picture.setFixedPosition(1, left, bottom));
        } catch (Exception e) {
            LOGGER.error("条形码生成失败！{}",e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("条形码生成失败");
        }
    }

    public static void createEAN13(String filePath, String mime, String content) {

        try {
            FileOutputStream out = new FileOutputStream(filePath);
            EAN13Bean bean = new EAN13Bean();
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, mime, 250, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            bean.generateBarcode(canvas, content);
            canvas.finish();
            LOGGER.info("文件生成操作完成。。。");
        } catch (FileNotFoundException e) {
            LOGGER.error("找不到文件异常{}",e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("生成条形码IO异常{}",e.getMessage());
        }

    }
}
