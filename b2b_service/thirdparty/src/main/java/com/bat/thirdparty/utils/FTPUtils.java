package com.bat.thirdparty.utils;


import com.bat.thirdparty.common.base.ThirdPartyOpenApiException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;


public class FTPUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPUtils.class);
    /**
     * 上传文件至FTP服务器
     *
     * @param url
     * 		服务器IP地址
     * @param port
     * 		服务器端口
     * @param userName
     * 		用户登录名
     * @param password
     * 		用户登录密码
     * @param storePath
     * 		服务器文件存储路径
     * @param fileName
     * 		服务器文件存储名称
     * @param is
     * 		文件输入流
     * @return
     * 		<b>true</b>：上传成功
     * 		<br/>
     * 		<b>false</b>：上传失败
     */
    public static boolean storeFile (String url, int port, String userName, String password, String storePath, String fileName, InputStream is) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            //判断输入流是否为空
            if(is ==null || is.available()<=0){
                LOGGER.error("FTP输入流为空"+fileName);
                throw new ThirdPartyOpenApiException("FTP输入流为空"+fileName);
            }
            // 连接至服务器，端口默认为21时，可直接通过URL连接
            ftp.connect(url ,port);
            // 登录服务器
            ftp.login(userName, password);
            LOGGER.info("FTP登录返回："+ftp.getReplyCode());
            // 判断返回码是否合法
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                // 不合法时断开连接
                ftp.disconnect();
                LOGGER.info("不合法的ftp请求");
                // 结束程序
                return result;
            }
            // 判断ftp目录是否存在，如果不存在则创建目录，包括创建多级目录
            String s = "/"+storePath;
            String[] dirs = s.split("/");
            ftp.changeWorkingDirectory("/");
            //按顺序检查目录是否存在，不存在则创建目录
            for(int i=1; dirs!=null&&i<dirs.length; i++) {
                if(!ftp.changeWorkingDirectory(dirs[i])) {
                    if(ftp.makeDirectory(dirs[i])) {
                        if(!ftp.changeWorkingDirectory(dirs[i])) {
                            return false;
                        }
                    }else {
                        return false;
                    }
                }
            }
            // 设置文件操作目录
            ftp.changeWorkingDirectory(storePath);
            // 设置文件类型，二进制
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置缓冲区大小
            ftp.setBufferSize(5076);
            ftp.enterLocalPassiveMode();
            // 上传文件
            result = ftp.storeFile(fileName, is);
            LOGGER.info("上传FTP结果："+result);
            // 关闭输入流
            is.close();
            // 登出服务器
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("上传图片到FTP服务器失败"+e.getMessage());
            new RuntimeException("上传图片到FTP服务器失败");
        } finally {
            try {
                // 判断输入流是否存在
                if (null != is) {
                    // 关闭输入流
                    is.close();
                }
                // 判断连接是否存在
                if (ftp.isConnected()) {
                    // 断开连接
                    ftp.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.info("断开FTP服务器失败"+e.getMessage());
                new RuntimeException("断开FTP服务器失败");
            }
        }
        return result;
    }

    /**
     * 从FTP服务器下载文件至本地
     *
     * @param url
     * 		服务器IP地址
     * @param port
     * 		服务器端口
     * @param userName
     * 		用户登录名
     * @param password
     * 		用户登录密码
     * @param remotePath
     * 		服务器文件存储路径
     * @param fileName
     * 		服务器文件存储名称
     * @param localPath
     * 		本地文件存储路径
     * @return
     * 		<b>true</b>：下载成功
     * 		<br/>
     * 		<b>false</b>：下载失败
     */
    public static boolean retrieveFile (String url, int port, String userName, String password, String remotePath, String fileName, String localPath) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        OutputStream os = null;
        try {
            // 连接至服务器，端口默认为21时，可直接通过URL连接
            ftp.connect(url ,port);
            // 登录服务器
            ftp.login(userName, password);
            // 判断返回码是否合法
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                // 不合法时断开连接
                ftp.disconnect();
                // 结束程序
                return result;
            }
            // 设置文件操作目录
            ftp.changeWorkingDirectory(remotePath);
            // 设置文件类型，二进制
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置缓冲区大小
            ftp.setBufferSize(3072);
            // 设置字符编码
            ftp.setControlEncoding("UTF-8");
            // 构造本地文件对象
            File localFile = new File(localPath + "/" + fileName);
            // 获取文件操作目录下所有文件名称
            String[] remoteNames = ftp.listNames();
            // 循环比对文件名称，判断是否含有当前要下载的文件名
            for (String remoteName: remoteNames) {
                if (fileName.equals(remoteName)) {
                    result = true;
                }
            }
            // 文件名称比对成功时，进入下载流程
            if (result) {
                // 构造文件输出流
                os = new FileOutputStream(localFile);
                // 下载文件
                result = ftp.retrieveFile(fileName, os);
                // 关闭输出流
                os.close();
            }
            // 登出服务器
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 判断输出流是否存在
                if (null != os) {
                    // 关闭输出流
                    os.close();
                }
                // 判断连接是否存在
                if (ftp.isConnected()) {
                    // 断开连接
                    ftp.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 从FTP服务器删除文件
     *
     * @param url
     * 		服务器IP地址
     * @param port
     * 		服务器端口
     * @param userName
     * 		用户登录名
     * @param password
     * 		用户登录密码
     * @param remotePath
     * 		服务器文件存储路径
     * @param fileName
     * 		服务器文件存储名称
     * @return
     * 		<b>true</b>：删除成功
     * 		<br/>
     * 		<b>false</b>：删除失败
     */
    public static boolean deleteFile (String url, int port, String userName, String password, String remotePath, String fileName) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            // 连接至服务器，端口默认为21时，可直接通过URL连接
            ftp.connect(url ,port);
            // 登录服务器
            ftp.login(userName, password);
            // 判断返回码是否合法
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                // 不合法时断开连接
                ftp.disconnect();
                // 结束程序
                return result;
            }
            // 设置文件操作目录
            ftp.changeWorkingDirectory(remotePath);
            // 设置文件类型，二进制
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置缓冲区大小
            ftp.setBufferSize(3072);
            // 设置字符编码
            ftp.setControlEncoding("UTF-8");
            // 获取文件操作目录下所有文件名称
            String[] remoteNames = ftp.listNames();
            // 循环比对文件名称，判断是否含有当前要下载的文件名
            for (String remoteName: remoteNames) {
                if (fileName.equals(remoteName)) {
                    result = true;
                }
            }
            // 文件名称比对成功时，进入删除流程
            if (result) {
                // 删除文件
                result = ftp.deleteFile(fileName);
            }
            // 登出服务器
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 判断连接是否存在
                if (ftp.isConnected()) {
                    // 断开连接
                    ftp.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static void main(String[] args) throws IOException {
//		try {
//			FileInputStream fis = new FileInputStream(new File("D:/Soft Storage/软件工具箱/HTML_Help_WorkShop_1.3_XiaZaiBa.zip"));
//			System.out.println(storeFile("192.168.1.2", 21, "admin", "1", "C:/Documents and Settings/Administrator/桌面", RandomUUID.random() + ".zip", fis));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//
        //File file = new File("C:/Users/freed/Desktop/1.txt");
        //InputStream is = new FileInputStream(file);

        //System.out.println(storeFile("127.0.0.1", 21, "feili", "feili", "examples", "2.txt", is));
        //System.out.println(retrieveFile("127.0.0.1", 21, "feili", "feili", "examples/jsp", "index.html", "C:/Users/freed/Desktop"));
        //System.out.println(deleteFile("127.0.0.1", 21, "feili", "feili", "testpath", "1.txt"));
        URL url = new URL("/165/110995/422867/1615972175463/WMEZH4_1615286941680.jpg");
        InputStream inputStream = url.openStream();
      //  storeFile("172.16.12.46",21,"lp900","bat2019","test","112.jpg",inputStream);
        storeFile("172.16.1.35",27,"ftpadmin","ftpadmin","test","115.jpg",inputStream);
        String ss="/165/110995/422867/1615972175463/WMEZH4_1615286941680.jpg";
        String[] arr= ss.split("com/");
        System.out.println(arr[1]);
        arr = arr[1].split("/");
        System.out.println(arr[arr.length-1]);
    }

}
