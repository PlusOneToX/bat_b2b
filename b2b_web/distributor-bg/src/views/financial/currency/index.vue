<template>
  <div class="page-container">
    <header>
      <span>币别列表</span>
    </header>
    <section class="main">
      <div class="main-header">
        <el-button class="mini-batch-btn"
                   size="mini"
                   :loading="syncCurrencyLoading"
                   @click.prevent="syncCurrency">同步币别</el-button>
      </div>
      <div>
        <el-table border
                  :data="currencyList"
                  :max-height="table.maxHeight"
                  style="width:100%"
                  header-row-class-name="header-row"
                  class="tableCenter"
                  v-loading="table.loading">
          <el-table-column align="center" prop="erpNo" label="编码"></el-table-column>
          <el-table-column align="center" prop="name" label="名称"></el-table-column>
          <el-table-column align="center" prop="currencyCode" label="货币代码"></el-table-column>
          <el-table-column align="center" prop="moneyAccuracy" label="金额精度"></el-table-column>
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
        syncCurrencyLoading: false, // 同步币种按键是否处于加载状态
        currencyList: [] // 币种列表
      }
    },
    created () {
      this.table.maxHeight = document.body.clientHeight - 220;
      this.getCurrencyList();
    },
   activated(){
      this.getCurrencyList();
    },
    methods: {
      /**
       * 获取币种信息
       */
      getCurrencyList () {
        this.table.loading = true;
        const params = {
          page: this.page.page,
          size: this.page.size
        }
        this.$http.currencyList(this, params).then(result => {  
          if (result.success) {
            this.currencyList = result.data.list;
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
        this.getCurrencyList();
      },
      /**
       * 每页显示数量变化
       */
      pageSizeChange (size) {
        this.page.size = size;
        this.getCurrencyList();
      },
      /**
       * 同步币种
       */
      syncCurrency () {
        const syncLoading = this.$loading({
          lock: true,
          text: '币种同步中，请稍等...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        this.syncCurrencyLoading = true;
        this.$http.currencySync(this).then(result => {  
          if (result.success) {
            this.syncCurrencyLoading = false;
            syncLoading.close();
            this.$message({
              type: 'success',
              message: '币种同步成功'
            })
          } else {
            this.$message({
              message: result.errMessage
            })
            this.syncCurrencyLoading = false;
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
