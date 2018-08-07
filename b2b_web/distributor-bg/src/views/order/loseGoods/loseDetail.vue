<template>
  <main class="lose-detail-wrap">
    <header>
      <h4>查看缺货登记</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
        返回缺货登记列表
      </el-button>
    </header>
    <div class="box-has-border clearfix">
      <div class="text-center title">
        <span>缺货登记信息</span>
      </div>
      <div class="half-width">
        <el-form ref="tableData" label-width="280px">
          <el-form-item label="缺货商品编号:"> {{tableData.goodsNo}} </el-form-item>
          <el-form-item label="缺货商品名称:"> {{tableData.goodsName}} </el-form-item>
          <el-form-item label="存货编码:"> {{tableData.itemCode}} </el-form-item>
          <el-form-item label="存货名称:"> {{tableData.itemName}} </el-form-item>
          <el-form-item label="缺货数量:"> {{tableData.needNumber}} </el-form-item>
          <el-form-item label="缺货备注:"> {{tableData.remark}} </el-form-item>
          <!-- 
          <el-form-item label="缺货商品编号:"> {{tableData.goodsNo}} </el-form-item>
          <el-form-item label="规格:"> {{tableData.specificationValueName}} </el-form-item>
          <el-form-item label="详细描述:"> {{tableData.remark}} </el-form-item> -->
        </el-form>
      </div>
      <div class="half-width">
        <el-form ref="tableData" label-width="190px">
          <el-form-item label="ERP预测单号:"> {{tableData.goodsLoseErpNo}} </el-form-item>
          <el-form-item label="登记分销商用户名:"> {{tableData.userName}} </el-form-item>
          <el-form-item label="登记时间:"> {{parseTime(tableData.createTime)}} </el-form-item>
          <el-form-item label="联系人:"> {{tableData.contactName}} </el-form-item>
          <el-form-item label="手机号:"> {{tableData.contactMobile}} </el-form-item>
        </el-form>
      </div>
    </div>
    <!-- <div class="box-has-border clearfix">
      <div class="text-center title">
        <span>处理信息</span>
      </div>
      <div v-if="!tableData.process" style="text-align:center;padding:30px 0;color:grey"> 
        <span>尚未有处理信息</span>
      </div>
      <div class="half-width" v-if="tableData.process">
        <el-form ref="tableData" label-width="280px">
          <el-form-item label="处理用户:"> {{tableData.process.processUserName}} </el-form-item>
          <el-form-item label="处理备注:"> {{tableData.process.processRemark}} </el-form-item>
        </el-form>
      </div>
      <div class="half-width">
        <el-form ref="tableData" label-width="190px">
          <el-form-item label="处理时间:"> {{parseTime(tableData.process.updateTime)}}  </el-form-item>
        </el-form>
      </div>
    </div> -->
    <!-- <div class="operation-bar clearfix">
      <el-form label-width="90px">
        <el-form-item label="处理备注" style="width:500px">
          <el-input v-model="remark" size="mini"></el-input>
        </el-form-item>
        <div class="operation-btns">
          <el-checkbox v-model="shortMsg" style="line-height:35px">短信通知</el-checkbox>
          <el-button size="mini" @click="handleOperation" type="primary">
            处理
          </el-button>
          <el-button size="mini" @click="new Function()">
            发送短信
          </el-button>

        </div>
      </el-form>
    </div> -->
  </main>
</template>

<script>
import {getlosegoodsDetail, handleLose} from '@/views/order/orderData'
import {confirmCreator} from '@/views/order/orderUtils'
import {parseTime} from '@/utils/index'

export default {
  name: 'loseDetail',
  components: {

  } ,
  created(){
    this.updateMainData();
  },
  data(){
    return { 
      tableData: {},
      remark: null,
      shortMsg: false
    }
  },
  // props: {
  //   chosenData: Object,
  // },
  methods:{
    cancel() { // 返回操作
      this.$router.push({ name: 'loseGoods'})
    },
    parseTime(code){ // 时间格式化
      return parseTime(code);
    },
    updateMainData(){ // 主数据
    // id: this.$route.query.userId 缺货登记id
      getlosegoodsDetail(this, {id: this.$route.query.id}).then(res => { // 缺货登记详情
        this.tableData = res.loseGoods
      })
    },
    handleOperation(){ // 处理操作
      const req = _ => {
        return handleLose(this, {id: this.$route.query.id, remark: this.remark}) // 缺货登记处理
      }

      this.handleOperation = _ => {
        return confirmCreator(this)('处理', req) // 发出请求
        .then(_ => this.updateMainData())
      };
      this.handleOperation();
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .lose-detail-wrap{
    height: 95%;
    background-color: white;
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
    .half-width{
      width: 50%;
      box-sizing: border-box;
      float: left;
    }
    .text-center{
      text-align: center;
    }
    .el-form div.el-form-item{
      margin-bottom: 0;
    }
    .box-has-border{
      .cost-line{
        padding-bottom: 10px;
        padding-top: 10px;
        border-bottom: 1px solid $tableColor;
        padding-left: 30px;
        span.cost-info{
          margin-left: 5px;
        }
        span.cost-info:last-child{
          margin-right: 35px;
        }
      }
      .cost-line:last-child{
        border-bottom: none;
      }

      .align-right{
        text-align: right;
      }
      div.form{
        margin-top: 30px;
        margin-bottom: 40px;
        form.el-form{
          margin-right: 0;
          width: 80%;
          min-width: 800px;
          // display: inline-block;
          max-width: 1000px;
        }
      }
      .operation{
        padding-bottom: 50px;
        .operation-part:first-child{
          margin-left: 100px;
        }
        .operation-part:last-child{
          float: right;
          margin-right: 80px;
        }
      }
    }
    .text-center.title{
      padding-top: 30px;
      padding-bottom: 30px;
      border-top: 1px solid $tableColor;
    }
    .operation-bar{
      border-bottom: 1px solid $tableColor;
      border-top: 1px solid $tableColor; 
      padding: 15px 0;
      .el-form-item{
        float: left;
      }
      .operation-btns{
        float: right;
        margin-right: 30px;
      }
    }
  }
</style>
