package com.bat.order.api.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.bat.order.api.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/19 19:09
 */
@Slf4j
@Component
public class Exporter {

    static ThreadLocal<Map<String, Object>> localVar = new ThreadLocal<>();

    public void init() {
        log.info("{} 初始化ThreadLocal", Thread.currentThread().getName());
        Map<String, Object> map = new HashMap<>();
        String fileName = "write" + System.currentTimeMillis() + ".xlsx";
        map.put("fileName", fileName);
        map.put("filePath", System.getProperty("java.io.tmpdir") + fileName);
        map.put("sheetName", "模板");
        map.put("async", false);
        localVar.set(map);
        log.info("{} 初始化ThreadLocal 结果：{}", Thread.currentThread().getName(), localVar.get());
    }

    private void destroy() {
        log.info("移除ThreadLocal");
        localVar.remove();
    }

    public Exporter setFileName(String fileName) {
        Map<String, Object> map = localVar.get();
        map.put("fileName", fileName);
        localVar.set(map);
        return this;
    }

    public Exporter setSheetName(String sheetName) {
        Map<String, Object> map = localVar.get();
        map.put("sheetName", sheetName);
        localVar.set(map);
        return this;
    }

    public Exporter setBusinessInfo(BusinessInfo businessInfo) {
        Map<String, Object> map = localVar.get();
        map.put("businessInfo", businessInfo);
        localVar.set(map);
        return this;
    }

    public Exporter setAsync() {
        Map<String, Object> map = localVar.get();
        map.put("async", false);
        localVar.set(map);
        return this;
    }

    /**
     * 返回输出流
     *
     * @param headClazz
     * @param data
     * @param outputStream
     */
    public void exportExcel(Class<?> headClazz, List data, OutputStream outputStream) {
        Map<String, Object> map = localVar.get();
        String sheetName = (String)map.get("sheetName");
        List<List<String>> lists = convertHead(headClazz);
        EasyExcel.write(outputStream, headClazz).head(lists).sheet(sheetName).doWrite(data);
        destroy();
    }

    /**
     * 回写 HttpServletResponse直接下载
     *
     * @param headClazz
     * @param data
     * @param response
     * @throws IOException
     */
    public void exportExcel(Class<?> headClazz, List data, HttpServletResponse response) throws IOException {
        Map<String, Object> map = localVar.get();
        String fileName = (String)map.get("fileName");
        String sheetName = (String)map.get("sheetName");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName1 = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName1 + ".xlsx");
        List<List<String>> lists = convertHead(headClazz);
        EasyExcel.write(response.getOutputStream(), headClazz).head(lists).sheet(sheetName)
            .doWrite(data);
        destroy();
    }

    private List<List<String>> convertHead(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<List<String>> list = new ArrayList<List<String>>();
        for (Field declaredField : declaredFields) {
            List<String> list1 = new ArrayList<>();
            ExcelProperty annotation = declaredField.getAnnotation(ExcelProperty.class);
            if (annotation != null) {
                String[] value = annotation.value();
                for (String s : value) {
                    if (s.startsWith("#")) {
                        String substring = s.substring(1);
                        list1.add(MessageUtils.get(substring));
                    } else {
                        list1.add(s);
                    }
                }
            } else {
                list1.add(declaredField.getName());
            }
            list.add(list1);
        }
        return list;
    }

}
