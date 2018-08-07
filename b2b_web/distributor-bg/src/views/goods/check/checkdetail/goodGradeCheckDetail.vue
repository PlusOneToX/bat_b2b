<template>
	<div class="good-grade-change-detail">
		<header >
		  <h4>商品等级变动审批单</h4>
		  <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="onCancel()"> 返回商品等级变动审批列表 </el-button>
		</header>

			

		<el-row v-loading="loading">
			<div class="box-has-info">
				<div class="half-width">
					<el-form ref="recordDetail">
						<el-form-item label="审批单号:"> {{checkData.id}} </el-form-item>
						<!-- <el-form-item label="生成类型:"> {{createTypeFormatter(record.createType)}} </el-form-item> -->
						<el-form-item label="审批类型:"> {{extFormatter(checkData.ext)}} </el-form-item>
						<el-form-item v-if="record" label="调整说明:" prop="remarks">
						<span>{{record.remarks != undefined ? record.remarks:'---'}}</span>
					</el-form-item>
					</el-form>
				</div>
				<div class="half-width">
					<el-form ref="recordDetail" label-width="190px">
						<el-form-item label="发起时间:"> {{timeFormatter(checkData.createTime)}} </el-form-item>
						<el-form-item label="审批状态:"> {{formatStatus(0,0,checkData.status)}} </el-form-item>
						<el-form-item label="生效时间:"> {{timeFormatter(record.startTime)+' 到 '+timeFormatter(record.endTime)}} </el-form-item>
					</el-form>
				</div>
			</div>

			<el-form ref="record"  label-position="left" style="margin-left: 30px;margin-right: 20px;">
			</el-form>

			<div class="nav clearfix">
				<el-form :inline="true">
					<template>
						<el-select placeholder="品牌" size="mini" v-model="pageInfo.brandId" clearable style="width:130px;">
							<el-option :key="item.id" :label="item.title" :value="item.id" v-for="item in brandList">
							</el-option>
						</el-select>
						<el-select placeholder="类目" size="mini" v-model="pageInfo.categoryId" clearable style="width:130px;">
							<el-option :key="item.id" :label="item.name" :value="item.id" v-for="item in categoryList">
							</el-option>
						</el-select>
						<el-select placeholder="原等级" size="mini" v-model="pageInfo.originalGradeId" clearable style="width:130px;">
							<el-option :key="item.id" :label="item.gradeName" :value="item.id" v-for="item in grades">
							</el-option>
						</el-select>
						<el-select placeholder="最终调整等级" size="mini" v-model="pageInfo.finalGradeId" clearable style="width:130px;">
							<el-option :key="item.id" :label="item.gradeName" :value="item.id" v-for="item in grades">
							</el-option>
						</el-select>
						<button class="mini-batch-btn" size="mini" style="margin-left:15px;float: right;" @click.prevent="handleChangeGrade()">导出商品等级变动单</button>
					</template>
				</el-form>
			</div >

			<div class="nav">
				<el-table :data="details" border header-row-class-name="header-row" class="goods-table tableCenter" style="width: 100%" max-height="500">
					<el-table-column align="center" label="存货编码" prop="itemCode" fixed="left" :width="130"> </el-table-column>
					<el-table-column align="center" label="存货名称" fixed="left" prop="itemName" show-overflow-tooltip :min-width="150"> </el-table-column>
					<el-table-column align="center" label="品牌" prop="brandName"  :formatter="formatData" :min-width="100"> </el-table-column>
					<el-table-column align="center" label="类目" prop="categoryName"  :formatter="formatData" :min-width="100"> </el-table-column>
					<el-table-column align="center" label="即时库存" prop="numInWarehouse"  :formatter="formatData" :min-width="100"> </el-table-column>
					<el-table-column align="center" label="预计入库量" prop="numOnWay" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="近三个月出库量" prop="sendAmount" :formatter="formatData" :min-width="100"> </el-table-column>
					<el-table-column align="center" label="平均发货量" prop="avgSendCount" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="存销比" prop="inventorySalesRate" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="近一个月出库量" prop="oneSendAmount" :formatter="formatData" :min-width="100"> </el-table-column>
					<el-table-column align="center" label="成本" prop="costPrice" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="一级代理价" prop="onePrice" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="二级代理价" prop="twoPrice" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="折扣一级代理" prop="onePriceDiscount" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="折扣二级代理" prop="twoPriceDiscount" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="折后一级代理毛利率" prop="oneProfit" :formatter="formatData" :min-width="110"> </el-table-column>
					<el-table-column align="center" label="原等级" fixed="right" prop="originalGradeName" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="建议等级" fixed="right" prop="suggestGradeName" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="最终调整等级" fixed="right" prop="finalGradeName" :min-width="120"> </el-table-column>
					<el-table-column align="center" fixed="right" label="上月折扣" prop="lastMonthDiscount" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" fixed="right" label="本月建议折扣" prop="suggestMonthDiscount" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" fixed="right" label="本月最终折扣" prop="thisMonthDiscount" :formatter="formatData" :min-width="80"> </el-table-column>
				</el-table>
				<page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
				<div style="margin-bottom: 30px;" >
					<check-procedure :flows="checkData.flows" v-if="checkData.flows.length>0"></check-procedure>
				</div>
				<el-form ref="recordDetail">
					<el-form-item label="审批备注：" v-if="checkStatus">
						<el-input type="textarea" v-model="remark" style="width: 700px;" :rows="3"></el-input>
					</el-form-item>
				</el-form>
			</div>

			<div v-if="checkStatus" style="margin-left: 45%;margin-bottom:30px">
				<el-button :loading="submitLoading" class="mini-search-btn" type="success" @click="submit(2)" size="mini">同意</el-button>
				<el-button :loading="submitLoading" @click="submit(3)" size="mini">拒绝</el-button>
			</div>
			
			<el-table :data="flows" border header-row-class-name="header-row" class="flows-table" style="text-align:center;">
				<el-table-column label="审批人" prop="checkUserName"></el-table-column>
				<el-table-column label="审批时间" prop="checkTime" :formatter="timeFormat"></el-table-column>
				<el-table-column label="审批状态" prop="checkStatus" :formatter="formatStatus"></el-table-column>
				<el-table-column label="备注" prop="remark"></el-table-column>
			</el-table>
		</el-row>


	</div>
