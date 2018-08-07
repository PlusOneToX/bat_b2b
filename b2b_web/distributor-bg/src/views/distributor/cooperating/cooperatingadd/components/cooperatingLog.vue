<template>
	<div>
		<div class="tip-left">
			<p><code>操作日志</code></p>
		</div>

		<el-row class="tip_table">
			<el-table :data="tableData" header-row-class-name="header-row" border class="tableCenter" max-height="600" :default-sort = "{prop: 'createTime', order: 'descending'}">
				<el-table-column align="center" label="操作者" prop="operator" :min-width="120"></el-table-column>
				<el-table-column align="center" label="操作时间" prop="operateTime" :formatter="formatTime" :min-width="120" sortable></el-table-column>
				<el-table-column align="center" label="操作类型" prop="operateType"  :min-width="130"></el-table-column>
				<el-table-column align="center" label="备注" prop="operateDes" show-overflow-tooltip :min-width="120"></el-table-column>
			</el-table>
       <page
        :page="pageInfo.page"
        :total="total"
        @sizeChange="sizeChange"
        @currentChange="currentChange"
      ></page>
		</el-row>

	</div>
</template>

<script type="text/javascript">
/*
 * @Author: lijiemin
 * @Date: 2018-05-06 17:15:58
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-06-Fr 10:17:37
 */
import { timeFormat } from '@/utils/timeFormat.js'
import page from "@/components/pagination";
export default {
  name: '',
  props: ['basicMessage', 'node'],
  components: { page },
  data() {
    return {
      tableData: [],
      pageInfo: {
        page:1,
        size: 10,
        distributorId: ''
      },
      total: 0
    }
  },
  methods: {
    getTableData() {
      this.$http.operationLogList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
        }
      })
    },
    sizeChange(size) {
      // 分页
      this.pageInfo.size = size;
      this.getTableData()
    },

    currentChange(page) {
      // 分页
      this.pageInfo.page = page;
      this.getTableData()
    },
    // ======== 转换 ========
    formatTime(row, col, val) { // 表格操作时间
      return timeFormat(val)
    },

    operationTypes(r, c, v) { // 操作类型 1为创建,2为编辑,3同步erp,4冻结,5提交审核'
      switch (v) {
        case 1:
          return '创建'
          // eslint-disable-next-line no-unreachable
          break
        case 2:
          return '编辑'
          // eslint-disable-next-line no-unreachable
          break
        case 3:
          return '同步erp'
          // eslint-disable-next-line no-unreachable
          break
        case 4:
          return '冻结'
        case 5:
          return '提交审核'
          // eslint-disable-next-line no-unreachable
          break
      }
    },
   
  },
  watch: {
    basicMessage(val) {
      this.pageInfo.distributorId = val.id
      this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
.tip_table {
	margin-top: 10px;
}
</style>
