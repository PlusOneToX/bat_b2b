<template>
	<div class="goodslabel-list">
		<header>
			<h4>商品标签列表</h4>
			<el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="add">
				添加标签
			</el-button>
		</header>
		<div class="search">
			<div class="search-right">
				<el-input v-model.trim="pageInfo.content" @keyup.enter.native="onSearch()" placeholder="标签名称" size="mini" class="box-search" ></el-input>
				<button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
			</div>

			<el-select class="custom_select" placeholder="状态" size="mini" v-model="pageInfo.openFlag" clearable>
				<el-option :key="item.status" :label="item.name" :value="item.status" v-for="item in statuss"> </el-option>
			</el-select>
		</div>
		<el-table :data="tableData" @selection-change="handleSelectionChange" header-row-class-name="header-row" border style="text-align:center;" v-loading="loading" >
			<el-table-column align="center" label="标签名称" prop="name" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="标签英文名称" prop="nameEn" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="排序" prop="sort" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="标签状态" prop="openFlag" :formatter="formatStatus"></el-table-column>
			<el-table-column align="center" label="操作" :min-width="120">
				<template slot-scope="scope">
					<!-- <el-button class="mini-search-btn" @click="perviewGoods(scope.row)" v-show="scope.row.openFlag">查看商品</el-button> -->
					<!-- <el-button class="mini-tableadd-btn" @click="checkGoods(scope.row)" v-show="scope.row.openFlag">添加商品</el-button> -->
					<el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
					<el-button class="mini-tableadd-btn" v-if="scope.row.openFlag === 0" @click="handleStatusChange(1,scope.row)">启用</el-button>
					<el-button class="mini-freeze-btn" v-else @click="handleStatusChange(0, scope.row)">停用</el-button>
					<el-button class="mini-delete-btn" v-if="scope.row.openFlag === 0" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
		<!-- 添加商品dialog -->
		<!-- <el-dialog :modal-append-to-body="false" :visible="selectShow" width="80%" :before-close="closeDialog">
			<select-goods ref="selectGoods" :saleStatus="3" :selectGoodsData='goodsData' @cancel="selectShow=false" @submit="getGoodsData"></select-goods>
		</el-dialog> -->

		<!-- 查看商品dialog -->
		<el-dialog :modal-append-to-body="false" :visible="previewShow" :before-close="closePreviewShow">
			<el-table :data="goodsData" :max-height="600">
				<el-table-column align="center" label="商品编号" prop="goodsNo"></el-table-column>
				<el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column>
				<!-- <el-table-column label="操作">
					<template slot-scope="scope">
						<el-button type="danger" size="mini" @click="stocksDelect(scope.row)">删除</el-button>
					</template>
				</el-table-column> -->
			</el-table>
			<page :page="apage" :total="atotalCount" @sizeChange="ahandleSizeChange" @currentChange="ahandleCurrentChange"></page>
		</el-dialog>
	</div>
