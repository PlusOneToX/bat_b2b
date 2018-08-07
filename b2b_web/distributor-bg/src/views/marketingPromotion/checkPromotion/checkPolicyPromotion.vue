<template>
  <div class="check-list">
    <div class="sales-promotion-check">
      <header>
        <h4>统一政策审批列表</h4>
      </header>
    </div>

    <el-row class="nav-bar">
      <el-col :span=10>
        <el-radio-group v-model="pageInfo.checkType" size="mini">
          <el-radio-button :label="1">我发起的</el-radio-button>
          <el-radio-button :label="2">待我审批</el-radio-button>
          <el-radio-button :label="3">我审批的</el-radio-button>
        </el-radio-group>
      </el-col>
    </el-row>

    <el-table :data="tableData" border header-row-class-name="header-row" class="tableCenter" v-loading="loading">
      <el-table-column align="center" label="审批单号" prop="id"></el-table-column>
      <el-table-column align="center" label="审批类型" prop="ext" :formatter="formPolicyType"></el-table-column>
      <el-table-column align="center" label="活动名称" prop="policyName"></el-table-column>
      <!-- <el-table-column label="活动类型 " prop="promotionAdvanceType"></el-table-column> -->
      <el-table-column align="center" label="审批状态" prop="lastCheckTime" :formatter="formCheckType"></el-table-column>
      <el-table-column align="center" label="发起人" prop="applyUserName" ></el-table-column>
      <el-table-column align="center" label="发起时间" prop="createTime" :formatter="formatTime"></el-table-column>
      <el-table-column align="center" label="操作" width="80" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleSee(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
  </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat.js";
import { getPolicyCheckList,policyCheckData } from '@/views/marketingPromotion/promotionData'
import PageHeader from "@/components/PageHeader"
export default {
  name: "frontPagePlate",
  components: { page,PageHeader },
  data() {
    return {
      loading: false,
      pageInfo: {
        page: 1,
        count: 10,
        checkType: 1
      },
      total: 0,
      tableData: [],
    };
  },
  mounted() {
    this.dataFot();
  },
  activated() {
    this.dataFot();
  },
  methods: {
    handleSee(row) { // 查看操作
      if(row.ext == 11){ //..商品等级折扣
        this.$router.push({ name: 'approvePolicyPromotion',params:{id:row.id}})
      }else if(row.ext == 12){ //..提货增长审批
        this.$router.push({ name: 'approvePickUpRatePromotion',params:{id:row.id}})
      }
    },

    show(row){
      this.centerDialogVisible = true;
      this.promotionRuleDescribe = row.promotionRuleDescribe;
    },

    dataFot() { // 数据列表
      this.loading = true;
      getPolicyCheckList(this,this.pageInfo).then(res => {
        if(res.code == 0) {
					let ary = [];
					res.checks.forEach(item => {
            item.children = [];
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
          this.loading = false
				}else {
          this.loading = false
        }
      })
      
      policyCheckData(this,this.pageInfo).then(res => { // 总列数
        this.total = res.count;
      })
    },

    sizeChange(size) {
      this.pageInfo.count = size;
      this.dataFot()
    },
    
    currentChange(page) {
      this.pageInfo.page = page;
      this.dataFot()
    },
    
    formatTime(row, col, val) { // 时间转换
      return timeFormat(val);
    },
    
		formCheckType(row) { // 审批状态
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
     
    formPolicyType(row){ // 审批类型
      switch(row.ext) {
				case 11: 
					return '商品等级折扣'
				case 12: 
					return '提货增长审批'
			}
    },
  },
  watch: {
     'pageInfo.checkType':{
      handler(){
        this.dataFot();
      },
      deep: true
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell{
  overflow: hidden; 
  white-space:nowrap; 
}
.check-list {
  background-color: white;
  min-height: 100%;
  .sales-promotion-check {
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
  .nav-bar {
    padding: 10px;
  }
}
</style>
