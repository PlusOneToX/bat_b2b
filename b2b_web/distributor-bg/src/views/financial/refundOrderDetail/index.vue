<template>
	<div class="add-account">
		<header>
		  <h4>查看退款单</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
				返回退款单列表
		  </el-button>
		</header>

		<div v-loading="loading2">
      <div class="add-sync">
        <div class="add-sync-sync">
          <el-button class="mini-search-btn" @click="syncDisERP" v-if="!distributorInfo.refundErpNo" :loading="loadingERP">同步ERP</el-button>
        </div>
      </div>

      <div class="box-has-border">
        <div class="text-center title" style="margin-top: 0px">
          <span>基本信息</span>
        </div>
        <div class="half-width">
          <el-form label-width="280px" size="mini">
            <el-form-item label="退款单号:">{{distributorInfo.id}}</el-form-item>
            <el-form-item label="EPR退款单号:">{{distributorInfo.refundErpNo}}</el-form-item>
            <el-form-item label="业务单号:">{{distributorInfo.businessId}}</el-form-item>
            <el-form-item label="分销商用户名:">{{distributorInfo.distributorName}}</el-form-item>
            <el-form-item label="退款方式:">{{psyRefundType(distributorInfo.refundWay)}}</el-form-item>
          </el-form>
        </div>
        <div class="half-width">
          <el-form label-width="280px" size="mini">
            <el-form-item label="退款状态:">{{ payStatusFormatter(distributorInfo.refundStatus)}}</el-form-item>
            <el-form-item label="申请时间:">{{timeFormatter(distributorInfo.createTime)}}</el-form-item>
            <el-form-item label="退款时间:" v-if="distributorInfo.refundTime">{{timeFormatter(distributorInfo.refundTime)}}</el-form-item>
            <el-form-item label="退款金额:"><i class="asmd">￥:&nbsp;</i>{{distributorInfo.amount | NumFormat}}</el-form-item>
          </el-form>
        </div>
      </div>
      <div class="box-has-border">
        <div class="box-has-border">
          <div class="text-center title">
            <span>操作记录</span>
          </div>
          <!-- 订单操作日志 -->
          <el-table :data="refundLogs" border header-row-class-name="header-row" class="tableCenter">
            <el-table-column align="center" label="操作者" prop="operator" :width="160"></el-table-column>
            <el-table-column align="center" label="操作时间" prop="operateTime" :width="200" :formatter="formatTime"></el-table-column>
            <el-table-column align="center" label="操作类型" prop="operateType" :width="150"></el-table-column>
						<el-table-column align="center" label="操作信息" prop="operateData" show-overflow-tooltip=""></el-table-column>
            <el-table-column align="center" label="备注" prop="operateDes" :width="200"></el-table-column>
          </el-table>
        </div>
      </div>
    </div>
		
	</div>
</template>
<script>
import { parseTime } from '@/utils/index';
import { timeFormat } from "@/utils/timeFormat";
export default {
	name: 'refundOrderDetail',
	data() {
		return {
			distributorInfo: [],
			refundLogs: [], // 退款操作日志
			refundList: [], // 退款列表数据
			returnList: [],
			pageInfo: {
				page: 1,
				size: 10000,
				distributorId: '',
				endTime: ''
			},
			remark: '',
      loading: false,
      loading2: false,
			loadingERP: false
		}
	},
	computed: {
		refundWay() { // 退款状态 1、退回支付账户 2、退回用户余额
			return this.distributorInfo.refundStatus
		}
	},
	activated() {
		this.getDistributorInfo() // 退款单详情,退款日志详情
	},
	methods: {
		syncDisERP() { // 退款单同步到ERP
			this.loadingERP = true;
		  this.$http.syncRefundBillToERP(this, {id: this.$route.query.id }).then(res => {	
				if(res.success) {
					this.$message({
						message: '成功同步到ERP',
						type: 'success',
						duration: 3 * 1000,
						onClose: () => {}
					})
					this.loadingERP = false;
					this.getDistributorInfo()
				}else {
					this.loadingERP = false
				}
			})
			setTimeout(() => { // 无法收到反馈则6S结束动画
				this.loadingERP = false
			}, 6000);
		},
    getDistributorInfo() { // 退款单详情,退款日志详情数据
      this.loading2 = true;
			let query = this.$route.query;
		  this.$http.refundDetail(this, {id: query.id}).then(res => {		
				this.distributorInfo = res.data
				res.success ? this.loading2 = false : this.loading2 = false
			})
		  this.$http.refundLogList(this, {page:1, size: 100, id: query.id}).then(res => {		
				this.refundLogs = res.data.list
			})
		},

		timeFormatter(cellValue) { // 基本信息时间格式化
			return parseTime(cellValue);
		},

		formatTime(row, col, val) { // 表格时间格式化
			return timeFormat(val);
		},
		payStatusFormatter(v) { // 退款状态
			switch(v) {
				case 1:
					return '待确认';
					break;
				case 2:
					return '已确认';
					break;
				case 3:
					return '已取消';
					break;
				default:
					return '-'
			}
		},
		statusLog(r, c, v) { // log日志状态
			switch(v) {
				case 1:
					return '创建'
				break;
				case 2:
					return '退款'
				break;
				case 3:
					return '核销'
				break;
				case 4:
					return '拒绝'
				break;
				case 5:
					return '同步ERP'
				break;
				default:
					return '-'
			}
		},
		psyRefundType(v) { // 退款类型
			switch(v) {
				case 1:
					return '退回支付账户';
					break;
				case 2:
					return '退回用户余额';
					break;
				case 3:
					return '其他方式';
					break;
				default:
					return '-'
			}
		},
		reimburse() { // 退款操作 1.待处理, 2.确认退款, 3.已核销, 4.拒绝
			this.$router.push({name : 'refundOrderHandle', query: {id: this.$route.query.id}})
		},
		cancel() { // 返回操作
			this.$router.push({ name: 'refundOrder' })
		}
	}
}

</script>
<style rel="stylesheet/scss" lang="scss">
.add-account {
	height: 95%;
	background-color: white;
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
	.btn-home{
		float: right;
		padding: 5px;
		margin-top: 8px;
		margin-right: 8px;
		margin-left: 0;
		font-size: 12px;
	}
	.add-sync {
		overflow: hidden;
		.add-sync-sync {
			float: right;
			margin: 10px;
		}
	}
	.title {
		text-align: center;
		font-size: 18px;
		margin: 20px 0;
	}
	.small {
		text-align: center;
		margin: 20px 0;
	}
	.operation {
		margin: 20px 20px 20px 55px;
		.instruction {
			margin-left: 48px;
			margin-top: 20px;
		}
	}
	.box-has-border {
		overflow: hidden;
		.half-width{
			width: 50%;
			box-sizing: border-box;
			float: left;
		}
	}
}

</style>
