<template>
  <transition name="fade" mode="out-in">
    <div class="warehouse-list">
      <header>
        <span>库存调拨列表</span>
        <router-link :to="{ name: 'stockAllocation'}">
          <el-button class="btn-add">库存调拨</el-button>
        </router-link>
      </header>

      <section v-if="!coverSwitch">
        <div class="function">
          <div class="search">
            <el-select v-model="searchMode" size="mini">
              <el-option value="goodsNo" label="商品编号"> </el-option>
              <el-option value="warehouseOutId" label="调出仓库"> </el-option>
              <el-option value="warehouseInId" label="调入仓库"> </el-option>
            </el-select>
            
            <el-input placeholder="请输入内容" v-model="searchWord" size="mini" class="box-search" />
            <el-button size="mini" class="btn-search" @click="search">搜索</el-button>
          </div>
        </div>
        <el-table :data="allocates" border max-height="700" style="width:100%" header-row-class-name="header-row">
          <el-table-column align="center" type="index" fixed>
          </el-table-column>
          <el-table-column align="center" prop="id" label="调拨单号"> </el-table-column>
          <el-table-column align="center" prop="warehouseOutId" label="调出仓库"> </el-table-column>
          <el-table-column align="center" prop="warehouseInId" label="调入仓库"> </el-table-column>
          <el-table-column align="center" :formatter="timeFormatter" label="调拨时间" width="150"> </el-table-column>
          <el-table-column align="center" prop="allocateCount" label="调拨数量" width="150"> </el-table-column>
          <el-table-column align="center" :formatter="statusFomater" label="状态" width="150"> </el-table-column>
          <el-table-column align="center" label="操作" fixed="right" width="200">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleConfirm(scope.$index, scope.row)">确认</el-button>
              <el-button size="mini" type="danger" @click="handleCancel(scope.$index, scope.row, scope)">取消</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
      </section>

      <section v-if="coverSwitch">
        <el-form ref="form" :model="form" label-width="180px" class="el-form">
        <el-form-item label="仓库编码" prop="no">
          <el-input v-model="form.no" />
        </el-form-item>

        <el-form-item label="仓库名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>

        <el-form-item label="所属区域" prop="areaId">
          <el-select v-model="form.areaId" placeholder="请选择活动区域" value="11">
            <el-option v-for="salesArea in salesAreas" :value="salesArea.name" :key="salesArea.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="是否集成"  prop="syncType">
          <el-radio-group v-model="form.syncType">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="是否平台仓" prop="isPlatform">
          <el-radio-group  v-model="form.isPlatform">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="仓库描述" prop="remark">
          <el-input type="textarea"  v-model="form.remark" :autosize="{ minRows: 5, maxRows: 8}"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit('form')">修改仓库</el-button>
          <el-button @click="onRevert">取消</el-button>
        </el-form-item>
        
      </el-form>
      </section>
    </div>
  </transition>
</template>

<script>
import { isvalidUsername } from '@/utils/validate'
import md5 from 'js-md5'
import Cookies from 'js-cookie'
import store from '@/store'
import pagination from '@/components/pagination/index'
import {parseTime} from '@/utils/index'
import {getAllocationList, getAllocatsCount, confirmAllocatsCount} from '@/views/warehouse/warehousesData'

import {
  getToken,
  getId
} from '@/utils/auth'
export default {
  name: 'login',
  mounted(){
    window.onkeydown = this.onEscPress;
    this.getWarehousesData();
    this.getTotal();
    getAllocationList(this)
    .then(data => {this.allocates = data.allocates})
    // this.getSaleareas();
  } ,
  components: {
    pagination
  } ,
  data(){
    return {
      checkedWarehouse: null,
      form: {
          name: '',
          no: '',
          areaId: '',
          isPlatform: '',
          syncType: '',
          remark: '',
          id: '',
      },
      salesAreas: [1,2],
      warehouses: [
      ],
      warehousesId: '',
      searchMode: '商品编号', //搜索相关*2
      searchWord: '',
      pageInfo: {
        page: 1,
        size: 10
      },
      total: 1,
      coverSwitch: false,
      allocates: [],
    }
  },
  methods:{
    parseTime,
    getAllocationList,
    getAllocatsCount,
    confirmAllocatsCount,
    search(){
      const params = {};
      params[this.searchMode] = this.searchWord;

      this.getAllocationList(this, params)
      .then(data => {this.allocates = data.allocates});

      this.getTotal();
    },
    timeFormatter(row, column, cellValue){
      return this.parseTime(row.createTime)
    },
    statusFomater(row, column, cellValue){
      switch(row.confirmStatus){
        case 0:
          return '未确认'
        case 1:
          return '确认中'
        case 2:
          return '取消'
      }
    },
    onRevert(){
      this.coverSwitch = false;
    },
    getSaleareas(){
        this.$http.getSalesareaList(this, {page:1, size:100}).then(res => {
          if (res.success) {
            this.salesAreas = res.data.list
          }
        })
    },
    onSubmit(){
      this.$confirm('确定要编辑仓库吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      })
      .then(_ => {
        return null
      })
      .then(res => {
        if(res.code === 0){
          this.$message({
            message: '编辑成功',
            type: 'success',
            duration: 3 * 1000,
          });
          this.$router.push('/warehouse/list');
        }else{
          this.$message({
            message: '编辑失败',
            type: 'warning',
            duration: 3 * 1000,
          });
        }
      })
      .catch(e => console.log(e))
    },
    handleConfirm(index, row){
      this.$confirm('此操作将确认调拨，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      })
      .then(_ => {
        this.confirmAllocatsCount(this, {id: row.id, confirmStatus: 2})
        .then(_ => {this.getAllocationList(this); this.getAllocatsCount(this)})
      })
      .catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    onEscPress(event){
      if(event.keyCode == 27){
        this.coverSwitch = false;
      }
    },
    handleCancel(index, row, scope){
      this.$confirm('此操作将取消该调拨，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      })
      .then(_ => {

        this.$api.put(this, 'admin/u/p/warehouse/stock/allocate/confirm',{id:row.id,  confirmStatus: 3})
        .then(_ => this.getWarehousesData())
      })
      .catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消调拨'
        })
      })
    },
    getWarehousesData(){
      this.$http.warehouseList(this, {page: this.pageInfo.page, size: this.pageInfo.size})
      .then(data => {
        this.$data.warehouses = data.data.list;
      })
    },
    // 分页方法*3
    getTotal() {
      this.getAllocatsCount(this).then(res => {
          this.total = res.count;
        })
    },
    onSizeCHange(val){
      this.pageInfo.size = val;
      this.getWarehousesData();
    },
    onCurrentChange(val){
      this.pageInfo.page = val;
      this.getWarehousesData();
    },

  },
  watch: {
    checkedWarehouse(){
      for(let attr in this.form){
        this.form[attr] = ('' + this.checkedWarehouse[attr]);
      }
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .warehouse-list{
    background-color: white;
    height: 100%;
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      span{
        margin-left: 30px;
      }
      .btn-add{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
      }
    }
    .function{
      padding: 16px;
      background-color: white;
      .btn-export{
        background-color: lighten(grey, 40%);
      }
      .search{
        float: right;
      }
      .box-search{
        width: 140px;
      }
      .btn-search{
        background-color: $lakeBlue;
        color: white;
      }
    }
    .header-row {
      background-color: $table-header-bg;
      th {
        padding: 5px;
        text-align: center;
      }
    }
    td {
      text-align: center;
    }
    .table-cell{
      text-align: center;
    }
    .el-form{
      width: 700px;
      margin-top: 30px;
    }
  }