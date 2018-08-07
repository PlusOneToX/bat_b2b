<template>
  <div class="exchange-data-list" v-loading="exportDownloadLoading" element-loading-text="正在导出...">
    <header>
      <h4>兑换数据列表</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleExport"
        >导出筛选后的信息</el-button
      >
    </header>
    <div class="function">
      <div class="Fheader">
        <div class="Fleft">
          <div class="select-date">
            <el-date-picker
              v-model="time"
              size="mini"
              type="datetimerange"
              range-separator="至"
              start-placeholder="下单开始时间"
              end-placeholder="下单结束时间"
              value-format="timestamp">
            </el-date-picker>
          </div>
          <el-select
            size="mini"
            v-model="pageInfo.orderStatus"
            placeholder="订单状态"
            style="width: 100px"
            @change="Csearch()"
            clearable
          >
            <el-option label="待确认" :value="1"></el-option>
            <el-option label="已确认" :value="2"></el-option>
            <el-option label="部分发货" :value="3"></el-option>
            <el-option label="全部发货" :value="4"></el-option>
            <el-option label="已关闭" :value="5"></el-option>
            <el-option label="已完成" :value="6"></el-option>
          </el-select>
        </div>
        <div class="Fsearch">
          <button class="mini-search-btn box-btn" @click="Csearch()">
            搜索
          </button>
          <el-input
            v-model.trim="pageInfo.content"
            size="mini"
            clearable
            @change="contentChange"
            @keyup.enter.native="Csearch()"
            placeholder="分销商/订单号/收货人"
            class="box-input"
          ></el-input>
        </div>
      </div>
    </div>

    <el-row v-loading="loading">
      <el-table
        :data="tableData"
        header-row-class-name="header-row"
        border
        class="tableCenter"
        ref="multipleSelect"
      >
        <!-- <el-table-column align="center" type="selection" width="55" :reserve-selection="true"></el-table-column> -->
        <el-table-column label="分销商" align="center" prop="distributorName" :min-width="120"></el-table-column>
        <el-table-column label="订单号" prop="orderNo" align="center" width="150"></el-table-column>
        <el-table-column label="收货人" align="center" prop="userName" width="100"></el-table-column>
        <el-table-column label="收货人手机号" align="center" prop="mobile"  width="120"></el-table-column>
        <el-table-column label="活动名称" prop="codeName" align="center" width="180"></el-table-column>
        <el-table-column label="卡片码(明码)" prop="plainCode" align="center" width="150"></el-table-column>
        <el-table-column label="包装码(盒码)" prop="boxCode" align="center" width="150"></el-table-column>
        <el-table-column label="批次ID" prop="exchangeFactoryId" align="center" width="90"></el-table-column>
        <el-table-column label="核销订单ID" prop="orderId" align="center" width="110"></el-table-column>
        <el-table-column label="订单状态" align="center" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.orderStatus === 1">待确认</span>
            <span v-else-if="scope.row.orderStatus === 2">已确认</span>
            <span v-else-if="scope.row.orderStatus === 3">部分发货</span>
            <span v-else-if="scope.row.orderStatus === 4">全部发货</span>
            <span v-else-if="scope.row.orderStatus === 5">已关闭</span>
            <span v-else-if="scope.row.orderStatus === 6">已完成</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="支付方式" align="center" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.payWay === 1">支付宝</span>
            <span v-else-if="scope.row.payWay === 2">微信</span>
            <span v-else-if="scope.row.payWay === 3">区间结算</span>
            <span v-else-if="scope.row.payWay === 4">公司转账</span>
            <span v-else-if="scope.row.payWay === 5">余额支付</span>
            <span v-else-if="scope.row.payWay === 6">快钱支付</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" prop="createTime" align="center" width="160" :formatter="formatTime"></el-table-column>
        <!-- <el-table-column label="状态" prop="status" align="center" width="160">
           <template slot-scope="scope">
             <span v-if="scope.row.status === 0">未激活</span>
            <span v-else-if="scope.row.status === 1">未使用</span>
            <span v-else-if="scope.row.status === 2">已核销</span>
            <span v-else-if="scope.row.status === 3">已过期</span>
            <span v-else-if="scope.row.status === 4">已作废</span>
            <span v-else>--</span>
          </template>
        </el-table-column> -->
      </el-table>
      <page
        :total="total"
        :page="pageInfo.page"
        @sizeChange="sizeChange"
        @currentChange="currentChange"
      ></page>
    </el-row>

  </div>
</template>

