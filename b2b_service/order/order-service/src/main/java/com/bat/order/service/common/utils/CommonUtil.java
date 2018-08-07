package com.bat.order.service.common.utils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import com.bat.order.service.common.enumtype.DeliverStatus;
import com.bat.order.service.common.enumtype.OrderStatus;
import com.bat.order.service.common.enumtype.PayStatus;
import com.bat.order.service.common.enumtype.PayWay;
import com.bat.order.service.order.constans.FrontOrderStatus;

public class CommonUtil {

    private static final char[] HEX_DIGITS =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 获取指定网站的日期时间
     *
     * @param webUrl
     * @return
     * @author SHANHY
     * @date 2015年11月27日
     */
    public static long getWebsiteDatetime(String webUrl) {
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            return ld;
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

    /**
     * 判断字符串是否是数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * sha1加密
     * 
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String sha1(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(data.getBytes("UTF-8"));
        byte[] messageDigest = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte message : messageDigest) {
            String shaHex = Integer.toHexString(message & 0xFF);
            if (shaHex.length() < 2)
                hexString.append(0);
            hexString.append(shaHex);
        }
        return hexString.toString().toUpperCase();
    }

    public static boolean isUTF8(String key) {
        try {
            key.getBytes("utf-8");
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    public static String getCharset(String str) {
        try {
            byte[] head = str.getBytes("ISO-8859-1");
            String code = "";
            code = "ISO-8859-1";
            if (head[0] == -1 && head[1] == -2)
                code = "UTF-16";
            if (head[0] == -2 && head[1] == -1)
                code = "Unicode";
            if (head[0] == -17 && head[1] == -69 && head[2] == -65)
                code = "UTF-8";
            return code;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期格式化
     * 
     * @param time
     * @param format
     * @return
     */
    private static String formatDate(Long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * 获取订单编码
     *
     * @return
     */
    public static String getOrderNo() {
        StringBuilder sb = new StringBuilder();
        sb.append("O");
        sb.append(String.valueOf(System.currentTimeMillis()));
        return sb.toString();
    }

    /**
     * 
     * 前台订单转换
     *
     * 计算 并转换状态
     *
     * 0全部 订单全部 发货全部
     *
     * 1待确认 orderStatus1 payStatus3 订单待确认 已付款 （区间结算 线下付款可以未付款）
     *
     * 2待发货 orderStatus2 deliverStatus1 deliverStatus2 订单已确认 发货待发货 发货出库中
     *
     * 3部分发货 orderStatus2 deliverStatus3 订单已确认 发货部分发货
     *
     * 4待收货 orderStatus2 deliverStatus4 订单已确认 发货已发货
     *
     * 5已关闭 orderStatus4 订单已取消
     *
     * 6已完成 orderStatus5 订单已完成
     *
     * 7待付款 orderStatus1 payStatus3 订单待确认 待付款 （区间结算 线下付款不算）
     */
    public static Short convertUserOrderStatus(Short orderStatus, Short payStatus, Short deliverStatus, Short payWay) {
        if (orderStatus.equals(OrderStatus.PENDING.getValue())) {
            boolean payFlag =
                payStatus.equals(PayStatus.UNPAID.getValue()) || payStatus.equals(PayStatus.PARTIAL_PAYMENT.getValue());
            boolean payWayFlag = !payWay.equals(PayWay.Period_settlement.getValue())
                && !payWay.equals(PayWay.Offline_payment.getValue());
            if (payFlag && payWayFlag) {
                return FrontOrderStatus.WAIT_PAYMENT;
            }
            boolean payFlag1 = payStatus.equals(PayStatus.PAID.getValue());
            boolean payWayFlag1 =
                payWay.equals(PayWay.Period_settlement.getValue()) || payWay.equals(PayWay.Offline_payment.getValue());
            if (payFlag1 || payWayFlag1) {
                return FrontOrderStatus.WAIT_CONFIRM;
            }
        } else {
            return convertCommonOrderStatus(orderStatus, payStatus, deliverStatus, payWay);
        }
        return null;
    }

    /**
     * 
     * 后台订单转换 柔性订单转换
     * 
     * 计算 并转换状态
     *
     * 0全部 订单全部 发货全部
     *
     * 1待确认 orderStatus1 payStatus3 订单待确认 已付款 （区间结算 线下付款可以未付款）
     *
     * 2待发货 orderStatus2 deliverStatus1 deliverStatus2 订单已确认 发货待发货 发货出库中
     *
     * 3部分发货 orderStatus2 deliverStatus3 订单已确认 发货部分发货
     *
     * 4待收货 orderStatus2 deliverStatus4 订单已确认 发货已发货
     *
     * 5已关闭 orderStatus4 订单已取消
     *
     * 6已完成 orderStatus5 订单已完成
     *
     * 7待付款 orderStatus1 payStatus3 订单待确认 待付款 （区间结算 线下付款不算）
     */
    public static Short convertAdminOrderStatus(Short orderStatus, Short payStatus, Short deliverStatus, Short payWay) {
        if (orderStatus.equals(OrderStatus.PENDING.getValue())) {
            return FrontOrderStatus.WAIT_CONFIRM;
        } else {
            return convertCommonOrderStatus(orderStatus, payStatus, deliverStatus, payWay);
        }
    }

    private static Short convertCommonOrderStatus(Short orderStatus, Short payStatus, Short deliverStatus,
        Short payWay) {
        if (orderStatus.equals(OrderStatus.CONFIRMED.getValue())) {
            if (deliverStatus.equals(DeliverStatus.Undelivered.getValue())) {
                return FrontOrderStatus.WAIT_SEND_GOODS;
            } else if (deliverStatus.equals(DeliverStatus.PartDelivered.getValue())) {
                return FrontOrderStatus.WAIT_PART_SEND_GOODS;
            } else if (deliverStatus.equals(DeliverStatus.Delivered.getValue())) {
                return FrontOrderStatus.WAIT_RECEIVE_GOODS;
            } else if (deliverStatus.equals(DeliverStatus.Delivering.getValue())) {
                // 出库中
                return FrontOrderStatus.OUTBOUNDING;
            }
        } else if (orderStatus.equals(OrderStatus.CANCELLED.getValue())) {
            return FrontOrderStatus.CLOSED;
        } else if (orderStatus.equals(OrderStatus.COMPLETED.getValue())) {
            return FrontOrderStatus.FINISHED;
        } else if (orderStatus.equals(OrderStatus.REDUSE.getValue())) {
            return FrontOrderStatus.REJECT;
        }
        return null;
    }

    /**
     * 比较两个实体属性值，返回一个map以有差异的属性名为key，value为一个list分别存obj1,obj2此属性名的值
     *
     * @param objB
     *            进行属性比较的对象1
     * @param objA
     *            进行属性比较的对象2
     * @return 属性差异比较结果map
     */
    public static Map<String, List<Object>> compareFields(Object objB, Object objA) throws Exception {
        Map<String, List<Object>> map = new HashMap<String, List<Object>>();
        Field[] ignoreList = objB.getClass().getDeclaredFields();
        // 只有两个对象都是同一类型的才有可比性
        if (objB.getClass() == objA.getClass()) {
            Class clazz = objB.getClass();
            // 获取object的属性描述
            PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
            // 这里就是所有的属性了
            for (PropertyDescriptor pd : pds) {
                // 属性名
                String name = pd.getName();
                // get方法
                Method readMethod = pd.getReadMethod();
                // 在obj1上调用get方法等同于获得obj1的属性值
                Object objBefore = readMethod.invoke(objB);
                // 在obj2上调用get方法等同于获得obj2的属性值
                Object objAfter = readMethod.invoke(objA);
                if (objBefore == null && objAfter == null) {
                    continue;
                } else if (objBefore == null && objAfter != null) {
                    List<Object> list = new ArrayList<Object>();
                    list.add(objBefore);
                    list.add(objAfter);
                    map.put(name, list);
                    continue;
                }
                // 比较这两个值是否相等,不等则放入map
                if (!objBefore.equals(objAfter)) {
                    List<Object> list = new ArrayList<Object>();
                    list.add(objBefore);
                    list.add(objAfter);
                    map.put(name, list);
                }
            }
        }
        return map;
    }

}
