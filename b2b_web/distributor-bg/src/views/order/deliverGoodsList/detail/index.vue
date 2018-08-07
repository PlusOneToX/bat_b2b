<template>
	<main class="deliver-detail">
		<header>
			<h4>查看发货单</h4>
			<el-button v-if="checkDeliverG" class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="orderCancel()" > 返回订单详情 </el-button>
			<el-button v-else class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel()" > 返回发货单列表 </el-button>
		</header>

    <div class="add-sync">
        <div class="add-sync-erp">
					<el-button :loading="syncLoading" v-show="(!deliverGoods.orderDeliverBill || deliverGoods.orderDeliverBill.deliverErpNo === undefined || deliverGoods.orderDeliverBill.deliverErpNo === '' || deliverGoods.orderDeliverBill.deliverErpNo === null)" class="mini-search-btn" @click="syncERP()" >同步ERP</el-button>
				</div>
        <div class="add-sync-customer">
					<el-button :loading="syncCustomerLoading" v-if="deliverGoods.orderDeliverBill && deliverGoods.orderDeliverBill.push !== undefined && deliverGoods.orderDeliverBill.push !== null && 
          deliverGoods.orderDeliverBill.push === 1 && deliverGoods.orderDeliverBill.pushStatus !== undefined && deliverGoods.orderDeliverBill.pushStatus !== null && deliverGoods.orderDeliverBill.pushStatus === 0" class="mini-search-btn" @click="syncCustomer()" >同步第三方</el-button>
        </div>
			</div>

    <div v-loading="loading">
      <div class="box-has-border">
        <div class="text-center title"  style="margin-top: 0px">
          <span>基本信息</span>
        </div>
        <div class="half-width">
          <el-form label-width="320px" size="mini">
            <el-form-item label="发货单流水号:"> {{deliverGoods.orderDeliverBill.id}} </el-form-item>
            <el-form-item label="订单号:"> {{orderId}} </el-form-item>
            <el-form-item label="购买者:"> {{deliverGoods.orderDeliverBill.distributorName}} </el-form-item>
            <el-form-item label="配送费用:"> <i class="asmd">￥:&nbsp;</i>{{deliverGoods.orderDeliverBill.distributionAmount}} </el-form-item>
            <el-form-item label="配送方式:"> {{deliverGoods.orderDeliverBill.distributionName}} </el-form-item>
            <el-form-item label="物流单号:"> 
              <template slot-scope="scope">
                <el-button type="text" @click="handleLogistics(scope.row)">{{deliverGoods.orderDeliverBill.logisticsNo}}</el-button>
              </template>
            </el-form-item>
          </el-form>
        </div>
        <div class="half-width">
          <el-form label-width="190px" size="mini">
            <el-form-item label="出库单号:">
              <el-tooltip content="出库单号为ERP的出库单号" placement="top" effect="light">
                <i class = "el-icon-question question-color"></i>
              </el-tooltip>
              <span v-show="deliverGoods.orderDeliverBill && deliverGoods.orderDeliverBill.deliverErpNo">{{deliverGoods.orderDeliverBill.deliverErpNo}}</span>
            </el-form-item>
            <el-form-item label="审核时间:"> {{timeFormatter(0, 0, deliverGoods.orderDeliverBill.deliverTime)}} </el-form-item>
            <el-form-item label="下单时间:"> {{timeFormatter(0,0,deliverGoods.orderDeliverBill.orderCreateTime)}} </el-form-item>
            <el-form-item label="审核状态:"> {{deliverStatusFormatter(deliverGoods.orderDeliverBill.deliverStatus)}} </el-form-item>
            <!-- <el-form-item label="同步ERP失败原因:"> {{deliverGoods.orderDeliverBill.deliverStatus}} </el-form-item> -->
          </el-form>
        </div>
      </div>

      <div class="box-has-border">
        <div class="text-center title">
          <span>收货人信息</span>
        </div>
        <div class="half-width">
          <el-form label-width="320px" size="mini" v-if="deliverGoods.orderDelivery	">
            <el-form-item label="收货人：" > {{deliverGoods.orderDelivery.userName}} </el-form-item>
            <el-form-item label="地址：">	{{deliverGoods.orderDelivery.countryName}}&nbsp;{{deliverGoods.orderDelivery.provinceName}}&nbsp;{{deliverGoods.orderDelivery.cityName}} &nbsp;{{deliverGoods.orderDelivery.districtName}} &nbsp;{{deliverGoods.orderDelivery.address}} </el-form-item>
            <el-form-item label="最近送货时间：" v-if="deliveryTimeShow"> {{timeFormatter(0,0,deliverGoods.orderDeliverBill.deliverTime)}} </el-form-item>
          </el-form>
        </div>
        <div class="half-width">
          <el-form label-width="190px" size="mini" v-if="deliverGoods.orderDelivery">
            <el-form-item label="邮编："> {{ deliverGoods.orderDelivery.zipCode}} </el-form-item>
            <el-form-item label="手机："> {{deliverGoods.orderDelivery.mobile}} </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="box-has-border">
        <div class="text-center title">
          <span>商品信息</span>
        </div>
        <el-table :data="deliverGoods.orderDeliverBillDetails" border header-row-class-name="header-row" class="tableCenter">
          <el-table-column align="center" type="index" fixed :min-width="50"></el-table-column>
          <el-table-column align="center" label="商品名称" prop="goodsName" :min-width="120"> </el-table-column>
          <el-table-column align="center" label="商品编号" prop="goodsNo" :min-width="120"> </el-table-column>
          <el-table-column align="center" label="货品号" prop="itemCode" :min-width="120"> </el-table-column>
          <el-table-column align="center" label="存货名称" prop="itemName" :min-width="120"> </el-table-column>
          <el-table-column align="center" label="仓库" prop="warehouseName" :min-width="120" > </el-table-column>
          <el-table-column align="center" label="发货数量" prop="count" :min-width="120" > </el-table-column>
          <el-table-column align="center" :label="modelName?'型号':'规格'" :min-width="120">
            <template slot-scope="scope">
              <div v-if="scope.row.modelName">{{scope.row.modelName}}</div>
              <div v-else-if="scope.row.specsName">{{scope.row.specsName}}</div>
            </template>
          </el-table-column>
          <!-- <el-table-column align="center" label="规格" prop="specsName" :min-width="120">
            <template slot-scope="scope">
              <div v-if="scope.row.specsName">{{scope.row.specsName}}</div>
              <div v-else>-</div>
            </template>
          </el-table-column> -->
        </el-table>
      </div>

      <div class="box-has-border">
        <div class="text-center" style=" padding-top: 10px; padding-bottom: 10px; margin-top: 20px;">
          <span>操作日志</span>
        </div>
        <el-table :data="logData" border header-row-class-name="header-row" class="tableCenter">
          <!-- <el-table-column type="index" fixed :min-width="50"></el-table-column> -->
          <el-table-column align="center" label="操作者" prop="operator" :min-width="120"> </el-table-column>
          <el-table-column align="center" label="操作时间" prop="operateTime" :formatter="timeFormat" :min-width="120"> </el-table-column>
          <el-table-column align="center" label="操作类型" prop="operateType" :min-width="120"> </el-table-column>
          <el-table-column align="center" label="备注" prop="operateDes" show-overflow-tooltip :min-width="120"> </el-table-column>
        </el-table>
      </div>
    </div>

    <el-dialog :visible="logisticsShow" :before-close="closeDialog" width="50%">
      <!--物流-->
        <div class="logistics" v-loading="loading2" >
          <h4 class="logistics_h4">查看物流信息</h4>
          <div class="logis_header">
            <span v-if="!logisticsData">物流公司：暂无物流公司</span>
            <span v-else>物流公司：{{delivery.orderDeliverBill.distributionName}}</span>
            <span v-if="!logisticsData">物流单号：暂无物流单号</span>
            <span v-else>物流单号：{{delivery.orderDeliverBill.logisticsNo}}</span>
          </div>
          <hr class="logis_hr">
          <div class="log-right" v-if="logisticsData">
            <div style="max-height: 450px;">
              <el-steps direction="vertical" :active="1" align-center >
                <el-step icon="el-icon-circle-check" v-for="item in delivery.orderDeliveryTrace" :key="item.id" style="margin-bottom: 15px;">
                  <template slot="description">
                    <h4 class="span-left">{{timeFormatter(0,0,item.acceptTime)}}</h4>
                    <span class="span-right">{{item.acceptStation}}</span>
                    <div>&nbsp;</div>
                  </template>
                </el-step>
              </el-steps>
            </div>
          </div>
          <div v-else class="log_logMessage">暂无物流信息</div>
        </div>
    </el-dialog>
		
	</main>
