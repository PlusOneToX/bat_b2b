package com.bat.distributor.dao.distributor.dataobject;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DistributorDO {
    private Integer id;

    private String name;

    private String password;

    private Short companyType;

    private String companyName;

    private Short applyType;

    private Short applyStatus;

    private Short profileStatus;

    private Date applyTime;

    private Date checkTime;

    private Short freezeStatus;

    private Date freezeTime;

    private Integer treeNode;

    private Date createTime;

    private Date updateTime;

    List<DistributorOneScalePriceDO> scalePriceDOS;

    List<DistributorOneScalePriceDO> addScalePriceDOS;
    List<DistributorOneScalePriceDO> updateScalePriceDOS;
    List<DistributorOneScalePriceDO> deleteScalePriceDOS;

    DistributorBusinessDO businessDO;

    List<DistributorSalesAreaDO> salesAreaDOS;

    DistributorAddressDO afterAddressDO;

    List<DistributorContactsDO> addContacts;
    List<DistributorContactsDO> updateContacts;
    List<DistributorContactsDO> deleteContacts;

    DistributorExtendDataDO afterExtendDataDO;

    DistributorExtendDataDO beforeExtendDataDO;

    DistributorFinancialDO afterFinancialDO;

    List<DistributorSpecialGoodsDO> addSpecialGoodsDOS;
    List<DistributorSpecialGoodsDO> updateSpecialGoodsDOS;
    List<DistributorSpecialGoodsDO> deleteSpecialGoodsDOS;

}