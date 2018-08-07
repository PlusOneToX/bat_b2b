<template>
  <div>
    <section class="order-detail" v-show="pageState == 'orderDetail'">
      <div class="box-btn-top">
        <!-- <div class="text-center">
          <el-button size="mini" @click="preEntry">前一个订单</el-button>
          <el-button size="mini" @click="nextEntry">后一个订单</el-button>
          <el-button size="mini" type="primary">打印订单</el-button>
        </div> -->
      </div>
      <div class="box-has-border">
        <div class="text-center title">
          <span>基本信息</span>
        </div>
        <div class="half-width">
          <el-form ref="chosenData" label-width="280px">
            <el-form-item label="订单号"> {{chosenData.id}} </el-form-item>
            <el-form-item label="购买者"> {{chosenData.distributorName || '该字段为空'}} </el-form-item>
            <el-form-item label="支付方式"> {{orderPaymentFormatter(chosenData.orderPayment)}} </el-form-item>
            <el-form-item label="配送方式">
              <span v-if="chosenData.distribution">
                {{distributionsFormatter(chosenData.distribution.id)}}
              </span>
              <el-button size="mini" @click="_ => {$emit('switchPageState', 'editDistributionMode')}">编辑</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="half-width">
          <el-form ref="chosenData" label-width="190px">
            <el-form-item label="订单状态"> {{orderStatusFormatter(chosenData.orderStatus)}} </el-form-item>
            <el-form-item label="下单时间"> {{timeFormatter(chosenData.createTime)}} </el-form-item>
            <el-form-item label="付款时间"> {{timeFormatter(chosenData.payTime) || '未付款'}} </el-form-item>
            <el-form-item label="发货时间"> {{timeFormatter(chosenData.deliverTime) || '未发货'}} </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="box-has-border">
        <div class="text-center title">
          <span>其他信息</span>
          <el-button size="mini" @click="_ => {$emit('switchPageState', 'editOtherInfo')}">编辑</el-button>
        </div>
        <div class="half-width">
          <el-form ref="chosenData" label-width="280px">
            <el-form-item label="是否开票"> {{booleanFormatter(chosenData.isInvoice)}} </el-form-item>
            <el-form-item label="是否加急"> {{booleanFormatter(chosenData.isUrgent)}} </el-form-item>
            <el-form-item label="发票类型" v-if="chosenData.invoice"> {{invoiceTypeFormatter(chosenData.invoice.invoiceType)}} </el-form-item>
            <el-form-item label="发票抬头" v-if="chosenData.invoice"> {{invoiceTitleFormatter(chosenData.invoice.invoiceTitle)}} </el-form-item>
            <el-form-item label="分销商留言"> {{chosenData.remark || '加载中or空字符串'}} </el-form-item>
          </el-form>
        </div>
        <div class="half-width" v-if="chosenData.invoice">
          <el-form ref="chosenData" label-width="190px">
            <el-form-item label="增值税发票注册地址"> {{chosenData.invoice.registerAddress}} </el-form-item>
            <el-form-item label="增值税发票注册电话"> {{chosenData.invoice.registerPhone}} </el-form-item>
            <el-form-item label="开户银行"> {{chosenData.invoice.bankAccountName}} </el-form-item>
            <el-form-item label="增值税发票银行账户"> {{chosenData.invoice.bankAccount}} </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="box-has-border" v-if="chosenData.delivery">
        <div class="text-center title">
          <span>收货人信息</span>
          <el-button size="mini" @click="_ => {$emit('switchPageState', 'editDeliveryInfo')}">编辑</el-button>
        </div>
        <div class="half-width">
          <el-form ref="chosenData" label-width="280px">
            <el-form-item label="收货人"> {{chosenData.delivery.userName || '空字符串或不存在...'}} </el-form-item>
            <el-form-item label="地址"> {{chosenData.delivery.address || '空字符串或不存在...'}} </el-form-item>
            <el-form-item label="电话"> {{chosenData.delivery.phone || '空字符串或不存在...'}} </el-form-item>
            <el-form-item label="最近送货时间"> {{chosenData.delivery.deliveryTime || '空字符串或不存在...'}} </el-form-item>
          </el-form>
        </div>
        <div class="half-width">
          <el-form ref="chosenData" label-width="190px">
            <el-form-item label="电子邮件"> 接口未提供 </el-form-item>
            <el-form-item label="邮编" v-if="chosenData.delivery"> {{chosenData.delivery.zipCode || '空字符串或不存在...'}} </el-form-item>
            <el-form-item label="手机" v-if="chosenData.delivery"> {{chosenData.delivery.mobile || '空字符串或不存在...'}} </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="box-has-border">
        <div class="text-center title">
          <span>商品信息</span>
          <el-button size="mini" @click="_ => {$emit('switchPageState', 'editGoodsInfo')}">编辑</el-button>
        </div>
        <el-table border :data="chosenData.goods" style="width: 100%" header-row-class-name="header-row">
          <el-table-column align="center" prop="goodsName" label="商品名称"> </el-table-column>
          <el-table-column align="center" prop="goodsId" label="商品编号"> </el-table-column>
          <el-table-column align="center" prop="itemId" label="货号"> </el-table-column>
          <el-table-column align="center" prop="itemCode" label="货品号"> </el-table-column>
          <el-table-column align="center" prop="specificationName" label="规格"> </el-table-column>
          <el-table-column align="center" prop="salePrice" label="价格"> </el-table-column>
          <el-table-column align="center" prop="itemDiscount" label="价格折扣"> </el-table-column>
          <el-table-column align="center" prop="saleTotalAmount" label="数量"> </el-table-column>
          <el-table-column align="center" prop="deliverCount" label="已发数量"> </el-table-column>
          <el-table-column align="center" prop="unDeliverCount" label="未发数量"> </el-table-column>
          <el-table-column align="center" prop="saleTotalAmount" label="小计"> </el-table-column>

        </el-table>
      </div>
      <div class="box-has-border"  v-if="chosenData.cost">
        <div class="text-center title">
          <span>费用信息</span>
          <el-button size="mini" @click="_ => {$emit('switchPageState', 'editCostInfo')}">编辑</el-button>
        </div>
        <div class="cost-line">
          <span class="cost-info">
            <span class="cost-label">商品总金额 :</span>
            <span class="cost-val">{{chosenData.cost.goodsAmount}}</span>
          </span> -
          <span class="cost-info">
            <span class="cost-label">订单折扣 :</span>
            <span class="cost-val">{{chosenData.cost.orderDiscount}}</span>
          </span> -
          <span class="cost-info">
            <span class="cost-label">商品折扣 :</span>
            <span class="cost-val">{{chosenData.cost.goodsDiscount}}</span>
          </span> +
          <span class="cost-info">
            <span class="cost-label">发票税额 :</span>
            <span class="cost-val">{{chosenData.cost.invoiceTax}}</span>
          </span> +
          <span class="cost-info">
            <span class="cost-label">配送费用 :</span>
            <span class="cost-val">{{chosenData.cost.distributionMoney}}</span>
          </span> +
          <span class="cost-info">
            <span class="cost-label">保价费用 :</span>
            <span class="cost-val">{{chosenData.cost.insurance}}</span>
          </span> +
          <span class="cost-info">
            <span class="cost-label">支付费用 :</span>
            <span class="cost-val">{{chosenData.cost.paidAmount}}</span>
          </span> +
          <span class="cost-info">
            <span class="cost-label">包装费用 :</span>
            <span class="cost-val">{{chosenData.cost.packingCost}}</span>
          </span>
        </div>
        <div class="cost-line align-right">
          <span class="cost-info ">
            <span class="cost-label"> = 订单总金额 : ￥ </span>
            <span class="cost-val"> {{chosenData.cost.orderAmount}} 元 </span>
          </span>
        </div>
        <div class="cost-line">
          <span class="cost-info">
            <span class="cost-label">已付款金额</span> :
            <span class="cost-val">{{chosenData.cost.paidAmount}}</span>
          </span> -
          <span class="cost-info">
            <span class="cost-label">使用余额</span> :
            <span class="cost-val">{{chosenData.cost.depositAmount}}</span>
          </span> -
          <!-- <span class="cost-info">
            <span class="cost-label">使用积分</span> :
            <span class="cost-val">后端未提供</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">使用优惠券</span> :
            <span class="cost-val">后端未提供</span>
          </span> -->
          <span class="cost-info">
            <span class="cost-label">促销金额</span> :
            <span class="cost-val">{{chosenData.cost.promotionAmount}}</span>
          </span>
        </div>
        <div class="cost-line align-right">
          <span class="cost-info">
            <span class="cost-label"> = 应付款金额 : ￥ </span>
            <span class="cost-val"> {{chosenData.cost.payAmount}} 元 </span>
          </span>
        </div>

      </div>

      <div class="box-has-border" v-if="approvalFlows[0]">
        <div class="text-center title">
          <span>审批流程</span>
        </div>
        <procedure :flows="approvalFlows" :statusFormatter="capitalStatusFormatter"></procedure>
      </div>
      <div class="box-has-border">
        <console :chosenData="chosenData" :inApprovalContext="inApprovalContext"></console>
        <el-table border :data="approvalFlows" style="width: 100%" header-row-class-name="header-row"  v-if="inApprovalContext">
          <el-table-column align="center" prop="checkUserName" label="审批人"> </el-table-column>
          <el-table-column align="center" prop="checkTime" label="审批时间"> </el-table-column>
          <el-table-column align="center" prop="checkStatus" :formatter="(row,columnm,val) => {return capitalStatusFormatter(val)}" label="审批状态"> </el-table-column>
          <el-table-column align="center" prop="remark" label="备注"> </el-table-column>
        </el-table>
      </div>
      <div class="text-center" style="margin-top:30px">
        <el-button type="primary" @click="_ => {$emit('switchPageState')}">
          返回上一级
        </el-button>
      </div>
    </section>

    <!-- 以下是编辑界面 子组件 orderDetail将参数chosenData传入子组件 使其知晓当前订单是哪个-->
    <editDistributionMode v-show="pageState == 'editDistributionMode'" v-if="chosenData.distribution" :chosenData="chosenData"></editDistributionMode>
    <editOtherInfo v-show="pageState == 'editOtherInfo'" :chosenData="chosenData"></editOtherInfo>
    <editCostInfo v-show="pageState == 'editCostInfo'" v-if="chosenData.cost" :chosenData="chosenData"></editCostInfo>
    <editDeliveryInfo v-show="pageState == 'editDeliveryInfo'" v-if="chosenData.delivery" :chosenData="chosenData"></editDeliveryInfo>
    <editGoodsInfo v-show="pageState == 'editGoodsInfo'" v-if="chosenData.goods" :chosenData="chosenData"></editGoodsInfo>

    <createDelivery v-if="pageState == 'createDelivery'" :chosenData="chosenData" :distributions="distributions"></createDelivery>
    <invalid v-if="pageState == 'invalid'" :chosenData="chosenData" :distributions="distributions"></invalid>
    <noPay v-if="pageState == 'noPay'" :chosenData="chosenData" :distributions="distributions"></noPay>
    
  </div>
