<template>
    <div class="saleDepartment-list">
        <div>
          <header>
            <h4>销售部门列表</h4>
            <el-button class="btn-home" icon="el-icon-plus" @click="addPlateData">
              添加销售部门
            </el-button>
          </header>
        </div>
        <div class="sale-header">
          <div class="sale-block">
            <el-input class="box-input" size="mini" @change="contentChange" @keyup.enter.native="filter()" placeholder="销售部门名称" v-model.trim="pageInfo.departmentName" clearable></el-input>
            <button class="mini-search-btn box-btn" size="mini" @click.prevent="filter()">搜索</button>
          </div>
        </div>
        <div class="function">
          <el-table :data="tableData" header-row-class-name="header-row" border class="tr-header" max-height="550" v-loading="loading">
          <el-table-column align="center" label="销售部门ID" prop="erpDepartmentId" ></el-table-column>
          <el-table-column align="center" label="销售部门名称" prop="departmentName"></el-table-column>
          <el-table-column align="center" label="所属销售组织" prop="organizationName"></el-table-column>
          <el-table-column align="center" label="销售部门描述" prop="description"></el-table-column>
          <el-table-column align="center" label="是否销售部门" :width="140" prop="saleType" :formatter="isSale"></el-table-column>
          <el-table-column align="center" label="操作" :width="200" fixed="right">
            <template slot-scope="scope">
              <el-button class="mini-search-btn" @click="handleSee(scope.$index,scope.row)">查看</el-button>
              <el-button class="mini-delete-btn" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
          </el-table>
          <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
        </div>
    </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
export default {
  name: 'saleDepartment',
  components: { page },
  data() {
    return {
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        departmentName: undefined
      },
      total: 0,
      tableData: [],
      parentId: 1,
      scope: [],
      forEach: [],
    };
  },
  mounted() {
    this.dataFot();
  },
  activated() {
    this.dataFot();
  },
  methods: {
    filter(){ // 搜索操作
      this.pageInfo.departmentName = this.pageInfo.departmentName.trim() === '' ? undefined : this.pageInfo.departmentName
      this.pageInfo.page = 1
      this.dataFot()
    },
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.filter()
      }
    },
    isSale(row, col, val) { // 活动范围
      switch (val) {
        case 1:
          return '是'
          break
        case 0:
          return '否'
          break
          break
      }
    },
    // ======== 操作 ========
    handleSee(index,row) { // 查看操作
      this.$router.push({ name: 'editAddDepartment',params:{rowData: row}})
    },
    
    handleDelete(row) { // 删除操作
      this.$confirm('删除此条销售部门?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
      }).then(() => {
        // deleteDepartment(this, {id: row.id}).then(res => {
        //   this.dataFot();
        // });
        this.$http.deleteDepartment(this, { id: row.id }).then(res => {
          if (res.success) {
            this.dataFot();
          }
        })
      })
    },

    addPlateData(){ // 添加操作
      this.$router.push({ name: 'addDepartment'})
    },
    // ======== 数据 ========
    dataFot() { // 数据列表
      this.loading = true;
      // getDepartmentList(this,this.pageInfo).then(res => {
      //     let ary = [];
      //     res.departments.forEach(item => {
      //         item.children = [];
      //         ary.push(item);
      //     });
      //     ary.sort((a, b) => {
      //       return a.sort - b.sort > 0;
      //     });
      //     this.tableData = ary;
      //     res.code == 0 ? this.loading = false : this.looking = false
      // })
      // columnDepData(this).then(res => { // 总列数
      //   this.total = res.count;
      // })
      this.$http.getDepartmentList(this, this.pageInfo).then(res => {
        if (res.success) {
          let ary = [];
          res.data.list.forEach(item => {
              item.children = [];
              ary.push(item);
          });
          ary.sort((a, b) => {
            return a.sort - b.sort > 0;
          });
          this.tableData = ary;
          this.total = res.data.total;
          this.loading = false
        } else {
          this.looking = false
        }
      })
    },
    
    handleSee(index,row) { // 查看操作
      this.$router.push({ name: 'editDepartment',params:{id:row.id}})
    },
    sizeChange(size) {
      this.pageInfo.size = size;
      this.dataFot()
    },
    
    currentChange(page) {
      this.pageInfo.page = page;
      this.dataFot()
    },
    addPlateData(){
      this.$router.push({ name: 'addDepartment'})
    }
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell{
  overflow: hidden; 
  white-space:nowrap; 
}
.saleDepartment-list {
  background-color: white;
  height: 100%;
  header {
    color: white;
    padding-right: 10px;
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
  .sale-header {
    width:100%;
    overflow: hidden;
    .sale-block {
      float: right;
      margin: 10px 10px;
      .box-input {
        width:200px;
      }
      .box-btn {
        position: relative;
        top: -1px;
      }
    }
  } 
}
.tr-header {
  text-align: center;
}
.function {
  padding: 16px 0;
  background-color: white;
  .btn-export {
    background-color: lighten(grey, 40%);
  }
  .search {
    float: right;
    margin-bottom: 10px;
  }
  .box-search {
    width: 140px;
  }
  .btn-search {
    background-color: $lakeBlue;
    color: white;
  }
  .Fheader {
    width: 96%;
    margin: 10px auto;
  }
}
.choose-stock {
  width: 100%;
  padding: 10px;
  padding-left: 20px;
}
.btn-add {
  float: right;
  padding: 5px;
  margin-top: 7px;
  font-size: 12px;
}

.el-table__row {
  text-align: center !important;
}
.btn-home{
	float: right;
	padding: 5px;
	margin-top: 8px;
	margin-left: 0;
	font-size: 12px;
  }
</style>
