package com.bat.flexible.manager.common.utils.pdf;



import com.bat.flexible.api.FlexibleDubboApiException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import static com.bat.dubboapi.order.order.dto.enmus.FactoryEnum.LHW;
import static com.bat.dubboapi.order.order.dto.enmus.FactoryEnum.MK;

public class PdfConversion {

    private static final Logger logger = LoggerFactory.getLogger(PdfConversion.class);

    /**
     * 将只有一页的pdf转成jpg
     *
     * @param file
     * @param manufactor
     * @return
     */
    public static String toJpg(File file, String manufactor) {
        //得到返回的图片路径
        String outName = file.getPath().replace("pdf", "jpg");
        PDDocument doc = null;
        ByteArrayOutputStream os = null;
        InputStream stream = null;
        OutputStream out = null;
        try {
            //pdf路径
            stream = new FileInputStream(file);
            // 加载解析PDF文件
            doc = PDDocument.load(stream);
            PDFRenderer pdfRenderer = new PDFRenderer(doc);

            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 500);

            int imageWidth = bim.getWidth(null);
            int imageHeight = bim.getHeight(null);
            //如果宽度小于高度，则顺时针转90度
            //麦客固定要纵向
            if(MK.name().equalsIgnoreCase(manufactor)){
                if(imageWidth >imageHeight) {
                    bim = rotateImage(bim, 90);
                }
            }else if(LHW.name().equalsIgnoreCase(manufactor)){
                //联辉王固定横向
                if(imageWidth<imageHeight) {
                    bim = rotateImage(bim, 90);
                }
            }

            os = new ByteArrayOutputStream();
            ImageIO.write(bim, "jpg", os);
            byte[] datas = os.toByteArray();

            //jpg文件转出路径
            out = new FileOutputStream(outName);
            out.write(datas);
            return outName;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("pdf转jpg失败:{}",e.getMessage());
            throw FlexibleDubboApiException.buildException("pdf转jpg失败");
        } finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     *
     * @param bufferedImage
     *            图片
     * @param angel
     *            旋转角度
     * @return
     */
    public static BufferedImage rotateImage(BufferedImage bufferedImage, int angel) {
        if (bufferedImage == null) {
            return null;
        }
        if (angel < 0) {
            // 将负数角度，纠正为正数角度
            angel = angel + 360;
        }
        int imageWidth = bufferedImage.getWidth(null);
        int imageHeight = bufferedImage.getHeight(null);
        // 计算重新绘制图片的尺寸
        Rectangle rectangle = calculatorRotatedSize(new Rectangle(new Dimension(imageWidth, imageHeight)), angel);
        // 获取原始图片的透明度
        int type = bufferedImage.getColorModel().getTransparency();
        BufferedImage newImage = null;
        newImage = new BufferedImage(rectangle.width, rectangle.height, type);
        Graphics2D graphics = newImage.createGraphics();
        // 平移位置
        graphics.translate((rectangle.width - imageWidth) / 2, (rectangle.height - imageHeight) / 2);
        // 旋转角度
        graphics.rotate(Math.toRadians(angel), imageWidth / 2, imageHeight / 2);
        // 绘图
        graphics.drawImage(bufferedImage, null, null);
        return newImage;
    }

    /**
     * 计算旋转后的尺寸
     *
     * @param src
     * @param angel
     * @return
     */
    private static Rectangle calculatorRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }
}
