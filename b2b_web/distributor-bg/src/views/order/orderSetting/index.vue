<template>
	<div class="category-list">
	  <header>
		<h4>订单类型</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="addpuls">
        添加订单类型
      </el-button>
	  </header>

	  <div class="function">
       <div class="search-box">
          <div class="top">
            <div class="search">
              <el-select
                size="mini"
                v-model="pageInfo.contentType"
                placeholder="类型"
                style="width: 140px"
                clearable
              >
                <el-option label="订单类型ID" value="2"></el-option>
                <el-option label="订单类型名称" value="1"></el-option>
              </el-select>
              <el-input
                placeholder="请输入搜索关键字"
                @change="contentChange"
                @keyup.enter.native="onSearch()"
                clearable
                v-model.trim="pageInfo.content"
                size="mini"
                class="box-search"
              ></el-input>
              <button
                class="mini-search-btn btn-box"
                @click.prevent="onSearch()"
              >
                搜索
              </button>
            </div>
          </div>
        </div>
		<el-row>
		  <el-table :data="tableData"  border header-row-class-name="header-row" class="tableCenter" v-loading="loading" :height="tableHeight">
			<el-table-column align="center" label="订单类型ID" prop="erpType" :min-width="120"></el-table-column>
			<el-table-column align="center" label="订单类型名称" prop="name" :min-width="120"></el-table-column>
      <el-table-column align="center" label="订单类型标识" prop="specialFlag" :min-width="120">
        <template slot-scope="scope">
           <span v-if="scope.row.specialFlag==1">普通</span>
           <span v-if="scope.row.specialFlag==2">mto</span>
           <span v-if="scope.row.specialFlag==3">现款</span>
           <span v-if="scope.row.specialFlag==4">定制</span>
           <span v-if="scope.row.specialFlag==5">直运</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="默认仓库ID" prop="warehouseId" :min-width="120"></el-table-column>
      
			<el-table-column align="center" label="订单类型描述" prop="desc" :min-width="120"></el-table-column>
			<el-table-column align="center" label="操作" :min-width="150">
			  <template slot-scope="scope">
				  <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
				  <el-button class="mini-delete-btn" v-if="scope.row.editable != 0" @click="handleDelete(scope.row)">删除</el-button>
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
export default {
  name: "orderSetting",
  components: { treeTable, page },
  data() {
    return {
      tableHeight: 600,
      loading: false,
      states: "0",
      mytext: "",
      total: 0,
      tableData: [],
      editedAccount: {},
      formData: {
        name: "",
        description: "",
        id: ""
      },
      pageInfo: {
        page: 1,
        size: 10,
        contentType:'',
        content:''
      },
      state: [
        { value: "0", label: "全部" },
        { value: "1", label: "启用" },
        { value: "2", label: "停用" }
      ],
      rules: {
        name: [{ required: true, message: "请输入销售名称", trigger: "blur" }]
			},
    };
  },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.82  -  100); // 计算表高度，固定表头
  },
  activated() {
    this.dataForamt();
  },
  methods: {
    // ======== 操作 ========
    onSearch() {
      // 搜索操作
      this.pageInfo.page = 1;
      this.dataForamt();
    },
     contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.onSearch();
      }
    },
    handleDelete(row) { // 删除操作
      this.$confirm('此操作将删除该订单类型，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        this.$http.deleteOrderType(this, {id: row.id}).then(res => { 
          if (res.success) {
            this.$message.success({
              message: "删除成功",
              duration: 3 * 1000,
              onClose: () => { }
            });
            this.dataForamt();
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    
    addpuls() { // 添加操作
      this.$router.push({ name: "orderSettingRedact" });
    },
    
    handleEdit(index, row) { // 查看操作
      this.editedAccount = JSON.parse(JSON.stringify(row));
      this.$router.push({ name: "orderSettingRedact", query: { row: this.editedAccount } });
    },

    // ======== 数据 ========
    dataForamt() { // 数据列表
      this.loading = true;
      this.$http.orderTypeList(this, this.pageInfo).then(res => {  
        this.tableData = res.data.list
        this.total = res.data.total
        res.success ? this.loading = false : this.looking = false
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
        this.dataForamt(1);
      } else if (this.states == 2) {
        this.dataForamt(2);
      } else {
        this.dataForamt();
      }
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
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
      padding: 0px 10px 10px 10px;
      overflow: hidden;
      .search-box {
        text-align: right;
        overflow: hidden;
        .top {
          display: flex;
          justify-content: space-between;
          align-items: center;
          .el-select{
            margin-bottom:10px;
          }
          .search {
            .box-search {
              width: 220px;
            }
            .btn-box {
              position: relative;
              top: -1px;
            }
          }
        }
      }
    }
}
</style>
