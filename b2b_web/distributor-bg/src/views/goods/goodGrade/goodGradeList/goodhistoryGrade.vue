<template>
	<div class="good-history-grade">
		<header >
		  <h4>查看历史等级</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="onCancel">
			返回商品等级列表
		  </el-button>
		</header>

		<el-form label-width="100px" style="margin:30px;" class = "pomotion" ref="formData" :model="formData" v-loading="loading">
			<el-form-item label="存货编码" style="margin-bottom:10px;" prop="label">
					<span>{{formData.itemCode}}</span>
			</el-form-item>

			<el-form-item label="存货名称" style="margin-bottom:10px;" prop="label">
				<span>{{formData.itemName}}</span>
			</el-form-item>

			<el-form-item label="历史等级" prop="goodsItems" style="margin-bottom:10px;">
				<div>
					<el-table :data="formData.histories" border class="tableCenter" header-row-class-name="header-row" style="margin-top:15px;" :height="tableHeight">
						<el-table-column align="center" label="货品等级" prop="gradeName"  :formatter="formatGradeData" show-overflow-tooltip> </el-table-column>
						<el-table-column align="center" label="存销比" prop="inventorySalesRate" :formatter="formatData" show-overflow-tooltip> </el-table-column>
						<el-table-column align="center" label="货品折扣" prop="gradeDiscount"  :formatter="formatData" show-overflow-tooltip> </el-table-column>
						<el-table-column align="center" label="生效时间" show-overflow-tooltip > 
							<el-table-column align="center" label="起始时间" prop="startTime"  :formatter="formatTime" show-overflow-tooltip> </el-table-column>
							<el-table-column align="center" label="结束时间" prop="endTime"  :formatter="formatTime" show-overflow-tooltip> </el-table-column>
						</el-table-column>
						<el-table-column align="center" label="管理单据" width="150" v-if="false">
							<template>
								<el-button class="mini-search-btn">查看关联审批单</el-button>
							</template>
						</el-table-column>
					</el-table>

					<page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
				</div>
			</el-form-item >
		</el-form>
	</div>
</template>
<script type="text/javascript">
import page from "@/components/pagination"
import { getHistoryGrade } from "@/views/goods/goodGrade/goodGradeData.js"
import { timeFormat } from "@/utils/timeFormat.js";
export default {
	name: 'goodhistoryGrade',
	components: { page },
	data() {
		return{
			tableHeight: 600,
			loading: false,
			pageInfo: {
				page: 1,
				count: 10,
				saleStatus: 0
			},
			total:0,
			pageInfos: {
				page: 1,
				count: 10000,
			},
			formData:{
				itemCode:'',
				itemName:'',
				histories:[]
			},
		}
	},
	created() {
		const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
  },
	activated() {
		this.getParams();
	},

	methods: {
		getParams(){ // 历史等级数据
			if(this.$route.params.id != undefined){
				this.loading = true;
				getHistoryGrade(this,{id:this.$route.params.id}).then(res =>{
					if(res.items !== undefined){
						this.formData = res.items
					}
					res.code == 0 ? this.loading = false : this.loading = false
				})
			}else{
				this.$message.error('缺少货品id！');
			}
		},

		onCancel(){ // 返回操作
			this.$router.go(-1)
		},

		sizeChange(size){
			this.pageInfo.count = size
			this.pageInfo.page = 1
		},

		currentChange(page){
			this.pageInfo.page = page;
		},

		formatGradeData(row, col, val){ // 商品等级
			if(val === undefined || val === null || val === '' || val == 0){
				return '-'
			}else{
				return val
			}
		},
		formatData(row, col, val){ // 存销比转换
			if(val === undefined || val === null || val === ''){
				return '-'
			}else{
				return val
			}
		},

		formatTime(row, col, val) { // 时间转换
			if(val !== null) {
				return timeFormat(val);
			}else {
				return '至今'
			}
		}
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.good-history-grade {
    background-color: white;
	padding-bottom: 20px;
	header {
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
	  margin-bottom: 20px;
    }
    h4 {
      display: inline-block;
      font-weight: 350;
      font-size: 16px;
      margin: 0 20px;
	} 
	.btn-home{
		float: right;
		padding: 5px;
		margin-top: 8px;
		margin-right: 8px;
		margin-left: 0;
		font-size: 12px;
  	}
}

</style>
