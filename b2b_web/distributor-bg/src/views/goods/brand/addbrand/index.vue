<template>
	<div class="add-brand">
		<header v-if="append">
		  <h4>添加商品品牌</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="backpuls">
			返回添加品牌
		  </el-button>
		</header>
		<header v-if="looking">
		  <h4>查看商品品牌</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="backpuls">
			返回商品品牌列表
		  </el-button>
		</header>

		<el-form label-width="150px" :rules="rules" ref="formData" :model="formData" v-loading="loading2">
			<el-form-item label="品牌名称" prop="name">
				<el-input style="width:30%;" v-model="formData.name" placeholder="不超过40个字" maxlength="40"></el-input>
			</el-form-item>
      <el-form-item label="品牌英文名称" prop="nameEn">
        <el-input style="width:30%;" v-model="formData.nameEn" placeholder="不超过80个字" maxlength="80"></el-input>
      </el-form-item>
			<el-form-item label="品牌网址" prop="logo">
				<el-input style="width:50%;" v-model="formData.logo" placeholder="不超过200个字" maxlength="200"></el-input>
			</el-form-item>
			<el-form-item label="品牌描述" prop="description">
				<el-input style="width:60%;" type="textarea" :rows="3" v-model="formData.description" placeholder="不超过200个字" maxlength="200"></el-input>
			</el-form-item>
			<el-form-item label="排序" prop="sort">
				<template>
				<span class="hint-msg"><el-input style="width:10%;" type="number" v-model="formData.sort" step="1" min="0" @keyup.native="proving" max="100000"></el-input> 数字越小越靠前</span>
				</template>
			</el-form-item>
			<!-- 可视范围组件 -->
			<goodsDistributor 
				v-if="disShow" 
				:isDefault="false" 
				ref="distributor" 
				:type="formData.distributorScope" 
				:gIds="formData.scalePriceIds" 
				:dIds="formData.distributorIds"
				:gdIds="formData.distributorGroupIds"
				:departmentIds="formData.departmentIds" 
				:adminIds="formData.adminIds" 
				></goodsDistributor>
			<el-button style="margin-left: 45%;" class="mini-search-btn" :loading="loading" @click="handleSubmit('formData')">保存</el-button>
			<el-button size="mini" @click="handleCancel">返回</el-button>
		</el-form>
	</div>
