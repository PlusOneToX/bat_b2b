package com.bat.goods.api.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CharUtils {

    /**
     * 去除字符串前后第一个字符；并转成list
     * @param str
     * @return
     */
    public static List<Integer> cuttingGetListFromStr(String str) {
        if (StringUtils.isBlank(str) || str.length() <= 2) {
            return new ArrayList<>();
        }
        str = str.substring(0, str.length() - 1);
        str = str.substring(1);
        List<String> strList = Arrays.asList(str.split(","));
        List<Integer> intList = strList.stream().map(Integer::valueOf).collect(Collectors.toList());
        return intList;
    }

    public static void main(String args[]){
        String d="d1d";
        System.out.println(cuttingGetListFromStr(d));
    }
}
