package com.bat.order.api.utils.excel;

import java.util.List;

/**
 * @author lx
 */
public interface ExcelService<T> {
    void saveData(List<T> date);
}
