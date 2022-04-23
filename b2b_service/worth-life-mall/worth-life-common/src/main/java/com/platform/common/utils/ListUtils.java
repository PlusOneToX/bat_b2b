package com.platform.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    /**
     * 利用subList方法进行分页
     * @param list 分页数据

     * @param pagesize  页面大小

     * @param currentPage   当前页面

     */


    /**
     * 自定义List分页工具
     *
     * @author hanwl
     */

    /**
     * 开始分页
     */
    public static List<String> pageBySubList(List list, int currentPage, int pagesize) {

        int totalcount = list.size();

        int pagecount = 0;

        List<String> subList;

        int m = totalcount % pagesize;

        if (m > 0) {

            pagecount = totalcount / pagesize + 1;

        } else {

            pagecount = totalcount / pagesize;

        }

        if (m == 0) {

            subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));

        } else {

            if (currentPage == pagecount) {

                subList = list.subList((currentPage - 1) * pagesize, totalcount);

            } else {

                subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));

            }

        }

        return subList;

    }

    public static void main(String[] args) {
        List list = pageBySubList(new ArrayList(), 1, 10);
        System.out.println(list);
    }

}

