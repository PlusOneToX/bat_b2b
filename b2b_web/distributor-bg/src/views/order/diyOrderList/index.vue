<template>
  <div class="order-list-wrap">
    <header>
      <h4>订单列表</h4>
    </header>
    <div class="order-list">
      <el-tabs v-model="pageInfo.status">
        <el-tab-pane label="全部" name="0"></el-tab-pane>
        <el-tab-pane label="待支付" name="1"></el-tab-pane>
        <el-tab-pane label="待发货" name="2"></el-tab-pane>
        <el-tab-pane label="待收货" name="3"></el-tab-pane>
        <el-tab-pane label="已失效" name="4"></el-tab-pane>
        <el-tab-pane label="已完成" name="5"></el-tab-pane>
        <el-tab-pane label="已取消" name="7"></el-tab-pane>
      </el-tabs>
      <div class="order-list-fun">
        <div class="order-list-search">
          <div class="order-list-header">
            <div>
              <el-select
                size="mini"
                v-model="pageInfo.orderSource"
                placeholder="订单来源"
                style="width:100px"
                clearable="">
                <el-option label="微信" value="1"></el-option>
                <el-option label="支付宝" value="2"></el-option>
              </el-select>
              <el-select
                size="mini"
                v-model="pageInfo.syncB2bFlag"
                placeholder="是否同步B2B"
                style="width: 120px;"
                clearable>
                <el-option label="已同步" value="1"></el-option>
                <el-option label="未同步" value="2"></el-option>
              </el-select>
              <el-date-picker
                size="mini"
                v-model="pageInfo.time"
                style="width: 330px;"
                type="datetimerange"
                value-format="timestamp"
                range-separator="至"
                start-placeholder="下单开始日期"
                end-placeholder="下单结束日期"></el-date-picker>
            </div>
            <div class="order-list-sear">
              <el-input
                placeholder="B2B分销商编号"
                @change="contentChange"
                @keyup.enter.native="onSearch()"
                clearable
                v-model.trim="pageInfo.content"
                size="mini"
                class="box-search"
              ></el-input>
              <button class="mini-search-btn btn-box" @click.prevent="onSearch()">搜索</button>
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
        :height="tableHeight">
        <el-table-column label="订单号" align="center" prop="id" :min-width="90"></el-table-column>
        <el-table-column label="订单编号" show-overflow-tooltip align="center" prop="orderNo" :min-width="160"></el-table-column>
        <el-table-column label="B2B订单号" align="center" prop="b2bId" :min-width="90"></el-table-column>
        <el-table-column label="订单来源" align="center" prop="orderSource" :formatter="formatOrderSource"  :min-width="90"></el-table-column>
        <el-table-column label="订单状态" align="center" prop="status" :formatter="formatStatus"  :min-width="90"></el-table-column>
        <el-table-column label="用户编号" align="center" prop="user" :min-width="90"></el-table-column>
        <el-table-column label="用户手机号" align="center" prop="userPhone" :min-width="120"></el-table-column>
        <el-table-column label="B2B分销商编号" align="center" prop="b2bDistributorId" :min-width="90"></el-table-column>
        <el-table-column label="留言" align="center" show-overflow-tooltip prop="message" :min-width="120"></el-table-column>
        <el-table-column label="支付方式" align="center" :formatter="formatPayment" prop="payment" :min-width="90"></el-table-column>
        <el-table-column label="下单时间" align="center" :formatter="timeFormatter" :min-width="160"></el-table-column>
        <el-table-column label="操作" :min-width="120" align="center">
          <template slot-scope="scope">
            <!-- <el-button class="mini-search-btn" @click="onEdit(scope.row, scope.$index)">查看</el-button> -->
            <el-button class="mini-search-btn" v-if="scope.row.status === 2 && scope.row.b2bId === null" @click="synOrder(scope.row, scope.$index)">同步B2B</el-button>
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
  </div>
</template>

<script>
import pagination from "@/components/pagination/index";
import { parseTime } from "@/utils/index";

