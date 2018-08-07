<template>
	<div class="add-account">
		<header>
		<h4>退款操作</h4>
	  </header>
		<el-row style="padding: 40px 10px  0px 160px;">
			<el-col>
				<span style="color: #333">退款方式:</span>
			   <el-radio-group v-model="refundType">
					<el-radio :label="3">线下退款</el-radio>
					<el-radio :label="2">退回用户余额</el-radio>
				</el-radio-group>
			</el-col>
		</el-row>

			<el-row style="padding: 20px 0px 20px 235px;">
				<el-col>
					<el-select v-if="CardNoShow" v-model="companyCardNo" size="mini" clearable filterable style="width: 320px;">
						<el-option v-for="item in bAccountList" :key="item.id" :label="item.bankName + item.name + item.cardNo" :value="item.cardNo">
						</el-option>
					</el-select>
				</el-col>
			</el-row>

		<el-row>
			<el-col :span='24'>
				<el-form label-width="235px" >
					<el-form-item label="退款说明:" style="max-width:80%;">
						<el-input type="textarea" :rows="5" v-model="remark"></el-input>
					</el-form-item>
				</el-form>
			</el-col>
		</el-row>

		<el-row class="sureClass">
			<el-button size="mini" type="primary" @click="sure()" :loading="loading">确认</el-button>
			<el-button size="mini" type="primary" @click="cancel">取消</el-button>
		</el-row>
	   
	   
	</div>
</template>
<script>
import { parseTime } from '@/utils/index'
export default {
	name: 'refundOrderHandle',
	data() {
		return {
			refundType: '',
			companyCardNo: '',
			remark: '',
			pageInfo: {
				page: 1
			},
			loading: false,
			bAccountList: [],
			CardNoShow: false
		}
	},
	activated() {
		this.bankAccountList()
	},
	methods: {
		bankAccountList() {
			this.$api.get(this,'admin/u/po/bankAccount/list').then(res => {
				this.bAccountList = res.allBankAccounts
			})
		},
		sure() { //确认操作
			this.loading = true;
			this.$api.put(this,'admin/u/p/refund/status',{
				id: this.$route.query.id,
				status: 2,
				refundType: this.refundType,
				remark: this.remark,
				companyCardNo: this.companyCardNo
			}).then(res => {
				if(res.code == 500) {
					this.loading = false;
				}
				if (res.code == 0) {
					this.loading = false;
					this.$message({
						message: "成功退款",
						type: "success",
						duration: 3 * 1000,
						onClose: () => { }
					})
					this.$router.push({ name: "refundOrder" });
				}else {
					this.$message({
						message: "退款失败，请重新退款",
						type: "success",
						duration: 3 * 1000,
					})
				}
			})
		},
		
		cancel() { // 取消操作
			this.$router.go(-1)
		},
	},
	watch: {
		refundType(val) {
			if(val == 3) {
				this.CardNoShow = true
			}else if(val == 2) {
				this.CardNoShow = false
				this.companyCardNo = ''
			}
		}
	}
}

</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.add-account {
	background-color: white;
	min-height: 100%;
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
	.sureClass {
		text-align: center;
	}
	.el-table {
		margin: 20px 0;
	}
	.header-row {
		@include table-header-color;
	}
}

</style>
