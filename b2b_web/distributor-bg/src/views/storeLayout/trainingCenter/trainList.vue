<template>
  <div class="train-list">
    <header class="header">
      <h4 class="title">培训中心</h4>
      <el-button class="mini-add-btn btn-right" icon="el-icon-plus" @click="handleAdd">添加</el-button>
    </header>
    <div v-loading="loading" class="table-wrapper">
      <div class="search">
        <el-select v-model="selectFid" placeholder="一级菜单"  size="mini" @change="handleSelectFirst">
          <el-option
          v-show="item.titleZh || item.titleEn"
          v-for="item in firstMenu"
          :key="item.id"
          :label="item.titleZh ? item.titleZh: item.titleEn"
          :value="item.id">
          </el-option>
        </el-select>
        <el-select v-model="selectSid" placeholder="二级菜单" size="mini" @change="handleSelectSecond">
          <el-option
          v-show="item.titleZh || item.titleEn"
          v-for="item in secondMenu"
          :key="item.id"
          :label="item.titleZh ? item.titleZh: item.titleEn"
          :value="item.id">
          </el-option>
        </el-select>
      </div>
      <!----培训中心数据列表----->
      <el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;"  >
        <el-table-column align="center" label="ID" prop="id" width="60"></el-table-column>
        <el-table-column align="center" label="一级菜单名称" prop="fid" :formatter="formatFName" show-overflow-tooltip>
        </el-table-column>
        <el-table-column align="center" label="二级菜单名称" prop="parentId" :formatter="formatSName" show-overflow-tooltip>
        </el-table-column>
        <el-table-column align="center" label="中文标题" prop="titleZh">
        </el-table-column>
        <el-table-column align="center" label="英文标题" prop="titleEn">
        </el-table-column>
        <el-table-column align="center" label="国内链接" prop="contentUrlZh" show-overflow-tooltip>
        </el-table-column>
        <el-table-column align="center" label="国外链接" prop="contentUrlEn" show-overflow-tooltip>
        </el-table-column>
        <el-table-column align="center" label="排序" prop="sort" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="操作" :width="350" align="center">
          <template slot-scope="scope">
            <el-button class="mini-tableadd-btn" @click="handleUp(scope.$index,scope.row)">上移</el-button>
            <el-button class="mini-freeze-btn" @click="handleDown(scope.$index,scope.row)">下移</el-button>
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
import page from "@/components/pagination"
import { swapItem } from "@/utils/index"
export default {
  data() {
    return {
      loading: false,
      tableData: [],
      firstMenu: [],
      secondMenu: [],
      selectFid: null,
      selectSid: null,
      pageInfo: {
        page: 1,
				size: 10
      },
      total: 0
    }
  },
  components: {
		page,
	},
  mounted() {
    // 获取数据列表
    this.getFMenu(0)
  },
  methods: {
    // 获取数据列表
    getTableData(id) {
      this.$http.trainingList(this, {   
        page: this.pageInfo.page,
        size: this.pageInfo.size,
        parentId: id
      }).then(res => {
        if (res.success) {
          this.loading = false;
          let dataArr = res.data.list
          this.total = res.data.total
          if (dataArr.length > 0) {
            this.tableData = []
            dataArr.forEach(item => {
              this.tableData.push({
                id: item.id,
                fid: this.selectFid,
                parentId: item.parentId,
                titleZh: item.titleZh,
                titleEn: item.titleEn,
                contentUrlZh: item.contentUrlZh,
                contentUrlEn: item.contentUrlEn,
                sort: item.sort,
                isEdit: false
              })
            })
          } else if ((dataArr === undefined || dataArr === null || dataArr.length === 0) && this.pageInfo.page > 1) {
            this.pageInfo.page = this.pageInfo.page - 1
            this.getTableData(id)
          } else {
            this.loading = false
            this.tableData = []
          }
        } else {
          this.loading = false
        }
      })
    },
    // 获取一级菜单列表
    getFMenu(id) {
      this.loading = true;
      this.$http.trainingPoList(this, {  
        parentId: id
      }).then(res => {
        if (res.success) {
          this.loading = false;
          this.firstMenu = res.data
          if(this.firstMenu.length>0) {
            let sid = this.firstMenu[0].id
            this.selectFid = sid
            this.getSMenu(sid)
          } else {
            this.selectFid = null
          }
        } else {
          this.loading = false;
          this.$message.error(res.msg)
        }
      })
    },
    // 获取二级菜单列表
    getSMenu(id) {
      this.$http.trainingPoList(this, {  
        parentId: id
      }).then(res => {
        if (res.success) {
          let secondArr = res.data
          if (secondArr === undefined || secondArr === null || secondArr.length === 0) {
            this.loading = false
            this.selectSid = null
            this.secondMenu = []
            this.tableData = []
            this.$message.warning('暂无数据')
          } else {
            this.secondMenu = secondArr
            this.selectSid = secondArr[0].id
            this.getTableData(this.selectSid)
          }
        } else {
          this.loading = false
          this.$message.error(res.msg)
        }
      })
    },
    // 一级菜单名
    formatFName(row, col, val){
      let name
      try {
        this.firstMenu.forEach(item => {
          if (item.id === val) {
            name = item.titleZh
            // throw Error('')
            return
          }
        })
      } catch (error) {
        // console.log(error)
      }
      return name
    },
    // 二级菜单名
    formatSName(row, col, val) {
       let name
      try {
        this.secondMenu.forEach(item => {
          if (item.id === val) {
            name = item.titleZh
            // throw Error('err')
            return
          }
        })
      } catch (error) {
        console.log(error)
      }
      return name
    },
    handleSelectFirst(val) {
      this.loading = true
      this.getSMenu(val)
    },
    handleSelectSecond(val) {
      this.getTableData(val)
    },
    // 添加
    handleAdd() {
      this.$router.push({name : 'trainAdd'})
    },
    // 上移
    handleUp(index, row) {
      this.$http.trainingUp(this, {id: row.id}).then(res => {
        if(res.success) {
          this.$message.success('上移成功')
          this.getTableData(this.selectSid)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    // 下移
    handleDown(index, row) {
      this.$http.trainingDown(this, {id: row.id}).then(res => {  
        if(res.success) {
          this.$message.success('下移成功')
          this.getTableData(this.selectSid)
        }
      })
    },
    // 编辑
    handleEdit(row) {
      this.$router.push({ name: 'trainEdit', params: {id: row.id, fid: this.selectFid} })
    },
    handleCancel(index){
      this.tableData[index].isEdit = false;
      if(this.tableData[index].id == undefined){
        this.tableData.splice(index,1);
      }else{
        this.getTableData(this.selectSid);
      }
    },
    handleDelete(index, row) {
      this.$confirm('确定删除此数据吗？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
        this.$http.delTraining(this, {id: row.id}).then(res => {    
					if(res.success){
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 3 * 1000,
						})
						this.getTableData(this.selectSid)
					}
				})
			}).catch(err => {
        console.log(err);
      })
    },
    sizeChange(size) {
			this.pageInfo.page = 1
			this.pageInfo.size = size;
			this.getTableData(this.selectSid)
		},
    currentChange(page) {
			this.pageInfo.page = page;
			this.getTableData(this.selectSid)
		}
  }
}
</script>

<style lang="scss">
  .train-list{
    .search{
      @extend .search
    }
    .input-url{
      height: 30px;
      font-size: 14px;
      border-radius: 5px;
      border: 1px solid #ccc;
      font-weight: bold;
      width: 100%;
    }
  }
</style>