<template>
    <div class="category-list">
      <header>
        <h4>分账配置列表</h4>
      </header>

      <div class="function">
        <div class="Fheader">
          <div class="Fleft"></div>
          <div class="Fsearch">
            <button class="mini-search-btn box-btn" @click="Csearch()">搜索</button>
            <el-input v-model.trim="pageInfo.content" size="mini" clearable @change="contentChange" @keyup.enter.native="Csearch()" placeholder="请输入关键词搜索" class="box-input"></el-input>
            <el-select size="mini"  v-model="pageInfo.searchType" placeholder="分销商用户名" style="width: 140px;" clearable>
              <el-option label="分销商用户名" value="1"> </el-option>
              <el-option label="配置名称" value="2"> </el-option>
              <el-option label="店铺名称" value="3"> </el-option>
            </el-select>
          </div>
        </div>
      </div>
      
      <el-row v-loading="loading">
        <el-table :data="tableData" header-row-class-name="header-row" border class="tableCenter"  :height="tableHeight">
            <el-table-column align="center" label="归属分销商" prop="distributorName" :min-width="120"></el-table-column>
            <el-table-column  label="配置名称" align="center" prop="name" :min-width="120"></el-table-column>
            <el-table-column label="比例类型" align="center" prop="amountType" :formatter="formatType" :min-width="100"></el-table-column>
            <el-table-column label="分账比例" align="center" prop="ratioStr" :min-width="180" show-overflow-tooltip>
            </el-table-column>
            <el-table-column label="店铺" :min-width="80" align="center">
            <template slot-scope="scope">
              <el-button class="mini-search-btn" @click="handleEdit(scope.$index,scope.row)">点击查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
      </el-row>
      <!----点击查看弹框----->
      <el-dialog
        title="关联店铺列表"
        :visible.sync="StoreDialogVisible"
        width="40%"
        center>
        <el-table
          :data="shopData"
          header-row-class-name="header-row"
          border
          style="text-align:center;"
          v-loading="loading"
      >
          <el-table-column align="center" label="门店名称"  prop="shopName" width="160"></el-table-column>
          <el-table-column align="center" label="门店编码" prop="shopCode" :min-width="100"></el-table-column>
          <el-table-column align="center" label="状态" prop="openFlag" width="100" show-overflow-tooltip>
              <template slot-scope="scope">
                  <div v-if="scope.row.openFlag===1">开启</div>
                  <div v-else>停用</div>
              </template>
          </el-table-column>
        </el-table>
      </el-dialog>
    </div>
</template>

<script type="text/javascript">
import page from '@/components/pagination'
export default {
  components: { page },
  created() {
    const documentHeight = document.body.clientHeight
    this.tableHeight = parseInt(documentHeight * 0.8 - 100) // 计算表高度，固定表头
  },
  activated() {
    this.dataFot()
  },
  data() {
    return {
      tableHeight: 600,
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        searchType: undefined,
        content: undefined
      },
      total: 0,
      tableData: [],
      shopData: [],
      StoreDialogVisible: false,
    }
  },
  methods: {
    contentChange (val){
      if(val === undefined || val === '' || val === null){
        this.Csearch()
      }
    },

    Csearch() { // 搜索操作
      this.pageInfo.page = 1
      this.dataFot()
    },

    handleEdit(index, row) { // 查看操作
      // 查看关联店铺列表
      this.$http.shoplistBycId(this, {userConfigId: row.id}).then(res => {
        if (res.success) {
          this.shopData = res.data
          this.StoreDialogVisible = true
        } else {
          this.$message.error(res.errMessage)
        }
      })
    },
    // ======== 数据 ========
    dataFot() { // 数据列表
      this.loading = true
      this.$http.subAccountConfigList(this, this.pageInfo).then(res => {
        if (res.success) {
          if (res.data.list.length > 0){
            res.data.list.forEach(item => {
              item.ratioStr = ''
              item.levelRatioList.forEach(elem => {
                item.ratioStr += elem.levelName + ':' + elem.ratio + '%;  '
              })
            })
          }
          this.tableData = res.data.list
          this.total = res.data.total
          this.loading = false
        } else {
          this.looking = false
        }
      })
    },

    sizeChange(size) { // 条数
      this.pageInfo.size = size
      this.pageInfo.page = 1
      this.dataFot()
    },

    currentChange(page) { // 页数
      this.pageInfo.page = page
      this.dataFot()
    },
    formatType(row, col, val) {
      switch (val) {
        case 1:
          return '实付金额'
        case 2:
          return '利润金额'
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .function {
    background-color: white;
    .Fheader {
        margin: 10px;
        display: flex !important;
        justify-content: space-between;
        align-items: center !important;
        .Fleft {
            overflow: hidden;
            float: left;
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
            /deep/.el-input{
              .el-input__inner{
                border-top-right-radius: 0;
                border-bottom-right-radius: 0;
              }
            }
            /deep/.box-input{
              .el-input__inner{
                border-top-left-radius: 0;
                border-bottom-left-radius: 0;
                border-left-width: 0;
              }
            }
        }
    }
}
</style>