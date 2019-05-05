//package com.bat.thirdparty.factory.haixing.service;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.bat.dubboapi.flexible.common.Response;
//import com.bat.dubboapi.flexible.common.ResponseBaseBean;
//import com.bat.dubboapi.flexible.picture.api.PictureServiceRpc;
//import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
//import com.bat.dubboapi.order.order.dto.factory.haixing.HaiXingOrderAddCmd;
//import com.bat.dubboapi.thirdparty.oss.ThirdPartySystemOssServiceRpc;
//import com.bat.order.api.common.exception.OrderException;
//import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
//import com.bat.order.dao.order.dataobject.OrderGoodsDO;
//import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
//import com.bat.order.dao.order.dataobject.OrderInfoDO;
//import com.bat.order.service.common.CommonRpcQryExe;
//import com.bat.order.service.deliver.executor.OrderDeliveryQryExe;
//import com.bat.order.service.order.executor.OrderGoodsDiyQryExe;
//import com.bat.order.service.order.executor.OrderGoodsQryExe;
//import com.bat.order.service.order.executor.OrderInfoQryExe;
//import com.bat.thirdparty.factory.haixing.bak.*;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.util.DigestUtils;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.Resource;
//import java.io.*;
//import java.net.URLEncoder;
//import java.util.*;
///**
// * 订单同步到海星（脚本类）
// */
//@Service
//public class HaixingServiceImpl implements HaixingServiceI {
//
//    private final static Logger logger = LoggerFactory.getLogger(HaixingServiceImpl.class);
//
//
//    private String haiXingAppID="974000719614125888";
//
//
//    private String haiXingSecret="ea8018ed85eacfc18a46a74d39771050";
//
//
//    private String haiXingUrl="https://diyopen.i-smartnet.com/open/api.do";
//
//    @Autowired
//    private OrderInfoQryExe orderInfoQryExe;
//
//    @Autowired
//    private OrderDeliveryQryExe orderDeliveryQryExe;
//
//    @Autowired
//    private OrderGoodsDiyQryExe orderGoodsDiyQryExe;
//
//    @Autowired
//    private OrderGoodsQryExe orderGoodsQryExe;
//
//    @Resource
//    private CommonRpcQryExe rpcQryExe;
//
//    @Resource
//    private PictureServiceRpc pictureServiceRpc;
//
//    @DubboReference(check = false,timeout = 10000)
//    private ThirdPartySystemOssServiceRpc thirdPartySystemOssServiceRpc;
//
//    public String getSign(Map<String, Object> params, String secret) throws UnsupportedEncodingException {
//        System.out.println("haixing签名前数据:"+params.toString());
//        String sign = "";
//        StringBuilder sb = new StringBuilder();
//        //step1：先对请求参数排序
//        Set<String> keyset = params.keySet();
//        TreeSet<String> sortSet = new TreeSet<String>();
//        sortSet.addAll(keyset);
//        Iterator<String> it = sortSet.iterator();
//        while (it.hasNext()) {
//            String value = "";
//            String key = it.next();
//            Object o = params.get(key);
//            String encode = URLEncoder.encode(o.toString(), "UTF-8");
//            sb.append(key).append("=").append(encode).append("&");
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        sb.append(secret);
//        return DigestUtils.md5DigestAsHex(sb.toString().getBytes()).toUpperCase();
//    }
//
//
//
//
//    @Override
//    public String synOrder(HaiXingRequest request) throws Exception {
//        boolean hasSyn=false;
//        boolean suc=false;
//        String fMsg="";
//        String haixingOrderNo = "";
//        try {
//            OrderInfoDO obj = orderInfoQryExe.getById(request.getOrderId());
//            if (obj == null) {
//                logger.info("订单不存在");
//                return null;
//            }
//            OrderDeliveryDO orderDeliveryDO = orderDeliveryQryExe.getByOrderId(obj.getId());
//            logger.info("请求到的发货数据:{}", JSONObject.toJSONString(orderDeliveryDO));
//            List<OrderGoodsDiyDO> list = orderGoodsDiyQryExe.listByOrderId(obj.getId());
//            ResponseBaseBean responseBaseBean = new ResponseBaseBean();
//            String dateStr = Long.toString(System.currentTimeMillis() / 1000L);
//            HaiXingOrderAddCmd haiXingOrder = new HaiXingOrderAddCmd();
//            haiXingOrder.setAddress(orderDeliveryDO.getProvinceName() + orderDeliveryDO.getCityName() + orderDeliveryDO.getAddress());
//            haiXingOrder.setProvince(orderDeliveryDO.getProvinceName());
//            haiXingOrder.setCity(orderDeliveryDO.getCityName());
//            haiXingOrder.setCounty(orderDeliveryDO.getDistrictName());
//            haiXingOrder.setContacts(orderDeliveryDO.getUserName());
//            haiXingOrder.setMobileNumber(orderDeliveryDO.getMobile());
//            haiXingOrder.setSellerOrderNo(obj.getId() + "");
//            haiXingOrder.setStoreCode("00683");
//            haiXingOrder.setMemo(request.getPb());
//            JSONObject orderCustomizationJson = new JSONObject();
//            // orderCustomizationJson.put("customer", obj.getDistributorId());
//            haiXingOrder.setOrderCustomization(orderCustomizationJson.toString());
//            ArrayList<HaiXingGood> haiXingGoods = new ArrayList<>();
//            for (int i = 0; i < list.size(); i++) {
//                OrderGoodsDiyDO orderGoods = list.get(i);
//                if (orderGoods.getModelId() == null || orderGoods.getMaterialId() == null || StringUtils.isBlank(orderGoods.getPreviewImage()) || StringUtils.isBlank(orderGoods.getGenerateImage())) {
//                    ///   List<OrderDetail> orderDetails = orderDetailRepository.findByOrderGoodsId(orderGoods.getId());
//                    //   orderGoods.setModelId(orderGoods.getModelId());
//                    //   orderGoods.setMaterialId(orderGoods.getMaterialId());
//                    //   orderGoods.setImage(orderDetails.get(0).getImage());
//                    //   orderGoods.setGenerateImage(orderDetails.get(0).getGenerateImage());
//                }
//                // GoodsCustomInfo material = goodsCustomInfoRepository.findByModelIdAndMaterialId(orderGoods.getModelId(), orderGoods.getMaterialId());
//                OrderGoodsDO orderGoodsDO = orderGoodsQryExe.getById(orderGoods.getOrderGoodsId());
//                HaiXingGood haiXingGood = new HaiXingGood();
//                haiXingGood.setGoodsImg(orderGoods.getPreviewImage());//效果图
//                haiXingGood.setSkuCode(request.getSku());
//                haiXingGood.setGoodsNum(orderGoodsDO.getItemCount().longValue());
//
//                File file = new File(PdfConversion.getFile(orderGoods.getGenerateImage()));
//                logger.info("开始将PDF转为JPG");
//                //将文件转成jpg并得到地址
//                String jpgFileStr = PdfConversion.toJpg(file);
//                //删除旧文件
//                file.delete();
//                //得到jpg文件
//                file = new File(jpgFileStr);
//                if (file.exists()) {
//                    String filePath = "";
//                    for (int j = 0; j < 10; j++) {
//                        try {
//                            //将文件推送到oos
//                            StringBuffer buffer = new StringBuffer();
//                            buffer.append(orderGoods.getId()).append("/").append(orderGoods.getOrderId()).append("/").append(orderGoods.getPictureId()).append("/").append(System.currentTimeMillis()).append("/").append(file.getName());
//                            InputStream inputStream = new FileInputStream(file);
//                            byte[] bytes = null;
//                            try {
//                                bytes = IOUtils.toByteArray(inputStream);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                throw OrderException.buildException("出现异常：" + e.getMessage());
//                            }
//                            filePath = thirdPartySystemOssServiceRpc.uploadExtendStream(buffer.toString(), "label/", bytes);
//                            logger.info("上传回来标签{}", filePath);
//                            if (StringUtils.isNotBlank(filePath)) {
//                                break;
//                            }
//                            //线程暂停5秒后重新上传
//                            Thread.sleep(5000);
//                        } catch (Exception e) {
//                            logger.error("文件上传失败:{}", e.getMessage());
//                            if (j >= 9) {
//                                throw new RuntimeException("文件上传失败、图片id" + orderGoods.getPictureId());
//                            }
//                        }
//                    }
//                    //将本地文件进行移除
//                    file.delete();
//                    haiXingGood.setDiyFileUrl(filePath);//生产图
//                }
//                haiXingGood.setGoodsCustomization(goodsCustomization(orderGoods));//定制数据
//                haiXingGoods.add(haiXingGood);
//            }
//            //商品数据数组
//            haiXingOrder.setGoods(JSON.toJSONString(haiXingGoods));
//            HashMap<String, Object> signMap = new HashMap<String, Object>();
//            signMap.put("appId", haiXingAppID);
//            signMap.put("timestamp", dateStr);
//            signMap.put("version", "1");
//            signMap.put("method", "createOrder");
//            Map map = CommonUtils.objectToMap(haiXingOrder);
//            signMap.putAll(map);
//            String sign = getSign(signMap, haiXingSecret);
//            signMap.put("sign", sign);
//            String jsonString = JSON.toJSONString(signMap);
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
//            headers.setContentType(type);
//            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//            HttpEntity entity = new HttpEntity(jsonString, headers);
//            logger.info("海星同步订单最终请求参数:{}", jsonString);
//            String result = restTemplate.postForObject(haiXingUrl, entity, String.class);
//            hasSyn=true;
//            JSONObject jsonObj = JSON.parseObject(result);
//            logger.info("海星返回:{}" + jsonObj);
//            Boolean success = (Boolean) jsonObj.get("success");
//            //如果订单创建成功
//            if (success) {
//                suc=true;
//                Map mapResult = jsonObj.toJavaObject(Map.class);
//                Map m = (Map) mapResult.get("result");
//                //得到海星订单号
//                String orderNo = m.get("orderNo").toString();
//                haixingOrderNo = orderNo;
//                String orderId = m.get("orderId").toString();
//                responseBaseBean.setData(orderNo);
//                //保存同步成功日志
//                //orderAdminService.addSystemOrderAdminLog(order.getId(), OrderConstant.OperateTypeFactoryOrder,"同步联辉王工厂成功：" + orderNo);
//                // order.setProductOrderNo(orderNo);
//                //更新订单
//                // orderDBManager.updateOrder(order.getId(), order.getProductOrderNo());
//                System.out.println("LHW" + orderNo);
//                HashMap<String, Object> mapSign = new HashMap<String, Object>();
//                mapSign.put("appId", haiXingAppID);
//                mapSign.put("timestamp", dateStr);
//                mapSign.put("version", "1");
//                mapSign.put("method", "prodOrder");
//                mapSign.put("orderId", orderId);
//                mapSign.put("sellerOrderNo", "");
//                String prodOrderSign = getSign(mapSign, haiXingSecret);
//                mapSign.put("sign", prodOrderSign);
//                String jsonStr = JSON.toJSONString(mapSign);
//                RestTemplate restTemplate1 = new RestTemplate();
//                HttpHeaders header = new HttpHeaders();
//                header.add("Accept", MediaType.APPLICATION_JSON.toString());
//                logger.info("海星开始发生产请求");
//                HttpEntity httpEntity = new HttpEntity(jsonStr, header);
//                try {
//                    String resp = restTemplate1.postForObject(haiXingUrl, httpEntity, String.class);
//                    logger.info("海星发生产返回:{}", resp);
//                    System.out.println("--[]----" + resp);
//                    JSONObject proJson = JSON.parseObject(resp);
//                    Boolean proJsonSuccess = (Boolean) proJson.get("success");
//                    if (proJsonSuccess) {
//                        System.out.println("---------haixing------" + proJson);
//                        //更新工厂订单发生产状态
//                        //orderDBManager.updateHaixingProduct(order.getId(), OrderConstant.HaixingProductYes);
//                        //订单设置已发生产状态
//                        // order.setIsHaixingProduct(OrderConstant.HaixingProductYes);
//                        //保存同步成功日志
//                        // orderAdminService.addSystemOrderAdminLog(order.getId(), OrderConstant.OperateTypeFactoryOrder,"同步联辉王工厂订单发生产成功：" + orderNo);
//                    } else {
//                        //如果订单发生产失败
//                        String msg = jsonObj.get("msg").toString();
//                        //保存同步成功日志
//                        // orderAdminService.addSystemOrderAdminLog(order.getId(), OrderConstant.OperateTypeFactoryOrder,"同步联辉王工厂订单发生产失败：" + msg);
//                        responseBaseBean.setCode(2000);
//                        responseBaseBean.setMsg(msg);
//                    }
//                } catch (Exception e) {
//                    System.out.println("同步生成出现异常：" + e.getMessage());
//                    responseBaseBean.setCode(2000);
//                    responseBaseBean.setMsg("同步生成出现异常：" + e.getMessage());
//                    //保存同步异常日志
//                    // orderAdminService.addSystemOrderAdminLog(order.getId(), OrderConstant.OperateTypeFactoryOrder,"同步生成出现异常：" + e.getMessage());
//                }
//            } else {
//                //如果订单创建失败
//                String msg = jsonObj.get("msg").toString();
//                fMsg=msg;
//                //  orderAdminService.addSystemOrderAdminLog(order.getId(), OrderConstant.OperateTypeFactoryOrder,"同步联辉王失败：" + msg);
//                responseBaseBean.setCode(2000);
//                responseBaseBean.setMsg(msg);
//                //  return responseBaseBean;
//            }
//            //return haixingOrderNo;
//            //  return responseBaseBean;
//        }catch (Exception e){
//          logger.info("同步海星出现异常:{}",e.getMessage());
//        }
//        if(!hasSyn){
//            logger.info("该订单未同步到海星:{}",request.getOrderId());
//        }
//        if(hasSyn&&!suc){
//            logger.info("同步后被拒绝的订单:{},返回信息:{}",request.getOrderId(),fMsg);
//        }
//        return haixingOrderNo;
//    }
//
//    /**
//     * 获取商品定制数据
//     * @return
//     * @param orderGoods
//     */
//    private String goodsCustomization(OrderGoodsDiyDO orderGoods) {
//        OrderGoodsDO orderGoodsDO= orderGoodsQryExe.getById(orderGoods.getOrderGoodsId());
//        JSONObject json = new JSONObject();
//        String barCode = orderGoodsDO.getBarCode();
//        if (barCode == null) {
//            barCode = "";
//        }
//
//        String itemCode = orderGoodsDO.getItemCode();
//        if (itemCode == null) {
//            itemCode = "";
//        }
//        json.put("条形码", barCode + "/" + itemCode);
//        json.put("货品中文名称", orderGoodsDO.getItemName());
//        /*GoodsItem goodsItem = goodsItemDBManager.findById(orderGoods.getItemId());
//        if (goodsItem == null) {
//            throw new CustomException("找不到货品信息");
//        }*/
//        String itemNameEn="";
//        if (StringUtils.isNotBlank(itemCode)) {
//            try {
//                itemNameEn = rpcQryExe.goodsItemByCode(itemCode).getItemNameEn();
//            } catch (Exception e) {
//                logger.info("获取货品信息出现异常:{}", e.getMessage());
//            }
//        }
//        if (itemNameEn == null) {
//            itemNameEn = "";
//        }
//        PictureDTORpcQry picture=null;
//        try {
//            Response<PictureDTORpcQry> response = pictureServiceRpc.getDTORpcById(orderGoods.getPictureId());
//            picture = response.getData();
//        }catch (Exception e){
//            logger.info("获取图片信息出现异常:{}", e.getMessage());
//        }
//        String pictureName="";
//        String pictureEnglishName="";
//        String pictureCode="";
//        if (picture != null) {
//            if(picture.getName()!=null){
//                pictureName= picture.getName();
//            }
//
//            if(picture.getEnglishName()!=null){
//                pictureEnglishName=picture.getEnglishName();
//            }
//            if(picture.getCode()!=null){
//                pictureCode=picture.getCode();
//            }
//        }
//        json.put("货品英文名称", itemNameEn);
//        json.put("图片中文名称", pictureName);
//        json.put("图片英文名称", pictureEnglishName);
//        json.put("图片ID", orderGoods.getPictureId() + "/" + pictureCode);
//        json.put("重量", orderGoodsDO.getWeight());
//        json.put("标签url", orderGoods.getLabelUrl());
//        return json.toJSONString();
//    }
//}
