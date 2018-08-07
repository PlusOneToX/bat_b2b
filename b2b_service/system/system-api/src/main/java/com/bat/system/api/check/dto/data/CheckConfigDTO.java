package com.bat.system.api.check.dto.data;

import java.util.List;

import com.bat.system.dao.check.dataobject.CheckConfigDO;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/15 15:58
 */
@Data
public class CheckConfigDTO {
    /**
     * 是否开启
     */
    private Short openFlag;
    /**
     * 审批类型
     */
    private Short ext;
    /**
     * 审批配置
     */
    private List<CheckConfigDO> checkConfigs;
}
