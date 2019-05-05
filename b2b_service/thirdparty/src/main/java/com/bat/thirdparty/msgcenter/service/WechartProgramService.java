/*
package com.bat.thirdparty.msgcenter.service;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

*/
/**
 * 微信小程序
 *//*

@Service
public class WechartProgramService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechartProgramService.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private OrderRepository orderMapper;

    @Autowired
    private UserPlatformRepository userPlatformRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    */
/**
     * 获取调用凭据并存入redis
     *//*

    public String getTokenAndSetRedis() {
        String url = WechatConstant.ProgramTokenUrl.replace("APPID", WechatConfig.appId)
                .replace("APPSECRET", WechatConfig.SECRET);
        LOGGER.info("小程序全局唯一调用凭据url:" + url);
        //进行接口调用
        String result = HttpUtil.getJson(url);
        LOGGER.info("微信返回（小程序）:" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String token = jsonObject.getString("access_token");
        if (StringUtils.isBlank(token)) {
            LOGGER.info("获取不到微信（小程序）数据");
            throw new WechatException(WechatErrorEnum.QueryUserMessageERROR.getMsg());
        }
        LOGGER.info("微信（小程序）返回的token", token);
        //将token存入缓存中
        redisCache.add(WechatConstant.ProgramTokenRedisKey, token, WechatConstant.ProgramTokenRedisKeyTimeOut);
        return token;
    }

    */
/**
     * 获取调用凭据
     * @return
     *//*

    public String getToken() {
        String token = (String) redisTemplate.opsForValue().get(WechatConstant.ProgramTokenRedisKey);
        if (StringUtils.isBlank(token)) {
            token = getTokenAndSetRedis();
        }
        return token;
    }

    */
