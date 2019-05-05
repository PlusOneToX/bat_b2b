package com.bat.promotion.service.rebatevoucher.validator;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.bat.promotion.api.base.ErrorCode;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.service.rebatevoucher.dto.RebateVoucherExcelDTO;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bat.promotion.service.rebatevoucher.executor.ErrorCode.B_IMPORT_PROMOTION_REBATE_VOUCHER_IS_EMPTY;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/3/23 11:22
 */
public class RebateVoucherValidator {

    public static List<RebateVoucherExcelDTO> analysisExcel(InputStream inputStream) {
        List<RebateVoucherExcelDTO> rebateVoucherExcelDTOS = new ArrayList<>();

        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(inputStream).build();
            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet = EasyExcel.readSheet(1).head(RebateVoucherExcelDTO.class)
                    .registerReadListener(new PageReadListener<RebateVoucherExcelDTO>(rebateVoucherExcelDTOS::addAll))
                    .build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }

        rebateVoucherExcelDTOS = rebateVoucherExcelDTOS.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(rebateVoucherExcelDTOS)) {
            throw PromotionException.buildException(B_IMPORT_PROMOTION_REBATE_VOUCHER_IS_EMPTY);
        }
        // 检查必传参数都有没有
        validExcelParams(rebateVoucherExcelDTOS);
        return rebateVoucherExcelDTOS;
    }

    @SneakyThrows
    private static void validExcelParams(List<RebateVoucherExcelDTO> list) {
        for (int i = 0; i < list.size(); i++) {
            RebateVoucherExcelDTO importOrderExcelDTO = list.get(i);
            Field[] fields = importOrderExcelDTO.getClass().getDeclaredFields();
            int lineNum = i + 2;
            for (Field field : fields) {
                field.setAccessible(true);
                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                String columnName = annotation.value()[0];
                String msg = "第{0}行，{1}:不能为空";
                if (columnName.startsWith("*") && field.get(importOrderExcelDTO) == null) {
                    throw PromotionException.buildException(ErrorCode.P_NOTNULL,
                            MessageFormat.format(msg, lineNum, columnName));
                }
            }
            validDistributorInfo(importOrderExcelDTO, lineNum);
        }
    }

    private static void validDistributorInfo(RebateVoucherExcelDTO importOrderExcelDTO, int lineNum) {
        if (importOrderExcelDTO.getDistributorId() == null
                && StringUtils.isBlank(importOrderExcelDTO.getErpDistributorNo())) {
            throw PromotionException.buildException(ErrorCode.P_NOTNULL, "第" + lineNum + "行，B2B编码与ERP分销商ID不能同时为空");
        }
    }
}
