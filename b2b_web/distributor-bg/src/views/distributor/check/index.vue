<template>
	<div class="check-list">
		<header>
		  <h4>分销商审批列表</h4>
		</header>
		<div class="nav-bar">
			<div class="nav-bar-group">
				<el-radio-group v-model="checkType" size="mini">
					<el-radio-button :label="1">我发起的</el-radio-button>
					<el-radio-button :label="2">待我审批</el-radio-button>
					<el-radio-button :label="3">我审批的</el-radio-button>
				</el-radio-group>
			</div>
			<div class="Fsearch">
				<el-select size="mini" change="el-select"  v-model="pageInfo.contentType" placeholder="分销商用户名" style="width: 140px;" clearable>
					<el-option label="分销商用户名" value="1"> </el-option>
					<el-option label="客户名称" value="2"> </el-option>
					<el-option label="分销商ID" value="3"> </el-option>
				</el-select>
				<button class="mini-search-btn box-btn" @click="Csearch()">搜索</button>
				<el-input v-model.trim="mytext" size="mini" clearable @change="contentChange" @keyup.enter.native="Csearch()" placeholder="请输入关键词搜索" class="box-input"></el-input>
			</div>
		</div>
		
		<el-table :data="tableData" border header-row-class-name="header-row" class="tableCenter" v-loading="loading" :height="tableHeight">
			<el-table-column type="index" fixed :min-width="50"></el-table-column>
			<el-table-column label="审批单号" align="center" prop="checkId" :min-width="120"></el-table-column>
			<el-table-column label="分销商编号" align="center" prop="id" :min-width="120"></el-table-column>
			<el-table-column label="ERP编号" align="center" prop="erpDistributorNo" :min-width="120"></el-table-column>
			<el-table-column label="分销商用户名" align="center" prop="name" :min-width="120"></el-table-column>
			<el-table-column label="审批状态" align="center" prop="status" :formatter="formStatus" :min-width="120"></el-table-column>
			<el-table-column label="审批类型" align="center" prop="checkType" :formatter="formCheckType" :min-width="120"></el-table-column>
			<el-table-column label="发起人" align="center" prop="applyName" :min-width="120"></el-table-column>
			<el-table-column label="发起时间 " align="center" prop="checkCreateTime" :formatter="timeFormat" :min-width="150"></el-table-column>
			<el-table-column label="操作" align="center" :min-width="120">
				<template slot-scope="scope">
					<el-button class="mini-search-btn" @click="previewCheck(scope.row)">查看</el-button>
				</template>
			</el-table-column>
		</el-table>
		<page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
	</div>
</template>
<script>
import PageHeader from "@/components/PageHeader";
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat"
export default {
	name: 'distributorcheck',
	data() {
		return {
			tableHeight: 600,
      loading: false,
			pageInfo: {
				page: 1,
				count: 10
			},
			checkType: 1,
			total: 0,
			tableData: [],
			mytext: "",
		}
	},
	components: { page, PageHeader },
	created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
	},
	activated() {
		this.getTableData()
	},
	methods: {
	contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.Csearch()
      }
    },
    // ======== 操作 ========
    Csearch() { // 搜索
      this.pageInfo.page = 1
      this.pageInfo.content=this.mytext;
      this.pageInfo.status = this.status;
      this.getTableData()
    },

    previewCheck(row){ // 查看操作
			this.$router.push({ name: 'distributorcooperatingadd',
				query:{ id: row.distributorCheckInfoId, checkMsg: 2, checkType: this.pageInfo.checkType, capitalStatus: row.status,distributorId: row.id}
			})
		},
    
    // ======== 数据 ========
    getTableData() { // 详情
      this.loading = true;
		this.pageInfo.checkType = this.checkType
		this.tableData =[]
		this.$api.get(this, 'admin/u/p/distributor/cooperating/checklist', this.pageInfo).then(res => {
			if(res.code == 0) {
				let ary = [];
				res.distributors.forEach(item => {
					ary.push(item.id)
				})
				if(ary.length > 0) {
					this.$api.get(this, 'admin/u/po/admin/ids', { ids: ary.join(',') }).then(result => {
						res.distributors.forEach(item => {
							result.admins.forEach(val => {
								if(val.id == item.id) {
									item.id = val.name
								}
							})
						})
						this.tableData = res.distributors
					})
				}else {
					this.tableData = res.distributors
				}
				this.loading = false
			}else {
				this.loading = fasle
			}
		})
		this.pageInfo.checkType = this.checkType,
		this.$api.get(this, 'admin/u/p/distributor/cooperating/checklist/count', this.pageInfo).then(res => { // 条数
			this.total = res.count
		})
    },
    
		sizeChange(size) { // 分页页数
	  	this.pageInfo.count = size;
	  	this.getTableData();
		},
	
		currentChange(page) { // 分页总数
			this.pageInfo.page = page;
			this.getTableData();
		},
		// ======== 转换 ========
		timeFormat(row, col, val) { // 表格时间戳转换
			return timeFormat(val)
		},
		
		formStatus(row,col,val) { // 审批状态
			switch(row.status) {
				case 0:
					return '未审批'
					break;
				case 1:
					return '审批中'
					break;
				case 2:
					return '已通过'
					break;
				case 3:
					return '已拒绝'
					break;
				default:
					return '_'
			}
		},
		
		formCheckType(row,col,val) { // 审批类型
			switch(row.checkType) {
				case 1:
					return '分销商新增审批'
					break;
				case 2:
					return '分销商编辑审批'
					break;
				default:
					return '_'
			}
    }
	},
	watch: {
		checkType: {
			handler() {
				this.getTableData();
			},
			deep: true,
		}
	}
}

</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell {
  overflow: hidden;
  white-space:nowrap;
}
.check-list {
	background-color: white;
	min-height: 100%;
	header {
		color: white;
		height: $lineHight;
		line-height: $lineHight;
		background-color: $lakeBlue;
		justify-content: space-between;
    h4 {
      display: inline-block;
      font-weight: 350;
      font-size: 16px;
	  margin: 0 20px;
    } 
	}
	.nav-bar {
		padding: 10px;
		// overflow: hidden;
		// display: inline-block;
		.nav-bar-group {
			display: inline-block;
		}
		.Fsearch {
			overflow: hidden;
			float: right;
			.el-select{
        /deep/.el-input__inner{
          border-top-right-radius: 0;
          border-bottom-right-radius: 0;
        }
      }
			.box-input {
				width: 220px;
				float: right;
				/deep/.el-input__inner{
          border-top-left-radius: 0;
          border-bottom-left-radius: 0;
          border-left-width: 0;
        }
			}
			.box-btn {
				float: right;
				margin-left: 5px;
			}
		}
	}
}
</style>
