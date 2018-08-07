<template>
  <div class="download-category-list">
    <header class="header">
      <h4 class="title">下载中心</h4>
      <el-button class="mini-add-btn btn-right" icon="el-icon-plus" @click="handleAdd">添加下载分类</el-button>
    </header>
     <!----下载中心分类数据列表----->
    <el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;margin-top:20px;" row-key="id" lazy 
    :tree-props="{children: 'childrens', hasChildren: 'hasChildren'}" 
    v-loading="loading">
      <el-table-column align="center" label="编号" prop="id" :width="120"></el-table-column>
      <el-table-column align="center" label="分类名称" show-overflow-tooltip>
        <template slot-scope="scope">
            <input class = "input-name" v-model='scope.row.titleZh' placeholder="分类名称" v-show="scope.row.isEdit"/>
            <span v-show="!scope.row.isEdit">{{scope.row.titleZh}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="分类英文名称" show-overflow-tooltip>
        <template slot-scope="scope">
            <input class = "input-name" v-model='scope.row.titleEn' placeholder="分类英文名称" v-show="scope.row.isEdit"/>
            <span v-show="!scope.row.isEdit">{{scope.row.titleEn}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="排序" :width="100">
        <template slot-scope="scope">
            <input class = "input-name" v-model='scope.row.sort' placeholder="排序" v-show="scope.row.isEdit"/>
            <span v-show="!scope.row.isEdit">{{scope.row.sort}}</span>
        </template>
      </el-table-column>
      <el-table-column  align="center" label="操作" :width="350">
        <template slot-scope="scope">
          <el-button class="mini-search-btn" @click="handleSave(scope.row)" style="background-color:#21b8cb" v-if="scope.row.isEdit">保存</el-button>
          <el-button class="mini-search-btn" @click="handleEdit(scope.row)" v-else >编辑</el-button>
          <el-button  size="mini" @click="handleCancel(scope.row)" v-if="scope.row.isEdit">取消</el-button>
          <el-button class="mini-delete-btn" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
  </div>
</template>

<script>
import page from "@/components/pagination"
export default {
  data() {
    return {
      loading: false,
      tableData: [],
      pageInfo: {
        page: 1,
				size: 10
      },
      total: 0
    }
  },
  components: {
		page
  },
  created() {
    this.getTableData()
  },
  methods: {
    // 获取下载中心分类列表
    getTableData() {
      this.loading = true
      this.$http.downloadList(this, {  
        page: this.pageInfo.page,
        size: this.pageInfo.size,
        parentId: 0
      }).then(res => {
        if (res.success) {
          this.loading = false
          let dataArr = res.data.list
          this.total = res.data.total
          if (dataArr.length > 0) {
            this.tableData = []
            let index = 0
            dataArr.forEach(item => {
              if (item.childrens.length > 0) {
                let childArr = []
                item.childrens.forEach(child => {
                  childArr.push({
                    id: child.id,
                    titleZh: child.titleZh,
                    titleEn: child.titleEn,
                    sort: child.sort,
                    isEdit: false
                  })
                })
                this.tableData.push({
                  id: item.id,
                  titleZh: item.titleZh,
                  titleEn: item.titleEn,
                  sort: item.sort,
                  childrens: childArr,
                  isEdit: false
                })
              } else {
                this.tableData.push({
                  id: item.id,
                  titleZh: item.titleZh,
                  titleEn: item.titleEn,
                  sort: item.sort,
                  childrens: [],
                  isEdit: false
                })
              }
            })
          } else{
            this.loading = false
            this.tableData = []
          }
        } else {
          this.loading = false
          this.$message.error(res.msg)
        }
      })
    },
    // 添加
    handleAdd() {
      this.$router.push({name : 'downCategoryAdd'})
    },
    // 编辑
    handleEdit(row) {
      let isFlag = false
      this.tableData.forEach(item => {
        if (item.isEdit) {
          isFlag = true
        }
        if(item.childrens.length > 0) {
          item.childrens.forEach(child => {
            if(child.isEdit) {
              isFlag = true
            }
          })
        }
      })
      if (isFlag) {
        this.$message.error('请先保存编辑中的数据！');
      } else {
        row.isEdit = true
      }
    },
    // 保存
    handleSave(row) {
      if (row.sort === '') {
        this.$message.error('请先输入排序号！');
      }else{
        const params = {
          id: row.id,
          titleZh: row.titleZh,
          titleEn: row.titleEn,
          sort: row.sort
        }
        this.$http.editDownload(this, params).then(res => {  
          if (res.success) {
            this.$message.success('更新成功')
            this.getTableData()
          }
        }).catch(err => {
          console.log(err)
        })
      }
    },
    // 删除
    handleDelete(id) {
      this.$confirm('确定删除此分类吗？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
        this.$http.delDownload(this, {id: id}).then(res => {    
					if(res.success){
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 3 * 1000,
						})
						this.getTableData()
					}
				})
			}).catch(err => {
        console.log(err);
      })
    },
     // 取消
    handleCancel(row) {
      row.isEdit = false
      this.getTableData()
    },
   // 更换页码数
    sizeChange(size){
			this.pageInfo.size = size
			this.pageInfo.page = 1
			this.getTableData()
    },
    // 更换页码
    currentChange(page){
			this.pageInfo.page = page
			this.getTableData()
		}
  }
}
</script>

<style lang="scss">
  .download-category-list{
    .input-name{
      height: 30px;
      font-size: 14px;
      border-radius: 5px;
      border: 1px solid #ccc;
      font-weight: bold;
      width: 100%;
    }
  }
</style>