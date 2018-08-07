<template>
  <div class="material-list">
    <header>
      <h4>图库分类列表</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add">
        添加图库分类
      </el-button>
    </header>
    <!-- <div class="search">
			<div class="search-right">
				<el-input v-model.trim="pageInfo.content" clearable @change="changeContent" placeholder="分类名称" size="mini" class="box-search" ></el-input>
				<button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
			</div>
			<el-select v-model="pageInfo.status" placeholder="分类状态" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in statuss"
				:key="item.status"
				:label="item.label"
				:value="item.status">
				</el-option>
			</el-select>
		</div> -->

    <el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;" row-key="id" lazy
       :tree-props="{children: 'childrenList', hasChildren: 'hasChildren'}" v-loading="loading">
      <el-table-column align="center" label="编号" prop="id" :width="80"></el-table-column>
      <el-table-column align="center" label="图片类型" prop="type" :formatter="formatType" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="分类名称" prop="name" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="分类英文名称" prop="englishName" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="分类图片" :width="160">
        <template slot-scope="scope" style="text-align: center; width: 60px; height: 60px;">
          <el-image v-if="scope.row.image" style="text-align: center;width: 60px; height: 60px;padding-right: 0px;"
            :src="scope.row.image" fit="contain" :preview-src-list="[scope.row.image]">
          </el-image>
          <div v-else style="text-align: center;line-height: 60px;padding-right: 0px;">-</div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" prop="openFlag" :width="100" :formatter="formatStatus"
        show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="操作" :width="450">
        <template slot-scope="scope">
          <el-button class="mini-browse-btn" @click="handlePictrue(scope.$index, scope.row)">查看图片</el-button>
          <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
          <el-button class="mini-tableadd-btn" @click="handleMove(scope.row, true)">上移</el-button>
          <el-button class="mini-freeze-btn" @click="handleMove(scope.row, false)">下移</el-button>
          <el-button class="mini-tableadd-btn" v-if="scope.row.openFlag === 0" @click="handleChange(scope.row,1)">启用
          </el-button>
          <el-button class="mini-freeze-btn" v-else @click="handleChange(scope.row,0)">停用</el-button>
          <el-button class="mini-delete-btn" v-if="scope.row.openFlag === 0"
            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    <!-- 查看图片dialog -->
    <el-dialog :modal-append-to-body="false" :visible="pictureListShow" :before-close="closePictureListShow">
      <el-table :data="pictureList" max-height="600">
        <el-table-column align="center" label="图片名称" prop="name"></el-table-column>
        <el-table-column align="center" label="图片展示">
          <template slot-scope="scope" style="text-align: center;">
            <el-image style="width: 60px; height: 60px" fit="contain"
              :src="scope.row.originImage+'?x-oss-process=image/resize,h_60,l_60'"
              :preview-src-list="[scope.row.originImage]">
            </el-image>
          </template>
        </el-table-column>
      </el-table>
      <page :total="atotal" @sizeChange="handleSizeChange" @currentChange="handleCurrentChange"></page>
    </el-dialog>
  </div>
