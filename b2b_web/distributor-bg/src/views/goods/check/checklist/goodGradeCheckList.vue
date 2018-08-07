<template>
	<div class="check-list">
		<header>
          <h4>商品等级变动审批列表</h4>
        </header>
		<el-row class="nav-bar">
			<el-col :span=10>
				<el-radio-group v-model="pageInfo.checkType" size="mini">
					<el-radio-button :label="1">我发起的</el-radio-button>
					<el-radio-button :label="2">待我审批</el-radio-button>
					<el-radio-button :label="3">我已审批</el-radio-button>
				</el-radio-group>
			</el-col>
			<!-- <el-col :span="6" :offset="4">
				<el-input size='mini' v-model.trim="content"></el-input>
			</el-col>
			<el-col :span="3" :offset="1">
				<el-button size='mini' type="primary" @click="filter">搜索</el-button>
			</el-col> -->
		</el-row>
		<el-table :data="tableData" border header-row-class-name="header-row" style="text-align:center;" v-loading="loading">
			<el-table-column align="center" label="审批单号" prop="id" :min-width="120"></el-table-column>
			<el-table-column align="center" label="审批类型" prop="ext" :formatter="formatType" :min-width="120"></el-table-column>
			<el-table-column align="center" label="审批状态" prop="lastCheckTime" :formatter="FormType" :min-width="120"> </el-table-column>
			<el-table-column align="center" label="发起人" prop="applyUserName" :min-width="120"></el-table-column>
			<el-table-column align="center" label="发起时间" prop="createTime" :formatter="timeFormat" :min-width="120"></el-table-column>
			<el-table-column align="center" label="操作" :min-width="150">
				<template slot-scope="scope">
					<el-button class="mini-search-btn" @click="previewCheck(scope.row)">查看</el-button>
				</template>
			</el-table-column>
		</el-table>
		<page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
	</div>
</template>
<script>
import page from '@/components/pagination'
import PageHeader from "@/components/PageHeader"
import { timeFormat } from "@/utils/timeFormat"
export default {
	data() {
		return {
			pageInfo: {
				page: 1,
				count: 10,
				checkType: 1
			},
			tableData: [],
			content: '',
			checkTypeNum: '',
			total: 0,
			loading: false,
		}
		
	},
	components: { page, PageHeader },
	created(){
		this.getTableData()
	},
	activated() {
		this.getTableData()
	},
	methods: {
		// 搜索按钮
		filter() {
			this.checkType = this.pageInfo.checkType;
			this.pageInfo.content = this.content;
			this.getTableData()
		},
		sizeChange(size) { // 分页页数
			this.pageInfo.count = size;
			this.pageInfo.page = 1;
			this.getTableData()
		},
		
		currentChange(page) { // 分页总数
			this.pageInfo.page = page;
			this.getTableData()
		},
		getTableData() {
			this.loading = true
			this.$api.get(this, 'admin/u/p/item/grade/change/check/list', this.pageInfo).then(res => {
				res.code == 0 ? this.loading = false : this.loading = false
				if(res.code == 0) {
					let ary = [];
					res.checks.forEach(item => {
						ary.push(item.applyUser)
					})
					if(ary.length > 0) {
						this.$api.get(this, 'admin/u/po/admin/ids', { ids: ary.join(',') }).then(result => {
							res.checks.forEach(item => {
								result.admins.forEach(val => {
									if(val.id == item.applyUser) {
										item.applyUserName = val.name
									}
								})
							})
							this.tableData = res.checks
							this.checkTypeNum = res.checkType
						})
					} else {
						this.tableData = res.checks
					}
				}
			})
			this.$api.get(this,'admin/u/p/item/grade/change/check/count',this.pageInfo).then(res => { // 条数
				this.total = res.count
			})
		},
		//申请类型
		formatType(row, col, val){
			switch(val) {
				case 10:
					return '等级变更';
					break;
				default:
					return '-'
			}
		},
		timeFormat(row, col, val) {
			if(val !== 0) {
				return timeFormat(val)
			} else {
				return '审批中'
			}
		},
		// 审批状态
		FormType(row) {
			switch(row.status) {
				case 0: 
					return '未审批'
				case 1: 
					return '审批中'
				case 2: 
					return '已通过'
				case 3: 
					return '已拒绝'
			}
		},
		previewCheck(row){
			this.$router.push({name:'goodGradeCheckDetail',params:{id:row.id}})
		},
	},
	watch: {
		pageInfo: {
			handler() {
				// this.pageInfo.content = this.content;
				this.getTableData();
			},
			deep: true,
		}
	}
}

</script>
<style lang="scss" scoped>
.check-list {
	background-color: white;
	min-height: 100%;
	.nav-bar {
		padding: 10px;
	}
	.header-row {
		@include table-header-color;
	}
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
}

</style>