</template>

<script type="text/javascript">
import { timeFormat } from "@/utils/timeFormat.js";
import { getGradeChangeCheckDetail,getGradeDetail,changeGoodGrade,getBrandList,getCategoryList,getGoodGrade } from "@/views/goods/goodGrade/goodGradeData.js"
import checkProcedure from "@/components/checkProcedure"
import page from "@/components/pagination"
import store from '@/store'
export default {
	name: 'goodGradeCheckDetail',
	components: { page, checkProcedure },
	data() {
		return{
			checkData: {
				flows: [],
			},
			details:[],
			record:[],
			pageInfo: {
				page: 1,
				count: 10,
				checkId:''
			},
			total:0,
			grades:[],
			loading:false,
			submitLoading:false,
			flows: [],
			remark: '',
			checkStatus:true,
			categoryList:[],
			brandList:[],
			showRemark:true,
			time: []
		}
	},
	created() {
		getGoodGrade(this).then(res =>{
		 	if(res.goodGrades !== undefined){
        this.grades = res.goodGrades
				this.selectGrades = res.goodGrades
			}
	 	})
		this.getParams();
		getBrandList(this,{page:1,count:1000}).then(res =>{
			this.brandList = res.brands
		})
		getCategoryList(this,{page:1,count:1000}).then(res =>{
			this.categoryList = res.productlines
		})
	},
	activated() {
    this.getParams();
	},
	methods: {
		// ======== 转换 ========
		formatData(row, col, val){ // 列表显示判断是否有值
			if(val === undefined || val === null || val === ''){
				return '-'
			}else{
				return val
			}
		},
		handleChangeGrade() { // 导出操作
			this.$api.exportData(this,'admin/u/p/grade/change/export',{recordId: this.record.id}).then(res =>{
				const content = res
				let fileName = '商品等级变动单.xls'
				let blob = new Blob([content],{
					type: "application/ms-excel"
				})
				if ('download' in document.createElement('a')) {
					let url = window.URL.createObjectURL(blob)
					let link = document.createElement('a')
					link.style.display ='none'
					link.href = url
					link.setAttribute('download',fileName)
					document.body.appendChild(link)
					link.click()
				}else{
					navigator.msSaveBlob(blob, fileName)
				}
			})
		},
		
		onCancel() { // 返回操作
			this.$router.go(-1)
    },
    
		getParams(){
			if(this.$route.params.id) {
				this.pageInfo.checkId = this.$route.params.id
				this.getGradeChangeCheckDetail()
			}
    },
    
		getGradeChangeCheckDetail(){
			this.loading = true;
			getGradeChangeCheckDetail(this,this.pageInfo).then(res =>{
				this.details = res.changeDetails
				this.time = [this.details.startTime,this.details.endTime]
				this.record = res.record
				res.check.flows.forEach(item =>{
					item.checkUserName = ''
				})
				this.checkData = res.check;
				let userIds = [];
				this.checkStatus = false
				if(this.checkData.status === 1){//是否轮到自己审批
					for(let i = 1;i<this.checkData.flows.length;i++){
						if(this.checkData.flows[i].checkStatus === 1 && this.checkData.flows[i].checkUser === store.getters.userinfo.id){
							this.checkStatus = true
							
						}
					}
				}
				this.checkData.flows.forEach(item => {
					userIds.push(item.checkUser);
				})
				this.$api.get(this, 'admin/u/po/admin/ids', { ids: userIds.join(',') }).then(result => {
					if(result.code == 0) {
						this.checkData.flows.forEach(item => {
							result.admins.forEach(val => {
								if(val.id == item.checkUser) {
									item.checkUserName = val.name;
								}
							})
						})
						this.flows = this.checkData.flows
					}
				})
				res.code == 0 ? this.loading = false : this.loading = false
				
			})
			this.$api.get(this,'admin/u/p/item/grade/change/check/detail/count',this.pageInfo).then(res => {
				this.total = res.count
			})
		},
		createTypeFormatter(createType){ // 生成类型
			switch(createType) {
				case 1:
				return "自动生成";
					break;
				case 2:
				return "手动调整";
					break;
				default:
					return "-";
					break;
			}
		},
		extFormatter(ext){ // 审批类型
			switch(ext) {
				case 10:
					return '商品等级变动审批';
					break;
				default:
					return '-'
			}
		},
		formatStatus(row, col, type) { // 审批状态
			switch(type) {
				case 0:
					return "待审批";
					break;
				case 1:
					return '审批中';
					break;
				case 2:
					return '审批通过';
					break;
				case 3:
					return '审批拒绝';
					break;
				case 5:
					return '发起审批';
					break;
				default:
					return '-'
			}
		},
		timeFormatter(createTime) {
      return timeFormat(createTime);
		},
		
		timeFormat(row, col, val) { // 时间戳
			return timeFormat(val)
		},

		sizeChange(size){
			this.pageInfo.count = size
      this.pageInfo.page = 1
		},
		currentChange(page){
			this.pageInfo.page = page;
		},
		
		submit(type) { // 同意 || 拒绝，按钮
			this.$confirm('确定审批通过/审批拒绝?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			}).then(() => {
				this.submitLoading = true
				this.$api.put(this, 'admin/u/p/item/grade/change/check', {
						id: this.$route.params.id,
						checkStatus: type,
						remark: this.remark
					}).then(res => {
						this.submitLoading = false
						if(res.code == 0) {
							this.$message({
								message: '提交成功',
								type: 'success',
								duration: 3 * 1000,
								onClose: () => { }
							})
							this.$router.go(-1)
						}
						if(res.code == 40012) { //..其他端已审批
							this.getParams();
						}
					})
			})
			
		},
	},
	watch: {
		pageInfo: {
			handler(){
				this.getGradeChangeCheckDetail()
			},
			deep: true
		},
	}
}
</script>

<style lang="scss" >
.good-grade-change-detail {
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
	.nav {
		padding-bottom: 15px;
		margin-left: 30px;
		margin-right: 20px;
	}
	.box-has-info{
		margin-left: 30px;
		overflow: hidden;
		.half-width{
			width: 50%;
			box-sizing: border-box;
			float: left;
		}
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
