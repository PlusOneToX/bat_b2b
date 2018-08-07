package com.bat.order.api.deliver.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 发货单详情
 */
@Data
@ApiModel(value = "AdminOrderDeliverBillDetailDTO", description = "发货单详情")
public class AdminOrderDeliverBillDetailDTO {

    @ApiModelProperty("订单发货单")
    private OrderDeliverBill orderDeliverBill;

    @ApiModelProperty("订单送货信息表")
    private OrderDelivery orderDelivery;

    @ApiModelProperty("订单发货单明细表")
    private List<OrderDeliverBillDetail> orderDeliverBillDetails;

    public OrderDeliverBill getOrderDeliverBill() {
        return orderDeliverBill;
    }

    public void setOrderDeliverBill(OrderDeliverBill orderDeliverBill) {
        this.orderDeliverBill = orderDeliverBill;
    }

    public OrderDelivery getOrderDelivery() {
        return orderDelivery;
    }

    public void setOrderDelivery(OrderDelivery orderDelivery) {
        this.orderDelivery = orderDelivery;
    }

    public List<OrderDeliverBillDetail> getOrderDeliverBillDetails() {
        return orderDeliverBillDetails;
    }

    public void setOrderDeliverBillDetails(List<OrderDeliverBillDetail> orderDeliverBillDetails) {
        this.orderDeliverBillDetails = orderDeliverBillDetails;
    }

    public static class OrderDeliverBill {
        @ApiModelProperty("发货单流水号")
        private Integer id;
        @ApiModelProperty("订单号")
        private Integer orderId;
        @ApiModelProperty("配送id")
        private Integer distributionId;
        @ApiModelProperty("配送方式编码")
        private String distributionCode;
        @ApiModelProperty("配送方式名称")
        private String distributionName;
        @ApiModelProperty("发货状态 1待确认,2.已确认,3.已取消")
        private Short deliverStatus;
        @ApiModelProperty("物流编号")
        private String logisticsNo;
        @ApiModelProperty("物流状态：2-在途中,3-签收,4-问题件")
        private String logisticsStatus;
        @ApiModelProperty("erp出库单号")
        private String deliverErpNo;
        @ApiModelProperty("发货时间")
        private Date deliverTime;
        @ApiModelProperty("下单时间")
        private Date orderCreateTime;
        @ApiModelProperty("购买者")
        private String distributorName;
        @ApiModelProperty("物流费用")
        private BigDecimal distributionAmount;
        @ApiModelProperty("否需要推送第三方,1 为是需要推送，0为不需要推送")
        private Short push;
        @ApiModelProperty("同步给第三方成功与否: 1为同步成功，0为同步失败")
        private Short pushStatus;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public Integer getDistributionId() {
            return distributionId;
        }

        public void setDistributionId(Integer distributionId) {
            this.distributionId = distributionId;
        }

        public String getDistributionCode() {
            return distributionCode;
        }

        public void setDistributionCode(String distributionCode) {
            this.distributionCode = distributionCode;
        }

        public String getDistributionName() {
            return distributionName;
        }

        public void setDistributionName(String distributionName) {
            this.distributionName = distributionName;
        }

        public Short getDeliverStatus() {
            return deliverStatus;
        }

        public void setDeliverStatus(Short deliverStatus) {
            this.deliverStatus = deliverStatus;
        }

        public String getLogisticsNo() {
            return logisticsNo;
        }

        public void setLogisticsNo(String logisticsNo) {
            this.logisticsNo = logisticsNo;
        }

        public String getLogisticsStatus() {
            return logisticsStatus;
        }

        public void setLogisticsStatus(String logisticsStatus) {
            this.logisticsStatus = logisticsStatus;
        }

        public String getDeliverErpNo() {
            return deliverErpNo;
        }

        public void setDeliverErpNo(String deliverErpNo) {
            this.deliverErpNo = deliverErpNo;
        }

        public Date getDeliverTime() {
            return deliverTime;
        }

        public void setDeliverTime(Date deliverTime) {
            this.deliverTime = deliverTime;
        }

        public Date getOrderCreateTime() {
            return orderCreateTime;
        }

        public void setOrderCreateTime(Date orderCreateTime) {
            this.orderCreateTime = orderCreateTime;
        }

        public String getDistributorName() {
            return distributorName;
        }

        public void setDistributorName(String distributorName) {
            this.distributorName = distributorName;
        }

        public BigDecimal getDistributionAmount() {
            return distributionAmount;
        }

        public void setDistributionAmount(BigDecimal distributionAmount) {
            this.distributionAmount = distributionAmount;
        }

        public Short getPush() {
            return push;
        }

        public void setPush(Short push) {
            this.push = push;
        }

        public Short getPushStatus() {
            return pushStatus;
        }

        public void setPushStatus(Short pushStatus) {
            this.pushStatus = pushStatus;
        }
    }

