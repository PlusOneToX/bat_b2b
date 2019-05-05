package com.bat.dubboapi.order.order.dto.factory.duohong;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/6/26 9:55
 */
@Data
public class DuoHongOrderAddCmd implements Serializable {

    /**
     * 订单(Trade)json字段 注意不是字符串是对象<br/>
     */
    @JsonIgnore
    private List<Trade> trades;

    /**
     * 名称 json类型 是否必须 示例值 描述<br/>
     * tid string Y 20191009172519 交易编号(长度限制>=10)<br/>
     * shopName string Y 空白格 店铺名<br/>
     * buyerNick string Y 3rd_张三 买家昵称<br/>
     * receiverName string Y 张三 收件人姓名<br/>
     * receiverState string Y 广东省 包裹标识<br/>
     * receiverCity string Y 深圳市 收件人城市<br/>
     * receiverDistrict string Y 龙岗区 收件人区<br/>
     * receiverAddress string Y 广东省深圳市龙岗区xx地铁站18号 收件人地址<br/>
     * receiverMobile string Y 138xxxxxx 收件人手机<br/>
     * payTime string Y 2019-01-01 12:12:09 付款时间<br/>
     * buyerMessage string N 买家留言<br/>
     * sellerMemo string N 卖家备注<br/>
     * expressName string N 中通 快递名<br/>
     * receiverZip string N 收件人邮编<br/>
     * email2 string N 内部备注<br/>
     * tradeWeight string N 商品重量<br/>
     * totalFee string N 应付金额<br/>
     * payment string N 实付金额<br/>
     * postFee string N 邮费v wflowLog string N 内部旗帜<br/>
     * sellerNick string N 卖家昵称<br/>
     * sellerFlag string N 卖家旗帜<br/>
     * orders List[Order] Y 子订单json 字符串（json格式为 List[order]）注意不是字符串是对象<br/>
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Trade implements Serializable {

        private String tid;
        private String shopName;
        private String buyerNick;
        private String receiverName;
        private String receiverState;
        private String receiverCity;
        private String receiverDistrict;
        private String receiverAddress;
        private String receiverMobile;
        private String payTime;
        private String buyerMessage;
        private String sellerMemo;
        private String expressName;
        private String receiverZip;
        private String email2;
        private String tradeWeight;
        private String totalFee;
        private String payment;
        private String postFee;
        private String wflowLog;
        private String sellerNick;
        private String sellerFlag;
        private List<Order> orders;

        /**
         * 名称 json类型 是否必须 示例值 描述<br/>
         * tid string Y 20191009172519 交易编号(长度限制>=10)<br/>
         * oid string Y 9619 子订单编号(长度限制>=10)<br/>
         * outerSkuId string Y 510002-玻璃壳-OPPOA11-黑色[0]-BL010 二级编码<br/>
         * num string Y 2 购买数量<br/>
         * outerIid string N 一级编码<br/>
         * skuPropertiesName string N SKU规格<br/>
         * title string N 商品标题<br/>
         * picPath string N 商品图片的绝对路径<br/>
         * uploadPicPath string N 调图图片的绝对路径<br/>
         * numIid string N 商品ID<br/>
         * skuId string N 平台skuId<br/>
         * price string N 商品价格<br/>
         * totalFee string N 应付金额<br/>
         * payment string N 实付金额<br/>
         * discountFee string N 订单优惠金额<br/>
         */
        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Order implements Serializable {
            private String tid;
            private String oid;
            private String outerSkuId;
            private String num;
            private String outerIid;
            private String skuPropertiesName;
            private String title;
            private String picPath;
            private String uploadPicPath;
            private String numIid;
            private String skuId;
            private String price;
            private String totalFee;
            private String payment;
            private String discountFee;
        }
    }
}
