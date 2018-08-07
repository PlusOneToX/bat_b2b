<template>
  <div class="brand-list">
    <!-- <page-header title="商品品牌列表" button="添加品牌" icon="el-icon-plus" @handle-click="add"></page-header> -->

    <header>
      <h4>商品品牌列表</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add()"> 添加品牌 </el-button>
    </header>

    <el-table :data="tableData" @selection-change="handleSelectionChange" border header-row-class-name="header-row" class="tableCenter" v-loading="loading" :height="tableHeight">
      <!-- <el-table-column type="index" fixed :min-width="50"></el-table-column> -->
      <el-table-column align="center" label="品牌名称" prop="name"  show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="品牌英文名称" prop="nameEn"  show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="品牌描述" prop="description"  show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="可视范围" prop="distributorScope" show-overflow-tooltip :formatter="formatDistributor" ></el-table-column>
      <el-table-column align="center" label="品牌状态" prop="openFlag" :formatter="formatStatus" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="排序" prop="sort" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="操作" :min-width="120">
        <template slot-scope="scope">
          <el-button class="mini-search-btn"  @click="handleEdit(scope.$index, scope.row)">查看</el-button>
          <el-button class="mini-tableadd-btn" v-if="!scope.row.openFlag"  @click="handleStart(scope.$index, scope.row)">启用</el-button>
          <el-button class="mini-freeze-btn" v-else  @click="handleStop(scope.$index, scope.row)">停用</el-button>
          <el-button class="mini-delete-btn" v-if="!scope.row.openFlag"  @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
  </div>
</template>
<script>
import PageHeader from "@/components/PageHeader"
import page from "@/components/pagination"
export default {
  data() {
    return {
      tableHeight: 600, //给表格高度一个默认高度，以防没有计算到表格高度
      loading: false,
      batch:'',
      action:'',
      tableData:[],
      pageInfo:{
        page:1,
        // count:10
        size: 10
      },
      total:'',
      multipleSelection:[]
    }
  },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
  },
  activated() {
    this.getTableData()
  },
  mounted() {
    this.getTableData()
  },
  components: {
    PageHeader,
    page,
  },
  methods:{
    add(){ // 添加品牌
      this.$router.push({name:'addbrand'})
    },

    sizeChange(size) { // 总条数
      this.pageInfo.size = size;
    },

    currentChange(page) { // 分页
      this.pageInfo.page = page;
    },
    getTableData(){
      this.loading = true
      this.$http.getBrandList(this,this.pageInfo).then(res=>{
        this.tableData = res.data.list
        this.total = res.data.total
        this.loading = false
      })
    },
    // 品牌状态
    formatStatus(row,col,val){
      if(val){
        return '启用'
      }else{
        return '停用'
      }
    },
    // 可视范围
    formatDistributor(row,col,val){
      switch(val)
      {
        case 0:
        return '不指定';
        break;
        case 1:
        return '全部';
        break;
        case 2:
        return '分销商等级';
        break;
        case 3:
        return '指定分销商';
        break;
        case 4:
        return '指定销售部门';
        break;
        case 5:
        return '指定业务员';
        case 6:
        return '分销商分组';
        break;
        case undefined:
        return '--';
        break;
      }
    },
    handleSelectionChange(val){
      this.multipleSelection=val
    },
    handleEdit(index,row){
      this.$router.push({name:'editbrand',params:{id:row.id}})
    },
    handleDelete(index,row){
      this.$confirm('确定删除此分类?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(()=>{
        // 删除品牌
        this.$http.deleteBrand(this, {
          id: row.id
        }).then(res => {
          if (res.success) {
            this.getTableData()
          }
        })
      })
    },
    handleStart(index,row){
     this.$confirm('确定启用此品牌？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(()=>{
        // 启用品牌
        this.$http.brandStatus(this, {
          id: row.id,
          openFlag: 1
        }).then(res => {
          if (res.success) {
            this.getTableData()
          }
        })
      })
    },
    handleStop(index,row){
      this.$confirm('确定停用此品牌？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(()=>{
        // 停用品牌
        this.$http.brandStatus(this, {
          id: row.id,
          openFlag: 0
        }).then(res => {
          if (res.success) {
            this.getTableData()
          }
        })
      })
    }
  },
  watch: {
    pageInfo: {
      handler() {
        this.getTableData()
      },
      deep: true
    },

  }
}

</script>
<style lang="scss" scoped>
.brand-list{
  background-color:white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
  }
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
  .action{
    margin:20px 0;
  }
  .header-row {
    @include table-header-color;
  }
}

</style>
