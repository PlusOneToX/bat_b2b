<template>
  <div class="order-list-wrap">
    <header>
      <h4>订单列表</h4>
    </header>
    <div class="order-list">
      <el-tabs v-model="pageInfo.frontOrderStatus">
        <el-tab-pane label="全部" name="0"></el-tab-pane>
        <el-tab-pane label="待确认" name="1"></el-tab-pane>
        <el-tab-pane label="待发货" name="2"></el-tab-pane>
        <el-tab-pane label="出库中" name="9"></el-tab-pane>
        <el-tab-pane label="部分发货" name="3"></el-tab-pane>
        <el-tab-pane label="待收货" name="4"></el-tab-pane>
        <el-tab-pane label="已关闭" name="5"></el-tab-pane>
        <el-tab-pane label="已完成" name="6"></el-tab-pane>
      </el-tabs>
      <div class="order-list-fun">
        <div class="order-list-search">
          <div class="order-list-header">
            <div>
              <el-select
                size="mini"
                v-model="pageInfo.stockType"
                placeholder="库存类别"
                style="width: 100px"
                clearable
              >
                <el-option label="在库订单" value="1"></el-option>
                <el-option label="在途订单" value="2"></el-option>
                <el-option label="预售(mto)" value="3"></el-option>
                <el-option label="在途+在库" value="4"></el-option>
                <el-option label="委外订单" value="5"></el-option>
              </el-select>
              <el-select
                size="mini"
                v-model="pageInfo.autoDelivery"
                placeholder="是否直发"
                style="width: 100px"
                clearable
              >
                <el-option label="是" value="1"></el-option>
                <el-option label="否" value="0"></el-option>
              </el-select>
              <el-select
                class="custom_select"
                placeholder="订单类型"
                size="mini"
                style="width: 120px"
                v-model="pageInfo.orderTypeId"
                clearable
              >
                <el-option
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                  v-for="item in orderTypes"
                ></el-option>
              </el-select>
              <el-select
                class="custom_select"
                filterable
                placeholder="订单来源"
                size="mini"
                style="width: 120px"
                v-model="pageInfo.orderSource"
                clearable
              >
                <el-option
                  :key="item.platformNo"
                  :label="item.name"
                  :value="item.platformNo"
                  v-for="item in orderSources"
                ></el-option>
              </el-select>
              <el-select
                size="mini"
                v-model="pageInfo.payWay"
                placeholder="支付方式"
                style="width: 100px"
                clearable
              >
                <el-option label="支付宝" value="1"></el-option>
                <el-option label="微信" value="2"></el-option>
                <el-option label="区间结算" value="3"></el-option>
                <el-option label="线下转账" value="4"></el-option>
                <el-option label="余额支付" value="5"></el-option>
                <el-option label="快钱支付" value="6"></el-option>
                <el-option label="余额+支付宝" value="7"></el-option>
                <el-option label="余额+微信" value="8"></el-option>
                <el-option label="余额+快钱支付" value="9"></el-option>
              </el-select>
              <el-select
                size="mini"
                v-model="pageInfo.payStatus"
                placeholder="付款状态"
                style="width: 100px"
                clearable
              >
                <el-option label="未付款" value="1"></el-option>
                <el-option label="部分付款" value="2"></el-option>
                <el-option label="已付款" value="3"></el-option>
                <el-option label="部分退款" value="4"></el-option>
                <el-option label="退款申请中" value="5"></el-option>
                <el-option label="已退款" value="6"></el-option>
              </el-select>
              <el-select
                size="mini"
                v-model="pageInfo.syncErpFlag"
                placeholder="是否同步ERP"
                style="width: 120px"
                clearable
              >
                <el-option label="全部" value="0"></el-option>
                <el-option label="已同步" value="1"></el-option>
                <el-option label="未同步" value="2"></el-option>
              </el-select>
              <!---是否收取服务费--->
              <!-- <el-select
                size="mini"
                v-model="pageInfo.isCollectFee"
                placeholder="是否收取服务费"
                style="width: 120px"
                clearable
              >
                <el-option label="全部" value="-1"></el-option>
                <el-option label="是" value="1"></el-option>
                <el-option label="否" value="0"></el-option>
              </el-select> -->
              <el-date-picker
                size="mini"
                v-model="pageInfo.time"
                style="width: 330px"
                type="datetimerange"
                value-format="timestamp"
                range-separator="至"
                start-placeholder="下单开始日期"
                end-placeholder="下单结束日期"
              ></el-date-picker>
            </div>
            <div class="order-list-sear">
              <el-select
                size="mini"
                v-model="pageInfo.contentType"
                placeholder="类型"
                style="width: 100px"
                clearable
              >
                <el-option label="订单ID" value="6"></el-option>
                <el-option label="订单号" value="1"></el-option>
                <el-option label="ERP订单号" value="2"></el-option>
                <el-option label="收货人" value="3"></el-option>
                <el-option label="分销商用户名" value="4"></el-option>
                <el-option label="订单金额" value="5"></el-option>
                <el-option label="第三方编号" value="7"></el-option>
                <el-option label="手机号" value="8"></el-option>
                <el-option label="快递单号" value="9"></el-option>
              </el-select>
              <el-input
                placeholder="请输入搜索关键字"
                @change="contentChange"
                @keyup.enter.native="onSearch()"
                clearable
                v-model.trim="pageInfo.content"
                size="mini"
                class="box-search"
              ></el-input>
              <button
                class="mini-search-btn btn-box"
                @click.prevent="onSearch()"
              >
                搜索
              </button>
            </div>
          </div>
        </div>
      </div>

      <el-table
        :data="tableData"
        border
        header-row-class-name="header-row"
        @selection-change="onSelectionChange"
        class="tableCenter"
        v-loading="loading"
        :height="tableHeight"
      >
        <el-table-column
          label="ID"
          align="center"
          prop="id"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="订单号"
          align="center"
          prop="orderNo"
          :min-width="140"
        ></el-table-column>
        <el-table-column
          label="ERP订单号"
          align="center"
          prop="orderErpNo"
          :min-width="150"
        ></el-table-column>
        <el-table-column
          label="第三方编号"
          show-overflow-tooltip
          align="center"
          :min-width="110"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.orderThirdpartyNo === null">-</span>
            <span v-else>{{ scope.row.orderThirdpartyNo }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="工厂编号" show-overflow-tooltip align="center" :min-width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.productOrderNo === null">-</span>
            <span v-else>{{scope.row.productOrderNo}}</span>
          </template>
        </el-table-column> -->
        <!-- <el-table-column label="订单类别" prop="" :formatter="category" :min-width="150"> </el-table-column> -->
        <el-table-column
          label="下单时间"
          align="center"
          :formatter="timeFormatter"
          :min-width="160"
        ></el-table-column>
        <el-table-column
          label="下单分销商用户"
          show-overflow-tooltip
          align="center"
          prop="orderDistributorName"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="同步ERP分销商用户"
          show-overflow-tooltip
          align="center"
          prop="syncErpDistributorName"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="收货人"
          align="center"
          prop="userName"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="支付币种"
          align="center"
          prop="currencyType"
          :min-width="90"
        ></el-table-column>
        <el-table-column
          label="下单汇率"
          align="center"
          prop="currentRates"
          :min-width="90"
        ></el-table-column>
        <el-table-column label="支付凭证" align="center" :min-width="90">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              v-if="scope.row.voucherImg && scope.row.voucherImg !== null"
              @click="showRecord(scope.row.voucherImg)"
              >查看</el-button
            >
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="订单总额" align="center" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd" v-show="scope.row.currencyType === 'CNY'"
              >￥:&nbsp;</i
            >
            <i class="asmd" v-show="scope.row.currencyType === 'USD'"
              >$:&nbsp;</i
            >
            {{ scope.row.payAmount | NumFormat }}
          </template>
        </el-table-column>
        <el-table-column
          label="付款状态"
          align="center"
          :formatter="payStatusFormatter"
          :min-width="100"
        ></el-table-column>
        <el-table-column
          label="支付方式"
          align="center"
          :formatter="orderPaymentFormatter"
          :min-width="100"
        ></el-table-column>
        <el-table-column
          label="订单状态"
          align="center"
          :formatter="orderStatusFormatter"
          :min-width="100"
        ></el-table-column>
        <!-- <el-table-column label="发货状态"  :formatter="deliverStatusFormatter"> </el-table-column> -->
        <el-table-column label="操作" :min-width="150" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              class="mini-search-btn"
              @click="onEdit(scope.row, scope.$index)"
              >查看</el-button
            >
            <!-- shopify 海外站 -->
            <el-button
              class="mini-browse-btn"
              v-if="
                Number(scope.row.orderSource) === 50 &&
                (scope.row.orderStatus === 2 || scope.row.orderStatus === 3)
              "
              @click="handleDelivery(scope.row, scope.$index)"
              >发货</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="total"
        :page="pageInfo.page"
        @sizeChange="onSizeCHange"
        @currentChange="onCurrentChange"
      ></pagination>
    </div>

    <!-- 支付凭证 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="showRecordModel"
      width="30%"
      class="record-wrap"
      @close="showRecordModel = false"
    >
      <div class="record-img">
        <img :src="recordImg" alt="支付凭证" />
      </div>
    </el-dialog>

    <!-- 发货 -->
    <deliveryDialog
      v-if="deliveryShow"
      :pageFlag="'orderList'"
      :curOrderId="curOrderId"
      :curProductOrderNo="curProductOrderNo"
    ></deliveryDialog>
  </div>
</template>

<script>
import eventBus from "@/views/order/eventBus";
import pagination from "@/components/pagination/index";
import { parseTime } from "@/utils/index";
import {
  orderStatusFormatter,
  payStatusFormatter,
  orderPaymentFormatter,
  deliverStatusFormatter,
  category,
} from "@/views/order/orderUtils"; // 格式化引入

import deliveryDialog from "./orderDelivery";

export default {
  name: "applyForPriceDetail",
  components: { pagination, deliveryDialog },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight * 0.75 - 100); // 计算表高度，固定表头
    this.getOrderTypes();
    this.getOrderSource();
  },
  activated() {
    eventBus.$on("shouldUpdateData", (_) => {
      // 生成发货单后更新页面数据
      this.updateMainData();
    });
    this.updateMainData();
  },
  beforeDestroy() {
    // 实例销毁之前执行的钩子 (此处用来销毁eventbus的创建)
    eventBus.$off("shouldUpdateData");
  },
  data() {
    return {
      tableHeight: 600, // 给表格高度一个默认高度，以防没有计算到表格高度
      loading: false,
      chosenOrders: [], // 表格中被勾选的订单
      tableData: [],
      orderTypes: [], //订单类型
      orderSources: [],
      resettingQuantityLoading: false,
      pageInfo: {
        frontOrderStatus: 0, // 前端订单状态
        searchType: 1, // 订单列表
        // orderStatus: '0', // 订单状态
        // payStatus: null, // 付款状态
        // payWay: null, // 支付方式
        // deliverStatus: null, // 发货状态
        // syncErpFlag: null, // 是否同步ERP
        page: 1,
        size: 10
        // content: '' // 搜索用关键词
      },
      total: 1,
      showRecordModel: false, // 支付凭证
      recordImg: "", // 支付凭证图片
      deliveryShow: false,
      curOrderId: "",
      curProductOrderNo: "",
    };
  },
  methods: {
    // 获取订单来源
    getOrderSource() {
      this.$http.getSysPlatformList(this, {page:1, size:1000}).then((res) => {
        if (res.success) {
          this.orderSources = res.data.list;
        }
      });
    },
    // 获取订单类型
    getOrderTypes() {
      this.$http.orderTypeList(this, {page:1, size:1000}) 
        .then((res) => {
          if (res.success) {
            this.orderTypes = res.data.list;
          }
        });
    },
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.onSearch();
      }
    },
    // ======== 操作 ========
    onSearch() {
      // 搜索操作
      this.pageInfo.page = 1;
      this.updateMainData();
    },

    onEdit(chosenOne, index) {
      // 查看操作
      const orderIdList = this.tableData.map((order) => order.id);
      this.$router.push({
        name: "orderDetail",
        query: {
          orderId: chosenOne.id,
          orderG: true,
          type: 1
        },
      });
    },

    // ================== 数据 ==================
    updateMainData() {
      // ..订单列表
      this.loading = true;
      this.$http.orderList(this, this.pageInfo).then((res) => {  
        this.tableData = res.data.list;
        this.total = res.data.total
        res.success ? (this.loading = false) : (this.loading = false);
      });
    },

    onSizeCHange(val) {
      // 分页方法
      this.pageInfo.size = val;
      this.updateMainData();
    },

    onCurrentChange(val) {
      // 分页方法
      this.pageInfo.page = val;
      this.updateMainData();
    },

    onSelectionChange(val) {
      // 表格勾选中的值
      this.chosenOrders = val;
    },

    // ======== 状态转换 ========
    payStatusFormatter(row) {
      // 付款状态
      return payStatusFormatter(row.payStatus);
    },

    orderStatusFormatter(row) {
      // 订单状态
      return orderStatusFormatter(row.frontOrderStatus);
    },

    orderPaymentFormatter(row) {
      // 支付方式
      return orderPaymentFormatter(row.payWay);
    },

    deliverStatusFormatter(row) {
      // 发货状态
      return deliverStatusFormatter(row.deliverStatus);
    },

    timeFormatter(row, column, cellValue) {
      // 时间格式化
      return parseTime(row.createTime);
    },

    category(row) {
      // 订单类别
      return category(row.category);
    },
    // 查看支付凭证
    showRecord(img) {
      this.showRecordModel = true;
      this.recordImg = img;
    },
    // 发货
    handleDelivery(row, index) {
      this.curOrderId = row.id; // 获取当前操作订单号
      this.curProductOrderNo = row.productOrderNo; // 获取当前操作工厂订单号
      this.deliveryShow = true;
    },
  },
  watch: {
    "pageInfo.frontOrderStatus": {
      // 前端订单状态
      handler(val, oldVal) {
        if (val !== oldVal) {
          if (val !== oldVal) {
            this.pageInfo.page = 1;
            this.updateMainData();
          }
        }
      },
      deep:true
    },
    "pageInfo.orderStatus": {
      // 发货状态
       handler(val, oldVal) {
        if (val !== oldVal) {
          this.pageInfo.page = 1;
          this.updateMainData();
        }
      },
      deep:true
    },
    "pageInfo.payStatus": {
      // 付款状态
       handler(val, oldVal) {
        if (val !== oldVal) {
          this.pageInfo.page = 1;
          this.updateMainData();
        }
      },
      deep:true
    },
    "pageInfo.payWay": {
      // 支付状态
      handler(val, oldVal) {
        if (val !== oldVal) {
          this.pageInfo.page = 1;
          this.updateMainData();
        }
      },
      deep:true
    },
    "pageInfo.stockType": {
      // 订单类别
       handler(val, oldVal) {
        if (val !== oldVal) {
          this.pageInfo.page = 1;
          this.updateMainData();
        }
      },
      deep:true
    },
    "pageInfo.syncErpFlag": {
      // 是否同步ERP
       handler(val, oldVal) {
         if (val !== oldVal) {
          this.pageInfo.page = 1;
          this.updateMainData();
        }
      },
      deep:true
    },
    "pageInfo.orderTypeId": {
      // 订单类型
       handler(val, oldVal) {
        if (val !== oldVal) {
          this.pageInfo.page = 1;
          this.updateMainData();
        }
      },
      deep:true
    },
    "pageInfo.orderSource": {
      // 订单来源
       handler(val, oldVal) {
        if (val === '') {
          this.pageInfo.orderSource = undefined
        } else if (val !== oldVal) {
          this.pageInfo.page = 1;
          this.updateMainData();
        }
      },
      deep:true
    },
    "pageInfo.time": {
     handler(val, oldVal) {
        if (val) {
          this.pageInfo.startTime = parseTime(val[0])
          this.pageInfo.endTime = parseTime(val[1])
        } else {
          this.pageInfo.startTime = undefined
          this.pageInfo.endTime = undefined
        }
        if (val !== oldVal) {
          this.pageInfo.page = 1;
          this.updateMainData();
        }
      },
      deep:true
    },
    "pageInfo.autoDelivery": {
      handler(val, oldVal) {
        if (val !== oldVal) {
          this.pageInfo.page = 1;
          this.updateMainData();
        }
      },
      deep:true
    },
    // "pageInfo.isCollectFee": {
    //   // 是否收取服务费
    //   handler() {
    //     this.pageInfo.page = 1;
    //     this.updateMainData();
    //   },
    //   deep: true,
    // },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.order-list-wrap {
  height: 100%;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    h4 {
      display: inline-block;
      font-weight: 350;
      font-size: 16px;
      margin: 0 20px;
    }
  }
  .order-list {
    background-color: white;
    height: 100%;
    .order-list-fun {
      padding: 0px 10px 10px 10px;
      overflow: hidden;
      .order-list-search {
        overflow: hidden;
        .order-list-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          .el-select{
            margin-bottom:10px;
          }
          .order-list-sear {
            .box-search {
              width: 180px;
            }
            .btn-box {
              position: relative;
              top: -1px;
            }
          }
        }
      }
    }
  }
}

// 支付凭证
.record-wrap {
  .record-img {
    text-align: center;
    img {
      max-width: 100%;
      max-height: 100%;
    }
  }
}
</style>
