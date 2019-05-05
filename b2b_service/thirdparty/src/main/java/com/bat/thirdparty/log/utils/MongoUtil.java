package com.bat.thirdparty.log.utils;

import lombok.Data;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;


@Data
@Component
public class MongoUtil<T> {
    public Integer pageSize;
    private Integer currentPage;


    public void start(Integer currentPage, Integer pageSize, Query query) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        query.limit(pageSize);
        query.skip((currentPage - 1) * pageSize);
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public PageHelper pageHelper(long total, List<T> list) {
        return new PageHelper(this.currentPage, total, this.pageSize, list);
    }

    public PageHelper pageHelper(List<T> list) {
        return new PageHelper(this.currentPage, this.pageSize, list);
    }

    public PageHelper pageHelper(long currentPage, long total, long pageSize, List<T> list) {
        return new PageHelper(currentPage, total, pageSize, list);
    }

    public PageHelper pageHelper(long currentPage, long pageSize, List<T> list) {
        return new PageHelper(currentPage, pageSize, list);
    }


    /**
     * 用于模糊查询忽略大小写
     *
     * @param string
     * @return
     */
    public Pattern getPattern(String string) {
        Pattern pattern = Pattern.compile("^.*" + string + ".*$", Pattern.CASE_INSENSITIVE);
        return pattern;
    }
}
