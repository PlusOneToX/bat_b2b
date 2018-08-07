<template>
    <div class="announcement-list">
      <header>
        <h4>首页板块</h4>
        <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="addPlateData">
          添加板块
        </el-button>
      </header>
      <div class="function" v-loading="loading">
        <el-table :data="tableData" header-row-class-name="header-row" border class="tr-header" max-height="550">
        <el-table-column align="center" label="序号" prop="sort" ></el-table-column>
        <el-table-column align="center" label="板块标题" prop="title"></el-table-column>
        <el-table-column align="center" label="创建时间" prop="createTime" :formatter="formatTime" ></el-table-column>
        <el-table-column align="center" label="发布状态" prop="releaseStatus" :formatter="formatStatus"></el-table-column>
        <el-table-column align="center" label="操作" :width="250" fixed="right">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="handleSee(scope.$index,scope.row)">查看</el-button>
            <el-button class="mini-tableadd-btn" @click="handleRelease(scope.row)" v-if="scope.row.releaseStatus === 2">发布</el-button>
            <el-button class="mini-freeze-btn" @click="handleRelease(scope.row)" v-else>取消发布</el-button>
            <el-button class="mini-delete-btn" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
        </el-table>
        <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
      </div>
    </div>
</template>

<script type="text/javascript">
import treeTable from "@/components/TreeTable";
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat.js";
import { sortBy } from '@/utils/common'
export default {
  name: "frontPagePlate",
  components: { treeTable, page },
  data() {
    return {
      loading: false,
      editDistributor: null,
      batch: "",
      pageInfo: {
        page: 1,
        size: 10
      },
      total: 0,
      tableData: [],
      parentId: 1,
      editId: "",
      mytext: "",
      scope: [],
      forEach: [],
      formData: {
        id: '',
        title: '',
        sectionOrder: '',
        releaseStatus: 0,
        createTime: '',
        updateTime: '',
      },
      // 销售区域编号
      territory: [],
      salesAreas :[],
    };
  },
  mounted() {
    this.dataFot();
  },
  activated() {
    this.dataFot();
  },
  methods: {
    // 发布状态值
		formatStatus(row,col,val){
			switch(val){
				case 1:
				return "已发布";
				break;
				case 0:
				return "未发布";
				break;
			}
		},
    formatTime(row, col, val) {
      return timeFormat(val);
    },
    sizeChange(size) {
      this.pageInfo.size = size;
      this.dataFot()
    },
    currentChange(page) {
      this.pageInfo.page = page;
      this.dataFot()
    },
    // 数据列表
    dataFot() {
      this.loading = true;
      // getSectionList(this,this.pageInfo).then(res => {
      this.$http.sectionList(this, this.pageInfo).then(res => {  
        let ary = [];
        res.data.list.forEach(item => {
            item.children = [];
            ary.push(item);
        });
        ary.sort((a, b) => {
          return a.sort - b.sort > 0;
        });
        ary.sort(sortBy('sectionOrder'))
        this.tableData = ary;
        this.total = res.data.total
        if(res.success) {
          this.loading = false
        }else {
          this.loading = false
        }
      })
    },
    
    handleSee(index,row) { // 查看操作
      this.$router.push({ name: 'editPlate',params:{id: row.id}})
    },
    
    handleDelete(row) { // 删除操作
      this.$confirm('删除此条板块?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
      }).then(() => {
        this.$http.delSection(this, {id: row.id}).then(res => {
          if (res.success) {
            this.dataFot();
          }
        });
      })
    },
    
    handleRelease(row) { // 发布操作
      let status = (row.releaseStatus === 1?2:1);
      this.$http.sectionStatus(this, {id: row.id,releaseStatus: status}).then(res => {  
        if(res.success) {
           if(status === 1){
              this.$message({
                message: '成功发布',
                type: 'success',
                duration: 3 * 1000,
                onClose: () => { }
              })
              this.loading = false
              this.dataFot();
            }else{
              this.$message({
                  message: '成功取消发布',
                  type: 'success',
                  duration: 3 * 1000,
                  onClose: () => { }
              })
              this.loading = false
              this.dataFot();
            }
        }
        
      })
    },
    addPlateData(){
      this.$router.push({ name: 'addPlate'})
    }
  },
  watch: {
     
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell{
  overflow: hidden; 
  white-space:nowrap; 
}
.announcement-list {
  background-color: white;
  height: 100%;
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
  .btn-home{
    float: right;
    padding: 5px;
    margin-top: 8px;
    margin-right: 8px;
    margin-left: 0;
    font-size: 12px;
  }
  // .header {
  //   background-color: $lakeBlue;
  //   line-height: 40px;
  //   color: white;
  //   padding-left: 20px;
  // }
  .tool-bar {
    padding: 20px;
  }
  .header-row {
    @include table-header-color;
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
  margin-right: 8px;
  font-size: 12px;
}

.el-table__row {
  text-align: center !important;
}
</style>
