<template>
  <main class="underline-set">
    <header>
      <h4>线下账户设置</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="onAdd">
        添加线下账户
      </el-button>
    </header>
    <section class="main-page-wrapper">
      <el-table :data="tableData" border max-height="600" header-row-class-name="header-row" class="tableCenter" v-loading="loading">
        <el-table-column align="center" label="开户行" prop="bankName" :min-width="120"> </el-table-column>
        <el-table-column align="center" label="开户名/姓名" prop="name" :min-width="120"> </el-table-column>
        <el-table-column align="center" label="账号" prop="cardNo" :min-width="120"> </el-table-column>
        <el-table-column align="center" label="操作" :min-width="160">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="onEdit(scope.row)">编辑</el-button>
            <el-button class="mini-delete-btn" @click="onDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
    </section>
  </main>
</template>

<script>
import pagination from '@/components/pagination/index'
import {parseTime} from '@/utils/index'
import {getBankCounts, getSettingsCount, addAccount, updateAccount} from '@/views/financial/financialData'
import {deleteCount} from '@/views/financial/financialData'

export default {
  name: 'underlineSet',
  components: { pagination } ,
  activated(){
    this.updateMainData();
  },
  data(){
    return {
      loading: false,
      chosenOrders : [],
      tableData: [],
      total: 1,
      editedAccount: {},
      search:{
        contentType: '1', // 搜索用关键词类型 1.订单编号, 2商品名称, 3分销商用户名, 4分销商姓名
        content: null,
      },
      pageInfo: {
        page: 1,
        count: 10
      }
    }
  },
  methods:{
    // ======== 操作 ========
    onEdit(row, index){ // 编辑操作
      this.$router.push({name: 'underlineSetList',query: {id: row.id}})
    },

    onAdd(){ // 添加操作
      this.$router.push({ name: 'underlineSetList',query: {id: 0}})
    },

    onDelete(row){ // 删除操作
      this.$confirm('此操作将删除账户？，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        deleteCount(this, {ids: row.id}).then(res => {
          if (res.code == 0) {
            this.$message.success({
              message: "操作成功",
              duration: 3 * 1000,
            })
            this.updateMainData()
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消'
        })
      })
    },
    // ======== 数据 ========
    updateMainData(){ // 数据列表
      this.loading = true;
      getSettingsCount(this).then(res => this.total = res.count);// 数据条数
        return getBankCounts(this, this.pageInfo).then(res => {
          this.tableData = res.bankAccounts
          res.code == 0 ? this.loading = false : this.loading = false
        })
    },

    onSizeCHange(val){ // 分页方法
      this.pageInfo.count = val;
      this.updateMainData();
    },

    onCurrentChange(val){ // 分页方法
      this.pageInfo.page = val;
      this.updateMainData();
    },

    // ======== 转换 ========
    timeFormatter(row, column, cellValue){ // 时间格式化
      return parseTime(row.createTime)
    },
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .underline-set{
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
      .btn-home{
        float: right;
        padding: 5px;
        margin-top: 8px;
        margin-right: 8px;
        margin-left: 0;
        font-size: 12px;
      }
    }
    .main-page-wrapper{
      background-color: white;
      height: 100%;
    }
  }
</style>
