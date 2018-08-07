<template>
  <div class="interface-list">
        <div>
          <header>
            <h4>微信消息模板设置</h4> 
          </header>
        </div>
       
        <div class="function">
          <el-table :data="tableData" header-row-class-name="header-row" border class="tr-header" max-height="550" v-loading="loading">
          
          <el-table-column align="center" label="消息渠道"  prop="channel"  show-overflow-tooltip>
                <template slot-scope="scope">
                    <span v-if="scope.row.channel==1">B2B</span>
                    <span v-if="scope.row.channel==2">兑换商城</span>
                    <span v-if="scope.row.channel==3">定制商城</span>
                </template>
          </el-table-column>
          <el-table-column align="center" label="消息名称" prop="type" show-overflow-tooltip>
              <template slot-scope="scope">
                    <span v-if="scope.row.type==1">订单状态通知</span>
                    <span v-if="scope.row.type==2">发货通知</span>
                    <span v-if="scope.row.type==3">订单未支付提醒</span>
                    <span v-if="scope.row.type==4">分销商审核通知</span>
                    <span v-if="scope.row.type==5">新订单通知</span>
                </template>
          </el-table-column>
          <el-table-column align="center" label="微信模板ID"  prop="templateId" show-overflow-tooltip>
             <template slot-scope="scope">
                <el-input v-model="scope.row.templateId" placeholder="请输入微信模板ID" size="small" maxlength="100" />
             </template>
          </el-table-column>
          <el-table-column align="center" label="说明"  prop="remark"  show-overflow-tooltip></el-table-column>
         
          </el-table>

          <div style="margin-top:40px">
             <el-button style="margin-left:47%;" class="mini-search-btn" @click="handleSubmit()" > 保存 </el-button>
             <el-button size="mini" @click="handleReset"> 重置 </el-button>
          </div>
          
        </div>
     
    </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import { parseTime } from "@/utils/index";
export default {
  components: { page },
  data() {
    return {
       loading:false,
       tableData:[],
       tableData2:[]
    };
  },
  mounted() {
    this.dataFot();
  },
  activated() {
    this.dataFot();
  },
  methods: {
   
  
    // ======== 数据 ========
    dataFot() { // 数据列表
      this.loading = true;
      this.$http.templatelist(this,{}).then(res => {
        if (res.success && res.data && res.data.length>0) {
          this.tableData=res.data;
          this.tableData2=JSON.parse(JSON.stringify(res.data)) ;
          this.loading=false;
        } else{
          this.loading=false;
        }
      })
    },

    // 保存
    handleSubmit(){
        
        this.$http.templatePut(this,this.tableData).then(res => {
          if (res.success ) {
            this.$message('修改成功')
           
          } 
        })
    },

    // 重置
    handleReset(){
      console.log(this.tableData2);
       this.tableData=JSON.parse(JSON.stringify(this.tableData2));
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