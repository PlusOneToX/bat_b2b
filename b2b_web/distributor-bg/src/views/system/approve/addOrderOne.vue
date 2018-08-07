<template>
	<div class="adduser">
			<div>
				<el-button style="float: right;margin-left:5px;margin-bottom:10px;" class="mini-search-btn" @click="filter">搜索</el-button>
				<el-input size="mini" style="float: right;width:250px;" placeholder="姓名/用户名/用户ID" v-model.trim="pageInfo.content"></el-input>
			</div>
			<el-table :data="allSales" border header-row-class-name="header-row" class="adduser-table" ref='multipleSelectFour'  @selection-change="handleSelectionChange">
				<el-table-column type="selection" width="55"></el-table-column>
				<el-table-column label="用户名" prop="label"></el-table-column>
			</el-table>
			<page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
				<el-button class="mini-search-btn" style="margin-left: 45%;margin-top:20px;"  autofocus @click="multipleSelectFour"> 确定</el-button>
				<el-button autofocus size="mini" >返回</el-button>
	</div>
</template>

<script>
import page from "@/components/pagination/index"
import {adminAddData,adminCountData} from "@/views/system/systemData";
	export default {
		data() {
			return {
				allSales: [], // 用户名数组
				total: 0,
				pageInfo: {
					page: 1,
					count: 10,
					status:1,
					content:''
				},
				selected:[],
				isSelect:false,
        		saleOrganizationList: []
			}
		},
		components: { page },
		mounted() {
			this.userList();
			this.userListCount();
		},
		methods: {
			filter(){
				this.pageInfo.page = 1;
				this.userList();
				this.userListCount();
			},
			// 选中的用户名值
			multipleSelectFour() {
				this.$emit('submitFive',this.multipleSelectTwo);
			},
			// 返回
			handleBack() {
				this.multipleSelect = []
			  	this.multipleSelect = this.multipleSelect.concat(this.selectUsers)
				this.$emit('submitBack')
			},
			userList() {  // 用户列表
				adminAddData(this,this.pageInfo).then(res => {
					this.allSales = [];
					this.allSales = res.admins
					this.isSelect = false
					this.multipleSelect.forEach(row1 => {//重新获取数据时，判断哪些选中了
						this.allSales.forEach(row2 => {
							if(row1.id === row2.id){
								this.$refs.multipleSelect.toggleRowSelection(row2);
								this.selected.push(row2)
							}
						})
					});
					if(this.saleOrganizationList.length === 0){
						this.getOrgList()
					}
				})
			},
			userListCount() {  // 用户列表总数
				adminCountData(this,{status:this.pageInfo.status, content:this.pageInfo.content}).then(res => {
					this.total = res.count;
				})
			},
			getOrgList(){//销售组织初始化
				this.$api.get(this,'admin/u/po/organization/list',{page:1,count:100}).then(res => {
					this.saleOrganizationList = res.organizations;
					this.allSales.forEach(item =>{
						for(let i = 0;i<this.saleOrganizationList.length;i++){
						if(item.organizationId === this.saleOrganizationList[i].id){
							item.organizationId = this.saleOrganizationList[i].name
							break
						}
						}
					})
				})
			},
			handleSelectionChange(val){  // 勾选中的值
				this.multipleSelectTwo = val;
			},
			
			sizeChange(size) {  // 分页
				this.pageInfo.count = size;
				this.userList();
				this.userListCount();
			},
		
			currentChange(page) { 	// 分页
				this.pageInfo.page = page;
				this.userList();
				this.userListCount();
			},
		}
	}
</script>

<style lang="scss">
	.adduser-table {
		text-align: center;
	}
</style>
