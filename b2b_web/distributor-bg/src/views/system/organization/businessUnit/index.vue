<template>
    <div class="businessUnit-list">
        <div>
          <header>
            <h4>事业部列表</h4>
            <el-button class="btn-home" icon="el-icon-plus" @click="addPlateData">
              添加事业部
            </el-button>
          </header>
        </div>
        <div class="function">
          <el-table :data="tableData" header-row-class-name="header-row" border class="tr-header" max-height="550" v-loading="loading">
          <el-table-column align="center" label="事业部ID" prop="erpBusinessUnitId" ></el-table-column>
          <el-table-column align="center" label="事业部名称" prop="name"></el-table-column>
          <el-table-column align="center" label="所属销售组织" prop="organizationName"></el-table-column>
          <el-table-column align="center" label="事业部描述" prop="description"></el-table-column>
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
import { getBusinessUnitList,columnBusinessUnitData,deleteBusinessUnit } from '@/views/system/organization/organizationData'
export default {
  name: 'businessUnit',
  components: { page },
  data() {
    return {
      loading: false,
      pageInfo: {
        page: 1,
        count: 10,
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
    // ======== 操作 ========
    handleSee(index,row) { // 查看操作
      this.$router.push({ name: 'editBusinessUnit',params:{id:row.id}})
    },
    
    handleDelete(row) { // 删除操作
      this.$confirm('删除此条销售部门?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
      }).then(() => {
        deleteBusinessUnit(this, {id: row.id}).then(res => {
          this.dataFot();
        });
      })
    },

    addPlateData(){ // 添加操作
      this.$router.push({ name: 'addBusinessUnit'})
    },
    // ======== 数据 ========
    dataFot() { // 数据列表
      this.loading = true;
      getBusinessUnitList(this,this.pageInfo).then(res => {
          res.code == 0 ? this.loading = false : this.loading = false
          let ary = [];
          if(res.businessUnits != undefined) {
            res.businessUnits.forEach(item => {
              item.children = [];
              ary.push(item);
            });
            ary.sort((a, b) => {
              return a.sort - b.sort > 0;
            });
            this.tableData = ary;
          }
          
      })
      columnBusinessUnitData(this).then(res => { // 总列数
        this.total = res.count;
      })
    },
    
    handleSee(index,row) { // 查看操作
      this.$router.push({ name: 'editBusinessUnit',params:{id:row.id}})
    },
    sizeChange(size) {
      this.pageInfo.count = size;
      this.dataFot()
    },
    
    currentChange(page) {
      this.pageInfo.page = page;
      this.dataFot()
    },
    addPlateData(){
      this.$router.push({ name: 'addBusinessUnit'})
    }
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell{
  overflow: hidden; 
  white-space:nowrap; 
}
.businessUnit-list {
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
