<template>
	<div class="good-change-grade">
		<header >
		  <h4>调整等级</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="onCancel">
			返回商品等级列表
		  </el-button>
		</header>
		<div style="margin:30px">
			<div style="margin-bottom:10px">
				<el-button class="mini-search-btn" @click="itemsShow=true">添加货品</el-button>
				<el-button class="mini-delete-btn" @click="clear">清空</el-button>
			</div>
			<div>
				<el-table :data="tableData" border header-row-class-name="header-row" class="goods-table" style="width: 100%">
					<el-table-column label="存货编码" prop="itemCode" align="center" show-overflow-tooltip> </el-table-column>
					<el-table-column label="存货名称" prop="itemName" align="center" show-overflow-tooltip> </el-table-column>
					<el-table-column label="即时库存" prop="numInWarehouse" align="center" :formatter="formatData"> </el-table-column>
					<el-table-column label="预计入库量" prop="numOnWay" align="center" :formatter="formatData"> </el-table-column>
					<!-- <el-table-column label="锁定数量" prop="numLock" align="center" :formatter="formatData"> </el-table-column> -->
					<el-table-column label="平均发货量" prop="avgSendAmount" align="center" :formatter="formatData"> </el-table-column>
					<el-table-column label="现存销比" prop="inventorySalesRate" align="center" :formatter="formatData"> </el-table-column>
					<el-table-column label="现等级" prop="itemGradeName" align="center" :formatter="formatData"> </el-table-column>
					<el-table-column label="现折扣(%)" prop="gradeDiscount" align="center" :formatter="formatData"> </el-table-column>
					<el-table-column label="更改产品等级" align="center" width="120">
						<template slot-scope="scope">
							<el-select @focus="index=scope.$index" @change="gradeChange" size="mini" v-model="scope.row.finalGradeId" placeholder="选择等级">
								<el-option
									v-for="item in grades"
									:key="item.id"
									:label="item.gradeName"
									:value="item.id">
								</el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column align="center" label="修改折扣(%)"  :min-width="80"> 
						<template slot-scope="scope">
							<el-input class="discount-input" v-model="scope.row.changeDiscount" @input="scope.row.changeDiscount=/^\d+\.?\d{0,2}$/.test(scope.row.changeDiscount)||scope.row.changeDiscount == '' ? scope.row.changeDiscount:Number(scope.row.changeDiscount.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0" />
						</template>
					</el-table-column>
					<el-table-column  align="center" label="操作" width="80">
						<template slot-scope="scope">
							<el-button class="mini-delete-btn" @click="handleDelete(scope.$index)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
			<page v-if="isPage" :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
			<el-form label-width="100px" ref="changeGrade" :model="changeGrade" label-position="right" style="margin-top:20px">
				<el-form-item label="调整生效时间:" prop="time">
					<el-date-picker v-model="time" type="datetimerange" value-format="timestamp" 
						range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" >
					</el-date-picker>
				</el-form-item>
				<el-form-item label="调整说明" prop="remarks">
					<el-input v-model="changeGrade.remarks" type="textarea" :rows="5" style="width:100%;"></el-input>
				</el-form-item>
			</el-form>
			<span style="margin-left: 50%; margin-top:20px;" >
			<el-button class="mini-search-btn" :loading="loading" type="success" @click.prevent="handlerSubmit()"> 保存 </el-button>
			</span>
		</div>
		<!-- 引用组件 -->
		<el-dialog :modal-append-to-body="false" :visible="itemsShow" width="80%" :before-close="closeItemsDialog">
		  <add-item ref="selectGoodItems" :goodsType="1" :saleStatus="3" :selectItemsData="goodItems" @cancel="itemsShow=false" @submit="getItemsData"></add-item>
		</el-dialog>
	</div>
