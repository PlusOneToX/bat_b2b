package com.bat.system.service.globalsetting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.globalsetting.FactorySettingService;
import com.bat.system.api.globalsetting.dto.FactorySettingUpdateCmd;
import com.bat.system.api.globalsetting.dto.data.FactorySettingDelayPushesDTO;
import com.bat.system.api.globalsetting.dto.data.FactorySettingOrderInvalidDTO;
import com.bat.system.service.globalsetting.executor.FactorySettingCmdExc;
import com.bat.system.service.globalsetting.executor.FactorySettingQryExc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.bat.dubboapi.thirdparty.xxljob.api.XxlJobServiceRpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/8 15:59
 */
@Service
@Slf4j
public class FactorySettingServiceImpl implements FactorySettingService {

    @Resource
    private FactorySettingQryExc factorySettingQryExc;

    @Resource
    private FactorySettingCmdExc factorySettingCmdExc;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private XxlJobServiceRpc xxlJobServiceRpc;

    @Override
    public List<FactorySettingDelayPushesDTO> listFactorySettingDelayPushes() {
        return factorySettingQryExc.listFactorySettingDelayPushes();
    }

    @Override
    public boolean updateDelayPushesPushTimeByTask() {
        return factorySettingCmdExc.updateDelayPushesPushTimeByTask();
    }

    @Override
    public List<FactorySettingOrderInvalidDTO> listFactorySettingOrderInvalid() {
        return factorySettingQryExc.listFactorySettingOrderInvalid();
    }

    @Override
    public FactorySettingOrderInvalidDTO getFactorySettingOrderInvalidByOrderSource(int orderSource) {
        return factorySettingQryExc.getFactorySettingOrderInvalidByOrderSource(orderSource);
    }

    @Override
    public void updateFactorySetting(FactorySettingUpdateCmd cmd) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(!CollectionUtils.isEmpty(cmd.getDelayPushes())){
            List<String> collect = cmd.getDelayPushes().stream().map(factorySettingDelayPushesUpdateCmd -> {
                Date pushTime = factorySettingDelayPushesUpdateCmd.getPushTime();
                return format.format(pushTime);
            }).collect(Collectors.toList());
            xxlJobServiceRpc.saveSyncFactoryXxlJob(collect);
        }
        factorySettingCmdExc.updateFactorySetting(cmd);
    }

}
