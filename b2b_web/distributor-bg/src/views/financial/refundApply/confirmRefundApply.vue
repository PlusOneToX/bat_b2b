<template>
	<div class="check-goods">
		<div class="foot-btn" style="text-align: center;">
			<el-form ref="form" :model="formData" label-width="80px">
				<el-form-item label="退款方式:">
					<el-radio-group v-model="formData.refundType" size="mini">
						<el-radio :label="2">退回用户余额</el-radio>
						<el-radio :label="3">其他退款方式</el-radio>
						<el-radio :label="4">拒绝退款</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="退款说明:">
					<el-input style="width:80%;margin-bottom: 20px;margin-top: 10px;" type="textarea" :rows="3" placeholder='请输入退款说明' v-model="formData.remark"></el-input>
				</el-form-item>
			</el-form>
			<el-button class="mini-pulloff-btn"  @click="cancel">取消</el-button>
			<el-button class="mini-search-btn"  @click="submit">确认</el-button>
		</div>
	</div>
</template>
<script>
export default {
	name : 'confirmRefundApply',
	props: ['id', 'applyType'],
	data() {
		return {
			formData: {
				refundType: 2,
				remark: ''
			}
		}
	},
	methods: {
		submit() { // 确定操作
			this.formData.id = this.id
			if (parseInt(this.formData.refundType) === 4) {
				// 拒绝退款
				this.formData.applyStatus = 2
				this.formData.refundType = undefined
			} else {
				this.formData.applyStatus = 1
				this.formData.refundType = parseInt(this.formData.refundType) 
			}
				this.formData.applyType = parseInt(this.applyType)
			this.$emit('submit',this.formData)
		},
		cancel() { // 返回操作
			this.$emit('cancel')
		}
	}
}

</script>
<style lang="scss">
.check-goods {
	background-color: white;
	.foot-btn {
		margin-top:10px;
		
	}
	element.style {
		margin-left: 0px;
	}
	.el-form-item__content {
		line-height: 40px;
		position: relative;
		font-size: 14px;
		text-align: left;
	}
	.el-form-item {
		margin-bottom: 0px;
	}
	.el-checkbox {
		margin-left: 0px;
		margin-right: 10px;
	}
	
}
</style>
