<template>
	<div class="good-grade-change-detail" v-loading="loading">
		<header >
		  <h4>商品等级变动确认</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="onCancel()"> 返回商品等级变动列表 </el-button>
		</header>

		<div v-loading="loading2">
			<div class="box-has-info">
				<div class="half-width">
					<el-form ref="recordDetail" >
						<el-form-item label="单据编号:"> {{this.recordDetail.recordId}} </el-form-item>
						<el-form-item label="生成类型:"> {{createTypeFormatter(recordDetail.createType)}} </el-form-item>
					</el-form>
				</div>
				<div class="half-width">
					<el-form ref="recordDetail">
						<el-form-item label="生成时间:"> {{timeFormatter(recordDetail.createTime)}} </el-form-item>
						<el-form-item label="确认状态:"> {{changeStatusFormatter(recordDetail.changeStatus)}} </el-form-item>
					</el-form>
				</div>
			</div>

			<div class="good_expot">
				<el-upload style="margin-left: 41px;"
						class="upload-demo"
						:headers="importHeaders"
						:action="action"
						v-if="isUpload"
						:auto-upload="true"
						:multiple="false"
						:show-file-list="false"
						:before-upload="beforeUpload"
						:on-success="uploadSuccess"
						:on-error="uploadFail"
						:on-progress="onProgress">
					<button class="mini-batch-btn expot-btn" size="mini">导入商品等级变动单</button>
				</el-upload>
				<button class="mini-batch-btn expot-btn" size="mini" @click.prevent="handleExport">导出商品等级变动单</button>
			</div>
			
			<!-- 下划线 -->
			<div class="underline"></div>
			
			<div class="nav">
				<div class="nav_header">
					<div>
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
					</div>

					<div class="nav_hea_right">
            <el-input v-model.trim="pageInfo.content" size="mini" placeholder="请输入存货编码/货品名称 自动检索" class="box-input"></el-input>
            <!-- <button class="mini-search-btn box-btn" @click="onSearch()">搜索</button> -->
					</div>

				</div>
				<el-table :data="recordDetail.details" border header-row-class-name="header-row" class="goods-table tableCenter" style="width: 100%" max-height="600">
					<el-table-column align="center" label="存货编码" prop="itemCode" fixed="left" :width="130"> </el-table-column>
					<el-table-column align="center" label="存货名称" fixed="left" prop="itemName" show-overflow-tooltip :min-width="150"> </el-table-column>
					<el-table-column align="center" label="品牌" prop="brandName"  :formatter="formatData" :min-width="100"> </el-table-column>
					<el-table-column align="center" label="类目" prop="categoryName"  :formatter="formatData" :min-width="100"> </el-table-column>
					<el-table-column align="center" label="即时库存" prop="numInWarehouse"  :formatter="formatData" :min-width="100"> </el-table-column>
					<el-table-column align="center" label="预计入库量" prop="numOnWay" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="近三个月出库量" prop="sendAmount" :formatter="formatData" :min-width="100"> </el-table-column>
					<!-- <el-table-column label="锁定数量" prop="numLock" :formatter="formatData" :min-width="110"> </el-table-column> -->
					<el-table-column align="center" label="平均发货量" prop="avgSendCount" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column  align="center" label="存销比" prop="inventorySalesRate" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="近一个月出库量" prop="oneSendAmount" :formatter="formatData" :min-width="100"> </el-table-column>
					<el-table-column align="center" label="成本" prop="costPrice" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="一级代理价" prop="onePrice" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="二级代理价" prop="twoPrice" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="折扣一级代理" prop="onePriceDiscount" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="折扣二级代理" prop="twoPriceDiscount" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="折后一级代理毛利率" prop="oneProfit" :formatter="formatData" :min-width="110"> </el-table-column>
					<el-table-column align="center" label="原等级" fixed="right" prop="originalGradeName" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" label="建议等级" fixed="right" prop="suggestGradeName" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" fixed="right" v-if="recordDetail.changeStatus == 1" label="最终调整等级" prop="finalGradeId" width="120" :min-width="120">
						<template slot-scope="scope">
							<el-select size="mini" @focus="index=scope.$index" @change="finalChange" v-model="scope.row.finalGradeId" placeholder="选择等级">
								<el-option v-for="item in selectGrades" :key="item.id" :label="item.gradeName" :value="item.id">
								</el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column align="center" v-else label="最终调整等级" fixed="right" prop="finalGradeName" :min-width="120"> </el-table-column>
					<el-table-column align="center" fixed="right" label="上月折扣" prop="lastMonthDiscount" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" fixed="right" label="本月建议折扣" prop="suggestMonthDiscount" :formatter="formatData" :min-width="80"> </el-table-column>
					<el-table-column align="center" fixed="right" v-if="recordDetail.changeStatus == 1"  label="本月最终折扣" prop="thisMonthDiscount" :formatter="formatData" :min-width="80"> 
						<template slot-scope="scope">
							<el-input class="discount-input" v-model="scope.row.thisMonthDiscount" @input="scope.row.thisMonthDiscount=/^\d+\.?\d{0,2}$/.test(scope.row.thisMonthDiscount)||scope.row.thisMonthDiscount == '' ? scope.row.thisMonthDiscount:Number(scope.row.thisMonthDiscount.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0" />
						</template>
					</el-table-column>
					<el-table-column align="center" fixed="right" v-else label="本月最终折扣" prop="thisMonthDiscount" :formatter="formatData" :min-width="80"> </el-table-column>
				</el-table>
        <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
				<el-form :disabled="!(recordDetail.changeStatus===1)" label-width="100px" ref="changeGrade" :model="changeGrade" label-position="right" style="margin-top:20px">
					<el-form-item label="调整生效时间:" prop="time">
						<el-date-picker v-model="time" type="datetimerange" value-format="timestamp" 
							range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" >
						</el-date-picker>
					</el-form-item>
					<el-form-item label="调整说明:" prop="remarks">
						<el-input v-model="changeGrade.remarks" type="textarea" :rows="5" style="width:100%;"></el-input>
					</el-form-item>
				</el-form>
				<span v-if="recordDetail.changeStatus===1" style="margin-left: 45%; margin-top:20px;" >
					<el-button class="mini-search-btn" :loading="loading" type="success" @click.prevent="handlerSubmit(1)"> 保存 </el-button>
					<el-button class="mini-search-btn" :loading="loading" type="success" @click.prevent="handlerSubmit(2)"> 提交 </el-button>
				</span>
			</div>
		</div>
	</div>