</template>
<script>
import PageHeader from "@/components/PageHeader"
import { parseTime } from '@/utils/index'
import {deliverStatusFormatter} from '@/views/order/orderUtils'
import { timeFormat } from "@/utils/timeFormat"

export default {
  computed: {
    checkDeliverG () {
      return this.$route.query.deliverG
    }
  },
  created() {
    this.getDetail()
  },
	activated(){
		this.getDetail()
    this.orderLogs()
	},
	components: {
		PageHeader
  },
  data() {
		return {
      modelName: '',
      loading: false,
			pageState: 'rootPage', // 页面状态
			deliverGoods: [],
			returnList: [],
			remark: '',
			order: null,
      deliveryTimeShow: true,
      logisticsShow: false,
      logisticsData: false,
      loading2: false,
      delivery: {},
      syncLoading:false,
      syncCustomerLoading: false,
      logData: [],
      orderId: ''
		}
	},
	methods: {
    orderLogs () {
      this.$http.orderDeliverBillLogList(this, {  
        page:1,
        size:100,  
        orderDeliverBillId: this.$route.query.id,
      }).then((res) => {
        if (res.success) {
          this.logData = res.data.list;
        }
      });
    },
    // ======== 操作 ========
    syncERP(){
      this.syncLoading = true
        this.$http.syncDiyDeliveryOrderToERP(this, {id: this.deliverGoods.orderDeliverBill.id}).then(res =>{ 
        this.syncLoading = false
        if(res.success){
          this.$message({
						message: '成功同步到ERP',
						type: 'success',
						duration: 3 * 1000,
          })
        }
        this.getDetail()
      })
    },
    syncCustomer(){
      this.syncCustomerLoading = true
      this.$http.syncLogisticsToThird(this, {id: this.deliverGoods.orderDeliverBill.id}).then(res =>{ 
        this.syncCustomerLoading = false
        if(res.success){
          this.$message({
						message: '成功同步到第三方',
						type: 'success',
						duration: 3 * 1000,
          })
        }
        this.getDetail()
      })
    },
    cancel(){ // 返回操作
			this.$router.push({name:'deliverGoods'});
    },

    orderCancel() {
      this.$router.push({ name: 'orderDetail', query: {
        orderId: this.$route.query.orderId,
        orderG: true,
        type: 1
      }})
    },
    
    handleLogistics(row) { //..查看物流信息
      this.logisticsShow = true;
      this.loading2 = true;
      this.$http.orderLogistics(this, {id: this.$route.query.id}).then(res => {
        if(res.data) {
          this.delivery = res.data
          this.logisticsData = true
          this.delivery.length != 0 ? this.logisticsData = true : this.logisticsData = false //..暂无物流信息
        }else {
          this.logisticsData = false
        }
        res.success ? this.loading2 = false : this.loading2 = false
      })
    },

    closeDialog() { // 关闭dialog的X
      this.logisticsShow = false;
		},

		// ======== 数据 ========
    getDetail() {  // 发货单主要数据
      this.loading = true;
      this.$http.orderDeliverBillDetail(this, {id:this.$route.query.id}).then(res =>{ 
        if (res.success) {
          this.deliverGoods = res.data;
          if(res.data.orderDeliverBill === undefined || res.data.orderDeliverBill.deliverTime === undefined || res.data.orderDeliverBill.deliverStatus !== 2) {
            this.deliveryTimeShow = false;
          }
          this.deliverGoods.orderDeliverBillDetails.forEach(item => {
            if (item.modelName) {
              item.modelTitle = item.modelName ? '型号' : '规格'
            }
          })
          let orderId = res.data.orderDeliverBill.orderId
          if (orderId) {
            this.$http.getOrderIdByOrderNo(this, {id: orderId}).then(res => {
              if (res.success) {
                this.orderId = res.data
              }
            })
          }
        }
        res.success ? this.loading = false : this.loading = false
      })
		},
		
		// getOrderDetail(){ // 如果没有订单详情，则发出请求订单详情
		// 	if(!this.order){
		// 		getOrderDetail(this, {
    //       id: this.deliverGoods.saleOrderId == 0? this.deliverGoods.changeOrderId : this.deliverGoods.saleOrderId
    //       }).then(res => {
		// 			this.order = res.order;
		// 		})
		// 	}
    // },
       // ======== 转换 ========
    timeFormatter(row, column, cellValue) { // 时间格式化
			return parseTime(cellValue)
    },

    deliverStatusFormatter(stateCode){ // 基本信息 > 发货状态
			this.deliverStatusFormatter = deliverStatusFormatter;
			return this.deliverStatusFormatter(stateCode);
		},
    
		operationType(r, c, v) { // 发货日志操作类型
			switch(v) {
				case 1:
					return '创建发货单'
					break;
				case 2:
					return '发货'
					break;
				case 3:
					return '取消发货'
          break;
        case 4:
					return '同步ERP销售采购'
          break;
        case 5:
					return '同步ERP销售出库'
					break;
				default:
					return '-'
			}
		},
		
		timeFormat(row, col, val) { // 时间戳转换
			return timeFormat(val)
    },
	}
}

