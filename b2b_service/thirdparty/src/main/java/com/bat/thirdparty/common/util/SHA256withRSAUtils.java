package com.bat.thirdparty.common.util;

import com.bat.thirdparty.common.base.ThirdPartyException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * SHA256withRSA工具类
 *
 *
 */
public class SHA256withRSAUtils {

    private static final Logger logger = LoggerFactory.getLogger(SHA256withRSAUtils.class);


    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 3072;//设置长度
    //private static final String PUBLIC_KEY = "publicKey";
    //private static final String PRIVATE_KEY = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAiFlg5PBNGc7PvAsr0jZoWSiMh4DS3EkHcLmof9WCWsGeF2HYUtWfaWWs5OIxHRHY1iwFIz+6odUObtdtA5hucwVKmncxgx5ivKTalPRvaNG9yW6y1jHmYUOp9HMjSMK0o6a2mKV+uJeD5qbzmMOPh3TSuhTyAzaaDNLyoqwSkgVm15HW/uPLaDVGHPO6ej6nTkp4K9GV5acQiZ1Krd8GdI61d/NAYD+JlYVoddoQkxv6kijL+LxqiEvac4K8hNDCLk6z/1f07mbinn3rtQNdCjUSwJ78JXv52v4fbVIDxQaQt3doVc3bA64FW5xqZsldh+LLQc6gqBpxesIWcCJH/9ZbW8Jl3751lTq4kzGJfYoeCeydffG8XxVA4ySsVot4dstgJqZnf2VDVXqWE6+AU2icTDQIvJHUE9SqfavZUvDdUdof6ddmU5SBKa7/5sN6FxiATETUoZMYx9Rf+ZpVSLnHJ6z6oRhPC49Mph2Q2bgZgK3k+VblRiGT2sNkF6BRAgMBAAE=";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA/PSS";
    public static final String ENCODE_ALGORITHM = "SHA-256";

