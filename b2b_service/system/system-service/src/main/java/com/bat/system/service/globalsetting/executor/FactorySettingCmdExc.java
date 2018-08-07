package com.bat.system.service.globalsetting.executor;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.globalsetting.dto.FactorySettingUpdateCmd;
import com.bat.system.dao.globalsetting.FactorySettingDelayPushMapper;
import com.bat.system.dao.globalsetting.FactorySettingOrderInvalidMapper;
import com.bat.system.dao.globalsetting.dataobject.FactorySettingDelayPushDO;
import com.bat.system.dao.globalsetting.dataobject.FactorySettingOrderInvalidDO;
import com.bat.system.service.globalsetting.convertor.FactorySettingConvertor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class FactorySettingCmdExc {

    @Resource
    private FactorySettingDelayPushMapper factorySettingDelayPushMapper;

    @Resource
    private FactorySettingOrderInvalidMapper factorySettingOrderInvalidMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean updateDelayPushesPushTimeByTask() {
        List<FactorySettingDelayPushDO> factorySettingDelayPushDOS = factorySettingDelayPushMapper.listAll();
        if (CollectionUtils.isNotEmpty(factorySettingDelayPushDOS)) {
            Calendar now = Calendar.getInstance();
            Calendar instance = Calendar.getInstance();
            factorySettingDelayPushDOS.forEach(aDo -> {
                instance.setTime(aDo.getPushTime());
                // 为了避免 长时间不启动，+1天不是当前时间
                instance.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                aDo.setPushTime(instance.getTime());
                factorySettingDelayPushMapper.updateByPrimaryKeySelective(aDo);
            });
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateFactorySetting(FactorySettingUpdateCmd cmd) {
        factorySettingDelayPushMapper.deleteAll();
        if (CollectionUtils.isNotEmpty(cmd.getDelayPushes())) {
            cmd.getDelayPushes().forEach(factorySettingDelayPushesUpdateCmd -> {
                FactorySettingDelayPushDO factorySettingDelayPushDO =
                    FactorySettingConvertor.toFactorySettingDelayPushesDO(factorySettingDelayPushesUpdateCmd);
                factorySettingDelayPushMapper.insert(factorySettingDelayPushDO);
            });
        }
        factorySettingOrderInvalidMapper.deleteAll();
        if (CollectionUtils.isNotEmpty(cmd.getOrderInvalid())) {
            cmd.getOrderInvalid().forEach(factorySettingOrderInvalidUpdateCmd -> {
                FactorySettingOrderInvalidDO factorySettingOrderInvalidDO =
                    FactorySettingConvertor.toFactorySettingOrderInvalidDO(factorySettingOrderInvalidUpdateCmd);
                factorySettingOrderInvalidMapper.insert(factorySettingOrderInvalidDO);
            });
        }
        FactorySettingOrderInvalidDO factorySettingOrderInvalidDO =
            FactorySettingConvertor.toFactorySettingOrderInvalidDO(cmd.getInvalidDefault());
        factorySettingOrderInvalidMapper.insert(factorySettingOrderInvalidDO);
    }

    public FactorySettingOrderInvalidDO getFactorySettingOrderInvalidByDistributorId(Integer integer) {
        FactorySettingOrderInvalidDO byOrderSource = factorySettingOrderInvalidMapper.getByOrderSource(integer);
        if (byOrderSource == null) {
            byOrderSource = factorySettingOrderInvalidMapper.getByOrderSource(0);
        }
        return byOrderSource;
    }
}