</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.question-color {
  color: $lakeBlue;
}
.el-dialog__body{height: 50vh;overflow: auto;}
.el-step.is-vertical {
  margin-bottom: 0 !important;
}
.deliver-detail {
  background-color: white;
	min-height: 100%;
	padding-bottom: 20px;
  .add-sync {
    overflow: hidden;
    .add-sync-erp {
      float: right;
      margin-top: 10px;
      margin-bottom: 10px;
      margin-right: 10px;
    }
    .add-sync-customer {
      float: right;
      margin-top: 10px;
      margin-bottom: 10px;
      margin-right: 10px;
    }
  }
	header {
		color: white;
		height: $lineHight;
		line-height: $lineHight;
		background-color: $lakeBlue;
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
	.box-has-border {
		overflow: hidden;
		.half-width{
			width: 50%;
			box-sizing: border-box;
			float: left;
		}
		.text-center{
			text-align: center;
			overflow: hidden;
		}
	}
}
/*物流*/
.logistics{
  background-color: #fff;
  position: relative;
  .logistics_h4 {
    margin:0 0 20px 0;
    text-align: center;
  }
  .logis_header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .logis_hr {
    margin-bottom: 20px;
  }
  .log_logMessage {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 340px;
  }
  .log-right{
    overflow-y: auto;
    .span-left{
      width: 120px;
      margin: 0;
      vertical-align: top;
    }
    .span-right{
      display: inline-block;
    }
  }
  .log-right::-webkit-scrollbar{/*滚动条整体样式*/
    width: 7px;     /*高宽分别对应横竖滚动条的尺寸*/
    height: 9px;
  }
  .log-right::-webkit-scrollbar-thumb{/*滚动条里面小方块*/
    border-radius: 5px;
    box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
    background: rgba(0,0,0,0.2);
  }
  .log-right::-webkit-scrollbar-track{/*滚动条里面轨道*/
    box-shadow: inset 0 0 5px rgba(0,0,0,0.0);
    border-radius: 0;
    background: rgba(0,0,0,0.0);
  }
  
}
</style>