    /**
     * 生成公、私钥
     * 根据需要返回String或byte[]类型
     * @return
     */
    private static Map<String, String> createRSAKeys(String publicKeyStr,String privateKeyStr){
        Map<String, String> keyPairMap = new HashMap<String, String>();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            /*Map<String, byte[]> byteMap = new HashMap<String, byte[]>();
            byteMap.put(PUBLIC_KEY_NAME, publicKey.getEncoded());
            byteMap.put(PRIVATE_KEY_NAME, privateKey.getEncoded());*/

            //获取公、私钥值
            String publicKeyValue = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyValue = Base64.getEncoder().encodeToString(privateKey.getEncoded());
           // privateKeyValue = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAiFlg5PBNGc7PvAsr0jZoWSiMh4DS3EkHcLmof9WCWsGeF2HYUtWfaWWs5OIxHRHY1iwFIz+6odUObtdtA5hucwVKmncxgx5ivKTalPRvaNG9yW6y1jHmYUOp9HMjSMK0o6a2mKV+uJeD5qbzmMOPh3TSuhTyAzaaDNLyoqwSkgVm15HW/uPLaDVGHPO6ej6nTkp4K9GV5acQiZ1Krd8GdI61d/NAYD+JlYVoddoQkxv6kijL+LxqiEvac4K8hNDCLk6z/1f07mbinn3rtQNdCjUSwJ78JXv52v4fbVIDxQaQt3doVc3bA64FW5xqZsldh+LLQc6gqBpxesIWcCJH/9ZbW8Jl3751lTq4kzGJfYoeCeydffG8XxVA4ySsVot4dstgJqZnf2VDVXqWE6+AU2icTDQIvJHUE9SqfavZUvDdUdof6ddmU5SBKa7/5sN6FxiATETUoZMYx9Rf+ZpVSLnHJ6z6oRhPC49Mph2Q2bgZgK3k+VblRiGT2sNkF6BRAgMBAAE=";
            //存入
            keyPairMap.put(publicKeyStr, publicKeyValue);
            keyPairMap.put(privateKeyStr, privateKeyValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyPairMap;
    }

    /**
     * 解码PublicKey
     * @param key
     * @return
     */
    public static PublicKey getPublicKey(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解码PrivateKey
     * @param key
     * @return
     */
    public static PrivateKey getPrivateKey(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 签名
     * @param privateKeyStr	私钥
     * @param requestData	请求参数
     * @return
     */
    public static String sign(String publicKeyStr, String requestData,String privateKeyStr){
        String signature = null;
        byte[] signed = null;
        try {
            createRSAKeys(publicKeyStr,privateKeyStr);
            PrivateKey privateKey = getPrivateKey(privateKeyStr);
            BouncyCastleProvider p = new BouncyCastleProvider();
            Signature Sign =Signature.getInstance(SIGNATURE_ALGORITHM,p);
            Sign.initSign(privateKey);
            Sign.update(requestData.getBytes());
            signed = Sign.sign();

            signature = Base64.getEncoder().encodeToString(signed);
            logger.info("SHA256withRSA签名结果："+signature);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("SHA256withRSA签名异常{}",e.getMessage());
            throw ThirdPartyException.buildException("签名失败");
        }
        return signature;
    }

    /**
     * 验签
     * @param key	公钥
     * @param requestData	请求参数
     * @param signature	签名
     * @return
     */
    public static boolean verifySign(String key, String requestData, String signature){
        boolean verifySignSuccess = false;
        try {
            PublicKey publicKey = getPublicKey(key);
            BouncyCastleProvider p = new BouncyCastleProvider();
            Signature verifySign =Signature.getInstance(SIGNATURE_ALGORITHM,p);
            //Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(requestData.getBytes());

            verifySignSuccess = verifySign.verify(Base64.getDecoder().decode(signature));
            System.out.println("===验签结果："+verifySignSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return verifySignSuccess;
    }

    public static void main(String[] args) {
        String  publicS = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAiFlg5PBNGc7PvAsr0jZoWSiMh4DS3EkHcLmof9WCWsGeF2HYUtWfaWWs5OIxHRHY1iwFIz+6odUObtdtA5hucwVKmncxgx5ivKTalPRvaNG9yW6y1jHmYUOp9HMjSMK0o6a2mKV+uJeD5qbzmMOPh3TSuhTyAzaaDNLyoqwSkgVm15HW/uPLaDVGHPO6ej6nTkp4K9GV5acQiZ1Krd8GdI61d/NAYD+JlYVoddoQkxv6kijL+LxqiEvac4K8hNDCLk6z/1f07mbinn3rtQNdCjUSwJ78JXv52v4fbVIDxQaQt3doVc3bA64FW5xqZsldh+LLQc6gqBpxesIWcCJH/9ZbW8Jl3751lTq4kzGJfYoeCeydffG8XxVA4ySsVot4dstgJqZnf2VDVXqWE6+AU2icTDQIvJHUE9SqfavZUvDdUdof6ddmU5SBKa7/5sN6FxiATETUoZMYx9Rf+ZpVSLnHJ6z6oRhPC49Mph2Q2bgZgK3k+VblRiGT2sNkF6BRAgMBAAE=";
        String privateKeyStr="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";
        Map<String, String> keyPairMap = createRSAKeys(publicS,privateKeyStr);
        System.out.println("生成公、私钥测试："+keyPairMap);

        String publicKey = keyPairMap.get(publicS);
        String privateKey = keyPairMap.get(privateKeyStr);
        System.out.println(publicKey.length()+"=="+publicS.length());

        //privateKey = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAiFlg5PBNGc7PvAsr0jZoWSiMh4DS3EkHcLmof9WCWsGeF2HYUtWfaWWs5OIxHRHY1iwFIz+6odUObtdtA5hucwVKmncxgx5ivKTalPRvaNG9yW6y1jHmYUOp9HMjSMK0o6a2mKV+uJeD5qbzmMOPh3TSuhTyAzaaDNLyoqwSkgVm15HW/uPLaDVGHPO6ej6nTkp4K9GV5acQiZ1Krd8GdI61d/NAYD+JlYVoddoQkxv6kijL+LxqiEvac4K8hNDCLk6z/1f07mbinn3rtQNdCjUSwJ78JXv52v4fbVIDxQaQt3doVc3bA64FW5xqZsldh+LLQc6gqBpxesIWcCJH/9ZbW8Jl3751lTq4kzGJfYoeCeydffG8XxVA4ySsVot4dstgJqZnf2VDVXqWE6+AU2icTDQIvJHUE9SqfavZUvDdUdof6ddmU5SBKa7/5sN6FxiATETUoZMYx9Rf+ZpVSLnHJ6z6oRhPC49Mph2Q2bgZgK3k+VblRiGT2sNkF6BRAgMBAAE=";
        System.out.println("===开始RSA公、私钥测试===");
        String str = "{\"brandId\":10016,\"brandName\":\"APPLE\",\"count\":1,\"extendField\":{\"skuCodeAndQtys\":\"0086011001050019201:1\"},\"generateImage\":\"\",\"image\":\"/customize/vmall_test/2019-8-30/40/KdteRf_1630304720370.png\",\"materialId\":74,\"modelId\":10249,\"modelName\":\"iPhone 12 Pro\",\"pictureId\":947,\"price\":49,\"skuNo\":\"810071000309\",\"totalPrice\":49}1630309491516";
        String sign = sign(publicS, str,privateKeyStr);

        verifySign(publicS, str, sign);
    }

}
