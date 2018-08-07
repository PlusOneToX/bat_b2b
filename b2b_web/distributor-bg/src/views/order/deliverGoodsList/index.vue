<template>
    <div class="warehouse-list">
      <header>
        <h4>发货单列表</h4>
      </header>
      <div class="nav">
        <div class="nav-header">
            <el-button :loading="syncLoading" class="mini-batch-btn" size="mini" @click.prevent="sysErpExpressNo">同步快递单号</el-button>
            <el-select size="mini" v-model="pageInfo.push" placeholder="是否需要推送" style="width: 120px;" clearable>
              <el-option label="是" value="1"></el-option>
              <el-option label="否" value="0"></el-option>
            </el-select>
            <el-select size="mini" v-model="pageInfo.pushStatus" placeholder="同步第三方状态" style="width: 130px;" clearable>
              <el-option label="成功" value="1"></el-option>
              <el-option label="失败" value="0"></el-option>
            </el-select>
            <el-select size="mini" v-model="pageInfo.synErpFlag" placeholder="同步ERP状态" style="width: 130px;" clearable>
              <el-option label="成功" value="1"></el-option>
              <el-option label="失败" value="0"></el-option>
            </el-select>
        </div>
        <div class="nav-sear">
          <el-select
            size="mini"
            v-model="pageInfo.contentType"
            placeholder="类型"
            style="width: 100px"
            clearable
          >
            <el-option label="发货单流水号" value="1"></el-option>
            <el-option label="出库单号" value="2"></el-option>
            <el-option label="订单号" value="3"></el-option>
            <el-option label="收货人" value="4"></el-option>
          </el-select>
          <el-input @change="contentChange" clearable placeholder="请输入搜索关键字" @keyup.enter.native="search()" v-model.trim="pageInfo.content" size="mini" class="box-input"></el-input>
           <button class="mini-search-btn box-search" @click="search()" >搜索</button>
        </div>
      </div>

      <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row" class="tableCenter" v-loading="loading" :height="tableHeight">
        <!-- <el-table-column type="index" fixed> </el-table-column> -->
        <el-table-column align="center" label="发货单流水号" prop="id" :min-width="100"></el-table-column>
        <el-table-column align="center" label="B2B订单号" show-overflow-tooltip prop="orderNo" :min-width="160"></el-table-column>
        <el-table-column align="center" label="出库单号" show-overflow-tooltip prop="deliverErpNo" :min-width="160"></el-table-column>
        <el-table-column align="center" label="收货人" prop="userName" :min-width="120"></el-table-column>
        <el-table-column align="center" label="下单时间" prop="orderCreateTime" :formatter="timeFormatter" :min-width="160"></el-table-column>
        <el-table-column align="center" label="创建时间" prop="createTime" :formatter="timeFormatter" :min-width="160"></el-table-column>
        <el-table-column align="center" label="审核时间" prop="deliverTime" :formatter="timeFormatter" :min-width="160"></el-table-column>
        <el-table-column align="center" label="审核状态" prop="deliverStatus" :formatter="deliverStatusFormatter" :min-width="110"></el-table-column>
        <el-table-column align="center" label="同步第三方状态" prop="pushStatus"  :formatter="pushStatusFormatter" :min-width="130"></el-table-column>
        <el-table-column align="center" label="同步ERP状态" prop="deliverErpNo" :min-width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.deliverErpNo">同步成功</span>
            <span v-else>同步失败</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="同步失败原因" prop="erpErrorLog"  :min-width="120"></el-table-column>
        <el-table-column align="center" label="操作" :min-width="200" fixed="right">
          <template slot-scope="scope">
            <el-button class="mini-tableadd-btn"  v-if="showSync(scope.row)" @click="syncCustomer(scope.row)">同步第三方</el-button>
            <el-button class="mini-search-btn" @click="preview(scope.$index,scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
    </div>
</template>
<script>
import pagination from '@/components/pagination/index'
import { parseTime } from '@/utils/index'

