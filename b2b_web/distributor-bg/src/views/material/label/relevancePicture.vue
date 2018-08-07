<template>
	<div class="check-goods">
		<div style="margin-bottom:10px;">
			<el-select v-model="pageInfo.type" placeholder="图片类型" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in types"
				:key="item.type"
				:label="item.label"
				:value="item.type">
				</el-option>
			</el-select>
			<el-select v-model="pageInfo.status" placeholder="图片状态" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in statuss"
				:key="item.status"
				:label="item.label"
				:value="item.status">
				</el-option>
			</el-select>
			<el-select class="custom_select" placeholder="图片分类" size="mini" v-model="pageInfo.categoryId" clearable>
				<el-option :key="item.id" :label="item.label" :value="item.id" v-for="item in pictureCategoryList">
				<div v-if="item.parentId === 1">{{item.label}}</div>
				<div v-else>&nbsp;&nbsp;&nbsp;├{{item.label}}</div>
				</el-option>
			</el-select>
			<el-button style="margin-left: 10px;float: right; " class="mini-search-btn" @click="filter()">搜索</el-button>
			<el-input size="mini" style="width: 200px;float: right;" placeholder="图片名称" clearable v-model.trim="pageInfo.content" @change="changeContent" />
		</div>
		<el-table 
			border 
			class="tableCenter"
			:data="tableData" 
			ref="multipleSelect" 
			tooltip-effect="dark" 
			@select="select" 
			@select-all="selectAll" 
			max-height="600px"
			header-row-class-name="header-row" 
			:height="tableHeight"
			@selection-change="handleSelectionChange" v-loading="loading">
			<el-table-column align="center" type="selection" width="55" :selectable="selectable"></el-table-column>
			<el-table-column align="center" label="图片编号" prop="id"></el-table-column>
			<el-table-column align="center" label="图片名称" prop="name" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="图片分类" prop="categoryName"></el-table-column>
			<el-table-column align="center" label="图片类型" prop="status" :formatter="formatStatus"></el-table-column>
			<el-table-column align="center" label="图片状态" prop="type" :formatter="formatType"></el-table-column>
			<el-table-column align="center" label="已关联货品" prop="itemCode"></el-table-column>
		</el-table>
		<div style="float: left;height: 40px;margin-top: 25px;">
			<span>支持用户图片上传</span>
			<el-radio-group v-model="isUserPicture" style="margin-left: 20px;">
				<el-radio :label="1">是</el-radio>
				<el-radio :label="2">否</el-radio>
			</el-radio-group>
		</div>
		<el-pagination
			style="margin-top: 15px;float: right;"
			background
			layout="total, sizes, prev, pager, next"
			:page-sizes="[10, 20, 30]"
			@current-change ="changeFormPage"
			@size-change="changeFormCount"
			:total="total">
		</el-pagination>
		<div class="foot-btn" style="text-align: center;">
			<el-button class="mini-search-btn" :loading="loading" @click="handleSubmit()">确定</el-button>
			<el-button  size="mini"  @click="handleCancel">返回</el-button>
		</div>
	</div>
