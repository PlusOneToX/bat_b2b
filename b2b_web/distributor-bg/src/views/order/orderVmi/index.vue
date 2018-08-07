<template>
  <div class="vmiOrder">
    <header>
      <h4>VMI订单明细</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-download" @click="orderVmiDownLoad">
        自定义导出
      </el-button>
    </header>
    <div class="nav clearfix">
      <el-form :inline="true">
        <div> 
          <button type="info" class="mini-search-btn box_btn" size="mini" @click.prevent="filter()">搜索</button>
          <el-input clearable @change="contentChange" class="box_input" size="mini" placeholder="存货编号/订单号/ERP订单号" v-model.trim="pageInfo.content"></el-input>
          <el-select
              class="box_type"
              size="mini"
              v-model="pageInfo.contentType"
              placeholder="类型"
              style="width: 120px"
              clearable
            >
              <el-option label="存货编号" value="1"></el-option>
              <el-option label="订单号" value="2"></el-option>
              <el-option label="ERP订单号" value="3"></el-option>
            </el-select>
          <el-select size="mini" v-model="pageInfo.orderStatus" clearable placeholder="订单状态" style="width:120px;margin-left:10px">
            <el-option label="待确认" value="1"> </el-option>
            <el-option label="待发货" value="2"> </el-option>
            <el-option label="部分发货" value="3"> </el-option>
            <el-option label="待收货" value="4"> </el-option>
            <el-option label="已关闭" value="5"> </el-option>
            <el-option label="已完成" value="6"> </el-option>
          </el-select>
          <el-select size="mini" v-model="pageInfo.payStatus" clearable placeholder="付款状态" style="width:120px">
            <el-option label="未付款" value="1"> </el-option>
            <el-option label="部分付款" value="2"> </el-option>
            <el-option label="已付款" value="3"> </el-option>
            <el-option label="已退款" value="4"> </el-option>
          </el-select>
          <el-date-picker size="mini" v-model="pageInfo.time" type="datetimerange" value-format="timestamp" 
            range-separator="至" start-placeholder="下单开始日期" end-placeholder="下单结束日期" >
          </el-date-picker>
        </div>
      </el-form>
      <div style="margin-top:10px">
        <el-table :data="tableData" border header-row-class-name="header-row" class="tableCenter" v-loading="loading">
          <el-table-column align="center" label="存货编号" prop="itemCode" fixed="left" :width="150"> </el-table-column>
          <el-table-column align="center" label="存货名称" show-overflow-tooltip prop="itemName" :min-width="140"> </el-table-column>
          <el-table-column align="center" label="VMI数量" prop="itemVmiCount" :min-width="100"> </el-table-column>
          <el-table-column align="center" label="B2B订单号" prop="orderId" :min-width="120"> </el-table-column>
          <el-table-column align="center" label="ERP订单号" prop="orderErpNo" :min-width="160"> </el-table-column>
          <el-table-column align="center" label="下单时间" prop="orderCreateTime" :min-width="160" :formatter="timeFormat"> </el-table-column>
          <el-table-column align="center" label="付款状态" prop="payStatus" :min-width="100" :formatter="payStatusFormatter"> </el-table-column>
          <el-table-column align="center" label="订单状态" prop="orderStatus" :min-width="100" :formatter="orderStatusFormatter"> </el-table-column>
          <el-table-column align="center" label="操作" :width="120">
            <template slot-scope="scope">
              <el-button @click="handleEdit(scope.row)" class="mini-search-btn"> 查看 </el-button>
            </template>
          </el-table-column>
        </el-table>
        <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
      </div>
    </div>
    <!-- 导出商品 -->
    <el-dialog title="导出配置" :visible.sync="downLoadShow" width="40%">
      <vmiDownLoad ref="vmiDownload"  @submit="vmiDownLoad" @cancel="closeDownLoad"></vmiDownLoad>
    </el-dialog>
  </div>
</template>

