<template>
  <div class="order-distr-wrap">
    <header>
      <h4>长时间未发货订单列表</h4>
      <el-button class="mini-add-btn btn-home" :loading="eLoading" @click="batchExport">批量导出</el-button>
    </header>
    <div class="order-list">
      <div class="order-list-fun">
        <div class="order-list-search">
          <div class="order-list-header">
            <div>
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
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
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
                <el-option label="订单ID" value="3"></el-option>
                <el-option label="订单号" value="1"></el-option>
                <el-option label="分销商用户名" value="2"></el-option>
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
        label="分销商用户"
        show-overflow-tooltip
        align="center"
        prop="distributorName"
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
        prop="payStatus"
        :min-width="100"
      ></el-table-column>
      <el-table-column
        label="支付方式"
        align="center"
        prop="payWay"
        :min-width="100"
      ></el-table-column>
      <el-table-column
        label="订单状态"
        align="center"
        prop="frontOrderStatus"
        :min-width="100"
      ></el-table-column>
      <!-- <el-table-column
        label="同步ERP失败原因"
        align="center"
        prop="currentRates"
        :min-width="140"
      ></el-table-column>
      <el-table-column
        label="同步工厂失败原因"
        align="center"
        prop="currentRates"
        :min-width="140"
      ></el-table-column> -->
      <el-table-column
        label="异常原因"
        align="center"
        prop="exceptionCauses"
        :min-width="140"
      ></el-table-column>
      <el-table-column label="操作" :min-width="150" align="center" fixed="right">
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
  </div>
</template>

<script>
import pagination from "@/components/pagination/index";
import { parseTime } from "@/utils/index";
import { formatJson } from "@/utils/common";
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
    this.getOrderTypes()
    this.getOrderSource()
  },
  activated() {
    this.updateMainData();
  },
  data() {
    return {
      tableHeight: 600, // 给表格高度一个默认高度，以防没有计算到表格高度
      loading: false,
      eLoading: false,
      type: this.$route.query.type,
      tableData: [],
      pageInfo: {
        page: 1,
        size: 10,
        errorType:3, // 长时间未发货订单
        searchType: 8, // 长时间未发货订单
        payStatus: undefined, // 付款状态1
        payWay: undefined, // 支付方式
        contentType: undefined,
        content: undefined // 搜索用关键词
      },
      total: 1,
      showRecordModel: false, // 支付凭证
      recordImg: "", // 支付凭证图片
      orderSources: [], // 订单来源
      orderTypes: []  // 订单类型
    };
  },
  methods: {
    // 获取订单类型
    getOrderTypes() {
      this.$http.orderTypeList(this, {page:1, size:1000}) 
        .then((res) => {
          if (res.success) {
            this.orderTypes = res.data.list;
          }
        });
    },
    // 获取订单来源
    getOrderSource() {
      this.$http.getSysPlatformList(this, {page:1, size:1000}).then((res) => {
        if (res.success) {
          this.orderSources = res.data.list;
        }
      });
    },
    onSearch() {
      // 搜索操作
      this.pageInfo.page = 1;
      this.updateMainData();
    },
    // 同步ERP失败订单列表
    updateMainData() {
      this.loading = true;
      this.$http.syncUndeliveredFailList(this, this.pageInfo).then((res) => {  
        this.tableData = res.data.list;
         this.tableData.forEach(item => {
            item.payStatus = payStatusFormatter(item.payStatus)
            item.payWay = orderPaymentFormatter(item.payWay)
            item.frontOrderStatus = orderStatusFormatter(item.frontOrderStatus)
          })
        this.total = res.data.total
        res.success ? (this.loading = false) : (this.loading = false);
      });
    },
    onEdit(chosenOne, index) {
      // 查看操作
      this.$router.push({
        name: "orderDetail",
        query: {
          orderId: chosenOne.id,
          syncUndeliverG: true,
          type: 7
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
    // 批量导出
    batchExport () {
      this.eLoading = true
      this.pageInfo.page = 1;
      this.pageInfo.size = this.total
      this.$http.syncUndeliveredFailList(this, this.pageInfo).then((res) => {  
        if (res.success) {
          this.eLoading = false
           import('@/utils/Export2Excel').then(excel => {
            let tHeader = [], filterVal = []
            tHeader = ['ID', '订单号', '下单时间', '分销商用户', '支付币种', '下单汇率', '支付凭证', '订单总额', '付款状态', '支付方式', '订单状态', '异常原因']
            filterVal = ['id', 'orderNo', 'createTime', 'distributorName', 'currencyType', 'currentRates', 'voucherImg', 'payAmount', 'payStatus', 'payWay', 'orderStatus', 'exceptionCauses']
            const data = formatJson(filterVal, res.data.list)
            excel.export_json_to_excel({
              header: tHeader,
              data,
              filename: '长时间未发货订单'
            })
          })
        }
      })
    },
     // 查看支付凭证
    showRecord(img) {
      this.showRecordModel = true;
      this.recordImg = img;
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
      return orderStatusFormatter(row.orderStatus);
    },
    timeFormatter(row, column, cellValue) {
      // 时间格式化
      return parseTime(row.createTime);
    }
  },
  watch: {
     "pageInfo.orderTypeId": {
      // 订单类型
      handler(val) {
        this.pageInfo.orderTypeId = val ? val : undefined
        this.pageInfo.page = 1;
        this.updateMainData();
      },
      deep:true
    },
    "pageInfo.orderSource": {
      // 订单来源
      handler(val) {
        this.pageInfo.orderSource = val ? val : undefined
        this.pageInfo.page = 1;
        this.updateMainData();
      },
      deep:true
    },
    "pageInfo.payStatus": {
      // 付款状态
      handler(val) {
        this.pageInfo.payStatus = val ? val : undefined
        this.pageInfo.page = 1;
        this.updateMainData();
      },
      deep: true,
    },
    "pageInfo.payWay": {
      // 支付方式
      handler(val) {
        this.pageInfo.payWay = val ? val : undefined
        this.pageInfo.page = 1;
        this.updateMainData();
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