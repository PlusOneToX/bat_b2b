
package com.bat.dubboapi.order.order.dto.factory.maike;



import lombok.Data;

import java.io.Serializable;

@Data
public class MaikeOrderAddCmd  implements Serializable {

    private static final long serialVersionUID = -2066939978310012960L;
    /**
     * 关联订单编号
     */
    private String order_number;


    /**
     * 下单人
     */
    private String creator;
    

    /**
     * 是否自动过审核，默认为 0，设置为 1 时需要设置 delivery_method_id 字段
     */
    private Integer auto_pass;

    /**
     * 云创主体公司名称
     */
    private String sender;

    /**
     * 真实客户名称
     */
    private String real_username;


    /**
     * 配送参数
     */
    private MaikeDeliveryInfo delivery_info;
    

    /**
     * 收货信息
     */
    private MaikeShipperAddressCmd shipping_address_info;


    /**
     * 结算方式
     */
    private MaikeSettlementInfo settlement_info;


    /**
     * 订单信息
     */
    private MaikeOrderInfo order_info;

    /**
     * B2B的配送方式ID、传参给麦客会设置为空
     */
    private Integer logisticsId;

    /**
     * 是否海外 true 是 false否
     */
    private Boolean overSeasFlag=false;

    /**
     * 工厂编码
     */
    private String manufactors;

    /**
     *密钥
     */

    public  String secret;


    /**
     * 公司 id
     */
    public  Integer company_id;
    

    

}