package com.bat.financial.api.export;

import java.io.InputStream;

import javax.servlet.ServletOutputStream;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/1/4 15:20
 */
public interface ExportService {
    void reconciliationExport(InputStream inputStream, ServletOutputStream outputStream);
}
