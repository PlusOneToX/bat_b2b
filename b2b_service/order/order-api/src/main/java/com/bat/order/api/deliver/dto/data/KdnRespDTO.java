package com.bat.order.api.deliver.dto.data;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/9 13:55
 */
@NoArgsConstructor
@Data
public class KdnRespDTO {

    /**
     * StateEx : 301 LogisticCode : 73152346708212 ShipperCode : ZTO Traces : [{"Action":"1","AcceptStation":"【深圳市】
     * 【龙岗坂田】（0755-28908542、0755-28900074、18938946839） 的 瞬必达云仓（18025841477） 已揽收","AcceptTime":"2018-04-11
     * 19:46:09","Location":"深圳市"},{"Action":"2","AcceptStation":"【深圳市】 快件离开 【龙岗坂田】 已发往 【广州中心】","AcceptTime":"2018-04-11
     * 23:33:07","Location":"深圳市"},{"Action":"2","AcceptStation":"【深圳市】 快件已经到达 【深圳中心】","AcceptTime":"2018-04-12
     * 04:24:57","Location":"深圳市"},{"Action":"2","AcceptStation":"【深圳市】 快件离开 【深圳中心】 已发往 【广州中心】","AcceptTime":"2018-04-12
     * 04:27:59","Location":"深圳市"},{"Action":"2","AcceptStation":"【广州市】 快件已经到达 【广州中心】","AcceptTime":"2018-04-12
     * 08:08:51","Location":"广州市"},{"Action":"2","AcceptStation":"【广州市】 快件离开 【广州中心】 已发往 【梧州】","AcceptTime":"2018-04-13
     * 14:31:06","Location":"广州市"},{"Action":"2","AcceptStation":"【梧州市】 快件离开 【梧州】 已发往 【广州中心】","AcceptTime":"2018-04-13
     * 14:32:00","Location":"梧州市"},{"Action":"2","AcceptStation":"【广州市】 快件已经到达 【广州东圃】","AcceptTime":"2018-04-14
     * 06:56:57","Location":"广州市"},{"Action":"202","AcceptStation":"【广州市】 【广州东圃】 的胡正球（13533561242） 正在第1次派件,
     * 请保持电话畅通,并耐心等待（95720为中通快递员外呼专属号码，请放心接听）","AcceptTime":"2018-04-14
     * 07:29:40","Location":"广州市"},{"Action":"304","AcceptStation":"【广州市】
     * 快件已被【菜鸟的广州石溪永宁大街15号店】代收，如有问题请电联（9519666），感谢使用中通快递，期待再次为您服务！","AcceptTime":"2018-04-14
     * 10:32:58","Location":"广州市"},{"Action":"301","AcceptStation":"【广州市】 您的快递已签收,
     * 签收人在【菜鸟的广州石溪永宁大街15号店】领取。如有疑问请电联:（13533561242）, 投诉电话:（9519666）, 您的快递已经妥投。风里来雨里去, 只为客官您满意。上有老下有小,
     * 赏个好评好不好？【请在评价快递员处帮忙点亮五颗星星哦~】","AcceptTime":"2018-04-14 19:03:53","Location":"广州市"}] State : 3 EBusinessID :
     * 1622925 Success : true Location : 广州市
     */

    @JsonProperty("StateEx")
    private String StateEx;
    @JsonProperty("ShipperCode")
    private String ShipperCode;
    @JsonProperty("Traces")
    private List<TracesDTO> Traces;
    @JsonProperty("NextCity")
    private String NextCity;
    @JsonProperty("EBusinessID")
    private String EBusinessID;
    @JsonProperty("DeliveryMan")
    private String DeliveryMan;
    @JsonProperty("DeliveryManTel")
    private String DeliveryManTel;
    @JsonProperty("Success")
    private Boolean Success;
    @JsonProperty("Station")
    private String Station;
    @JsonProperty("LogisticCode")
    private String LogisticCode;
    @JsonProperty("StationTel")
    private String StationTel;
    @JsonProperty("State")
    private String State;
    @JsonProperty("Location")
    private String Location;
    @JsonProperty("StationAdd")
    private String StationAdd;
    @JsonProperty("Reason")
    private String Reason;

    @NoArgsConstructor
    @Data
    public static class TracesDTO {
        /**
         * Action : 1 AcceptStation : 【深圳市】 【龙岗坂田】（0755-28908542、0755-28900074、18938946839） 的 瞬必达云仓（18025841477） 已揽收
         * AcceptTime : 2018-04-11 19:46:09 Location : 深圳市
         */

        @JsonProperty("Action")
        private String Action;
        @JsonProperty("AcceptStation")
        private String AcceptStation;
        @JsonProperty("AcceptTime")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private Date AcceptTime;
        @JsonProperty("Location")
        private String Location;
    }
}
