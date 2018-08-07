<template>
  <main class="bills-age">
    <header>
      <span>账龄分析</span>
    </header>
    <el-tabs v-model="filter.type" @tab-click="_ => 0">
      <el-tab-pane label="到期未回款" name="0"></el-tab-pane>
      <el-tab-pane label="原生订单" name="1"></el-tab-pane>
    </el-tabs>
    <div class="function">
      <!-- <el-button size="mini" @click="_ => 0">导出</el-button> -->
      <div class="search">
        <el-select size="mini" value="0" placeholder="请选择" style="width:120px">
          <el-option label="全部" value="0"> </el-option>
          <el-option label="未付款" value="1"> </el-option>
          <el-option label="部分付款" value="2"> </el-option>
          <el-option label="已付款" value="3"> </el-option>
        </el-select>
        <el-input placeholder="请输入内容" v-model="search.content" size="mini" class="box-search"></el-input>
        <el-button size="mini" class="btn-search" @click="onSearch">搜索</el-button>
      </div>
    </div>
    <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row">
      <el-table-column align="center" prop="aaaa" label="分销商用户名"></el-table-column> 
      <el-table-column align="center" prop="aaaa" label="姓名"></el-table-column> 
      <el-table-column align="center" prop="aaaa" label="公司名"></el-table-column> 
      <el-table-column align="center" prop="aaaa" label="电话"></el-table-column> 
      <el-table-column align="center" prop="aaaa" label="手机"></el-table-column> 
      <el-table-column align="center" prop="aaaa" label="支付方式"></el-table-column> 
      <el-table-column align="center" prop="aaaa" label="金额"></el-table-column> 
      <el-table-column align="center" prop="aaaa" label="发票备注"></el-table-column> 
      <el-table-column align="center" label="操作" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="onCheck(scope.row, scope.$index)" >
            查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </main>
</template>

<script>
import pagination from '@/components/pagination/index'
import {parseTime} from '@/utils/index'
// import {getBalanceList, getBalanceListCount} from '@/views/financial/financialData'
import {confirmCreator} from '@/views/order/orderUtils'
// import {sexFormatter} from '@/views/financial/financialUtils'

export default {
  name: 'billsAge',
  created(){},
  components: { pagination } ,
  data(){
    return {
      filter:{
        type: 0
      },
      search:{
        contentType: '1', // 搜索用关键词类型
        content: null, // 搜索用关键词
      },
      pageInfo: { //分页信息
        page: 1,
        count: 10
      },
      total: 1, //分页信息
      tableData: [], // 表格信息 @main Array of orders
    }
  },
  methods:{
    onSearch(){},
    onCheck(){}
  },
  watch:{},
  computed: {}
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .bills-age{
    background-color: white;
    height: 100%;
    // min-width: 1050px;
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      span{
        margin-left: 30px;
      }
      .btn-home{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
      }
    }
    .function{
      padding: 0px 16px 16px 16px;
      // background-color: white;
      .btn-export{
        background-color: lighten(grey, 40%);
      }
      .search{
        float: right;
      }
      .box-search{
        width: 140px;
      }
      .btn-search{
        background-color: $lakeBlue;
        color: white;
      }
    }
    .order-list{
      height: 100%;
      min-width: 1000px;

      /*background-color: blue;*/
      .header-row {
        background-color: $table-header-bg;
        th {
          padding: 5px;
          text-align: center;
        }
      }
      td {
        text-align: center;
      }
      .table-cell{
        text-align: center;
      }
      .el-form{
        width: 700px;
        margin-top: 30px;
      }
    }
    div.el-tabs__nav{
      margin-left: 30px;
    }
  }
</style>
