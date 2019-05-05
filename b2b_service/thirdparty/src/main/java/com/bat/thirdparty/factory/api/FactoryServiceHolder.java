package com.bat.thirdparty.factory.api;

import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/26 9:45
 */
@Component
public class FactoryServiceHolder implements ApplicationContextAware {

    Map<FactoryEnum, FactoryService> map = new HashMap<>();

    private ApplicationContext app;

    @PostConstruct
    public void init() {
        // 通用发货 手动发货
        map.put(FactoryEnum.COMMON, app.getBean("CommonFactoryServiceImpl", FactoryService.class));
        map.put(FactoryEnum.LHW, app.getBean("HaixingFactoryServiceImpl", FactoryService.class));
        map.put(FactoryEnum.MK, app.getBean("MaikeFactoryServiceImpl", FactoryService.class));
        map.put(FactoryEnum.DH_OLK, app.getBean("DuoHongFactoryServiceImpl", FactoryService.class));
        map.put(FactoryEnum.KDS_FK, app.getBean("FeiKuaiFactoryServiceImpl", FactoryService.class));
    }

    public FactoryService getService(FactoryEnum key) {
        return map.get(key);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.app = applicationContext;
    }
}
