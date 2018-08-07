<template>
    <div class="recycle">
      <header>
        <h4>仓库回收站</h4>
      </header>
      <el-table border :data="recycle" max-height="700" header-row-class-name="header-row" v-loading="loading" class="tableCenter">
        <el-table-column align="center" type="index" fixed :min-width="50"> </el-table-column>
        <el-table-column align="center" prop="name" label="仓库名称" :min-width="120"> </el-table-column>
        <el-table-column align="center" prop="areaName" label="所属区域" :min-width="120"> </el-table-column>
        <el-table-column align="center" prop="remark" label="仓库描述" :min-width="120"> </el-table-column>
        <el-table-column align="center" prop="syncType" :formatter="booleanFormatter" label="是否集成" :min-width="120"> </el-table-column>
        <el-table-column align="center" prop="updateTime" :formatter="timeFormatter" label="删除时间" :min-width="150"> </el-table-column>
        <el-table-column align="center" label="操作" :min-width="150">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="handleRecover(scope.$index, scope.row)">还原</el-button>
            <el-button class="mini-delete-btn" @click="handleDelete(scope.$index, scope.row, scope)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>
      <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    </div>
</template>

<script>
import {booleanFormatter} from '@/views/order/orderUtils'
import { parseTime } from '@/utils/index'
import page from '@/components/pagination'
import {confirmCreator} from '@/views/order/orderUtils'

export default {
  name: 'recycle',
  components: {
    page,
  },
  activated(){
    this.updateMainData()
  },
  data(){
    return {
      recycle: [],
      total: 0,
      pageInfo: {
        page: 1,
        size: 10
      },
      loading: false
    }
  },
  methods: {
    // ======== 操作 ========
    handleRecover(index, row){ // 还原仓库操作
      confirmCreator(this)('还原仓库', _ => {
        return this.$http.warehouseStatus(this, { id:row.id, openFlag:1 })
      }).then(this.updateMainData)
    },

    handleDelete(index, row){ // 删除仓库操作
      confirmCreator(this)('彻底删除仓库', _ => {
        return this.$http.deleteWarehouse(this, {id:row.id})
      }).then(this.updateMainData)
    },
    
    // ======== 数据 ========
    updateMainData(){
      this.getRecycleData();
    },
    getRecycleData(){ // 仓库列表
      this.loading = true
      this.$http.warehouseList(this, {
        page: this.pageInfo.page, 
        size: this.pageInfo.size,
        openFlag: 0
      }).then(res => {
        if (res.success) {
          this.recycle = res.data.list
          this.total = res.data.total
          this.loading = false
        } else {
          this.loading = false
        }
      })
    },

    sizeChange(size) {
      this.pageInfo.size = size;
      this.getRecycleData();
    },

    currentChange(page) {
      this.pageInfo.page = page;
      this.getRecycleData();
    },

    // ======== 转换 ========
    booleanFormatter(row, column, cellValue){
      return booleanFormatter(cellValue)
    },

    timeFormatter(row, column, cellValue) { // 时间格式化
      return parseTime(cellValue)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  $lineHight: 40px;
  $lakeBlue: rgb(33,184,203);
  .recycle{
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      h4{
        margin-left: 30px;
        font-weight: 350;
       font-size: 16px;
      }
    }
    .wraper{
      height: 100%;
      .btn-add{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
      }
    }
  }
</style>
