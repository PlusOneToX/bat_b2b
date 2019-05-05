package com.bat.thirdparty.common.base;

import java.util.List;

public class RequestPageBean<T> extends RequestBaseBean {

    private int page = 1;//用户想看的页码
    private int count = 10;    //页的大小
    private List<T> list;   //分装分页数据
    private int totalPage;   //总页数
    private int allCount;       //总记录数
    private int startIndex;   //代表用户想看的页的数据从数据库哪个地方开始取

    public RequestPageBean() {
    }

    public RequestPageBean(int allCount, int page) {
        this.allCount = allCount;
        this.page = page;
        if ((this.allCount % this.count) == 0) {
            this.totalPage = (this.allCount / this.count);
        } else {
            this.totalPage = (this.allCount / this.count) + 1;
        }
        this.startIndex = (this.page - 1) * this.count;
    }

    public void setAllCount(int allCount, int count) {
        this.allCount = allCount;
        this.count = count;
        if ((this.allCount % this.count) == 0) {
            this.totalPage = (this.allCount / this.count);
        } else {
            this.totalPage = (this.allCount / this.count) + 1;
        }
        this.startIndex = (this.page - 1) * this.count;
    }

    public void setCount(int count) {
        // 不允许超过1000
        if (count > 1000) {
            count = 1000;
        }
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
}
