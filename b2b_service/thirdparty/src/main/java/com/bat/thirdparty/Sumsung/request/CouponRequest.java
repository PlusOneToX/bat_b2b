package com.bat.thirdparty.Sumsung.request;


public class CouponRequest {

    private short postReason;// 1 优惠券同步  -1 清空优惠券
    private String couponData;

    public short getPostReason() {
        return postReason;
    }

    public void setPostReason(short postReason) {
        this.postReason = postReason;
    }

    public String getCouponData() {
        return couponData;
    }

    public void setCouponData(String couponData) {
        this.couponData = couponData;
    }

    public static class CouponData{
        private Long couponId = 1L;
        private Long id;
        private String cpName = "";
        private String uid;
        private String cpService = "主题手机壳";
        private String category = "购物";
        private String subcategory = "手机壳";
        private Double price;
        private String currency = "RMB";
        private Short status; // 0 进行中 1 已使用
        private String startTime;
        private String endTime;
        private String url;
        private String detailInformation;
        private String userCondition = "常规";
        private String fixCpName = "";
        private String fixCpService="MainthemePhonecase";

        public Long getCouponId() {
            return couponId;
        }

        public void setCouponId(Long couponId) {
            this.couponId = couponId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCpName() {
            return cpName;
        }

        public void setCpName(String cpName) {
            this.cpName = cpName;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCpService() {
            return cpService;
        }

        public void setCpService(String cpService) {
            this.cpService = cpService;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(String subcategory) {
            this.subcategory = subcategory;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Short getStatus() {
            return status;
        }

        public void setStatus(Short status) {
            this.status = status;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDetailInformation() {
            return detailInformation;
        }

        public void setDetailInformation(String detailInformation) {
            this.detailInformation = detailInformation;
        }

        public String getUserCondition() {
            return userCondition;
        }

        public void setUserCondition(String userCondition) {
            this.userCondition = userCondition;
        }

        public String getFixCpName() {
            return fixCpName;
        }

        public void setFixCpName(String fixCpName) {
            this.fixCpName = fixCpName;
        }

        public String getFixCpService() {
            return fixCpService;
        }

        public void setFixCpService(String fixCpService) {
            this.fixCpService = fixCpService;
        }
    }


}
