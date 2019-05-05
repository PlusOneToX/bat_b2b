package com.bat.platform;

import com.alibaba.fastjson.JSON;
import com.bat.platform.service.common.config.PlatformDBConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
class BaseApplicationTests {

    @Resource
    private PlatformDBConfig config;

    @Test
    void contextLoads() {
        List<String> goods_db = config.getGoods_db();
        List<String> order_db = config.getOrder_db();
        log.info(JSON.toJSONString(goods_db));
    }

}
