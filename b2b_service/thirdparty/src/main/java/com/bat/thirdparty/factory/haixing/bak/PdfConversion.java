//package com.bat.thirdparty.factory.haixing.bak;
//
//import com.bat.order.api.common.exception.OrderException;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.rendering.PDFRenderer;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
///**
// * 订单同步到海星（脚本类-放同一个包方便管理）
// */
//public class PdfConversion {
//
//    /**
//     * 将只有一页的pdf转成jpg
//     *
//     * @param file
//     * @return
//     */
//    public static String toJpg(File file) {
//        //得到返回的图片路径
//        String outName = file.getPath().replace("pdf", "jpg");
//        PDDocument doc = null;
//        ByteArrayOutputStream os = null;
//        InputStream stream = null;
//        OutputStream out = null;
//        try {
//            //pdf路径
//            stream = new FileInputStream(file);
//            // 加载解析PDF文件
//            doc = PDDocument.load(stream);
//            PDFRenderer pdfRenderer = new PDFRenderer(doc);
//
//            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 500);
//
//            int imageWidth = bim.getWidth(null);
//            int imageHeight = bim.getHeight(null);
//            //如果宽度小于高度，则顺时针转90度
//            if(imageWidth<imageHeight) {
//             //   bim = rotateImage(bim, 90);
//            }
//            os = new ByteArrayOutputStream();
//            ImageIO.write(bim, "jpg", os);
//            byte[] datas = os.toByteArray();
//
//            //jpg文件转出路径
//            out = new FileOutputStream(outName);
//            out.write(datas);
//            return outName;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw  OrderException.buildException("pdf转jpg失败");
//        } finally {
//            if (doc != null) {
//                try {
//                    doc.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (os != null) {
//                try {
//                    os.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (stream != null) {
//                try {
//                    stream.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    /**
//     *
//     * @param bufferedImage
//     *            图片
//     * @param angel
//     *            旋转角度
//     * @return
//     */
//    public static BufferedImage rotateImage(BufferedImage bufferedImage, int angel) {
//        if (bufferedImage == null) {
//            return null;
//        }
//        if (angel < 0) {
//            // 将负数角度，纠正为正数角度
//            angel = angel + 360;
//        }
//        int imageWidth = bufferedImage.getWidth(null);
//        int imageHeight = bufferedImage.getHeight(null);
//        // 计算重新绘制图片的尺寸
//        Rectangle rectangle = calculatorRotatedSize(new Rectangle(new Dimension(imageWidth, imageHeight)), angel);
//        // 获取原始图片的透明度
//        int type = bufferedImage.getColorModel().getTransparency();
//        BufferedImage newImage = null;
//        newImage = new BufferedImage(rectangle.width, rectangle.height, type);
//        Graphics2D graphics = newImage.createGraphics();
//        // 平移位置
//        graphics.translate((rectangle.width - imageWidth) / 2, (rectangle.height - imageHeight) / 2);
//        // 旋转角度
//        graphics.rotate(Math.toRadians(angel), imageWidth / 2, imageHeight / 2);
//        // 绘图
//        graphics.drawImage(bufferedImage, null, null);
//        return newImage;
//    }
//
//    /**
//     * 计算旋转后的尺寸
//     *
//     * @param src
//     * @param angel
//     * @return
//     */
//    private static Rectangle calculatorRotatedSize(Rectangle src, int angel) {
//        if (angel >= 90) {
//            if (angel / 90 % 2 == 1) {
//                int temp = src.height;
//                src.height = src.width;
//                src.width = temp;
//            }
//            angel = angel % 90;
//        }
//        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
//        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
//        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
//        double angel_dalta_width = Math.atan((double) src.height / src.width);
//        double angel_dalta_height = Math.atan((double) src.width / src.height);
//
//        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
//        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
//        int des_width = src.width + len_dalta_width * 2;
//        int des_height = src.height + len_dalta_height * 2;
//        return new Rectangle(new Dimension(des_width, des_height));
//    }
//
//    public static String getFile(String fileUrl) throws Exception {
//      //  String fileUrl="/customize/aisidiy/2019-11-2/null/YkT3BK_1635829544133.pdf";
//        //new一个URL对象
//        URL url = new URL(fileUrl);
//        //打开链接
//        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        //设置请求方式为"GET"
//        conn.setRequestMethod("GET");
//        //超时响应时间为5秒
//        conn.setConnectTimeout(5 * 1000);
//        //通过输入流获取图片数据
//        InputStream inStream = conn.getInputStream();
//        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
//        byte[] data = readInputStream(inStream);
//        String fName = fileUrl.trim();
//        String fileName = "/home/B2B2C/order/"+fName.substring(fName.lastIndexOf("/")+1);
//        //new一个文件对象用来保存图片，默认保存当前工程根目录
//        File imageFile = new File(fileName);
//        //创建输出流
//        FileOutputStream outStream = new FileOutputStream(imageFile);
//        //写入数据
//        outStream.write(data);
//        //关闭输出流
//        outStream.close();
//        return fileName;
//    }
//    public static byte[] readInputStream(InputStream inStream) throws Exception{
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        //创建一个Buffer字符串
//        byte[] buffer = new byte[1024];
//        //每次读取的字符串长度，如果为-1，代表全部读取完毕
//        int len = 0;
//        //使用一个输入流从buffer里把数据读取出来
//        while( (len=inStream.read(buffer)) != -1 ){
//            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
//            outStream.write(buffer, 0, len);
//        }
//        //关闭输入流
//        inStream.close();
//        //把outStream里的数据写入内存
//        return outStream.toByteArray();
//    }
//}
