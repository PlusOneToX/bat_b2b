<template>
  <div class="specification-list">
    <header>
      <h4>商品属性列表</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add">
        添加属性
      </el-button>
    </header>

    <div class="nav">
      <div class="Fheader">
        <el-radio-group v-model="pageInfo.attributeType" size="mini">
          <el-radio-button :label="1">规格</el-radio-button>
          <el-radio-button :label="2">颜色</el-radio-button>
          <el-radio-button :label="3">参数</el-radio-button>
        </el-radio-group>
        <div class="search">
          <el-button class="mini-search-btn box-btn" @click.prevent="filter()">搜索</el-button>
          <el-input v-model.trim="pageInfo.content" @keyup.enter.native="filter()" :placeholder="pageInfo.attributeType == 1?'规格名称':(pageInfo.attributeType == 2?'颜色名称':'参数名称')" size="mini" class="box-search box-input"></el-input>
        </div>
       </div>
    </div>

    <el-table :data="tableData" @selection-change="handleSelectionChange" header-row-class-name="header-row" border class="tableCenter" v-loading="loading">
      <el-table-column align="center" :label="pageInfo.attributeType == 1?'规格名称':(pageInfo.attributeType == 2?'颜色名称':'参数名称')" prop="name" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" :label="pageInfo.attributeType == 1?'规格英文名称':(pageInfo.attributeType == 2?'颜色英文名称':'参数英文名称')" prop="nameEn" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" :label="pageInfo.attributeType == 1?'规格备注':(pageInfo.attributeType == 2?'颜色备注':'参数备注')" prop="description" show-overflow-tooltip></el-table-column>
      <!-- <el-table-column align="center" :label="pageInfo.attributeType == 1?'规格值':(pageInfo.attributeType == 2?'颜色值':'参数值')" prop="values" :formatter="formatValue" show-overflow-tooltip></el-table-column> -->
      <el-table-column align="center" :label="pageInfo.attributeType == 1?'规格状态':(pageInfo.attributeType == 2?'颜色状态':'参数状态')" prop="openFlag" :formatter="formatStatus" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="操作" :min-width="80">
        <template slot-scope="scope">
          <el-button class="mini-search-btn" size="mini" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
          <el-button class="mini-tableadd-btn" v-if="scope.row.openFlag == 0" size="mini" @click="handleChangeStatus(1, scope.row)">启用</el-button>
          <el-button class="mini-freeze-btn" v-else size="mini" @click="handleChangeStatus(0, scope.row)">停用</el-button>
          <el-button class="mini-delete-btn" size="mini" v-if="scope.row.openFlag == 0" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
  </div>
</template>
<script>
import page from "@/components/pagination"
export default {
  data() {
    return {
      loading: false,
      batch:'',
      action:'',
      tableData:[],
      pageInfo:{
        page:1,
        size:10,
        content:'',
        openFlag: undefined,
        attributeType: 1
      },
      total:'',
      multipleSelection:[],
    }
  },
  activated () {
    this.getTableData();
  },
  components: { page },
  methods:{
    add(){
      this.$router.push({name:'addspecification'})
    },
    filter(){
      this.pageInfo.page = 1
      this.getTableData();
    },
    sizeChange(size) {
      this.pageInfo.page = 1
      this.pageInfo.size = size;
      this.getTableData()
    },
    currentChange(page) {
      this.pageInfo.page = page;
      this.getTableData()
    },
    // 获取商品属性列表
    getTableData(){
      this.loading = true;
      this.$http.getAttributeList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
        }
         this.loading = false
      })
    },
    formatValue(row,col,val){
      let ary=[];
      row.values.forEach(item=>{
        ary.push(item.name)
      })
      return ary.join(',')
    },
    // 规格状态
    formatStatus(row,col,val){
			switch(val)
			{
				case 1:
				return "启用";
				break;
				case 0:
				return "停用";
				break;
			}
		},
    handleSelectionChange(val){
      this.multipleSelection=val
    },
    handleEdit(index,row){ // 编辑按钮
      this.$store.dispatch('getSpecification',row)
      this.$router.push({name:'editspecification',params:{id:row.id}})
    },
    handleDelete(index,row){
      var str =''
      if(this.pageInfo.attributeType ===1 ){
        str = '确定删除此规格？'
      }else if(this.pageInfo.attributeType ===2 ){
        str = '确定删除此颜色？'
      }else {
        str = '确定删除此参数？'
      }
      this.$confirm(str, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(()=>{
        this.$http.deleteAttribute(this, {id: row.id }).then(res => {
          if (res.success) {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 3 * 1000,
            })
            this.getTableData()
          }
        })
      })
    },
    handleChangeStatus(status,row){
      var str =''
      if(status === 0){
         if(this.pageInfo.attributeType ===1 ){
          str = '确定停用此规格？'
        }else if(this.pageInfo.attributeType ===2){
          str = '确定停用此颜色？'
        }else {
          str = '确定停用此参数？'
        }
      }else {
        if(this.pageInfo.attributeType ===1 ){
          str = '确定启用此规格？'
        }else if(this.pageInfo.attributeType ===2 ){
          str = '确定启用此颜色？'
        }else{
          str = '确定启用此参数？'
        }
      }
      this.$confirm(str, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(()=>{
        this.$http.attributeStatus(this, {id: row.id, openFlag:status }).then(res => {
          if (res.success) {
            this.getTableData()
          }
        })
      })
    }
  },
  watch: {
    'pageInfo.attributeType': {
      handler() {
        this.pageInfo.page = 1
        this.getTableData()
      },
      deep: true
    }
  }
}

</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.specification-list{
  background-color:white;
  min-height:100%;
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
		margin-top: 8px;
		margin-right: 8px;
		margin-left: 0;
	}
  .nav {
    background-color: white;
    .Fheader {
      width: 98%;
      margin: 10px auto;
      overflow: hidden;
      .search {
        float: right;
        .box-search {
          width: 140px;
          float: right;
        }
        .box-btn {
          float: right;
          margin-left:5px;
          margin-right:10px;
        }
      }
    }
  }
}


</style>
