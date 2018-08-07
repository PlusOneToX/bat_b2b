<template>
  <div class="organization-add-edit">
	  <header>
			<h4 v-if="looking">查看销售组织</h4>
			<h4 v-if="append">添加销售组织</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
				返回销售组织列表
			</el-button>
		</header>

		<div v-loading="loading">
			<el-form ref="form" :model="form" label-width="150px" class="el-form1" :rules="rules">
				<el-form-item label="销售组织ID：" prop="erpOrganizationId">
					<el-tooltip content="销售组织ID需同ERP中的销售组织ID一致，如果不一致无法同步信息" placement="right">
						<el-input v-model.trim="form.erpOrganizationId" maxlength="25" placeholder="不超过25个字"></el-input>
					</el-tooltip>
				</el-form-item>
				<el-form-item label="销售组织名称：" prop="name">
					<el-input v-model.trim="form.name" maxlength="50" placeholder="不超过50个字"></el-input>
				</el-form-item>
				<el-form-item label="销售组织描述：" prop="description">
					<el-input :rows="4" v-model.trim="form.description" type="textarea" maxlength="100" placeholder="不超过100个字"></el-input>
				</el-form-item>
			</el-form>

			<div class="clearfix footbtn">
					<el-button style="margin-left:25%;" class="mini-search-btn" @click="submitForm('form')" >保存</el-button>
					<el-button size="mini" @click="cancel">返回</el-button>
			</div>
		</div>
	
  </div>
</template>

<script>
  export default {
	data() {
	  return {
			loading: false,
			form: {
				erpOrganizationId: '',
				name: '',
				description: '',
			},
			rules: {
				erpOrganizationId: [{
					required: true,
					message: '请输入销售组织ID',
					trigger: 'blur'
				}],
				name: [{
					required: true,
					message: '请输入销售组织名称',
					trigger: 'blur'
				}]
			},
			rowData: {},
			append: false,
			looking: false
	  }
	},
	mounted() {
		this.getParams();
	},
	activated() {
		this.getParams();
	},
	methods: {
	  submitForm(form){
		  this.$refs[form].validate((valid) => {
			if (valid) {
				this.handleSave();
			} else {
				return false;
			}
		});
	  },
	  handleSave(){
		let isEdit = false
		if(this.form != undefined){
			this.rowData.erpOrganizationId = this.form.erpOrganizationId
			this.rowData.name = this.form.name
			this.rowData.description = this.form.description
			if(this.rowData.id != undefined){
				isEdit = true
			}
		}
		if(isEdit){
			this.$http.editOrganization(this, this.rowData).then(res => {
				if (res.success) {
					this.$message({
						message: '保存成功',
						type: 'success',
						duration: 3 * 1000,
						onClose: () => { }
					})
					this.cancel()
				}
			})
		}else{
			this.$http.addOrganization(this, this.rowData).then(res => {
				if (res.success) {
					this.$message({
						message: '保存成功',
						type: 'success',
						duration: 3 * 1000,
						onClose: () => { }
					})
					this.cancel()
				}
			})
		}
	  },
	  cancel(){
		this.$router.push({name:'saleOrganization'})
	  },
	  getParams(){
			if(this.$route.params.id!= undefined){
				this.looking = true
				this.append = false
				this.$http.organizationDetail(this, { id: this.$route.params.id }).then(res => {
					if (res.success) {
						this.rowData = res.data
						if(this.rowData != undefined){
							this.form.erpOrganizationId = this.rowData.erpOrganizationId;
							this.form.name = this.rowData.name;
							this.form.description = this.rowData.description;
						}else{
							this.rowData.id = undefined;
							this.form.erpOrganizationId = "";
							this.form.name = "";
							this.form.description = "";
						}
					}
				})
			}else{
				this.append = true
				this.looking = false
				this.rowData.id = undefined;
				this.form.erpOrganizationId = "";
				this.form.name = "";
				this.form.description = "";
			}
	  }
	}
  }
</script>

<style rel="stylesheet/scss" lang="scss">
.organization-add-edit {
  background-color: white;
	header {
			color: white;
			height: $lineHight;
			line-height: $lineHight;
			background-color: $lakeBlue;
			margin-bottom: 3%;
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
	}
	.el-form1{
		width: 700px;
		margin-top: 30px;
		padding-left: 20px;
	}
	.el-form2{
		width: 400px;
		margin-top: 30px;
		padding-left: 20px;
	}

	.file{ 
		position: absolute;
		width: 290px;
		height: 40px;
		top: 0;
		opacity: 0;
		padding: 0px;
		filter: alpha(opacity=0);
		cursor: pointer
	}
	.file-name{
		top: 0;
		position: relative;
		display: inline-block;
		overflow: hidden;
	}

	.el-icon-document::before {
		content: url("/src/assets/images/attachment.png");
	}
	.footbtn {
		margin-top: 40px;
		padding-bottom: 20px;
	}
	.header-row {
		@include table-header-color;
	}
</style>

