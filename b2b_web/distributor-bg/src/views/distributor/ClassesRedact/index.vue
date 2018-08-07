<template>    
	<div class="add-category">
		<header>
			<h4 v-if="looking">查看分销商类别</h4>
			<h4 v-if="append">添加分销商类别</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel()"> 返回分销商类别列表 </el-button>
		</header>
	  <el-col :span="20" :offset="1" v-loading="loading">
		<el-form label-width="110px" :model="formData" :rules="rules" ref="formData" label-position="right">
		  <el-row>
			  <el-col :span="15">
				<el-tooltip content="类别ID需同ERP中“客户类别ID”一致，如果不一致，无法同步信息。" placement="right">
					<el-form-item label="类别ID" prop="erpCategoryNo">
						<el-input v-model="formData.erpCategoryNo" maxlength="30"></el-input>
					</el-form-item>
				</el-tooltip>
			  </el-col>
		  </el-row>

		  <el-row>
			  <el-col :span="15">
					<el-form-item label="类别名称" prop="name">
						<el-input v-model="formData.name" maxlength="40"></el-input>
					</el-form-item>
			  </el-col>
		  </el-row>

	    <el-row>
        <el-col :span="15">
					<el-form-item label="默认订单类型" prop="orderTypeId">
						<el-tooltip content="该类客户默认下单的订单类型" placement="right">
							<el-select v-model="formData.orderTypeId" placeholder="请选择">
								<el-option v-for="item in orderTypes" :key="item.id" :label="item.name" :value="item.id"></el-option>
							</el-select>
						</el-tooltip>
					</el-form-item>
					</el-col>
		    </el-row>

        <el-row>
          <el-col :span="15">
            <el-form-item label="类别描述" prop="no">
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
export default {
	name: 'distributorClassesRedact',
	data() {
		return {
      loading: false,
			formData: {
				name: '',
				erpCategoryNo: '',
				description: '',
				orderTypeId: '',
				openFlag: 0
			},
			rules:{
				erpCategoryNo: [
					{ required: true, message: '请输入类别ID', trigger: 'blur' }
				],
				name: [
					{ required: true, message: '请输入类别名称', trigger: 'blur' }
				],
				orderTypeId: [
					{ required: true, message: '请选择默认订单类型', trigger: 'change' }
				],
			},
			append: false,
			looking: false,
			orderTypes: [],
			disabledShow: false
		}
	},
	activated() {
			if(this.$route.query.id) {
        this.orderType(); // 默认订单类型
        this.loading = true;
				this.$http.distCategoryDetail(this, { id: this.$route.query.id }).then(res => {
					if (res.success) {
						this.formData = res.data
						this.loading = false
					} else {
						this.looking = false
					}
				})
				this.compile = true;
				this.looking = true;
				this.append = false;
				this.disabledShow = false
			}else {
				this.orderType();
				this.formData.name = "";
				this.formData.erpCategoryNo = "";
				this.formData.orderTypeId = ''
				this.formData.description = "";
				this.append = true;
				this.looking = false;
				this.compile = false;
				this.disabledShow = false
			}
	},
	methods: { 
		// ======== 操作 ========
		cancel() { // 返回分销商类别
			this.$router.push({name: 'distributorClasses'})
		},
		
		handleSubmit(data) { // 分销商类别修改
			if(this.compile == true) {
				this.$refs[data].validate(valid => {
					if(valid) {
						this.disabledShow = true
						this.amend()
					}
				})
			}else {
				this.$refs[data].validate(valid => {
					if(valid) {
						this.disabledShow = true
						this.addAmend()
					}
				})
			}
		},
		// ======== 数据 ========
		orderType() { // 管理员(订单类型模块) - 订单类型列表
			this.$http.orderTypeList(this, {page:1, size:1000}).then((res) => {
				if (res.success) {
					this.orderTypes = res.data.list;
				}
			});
		},
		// ======== 封装 ========
		amend() { // 修改，请求参数
			this.$http.editDistCategory(this, this.formData).then(res => {
				if (res.success) {
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 3 * 1000,
						onClose: () => { }
					})
					this.$router.push({ name: 'distributorClasses'});
				}
			})
		},
		
		addAmend() { // 添加，请求参数
			this.$http.addDistCategory(this, this.formData).then(res => {
				if (res.success) {
					this.$message({
						message: '添加成功',
						type: 'success',
						duration: 3 * 1000,
						onClose: () => { }
					})
					this.$router.push({ name: 'distributorClasses'});
				}
			})
		}
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
