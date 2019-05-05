package com.bat.thirdparty.feikuaitest;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.flexible.model.dto.ModelMaterialRelevanceDTORpcQry;
import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.feikuai.FeiKuaiOrderAddCmd;
import com.bat.thirdparty.ThirdPartyApplicationTests;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.factory.feikuai.executor.FeiKuaiCmdExe;
import com.bat.thirdparty.factory.feikuai.service.FeiKuaiFactoryServiceImpl;
import com.bat.thirdparty.tenant.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
public class ApiTest extends ThirdPartyApplicationTests{

    @Resource
    private HttpUtil httpUtil;

    private final String clientId = "123123";
    private final String secretKey = "435E8CA8CC4347BCA07216089BD778C7";
    private final BASE64Encoder encoder = new BASE64Encoder();

    @Resource
    private FeiKuaiFactoryServiceImpl feiKuaiFactoryService;

    @Resource
    private FeiKuaiCmdExe cmdExe;


    @Test
    public void getSupplierBarCodeTest() throws IOException {



        String url = "http://diy.aeding.com/robot_api/api/robot/query.supplier.barcode";
//       String url = "http://127.0.0.1:8112/api/robot/query.supplier.barcode";

        Map<String,Object> req_para = new HashMap<>();
        req_para.put("dd_user_id",4394);
        req_para.put("start_time",1630556007);
        req_para.put("end_time",1630650655);
        req_para.put("page",1);
        String barcode = postApi(req_para, "lj_wuliu1",url);
        System.out.println(barcode);

    }

    public String postApi(Map<String,Object> req_para, String shardKey, String url) throws IOException {

        String resultStr = "";

        Map<String,Object> shardView =new HashMap<>();
        Map<String,Object> postParam =new HashMap<>();

        shardView.put("shardKeySchema",shardKey);
        shardView.put("shardKeyTable","");
        shardView.put("shardKeyTableNumber",0);

        postParam.put("req_para",req_para);
        postParam.put("shardView",shardView);

        Map<String,String> postParamMap =new HashMap<>();
        postParamMap.put("jsonStringParameter",JSONObject.toJSONString(postParam));
        postParamMap.put("time","1597299600");
        postParamMap.put("bizID","6665");
        postParamMap.put("userID","6665");
        postParamMap.put("groupID","0");
        postParamMap.put("app_id",clientId);
        postParamMap.put("sign",getSign(postParamMap, secretKey));
        postParamMap.put("jsonStringParameter",URLEncoder.encode(encoder.encode(postParamMap.get("jsonStringParameter").getBytes(StandardCharsets.UTF_8))));
        postParamMap.remove("app_id");
        String string = JSONObject.toJSONString(postParamMap);
        log.info(string);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        resultStr = httpUtil.requestFor(url, HttpMethod.POST,headers,postParamMap,String.class);

        return resultStr;
    }


    private String getSign(Map<String,String> kv, String secretKey){

        List<Map.Entry<String, String>> list = sortHashKeyAsc(kv);
        StringBuilder source = new StringBuilder(secretKey);
        list.forEach(entry ->{
            source.append(entry.getKey()).append(entry.getValue());
        });

        StringBuilder sign = new StringBuilder();
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.toString().getBytes(StandardCharsets.UTF_8));
            for (byte aByte : bytes) {

                String hex = Integer.toHexString(aByte & 255);
                if (hex.length() == 1) {

                    sign.append("0");
                }
                sign.append(hex.toUpperCase());
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sign.toString();
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

    @Test
    public void syncOrderToFactory() throws IOException {
        TenantContext.setTenantNo("100");
        FactoryOrderAddCmd addCmd = new FactoryOrderAddCmd();
        addCmd.setFactoryEnum(FactoryEnum.KDS_FK);
        FeiKuaiOrderAddCmd feiKuaiOrderAddCmd = new FeiKuaiOrderAddCmd();
        addCmd.setFeiKuaiOrderAddCmd(feiKuaiOrderAddCmd);
        // 主订单数据组装
        FeiKuaiOrderAddCmd.Trade trade = new FeiKuaiOrderAddCmd.Trade();
        feiKuaiOrderAddCmd.setTrade(trade);

        trade.setTid("O1657075611252");
        trade.setReceiver_address("昌东工业园昌东西一路，南昌隆鑫制衣有限公司");
        trade.setReceiver_state("江西省");
        trade.setReceiver_city("南昌市");
        trade.setReceiver_district("青山湖区");
        trade.setReceiver_name("李亮亮");
        trade.setReceiver_mobile("17099190666");
        trade.setCreated((long) 1657046829);
        trade.setPay_time((long) 1657046829);
        // 获取配送方式代码
        trade.setCp_code("SF");
        trade.setBuyer_nick("");
        // 子订单数据组装
        List<FeiKuaiOrderAddCmd.Order> orders = new ArrayList<>();
        List<FeiKuaiOrderAddCmd.Barcode> barcodes = new ArrayList<>();
        feiKuaiOrderAddCmd.setOrder(orders);
        feiKuaiOrderAddCmd.setBarcode(barcodes);
        // 子订单信息
        FeiKuaiOrderAddCmd.Order order = new FeiKuaiOrderAddCmd.Order();
        orders.add(order);
        order.setPic_path("/customize/samsung/2019-7-6/404747/NdiCnf_1657075601077.png");
        String oid = "874116";
        order.setOid(oid);
        order.setNum(1);
        order.setPrice(new BigDecimal(10.01));
        order.setTitle("Galaxy Z Fold3" + " "+"主题图片");
        order.setSku_properties_name("晶透浮雕壳 - 黑边透明"+" "+"Galaxy Z Fold3");
        order.setSku_id("10392");
        // 配货信息
        FeiKuaiOrderAddCmd.Barcode barcode = new FeiKuaiOrderAddCmd.Barcode();
        barcodes.add(barcode);
        barcode.setOid(oid);
        barcode.setPic_path("/customize/samsung/2019-7-6/404747/jaTmW4_1657075601077.png");
        // 生产手机型号和材质关键字段
        barcode.setWms_goods_id(497746);
        barcode.setOuter_iid("黑色磨砂壳"+" "+"GalaxyA50");
        barcode.setOrder_sjbm("GalaxyA50"+"+"+0);
        String result = feiKuaiFactoryService.syncOrder(addCmd);

        System.out.println(result);

    }

    @Test
    public void cancelOrderToFactory() throws IOException {
        TenantContext.setTenantNo("100");
        cmdExe.cancelOrderToFactory(FactoryEnum.KDS_FK.name(),"O1657075611250");
    }

    @Test
    public void synchronizedLogisticsByOrderID() throws IOException {
        TenantContext.setTenantNo("100");
        cmdExe.synchronizedLogisticsByOrderID(FactoryEnum.KDS_FK.name(),"O1657702420409");
    }

}
