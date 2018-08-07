<template>
	<div class="category-list">
		<header>
		  <h4>分销商分组列表</h4>
		  <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="addpuls">
				添加分销商分组
		  </el-button>
		</header>

		<div class="function">
      <div class="Fheader">
          <el-select class="choose" v-model="states" placeholder="状态" size="mini" style="width: 100px;" clearable>
            <el-option v-for="item in state" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
      </div>
			<el-row>
			  <el-table :data="tableData" border header-row-class-name="header-row" class="tableCenter" v-loading="loading" :height="tableHeight">
				<el-table-column align="center" label="分销商分组ID" prop="erpGroupNo" :min-width="120"></el-table-column>
				<el-table-column align="center" label="分销商分组名称" prop="name" :min-width="120"></el-table-column>
				<el-table-column align="center" label="分销商分组描述" prop="description" :min-width="120"></el-table-column>
        <el-table-column align="center" label="状态" prop="openFlag" :formatter="forstatus" :min-width="120"></el-table-column>
				<el-table-column align="center" label="操作" :min-width="150">
				  <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
					  <el-button class="mini-tableadd-btn" @click="handleStop(scope.row)" v-if="scope.row.openFlag===1">停用</el-button>
					  <el-button class="mini-freeze-btn" @click="handleStart(scope.row)" v-else>启用</el-button>
					  <el-button class="mini-delete-btn" @click="handleDelete(scope.row)" v-if="scope.row.openFlag===0">删除</el-button>
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
  import { postredactData, getdatalistData, getnumberData, deleteData, putstopData, putstarData } from "@/views/distributor/distributorData";
  export default {
    name: "distributorGrouping",

    components: { page },

    data() {
      return {
        tableHeight: 600,
        loading: false,
        addBtn: true,
        states: "",
        pageInfo: {
          page: 1,
          size: 10,
          openFlag: 1
        },
        total: 0,
        tableData: [],
        state: [
          { value: "1", label: "启用" },
          { value: "0", label: "停用" }
        ],
        mytext: "",
        formData: {
          name: "",
          description: "",
          id: ""
        },
        rules: {
          name: [{ required: true, message: "请输入销售名称", trigger: "blur" }]
        },
        editedAccount: {}
      };
    },
    created() {
      const documentHeight = document.body.clientHeight;
      this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
    },
    activated() {
      this.dataForamt();
    },
    methods: {
      // ======== 操作 ========
      handleDelete(row) { // 删除操作
        this.$confirm("此操作将永久删除该分销商分组，是否继续？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true
        }).then(_ => {
          this.$http.deleteDistGroup(this, {id: row.id}).then(res => {  
            if (res.success) {
                this.$message.success({
                  message: "删除成功",
                  duration: 3 * 1000,
                  onClose: () => {
                  }
                });
                this.dataForamt();
              }
          })
        }).catch(_ => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
      },
      
      handleEdit(index, row) { // 查看操作
        this.$router.push({name: 'distributorGroupingRedact',query: {id: row.id}})
      },
      
      addpuls() { // 添加分销商分组
        this.$router.push({ name: "distributorGroupingRedact" });
      },
      
      handleStop(row) { // 停用操作
        this.$confirm("此操作停用该分销商分组，是否继续？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true
        }).then(_ => {
            this.$http.distGroupStatus(this, { id: row.id, openFlag: 0 }).then(res => {
              if (res.success) {
                this.$message.success({
                  message: "停用成功",
                  duration: 3 * 1000,
                  onClose: () => {
                  }
                });
                this.dataForamt();
              }
            });
          }).catch(_ => {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      },
      
      handleStart(row) { // 启用操作
        this.$confirm("此操作将启用该分销商分组，是否继续？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          center: true
        }).then(_ => {
          this.$http.distGroupStatus(this, { id: row.id, openFlag: 1 }).then(res => {
            if (res.success) {
                this.$message.success({
                  message: "启用成功",
                  duration: 3 * 1000,
                  onClose: () => {
                  }
                });
                this.dataForamt();
              }
          });
        }).catch(_ => {
          this.$message({
            type: "info",
            message: "已取消启用"
          });
        });
      },

      dataForamt() { // 数据
        this.loading = true;
        this.pageInfo.openFlag = this.states
        this.$http.getDistGroupList(this, this.pageInfo).then(res => {
          if (res.success) {
            this.tableData = res.data.list
            this.total = res.data.total
            this.loading = false
          } else {
            this.looking = false
          }
        })
      },

      sizeChange(size) { // 分页页数
        this.pageInfo.size = size;
        this.dataForamt();
      },
      
      currentChange(page) { // 分页总数
        this.pageInfo.page = page;
        this.dataForamt();
      },
      forstatus(row) { // 申请状态
        switch(row.openFlag) {
          case 0:
            return '停用';
          case 1:
            return '启用';
        }
      },
    },
    watch: {
      states: function(val) { // 启用停用
        this.states = val
        this.pageInfo.page = 1
        this.dataForamt()
      }
    }
  };
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .category-list {
    background-color: white;
    height: 90vh;
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
      .btn-home {
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
        width: 98%;
        margin: 10px auto;
        display: flex !important;
        justify-content: space-between !important;
        align-items: center !important;
        overflow: hidden;
        .search {
          float: right;
          .box-search {
            width: 140px;
          }
          .box-btn {
            position: relative;
            top: -1px;
          }
        }
      }
    }
  }
</style>
