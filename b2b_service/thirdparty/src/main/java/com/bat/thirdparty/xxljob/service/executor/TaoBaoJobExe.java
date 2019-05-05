package com.bat.thirdparty.xxljob.service.executor;

import com.bat.thirdparty.alibaba.taobao.api.TaoBaoPullOrderServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 沙漠
 */
@Component
public class TaoBaoJobExe {

    private static final Logger log = LoggerFactory.getLogger(TaoBaoJobExe.class);

    @Autowired
    private TaoBaoPullOrderServiceI taoBaoPullOrderServiceI;

    public void doPullTaoBaoOrder(LocalDateTime startTime, LocalDateTime endTime) {
        taoBaoPullOrderServiceI.doPullTaoBaoOrder(startTime, endTime);
    }

}
