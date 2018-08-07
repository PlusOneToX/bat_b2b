<template>
	<div class="add-category">
		<header>
			<h4 v-if="looking">查看价格等级</h4>
			<h4 v-if="append">添加价格等级</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="backpuls">
				返回价格等级列表
			</el-button>
		</header>
	  <el-col :span="20" :offset="1" v-loading="loading">
		<el-form label-width="120px" :model="formData" :rules="rules" ref="formData" label-position="right">
		  <el-row>
			  <el-col :span="18">
					<el-form-item label="价格等级名称" prop="name">
						<el-input v-model="formData.name" maxlength="40"></el-input>
					</el-form-item>
			  </el-col>
		  </el-row>
		  <el-row>
			  <el-col :span="18">
					<el-tooltip content="ERP价目表内对应的取价字段（必须一致），如不一致可能影响其他价格等级同步失败" placement="right">
						<el-form-item label="取价字段" prop="erpField">
							<el-input v-model="formData.erpField" maxlength="30"></el-input>
						</el-form-item>
					</el-tooltip>
			  </el-col>
		  </el-row>
		  <el-row>
			  <el-col :span="18">
					<el-tooltip content="取ERP上的价格顺序（序号为1为默认价格等级），不可随意修改，会导致价格不匹配。" placement="right">
						<el-form-item label="取价序号" prop="sort">
							<el-input v-model="formData.sort" type="number" @input="formData.sort=/^\d+\.?\d{0,2}$/.test(formData.sort)||formData.sort == '' ? formData.sort:Number(formData.sort.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0" max="1000000"></el-input>
						</el-form-item>
					</el-tooltip>
			  </el-col>
		  </el-row>
		   <el-row>
			  <el-col :span="18">
					<el-tooltip content="只能有一个建议零售价，如当前价格等级被设置为建议零售价，则会取消掉原建议零售价" placement="right">
						<el-form-item label="是否建议零售价" prop="retailFlag">
							<el-select v-model="formData.retailFlag" placeholder="请选择">
								<el-option key="1" label="是" value="1"></el-option>
								<el-option key="0" label="否" value="0"></el-option>
							</el-select>
						</el-form-item>
					</el-tooltip>
			  </el-col>
		  </el-row>
		  <el-row>
				<el-col :span="18">
					<el-form-item label="价格等级描述">
						<el-input type="textarea" :rows="5" v-model="formData.description"></el-input>
			  	</el-form-item>
		  	</el-col>
			</el-row>

		  <el-col :span="2" :offset="9">
			<el-button class="mini-search-btn" @click="handleSubmit('formData')" :disabled="disabledShow" >确定</el-button>
		  </el-col>

		</el-form>
	  </el-col>
	</div>
</template>

<script type="text/javascript">
import {gradeEdit} from "@/views/distributor/distributorData";
import {addGrade} from '@/views/distributor/distributorData'
export default {
	name: 'distributorareaRedact',
	data() {
		return {
			formData: {
				// id: '',
				// erpGradeId: '',  // erp分销商等级id
				erpField: '', // 取价字段
				sort: '', // 取价顺序
				name: '', // 分销商等级名称
				description: '', // 等级描述
				openFlag: 0,
				retailFlag:'0',  //是否建议零售价
			},
			rules: {
				name: [
					{required: true,message: '请输入价格等级名称', trigger: 'blur' }
				],
				erpField: [
					{required: true,message: '请输入取价字段', trigger: 'blur' }
				],
				sort: [
					{required: true,message: '请输入取价顺序', trigger: 'blur' }
				],
				retailFlag:[
					{required: true,message: '请选择建议零售价', trigger: 'change' }
				]
			},
			append: false,
			looking: false,
      disabledShow: false,
      loading: false
		}
	},
	activated() {
			if(this.$route.query.id) {
				this.details()
				this.compile = true;
				this.looking = true;
				this.append = false;
				this.disabledShow = false
			}else {
				this.formData.name = "";
				this.formData.erpField = '', // 取价字段
				this.formData.sort = '', // 取价顺序
				this.formData.description = "";
				this.append = true;
				this.looking = false;
				this.compile = false;
				this.disabledShow = false
			}
	},
	methods: {
		// ======== 操作 ========
		handleSubmit(data) { // 保存操作
			if(this.compile == true) {
				this.$refs[data].validate(valid => {
					if (valid) {
						this.disabledShow = true
						this.amend() // 修改
					}
	  		});
			}else {
				this.$refs[data].validate(valid => {
					if (valid) {
						this.disabledShow = true
						this.addAmend() // 添加
					}
				})
			}
		},

		backpuls() { // 返回操作
			this.$router.push({ name: 'distributorarea'})
		},

		// ======== 数据 ========
    details() { // 详情
      this.loading = true;
			this.$http.getGradeDetail(this, {id: this.$route.query.id}).then(res => {
				if (res.success) {
					console.log('-----',res.data);
					this.formData = res.data;
					this.formData.retailFlag=this.formData.retailFlag==1?'1':'0'
					res.success ? this.loading = false : this.looking = false
				}
			})
		},
		
		// ======== 封装 ========
		amend() { // 修改，请求参数
			this.formData.id = this.$route.query.id,
				// gradeEdit(this,this.formData).then(res => {
				// 	if (res.code == 0) {
				// 		this.$message({
				// 			message: "修改成功",
				// 			type: "success",
				// 			duration: 3 * 1000,
				// 			onClose: () => {}
				// 		});
				// 		this.$router.push({ name: 'distributorarea'})
				// 	}else {
        //     this.disabledShow = false
        //   }
				// });	
				this.formData.retailFlag=this.formData.retailFlag==1?1:0;
				this.$http.editGrade(this, this.formData).then(res => {
					if (res.success) {
						this.$message({
							message: "修改成功",
							type: "success",
							duration: 3 * 1000,
							onClose: () => {}
						});
						this.$router.push({ name: 'distributorarea'})
					} else {
						this.disabledShow = false
					}
				})
		},

		addAmend() { // 添加，请求参数
			// addGrade(this,this.formData).then(res => {
			// 	if(res.code == 0) {
			// 		this.$message({
			// 			message: '添加成功',
			// 			type: 'success',
			// 			duration: 3 * 1000,
			// 			onClose: () => { }
			// 		})
			// 		this.$router.push({ name: 'distributorarea'});
			// 	}else {
			// 		this.disabledShow = false
			// 	}
			// })
			this.$http.addGrade(this, this.formData).then(res => {
				if(res.success) {
					this.$message({
						message: '添加成功',
						type: 'success',
						duration: 3 * 1000,
						onClose: () => { }
					})
					this.$router.push({ name: 'distributorarea'});
				}else {
					this.disabledShow = false
				}
			})	
		}
	}
}
</script>


<style rel="stylesheet/scss" lang="scss" scoped>
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
