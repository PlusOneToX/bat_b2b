<template>
	<div class="adduser">
    <div class="add_header">
      <div>
        <span class="add_span">账号中心ID为空，不允许被选择为审批人，需先到用户列表修改补充</span>
      </div>
      
      <div>
        <el-input size="mini" style="width:250px;" placeholder="用户名" v-model.trim="pageInfo.content" @keyup.enter.native="filter()"></el-input>
        <el-button class="mini-search-btn add_btn" @click="filter()">搜索</el-button>
      </div> 
    </div>

    <el-table
      class="tableCenter"
      ref="multipleSelect"
      :data="allSales"
      tooltip-effect="dark"
      @select="select"
      @select-all="selectAll"
      border header-row-class-name="header-row" 
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :selectable="selectable"></el-table-column>
      <el-table-column label="账号中心ID" prop="rockAccountId" align="center" show-overflow-tooltip :formatter="formRockData"></el-table-column>
      <el-table-column label="姓名" prop="realName" align="center" show-overflow-tooltip></el-table-column>
      <el-table-column label="用户名" prop="userName" align="center" show-overflow-tooltip></el-table-column>
      <el-table-column label="用户编号" prop="erpUserNo" align="center" show-overflow-tooltip></el-table-column>
      <el-table-column label="销售组织" prop="organizationId" align="center" show-overflow-tooltip></el-table-column>
      <el-table-column label="销售部门" prop="departmentName" align="center" show-overflow-tooltip></el-table-column>
    </el-table>
    <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>

    <el-button class="mini-search-btn" style="margin-left: 45%;margin-top:20px;" autofocus size="mini" @click="handleSubmitOne"> 确定</el-button>
    <el-button autofocus size="mini" @click="handleBack">返回</el-button>
	</div>
</template>

<script>
import page from "@/components/pagination/index"
import {adminAddData} from "@/views/system/systemData";
	export default {
		props: ['selectUsers'],
		data() {
			return {
				allSales: [], // 用户名数组
				total: 0,
				pageInfo: {
					page: 1,
					size: 10,
					status:1,
					contentType: 1,
					content:''
				},
				selected:[],
				isSelect:false,
        		saleOrganizationList: []
			}
		},
		components: { page },
		created() {
			this.multipleSelect = []
      this.multipleSelect = this.multipleSelect.concat(this.selectUsers)//第一次进入时
			this.userList();
		},
		methods: {
      selectable(row, index) { // 有ID才能选，为空没ID则不能选择
        if(row.rockAccountId == "") {
          // console.log('不能选择该用户')
          return false
        }else {
          // console.log('可以选择该用户')
          return true
        }
      },
			filter(){
				this.pageInfo.page = 1;
				this.userList();
			},
			
			handleSubmitOne() { // 选中的用户名值
				this.$emit('submitOne',this.multipleSelect);
			},
			 
			handleBack() { // 返回
				this.multipleSelect = []
			  	this.multipleSelect = this.multipleSelect.concat(this.selectUsers)
				this.$emit('submitBack')
				this.selectRow()
			},
      userList() {  // 用户列表
				adminAddData(this,this.pageInfo).then(res => {
					this.allSales = [];
					this.allSales = res.data.list
					this.total = res.data.total
					this.isSelect = false
					this.multipleSelect.forEach(row1 => {//重新获取数据时，判断哪些选中了
						this.allSales.forEach(row2 => {
							if(row1.id === row2.id){
								this.$refs.multipleSelect.toggleRowSelection(row2);
								this.selected.push(row2)
							}
						})
					});
					if(this.saleOrganizationList.length === 0){
						this.getOrgList()
					}else{
						this.allSales.forEach(item =>{
							for(let i = 0;i<this.saleOrganizationList.length;i++){
								if(item.organizationId === this.saleOrganizationList[i].id){
									item.organizationId = this.saleOrganizationList[i].name
									break
								}
							}
						})
					}
				})
			},
			getOrgList(){ // 销售组织初始化
				this.$http.getOrganizationPoList(this, {page:1,size:100}).then(res => {		
					this.saleOrganizationList = res.data.list;
					this.allSales.forEach(item =>{
						for(let i = 0;i<this.saleOrganizationList.length;i++){
						if(item.organizationId === this.saleOrganizationList[i].id){
							item.organizationId = this.saleOrganizationList[i].name
							break
						}
						}
					})
				})
			},
			selectRow(){ // 请求数据变化时，重新选择行（如，删除、数据变化）
				this.$refs.multipleSelect.clearSelection();
				this.multipleSelect.forEach(row1 => {
				this.allSales.forEach(row2 => {
					if(row1.id === row2.id){
					this.$refs.multipleSelect.toggleRowSelection(row2);
					}
				})
				});
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
			selectAll(selection){ // 全选时调用
				this.isSelect = true
				if(selection.length === 0){
					this.allSales.forEach(row => {
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
			
			setArr(arr){ // 去重
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
			handleSelectionChange(val) {//当切换页面时的作用
				if(val.length === 0 && this.multipleSelect.length != 0 && !this.isSelect){
				this.multipleSelect.forEach(row1 => {
					this.allSales.forEach(row2 => {
					if(row1.id === row2.id){
						this.$refs.multipleSelect.toggleRowSelection(row2);
					}
					})
				});
				this.isSelect = true
				}
			},
			
			sizeChange(size) {  // 分页
				this.pageInfo.size = size;
				this.pageInfo.page = 1;
				this.userList();
			},
		
			currentChange(page) { 	// 分页
				this.pageInfo.page = page;
				this.userList();
      },
      // ======== 转换 ========
      formRockData(row, col, val) { // 账号中心ID是否为空判断
        if(val === undefined || val === null || val === '' || val == 0){
          return '-'
        }else{
          return val
        }
      }
		},
		 watch: {
			selectUsers: {//请求的数据变化时
				handler() {
				this.multipleSelect = []
			  	this.multipleSelect = this.multipleSelect.concat(this.selectUsers)
				this.selectRow()
				},
				deep: true
			}
	  }
		
	}
</script>

<style lang="scss">
  .adduser {
    .add_header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin: 10px 0;
      .add_span {
        font-size: 14px;
        color: #333
      }
      .add_btn {
        position: relative;
        top: -1px;
      }
   }
  }
</style>
