package com.bat.order.service.importOrder;

import static com.bat.order.service.common.error.ImportOrderErrorCode.B_FILE_IS_EMPTY;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.github.pagehelper.PageInfo;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.importOrder.api.ImportOrderService;
import com.bat.order.api.importOrder.dto.*;
import com.bat.order.service.importOrder.dto.ImportOrderExcelDTO;
import com.bat.order.service.importOrder.executor.ImportOrderCmdExe;
import com.bat.order.service.importOrder.executor.ImportOrderQryExe;
import com.bat.order.tenant.TenantContext;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/2 13:46
 */
@Service
@Slf4j
public class ImportOrderServiceImpl implements ImportOrderService {

    @Resource
    private ImportOrderCmdExe importOrderCmdExe;

    @Resource
    private ImportOrderQryExe importOrderQryExe;

    @Value("${oss.excel.user-import-order}")
    public String userImportOrderUrl;

    @Value("${oss.excel.admin-import-order}")
    public String adminImportOrderUrl;

    @Override
    public String getTempUrl(Integer fileType, Boolean adminFlag) {
        if (fileType == 1) {
            if (adminFlag) {
                return adminImportOrderUrl;
            } else {
                return userImportOrderUrl;
            }
        } else {

        }
        return null;
    }

    @Override
    public void importOrder(InputStream inputStream, String userId) {
        List<ImportOrderExcelDTO> dtos = new ArrayList<>();
        // 全部读完 然后在内存中校验 出错的数据，本次就不能导入
        // 可能内存溢出
        ExcelReader excelReader = null;
        // 读取除了模板之外的所有sheet
        try {
            excelReader = EasyExcel.read(inputStream).build();
            List<ReadSheet> sheets = excelReader.excelExecutor().sheetList();
            List<ReadSheet> readSheets = new ArrayList<>();
            for (int i = 1; i <= sheets.size(); i++) {
                ReadSheet readSheet = EasyExcel.readSheet(i).head(ImportOrderExcelDTO.class)
                    .registerReadListener(new PageReadListener<ImportOrderExcelDTO>(dtos::addAll)).build();
                readSheets.add(readSheet);
            }
            excelReader.read(readSheets);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
        // 前台导入 全excel一个值。后台导入取导入的值
        if (StringUtils.isNotBlank(userId)) {
            dtos.forEach(importOrderExcelDTO -> importOrderExcelDTO.setDistributorId(Integer.parseInt(userId)));
        }
        if (!CollectionUtils.isEmpty(dtos)) {
            importOrderCmdExe.save(dtos);
        } else {
            throw OrderException.buildException(B_FILE_IS_EMPTY);
        }
    }

    @Override
    public PageInfo<ImportOrderDTO> importOrderList(ImportOrderListQry qry) {
        return importOrderCmdExe.listImportOrder(qry);
    }

    @Override
    public ImportOrderDetailDTO importOrderDetail(ImportOrderDetailQry qry) {
        return importOrderCmdExe.importOrderDetail(qry);
    }

    @Override
    public void deleteImportOrderInfo(ImportOrderIds request) {
        importOrderCmdExe.deleteImportOrderInfo(request);
    }

    @Override
    public void importOrders(ImportOrderIds request, Boolean adminFlag, String contactId, String contactName,
        String language) {
        importOrderCmdExe.addImportOrders(request, adminFlag, contactId, contactName, language,
            TenantContext.getTenantNo());
    }

    @Override
    public void importOrderUpdate(ImportOrderUpdateCmd cmd) {
        importOrderCmdExe.importOrderUpdate(cmd);
    }

    @Override
    public void downLoad(ImportOrderDownloadQry qry) {
        importOrderQryExe.downLoad(qry);
    }
}