</template>
<script>
import { isNumber } from "@/utils/validate";
import goodsDistributor from "@/views/goods/components/goodsDistributor"
import addimg from '@/views/goods/brand/components/addImg'
export default {
	data() {
		const validateNumber = (rule, value, callback) => {
			if(!isNumber(value)) {
				callback(new Error('只能输入数字'))
			} else {
				callback()
			}
		}
		return {
			loading: false,
			loading2: false,
			append: true, // 添加商品品牌
			looking: false, // 查看商品牌品详情
			formData: {
				name: '',
        nameEn: '',
				logo: '',
				description: '',
				sort: 0,
				openFlag: 1,
				distributorScope: 1,
				distributorScopeNo: 0,
				scalePriceIds: [],
				distributorGroupIds: [],
				distributorIds: [],
				departmentIds: [],
				adminIds: []
			},
			rules: {
				name: [{required: true, message: '请输入品牌名称', trigger: 'blur'}],
				nameEn: [{required: true, message: '请输入品牌英文名称', trigger: 'blur'}],
				distributorIds: [{required: true, message: '请至少指定一个分销商', trigger: 'change'}],
				sort: [{
					validator: validateNumber,
					trigger: 'blur'
				}]
			},
			distributorList: [],
			distributorData: [],
			disShow:false,
		}
	},
	components: { addimg, goodsDistributor },
	created() {
		if(this.$route.params.id) {
			this.loading2 = true;
			this.looking = true;
			this.append = false;
			// 获取品牌详情
			this.$http.getBrandDetail(this, { id: this.$route.params.id }).then(res => {
				this.formData = res.data
				this.formData.scalePriceIds = res.data.scalePriceIds == undefined ? [] : res.data.scalePriceIds
				this.formData.distributorIds = res.data.distributors == undefined ? [] : res.data.distributors
				// 指定分销商
				if (res.data.distributors) {
					this.formData.distributorIds = []
					res.data.distributors.forEach(item => {
						this.formData.distributorIds.push({
							id: item.distributorId,
							companyName: item.companyName,
							name: item.name
						})
					})
					this.formData.distributors = undefined
				}
				// 指定分销商分组
				if (res.data.distributorGroups) {
					this.formData.distributorGroupIds = []
					res.data.distributorGroups.forEach(item => {
						this.formData.distributorGroupIds.push({
							id: item.distributorGroupId,
							erpGroupNo: item.erpGroupNo,
							name: item.name
						})
					})
					this.formData.distributorGroups = undefined
				}
				this.disShow=true;
				this.loading2 = false
			})
		}
		else
		{
			this.disShow=true;
		}
	},
	methods: {
		proving(){
			this.formData.sort=this.formData.sort.replace(/[^\.\d]/g,'');
			this.formData.sort=this.formData.sort.replace('.','');
			if(this.formData.sort>100000){
				this.formData.sort = 100000
			}
		},
		// 返回
		backpuls() {
			this.$router.go(-1)
		},
		// 提交
		handleSubmit(data) {
			// 获取组件数据
			this.formData.distributorScope = this.$refs.distributor.formData.distributorType; //..可视范围
      this.formData.distributorScopeNo = 0; //..不可视范围
      this.formData.scalePriceIds = this.$refs.distributor.formData.gradeIds; //..指定分销商等级
      this.formData.departmentIds = this.$refs.distributor.formData.DepartmentIds // 指定部门
      this.formData.adminIds = this.$refs.distributor.formData.adminIds // 指定业务员
      this.formData.distributorIds = this.$refs.distributor.formData.distributorIds; //..指定分销商用户ID
			this.formData.distributorGroupIds = this.$refs.distributor.formData.distributorGroupIds; //..指定分销商分组用户ID
			this.$refs[data].validate(valid => {  // 验证必填
				if(valid) {
					this.loading = true;
					if(this.formData.distributorScope === 1) {
						this.formData.scalePriceIds = []
						this.formData.distributorIds = []
						this.formData.departmentIds = []
						this.formData.adminIds = []
						this.formData.distributorGroupIds = []
					}
					if((this.formData.distributorScope === 2 && (this.formData.scalePriceIds === undefined || this.formData.scalePriceIds === '' || this.formData.scalePriceIds.length === 0)) ||
					(this.formData.distributorScopeNo === 2 && (this.formData.scalePriceNoIds === undefined || this.formData.scalePriceNoIds === '' || this.formData.scalePriceIds.length === 0))) {
						this.$message.error("请先指定分销商等级范围")
						this.loading = false
						return
					} else if (this.formData.distributorScope === 2 && this.formData.scalePriceIds.length > 0) {
						this.formData.distributorIds = []
						this.formData.departmentIds = []
						this.formData.adminIds = []
						this.formData.distributorGroupIds = []
					}
					if((this.formData.distributorScope === 3 && (this.formData.distributorIds === undefined || this.formData.distributorIds === ''|| this.formData.distributorIds.length === 0)) ||
					(this.formData.distributorScopeNo === 3 && (this.formData.distributorNoIds === undefined || this.formData.distributorNoIds === '' || this.formData.distributorNoIds.length === 0))) {
						this.$message.error("请先指定分销商范围")
						this.loading = false
						return
					} else if (this.formData.distributorScope === 3 && this.formData.distributorIds.length > 0) {
						let disArr = []
						this.formData.distributorIds.forEach(item => {
							disArr.push(item.id)
						})
						this.formData.distributorIds = disArr
						this.formData.scalePriceIds = []
						this.formData.departmentIds = []
						this.formData.adminIds = []
						this.formData.distributorGroupIds = []
					}
					if((this.formData.distributorScope === 4 && (this.formData.departmentIds === undefined || this.formData.departmentIds === '' || this.formData.departmentIds.length === 0)) ||
					(this.formData.distributorScopeNo === 4 && (this.formData.departmentNoIds === undefined || this.formData.departmentNoIds === '' || this.formData.departmentNoIds.length === 0))) {
						this.$message.error("请先指定销售部门")
						this.loading = false
						return
					} else if (this.formData.distributorScope === 4 && this.formData.departmentIds.length > 0) {
						this.formData.distributorIds = []
						this.formData.scalePriceIds = []
						this.formData.adminIds = []
						this.formData.distributorGroupIds = []
					}
					if((this.formData.distributorScope === 5 && (this.formData.adminIds === undefined || this.formData.adminIds === '' || this.formData.adminIds.length === 0)) ||
					(this.formData.distributorScopeNo === 5 && (this.formData.adminNoIds === undefined || this.formData.adminNoIds === '' || this.formData.adminNoIds.length === 0))){
						this.$message.error("请先指定业务员")
						this.loading = false
						return
					} else if (this.formData.distributorScope === 5 && this.formData.adminIds.length > 0) {
						this.formData.scalePriceIds = []
						this.formData.distributorIds = []
						this.formData.departmentIds = []
						this.formData.distributorGroupIds = []
					}
					if((this.formData.distributorScope === 6 && (this.formData.distributorGroupIds === undefined || this.formData.distributorGroupIds === ''|| this.formData.distributorGroupIds.length === 0)) ||
					(this.formData.distributorScopeNo === 6 && (this.formData.distributorGroupNoIds === undefined || this.formData.distributorGroupNoIds === '' || this.formData.distributorGroupNoIds.length === 0))) {
						this.$message.error("请先指定分销商分组范围")
						this.loading = false
						return
					} else if (this.formData.distributorScope === 6 && this.formData.distributorGroupIds.length > 0) {
						let groupArr = []
						this.formData.distributorGroupIds.forEach(item => {
							groupArr.push(item.id)
						})
						this.formData.distributorGroupIds = groupArr
						this.formData.scalePriceIds = []
						this.formData.distributorIds = []
						this.formData.departmentIds = []
						this.formData.adminIds = []
					}
					if(this.$route.params.id) {
						this.$http.editBrand(this, this.formData).then(res => {
							if (res.success) {
								this.$message({
									message: '修改成功',
									type: 'success',
									duration: 3 * 1000,
									onClose: () => { }
								})
								this.$router.push({ name: 'brandlist' })
							}
							this.loading = false
						})
					} else {
						this.$http.addBrand(this, this.formData).then(res => {
							if (res.success) {
								this.$message({
									message: '添加成功',
									type: 'success',
									duration: 3 * 1000,
									onClose: () => { }
								})
								this.$router.push({ name: 'brandlist' })
							}
							this.loading = false
						})
					}

				} else {
					this.loading = false
					return false
				}
			})
		},
		handleCancel() {
			this.$router.push({ name: 'brandlist' })
		}
	}
}

</script>
<style lang="scss">
.add-brand {
	background-color: white;
	padding-bottom: 20px;
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
	.hint-msg {
		color: #ccc;
		font-size: 12px;
		line-height: 40px;
	}
	.dis-item {
		margin-top: 15px;
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
}

</style>