</template>
<script>
  import page from "@/components/pagination"
  export default {
    data() {
      return {
        tableHeight: 600,
        loading: false,
        tableData: [],
        picturePageInfo: {
          page: 1,
          size: 10,
          categoryId: undefined
        },
        pageInfo: {
          page: 1,
          size: 10,
          status: undefined,
          content: undefined,
        },
        atotal: 0,
        total: '',
        statuss: [{
          status: 1,
          label: '启用'
        }, {
          status: 0,
          label: '停用'
        }],
        parentId: 0,
        pictureList: [],
        pictureListShow: false
      }
    },
    created() {
      const documentHeight = document.body.clientHeight;
      this.tableHeight = parseInt(documentHeight * 0.8 - 100); // 计算表高度，固定表头
      this.getTableData()
    },
    components: {
      page
    },
    methods: {
      closePictureListShow() {
        this.pictureListShow = false
        this.pictureList = []
      },
      changeContent(val) {
        if (val === undefined || val === '' || val === null) {
          this.getTableData()
        }
      },
      onSearch() { // 搜索操作
        this.pageInfo.page = 1;
        this.getTableData()
      },
      add() {
        this.$router.push({
          name: 'pictureCategoryAdd'
        })
      },
      filter() {
        this.getTableData();
      },
      sizeChange(size) {
        this.pageInfo.page = 1
        this.pageInfo.size = size;
        this.getTableData()
      },
      handleSizeChange(size) {
        this.picturePageInfo.size = size
        this.picturePageInfo.page = 1;
        this.getPictrues()
      },
      currentChange(page) {
        this.pageInfo.page = page;
        this.getTableData()
      },
      handleCurrentChange(page) {
        this.picturePageInfo.page = page;
        this.getPictrues()
      },
      getTableData() {
        this.loading = true;
        this.$http.pictureCategoryList(this, this.pageInfo).then(res => {
          if (res.success) {
            this.tableData = res.data.list
            this.total = res.data.total
          }
          this.loading = false
        })
      },
      formatStatus(row, col, val) {
        switch (val) {
          case 1:
            return '启用';
            break
          case 0:
            return '停用';
            break
        }
      },
      formatCategory(row, col, val) {
        switch (val) {
          case 10010:
            return '手机壳';
            break
          case 10020:
            return '手机背膜';
            break
        }
      },
      formatType(row, col, val) {
        switch (val) {
          case 1:
            return '普通素材';
            break
          case 2:
            return 'IP素材';
            break
          case 3:
            return '模板';
            break
          case 4:
            return '贴图';
            break
        }
      },
      handlePictrue(index, row) {
        this.pictureListShow = true
        this.picturePageInfo.categoryId = row.id
        this.getPictrues()
      },
      getPictrues() {
        this.$http.pictureList(this, this.picturePageInfo).then(res => {  
          if (res.success) {
            this.pictureList = res.data.list
            this.atotal = res.data.total
          }
        })
      },
      handleEdit(index, row) {
        this.$router.push({
          name: 'pictureCategoryEdit',
          params: {
            id: row.id
          }
        })
      },
      handleDelete(index, row) {
        this.$confirm('确定删除此分类吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.$http.delPictureCategory(this, {id: row.id}).then(res => {    
            if (res.success) {
              this.$message({
                message: '删除成功',
                type: 'success',
                duration: 3 * 1000,
              })
              this.removeData(this.tableData, row)
            }
          })
        })
      },
      removeData(data, row) {
        for (let i = 0; i < data.length; i++) {
          if (data[i].id === row.id) {
            data.splice(i, 1)
            if (this.tableData.length === 0 && this.pageInfo.page > 1) {
              this.pageInfo.page = this.pageInfo.page - 1
              this.getTableData()
            }
            break
          }
          if (data[i].children !== undefined && data[i].children.length > 0 && data[i].children.length > 0) {
            this.removeData(data[i].children, row)
          }
        }
      },
      handleChange(row, status) {
        let msg = ''
        if (status === 1) {
          msg = '确定启用此分类吗？'
        } else if (status === 0) {
          msg = '确定停用此分类吗？'
        }
        this.$confirm(msg, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          if (status === 1) {
            this.$http.pictureCategoryStatus(this, {id: row.id, openFlag: 1}).then(res => {  
              if (res.success) {
                this.getTableData()
              }
            })
          } else if (status === 0) {
            this.$http.pictureCategoryStatus(this, {id: row.id, openFlag: status}).then(res => {    
              if (res.success) {
                this.getTableData()
              }
            })
          }
        })
      },

      // 上移/下移
      handleMove(row, status) {
        this.$http.pictureCategorySort(this, {
          id: row.id,
          flag: status
        }).then(res => {
          if (res.success) {
            this.$message({
              message: status ? '上移成功' : '下移成功',
              type: 'success',
            });
            this.getTableData();
          }
        })
      }
    },
    watch: {
      'pageInfo.category': {
        handler() {
          this.pageInfo.page = 1
          this.getTableData()
        },
        deep: true
      },
      category(val) {
        this.getTableData()
      }
    }
  }

</script>
<style lang="scss" scoped>
  .material-list {
    background-color: white;
    min-height: 100%;

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

    .btn-home {
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }

    .action {
      margin: 20px 0;
    }

    .search {
      width: 70%;
    }

    header {
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      // position: absolute;
      // bottom: auto;
      // top: expression(eval(document.documentElement.scrollTop));
    }

    h4 {
      display: inline-block;
      font-weight: 350;
      font-size: 16px;
      margin: 0 20px;
    }

    .btn-home {
      float: right;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
    }

    .search {
      width: 100%;
      border-bottom: 1px solid #dcdcdc;
      padding: 10px;
      overflow: hidden;

      .search-right {
        float: right;
        overflow: hidden;

        .box-search {
          width: 215px;
          // float: right;
        }

        .box-btn {
          float: right;
          margin-left: 5px;
          margin-right: 10px;
        }
      }
    }
  }

</style>
