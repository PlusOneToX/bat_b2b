<template>
  <div class="interface-list">
        <div>
          <header>
            <h4>消息发送日志</h4> 
          </header>
        </div>
        <div class="interface-header">
          <div class="interface-left">
              
              <el-select
                size="mini"
                v-model="pageInfo.pushFlag"
                placeholder="推送结果"
                style="width: 100px"
                clearable
              >
                <el-option label="成功" value="1"></el-option>
                <el-option label="失败" value="0"></el-option>
              </el-select>
             <el-date-picker
                size="mini"
                v-model="time"
                style="width: 330px"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="下单开始日期"
                end-placeholder="下单结束日期"
                @change="dateChange"
              ></el-date-picker>
          </div>
          <div class="interface-block">
            <el-select
              size="mini"
              v-model="searchType"
              placeholder="类型"
              style="width: 120px"
              clearable
              >
                <el-option label="用户名" value="1"></el-option>
                <el-option label="手机号" value="2"></el-option>
                
            </el-select>
            <el-input class="box-input" size="mini" @change="contentChange" @keyup.enter.native="filter()" placeholder="请输入搜索关键字" v-model.trim="content" clearable></el-input>
            <button class="mini-search-btn box-btn" size="mini" @click.prevent="filter()">搜索</button>
          </div>
        </div>




        <div class="function">
          <el-table :data="tableData" header-row-class-name="header-row" border class="tr-header" max-height="550" v-loading="loading">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column align="center" label="ID" :width="80" prop="id" ></el-table-column>
          <el-table-column align="center" label="消息接收用户名" prop="toUsername" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="消息类型" :width="140" prop="msgType" show-overflow-tooltip>
              <template slot-scope="scope">
              <div v-if="scope.row.msgType == 1">订单状态通知</div>
              <div v-if="scope.row.msgType == 2">发货通知</div>
              <div v-if="scope.row.msgType == 3">订单未支付提醒</div>
              <div v-if="scope.row.msgType == 4">分销商审核通知</div>
              <div v-if="scope.row.msgType == 5">新订单通知</div>
              <div v-if="scope.row.msgType == 6">自定义消息</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="发送时间"  prop="createTime" show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="是否推送" :width="200" prop="pushSwitch"  show-overflow-tooltip>
            <template slot-scope="scope">
              <div v-if="scope.row.pushSwitch == 1">是</div>
              <div v-if="scope.row.pushSwitch == 0">否</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="推送终端" :width="140" prop="pushTerminal" show-overflow-tooltip>
            <template slot-scope="scope">
              <div v-if="scope.row.pushTerminal == 1">短信</div>
              <div v-if="scope.row.pushTerminal == 2">微信B2B小程序</div>
              <div v-if="scope.row.pushTerminal == 3">微信定制小程序</div>
              <div v-if="scope.row.pushTerminal == 4">抖音定制小程序</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="推送结果" :width="200" prop="status" show-overflow-tooltip>
            <template slot-scope="scope">
              <div v-if="scope.row.pushFlag == 1">成功</div>
              <div v-if="scope.row.pushFlag == 0">失败</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" :width="200">
            <template slot-scope="scope">
              <el-button class="mini-search-btn" @click="handleSee(scope.$index,scope.row)">查看</el-button>
              <el-button class="mini-search-btn" v-if="scope.row.pushFlag==0" @click="msgcenterSendAgainFun(scope.row)">重新推送</el-button>
            </template>
          </el-table-column>
          </el-table>
          <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
        </div>
      <!-- 查看详情弹框 -->
      <el-dialog title="消息详情" :modal-append-to-body="false" :visible="dialogShow" width="60%" :before-close="closeDialog">
        <el-form :model="formData" ref="formData">
          <el-row>
            <el-col :span="12">
              <el-form-item label="用户名：" prop="name">
                {{formData.toUsername}}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发送时间" prop="createTime">
                {{formData.createTime}}
              </el-form-item>
            </el-col>
             <el-col :span="12">
              <el-form-item label="消息类型：" prop="otherOrderNo">
                 <span v-if="formData.msgType==1">订单状态通知</span>
                <span v-if="formData.msgType==2">发货通知</span>
                <span v-if="formData.msgType==3">订单未支付提醒</span>
                <span v-if="formData.msgType==4">分销商审核通知</span>
                <span v-if="formData.msgType==5">新订单通知</span>
                <span v-if="formData.msgType==6">自定义消息</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否推送：" prop="platform">
               {{formData.pushSwitch==1?'是':'否'}}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="推送终端" prop="createTime">
                <span v-if="formData.pushTerminal==1">短信</span>
                <span v-if="formData.pushTerminal==2">微信B2B小程序</span>
                <span v-if="formData.pushTerminal==3">微信定制小程序</span>
                <span v-if="formData.pushTerminal==4">抖音定制小程序</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="推送结果：" prop="requestParamJson" class="content">
                <span>{{formData.pushFlag==1?'成功':'失败'}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="消息内容：" prop="responseMsg" class="content">
                <span>{{formData.content}}</span>
              </el-form-item>
             </el-col>
             <el-col :span="24">
              <el-form-item label="推送失败原因：" prop="responseMsg" class="content">
                <span>{{formData.sendFailError}}</span>
              </el-form-item>
             </el-col>
          </el-row>
        </el-form>
          <!-- <el-form-item label="被调用平台：" prop="email">
            <div>{{formData.operateType}}</div>
          </el-form-item> -->
         
        <div class="btn-footer">
          <!-- <el-button class="mini-search-btn check_btn" @click="dialogShow=false">确定</el-button> -->
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
        pushFlag:undefined, //结果是否已经推送 0否 1是
        mobile:undefined,  //手机
        username:undefined,  //用户名
        startTime:undefined,
        endTime:undefined,
      },
      content: '',
      time:'', //时间
      searchType:'',  //类型：1：用户名  2：手机号
      total: 0,
      tableData: [],
      formData: {},
      dialogShow: false,
     
    }
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

    // 日历选择
    dateChange(date){
        console.log(date);
        console.log(this.time);
        if(date!=null){
           this.pageInfo.startTime=date[0];
           this.pageInfo.endTime=date[1];
        }else{
            this.pageInfo.startTime=undefined;
           this.pageInfo.endTime=undefined;
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
      if(this.searchType==1){
          this.pageInfo.username=this.content;
      }else if(this.searchType==2){
          this.pageInfo.mobile=this.content;
      }
      this.$http.msgcenterLogList(this, this.pageInfo).then(res => {
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

    //再次推送
    msgcenterSendAgainFun(row){
      console.log(row);
        this.$http.msgcenterSendAgain(this,{id:row.id}).then(res=>{
            if(res.success){
               this.$message('已经推送')
            }
        })
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