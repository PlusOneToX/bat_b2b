package com.bat.dubboapi.system.check.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/9/2 17:19
 */
public class CheckConfigDetailRpcDTO implements Serializable {
    /**
     * 是否开启
     */
    private Short openFlag;
    /**
     * 审批类型 审批单类型 1.商品管理上下架审批（在用） 2. 分销商编辑审批（在用） 3. 仓库库存调整审批 4. 仓库库存预留审批 5. 订单价格审批 6. 订单对账折扣审批 7促销活动新增审批 8促销活动编辑审批 9
     * 10商品等级变动（在用） 11 12 13 促销活动审批（在用） 14 优惠券（在用） 15 拼团（在用）
     */
    private Short ext;
    /**
     * 审批配置
     */
    private List<CheckConfigRpcDTO> checkConfigs;

    public Short getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Short openFlag) {
        this.openFlag = openFlag;
    }

    public Short getExt() {
        return ext;
    }

    public void setExt(Short ext) {
        this.ext = ext;
    }

    public List<CheckConfigRpcDTO> getCheckConfigs() {
        return checkConfigs;
    }

    public void setCheckConfigs(List<CheckConfigRpcDTO> checkConfigs) {
        this.checkConfigs = checkConfigs;
    }
}
