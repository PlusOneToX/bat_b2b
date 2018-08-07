<template>
	<div class="check-distributor">
		<div>
			<el-button style="float: right;margin-bottom: 10px;margin-left: 10px;" class="mini-search-btn" @click="filter">搜索</el-button>
			<el-input size="mini" style="width:20%;float: right;margin-bottom: 10px;line-height:0px;" v-model="content" placeholder="请输入关键词搜索"></el-input>
			<el-select size="mini"  v-model="contentType" placeholder="分销商用户名" style="width: 140px;float:right;margin-right:5px;" clearable>
				<el-option label="分销商用户名" value="1"> </el-option>
				<el-option label="客户名称" value="2"> </el-option>
				<el-option label="分销商ID" value="3"> </el-option>
			</el-select>
		</div>
		<el-table
			ref="multipleSelect"
			:data="tableData"
			tooltip-effect="dark"
			@select="select"
			@select-all="selectAll"
			style="text-align:center;"
			border header-row-class-name="header-row" 
			@selection-change="handleSelectionChange"
      v-loading="loading">
			<el-table-column align="center" type="selection" with="100" :selectable="checkSelectable"></el-table-column>
			<el-table-column align="center" label="分销商用户名" prop="name"></el-table-column>
			<el-table-column align="center" label="B2B编码" prop="id"></el-table-column>
			<el-table-column align="center" label="是否直发" prop="autoDelivery">
				<template slot-scope="scope">
					<div v-if="scope.row.autoDelivery===1">是</div>
					<div v-else>否</div>
				</template>
			</el-table-column>
			<el-table-column align="center" label="更新时间" prop="updateTime" :formatter="formatTime"></el-table-column>
		</el-table>
		<page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
		<el-button style="margin-left: 46%;margin-top: 10px;" class="mini-search-btn" @click="handleSubmit">确定</el-button>
		<el-button size="mini" style="margin-top: 10px;" @click="handleCancel">返回</el-button>
	</div>
