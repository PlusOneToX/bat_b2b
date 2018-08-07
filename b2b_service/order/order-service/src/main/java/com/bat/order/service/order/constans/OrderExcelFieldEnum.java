package com.bat.order.service.order.constans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bat.order.dao.order.co.OrderExcelCO;

public enum OrderExcelFieldEnum {

    CREATE_TIME("CREATE_TIME", "下单日期", "CREATE TIME", true, 1, "order_info"),

    ORDER_NO("ORDER_NO", "B2B订单号", "B2B ORDER NO", true, 2, "order_info"),

    ORDER_TYPE_ID("ORDER_TYPE_ID", "订单类型", "ORDER TYPE", true, 3, "order_info"),

    ORDER_STATUS("ORDER_STATUS", "订单状态", "ORDER STATUS", true, 4, "order_distributor_data"),

    DELIVER_STATUS("DELIVER_STATUS", "发货状态", "DELIVER STATUS", true, 5, "order_info"),

    CURRENCY_TYPE("CURRENCY_TYPE", "订单币种", "CURRENCY TYPE", true, 6, "order_distributor_data"),

    CURRENCY_RATES("CURRENT_RATES", "订单汇率", "CURRENCY RATES", true, 7, "order_distributor_data"),

    PAY_STATUS("PAY_STATUS", "付款状态", "PAY STATUS", true, 8, "order_distributor_data"),

    PAY_WAY("PAY_WAY", "支付方式", "PAY WAY", true, 9, "order_distributor_data"),

    REMARK("REMARK", "订单备注", "ORDER REMARK", true, 10, "order_distributor_data"),

    ORDER_ERP_NO("ORDER_ERP_NO", "ERP订单号", "ERP ORDER NO", true, 11, "order_extend_data"),

    ORDER_FACTORY_NO("ORDER_FACTORY_NO", "工厂订单号", "FACTORY ORDER NO", true, 12, "order_extend_data"),

    ORDER_THIRDPARTY_NO("ORDER_THIRDPARTY_NO", "第三方订单号", "THIRD ORDER NO", true, 13, "order_extend_data"),

    ERP_OUTBOUND_NO("deliver_erp_no ERP_OUTBOUND_NO", "ERP出库单号", "ERP OUTBOUND NO", true, 14, "order_deliver_bill"),

    ORDER_DELIVER_BILL_ID("ID ORDER_DELIVER_BILL_ID", "B2B发货单号", "B2B ORDER DELIVERY BILL NO", true, 15,
        "order_deliver_bill"),

    DISTRIBUTOR_ID("DISTRIBUTOR_ID", "分销商ID(B2B编码)", "DISTRIBUTOR ID", true, 16, "order_distributor_data"),
    // 分销商上线再改回来ERP参数
    DISTRIBUTOR_ERP_NO("distributor_id DISTRIBUTOR_ERP_ID", "分销商ID(B2B客户编码)", "DISTRIBUTOR B2B ID", true, 17,
        "order_distributor_data"),

    DISTRIBUTOR_NAME("DISTRIBUTOR_NAME", "客户名称", "DISTRIBUTOR NAME", true, 18, "order_distributor_data"),

    DEPARTMENT_NAME("sales_id DEPARTMENT_NAME", "部门名称", "DEPARTMENT NAME", true, 19, "order_info"),

    SALES_NAME("SALES_NAME", "销售员", "SALE_NAME", true, 20, "order_info"),

    RECEIVER("user_name RECEIVER", "收货人", "RECEIVER", true, 21, "order_delivery"),

    RECEIVE_ADDRESS(
        "CONCAT(order_delivery.province_name,order_delivery.city_name,order_delivery.district_name,order_delivery.address) RECEIVE_ADDRESS",
        "收货地址", "RECEIVE ADDRESS", true, 22, "order_delivery"),

    CONTACT_MOBILE("mobile CONTACT_MOBILE", "联系电话", "CONTACT MOBILE", true, 23, "order_delivery"),

    DISTRIBUTION_NAME("DISTRIBUTION_NAME", "配送方式", "DISTRIBUTION NAME", true, 24, "order_deliver_bill"),

    LOGISTICS_NO("LOGISTICS_NO", "物流单号", "LOGISTICS NO", true, 25, "order_deliver_bill"),

    ITEM_CODE("ITEM_CODE", "货品编码", "ITEM CODE", false, 26, "order_goods"),

    ITEM_NAME("ITEM_NAME", "货品名称", "ITEM NAME", false, 27, "order_goods"),

    ITEM_COUNT("ITEM_COUNT", "销售数量", "ITEM COUNT", false, 28, "order_goods"),

    DELIVER_COUNT("DELIVER_COUNT", "已发货数", "DELIVER COUNT", false, 29, "order_goods"),

