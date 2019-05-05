package com.bat.dubboapi.order.order.dto.factory;

import com.bat.dubboapi.order.order.dto.factory.duohong.DuoHongOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.feikuai.FeiKuaiOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.haixing.HaiXingOrderAddCmd;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.dubboapi.order.order.dto.factory.maike.MaikeOrderAddCmd;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/6/26 9:53
 */
@Data
public class FactoryOrderAddCmd implements Serializable {

    private FactoryEnum factoryEnum;

    private MaikeOrderAddCmd maikeOrderAddCmd;

    private DuoHongOrderAddCmd duoHongOrderAddCmd;

    private HaiXingOrderAddCmd haiXingOrderAddCmd;

    private FeiKuaiOrderAddCmd feiKuaiOrderAddCmd;
}
