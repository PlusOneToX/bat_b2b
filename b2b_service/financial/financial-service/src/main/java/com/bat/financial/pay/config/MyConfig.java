package com.bat.financial.pay.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/15 17:18
 */
public class MyConfig extends WXPayConfig {

    private String appID = "wx65f4056d3c04f9df";

    private String mchID = "1607647316";

    private String key = "BkPKm7DhDrz2wsfWVBkcDcCbXA8SMte2";

    /**
     * 暂时没有收集证书 V2不支持分销商退款
     */
    private byte[] certData;

    public MyConfig() throws Exception {
        // String certPath = "/path/to/apiclient_cert.p12";
        // File file = new File(certPath);
        // InputStream certStream = new FileInputStream(file);
        // this.certData = new byte[(int)file.length()];
        // certStream.read(this.certData);
        // certStream.close();
    }

    @Override
    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    @Override
    public String getMchID() {
        return mchID;
    }

    public void setMchID(String mchID) {
        this.mchID = mchID;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        // 这个方法需要这样实现, 否则无法正常初始化WXPay
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