export default {
  name: "diyOrderList",
  components: { pagination },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight * 0.75 - 100); // 计算表高度，固定表头

    var host = window.location.host;
    if (host === "admin.bat.com") {
      // 正式
      this.baseURL = "https://api.bat.com"
    }  else {
      // 测试
      this.baseURL = "https://test.bat.com"
    }
  },
  data() {
    return {
      tableHeight: 600, // 给表格高度一个默认高度，以防没有计算到表格高度
      loading: false,
      synLoading:false,
      tableData: [],
      pageInfo: {
        page: 1,
        size: 10,
        status:undefined,
        orderSource:undefined,
        syncB2bFlag:undefined,

      },
      total: 0,
      baseURL: "",
    }
  },
  methods: {
    formatStatus(row, col, val) {
      // 订单状态
      switch (val) {
        case 1:
          return "待支付"
          break
        case 2:
          return "待发货"
          break
        case 3:
          return "待收货"
          break
        case 4:
          return "已失效"
          break
        case 5:
          return "已完成"
          break
        case 7:
          return "已取消"
          break
        default :
          return "-"
          break;
      }
    },
    formatOrderSource(row, col, val) {
      // 订单状态
      switch (val) {
        case 1:
          return "微信"
          break
        case 2:
          return "支付宝"
          break
        case 6:
          return "公司"
          break
        default :
          return "-"
          break;
      }
    },
    formatPayment(row, col, val) {
      // 订单状态
      switch (val) {
        case 1:
          return "微信"
          break
        case 2:
          return "支付宝"
          break
        default :
          return "-"
          break;
      }
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
      this.getOrderList()
      this.getOrderListCount()
    },

    // onEdit(chosenOne, index) {
    //   // 查看操作
    //   const orderIdList = this.tableData.map(order => order.id);
    //   this.$router.push({
    //     name: "orderDetail",
    //     query: {
    //       orderId: chosenOne.id,
    //       orderG: true
    //     }
    //   });
    // },

    synOrder(row, index){
      this.loading = true
        this.$api.post(this, this.baseURL + "/b2b/synOrder", {id:row.id}).then(res => {
        if (res.status === 200) {
          this.$message({
            message: "同步请求成功，请过等待一分钟后筛选页面",
            type: "success",
            duration: 3 * 1000
          });
        }
        this.loading = false
      })
    },
    // ================== 数据 ==================
    getOrderList() {
      // ..订单列表详情
      this.loading = true;
        this.$api.post(this, this.baseURL + "/b2b/order/list", this.pageInfo).then(res => {
        if (res.status === 200) {
          this.tableData = res.data;
        }
        this.loading = false
      })
    },
    getOrderListCount(){
        this.$api.post(this, this.baseURL + "/b2b/order/list/count", this.pageInfo).then(res => {
        if (res.status === 200) {
          this.total = res.data;
        }
      })
    },
    onSizeCHange(val) {
      // 分页方法
      this.pageInfo.size = val;
      this.pageInfo.page = 1;
      this.getOrderList()
      this.getOrderListCount()
    },
    onCurrentChange(val) {
      // 分页方法
      this.pageInfo.page = val;
      this.getOrderList();
    },
    timeFormatter(row, column, cellValue) {
      // 时间格式化
      return parseTime(row.createTime);
    },
  },
  watch: {
    "pageInfo.status": {
      // 订单状态
      handler() {
        this.pageInfo.page = 1
        this.getOrderList()
        this.getOrderListCount()
      },
      deep: true
    },
    "pageInfo.orderSource": {
      // 订单来源
      handler() {
        this.pageInfo.page = 1
        this.getOrderList()
        this.getOrderListCount()
      },
      deep: true
    },
    "pageInfo.syncB2bFlag": {
      // 是否同步B2B
      handler() {
        this.pageInfo.page = 1
        this.getOrderList()
        this.getOrderListCount()
      },
      deep: true
    },
    "pageInfo.time": function() {
      this.pageInfo.page = 1
      this.getOrderList()
      this.getOrderListCount()
    },
  }
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
          .order-list-sear {
            .box-search {
              width: 300px;
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
