<template>
	<div class="adduser">
		<el-row>
			<el-table :data="allSales" border header-row-class-name="header-row" class="adduser-table" ref='multipleSelectFive'  @selection-change="handleSelectionChange">
				<el-table-column type="selection" width="55"></el-table-column>
				<el-table-column label="用户名" prop="label"></el-table-column>
			</el-table>
			<page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
		</el-row>
		<el-row>
			<el-col :span="2" :offset="9">
				<el-button autofocus class="mini-search-btn" @click="multipleSelectFive"> 确定</el-button>
			</el-col>
			<el-col :span="2" :offset="2">
				<el-button autofocus size="mini" >返回</el-button>
			</el-col>
		</el-row>
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
					count: 10
				},
			}
		},
		components: { page },
		mounted() {
			this.userList();
			this.userListCount();
		},
		methods: {
			// 选中的用户名值
			multipleSelectFive() {
				this.$emit('submitSix',this.multipleSelectTwo);
			},
			// 返回
			handleBack() {
				this.$emit('submitBack')
			},

			userList() {  // 用户列表
				adminAddData(this,this.pageInfo).then(res => {
					this.allSales = [];
					res.admins.forEach(item => {
						let obj = {
							id: item.id,
							label: item.name,
							disabled: false,
						}
						this.allSales.push(obj)
					})
				})
			},

			userListCount() {  // 用户列表总数
				adminCountData(this).then(res => {
					this.total = res.count;
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
