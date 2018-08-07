<template>
  <div class="ulog-list">
        <div>
          <header>
            <h4>系统操作日志</h4>
          </header>
        </div>
        <div class="log-header">
          <div class="log-left">
              <el-select
                size="mini"
                v-model="pageInfo.operateDes"
                placeholder="操作结果"
                style="width: 100px"
                clearable
              >
                <el-option label="成功" value="成功"></el-option>
                <el-option label="失败" value="失败"></el-option>
              </el-select>
             <el-date-picker
                size="mini"
                v-model="pageInfo.time"
                style="width: 330px"
                type="datetimerange"
                value-format="timestamp"
                range-separator="至"
                start-placeholder="下单开始日期"
                end-placeholder="下单结束日期"
              ></el-date-picker>
          </div>
          <div class="log-block">
            <el-select
              size="mini"
              v-model="pageInfo.contentType"
              placeholder="类型"
              style="width: 120px"
              clearable
              >
                <el-option label="操作用户名" value="4"></el-option>
                <el-option label="功能名称" value="2"></el-option>
                <el-option label="操作内容" value="7"></el-option>
                <el-option label="操作类型" value="5"></el-option>
            </el-select>
            <el-input class="box-input" size="mini" @change="contentChange" @keyup.enter.native="filter()" placeholder="请输入搜索关键字" v-model.trim="pageInfo.content" clearable></el-input>
            <button class="mini-search-btn box-btn" size="mini" @click.prevent="filter()">搜索</button>
          </div>
        </div>
        <div class="function">
          <el-table :data="tableData" header-row-class-name="header-row" border class="tr-header" max-height="550" v-loading="loading">
          <el-table-column align="center" label="ID" :width="80" prop="operateId" ></el-table-column>
          <el-table-column align="center" label="操作用户名" :width="120" prop="operator" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="操作时间" prop="operateTime" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="功能名称" :width="140" prop="businessFunction" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="类型" :width="260" prop="operateType" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="结果" :width="200" prop="operateDes" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="操作内容" :width="300"  prop="operateData" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="操作" :width="120" fixed="right">
            <template slot-scope="scope">
              <el-button class="mini-search-btn" @click="handleSee(scope.$index,scope.row)">查看</el-button>
            </template>
          </el-table-column>
          </el-table>
          <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
        </div>
      <!-- 查看详情弹框 -->
      <el-dialog title="操作详情" :modal-append-to-body="false" :visible="dialogShow" width="60%" :before-close="closeDialog">
        <el-form :model="formData" ref="formData">
          <el-row>
            <el-col :span="12">
              <el-form-item label="操作用户名：">
                <span>{{formData.operator}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
               <el-form-item label="功能名称：">
                {{formData.businessFunction}}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="操作类型：">
                {{formData.operateType}}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="操作时间：">
                {{formData.operateTime}}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="操作结果：">
                {{formData.operateDes}}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="操作内容：" class="content">
              {{formData.operateData}}
              </el-form-item>
            </el-col>
        </el-row>
        </el-form>
        <div class="btn-footer">
          <el-button class="mini-search-btn check_btn" @click="dialogShow=false">确定</el-button>
        <!-- <el-button class="check_back_btn" size="mini" @click="dialogShow=false">返回</el-button> -->
        </div>
      </el-dialog>
    </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import { parseTime } from "@/utils/index";
export default {
  components: { page },
  data() {
    return {
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        contentType: undefined,
        content: undefined
      },
      total: 0,
      tableData: [],
      formData: {},
      dialogShow: false
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
      this.pageInfo.page = 1
      this.dataFot()
    },
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.filter()
      }
    },
    // ======== 操作 ========
    handleSee(index,row) { // 查看操作
      this.formData = row
      this.dialogShow = true
    },
  
    // ======== 数据 ========
    dataFot() { // 数据列表
      this.loading = true;
      this.$http.systemLogList(this, this.pageInfo).then(res => {
        if (res.success && res.data.list && res.data.list.length>0) {
          this.tableData = res.data.list;
          this.loading = false
        } else {
          this.tableData = []
          this.loading = false
        }
        this.total = res.data.total;
      })
    },
    
    sizeChange(size) {
      this.pageInfo.size = size;
      this.dataFot()
    },
    currentChange(page) {
      this.pageInfo.page = page;
      this.dataFot()
    },
    closeDialog () { // 关闭dialog的
      this.dialogShow = false
    }
  },
  watch: {
    "pageInfo.contentType": {
      // 操作结果
      handler(val) {
        this.pageInfo.contentType = val;
        this.dataFot();
      },
      deep:true
    },
    "pageInfo.operateDes": {
      // 操作结果
      handler(val) {
        this.pageInfo.operateDes = val;
        this.dataFot();
      },
      deep:true
    },
    "pageInfo.time": {
      handler(val) {
        if (val) {
          this.pageInfo.startTime = parseTime(val[0])
          this.pageInfo.endTime = parseTime(val[1])
        } else {
          this.pageInfo.startTime = undefined
          this.pageInfo.endTime = undefined
        }
        this.pageInfo.page = 1;
        this.dataFot();
      },
      deep:true
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell{
  overflow: hidden; 
  white-space:nowrap; 
}
.ulog-list {
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
  .log-header {
    width:100%;
    overflow: hidden;
    .log-left{
      display: inline-block;
      margin: 10px 10px;
    }
    .log-block {
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
.el-dialog__wrapper{
  /deep/.el-dialog__body{
    .el-form-item{
      margin-bottom:0;
      .el-form-item__content{
        display: inline-block;
        width:70% !important;
      }
      &.content{
        .el-form-item__content{
          width: 80% !important;
          max-height: 200px;
          overflow-y: scroll;
          -ms-overflow-style:none;
          overflow:-moz-scrollbars-none;
          &::-webkit-scrollbar{
            display: none;
          }
        }
      }
    }
  }
}
</style>