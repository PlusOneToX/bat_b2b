<template>
    <div class="warehouse-list">
      <header>
        <h4>退款申请单列表</h4>
      </header>

      <div class="function">
        <el-tabs v-model="applyType" @tab-click="handleClick">
          <el-tab-pane label="分销商申请" name="1"></el-tab-pane>
          <el-tab-pane label="用户申请" name="2"></el-tab-pane>
        </el-tabs>
        <div class="Fheader">
          <div class="f-left">
            <el-select size="mini" v-model="pageInfo.applyStatus" clearable placeholder="状态" style="width:120px;">
              <el-option label="待处理" :value="0"></el-option>
              <el-option label="已处理" :value="1"></el-option>
              <el-option label="已取消" :value="2"></el-option>
            </el-select>
            <el-select size="mini" v-model="pageInfo.refundType" clearable placeholder="退款方式" style="width:150px;padding-left: 10px;">
              <el-option label="原路退回" :value="1"></el-option>
              <el-option label="退回用户余额" :value="2"></el-option>
              <el-option label="其他退款方式" :value="3"></el-option>
            </el-select>
            <el-select size="mini" v-model="pageInfo.businessTypes" clearable placeholder="业务类型" style="width:120px;padding-left: 10px;">
              <el-option label="订单取消" :value="1"></el-option>
              <el-option label="订单变更" :value="2"></el-option>
            </el-select>
          </div>
          <div class="f-search">
            <button class="mini-search-btn box-btn" @click.prevent="filterBill()">搜索</button>
            <el-input placeholder="请输入搜索关键字" @change="contentChange" clearable @keyup.enter.native="filterBill()"  
            size="mini" class="box-search" v-model.trim="pageInfo.content"></el-input>
            <el-select
                class="content_select"
                placeholder="选择类型"
                size="mini"
                v-model="pageInfo.contentType"
                clearable
              >
                <el-option
                  v-for="item in contentTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
          </div>
        </div>
      </div>    
      
      <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row" class="tableCenter" v-loading="loading">
        <el-table-column align="center" label="退款申请编号" prop="id" :min-width="100"></el-table-column>
        <el-table-column align="center" label="业务单号" prop="businessId" :min-width="100"></el-table-column>
        <el-table-column align="center" :label="applyType==='1'?'分销商':'客户名称'" prop="distributorName" show-overflow-tooltip :min-width="150">
          <template slot-scope="scope">
            <div v-if="applyType==='1'">{{scope.row.distributorName}}</div>
            <div v-else>{{scope.row.customerName}}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="业务类型" prop="businessTypes" :formatter="businessTypes" :min-width="120"></el-table-column>
        <el-table-column align="center" label="金额" :min-width="100">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i>
            {{ scope.row.amount| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="处理状态" prop="applyStatus" :min-width="100" :formatter="statusFormatter"></el-table-column>
        <el-table-column align="center" label="退款方式" prop="refundType" :formatter="refundType" :min-width="120"></el-table-column>
        <el-table-column align="center" label="操作人" prop="operatorName" :min-width="120"></el-table-column>
        <el-table-column align="center" label="退款说明" prop="remark" show-overflow-tooltip :min-width="120"></el-table-column>
        <el-table-column align="center" label="操作" :min-width="180" fixed="right">
          <template slot-scope="scope">
            <el-button class="mini-freeze-btn" v-if="scope.row.applyStatus === 0" @click="confirmRefundApply(scope.row)" >处理</el-button>
            <el-button class="mini-search-btn" @click="lockOrder(scope.row)" >查看订单</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="total" :page="pageInfo.page" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>

      <!-- 处理退款申请 -->
      <el-dialog  title="退款申请单处理" :visible.sync="dialogShow" width="40%">
        <confirmRefundApply ref="refundApplyDialog" :id="rId" :applyType="applyType" @submit="submit" @cancel="cancel"></confirmRefundApply>
      </el-dialog>
    </div>
</template>
<script>
import pagination from '@/components/pagination/index'
import { parseTime } from '@/utils/index'
import confirmRefundApply from './confirmRefundApply.vue'

export default {
  name: 'refundApply',
  components: { pagination,confirmRefundApply },
  data() {
    return {
      pageInfo: {
        page: 1,
        size: 10,
        userId: '',
        applyStatus: undefined,
        businessTypes: undefined,
        contentType: undefined,
        content:undefined
      },
       contentTypes: [
        { value: 1, label: '用户名' },
        { value: 2, label: '操作人' },
        { value: 3, label: '退款申请编号' }
      ],
      applyType: '1', // 申请类型 1-分销商  2-用户
      endTime: '',
      total: 1,
      tableData: [],
      action: 0,
      loading: false,
      dialogShow:false,
      rId:'',
      userInfo: {}
    }
  },
  created () {
    this.userInfo = this.$store.getters.userinfo
    this.pageInfo.userId = this.userInfo.id
  },
  activated() {
    this.getTableData()
  },

  methods: {
    submit(val){
      this.dialogShow = false
      this.$http.refundApply(this, {
        id: val.id,
        operatorId: this.userInfo.id,
        operatorName: this.userInfo.userName,
        receiverType: val.applyType,
        refundType: val.refundType,
        remark: val.remark,
        applyStatus: val.applyStatus
      }).then(res => {
        if(res.success){
          this.$message({
            message: '退款申请处理成功',
            type: 'success',
            duration: 3 * 1000
          })
          this.getTableData()
          this.$refs.refundApplyDialog.formData.refundType = 2
          this.$refs.refundApplyDialog.formData.remark=''
        }
      })
    },
    cancel(){
      this.dialogShow = false
      this.$refs.refundApplyDialog.formData.remark=''
    },
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.filterBill()
      }
    },
    // ======== 操作 ========
    getTableData() { // 退款单数据
      if(this.$route.params.id !== undefined){
        this.pageInfo.contentType = 3
        this.pageInfo.content = this.$route.params.id
        this.applyType = this.$route.params.applyType.toString()
      }
      if (this.pageInfo.contentType === 3 && isNaN(this.pageInfo.content) && this.pageInfo.contentType) {
        // 按编号 只能输入数字
        this.$message.error('只能输入数字！')
        return
      }
      this.loading = true;
      if (this.applyType === '1') {
        // 分销商申请
        this.$http.refundApplyList(this, this.pageInfo).then(res => {
          this.loading = false
          if(res.success && res.data.list.length > 0){
            res.data.list.forEach(element => {
              element.operatorName = (element.operatorName === undefined || element.operatorName === '' || element.operatorName === null) ? '-':element.operatorName
            });
            this.tableData = res.data.list
            this.total = res.data.total
          } else {
            this.tableData = []
            this.total = 0
          }
        })
      } else {
        // 用户申请
        this.$http.refundApplyCustomerList(this, this.pageInfo).then(res => {
          this.loading = false
          if(res.success && res.data.list.length > 0){
            res.data.list.forEach(element => {
              element.operatorName = (element.operatorName === undefined || element.operatorName === '' || element.operatorName === null) ? '-':element.operatorName
            });
            this.tableData = res.data.list
            this.total = res.data.total
          } else {
            this.tableData = []
            this.total = 0
          }
        })
      }
    },
    handleClick (tab, event) {
      this.applyType = tab.name
      this.pageInfo.page = 1
      this.getTableData()
    },
    filterBill() { // 搜索操作
      this.pageInfo.page = 1
      this.getTableData()
    },
    confirmRefundApply(row) { // 处理操作
      this.dialogShow = true
      this.rId = row.id
    },
    lockOrder(row){//订单查看
      if(this.$route.params.id !== undefined){
        this.$router.push({name: 'orderDetail',query: { orderId: row.businessId,orderG: true, type: 1 }})
      }else{
        this.$router.push({name: 'orderDetail',query: { orderId: row.businessId,RefundApply: true, type: 1 }})
      }
    },
    // ======== 数据 ========
    onSizeCHange(val) { // 条数
      this.pageInfo.size = val
      this.pageInfo.page = 1
      this.getTableData()
    },

    onCurrentChange(val) { // 页数
      this.pageInfo.page = val
      this.getTableData()
    },

    // ======== 转换 ========
    statusFormatter(r, c, v) { // 处理状态
      switch (v) {
        case 0:
          return '待处理'
          break
        case 1:
          return '已处理'
          break
        case 2:
          return '已取消'
          break
        default:
          return '-'
      }
    },
    refundType(r, c, v) { // 退款方式
      switch (v) {
        case 1:
          return '原路退回'
          break
        case 2:
          return '退回用户余额'
          break
        case 3:
          return '其他退款方式'
          break
        default:
          return '-'
      }
    },
    businessTypes(r, c, v) { // 业务类型
      switch (v) {
        case 1:
          return '订单取消'
          break
        case 2:
          return '订单关闭'
          break
        case 3:
          return '订单变更'
          break
        default:
          return '-'
      }
    },
    timeFormatter(row, column, cellValue) { // 时间格式化
      return parseTime(cellValue)
    }
  },
  watch: {
    'pageInfo.applyStatus': function() {
      this.pageInfo.page = 1
      this.getTableData()
    },
    'pageInfo.refundType': function() {
      this.pageInfo.page = 1
      this.getTableData()
    },
    'pageInfo.businessTypes': function() {
      this.pageInfo.page = 1
      this.getTableData()
    },
    'pageInfo.content': function(val) {
      if (this.pageInfo.contentType === 3 && isNaN(this.pageInfo.content) && this.pageInfo.content) {
        // 按编号 只能输入数字
        this.$message.error('只能输入数字！')
        return
      }
    },
  }
}

