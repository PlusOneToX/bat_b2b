<template>
  <div class="order-distr-wrap">
    <header>
      <h4>分销订单列表</h4>
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
                style="width: 120px"
                clearable
              >
                <el-option label="订单ID" value="6"></el-option>
                <el-option label="订单号" value="1"></el-option>
                <el-option label="收货人" value="3"></el-option>
                <el-option label="分销商用户名" value="4"></el-option>
                <el-option label="收款人" value="5"></el-option>
                <el-option label="手机号" value="8"></el-option>
                <el-option label="快递单号" value="9"></el-option>
              </el-select>
              <el-input
                placeholder="请输入搜索关键字"
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
    </div>
    <el-table
      :data="tableData"
      border
      header-row-class-name="header-row"
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
        :min-width="120"
      ></el-table-column>
      <el-table-column
        label="下单时间"
        align="center"
        :formatter="timeFormatter"
        :min-width="160"
      ></el-table-column>
      <el-table-column
        label="下单分销商"
        show-overflow-tooltip
        align="center"
        prop="orderDistributorName"
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
      <el-table-column label="公司结算金额" align="center" :min-width="120">
        <template slot-scope="scope">
          <i class="asmd" v-show="scope.row.currencyType === 'CNY'"
            >￥:&nbsp;</i
          >
          <i class="asmd" v-show="scope.row.currencyType === 'USD'"
            >$:&nbsp;</i
          >
          <span v-if="scope.row.settlementAmount">{{ scope.row.settlementAmount | NumFormat }}</span>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column
        label="付款状态"
        align="center"
        :formatter="payStatusFormatter"
        :min-width="100"
      ></el-table-column>
      <el-table-column
        label="收款人"
        align="center"
        prop="payee"
        :min-width="90"
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
      <el-table-column label="操作" :min-width="150" align="center">
        <template slot-scope="scope">
          <el-button
            class="mini-search-btn"
            @click="onEdit(scope.row, scope.$index)"
            >查看</el-button
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
</template>

<script>
import pagination from "@/components/pagination/index";
import { parseTime } from "@/utils/index";
import {
  orderStatusFormatter,
  payStatusFormatter,
  orderPaymentFormatter,
} from "@/views/order/orderUtils"; // 格式化引入
export default {
  components: { pagination },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight * 0.75 - 100); // 计算表高度，固定表头
  },
   activated() {
    this.updateMainData();
  },
  data() {
    return {
      tableHeight: 600, // 给表格高度一个默认高度，以防没有计算到表格高度
      loading: false,
      tableData: [],
      pageInfo: {
        frontOrderStatus: 0, // 前端订单状态
        searchType: 2, // 分销订单
        // payStatus: null, // 付款状态
        // payWay: null, // 支付方式
        page: 1,
        size: 10,
        // content: '' // 搜索用关键词
      },
      total: 1
    };
  },
  methods: {
    onSearch() {
      // 搜索操作
      this.pageInfo.page = 1;
      this.updateMainData();
    },
    // 分销订单列表
    updateMainData() {
      this.loading = true;
      this.$http.distributionOrderList(this, this.pageInfo).then((res) => {  
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total
        }
        res.success ? (this.loading = false) : (this.loading = false);
      });
    },
    onEdit(chosenOne, index) {
      // 查看操作
      this.$router.push({
        name: "orderDetail",
        query: {
          orderId: chosenOne.id,
          orderDistrG: true,
          type: 2
        },
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
    // ======== 状态转换 ========
    payStatusFormatter(row) {
      // 付款状态
      return payStatusFormatter(row.payStatus);
    },
    orderPaymentFormatter(row) {
      // 支付方式
      return orderPaymentFormatter(row.payWay);
    },
    orderStatusFormatter(row) {
      // 订单状态
      return orderStatusFormatter(row.frontOrderStatus);
    },
    timeFormatter(row, column, cellValue) {
      // 时间格式化
      return parseTime(row.createTime);
    }
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
    "pageInfo.payStatus": {
      // 付款状态
      handler(val, oldVal) {
        if (val !== oldVal) {
          this.pageInfo.page = 1;
          this.updateMainData();
        }
      },
      deep: true,
    },
    "pageInfo.payWay": {
      // 支付状态
      handler(val, oldVal) {
        if (val !== oldVal) {
          this.pageInfo.page = 1;
          this.updateMainData();
        }
      },
      deep: true,
    },
    "pageInfo.time": {
      handler(val) {
        if (val) {
          this.pageInfo.startTime = parseTime(val[0])
          this.pageInfo.endTime = parseTime(val[1])
        } else {
          this.pageInfo.startTime = undefined
        this.pageInfo.endTime = undefined
        }
        this.pageInfo.page = 1;
        this.updateMainData();
      },
      deep: true,
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.order-distr-wrap {
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
    margin-top:20px;
    .order-list-fun {
      padding: 0px 10px 10px 10px;
      overflow: hidden;
      .order-list-search {
        overflow: hidden;
        .order-list-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
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
  .btn-footer{
    text-align: center;
    .el-button{
      margin: 0 20px;
    }
  }
}
</style>