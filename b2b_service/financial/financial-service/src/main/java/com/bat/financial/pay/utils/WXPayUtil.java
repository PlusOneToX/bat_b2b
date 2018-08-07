package com.bat.financial.pay.utils;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: lim
 * @description: 微信V3 RSA签名工具
 * @date: 2018/7/5 10:32
 */
public class WXPayUtil {

    private static Logger log = LoggerFactory.getLogger(WXPayUtil.class);

    private static final String CHARSET = "utf-8";
    private static final String ALGORITHMS = "RSA";
    private static final String SIGNALGORITHMS = "SHA256withRSA";

    /**
     * RSA 签名加密 微信没有实现
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        String signType = data.get("signType");
        if (!ALGORITHMS.equals(signType)) {
            log.error("自定义 工具类只支持V3 RSA V2接口请使用官方工具类");
        }
        key = key.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replace("\n", "");
        String appId = data.get("appId");
        String timeStamp = data.get("timeStamp");
        String nonceStr = data.get("nonceStr");
        String packages = data.get("package");
        String encryptText = appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + packages + "\n";
        Signature signature = Signature.getInstance(SIGNALGORITHMS);
        PrivateKey privateKey = getPrivateKey(key);
        if (privateKey != null) {
            signature.initSign(privateKey);
            log.info("加密数据：{}", encryptText);
            signature.update(encryptText.getBytes(CHARSET));
            byte[] bytes = signature.sign();
            // 转成hex字符串
            return Base64.getEncoder().encodeToString(bytes);
        }
        return null;
    }

    public static PublicKey getPublicKey(String publicKey) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHMS);
            return keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static PrivateKey getPrivateKey(String privateKey) {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHMS);
            return keyFactory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String key =
            "-----BEGIN PRIVATE KEY-----\n" + "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDAsN7gOKhguF+w\n"
                + "l2GkChIcRoKKuvtffBhbnreAZi7NP218LSfU+zFTvqIvGnanjMUEzQ0U7Hppeit5\n"
                + "3JwAF3UnJiO3pDZBryC6+/mvPvNBUmRJHJMWGTMWbCyOcVXhfrIWWnblpgPeL44p\n"
                + "iWUAcD+vKTpYxpizgOmIEnb34NVF+JYCjXGEoLY5zBzcVlTVPYc7d7ltxNhy+t9z\n"
                + "TndWv0UzQP7fJ6HCApgdLcbNk7huaotwipfyTF7veK7M2YeLVwZNXwNaE+CBEADq\n"
                + "v1pNPYL/buwVLkBtwksZVe3UNSh3+vP0HNH3H5exXVRkhMNNZtrK10Okp5Wq3OwF\n"
                + "z6yoGqglAgMBAAECggEABS48L8cWL9OFLxhC1cg3iG8U32n7jCun8/6mbFtS8s27\n"
                + "wdTs9zrxrpC3h5CslACi42gn6+IXwHI6bQJTZ2U6Dx1lZHCTzNxKgvx3UBDfNWhj\n"
                + "rduI0NVInFe4MvQ1HOTtINC51Z7LB9Q7fD5nasWZ0LnJCc39GQZkQPZOIIf48/KZ\n"
                + "EPyJ1D97DCxoFhCu6tKZSYv1242M6B93pT4AU7/9/VilkX4QJYUrxTx2Yp2iMLEg\n"
                + "+dpb59R5e8n7jgbKGDLGwsdNG2jerqBo2ShMq2MJplEWK8fVVsEHng9qM1wxgnP/\n"
                + "hxjjk9O6b9wfOlrOslXCU/JOOjH5kcaHoggWmnGd2QKBgQDpDz7x+jRIul6Z9vWq\n"
                + "76mYy9hK98ypf0JNyXH+Gc/pb2qN+fcyyRuolWAlEJYDtIeELUbJzqFyu8BWsO/Y\n"
                + "O6KaInEPMK7j0xzSRQW6Myaql13yQ4mSMykQXRl2UnoCCyz9nCGV8kVGQXtqru27\n"
                + "Oo+s1Dx4Q8deTZ3TCACOpJsVxwKBgQDTqGULfEkUVx3a33RHwL6haVbfkRRNgqGl\n"
                + "nEFd9bJf7eEPPZQnjOfpFB53vP0B+QqYaNid4DK5BZh50JpW1PQ15OQXN+1BVEsD\n"
                + "af1a/V1OL0vkewzV8+8v01UgzTls8Rbmfkb6Pn0BAav/bvlikXd7lzPnfeeD0qA8\n"
                + "pq0G+woiswKBgBOpdp0Bh9Gu73Y70IQITh2W7Pt+JmUSWGyplxVh5hmCjdHGieVd\n"
                + "9Bhn6cGWwaE0ZM5w52z1IsfEMTjfGoz6kwA/8ZvfdpG/6MmQiygnUpbHyGQ88GlG\n"
                + "Kau4vwIteR+dA8Gtn2PAGboQojHg6iuGiEKDcUqQJUy+bZc1MJ7YIkFXAoGASgkB\n"
                + "Lz9OILtISiMXfXHCPJuoM5N/0oz7Ff7OLpuexUdkDfywMhVrmW/cKG4G4gXG2wDx\n"
                + "pGej1mxKpXF+Sxk6Qw/JaQW7C7r8cjlE0esGFC8fENCp8Lze0Y/OmyauC5lZNU6+\n"
                + "bRImj4+SJksfGIdhj+2XzkWQEeA/BHgY1fRB21kCgYAyCZ9z4PQQV3HP1WSq1Gmx\n"
                + "2N6QyqwGWO/rfkvfSD/FoYxK5I7aAhjmps9xmHoJ5jvvEYQ47m85mDDxZN7xxZ40\n"
                + "fJkUw+BVImzFYbrPqH8nwWKuWRrbdckgwfuaUkMtG2k5OYTiDoL+cgTdBzopc2ma\n" + "qcps4kI+QRG1TMd/LQMQuw==\n"
                + "-----END PRIVATE KEY-----\n";
        Map<String, String> map = new HashMap<>();
        map.put("appId", "wx84276a50b449610d");
        map.put("timeStamp", "1625457106");
        map.put("nonceStr", "d10e05d8d8a249f6a09cf326816830b4");
        map.put("package", "prepay_id=wx05115142505766bae123f48a05cc2b0000");
        map.put("signType", "RSA");
        String s = generateSignature(map, key);
        System.out.println(s);
    }
}
