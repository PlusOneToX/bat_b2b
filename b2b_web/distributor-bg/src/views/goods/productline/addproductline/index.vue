<template>
	<div class="add-productline">
		 <header v-if="append">
		  <h4>添加品类</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
			返回品类列表
		  </el-button>
		</header>
		 <header v-if="looking">
		  <h4>查看品类</h4>
		  <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
			返回品类列表
		  </el-button>
		</header>
		<el-form label-width="150px" :rules="rules" ref="formData" :model="formData" v-loading="loading2">
			<el-form-item label="品类名称" prop="name">
				<el-input style="width:50%;" v-model="formData.name" placeholder="不超过40个字" maxlength="40"></el-input>
			</el-form-item>
      <el-form-item label="品类英文名称" prop="nameEn">
        <el-input style="width:50%;" v-model="formData.nameEn" placeholder="不超过80个字" maxlength="80"></el-input>
      </el-form-item>
			<el-form-item label="品类描述" prop="description">
				<el-input style="width:50%;"  type="textarea" :rows="4" v-model="formData.description" placeholder="不超过200个字" maxlength="200"></el-input>
			</el-form-item>
			<!-- 可视范围 -->
			<distributor v-show="false" :type="formData.distributorScope" :gIds="formData.scalePriceIds" :dIds="formData.distributorIds" v-if="disShow"  @change="getChange"></distributor>
			<el-button style="margin-left: 25%; margin-top:30px;" class="mini-search-btn" :loading="loading" @click="handleSubmit('formData')">保存</el-button>
			<el-button style="margin-bottom:30px;" size="mini" class="mini-search-btn" @click="handleCancel">返回</el-button>
		</el-form>

	</div>
</template>
<script>
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
				openFlag: 1,
				description: '',
				distributorScope: 0,
				distributorScopeNo: 0,
				scalePriceIds: [],
				distributorIds: [],
				sort:0
			},
			rules: {
				name: [{
					required: true,
					message: '请输入品类名称',
					trigger: 'blur'
				}],
        nameEn: [{
          required: true,
          message: '请输入品类英文名称',
          trigger: 'blur'
        }]
			},
			distributorList: [],
			distributorData: [],
			disShow:false,
		}
	},
	components: { distributor },
	created() {
		if(this.$route.params.id != undefined) {
			this.loading2 = true
			this.append = false;
			this.looking = true;
			this.$http.getCategoryDetail(this, {id: this.$route.params.id }).then(res => {
				if (res.success) {
					this.formData = res.data
					this.formData.scalePriceIds = res.data.scalePriceIds == undefined ? [] : res.data.scalePriceIds.splitnum(',')
					this.formData.distributorIds = res.data.distributorIds == undefined ? [] : res.data.distributorIds.splitnum(',')
					this.disShow=true;
					this.loading2 = false
				}
			})
		}
		else{
			this.disShow=true;
		}
	},
	methods: {
		handleSubmit(data) {
			if(this.distributorData.length > 0) {
				let ary=[]
				this.distributorData.forEach(item => {
					ary.push(item.id)
				})
				this.formData.distributorIds=ary;
			}
			this.$refs[data].validate(valid => {
				if(valid) {
					this.loading = true;
					// 可视分销商等级id
					this.formData.scalePriceIds = this.formData.scalePriceIds instanceof Array ? this.formData.scalePriceIds.join(',') : this.formData.scalePriceIds
					// 不可视分销商等级id
					this.formData.scalePriceNoIds = this.formData.scalePriceNoIds instanceof Array ? this.formData.scalePriceNoIds.join(',') : this.formData.scalePriceNoIds
					// 可视分销商id
					this.formData.distributorIds = this.formData.distributorIds instanceof Array ? this.formData.distributorIds.join(","):this.formData.distributorIds
					// 不可视分销商id
					this.formData.distributorNoIds = this.formData.distributorNoIds instanceof Array ? this.formData.distributorNoIds.join(","):this.formData.distributorNoIds
					// 可视销售部门id
					this.formData.departmentIds = this.formData.departmentIds instanceof Array ? this.formData.departmentIds.join(","):this.formData.departmentIds
					// 不可视销售部门id
					this.formData.departmentNoIds = this.formData.departmentNoIds instanceof Array ? this.formData.departmentNoIds.join(","):this.formData.departmentNoIds
					// 可视业务员id
					this.formData.adminIds = this.formData.adminIds instanceof Array ? this.formData.adminIds.join(","):this.formData.adminIds
					// 不可视业务员id
					this.formData.adminNoIds = this.formData.adminNoIds instanceof Array ? this.formData.adminNoIds.join(","):this.formData.adminNoIds
					
					if((this.formData.distributorScope === 2 && (this.formData.scalePriceIds === undefined || this.formData.scalePriceIds === '')) ||
					(this.formData.distributorScopeNo === 2 && (this.formData.scalePriceNoIds === undefined || this.formData.scalePriceNoIds === ''))) {
						this.$message.error("请先指定分销商等级范围")
						this.loading = false
						return
					}
					if((this.formData.distributorScope === 3 && (this.formData.distributorIds === undefined || this.formData.distributorIds === '')) ||
					(this.formData.distributorScopeNo === 3 && (this.formData.distributorNoIds === undefined || this.formData.distributorNoIds === ''))) {
						this.$message.error("请先指定分销商范围")
						this.loading = false
						return
					}
					if((this.formData.distributorScope === 4 && (this.formData.departmentIds === undefined || this.formData.departmentIds === '')) ||
					(this.formData.distributorScopeNo === 4 && (this.formData.departmentNoIds === undefined || this.formData.departmentNoIds === ''))) {
						this.$message.error("请先指定销售部门")
						this.loading = false
						return
					}
					if((this.formData.distributorScope === 5 && (this.formData.adminIds === undefined || this.formData.adminIds === '')) ||
					(this.formData.distributorScopeNo === 5 && (this.formData.adminNoIds === undefined || this.formData.adminNoIds === ''))){
						this.$message.error("请先指定业务员")
						this.loading = false
						return
					}

					if(this.$route.params.id) {
						// 修改品类
						this.$http.editCategory(this, this.formData).then(res => {
							if (res.success) {
								this.$message({
									message: '修改成功',
									type: 'success',
									duration: 3 * 1000,
									onClose: () => { }
								})
								this.$router.push({ name: 'productlinelist' })
							}
						})
						this.loading = false;
					} else {
						// 添加品类
						this.$http.addCategory(this, this.formData).then(res => {
							if (res.success) {
								this.$message({
									message: '添加成功',
									type: 'success',
									duration: 3 * 1000,
									onClose: () => { }
								})
								this.$router.push({ name: 'productlinelist' })
							}
						})
						this.loading = false;
					}
				} else {
					this.loading = false
					return false
				}
			})
		},
		handleCancel() {  // 返回操作
			this.$router.push({ name: 'productlinelist' })
		},
		getChange(val){  // 监听子组件
			this.formData.distributorScope=val.distributorScope;
			this.formData.scalePriceIds=val.scalePriceIds;
			this.formData.distributorIds=val.distributorIds;
			this.distributorData=val.distributorData;
		}
	}
}

</script>
<style lang="scss">
.add-productline {
	background-color: white;
	min-height: 100%;
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
