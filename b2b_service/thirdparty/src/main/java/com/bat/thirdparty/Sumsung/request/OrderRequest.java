package com.bat.thirdparty.Sumsung.request;


import java.util.ArrayList;
import java.util.List;

public class OrderRequest {

    private String version = "2.0";
    private List<Transaction> transactions = new ArrayList<>();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public static class Transaction{
        private String cpName = "";
        private String cpService = "MainthemePhonecase";
        private String uid;
        private Long transactionDateTime;
//        private TransactionData transactionData;
        private String transactionData;
        private String url;

        public String getCpName() {
            return cpName;
        }

        public void setCpName(String cpName) {
            this.cpName = cpName;
        }

        public String getCpService() {
            return cpService;
        }

        public void setCpService(String cpService) {
            this.cpService = cpService;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Long getTransactionDateTime() {
            return transactionDateTime;
        }

        public void setTransactionDateTime(Long transactionDateTime) {
            this.transactionDateTime = transactionDateTime;
        }

        public String getTransactionData() {
            return transactionData;
        }

        public void setTransactionData(String transactionData) {
            this.transactionData = transactionData;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class TransactionData{
        private String orderNumber;
        private Double totalAmount;
        private OrderStatus orderStatus;
        private String paymentStatus;
        private List<OrderItem> orderItem = new ArrayList<>();
        private Long orderQuantity;

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public OrderStatus getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public List<OrderItem> getOrderItem() {
            return orderItem;
        }

        public void setOrderItem(List<OrderItem> orderItem) {
            this.orderItem = orderItem;
        }

        public Long getOrderQuantity() {
            return orderQuantity;
        }

        public void setOrderQuantity(Long orderQuantity) {
            this.orderQuantity = orderQuantity;
        }
    }
    public static class OrderStatus{
        private String statusDesc;
        private String status;

        public OrderStatus(String statusDesc, String status) {
            this.statusDesc = statusDesc;
            this.status = status;
        }

        public String getStatusDesc() {
            return statusDesc;
        }

        public void setStatusDesc(String statusDesc) {
            this.statusDesc = statusDesc;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
    public static class OrderItem{
        private String image;
        private Long quantity;
        private Double price;
        private String name;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Long getQuantity() {
            return quantity;
        }

        public void setQuantity(Long quantity) {
            this.quantity = quantity;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
