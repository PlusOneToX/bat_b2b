package com.bat.thirdparty.vmall.request;

import lombok.Data;

import java.util.List;

@Data
public class BopOrderStatusUpdateRequest {

    private List<shipmentUpdate> shipmentList;

    @Data
    public static class shipmentUpdate {
        /**
         * vmall订单编号
         */
        private String orderCode;

        /**
         * vmall状态 20-已发货,30-已签收,31-拒签
         */
        private String status;

        /**
         * 物流运单号
         */
        private String expressNo;

        /**
         * vmall物流公司编码：DEPPON:德邦,OTHER:其他,KYE:跨越速运,HOAU:华宇,LBEX:龙邦,ZTKY:中铁,CXCOD:传喜,FKD:飞康达,BESTEX:百事快递,HENGLU:恒路物流,
         * JIAJI:佳吉快运,KERRY:嘉里大通,LHTEX:联昊通,SZML:明亮物流,SA:圣安物流,SHENGHUI:盛辉物流,WINSHINE:万象物流,XBWL:新邦物流,XFEXPRESS:信丰物流,
         * YCG:远成物流,SF:顺丰快递,YTO:圆通快递,ZTO:中通快递,SFHK:香港順豐,STO:申通快递,ZT:国内自提,ZPTW:宅配,BEST:百事汇通,ANE:安能,CB:晟邦,HUANGMAJIA:黄马甲,KJ:快捷,QUANFENG:全峰,SUNING:苏宁,TTK:天天,UC:优速,WX:万象,YUNDA:韵达,ZMKM:芝麻开门,DEKUN:德坤物流,SR:速尔快递,DEPPONKD:德邦快递,LSKD:龙赛物流,
         * QBWL:群邦物流,XJWL:新杰物流,YMDD:壹米滴答,JCWL:加成物流,YZBG:邮政包裹
         */
        private String logisticsCompanyCode;

        /**
         * 发货单号
         */
        private String shipmentNo;

    }
}
