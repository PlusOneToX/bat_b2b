package src.main.java.com.bat.financial.service.pay.utils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/5 11:11
 */

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.xmlbeans.impl.util.HexBin;

/**
 * @author txb
 * @date 2018年03月30日
 */
public class RSATest {

    private static final String CHARSET = "utf-8";
    private static final String ALGORITHMS = "RSA";
    private static final String SIGNALGORITHMS = "SHA256withRSA";

    static String myRSAPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9j9jvyIoanJebGJb+Zns2TaHl"
        + "xUiV9DrPCRXGHwKwy+Na8Au0zdu4rg6Qcly632HXDGlNApZYm1YFr7olGgIWCslp"
        + "v6C2kDwkIENZC41rBebT4V21E9Fd6EAdOg+sWmv9hj5HZlcfLUyLqJffv7NF9Z7H" + "zCbscDuUtTAjjENO8wIDAQAB";
    static String myRSAPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL2P2O/Iihqcl5sY"
        + "lv5mezZNoeXFSJX0Os8JFcYfArDL41rwC7TN27iuDpByXLrfYdcMaU0CllibVgWv"
        + "uiUaAhYKyWm/oLaQPCQgQ1kLjWsF5tPhXbUT0V3oQB06D6xaa/2GPkdmVx8tTIuo"
        + "l9+/s0X1nsfMJuxwO5S1MCOMQ07zAgMBAAECgYAxahonB6PYpBFxQMu7rYNY+IvH"
        + "ldICJRRffALNDrBo3f16azDiBPfOALGTPfymSiz7HiUlChOfLX8TJGfl3A3Gtisw"
        + "YnLPYw/+uZXLLfwSA+sJjOZqxozTrr90o5Lni2XQx8u1oj1XnPTxZrHlGnuiXpUL"
        + "IHugjb1xR6cs70heoQJBAOivnfe2JHTnDT5wYgZ9q/7hKRuqINnhcJ/eNnIN8HPp"
        + "daRTYf7VjNcBzHRFSmDQVmciQ1PHJtN/G0nK4vSUoEUCQQDQjhl/V5KO535u6br2"
        + "zqb99J9TKIZtAuTly6Su1tSuREBZ2TFRZ2kQg7ayzgBL3+0Q3N+8EGWhFPWJembq"
        + "rbHXAkEAo8Z4OVJKdr3p5HjVhhwOY2e3t3EXjkPPMbcGO2D0TaGU3ASUhPlqGCt7"
        + "7JqYr2v0ZMMJAH68JUDhxB65f8zcwQJBAJA51PQ1kYr2GH+Hb96CpwV1CEGwAtac"
        + "a8NnA2pN8yLY3E/GXc7X3tam38/jmlzebMl+ldMu27l8DxmV5lpg6F0CQEofIGtb"
        + "TxasTdNIDUvHcVOn1N5fPN/bNy8oeLaL2a8Y1UA8CIYMthkcigGhqkXtNAKCUR0k" + "9E4nufNoYHANU1M=";

    public static PublicKey getPublicKey(String publicKey) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHMS);
            return keyFactory.generatePublic(spec);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static PrivateKey getPrivateKey(String privateKey) {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHMS);
            return keyFactory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 公钥加密
    public static String encrypt(String plainText, String publicKeyStr) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHMS);// 根据需要选择分组模式、填充。比如：RSA/ECB/NoPadding
            PublicKey publickKey = getPublicKey(publicKeyStr);
            if (publickKey != null) {
                cipher.init(Cipher.ENCRYPT_MODE, publickKey);
                byte[] bytes = cipher.doFinal(plainText.getBytes(CHARSET));
                return Base64.getEncoder().encodeToString(bytes);// base64编码
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 私钥解密
    public static String decrypt(String encryptText, String privateKeyStr) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHMS);// 根据需要选择分组模式、填充。比如：RSA/ECB/NoPadding
            PrivateKey privateKey = getPrivateKey(privateKeyStr);
            if (privateKey != null) {
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(encryptText));// encryptText之前用base64编码过，要解码成bytes
                return new String(bytes);// 得到明文
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 签名 发送方私钥签名
     */
    public static String sign(String encryptText, String privateKeyStr) {
        try {
            Signature signature = Signature.getInstance(SIGNALGORITHMS);
            PrivateKey privateKey = getPrivateKey(privateKeyStr);
            if (privateKey != null) {
                signature.initSign(privateKey);
                signature.update(encryptText.getBytes(CHARSET));
                byte[] bytes = signature.sign();
                return HexBin.bytesToString(bytes);// 转成hex字符串
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 验签 发送方公钥验签
     */
    public static boolean verifySign(String signText, String encryptText, String publicKeyStr) {
        PublicKey publickKey = getPublicKey(publicKeyStr);
        try {
            if (publickKey != null) {
                Signature signature = Signature.getInstance(SIGNALGORITHMS);
                signature.initVerify(publickKey);
                signature.update(encryptText.getBytes(CHARSET));
                return signature.verify(HexBin.stringToBytes(signText));// hex字符串转bytes
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 为了方便，只用一对公私钥，实际需要发送方、接收方两对公私钥
    public static void main(String[] args) {
        String plainText = "我是明文 1 3 5 7 9";
        System.out.println("明文:" + plainText);
        String encryptText = encrypt(plainText, myRSAPublicKey);
        System.out.println("密文" + encryptText);

        String signText = sign(encryptText, myRSAPrivateKey);
        System.out.println("信息摘要：" + signText);
        if (verifySign(signText, encryptText, myRSAPublicKey)) {
            System.out.println("验签通过");
            String decodeText = decrypt(encryptText, myRSAPrivateKey);
            System.out.println("解密后：" + decodeText);
        }
    }

}
