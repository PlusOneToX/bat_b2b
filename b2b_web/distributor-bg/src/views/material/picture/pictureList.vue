<template>
	<div class="material-list">
		<header>
			<h4>图片列表</h4>
			<el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add">
				新增图片
			</el-button>
    	</header>
		<div class="search">
			<div class="search-right">
				<el-input v-model.trim="pageInfo.content" clearable @change="changeContent" placeholder="请输入图片编号/名称" size="mini" class="box-search" ></el-input>
				<button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
			</div>
			<el-select v-model="pageInfo.type" placeholder="图片类型" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in types"
				:key="item.type"
				:label="item.label"
				:value="item.type">
				</el-option>
			</el-select>
			<el-select v-model="pageInfo.openFlag" placeholder="图片状态" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in statuss"
				:key="item.status"
				:label="item.label"
				:value="item.status">
				</el-option>
			</el-select>

			<el-select v-model="pageInfo.categoryId" placeholder="图片分类" filterable clearable size="mini" >
				<el-option
				v-for="item in pictureCategoryList"
				:key="item.id"
				:label="item.name"
				:value="item.id">
					<div v-if="item.parentId===0">{{item.name}}</div>
					<div v-else-if="item.childrenList.length>0" v-for="child in item.childrenList" :key="child.id"> &nbsp;&nbsp;|-{{child.name}}</div>
				</el-option>
			</el-select>
		</div>
		
		<el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;" v-loading="loading" >
			<!-- <el-table-column align="center" type="selection" width="55" :selectable="selectable"></el-table-column> -->
			<el-table-column align="center" label="图片ID" prop="id"></el-table-column>
			<el-table-column align="center" label="图片编码" prop="code"></el-table-column>
			<el-table-column align="center" label="图片名称" prop="name" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="图片英文名称" prop="englishName" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="图片分类" prop="categoryName"></el-table-column>
			<el-table-column align="center" label="图片类型" prop="type" :formatter="formatType"></el-table-column>
			<el-table-column align="center" label="缩略图" :width="100">
				<template slot-scope="scope" style="text-align: center; width: 60px; height: 60px;">
					<div v-if="!scope.row.thumbnail">暂无图片</div>
					<div v-else>
						<el-image 
							style="text-align: center;width: 60px; height: 60px;padding-right: 0px;"
							fit="contain"
							:src="((scope.row.thumbnail !== undefined && scope.row.thumbnail !== null && scope.row.thumbnail !== '') ? scope.row.thumbnail:scope.row.originImage+'?x-oss-process=image/resize,h_60,l_60')" 
							:preview-src-list="[((scope.row.thumbnail !== undefined && scope.row.thumbnail !== null && scope.row.thumbnail !== '') ? scope.row.thumbnail:scope.row.originImage)]">
						</el-image>
					</div>
				</template>
			</el-table-column>
			<el-table-column align="center" label="适用材质" prop="materialNameS" :width="120" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="图片状态" prop="openFlag" :formatter="formatStatus"></el-table-column>
			<el-table-column  align="center" label="操作" :min-width="160">
				<template slot-scope="scope">
					<el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
					<el-button class="mini-tableadd-btn" @click="handleMove(scope.row, true)">上移</el-button>
					<el-button class="mini-freeze-btn" @click="handleMove(scope.row, false)">下移</el-button>
					<el-button class="mini-tableadd-btn" v-if="scope.row.openFlag === 0" @click="handleChange(scope.row,1)">启用</el-button>
					<el-button class="mini-freeze-btn" v-else @click="handleChange(scope.row,0)">停用</el-button>
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
				categoryId:undefined,
				content:undefined,
				type:undefined,
			},
			total: '',
			types:[{
				type:1,
				label:'普通素材'
			},{
				type:2,
				label:'IP素材'
			},{
				type:3,
				label:'模板'
			},{
				type:4,
				label:'贴图'
			}],
			statuss:[{
				status:1,
				label:'启用'
			},{
				status:0,
				label:'停用'
			}],
			parentId:0,
			pictureCategoryList:[], // 图片分类
		}
	},
	created() {
		const documentHeight = document.body.clientHeight;
		this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
		this.getPictureCategory()
	},
	activated() {
		this.getPictureCategory()
		this.getTableData()
	},
	components: {
		page,
	},
	methods: {
		selectable(row, index) { //..是否可勾选s
			return true
		},
		// 图片分类
		getPictureCategory(){
			this.$http.pictureCategoryPoList(this, {page:1,size:10000,atLastTrademark:1}).then(res => {
				if (res.success) {
					this.pictureCategoryList = res.data.list
				}
			})
		},
		// 图片状态
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
		// 图片类型
		formatType(row, col, val){
			switch(val) {
				case 1:
				return "普通素材";
				break;
				case 2:
				return "IP素材";
				break;
				case 3:
				return "模板";
				break;
				case 4:
				return "贴图";
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
			this.$router.push({ name: 'pictureAdd' })
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
			this.$http.pictureList(this, this.pageInfo).then(res => {	
				if(res.success) {
					this.tableData = res.data.list
					this.total = res.data.total
					if((this.tableData === undefined || this.tableData === null || this.tableData.length === 0) && this.pageInfo.page > 1){
						this.pageInfo.page = this.pageInfo.page - 1
						this.getTableData()
					}else {
						this.loading = false
					}
				} else {
					this.loading = false
				}
			})
		},
		formatStatus(row, col, val) {
			switch(val) {
				case 1:
				 return '启用';
				 break
				case 0:
				 return '停用';
				 break
			}
		},
		handleEdit(index, row) {
			this.$router.push({ name: 'pictureEdit', params: { id: row.id } })
		},
		handleDelete(index, row) {
			this.$confirm('确定删除此图片吗？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				 this.$http.delPicture(this, {id:row.id}).then(res => {			
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
		removeData(data,row){
			for(let i = 0;i < data.length;i++){
				if(data[i].id === row.id){
					data.splice(i,1)
					break
				}
			}
		},
		handleChange(row,status){
			let msg = ''
			if(status === 1){
				msg = '确定启用此图片吗？'
			}else if(status === 0){
				msg = '确定停用此图片吗？'
			}
		 this.$confirm(msg, '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
			center: true
		  }).then(()=>{
		  this.$http.pictureStatus(this, {id:row.id, openFlag:status}).then(res => {		
				if(res.success){
					if(row.openFlag === 0){
						row.openFlag = 1
					}else {
						row.openFlag = 0
					}
				}
			})
		  })
		},
		// 上移/下移
		handleMove(row, status) {
			this.$http.pictureSort(this, {	
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
		}
	},
	watch: {
		'pageInfo.openFlag': {
			handler() {
				this.pageInfo.page = 1
				this.getTableData()
			},
			deep: true
		},
		'pageInfo.categoryId': {
			handler() {
				this.pageInfo.page = 1
				this.getTableData()
			},
			deep: true
		},
		'pageInfo.type': {
			handler() {
				this.pageInfo.page = 1
				this.getTableData()
			},
			deep: true
		},
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
