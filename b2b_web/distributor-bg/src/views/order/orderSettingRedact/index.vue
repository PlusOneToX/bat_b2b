<template>    
	<div class="add-category">
		<header>
			<h4 v-if="looking">查看订单类型</h4>
			<h4 v-if="append">添加订单类型</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
				返回订单类型列表
			</el-button>
		</header>
	  <el-col :span="20" :offset="1" v-loading="loading">
			<el-form label-width="110px" :model="formData" :rules="rules" ref="formData" label-position="left">
				<el-row>
					<el-col :span="18">
						<el-tooltip content="类别ID需同ERP中“订单类型ID”一致，如果不一致无法同步信息" placement="right">
							<el-form-item label="订单类型ID" prop="erpType">
								<el-input v-model="formData.erpType" :disabled="editableShow"></el-input>
							</el-form-item>
						</el-tooltip>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="18">
						<el-form-item label="订单类型名称" prop="name">
							<el-input v-model="formData.name" :disabled="editableShow"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="18">
						<el-form-item label="订单类型" prop="specialFlag">
							<el-select v-model="formData.specialFlag" placeholder="请选择">
								<el-option key="1" label="普通" value="1"></el-option>
								<el-option key="2" label="mto" value="2"></el-option>
								<el-option key="3" label="现款" value="3"></el-option>
								<el-option key="4" label="定制" value="4"></el-option>
								<el-option key="5" label="直运" value="5"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="18">
						<el-form-item label="默认仓库" prop="warehouseId">
							<!-- <el-input v-model="formData.warehouseId" :disabled="editableShow"></el-input> -->
							<el-select v-model="formData.warehouseId" placeholder="请选择">
								<el-option v-for="item in warehousePoListV" :key="item.id" :label="item.name" :value="item.id"></el-option>
								
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="18">
						<el-form-item label="订单类型描述">
							<el-input type="textarea" :rows="5" v-model="formData.desc" :disabled="editableShow"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="18">
						<el-form-item label="是否可编辑">
							<el-radio-group v-model="formData.editable" :disabled="editableShow">
								<el-radio :label="1">是</el-radio>
								<el-radio :label="0">否</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
				</el-row>
				<el-col :span="2" :offset="9">
					<el-button v-if="butShow" class="mini-search-btn" @click="handleSubmit('formData')" :disabled="editableShow">保存</el-button>
				</el-col>
			</el-form>
	  </el-col>
	</div>
</template>
<script type="text/javascript">
export default {
	name: 'orderSettingRedact',
	activated() {
		if(this.$route.query.row) {
			this.loading = true
		    this.$http.orderTypeDetail(this, {id: this.$route.query.row.id}).then(res => {	
				this.formData = res.data;
				//this.formData.specialFlag = this.formData.specialFlag==1?this.formData.specialFlag.toString():'';
				if(this.formData.specialFlag == 1){
					this.formData.specialFlag = '普通'
				}
				if(this.formData.specialFlag == 2){
					this.formData.specialFlag = 'mto'
				}
				if(this.formData.specialFlag == 3){
					this.formData.specialFlag = '现款'
				}
				if(this.formData.specialFlag == 4){
					this.formData.specialFlag = '定制'
				}
				if(this.formData.specialFlag == 5){
					this.formData.specialFlag = '直运'
				}
				res.data.editable == 0 ? this.editableShow = true : this.editableShow = false
				res.data.editable == 0 ? this.butShow = false : this.butShow = true
				res.success ? this.loading = false : this.loading = false
			})
			this.compile = true;
			this.looking = true;
			this.append = false;
		}else {
			this.formData.id = undefined
			this.formData.erpType = "";
			this.formData.name = "";
			this.formData.desc = "";
			this.formData.editable = 1
			this.append = true;
			this.looking = false;
			this.compile = false;
			this.editableShow = false
			this.butShow = true
		}
  },
  
    data() {
		return {
			loading: false,
			append: false,
			looking: false,
			salesAreas: [],
			formData: {
				erpType: '',
				name: '',
				desc: '',
				openFlag: 1,
				editable: 1,
				specialFlag:'',
				warehouseId:'',
			},
			rules:{
				erpType: [
					{ required: true, message: '请输入订单类型ID', trigger: 'blur' }
				],
				name: [
					{ required: true, message: '请输入订单类型名称', trigger: 'blur' }
				],
				specialFlag:[{ required: true, message: '请选择订单类型', trigger: 'change' }],
				warehouseId:[{ required: true, message: '请输入默认仓库ID', trigger: 'blur' }]
			},
			editableShow: false,
			butShow:true,
			warehousePoListV:[],
		}
		
	},
	mounted(){
        this.warehousePoListFun();
    },
	methods: {
    // ======== 操作 ========
		cancel() { // 返回操作
			this.$router.push({name: 'orderSetting'})
		},
		// 获取仓库列表
		warehousePoListFun(){
            this.$http.warehousePoList(this,{page:1,size:500}).then(res=>{
              console.log('仓库列表：',res.data);
			  if(res.success){
				  this.warehousePoListV=res.data.list;
			  }
			})
		},
		
		handleSubmit(data) { // 确定操作
			if(this.compile == true) {
				this.$refs[data].validate(valid => {
				  this.$http.updateOrderType(this, this.formData).then(res => {		
						if(res.success) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 3 * 1000,
								onClose: () => { }
							})
							this.$router.push({ name: 'orderSetting'});
						}
					})
				})
			}else {
				this.$refs[data].validate(valid => {
				  this.$http.addWxOrderType(this, this.formData).then(res => {				
							if(res.success) {
								this.$message({
									message: '添加成功',
									type: 'success',
									duration: 3 * 1000,
									onClose: () => { }
								})
								this.$router.push({ name: 'orderSetting'});
							}
					})
				})
			}
		}
	}
}

</script>
<style lang="scss" scoped rel="stylesheet/scss">
.add-category {
	background-color: white;
	height: 100vh;
	header {
		color: white;
		height: $lineHight;
		line-height: $lineHight;
		background-color: $lakeBlue;
		margin-bottom: 3%;
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
}
</style>