</template>
<script type="text/javascript">
import addItem from "@/views/marketingPromotion/compomemts/selectItem/addItem"
import page from "@/components/pagination"
import { getGradeChangeItems,getGoodGradePo,changeGoodGrade,discountCalculation } from "@/views/goods/goodGrade/goodGradeData.js"
export default {
	name: 'goodChangeGrade',
	components: {
		page,
		addItem
	},
	data() {
		return{
			goods:[],
			pageInfo: {
				page: 1,
				count: 10,
			},
			total:0,
			itemsShow:false,
			loading:false,
			goodItems:[],
			changeGrade:{
				btnType:2,
				remarks:'',
				items:[]
			},
			isPage:false,
			grades:[],
      tableData:[],
			hintStr: '',
			index:0,
			time: []
		}
	},
	created() {
    getGoodGradePo(this,{status:1}).then(res =>{
      if(res.goodGrades !== undefined){
        this.grades = res.goodGrades
      }
    })
    this.getParams()
    this.checkDetail()
	},
	methods: {
		gradeChange(val){
			discountCalculation(this,{itemId:this.tableData[this.index].itemId,gradeId:this.tableData[this.index].finalGradeId}).then(res =>{
				if(res.code == 0){
					this.$set(this.tableData[this.index], 'changeDiscount', res.gradeDiscount)
					// this.tableData[this.index].changeDiscount = JSON.parse(JSON.stringify(res.gradeDiscount))
				}
			})
		},
    checkDetail() { // 是否开启审批
      this.$api.get(this,'admin/u/po/checkDetail').then(res => {
        res.checkConfigs.forEach(item => {
          if(item.ext == 10) { // 10 商品等级变动
            if(item.isOpen == 1) { // isOpen 1.审批开启 2.审批关闭
              this.hintStr = '提交成功，等待审批通过后才会应用。请到”商品——商品等级变动审批“中查看'
            }else if(item.isOpen == 2) {
              this.hintStr = '修改成功'
            }
          }
        })
      })
    },
		formatData(row, col, val){
			if(val === undefined || val === null || val === ''){
				return '-'
			}else{
				return val
			}
		},
		getParams(){
			this.clear()
		},
		onCancel(){
			this.$router.go(-1)
		},
		handleDelete(index){
			let item = this.tableData[index]
			this.goodItems.forEach((res,index) =>{
				if(res.id === item.id){
					this.goodItems.splice(index,1)
				}
			})
			this.tableData.splice(index,1)
			this.total -= 1
		},
		clear(){
			this.goodItems.splice(0,this.goodItems.length)
			this.tableData.splice(0,this.tableData.length)
			this.pageInfo.page = 1
			this.total = 0
		},
		closeItemsDialog(){
			this.$refs.selectGoodItems.handleCancel()
		//   this.itemsShow = false
		},
		sizeChange(size){
			this.pageInfo.count = size;
			this.pageInfo.page = 1;
			this.tableData = this.goodItems.slice((this.pageInfo.page-1)*this.pageInfo.count,this.pageInfo.page*this.pageInfo.count);
		},
		currentChange(page){
			this.pageInfo.page = page;
			this.tableData = this.goodItems.slice((this.pageInfo.page-1)*this.pageInfo.count,this.pageInfo.page*this.pageInfo.count);
		},
		getItemsData(val){
			val.forEach(element => {
				if(element.itemGradeName == undefined){
						element.numInWarehouse='-',
						element.numOnWay='-',
						element.numLock='-',
						element.avgSendAmount='-',
						element.inventorySalesRate="-",
						element.itemGradeName='-',
						element.gradeDiscount=0,
						element.finalGradeId='',
						element.changeGrade=''
				}
			});
			this.goodItems = this.goodItems.concat(val);
			let obj ={};  
			let temp=[];  
			for( let i = 0; i < this.goodItems.length; i++ ) {
				let type= Object.prototype.toString.call(this.goodItems[i].itemId);//不加类型 分不清 1 '1'    
				if( !obj[ this.goodItems[i].itemId +type] ) {  
					temp.push( this.goodItems[i] );  
					obj[ this.goodItems[i].itemId+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读  
				}
			}
			this.goodItems = JSON.parse(JSON.stringify(temp))
			this.itemsShow = false;
			this.total = this.goodItems.length
			if(this.pageInfo.count >this.total){//判断是否需分页
				this.isPage = false
			}else{
				this.isPage = true
			}
			this.getGradeChangeItems();
		},
		getGradeChangeItems(){//通过货品id获取货品等级
			let itemIds =''
			for(let i = 0; i<this.goodItems.length;i++){
				if(itemIds === '' && (this.goodItems[i].itemGradeName === undefined || this.goodItems[i].itemGradeName === '-')) {
					itemIds = String(this.goodItems[i].itemId);
				}else if((this.goodItems[i].itemGradeName === undefined || this.goodItems[i].itemGradeName === '-')){
					itemIds =itemIds  +","+ String(this.goodItems[i].itemId);
				}
			}
			if(itemIds != ''){
				getGradeChangeItems(this,{itemIds:itemIds}).then(res =>{
					this.goodItems.forEach(item2 =>{
						res.items.forEach(item1 =>{
							if(item1.id === item2.itemId){
								item2.numInWarehouse=item1.numInWarehouse
								item2.numOnWay=item1.numOnWay
								item2.numLock=item1.numLock
								item2.avgSendAmount=item1.avgSendAmount
								item2.inventorySalesRate=item1.inventorySalesRate
								item2.itemGradeName=item1.itemGradeName
								item2.gradeDiscount=item1.gradeDiscount
								item2.finalGradeId = item1.gradeId
							}
						})
					})
					this.tableData = this.goodItems.slice((this.pageInfo.page-1)*this.pageInfo.count,this.pageInfo.page*this.pageInfo.count)
			  })
			}else{
				this.tableData = this.goodItems.slice((this.pageInfo.page-1)*this.pageInfo.count,this.pageInfo.page*this.pageInfo.count)
			}
		},
		handlerSubmit(){
			this.loading = true
			this.changeGrade.items = []
			let b = false
			this.goodItems.forEach(item =>{
				if(item.changeDiscount ===  undefined || item.changeDiscount === ''){
					this.$message.error("货品"+item.itemCode+"修改折扣不能为空");
					b = true
					return
				}
				if(item.finalGradeId == ''){
					this.$message.error("请先选择货品"+item.itemCode+"的调整等级");
					b = true
					return
				}
				let obj = {
					itemId:item.itemId,
					finalGradeId:item.finalGradeId,
					gradeDiscount:item.changeDiscount
				}
				this.changeGrade.items.push(obj)
			})
			if(b){
				this.loading = false
				return
			}
			if(this.changeGrade.items.length === 0){
				this.$message.error("货品不允许为空!")
				this.loading = false
				return
			}
			this.changeGrade.startTime = this.time[0]
			this.changeGrade.endTime = this.time[1]
			if(this.changeGrade.startTime === undefined || this.changeGrade.endTime === undefined || this.changeGrade.startTime === 0 || this.changeGrade.endTime === 0){
				this.$message.error('生效起始时间或结束时间不能为空！');
				this.loading = false;
				return
			}
			changeGoodGrade(this,this.changeGrade).then(res =>{
				this.loading = false
				if(res.code == 0) {
					this.$message({
						message: this.hintStr,
						type: 'success',
						duration: 3 * 1000,
						onClose: () => {}
					})
					this.$router.go(-1)
				}
			})
		}
	}
}

</script>
<style lang="scss" >
.good-change-grade {
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
	.el-input__inner{
		font-weight:bold;
		color: #606266;
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