/**
     * 发送下单订阅通知
     *//*

    public void sendAddOrderMsg(List<Long> orderIds) {
        LOGGER.info("开始发送微信通知:{}",orderIds);
        if (orderIds == null || orderIds.size() == 0) {
            return;
        }
        for (Long orderId : orderIds) {
            try {
                OrderInfo orderInfo = orderMapper.findById(orderId).orElse(null);
                LOGGER.info("开始发送微信通知,当前订单信息为:{}",JSONObject.toJSONString(orderInfo));
                if (orderInfo == null) {
                    continue;
                }
                if (orderInfo.getOrderSource() != EXCHANGE_WECHAT_SOURCE) {
                    continue;
                }
                //从订单信息获取openId
                String openId= getOpenId(orderInfo);
                LOGGER.info("获取用户openid:{}",openId);
                if(StringUtils.isBlank(openId)){
                    continue;
                }
                SubscribeSend subscribeSend=new SubscribeSend();
                //设置openid
                subscribeSend.setTouser(openId);
                //设置模板
                subscribeSend.setTemplate_id(ProgramConfig.templateIdSuccess);
                if(StringUtils.isNotBlank(ProgramConfig.miniprogramState)){
                    //设置跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
                    subscribeSend.setMiniprogram_state(ProgramConfig.miniprogramState);
                }
                //设置模板内容
                subscribeSend.setData(getTemplateIdSuccessData(orderInfo));
                //设置跳转页面
                subscribeSend.setPage(ProgramConfig.webOrderDetailUrl);
                //发送消息
                WechatUtil.programMessageSend(subscribeSend,getToken());
                Thread.sleep(10000);
            } catch (Exception e) {
                LOGGER.error("推送下单订阅消息失败:{}",e);
            }
        }
    }
    //获取下单成功模板内容
    private Object getTemplateIdSuccessData(OrderInfo orderInfo) {
        List<OrderItem> orderItems=orderItemRepository.findByOrderId(orderInfo.getId());
        StringBuilder goodsName=new StringBuilder();
        if(orderItems.size()>0){
            for(OrderItem orderItem:orderItems){
                goodsName.append(orderItem.getMaterialName()+orderItem.getModelName()+",");
            }
        }
        TemplateSuccess templateSuccess = new TemplateSuccess();
        //设置订单编号
        TemplateValue templateValue = new TemplateValue();
        templateValue.setValue(orderInfo.getOrderNo());
        if(templateValue.getValue().length()>=32){
            templateValue.setValue(templateValue.getValue().substring(0,32));
        }
        templateSuccess.setCharacter_string1(templateValue);

        //设置商品名称
        templateValue = new TemplateValue();
        templateValue.setValue(goodsName.deleteCharAt(goodsName.length()-1).toString());
        if(templateValue.getValue().length()>=20){
            templateValue.setValue(templateValue.getValue().substring(0,20));
        }
        templateSuccess.setThing3(templateValue);

        //设置下单时间
        templateValue = new TemplateValue();
        templateValue.setValue(DataUtils.convertString(new Date(orderInfo.getCreateTime())));
        templateSuccess.setDate2(templateValue);

        //设置订单状态
        templateValue = new TemplateValue();
        templateValue.setValue(OrderConstant.getStatusStr(orderInfo.getStatus()));
        templateSuccess.setPhrase9(templateValue);

        //设置温馨提示
        templateValue = new TemplateValue();
        templateValue.setValue("您的订单已提交至工厂制作，请耐心等待！");
        templateSuccess.setThing5(templateValue);
        return templateSuccess;
    }


    public void sendDeliverMsg(OrderInfo orderInfo, OrderDelivery orderDelivery) {
        LOGGER.info("开始发送发货消息");
        if (orderInfo == null || orderInfo == null) {
            return;
        }
        //从订单信息获取openId
        String openId = getOpenId(orderInfo);
        LOGGER.info("获取到openId:{}",openId);
        if (StringUtils.isBlank(openId)) {
          return;
        }
        SubscribeSend subscribeSend = new SubscribeSend();
        //设置openid
        subscribeSend.setTouser(openId);
        //设置模板
        subscribeSend.setTemplate_id(ProgramConfig.templateIdDeliverGoods);
        if (StringUtils.isNotBlank(ProgramConfig.miniprogramState)) {
            //设置跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
            subscribeSend.setMiniprogram_state(ProgramConfig.miniprogramState);
        }
        //设置模板内容
        subscribeSend.setData(templateDeliverData(orderInfo,orderDelivery));
        //设置跳转页面
        subscribeSend.setPage(ProgramConfig.webOrderDetailUrl);
        //发送消息
        WechatUtil.programMessageSend(subscribeSend, getToken());
    }

    //获取订单发货模板内容
    private Object templateDeliverData(OrderInfo orderInfo,OrderDelivery orderDelivery) {
        List<OrderItem> orderItems=orderItemRepository.findByOrderId(orderInfo.getId());
        StringBuilder goodsName=new StringBuilder();
        if(orderItems.size()>0){
            for(OrderItem orderItem:orderItems){
                goodsName.append(orderItem.getMaterialName()+orderItem.getModelName()+",");
            }
        }
        TemplateDeliverGoods templateDeliverGoods = new TemplateDeliverGoods();
        //设置订单编号
        TemplateValue templateValue = new TemplateValue();
        templateValue.setValue(orderInfo.getOrderNo());
        if(templateValue.getValue().length()>=32){
            templateValue.setValue(templateValue.getValue().substring(0,32));
        }
        templateDeliverGoods.setCharacter_string1(templateValue);

        //设置商品名称
        templateValue = new TemplateValue();
        templateValue.setValue(goodsName.deleteCharAt(goodsName.length()-1).toString());
        if(templateValue.getValue().length()>=20){
            templateValue.setValue(templateValue.getValue().substring(0,20));
        }
        templateDeliverGoods.setThing2(templateValue);

        //设置快递单号
        templateValue = new TemplateValue();
        templateValue.setValue(orderDelivery.getExpressOn());
        if(templateValue.getValue()==null){
            templateValue.setValue("");
        }
        if(templateValue.getValue().length()>=32){
            templateValue.setValue(templateValue.getValue().substring(0,32));
        }
        templateDeliverGoods.setCharacter_string4(templateValue);

        //设置快递公司
        templateValue = new TemplateValue();
        templateValue.setValue(orderDelivery.getExpressName());
        if(templateValue.getValue()==null){
            templateValue.setValue("");
        }
        if(templateValue.getValue().length()>=10){
            templateValue.setValue(templateValue.getValue().substring(0,10));
        }
        templateDeliverGoods.setName3(templateValue);

        //设置温馨提示
        templateValue = new TemplateValue();
        templateValue.setValue("您的订单已发货，点击查看物流详情");
        templateDeliverGoods.setThing7(templateValue);
        return templateDeliverGoods;
    }

    */
/**
     * 通过订单信息获取用户openid
     * @param orderInfo
     * @return
     *//*

    private String getOpenId(OrderInfo orderInfo) {
        List<UserPlatform> userPlatforms = userPlatformRepository.findByUserIdAndPlatform(orderInfo.getUser(), PlatformConstant.PlatformProgramExchange);
        if (userPlatforms.size() == 0) {
            return null;
        }
        String openId = null;
        for (UserPlatform userPlatform : userPlatforms) {
            if (StringUtils.isNotBlank(userPlatform.getOpenId())) {
                openId = userPlatform.getOpenId();
                break;
            }
        }
        return openId;
    }

}
*/
