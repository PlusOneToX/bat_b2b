<template>
	<div class="good-grade-list">
		<header >
		  <h4>商品等级变动列表</h4>
		</header>
		<el-tabs v-model="pageInfo.status">
			<el-tab-pane label="全部" name="0"></el-tab-pane>
			<el-tab-pane label="待确认" name="1"></el-tab-pane>
			<el-tab-pane label="审批中" name="2"></el-tab-pane>
			<el-tab-pane label="已确认" name="3"></el-tab-pane>
      <el-tab-pane label="已拒绝" name="4"></el-tab-pane>
      <el-tab-pane label="已过期" name="5"></el-tab-pane>
		</el-tabs>
    <div class="nav clearfix">
      <el-form :inline="true">
        <div>
          <el-button :loading="recountLoading" class="mini-batch-btn" size="mini" style="margin-left:15px;" @click.prevent="recountGrade()">重新计算</el-button>
        </div>
      </el-form>
    </div>
    <div>
      <el-table :data="tableData" @selection-change="handleSelectionChange" border header-row-class-name="header-row" ref="multipleSelect" class="tableCenter" v-loading="loading" :height="tableHeight">
        <el-table-column align="center" label="单据编号" prop="id" :min-width="100"> </el-table-column>
        <el-table-column align="center" :formatter="createType" label="生成类型" prop="createType" :min-width="120"> </el-table-column>
        <el-table-column align="center" :formatter="formatTime" label="生成时间" prop="createTime" :min-width="160"> </el-table-column>
        <el-table-column align="center" :formatter="changeStatus" label="确认状态" prop="changeStatus" :min-width="120"> </el-table-column>
        <el-table-column align="center" label="操作人" prop="creatorName" :min-width="120"> </el-table-column>
        <el-table-column align="center" label="操作" :min-width="120">
          <template slot-scope="scope">
            <span v-show="scope.row.createStatus === 1">数据生成中</span>
            <span v-show="scope.row.createStatus === 3">生成失败</span>
            <el-button v-show="scope.row.createStatus === 2" @click="handleEdit(scope.row)" class="mini-search-btn" type="success"> 查看 </el-button>
          </template>
        </el-table-column>
      </el-table>
      <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    </div>
	</div>
</template>
<script type="text/javascript">
import { getGradeChangeList,getGradeChangeCount,recountGrade } from "@/views/goods/goodGrade/goodGradeData.js"
import page from "@/components/pagination"
import { timeFormat } from "@/utils/timeFormat.js";
export default {
  name: 'goodGradeChangeList',
  components: { page },
	data() {
		return{
      tableHeight: 600, //给表格高度一个默认高度，以防没有计算到表格高度
      loading: false,
      recountLoading:false,
			pageInfo: {
				page: 1,
				count: 10,
				status: 0
			},
			pageInfos: {
				page: 1,
				count: 10000,
			},
      total:0,
      page1:1,
      page2:1,
      page3:1,
      page4:1,
      page5:1,
      page6:1,
      tableData:[]
		}
  },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
  },
	activated() {
    this.getParams();
  },

	methods: {
		getParams(){
      this.getGradeChangeList()
    },
    getGradeChangeList(){
      this.loading = true
      this.changePage()
      getGradeChangeList(this,this.pageInfo).then(res =>{
        if(res.code === 0){
          let ids = [];
          res.records.forEach(item =>{
            item.creatorName = '-'
            if(item.creatorId !== undefined &&  item.creatorId !== null && item.creatorId !== 0){
              ids.push(item.creatorId);
            }
          })
          let obj ={};  
          let temp=[];  
          for( let i = 0; i < ids.length; i++ ) {  //..用来去重ids
            let type= Object.prototype.toString.call(ids[i]);//不加类型 分不清 1 '1'    
            if( !obj[ ids[i] +type] ) {  
              temp.push( ids[i] );  
              obj[ ids[i]+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读  
            }
          }  
          ids = temp;
          this.tableData = res.records
          
          if(ids.length !== 0){
            this.$api.get(this, 'admin/u/po/admin/ids', { ids: ids.join(',') }).then(res => {//操作人
              if(res.code == 0) {
                this.tableData.forEach(item => {
                  res.admins.forEach(val => {
                    if(val.id == item.creatorId) {
                      item.creatorName = val.name;
                    }
                  })
                })
              }
            })
          }
        }
        res.code == 0 ? this.loading = false : this.loading = false
      })
      
      getGradeChangeCount(this,this.pageInfo).then(res => { // 总列数
        this.total = res.count;
      })
    },
    handleSelectionChange(){

    },

    recountGrade(){//重新计算
      this.recountLoading = true
      recountGrade(this).then(res =>{
        this.recountLoading = false
        if(res.code === 0){
          this.$message.success({
              message: "商品等级计算提交成功，请耐心等待计算结果",
              duration: 3 * 1000,
              onClose: () => { 
                this.getParams();
              }
            })
        }
      })
    },

    handleEdit(row){ // 查看操作
      this.$router.push({ name: 'goodGradeChangeDetail',query:{id:row.id}})
    },
    
    changeStatus(row, col, val) { // 单据状态
      switch(val) {
        case 1:
          return "待确认";
          break;
        case 2:
          return "审批中";
          break;
        case 3:
          return "已通过";
          break;
        case 4:
          return "已拒绝";
          break;
        case 5:
          return "已过期";
          break;
        default:
          return "-";
          break;
      }
    },
    //单据类型
    createType(row, col, val) {
      switch(val) {
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
    formatTime(row, col, val) {
      return timeFormat(val);
    },
    sizeChange(size){
      this.pageInfo.count = size;
      this.pageInfo.page = 1;
      this.changeOPage()
    },
    currentChange(page){
      this.pageInfo.page = page;
      this.changeOPage()
    },
    changeOPage(){
      switch(Number(this.pageInfo.status)) {
        case 0:
          this.page1 = this.pageInfo.page
          break;
        case 1:
          this.page2 = this.pageInfo.page
          break;
        case 2:
          this.page3 = this.pageInfo.page
          break;
        case 3:
          this.page4 = this.pageInfo.page
          break;
        case 4:
          this.page5 = this.pageInfo.page
          break;
        case 5:
          this.page6 = this.pageInfo.page
          break;
      }
    },
    changePage(){
      switch(Number(this.pageInfo.status)) {
        case 0:
          this.pageInfo.page = this.page1 
          break;
        case 1:
          this.pageInfo.page = this.page2
          break;
        case 2:
          this.pageInfo.page = this.page3
          break;
        case 3:
          this.pageInfo.page = this.page4
          break;
        case 4:
          this.pageInfo.page = this.page5
          break;
        case 5:
          this.pageInfo.page = this.page6
          break;
      }
    }
  },
  watch: {
    pageInfo: {
        handler() {
          this.getGradeChangeList()
        },
        deep: true
      }
  }
}

</script>
<style lang="scss" >
.good-grade-list {
    background-color: white;
	padding-bottom: 20px;
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
	.nav {
		padding-bottom: 15px;
	}
	.el-tabs__nav{
		margin-left: 30px;
		margin-right: 30px;
	}
}

</style>
