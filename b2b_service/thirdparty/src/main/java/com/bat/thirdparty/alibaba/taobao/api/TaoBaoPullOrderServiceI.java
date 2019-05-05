package com.bat.thirdparty.alibaba.taobao.api;

import java.time.LocalDateTime;

public interface TaoBaoPullOrderServiceI {

    void doPullTaoBaoOrder(LocalDateTime startTime, LocalDateTime endTime);
}
