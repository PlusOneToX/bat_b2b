<template>
  <div class="service-orderlist">
    <header class="header">
      <h4>服务费活动订单列表</h4>
      <el-button class="mini-add-btn btn-right" @click="handleExport(2)">导出全部信息</el-button>
      <el-button class="mini-add-btn btn-right" @click="handleExport(1)">导出筛选后信息</el-button>
    </header>
    <div class="order-list">
      <el-tabs v-model="pageInfo.orderStatus" @tab-click="handleOrderStatus">
        <el-tab-pane label="全部" name="0"></el-tab-pane>
        <el-tab-pane label="待确认" name="1"></el-tab-pane>
        <el-tab-pane label="待发货" name="2"></el-tab-pane>
        <el-tab-pane label="待收货" name="4"></el-tab-pane>
        <el-tab-pane label="已关闭" name="5"></el-tab-pane>
        <el-tab-pane label="已完成" name="6"></el-tab-pane>
      </el-tabs>
      <div class="order-list-fun">
        <div class="order-list-search">
          <div class="order-list-header">
            <div>
              <el-date-picker
                size="mini"
                v-model="pageInfo.time"
                style="width: 330px;"
                type="datetimerange"
                value-format="timestamp"
                range-separator="至"
                start-placeholder="下单开始日期"
                end-placeholder="下单结束日期"></el-date-picker>
              <el-select
                size="mini"
                v-model="pageInfo.payStatus"
                placeholder="付款状态"
                style="width:100px"
                clearable>
                <el-option label="全部" value="0"></el-option>
                <el-option label="未付款" value="1"></el-option>
                <el-option label="已付款" value="3"></el-option>
                <el-option label="退款中" value="6"></el-option>
                <el-option label="已退款" value="4"></el-option>
              </el-select>
              <el-select
                size="mini"
                v-model="pageInfo.orderGoodsType"
                placeholder="库存类别"
                style="width:100px"
                clearable>
                <el-option label="全部" value="0"></el-option>
                <el-option label="在途订单" value="1"></el-option>
                <el-option label="在库订单" value="2"></el-option>
              </el-select>
              <el-select
                size="mini"
                v-model="pageInfo.payWay"
                placeholder="支付方式"
                style="width:100px"
                clearable>
                <el-option label="全部" value="0"></el-option>
                <el-option label="支付宝" value="1"></el-option>
                <el-option label="微信" value="2"></el-option>
                <el-option label="区间结算" value="3"></el-option>
                <el-option label="银行转账" value="4"></el-option>
                <el-option label="余额支付" value="5"></el-option>
                <el-option label="网银支付" value="6"></el-option>
              </el-select>
              </div>
              <div class="order-list-sear">
              <el-input
                placeholder="分销商用户名/分销商编码/门店名称/门店编码/收货人/订单号/公众号名称"
                @clear="clearKeyWord"
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
        :height="tableHeight"
        row-key="id"
        ref="multipleSelect"
        @select="select"
        @select-all="selectAll"
        @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="50" reserve-selection=""></el-table-column>
        <el-table-column label="分销商用户名" show-overflow-tooltip align="center" prop="distributorName" :min-width="160"></el-table-column>
        <el-table-column label="分销商编码" show-overflow-tooltip align="center" prop="distributorId" :min-width="160"></el-table-column>
        <el-table-column label="门店名称" show-overflow-tooltip align="center" prop="shopName" :min-width="160"></el-table-column>
        <el-table-column label="门店编码" show-overflow-tooltip align="center" prop="shopCode" :min-width="160"></el-table-column>
         <el-table-column label="公众号名称" show-overflow-tooltip align="center" prop="appName" :min-width="160"></el-table-column>
        <el-table-column label="订单编号" show-overflow-tooltip align="center" prop="id" :min-width="160"></el-table-column>
        <el-table-column label="收货人" show-overflow-tooltip align="center" prop="userName" :min-width="160"></el-table-column>
        <el-table-column label="收货人手机号" align="center" prop="mobile" :min-width="120"></el-table-column>
        <el-table-column label="订单总额" align="center" prop="orderAmount"  :min-width="90"></el-table-column>
        <el-table-column label="付款状态" align="center" prop="payStatus" :formatter="formatPayStatus" :min-width="90"></el-table-column>
        <el-table-column label="留言" align="center" show-overflow-tooltip prop="remark" :min-width="120"></el-table-column>
        <el-table-column label="支付方式" align="center" :formatter="formatPayment" prop="payWay" :min-width="90"></el-table-column>
        <el-table-column label="下单时间" align="center" :formatter="timeFormatter" :min-width="160"></el-table-column>
      </el-table>
      <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    </div>
  </div>
