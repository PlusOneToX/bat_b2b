<template>
	<div class="add-category">
		<header>
			<h4 v-if="looking">查看分销商分组</h4>
			<h4 v-if="append">添加分销商分组</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="addpuls">
				返回分销商分组列表
			</el-button>
		</header>
	  <el-col :span="20" :offset="1" v-loading="loading">
		<el-form label-width="120px" :model="formData" :rules="rules" ref="formData" label-position="right">
		  <el-row>
			  <el-col :span="18">
					<el-tooltip content="分组ID需同ERP中“客户分组ID”一致，如果不一致无法同步信息" placement="right">
						<el-form-item label="分销商分组ID" prop="erpGroupNo" >
							<el-input v-model="formData.erpGroupNo" maxlength="30"></el-input>
						</el-form-item>
					</el-tooltip>
			  </el-col>
		  </el-row>
		  <el-row>
			  <el-col :span="18">
					<el-form-item label="分销商分组名称" prop="name">
						<el-input v-model="formData.name" maxlength="40"></el-input>
					</el-form-item>
			  </el-col>
		  </el-row>
		  <el-row>
				<el-col :span="18">
					<el-form-item label="分销商分组描述" prop="no">
					<el-input type="textarea" :rows="5" v-model="formData.description" maxlength="200"></el-input>
			  </el-form-item>
		  </el-col>
			</el-row>

		  <el-col :span="2" :offset="9">
			<el-button class="mini-search-btn" @click="handleSubmit('formData')" :disabled="disabledShow">确定</el-button>
		  </el-col>
		</el-form>
	  </el-col>
	</div>
</template>
<script type="text/javascript">
import distributor from "@/views/goods/components/distributor"
import {postaddData} from '@/views/distributor/distributorData'
export default {
	name: 'distributorGroupingRedact',
	data() {
		return {
			formData: {
				name: '',
				erpGroupNo: '',
				description: '',
			},
			rules:{
				erpGroupNo: [
					{ required: true, message: '请输入分组ID', trigger: 'blur' }
				],
				name: [
					{ required: true, message: '请输入分组名称', trigger: 'blur' }
				],
			},
			append: false,
			looking: false,
      disabledShow: false,
      loading: false
		}
	},

	activated() {
		if(this.$route.query.id) {
			this.loading = true
			this.$http.distGroupDetail(this, {id: this.$route.query.id}).then(res => { 	
				this.formData = res.data
				res.success ? this.loading = false : this.looking = false
			})
			this.compile = true;
			this.looking = true;
			this.append = false;
			this.disabledShow = false
		}else {
			this.formData.name = "";
			this.formData.erpGroupNo = "";
			this.formData.description = "";
			this.formData.openFlag = 1
			this.compile = false;
			this.append = true;
			this.looking = false;
			this.disabledShow = false
		}
	},
   
	methods: {
		// ======== 操作 ========
		addpuls() { // 返回操作
			this.$router.push({name: 'distributorGrouping'})
		},
		
		handleSubmit(data) { // 分销商分组修改
			if(this.compile == true) {
				this.$refs[data].validate(valid => { // 修改
					if(valid) {
						this.disabledShow = true
						this.amend()
					}
				})
			}else {
				this.$refs[data].validate(valid => { // 添加
					if(valid) {
						this.disabledShow = true
						this.addAmend()
					}
				})
			}
		},

		// ======== 封装 ========
		amend() { // 修改，请求参数
			this.$http.editDistGroup(this, this.formData).then(res => {  	
				if(res.success) {	
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 3 * 1000,
						onClose: () => { }
					})
					this.$router.push({ name: 'distributorGrouping'})
				}
			})
		},

		addAmend() { // 添加，请求参数
			this.$http.addDistGroup(this, this.formData).then(res => {  	
				if(res.success) {
					this.$message({
						message: '分组添加成功',
						type: 'success',
						duration: 3 * 1000,
						onClose: () => { }
					})
					this.$router.push({ name: 'distributorGrouping'})
				}
			})
		},
	}
}

</script>

<style lang="scss" scoped rel="stylesheet/scss" scoped>
	.add-category {
		background-color: white;
		height: 90vh;
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
