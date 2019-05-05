package com.bat.thirdparty.msgcenter.service.executor;

import com.bat.thirdparty.msgcenter.api.dto.template.*;
import com.bat.thirdparty.utils.CommonUtil;
import com.bat.thirdparty.msgcenter.api.dto.template.*;
import com.bat.thirdparty.msgcenter.common.MsgCenterConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 模板内容获取
 */
@Component
public class TemplateExe {

    /**
     * 订单状态通知模板内容
     * @param orderNo
     * @return
     */
    public TemplateOrderStatus getTemplateOrderStatus(String orderNo) {
        TemplateOrderStatus templateOrderStatus = new TemplateOrderStatus();
        //订单编号
        templateOrderStatus.setCharacter_string1(getTemplateValue(orderNo, 32));
        //订单状态
        templateOrderStatus.setPhrase2(getTemplateValue(MsgCenterConstant.ORDER_EXAMINE_STATUS, 5));
        //温馨提示
        templateOrderStatus.setThing11(getTemplateValue(MsgCenterConstant.ORDER_STATUS_TIP, 20));
        return templateOrderStatus;
    }

    /**
     * 订单发货提醒模板内容
     * @param orderNo
     * @param distributionName
     * @param logisticsNo
     * @param deliverTime
     * @return
     */
    public TemplateOrderDelivery getTemplateOrderDelivery(String orderNo, String distributionName, String logisticsNo, Date deliverTime) {
        TemplateOrderDelivery templateOrderDelivery = new TemplateOrderDelivery();
        //订单编号
        templateOrderDelivery.setCharacter_string1(getTemplateValue(orderNo, 32));
        //快递公司
        templateOrderDelivery.setName3(getTemplateValue(distributionName, 10));
        if(StringUtils.isBlank(logisticsNo)){
            logisticsNo="未知的物流单号";
        }
        //快递单号
        templateOrderDelivery.setCharacter_string4(getTemplateValue(logisticsNo, 32));
        //发货时间
        templateOrderDelivery.setDate10(getTemplateValue(CommonUtil.dateToStr(deliverTime), null));
        //温馨提示
        templateOrderDelivery.setThing7(getTemplateValue(MsgCenterConstant.ORDER_DELIVERY_TIP, 20));
        return templateOrderDelivery;
    }

    /**
     * 订单未支付提醒模板内容
     * @param orderNo
     * @param amount
     * @param createTime
     * @return
     */
    public TemplateOrderUnpaid getTemplateOrderUnpaid(String orderNo,String amount,Date createTime) {
        TemplateOrderUnpaid templateOrderUnpaid = new TemplateOrderUnpaid();
        //订单编号
        templateOrderUnpaid.setCharacter_string5(getTemplateValue(orderNo, 32));
        //订单金额
        templateOrderUnpaid.setAmount7(getTemplateValue(amount, null));
        //下单时间
        templateOrderUnpaid.setDate6(getTemplateValue(CommonUtil.dateToStr(createTime), null));
        //温馨提醒
        templateOrderUnpaid.setThing4(getTemplateValue(MsgCenterConstant.ORDER_UNPAID_TIP, 20));
        return templateOrderUnpaid;
    }

    /**
     * 分销商审核通知模板内容
     * @param applyTime
     * @param distributorName
     * @return
     */
    public TemplateDistributorExamine getTemplateDistributorExamine(Date applyTime, String distributorName) {
        TemplateDistributorExamine templateDistributorExamine = new TemplateDistributorExamine();
        //申请时间
        templateDistributorExamine.setDate1(getTemplateValue(CommonUtil.dateToStr(applyTime), null));
        //分销商名称
        templateDistributorExamine.setThing5(getTemplateValue(distributorName, 20));
        //备注信息
        templateDistributorExamine.setThing4(getTemplateValue(MsgCenterConstant.DISTRIBUTOR_EXAMINE_TIP, 20));
        return templateDistributorExamine;
    }

    /**
     * 新订单提醒模板内容
     * @param orderNo
     * @param distributorName
     * @param amount
     * @return
     */
    public TemplateNewOrder getTemplateNewOrder(String orderNo, String distributorName, String amount) {
        TemplateNewOrder templateNewOrder = new TemplateNewOrder();
        //订单号
        templateNewOrder.setCharacter_string2(getTemplateValue(orderNo, 32));
        //下单用户
        templateNewOrder.setName1(getTemplateValue(distributorName, 10));
        //订单金额
        templateNewOrder.setAmount3(getTemplateValue(amount, null));
        return templateNewOrder;
    }

    /**
     * 获取值的实体
     * @param value 值
     * @param length 限制的长度
     * @return
     */
    private TemplateValue getTemplateValue(String value, Integer length) {
        TemplateValue templateValue = new TemplateValue();
        templateValue.setValue(value);
        if (length != null) {
            if (templateValue.getValue().length() >= length) {
                templateValue.setValue(templateValue.getValue().substring(0, length));
            }
        }
        return templateValue;
    }

}
