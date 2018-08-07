<template>
  <div class="order-distr-wrap">
    <header>
      <h4>同步ERP失败订单列表</h4>
      <el-button class="mini-add-btn btn-home" :loading="eloading" @click="batchExport">批量导出</el-button>
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
      row-key="id"
      v-loading="loading"
      :height="tableHeight"
      @select="select"
      @select-all="selectAll"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        align="center"
        type="selection"
        width="50"
        reserve-selection=""
      ></el-table-column>
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
      <el-table-column
        label="同步ERP失败原因"
        align="center"
        prop="currentRates"
        :min-width="140"
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
import axios from 'axios'
import url from '@/api/allUrl'
import { getToken } from '@/utils/auth'
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
      eloading: false,
      loading2: "",
      type: this.$route.query.type,
      tableData: [],
      pageInfo: {
        page: 1,
        size: 10,
        errorType:1, // 同步ERP失败订单
        searchType: 6, // 同步ERP失败订单
        orderTypeId: undefined,
        orderSource: undefined,
        payStatus: undefined, // 付款状态1
        payWay: undefined, // 支付方式
        contentType: undefined,
        content: undefined // 搜索用关键词
      },
      total: 1,
      showRecordModel: false, // 支付凭证
      recordImg: "", // 支付凭证图片
      orderSources: [], // 订单来源
      orderTypes: [],  // 订单类型
      multipleSelect: [],
      isSelect: false
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
      this.multipleSelect = []
      this.$http.syncERPFailList(this, this.pageInfo).then((res) => {  
        this.tableData = res.data.list;
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
          syncErpFailG: true,
          type: 5
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
    // 批量导出
    batchExport () {
      if (this.tableData.length>0) {
        this.eloading = false
        // let loading = this.$loading({
        //   lock: true,
        //   text: "文件导出中....",
        //   spinner: "el-icon-loading",
        //   background: "rgba(0, 0, 0, 0.7)",
        // });
        this.pageInfo.page = 1
        this.pageInfo.size = this.total
        let tenantUrl = localStorage.getItem('tenantUrl');
        axios({
          method: 'GET',
          url: tenantUrl + '/' + url.syncERPFailExport,
          params:this.pageInfo,
          responseType: 'arraybuffer',
          headers: {
            'Content-Type': 'application/json',
            token: getToken()
          }
        }).then((res) => {
          if (res) {
            this.eloading = false
            const content = res.data;
            let blob = new Blob([content], {
              type: "application/ms-excel",
            });1
            let url = window.URL.createObjectURL(blob);
            if ("download" in document.createElement("a")) {
              let link = document.createElement("a");
              link.style.display = "none";
              link.href = url;
              link.setAttribute("download", "同步erp失败订单.xls");
              document.body.appendChild(link);
              link.click();
            } else {
              navigator.msSaveBlob(blob, "同步erp失败订单.xls");
            }
            // loading.close();
          } else {
            this.$messag.error("导出失败！");
          }
        })
        .finally(() => {
          // loading.close();
        });
      } else {
        this.$message({
          type: "error",
          message: "暂无数据",
        });
      }
     
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
      return orderStatusFormatter(row.frontOrderStatus);
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
        console.log('type==', val);
        this.pageInfo.orderTypeId = val ? val : undefined
        this.pageInfo.page = 1;
        this.updateMainData();
      },
      deep:true
    },
    "pageInfo.orderSource": {
      // 订单来源
      handler(val) {
        console.log('orderSource==', val);
        this.pageInfo.orderSource = val ? val :undefined
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