<template>
	<div class="restriction-list">
		<!-- <page-header title="限购规则列表" button="添加限购规则" icon="el-icon-plus" @handle-click="add"></page-header> -->
		<header>
			<h4>限购规则列表</h4>
			<el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add()"> 添加限购规则 </el-button>
		</header>

		<div class="nav">
			<div class="Fheader">

				<el-select v-model="ruleStatus" placeholder="启用状态" size="mini" @change="ruleStatusChange" clearable>
					<el-option label="启用" :value="1"></el-option>
					<el-option label="停用" :value="2"></el-option>
				</el-select>

				<!-- <el-select v-model="batch" size="mini" @change="handleBatchChange">
					<el-option label="批量停用" :value="1"></el-option>
					<el-option label="批量启用" :value="2"></el-option>
					<el-option label="批量删除" :value="3"></el-option>
				</el-select> -->

				<!-- <el-select v-model="action" size="mini">
					<el-option label="导入excel" :value="1"></el-option>
					<el-option label="导出excel" :value="2"></el-option>
				</el-select> -->

				<div class="search">
          <el-input v-model.trim="content" @keyup.enter.native="filter()" placeholder="限购名称" size="mini" class="box-search"></el-input>
          <el-button class="mini-search-btn" @click.prevent="filter()">搜索</el-button>
				</div>
			</div>
		</div>
		<el-table :data="tableData" @selection-change="handleSelectionChange" header-row-class-name="header-row" border style="text-align:center;" v-loading="loading" :height="tableHeight">
			<!-- <el-table-column type="selection" width="55"> </el-table-column> -->
			<el-table-column align="center" type="index" :min-width="50"> </el-table-column>
			<el-table-column align="center" label="限购名称" prop="ruleName" :min-width="120"></el-table-column>
			<el-table-column align="center" label="限购对象" prop="ruleTarget" :formatter="fromatTarget" :min-width="120"></el-table-column>
			<!-- <el-table-column label="限制购买数量" prop="dayLimit" :min-width="120"></el-table-column> -->
			<el-table-column align="center" label="最后更新时间" prop="updateTime" :formatter="formatTime" :min-width="160"></el-table-column>
			<!-- <el-table-column label="规格值" prop="values" :min-width="120"></el-table-column> -->
			<el-table-column align="center" label="规则状态" prop="ruleStatus" :formatter="formatStatus" :min-width="120"></el-table-column>
			<el-table-column align="center" label="操作" :min-width="200">
				<template slot-scope="scope">
					<el-button class="mini-search-btn" size="mini" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
					<el-button class="mini-tableadd-btn" v-if="scope.row.ruleStatus==2" size="mini" @click="handleStart(scope.$index, scope.row)">启用</el-button>
					<el-button class="mini-freeze-btn" v-else size="mini" @click="handleStop(scope.$index, scope.row)">停用</el-button>
					<el-button class="mini-delete-btn" size="mini" v-if="scope.row.ruleStatus==2" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
	</div>
