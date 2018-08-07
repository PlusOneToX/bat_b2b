<template>
    <div class="category-list">
      <header>
        <h4>价格等级列表</h4>
        <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="addpuls">
          添加价格等级
        </el-button>
      </header>
      <div class="function">
        <div class="Fheader">
          <el-select class="choose" v-model="pageInfo.openFlag" placeholder="等级状态" size="mini" clearable>
            <el-option v-for="item in state" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <el-row>
          <el-table :data="tableData"  border header-row-class-name="header-row" class="tableCenter" v-loading="loading" :height="tableHeight">
            <el-table-column type="index" fixed :min-width="50"> </el-table-column>
            <el-table-column align="center" label="价格等级名称" prop="name" :min-width="170"></el-table-column>
            <el-table-column align="center" label="取价序号" prop="sort" :min-width="100"></el-table-column>
            <el-table-column align="center" label="价格等级描述" prop="description" :min-width="230"></el-table-column>
            <el-table-column align="center" label="是否建议零售价" prop="retailFlag" :min-width="230">
                <template slot-scope="scope">
                {{scope.row.retailFlag == 1?'是':'否'}}
              </template>
            </el-table-column>
            <el-table-column align="center" label="等级状态" :min-width="100">
              <template slot-scope="scope">
                {{scope.row.openFlag == 1?'启用':'停用'}}
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" :min-width="150">
              <template slot-scope="scope">
                <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
                <el-button class="mini-freeze-btn" @click="handleStop(scope.row)" v-if="scope.row.openFlag == 1">停用</el-button>
                <el-button class="mini-tableadd-btn" @click="handleStart(scope.row)" v-else>启用</el-button>
                <el-button class="mini-delete-btn" @click="handleDelete(scope.row)" v-if="scope.row.openFlag == 0">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
        </el-row>
      </div>

    </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import {gradeEdit, gradeData, gradeNumberData,gradeDelete,gradeStop,gradeStar} from "@/views/distributor/distributorData";
  export default {
    name: "distributorarea",
    components: { page },
    created() {
      const documentHeight = document.body.clientHeight;
      this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
    },
    activated() {
      this.dataForm();
    },
    data() {
      return {
        tableHeight: 600,
        loading: false,
        pageInfo: {
          page: 1,
          size: 10,
          content: '',
          openFlag: undefined
        },
        total: 0,
        tableData: [],
        mytext: "",
        formData: {
          name: "",
          description: "",
          id: ""
        },
        state: [
          // { value: '', label: '全部状态' },
          { value: '1', label: '启用' },
          { value: '0', label: '停用'}
        ],
        rules: {
          name: [{ required: true, message: "请输入销售名称", trigger: "blur" }]
        },
        editedAccount: {}
      };
    },
  methods: {
    // ======== 操作 ========
    addpuls() { // 添加分销商等级
      this.$router.push({name: 'distributorareaRedact'})
    },

    handleEdit(index, row) { // 查看操作
      this.$router.push({name: 'distributorareaRedact',query: {id: row.id}})
    },
    handleStop(row) { // 停用操作
      this.$confirm('此操作将停用该价格等级列表，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        this.$http.gradeStatus(this, { id: row.id, openFlag: 0 }).then(res => {
          if (res.success) {
             this.$message.success({
              message: "停用成功",
              duration: 3 * 1000,
              onClose: () => { }
            });
            this.dataForm();
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消停用'
        })
      })
    },

    handleStart(row) { // 启用操作
      this.$http.gradeStatus(this, { id: row.id, openFlag: 1 }).then(res => {
        if (res.success) {
          this.$message.success({
            message: "启用成功",
            duration: 3 * 1000,
            onClose: () => { }
          });
        } else {
          this.$message.success({
            message: "启用失败，请重新停用",
            duration: 3 * 1000,
            onClose: () => { }
          });
        }
        this.dataForm();
      })
    },

    handleDelete(row) { // 删除操作
      this.$confirm('此操作将永久删除该分销商价格等级，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        // gradeDelete(this, { id: row.id }).then(res => {
        //   if (res.code == 0) {
        //     this.$message.success({
        //       message: "删除成功",
        //       duration: 3 * 1000,
        //       onClose: () => {
        //       }
        //     });
        //     this.dataForm();
        //   }
        // });
        this.$http.deleteGrade(this, { id: row.id }).then(res => {
          if (res.success) {
            this.$message.success({
              message: "删除成功",
              duration: 3 * 1000,
              onClose: () => {
              }
            });
            this.dataForm();
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },

    // ======== 数据 ========
    dataForm() { // 详情
      this.loading = true;
      this.$http.getGradeList(this, this.pageInfo).then(res => {
        if(res.success) {
          let ary = []
          res.data.list.forEach(item => {
            item.children = []
            ary.push(item)
          })
          ary.sort((a, b) => {
            return a.sort - b.sort > 0
          })
          this.tableData = ary
          this.total = res.data.total
          this.loading = false
        }
      })
    },

    sizeChange(size) { // 分页页数
      this.pageInfo.count = size;
      this.dataForm();
    },
    
    currentChange(page) { // 分页总数
      this.pageInfo.page = page;
      this.dataForm();
    }    
  },
  watch: {
    'pageInfo.openFlag': function(val) {
      this.pageInfo.openFlag = val
      this.pageInfo.page = 1
      this.dataForm()
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .main[data-v-6beed8bc] {
    background-color: #fff;
  }
  .el-table .cell {
    overflow: hidden;
    white-space:nowrap;
  }
  .category-list {
    background-color: white;
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
    .function {
      background-color: white;
      .Fheader {
        width: 99%;
        margin: 10px auto;
        margin-left: 20px;
      }
    }
  }
</style>
