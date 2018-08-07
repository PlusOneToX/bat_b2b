<template>
	<div class="add-goodslabel">
		 <header v-if="append">
		  <h4>添加商品标签</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
			返回列表
		  </el-button>
		</header>
		 <header v-if="looking">
		  <h4>查看商品标签</h4>
		  <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
			返回列表
		  </el-button>
		</header>

		<el-form label-width="150px" :rules="rules" ref="formData" :model="formData" v-loading="loading2">
			<el-form-item label="标签名称" prop="name">
				<el-input style="width:30%;" v-model="formData.name" placeholder="不超过6个字" maxlength="6"></el-input>
			</el-form-item>
			<el-form-item label="标签英文名称" prop="nameEn">
				<el-input style="width:30%;" v-model="formData.nameEn" placeholder="不超过12个字" maxlength="12"></el-input>
			</el-form-item>
			<el-form-item label="标签排序" prop="sort">
				<el-input style="width:30%;" v-model="formData.sort" placeholder="不超过100000" type="number" step="1" min="0" max="100000" @keyup.native="proving"> </el-input>
			</el-form-item>
			<el-form-item label="标签状态">
				<el-radio-group v-model="formData.openFlag">
					<el-radio :label="1">启用</el-radio>
					<el-radio :label="0">停用</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-button style="margin-left: 15%; margin-top:30px;" class="mini-search-btn" :loading="loading" @click="handleSubmit('formData')">保存</el-button>
			<el-button style="margin-bottom:30px;" size="mini" class="mini-search-btn" @click="handleCancel">返回</el-button>
		</el-form>

	</div>
</template>
<script>
import { isNumber } from "@/utils/validate";
import distributor from "@/views/goods/components/distributor"
export default {
	data() {
		return {
			loading: false,
			loading2: false,
			append: true, // 添加品类
			looking: false, // 查看品类
			formData: {
				name: '',
				nameEn: '',
				sort: 0,
				openFlag:1,
			},
			rules: {
				name: [{
					required: true,
					message: '请输入标签名称',
					trigger: 'blur'
				}],
				nameEn: [{
					required: true,
					message: '请输入标签英文名称',
					trigger: 'blur'
					}],
				sort: [{
					required: true,
					message: '请输入标签排序',
					trigger: 'blur'
					}],
				},
		}
	},
	components: { distributor },
	created() {
		if(this.$route.params.id != undefined) {
			this.loading2 = true
			this.append = false
			this.looking = true
			this.$http.getTagDetail(this, {
				id: this.$route.params.id
			}).then(res => {
				if (res.success) {
					this.formData = res.data
				}
				this.loading2 = false
			})
		}
	},
	methods: {
		proving(){
			this.formData.sort=this.formData.sort.replace(/[^\.\d]/g,'');
			this.formData.sort=this.formData.sort.replace('.','');
			if(Number(this.formData.sort) >100000){
				this.formData.sort = 100000
			}
		},
		handleSubmit(data) {
			this.$refs[data].validate(valid => {
				if(valid) {
					this.loading = true;
					if(this.$route.params.id) {
						this.$http.editTag(this, this.formData).then(res => {
							if(res.success) {
								this.$message({
									message: '修改成功',
									type: 'success',
									duration: 3 * 1000,
									onClose: () => { }
								})
								this.loading = false
								this.$router.go(-1)
							}else {
								this.loading = false
							}
						})
					} else {
						this.$http.addTag(this, this.formData).then(res => {
							if(res.success) {
								this.$message({
									message: '添加成功',
									type: 'success',
									duration: 3 * 1000,
									onClose: () => { }
								})
								this.loading = false
								this.$router.go(-1)
							}else {
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
			this.$router.go(-1)
		},
	}
}

</script>
<style lang="scss">
.add-goodslabel {
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
