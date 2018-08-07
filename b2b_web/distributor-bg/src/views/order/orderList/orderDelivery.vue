<!--
 * @Author: yaowei
 * @Date: 2018-03-24 17:27:36
 * @LastEditors: yaowei
 * @LastEditTime: 2018-03-31 15:45:44
-->
<template>
  <el-dialog
    title="发货"
    :modal-append-to-body="false"
    :visible="deliveryShow"
    :before-close="handleCloseDelivery"
    width="480px"
  >
    <el-form
      ref="deliveryForm"
      :model="deliveryInfo"
      :rules="deliveryRules"
      label-width="100px"
    >
      <el-form-item label="物流公司：" prop="lcName">
        <el-select
          class="custom_select"
          placeholder="请选择"
          size="mini"
          v-model="deliveryInfo.lcName"
          clearable
        >
          <el-option
            :key="item.name"
            :label="item.name"
            :value="item.name"
            v-for="item in expressData"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="物流单号：" prop="lcOrderno">
        <el-input
          size="mini"
          v-model.number="deliveryInfo.lcOrderno"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button size="mini" @click="handleCloseDelivery">返回</el-button>
      <el-button
        class="mini-search-btn box-btn"
        @click="handleConfirmDelivery"
        :loading="loading"
        >确定</el-button
      >
    </span>
  </el-dialog>
</template>

<script>
export default {
  props: ["pageFlag", "curOrderId", "curProductOrderNo"],
  data() {
    return {
      //发货
      deliveryShow: true,
      deliveryInfo: {
        lcName: "",
        lcOrderno: "",
      },
      expressData: [],
      deliveryRules: {
        lcName: [
          {
            validator: (rule, value, callback) => {
              if (!value) {
                callback(new Error("请选择物流公司"));
              } else {
                callback();
              }
            },
          },
        ],
        lcOrderno: [
          {
            validator: (rule, value, callback) => {
              if (!value) {
                callback(new Error("请输入物流单号"));
              } else {
                callback();
              }
            },
          },
        ],
      },
      loading: false,
    };
  },
  created() {
    this.deliveryShow = this.$parent.deliveryShow;
    this.getExpressData(); // 获取配送方式
  },
  methods: {
    handleCloseDelivery() {
      this.deliveryShow = false;
      this.$parent.deliveryShow = false;
      this.deliveryInfo.lcName = "";
      this.deliveryInfo.lcOrderno = "";
    },
    handleConfirmDelivery() {
      // 获取编码
      let lcCode = "";
      this.expressData.forEach((item) => {
        if (item.name === this.deliveryInfo.lcName) {
          lcCode = item.distributionFactoryId;
        }
      });
      // 订单发货
      this.$refs["deliveryForm"].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          this.loading = true;
          this.$api
            .exportData(this, "admin/u/p/deliver/order", {
              data: JSON.stringify({
                lcName: this.deliveryInfo.lcName,
                lcOrderno: this.deliveryInfo.lcOrderno,
                sellerOrderNo: this.curOrderId,
                lcCode: lcCode,
                orderNo: this.curProductOrderNo,
              }),
              timestamp: Date.parse(new Date()) / 1000,
            })
            .then((res) => {
              if (this.pageFlag === "orderList") {
                // 订单列表
                this.$parent.pageInfo.orderSources = 50;
                this.$parent.pageInfo.page = 1;
                this.$parent.updateMainData();
              } else if (this.pageFlag === "orderDetail") {
                // 订单详情
                this, $parent.requestData();
              }
              this.loading = false;
              this.deliveryShow = false;
              this.$parent.deliveryShow = false;
              this.deliveryInfo.lcName = "";
              this.deliveryInfo.lcOrderno = "";
            });
        }
      });
    },
    // 获取配送方式列表
    getExpressData() {
      this.$http.logisticsList(this, {page: 1,size: 100,}).then(res => {
        if (res.success) {
          this.expressData = res.distributions;
        }
      });
    },
  },
};
</script>
