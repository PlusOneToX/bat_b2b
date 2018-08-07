package com.bat.financial.pay.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.bat.financial.pay.data.WxConfig;
import com.bat.financial.pay.executor.ErrorCode;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bat.financial.api.base.FinancialException;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import okhttp3.HttpUrl;

public class WechatUtil {

    private static final Logger log = LoggerFactory.getLogger(WechatUtil.class);

    /**
     * 生成随机数
     *
     * @return
     */
    public static String createNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 签名字符串
     *
     * @return 签名结果
     */
    public static String sign(Map<String, String> params) {
        String text = createLinkString(params);
        text = text + "&key=" + "BkPKm7DhDrz2wsfWVBkcDcCbXA8SMte2";
        return DigestUtils.md5Hex(getContentBytes(text, "utf-8")).toUpperCase();
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    public static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     * Map转换为 Xml
     *
     * @return Xml
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> map) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        // 防止XXE攻击
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();
        try {
            writer.close();
        } catch (Exception ex) {
        }
        return output;
    }

    /**
     * XML转换为Map
     *
     * @param strXML
     * @return Map
     * @throws Exception
     */
    public static Map<String, Object> getMapFromXML(String strXML) throws Exception {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            // 防止XXE攻击
            documentBuilderFactory.setXIncludeAware(false);
            documentBuilderFactory.setExpandEntityReferences(false);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element)node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /* public static String getToken(String method, HttpUrl url, String body) {
        String nonceStr = "your nonce string";
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, url, timestamp, nonceStr, body);
        String signature = sign(message.getBytes("utf-8"));
    
        return "mchid=\"" + yourMerchantId + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + yourCertificateSerialNo + "\","
                + "signature=\"" + signature + "\"";
    }
    
     public static String sign(byte[] message) {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(yourPrivateKey);
        sign.update(message);
    
        return Base64.getEncoder().encodeToString(sign.sign());
    }*/

    public static String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }

        return method + "\n" + canonicalUrl + "\n" + timestamp + "\n" + nonceStr + "\n" + body + "\n";
    }

    /**
     * 获取微信支付V3版本http请求
     * 
     * @param config
     * @return
     */
    public static HttpClient getWechatHttpClientV3(WxConfig config) {
        PrivateKey merchantPrivateKey = null;
        try {
            merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(config.getApiclientKey().getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            throw FinancialException.buildException(ErrorCode.B_INVALID_KEY_FORMAT);
        }
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
            new WechatPay2Credentials(config.getAccountNo(),
                new PrivateKeySigner(config.getSerialNumber(), merchantPrivateKey)),
            config.getAppKey().getBytes(StandardCharsets.UTF_8));
        WechatPayHttpClientBuilder clientBuilder = WechatPayHttpClientBuilder.create()
            .withMerchant(config.getAccountNo(), config.getSerialNumber(), merchantPrivateKey)
            .withValidator(new WechatPay2Validator(verifier));
        HttpClient httpClient = clientBuilder.build();
        return httpClient;
    }

    public static HttpPost getHttpPost(String url, String params) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setEntity(new StringEntity(params, "UTF-8"));
        return httpPost;
    }
}
