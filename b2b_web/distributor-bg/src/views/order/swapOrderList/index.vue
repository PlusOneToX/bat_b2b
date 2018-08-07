<template>
  <div>
    <section class="order-detail" v-show="pageState == 'orderDetail'">
      <div class="box-btn-top">
        <div class="text-center">
          <el-button size="mini" @click="preEntry">前一个订单</el-button>
          <el-button size="mini" @click="nextEntry">后一个订单</el-button>
          <el-button size="mini" type="primary">打印订单</el-button>
        </div>
      </div>
      <div class="box-has-border">
        <div class="text-center title">
          <span>基本信息</span>
        </div>
        <div class="half-width">
          <el-form ref="chosenData" label-width="280px">
            <el-form-item label="订单号">
              {{chosenData.id}}
            </el-form-item>
            <el-form-item label="购买者">
              {{chosenData.distributorName || '该字段为空'}}
            </el-form-item>
            <el-form-item label="支付方式">
              {{chosenData.orderPayment}}
            </el-form-item>
            <el-form-item label="配送方式">
              <span v-if="chosenData.distribution">
                {{chosenData.distribution.distributionId}}
              </span>
              <el-button size="mini" @click="_ => {$emit('switchPageState', 'editDistributionMode')}">编辑</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="half-width">
          <el-form ref="chosenData" label-width="190px">
            <el-form-item label="订单状态">
              {{chosenData.orderStatus}}
            </el-form-item>
            <el-form-item label="下单时间">
              {{chosenData.createTime}}
            </el-form-item>
            <el-form-item label="付款时间">
              何志超：迟些补上
            </el-form-item>
            <el-form-item label="发货时间">
              <span v-if="chosenData.distribution">
                {{chosenData.distribution.createTime}}
              </span>
            </el-form-item>

          </el-form>
        </div>
      </div>
      <div class="box-has-border">
        <div class="text-center title">
          <span>其他信息</span>
          <el-button size="mini" @click="_ => {return}">编辑</el-button>
        </div>
        <div class="half-width">
          <el-form ref="chosenData" label-width="280px">
            <el-form-item label="是否开票">
              {{chosenData.isInvoice}}
            </el-form-item>
            <el-form-item label="是否加急">
              {{chosenData.isUrgent}}
            </el-form-item>
            <el-form-item label="发票类型">
              {{chosenData.invoiceType}}
            </el-form-item>
            <el-form-item label="发票抬头">
              {{chosenData.invoiceTitle}}
            </el-form-item>
            <el-form-item label="分销商留言">
              {{chosenData.WhName}}
            </el-form-item>

          </el-form>
        </div>
        <div class="half-width">
          <el-form ref="chosenData" label-width="190px">
            <el-form-item label="增值税发票注册地址">
              {{chosenData.orderStatus}}
            </el-form-item>
            <el-form-item label="增值税发票注册电话">
              {{chosenData.createTime}}
            </el-form-item>
            <el-form-item label="开户银行">
              {{chosenData.bankDepositName}}
            </el-form-item>
            <el-form-item label="增值税发票银行账户">
              {{chosenData.WhName}}
            </el-form-item>

          </el-form>
        </div>
      </div>
      <div class="box-has-border" v-if="chosenData.delivery">
        <div class="text-center title">
          <span>收货人信息</span>
          <el-button size="mini" @click="_ => {return}">编辑</el-button>
        </div>
        <div class="half-width">
          <el-form ref="chosenData" label-width="280px">
            <el-form-item label="收货人">
              {{chosenData.delivery.userName || '加载中...'}}
            </el-form-item>
            <el-form-item label="地址">
              {{chosenData.delivery.address || '加载中...'}}
            </el-form-item>
            <el-form-item label="电话">
              {{chosenData.delivery.phone || '加载中...'}}
            </el-form-item>
            <el-form-item label="最近送货时间">
              {{chosenData.delivery.deliveryTime || '加载中...'}}
            </el-form-item>
          </el-form>
        </div>
        <div class="half-width">
          <el-form ref="chosenData" label-width="190px">
            <el-form-item label="电子邮件">
              接口未提供
            </el-form-item>
            <el-form-item label="邮编" v-if="chosenData.delivery">
              {{chosenData.delivery.zipCode || '加载中...'}}
            </el-form-item>
            <el-form-item label="手机" v-if="chosenData.delivery">
              {{chosenData.delivery.mobile || '加载中...'}}
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="box-has-border">
        <div class="text-center title">
          <span>商品信息</span>
          <el-button size="mini" @click="_ => {return}">编辑</el-button>
        </div>
        <el-table border :data="chosenData.goods" style="width: 100%" header-row-class-name="header-row">
          <el-table-column align="center" prop="goodsName" label="商品名称"> </el-table-column>
          <el-table-column align="center" prop="goodsId" label="商品编号"> </el-table-column>
          <el-table-column align="center" prop="itemId" label="货号"> </el-table-column>
          <el-table-column align="center" prop="itemCode" label="货品号"> </el-table-column>
          <el-table-column  align="center" prop="specificationName" label="规格"> </el-table-column>
          <el-table-column align="center" prop="salePrice" label="价格"> </el-table-column>
          <el-table-column align="center" prop="itemDiscount" label="价格折扣"> </el-table-column>
          <el-table-column align="center" prop="saleTotalAmount" label="数量"> </el-table-column>
          <el-table-column align="center" prop="ccccc" label="已发数量"> </el-table-column>
          <el-table-column align="center" prop="cccccc" label="未发数量"> </el-table-column>
          <el-table-column align="center" prop="cccccc" label="小计"> </el-table-column>
        </el-table>
      </div>
      <div class="box-has-border">
        <div class="text-center title">
          <span>费用信息</span>
          <el-button size="mini" @click="_ => {return}">编辑</el-button>
        </div>
        <div class="cost-line">
          <span class="cost-info">
            <span class="cost-label">商品总金额 :</span>
            <span class="cost-val">bbb</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">订单折扣 :</span>
            <span class="cost-val">bbb</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">商品折扣 :</span>
            <span class="cost-val">bbb</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">发票税额 :</span>
            <span class="cost-val">bbb</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">配送费用 :</span>
            <span class="cost-val">bbb</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">保价费用 :</span>
            <span class="cost-val">bbb</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">支付费用 :</span>
            <span class="cost-val">bbb</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">包装费用 :</span>
            <span class="cost-val">bbb</span>
          </span>
        </div>
        <div class="cost-line align-right">
          <span class="cost-info ">
            <span class="cost-label">
              = 订单总金额 : ￥
            </span>
            <span class="cost-val">
              ccccc 元
            </span>
          </span>
        </div>
        <div class="cost-line">
          <span class="cost-info">
            <span class="cost-label">已付款金额</span> :
            <span class="cost-val">bbb</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">使用余额</span> :
            <span class="cost-val">bbb</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">使用积分</span> :
            <span class="cost-val">bbb</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">使用优惠券</span> :
            <span class="cost-val">bbb</span>
          </span>
          <span class="cost-info">
            <span class="cost-label">促销金额</span> :
            <span class="cost-val">bbb</span>
          </span>
        </div>
        <div class="cost-line align-right">
          <span class="cost-info">
            <span class="cost-label">
              = 应付款金额 : ￥
            </span>
            <span class="cost-val">
              ccccc 元
            </span>
          </span>
        </div>

      </div>

      <div class="box-has-border" v-if="approvalFlows[0]">
        <div class="text-center title">
          <span>审批流程</span>
        </div>
        <procedure :Flows="approvalFlows" :statusFormatter="param => {return 0}"></procedure>
      </div>
      <div class="box-has-border">
        <div class="text-center title">
          <span>操作信息</span>
          <el-button size="mini" @click="_ => {return}">编辑</el-button>
        </div>
        <div class="form">
          <el-form label-width="180px">
            <el-form-item label="操作备注">
              <el-input
                type="textarea"
                :rows="5"
                placeholder="请输入内容"
                >
              </el-input>
            </el-form-item>
          </el-form>
          <!-- <el-button size="mini" @click="_ => {return}">确认</el-button> -->
        </div>
        <div class="operation">
          <span class="operation-part">
            <span class="instruction"> 当前可执行操作： </span>
            <el-button size="mini" @click="_ => {return}">付款</el-button>
            <el-button size="mini" @click="_ => {return}">设为未付款</el-button>
            <el-button size="mini" @click="_ => {return}">生成发货单</el-button>
            <el-button size="mini" @click="_ => {return}">取消发货单</el-button>
          </span>
          <span class="operation-part">
            <el-button size="mini" @click="_ => {return}">完成</el-button>
            <el-button size="mini" @click="_ => {return}">同意</el-button>
            <el-button size="mini" @click="_ => {return}">拒绝</el-button>
            <el-button size="mini" @click="_ => {return}">取消</el-button>
            <el-button size="mini" @click="_ => {return}">无效</el-button>
          </span>
        </div>
        <el-table border :data="chosenData.goods" style="width: 100%" header-row-class-name="header-row">
          <el-table-column align="center"
          prop="goodsName"
          label="操作者">
          </el-table-column>
          <el-table-column align="center"
          prop="goodsName"
          label="操作时间">
          </el-table-column>
          <el-table-column align="center"
          prop="goodsName"
          label="订单状态">
          </el-table-column>
          <el-table-column align="center"
          prop="goodsName"
          label="付款状态">
          </el-table-column>
          <el-table-column align="center"
          prop="goodsName"
          label="发货状态">
          </el-table-column>
          <el-table-column align="center"
          prop="goodsName"
          label="备注">
          </el-table-column>
        </el-table>
        <el-table border :data="chosenData.goods" style="width: 100%" header-row-class-name="header-row">
          <el-table-column align="center"
          prop="goodsName"
          label="审批人">
          </el-table-column>
          <el-table-column align="center"
          prop="goodsName"
          label="审批时间">
          </el-table-column>
          <el-table-column align="center"
          prop="goodsName"
          label="审批状态">
          </el-table-column>
          <el-table-column align="center"
          prop="goodsName"
          label="备注">
          </el-table-column>
          
        </el-table>
      </div>
    </section>
    <editDistributionMode v-if="pageState == 'editDistributionMode'"></editDistributionMode>
  </div>