</template>

<script>
import {getOrderDetail, getdistributions, getCheckDetail, getUsers} from '@/views/order/orderData'
import {orderPaymentFormatter, orderStatusFormatter, isInvoiceFormatter, booleanFormatter, invoiceTypeFormatter, invoiceTitleFormatter, capitalStatusFormatter} from '@/views/order/orderUtils'
import {parseTime} from '@/utils/index'
import console from '@/views/order/components/console'
// ======引用组件======
import procedure from '@/components/Procedure/index'
import editDistributionMode from '@/views/order/orderList/edit/editDistributionMode'
import editOtherInfo from '@/views/order/orderList/edit/editOtherInfo'
import editCostInfo from '@/views/order/orderList/edit/editCostInfo'
import editDeliveryInfo from '@/views/order/orderList/edit/editDeliveryInfo'
import editGoodsInfo from '@/views/order/orderList/edit/editGoodsInfo'
import createDelivery from '@/views/order/orderList/operation/createDelivery'
import invalid from '@/views/order/orderList/operation/invalid'
import noPay from '@/views/order/orderList/operation/noPay'


const _ = require('ramda');

export default {
  name: 'OrderDetail',
  components: {
    procedure,
    editDistributionMode,
    editOtherInfo,
    editCostInfo,
    editDeliveryInfo,
    editGoodsInfo,
    console,
    createDelivery,
    invalid,
    noPay,
    // editOperationInfo,
  } ,
  created(){
    this.updateMainData();
    getdistributions(this) .then(res => {
      this.distributions = res.distributions;
    });

  },
  data(){
    return { 
      goodsInfoTable: [],
      chosenData: {},
      approvalFlows: [],
      distributions: [],
      checkDetail: {}, //审批数据
      // operateLog: [],
    }
  },
  props: {
    // 0 订单详情 1 编辑配送方式 2 编辑其他信息 
    // 3 编辑收货人信息 4 编辑商品信息 5 编辑费用信息 
    pageState: String,
    entryIndex: Number, // 本组件主数据在父组件数组中的index
    tableData: Array,
  },
  computed: {
    inApprovalContext(){
      return this.$options.parent.$options.name == 'applyForPrice';
    },
  },
  methods:{
    capitalStatusFormatter(stateCode){
      this.capitalStatusFormatter = capitalStatusFormatter;
      return this.capitalStatusFormatter(stateCode);
    },
    invoiceTypeFormatter(rawData){
      this.invoiceTypeFormatter = invoiceTypeFormatter;
      return this.invoiceTypeFormatter(rawData);
    },
    invoiceTitleFormatter(rawData){
      this.invoiceTitleFormatter = invoiceTitleFormatter;
      return this.invoiceTitleFormatter(rawData);
    },
    booleanFormatter(rawData){
      this.booleanFormatter = booleanFormatter;
      return this.booleanFormatter(rawData);
    },
    timeFormatter(rawData){
      this.timeFormatter = parseTime;
      return this.timeFormatter(rawData);
    },
    orderStatusFormatter(stateCode){
      this.orderStatusFormatter = orderStatusFormatter;
      return this.orderStatusFormatter(stateCode);
    },
    distributionsFormatter(id){
      const distributions = this.distributions;
      for(let distribution of distributions ){
        if(distribution.id === id){
          return distribution.name;
        }
      }
      return id;
    },
    orderPaymentFormatter(stateCode){
      this.orderPaymentFormatter = orderPaymentFormatter;
      this.orderPaymentFormatter(stateCode);
    },
    nextEntry(){
      if(this.entryIndex >= this.tableData.length - 1){
        this.$message({message: '已经是最后一条了', type:'warning'})
      }else{
        this.$emit('checkNextOrder')
      }
    },
    preEntry(){
      if(this.entryIndex <= 0){
        this.$message({message: '已经是第一条了', type:'warning'})
      }else{
        this.$emit('checkPreOrder')
      }
    },
    updateMainData(){
      const
        chosenOne = this.tableData[this.entryIndex],
        // 订单id字段有可能是orderId，也可能是id
        orderId = chosenOne.orderId || chosenOne.id;
      getOrderDetail(this, {id: orderId}).then(res => {
        this.chosenData = res.order;
      })


      /**
       * 补全flows的userName，柯里化分步传参
       * patchFlows :: (Array, Array) -> Array
       * @param  {Array} flows
       * @param  {Array} checkers
       * @return {Array}
       */
      let patchFlows = _.curry((flows, checkers) => {
        // 检查flows和checkers长度是否相等
        if(flows.length != checkers.length){
          throw(new Error('flows checkers 长度不等！'))
        }
        // 拼装flows, checkers
        const isCouple = (flow, checker) => flow.checkUser == checker.id;

        for(let flow of flows){
          for(let checker of checkers){
            if(isCouple(flow, checker)){
              flow.checkUserName = checker.name;
            }
          }
        }
        // 将结果赋值给approvalFlows
        this.approvalFlows = flows;
      })

      // 如果需要审批人数据，发出请求
      if(this.inApprovalContext){
        getCheckDetail(this, {id: this.tableData[this.entryIndex].id})
        .then(res => {
          const flows = res.checkDetail.check.flows;
          patchFlows = patchFlows(flows); //分步传参第一步
          // 拼装ids
          let ids = flows[0].checkUser.toString() ;
          for(let i = 1, len = flows.length; i < len; i++){
            ids = ids + ',' + flow.checkUser;
          }
          // 根据flows的ids请求用户信息
          return getUsers(this,{ids})
        })
        .then(res => {
          patchFlows(res.admins); //分步传参第二步/最后一步
        })


      }
    },
  },
  watch: {
    entryIndex(){
      this.updateMainData();
    },
    '$route' (to, from) {
      this.$router.go(0);
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .warehouse-list-wrap{
    height: 100%;
    min-width: 1050px;
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
    .el-form div.el-form-item{
      margin-bottom: 5px;
    }
    .order-detail{
      background-color: white;
      padding-bottom: 30px;
      form.el-form{
        margin-top: 0;
      }
      .half-width{
        width: 50%;
        box-sizing: border-box;
        float: left;
      }
      .text-center{
        text-align: center;
      }
      .text-center.title{
        background-color: $bg;
        padding-top: 10px;
        padding-bottom: 10px;
        border-bottom: 1px solid $tableColor;
        border-top: 1px solid $tableColor;
        
      }
      .box-btn-top{
        padding: 20px;
      }
      .box-has-border{
        overflow: hidden;
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
            max-width: 1000px;
          }
        }
      }
      .function{
        padding: 0px 16px 16px 16px;
        background-color: white;
        .btn-export{
          background-color: lighten(grey, 40%);
        }
        .search{
          float: right;
        }
        .box-search{
          width: 140px;
        }
        .btn-search{
          background-color: $lakeBlue;
          color: white;
        }
      }
      .header-row {
        background-color: $table-header-bg;
        th {
          padding: 5px;
          text-align: center;
        }
      }
      td {
        text-align: center;
      }
      .table-cell{
        text-align: center;
      }
      div.el-tabs__nav{
        margin-left: 30px;
      }
    }
  }
</style>
