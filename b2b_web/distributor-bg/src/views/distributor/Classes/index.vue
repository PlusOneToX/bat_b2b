<template>
	<div class="category-list">
		<header>
		  <h4>分销商类别</h4>
		  <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="addpuls()"> 添加分销商类别 </el-button>
		</header>

		<div class="function">
      <div class="Fheader">
          <el-select class="choose" v-model="states" placeholder="状态" size="mini" style="width: 100px;" clearable>
            <el-option v-for="item in state" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
      </div>
			<el-row>
			  <el-table :data="tableData" border header-row-class-name="header-row" class="tableCenter" v-loading="loading">
				<el-table-column align="center" label="类别ID" prop="erpCategoryNo" :min-width="120"></el-table-column>
				<el-table-column align="center" label="类别名称" prop="name" :min-width="120"></el-table-column>
				<el-table-column align="center" label="默认订单类型" prop="orderTypeId" :min-width="120"></el-table-column>
        <el-table-column align="center" label="状态" prop="openFlag" :formatter="forstatus" :min-width="120"></el-table-column>
				<el-table-column align="center" label="类别描述" prop="description" :min-width="120"></el-table-column>
				<el-table-column align="center" label="操作" :min-width="150">
				  <template slot-scope="scope">
					  <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
            <el-button class="mini-tableadd-btn" @click="handleStatus(scope.row, 1)" v-if="scope.row.openFlag===0">启用</el-button>
            <el-button class="mini-freeze-btn" @click="handleStatus(scope.row, 0)" v-else>停用</el-button>
					  <el-button n class="mini-delete-btn" @click="handleDelete(scope.row)" v-if="scope.row.openFlag===0">删除</el-button>
					</template>
				</el-table-column>
			  </el-table>
			  <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
			</el-row>
		</div>

	</div>
</template>

<script type="text/javascript">
import treeTable from "@/components/TreeTable";
import page from "@/components/pagination";
import {postredactData,getdatalistData,getnumberData,deleteData, putstopData, putstarData } from "@/views/distributor/distributorData";
export default {
  name: "distributorClasses",
  components: { treeTable, page },
  data() {
    return {
      loading: false,
      states: "",
      pageInfo: {
        page: 1,
        size: 10,
        openFlag: 1
      },
      total: 0,
      tableData: [],
      state: [
        { value: "1",label: "启用" },
        { value: "0",label: "停用" }
      ],
      mytext: "",
      formData: {
        erpCategoryId: "",
        categoryName: "",
        categoryDescribe: ""
      },
      rules: {
        name: [{ required: true, message: "请输入销售名称", trigger: "blur" }]
      },
    };
  },
  activated() {
    this.dataForamt();
  },
  methods: {
    // ======== 操作 ========
    handleDelete(row) { // 删除按钮
      this.$confirm("此操作将永久删除该分销商类别，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true
      }).then(_ => {
        this.$http.deleteDistCategroy(this, { id: row.id }).then(res => {
          if (res.success) {
            this.$message.success({
              message: "删除成功",
              duration: 3 * 1000,
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
      this.$router.push({name: 'distributorClassesRedact',query: {id: row.id}})
    },

    addpuls() { // 添加分销商类别
      this.$router.push({ name: 'distributorClassesRedact'})
    },
    handleStatus(row, status) {
      let msg = ''
      if (status === 1) {
        msg = '此操作将启用该分销商类别，是否继续？'
      } else {
        msg = '此操作将停用该分销商类别，是否继续？'
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true
      }).then(_ => {
        this.$http.distCategoryStatus(this, { id: row.id, openFlag: status }).then(res => {
          if (res.success) {
            this.$message.success({
              message: "更新成功",
              duration: 3 * 1000,
            });
            this.dataForamt();
          }
        })
      }).catch(_ => {
        this.$message({
          type: "info",
          message: "已取消"
        });
      });
    },
    forstatus(row) { // 申请状态
      switch(row.openFlag) {
        case 0:
          return '停用';
        case 1:
          return '启用';
      }
    },

    // ======== 数据 ========
    dataForamt() { // 详情
      this.loading = true;
      this.pageInfo.openFlag = this.states
      this.$http.getDistCategoryList(this, this.pageInfo).then(res => {
        if (res.success) {
          this. tableData = res.data.list
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
    }    
  },
  watch: {
    states: function() { // 启用停用
      if (this.states == 1) {
        this.pageInfo.page = 1
        this.dataForamt();
      } else if (this.states == 0) {
        this.pageInfo.page = 1
        this.dataForamt();
      } else {
        this.pageInfo.page = 1
        this.dataForamt();
      }
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" >
.el-table .cell {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis
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
