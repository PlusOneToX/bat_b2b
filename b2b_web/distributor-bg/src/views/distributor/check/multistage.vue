<template>
	<div class="check-list">
		<header>
		  <h4>分销商审批列表</h4>
		</header>
		<div class="nav-bar">
			<div class="nav-bar-group">
				<el-radio-group v-model="labelType" size="mini">
					<el-radio-button :label="1">我发起的</el-radio-button>
					<el-radio-button :label="2">待我审批</el-radio-button>
					<el-radio-button :label="3">我审批的</el-radio-button>
				</el-radio-group>
			</div>
			<div class="Fsearch">
				<button class="mini-search-btn box-btn" @click="Csearch()">搜索</button>
				<el-input v-model.trim="mytext" size="mini" clearable @change="contentChange" @keyup.enter.native="Csearch()" placeholder="搜索关键字" class="box-input"></el-input>
				<el-select size="mini"  v-model="pageInfo.contentType" placeholder="分销商用户名" style="width: 140px;" clearable>
					<el-option label="分销商用户名" value="1"> </el-option>
					<el-option label="客户名称" value="2"> </el-option>
					<el-option label="分销商ID" value="3"> </el-option>
				</el-select>
			</div>
		</div>
		
		<el-table :data="tableData" border header-row-class-name="header-row" class="tableCenter" v-loading="loading" :height="tableHeight">
			<el-table-column type="index" fixed :min-width="50"></el-table-column>
			<el-table-column label="审批单号" align="center" prop="id" :min-width="120"></el-table-column>
			<el-table-column label="分销商编号" align="center" prop="distributorId" :min-width="120"></el-table-column>
			<el-table-column label="ERP编号" align="center" prop="erpNo" :min-width="120"></el-table-column>
			<el-table-column label="分销商级数" align="center" prop="treeNode" :min-width="120"></el-table-column>
			<el-table-column label="分销商用户名" align="center" prop="name" :min-width="120"></el-table-column>
			<el-table-column label="审批状态" align="center" prop="checkStatus" :formatter="formStatus" :min-width="120"></el-table-column>
			<el-table-column label="审批类型" align="center" prop="checkType" :formatter="formCheckType" :min-width="120"></el-table-column>
			<el-table-column label="发起人" align="center" prop="userName" :min-width="120"></el-table-column>
			<el-table-column label="发起时间 " align="center" prop="createTime" :formatter="timeFormat" :min-width="180"></el-table-column>
			<el-table-column label="操作" align="center" :min-width="120" fixed="right">
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
				size: 10,
			},
			labelType : 1,
			total: 0,
			tableData: [],
			mytext: "",
		}
	},
	components: { page, PageHeader },
	created() {
		const documentHeight = document.body.clientHeight;
		this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
		this.getTableData()
	},
	activated() {
		
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
			if (row.checkType === 1) {
				// 新增
				// this.$router.push({ name: 'checkAdd',
				// 	query:{ id: row.id, checkMsg: 1, labelType : this.pageInfo.labelType, capitalStatus: row.status,distributorId: row.id}
				// })
				this.$router.push({ name: 'checkAdd',
					query:{ id: row.id, checkMsg: 1, node: row.treeNode}
				})
			} else {
				// 修改
				this.$router.push({ name: 'checkDetail', query: {id: row.id} })
			}	
			
		},
		
		// ======== 数据 ========
		getTableData() { // 详情
		this.loading = true;
			this.pageInfo.labelType = this.labelType
			this.tableData =[]
			this.$http.distributorCheckList(this, this.pageInfo).then(res => {		
				if(res.success) {
					this.tableData = res.data.list
					this.total = res.data.total
					this.loading = false
				}else {
					this.loading = fasle
				}
			})
		},
    
		sizeChange(size) { // 分页页数
	  	this.pageInfo.size = size;
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
			switch(row.checkStatus) {
				case 0:
					return '审批中'
					break;
				case 1:
					return '审批通过'
					break;
				case 2:
					return '审批未通过'
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
		labelType: {
			handler(val) {
				if (val === 2) {
					this.pageInfo.checkStatus = 0
				} else {
					this.pageInfo.checkStatus = undefined
				}
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
		.nav-bar-group {
			display: inline-block;
		}
		.Fsearch {
            overflow: hidden;
            float: right;
			/deep/.el-input{
				.el-input__inner{
					border-top-right-radius: 0;
					border-bottom-right-radius: 0;
				}
			}
            /deep/.box-input {
                width: 200px;
                float: right;
				.el-input__inner{
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
