<template>
	<div class="grade-list">
		<header>
			<h4>商品等级列表</h4>
			<el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add">
				添加等级
			</el-button>
    </header>

		<div class="search">
			<el-select placeholder="启用状态" size="mini" v-model="pageInfo.status" clearable style="width:130px;">
				<el-option :key="item.id" :label="item.name" :value="item.id" v-for="item in statusList"> </el-option>
			</el-select>
			<div class="search-right">
				<el-input v-model.trim="pageInfo.content" @keyup.enter.native="onSearch()" placeholder="等级名称" size="mini" class="box-search" ></el-input>
				<button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
			</div>
		</div>
		<el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;" v-loading="loading" >
			<el-table-column align="center" label="等级名称" prop="gradeName" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="等级描述" prop="remark" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="等级状态" prop="status" :formatter="formatStatus" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="操作" :min-width="120">
				<template slot-scope="scope">
					<el-button class="mini-search-btn" v-if="scope.row.id !== 10000 && scope.row.id !== 10001" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
					<el-button class="mini-tableadd-btn" v-if="scope.row.status === 0 && scope.row.id !== 10000 && scope.row.id !== 10001" @click="handleStart(scope.$index, scope.row)">启用</el-button>
					<el-button class="mini-freeze-btn" v-if="scope.row.status !== 0 && scope.row.id !== 10000 && scope.row.id !== 10001" @click="handleStop(scope.$index, scope.row)">停用</el-button>
					<el-button class="mini-delete-btn" v-if="scope.row.status === 0 && scope.row.id !== 10000 && scope.row.id !== 10001" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
	</div>
</template>
<script>

import page from "@/components/pagination"
export default {
	data() {
		return {
			statusList:[{
				id:0,
				name:'停用'
			},{
				id:1,
				name:'启用'
			}],
			tableHeight: 600,
			loading: false,
			batch: '',
			action: '',
			tableData: [],
			pageInfo: {
				page: 1,
				count: 10,
				content: '',
				status:''
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
		// PageHeader,
		page,
	},
	methods: {
		formatStatus(row, col, val) { // 等级状态
			switch(val) {
				case 0:
					return "停用";
					break;
				case 1:
					return "启用";
					break;
				}
		},
		onSearch(){ // 搜索操作
			this.pageInfo.page = 1;
			this.getTableData()
    },
		add() {
			this.$router.push({ name: 'addgoodGrade'})
		},
		filter() {
			this.getTableData();
		},
		sizeChange(size) {
			this.pageInfo.count = size;
			this.pageInfo.page = 1;
			this.getTableData()
		},
		currentChange(page) {
			this.pageInfo.page = page;
			this.getTableData()
		},
		getTableData() {
			this.loading = true;
			this.$api.get(this, 'admin/u/p/good/grade/list', this.pageInfo).then(res => {
				if(res.code == 0) {
					this.tableData = res.goodGrades;
					this.$api.get(this, 'admin/u/p/good/grade/count').then(res => {
						if(res.code == 0) {
							this.total = res.count
						}
					})
				}
				res.code == 0 ? this.loading = false : this.loading = false
			})
		},
		handleEdit(index, row) {
			this.$router.push({ name: 'editgoodGrade', params: { id: row.id } })
		},
		handleDelete(index, row) {
			this.$confirm('确定删除此等级？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.$api.delete(this, 'admin/u/p/good/grade', { id: row.id }).then(res => {
					this.$message({
						message: '删除成功',
						type: 'success',
						duration: 3 * 1000,
					})
					this.getTableData()
				})
			})
		},
		handleStart(index,row){
		 this.$confirm('确定启用此等级？', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
			center: true
		  }).then(()=>{
			this.$api.put(this,'admin/u/p/good/grade/stopStart',{id:row.id,status:1}).then(res=>{
			  if(res.code==0)
			  {
				this.getTableData()
			  }
			})
		  })
		},
		handleStop(index,row){
		  this.$confirm('确定停用此等级？', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
			center: true
		  }).then(()=>{
			this.$api.put(this,'admin/u/p/good/grade/stopStart',{id:row.id,status:0}).then(res=>{
			  if(res.code==0)
			  {
				this.getTableData()
			  }
			})
		  })
		}
	},
	watch: {
		'pageInfo.status': {
      handler() {
        this.pageInfo.page = 1;
				this.getTableData()
      },
      deep: true
    },
	}
}

</script>
<style lang="scss" scoped>
.grade-list {
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
    // position: absolute;
    // bottom: auto;
    // top: expression(eval(document.documentElement.scrollTop));
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
				// float: right;
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
