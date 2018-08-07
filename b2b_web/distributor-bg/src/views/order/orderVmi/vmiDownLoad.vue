<template>
	<div class="check-goods">
		<div class="foot-btn" style="text-align: center;">
			<el-form ref="form" :model="formData" label-width="80px">
				<el-form-item label="订单编号:">
					<el-input style="width:80%;" clearable placeholder='置空默认为不限制，多个订单可用英文","隔开' size="mini" v-model="formData.orderIds"></el-input>
				</el-form-item>
				<el-form-item label="存货编码:">
					<el-input  style="width:80%;" clearable placeholder='置空默认为不限制，多个货品可用英文","隔开(ERP80码)'  size="mini" v-model="formData.itemCodes"></el-input>
				</el-form-item>
				<el-form-item label="下单时间:">
					<el-date-picker size="mini" v-model="formData.time" type="datetimerange" value-format="timestamp" 
						range-separator="至" start-placeholder="下单开始日期" end-placeholder="下单结束日期" >
					</el-date-picker>
				</el-form-item>
				<el-form-item label="导出字段:">
					<el-checkbox-group v-model="formData.fields">
						<el-checkbox v-for="field in fields" :label="field.id" :key="field.id">{{field.name}}</el-checkbox>
					</el-checkbox-group>
				</el-form-item>
				<el-form-item label="订单状态:">
					<el-checkbox-group v-model="formData.orderStatus">
						<el-checkbox v-for="orderStatu in orderStatus" :label="orderStatu.id" :key="orderStatu.id">{{orderStatu.name}}</el-checkbox>
					</el-checkbox-group>
				</el-form-item>
				<el-form-item label="付款状态:">
					<el-checkbox-group v-model="formData.payStatus">
						<el-checkbox v-for="payStatu in payStatus" :label="payStatu.id" :key="payStatu.id">{{payStatu.name}}</el-checkbox>
					</el-checkbox-group>
				</el-form-item>

			</el-form>
			<el-button class="mini-pulloff-btn"  @click="cancel">取消</el-button>
			<el-button class="mini-search-btn"  @click="submit">确认导出</el-button>
		</div>
	</div>
</template>
<script>
export default {
	name : 'vmiDownLoad',
	props: {
		
	},
	data() {
		return {
			formData:{
				orderIds:null,
				itemCodes:null,
				time:null,
				fields:[],
				orderStatus:[],
				payStatus:[]
			},
			fields:[{id:0, name:'存货编号', value:'itemCode'},
				{id:1, name:'存货名称', value:'itemName'},
				{id:2, name:'VMI数量', value:'itemVmiCount'},
				{id:3, name:'B2B订单号', value:'orderId'},
				{id:4, name:'ERP订单号', value:'orderErpNo'},
				{id:5, name:'下单时间', value:'orderCreateTime'},
				{id:6, name:'付款状态', value:'payStatus'},
				{id:7, name:'订单状态', value:'orderStatus'}],
			orderStatus:[{id:1, name:'待确认'},
				{id:2, name:'待发货'},
				{id:3, name:'部分发货'},
				{id:4, name:'待收货'},
				{id:5, name:'已关闭'},
				{id:6, name:'已完成'}],
			payStatus:[{id:1, name:'未付款'},
				{id:2, name:'部分付款'},
				{id:3, name:'已付款'},
				{id:4, name:'已退款'}]
		}
	},
	created() {

	},
	methods: {
		submit() { // 确定操作
			this.$emit('submit',this.formData);
		},
		cancel() { // 返回操作
			this.$emit('cancel')
		},
		handleFieldsChange(value){
		},
	},
	watch: {
		"formData.fields":function(){
			console.log(this.formData.fields);
		},
		"formData.orderStatus":function(){
			console.log(this.formData.orderStatus);
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
