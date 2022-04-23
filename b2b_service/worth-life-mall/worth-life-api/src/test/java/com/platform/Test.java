package com.platform;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        try {
            FileInputStream in = new FileInputStream("/Users/pfrong/Desktop/价值人生/证书/1604334321_20201124_cert/apiclient_cert.p12");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            out.write(buffer);
            String strHex = "";
            StringBuilder sb = new StringBuilder("");
            for (int n = 0; n < buffer.length; n++) {
                strHex = Integer.toHexString(buffer[n] & 0xFF);
                sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
            }
            System.out.print(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
