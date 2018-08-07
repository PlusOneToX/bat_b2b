<template>
	<div class="material-list">
		<header>
			<h4>材料列表</h4>
			<el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add">
				添加材料
			</el-button>
    	</header>
		<div class="search">
			<div class="search-right">
				<el-input v-model.trim="pageInfo.content" clearable @change="changeContent" placeholder="材料名称" size="mini" class="box-search" ></el-input>
				<button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
			</div>
			<el-select v-model="pageInfo.categoryId" placeholder="材料类型" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in categorys"
				:key="item.id"
				:label="item.name"
				:value="item.id">
				</el-option>
			</el-select>
			<el-select v-model="pageInfo.openFlag" placeholder="状态" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in statuss"
				:key="item.status"
				:label="item.label"
				:value="item.status">
				</el-option>
			</el-select>
		</div>
      <el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;" row-key="id" lazy
				:tree-props="{children: 'childrenList', hasChildren: 'hasChildren'}" v-loading="loading" >
			<el-table-column align="center" label="材料ID" prop="id" :width="120" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="材料编码" :width="120" prop="materialNo" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="材料名称" prop="name" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="材料英文名称" prop="englishName" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="材料类型" :width="120" prop="categoryEnglishName"  show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="图片" :width="100">
				<template slot-scope="scope" style="text-align: center; width: 60px; height: 60px;">
					<el-image
						v-if="scope.row.image"
						style="text-align: center;width: 60px; height: 60px;padding-right: 0px;"
						:src="scope.row.image"
						fit="contain"
						:preview-src-list="[scope.row.image]">
					</el-image>
					<div v-else style="text-align: center;line-height: 60px;padding-right: 0px;">-</div>
				</template>
			</el-table-column>
			<el-table-column align="center" label="状态" :width="80" prop="openFlag" :formatter="formatStatus" show-overflow-tooltip></el-table-column>
			<!-- <el-table-column align="center" label="第三方编号" prop="code" show-overflow-tooltip></el-table-column> -->
			<el-table-column  align="center" label="操作" :min-width="120">
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
        parentId: 0,
				categoryId:'',
				openFlag:'',
				content:''
			},
			total: '',
			statuss:[{
				status:1,
				label:'启用'
			},{
				status:0,
				label:'停用'
			}],
			categorys:[]
		}
	},
	created() {
		const documentHeight = document.body.clientHeight;
		this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
		this.getProCategory()
	},
	mounted() {
		this.getTableData()
	},
	components: {
		page,
	},
	methods: {
		// 产品材料类型下拉列表
		getProCategory () {
			this.$http.productUsableList(this).then(res => {
				if (res.success) {
					this.categorys = res.data
				}
			})
		},
		changeContent(val){
			if(val === undefined || val === '' || val === null){
				this.onSearch()
			}
		},
		onSearch(){ // 搜索操作
			this.pageInfo.page = 1;
			this.getTableData()
    	},
		add() {
			this.$router.push({ name: 'materialAdd'})
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
			if(this.pageInfo.categoryId === ''){
				this.pageInfo.categoryId = undefined
			}
			if(this.pageInfo.openFlag === ''){
				this.pageInfo.openFlag = undefined
			}
			if(this.pageInfo.content === ''){
				this.pageInfo.content = undefined
			}
		  this.$http.materialList(this, this.pageInfo).then(res => {	
				if(res.success) {
					this.tableData = res.data.list
					this.total = res.data.total
				}
				this.loading = false
			})
		},
		handleEdit(index, row) {
			this.$router.push({ name: 'materialEdit', params: { id: row.id } })
		},
		handleDelete(index, row) {
			this.$confirm('确定删除此材料吗？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.$http.delMaterial(this, {id: row.id}).then(res => {		
					if(res.success){
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 3 * 1000,
						})
            this.pageInfo.parentId = 0
						this.getTableData()
					}
				})
			})
		},
		handleChangeStart(row,status){
			let msg = ''
			if(status === 1){
				msg = '确定启用此材料吗？'
			}else if(status === 0){
				msg = '确定停用此材料吗？'
			}
		 this.$confirm(msg, '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
			center: true
		  }).then(()=>{
		  this.$http.materialStatus(this, {id:row.id, openFlag:status}).then(res => {		
			  if(res.success){
				  if(row.openFlag === 0){
					  row.openFlag = 1
				  }else{
					  row.openFlag = 0
				  }
			  }
			})
		  })
		},
		// 上移/下移
		handleMove(row, status) {
			this.$http.materialSort(this, {
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
		formatStatus(val) {
      switch(val.openFlag) {
        case 0 :
					return "禁用";
					break;
        case 1:
          return '启用';
          break;
      }
    }
	},
	watch: {
		'pageInfo.categoryId': {
			handler() {
				this.pageInfo.page = 1
				this.getTableData()
			},
			deep: true
		},
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
