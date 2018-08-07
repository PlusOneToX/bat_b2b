<template>
	<div class="material-list">
		<header>
			<h4>型号列表</h4>
			<el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add">
				添加型号
			</el-button>
    	</header>
		<div class="search">
			<el-select v-model="pageInfo.categoryId" placeholder="产品类别" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in categorys"
				:key="item.id"
				:label="item.name"
				:value="item.id">
				</el-option>
			</el-select>
			<el-button class="mini-search-btn" @click="handleExport()">导出</el-button>
		</div>

		<el-table :data="tableData" header-row-class-name="header-row" border style="text-align:center;" row-key="id" lazy
		:tree-props="{children: 'childrenList', hasChildren: 'hasChildren'}" v-loading="loading" >
			<el-table-column align="center" label="型号ID" prop="id" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="型号编码" prop="modelNo" show-overflow-tooltip>
				<template slot-scope="scope">
					<div v-if="scope.row.modelNo">{{scope.row.modelNo}}</div>
					<div v-else>--</div>
				</template>
			</el-table-column>
			<el-table-column align="center" label="型号名称" prop="name" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="型号英文名称" prop="englishName" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="型号类型" prop="categoryName"  show-overflow-tooltip>
				<template slot-scope="scope">
					<div v-if="scope.row.categoryName">{{scope.row.categoryName}}</div>
					<div v-else>--</div>
				</template>
			</el-table-column>
			<el-table-column align="center" label="图片" :width="100">
				<template slot-scope="scope" style="text-align: center; width: 60px; height: 60px;">
					<el-image
						style="text-align: center;width: 60px; height: 60px;padding-right: 0px;"
						v-if="scope.row.image"
						:src="scope.row.image"
						fit="contain"
						:preview-src-list="[scope.row.image]">
					</el-image>
					<div v-else style="text-align: center;line-height: 60px;padding-right: 0px;">-</div>
				</template>
			</el-table-column>
			<el-table-column align="center" label="状态" prop="openFlag" :formatter="formatStatus" show-overflow-tooltip></el-table-column>
			<!-- <el-table-column align="center" label="第三方编号" prop="code" show-overflow-tooltip></el-table-column> -->
			<el-table-column  align="center" label="操作" :min-width="120">
				<template slot-scope="scope">
					<el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
					<el-button class="mini-tableadd-btn" @click="handleMove(scope.row, true)">上移</el-button>
					<el-button class="mini-freeze-btn" @click="handleMove(scope.row, false)">下移</el-button>
					<el-button class="mini-tableadd-btn" v-if="scope.row.openFlag === 0" @click="handleChangeStart(scope.row,1)">启用</el-button>
					<el-button class="mini-freeze-btn" v-else @click="handleChangeStart(scope.row,0)">停用</el-button>
					<el-button class="mini-delete-btn" v-if="scope.row.openFlag === 0" @click="handleDelete(scope.$index, scope.row, scope)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
	</div>
</template>
<script>
import axios from 'axios'
import url from '@/api/allUrl'
import { getToken } from '@/utils/auth'
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
				categoryId:undefined,
				parentId:0,
				openFlag:undefined,
				content:undefined,
			},
			total: '',
			statuss:[{
				status:1,
				label:'启用'
			},{
				status:0,
				label:'停用'
			}],
			categorys:[],
			category:undefined,
			parentId:0,
		}
	},
	created() {
		const documentHeight = document.body.clientHeight;
		this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
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
			this.$router.push({ name: 'modelAdd'})
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
			//型号类别
			this.getProCategory()
			this.$http.modelList(this, this.pageInfo).then(res => {
				if(res.success) {
					if(res.data.list !== undefined && res.data.list !== null && res.data.list.length > 0){
						for(let i = 0;i<res.data.list.length;i++){
							if(res.data.list[i].trademark === 0){
								res.data.list[i].hasChildren = true
							}
						}
					}
					this.tableData = res.data.list
					this.total = res.data.total
				}
				this.loading = false
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
			this.$router.push({ name: 'modelEdit', params: { id: row.id } })
		},
		handleDelete(index, row, scope) {
			this.$confirm('确定删除此型号吗？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.$http.delModel(this, {id: row.id}).then(res => {	
					if(res.success){
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 3 * 1000,
						})
						this.removeData(this.tableData,row)
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
				if(data[i].children !== undefined && data[i].children.length>0 && data[i].children.length > 0){
					this.removeData(data[i].children,row)
				}
			}
		},
		handleChangeStart(row,status){
			let msg = ''
			if(status === 1){
				msg = '确定启用此型号吗？'
			}else if(status === 0){
				msg = '确定停用此型号吗？'
			}
		 this.$confirm(msg, '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
			center: true
		  }).then(()=>{
			 this.$http.modelStatus(this, {id:row.id, openFlag:status}).then(res => {		
			  if(res.success){
				row.openFlag = status
			  }
			})
		  })
		},
		// 上移/下移
		handleMove(row, status) {
			this.$http.modelSort(this, {	
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
		// 导出
		handleExport() {
      this.exportDownloadLoading = true;
			let tenantUrl = localStorage.getItem('tenantUrl');
      axios({
          method: 'post',
          url: tenantUrl + '/' + url.modelExport,
          responseType: 'arraybuffer',
          headers: {
            'Content-Type': 'application/json',
            token: getToken()
          }
        })
      .then(res => {  
        this.exportDownloadLoading = false;
        const content = res.data;
        let blob = new Blob([content], {
          type: "application/ms-excel",
        });
        let url = window.URL.createObjectURL(blob);
        if ("download" in document.createElement("a")) {
          let link = document.createElement("a");
          link.style.display = "none";
          link.href = url;
          link.setAttribute("download", "定制商品型号列表.xls");
          document.body.appendChild(link);
          link.click();
        } else {
          navigator.msSaveBlob(blob, "定制商品型号列表.xls");
        }
      });
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
		category(val){
			this.getTableData()
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
