<template>
  <div class="page-container">
    <header>
      <span>汇率列表</span>
    </header>
    <section class="main">
      <div class="main-header">
        <el-button class="mini-batch-btn"
                   size="mini"
                   :loading="syncExchangeRateLoading"
                   @click.prevent="syncExchangeRate">同步汇率</el-button>
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
          <el-table-column align="center" prop="rateType" label="汇率类型"></el-table-column>
          <el-table-column align="center" prop="cyForID" label="原币"></el-table-column>
          <el-table-column align="center" prop="cyToID" label="目标币"></el-table-column>
          <el-table-column align="center" prop="begDate" label="生效日期">
            <template slot-scope="scope">
              {{scope.row.begDate.replace(/T/, ' ')}}
            </template>
          </el-table-column>
          <el-table-column align="center" prop="endDate" label="失效日期">
            <template slot-scope="scope">
              {{scope.row.endDate.replace(/T/, ' ')}}
            </template>
          </el-table-column>
          <el-table-column align="center" prop="documentStatus" label="数据状态">
            <template slot-scope="scope">
              {{statusChange(scope.row.documentStatus)}}
            </template>
          </el-table-column>
        </el-table>
        <page :total="page.total"
              :page="page.curPage"
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
          curPage: 1,
          count: 10,
          total: 0
        },
        syncExchangeRateLoading: false, // 同步汇率按键是否处于加载状态
        exchangeRateList: [] // 汇率列表
      }
    },
    created () {
      this.table.maxHeight = document.body.clientHeight - 220;
      this.getExchangeRateList();
    },
    methods: {
      /**
       * 获取汇率列表信息和分页数据
       */
      getExchangeRateList () {
        this.table.loading = true;
        const params = {
          page: this.page.curPage,
          count: this.page.count
        }
        this.$api.get(this, 'admin/u/p/rate/getRateList', params).then(result => {
          if (result.code === 0) {
            this.exchangeRateList = result.list;
            this.table.loading = false;
          } else {
            this.table.loading = false;
          }
        })
        this.$api.get(this, 'admin/u/p/rate/count', params).then(result => {
          if (result.code === 0) {
            this.page.total = result.count;
          } else {}
        })
      },
      /**
       * 分页页码变化
       */
      pageChange (page) {
        this.page.curPage = page;
        this.getExchangeRateList();
      },
      /**
       * 每页显示数量变化
       */
      pageSizeChange (count) {
        this.page.count = count;
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
        this.$api.get(this, 'admin/u/p/rate/syncRate', {}).then(result => {
          if (result.code === 0) {
            this.syncExchangeRateLoading = false;
            syncLoading.close();
            this.$message({
              type: 'success',
              message: '汇率同步成功'
            })
          } else {
            this.$message({
              message: result.msg
            })
            this.syncExchangeRateLoading = false;
            syncLoading.close();
          }
        })
      },
      /**
       * 状态转换
       */
      statusChange (value) {
        let returnStr = '其他';
        if (value === 'A') {
          returnStr = '新建';
        }
        if (value === 'Z') {
          returnStr = '暂存';
        }
        if (value === 'B') {
          returnStr = '审核中';
        }
        if (value === 'C') {
          returnStr = '已审核';
        }
        if (value === 'D') {
          returnStr = '重新审核';
        }
        return returnStr;
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
