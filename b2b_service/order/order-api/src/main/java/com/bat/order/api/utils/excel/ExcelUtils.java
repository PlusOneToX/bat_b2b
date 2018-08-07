package com.bat.order.api.utils.excel;

import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/19 18:34
 */
@Configuration()
public class ExcelUtils {

    @Resource
    private Exporter exporter;

    @Resource
    private Importer importer;

    public Exporter getExporter() {
        exporter.init();
        return exporter;
    }

    public Importer getImporter() {
        return importer;
    }
}
