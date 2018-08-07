package com.bat.system.service.globalsetting.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.globalsetting.dto.data.FactorySettingDelayPushesDTO;
import com.bat.system.api.globalsetting.dto.data.FactorySettingOrderInvalidDTO;
import com.bat.system.dao.globalsetting.FactorySettingDelayPushMapper;
import com.bat.system.dao.globalsetting.FactorySettingOrderInvalidMapper;
import com.bat.system.dao.globalsetting.dataobject.FactorySettingDelayPushDO;
import com.bat.system.dao.globalsetting.dataobject.FactorySettingOrderInvalidDO;
import com.bat.system.service.globalsetting.convertor.FactorySettingConvertor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class FactorySettingQryExc {

    @Resource
    private FactorySettingDelayPushMapper factorySettingDelayPushMapper;

    @Resource
    private FactorySettingOrderInvalidMapper factorySettingOrderInvalidMapper;

    public List<FactorySettingDelayPushesDTO> listFactorySettingDelayPushes() {
        List<FactorySettingDelayPushDO> factorySettingDelayPushDOS = factorySettingDelayPushMapper.listAll();
        return FactorySettingConvertor.toFactorySettingDelayPushesDTOList(factorySettingDelayPushDOS);
    }

    public List<FactorySettingOrderInvalidDTO> listFactorySettingOrderInvalid() {
        List<FactorySettingOrderInvalidDO> factorySettingOrderInvalidDOS = factorySettingOrderInvalidMapper.listAll();
        factorySettingOrderInvalidDOS
            .removeIf(factorySettingOrderInvalidDO -> factorySettingOrderInvalidDO.getOrderSource() == 0);
        return FactorySettingConvertor.toFactorySettingOrderInvalidDTOList(factorySettingOrderInvalidDOS);
    }

    public FactorySettingOrderInvalidDTO getFactorySettingOrderInvalidByOrderSource(int orderSource) {
        FactorySettingOrderInvalidDO factorySettingOrderInvalidDO =
            factorySettingOrderInvalidMapper.getByOrderSource(orderSource);
        return FactorySettingConvertor.toFactorySettingOrderInvalidDTO(factorySettingOrderInvalidDO);
    }
}
