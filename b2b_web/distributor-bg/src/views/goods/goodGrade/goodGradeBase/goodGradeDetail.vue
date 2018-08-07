<template>
	<div class="add-productline">
		 <header v-if="append">
		  <h4>添加等级</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="back">
			返回等级列表
		  </el-button>
		</header>
		 <header v-if="looking">
		  <h4>查看等级</h4>
		  <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="back">
			返回等级列表
		  </el-button>
		</header>

		<el-form label-width="150px" :rules="rules" ref="formData" :model="formData" v-loading="loading2">
			<el-form-item label="等级名称" prop="gradeName">
				<el-input :disabled="disabled" style="width:50%;" v-model="formData.gradeName" placeholder="不超过15个字" maxlength="15"></el-input>
			</el-form-item>
			<el-form-item label="启用状态">
				<el-radio-group :disabled="disabled" style="margin-left:10px;" v-model="formData.status">
				<el-radio :label="1">启用</el-radio>
				<el-radio :label="0">停用</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="等级描述" prop="remark">
				<el-input :disabled="disabled" style="width:50%;"  type="textarea" :rows="4" v-model="formData.remark" placeholder="不超过200个字" maxlength="200"></el-input>
			</el-form-item>
			<el-button style="margin-left: 25%; margin-top:30px; margin-bottom:30px;" class="mini-search-btn" v-if="disabled" :loading="loading" @click="isDisabled">编辑</el-button>
			<el-button style="margin-left: 25%; margin-top:30px; margin-bottom:30px;" class="mini-search-btn" v-else :loading="loading" @click="handleSubmit('formData')">保存</el-button>
			<el-button style="margin-bottom:30px;" size="mini" class="mini-pulloff-btn" v-if="!disabled" @click="handleCancel">取消</el-button>
		</el-form>

	</div>
</template>
<script>
export default {
	data() {
		return {
			loading: false,
			loading2: false,
			append: true, // 添加等级
			looking: false, // 查看等级
			formData: {
				id: '',
				gradeName:'',
				remark: '',
				status:1,
			},
			rules: {
				gradeName: [{
					required: true,
					message: '请输入等级名称',
					trigger: 'blur'
				}],
			},
			disShow:false,
			disabled:false
		}
	},
	created() {
		this.getParam()
		

	},
	methods: {
		getParam(){
			this.disabled = false
			if(this.$route.params.id != undefined) {
				this.loading2 = true
				this.append = false;
				this.looking = true;
				this.disabled = true
				this.$api.get(this, 'admin/u/p/good/grade/ids', { ids: this.$route.params.id }).then(res => {
					if(res.code == 0 && res.goodGrades != undefined && res.goodGrades.length >0) {
						// this.formData = res
						this.formData = res.goodGrades[0]
					}
					res.code == 0 ? this.loading2 = false : this.loading2 = false
				})
			}
			else{
				this.disShow=true;
			}
		},
		isDisabled(){
			this.disabled = false
		},
		handleSubmit(data) {
			this.$refs[data].validate(valid => {
				if(valid) {
					this.loading = true;
					if(this.$route.params.id) {
						this.$api.put(this, 'admin/u/p/good/grade', this.formData).then(res => {
							if(res.code == 0) {
								this.$message({
									message: '修改成功',
									type: 'success',
									duration: 3 * 1000,
									onClose: () => { }
								})
								this.loading = false
								this.$router.push({ name: 'goodGradeBase' })
							}else {
								this.loading = false
							}
						})
					} else {
						this.$api.post(this, 'admin/u/p/good/grade', this.formData).then(res => {
							if(res.code == 0) {
								this.$message({
									message: '添加成功',
									type: 'success',
									duration: 3 * 1000,
									onClose: () => { }
								})
								this.loading = false
								this.disabled = true
								// this.$router.push({ name: 'goodGradeBase' })
							}else {
								this.disabled = false
								this.loading = false;
							}
						})
					}

				} else {
					this.loading = false
					return false
				}
			})
		},
		handleCancel() {  // 返回操作
			this.$confirm('您还未保存，是否要取消', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.getParam()
			})
			
			// this.$router.push({ name: 'goodGradeBase' })
		},
		back(){
			this.$router.push({ name: 'goodGradeBase' })
		}
	}
}

</script>
<style lang="scss">
.add-productline {
	background-color: white;
	min-height: 100%;
	// padding-top: 20px;
	.hint-msg {
		color: #ccc;
		font-size: 12px;
		line-height: 40px;
		margin-left: 10px;
	}
	.el-input__inner,
	.el-textarea__inner {
		background-color: white;
	}
	.header-row {
		@include table-header-color;
	}
	.distributor-table {
		margin-top: 20px;
	}
	.distributor-content {
		margin-top: 10px;
	}
	header {
		color: white;
		height: $lineHight;
		line-height: $lineHight;
		background-color: $lakeBlue;
		margin-bottom: 20px;
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

</style>
