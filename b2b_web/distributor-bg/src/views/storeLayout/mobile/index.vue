<template>
  <div class="mobile-list">
    <header class="header">
      <h4 class="title">移动端首页</h4>
      <el-button class="mini-add-btn btn-right" icon="el-icon-plus" @click="handleAdd">添加</el-button>
    </header>
     <div v-loading="loading" class="table-wrapper">
      <div class="search">
        <el-select v-model="pageInfo.moduleType " placeholder="模块类型"  size="mini" @change="handleType" clearable style="width:150px">
          <el-option label="轮播图" value="1"></el-option>
          <el-option label="图片模块" value="2"></el-option>
          <el-option label="商品推广模块" value="3"></el-option>
          <el-option label="商品列表模块" value="4"></el-option>
        </el-select>
        <el-select v-model="pageInfo.status" placeholder="状态" size="mini" @change="handleStatus" clearable style="width:100px">
          <el-option label="显示" value="1"></el-option>
          <el-option label="隐藏" value="0"></el-option>
        </el-select>
      </div>
      <!----移动端首页数据列表----->
      <el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;"  >
        <el-table-column align="center" type="selection" width="60"></el-table-column>
        <el-table-column align="center" label="排序" prop="sort" :width="100">
        </el-table-column>
        <el-table-column align="center" label="模块类型" prop="moduleType" :formatter="formatType" :width="200" >
        </el-table-column>
        <el-table-column align="center" label="状态" prop="status" :formatter="formatStatus" :width="160">
        </el-table-column>
        <el-table-column align="center" label="图片">
          <template slot-scope="scope">
            <el-image 
						style="text-align: center;width: 60px; height: 60px;padding-right: 0px; margin-right:5px;"
						fit="contain"
            v-for="img in scope.row.list"
            :key="img.id"
						:src="img.imageUrl+'?x-oss-process=image/resize,h_60,l_60'" >
					</el-image>
          </template>
        </el-table-column>
        <el-table-column label="操作" :width="240" align="center">
          <template slot-scope="scope">
            <button class="mini-search-btn" @click="handleEdit(scope.row)">编辑</button>
            <el-button class="mini-delete-btn" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
     </div>
  </div>
</template>

<script>
import page from "@/components/pagination";
export default {
  data() {
    return {
      loading: false,
      tableData: [],
      pageInfo: {
        page: 1,
        size: 10,
        moduleType: '',
        status: ''
      },
      total: 0
    }
  },
  components: {
		page,
	},
  created() {
    this.initData()
  },
  methods: {
    initData () {
      this.$http.mobileList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
        }
      })
    },
    // 添加
    handleAdd() {
      this.$router.push({name : 'editMobile'})
    },
     // 编辑
    handleEdit(row) {
      this.$router.push({ name: 'editMobile', query: {id: row.id } })
    },
    // 删除
    handleDelete (index, row) {
      this.$confirm('此操作将永久删除数据，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(_ => {
          this.$http.delMobile(this, {id: row.id}).then(res => {
            if (res.success) {
              this.$message.success({
                message: "删除成功",
                duration: 3 * 1000,
              })
              this.initData();
            }
          })
        }).catch(_ => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    // 筛选模块类型
    handleType (val) {
      this.pageInfo.moduleType = val
      this.initData()
    },
    // 筛选状态
    handleStatus (val) {
      this.pageInfo.status = val
      this.initData()
    },
    sizeChange(size) {
      this.pageInfo.size = size;
      this.initData()
    },
    currentChange(page) {
      this.pageInfo.page = page;
      this.initData()
    },
    formatType(row,col,val){ // 模块类型
			switch(val){
				case 1:
				return "轮播图";
				break;
				case 2:
				return "图片模块";
				break;
        case 3:
				return "商品推广模块";
				break;
        case 4:
				return "商品列表模块";
				break;
			}
		},
    formatStatus(row,col,val){ // 发布状态
			switch(val){
				case 1:
				return "显示";
				break;
				case 0:
				return "隐藏";
				break;
			}
		}
  }
}
</script>

<style lang="scss">
   .mobile-list{
    .search{
      @extend .search
    }
  }
</style>