<script>
import page from "@/components/pagination";
import codeList from "@/views/marketingPromotion/exchange//components/codeList";
import { timeFormat } from "@/utils/timeFormat.js";
import {
  getExchangeCodeOrder,
  ExportExchangeCodeOrder
} from "@/views/marketingPromotion/exchange/exchangeData";
export default {
  components: { page, codeList },
  data() {
    return {
      loading: false,
      pageInfo: {
        // 搜索
        page: 1,
        size: 10,
        startTime: '',
        endTime: '',
        orderStatus: '',
        content: ''
      },
      tableData: [], // 表格
      multipleSelect: [],
      isSelect: false,
      total: "", // 总数
      time:[], // 开始结束时间
      exportDownloadLoading: false
    };
  },
  watch: {
    time(val){
      if(this.time != null && this.time instanceof Array && this.time.length>0){
        this.pageInfo.startTime = this.time[0]
        this.pageInfo.endTime = this.time[1]
      }else {
        this.pageInfo.startTime = ''
        this.pageInfo.endTime = ''
        this.pageInfo.page = 1
        this.initData()
      }
    }
  },
  methods: {
    // 初始化数据
    initData() {
      this.$http.exchangeOrderList(this, this.pageInfo).then(res => {  
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      });
    },
    // 导出筛选后信息
    handleExport() {
     if (this.tableData.length > 0) {
      // this.exportDownloadLoading = true
      // ExportExchangeCodeOrder(this, {
      //   startTime: this.pageInfo.startTime,
      //   endTime: this.pageInfo.endTime,
      //   orderStatus: this.pageInfo.orderStatus,
      //   content: this.pageInfo.content
      // }).then(res => {
      //     this.exportDownloadLoading = false
      //   if (res) {
      //       const content = res
      //       let blob = new Blob([content],{
      //         type: "application/ms-excel"
      //       })
      //       let url = window.URL.createObjectURL(blob)
      //       if ('download' in document.createElement('a')) {
      //         let link = document.createElement('a')
      //         link.style.display ='none'
      //         link.href = url
      //         link.setAttribute('download','兑换数据导出列表.xls')
      //         document.body.appendChild(link)
      //         link.click()
      //       }else{
      //         navigator.msSaveBlob(blob, '兑换数据导出列表.xls')
      //       }
      //   } else {
      //       this.$message({
      //         type: "error",
      //         message: res.msg
      //       })
      //   }
      // }).catch(err => {
      //   console.log(err);
      // })

      import('@/utils/Export2Excel').then(excel => {
          const tHeader = ['分销商', '订单号', '收货人', '收货人手机号', '活动名称', '卡片码（明码）', '包装码（盒码）', '批次ID', '核销订单ID', '订单状态', '支付方式', '下单时间']
          const filterVal = ['distributorName', 'distributorOrderId', 'receiveName', 'receiveMobile', 'activeName', 'plainCode', 'boxCode', 'exchangeFactoryId', 'userOrderId', 'orderStatus', 'payWay', 'createTime']
          const data = this.formatJson(filterVal, this.tableData)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: '兑换数据列表'
          })
        })
     } else {
      this.$message({
        type: "error",
        message: "暂无数据"
      })
     }
    },
    sizeChange(val) {
      // 分页方法
      this.pageInfo.size = val;
      this.pageInfo.page = 1;
      this.initData()
    },
    currentChange(val) {
      // 分页方法
      this.pageInfo.page = val;
      this.initData();
    },
    formatTime(row, col, val) { // 表格时间转换
      return timeFormat(val)
    },
    // 搜索兑换活动
    Csearch() {
      this.pageInfo.page = 1;
      this.initData();
    },
    // 输入框输入搜索兑换活动
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.Csearch();
      }
    },
    // 兑换活动列表条数
    sizeChange(size) {
      this.pageInfo.count = size;
      this.pageInfo.page = 1;
      this.initData();
    },
    // 兑换活动列表页数
    currentChange(page) {
      this.pageInfo.page = page;
      this.initData();
    }
  },
  created() {
    this.initData();
  },
  activated() {
    this.initData();
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.exchange-data-list {
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
    .btn-home {
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
}

.function {
  background-color: white;
}
.Fheader {
  margin: 10px;
  display: flex !important;
  justify-content: space-between;
  align-items: center !important;
  .Fleft {
    overflow: hidden;
    float: left;
    .select-date{
      display: inline-block;
      margin-right: 10px;
    }
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
  }
}
.qrcode-content {
  h6 {
    font-size: 14px;
    margin-bottom: 5px;
  }
  .link {
    margin-bottom: 30px;
    .el-input {
      margin-right: 10px;
      width: 350px;
    }
  }
  .qrcode {
    img {
      width: 100px;
      height: 100px;
      vertical-align: bottom;
    }
    .el-button {
      margin-left: 10px;
    }
  }
}
.sync-factory-wrap {
  .pic-wrap {
    position: relative;
    margin-top: 20px;
    padding-left: 100px;
    span {
      position: absolute;
      top: 50%;
      left: 0;
      margin-top: -7px;
    }
  }
  .dialog-footer {
    text-align: center;
  }
}
.avatar-uploader {
  /deep/ .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    &:hover {
      border-color: #21b8cb;
    }
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 296px;
    height: 144px;
    line-height: 144px;
    text-align: center;
  }
  .avatar {
    width: 296px;
    height: 144px;
    display: block;
  }
}
</style>