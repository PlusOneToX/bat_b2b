<template>
  <main class="create-invoice">
      <div class="function clearfix">
        <div class="search">
          <el-input placeholder="请输入内容" size="mini" class="box-search"></el-input>
          <el-button size="mini" class="btn-search" @click="_ => 0">搜索</el-button>
        </div>
      </div>
      <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row">
        <el-table-column align="center" label="分销商用户名" prop="distributorName">
        </el-table-column>
        <el-table-column align="center" label="分销商等级" prop="gradeName">
        </el-table-column>
        <el-table-column align="center" label="销售区域" prop="createTime">
        </el-table-column>
        <el-table-column align="center" label="公司名" prop="companyName">
        </el-table-column>
        <el-table-column align="center" label="上次开票时间" prop="lastInvoiceTime">
        </el-table-column>
        <el-table-column align="center" label="操作" prop="createTime">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleCreate(scope.row)" type='primary'>开票</el-button>
          </template>
        </el-table-column>
      </el-table>
  </main>
</template>
<script>
import pagination from '@/components/pagination/index'
import { parseTime } from '@/utils/index'
import { getInvoiceDistributor, getDistributorsIds } from '@/views/financial/financialData'
import { confirmCreator, asyncPatch } from '@/views/order/orderUtils'

const curry = require('ramda').curry;

export default {
  name: 'createInvoice',
  created(){
    this.updateMainData()
  },
  props: {},
  components: { } ,
  data(){
    return {
      tableData: [],
      pageInfo: { //分页信息
        page: 1,
        count: 10
      },
      total: 1, //分页信息
      search:{
        content: null, // 搜索用关键词
      },
    }
  },
  methods:{
    
    updateMainData(){
      // let patchData = curry(asyncPatch)(this, 'id', 'id');

      getInvoiceDistributor(this, this.pageInfo)
      // get可开发票分销商列表
      .then(res => {return this.tableData = res.distributors})
      // 计算分销商ids
      .then(tableData => tableData.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + cur
        }, ''))
      // 请求分销商数据
      .then(ids => getDistributorsIds(this, {ids}).then(res => res.distributors))
      // 拼装数据
      .then(distributors => {
        // patchData = patchData(this.tableData, distributors);
        // patchData()
      })
      .catch(e => console.log(e))
    },
    handleCreate(row){
      this.$emit('createInvoice')
      this.$router.push({name: 'createInvoice', query: {distrId: row.id}})
    }

  },
  watch:{},
  // computed: {}
}
</script>
<style>

</style>