    MATERIAL_NAME("MATERIAL_NAME", "材质", "MATERIAL NAME", false, 30, "order_goods_diy"),

    MODEL_NAME("MODEL_NAME", "型号", "DELIVER COUNT", false, 31, "order_goods_diy"),

    PICTURE_ID("PICTURE_ID", "图片编码", "PICTURE ID", false, 31, "order_goods_diy"),

    SALE_PRICE("SALE_PRICE", "价格（含税单价）", "SALE PRICE", false, 32, "order_goods_distributor_cost"),

    ACTUAL_PRICE("ACTUAL_PRICE", "折后价（折后含税单价）", "ACTUAL PRICE", false, 33, "order_goods_distributor_cost"),

    SALE_PRICE_SUM("SALE_PRICE SALE_PRICE_SUM", "总额（折前含税总额）", "SALE PRICE SUM", false, 34,
        "order_goods_distributor_cost"),

    ACTUAL_PRICE_SUM("ACTUAL_PRICE ACTUAL_PRICE_SUM", "总额（折后含税总额）", "ACTUAL PRICE SUM", false, 35,
        "order_goods_distributor_cost"),

    USER_ACTUAL_PRICE("ACTUAL_PRICE user_actual_price", "C端下单总额（实付金额）", "USER ACTUAL PRICE SUM", false, 36,
        "order_goods_customer_cost"),

    PROMOTION_ID(
        "IFNULL(order_goods_distributor_cost.goods_promotion_id,order_goods_distributor_cost.order_promotion_id) PROMOTION_ID",
        "促销活动编码", "PROMOTION ID", false, 37, "order_goods_distributor_cost"),

    SHOP_NAME("SHOP_NAME", "门店名称", "SHOP NAME", true, 38, "order_customer_data"),

    SHOP_CODE("SHOP_CODE", "门店编码", "SHOP CODE", true, 39, "order_customer_data"),

    RETAIL_PRICE("RETAIL_PRICE", "零售价", "RETAIL PRICE", false, 40, "goods_item_scale_price")

    ;

    /**
     * 前端传过来的字段
     */
    private String field;

    /**
     * 英文
     */
    private String en;

    /**
     * 中文
     */
    private String zh;

    /**
     * 是否合并 True 合并 false不合并
     */
    private Boolean mergeFlag;

    /**
     * 排序
     */
    private Integer sequence;

    private String table;

    OrderExcelFieldEnum(String field, String en, String zh, Boolean mergeFlag, Integer sequence, String table) {
        this.field = field;
        this.en = en;
        this.zh = zh;
        this.mergeFlag = mergeFlag;
        this.sequence = sequence;
        this.table = table;
    }

