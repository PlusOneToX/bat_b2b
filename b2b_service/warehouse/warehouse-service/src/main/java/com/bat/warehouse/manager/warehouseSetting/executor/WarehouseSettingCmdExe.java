package com.bat.warehouse.manager.warehouseSetting.executor;

import com.alibaba.fastjson.JSON;
import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseSettingErrorCode;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantRpcDTO;
import com.bat.dubboapi.thirdparty.xxljob.api.XxlJobServiceRpc;
import com.bat.dubboapi.thirdparty.xxljob.dto.XxlJobRpcCmd;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.base.AdminResponse;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.setting.dto.WarehouseSettingCmd;
import com.bat.warehouse.dao.warehouseSetting.WarehouseSettingDOMapper;
import com.bat.warehouse.dao.warehouseSetting.dataobject.WarehouseSettingDO;
import com.bat.warehouse.manager.common.config.ConfigQry;
import com.bat.warehouse.manager.common.constant.WarehouseSettingConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class WarehouseSettingCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseSettingCmdExe.class);

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private XxlJobServiceRpc xxlJobServiceRpc;

    @Resource
    private ConfigQry configQry;

    @Autowired
    private WarehouseSettingDOMapper warehouseSettingDOMapper;

    public Response createWarehouseSetting(WarehouseSettingCmd warehouseSettingCmd, AdminResponse currentUser) {
        WarehouseSettingDO warehouseSettingDO = warehouseSettingDOMapper.findByType(warehouseSettingCmd.getType());
        if (warehouseSettingDO != null) {
            throw new WarehouseException(WarehouseSettingErrorCode.SETTING_TYPE_EXIST);
        }
        warehouseSettingDO = new WarehouseSettingDO();
        warehouseSettingDO.setType(warehouseSettingCmd.getType());
        setAdminMsg(currentUser, warehouseSettingDO);
        warehouseSettingDO.setValue(warehouseSettingCmd.getValue());
        warehouseSettingDOMapper.insert(warehouseSettingDO);
        setXxlJob(warehouseSettingDO);
        return Response.buildSuccess();
    }

    private void setXxlJob(WarehouseSettingDO warehouseSettingDO) {
        if (!WarehouseSettingConstant.WAREHOUSE_SETTING_TYPE_SYNC_ERP_STOCK.equals(warehouseSettingDO.getType())) {
            return;
        }
        String tenantNo = TenantContext.getTenantNo();
        if (StringUtils.isBlank(tenantNo)) {
            LOGGER.info("租户id为空");
            return;
        }
        PlatformTenantRpcDTO tenantConfig = configQry.getTenantConfig();
        String companyName = tenantConfig.getCompanyName();
        XxlJobRpcCmd xxlJobRpcCmd = new XxlJobRpcCmd();
        xxlJobRpcCmd.setJobGroup(2);
        xxlJobRpcCmd.setJobDesc("同步ERP库存到B2B(" + companyName + ")");
        xxlJobRpcCmd.setAuthor("warehouse-service");
        // 每小时执行
        xxlJobRpcCmd.setScheduleType(XxlJobRpcCmd.SCHEDULE_TYPE_FIX_RATE);
        xxlJobRpcCmd.setScheduleConf(warehouseSettingDO.getValue() * 60 + "");
        xxlJobRpcCmd.setExecutorHandler("warehouseErpSyncB2BJobHandler");
        xxlJobRpcCmd.setOverwrite(true);
        List<XxlJobRpcCmd> list = new ArrayList<>();
        list.add(xxlJobRpcCmd);
        LOGGER.info("设置库存同步参数,{}", JSON.toJSONString(list));
        com.bat.dubboapi.thirdparty.common.Response response = xxlJobServiceRpc.xxlJobAdd(list);
        LOGGER.info("设置库存同步参数,响应{}", JSON.toJSONString(response));
    }

    public static void main(String[] args) {
        Integer time = 120;
        // 得到小时
        Integer hours = time / 60;
        // 得到分钟
        Integer minutes = time - 60 * hours;
        System.out.println(hours + "==" + minutes);
    }

    private void setAdminMsg(AdminResponse currentUser, WarehouseSettingDO warehouseSettingDO) {
        if (warehouseSettingDO != null) {
            warehouseSettingDO.setCreateTime(new Date());
            warehouseSettingDO.setCreateUserId(currentUser.getId());
            warehouseSettingDO.setCreateUserName(currentUser.getUserName());
        }
        warehouseSettingDO.setUpdateTime(new Date());
        warehouseSettingDO.setCreateUserName(currentUser.getUserName());
        warehouseSettingDO.setUpdateUserId(currentUser.getId());
    }

    public Response updateWarehouseSetting(WarehouseSettingCmd warehouseSettingCmd, AdminResponse currentUser) {
        WarehouseSettingDO warehouseSettingDO = warehouseSettingDOMapper.findByType(warehouseSettingCmd.getType());
        if (warehouseSettingDO == null) {
            warehouseSettingDO = new WarehouseSettingDO();
            warehouseSettingDO.setValue(warehouseSettingCmd.getValue());
            warehouseSettingDO.setType(warehouseSettingCmd.getType());
            setAdminMsg(currentUser, warehouseSettingDO);
            warehouseSettingDOMapper.insert(warehouseSettingDO);
            setXxlJob(warehouseSettingDO);
        } else {
            warehouseSettingDO.setValue(warehouseSettingCmd.getValue());
            setAdminMsg(currentUser, warehouseSettingDO);
            warehouseSettingDOMapper.updateByPrimaryKey(warehouseSettingDO);
            setXxlJob(warehouseSettingDO);
        }
        return Response.buildSuccess();
    }

    private WarehouseSettingDO findById(Integer id) {
        if (id == null) {
            throw new WarehouseException(WarehouseCommonErrorCode.ID_NULL);
        }
        WarehouseSettingDO warehouseSettingDO = warehouseSettingDOMapper.selectByPrimaryKey(id);
        if (warehouseSettingDO == null) {
            throw new WarehouseException(WarehouseCommonErrorCode.ID_ERROR);
        }
        return warehouseSettingDO;
    }

    public void create(WarehouseSettingDO warehouseSettingDO) {
        warehouseSettingDOMapper.insert(warehouseSettingDO);
    }
}
