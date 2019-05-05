package com.bat.thirdparty.quanyi.dao.dataobject;

import lombok.Data;

import java.util.Date;
@Data
public class ThirdQuanyiDO {

    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private String thirdPhone;

    private String thirdCode;

    private String thirdSkuNo;

    private String thirdUserRemark;

    private Integer exchangeId;

    private String exchangeName;

    private Integer exchangeCodeId;

    private String plainCode;

    private String secretCode;

    private Integer materialId;

    private String materialName;

    private Integer customerId;

    private String customerNo;

    private Integer orderId;

    private Short dispatchFlag;

    private Short signInFlag;

    private Short destroyFlag;

    private Short cancelFlag;

    private Date createTime;

    private Date destroyTime;

    private Date cancelTime;

    private Date updateTime;

    private String thirdOrderDetail;

    private String lastModifyRequest;

}