</template>
<script>
import page from "@/components/pagination";
import { timeFormat } from '@/utils/timeFormat.js'
export default {
	props: ['distributorData', 'isFinancial', 'appType'],
	data() {
		return {
			pageInfo: {
				page: 1,
				size: 10,
				freezeStatus: 1,
				profileStatus: 2
			},
			tableData: [],
			total: 0,
			grades: [],
			multipleSelect: [],
			content: '',
			contentType: '',
			isSelect:false,
      selected:[],
      loading: false
		}
	},
	components: { page },
	created() {
		this.multipleSelect = []
		this.multipleSelect = this.multipleSelect.concat(this.distributorData)
		this.getTableData();
	},
	methods: {
		checkSelectable(row) {
			return row.isDisabled !== 1
		},
		selectRow(){//分销商请求数据变化时，重新选择行（如，删除、数据变化）
			this.$refs.multipleSelect.clearSelection();
			this.multipleSelect.forEach(row1 => {
				this.tableData.forEach(row2 => {
					if(row1.id === row2.id){
						this.$refs.multipleSelect.toggleRowSelection(row2);
					}
				})
			});
		},
		getTableData() {
      		this.loading = true
			if(this.grades.length == 0) {
				this.$http.getGradePoList(this, { page: 1, size: 1000, openFlag: 1 }).then(res => {
					if (res.success) {
						this.grades = res.data.list
					}
				})
			}
		  this.$http.getDistributorPoList(this, this.pageInfo).then(res => {
				this.isSelect = false
				this.selected = []
				if(res.success) {
					res.data.list.forEach(item => {
						this.grades.forEach(val => {
							if(val.id == item.gradeId) {
								item.gradeName = val.name
							}
						})
					})
					// 财务支付判断分销商是否可用
					if (this.isFinancial) {
						this.checkDistributor(this.isFinancial).then(res2 => {
							if (res.success) {
								// 解禁当前选中分销商
								let arr1 = []
								if (this.distributorData.length > 0) {
									this.distributorData.forEach(item => {
										arr1.push(item.id)
									})
								}
								let arr2 = res2.data
								if (arr2.length > 0) {
									let RemoveSame=[...new Set([...arr1,...arr2])]  // 合并
									let SamePart=arr1.filter(item=>arr2.includes(item))  // 获取重复数据
									let differentce = RemoveSame.filter(item => !SamePart.includes(item))
									this.tableData = []
									res.data.list.forEach(tab => {
										if (differentce.join(',').indexOf(tab.id) > -1) {
											this.tableData.push({
												applyType: tab.applyType,
												autoDelivery: tab.autoDelivery,
												companyName: tab.companyName,
												companyType: tab.companyType,
												createTime: tab.createTime,
												freezeStatus: tab.freezeStatus,
												id: tab.id,
												name: tab.name,
												profileStatus: tab.profileStatus,
												treeNode: tab.treeNode,
												updateTime: tab.updateTime,
												isDisabled: 1
											})
										} else {
											this.tableData.push(tab)
										}
									})
								} else {
									this.tableData = res.data.list;
								}
								this.total = res.data.total;
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
					} else {
						this.tableData = res.data.list;
						this.total = res.data.total;
						this.multipleSelect.forEach(row1 => {//重新获取数据时，判断哪些选中了
							this.tableData.forEach(row2 => {
								if(row1.id === row2.id){
									this.$refs.multipleSelect.toggleRowSelection(row2);
									this.selected.push(row2)
								}
							})
						});
					}
        }
        res.success ? this.loading = false : this.loading = false
			})
		},
		checkDistributor (type) {
			if (type === 1) { // 微信
				return new Promise((resolve, reject) => {
					this.$http.checkDistributorWx(this, {appType: this.appType}).then(res => {
						if (res.success) {
							resolve(res)
						} else {
							reject()
						}
				})
				})
			} else if (type === 2) { // 支付宝
				return new Promise((resolve, reject) => {
					this.$http.checkDistributorAlipay(this).then(res => {
						if (res.success) {
							resolve(res)
						} else {
							reject()
						}
					})
				})
			}
		},
		handleSubmit() {
			this.$emit('submit', this.multipleSelect)
		},
		handleCancel() {
			this.multipleSelect = []
			this.multipleSelect = this.multipleSelect.concat(this.distributorData)
			this.$emit('cancel');
			this.selectRow()
		},
		select(selection, row){//单选时调用
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
				this.multipleSelect = this.setArr(this.multipleSelect)
			}
		},
		selectAll(selection){//全选时调用
			this.isSelect = true
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
				this.multipleSelect = this.setArr(this.multipleSelect.concat(selection))
			}
		},
		handleSelectionChange(val) {//当切换页面时的作用
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
		formatTime(row, col, val) { // 表格时间格式化
      return timeFormat(val)
    },
		//去重
		setArr(arr){
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
		filter() {
			this.pageInfo.content = this.content
			this.pageInfo.contentType = this.contentType
			this.pageInfo.page = 1
			this.getTableData();
		},
		// 分页页数
		sizeChange(size) {
			this.pageInfo.size = size;
		},
		// 分页总数
		currentChange(page) {
			this.pageInfo.page = page;
		}
	},
	watch: {
		pageInfo: {
			handler() {
				this.getTableData()
			},
			deep: true
		},
		distributorData: {//请求的分销商数据变化时
			handler() {
				this.multipleSelect = []
				this.multipleSelect = this.multipleSelect.concat(this.distributorData)
				this.selectRow()
			},
			deep: true
		},
		appType: {
			handler(val) {
				this.appType = val
				this.getTableData()
			},
			deep: true
		}
	}
}
</script>
<style lang="scss" scoped>
.check-distributor {
	background-color: white;
	min-height: 100%;
	.header-row {
		@include table-header-color;
	}
	.foot-btn {
		margin-top: 50px;
	}
}

</style>
