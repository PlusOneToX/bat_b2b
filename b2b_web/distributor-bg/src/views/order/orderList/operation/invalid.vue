<template>
  <div class="invalid-order">
    <header>
      <h4>取消订单</h4>
      <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="onCancel">
        返回订单详情
      </el-button>
    </header>
         
    <el-form :model="formData" status-icon :rules="rules" label-width='20%' label-position="right" ref="formData" class="el-form">
      <!-- <el-form-item label="操作备注" prop="operationRemark">
        <el-input type="textarea" v-model="formData.operationRemark" :autosize="{ minRows: 5, maxRows: 8}"></el-input>
      </el-form-item> -->
      <el-form-item label="取消原因" prop="reason"> <!--  v-if="reaOfCancel == 0" -->
        <el-input type="textarea" v-model="formData.reason" :autosize="{ minRows: 5, maxRows: 8}" maxlength="1000" ></el-input>
      </el-form-item>
      <el-form-item label="退款方式" prop="refundWay" v-if="refWayPayWay == 0">
        <el-radio-group v-model="formData.refundWay" style="margin-top: 10px;">
          
          <!-- <el-radio :label="1" v-if="accPayWay == 0">退回支付账户</el-radio> <br/>
          <div class="place-holder" v-if="accPayWay == 0">退回原支付账户</div> <br/> -->

          <el-radio :label="2" v-if="userPayWay == 0">退回用户余额</el-radio> <br/>
          <div class="place-holder" v-if="userPayWay == 0">退回用户余额中</div> <br/>

          <!-- <el-radio :label="3" v-if="elsePayWay == 0">其他退款方式</el-radio> <br/>
          <div class="place-holder" v-if="elsePayWay == 0">线下退款或其他退款方式</div> <br/> -->

        </el-radio-group>
      </el-form-item>
      <el-form-item label="退款说明"  v-if="refundPayWay == 0">
        <el-input type="textarea" v-model="formData.refundRemark" :autosize="{ minRows: 5, maxRows: 8}" maxlength="1000"></el-input>
      </el-form-item>

      <el-form-item>
        <el-col :span="2" :offset="8">
          <el-button class="mini-search-btn" @click="onConfirm('formData')" :loading="loading" >确定</el-button>
        </el-col>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import eventBus from '@/views/order/eventBus'
import { operateOrder } from '@/views/order/orderData'
import { confirmCreator } from '@/views/order/orderUtils'

