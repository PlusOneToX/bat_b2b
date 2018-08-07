<template>
	<div class="productline-list">
		<header>
			<h4>品类列表</h4>
			<el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add">
				添加品类
			</el-button>
    </header>
		<div class="search">
			<div class="search-right">
				<el-input v-model.trim="pageInfo.content" @change="contentChange" @keyup.enter.native="onSearch()" placeholder="类目名称" clearable size="mini" class="box-search" ></el-input>
				<button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
			</div>
			<!-- <button class="mini-search-btn"  @click="syncCategory()">同步类目</button> -->
		</div>
		<el-table :data="tableData" @selection-change="handleSelectionChange" header-row-class-name="header-row" border style="text-align:center;" v-loading="loading" >
			<el-table-column align="center" label="品类名称" prop="name" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="品类英文名称" prop="nameEn" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="品类描述" prop="description" show-overflow-tooltip></el-table-column>
			<el-table-column  align="center" label="可视范围" prop="distributorScope" :formatter="formatDistributorType" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="品类状态" prop="openFlag" :formatter="formatStatus" show-overflow-tooltip></el-table-column>
			<el-table-column  align="center" label="操作" :min-width="120">
				<template slot-scope="scope">
					<el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
					<el-button class="mini-tableadd-btn" v-if="!scope.row.openFlag" @click="handleStart(scope.$index, scope.row)">启用</el-button>
					<el-button class="mini-freeze-btn" v-else @click="handleStop(scope.$index, scope.row)">停用</el-button>
					<el-button class="mini-delete-btn" v-if="!scope.row.openFlag" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
	</div>
</template>
<script>
import page from "@/components/pagination"
import api from '@/api/allApi'
export default {
	data() {
		return {
			tableHeight: 600,
			loading: false,
			batch: '',
			action: '',
			tableData: [],
			pageInfo: {
				page: 1,
				size: 10,
				content: ''
			},
			total: '',
			multipleSelection: [],
		}
	},
	created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
	},
	activated() {
		this.getTableData()
	},
	mounted() {
		this.getTableData()
	},
	components: {
		page
	},
	methods: {
		syncCategory() { // erp同步类目
			this.$api.put(this, api.syncCategory).then(res =>{ // 总条数
				if(res.code == 0) {
					this.$message({
						message: '同步成功',
						type: 'success',
						duration: 3 * 1000,
					})
					this.getTableData()
				}
			})
		},
		onSearch(){ // 搜索操作
			this.pageInfo.page = 1;
			this.getTableData()
    },
		contentChange (val){
			if(val === undefined || val === '' || val === null){
				this.onSearch()
			}
		},
		add() {
			this.$router.push({ name: 'addproductline'})
		},
		filter() {
			this.getTableData();
		},
		sizeChange(size) {
			this.pageInfo.size = size;
			this.getTableData()
		},
		currentChange(page) {
			this.pageInfo.page = page;
			this.getTableData()
		},
		getTableData() {
			this.loading = true;
			this.$http.getCategoryList(this, this.pageInfo).then(res => {
				if (res.success) {
					this.tableData = res.data.list;
					this.total = res.data.total
				}
				this.loading = false
			})
		},
		formatStatus(row, col, val) {
			if(val) {
				return '启用'
			} else {
				return '停用'
			}
		},
		formatDistributorType(row, col, val) {
			switch(val) {
				case 0:
					return '不指定';
					break;
				case 1:
					return '全部';
					break;
				case 2:
					return '分销商等级';
					break;
				case 3:
					return '指定分销商';
					break;
				case 4:
					return '指定销售部门';
					break;
				case 5:
					return '指定业务员';
					break;			
				default:
					return "-"

			}
		},
		handleSelectionChange(val) {
			this.multipleSelection = val
		},
		handleEdit(index, row) {
			this.$router.push({ name: 'editproductline', params: { id: row.id } })
		},
		handleDelete(index, row) {
			this.$confirm('确定删除此品类？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				// 删除品类
				this.$http.deleteCategory(this, {id: row.id }).then(res => {
					if (res.success) {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 3 * 1000,
						})
						this.getTableData()
					} else {
						this.$message.error(res.errMessage)
					}
				})
			})
		},
		handleStart(index,row){
		 this.$confirm('确定启用此品类？', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
			center: true
		  }).then(()=>{
				// 启用品类
				this.$http.categoryStatus(this, {id: row.id, openFlag: 1 }).then(res => {
					if (res.success) {
						this.getTableData()
					}
				})
		  })
		},
		handleStop(index,row){
		  this.$confirm('确定停用此品类？', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
			center: true
		  }).then(()=>{
				// 停用品类
				this.$http.categoryStatus(this, {id: row.id, openFlag: 0 }).then(res => {
					if (res.success) {
						this.getTableData()
					}
				})
		  })
		}
	}
}

</script>
<style lang="scss" scoped>
.productline-list {
	background-color: white;
	min-height: 100%;
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
	.action {
		margin: 20px 0;
	}
	.search {
		width: 70%;
	}
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
		margin-top: 8px;
		margin-right: 8px;
		margin-left: 0;
	}

	.search{
		width: 100%;
		border-bottom: 1px solid #dcdcdc;
		padding: 10px;
		overflow: hidden;
		.search-right {
			float: right;
			overflow: hidden;
			.box-search{
				width: 215px;
			}
			.box-btn {
				float: right;
				margin-left:5px;
				margin-right:10px;
			}
		}
	}
}

</style>
