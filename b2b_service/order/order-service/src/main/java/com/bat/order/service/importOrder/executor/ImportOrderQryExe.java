package com.bat.order.service.importOrder.executor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bat.order.api.importOrder.dto.ImportOrderDownloadQry;
import com.bat.order.dao.importOrder.ImportOrderGoodsMapper;
import com.bat.order.dao.importOrder.ImportOrderMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2018/05/19 13:55
 */
@Component
@Slf4j
public class ImportOrderQryExe {

    @Resource
    private ImportOrderMapper importOrderMapper;

    @Resource
    private ImportOrderGoodsMapper importOrderGoodsMapper;

    public void downLoad(ImportOrderDownloadQry qry) {

    }
}
