<template>
    <div class="category-list">
      <header>
        <h4>分账记录列表</h4>
      </header>
      <div class="function">
        <div class="Fheader">
          <div class="Fleft">
            <el-select size="mini"  v-model="pageInfo.status" placeholder="分账状态" style="width: 100px;" clearable>
              <el-option label="关闭" value="0"> </el-option>
              <el-option label="待分账" value="1"> </el-option>
              <el-option label="部分分账" value="2"> </el-option>
              <el-option label="全部分账" value="3"> </el-option>
              <el-option label="分账失败" value="4"> </el-option>
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
          <div class="Fsearch">
            <button class="mini-search-btn box-btn" @click="Csearch()">搜索</button>
            <el-input v-model.trim="pageInfo.content" size="mini" clearable @change="contentChange" @keyup.enter.native="Csearch()" placeholder="请输入关键词搜索" class="box-input"></el-input>
            <el-select size="mini"  v-model="pageInfo.contentType" placeholder="分销商用户名" style="width: 140px;" clearable>
              <el-option label="分销商用户名" value="1"> </el-option>
              <el-option label="店铺名称" value="2"> </el-option>
            </el-select>
          </div>
        </div>
      </div>
      <el-row v-loading="loading">
        <el-table :data="tableData" header-row-class-name="header-row" border class="tableCenter"  :height="tableHeight">
          <el-table-column align="center" label="归属分销商" prop="distributorName" :min-width="120"></el-table-column>
          <el-table-column sortable  align="center" label="时间" prop="createTime" :formatter="formatTime" :min-width="160"></el-table-column>
          <el-table-column  label="订单号" align="center" prop="orderNo" :min-width="120"></el-table-column>
          <el-table-column label="店铺名称" align="center" prop="shopName" :min-width="120"></el-table-column>
          <el-table-column label="实付金额" align="center" prop="payAmount" :min-width="120">
            <template slot-scope="scope">
              <div>￥{{scope.row.payAmount}}</div>
            </template>
          </el-table-column>
          <el-table-column label="总分账金额" align="center" prop="maxSubAccountAmount" :min-width="140">
            <template slot-scope="scope">
              <!-- <div @click="handleSelect(scope.row.id)">￥{{scope.row.maxSubAccountAmount}}</div> -->
              <!-- <el-button type="primary" @click="handleSelect(scope.row.id)">￥{{scope.row.maxSubAccountAmount}}</el-button> -->
              <button class="mini-search-btn box-btn" @click="handleSelect(scope.row.id)">￥{{scope.row.maxSubAccountAmount}}</button>
            </template>
          </el-table-column>
          <el-table-column label="状态" :formatter="formatStatus" align="center" prop="status" :min-width="120"></el-table-column>
          <el-table-column label="备注" align="center" prop="remark" :min-width="120"></el-table-column>
        </el-table>
        <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
      </el-row>
       <!----分账详情弹框----->
      <el-dialog
        title="分账详情"
        :visible.sync="dialogVisible"
        width="70%"
        center>
        <el-table
          :data="gradeList"
          header-row-class-name="header-row"
          border
          style="text-align:center;"
          v-loading="loading"
      >
          <el-table-column align="center" label="等级"  prop="levelName" width="120"></el-table-column>
          <el-table-column align="center" label="姓名" prop="salemanName" width="140"></el-table-column>
          <el-table-column align="center" label="openID/商户号" prop="merchantNumber" show-overflow-tooltip>
              <template slot-scope="scope">
                  <div>{{scope.row.openId}} / {{scope.row.merchantNumber}}</div>
              </template>
          </el-table-column>
          <el-table-column align="center" label="分账金额" prop="maxSubAccountAmount" width="180">
            <template slot-scope="scope">
              <div>￥{{scope.row.maxSubAccountAmount}}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="状态" prop="status" width="140" :formatter="formatStatus"></el-table-column>
          <el-table-column align="center" label="失败原因"  prop="failReason">
            <template slot-scope="scope">
              <div>{{scope.row.failReason?scope.row.failReason:'--'}}</div>
            </template>
          </el-table-column>
      </el-table>
      </el-dialog>
    </div>
</template>

<script type="text/javascript">
  import page from '@/components/pagination'
  import { timeFormat } from '@/utils/timeFormat.js'
  import { parseTime } from "@/utils/index"
export default {
  components: { page },
  created() {
    const documentHeight = document.body.clientHeight
    this.tableHeight = parseInt(documentHeight * 0.8 - 100) // 计算表高度，固定表头
  },
  activated() {
    this.dataFot()
  },
  data() {
    return {
      tableHeight: 600,
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        status: undefined,
        contentType: undefined,
        content: undefined
      },
      total: 0,
      tableData: [],
      gradeList: [],
      dialogVisible: false
    }
  },
  methods: {
    contentChange (val){
      if(val === undefined || val === '' || val === null){
        this.Csearch()
      }
    },
    Csearch() { // 搜索操作
      this.pageInfo.page = 1
      this.dataFot()
    },
    // ======== 数据 ========
    dataFot() { // 数据列表
      this.loading = true
      this.$http.orderSubAccountList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
          this.loading = false
        } else {
          this.looking = false
        }
      })
    },
    // 查看分账详情
    handleSelect(id) {
      this.$http.orderSubAccountDetail(this, {id: id}).then(res => {
        if (res.success) {
          this.gradeList = res.data
          this.dialogVisible = true
        }
      })
    },
    sizeChange(size) { // 条数
      this.pageInfo.size = size
      this.pageInfo.page = 1
      this.dataFot()
    },

    currentChange(page) { // 页数
      this.pageInfo.page = page
      this.dataFot()
    },
    // ======== 转换 ========
    formatTime(row, col, val) { // 表格时间格式化
      return timeFormat(val)
    },
    formatStatus(row, col, val){
      switch(val) {
        case 0:
          return "关闭";
          break;
        case 1:
          return "待分账";
          break;
        case 2:
          return "部分分账";
          break;
        case 3:
          return "全部分账";
          break;
        case 4:
          return "分账失败";
          break;     
      }
    },
  },
  watch: {
    "pageInfo.status":function(){
      this.pageInfo.page = 1
      this.dataFot()
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
          this.dataFot();
        }
      },
      deep:true
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .function {
    background-color: white;
    .Fheader {
        margin: 10px;
        display: flex !important;
        justify-content: space-between;
        align-items: center !important;
        .Fleft {
            overflow: hidden;
            float: left;
        }
        .Fsearch {
            overflow: hidden;
            float: right;
            .box-input {
                width: 215px;
                float: right;
            }
            .box-btn {
                float: right;
                margin-left: 5px;
            }
            /deep/.el-input{
              .el-input__inner{
                border-top-right-radius: 0;
                border-bottom-right-radius: 0;
              }
            }
            /deep/.box-input{
              .el-input__inner{
                border-top-left-radius: 0;
                border-bottom-left-radius: 0;
                border-left-width: 0;
              }
            }
        }
    }
  }
</style>