<template>
  <div class="order-distr-wrap">
    <header>
      <h4>柔性同步订单列表</h4>
    </header>
    <div class="order-list">
      <div class="order-list-fun">
        <div class="order-list-search">
          <div class="order-list-header">
            <div>
               <el-select
                size="mini"
                v-model="pageInfo.payStatus"
                placeholder="渠道来源"
                style="width: 100px"
                clearable
              >
                <el-option label="全部" value="0"></el-option>
                <el-option label="未付款" value="1"></el-option>
              </el-select>
              <el-select
                size="mini"
                v-model="pageInfo.payWay"
                placeholder="同步状态"
                style="width: 100px"
                clearable
              >
                <el-option label="同步成功" value="1"></el-option>
                <el-option label="同步失败" value="2"></el-option>
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
                v-model="pageInfo.searchType"
                placeholder="类型"
                style="width: 120px"
                clearable
              >
                <el-option label="渠道订单号" value="1"></el-option>
                <el-option label="收货人" value="4"></el-option>
                <el-option label="手机号" value="3"></el-option>
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
        label="渠道来源"
        align="center"
        prop="userSourceName"
        :min-width="120"
      ></el-table-column>
      <el-table-column
        label="渠道订单号"
        align="center"
        prop="orderNo"
        :min-width="120"
      ></el-table-column>
      <el-table-column
        label="收货人"
        align="center"
        prop="userName"
        :min-width="120"
      ></el-table-column>
      <el-table-column
        label="手机号"
        align="center"
        prop="mobile"
        :min-width="120"
      ></el-table-column>
      <el-table-column
        label="地址"
        align="center"
        prop="address"
        :min-width="120"
      ></el-table-column>
      <el-table-column
        label="下单时间"
        align="center"
        :formatter="timeFormatter"
        :min-width="160"
      ></el-table-column>
      <el-table-column
        label="订单同步状态"
        align="center"
        prop=""
        :min-width="100"
      ></el-table-column>
      <el-table-column
        label="异常原因"
        align="center"
        prop="currentRates"
        :min-width="90"
      ></el-table-column>
       <el-table-column
        label="B2B订单号"
        align="center"
        prop="orderNo"
        :min-width="120"
      ></el-table-column>
      <el-table-column label="操作" :min-width="200" align="center">
        <template slot-scope="scope">
          <el-button
            class="mini-search-btn"
            @click="onEdit(scope.row, scope.$index)"
            >查看</el-button
          >
          <el-button
            class="mini-search-btn"
            @click="handleSync(scope.row, scope.$index)"
            >同步订单</el-button
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
     <!-- 查看详情弹框 -->
      <el-dialog title="同步订单详情" :modal-append-to-body="false" :visible="dialogShow" width="50%" :before-close="closeDialog">
        <div class="box-has-border">
          <div class="text-center title" style="margin-top: 10px">
          <span>订单信息</span>
          </div>
          <div class="half-width" >
            <el-form :model="formData" label-width="280px" ref="formData">
              <el-form-item label="渠道单号：">
                <div>{{formData.name}}</div>
              </el-form-item>
              <el-form-item label="下单时间：">
                <div>{{formData.businessFunction}}</div>
              </el-form-item>
              <el-form-item label="同步状态：">
                <div>{{formData.operateDes}}</div>
              </el-form-item>
            </el-form>
          </div>
          <div class="half-width">
            <el-form :model="formData" label-width="280px" ref="formData">
              <el-form-item label="渠道来源：">
                <div>{{formData.name}}</div>
              </el-form-item>
              <el-form-item label="异常原因：">
                <div>{{formData.businessFunction}}</div>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <div class="box-has-border">
          <div class="text-center title" style="margin-top: 10px">
          <span>收货人信息</span>
          </div>
          <div class="half-width">
            <el-form :model="formData" label-width="280px" ref="formData">
              <el-form-item label="收件人：">
                <div>{{formData.name}}</div>
              </el-form-item>
              <el-form-item label="收货地址：">
                <div>{{formData.businessFunction}}</div>
              </el-form-item>
            </el-form>
          </div>
          <div class="half-width">
            <el-form :model="formData" label-width="280px" ref="formData">
              <el-form-item label="手机号：">
                <div>{{formData.name}}</div>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <el-button class="mini-search-btn check_btn" @click="dialogShow=false">确定</el-button>
        <el-button class="check_back_btn" size="mini" @click="dialogShow=false">返回</el-button>
      </el-dialog>
  </div>
</template>

<script>
import pagination from "@/components/pagination/index";
import { parseTime } from "@/utils/index";
import {
  orderStatusFormatter,
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
        page: 1,
        size: 10,
        searchType: '',
        content: '' // 搜索用关键词
      },
      total: 1,
      dialogShow: false, // 详情弹框
      formData: {}
    };
  },
  methods: {
    onSearch() {
      // 搜索操作
      this.pageInfo.page = 1;
      this.updateMainData();
    },
    // 柔性同步订单列表
    updateMainData() {
      this.loading = true;
      this.$http.customerSyncOrderList(this, this.pageInfo).then((res) => {  
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total
        }
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
     onEdit(chosenOne, index) {
      // 查看操作
      this.formData = chosenOne
      this.dialogShow = true
    },
    // 同步订单
    handleSync (row) {
      this.$http.orderPushAgain(this, {id: row.id}).then(res => {
        if (res.success) {
          this.$message({
            message: res.data.errMessage,
            type: "success",
            duration: 3 * 1000,
            onClose: () => {
              this.updateMainData();
            }
          })
        } else {
          this.$message({
            message: res.data.errMessage,
            type: "error",
            duration: 3 * 1000,
            onClose: () => {
              this.updateMainData();
            }
          })
        }
      }).catch(function (error) {
            this.$message({
              message: error,
              type: "error",
              duration: 3 * 1000,
              onClose: () => {
                this.updateMainData();
              }
            })
          });
    },
    // ======== 状态转换 ========
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
    "pageInfo.time": { // 时间
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
  .box-has-border {
    overflow: hidden;
    .cost-line {
      padding-bottom: 10px;
      font-weight: bold;
      padding-top: 10px;
      font-size: 14px;
      color: #333;
      border-bottom: 1px solid $tableColor;
      padding-left: 30px;
      span.cost-info {
        margin-left: 5px;
      }
      span.cost-info:last-child {
        margin-right: 35px;
      }
    }
    .cost-line:last-child {
      border-bottom: none;
    }
    .align-right {
      text-align: right;
    }
  }
}
</style>