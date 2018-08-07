<template>
  <div class="category-list">
    <header>
			<h4 v-if="looking">查看销售区域</h4>
			<h4 v-if="append">添加销售区域</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="backpuls">
				返回销售区域列表
			</el-button>
		</header>

    <div class="function" >
      <el-col :span="20" :offset="2" v-loading="loading">
				<el-form :model="formData" label-width="110px" :rules="rules" ref="formData" label-position="right" >
					<!-- <el-col :span="15">
						<el-tooltip content="销售区域ID需同ERP中对应的销售区域ID一致，如果不一致，无法同步信息。" placement="right">
							<el-form-item label="销售区域ID" >
								<el-input v-model="formData.salesAreaErpId" maxlength="30"></el-input>
							</el-form-item>
						</el-tooltip>
					</el-col> -->

					<el-col :span="15">
						<el-form-item label="销售区域名称" prop="name">
							<el-input v-model="formData.name" maxlength="20"></el-input>
						</el-form-item>
					</el-col>

					<el-col :span="15">
						<el-form-item label="销售区域描述">
							<el-input type="textarea" :rows="5" v-model="formData.description" size="mini"></el-input>
						</el-form-item>
					</el-col>
					
					<el-col :span="2" :offset="9">
						<el-button class="mini-search-btn" @click="handleSubmit('formData')" size="mini">保存</el-button>
					</el-col>
				</el-form>
      </el-col>
    </div>
  </div>
</template>

<script type="text/javascript">
export default {
	name: 'distributorgraderedact',
	data() {
		return {
      loading: false,
			formData: {
				id: '',
				name: '', // 区域名称
				description: '', // 区域描述
				// salesAreaErpId: '', // 销售区域ID
			},
			rules: {
				name: [{ required: true, message: "请输入销售区域名称", trigger: "blur" }]
			},
			append: false,
			looking: false
		}
	},
	activated() {
		if(this.$route.query.id) {
			this.details()
			this.compile = true;
			this.looking = true;
			this.append = false;
		}else {
			this.formData.name = ""
			this.formData.description = ""
			this.formData.openFlag = 0
			this.compile = false;
			this.append = true;
			this.looking = false;
		}
	},
	methods: {
		handleSubmit(data) { // 保存按钮
			if(this.compile == true) {
				this.$refs[data].validate(valid => {
					this.amend() // 修改
				});
			}else {
				this.$refs[data].validate(valid => {
					this.addAmend() // 添加
				})
			}
		},
    details() { // 销售区域详情
      this.loading = true;
			this.$http.salesareaDetail(this, {id: this.$route.query.id}).then(res => {
				if (res.success) {
					this.formData = res.data
					this.loading = false 
				} else {
					this.looking = false
				}
			})
		},
		amend() { // 修改分销商
			this.formData.id = this.$route.query.id,
			this.$http.editSalesarea(this, this.formData).then(res => {		
				if (res.success) {
					this.$message({
						message: "修改成功",
						type: "success",
						duration: 3 * 1000,
						onClose: () => { }
					});
					this.loading = false
					this.$router.push({ name: 'distributorlist'})
				}
			});
		},
		addAmend() { // 添加分销商
			this.$http.addSalesarea(this, this.formData).then(res => {			
				if(res.success) {
					this.$message({
						message: '添加成功',
						type: 'success',
						duration: 3 * 1000,
						onClose: () => {
						}
					})
					this.loading = false
					this.$router.push({ name: 'distributorlist'});
				}
			})
		},
		
		backpuls() { // 返回操作
			this.$router.push({ name: 'distributorlist'})
		},
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.main {
		background-color: #fff;
	}
	.category-list {
		height: 90vh;
		background-color: white;
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
		.function {
			margin-top: 36px;
		}
	}
</style>