</template>

<script>
import { parseTime } from "@/utils/index"
import page from "@/components/pagination"
import {
  payStatusFormatter,
  orderPaymentFormatter
} from "@/views/order/orderUtils" // 格式化引入
export default {
  components: { page },
  mounted() {
    const documentHeight = document.body.clientHeight
    this.tableHeight = parseInt(documentHeight * 0.75 - 100) // 计算表高度，固定表头
    this.getOrderList()
    this.getOrderListCount()
  },
  data() {
    return {
      tableHeight: 600, // 给表格高度一个默认高度，以防没有计算到表格高度
      loading: false,
      synLoading:false,
      tableData: [],
      tableAllData: [],
      multipleSelect: [],
      pageInfo: {
        page: 1,
        count: 10,
        orderStatus: 0, // 订单状态
        payWay: null, // 支付方式
        payStatus: null, // 支付状态
        time:[], // 时间
        content: null, // 搜索关键字
        orderGoodsType: null, // 库存类别
        isCollectFee: 1   // 收取服务费
      },
      total: 0
    }
  },
  methods: {
    // 切换订单状态
    handleOrderStatus (tab, event) {
      this.pageInfo.orderStatus = tab.name
      this.getOrderList()
      this.getOrderListCount()
    },
    // 导出信息
    handleExport (type) {
      let downData = [];
      if (type === 1) {
        // 导出筛选后信息
        for (let i = 0; i < this.multipleSelect.length; i++) {
           // 付款状态
          this.multipleSelect[i].payStatus = this.formatPayStatus(null, null, this.multipleSelect[i].payStatus)
          // 支付方式
          this.multipleSelect[i].payWay = this.formatPayment(null, null, this.multipleSelect[i].payWay)
          // 下单时间
          this.multipleSelect[i].createTime = parseTime(this.multipleSelect[i].createTime)
          downData.push(this.multipleSelect[i])
        }
        this.ExportTable(downData, type)
      } else {
        let tableAllData = []
        // 导出全部信息
        this.$api.post(this, "/admin/u/p/order/list", {
          page: 1,
          count: this.total,
          orderStatus: 0,
          searchFrom: 'rxShopOrderList'
        }).then(res => {
          if(res.code === 0) {
            tableAllData = res.orders
            for (let i = 0; i < tableAllData.length; i++) {
              // 付款状态
              tableAllData[i].payStatus = this.formatPayStatus(null, null, tableAllData[i].payStatus)
              // 支付方式
              tableAllData[i].payWay = this.formatPayment(null, null, tableAllData[i].payWay)
              // 下单时间
              tableAllData[i].createTime = parseTime(tableAllData[i].createTime)
              downData.push(tableAllData[i])
            }
            this.ExportTable(downData, type)
          }
        }).catch(err => {
          console.log(err)
        })
      }
    },
    ExportTable (downData, type) {
       if (downData.length === 0 && type === 1) {
        this.$message({
          type: "error",
          message: "未勾选数据"
        })
      } else {
        //  import('./Export2Excel').then(excel => {
        //   const tHeader = ['分销商用户名', '分销商编码', '门店名称', '门店编码', '公众号名称', '订单编号', '收货人', '收货人手机号', '订单总额', '付款状态', '留言', '支付方式', '下单时间']
        //   const filterVal = ['distributorName', 'distributorId', 'shopName', 'shopCode', 'appName', 'id', 'userName', 'mobile', 'orderAmount', 'payStatus', 'remark', 'payWay', 'createTime']
        //   const data = this.formatJson(filterVal, downData)
        //   excel.export_json_to_excel({
        //     header: tHeader,
        //     data,
        //     filename: '店铺订单列表'
        //   })
        // })
      }
    },
    formatJson (filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return 'parseTime(v[j])'
        } else {
          return v[j]
        }
      }))
    },
    // 付款状态
    formatPayStatus(row, col, val) {
      return payStatusFormatter(val)
    },
    // 支付方式
    formatPayment(row, col, val) {
      return orderPaymentFormatter(val)
    },
    clearKeyWord () {
      this.pageInfo.content = ''
      this.getOrderList()
      this.getOrderListCount()
    },
    clearPayStatus () {
      this.pageInfo.payStatus = null
    },
    // 更换付款状态
    changePayStatus (val) {
      this.pageInfo.payStatus = val
      this.getOrderList()
      this.getOrderListCount()
    },
    // 搜索
    onSearch() {
      this.pageInfo.page = 1
      this.getOrderList()
      this.getOrderListCount()
    },
    // 获取订单列表
    getOrderList() {
      this.multipleSelect = []
      // 订单列表详情
      this.loading = true;
      this.$api.post(this, "/admin/u/p/order/list", this.pageInfo).then(res => {
        if (res.code === 0) {
          this.tableData = res.orders
        }
        this.loading = false
      })
    },
    // 获取订单总数
    getOrderListCount(){
        this.$api.post(this, "/admin/u/p/order/count", this.pageInfo).then(res => {
        if (res.code === 0) {
          this.total = res.count
        }
      })
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
        this.tableData.forEach((row) => {
          for (let i = 0; i < this.multipleSelect.length; i++) {
            if (this.multipleSelect[i].id === row.id) {
              this.multipleSelect.splice(i, 1);
              break;
            }
          }
        });
      } else {
        this.multipleSelect = this.setArr(
          this.multipleSelect.concat(selection)
        );
      }
    },
     // 当切换页面时的作用
    handleSelectionChange(val) {
      if (
        val.length === 0 &&
        this.multipleSelect.length != 0 &&
        !this.isSelect
      ) {
        this.multipleSelect.forEach((row1) => {
          this.tableData.forEach((row2) => {
            if (row1.id === row2.id) {
              this.$refs.multipleSelect.toggleRowSelection(row2);
            }
          });
        });
        this.isSelect = true;
      }
    },
    sizeChange(val) {
      // 分页方法
      this.pageInfo.size = val;
      this.pageInfo.page = 1;
      this.getOrderList()
      // this.getOrderListCount()
    },
    currentChange(val) {
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
    "pageInfo.payStatus": {
      // 付款状态
      handler() {
        this.pageInfo.page = 1;
        this.getOrderList()
        this.getOrderListCount()
      },
      deep: true
    },
    "pageInfo.payWay": {
      // 支付状态
      handler() {
        this.pageInfo.page = 1;
        this.getOrderList()
        this.getOrderListCount()
      },
      deep: true
    },

    "pageInfo.orderGoodsType": {
      // 订单类别
      handler() {
        this.pageInfo.page = 1;
        this.getOrderList()
        this.getOrderListCount()
      },
      deep: true
    },
    "pageInfo.time": function() {
      this.pageInfo.page = 1;
      this.getOrderList()
      this.getOrderListCount()
    }
  }
}
</script>

<style  rel="stylesheet/scss" lang="scss" scoped>
  .service-orderlist {
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