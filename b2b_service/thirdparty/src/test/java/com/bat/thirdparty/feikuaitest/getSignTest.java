package com.bat.thirdparty.feikuaitest;

import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.util.*;

class getSignTest {

    private String rotbotKey="123123";
    private String rotbotToken="435E8CA8CC4347BCA07216089BD778C7";

    @Test
    void test() throws Exception {

        Map<String,String> reqData = new LinkedHashMap<>();

        reqData.put("bizID","5123");
        reqData.put("groupID","0");
        reqData.put("userID","5123");
        reqData.put("time", "1622535130");
        reqData.put("jsonStringParameter", "{\"req_para\":{\"dd_user_id\":4394,\"start_time\":1619859837,\"end_time\":1622535131,\"page\":1},\"shardView\":{\"shardKeySchema\":\"lj_wuliu1\",\"shardKeyTable\":\"\",\"shardKeyTableNumber\":0}}");
        reqData.put("app_id", rotbotKey);
        String sign = getSign(reqData, rotbotToken);
        System.out.println(sign);

    }


    private static String getSign(Map<String,String> kv, String PARTNER_KEY) throws Exception {

        List<Map.Entry<String, String>> list = sortHashKeyAsc(kv);
        String value = PARTNER_KEY;
        for (int i = 0; i < list.size(); i++) {

            value += list.get(i).getKey() + list.get(i).getValue();
        }
        return md5(value);
    }

    public static List<Map.Entry<String, String>> sortHashKeyAsc(Map<String, String> kv) {

        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(kv.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2){
                // 升序排序
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return list;
    }

    public static String md5(String source) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(source.getBytes("utf-8"));
        return byte2hex(bytes);
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();

        for(int i = 0; i < bytes.length; ++i) {

            String hex = Integer.toHexString(bytes[i] & 255);
            if (hex.length() == 1) {

                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }

        return sign.toString();
    }
}