</script>

<style rel="stylesheet/scss" lang="scss">
.warehouse-list {
  background-color: white;
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
  .function {
      background-color: white;
      overflow: hidden;
      
      .search {
        padding: 10px;
        float: right;
        overflow: hidden;
        .box-btn {
          float: right;
          margin-left: 5px;
        }
        .box-search {
          width: 190px;
          float: right;
        }
      }
      .Fheader {
        display: inline-block;
        width:100%;
        padding: 10px;
        .f-left{
          display: inline-block;
        }
        .f-search {
          display: inline-block;
          float:right;
          .content_select{
            width:140px;float:right;
          }
          .box-search {
            width: 180px;
            float:right;
          }
          .box-btn {
            float: right;
            margin-left: 5px;
          }
        }
      }
    }
    .el-dialog__title {
      line-height: 24px;
      font-size: 14px;
      color: #303133;
    }
    .el-dialog__header {
        padding: 10px 20px 10px;
        border-bottom:1px solid #999999;
    }
    .el-dialog__headerbtn {
        position: absolute;
        top: 10px;
        right: 20px;
        padding: 0;
        background: 0 0;
        border: none;
        outline: 0;
        cursor: pointer;
        font-size: 16px;
    }
    .el-dialog__body {
      padding: 0px 20px 20px 10px;
    }
}
</style>
