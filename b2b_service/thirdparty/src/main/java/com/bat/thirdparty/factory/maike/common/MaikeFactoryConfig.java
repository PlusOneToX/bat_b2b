package com.bat.thirdparty.factory.maike.common;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class MaikeFactoryConfig {

    private String maikeKey;

    private String maikeSecret;

    /**
     * 域名
     */
    private String maikeDomain;

    /**
     * 下单
     */
    private String addOrderUrl;

    /**
     * 配送方式查询接口
     */
    private String deliveryQueryUrl;

    /**
     * 获取库存列表
     */
    private String skuQueryUrl;

    /**
     * 取消订单
     */
    private String cancelOrderUrl;

    /**
     * 公司 id
     */
    private Integer companyId;

    // 麦客供应商Id
    private String supplierNo;

    // 麦客仓库编码
    private String warehouseNo;

    /**
     * FTP服务器存储文件的根路径
     */
    private String ftpRootFolder;

    /**
     * FTP服务器存储文件的根路径存储的文件夹
     */
    private String ftpLocalFile;

    /**
     * IP访问到FTP服务器
     */
    private String ftpIp;

    /**
     * FTP服务器端口
     */
    private Integer ftpPort;

    /**
     * FTP服务器用户名
     */
    private String ftpUserName;

    /**
     * FTP密码
     */
    private String ftpPassword;
}
