package com.bat.dubboapi.order.order.dto.factory.feikuai;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 飞快订单参数
 */
@Data
public class FeiKuaiOrderAddCmd implements Serializable {
    /**
     * 主订单信息
     */
    private Trade trade;
    /**
     * 子订单列表
     */
    private List<Order> order;
    /**
     * 配货列表
     */
    private List<Barcode> barcode;
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Trade implements Serializable {
        /**
         * 平台标识:49（与需要和飞快最终确认）
         */
        private Short platform;
        /**
         * 用户ID:2540（与需要和飞快最终确认）
         */
        private Integer user_id;
        /**
         * 卖家昵称:151305966 或者 魅力旗舰店
         */
        private String seller_nick;
        /**
         * 买家昵称:相视一笑
         */
        private String buyer_nick;
        /**
         * 主订单号:200323-338805720151853,同一个系统内需要唯一
         */
        private String tid;
        /**
         * 收货人:李四
         */
        private String receiver_name;
        /**
         * 收货人的所在省份:广东省
         */
        private String receiver_state;
        /**
         * 收货城市:深圳市
         */
        private String receiver_city;
        /**
         * 收货所在区:龙华区
         */
        private String receiver_district;
        /**
         * 收货人的详细地址:观澜街道
         */
        private String receiver_address;
        /**
         * 收货人的邮编:632000
         */
        private String receiver_zip;
        /**
         * 收货人手机:13645456789
         */
        private String receiver_mobile;
        /**
         * 快递公司代码:ZTO
         */
        private String cp_code;
        /**
         * 卖家备注:换成钢化膜
         */
        private String remark;
        /**
         * 买家留言:不要礼品
         */
        private String buyer_remark;
        /**
         * 交易创建时间(当前时间戳):1591863387
         */
        private Long created;
        /**
         * 付款时间:1591863387
         */
        private Long pay_time;


    }
    /**
     * 子订单信息
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Order implements Serializable {
        /**
         * 商品标题
         */
        private String title;
        /**
         * 子订单号:94059942689485985017595, 同一个系统内需要唯一
         */
        private String oid;
        /**
         * 子订单价格:13.5
         */
        private BigDecimal price;
        /**
         * 商品图片地址:https://***.com/03-15/52b35ba6-6a8d-41c3-8276-e0ecb492e496.jpg
         */
        private String pic_path;
        /**
         * 购买数量:2
         */
        private Integer num;
        /**
         * 购买SKU:规格1:Nm004年轻宇航员;规格2:P10青春
         */
        private String sku_properties_name;
        /**
         * SKU ID:485985017595
         */
        private String sku_id;
    }

    /**
     * 配货信息
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Barcode implements Serializable {
        /**
         * 平台标识:49（与需要和飞快最终确认）
         */
        private Short platform;
        /**
         * 供货商ID:2785（与需要和飞快最终确认）
         */
        private Integer to_user_id;
        /**
         * 商家编码（材料 型号 别名）以空格隔开
         */
        private String outer_iid;
        /**
         * 定制图片地址:https://***.com/03-15/52b35ba6-6a8d-41c3-8276-e0ecb492e496.jpg
         */
        private String pic_path;
        /**
         * 礼物（多个礼品空格隔开）:挂绳(2) 指环支架(2) 膜-201(2)
         */
        private String gift;
        /**
         * 飞快定制中的手机型号ID :901(生产手机型号和材质关键字段)
         */
        private Integer wms_goods_id;
        /**
         * 商家编码:GX7290+P374 或者GX7290-P374，且第一段的型号编码由来源于飞快系统
         */
        private String order_sjbm;
        /**
         * 手机壳膜和手机壳礼品:[{"id":"5","quantity":1},{"id":"22","quantity":1}]
         */
        private String gift_detail = "[]";
        /**
         * 子订单号
         */
        private String oid;
    }

}
