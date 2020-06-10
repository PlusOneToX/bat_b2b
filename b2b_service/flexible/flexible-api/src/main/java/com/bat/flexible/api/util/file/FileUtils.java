package com.bat.flexible.api.util.file;

import com.bat.flexible.api.FlexibleCustomException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileUtils {

    /**
     *
     * @param urlString 需要下载图片的路径
     * @param filename  下载后图片的命名
     * @param savePath  下载到那个文件夹下
     * @throws Exception
     */
    public static void download(String urlString, String filename,String savePath)  {
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5*1000);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf=new File(savePath);
            if(!sf.exists()){
                sf.mkdirs();
            }
            System.out.println(sf.getPath());
            System.out.println(filename);
            String fileUrl = sf.getPath()+"/"+filename;
            System.out.println(fileUrl);
            OutputStream os = new FileOutputStream(fileUrl);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            System.out.println(filename);
            throw new FlexibleCustomException("下载文件异常");
        }

    }

    /**
     * 获取本地文件路径
     *
     * @param folderName 文件夹名
     * @return
     */
    public static String getSystemDirectory(String folderName) {

        String basicPath = System.getProperty("user.dir");
        String filePath = basicPath.concat("/").concat(folderName).concat("/").concat("out");
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String path= directory.getPath();
        return path+"/";
        //  return path.concat("\\" );
    }

    /**
     * 根据文件名或者最终的文件名
     * @param url 网络图片
     * @return
     */
    public static String getFileName(String url){
        if(url==null || url==""){
            return null;
        }
        String [] arr =url.split("/");

        return arr[arr.length-1];
    }

    public static void main(String[] args) {
        String url ="http:10.81.36.193:8081png.png";
        System.out.println(getFileName(url));
    }

}
