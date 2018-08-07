package com.bat.financial.Tenant;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 模块名称: 租户实体类 模块描述: 用于存储租户信息
 * 
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/05/21 10:14
 */
@Data
public class Tenant {
    private String tenantNo;
    private Map<String, String> configMap = new HashMap<>();

    public Tenant() {}

    public Tenant(String tenantNo) {
        this.tenantNo = tenantNo;
    }
}
