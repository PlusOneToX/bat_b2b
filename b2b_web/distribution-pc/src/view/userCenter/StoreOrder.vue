<template>
  <div class="store-order">
    <div class="store-manage">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="store-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div
          class="
            store-right
            rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double
          "
        >
          <div class="content">
            <h6 class="store-right-title">{{ $t("UserCenter.StoreOrder") }}</h6>
            <div class="search-box">
              <el-input
                size="mini"
                :placeholder="$t('UserCenter.StoreName')"
                type="text"
                clearable
                v-model.trim="pageInfo.shopName"
                @clear="handleClear(1)"
              ></el-input>
              <el-input
                class="ml-15"
                size="mini"
                :placeholder="$t('UserCenter.StoreCode')"
                type="text"
                clearable
                v-model.trim="pageInfo.shopCode"
                @clear="handleClear(2)"
              ></el-input>
              <el-input
                class="ml-15"
                size="mini"
                :placeholder="$t('UserCenter.OrderNo')"
                type="text"
                clearable
                v-model.trim="pageInfo.orderIdQryStr"
                @clear="handleClear(3)"
              ></el-input>
              <el-input
                class="ml-15"
                size="mini"
                :placeholder="$t('UserCenter.RecipientName')"
                type="text"
                clearable
                v-model.trim="pageInfo.name"
                @clear="handleClear(4)"
              ></el-input>
              <el-button
                class="mini-search-btn ml-15"
                type="info"
                @click="searchOrder"
                size="mini"
                >{{ $t("P.Search") }}</el-button
              >
              <el-button
                class="mini-search-btn ml-15"
                type="info"
                @click="showSenior"
                size="mini"
                >{{ $t("UserCenter.AdvancedSearch") }}</el-button
              >
            </div>
            <div
              class="search-info rl-clear rl-padding-top-xxxs"
              v-show="senior === true"
            >
              <div class="items date-items rl-fl rl-clear">
                <!-- <el-select
                  class="app_select ml-15"
                  :placeholder="$t('UserCenter.AppName')"
                  size="mini"
                  v-model="pageInfo.appName"
                  clearable
                  @change="handleSelect"
                >
                  <el-option
                    v-for="item in appArr"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select> -->
                <div class="rl-fl">
                  <el-date-picker
                    v-model="filters.column.starDate"
                    type="datetime"
                    :placeholder="$t('ExportOrder.StartTime')"
                    :picker-options="pickerStarDate"
                  ></el-date-picker>
                </div>
                <span class="rl-fl">－</span>
                <div class="rl-fl">
                  <el-date-picker
                    v-model="filters.column.endDate"
                    type="datetime"
                    :placeholder="$t('ExportOrder.EndTime')"
                    :picker-options="pickerEndDate"
                  ></el-date-picker>
                </div>
              </div>
            </div>
            <div class="query-detail">
              <div class="nav rl-clear">
                <ul class="rl-fl" :class="$i18n.locale === 'en' ? 'navEn' : ''">
                  <li
                    :class="orderStatus === 0 ? 'current' : ''"
                    @click="changeState(0)"
                  >
                    {{ $t("UserCenter.AllOrders") }}
                  </li>
                  <!-- <li
                      :class="orderStatus === 1? 'current': ''"
                      @click="changeState(1)"
                    >{{$t('UserCenter.PendingPayment')}}</li> -->
                  <!--待确认--->
                  <li
                    :class="orderStatus === 10 ? 'current' : ''"
                    @click="changeState(10)"
                  >
                    {{ $t("UserCenter.ToBeConfirmed") }}
                  </li>
                  <li
                    :class="orderStatus === 2 ? 'current' : ''"
                    @click="changeState(2)"
                  >
                    {{ $t("UserCenter.ToBeShipped") }}
                  </li>
                  <li
                    :class="orderStatus === 3 ? 'current' : ''"
                    @click="changeState(3)"
                  >
                    {{ $t("UserCenter.Shipped") }}
                  </li>
                  <li
                    :class="orderStatus === 4 ? 'current' : ''"
                    @click="changeState(4)"
                  >
                    {{ $t("UserCenter.Completed") }}
                  </li>
                  <li
                    :class="orderStatus === 6 ? 'current' : ''"
                    @click="changeState(6)"
                  >
                    {{ $t("UserCenter.Closed") }}
                  </li>
                </ul>
                <div @click="handleExport" class="rl-fr export-btn">
                  <i class="iconfont icon-export"></i>
                  {{ $t("UserCenter.ExportOrder") }}
                </div>
              </div>
              <div class="table-box">
                <el-table
                  ref="tableRef"
                  :data="orderList"
                  header-row-class-name="header-row"
                  border
                  style="text-align: center"
                  @select="select"
                  @select-all="selectAll"
                  @selection-change="handleSelectionChange"
                  :row-key="getRowKeys"
                  v-loading="loading"
                >
                  <el-table-column
                    align="center"
                    type="selection"
                    :reserve-selection="true"
                    key="1"
                    label="全选"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('UserCenter.StoreName')"
                    prop="shopName"
                    width="100"
                    show-overflow-tooltip
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('UserCenter.StoreCode')"
                    prop="shopCode"
                    :min-width="70"
                    show-overflow-tooltip
                  ></el-table-column>
                  <!-- <el-table-column
                    align="center"
                    :label="$t('UserCenter.AppName')"
                    prop="userSourceName"
                    :min-width="90"
                    show-overflow-tooltip
                  ></el-table-column> -->
                  <el-table-column
                    align="center"
                    :label="$t('UserCenter.OrderNo')"
                    prop="orderNo"
                    :min-width="70"
                    show-overflow-tooltip
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('UserCenter.RecipientName')"
                    prop="userName"
                    :min-width="70"
                    show-overflow-tooltip
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('P.Mobile')"
                    prop="mobile"
                    :min-width="120"
                    show-overflow-tooltip
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('OrderSuccess.TotalOrder')"
                    prop="payAmount"
                    :min-width="80"
                    show-overflow-tooltip
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('UserCenter.Remarks')"
                    prop="remark"
                    :min-width="100"
                    show-overflow-tooltip
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('Foot.PaymentMethod')"
                    prop="payWay"
                    :max-width="110"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      <div>{{ confirmPayWays(scope.row.payWay) }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('UserCenter.OrderTime')"
                    prop="createTime"
                    :min-width="140"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      <div style="font-size: 12px">
                        {{ scope.row.createTime }}
                      </div>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>
          <div class="rl-tr rl-margin-top-default" v-if="totalCount > 0">
            <el-pagination
              background
              :current-page="pageInfo.page"
              :page-size="pageInfo.count"
              @current-change="onCurrentChange"
              layout=" prev, pager, next , total , jumper"
              :total="totalCount"
              class="page"
            ></el-pagination>
          </div>
        </div>
      </div>
      <!----导出弹框----->
      <el-dialog
        title="导出表格"
        :visible.sync="ExportDialogVisible"
        width="40%"
        center
      >
        <div class="raido-group">
          <el-radio v-model="radioType" label="1">
            <i class="icon screen"></i>
            <div class="text">筛选后信息</div>
          </el-radio>
          <el-radio v-model="radioType" label="2">
            <i class="icon all"></i>
            <div class="text">全部信息</div>
          </el-radio>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="handleExportOrder">确 定</el-button>
          <el-button @click="ExportDialogVisible = false">取 消</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import { formatDate } from "@/assets/js/common.js";
import { getToken } from '@/utils/auth'
import axios from 'axios'

// liu
import {
  orderDiyLists,
  platformList,
} from "@/apiService/api";
export default {
  components: {
    Header,
    Left,
  },
  data() {
    return {
      loading: false,
      userState: 2,
      orderStatus: 0,
      radioType: "2",
      appArr: [],
      appId: null,
      pageInfo: {
        appName: null,
        tradeStatus: 0,
        shopName: null,
        shopCode: null,
        orderIdQryStr: null,
        name: null,
        startTime: null,
        endTime: null,
        page: 1,
        count: 10,
        searchFrom: "rxShopOrderList",
      },
      totalCount: 0,
      orderList: [],
      orderListImp: [],
      multipleSelect: [],
      isSelect: false,
      senior: false,
      ExportDialogVisible: false,
      filters: {
        column: {
          starDate: "",
          endDate: "",
        },
      },
      pickerStarDate: {
        disabledDate: (time) => {
          let beginDateVal = this.pageInfo.endTime;
          if (beginDateVal) {
            return time.getTime() > beginDateVal;
          }
        },
      },
      pickerEndDate: {
        disabledDate: (time) => {
          let beginDateVal = this.filters.column.starDate;
          if (beginDateVal) {
            return time.getTime() < beginDateVal;
          }
        },
      },
      platform: "",
    };
  },
  mounted() {
    // this.getAppList();
    this.getOrderList();
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
  },
  methods: {
    // 获取订单列表
    getOrderList() {
      this.multipleSelect = [];
      let userId = localStorage.getItem("userId");

      let params = {
        shopName: this.pageInfo.shopName || null,
        shopCode: this.pageInfo.shopCode || null,
        orderNo: this.pageInfo.orderIdQryStr
          ? String(this.pageInfo.orderIdQryStr)
          : undefined, //订单编号
        userName: this.pageInfo.name != "" ? this.pageInfo.name : undefined, //收货人
        startTime:
          this.pageInfo.startTime != "" ? this.pageInfo.startTime : undefined, //开始时间
        endTime:
          this.pageInfo.endTime != "" ? this.pageInfo.endTime : undefined, //结束时间
        deliverStatus: 0, //发货状态 0.全部 1.未发货2.出库中 3.部分发货 4.已发货 (如果订单状态为已确认 才判断发货状态)
        frontOrderStatus: this.orderStatus, //订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成  7 待付款
        distributorId: userId,
        page: this.pageInfo.page,
        size: this.pageInfo.count,
      };
      orderDiyLists(params).then((res) => {
        if (res.success) {
          this.orderList = res.data.list;
          this.totalCount = res.data.total;
        }
      });
    },
    // 获取导入订单
    getImpOrderList() {
      var myDate = new Date();
      let pageInfo = {
        appName: this.pageInfo.appName,
        tradeStatus: this.pageInfo.tradeStatus,
        shopName: this.pageInfo.shopName,
        shopCode: this.pageInfo.shopCode,
        orderIdQryStr: this.pageInfo.orderIdQryStr,
        name: this.pageInfo.name,
        startTime: this.pageInfo.startTime,
        endTime: this.pageInfo.endTime,
        page: 1,
        count: this.totalCount,
        searchFrom: "rxShopOrderList",
      };
      this.$api
        .get(
          this,
          "user/u/order/list?" + myDate.getMinutes() + myDate.getSeconds(),
          pageInfo
        )
        .then((res) => {
          if (
            res.code === 0 &&
            res.orders !== undefined &&
            res.orders !== null
          ) {
            this.orderListImp = res.orders;
          } else if (res.code === 3) {
          } else {
            this.orderListImp = [];
          }
        });
    },

    // 搜索
    searchOrder() {
      let startDateFlag =
        this.filters.column.starDate !== undefined &&
        this.filters.column.starDate !== null &&
        this.filters.column.starDate !== "";
      this.pageInfo.startTime = startDateFlag
        ? this.formatTime(this.filters.column.starDate)
        : "";
      let endDateFlag =
        this.filters.column.endDate !== undefined &&
        this.filters.column.endDate !== null &&
        this.filters.column.endDate !== "";
      this.pageInfo.endTime = endDateFlag
        ? this.formatTime(this.filters.column.endDate)
        : "";
      this.$refs.tableRef.clearSelection();
      this.getOrderList();
    },
    formatNumber(n) {
      n = n.toString();
      return n[1] ? n : "0" + n;
    },

    formatTime(number) {
      let date = new Date(number);
      let year = date.getFullYear();
      let month = this.formatNumber(date.getMonth() + 1);
      let day = this.formatNumber(date.getDate());
      let hours = this.formatNumber(date.getHours());
      let Minutes = this.formatNumber(date.getMinutes());
      let Seconds = this.formatNumber(date.getSeconds());
      let format =
        year +
        "-" +
        month +
        "-" +
        day +
        " " +
        hours +
        ":" +
        Minutes +
        ":" +
        Seconds;
      return format;
    },
    changeState(type) {
      this.orderStatus = type;
      this.pageInfo.tradeStatus = type;
      this.pageInfo.page = 1;
      this.getOrderList();
    },
    // 付款状态
    confirmPayStatus(row) {
      switch (row) {
        case 1:
          return this.$t("UserCenter.PendingPayment");
        case 2:
          return this.$t("UserCenter.ToBeShipped");
        case 3:
          return this.$t("UserCenter.PartOfPayment");
        case 4:
          return this.$t("UserCenter.ToBeConfirmed");
        case 5:
          return this.$t("UserCenter.Confirmed");
        case 6:
          return this.$t("UserCenter.PartOfShipped");
        case 7:
          return this.$t("UserCenter.Shipped");
        case 8:
          return this.$t("UserCenter.Closed");
        case 9:
          return this.$t("UserCenter.Completed");
      }
    },
    // 支付状态
    confirmPayWays(val) {
      switch (val) {
        case 1:
          return this.$t("P.Alipay");
        case 2:
          return this.$t("P.WechatPay");
        case 3:
          return this.$t("P.RangeCheckout");
        case 4:
          return this.$t("P.BankTransfer");
        case 5:
          return this.$t("ConfirmOrder.BalancePaid");
        case 6:
          return this.$t("P.EcurrencyPayment");
      }
    },
    // 获取公众号列表-y
    getAppList() {
      // type 平台类型：1 公众号 2 小程序,不传查询全部
      platformList({ page: 1, size: 50, type: 1 }).then((res) => {
        if (res.success) {
          this.appArr = res.data;
        }
      });
    },
    // 公众号下拉列表
    handleSelect(val) {
      if (val === "") {
        this.appId = null;
        this.pageInfo.appName = null;
      } else {
        this.appArr.forEach((item) => {
          if (item.id === val) {
            this.pageInfo.appName = item.name;
            this.appId = item.appId;
            this.platform = item.platform;
          }
        });
      }
      this.getOrderList();
    },

    // 单选时调用
    select(selection, row) {
      this.isSelect = true;
      let d = false;
      for (let i = 0; i < this.multipleSelect.length; i++) {
        if (this.multipleSelect[i].id === row.id) {
          this.multipleSelect.splice(i, 1);
          d = true;
          break;
        }
      }
      if (!d) {
        this.multipleSelect.push(row);
        this.multipleSelect = this.setArr(this.multipleSelect);
      }
    },
    // 去重
    setArr(arr) {
      const obj = {};
      const temp = [];
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].id); // 不加类型 分不清 1 '1'
        if (!obj[arr[i].id + type]) {
          temp.push(arr[i]);
          obj[arr[i].id + type] = true; // 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp;
    },
    // 全选时调用
    selectAll(selection) {
      this.isSelect = true;
      if (selection.length === 0) {
        this.multipleSelect = [];
      } else {
        this.multipleSelect = this.setArr(
          this.multipleSelect.concat(selection)
        );
      }
    },
    // 当切换页面时的作用
    handleSelectionChange(val) {
      this.multipleSelect = val;
    },
    getRowKeys(row) {
      return row.id;
    },
    // 高级搜索显隐
    showSenior() {
      this.senior = !this.senior;
    },
    onCurrentChange(val) {
      // 分页方法
      this.pageInfo.page = val;
      this.getOrderList();
    },
    // 清除门店名称
    handleClear(type) {
      if (type === 1) {
        this.pageInfo.shopName = null;
      } else if (type === 2) {
        this.pageInfo.shopeCode = null;
      } else if (type === 3) {
        this.pageInfo.orderIdQryStr = null;
      } else if (type === 4) {
        this.pageInfo.name = null;
      }
      this.getOrderList();
    },

    openDialog() {
      if (this.orderList.length > 0) {
        this.getImpOrderList();
        this.ExportDialogVisible = true;
      } else {
        this.$message({
          type: "error",
          message: "暂无订单数据",
        });
      }
    },
    // 导出订单
    handleExportOrder() {
      let downData = [];
      if (Number(this.radioType) === 1) {
        // 导出筛选后信息
        for (let i = 0; i < this.multipleSelect.length; i++) {
          // 付款状态
          // this.multipleSelect[i].payStatus = this.confirmPayStatus(this.multipleSelect[i].payStatus)
          // 支付方式
          this.multipleSelect[i].payWay = this.confirmPayWays(
            this.multipleSelect[i].payWay
          );
          // 下单时间
          this.multipleSelect[i].createTime = formatDate(
            new Date(this.multipleSelect[i].createTime),
            "yyyy-MM-dd hh:mm:ss"
          );
          downData.push(this.multipleSelect[i]);
        }
      } else {
        // 导出全部信息
        for (let i = 0; i < this.orderListImp.length; i++) {
          // 付款状态
          // this.orderList[i].payStatus = this.confirmPayStatus(this.orderList[i].payStatus)
          // 支付方式
          this.orderListImp[i].payWay = this.confirmPayWays(
            this.orderListImp[i].payWay
          );
          // 下单时间
          this.orderListImp[i].createTime = formatDate(
            new Date(this.orderListImp[i].createTime),
            "yyyy-MM-dd hh:mm:ss"
          );
          downData.push(this.orderListImp[i]);
        }
      }
      if (downData.length === 0 && Number(this.radioType) === 1) {
        this.$message({
          type: "error",
          message: "未勾选数据",
        });
      } else {
        import("../../assets/js/Export2Excel").then((excel) => {
          const tHeader = [
            "门店名称",
            "门店编码",
            "公众号名称",
            "订单号",
            "收货人",
            "手机号",
            "订单总额",
            "留言",
            "支付方式",
            "下单时间",
          ];
          const filterVal = [
            "shopName",
            "shopCode",
            "appName",
            "id",
            "userName",
            "mobile",
            "orderAmount",
            "remark",
            "payWay",
            "createTime",
          ];
          const data = this.formatJson(filterVal, downData);
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: "店铺列表",
          });
        });
      }
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map((v) =>
        filterVal.map((j) => {
          if (j === "timestamp") {
            return "parseTime(v[j])";
          } else {
            return v[j];
          }
        })
      );
    },
    // 导出订单
    handleExport() {
      let userId = localStorage.getItem("userId");

      let params = {
        shopName: this.pageInfo.shopName || null,
        shopCode: this.pageInfo.shopCode || null,
        orderNo: this.pageInfo.orderIdQryStr
          ? String(this.pageInfo.orderIdQryStr)
          : undefined, //订单编号
        userName: this.pageInfo.name != "" ? this.pageInfo.name : undefined, //收货人
        startTime:
          this.pageInfo.startTime != "" ? this.pageInfo.startTime : undefined, //开始时间
        endTime:
          this.pageInfo.endTime != "" ? this.pageInfo.endTime : undefined, //结束时间
        deliverStatus: 0, //发货状态 0.全部 1.未发货2.出库中 3.部分发货 4.已发货 (如果订单状态为已确认 才判断发货状态)
        frontOrderStatus: this.orderStatus, //订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成  7 待付款
        distributorId: userId,
        page: this.pageInfo.page,
        size: this.pageInfo.count,
      };

      let loading = this.$loading({
        lock: true,
        text: "文件导出中....",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });

      let url =
        this.locationUrl_L +
        "order/v1/web/user/distributor/order/pc/shop/export";
      axios({
        method: "get",
        url: url,
        params: params,
        responseType: "arraybuffer",
        headers: {
          "Content-Type": "application/json",
          token: getToken(),
        },
      }).then((res) => {
        if (res) {
          const content = res.data;
          let blob = new Blob([content], {
            type: "application/ms-excel",
          });
          let url = window.URL.createObjectURL(blob);
          if ("download" in document.createElement("a")) {
            let link = document.createElement("a");
            link.style.display = "none";
            link.href = url;
            link.setAttribute("download", "店铺订单.xls");
            document.body.appendChild(link);
            link.click();
          } else {
            navigator.msSaveBlob(blob, "店铺订单.xls");
          }
          loading.close();
        } else {
          this.$messag.error("导出失败！");
          loading.close();
        }
      });
    },
  },
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.store-order {
  width: 100%;
}
.store-manage {
  .store-main {
    width: 1200px;
    .store-right {
      width: 1030px;
      .content {
        padding: 24px 40px 0;
        border: 2px solid @bdLightColor;
        border-radius: 8px;
      }
      .store-right-title {
        padding-bottom: 10px;
        border-bottom: 1px solid @bdLightColor;
        font-size: 20px;
      }
      .search-box {
        margin-top: 20px;
        .el-input {
          display: inline-block;
          width: 160px;
          /deep/.el-input__inner {
            height: 38px;
            line-height: 38px;
          }
        }
        .el-select {
          display: inline-block;
          width: 120px;
        }
        .el-button {
          height: 38px;
          vertical-align: middle;
        }
        .ml-15 {
          margin-left: 15px;
        }
        .handle-wrap {
          position: absolute;
          right: 0;
          bottom: 15px;
          font-size: 14px;
          color: @blue;
          line-height: 1;
          cursor: pointer;
          &:hover {
            opacity: 0.6;
          }
          .mini-export {
            color: @blue;
          }
          .iconfont {
            margin-right: 2px;
            color: @lighterGray;
          }
        }
      }
      .search-info {
        .date-items {
          line-height: 38px;
        }
        /deep/.el-select {
          margin-left: 20px;
          line-height: 38px;
          .el-input__inner {
            height: 38px;
            line-height: 38px;
          }
        }
      }
      .query-detail {
        margin-top: 30px;
        .nav {
          margin-bottom: 15px;
          width: 100%;
          height: 40px;
          line-height: 40px;
          ul {
            overflow: hidden;
            background-color: @bdLightColor;
            border-radius: 4px;
            &.navEn {
              li {
                width: auto;
                padding: 0 12px;
                font-size: 13px;
              }
            }
            li {
              float: left;
              width: 93px;
              cursor: pointer;
              font-size: 14px;
              color: @lighterBlack;
              text-align: center;
              &:hover,
              &.current {
                color: @white;
                background-color: @blue;
              }
            }
          }
        }
        .export-btn {
          padding-top: 24px;
          color: @blue;
          line-height: 1;
          cursor: pointer;
          .iconfont {
            margin-right: 2px;
            color: @lighterGray;
          }
          &:hover {
            opacity: 0.6;
          }
        }
        .table-box {
          width: 100%;
          margin-top: 20px;
          margin-bottom: 40px;
          /deep/ .el-table {
            width: 100%;
            word-wrap: break-word;
            word-break: break-all;
            border-collapse: collapse;
            tr {
              &:hover {
                background-color: @lightGrayBg;
              }
              th {
                padding: 0;
                height: 32px;
                line-height: 32px;
                text-align: center;
                background-color: @bdLightColor;
                font-size: 12px;
                color: @gray;
                font-weight: normal;
              }
              td {
                height: 53px;
                text-align: center;
                font-size: 12px;
                color: @lightBlack;
                border: none;
                border-top: 1px dashed @bdLighterColor;
                .log {
                  margin: 0 10px;
                  color: @blue;
                  &:hover {
                    opacity: 0.6;
                  }
                }
                .cell {
                  canvas {
                    width: 40px;
                    height: 40px;
                    vertical-align: middle;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  /deep/.el-dialog__wrapper {
    .el-dialog__header {
      padding-top: 30px;
    }
    .el-dialog__body {
      margin: 48px auto 65px;
      width: 80%;
      text-align: center;
      .raido-group {
        .el-radio {
          display: inline-block;
          margin: 0 30px;
          text-align: center;
          .el-radio__input {
            display: none;
          }
          .el-radio__label {
            padding: 0;
            .icon {
              display: inline-block;
              width: 64px;
              height: 64px;
              background-size: 64px 64px;
              background-repeat: no-repeat;
              &.screen {
                background-image: url("../../assets/images/store/icon_screen@2x.png");
              }
              &.all {
                background-image: url("../../assets/images/store/icon_all@2x.png");
              }
            }
            .text {
              margin-top: 10px;
              color: #555555;
            }
          }
          &.is-checked {
            .el-radio__label {
              .screen {
                background-image: url("../../assets/images/store/icon_screen_pre@2x.png");
              }
              .all {
                background-image: url("../../assets/images/store/icon_all_pre@2x.png");
              }
            }
            .text {
              color: #0db8cf;
            }
          }
        }
      }
    }
    .el-dialog__footer {
      padding-bottom: 40px;
      .el-button {
        margin: 0 30px;
        &.el-button--primary {
          background-color: #0db8cf;
          border-color: #0db8cf;
        }
      }
    }
  }
}
</style>