</template>

<script>
// import pagination from '@/components/pagination/index'
import { getOrderDetail } from '@/views/order/orderData'
import procedure from '@/components/Procedure/index'
// ======引用组件======
import editDistributionMode from '@/views/order/orderList/edit/editDistributionMode'
import editCostInfo from '@/views/order/orderList/edit/editCostInfo'

export default {
  name: '',
  components: {
    procedure,
    editDistributionMode
  },
  mounted() {
    this.updateMainData()
  },
  data() {
    return {
      // 0 订单详情 1 编辑配送方式 2 编辑其他信息
      // 3 编辑收货人信息 4 编辑商品信息 5 编辑费用信息
      // pageState: 0,
      goodsInfoTable: [],
      chosenData: {},
      approvalFlows: []
    }
  },
  props: {
    // 0 订单详情 1 编辑配送方式 2 编辑其他信息
    // 3 编辑收货人信息 4 编辑商品信息 5 编辑费用信息
    pageState: String,
    entryIndex: Number, // 本组件主数据在父组件数组中的index
    tableData: Array
  },
  computed: {
    // 能否compose?
    // 本页主要渲染的数据
    // chosenData(){
    //   return this.chosenData;
    // }
  },
  methods: {
    nextEntry() {
      if (this.entryIndex >= this.tableData.length - 1) {
        this.$message({ message: '已经是最后一条了', type: 'warning' })
      } else{
        this.$emit('checkNextOrder')
      }
    },
    preEntry() {
      if (this.entryIndex <= 0) {
        this.$message({ message: '已经是第一条了', type: 'warning' })
      } else{
        this.$emit('checkPreOrder')
      }
    },
    updateMainData() {
      getOrderDetail(this, { id: this.tableData[this.entryIndex].id })
        .then(res => {
          this.chosenData = res.order
      })
    }
  },
  watch: {
    entryIndex() {
      this.updateMainData()
    }
  }
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
      // height: 100%;
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
        // margin-bottom: 25px;
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
          span.cost-info{
            margin-left: 35px;
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

      /*background-color: blue;*/
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