</template>
<script>
import page from "@/components/pagination"
import selectGoods from "@/views/goods/components/selectGoods/index"
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
				content: '',
				openFlag:undefined
			},
			total: '',
			multipleSelection: [],
			statuss:[{
				status:1,
				name:'启用'
			},{
				status:0,
				name:'停用'
			}],
			goodsData: [], // 查看商品dialog的数组
			selectShow: false,
			previewShow:false,
			labelId:'',
			apageSize:10,
			apage:1,
			atotalCount:0
		}
	},
	created() {
		const documentHeight = document.body.clientHeight;
		this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
	},
	activated() {
		this.getTableData()
		// this.getTableDataCount()
	},
	mounted() {
		this.getTableData()
		// this.getTableDataCount()
	},
	components: {
		// PageHeader,
		page,
		selectGoods
	},
	methods: {
		ahandleCurrentChange (val) {
			this.apage = val
			this.getTableData()
		},
		ahandleSizeChange (val) {
			this.apageSize = val
			this.getTableData()
			// this.getTableDataCount()
		},

		// 添加商品暂不需要
		// getGoodsData(val) {  // 查看商品dialog子组件传上来选中的商品的值
		// 	this.selectShow = false;
		// 	let ary = [];
		// 	val.forEach(item => {
		// 		ary.push(item.id)
		// 	})
		// 	this.$api.post(this, api.goodsLable, { labelId: this.labelId, goodsIds: ary }).then(res => {
		// 		if(res.code == 0) {
		// 			this.$message.success({
		// 				message: '添加成功',
		// 				duration: 3 * 1000,
		// 				onClose: () => { }
		// 			})
		// 			this.labelId = '';
		// 			this.selectShow = false;
		// 		}
		// 	})
		// },
		closeDialog() {  // 关闭添加商品dialog
			this.$refs.selectGoods.handleCancel()
			// this.selectShow = false;
		},
		closePreviewShow(){  // 关闭查看商品的dialog
			this.previewShow = false
		},
		checkGoods(row) {  // 添加商品
			this.selectShow = true;
			this.labelId = row.id;
		},
		// 查看商品暂不需要
		perviewGoods(row) {  // 查看商品dialog数据来源
			this.previewShow = true;
			this.getShowGoods(row)
			this.getShowGoodsCount(row)
		},
		// getShowGoods(row){
		// 	this.$api.get(this, 'admin/u/p/goods/label/showGoods', { page: this.apage, count: this.apageSize, labelId: row.id }).then(data => {
		// 		 if(data.code == 0) {
		// 			this.goodsData = data.goodsList;
		// 		}
		// 	})
		// },
		// getShowGoodsCount(row){
		// 	this.$api.get(this, 'admin/u/p/goods/label/showGoods/count', { labelId: row.id }).then(data => {
		// 		 if(data.code == 0) {
		// 			this.atotalCount = data.count;
		// 		}
		// 	})
		// },
		onSearch(){ // 搜索操作
			this.pageInfo.page = 1
			this.getTableData()
    	},
		add() {
			this.$router.push({ name: 'addgoodslabel'})
		},
		sizeChange(size) {
			this.pageInfo.size = size
			this.getTableData()
		},
		currentChange(page) {
			this.pageInfo.page = page
			this.getTableData()
		},
		getTableData() {
			this.loading = true;
			this.$http.getTagList(this, this.pageInfo).then(res => {
				if(res.success) {
					this.tableData = res.data.list
					this.total = res.data.total
				}
				this.loading = false
			})
		},
		getTableDataCount(){
			this.$api.get(this, 'admin/u/p/goods/label/count',this.pageInfo).then(res => {
				if(res.code == 0) {
					this.total = res.count
				}
			})
		},
		formatStatus(row, col, val) {
			if(val) {
				return '启用'
			} else {
				return '停用'
			}
		},
		handleSelectionChange(val) {
			this.multipleSelection = val
		},
		handleEdit(index, row) {
			this.$router.push({ name: 'editgoodslabel', params: { id: row.id } })
		},
		handleDelete(index, row) {
			this.$confirm('确定删除此商品标签？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.$http.deleteTag(this, { id: row.id }).then(res => {
					if (res.success) {
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
		handleStatusChange(status,row){
			let label = JSON.parse(JSON.stringify(row))
			label.status = status
			let htr = ''
			if(status === 0){
				htr = '确定停用此标签'
			}else {
				htr = '确定启用此标签'
			}
			this.$confirm(htr, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(()=>{
				this.$http.tagStatus(this, {
					id: row.id,
					openFlag: status
				}).then(res => {
					if (res.success) {
						this.getTableData()
					}
				})
			})
		},
	},
	watch: {
		'pageInfo.openFlag' :{
			handler() {
				this.getTableData()
			},
			deep: true
		}
	}
}

</script>
<style lang="scss" scoped>
.goodslabel-list {
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
	.custom_select {
		width:120px;
	}
}

</style>
