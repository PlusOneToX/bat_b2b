<template>
  <div class="warehouse-list-wrap" >
    <header>
      <span>查看详情</span>
    </header>
    
    <div class="box-has-border">
      <div class="text-center title">
        <span>基本信息</span>
      </div>
      <!-- 左侧 -->
      <div class="half-width">
        <el-form ref="chosenData" label-width="280px">
          <el-form-item label="退换单流水号:"> 
            {{checkData.id}} 
          </el-form-item>
          <el-form-item label="退换货申请单号:"> 
            {{checkData.afterServiceOrderId}} 
          </el-form-item>
          <el-form-item label="申请人:"> 
            {{checkData.distributorName}} 
          </el-form-item>
        </el-form>
      </div>
      <!-- 右侧 -->
      <div class="half-width">
        <el-form ref="chosenData" label-width="280px">
          <el-form-item label="退货时间:"> 
            {{timeFormatter(checkData.returnTime)}} 
          </el-form-item>
          <el-form-item label="申请时间:">
            {{timeFormatter(checkData.updateTime)}}
          </el-form-item>
          <el-form-item label="状态:">
             {{forstatus(checkData.state)}}
          </el-form-item>
        </el-form> 
      </div>
    </div>

    <!-- 商品信息 -->
    <div class="box-has-border">
      <div class="text-center title">
        <span>商品信息</span>
      </div>
      <!-- :data="orderData"  -->
      <el-table class="table"  :data="orderData" border  style="width: 100%" header-row-class-name="header-row">
        <el-table-column align="center" prop="goodsName" label="商品名称"> </el-table-column>
        <el-table-column align="center" prop="goodsId" label="商品编号"> </el-table-column>
        <el-table-column align="center" prop="orderId" label="订单号"> </el-table-column>
        <el-table-column align="center" prop="specificationName" label="规格"> </el-table-column>
        <el-table-column align="center" prop="num" label="库存数量"> </el-table-column>
      </el-table>

    </div>

    <!-- 操作信息 -->
    <div class="box-has-border" style="margin:0;">
      <div class="text-center title">
        <span>操作信息</span>
      </div>
      <div class="form">
        <el-form label-width="180px">
          <el-form-item label="操作人">
            <el-input v-model="mytext" type="textarea" :rows="5"></el-input>
          </el-form-item>
        </el-form>
      </div>

      <div class="operation">
        <span class="instruction">
          <span class="instruction_left" v-if="checkedData == 1 || checkData == 2"> 当前可执行操作： </span>
          <!-- v-if="checkedData == 1" -->
          <el-button size="mini" v-if="checkedData == 1" @click="salesReturn()">退货</el-button>
          <el-button size="mini" v-if="checkedData == 1 || checkData == 2" @click="callOff()" >取消到货</el-button>
        </span>
      </div>

      <!-- 操作日志 -->
      <div class="sheet">
        <el-table class="table" :data="log" border  style="width: 100%" header-row-class-name="header-row">
          <el-table-column prop="operationName" label="操作者"> </el-table-column>
          <el-table-column prop="goodsId" label="操作时间"> </el-table-column>
          <el-table-column prop="itemId" label="订单状态"> </el-table-column>
          <el-table-column prop="itemCode" label="付款状态"> </el-table-column>
          <el-table-column prop="specificationName" label="状态"> </el-table-column>
          <el-table-column prop="specificationName" label="备注"> </el-table-column>
        </el-table>
      </div>
    </div>

    <div class="box_small">
      <el-button @click="back()" size="small" >返回</el-button>
    </div>


  </div>

  
</template>

<script>
// import {getOrderDetail} from '@/views/order/orderData'
import procedure from '@/components/Procedure/index'
import { parseTime } from "@/utils/index";
// ======引用组件======
// import editDistributionMode from '@/views/order/orderList/edit/editDistributionMode'
// import editCostInfo from '@/views/order/orderList/edit/editCostInfo'


export default {
  name: 'billsindex',
  mounted(){
    // if(this.$route.params.id) {
      this.updada()
    // }
    
  },
  data(){
    return {
      checkData: {},
      orderData: [],
      log: [],
      mytext: "",
    }
  },
  // props: { 
  //   // pageState: String,
  //   // entryIndex: Number, // 本组件主数据在父组件数组中的index
  //   // tableData: Array,
  // },
  computed: {
    checkedData(){
      let params = this.$route;
      return this.$route.params.state
    }
  },
  methods:{
    // 页面主要数据
    updada() {
      let params = this.$route
      if(this.$route.params.id) {
        this.$api.get(this, 'admin/u/p/returnOrder', { id: this.$route.params.id }).then(res => {
        this.checkData = res.returnOrder;
        this.orderData = res.returnOrderDetailQuerie
      })
       this.$api.get(this, 'admin/u/p/returnOrder/log', { returnOrderId: this.$route.params.id }).then(res => {
        this.log = res.returnOrderLog
      })
    }
    
       
    },
    // 返回按钮
    back() {
      this.$router.push({name: 'orderbills'})
    },
    // 时间格式化
    timeFormatter(cellValue){ 
      return parseTime(cellValue)
    },
     // 包裹状态
    forstatus(row) {
      switch (row) {
        case 1:
          return "待处理";
        case 2:
          return "已退货";
        case 3:
          return "取消退货";
      }
    },
    // 退货按钮
    salesReturn() {
      this.$api.put(this,'admin/u/p/returnOrder/state',{
        returnOrderId: this.$route.params.id,
        state: 2,
        remark: this.mytext
        }).then(res => {
          if(res.code == 0) {
            this.$message.success({
                  message: '成功退货',
                  duration: 3 * 1000,
              })
            this.$router.go(-1)
          }else {
            this.$message.success({
                  message: '退货失败，请返回重新退货',
                  duration: 3 * 1000,
              })
          }
        })
    },
    // 取消到货按钮
    callOff() {
      this.$api.put(this,'admin/u/p/returnOrder/state',{
        returnOrderId: this.$route.params.id,
        state: 3,
        remark: this.mytext
      }).then(res => {
        if(res.code == 0) {
            this.$message.success({
                  message: '成功取消到货',
                  duration: 3 * 1000,
              })
            this.$router.go(-1)
          }else {
            this.$message.success({
                  message: '取消到货失败，请返回重新取消到货',
                  duration: 3 * 1000,
              })
          }
      })
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .warehouse-list-wrap{
    min-width: 1050px;
    background-color: #fff;
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      span{
        margin-left: 30px;
      }
      .btn-add{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
      }
    }
    .box-has-border{
        overflow: hidden;
        margin-top: 15px;
        background-color: #fff;
        .table {
          text-align: center;
        }
        .title{
          padding-bottom: 10px;
          padding-top: 10px;
          border-top: 1px solid $tableColor;
          border-bottom: 1px solid $tableColor;
          text-align: center;
          background-color: lighten(grey, 40%);
        }
        .form {
          width: 50%;
          padding: 20px 0 0 0
        }
        .operation {
          margin-left: 45px;
        }
        
        .sheet {
          margin-top: 15px;
        }
        .half-width{
          width: 50%;
          box-sizing: border-box;
          float: left;
          .half_search {
            width: 35%;
          }
          .el-form-item {
            margin-bottom: 10px;
          }
        }
      }
   .box_small {
     text-align: center;
     margin-top: 20px;
   }
  }
</style>
