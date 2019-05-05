package com.bat.thirdparty.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.bat.thirdparty.oss.service.OssService;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/1/28 19:42
 */
@Component
public class ConvertUtils implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(ConvertUtils.class);

    /**
     * 注入上下文 判断环境 懒得写配置文件了
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Autowired
    private OssService ossService;

    /** user space units per inch */
    private static final float POINTS_PER_INCH = 72;

    /** user space units per millimeter */
    private static final float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;

    /**
     * png 转 pdf
     *
     * @param generateImage
     * @param manufactor
     * @param phoneW
     * @param phoneH
     * @return
     */
    public String convertPng2Pdf(String generateImage, String manufactor, String phoneW, String phoneH,
        Long distributorId) {
        // 如果是云创并且是png图片
        if ("YC".equals(manufactor) && generateImage.endsWith(".png")) {
            log.info("png转换pdf params:{},{},{},{}", generateImage, manufactor, phoneH, phoneW);
            try (PDDocument document = new PDDocument()) {
                float width = Float.parseFloat(phoneW) * POINTS_PER_MM;
                float height = Float.parseFloat(phoneH) * POINTS_PER_MM;
                String folder = System.getProperty("java.io.tmpdir");
                File file = new File(folder + "a.png");
                BufferedImage read = ImageIO.read(new URL(generateImage));
                ImageIO.write(read, "png", file);;
                PDPage page = new PDPage(new PDRectangle(width, height));
                PDImageXObject pdImage = PDImageXObject.createFromFileByContent(file, document);
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.drawImage(pdImage, 0, 0, width, height);
                contentStream.close();
                document.addPage(page);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                document.save(outputStream);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                String folder1 = "customize/";
                if (applicationContext.getEnvironment().acceptsProfiles("production")) {
                    folder1 += "bat/";
                } else {
                    folder1 += "bat_test/";
                }

                LocalDate localDate = LocalDate.now();
                int year = localDate.getYear();
                int monthValue = localDate.getMonthValue();
                int dayOfMonth = localDate.getDayOfMonth();
                folder1 = folder1 + year + "-" + monthValue + "-" + dayOfMonth + "/" + distributorId + "/";
                return ossService.uploadExtendStream(System.currentTimeMillis() + ".pdf", folder1, inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return generateImage;
    }

    /**
     *
     * @param generateImage
     * @param materialId
     * @param phoneW
     * @param phoneH
     * @param distributorId
     * 
     *            切换联辉王 多鸿之后 淘宝没有转PDF的需求了
     * @return
     */
    public String convertPng2Pdf(String generateImage, Integer materialId, String phoneW, String phoneH,
        Long distributorId) {
        // 如果是云创并且是png图片
        // if ((materialId == 74 || materialId == 84) && generateImage.endsWith(".png")) {
        // log.info("png转换pdf params:{},{},{},{}", generateImage, materialId, phoneH, phoneW);
        // try (PDDocument document = new PDDocument()) {
        // float width = Float.parseFloat(phoneW) * POINTS_PER_MM;
        // float height = Float.parseFloat(phoneH) * POINTS_PER_MM;
        // String folder = System.getProperty("java.io.tmpdir");
        // String fileName = System.currentTimeMillis() + ".png";
        // File file = new File(folder + fileName);
        // // 水平翻转 （与上面方法 唯一的不同）
        // BufferedImage read;
        // if (materialId == 74 || materialId == 84) {
        // read = flipHorizontally(generateImage);
        // } else {
        // read = ImageIO.read(new URL(generateImage));
        // }
        // ImageIO.write(read, "png", file);;
        // PDPage page = new PDPage(new PDRectangle(width, height));
        // PDImageXObject pdImage = PDImageXObject.createFromFileByContent(file, document);
        // PDPageContentStream contentStream = new PDPageContentStream(document, page);
        // contentStream.drawImage(pdImage, 0, 0, width, height);
        // contentStream.close();
        // document.addPage(page);
        // ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // document.save(outputStream);
        // ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        // String folder1 = "customize/";
        // if (applicationContext.getEnvironment().acceptsProfiles("prod")) {
        // folder1 += "bat/";
        // } else {
        // folder1 += "bat_test/";
        // }
        // LocalDate localDate = LocalDate.now();
        // int year = localDate.getYear();
        // int monthValue = localDate.getMonthValue();
        // int dayOfMonth = localDate.getDayOfMonth();
        // folder1 = folder1 + year + "-" + monthValue + "-" + dayOfMonth + "/" + distributorId + "/";
        // String s = ossService.uploadExtendStream(System.currentTimeMillis() + ".pdf", folder1, inputStream);
        // boolean delete = file.delete();
        // return s;
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }
        return generateImage;
    }

    private static BufferedImage flipHorizontally(String url) throws IOException {
        BufferedImage im = ImageIO.read(new URL(url));
        int w = im.getWidth(null);
        int h = im.getHeight(null);
        BufferedImage b = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
        b.getGraphics().drawImage(im.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        BufferedImage b2 = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
        b2.getGraphics().drawImage(im.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        for (int x = 0; x < h; x++) {
            for (int y = 0; y < w; y++) {
                int rgb = b.getRGB(y, x);
                b2.setRGB(w - 1 - y, x, rgb);
            }
        }
        return b2;
    }

    public static void main(String[] args) {
        //// this.convertPng2Pdf("https://isv.alibabausercontent.com/00000000/imgextra/i2/1103997617/O1CN01nm6tb6268dVSgkKDt_!!1103997617-2-isvtu-00000000.png",
        //// 74,"164.9","76.35");
        // try (PDDocument document = new PDDocument()) {
        // float width = Float.parseFloat("76.35") * POINTS_PER_MM;
        // float height = Float.parseFloat("164.9") * POINTS_PER_MM;
        // String folder = System.getProperty("java.io.tmpdir");
        // File file = new File("a.png");
        // //水平翻转 （与上面方法 唯一的不同）
        // BufferedImage read =
        //// flipHorizontally("https://isv.alibabausercontent.com/00000000/imgextra/i2/1103997617/O1CN01nm6tb6268dVSgkKDt_!!1103997617-2-isvtu-00000000.png");
        //
        // ImageIO.write(read, "png", file);;
        //// PDPage page = new PDPage(new PDRectangle(width, height));
        //// PDImageXObject pdImage = PDImageXObject.createFromFileByContent(file, document);
        //// PDPageContentStream contentStream = new PDPageContentStream(document, page);
        //// contentStream.drawImage(pdImage, 0, 0, width, height);
        //// contentStream.close();
        //// document.addPage(page);
        ////// ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //// document.save(new File("a.pdf"));
        //// ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        //// String folder1 = "customize/";
        //// if (applicationContext.getEnvironment().acceptsProfiles("production")) {
        //// folder1 += "bat/";
        //// } else {
        //// folder1 += "bat_test/";
        //// }
        //// LocalDate localDate = LocalDate.now();
        //// int year = localDate.getYear();
        //// int monthValue = localDate.getMonthValue();
        //// int dayOfMonth = localDate.getDayOfMonth();
        //// folder1 = folder1 + year + "-" + monthValue + "-" + dayOfMonth + "/" + distributorId + "/";
        //// return ossUtil.uploadOss(System.currentTimeMillis() + ".pdf", folder1, inputStream);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
    }
}
