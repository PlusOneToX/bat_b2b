<template>
	<div class="material-list">
		<header>
			<h4>标签列表</h4>
			<el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add">
				新增标签
			</el-button>
    	</header>
		<div class="search">
			<div class="search-right">
				<el-input v-model.trim="pageInfo.content" clearable @change="changeContent" placeholder="请输入标签名称" size="mini" class="box-search" ></el-input>
				<button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
			</div>
			<el-select v-model="pageInfo.type" placeholder="标签类型" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in types"
				:key="item.type"
				:label="item.label"
				:value="item.type">
				</el-option>
			</el-select>
			<el-select v-model="pageInfo.openFlag" placeholder="标签状态" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in statuss"
				:key="item.status"
				:label="item.label"
				:value="item.status">
				</el-option>
			</el-select>
		</div>
		
		<el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;" v-loading="loading" >
			<el-table-column align="center" label="标签编号" prop="id" width="100"></el-table-column>
			<el-table-column align="center" label="标签名称" prop="name" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="标签类型" prop="type" width="100" :formatter="formatType" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="产品名称位置" show-overflow-tooltip>
				<template slot-scope="scope" style="text-align: center;">
					<span>{{scope.row.productNamePosition}}</span>
				</template>
			</el-table-column>
			<el-table-column align="center" label="英文名称位置" show-overflow-tooltip>
				<template slot-scope="scope" style="text-align: center;">
					<span>{{scope.row.enNamePosition}}</span>
				</template>
			</el-table-column>
			<el-table-column align="center" label="产品型号位置" show-overflow-tooltip>
				<template slot-scope="scope" style="text-align: center;">
					<span>{{scope.row.modelPosition}}</span>
				</template>
			</el-table-column>
			<el-table-column align="center" label="条形码位置"  show-overflow-tooltip>
				<template slot-scope="scope" style="text-align: center;">
					<span>{{scope.row.barCodePosition}}</span>
				</template>
			</el-table-column>
			<el-table-column align="center" label="状态" :width="60" prop="openFlag" :formatter="formatStatus"></el-table-column>
			<el-table-column  align="center" label="操作" :width="280">
				<template slot-scope="scope">
					<el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
					<el-button class="mini-tableadd-btn" v-if="scope.row.openFlag === 0" @click="handleChangeStart(scope.row,1)">启用</el-button>
					<el-button class="mini-freeze-btn" v-else @click="handleChangeStart(scope.row,0)">停用</el-button>
					<el-button class="mini-delete-btn" v-if="scope.row.openFlag === 0" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
					<el-button class="mini-browse-btn" @click="handlePreview(scope.row)">预览</el-button>
				</template>
			</el-table-column>
		</el-table>
		<page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
	</div>
</template>
<script>
import page from "@/components/pagination"
import axios from 'axios'
import url from '@/api/allUrl'
import { getToken } from '@/utils/auth'
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
				content:undefined,
				type:undefined,
			},
			total: '',
			types:[{
				type:1,
				label:'标准标签'
			},{
				type:2,
				label:'定制标签'
			}],
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
		this.getLabel()
	},
	activated() {
		this.getLabel()
	},
	components: {
		page,
	},
	methods: {
		formatType(row, col, val){
			switch(val) {
				case 1:
				return "标准标签";
				break;
				case 2:
				return "定制标签";
				break;
			}
		},
		selectable(row, index) { //..是否可勾选s
			return true
		},
		getLabel(){
			this.loading = true;
			this.$http.labelList(this, this.pageInfo).then(res => {		
				if(res.success) {
					this.tableData = res.data.list
					this.total = res.data.total
				}
				this.loading = false
			})
		},
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
				this.getLabel()
			}
		},
		onSearch(){ // 搜索操作
			this.pageInfo.page = 1;
			this.getLabel()
    	},
		add() {
			this.$router.push({ name: 'labelAdd'})
		},
		filter() {
			this.getLabel();
		},
		sizeChange(size) {
			this.pageInfo.page = 1
			this.pageInfo.size = size;
			this.getLabel()
		},
		currentChange(page) {
			this.pageInfo.page = page;
			this.getLabel()
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
		formatCategory(row, col, val) {
			switch(val) {
				case 10010:
				 return '手机壳';
				 break
				case 10020:
				 return '手机背膜';
				 break
			}
		},
		handleEdit(index, row) {
			this.$router.push({ name: 'labelEdit', params: { id: row.id } })
		},
		handleDelete(index, row) {
			this.$confirm('确定删除此标签吗？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.$http.delLabel(this, {id:row.id}).then(res => {	
					if(res.success){
						this.getLabel()
					}
				})
			})
		},
		handleChangeStart(row,status){
			let msg = ''
			if(status === 1){
				msg = '确定启用此标签吗？'
			}else if(status === 0){
				msg = '确定停用此标签吗？'
			}
		 this.$confirm(msg, '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
			center: true
		  }).then(()=>{
		  this.$http.labelStatus(this, {id:row.id, openFlag: status}).then(res => {			
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
		handlePreview(row){
				let tenantUrl = localStorage.getItem('tenantUrl');
				axios({
          method: 'get',
          url: tenantUrl + '/' + url.tabPreview,
					params: {id: row.id},
          responseType: 'arraybuffer',
          headers: {
            'Content-Type': 'application/json',
            token: getToken()
          }
        })	.then((res) => {
					this.loading = false
					if(res === undefined || res.success == undefined) {
						this.timeData = [] //..成功下载Excel后初始化选中时间
					}
					const content = res.data
					let blob = new Blob([content],{
						type: "application/pdf"
					})
					let url = window.URL.createObjectURL(blob)
					this.$router.push({ name: 'pdfPreview', params: { url: url } })
				})
		}
	},
	watch: {
		'pageInfo.openFlag': {
			handler() {
				this.pageInfo.page = 1
				this.getLabel()
			},
			deep: true
		},
		'pageInfo.type': {
			handler() {
				this.pageInfo.page = 1
				this.getLabel()
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