export default {
  name: 'login',
  components: { pagination },
  data() {
    return {
      tableHeight: 600,
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        push: undefined,
        pushStatus: undefined,
        synErpFlag: undefined,
        contentType:undefined,
        content: undefined
      },
      total: 0,
      tableData: [],
      syncLoading:false,
      distributors:[],
      syncCustomerLoading: false
    }
  },
  created(){
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.82  -  100); // 计算表高度，固定表头
  },
  activated(){
    this.getTableData()
  },
  methods: {
    // 是否显示同步按钮
    showSync(value) {
      if (value.push && value.push === 1 && (value.pushStatus == undefined || value.pushStatus === null || value.pushStatus === 0)) {
        return true
      } else {
        return false
      }
    },
    // 同步第三方
    syncCustomer(row){
      this.syncCustomerLoading = true
      this.$http.syncLogisticsToThird(this, {id: row.id}).then(res =>{   
        this.syncCustomerLoading = false
        if(res.success){
          row.pushStatus = 1
          this.$message({
						message: '成功同步到第三方',
						type: 'success',
						duration: 3 * 1000,
          })
        }
      })
    },
    sysErpExpressNo(){
      this.syncLoading = true
      this.$http.syncExpressNoToERP(this).then(res => { 
        this.syncLoading = false
        if(res.success) {
          this.$message({
            message: '正在同步快递单号',
            type: 'success',
            duration: 3 * 1000,
          })
        }
      })
    },
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.search()
      }
    },
    preview(index,row){  // 查看操作
      this.$router.push({name:'deliverGoodsDetail',query:{id:row.id,orderG: false}})
    },

    search() { // 搜搜操作
      this.pageInfo.page = 1
      this.getTableData()
    },
    
    getTableData(){ // 主要数据
      this.loading = true;
      this.$http.orderDeliverBillList(this, this.pageInfo).then(res => { 
        if (res.success) {
          this.tableData=res.data.list
          this.total=res.data.total
        }
        res.success ? this.loading = false : this.loading = false
      })
    },
    onSizeCHange(val) { // 分页方法
      this.pageInfo.size = val;
      this.getTableData()
    },
    onCurrentChange(val) { // 分页方法
      this.pageInfo.page = val;
      this.getTableData()
    },
    timeFormatter(row, column, cellValue) { // 时间格式化
      return parseTime(cellValue)
    },
    deliverStatusFormatter(r,c,v){
      switch(v) {
        case 1:
        return '待确认';
        break;
        case 2:
        return '已确认';
        break;
        case 3:
        return '已作废';
        break;
      }
    },
    pushStatusFormatter(r,c,v){
      switch(v) {
        case 1:
        return '同步成功';
        break;
        case 0:
        return '同步失败';
        break;
        default:
        return '-';
      }
    }
  },
  watch: {
    // 'pageInfo.erpDeliverStatus': { // 是否同步ERP
    //   handler() {
    //     this.pageInfo.page = 1
    //     this.getTableData()
    //   },
    //   deep: true
    // },
    // 'pageInfo.distributorId': {
    //   handler() {
    //     this.pageInfo.page = 1
    //     this.getTableData()
    //   },
    //   deep: true
    // },
    'pageInfo.push': {
      handler() {
        this.pageInfo.page = 1
        this.getTableData()
      },
      deep: true
    },
    'pageInfo.pushStatus': {
      handler() {
        this.pageInfo.page = 1
        this.getTableData()
      },
      deep: true
    },
    'pageInfo.synErpFlag': {
      handler() {
        this.pageInfo.page = 1
        this.getTableData()
      },
      deep: true
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.warehouse-list{
    background-color: white;
    header {
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
    }
    h4 {
      display: inline-block;
      font-weight: 350;
      font-size: 16px;
      margin: 0 20px;
    } 
}
.nav {
  padding: 10px;
  display: flex;
  // justify-content: flex-end;
  align-items: center;
  .nav-header {
    display: inline-block;
    flex:1;
  }
  .nav-sear{
    display: inline-block;
    .el-select{
      display: inline-block;
    }
    .box-search {
      margin-left: 5px;
      width: 60px;
      text-align: right;
    }
    .box-input{
      display: inline-block;
      width: 180px;
    }
   
  }
}

</style>
