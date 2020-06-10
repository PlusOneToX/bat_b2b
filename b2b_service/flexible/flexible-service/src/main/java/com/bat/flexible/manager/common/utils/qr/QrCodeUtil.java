package com.bat.flexible.manager.common.utils.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/12/16 21:40
 */
@Component
public class QrCodeUtil {
    public static void main(String[] args) {
        String url =
            "http://127.0.0.1:9988/developer/diy/index?platform=6&distributorId=2507&appid=wx5379b77e9cf05a20&shopId=654";// 需要转化二维码的地址链接
        String path = FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "testQrcode"; // 图片保存的位置
        String fileName = "XXX.jpg"; // 图片名称
        createQrCode(url, path, fileName);
    }

    public static String fontPath;

    @Value("${font.path}")
    public void setFontPath(String fontPath) {
        QrCodeUtil.fontPath = fontPath;
    }

    public static String createQrCode(String url, String path, String fileName) {
        try {
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
            File file = new File(path, fileName);
            if (file.exists()
                || ((file.getParentFile().exists() || file.getParentFile().mkdirs()) && file.createNewFile())) {
                writeToFile(bitMatrix, "jpg", file);
                System.out.println("搞定：" + file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createQrCodeStream(String url, OutputStream out) {
        try {
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
            // File file = new File(path, fileName);
            writeToStream(bitMatrix, "jpg", out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在原有生成流的基础上，在二维码下方增加店铺编码
     * @param url
     * @param shopCode
     * @param out
     * @return
     */
    public static String createQrCodeExtendStream(String url, OutputStream out,String shopCode) {
        try {
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
            // File file = new File(path, fileName);
            writeToExtendStream(bitMatrix, "jpg", out,shopCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    //添加编码
    static void writeToExtendStream(BitMatrix matrix, String format, OutputStream stream, String shopCode) throws IOException, FontFormatException {
        BufferedImage image = toBufferedImageExtend(matrix);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int fontSize=40;
        //如果内容过多；字体缩小
        if(shopCode.length()>10){
            fontSize=25;
        }
        if(shopCode.length()>12){
            fontSize=22;
        }
        if(shopCode.length()>15){
            fontSize=18;
        }
        Font font =  Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont((float) fontSize);
        //Font font = new Font("微软雅黑", Font.PLAIN, fontSize);
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        // 计算文字长度，计算居中的x点坐标
        FontMetrics fm = graphics.getFontMetrics(font);
        int textWidth = fm.stringWidth(shopCode);
        int widthX = (image.getWidth() - textWidth) / 2;
        // 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
        graphics.drawString(shopCode,widthX,450);
        graphics.dispose();
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        System.out.println(height);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    private static BufferedImage toBufferedImageExtend(BitMatrix matrix) {
        // 源 400*400 现 400*600 多出200为编码区域
        int width = matrix.getWidth();
        int height = matrix.getHeight()+100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(y>=400){
                    image.setRGB(x, y, WHITE);
                }else{
                    image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
                }
            }
        }
        return image;
    }
}