<template>
  <div class="interface-list">
        <div>
          <header>
            <h4>接口调用日志</h4>
          </header>
        </div>
        <div class="interface-header">
          <div class="interface-left">
              <el-select
                size="mini"
                v-model="pageInfo.logType"
                placeholder="日志类型"
                style="width: 200px"
              >
                <el-option v-for="log in logTypes" :label="log.label" :value="log.value" :key="log.value"></el-option>
              </el-select>
              <el-select
                size="mini"
                v-model="pageInfo.status"
                placeholder="调用结果"
                style="width: 100px"
                clearable
              >
                <el-option label="成功" value="1"></el-option>
                <el-option label="失败" value="0"></el-option>
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
          <div class="interface-block">
            <el-select
              size="mini"
              v-model="pageInfo.searchType"
              placeholder="类型"
              style="width: 120px"
              clearable
              >
                <el-option label="单据编号" value="1"></el-option>
                <el-option label="调用平台名称" value="2"></el-option>
                <el-option label="请求内容" value="3"></el-option>
                <el-option label="返回结果" value="4"></el-option>
            </el-select>
            <el-input class="box-input" size="mini" @change="contentChange" @keyup.enter.native="filter()" placeholder="请输入搜索关键字" v-model.trim="pageInfo.content" clearable></el-input>
            <button class="mini-search-btn box-btn" size="mini" @click.prevent="filter()">搜索</button>
          </div>
        </div>
        <div class="function">
          <el-table :data="tableData" header-row-class-name="header-row" border class="tr-header" max-height="550" v-loading="loading">
          <el-table-column align="center" label="ID" :width="80" prop="id" ></el-table-column>
          <el-table-column align="center" label="接口名称" :width="200" prop="logType" :formatter="formatLogType" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="单据编号" :width="140" prop="otherOrderNo" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="调用时间" :width="160" prop="createTime" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="数据流向" :width="200" prop="towardType" :formatter="formatToward" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="结果" :width="200" prop="status" show-overflow-tooltip>
            <template slot-scope="scope">
              <div v-if="scope.row.status === 1">成功</div>
              <div v-if="scope.row.status === 0">失败</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="返回结果" :width="300"  prop="responseMsg" show-overflow-tooltip></el-table-column>
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
              <el-form-item label="接口名称：" prop="name">
                {{formatLogType(formData)}}
              </el-form-item>
            </el-col>
             <el-col :span="12">
              <el-form-item label="单据编号：" prop="otherOrderNo">
                {{formData.otherOrderNo}}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="调用平台：" prop="platform">
               {{formData.platform}}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="调用时间" prop="createTime">
                {{formData.createTime}}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="请求内容：" prop="requestParamJson" class="content">
                <span>{{formData.requestParamJson}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="返回结果：" prop="responseMsg" class="content">
                <span>{{formData.responseMsg}}</span>
              </el-form-item>
             </el-col>
          </el-row>
        </el-form>
          <!-- <el-form-item label="被调用平台：" prop="email">
            <div>{{formData.operateType}}</div>
          </el-form-item> -->
         
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
        logType: 1,  // 日志类型
        searchType: undefined,
        content: undefined
      },
      total: 0,
      tableData: [],
      formData: {},
      dialogShow: false,
      logTypes: [
        {
          value: 1,
          label: '推送定制信息给第三方'
        }, {
          value: 2,
          label: '接收第三方订单（基于ID）'
        }, {
          value: 3,
          label: '接收第三方订单（基于编码）'
        }, {
          value: 4,
          label: '推送销售单给ERP'
        }, {
          value: 5,
          label: 'B2B推单给工厂'
        }, {
          value: 6,
          label: '工厂发货'
        }, {
          value: 7,
          label: '推送物流信息给第三方'
        }, {
          value: 8,
          label: '第三方取消订单'
        }, {
          value: 9,
          label: '工厂取消订单'
        }, {
          value: 10,
          label: '推送核销信息给第三方'
        }, {
          value: 11,
          label: '同步erp上的物流单号'
        }
      ]
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
      this.$http.orderBusinessLogList(this, this.pageInfo).then(res => {
        if (res.success && res.data.list && res.data.list.length>0) {
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.loading = false
        } else {
          this.tableData = []
          this.loading = false
        }
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
    },
    formatToward (val) {
      switch(val.towardType) {
        case 1:
          return 'B2B->第三方'
        case 2:
          return '第三方->B2B' 
        case 3:
          return 'B2B->ERP'
        case 4:
          return 'ERP->B2B' 
        case 5:
          return '工厂-->B2B'
        case 6:
          return 'B2B->>工厂'      
      }
    },
    formatLogType (val) {
      let value = ''
      if (val && val.logType) {
        this.logTypes.forEach(item => {
          if (item.value === val.logType) {
            value = item.label
          }
        })
      }
      return value
    }
  },
  watch: {
    "pageInfo.logType": {
      // 操作结果
      handler(val) {
        this.pageInfo.logType = val;
        this.dataFot();
      },
      deep:true
    },
    "pageInfo.status": {
      // 操作结果
      handler(val) {
        this.pageInfo.status = val;
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
.interface-list {
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
  .interface-header {
    width:100%;
    overflow: hidden;
    .interface-left{
      display: inline-block;
      margin: 10px 10px;
    }
    .interface-block {
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
      .el-form-item__label{
        font-weight: bold;
      }
      .el-form-item__content{
        display: inline-block;
      }
      &.content{
        .el-form-item__content{
          width: 80% !important;
          max-height: 300px;
          overflow-y: scroll;
          &::-webkit-scrollbar{
            display: none;
          }
        }
      }
    }
  }
}
</style>