    /**
     * 计算相同订单的个数 orderId肯定不为空
     * 
     * @param orderExcelCOList
     */
    public static void calculateItemTypeCount(List<OrderExcelCO> orderExcelCOList) {
        Integer startIndex = 0;
        Integer startOrderId = orderExcelCOList.get(0).getOrderId();
        for (int x = 1; x < orderExcelCOList.size(); x++) {
            if (startOrderId - orderExcelCOList.get(x).getOrderId() != 0) {
                // 订单改变
                Integer count = x - startIndex;
                // 设置数量
                orderExcelCOList.get(startIndex).setOrderLength(count);
                startIndex = x;
                startOrderId = orderExcelCOList.get(x).getOrderId();
            }
        }
        if (orderExcelCOList.size() > 1 && orderExcelCOList.get(orderExcelCOList.size() - 1).getOrderId()
            - orderExcelCOList.get(orderExcelCOList.size() - 2).getOrderId() == 0) {
            // 处理最后全都是一个单号
            orderExcelCOList.get(startIndex).setOrderLength(orderExcelCOList.size() - startIndex);
        }
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public Boolean getMergeFlag() {
        return mergeFlag;
    }

    public void setMergeFlag(Boolean mergeFlag) {
        this.mergeFlag = mergeFlag;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    /**
     * 判断字段是否含有表格的字段
     * 
     * @param fieldList
     * @param table
     * @return
     */
    public static Boolean existByTable(List<String> fieldList, String table) {
        if (fieldList == null || fieldList.size() == 0) {
            return false;
        }
        OrderExcelFieldEnum[] values = OrderExcelFieldEnum.values();
        for (int x = 0; x < values.length; x++) {
            OrderExcelFieldEnum orderExcelFieldEnum = values[x];
            String fieldName = String.valueOf(orderExcelFieldEnum);
            if (fieldList.contains(fieldName) && orderExcelFieldEnum.table.equals(table)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 组装查询sql
     * 
     * @param orderExcelSqlQry
     *            返回的对象（前面sql和后面sql）
     * @param fieldList
     *            查询的字段列表
     * @param table
     *            表名
     * @param joinParam
     *            lefe join on 后面的参数（基于order_info来匹配）
     * @param appendFlag
     *            是否lefe join true是（针对查询条件肯定要进行left join的）、false（不、条件查询不需要、但查询字段有这个table的字段、这个值在方法会改为true）
     */
    public static void queryTableSql(OrderExcelSqlQry orderExcelSqlQry, List<String> fieldList, String table,
        String joinParam, Boolean appendFlag) {
        OrderExcelFieldEnum[] values = OrderExcelFieldEnum.values();
        String frontStr = orderExcelSqlQry.getFrontStr();
        String endSql = orderExcelSqlQry.getEndSql();
        for (int x = 0; x < values.length; x++) {
            OrderExcelFieldEnum fieldEnum = values[x];
            String s = fieldEnum.toString();
            if (table.equals(fieldEnum.table) && fieldList.contains(s)) {
                if (fieldEnum.field.toLowerCase().startsWith("ifnull")
                    || fieldEnum.field.toLowerCase().contains("concat(")) {
                    // 不加表名
                    frontStr = frontStr + " " + fieldEnum.field.toLowerCase() + ",";
                } else {
                    frontStr = frontStr + " " + fieldEnum.table + "." + fieldEnum.field.toLowerCase() + ",";
                }

                appendFlag = true;
            }
        }
        if (StringUtils.isBlank(endSql)) {
            // 第一次才有
            endSql = " FROM " + table + " " + table;
        } else if (appendFlag) {
            // 跟order_info做一个基准
            endSql = endSql + " LEFT JOIN " + table + " " + table + " ON " + joinParam;
        }
        orderExcelSqlQry.setFrontStr(frontStr);
        orderExcelSqlQry.setEndSql(endSql);
    }

    public static Object getValueByField(OrderExcelFieldEnum orderExcelFieldEnum, OrderExcelCO orderExcelCO) {

        if (orderExcelFieldEnum == null) {
            return null;
        }
        String param = orderExcelFieldEnum.field;

        if (param.contains(" ")) {
            // 别名
            String[] str = param.split(" ");
            param = str[1];
        }
        // 小写
        param = param.toLowerCase();
        //
        Class clazz = orderExcelCO.getClass();
        try {
            if (param.contains("_")) {
                // 包含下划线、首字母要大写
                String[] arr = param.split("_");
                param = arr[0];
                for (int x = 1; x < arr.length; x++) {
                    param = param + captureName(arr[x]);
                }
            }
            Field declaredField = clazz.getDeclaredField(param);
            declaredField.setAccessible(true);
            Object o = declaredField.get(orderExcelCO);
            return o;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将字符串的首字母转大写
     * 
     * @param str
     *            需要转换的字符串
     * @return
     */
    private static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    public static void main(String[] args) {
        /*  OrderExcelSqlQry orderExcelSqlQry = new OrderExcelSqlQry();
        List<String> fieldList = new ArrayList<>();
        fieldList.add("ORDER_ERP_NO");
        //fieldList.add("CREATE_TIME");
        queryTableSql(orderExcelSqlQry,fieldList, "order_info","",false);
        queryTableSql(orderExcelSqlQry,fieldList, "order_extend_data","order_extend_data.order_id = order_info.id",false);
        queryTableSql(orderExcelSqlQry,fieldList, "order_distributor_data","order_distributor_data.order_id = order_info.id and order_distributor_data.erp_flag=1",false);
        orderExcelSqlQry.setFrontStr(orderExcelSqlQry.getFrontStr().substring(0,orderExcelSqlQry.getFrontStr().length()-1));
        System.out.println(JSON.toJSONString(orderExcelSqlQry));
        System.out.println(orderExcelSqlQry.getFrontStr()+orderExcelSqlQry.getEndSql());
        System.out.println(orderExcelSqlQry.getFullSql());
        */
        List<OrderExcelCO> excelCOList = new ArrayList<>();
        OrderExcelCO orderExcelCO = new OrderExcelCO();
        orderExcelCO.setOrderId(1);
        excelCOList.add(orderExcelCO);
        OrderExcelCO orderExcelCO2 = new OrderExcelCO();
        orderExcelCO2.setOrderId(1);
        // excelCOList.add(orderExcelCO2);
        OrderExcelCO orderExcelCO3 = new OrderExcelCO();
        orderExcelCO3.setOrderId(1);
        // excelCOList.add(orderExcelCO3);
        OrderExcelCO orderExcelCO4 = new OrderExcelCO();
        orderExcelCO4.setOrderId(2);
        // excelCOList.add(orderExcelCO4);
        OrderExcelCO orderExcelCO5 = new OrderExcelCO();
        orderExcelCO5.setOrderId(2);
        // excelCOList.add(orderExcelCO5);
        calculateItemTypeCount(excelCOList);
        System.out.println(JSON.toJSONString(excelCOList));
    }
}
