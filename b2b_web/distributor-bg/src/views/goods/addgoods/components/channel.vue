<template>
	<div class="check-goods">
		<div>
			<el-button style="margin-left: 10px;float: right; margin-bottom:10px;" class="mini-search-btn" @click="filter()">搜索</el-button>
			<el-input size="mini" style="width: 200px;float: right;" placeholder="货品名称/货品编号/条形码" v-model.trim="content" @keyup.enter.native="filter()"></el-input>
		</div>
			<el-table 
			border 
			class="tableCenter"
			:data="tableData" 
			ref="multipleSelect" 
			tooltip-effect="dark" 
			@select="select" 
			@select-all="selectAll" 
			header-row-class-name="header-row" 
			:height="tableHeight"
			@selection-change="handleSelectionChange">
				<!-- <el-table-column align="center" type="selection" width="55" :selectable="selectable"></el-table-column> -->
				<el-table-column align="center" type="selection" width="55"></el-table-column>
				<el-table-column align="center" label="货品名称" prop="itemName" show-overflow-tooltip></el-table-column>
				<el-table-column align="center" label="英文名称" prop="itemNameEn" show-overflow-tooltip></el-table-column>
				<el-table-column align="center" label="货品编号" prop="itemCode"></el-table-column>
				<el-table-column align="center" label="条形码" prop="barCode"></el-table-column>
				<!-- <el-table-column label="MOQ" prop="moq"></el-table-column> -->
				<!-- <el-table-column label="物料编码id" prop="itemErpId"></el-table-column> -->
				<el-table-column align="center" label="重量" prop="weight"></el-table-column>
				<el-table-column align="center" label="尺寸">
					<template slot-scope="scope">
            {{scope.row.length+'*'+scope.row.width+'*'+scope.row.height}}
          </template>
				</el-table-column>
				<el-table-column align="center" label="尺寸单位" prop="unit"></el-table-column>
				<el-table-column align="center" label="品类" prop="categoryName"></el-table-column>
				<el-table-column align="center" label="默认价格" prop="salePrice">
					<template slot-scope="scope">
						<p>¥{{scope.row.salePrice|NumFormat}}</p>
					</template>
				</el-table-column>
				<el-table-column align="center" label="商品名称" prop="goodsName" show-overflow-tooltip></el-table-column>
			</el-table>
			<div style="float:right;margin-top:20px;">
				<el-button class="mini-search-btn" plain @click="learnLess" v-if="LessShow">上一页</el-button>
				<el-button class="mini-search-btn" plain @click="learnMore" v-if="MoreShow">下一页</el-button>
			</div>
		<div class="foot-btn" style="text-align: center;">
			<el-button class="mini-search-btn" :loading="loading" @click="handleSubmit()">确定</el-button>
			<el-button  size="mini"  @click="handleCancel">返回</el-button>
		</div>
	</div>
