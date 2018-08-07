<template>
<div>
  <!-- v-if="pageState == 'orderBillslist'" -->
  <div class="add-goods" >
    <div class="warehouse-list-wrap">
      <header>
        <span style="margin-left:30px;">退货列表</span>
      </header>
    </div>

    <div class="function">
      <div>
          <span class="box-character">货单流水号</span>
          <el-input v-model="serialNumber" placeholder="请输入内容" size="mini" class="box-search"></el-input>
          <span class="box-character">退换货申请单号</span>
          <el-input v-model="changingNumber" placeholder="请输入内容" size="mini" class="box-search"></el-input>
          <el-button size="mini" class="btn-search" style="border-radius: 5px;" @click="serach">搜索</el-button>
          <el-button type="primary" size="mini" style="border-radius: 5px;" @click="onEdit()">
              导出
            </el-button>
      </div>
    </div>
    

    <el-table :data="tableData" header-row-class-name="header-row"  border ref="multipleTable" class="table_size">
        <el-table-column align="center" label="退货单流水号" prop="id" ></el-table-column>
        <el-table-column align="center" label="退换货申请单号"  prop="afterServiceOrderId"></el-table-column>
        <el-table-column align="center" label="申请时间" :formatter="timeFormatter" prop="updateTime"></el-table-column>
        <el-table-column align="center" label="申请人" prop="distributorName"></el-table-column>
        <el-table-column align="center" label="退货时间" :formatter="timeFormatter" prop="returnTime"></el-table-column>
        <el-table-column align="center" label="状态" :formatter="forstatus" :width="100" :max-height="100"></el-table-column>
        <el-table-column align="center" label="操作人" prop="createAdminName"></el-table-column>
            <el-table-column label="操作" min-width="150">
                <template slot-scope="scope">
                    <el-button type="success" style="border-radius: 5px;" size="mini" @click="handleEdit(scope.row, scope.$index)">查看</el-button>
                </template>
            </el-table-column>
    </el-table>
    
    <!-- <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination> -->
  </div>

  <!-- <bills  v-show="pageState == 'sales'"></bills> -->
</div>  
</template>

<script>
import pagination from "@/components/pagination/index";
import { parseTime } from "@/utils/index";
import bills from '@/views/order/bills/billsindex';
import {getOrderList, getOrderListCount, confirmOrder, cancelOrder} from '@/views/order/orderData'


export default {
  name: 'orderbills',
  components: {
    bills
  } ,
  mounted() {
    // window.addEventListener('keydown',this.onEscPress);
    this.updata();
  },
  data() {
    return {
      batch: 1,
        state: "",
        tableData: [],
        serialNumber: "",
        changingNumber: "",
        afterServiceOrderId: "",
        pageInfo:{
          page: 1,
          count: 10,
        },
    }
  }, 
  methods:{
    // 页面主要数据
    updata() {
       this.$api.get(this,'admin/u/p/returnOrder/list',this.pageInfo).then(res => {
        this.tableData = res.returnOrders
      })
    },

    // 搜索按钮
    serach() {
      this.pageInfo.id = this.serialNumber;
      this.pageInfo.afterServiceOrderId = this.changingNumber
      this.updata();
    },

    // 查看
    handleEdit(row) {
        this.$router.push({name: 'billsindex',params:{id:row.id,state: row.state} })
    },

    // onEscPress(event){
    //   if(event.keyCode == 27){
    //     // this.$router.push({ name: 'orderExchangeGoodsList' })
    //     // this.pageState = 'orderBillslist';
    //   }
    // },
    
    // 时间格式化
    timeFormatter(row, column, cellValue){ 
      return parseTime(row.createTime)
    },
    // 列表表格状态
    forstatus(row) {
      switch(row.state) {
        case 1:
          return "待处理";
        case 2:
          return "已退货";
        case 3:
          return "取消退货";
      }
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
* {
  box-sizing: border-box;
  padding: 0;
  margin: 0;
}
.add-goods {
  background-color: white;
  min-height: 100%;
  padding-bottom: 30px;
  .warehouse-list-wrap {
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
    }
  }
  .nav {
    width: 100%;
    .nav_list {
      width: 100%;
      padding: 10px 0 0px 0;
      .nav_heder {
        width: 100%;
      }
    }
  }
  .el-radio-button__inner {
    border: 0;
  }
  .btn {
    float: right;
    width: 80px;
    margin-right: 5%;
  }
  .function {
    padding: 16px 16px 16px 16px;
    border-top: 0px;
    background-color: #fff;
    .box-character {
        color: #909399;
        font-size: 14px;
    }
    .box-search {
      width: 140px;
      margin-right: 30px;
    }
    .btn-search {
      background-color: $lakeBlue;
      color: #fff;
    }
  }
  .table_size {
    text-align: center;
  }
   div.el-tabs__nav{
        margin-left: 30px;
      }
}
</style>
