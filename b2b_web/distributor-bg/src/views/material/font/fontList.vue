<template>
	<div class="material-list">
		<header>
			<h4>字体列表</h4>
			<el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add">
				新增字体
			</el-button>
    	</header>
		<div class="search">
			<div class="search-right">
				<el-input v-model.trim="pageInfo.content" clearable @change="changeContent" placeholder="请输入字体编号/名称" size="mini" class="box-search" ></el-input>
				<button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
			</div>
			<el-select v-model="pageInfo.openFlag" placeholder="字体状态" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in statuss"
				:key="item.status"
				:label="item.label"
				:value="item.status">
				</el-option>
			</el-select>
		</div>
		
		<el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;" v-loading="loading" >
			<el-table-column align="center" label="编号" prop="id"></el-table-column>
			<el-table-column align="center" label="字体名称" prop="name" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="字体英文名称" prop="englishName" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="状态" prop="openFlag" :formatter="formatStatus"></el-table-column>
			<el-table-column align="center" label="操作" :min-width="120">
				<template slot-scope="scope">
					<el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
					<el-button class="mini-tableadd-btn" @click="handleMove(scope.row, true)">上移</el-button>
					<el-button class="mini-freeze-btn" @click="handleMove(scope.row, false)">下移</el-button>
					<el-button class="mini-tableadd-btn" v-if="scope.row.openFlag === 0" @click="handleChangeStart(scope.row,1)">启用</el-button>
					<el-button class="mini-freeze-btn" v-else @click="handleChangeStart(scope.row,0)">停用</el-button>
					<el-button class="mini-delete-btn" v-if="scope.row.openFlag === 0" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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
			tableHeight: 600,
			loading: false,
			tableData: [],
			pageInfo: {
				page: 1,
				size: 10,
				openFlag:undefined,
				content:'',
			},
			total: '',
			statuss:[{
				status:1,
				label:'启用'
			},{
				status:0,
				label:'停用'
			}],
		}
	},
	created() {
		const documentHeight = document.body.clientHeight;
		this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
	},
	activated() {
		this.getTableData()
	},
	components: {
		page,
	},
	methods: {
		formatStatus(row, col, val){
			switch(val) {
				case 0:
				return "停用";
				break;
				case 1:
				return "启用";
				break;
			}
		},
		changeContent(val){
			if(val === undefined || val === '' || val === null){
				this.getTableData()
			}
		},
		onSearch(){ // 搜索操作
			this.pageInfo.page = 1;
			this.getTableData()
    	},
		add() {
			this.$router.push({ name: 'fontAdd'})
		},
		filter() {
			this.getTableData();
		},
		sizeChange(size) {
			this.pageInfo.page = 1
			this.pageInfo.size = size;
			this.getTableData()
		},
		currentChange(page) {
			this.pageInfo.page = page;
			this.getTableData()
		},
		getTableData() {
			this.loading = true;
			this.$http.fontList(this, this.pageInfo).then(res => {	
				if(res.success) {
					this.tableData = res.data.list
					this.total = res.data.total
					if((this.tableData === undefined || this.tableData === null || this.tableData.length === 0) && this.pageInfo.page > 1){
						this.pageInfo.page = this.pageInfo.page - 1
						this.getTableData()
					}else {
						this.loading = false
					}
				}else{
					this.loading = false
				}
			})
		},
		handleEdit(index, row) {
			this.$router.push({ name: 'fontEdit', params: { id: row.id } })
		},
		// 上移/下移
		handleMove(row, status) {
			this.$http.fontSort(this, {	
				id: row.id,
				flag: status
			}).then(res=>{
			  if(res.success){
				  this.$message({
						message: status ? '上移成功' : '下移成功',
						type: 'success',
					});
					this.getTableData();
			  }
			})
		},
		handleDelete(index, row) {
			this.$confirm('确定删除此字体吗？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.$http.delFont(this, {id:row.id}).then(res => {	
					if(res.success){
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 3 * 1000,
						})
						this.getTableData()
					}
				})
			})
		},
		handleChangeStart(row,status){
			let msg = ''
			let font = JSON.parse(JSON.stringify(row))
			if(status === 1){
				msg = '确定启用此字体吗？'
			}else if(status === 0){
				msg = '确定停用此字体吗？'
			}
		 this.$confirm(msg, '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
			center: true
		  }).then(()=>{
			font.openFlag = status
			this.$http.fontStatus(this, font).then(res => {		
				if(res.success){
					row.openFlag = font.openFlag
				}
			})
		  })
		}
	},
	watch: {
		'pageInfo.openFlag': {
			handler() {
				this.pageInfo.page = 1
				this.getTableData()
			},
			deep: true
		}
	}
}

</script>
<style lang="scss" scoped>
.material-list {
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
		padding-bottom: 10px;
		padding-top: 10px;
		padding-left: 10px;
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