    public static class OrderDelivery {
        @ApiModelProperty("国家id")
        private Integer countryId;
        @ApiModelProperty("省份id")
        private Integer provinceId;
        @ApiModelProperty("城市id")
        private Integer cityId;
        @ApiModelProperty("区/县id")
        private Integer districtId;
        @ApiModelProperty("国家名称")
        private String countryName;
        @ApiModelProperty("省份名称")
        private String provinceName;
        @ApiModelProperty("城市名称")
        private String cityName;
        @ApiModelProperty("区县名称")
        private String districtName;
        @ApiModelProperty("街道地址")
        private String address;
        @ApiModelProperty("邮政编码")
        private String zipCode;
        @ApiModelProperty("收货人姓名")
        private String userName;
        @ApiModelProperty("手机")
        private String mobile;
        @ApiModelProperty("固定电话")
        private String phone;

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public Integer getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(Integer provinceId) {
            this.provinceId = provinceId;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }

        public Integer getDistrictId() {
            return districtId;
        }

        public void setDistrictId(Integer districtId) {
            this.districtId = districtId;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }


    public static class OrderDeliverBillDetail {
        @ApiModelProperty("主键")
        private Integer id;

        @ApiModelProperty("订单发货单id")
        private Integer orderDeliverBillId;

        @ApiModelProperty("订单id")
        private Integer orderId;

        @ApiModelProperty("订单明细id")
        private Integer orderGoodsId;

        @ApiModelProperty("行项目序号")
        private Integer serialNumber;

        @ApiModelProperty("商品id")
        private Integer goodsId;

        @ApiModelProperty("商品自定义编号")
        private String goodsNo;

        @ApiModelProperty("商品名称")
        private String goodsName;

        @ApiModelProperty("货品id")
        private Integer itemId;

        @ApiModelProperty("货品编号")
        private String itemCode;

        @ApiModelProperty("货品名称")
        private String itemName;

        @ApiModelProperty("发货数量")
        private Integer count;

        @ApiModelProperty("出库仓库id")
        private Integer warehouseId;

        @ApiModelProperty("出库仓库名称")
        private String warehouseName;

        @ApiModelProperty("商品类型 1-普通 2-定制")
        private Short goodsType;
        @ApiModelProperty("定制类型 0、标准定制 1、DIY定制")
        private Short diyType;
        @ApiModelProperty("规格值名称")
        private String specsName;
        @ApiModelProperty("颜色值名称")
        private String colorName;
        @ApiModelProperty("货品总数量")
        private Integer itemCount;
        @ApiModelProperty("在库货品数量")
        private Integer itemInCount;
        @ApiModelProperty("vmi货品数量")
        private Integer itemVmiCount;
        @ApiModelProperty("在途货品数量")
        private Integer itemOnWayCount;
        @ApiModelProperty("已发货数量")
        private Integer deliverCount;
        @ApiModelProperty("未发货数量")
        private Integer unDeliverCount;

        @ApiModelProperty("型号名称")
        private String modelName;

        @ApiModelProperty("品牌名称")
        private String brandName;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getOrderDeliverBillId() {
            return orderDeliverBillId;
        }

        public void setOrderDeliverBillId(Integer orderDeliverBillId) {
            this.orderDeliverBillId = orderDeliverBillId;
        }

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public Integer getOrderGoodsId() {
            return orderGoodsId;
        }

        public void setOrderGoodsId(Integer orderGoodsId) {
            this.orderGoodsId = orderGoodsId;
        }

        public Integer getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(Integer serialNumber) {
            this.serialNumber = serialNumber;
        }

        public Integer getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Integer goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsNo() {
            return goodsNo;
        }

        public void setGoodsNo(String goodsNo) {
            this.goodsNo = goodsNo;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Integer getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(Integer warehouseId) {
            this.warehouseId = warehouseId;
        }

        public String getWarehouseName() {
            return warehouseName;
        }

        public void setWarehouseName(String warehouseName) {
            this.warehouseName = warehouseName;
        }

        public Short getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(Short goodsType) {
            this.goodsType = goodsType;
        }

        public Short getDiyType() {
            return diyType;
        }

        public void setDiyType(Short diyType) {
            this.diyType = diyType;
        }

        public String getSpecsName() {
            return specsName;
        }

        public void setSpecsName(String specsName) {
            this.specsName = specsName;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public Integer getItemCount() {
            return itemCount;
        }

        public void setItemCount(Integer itemCount) {
            this.itemCount = itemCount;
        }

        public Integer getItemInCount() {
            return itemInCount;
        }

        public void setItemInCount(Integer itemInCount) {
            this.itemInCount = itemInCount;
        }

        public Integer getItemVmiCount() {
            return itemVmiCount;
        }

        public void setItemVmiCount(Integer itemVmiCount) {
            this.itemVmiCount = itemVmiCount;
        }

        public Integer getItemOnWayCount() {
            return itemOnWayCount;
        }

        public void setItemOnWayCount(Integer itemOnWayCount) {
            this.itemOnWayCount = itemOnWayCount;
        }

        public Integer getDeliverCount() {
            return deliverCount;
        }

        public void setDeliverCount(Integer deliverCount) {
            this.deliverCount = deliverCount;
        }

        public Integer getUnDeliverCount() {
            return unDeliverCount;
        }

        public void setUnDeliverCount(Integer unDeliverCount) {
            this.unDeliverCount = unDeliverCount;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }
    }
}
