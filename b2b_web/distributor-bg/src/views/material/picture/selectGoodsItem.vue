<template>
	<div class="check-goods">
		<div>
			<el-select v-model="pageInfo.materialType" placeholder="材料类型" clearable size="mini" style="width: 150px;">
				<el-option
				v-for="item in categorys"
				:key="item.id"
				:label="item.name"
				:value="item.id">
				</el-option>
			</el-select>
			<el-select v-model="pageInfo.materialId" placeholder="选择材质" clearable size="mini" style="width: 150px;">
				<el-option
				v-for="item in materialList"
				:key="item.id"
				:label="item.name"
				:value="item.id">
				</el-option>
			</el-select>

			<el-select placeholder="上架状态" size="mini" v-model="pageInfo.saleStatus" clearable>
				<el-option v-for="item in state" :key="item.value" :label="item.label" :value="item.value"> </el-option>
			</el-select>
			<el-button style="margin-left: 10px;float: right; margin-bottom:10px;" class="mini-search-btn" @click="filter()">搜索</el-button>
			<el-input size="mini" style="width: 300px;float: right;" placeholder="商品编码/商品名称/货品名称/存货编码" v-model.trim="pageInfo.content" @keyup.enter.native="filter()"></el-input>
		</div>
			<el-table 
			border 
			class="tableCenter"
			:data="tableData"
			ref="multipleSelect" 
			tooltip-effect="dark" 
			max-height="600px"
			@select="select" 
			@select-all="selectAll" 
			header-row-class-name="header-row" 
			@selection-change="handleSelectionChange">
				<el-table-column align="center" type="selection" width="55" :selectable="selectable"></el-table-column>
				<el-table-column align="center" label="货品名称" prop="itemName" show-overflow-tooltip></el-table-column>
				<el-table-column align="center" label="货品编号" prop="itemCode" show-overflow-tooltip></el-table-column>
				<el-table-column align="center" label="条形码" prop="barCode" show-overflow-tooltip></el-table-column>
				<el-table-column align="center" label="材料类型" prop="materialType" :formatter="formatCategory" show-overflow-tooltip></el-table-column>
				<el-table-column align="center" label="材质" prop="materialName" show-overflow-tooltip></el-table-column>
				<el-table-column align="center" label="上架状态" prop="saleStatus" :formatter="formatStatus" show-overflow-tooltip></el-table-column>
			</el-table>
			<page :page="pageInfo.page"  :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
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
		selectItemsData: Array,
	},
	data() {
		return {
			tableHeight: 600, //给表格高度一个默认高度，以防没有计算到表格高度
			content: '',
			pageInfo: {
				page: 1,
				size: 10,
				goodsType:3,
				materialType:undefined,
				materialId:undefined,
				content:undefined,
				saleStatus:3
			},
			total:0,
			categorys:[],
			state: [
				{ value: 3, label: '已上架'},
				{ value: 1, label: '已下架' },
				{ value: 2, label: '上架审批中'},
				{ value: 4, label: '下架审批中'}
			],
			tableData: [],
			total: 0,
			loading: false,
			multipleSelect:[],
			selected:[],
			isSelect:false,
			materialList:[],
		}
	},
	components: { page },
	created() {
		const documentHeight = document.body.clientHeight;
		this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
		this.multipleSelect = []
		if(this.selectItemsData !== undefined && this.selectItemsData.length>0){
			this.multipleSelect = this.multipleSelect.concat(this.selectItemsData) // 第一次进入时
		}
		this.proCategoryList()
		this.getmaterialList()
		this.getTableData()
		this.getTotalCount()
	},
	methods: {
		formatStatus(row, col, val) { // 上架状态
			switch(val) {
				case 1:
				return "下架";
				break;
				case 2:
				return "上架审批中";
				break;
				case 3:
				return "上架";
				break;
				case 4:
				return "下架审批中";
				break;
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
		selectable(row, index) { //..是否可勾选
			return true
		},
		proCategoryList () { 	// 材料类型下拉列表
			this.$http.productUsableList(this).then(res => {
				if (res.success) {
					this.categorys = res.data
				}
			})
		},
		getmaterialList(){// 获取材质列表
			this.$api.get(this, 'admin/u/p/material/search', {materialType:this.pageInfo.materialType,page:1,size:1000,status:1}).then(res => {
				if(res.code === 0){
					this.materialList = res.data.content
				}
			})
		},
		getTableData() { // 型号列表
			this.$api.get(this, 'admin/u/po/goods/item/list', this.pageInfo).then(res => {
				this.isSelect = false
				if(res.code === 0){
					this.tableData = res.items
					this.multipleSelect.forEach(row1 => {//重新获取数据时，判断哪些选中了
						this.tableData.forEach(row2 => {
							if(row1.id === row2.id){
								this.$refs.multipleSelect.toggleRowSelection(row2);
								this.selected.push(row2)
							}
						})
					});
				}
			})
		},
		getTotalCount(){
			this.$api.get(this, 'admin/u/po/goods/item/count', this.pageInfo).then(res => {
				if(res.code === 0){
					this.total = res.count
				}
			})
		},
		selectRow(){ // 请求数据变化时，重新选择行（如，删除、数据变化）
			this.$refs.multipleSelect.clearSelection();
			if(this.multipleSelect !== undefined && this.multipleSelect.length > 0){
				this.multipleSelect.forEach(row1 => {
					this.tableData.forEach(row2 => {
						if(row1.id === row2.id){
							this.$refs.multipleSelect.toggleRowSelection(row2);
						}
					})
				});
			}
		},
		select(selection, row){ // 单选时调用
			this.isSelect = true
			let d = false
			for(let i = 0;i<this.multipleSelect.length;i++){
				if(this.multipleSelect[i].id === row.id){
					this.multipleSelect.splice(i,1)
					d = true
					break
				}
			}
			if(!d){
				for(let i = 0;i<this.multipleSelect.length;i++){
					if(this.multipleSelect[i].materialId === row.materialId){
						this.$message.error("图片已关联材质 "+row.materialName+" SKU，不能重复关联")
						this.selectRow()
						return
					}
				}
				this.multipleSelect.push(row)
				this.multipleSelect = this.setArr(this.multipleSelect)
			}
    	},
    
		selectAll(selection){ // 全选时调用
			this.isSelect = true
			let arr = []
			arr = arr.concat(selection)
			if(selection.length === 0){
				this.tableData.forEach(row => {
					for(let i = 0;i<this.multipleSelect.length;i++){
						if(this.multipleSelect[i].id === row.id){
							this.multipleSelect.splice(i,1)
							break
						}
					}
				})
			}else{
				for(let j = 0;j<arr.length;j++){
					let b = false
					for(let i = 0;i<this.multipleSelect.length;i++){
						if(this.multipleSelect[i].materialId === arr[j].materialId){
							b = true
							break
						}
					}
					if(!b){
						this.multipleSelect.push(arr[j])
						arr.splice(j,1)
						j--;
					}
				}
				this.multipleSelect = this.setArr(this.multipleSelect)
				if(arr.length !== 0){
					this.selectRow()
				}
			
			}
		},
		
		setArr(arr){ // 去重
			let obj ={};
			let temp=[];  
			for( let i = 0; i < arr.length; i++ ) {  
				let type= Object.prototype.toString.call(arr[i].id);//不加类型 分不清 1 '1'    
				if( !obj[ arr[i].id +type] ) {  
					temp.push( arr[i] );  
					obj[ arr[i].id+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读  
				}
			} 
			return temp; 
		},
		sizeChange(size) {
			this.pageInfo.count = size
			this.pageInfo.page = 1
			this.getTableData()
		},
		currentChange(page) {
			this.pageInfo.page = page;
			this.getTableData()
		},
		handleSelectionChange(val) { // 当切换页面时的作用
			if(val.length === 0 && this.multipleSelect.length != 0 && !this.isSelect){
				this.multipleSelect.forEach(row1 => {
					this.tableData.forEach(row2 => {
						if(row1.id === row2.id){
							this.$refs.multipleSelect.toggleRowSelection(row2);
						}
					})
				});
				this.isSelect = true
			}
		},
		filter() { // 搜索操作
			this.pageInfo.page = 1
			this.getTableData()
			this.getTotalCount()
		},
		handleSubmit() { // 确定操作
			this.$emit('submit',this.multipleSelect);
		},
		handleCancel() { // 返回操作
			this.multipleSelect = []
			if(this.selectItemsData !== undefined && this.selectItemsData.length > 0){
				this.multipleSelect = this.multipleSelect.concat(this.selectItemsData)
			}
			this.selectRow()
			this.$emit('cancel',this.multipleSelect)
		}
	},
	watch: {
		selectItemsData: { // 请求的分销商数据变化时
			handler() {
				this.multipleSelect = []
				if(this.selectItemsData !== undefined && this.selectItemsData.length >0){
					this.multipleSelect = this.multipleSelect.concat(this.selectItemsData)
				}
				this.selectRow()
			},
			deep: true
		},
		'pageInfo.materialType':{
			handler() {
				this.pageInfo.page = 1
				this.getTableData()
				this.getTotalCount()
			},
			deep: true
		},
		'pageInfo.materialId':{
			handler() {
				this.pageInfo.page = 1
				this.getTableData()
				this.getTotalCount()
			},
			deep: true
		},
		'pageInfo.saleStatus':{
			handler() {
				this.pageInfo.page = 1
				this.getTableData()
				this.getTotalCount()
			},
			deep: true
		}
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
		margin-top:10px;
	}
	.el-dialog__body{
		padding: 10px 20px;
	}
}
</style>