</template>
<script>
import page from "@/components/pagination/index"
export default {
	name : 'channel',
	props: {
		selectItemId: undefined
	},
	data() {
		return {
			tableHeight: 600, //给表格高度一个默认高度，以防没有计算到表格高度
			content: '',
			pageInfo: {
				page: 1,
				count: 10,
				type:'',// 图片类型
				status:'',// 图片分类
				categoryId:'',
			},
			tableData: [],
			total: 0,
			loading: false,
			selectPictureIds:[],
			multipleSelect:[],
			selected:[],
			isSelect:false,
			grades:[],
			types:[{
				type:1,
				label:'普通素材'
			},{
				type:2,
				label:'IP素材'
			}],
			statuss:[{
				status:1,
				label:'启用'
			},{
				status:0,
				label:'停用'
			}],
			pictureCategoryList:[], // 图片分类
			deletePictureGoodsReference:[], // 删除关系
			addPictureGoodsReference:[], // 增加关系
			isUserPicture:2
		}
	},
	created() {
		const documentHeight = document.body.clientHeight;
		this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
		this.getPictureGoodsReferences();
		this.getPictureListCount();
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
		formatType(row, col, val){
			switch(val) {
				case 1:
				return "普通素材";
				break;
				case 2:
				return "IP素材";
				break;
			}
		},
		changeContent(val){
			if(val === undefined || val === '' || val === null){
				this.filter()
			}
		},
		getPictureList(){// 获取图片列表
			this.loading = true
			this.$api.get(this, 'admin/u/p/picture/references/list',this.pageInfo).then(res => {
				if(res.code === 0){
					this.tableData = res.pictures
					this.$refs.multipleSelect.clearSelection();
					this.selectPictureIds.forEach(row1 => {
						this.tableData.forEach(row2 => {
							if(row1.pictureId === row2.id){
								this.$refs.multipleSelect.toggleRowSelection(row2);
							}
						})
					})
				}
				this.loading = false
			})
		},
		getPictureListCount(){
			this.$api.get(this, 'admin/u/p/picture/references/list/count', this.pageInfo).then(res => {
				if(res.code === 0){
					this.total = res.count
				}
			})
		},
		getPictureGoodsReferences(){// 获取图片关联IDs
			this.$api.get(this, 'admin/u/p/picture/goods/references', { itemId:this.selectItemId }).then(res => {
				if(res.code === 0){
					this.selectPictureIds = res.pictureReferenceIds
					this.getPictureList()
				}
			})
		},
		changeFormCount(val){
			this.pageInfo.page = 1
			this.pageInfo.count = val
			this.getPictureList()
		},
		changeFormPage(val){
			this.pageInfo.page = val
			this.getPictureList()
		},
		selectable(row, index) { //..是否可勾选
			if(row.itemCode === undefined || row.itemCode === null || row.itemCode === '' || row.id !== this.selectItemId) {
				return true
			}else {
				return false
			}
		},
		selectRow(){ // 请求数据变化时，重新选择行（如，删除、数据变化）
			this.$refs.multipleSelect.clearSelection();
			this.selectPictureIds.forEach(row1 => {
				this.tableData.forEach(row2 => {
					if(row1.pictureId === row2.id){
						this.$refs.multipleSelect.toggleRowSelection(row2);
					}
				})
			});
		},
		select(selection, row){ // 单选时调用
			this.isSelect = true
			let d = false
			for(let i = 0;i<this.selectPictureIds.length;i++){
				if(this.selectPictureIds[i].pictureId === row.id){
					this.selectPictureIds.splice(i,1)
					d = true
					break
				}
			}
			if(!d){
				if(row.itemCode === undefined || row.itemCode === null || row.itemCode === '' || row.id !== this.selectItemId){
					this.$message.error("图片已关联，不能重复关联")
					this.selectRow()
					return
				}
				this.selectPictureIds.push({
					id:undefined,
					pictureId:row.id
				})
				
				this.selectPictureIds = this.setArr(this.selectPictureIds)
			}
		},
		selectAll(selection){ // 全选时调用
			this.isSelect = true
			let arr = []
			arr = arr.concat(selection)
			if(selection.length === 0){
				this.tableData.forEach(row => {
					for(let i = 0;i<this.selectPictureIds.length;i++){
						if(this.selectPictureIds[i].pictureId === row.id){
							this.selectPictureIds.splice(i,1)
							break
						}
					}
				})
			}else{
				for(let j = 0;j<arr.length;j++){
					if(arr.itemCode === undefined || arr.itemCode === null || arr.itemCode === '') {
						arr.splice(j,1)
						j--
						this.$message.error("图片已关联，不能重复关联")
					}else{
						this.selectPictureIds.push({
							id:undefined,
							pictureId:arr[j].id
						})
					}
				}
				if(arr.length != selection.length){
					this.selectRow()
				}
			}
		},
		setArr(arr){ // 去重
			let obj ={};  
			let temp=[];  
			for( let i = 0; i < arr.length; i++ ) {  
				let type= Object.prototype.toString.call(arr[i].pictureId);//不加类型 分不清 1 '1'    
					if( !obj[ arr[i].pictureId +type] ) {  
						temp.push( arr[i] );  
						obj[ arr[i].pictureId+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读  
					}
				} 
			return temp; 
		},
		handleSelectionChange(val) { // 当切换页面时的作用
			if(val.length === 0 && this.multipleSelect.length != 0 && !this.isSelect){
				this.multipleSelect.forEach(row1 => {
					this.tableData.forEach(row2 => {
						if(row1.itemErpId === row2.itemErpId){
							this.$refs.multipleSelect.toggleRowSelection(row2);
						}
					})
				});
				this.isSelect = true
			}
		},
		filter() { // 搜索操作
			this.pageInfo.page = 1
			this.pageInfo.count = 10
			this.getPictureList()
		},
		handleSubmit() { // 确定操作
			this.$emit('submit');
		},
		handleCancel() { // 返回操作
			this.$emit('cancel')
		}
	},
	watch: {
		selectItemId: {// 货品ID变化时重新请求
			handler() {
				this.pageInfo.page = 1
				this.getPictureGoodsReferences()
			},
			deep: true
		},
		'pageInfo.type': {
			handler() {
				this.pageInfo.page = 1
				this.getPictureList()
				this.getPictureListCount()
			},
			deep: true
		},
		'pageInfo.status': {
			handler() {
				this.pageInfo.page = 1
				this.getPictureList()
				this.getPictureListCount()
			},
			deep: true
		},
		'pageInfo.categoryId': {
			handler() {
				this.pageInfo.page = 1
				this.getPictureList()
				this.getPictureListCount()
			},
			deep: true
		},
	}
}

</script>
<style lang="scss" scoped>
.check-goods {
	background-color: white;
	.header-row {
		@include table-header-color;
	}
	.foot-btn {
		
		margin-top:50px;
	}
	.el-dialog__body{
		padding: 10px 20px;
		
	}
}

</style>
