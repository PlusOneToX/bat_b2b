package com.bat.thirdparty.msgcenter.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 传运单接口参数
 */
@Data
public class FollowWay {

    /**
     * 用户openid
     */
    @JSONField(name = "openid")
    private String openid;

    /**
     * 收件人手机号，部分运力需要用户手机号作为查单依据
     */
    @JSONField(name = "receiver_phone")
    private String receiverPhone;

    /**
     * 运单号
     */
    @JSONField(name = "waybill_id")
    private String waybillId;

    /**
     * 商品信息
     */
    @JSONField(name = "goods_info")
    private DetailList goodsInfo;

    /**
     * 交易单号（微信支付生成的交易单号，一般以420开头）
     */
    @JSONField(name = "trans_id")
    private String transId;

    /**
     * 点击落地页商品卡片跳转路径（建议为订单详情页path），不传默认跳转小程序首页。
     */
    @JSONField(name = "order_detail_path")
    private String orderDetailPath;

    @Data
    public static class DetailList {
        /**
         * 商品信息
         */
        @JSONField(name = "detail_list")
        private List<GoodsInfo> detailList;
    }

    @Data
    public static class GoodsInfo {
        /**
         * 商品名称，最多40汉字
         */
        @JSONField(name = "goods_name")
        private String goodsName;

        /**
         * 商品图片url
         */
        @JSONField(name = "goods_img_url")
        private String goodsImgUrl;

    }
}
