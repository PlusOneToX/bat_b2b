<template>
	<div class="check-goods">
		<div>
			<el-select v-model="pageInfo.openFlag" placeholder="状态" clearable size="mini" style="width: 100px;">
				<el-option
				v-for="item in statuss"
				:key="item.status"
				:label="item.label"
				:value="item.status">
				</el-option>
			</el-select>
			<el-button style="margin-left: 10px;float: right; margin-bottom:10px;" class="mini-search-btn" @click="filter()">搜索</el-button>
			<el-input size="mini" style="width: 200px;float: right;" clearable placeholder="材料名称" @change="changeContent" v-model.trim="pageInfo.content" @keyup.enter.native="filter()"></el-input>
		</div>
			<el-table 
			border 
			class="tableCenter"
			:data="tableData" 
			ref="multipleSelect" 
			tooltip-effect="dark" 
			@select="select" 
			@select-all="selectAll" 
			header-row-class-name="header-row" 
			:height="tableHeight"
			@selection-change="handleSelectionChange">
				<el-table-column align="center" type="selection" width="55" :selectable="selectable"></el-table-column>
				<el-table-column align="center" label="材料ID" prop="materialId" show-overflow-tooltip></el-table-column>
				<el-table-column align="center" label="材料名称" prop="name" show-overflow-tooltip></el-table-column>
				<el-table-column align="center" label="材料类型" prop="categoryName" show-overflow-tooltip></el-table-column>
				<el-table-column align="center" label="状态" prop="openFlag" :formatter="formatStatus" show-overflow-tooltip></el-table-column>
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
		categoryId: {
			type: Number,
		},
		selectItemsData: Array
	},
	data() {
		return {
			tableHeight: 600, //给表格高度一个默认高度，以防没有计算到表格高度
			content: '',
			pageInfo: {
				page: 1,
				size: 10,
				categoryId:undefined,
				content:undefined,
				openFlag:1
			},
			total:0,
			statuss:[{
				status:1,
				label:'启用'
			},{
				status:0,
				label:'停用'
			}],
			tableData: [],
			total: 0,
			loading: false,
			multipleSelect:[],
			selected:[],
			isSelect:false,
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
		this.pageInfo.categoryId = this.categoryId
		this.getTableData()  
	},
	methods: {
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
				this.filter()
			}
		},
		selectable(row, index) { //..是否可勾选
			return true
		},
		getTableData() { // 材质列表
			this.$http.materialLowestList(this, this.pageInfo).then(res => {	
				this.isSelect = false
				if(res.success){
					 res.data.list.forEach(row2 => {
						row2.modelId = ''
						row2.bomCode = ''
						row2.modelName = ''
						row2.materialId = row2.id
						row2.materialName = row2.name
						row2.categoryName = row2.parentName
						row2.length = ''
						row2.width = ''
						row2.height = ''
						row2.weight = ''
						row2.thirdSku = ''
						row2.outImage = ''
						row2.floorImage = ''
						row2.openFlag = 1
					})
					this.tableData = res.data.list
					this.total = res.data.total
					this.multipleSelect.forEach(row1 => {//重新获取数据时，判断哪些选中了
						this.tableData.forEach(row2 => {
							if(row1.materialId === row2.materialId){
								row2.modelId = row1.modelId
								row2.modelName = row1.modelName
								row2.bomCode = row1.bomCode
								row2.length = row1.length
								row2.width = row1.width
								row2.height = row1.height
								row2.weight = row1.weight
								row2.thirdSku = row1.thirdSku
								row2.outImage = row1.outImage
								row2.floorImage = row1.floorImage
								row2.openFlag = row1.openFlag
								this.$refs.multipleSelect.toggleRowSelection(row2);
								this.selected.push(row2)
							}
						})
					});
				}
			})
		},
		selectRow(){ // 请求数据变化时，重新选择行（如，删除、数据变化）
			this.$refs.multipleSelect.clearSelection();
			if(this.multipleSelect !== undefined && this.multipleSelect.length > 0){
				this.multipleSelect.forEach(row1 => {
					this.tableData.forEach(row2 => {
						if(row1.materialId === row2.materialId){
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
				if(this.multipleSelect[i].materialId === row.materialId){
					this.multipleSelect.splice(i,1)
					d = true
					break
				}
			}
			if(!d){
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
						if(this.multipleSelect[i].materialId === row.materialId){
							this.multipleSelect.splice(i,1)
							break
						}
					}
				})
			}else{
				this.multipleSelect = this.setArr(this.multipleSelect.concat(arr))
				if(arr.length != selection.length){
					this.selectRow()
				}
			
			}
		},
		
		setArr(arr){ // 去重
			let obj ={};
			let temp=[];  
			for( let i = 0; i < arr.length; i++ ) {  
				let type= Object.prototype.toString.call(arr[i].materialId);//不加类型 分不清 1 '1'    
				if( !obj[ arr[i].materialId +type] ) {  
					temp.push( arr[i] );  
					obj[ arr[i].materialId+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读  
				}
			} 
			return temp; 
		},
		sizeChange(size) {
			this.pageInfo.size = size
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
						if(row1.materialId === row2.materialId){
							this.$refs.multipleSelect.toggleRowSelection(row2);
						}
					})
				});
				this.isSelect = true
			}
		},
		filter() { // 搜索操作
			this.pageInfo.page = 1
			this.pageInfo.content
			this.getTableData()
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
			this.$emit('cancel')
		}
	},
	watch: {
		categoryId(val){
			this.pageInfo.categoryId = val
			this.pageInfo.page = 1
			this.getTableData()
		},
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
		'pageInfo.categoryId':{
			handler() {
				this.pageInfo.page = 1
				this.getTableData()
			},
			deep: true
		},
		'pageInfo.openFlag':{
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
