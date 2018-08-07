<template>
	<div>
		<section class="order-detail">
			<header>
				<span>收款单详情</span>
				<el-button v-if="checkVoucherG" icon="el-icon-d-arrow-left" class="mini-back-btn btn-home" @click="orderCancel()"> 返回订单详情 </el-button>
				<el-button v-else icon="el-icon-d-arrow-left" class="mini-back-btn btn-home" @click="cancel()"> 返回收款单列表 </el-button>
			</header>
			<div class="add-sync">
				<div class="add-sync-sync">
					<el-button :loading="syncLoading" v-if="(chosenData.voucherErpNo === undefined || chosenData.voucherErpNo === '' || chosenData.voucherErpNo === null)" class="mini-search-btn" @click="syncDisERP()" >同步ERP</el-button>
				</div>
			</div>
			<div class="box-has-border">
				<div class="text-center title" style="margin-top: 0px">
					<span>基本信息</span>
				</div>
				<div class="half-width">
					<el-form ref="chosenData" label-width="280px" v-if="chosenData">
						<el-form-item label="收款单号:"> {{chosenData.id}} </el-form-item>
						<el-form-item label="ERP收款单号:"> {{chosenData.voucherErpNo}} </el-form-item>
						<el-form-item label="业务单号:"> {{chosenData.businessId}} </el-form-item>
						<el-form-item label="分销商用户名:"> {{chosenData.distributorName}} </el-form-item>
						<el-form-item label="付款人:"> {{chosenData.collectPayer}} </el-form-item>
					</el-form>
				</div>
				<div class="half-width">
					<el-form ref="chosenData" label-width="280px">
						<el-form-item label="支付状态:"> {{orderStatusRefund(chosenData.voucherStatus)}} </el-form-item>
						<el-form-item label="支付时间:"> {{timeFormatter(chosenData.updateTime)}} </el-form-item>
						<el-form-item label="支付币别:"> {{chosenData.currencyType}} </el-form-item>
					</el-form>
				</div>
			</div>

			<div class="box-has-border">
				<div class="text-center title">
					<span>支付信息</span>
				</div>
				<div class="half-width">
					<el-form ref="chosenData" label-width="280px">
						<el-form-item label="支付方式:"> {{orderStatusWay(chosenData.payWay)}} </el-form-item>
					</el-form>
				</div>
				<div class="half-width" >
					<el-form ref="chosenData" label-width="280px">
						<el-form-item label="支付金额:"> {{chosenData.amount}} </el-form-item>
					</el-form>
				</div>
			</div>

			<!-- <div class="box-has-border">
				<div class="box-has-border">
					<div class="text-center title">
						<span>操作信息</span>
					</div>
					<div class="operation" style="margin: 20px 0;">
						<span class="instruction" style="margin-left: 48px;margin-top: 20px;"> 
							当前可执行操作：
						</span>
						<span class="operation-part" >
							<el-button class="mini-search-btn" @click="sureReceipt" :loading="loading">确认收款</el-button>
						</span>
					</div>
					<div>
						<el-form label-width="180px">
							<el-form-item label="操作备注:" style="width: 80%;margin-top: 30px;">
								<el-input v-model="remark" type="textarea" :rows="5" placeholder="请输入内容"> </el-input>
							</el-form-item>
						</el-form>
					</div>
				</div>
			</div> -->

			<div class="box-has-border">
				<div class="box-has-border">
					<div class="text-center title">
						<span>操作记录</span>
					</div>
					<!-- 订单操作日志 -->
					<el-table :data="chsoseLogData" border style="width: 100%" header-row-class-name="header-row" >
						<el-table-column align="center" label="操作人" prop="operator" > </el-table-column>
						<el-table-column align="center" label="操作" prop="operateType" > </el-table-column>
						<el-table-column align="center" label="操作时间" prop="operateTime" :formatter="formatTime" > </el-table-column>
						<el-table-column align="center" label="操作备注" prop="operateDes" > </el-table-column>
					</el-table>
				</div>
			</div>

		</section>

	</div>
</template>

<script>
import { orderStatusRefund, orderStatusCurrency, orderStatusWay } from '@/views/financial/financialUtils'
import { parseTime } from '@/utils/index';
import { timeFormat } from "@/utils/timeFormat";