export default {
  name: 'invalid',
  data() {
    return {
      formData: {
        operationRemark: '', // 操作备注
        reason: '', // 取消原因
        refundRemark: '', // 退款说明
        refundWay: '' // 退款方式
      },
      cancelTheReason: {
        id: '',
        refundWay: '',
        reason: '', // 取消原因
        refundRemark: '', // 退款说明
        refundWay: 2
      },
      refundWay: 1,
      salesAreas: [],
      rules: {
        reason: [{ required: true, message: '请输入取消原因', trigger: 'blur' }],
        refundWay: [{ required: true, message: '请选择退款方式', trigger: 'change' }]
      },
      loading: false
    }
  },
  computed: {
    /**
     * orderStatus: 订单状态            1: 待确认 2: 待发货 3: 部分发货 4: 待收货 5:已关闭 6: 已完成
     * payWay     : 支付方式            1: 支付宝 2: 微信 3: 区间结算 4: 银行转账 5: 余额支付
     * splitFlag  : 订单是否拆分         1: 拆分 0 未拆分
     * payStatus  : 付款状态            1: 未付款 2: 部分付款 3: 已付款
     */
    reaOfCancel() { // 取消原因
      if (this.$route.query.payStatus == 1 && this.$route.query.orderStatus == 2) {
        return 0 // ==== 付款状态 （未付款）&& 订单状态判断操作权限（代发货）====
      } else if (this.$route.query.payWay == 3) {
        return 0 // ==== 支付方式（区间结算） ====
      }
    },
    refWayPayWay() { // 退款方式
      if (this.$route.query.payStatus == 2 || this.$route.query.payStatus == 3 && this.$route.query.payWay == 1) {
        return 0 // ==== 付款状态（部分付款）|| 付款状态（已付款）&& 支付方式（支付宝） ====
      } else if (this.$route.query.payStatus == 2 || this.$route.query.payStatus == 3 && this.$route.query.payWay == 2) {
        return 0 // ==== 付款状态（部分付款）|| 付款状态（已付款）&& 支付方式（微信） ====
      } else if (this.$route.query.payStatus == 2 || this.$route.query.payStatus == 3 && this.$route.query.payWay == 4) {
        return 0 // ==== 付款状态（部分付款）|| 付款状态（已付款）&& 支付方式（银行转账） ====
      } else if (this.$route.query.payStatus == 2 || this.$route.query.payStatus == 3 && this.$route.query.payWay == 5) {
        return 0 // ==== 付款状态（部分付款）|| 付款状态（已付款）&& 支付方式（余额支付） ====
      } else if (this.$route.query.payStatus == 2 || this.$route.query.payStatus == 3 && this.$route.query.payWay == 6) {
        return 0 // ==== 付款状态（部分付款）|| 付款状态（已付款）&& 支付方式（网银支付）
      }
    },
    accPayWay() { // 支付账户
      if (this.$route.query.payWay == 1 && this.$route.query.splitFlag == 0) {
        return 0 // ==== 支付方式（支付宝） && 订单（未拆分） ====
      }
    },
    userPayWay() { // 用户余额
      if (this.$route.query.payStatus == 2 || this.$route.query.payStatus == 3) {
        return 0
      }
      // if(this.$route.query.payWay == 5 && this.$route.query.payStatus == 2 || this.$route.query.payWay == 5 && this.$route.query.payStatus == 3 || this.$route.query.payWay == 2 && this.$route.query.payStatus == 2 || this.$route.query.payWay == 2 && this.$route.query.payStatus == 3) {
      //   return 0 // ==== 支付方式（支付宝）|| (微信)====
      // }
    },
    elsePayWay() { // 其他退款方式
      if (this.$route.query.payWay == 1 && this.$route.query.payStatus == 2 || this.$route.query.payWay == 1 && this.$route.query.payStatus == 3 ||
      this.$route.query.payWay == 2 && this.$route.query.payStatus == 2 || this.$route.query.payWay == 2 && this.$route.query.payStatus == 3 ||
      this.$route.query.payWay == 4 && this.$route.query.payStatus == 2 || this.$route.query.payWay == 4 && this.$route.query.payStatus == 3) {
        return 0 // ==== 支付方式（支付宝）&& 部分付款 || 支付宝 && 已付款 ====
        // ==== 支付方式（微信）&& 部分付款 || 微信 && 已付款 ====
        // ==== 支付方式（银行转账）&& 部分付款 || 银行转账 && 已付款 ====
      }
    },
    refundPayWay() { // 退款说明
      if (this.$route.query.payStatus == 3 && this.$route.query.orderStatus == 2 && this.$route.query.payWay == 1) {
        return 0 // ==== 付款状态（部分付款）|| 付款状态（已付款）&& 支付方式（支付宝） ====
      } else if (this.$route.query.payStatus == 2 || this.$route.query.payStatus == 3 && this.$route.query.payWay == 2) {
        return 0 // ==== 付款状态（部分付款）|| 付款状态（已付款）&& 支付方式（微信） ====
      } else if (this.$route.query.payStatus == 2 || this.$route.query.payStatus == 3 && this.$route.query.payWay == 4) {
        return 0 // ==== 付款状态（部分付款）|| 付款状态（已付款）&& 支付方式（银行转账） ====
      } if(this.$route.query.payStatus == 2 || this.$route.query.payStatus == 3 && this.$route.query.payWay == 5) {
        return 0 // ==== 付款状态（部分付款）|| 付款状态（已付款）&& 支付方式（余额支付） ====
      }
    }
  },
  methods: {
    onConfirm(formData) { // 确定操作
      this.$refs[formData].validate((valid) => {
        if (valid) {
          this.$confirm('取消后该订单将作废，是否继续？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(_ => {
            this.loading = true
            this.cancelTheReason = {
              id: this.$route.query.id,
              refundWay: this.formData.refundWay,
              reason: this.formData.reason, // 取消原因
              refundRemark: this.formData.refundRemark, // 退款说明
              refundWay: 2
            }
            if (this.$route.query.payStatus == 1) { // ..未付款不传refundWay值
              delete this.cancelTheReason.refundWay
            }
            this.$api.put(this, 'admin/u/p/order/cancel', this.cancelTheReason).then(res => {
              if (res.code) {
                this.loading = false
              }
              if (res.code == 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 3 * 1000,
                  onClose: () => { }
                })
                const orderid = this.$route.query.id
                this.$router.go(-1)
              }
            })
          }).catch(_ => {
            this.$message({
              type: 'info',
              message: '已取消操作'
            })
          })
        }
      })
    },
  	onCancel() { // 返回操作
      this.$router.go(-1)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .invalid-order{
    header {
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
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
    }
    .btn-add{
      float: right;
      padding:5px;
      margin-top:7px;
      margin-right:8px;
    }
    .el-form-item{
      padding: 15px 0;
    }
    background-color: white;
    padding-bottom: 100px;
    position: relative;
    .el-form{
      width: 900px;
      margin-top: 30px;
    }
    .place-holder {
      color: #ccc;
      font-size: 12px;
      margin: 2px 0 15px 24px;
    }
  }
</style>
