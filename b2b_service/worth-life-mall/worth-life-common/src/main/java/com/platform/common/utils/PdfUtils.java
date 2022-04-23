/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.common.utils;

import com.lowagie.text.Element;
import com.lowagie.text.pdf.*;

import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author 李鹏军
 */
public class PdfUtils {

    /**
     * @param filePath  原PDF位置
     * @param waterMark 要添加的水印
     * @param attrName  PDF文件名称
     * @return 添加水印的文件路径
     * @throws Exception Exception
     */
    public static String setWatermark(String filePath, String waterMark, String attrName) throws Exception {
        String result = filePath + "autoMail" + File.separator + attrName;
        // 要输出的pdf文件
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(result)));
        PdfReader reader = new PdfReader(filePath + attrName);
        PdfStamper stamper = new PdfStamper(reader, bos);
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
        BaseFont base = BaseFont.createFont("Times-Italic", "", false);
        PdfGState gs = new PdfGState();
        for (int i = 1; i < total; i++) {
            //在内容下方加水印
            content = stamper.getUnderContent(i);
            gs.setFillOpacity(0.2f);
            // content.setGState(gs);
            content.beginText();
            content.setColorFill(Color.LIGHT_GRAY);
            content.setFontAndSize(base, 50);
            content.setTextMatrix(70, 200);
            content.showTextAligned(Element.ALIGN_CENTER, waterMark, 400, 400, 45);
            content.setColorFill(Color.BLACK);
            content.setFontAndSize(base, 8);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            content.showTextAligned(Element.ALIGN_CENTER, "AutoWaterPrint: " + format.format(cal.getTime()), 300, 10, 0);
            content.endText();
        }
        stamper.close();
        return result;
    }
}
