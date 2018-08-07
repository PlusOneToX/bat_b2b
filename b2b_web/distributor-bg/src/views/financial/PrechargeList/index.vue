<template>
  <div class="precharge-list">
    <header>
      <h4>收款列表</h4>
    </header>

    <div class="function">
      <div class="Fheader">
        <el-select size="mini" v-model="search.confirmStatus" clearable placeholder="状态" style="width:120px;">
          <el-option label="待确认" value="1"></el-option>
          <el-option label="已确认" value="2"></el-option>
          <el-option label="已取消" value="3"></el-option>
        </el-select>
        <el-select size="mini" v-model="search.rechargeType" clearable placeholder="收款方式" style="width:120px;">
          <el-option label="支付宝" value="1"></el-option>
          <el-option label="微信" value="2"></el-option>
          <el-option label="区间结算" value="3"></el-option>
          <el-option label="线下转账" value="4"></el-option>
          <el-option label="余额支付" value="5"></el-option>
          <el-option label="快钱支付" value="6"></el-option>
        </el-select>
         <el-select size="mini" v-model="search.confirmStatus" clearable placeholder="业务类型" style="width:120px;">
          <el-option label="订单收款" value="1"></el-option>
          <el-option label="在线充值" value="2"></el-option>
        </el-select>
        <div class="f-search">
          <button class="mini-search-btn box-btn" @click.prevent="onSearch()">搜索</button>
          <el-select
            class="content_select"
            placeholder="选择类型"
            size="mini"
            style="width:140px;"
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
          <el-input v-model.trim="search.content" @change="contentChange" clearable @keyup.enter.native="onSearch()" placeholder="分销商/充值金额" size="mini" class="box-search"></el-input>
        </div>
      </div>
    </div>

    <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row" @selection-change="onSelectionChange" class="tableCenter" v-loading="loading">
      <el-table-column align="center" label="收款单编号" prop="payBillsDTO.id" :min-width="80"> </el-table-column>
      
      <el-table-column align="center" label="分销商用户名" prop="distributorName" show-overflow-tooltip :min-width="120"> </el-table-column>
       <el-table-column align="center" label="业务类型" prop="distributorName" show-overflow-tooltip :min-width="120"> </el-table-column>
      <el-table-column align="center" label="充值方式" prop="rechargeType" :formatter="rechargeTypeFormatter" :min-width="120"> </el-table-column>
      <el-table-column align="center" label="收款金额" :min-width="120"> 
        <template slot-scope="scope">
          <i class="asmd">￥:&nbsp;</i>
          {{ scope.row.rechargeAmount| NumFormat}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="支付凭证ID" prop="rechargeOrderId" show-overflow-tooltip :min-width="200"> </el-table-column>
      <el-table-column align="center" label="充值状态" prop="confirmStatus" :formatter="ConfirmStatusFormatter" :min-width="120"> </el-table-column>
      <el-table-column align="center" label="备注" prop="bookDTO.remark" :formatter="ConfirmStatusFormatter" :min-width="120"> </el-table-column>
      <el-table-column align="center" label="操作" :min-width="120">
        <template slot-scope="scope">
          <!-- <el-button type="info" size="mini" @click="operatePrecharge(scope.row, 2)">确认</el-button> -->
          <el-button class="mini-search-btn" @click="onEdit(scope.row, scope.$index)">查看</el-button>
          <!-- <el-button type="warning" size="mini" @click="operatePrecharge(scope.row, 3)">取消</el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <pagination :total="total" :page="pageInfo.page" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
  </div>
</template>

<script>
import pagination from '@/components/pagination/index'
import { prechargeList, prechargeListCount} from '@/views/financial/financialData'
import {confirmCreator,rechargeTypeFormatter, ConfirmStatusFormatter} from '@/views/financial/financialUtils'

export default {
  name: 'PrechargeList',
  components: { pagination } ,
  created(){
    this.updateMainData();
  },
  activated(){
    this.updateMainData();
  },
  data(){
    return {
      loading: false,
      total: 1,
      editedId: '',
      tableData: [],
      chosenItems : [], // 表格中被勾选的订单
      pageInfo: {
        page: 1,
        size: 10,
        userId:'',
        contentType: undefined,
        content: undefined
      },
      search:{
        confirmStatus: null, // 0.全部, 1.待确认, 2.确认, 3.未通过
        content: null,
      },
      contentTypes: [
        { value: 1, label: '分销商用户名' },
        { value: 2, label: '收款编号' },
        { value: 3, label: '支付凭证ID' },
      ]
    }
  },
  methods:{
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.onSearch()
      }
    },
    // ======== 操作 ========
    onSearch(){ // 搜索操作
      this.pageInfo.page = 1;
      this.updateMainData();
    },

    onEdit(row, index){ // 查看操作
      this.$router.push({ name: 'prechargeData', query:{ id: row.id, rechargeType: row.rechargeType}})
    },

    // ======== 数据 ========
    updateMainData(){ // 数据列表
      this.loading = true;
      let userInfo = this.$store.getters.userinfo
      this.pageInfo.userId = userInfo.id
      this.$http.rechargeList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
        }
        res.success ? this.loading = false : this.loading = false
      })
    },
    
    onSelectionChange(val){ // 当选择项发生变化时会触发该事件
      this.chosenItems = val;
    },

    onSizeCHange(val){ // 分页方法
      this.pageInfo.size = val;
      this.updateMainData();
    },
    onCurrentChange(val){ // 分页方法
      this.pageInfo.page = val;
      this.updateMainData();
    },
    // ======== 转换 ========
    rechargeTypeFormatter(row, column, stateCode, index){ // 充值方式
      return rechargeTypeFormatter(stateCode)
    },

    ConfirmStatusFormatter(row, column, stateCode, index){ // 充值状态
      return ConfirmStatusFormatter(cellValue)
    },
  },
  watch: {
    'search.confirmStatus': function(){ // 充值状态监听
      this.pageInfo.page = 1;
      this.updateMainData();
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .precharge-list{
    height: 100%;
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
    .function {
      background-color: white;
      .Fheader {
        display: inline-block;
        width:100%;
        padding: 10px;
      }
      .f-search {
        display: inline-block;
        float: right;
        .box-search {
          width: 200px;
        }
        .box-btn {
          float: right;
          margin-left: 5px;
        }
      }
    }
  }
</style>
