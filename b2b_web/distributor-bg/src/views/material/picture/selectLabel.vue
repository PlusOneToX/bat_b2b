<template>
	<div class="check-goods">
		<div>
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
			<el-button style="margin-left: 10px;float: right; margin-bottom:10px;" class="mini-search-btn" @click="filter()">搜索</el-button>
			<el-input size="mini" style="width: 200px;float: right;" placeholder="标签名称" v-model.trim="pageInfo.content" @keyup.enter.native="filter()"></el-input>
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
			<el-table-column align="center" label="标签ID" prop="id" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="标签名称" prop="name" show-overflow-tooltip></el-table-column>
			<el-table-column align="center" label="标签类型" prop="type" :formatter="formatType" show-overflow-tooltip></el-table-column>
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
import {setArr} from '@/utils/common.js'
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
				openFlag:1,
				content:undefined,
				type:undefined,
			},
			total:0,
			statuss:[{
				status:1,
				label:'启用'
			},{
				status:0,
				label:'停用'
			}],
			types:[{
				type:1,
				label:'标准标签'
			},{
				type:2,
				label:'定制标签'
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
		this.getTableData()  
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
				return "标准标签";
				break;
				case 2:
				return "定制标签";
				break;
			}
		},
		selectable(row, index) { //..是否可勾选
			return true
		},
		getTableData() {
			this.$http.labelList(this, this.pageInfo).then(res => {	
				this.isSelect = false
				if(res.success) {
					this.tableData = res.data.list
					this.total = res.data.total
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
				this.multipleSelect.push(row)
				// this.multipleSelect = setArr(this.multipleSelect)
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
				this.multipleSelect = setArr(this.multipleSelect.concat(arr))
				if(arr.length != selection.length){
					this.selectRow()
				}
			
			}
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
		},
		handleSubmit() { // 确定操作
		console.log('msele==', this.multipleSelect);
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
		'pageInfo.type':{
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
	height: 60%;
	.header-row {
		@include table-header-color;
	}
	.foot-btn {
	}
	.el-dialog__body{
		padding: 10px 20px;
	}
}
</style>
