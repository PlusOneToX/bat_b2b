<template>
	<div class="category-list">
		<header v-if="append">
		  <h4>商品分类列表</h4>
		  <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="handleAddCategory(0)" v-if="!addShow">	添加商品分类 </el-button>
		</header>
		<header v-if="looking">
		  <h4>查看商品分类</h4>
		</header>
		<div v-if="!addShow">
			<div style="padding: 10px;">
				<button class="mini-batch-btn"  @click="expandAll()">全部展开</button>
				<button class="mini-batch-btn" @click="closeAll()">全部收起</button>
				<button class="mini-batch-btn" @click="recommendedFun()">推荐分类</button>
			</div>
			<el-row>
				<tree-table :data="tableData" :props="defaultProps"  :columns="columns" border header-row-class-name="header-row" class="tableCenter"  v-loading="loading" max-height="600">
					<el-table-column align="center" type="index" fixed :min-width="50"></el-table-column>
					<el-table-column label="状态" align="center" :min-width="120">
						<template slot-scope="scope">
							{{scope.row.openFlag?'启用':'停用'}}
						</template>
					</el-table-column>
					<el-table-column label="是否推荐分类" align="center" :min-width="120">
						<template slot-scope="scope">
							{{scope.row.recommendFlag==1?'是':'否'}}
						</template>
					</el-table-column>
					<el-table-column label="操作" align="center" :min-width="530">
						<template slot-scope="scope">
							<el-button class="mini-tableadd-btn" v-if="scope.row.parentId==0" icon="el-icon-plus" @click="handleAddCategory(scope.row)" v-show="scope.row.openFlag">添加子分类</el-button>
							<el-button class="mini-search-btn" @click="perviewGoods(scope.row)" v-show="scope.row.openFlag">查看商品</el-button>
							<!-- <el-button class="mini-tableadd-btn" @click="checkGoods(scope.row)" v-show="scope.row.openFlag">添加商品</el-button> -->
							<el-button class="mini-search-btn" @click="handleEdit(scope.row)">查看</el-button>
							<el-button class="mini-freeze-btn" @click="handleStop(scope.row)" v-if="scope.row.openFlag">停用</el-button>
							<el-button class="mini-tableadd-btn" @click="handleStart(scope.row)" v-else>启用</el-button>
							<el-button class="mini-delete-btn" @click="handleDelete(scope.row)" v-if="scope.row.openFlag === 0">删除</el-button>
						</template>
					</el-table-column>
				</tree-table>
				<page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
			</el-row>
		</div>
		<!-- 详情编辑组件 -->
		<add-category v-else @cancel="handleCancel" :parentId="parentId" :id="editId"></add-category>

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
		</el-dialog>
		<!-- <router-view v-else @cancel="handleCancel"></router-view> -->
	</div>
