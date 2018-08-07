<template>
  <main class="frozen-window">
    <div class="function">
      <div class="search">
        <el-input placeholder="分销商用户名" size="mini" class="box-search" style="width: 140px;" v-model.trim="content"></el-input>
        <button class="mini-search-btn box-btn" @click="filter(content)">搜索</button>
      </div>
    </div>
    <el-table :data="tableData" border max-height="700" @selection-change="onSelectionChange" class="tableCenter" v-loading="loading">
      <el-table-column align="center" type="selection"> </el-table-column>
      <el-table-column align="center" prop="name" label="分销商用户名"></el-table-column>
      <el-table-column align="center" prop="registerName" label="姓名"></el-table-column>
      <el-table-column align="center" prop="mobile" label="手机"></el-table-column>
    </el-table>
    <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    <div style="text-align: center;padding: 10px">
      <el-button class="mini-search-btn" @click="dialogShow()">确定</el-button>
    </div>
    <el-dialog :modal-append-to-body="false" center append-to-body title="设置冻结金额" :visible.sync="dialogVisible" width="900px">
      <execFrozen @confirmedFrozen="confirmedFrozen" :givenItems="chosenItems"></execFrozen>
    </el-dialog>
  </main>
</template>

<script>
import eventBus from '@/views/order/eventBus'
import pagination from '@/components/pagination/index'
import {getDistributorsToFroze, getDistributorsToFrozeCount} from '@/views/financial/financialData'
import execFrozen from '@/views/financial/frozen/execFrozen'
import page from "@/components/pagination/index"

export default {
  name: 'frozenWindow',
  created(){
    this.updateMainData();
    eventBus.$on('addFreezingsShow',payLoad => {
      this.dialogVisible = false
    })
  },
  components: { pagination, execFrozen, page} ,
  data(){
    return {
      loading: false,
      tableData: [], //主要数据
      pageInfo: { //分页信息
        page: 1,
        count: 10
      },
      total: 0,
      chosenItems: [], // 勾选的项目
      dialogVisible: false,
      content: ''
    }
  },
  methods:{
    // ======== 操作 ========
    dialogShow() { // 选择冻结用户操作
      if(this.chosenItems == '') {
        this.$message.warning({
          message: "请选择要冻结的分销商",
          duration: 3 * 1000,
        });
      }else {
        this.dialogVisible = true
      }
    },

    filter() {  // 搜索操作
      this.pageInfo.page = 1
      this.updateMainData();
    },

    // ======== 数据 ========
    updateMainData(){  // 选择用户主要数据
      this.loading = true;
      const params = {};
      Object.assign(params, this.pageInfo);
      params.content = this.content;

      getDistributorsToFrozeCount(this, params).then(res => {
        this.total = res.count
      })

      return getDistributorsToFroze(this, params).then(res => {
        this.tableData = res.distributors
        res.code == 0 ? this.loading = false : this.looking = false
      })
    },

    sizeChange(size){  // 分页
      this.pageInfo.count = size;
      this.updateMainData();
    },

    currentChange(page){ // 分页
      this.pageInfo.page = page
      this.updateMainData();
    },

    onSelectionChange(val){  // 勾选用户的值 > 传到下个嵌套的dialog
      this.chosenItems = val;
    },

    // ======== 监听 ========
    confirmedFrozen() { // 监听子组件的成功冻结
      this.dialogVisible = false;
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss">
  .frozen-window{
    overflow: hidden;
    .function {
      float: right;
      // padding: 0 10px 20px 0;
      .search {
        padding: 10px;
        .box-btn {
          position: relative;
          top: -1px;
        }
      }
    }
    th{
      text-align: center;
    }
    .el-dialog__body{
      padding-top: 0;
    }
  }
</style>