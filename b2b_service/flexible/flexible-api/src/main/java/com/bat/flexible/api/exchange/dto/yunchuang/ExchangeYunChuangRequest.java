package com.bat.flexible.api.exchange.dto.yunchuang;

public class ExchangeYunChuangRequest {

    /**
     * 批次号/采购订单号
     */
    private String batchno;

    /**
     * 开始日期 yyyy-MM-dd
     */
    private String starttime;

    /**
     * 码值txt链接
     */
    private String codetxturl;

    /**
     * 截止日期 格式yyyy-MM-dd
     */
    private String deadline;

    /**
     * 印刷文件样式图片链接
     */
    private String imgurl;

    /**
     * 型号
     */
    private String model;

    /**
     * 数量
     */
    private Integer quantiy;

    /**
     * 物料名称
     */
    private String skuname;

    /**
     * 物料skuno/69码
     */
    private String skuno;

    /**
     * 应用appid
     */
    private String appid;


    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getCodetxturl() {
        return codetxturl;
    }

    public void setCodetxturl(String codetxturl) {
        this.codetxturl = codetxturl;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getQuantiy() {
        return quantiy;
    }

    public void setQuantiy(Integer quantiy) {
        this.quantiy = quantiy;
    }

    public String getSkuname() {
        return skuname;
    }

    public void setSkuname(String skuname) {
        this.skuname = skuname;
    }

    public String getSkuno() {
        return skuno;
    }

    public void setSkuno(String skuno) {
        this.skuno = skuno;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }


}