</template>
<script type="text/javascript">
import { getToken } from '@/utils/auth'
import { timeFormat } from "@/utils/timeFormat.js";
import { getGradeChangeDetail, getGoodGrade,changeGoodGrade,exportGrade,getGradeChangeDetailCount,getBrandList,getCategoryList,discountCalculation } from "@/views/goods/goodGrade/goodGradeData.js"
import Vue from 'vue';
import page from "@/components/pagination";
export default {
	name: 'goodGradeChangeDetail',
	components: { page },
	data() {
		return{
			recordDetail:{
				createTime:0,
				changeStatus:0,
				createType:0,
				details:[]
			},
			changeGrade:{
				btnType:2,
				remarks:'',
				items:[]
			},
			itemsTmp:[],
			pageInfo: {
				page: 1,
				count: 10,
				content: ''
			},
			total:0,
			grades:[],
			selectGrades:[],
			loading:false,
			loading2: false,
			uploading:false,
			showRemark:true,
			isUpload:true,
			hintStr: '',
			flag: false,
			brandList:[],
			categoryList:[],
			index:0,
			time: [],
			Authorization:'',
			fileList: [],
			importHeaders: {
				Accept: 'application/json',
				enctype: 'multipart/form-data',
				Platform: 'web',
				Version: '1.0.0',
				Authorization: ''
			},
			action: process.env.BASE_API + 'admin/u/p/grade/change/import'
		}
	},
	created() {
		this.importHeaders.Authorization = getToken();
		getGoodGrade(this,{status:1}).then(res =>{
		 	if(res.goodGrades !== undefined){
        this.grades = res.goodGrades
				this.selectGrades = res.goodGrades
			}
	 	})
		this.changeGrade = {
			btnType:2,
			remarks:'',
			items:[]
		}
		this.getParams(); // 商品等级变动详情
		this.isCheckDetail(); // 是否开启审批
		getBrandList(this,{page:1,count:1000}).then(res =>{
			this.brandList = res.brands
		})
		getCategoryList(this,{page:1,count:1000}).then(res =>{
			this.categoryList = res.productlines
		})
	},
	methods: {
		beforeUpload(file) { //上传前配置
			let excelfileExtend = ".xls,.xlsx"; //设置文件格式
			let fileExtend = file.name
				.substring(file.name.lastIndexOf("."))
				.toLowerCase();
			if (excelfileExtend.indexOf(fileExtend) <= -1) {
				this.$message.error("只能上传.xls,.xlsx格式");
				return false;
			}
		},
		onProgress(event, file, fileList) { // 上传时的钩子
			this.uploading = this.$loading({
				lock: true,
				text: '文件上传中....',
				spinner: 'el-icon-loading',
				background: 'rgba(0, 0, 0, 0.7)'
			});
		},
		uploadFail(err, file, fileList) { //上传错误
			this.$message.error({
				message: err.msg,
				duration: 3 * 1000,
				onClose: () => { }
			});
			this.uploading.close();
		},
		beforeUpload() { // 上传前的配置
			this.uploading = this.$loading({
				lock: true,
				text: '文件正在处理中....',
				spinner: 'el-icon-loading',
				background: 'rgba(0, 0, 0, 0.7)'
			});
		},
		
		uploadSuccess(response, file, fileList) { //上传成功
			if(response.code == 0) {
				this.$message.success({
					message: response.msg,
					duration: 1000,
					onClose: () => { 
						this.getParams()
					}
				})
				this.uploading.close()
			}else {
				this.$message.error({
					message: response.msg,
					duration: 3 * 1000,
					onClose: () => { }
				})
				this.uploading.close()
			}
		},
		finalChange(val){
			discountCalculation(this,{itemId:this.recordDetail.details[this.index].itemId,gradeId:this.recordDetail.details[this.index].finalGradeId}).then(res =>{
				if(res.code == 0){
					this.recordDetail.details[this.index].thisMonthDiscount = res.gradeDiscount
					if(this.recordDetail.details[this.index].thisMonthDiscount != undefined){
						if(this.recordDetail.details[this.index].onePrice != undefined ){
							this.recordDetail.details[this.index].onePriceDiscount = (this.recordDetail.details[this.index].onePrice*this.recordDetail.details[this.index].thisMonthDiscount/100).toFixed(2)
							if(this.recordDetail.details[this.index].costPrice != undefined && this.recordDetail.details[this.index].costPrice != 0 && this.recordDetail.details[this.index].onePriceDiscount != undefined && this.recordDetail.details[this.index].onePriceDiscount != 0){
								this.recordDetail.details[this.index].oneProfit = ((this.recordDetail.details[this.index].onePriceDiscount -this.recordDetail.details[this.index].costPrice)/this.recordDetail.details[this.index].onePriceDiscount).toFixed(2)
							}
						}
						if(this.recordDetail.details[this.index].twoPrice != undefined){
							this.recordDetail.details[this.index].twoPriceDiscount = (this.recordDetail.details[this.index].twoPrice*this.recordDetail.details[this.index].thisMonthDiscount/100).toFixed(2)
						}
					}else{
						this.recordDetail.details[this.index].onePriceDiscount = undefined
						this.recordDetail.details[this.index].twoPriceDiscount = undefined
						this.recordDetail.details[this.index].oneProfit = undefined
					}
				}
			})
		},
		// ======== 操作 ========
		cacheGrade(){ // 列表匹配名称
			this.recordDetail.details.forEach(item =>{
				let b = false;
				for(let i=0;i<this.changeGrade.items.length;i++){
					if(this.changeGrade.items[i].itemId === item.itemId && (item.finalGradeId != null || item.thisMonthDiscount != null)){
						this.changeGrade.items[i].finalGradeId = item.finalGradeId
						this.changeGrade.items[i].gradeDiscount = item.thisMonthDiscount
						b = true
						break
					}
				}
				if(!b){
					let obj = {
						itemId:item.itemId,
						finalGradeId:item.finalGradeId,
						gradeDiscount:item.thisMonthDiscount
					}
					this.changeGrade.items.push(obj)
				}
			})
		},
		
		handlerSubmit(btnType){ // 保存(1) / 提交(2)操作
			let changeTmp = true
			this.loading = true;
			this.cacheGrade() // 列表匹配名称
			this.changeGrade.btnType = btnType
			this.changeGrade.recordId = this.pageInfo.recordId
			// for(let i = 0;i < this.changeGrade.items.length;i++) {
			// 	if(this.changeGrade.items[i].finalGradeId == '') {
			// 		changeTmp = false
			// 		this.loading = false
			// 		this.$message.warning('最终调整等级不能为空')
			// 		return
			// 	}else {
			// 		changeTmp = true
			// 	}
			// }
			this.changeGrade.startTime = this.time[0]
			this.changeGrade.endTime = this.time[1]
			if(this.changeGrade.startTime === undefined || this.changeGrade.endTime === undefined || this.changeGrade.startTime === 0 || this.changeGrade.endTime === 0){
				this.$message.error('生效起始时间或结束时间不能为空！');
				this.loading = false;
				return
			}
			if(changeTmp) {
				this.uploading = this.$loading({
					lock: true,
					text: '数据保存中....',
					spinner: 'el-icon-loading',
					background: 'rgba(0, 0, 0, 0.7)'
				});
				changeGoodGrade(this,this.changeGrade).then(res =>{
					this.loading = false
					if(res.code == 0) {
						if(this.flag && btnType == 1) { //开启审批 && 保存操作
							this.hintStr = '保存成功'
						}else if(this.flag && btnType == 2) { // 提交
							this.hintStr = '提交成功，等待审批通过后才会应用。请到”商品——商品等级变动审批“中查看'
						}else if(!this.flag && btnType == 1) {
							this.hintStr = '保存成功'
						}else if(!this.flag && btnType == 2) {
							this.hintStr = '提交成功'
						}
						this.$message({
							message: this.hintStr,
							type: 'success',
							duration: 3 * 1000,
							onClose: () => { 
								this.changeGrade = {
									btnType:2,
									remarks:'',
									items:[]
								}
							}
						})
						this.$router.go(-1)
					}
					this.uploading.close();
				})
			}
		},

		onSearch() { //..搜索功能

		},

		handleExport(){ // 等级列表下载
			this.uploading = this.$loading({
				lock: true,
				text: '文件导出中....',
				spinner: 'el-icon-loading',
				background: 'rgba(0, 0, 0, 0.7)'
			});
			this.$api.exportData(this,'admin/u/p/grade/change/export',{recordId:this.pageInfo.recordId}).then(res =>{
				const content = res
				let fileName = '等级变动单'+new Date().getTime()+'_'+this.pageInfo.recordId+'.xls'
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
				this.uploading.close();
			})
		},

		onCancel(){ // 返回商品等级变动列表
			this.$router.go(-1)
		},

		// ======== 数据 ========
		isCheckDetail() { // 是否开启审批
			this.$api.get(this,'admin/u/po/checkDetail').then(res => {
				res.checkConfigs.forEach(item => {
					if(item.ext == 10) { // 1 审批开启
						item.isOpen == 1 ? this.flag = true : this.flag = false
					}
				})
			})
		},

		getParams(){ // 商品等级变动详情
			this.pageInfo.recordId = this.$route.query.id
			if(this.pageInfo.recordId != undefined){
				this.getGradeChangeDetail()
			}
		},
		getGradeChangeDetail(){
			this.loading2 = true
			this.cacheGrade() // 列表匹配名称
      		getGradeChangeDetailCount(this,this.pageInfo).then(res =>{
				this.total = res.count
			})
			getGradeChangeDetail(this,this.pageInfo).then(res =>{ // 详情数据
				this.recordDetail = res
				if(this.time == undefined || this.time.length ==0)
					this.time = [this.recordDetail.startTime,this.recordDetail.endTime]
				this.changeGrade.remarks = res.remarks
				res.code == 0 ? this.loading2 = false : this.loading2 = false
				this.recordDetail.details.forEach(item => { // 获取缓存等级
					let b = false
					for(let i = 0;i<this.changeGrade.items.length;i++){
						if(item.itemId === this.changeGrade.items[i].itemId){
							item.finalGradeId = this.changeGrade.items[i].finalGradeId
							item.thisMonthDiscount = this.changeGrade.items[i].gradeDiscount
							b = true
							break
						}
					}
					if(item.finalGradeId == null || item.finalGradeId == '' || item.finalGradeId == 0) { // 最终调整等级被删除显示为空
						item.finalGradeId = ''
					}else if(!b){
						let obj = {
							itemId:item.itemId,
							finalGradeId:item.finalGradeId,
							gradeDiscount:item.thisMonthDiscount
						}
						this.changeGrade.items.push(obj)
					}
				})
			})
		},

		sizeChange(size) { // 条数
	  this.pageInfo.count = size;
	  this.getGradeChangeDetail()
    },
    currentChange(page) { // 页数
	  this.pageInfo.page = page;
	  this.getGradeChangeDetail()
		},
		
		// ======== 转换 ========
		formatData(row, col, val){ // 列表显示判断是否有值
			if(val === undefined || val === null || val === ''){
				return '-'
			}else{
				return val
			}
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

		changeStatusFormatter(changeStatus){ // 确认状态
			switch(changeStatus) {
        case 1:
			this.isUpload = true
          return "待确认"
          break
				case 2:
					this.isUpload = false
					this.showRemark = false
						return "审批中";
					break;
				case 3:
					this.isUpload = false
						return "已通过";
					break;
				case 4:
					this.isUpload = false
						return "已拒绝";
					break;
				case 5:
					this.isUpload = false
					this.showRemark = false
					return "已过期";
					break;
				default:
					this.isUpload = false
					return "-";
				break;
      }
		},

		timeFormatter(createTime) { // 时间转换
      return timeFormat(createTime);
		},
		// ========  ========
		
		fileDownload(data, fileName) {
      let blob = new Blob([data], {
        type: "application/ms-excel"
      });
      let filename = fileName || "filename.xls";
      if (typeof window.navigator.msSaveBlob !== "undefined") {
        window.navigator.msSaveBlob(blob, filename);
      } else {
        var blobURL = window.URL.createObjectURL(blob);
        var tempLink = document.createElement("a");
        tempLink.style.display = "none";
        tempLink.href = blobURL;
        tempLink.setAttribute("download", filename);
        if (typeof tempLink.download === "undefined") {
          tempLink.setAttribute("target", "_blank");
        }
        document.body.appendChild(tempLink);
        tempLink.click();
        document.body.removeChild(tempLink);
        window.URL.revokeObjectURL(blobURL);
      }
		},
	},
	watch: {
		"pageInfo.brandId":function(){
			this.pageInfo.page = 1;
			this.getGradeChangeDetail()
		},
		"pageInfo.categoryId":function(){
			this.pageInfo.page = 1;
			this.getGradeChangeDetail()
		},
		"pageInfo.originalGradeId":function(){
			this.pageInfo.page = 1;
			this.getGradeChangeDetail()
		},
		"pageInfo.finalGradeId":function(){
			this.pageInfo.page = 1;
			this.getGradeChangeDetail()
		},
		"pageInfo.content":function(){
			this.pageInfo.page = 1;
			this.getGradeChangeDetail()
		}
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
		h4 {
			display: inline-block;
			font-weight: 350;
			font-size: 16px;
			margin: 0 20px;
		} 
	}
	.nav {
		padding-bottom: 15px;
		margin-left: 30px;
		margin-right: 20px;
		.nav_header {
			margin-bottom: 10px;
			display: flex;
			justify-content: space-between;
			align-items: center;
			.nav_hea_right {
				display: inherit;
				.box-input {
					width: 225px;
				}
				.box-btn {
					margin-left: 5px;
				}
			}
		}
	}
	.good_expot {
		display: flex;
    justify-content: flex-end;
    align-items: center;
		.expot-btn {
			margin-right: 20px;
		}
	}
	.underline {
		border-top: 1px solid #dcdcdc;
		margin: 10px 0;
	}
	.box-has-info{
		margin-left: 30px;
		overflow: hidden;
		.half-width{
			width: 20%;
			box-sizing: border-box;
			float: left;
			height: 100px;
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
	.discount-input {
		font-weight:bold;
		color: #606266;
		text-align: center;
		.el-input__inner{
			height: 30px;
		}
	}
	
}

</style>