</template>
<script>
import PageHeader from "@/components/PageHeader"
import page from "@/components/pagination"
import {timeFormat} from "@/utils/timeFormat.js"
export default {
	name: 'restrictionlist',
	data() {
		return {
      tableHeight: 600,
			batch: '', // 批量启用停用删除操作
			ruleStatus: '', // 启用停用筛选
			action: '', // 导入导出
			tableData: [],
			pageInfo: {
				page: 1,
				count: 10,
			},
			content:'',
			total: '',
      multipleSelection: [],
      loading: false,
		}
	},
	created() {
    this.getTableData();
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
	},
	components: {
		PageHeader,
		page,
	},
	methods: {
		// ======== 操作 ========
		filter() { // 搜索操作
			this.pageInfo.page = 1
			this.pageInfo.content=this.content;
			this.getTableData();
		},
		
		add() { // 添加限购规则操作
			this.$router.push({ name: 'checklin',query:{ checkMsg: 0}})
		},

		handleEdit(index, row) { // 查看操作
			this.$router.push({ name: 'checklin', query: { id: row.id, checkMsg: 1 }})
		},

		handleDelete(index, row) { // 删除操作
			this.$confirm('此操作将永久删除该规则, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.$api.delete(this, 'admin/u/p/goods/restriction', { id: row.id }).then(res => {
					this.$message({
						message: '删除成功',
						type: 'success',
						duration: 3 * 1000,
					})
					this.getTableData()
				})
			})
		},

		handleStart(index,row){ // 启用操作
		 this.$confirm('此操作将启用该规则, 是否继续?', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
			center: true
		  }).then(()=>{
				this.$api.put(this,'admin/u/p/goods/restriction/status',{id:row.id,ruleStatus:1}).then(res=>{
					if(res.code==0) {
						this.getTableData()
					}
				})
		  })
		},

		handleStop(index,row){ // 禁用操作
		  this.$confirm('此操作将禁用该规则, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
		  }).then(()=>{
				this.$api.put(this,'admin/u/p/goods/restriction/status',{id:row.id,ruleStatus:2}).then(res=>{
					if(res.code==0) {
						this.getTableData()
					}
				})
		  })
		},

		ruleStatusChange(event) { // 启用停用筛选
			this.pageInfo.ruleStatus = event
			this.getTableData();
		},

		// ======== 数据 ========
    getTableData() {  // 限购列表数据
			this.loading = true;
			if(this.pageInfo.ruleStatus === ''){
				this.pageInfo.ruleStatus = undefined
			}
			this.$api.get(this, 'admin/u/p/goods/restriction/list', this.pageInfo).then(res => {
				this.tableData = res.rules;
				res.code == 0 ? this.loading = false : this.looking = false
				this.$api.get(this, 'admin/u/p/goods/restriction/count', this.pageInfo).then(res => {
					if(res.code == 0) {
						this.total = res.count
					}
				})
			})
		},

		handleSelectionChange(val) { // 表格选中的值
			this.multipleSelection = val
		},

		handleBatchChange() { // 批量启用停用删除操作
			let ids = [];
			this.multipleSelection.forEach(item => {
				ids.push(item.id)
			})
			ids = ids.join(',')
			switch(this.batch) {
				case 1:
					this.$confirm('此操作将禁用选中品类, 是否继续?', '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning',
						center: true
					}).then(() => {
						this.$api.put(this, 'admin/u/p/productline/open', { ids: ids, isOpen: false }).then(res => {
							if(res.code == 0) {
								this.getTableData()
							}
						})
					})
				break;
				case 2:
					this.$confirm('此操作将启用选中品类, 是否继续?', '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning',
						center: true
					}).then(() => {
						this.$api.put(this, 'admin/u/p/productline/open', { ids: ids, isOpen: true }).then(res => {
							if(res.code == 0) {
								this.getTableData()
							}
						})
					})
				break;
				case 3:
					this.$confirm('此操作将永久删除选中品类, 是否继续?', '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning',
						center: true
					}).then(() => {
						this.$api.delete(this, 'admin/u/p/productline', { ids: ids }).then(res => {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 3 * 1000,
						})
							this.getTableData()
						})
					})
				break;
			}
		},
		// ======== 转换 ========
		formatStatus(row, col, val) { // 规则状态
			if(val == 1) {
				return '启用'
			} else if(val == 2) {
				return '停用'
			}
		},

		fromatTarget(row, col, val) { // 限购对象
			if(val == 1) {
				return '商品限购'
			}else if(val == 2) {
				return '订单限购'
			}
		},

		formatTime(row,col,val) { // 时间转换
			return timeFormat(val)
		},

		sizeChange(size) {
			this.pageInfo.count = size;
			this.getTableData()
		},

		currentChange(page) {
			this.pageInfo.page = page;
			this.getTableData()
		},
	},
	watch: {
		pageInfo: {
			handler() {
				this.getTableData()
			},
			deep: true
		},

	}
}

</script>
<style lang="scss" scoped>
.restriction-list {
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
	.nav {
		background-color: white;
		.Fheader {
      padding: 10px;
			overflow: hidden;
			.search {
				width:300px;
				float: right;
				.box-search {
					width: 140px;
				}
			}
		}
	} 
}

</style>
