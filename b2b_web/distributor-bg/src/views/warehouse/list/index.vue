<template>
    <div class="warehouse-list">
      <section>
        <header>
          <h4>仓库列表</h4>
          <el-button v-if="isSuperAdmin || hasAddWarehRight" class="mini-add-btn btn-home" icon="el-icon-plus" @click="addpuls()"> 添加仓库 </el-button>
        </header>

        <div class="search">
          <div class="search-right">
            <button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
            <el-input v-model.trim="searchContent"  @change="contentChange" @keyup.enter.native="onSearch()" placeholder="仓库名称/所属区域" size="mini" class="box-search" clearable=""></el-input>
          </div>
        </div>
        <el-table :data="warehouses" border max-height="700" style="width:100%" header-row-class-name="header-row" class="tableCenter" v-loading="loading">
          <el-table-column align="center" prop="sortNo" label="排序" :min-width="50"> </el-table-column>
          <el-table-column align="center" prop="name" label="仓库名称" :min-width="120" > </el-table-column>
          <!-- <el-table-column align="center" prop="areaName"  label="所属区域" :min-width="120"> </el-table-column> -->
          <el-table-column align="center" prop="remark" label="仓库描述" :min-width="120"> </el-table-column>
          <el-table-column align="center" prop="openFlag" label="状态" :min-width="120">
            <template slot-scope="scope">
              <div v-if="scope.row.openFlag===1">开启</div>
              <div v-else>停用</div>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="syncType" :formatter="booleanFormatter" label="是否集成" :min-width="120"> </el-table-column>
          <el-table-column align="center" prop="calculationType" :formatter="calculationFormatter" label="参与存销比计算" :min-width="120"> </el-table-column>
          <el-table-column align="center" label="操作" :min-width="260">
            <template slot-scope="scope">
              <el-button class="mini-tableadd-btn" @click="handleUp(scope.$index,scope.row)">上移</el-button>
              <el-button class="mini-freeze-btn" @click="handleDown(scope.$index,scope.row)">下移</el-button>
              <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
              <el-button class="mini-delete-btn" @click="handleStatus(scope.$index, scope.row)">禁用</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
      </section>
    </div>
</template>

<script>
import pagination from '@/components/pagination/index'
import {booleanFormatter} from '@/views/order/orderUtils'
import {calculationFormatter} from '@/views/warehouse/warehousesUtils'
export default {
  name: 'warehouseList',
  activated(){
    this.getWarehousesData();  // 仓库列表数据
  },
  
  components: { pagination },
  computed:{
    hasPermis() {
      return this.$store.getters.hasPermis
    },

    /** 权限, 废弃 */
    permissions() {
      return this.$store.getters.permissions.warehouse
    },
    /** 是否超级管理员 */
    isSuperAdmin() {
      return this.$store.getters.userinfo.adminType === 1
    },
    /** ------------- 本页面特有的权限判断under ------------- */
    hasOperaRight(){
      const actions = this.permissions.actions;
      return actions.filter(obj => obj.module === 'warehouse-put').length
    },
    hasAddWarehRight(){
      const menus = this.permissions.menus;
      return menus.filter(obj => obj.module === 'warehouse-add').length
    },
    
  },
  data(){
    return {
      loading: false,
      addpend: true,
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
      salesAreas: [],
      warehouses: [],
      warehousesId: '',
      searchWord: '',
      pageInfo: {
        page: 1,
        size: 10
      },
      searchContent:'',
      total: 1,
      editedAccount: {}
    }
  },
  methods:{
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.getWarehousesData()
      }
    },
    // ======== 操作 ========
    onSearch(){ // 搜索操作
      this.getWarehousesData()
    },
    addpuls() { // 添加仓库列表
      this.$router.push({name: 'warehouseAdd'})
    },
    
    handleSearch() { //..搜索操作
      this.getWarehousesData()
    },
    handleUp(index, row) {  // 上移
      this.$http.warehouseUoD(this, { id: row.id, flag: true }).then(res => {
        if (res.success) {
          this.getWarehousesData()
        }
      })
    },
    handleDown(index, row) {  // 下移
      this.$http.warehouseUoD(this, { id: row.id, flag: false }).then(res => {
        if (res.success) {
          this.getWarehousesData()
        }
      })
    },
    handleEdit(index, row){ // 编辑操作
      this.$router.push({ name: 'warehouseAdd', query: {id : row.id}})
    },
    // 禁用仓库
    handleStatus(index, row){
      this.$confirm('确定要禁用该仓库吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.warehouseStatus(this, { id: row.id, openFlag: 0 }).then(res =>{  
          if(res.success) {
            this.$message.success({
              message: "禁用成功",
              duration: 3 * 1000,
              onClose: () => { }
            })
            this.getWarehousesData()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        });
      });
    },
    
    handleDelete(index, row){ // 删除操作
    this.$confirm('确定要删除该仓库吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.deleteaddWarehouse(this, { id: row.id }).then(res =>{  
          if(res.success) {
            this.$message.success({
              message: "删除成功",
              duration: 3 * 1000,
              onClose: () => { }
            })
            this.getWarehousesData()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    
    // ======== 数据 ========
    getWarehousesData(){ // 仓库列表主数据
      this.loading = true
      this.$http.warehouseList(this, {
        page: this.pageInfo.page, 
        size: this.pageInfo.size,
        openFlag: 1,
        content:this.searchContent
      }).then(res => {
        if (res.success) {
          this.warehouses = res.data.list
          this.total = res.data.total
          this.loading = false
        } else {
          this.loading = false
        }
      })
    },

    onSizeCHange(val){
      this.pageInfo.count = val;
      this.getWarehousesData();
    },

    onCurrentChange(val){
      this.pageInfo.page = val;
      this.getWarehousesData();
    },

    // ======== 转换 ========
    booleanFormatter(row, column, cellValue){
      return booleanFormatter(cellValue)
    },

    // ======== 转换 ========
    calculationFormatter(row, column, cellValue){
      return calculationFormatter(cellValue)
    },

    areaFormatter(row){
      return this.saleareas[row.areaId]
    },
  },
  watch: {
    checkedWarehouse(){
      for(let attr in this.form){
        this.form[attr] = ('' + this.checkedWarehouse[attr]);
      }
    },
    'form.areaId': function(val){
      this.form.areaId = parseInt(val)
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss" sceopd>
  .el-table .cell{
    overflow: hidden; 
    white-space:nowrap; 
  }
  .warehouse-list{
    background-color: white;
    height: 100%;
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
    .search{
      width: 100%;
      padding: 10px;
      margin-bottom: 5px;
      overflow: hidden;
      .search-right {
        float: right
      }
    }
    .box-search{
      width: 250px;
    }
    .box-btn {
      margin-left: 5px;
      float: right;
    }
  }
</style>