export default {
	name: 'receiptDetail',
	computed: {
		checkVoucherG() {
			return this.$route.query.voucherG
		}
	},
  activated() {
    this.getReceiptData() // 收款单详情数据
  },
  data() {
    return {
			chosenData: {},
			chsoseLogData: [],
			remark: '',
			loading: false,
			syncLoading:false
			
    }
  },
  methods: {
		syncDisERP() { // 收款单同步到ERP
			this.syncLoading = true
		  this.$http.syncVouchersToERP(this, {ids: [this.chosenData.id] }).then(res => {		
				if(res.success) {
					this.$message({
						message: '成功同步到ERP',
						type: 'success',
						duration: 3 * 1000,
					})
				}else {
					this.$message({
						message: res.msg,
						type: 'error',
						duration: 3 * 1000,
					})
				}
				res.success ? this.syncLoading = false : this.syncLoading = false
				this.getReceiptData();
			})
		},
    getReceiptData() { // 收款单详情数据
			let daInfo = {}
			if (this.$route.query.id) {
				daInfo = {
					id: this.$route.query.id
				}
			} else {
				daInfo = {
					businessId: this.$route.query.orderId
				}
			}
			this.$http.voucherDetail(this, daInfo).then(res => {	
				if (res.success) {
					this.chosenData = res.data
					let orderId = parseInt(res.data.businessId)
					if (orderId) {
						this.$http.voucherLogList(this, {page:1,size:100,orderId: orderId}).then(res => {
							if (res.success) {
								this.chsoseLogData = res.data.list
							}
						})
					}
				} else {
					this.$message({
						message: '查看收款单失败',
						type: 'error',
						duration: 3 * 1000,
					})
					this.orderCancel()
				}
			})
    },
    cancel() { // 返回收款单列表操作
      this.$router.push({ name: 'receipt' })
		},
		orderCancel() { // 返回订单详情操作
			// this.$router.push({ name: 'orderDetail', query: {
			// 	orderId: this.$route.query.orderId,
			// 	orderG: true,
			// 	recopetDia: true,
			// 	type: 1
			// }})
			this.$router.go(-1)
		},
	sureReceipt() { // 确认操作
      this.$confirm('确定将此订单同步到ERP，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
		  
		  this.loading = true
			// 暂不使用
        // this.$api.get(this,'admin/u/p/order/syncOrder', {id: this.chosenData.id}).then(res => {
        // 	if(res.code == 0) {
        // 		this.$message({
        // 			message: "同步到ERP成功",
        // 			type: "success",
        // 			duration: 3 * 1000,
        // 			onClose: () => {
				// 				this.loading = false
        // 				location.reload() // 刷新页面数据
        // 			}
        // 		});
        // 	}else{ 
				// 		this.loading = false
				// 	}
        // })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消同步'
        })
      })
    },
    timeFormatter(rawData) { // 时间格式化
      this.timeFormatter = parseTime
      return this.timeFormatter(rawData)
		},
    orderStatusRefund(stateCode) { // 基本信息  > 支付状态的switch语句
      this.orderStatusRefund = orderStatusRefund
      return this.orderStatusRefund(stateCode)
    },
    orderStatusCurrency(stateCode) { // 基本信息  > 支付币别的switch语句
      this.orderStatusCurrency = orderStatusCurrency
      return this.orderStatusCurrency(stateCode)
    },
    orderStatusWay(stateCode) { // 基本信息  > 支付方式的switch语句
      this.orderStatusWay = orderStatusWay
      return this.orderStatusWay(stateCode)
    },
		formatTime(row, col, val) { // 表格时间格式化
      return timeFormat(val);
		},
		forstatus(row) { // 日志操作记录
      switch(row.operationType) {
        case 1:
          return '创建';
        case 2: 
          return '确认收款';
        case 3:
          return '同步ERP';
      }
    },
		handleStatus(row) { // 操作
      switch(row.status) {
        case 1:
          return '创建';
        case 2: 
          return '确认收款';
        case 3:
          return '同步ERP';
      }
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
		.order-detail{
			height: 100%;
			min-width: 1050px;
			background-color: white;
			padding-bottom: 30px;
			.el-form div.el-form-item{
				margin-bottom: 5px;
			}
			header{
				color: white;
				height: $lineHight;
				line-height: $lineHight;
				background-color: $lakeBlue;
				span{
					margin-left: 30px;
				}
				.btn-home{
					text-align: center;
					float: right;
					padding:5px;
					margin-top:7px;
					margin-right:8px;
				}
			}
			.add-sync {
				overflow: hidden;
				.add-sync-sync {
					float: right;
					margin: 10px;
				}
			}
			form.el-form{
				margin-top: 0;
			}
			.half-width{
				width: 50%;
				box-sizing: border-box;
				float: left;
			}
			.title {
				text-align: center;
				font-size: 18px;
				margin: 20px 0;
			}
			.box-btn-top{
				padding: 20px;
			}
			.box-has-border{
				overflow: hidden;
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
</style>
