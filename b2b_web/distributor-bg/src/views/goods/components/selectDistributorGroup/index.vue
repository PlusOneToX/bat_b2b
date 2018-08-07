<template>
	<div class="check-distributor">
		<!-- <div>
			<el-select size="mini"  v-model="pageInfo.openFlag" placeholder="状态" style="width: 140px;float:right;margin-right:5px;" clearable>
				<el-option label="启用" value="1"> </el-option>
				<el-option label="停用" value="0"> </el-option>
			</el-select>
		</div> -->
		<el-table
			ref="multipleSelect"
			:data="tableData"
			tooltip-effect="dark"
			@select="select"
			@select-all="selectAll"
			style="text-align:center;"
			border header-row-class-name="header-row" 
			@selection-change="handleSelectionChange"
      v-loading="loading">
			<el-table-column align="center" type="selection" with="100" :selectable="checkSelectable"></el-table-column>
			<el-table-column align="center" label="分销商分组ID" prop="erpGroupNo"></el-table-column>
			<el-table-column align="center" label="分销商分组用户名" prop="name"></el-table-column>
			<el-table-column align="center" label="状态" prop="openFlag">
				<template slot-scope="scope">
					<div v-if="scope.row.openFlag===1">启用</div>
					<div v-else>停用</div>
				</template>
			</el-table-column>
		</el-table>
		<page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
		<el-button style="margin-left: 46%;margin-top: 10px;" class="mini-search-btn" @click="handleSubmit">确定</el-button>
		<el-button size="mini" style="margin-top: 10px;" @click="handleCancel">返回</el-button>
	</div>
</template>
<script>
import page from "@/components/pagination";
import { timeFormat } from '@/utils/timeFormat.js'
export default {
	props: ['distributorData', 'isFinancial', 'appType'],
	data() {
		return {
			pageInfo: {
				page: 1,
				size: 10,
				openFlag: 1
			},
			tableData: [],
			total: 0,
			grades: [],
			multipleSelect: [],
			isSelect:false,
      selected:[],
      loading: false
		}
	},
	components: { page },
	created() {
		this.multipleSelect = []
		this.multipleSelect = this.multipleSelect.concat(this.distributorData)
		this.getTableData();
	},
	methods: {
		checkSelectable(row) {
			return row.isDisabled !== 1
		},
		selectRow(){//分销商请求数据变化时，重新选择行（如，删除、数据变化）
			this.$refs.multipleSelect.clearSelection();
			this.multipleSelect.forEach(row1 => {
				this.tableData.forEach(row2 => {
					if(row1.id === row2.id){
						this.$refs.multipleSelect.toggleRowSelection(row2);
					}
				})
			});
		},
		getTableData() {
      this.loading = true
		  this.$http.getDistGroupList(this, this.pageInfo).then(res => {
				this.isSelect = false
				this.selected = []
				if(res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
          if (this.multipleSelect && this.multipleSelect.length > 0) {
            this.multipleSelect.forEach(row1 => {//重新获取数据时，判断哪些选中了
              this.tableData.forEach(row2 => {
                if(row1.id === row2.id){
                  this.$refs.multipleSelect.toggleRowSelection(row2);
                  this.selected.push(row2)
                }
              })
            })
          }
        }
        res.success ? this.loading = false : this.loading = false
			})
		},
		handleSubmit() {
			this.$emit('submit', this.multipleSelect)
		},
		handleCancel() {
			this.multipleSelect = []
			this.multipleSelect = this.multipleSelect.concat(this.distributorData)
			this.$emit('cancel');
			this.selectRow()
		},
		select(selection, row){//单选时调用
			this.isSelect = true
			let d = false
			for(let i = 0;i<this.multipleSelect.length;i++){
				if(this.multipleSelect[i].id === row.id){
					this.multipleSelect.splice(i,1)
					d = true
					break
				}
			}
			if(!d){
				this.multipleSelect.push(row)
				this.multipleSelect = this.setArr(this.multipleSelect)
			}
		},
		selectAll(selection){//全选时调用
			this.isSelect = true
			if(selection.length === 0){
				this.tableData.forEach(row => {
					for(let i = 0;i<this.multipleSelect.length;i++){
						if(this.multipleSelect[i].id === row.id){
							this.multipleSelect.splice(i,1)
							break
						}
					}
				})
			}else{
				this.multipleSelect = this.setArr(this.multipleSelect.concat(selection))
			}
		},
		handleSelectionChange(val) {//当切换页面时的作用
			if(val.length === 0 && this.multipleSelect.length != 0 && !this.isSelect){
				this.multipleSelect.forEach(row1 => {
					this.tableData.forEach(row2 => {
						if(row1.id === row2.id){
							this.$refs.multipleSelect.toggleRowSelection(row2);
						}
					})
				});
				this.isSelect = true
			}
		},
		formatTime(row, col, val) { // 表格时间格式化
      return timeFormat(val)
    },
		//去重
		setArr(arr){
			let obj ={};  
			let temp=[];  
			for( let i = 0; i < arr.length; i++ ) {  
				let type= Object.prototype.toString.call(arr[i].id);//不加类型 分不清 1 '1'    
					if( !obj[ arr[i].id +type] ) {  
						temp.push( arr[i] );  
						obj[ arr[i].id+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读  
					}
				} 
			return temp; 
		},
		filter() {
			this.pageInfo.content = this.content
			this.pageInfo.contentType = this.contentType
			this.pageInfo.page = 1
			this.getTableData();
		},
		// 分页页数
		sizeChange(size) {
			this.pageInfo.size = size;
		},
		// 分页总数
		currentChange(page) {
			this.pageInfo.page = page;
		}
	},
	watch: {
		pageInfo: {
			handler() {
				this.getTableData()
			},
			deep: true
		},
		distributorData: {//请求的分销商数据变化时
			handler() {
				this.multipleSelect = []
				this.multipleSelect = this.multipleSelect.concat(this.distributorData)
				this.selectRow()
			},
			deep: true
		},
		appType: {
			handler(val) {
				this.appType = val
				this.getTableData()
			},
			deep: true
		}
	}
}
</script>
<style lang="scss" scoped>
.check-distributor {
	background-color: white;
	min-height: 100%;
	.header-row {
		@include table-header-color;
	}
	.foot-btn {
		margin-top: 50px;
	}
}

</style>
