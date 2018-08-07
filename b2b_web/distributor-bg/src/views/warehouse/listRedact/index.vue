<template>
	<div class="category-list">
		<header>
			<h4>编辑仓库</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
				返回仓库列表
			</el-button>
		</header>

		<div class="add-category">
			<el-form ref="formData" :model="formData" label-width="180px" class="el-form" :rules="rules">
				<el-tooltip content="仓库编号需要同ERP中对应仓库一致，如果不一致，无法同步ERP中库存信息" placement="right">
					<el-form-item label="仓库编码" prop="no">
						<el-input v-model="formData.no"></el-input>
					</el-form-item>
				</el-tooltip>

        <el-form-item label="仓库名称" prop="name">
          <el-input v-model="formData.name"></el-input>
        </el-form-item>

        <el-form-item label="所属区域" prop="areaId">
          <el-select  placeholder="请选择活动区域"
            v-model="formData.areaId">
            <el-option v-for="salesArea in salesAreas" :label="salesArea.name" :value="salesArea.id" :key="salesArea.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="是否集成"  prop="syncType">
					<el-tooltip content="仓库编号需要同ERP中对应仓库一致，如果不一致，无法同步ERP中库存信息" placement="right">
						<el-radio-group  v-model="formData.syncType">
							<el-radio :label="1">是</el-radio>
							<el-radio :label="0">否</el-radio>
						</el-radio-group>
					</el-tooltip>
        </el-form-item>
        <!-- <el-form-item label="是否平台仓" prop="isPlatform">
          <el-radio-group  v-model="form.isPlatform">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item> -->
        <el-form-item label="仓库描述" prop="remark">
          <el-input type="textarea"  v-model="formData.remark" :autosize="{ minRows: 5, maxRows: 8}"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button class="mini-search-btn" @click="onSubmit('form')">保存</el-button>
        </el-form-item>
        
      </el-form>
		</div>

	</div>
</template>

<script type="text/javascript">
export default {
	name: 'listRedact',
	activated() {
		if(this.$route.query.id) {
			this.details()
		}else {
			this.formData.no = ''
			this.formData.name = ''
			this.formData.areaId = ''
			this.formData.syncType = 1
			this.formData.remark = ''
		}
	},
	data() {
		return {
			formData: {
        name: '',
        no: '',
        areaId: '',
        isPlatform: '',
        syncType: '',
        remark: '',
        id: '',
			},
			salesAreas: [], // 所属区域arr
			rules: {
				name: [
          { required: true, message: '请输入仓库名称', trigger: 'blur' }
        ],
        no: [
          { required: true, message: '请输入仓库编码', trigger: 'blur' }
        ],
        areaId: [
          { required: true, message: '请输入所属区域', trigger: 'blur' }
        ],
        isPlatform: [
          { required: true, message: '请输入是否平台', trigger: 'blur' }
        ],
        syncType: [
          { required: true, message: '请输入是否集成', trigger: 'blur' }
        ], 
			}
		}
	},
	mounted() {
		this.getSaleareas(); // 仓库所属销售区域
	},
	methods: {
		details() { // 仓库详情
			this.$http.warehouseDetail(this, { id: this.$route.query.id }).then(res => {
				this.formData = res.data
			})
		},
		cancel() { // 返回操作
			this.$router.push({ name: 'warehouseList'})
		},
		onSubmit() { // 修改操作
			this.$confirm('确定要编辑仓库吗？','提示', {
				confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
			}).then(res => {
				this.formData.id = this.$route.query.id
				this.$http.editWarehouse(this,this.formData).then(res => {
					if(res.success) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 3 * 1000,
							onClose: () => {}
						})
						this.$router.push({ name: 'warehouseList'})
					}else {
						this.$message({ 
							message: res.msg,
							type: 'warning',
							duration: 3 * 1000,
						})
					}
				})
			})
		},
		getSaleareas(){ // 仓库所属销售区域
			this.$http.getSalesareaPoList(this, {page:1, size:10000, openFlag:1}).then(res => {this.salesAreas = res.data.list})
    },
	}
}
</script>


<style rel="stylesheet/scss" lang="scss">
	.main {
		background-color: #fff;
	}
	.category-list {
		background-color: white;
		height: 100vh;
		header {
			color: white;
			height: $lineHight;
			line-height: $lineHight;
			background-color: $lakeBlue;
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
		.tool-bar {
			padding: 20px;
		}
		.header-row {
			@include table-header-color;
		}
		.el-form{
      max-width: 600px;
      margin-top: 30px;
    }
	}
</style>
