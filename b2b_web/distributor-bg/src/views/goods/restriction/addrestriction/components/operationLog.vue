<template>
  <div>
		<el-row style="margin-top: 10px">
			<el-table :data="logs" header-row-class-name="header-row" border class="tableCenter" max-height="600" :default-sort = "{prop: 'createTime', order: 'descending'}">
				<el-table-column align="center" label="操作者" prop="operateName" :min-width="120"></el-table-column>
				<el-table-column align="center" label="操作时间" prop="createTime" :formatter="formatTime" :min-width="120" sortable></el-table-column>
				<el-table-column align="center" label="操作类型" prop="operateType" :formatter="operationTypes" :min-width="130"></el-table-column>
				<el-table-column align="center" label="备注" prop="remark" :min-width="120"></el-table-column>
			</el-table>
		</el-row>

	</div>
</template>
<script type="text/javascript">
/*
 * @Author: lijiemin 
 * @Date: 2018-05-06 17:15:58 
 * @Last Modified by: lijiemin
 * @Last Modified time: 2018-03-11 18:33:10
 */
import { timeFormat } from "@/utils/timeFormat.js";
	export default {
    name: '',
    props: ['checkId'],
    created() {
			if(this.$route.query.checkMsg == 1) { // 详情页面请求日志详情
				this.fotData()
			}
    },
		data() {
			return {
				logs: []
			}
		},
		methods: {
      // ======== 数据 ========
      fotData() {
        this.$api.get(this,'admin/u/p/goods/restriction/logs',{id: this.checkId}).then(res => {
          this.logs = res.logs
        })
      },
      // ======== 转换 ========
			formatTime(row, col, val) { // 表格操作时间
				return timeFormat(val);
      },
      
			operationTypes(r,c,v){ // 操作类型 1为创建,2为编辑,3同步erp,4冻结,5提交审核'
				switch(v) {
					case 1:
						return '新增';
					break;
					case 2:
						return '修改';
					break;
					case 3:
						return '停用';
					break;
					case 4:
						return '启用';
					break;
				}
			},
		},
		watch: {
			// basicMessage(val) {
			// 	this.$api.get(this,'admin/u/p/distributor/cooperating/log', {id: val.id}).then(res => { // 操作日志
			// 		this.tableData = res.distributorLogs
			// 	})	
			// },
		}
	}
</script>