</template>
<script>
import page from "@/components/pagination/index"
export default {
	name : 'channel',
	props: {
		saleStatus: {
			type: Number,
		},
		selectItemsData: Array,
		goodsId:0,
		productlinelist:Array
	},
	data() {
		return {
			tableHeight: 600, //给表格高度一个默认高度，以防没有计算到表格高度
			content: '',
			pageInfo: {
				page: 1,
				size: 10,
			},
			tableData: [],
			total: 0,
			loading: false,
			multipleSelect:[],
			LessShow: true,
			MoreShow: true,
			selected:[],
			isSelect:false,
			grades:[],
			addloading: '',
		}
	},
	components: { page },
	created() {
		const documentHeight = document.body.clientHeight;
		this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
		this.multipleSelect = []
		this.multipleSelect = this.multipleSelect.concat(this.selectItemsData) // 第一次进入时
		if(this.pageInfo.page == 1) {
			this.LessShow = false;
		}
		this.getTableData()
	},
	methods: {
		selectable(row, index) { //..是否可勾选
      if(row.produceFlag == 1) {
        return true
      }else {
        return false
      }
    },
		getTableData() { // 分销商等级列表
			this.$http.getGradePoList(this, { page: 1, size: 10000, openFlag: 1 }).then(res => {
				if (res.success) {
					this.grades = this.sortKey(res.data.list,'sort')
					this.lead()
				}
			})
		},
		sortKey(array,key){
			return array.sort(function(a,b){
				var x = a[key];
				var y = b[key];
				return ((x<y)?-1:(x>y)?1:0)
			})
		},
		learnMore() {
			this.pageInfo.page++
			if(this.pageInfo.page === 1) {
				this.LessShow = false;
			}
			this.lead()
		},
		learnLess() {
			this.pageInfo.page--
			this.lead()
		},
		lead() {
			this.addloading = this.$loading({
				lock: true,
				text: '数据请求中....',
				spinner: 'el-icon-loading',
				background: 'rgba(0, 0, 0, 0.7)'
			});
			this.$http.goodsErpList(this, this.pageInfo).then(res => {
				if (res.success) {
					this.isSelect = false
					this.tableData = []
					if(res.data.list === undefined){
						this.$message({
							message: '没有找到物料，请确保erp物料已审核且已上架！',
							type: 'warning'
						})
						this.addloading.close()
						return
					}
					res.data.list.forEach(item =>{
						for(let i=0;i<this.productlinelist.length;i++){
							if(item.categoryErpId === this.productlinelist[i].erpId){
								item.categoryId = this.productlinelist[i].id
								item.categoryName = this.productlinelist[i].name
								break
							}
						}
						item.advanceSaleFlag=0
						item.onwaySaleFlag = 0
					})
					this.tableData = res.data.list;
					if(this.tableData != undefined && this.tableData != ''){
						if(this.pageInfo.page == 1) {
							this.LessShow = false;
						}else{
							this.LessShow = true;
						}
						if(res.data.list.length < this.pageInfo.size) {
							this.MoreShow = false;
						}else {
							this.MoreShow = true;
						}
						this.multipleSelect.forEach(row1 => {//重新获取数据时，判断哪些选中了
							this.tableData.forEach(row2 => {
								if(row1.itemErpId === row2.itemErpId){
									this.$refs.multipleSelect.toggleRowSelection(row2);
									this.selected.push(row2)
								}
							})
						});
					}
				}
				this.addloading.close()
			})
		},
		selectRow(){ // 请求数据变化时，重新选择行（如，删除、数据变化）
			this.$refs.multipleSelect.clearSelection();
			this.multipleSelect.forEach(row1 => {
				this.tableData.forEach(row2 => {
					if(row1.itemErpId === row2.itemErpId){
						this.$refs.multipleSelect.toggleRowSelection(row2);
					}
				})
			});
		},
		select(selection, row){ // 单选时调用
			this.isSelect = true
			let d = false
			for(let i = 0;i<this.multipleSelect.length;i++){
				if(this.multipleSelect[i].itemErpId === row.itemErpId){
					this.multipleSelect.splice(i,1)
					d = true
					break
				}
			}
			if(!d){
				if(row.salePrice == undefined || row.salePrice === 0){
					this.$message.error("货品默认价格不能为零或空，请先维护ERP价目表中的默认价格！")
					this.selectRow()
					return
				}
				if(row.goodsId !== undefined && row.goodsId !== null && row.goodsId !== 0 && row.goodsId !== this.goodsId){
					this.$message.error("货品已被分配！")
					this.selectRow()
					return
				}
				this.multipleSelect.push(row)
				this.multipleSelect = this.setArr(this.multipleSelect)
			}
    },
    
		selectAll(selection){ // 全选时调用
			this.isSelect = true
			let arr = []
			arr = arr.concat(selection)
			if(selection.length === 0){
				this.tableData.forEach(row => {
					for(let i = 0;i<this.multipleSelect.length;i++){
						if(this.multipleSelect[i].itemErpId === row.itemErpId){
							this.multipleSelect.splice(i,1)
							break
						}
					}
				})
			}else{
				for(let j = 0;j<arr.length;j++){
					if(arr[j].salePrice == undefined || arr[j].salePrice === 0){
						arr.splice(j,1)
						j--
						continue
					}
					if(arr[j].goodsId !== undefined && arr[j].goodsId !== null && arr[j].goodsId !== 0 && arr[j].goodsId !== this.goodsId) {
						arr.splice(j,1)
						j--
						this.$message.error('货品已被分配')
					}
				}
				this.multipleSelect = this.setArr(this.multipleSelect.concat(arr))
				
				if(arr.length != selection.length){
					this.selectRow()
				}
			
			}
		},
		
		setArr(arr){ // 去重
			let obj ={};  
			let temp=[];  
			for( let i = 0; i < arr.length; i++ ) {  
				let type= Object.prototype.toString.call(arr[i].itemErpId);//不加类型 分不清 1 '1'    
					if( !obj[ arr[i].itemErpId +type] ) {  
						temp.push( arr[i] );  
						obj[ arr[i].itemErpId+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读  
					}
				} 
			return temp; 
		},
		sizeChange(size) {
			this.pageInfo.size = size;
		},
		currentChange(page) {
			this.pageInfo.page = page;
		},
		handleSelectionChange(val) { // 当切换页面时的作用
			if(val.length === 0 && this.multipleSelect.length != 0 && !this.isSelect){
				this.multipleSelect.forEach(row1 => {
					this.tableData.forEach(row2 => {
						if(row1.itemErpId === row2.itemErpId){
							this.$refs.multipleSelect.toggleRowSelection(row2);
						}
					})
				});
				this.isSelect = true
			}
		},
		filter() { // 搜索操作
			this.pageInfo.page = 1
			this.pageInfo.size = 10
			this.pageInfo.content = this.content
			this.lead()
		},
		handleSubmit() { // 确定操作
			this.multipleSelect.map(item => {
				item.saleStatus = 3
				item.scalePrices.map(val => {
					if (item.goodsId) {
						val.id = item.id
						val.goodsId = item.goodsId
					}
					val.itemId = item.itemErpId
				})
			})
			this.$emit('submit',this.multipleSelect);
		},
		handleCancel() { // 返回操作
			this.multipleSelect = []
			this.multipleSelect = this.multipleSelect.concat(this.selectItemsData)
			this.$emit('cancel')
		}
	},
	watch: {
		selectItemsData: { // 请求的分销商数据变化时
			handler() {
				this.multipleSelect = []
				this.multipleSelect = this.multipleSelect.concat(this.selectItemsData)
				this.selectRow()
			},
			deep: true
		},
	}
}

</script>
<style lang="scss" scoped>
.check-goods {
	background-color: white;
	.header-row {
		@include table-header-color;
	}
	.foot-btn {
		
		margin-top:50px;
	}
	.el-dialog__body{
		padding: 10px 20px;
	}
}

</style>
