package com.bat.financial.pay.utils;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;

public class Pkipair {

    public static String signMsg(String signMsg, String signFileName, String signPwd, String signPrivateKey) {
        String base64 = "";
        try {
            // 密钥仓库
            KeyStore ks = KeyStore.getInstance("PKCS12");
            // 读取密钥仓库
            // FileInputStream ksfis = new FileInputStream("e:/tester-rsa.pfx");
            InputStream ins = Pkipair.class.getClassLoader().getResourceAsStream(signFileName);
            BufferedInputStream ksbufin = new BufferedInputStream(ins);
            char[] keyPwd = signPwd.toCharArray();
            // char[] keyPwd = "YaoJiaNiLOVE999Year".toCharArray();
            ks.load(ksbufin, keyPwd);
            // 从密钥仓库得到私钥 test-alias
            PrivateKey priK = (PrivateKey)ks.getKey(signPrivateKey, keyPwd);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(priK);
            signature.update(signMsg.getBytes("utf-8"));
            Base64 aaa = new Base64();
            base64 = aaa.encodeToString(signature.sign());
            // base64 = encoder.encode(signature.sign());
        } catch (FileNotFoundException e) {
            System.out.println("文件找不到");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // System.out.println("test = " + base64);
        return base64;
    }

    public static String signMsg(String signMsg, InputStream ins, String signPwd, String signPrivateKey) {
        String base64 = "";
        try {
            // 密钥仓库
            KeyStore ks = KeyStore.getInstance("PKCS12");
            // 读取密钥仓库
            BufferedInputStream ksbufin = new BufferedInputStream(ins);
            char[] keyPwd = signPwd.toCharArray();
            // char[] keyPwd = "YaoJiaNiLOVE999Year".toCharArray();
            ks.load(ksbufin, keyPwd);
            // 从密钥仓库得到私钥 test-alias
            PrivateKey priK = (PrivateKey)ks.getKey(signPrivateKey, keyPwd);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(priK);
            signature.update(signMsg.getBytes("utf-8"));
            Base64 aaa = new Base64();
            base64 = aaa.encodeToString(signature.sign());
            // base64 = encoder.encode(signature.sign());
        } catch (FileNotFoundException e) {
            System.out.println("文件找不到");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // System.out.println("test = " + base64);
        return base64;
    }

    public static boolean enCodeByCer(String checkSignFileName, String val, String msg) {
        System.out.println("checkSignFileName:" + checkSignFileName);
        System.out.println("val:" + val);
        System.out.println("msg:" + msg);
        boolean flag = false;
        try {
            // 获得文件(绝对路径)
            // InputStream inStream = new FileInputStream("e:/99bill[1].cert.rsa.20140803.cer");
            InputStream ins = Pkipair.class.getClassLoader().getResourceAsStream(checkSignFileName);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate)cf.generateCertificate(ins);
            // 获得公钥
            PublicKey pk = cert.getPublicKey();
            // 签名
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(pk);
            signature.update(val.getBytes());

            // 解码
            java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
            byte[] cs = decoder.decode(msg);
            System.out.println(cs.length);
            flag = signature.verify(cs);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("no");
        }
        return flag;
    }

    public static void main(String[] args) {
        boolean b = enCodeByCer("payconfig/kuaiqian/prod/99bill.rsa.cer",
            "merchantAcctId=1021023779601&version=v2.0&language=1&signType=4&payType=14&bankId=ABC&orderId=kqc0ce1430107807459381248&orderTime=20180724180046&orderAmount=80951&dealId=4647152459&bankDealId=2018072490101180054101200210604&dealTime=20180724180304&payAmount=80951&fee=1000&payResult=10",
            "fKhucw5GBCq+nHWiY8v9Y1PifSrZ7V/sdE9+C7moCTsuMC8dic/dY6L0gnhghpuTy+n4G0M7qdEJYbJ9OuRogjfOjnW8Oo0rkn8jpEf9/S5addDKvHqyKzDzL/duMb4BEqvavo0fw7do1G2P8SWaGAT540w3+IrCR3GH/eFT+feaXL7dy8Zurh0Dd343bfnYfLAyyCYNsePTgcRptqtgBauV/m+MDyxe3oH5iT4EzsLdSLGxydh86SyUm+RJesKTfws2CmxZjxh4N8NEoL0fuTYOcMZ/Bn3uGx4g0Xo3b7ZkMvFnPVfwsG+0Gp/wqmsVSmneiOo5hJd8Kw2TYXtpsQ==");
        System.out.println(b);
    }

}
