package com.bat.financial.export;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import com.bat.financial.export.executor.ExportCmdExc;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.bat.financial.api.export.ExportService;
import com.bat.financial.api.export.dto.ReconciliationExportDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/1/4 15:23
 */
@Service
@Slf4j
public class ExportServiceImpl implements ExportService {

    @Resource
    private ExportCmdExc exportCmdExc;

    @Override
    public void reconciliationExport(InputStream inputStream, ServletOutputStream outputStream) {
        List<ReconciliationExportDTO> dtoList = new ArrayList<>();
        EasyExcel.read(inputStream, ReconciliationExportDTO.class,
            new PageReadListener<ReconciliationExportDTO>(dtoList::addAll)).sheet().doRead();
        exportCmdExc.reconciliationExport(dtoList);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(outputStream, ReconciliationExportDTO.class).sheet(1).doWrite(() -> {
            // 分页查询数据
            return dtoList;
        });
    }
}
