package com.bat.dubboapi.order.order.dto.factory;

import java.io.Serializable;

import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.dubboapi.order.order.dto.factory.duohong.DuoHongOrderQueCmd;
import com.bat.dubboapi.order.order.dto.factory.haixing.HaiXingOrderQueCmd;

import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/6/26 9:53
 */
@Data
public class FactoryOrderQueCmd implements Serializable {

    private FactoryEnum factoryEnum;

    private DuoHongOrderQueCmd duoHongOrderQueCmd;

    private HaiXingOrderQueCmd haiXingOrderQueCmd;

    private String orderNo;

    private String orderFactoryNo;

}
