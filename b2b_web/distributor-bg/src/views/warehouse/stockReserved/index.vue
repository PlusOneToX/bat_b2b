<template>
    <div class="stock-reserved">
      <header>
          <span>预留明细列表</span>
          <el-button icon="el-icon-plus" class="mini-add-btn btn-add" @click="addReserved()">添加预留</el-button>
      </header>
      <div class="search">
        <el-select class="custom_select" placeholder="预留状态" size="mini" v-model="pageInfo.status" clearable>
          <el-option :key="item.id" :label="item.label" :value="item.id" v-for="item in statuss"> </el-option>
        </el-select>

        <div class="search-right">
          <button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
          <el-input v-model.trim="pageInfo.content" @keyup.enter.native="onSearch()" @change="contentChange" placeholder="预留备注/业务单号" size="mini" class="box-search" clearable ></el-input>
        </div>
      </div>

      <el-table :data="reserveds" border header-row-class-name="header-row" v-loading="loading">
        <el-table-column align="center" prop="id" label="编号" width="120" > </el-table-column>
        <el-table-column align="center" prop="businessId" label="业务单号" > </el-table-column>
        <!-- <el-table-column align="center" prop="source" label="预留来源" :formatter="formatSource"  > </el-table-column> -->
        <el-table-column align="center" prop="status" label="预留状态" :formatter="formatStatus" > </el-table-column>
        <el-table-column align="center" prop="remark" label="预留备注" show-overflow-tooltip > </el-table-column>
        <el-table-column align="center" prop="createTime" label="创建时间" :formatter="timeFormat" > </el-table-column>
        <el-table-column align="center" label="操作" width="180">
            <template slot-scope="scope">
              <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
              <el-button class="mini-delete-btn" v-if="scope.row.status === 0" :loading="releaseLoading" @click="handleRelease(scope.row)">释放</el-button>
            </template>
          </el-table-column>
      </el-table>
      <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
    </div>
</template>

<script>
import {timeFormat} from '@/utils/timeFormat'
import pagination from '@/components/pagination/index'

export default {
  name: 'stockReserved',
  data(){
    return {
      reserveds: [],
      loading:true,
      releaseLoading:false,
      pageInfo: {
        page: 1,
        size: 10,
        status:undefined,
        // source:undefined,
        content:undefined
      },
      statuss:[{
        id:0,
        label:"预留中"
      },{
        id:1,
        label:"已释放"
      }],
      total: 0,
    }
  },
  activated(){
   this.getData()
  } ,
  watch: {
   'pageInfo.status': {
      handler() {
        this.pageInfo.page = 1
        this.getData()
      },
      deep: true
    },
    // 'pageInfo.source': {
    //   handler() {
    //     this.pageInfo.page = 1
    //     this.getData()
    //   },
    //   deep: true
    // },
  },
  components: {
    pagination
  } ,
  methods: {
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.onSearch()
      }
    },
    onSearch(){
      this.pageInfo.page = 1
      this.getData()
    },
    // formatSource(row, col, val) { // 预留来源
    //   switch(val) {
    //     case 1:
    //       return "手工添加";
    //       break;
    //     case 2:
    //       return "收单工具";
    //       break;
    //   }
    // },
    formatStatus(row, col, val) { // 预留状态
      switch(val) {
        case 0:
          return "预留中";
          break;
        case 1:
          return "已释放";
          break;
      }
    },
    timeFormat(row, col, val) { // 时间戳转换
      if(val === 0){
        return '-'
      }else{
        return timeFormat(val)
      }
    },
    handleRelease(row){
      this.releaseLoading = true
      this.$http.stockRelease(this, {id:row.id}).then(res => { 
        if(res.success){
          this.$message({
            message: "预留释放成功",
            type: "success",
            duration: 1 * 1000,
            onClose: () => {
              this.getData()
            }
          });
        }
        this.releaseLoading = false
      })
    },
    getData(){
      this.loading = true
      this.$http.stockReservedList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.reserveds = res.data.list
          this.total = res.data.total
        }
        this.loading = false
      })
    },
    handleEdit(index, row){ // 编辑操作
      this.$router.push({ name: 'stockReservedDetail', query: {id : row.id}})
    },
    addReserved(){
      this.$router.push({name: 'stockReservedDetail'})
    },
    onSizeCHange(val){
      this.pageInfo.size = val
      this.pageInfo.page = 1
      this.getData();
    },
    onCurrentChange(val){
      this.pageInfo.page = val
      this.getData();
    },
  },
  
  computed: {

  }
}
</script>

<style rel="stylesheet/scss" lang="scss">

  .stock-reserved{

    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      span{
        margin-left: 30px;
      }
    }
    .search{
      width: 100%;
      padding: 10px;
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
    .btn-add{
      float: right;
      padding:5px;
      margin-top:7px;
      margin-right: 10px;
    }
    .wraper{
      height: 100%;
      /*background-color: blue;*/
      .btn-add{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
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
    
  }

</style>