<script> 
import page from "@/components/pagination"
import { timeFormat } from "@/utils/timeFormat"
import { parseTime } from "@/utils/index"
import { formatJson } from "@/utils/common"
import { orderStatusFormatter, payStatusFormatter } from '@/views/order/orderUtils' // 格式化引入
import vmiDownLoad from './vmiDownLoad.vue'
export default {
  name: 'orderVmi',
  components: { page,vmiDownLoad },
  
  data() {
    return{
      pageInfo: {
        orderStatus: undefined, // 订单状态
        payStatus: undefined, // 付款状态
        page: 1,
        size: 10,
        contentType: undefined,
        content: undefined // 搜索用关键词
      },
      total: 0,
      loading: false,
      tableData: [],
      downLoadShow:false,
      uploading:false,
    }
  },
  activated() {
    this.getOrderVmi()
  },
  methods: {
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.pageInfo.page = 1
        this.getOrderVmi()
      }
    },
    vmiDownLoad(val){
      if(val.fields.length === 0) {
        this.$message.error('导出数据至少需选择一个字段')
        return
      } else {
        this.downLoadShow = false
        // this.uploading = this.$loading({
        //   lock: true,
        //   text: '文件导出中....',
        //   spinner: 'el-icon-loading',
        //   background: 'rgba(0, 0, 0, 0.7)'
        // });
        import('@/utils/Export2Excel').then(excel => {
          let tHeader = [], filterVal = []
          let fieldsArr = this.$refs.vmiDownload.fields
          fieldsArr.forEach(element => {
            val.fields.forEach(item => {
              if (element.id === item) {
                tHeader.push(element.name)
                filterVal.push(element.value)
              }
            })
          });
          const data = formatJson(filterVal, this.tableData)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: 'VMI订单明细'
          })
        })
      }
      
			// this.$api.exportData(this,'admin/u/p/order/orderVmiDownLoad',val).then(res =>{
      //   const content = res
      //   if(content === undefined || content === null){
      //     this.uploading.close();
      //     return
      //   }
			// 	let fileName = 'VMI订单明细.xls'
			// 	let blob = new Blob([content],{
			// 		type: "application/ms-excel"
			// 	})
			// 	if ('download' in document.createElement('a')) {
			// 		let url = window.URL.createObjectURL(blob)
			// 		let link = document.createElement('a')
			// 		link.style.display ='none'
			// 		link.href = url
			// 		link.setAttribute('download',fileName)
			// 		document.body.appendChild(link)
			// 		link.click()
			// 	}else{
			// 		navigator.msSaveBlob(blob, fileName)
			// 	}
			// 	this.uploading.close();
			// })
    },
    closeDownLoad(){
      this.downLoadShow = false
    },
    getOrderVmi(){
      this.loading = true
      this.$http.vimOrderList(this, this.pageInfo).then(res => {  
        this.loading = false
        if(res.success){
          this.tableData = res.data.list
          this.total = res.data.total
        }
      })
    },
    filter(){
      this.pageInfo.page = 1
      this.getOrderVmi()
    },
    sizeChange(size) { // 分页
      this.pageInfo.size = size
      this.pageInfo.page = 1
      this.getOrderVmi()
    },
    
    currentChange(page) { // 分页
      this.pageInfo.page = page
      this.getOrderVmi()
    },
    handleEdit(row){ // 查看操作
      this.$router.push({ name: 'orderDetail', query: {
        orderId: row.orderId,
        vmiG: true,
        type:1
      }})
    },
    orderVmiDownLoad(val){
      this.downLoadShow = true
    },
    timeFormat(row, col, val) { // 时间戳转换
      if(val === 0){
        return '-'
      }else{
        return timeFormat(val)
      }
    },
    // ======== 状态转换 ========
    payStatusFormatter(row) { // 付款状态
      return payStatusFormatter(row.payStatus)
    },

    orderStatusFormatter(row) { // 订单状态
      return orderStatusFormatter(row.orderStatus)
    },
  },
  watch: {
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
        this.getOrderVmi();
      },
      deep:true
    },
    "pageInfo.orderStatus":function(){
      this.pageInfo.page = 1
      this.getOrderVmi()
    },
    "pageInfo.payStatus":function(){
      this.pageInfo.page = 1
      this.getOrderVmi()
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .vmiOrder {
    background-color: #fff;
		min-width: 1050px;
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
    .btn-home{
      float: right;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
    }
    .nav {
      padding: 10px 0;
      .box_btn {
        float: right;
        margin-left:5px;
        margin-right:10px;
      }
      .box_input {
        float: right;
        width:220px;
      }
      .ERP_btn {
        margin-left:10px;
      }
      .box_type{
        float:right;
      }
    }
    .custom_select {
      width:130px;
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

