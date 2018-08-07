<template>
    <div class="category-list">
       <header>
        <h4>合作中的分销商列表</h4>
      </header>
      <div class="function">
        <div class="Fheader">
          <div class="Fleft">
            <el-select size="mini"  v-model="pageInfo.treeNode" placeholder="级数" style="width: 100px;" clearable>
              <el-option :key="item" :label="item" :value="item" v-for="item in treeNodes"></el-option>
            </el-select>
          </div>
          <div class="Fsearch">
            <button class="mini-search-btn box-btn" @click="Csearch()">搜索</button>
            <el-input v-model.trim="mytext" size="mini" clearable @change="contentChange" @keyup.enter.native="Csearch()" placeholder="请输入关键词搜索" class="box-input"></el-input>
            <el-select size="mini"  v-model="pageInfo.contentType" placeholder="分销商用户名" style="width: 140px;" clearable>
              <el-option label="分销商用户名" value="1"> </el-option>
              <el-option label="客户名称" value="2"> </el-option>
              <el-option label="分销商ID" value="3"> </el-option>
              <el-option label="上级分销商客户名" value="4"> </el-option>
              <el-option label="手机号" value="5"> </el-option>
            </el-select>
          </div>
        </div>
      </div>
      <el-row v-loading="loading">
        <el-table :data="tableData" header-row-class-name="header-row" border class="tableCenter"  :height="tableHeight">
          <!-- <el-table-column align="center" label="上级分销商" prop="upCompanyName" :min-width="120"></el-table-column> -->
          <el-table-column label="B2B编码" align="center" prop="id" :min-width="120"></el-table-column>
          <el-table-column label="分销商名称" align="center" prop="name" :min-width="120"></el-table-column>
          <el-table-column label="级数" align="center" prop="treeNode" :min-width="120"></el-table-column>
          <el-table-column label="联系人" align="center" prop="userName" :min-width="120"></el-table-column>
          <el-table-column label="联系人手机号" align="center" prop="phone" :min-width="120"></el-table-column>
          <el-table-column sortable  align="center" label="更新时间" prop="updateTime" :formatter="formatTime" :min-width="160"></el-table-column>
          <el-table-column label="状态" align="center" prop="applyStatus" :formatter="forstatus" :min-width="120"></el-table-column>
          <el-table-column label="操作" :min-width="220" align="center">
            <template slot-scope="scope">
              <el-button class="mini-tableadd-btn" @click="handleStatus(scope.row)" v-if="scope.row.applyStatus===1">审核</el-button>
              <!-- v-if="isSuperAdmin || hasPermis.actions['distributor-cooperating-get']"-->
              <el-button class="mini-search-btn" @click="handleEdit(scope.$index,scope.row)">查看</el-button>
              <el-button class="mini-tableadd-btn" @click="handleStart(scope.row)" v-if="scope.row.freezeStatus===2">启用</el-button>
              <el-button class="mini-freeze-btn" @click="handleStop(scope.row)" v-else>冻结</el-button>
            </template>
          </el-table-column>
        </el-table>
        <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
      </el-row>
    </div>
</template>

<script type="text/javascript">

import treeTable from '@/components/TreeTable'
import page from '@/components/pagination'
import { timeFormat } from '@/utils/timeFormat.js'
export default {
  name: 'distributorcooperating',
  components: { treeTable, page },
  created() {
    const documentHeight = document.body.clientHeight
    this.tableHeight = parseInt(documentHeight * 0.8 - 100) // 计算表高度，固定表头
  },
  activated() {
    // 级数列表
    this.iniTree()
    this.dataFot()
  },
  computed: {
    isSuperAdmin() { /** 是否超级管理员 */
      return this.$store.getters.userinfo.adminType === 1
    },
    hasPermis() { /** 是否有操作权限 */
      return this.$store.getters.hasPermis
    }
  },
  data() {
    return {
      tableHeight: 600,
      loading: false,
      editDistributor: null,
      batch: '',
      pageInfo: {
        page: 1,
        size: 10,
        treeNode: '',
        contentType: '',
        content: this.mytext
      },
      total: 0,
      tableData: [],
      treeNodes: [],
      mytext: '',
      scope: [],
      forEach: []
    }
  },
  methods: {
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.Csearch()
      }
    },
    Csearch() { // 搜索操作
      this.pageInfo.page = 1
      this.pageInfo.content = this.mytext
      this.dataFot()
    },

    handleEdit(index, row) { // 查看操作
      // node 1 一级分销商  2 多级分销
      this.$router.push({ name: 'distributorcooperatingadd', query: { id: row.id, checkMsg: 1, node: 2 }})
    },
    // 审核
    handleStatus (row) {
      this.$confirm('审核通过或拒绝', '分销商审核', {
          confirmButtonText: '通过',
          cancelButtonText: '拒绝',
          center: true
        }).then(_ => {
          // 审核通过
          this.$http.disNextCheck(this, {id: row.id, applyStatus: 2}).then(res => {
            if (res.success) {
              this.dataFot()
            }
          })
        }).catch(_ => {
          // 拒绝
          this.$http.disNextCheck(this, {id: row.id, applyStatus: 3}).then(res => {
            if (res.success) {
              this.dataFot()
            }
          })
        })
    },
    handleStop(row) { // 冻结操作
      this.$confirm('此操作将冻结该分销商，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        this.loading = true
        this.$http.distributorStatus(this, { id: row.id, freezeStatus: 2 }).then(res => {
          if (res.success) {
            this.$message({
              message: '成功冻结',
              type: 'success',
              duration: 3 * 1000,
              onClose: () => {}
            })
            this.dataFot()
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        })
      })
    },

    handleStart(row) { // 启用操作
      this.$http.distributorStatus(this, { id: row.id, freezeStatus: 1 }).then(res => {
        if (res.success) {
          this.dataFot()
        }
      })
    },
    // ======== 数据 ========
    iniTree () {
      this.$http.baseSetting(this, {key: 'distribution_layers'}).then(res => {
        if (res.success) {
          this.treeNodes = []
          for(let i=2;i<=res.data.value;i++) {
            this.treeNodes.push(i)
          }
        }
      })
    },
    dataFot() { // 数据列表
      this.loading = true
      this.$http.getDistributorNList(this, this.pageInfo).then(res => {
        if (res.success) {
          const ary = []
          res.data.list.forEach(item => {
            item.children = []
            ary.push(item)
          })
          ary.sort((a, b) => {
            return a.sort - b.sort > 0
          })
          this.tableData = ary
           this.total = res.data.total
          this.loading = false
        } else {
          this.loading = false
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
    // ======== 转换 ========
    formatTime(row, col, val) { // 表格时间格式化
      return timeFormat(val)
    },
    forstatus(row) { // 申请状态
      switch(row.applyStatus) {
        case 0:
          return '未提交';
        case 1: 
          return '待审核';
        case 2:
          return '已通过';
        case 3:
          return '已拒绝';
      }
    },
  },
  watch: {
    "pageInfo.treeNode":function(){
      this.pageInfo.page = 1
      this.dataFot()
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import '../scss/cooperatinglist.scss';
  .Fsearch{
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
</style>