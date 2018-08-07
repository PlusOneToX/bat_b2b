<template>
  <div class="page-container">
    <header>
      <span>汇率列表</span>
      <el-button class="mini-back-btn btn-home" icon="el-icon-plus" @click="add">
				添加汇率
			</el-button>
    </header>
    <section class="main">
      <div class="main-header">
        <!-- <el-button class="mini-batch-btn"
                   size="mini"
                   :loading="syncExchangeRateLoading"
                   @click.prevent="syncExchangeRate">同步汇率</el-button> -->
      </div>
      <div>
        <el-table border
                  :data="exchangeRateList"
                  :max-height="table.maxHeight"
                  style="width:100%"
                  header-row-class-name="header-row"
                  class="tableCenter"
                  v-loading="table.loading">
          <el-table-column align="center" prop="exchangeRate" label="直接汇率"></el-table-column>
          <el-table-column align="center" prop="reverseExRate" label="间接汇率"></el-table-column>
          <el-table-column align="center" prop="cyForid" label="原币"></el-table-column>
          <el-table-column align="center" prop="cyToid" label="目标币"></el-table-column>
          <el-table-column align="center" prop="begDate" label="开始生效日期" :formatter="timeFormatter" :min-width="120"></el-table-column>
          <el-table-column label="操作" :width="160" align="center">
          <template slot-scope="scope">
            <el-button @click="handleLook(scope.row)" class="mini-search-btn"> 查看 </el-button>
            <el-button @click="handleDelete(scope.row)" class="mini-freeze-btn"> 删除 </el-button>
          </template>
        </el-table-column>
        </el-table>
        <page :total="total"
              :page="page.page"
              @sizeChange="pageSizeChange"
              @currentChange="pageChange"></page>
      </div>
    </section>
  </div>
</template>

<script>
  import page from "@/components/pagination/index"
  import {parseTime} from '@/utils/index'
  export default {
    name: 'index',
    components: { page },
    data () {
      return {
        table: {
          maxHeight: 0, // table最大高度
          loading: true // table加载中
        },
        page: {
          page: 1,
          size: 10
        },
        total: 0,
        syncExchangeRateLoading: false, // 同步汇率按键是否处于加载状态
        exchangeRateList: [] // 汇率列表
      }
    },
    created () {
      this.table.maxHeight = document.body.clientHeight - 220;
      this.getExchangeRateList();
    },
    activated(){
      this.getExchangeRateList();
    },
    methods: {
      timeFormatter(row, column, cellValue){ // 时间格式化
        return parseTime(row.begDate)
      },
      add(){// 添加币种
        this.$router.push({ name: 'editAddRate'})
      },
      handleDelete(value){// 删除
        this.$confirm('此操作将永久删除该汇率，是否继续？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(_ => {
            this.$http.delCurrencyRate(this, {id: value.id}).then(res => { 
              if (res.success) {
                this.$message({
                  type: 'success',
                  message: '删除成功'
                })
                this.getExchangeRateList();
              } 
            })
          }).catch(_ => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            })
          })     
      },
      handleLook(value){// 查看
        this.$router.push({ name: 'editAddRate', query: {id: value.id}})
      },
      /**
       * 获取汇率列表信息和分页数据
       */
      getExchangeRateList () {
        this.table.loading = true;
        const params = {
          page: this.page.page,
          size: this.page.size
        }
        this.$http.currencyRateList(this, params).then(result => {  
          if (result.success) {
            this.exchangeRateList = result.data.list;
            this.total = result.data.total;
            this.table.loading = false;
          } else {
            this.table.loading = false;
          }
        })
      },
      /**
       * 分页页码变化
       */
      pageChange (page) {
        this.page.page = page;
        this.getExchangeRateList();
      },
      /**
       * 每页显示数量变化
       */
      pageSizeChange (size) {
        this.page.size = size;
        this.page.page = 1;
        this.getExchangeRateList();
      },
      /**
       * 同步汇率
       */
      syncExchangeRate () {
        const syncLoading = this.$loading({
          lock: true,
          text: '汇率同步中，请稍等...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        this.syncExchangeRateLoading = true;
        this.$http.currencyRateSync(this).then(result => {  
          if (result.success) {
            this.syncExchangeRateLoading = false;
            syncLoading.close();
            this.$message({
              type: 'success',
              message: '汇率同步成功'
            })
          } else {
            this.$message({
              message: result.errMessage
            })
            this.syncExchangeRateLoading = false;
            syncLoading.close();
          }
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .page-container{
    height: 100%;
  }
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
  .main{
    .main-header{
      background-color: white;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px;
    }
  }
</style>
