package com.bat.flexible.manager.common.utils.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.bat.flexible.api.util.file.FileUtils;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.manager.common.utils.IdWorker;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;

public class ExcelFileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelFileUtils.class);

    /**
     * 生成excel文件
     * @param listData 列表信息
     * @param pojoClass 类信息
     * @param FileType 文件类型
     * @param fileSuffix 文件后缀
     * @return
     */
    public static File create(Collection<?> listData, Class<?> pojoClass, String FileType, String fileSuffix) {
        Workbook workbook = null;
        FileOutputStream fileOutputStream = null;
        try {
            workbook = ExcelExportUtil.exportExcel(new ExportParams(), pojoClass, listData);
            //获取文件夹
            String folder = FileUtils.getSystemDirectory(FileType);
            IdWorker idWorker = new IdWorker();
            //获取文件名
            String fileName = idWorker.nextId() + "." + fileSuffix;
            //最终文件整体地址
            String filePath = folder + "/" + fileName;
            LOGGER.info("最终文件生成地址:{}", filePath);
            File file = new File(filePath);
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            if (!file.exists()) {
                throw new FlexibleCustomException("文件生成失败!");
            }
            return file;
        } catch (Exception e) {
            throw new FlexibleCustomException("文件生成失败:" + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (Exception e) {
                LOGGER.error("关闭流出现异常:{}", e.getMessage());
            }
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                LOGGER.error("关闭流出现异常:{}", e.getMessage());
            }
        }
    }
}
