package com.bat.order.api.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/19 19:09
 */
@Slf4j
@Component
public class Importer {

    // @PostMapping("/upload")
    // public void upload(MultipartFile file) throws IOException {
    // List<Object> objects = excelUtils.getImporter().importExcel(DepositDistributorDTO.class, file.getInputStream());
    // System.out.println(objects);
    // }

    /**
     * 自己实现回调
     * 
     * @param headClazz
     * @param inputStream
     * @param listener
     */
    public void importExcel(Class<?> headClazz, InputStream inputStream, ReadListener listener) {
        EasyExcel.read(inputStream, headClazz, listener).sheet().doRead();
    }

    /**
     * 实现提供的 ExcelService 的saveData方法
     * 
     * @param headClazz
     * @param inputStream
     * @param service
     */
    public void importExcel(Class<?> headClazz, InputStream inputStream, ExcelService service) {
        EasyExcel.read(inputStream, headClazz, new DefaultListener(service)).sheet().doRead();
    }

    /**
     * 阻塞读取 警告 数据量大不要使用
     * 
     * @param headClazz
     * @param inputStream
     * @param <T>
     * @return
     */
    public <T> List<T> importExcel(Class<?> headClazz, InputStream inputStream) {
        final boolean[] flag = {true};
        List<T> datas = new ArrayList<>();
        EasyExcel.read(inputStream, headClazz, new DefaultListener<T>() {
            @Override
            public void saveData(List date) {
                datas.addAll(date);
                flag[0] = false;
            }
        }).sheet().doRead();
        while (flag[0]) {
        }
        return datas;
    }
}