</template>
<script type="text/javascript">
import treeTable from '@/components/TreeTable'
import page from '@/components/pagination'
import addCategory from "../addcategory/index";
import selectGoods from "@/views/goods/components/selectGoods/index"
import { sortBy } from '@/utils/common'
export default {
	name: 'treeTableDemo',
	components: { treeTable, page, addCategory, selectGoods },
	data() {
		return {
      tableHeight: 600, //给表格高度一个默认高度，以防没有计算到表格高度
			loading: false,
			pageInfo: {
				page: 1,
				size: 10,
				content: '',
				openFlag: undefined
				// parentId: 0
			},
			append: true, // 添加商品标题
			redact: false, // 编辑商品标题
			looking: false, // 查看商品标题
			total: 0,
			addShow: false,
			tableData: [],
			columns: [
				{
					value: 'name',
					label: '分类名称',
					'min-width': '300',
					align: 'left'
				}, 
				{
					value: 'nameEn',
					label: '分类英文名称',
					'min-width': '300'
				},
				{
					value: 'appletName',
					label: '小程序分类名称',
					'min-width': '300'
				},
				{
					value: 'sort',
					label: '排序',
					width: '150',
					align: 'center'
			}, ],
			defaultProps: {
				children: 'subClassifies',
				label: 'name'
			},
			parentId: 1,
			editId: '',
			selectShow: false,
			categoryId: '',
			stocks: [],
			goodsData: [], // 查看商品dialog的数组
			previewShow: false,
			defaultExpandAllMenu: false
		}
  },
  created() {
		const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
	},
	mounted() {
		this.dataForamt()
	},
	activated() {
	 this.dataForamt()
  },
	watch:{
		pageInfo:{
			handler(){
				this.dataForamt()
			},
			deep:true,
		}
	},
	methods: {
		addpuls() { // 添加分类
			this.$router.push({ name: ''})
       },
	  //    推荐分类
	  recommendedFun(){
          this.$router.push({name:'recommendcategory'})
	  },
	

		dataForamt() {  // 主数据
			this.loading = true;
			this.$http.getClassifyList(this, this.pageInfo).then(res => {
				for(let i = 0;i<res.tableData.length;i++){
					for(let j = 0;j<this.tableData.length;j++){
						if(res.tableData[i].id === this.tableData[j].id){
							res.tableData[i]._expanded = true
						}
					}
				}
				res.tableData.sort(sortBy('sort'))
				this.tableData = res.tableData
				this.total = res.total
				this.loading = false
			})
		},
		checkGoods(row) {  // 添加商品
			this.selectShow = true;
			this.categoryId = row.id;
		},
		closeDialog() {  // 关闭添加商品dialog
			this.$refs.selectGoods.handleCancel()
		},
		// 暂不需要
		// stocksDelect(row) {  // 查看商品dialog删除操作
		// 	this.$api.delete(this,'admin/u/p/goods', {ids: row.id}).then(res => {
		// 		if(res.code == 0) {
		// 			this.$message.success({
		// 				message: '删除成功',
		// 				duration: 3 * 1000,
		// 				// onClose: () => { }
		// 			})
		// 		}
		// 	})
		// },

		// 添加商品功能暂不需要
		// getGoodsData(val) {  // 查看商品dialog子组件传上来选中的商品的值
		// 	this.selectShow = false;
		// 	let ary = [];
		// 	val.forEach(item => {
		// 		ary.push(item.id)
		// 	})
		// 	this.$api.post(this, 'admin/u/p/category/goods', { categoryId: this.categoryId, commodityIds: ary.join(',') }).then(res => {
		// 		if(res.code == 0) {
		// 			this.$message.success({
		// 				message: '添加成功',
		// 				duration: 3 * 1000,
		// 				onClose: () => { }
		// 			})
		// 			this.categoryId = '';
		// 			this.selectShow = false;
		// 			this.dataForamt();
		// 		}
		// 	})
		// },
		perviewGoods(row) {  // 查看商品dialog数据来源
			this.$http.getGoodsList(this, { page:1, size:10000, classifyId: row.id }).then(res => {		
				 if(res.success) {
					this.goodsData = res.data.list;
					this.previewShow = true;
				}
			})
		},
		closePreviewShow(){  // 关闭查看商品的dialog
			this.previewShow = false;
		},
		handleDelete(row) { // 删除操作
			this.$confirm('确定删除此分类?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.$http.deleteClassify(this, { id: row.id }).then(res => {
					if (res.success) {
						this.$message.success({
							message: '删除成功',
							duration: 3 * 1000,
							onClose: () => { }
						})
						this.dataForamt()
					}
				})
			})
		},
		handleCancel() { // 监听编辑页面的返回操作
			this.addShow = false;
			this.append = true;
			this.looking = false;
			this.dataForamt()
		},
		handleAddCategory(row) {
			// this.addShow = true;
			// this.editId = '';
			// if(row !== 0) {
			// 	this.parentId = row.id;
			// } else {
			// 	this.parentId = 1;
			// }
			if(row === 0){
				this.$router.push({name:'addcategory'})
			}else{
				this.$router.push({name:'addcategory',query:{parentId: row.id}})
			}


		},
		handleEdit(row) {
			this.$router.push({name:'addcategory',query:{id:row.id, parentId: row.parentId}})
			// this.addShow = true;
			// this.append = false; // 添加标题
			// this.looking = true; // 查看标题
			// this.editId = row.id
			// this.parentId = row.parentId;
		},
		handleStop(row) {
			this.$confirm('确定停用此分类?', '提示', {
				confirmButtonText: '停用',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.$http.classifyStatus(this, { id: row.id, openFlag: 0 }).then(res => {
					if (res.success) {
						this.dataForamt()
					}
				})
			})

		},
		handleStart(row) {
			this.$confirm('确定启用此分类?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.$http.classifyStatus(this, { id: row.id, openFlag: 1 }).then(res => {
					if (res.success) {
						this.dataForamt()
					}
				})
			})
		},
		expandAll() { // 全部展开操作
			this.tableData.forEach(item => {
				item._expanded = true;
			})
    },
		closeAll() { // 全部收起
			this.tableData.forEach(item => {
				item._expanded = false;
			})
    },

    sizeChange(size) {
			this.pageInfo.size = size;
			this.dataForamt();
    },

		currentChange(page) {
			this.pageInfo.page = page;
			this.dataForamt();
    }
	}
}

</script>
<style  rel="stylesheet/scss" lang="scss" scoped>
	.category-list {
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
			// font-size: 12px;
		}
		.tool-bar {
			padding: 10px;
		}
		.header-row {
			@include table-header-color;
		}
	}
</style>
