<template>
	<div class="good-grade-list">
		<header >
		  <h4>商品等级列表</h4>
		</header>
		<el-tabs v-model="gradeId">
			<el-tab-pane  :key="item.id" v-for="item in grades" :label="item.gradeName" :name="String(item.id)" > </el-tab-pane>
		</el-tabs>
    <div class="nav">

      <div class="Fheader">
        <el-button class="mini-batch-btn" style="margin-left:15px;" @click.prevent="handleChangeGrade">调整等级</el-button>
        <el-select placeholder="商品品类" size="mini" v-model="pageInfo.categoryId" clearable style="width:130px;">
          <el-option :key="item.id" :label="item.name" :value="item.id" v-for="item in categoryList">
          </el-option>
        </el-select>
        <el-select placeholder="品牌" size="mini" v-model="pageInfo.brandId" clearable style="width:130px;">
          <el-option :key="item.id" :label="item.title" :value="item.id" v-for="item in brandList">
          </el-option>
        </el-select>

        <!-- <el-select class="choose" v-model="pageInfo.gradeId" placeholder="请选择" size="mini">
          <el-option v-for="item in grades" :key="item.id" :label="item.gradeName" :value="item.id">
          </el-option>
        </el-select> -->
        <div class="search">
          <el-input v-model.trim="content"  size="mini" placeholder="存货名称/存货编码" style="width: 100%;margin-right: 5px;" ></el-input>
          <el-button class="mini-search-btn" @click.prevent="filter" >搜索</el-button>
        </div>
      </div>
      <!-- <el-form :inline="true">
        <div>
          <button class="mini-batch-btn" size="mini" style="margin-left:15px;" @click.prevent="handleChangeGrade">调整等级</button>
          <el-select placeholder="商品分类" size="mini" v-model="pageInfo.categoryId" clearable style="width:130px;">
            <el-option :key="item.id" :label="item.name" :value="item.id" v-for="item in categoryList">
            </el-option>
          </el-select>
          <el-select placeholder="品牌" size="mini" v-model="pageInfo.brandId" clearable style="width:130px;">
            <el-option :key="item.id" :label="item.title" :value="item.id" v-for="item in brandList">
            </el-option>
          </el-select>
          <el-col :span="5" style="float:right;margin-right: 25px;">
            <el-col :span="19">
              <el-input style="width: 100%;" size="mini" placeholder="存货名称/存货编码" v-model.trim="pageInfo.content"></el-input>
            </el-col>
            <el-col :offset="1" :span="4">
              <button @click.prevent="filter" class="mini-search-btn">搜索</button>
            </el-col>
          </el-col>
        </div>
      </el-form> -->
    </div>
    <div>
      <el-table :data="tableData" border header-row-class-name="header-row" ref="multipleSelect"  class="tableCenter" v-loading="loading" :height="tableHeight">
        <el-table-column align="center" label="存货编号" prop="itemCode" :min-width="120" show-overflow-tooltip> </el-table-column>
        <el-table-column align="center" label="存货名称" prop="itemName" :min-width="120" show-overflow-tooltip> </el-table-column>
        <el-table-column align="center" label="品牌" prop="brandName" :min-width="120" show-overflow-tooltip> </el-table-column>
        <el-table-column align="center" label="品类" prop="categoryName" :min-width="120" show-overflow-tooltip> </el-table-column>
        <el-table-column align="center" label="存销比" prop="inventorySalesRate" :min-width="120" :formatter="formatData" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" label="货品等级" prop="gradeName" :min-width="120" :formatter="formatData" show-overflow-tooltip> </el-table-column>
        <el-table-column align="center" label="货品折扣" prop="gradeDiscount" :min-width="120" :formatter="formatData" show-overflow-tooltip> </el-table-column>
        <el-table-column align="center" label="生效时间" show-overflow-tooltip > 
          <el-table-column align="center" label="起始时间"
            prop="startTime"
            width="120" :formatter="timeFormat">
          </el-table-column>
          <el-table-column align="center" label="结束时间"
            prop="endTime"
            width="120" :formatter="timeFormat">
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="操作" :min-width="120">
          <template slot-scope="scope">
            <el-button @click="handleHistory(scope.row)" class="mini-search-btn"> 查看历史等级 </el-button>
          </template>
        </el-table-column>
      </el-table>
      <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    </div>
	</div>
</template>
<script type="text/javascript">
import { getGoodGradeList,getGoodGrade,getGradeList } from "@/views/goods/goodGrade/goodGradeData.js"
import { getBrandPoList } from '@/views/goods/brand/data/brandManage.js'
import page from "@/components/pagination"
import { monthDay } from "@/utils/timeFormat"
export default {
  name: 'goodGradeList',
  components: { page },
	data() {
		return{
      tableHeight: 600,
      loading: false,
			pageInfo: {
				page: 1,
				count: 10,
        content:'',
        gradeId:''
			},
      grades:[],
      pages:[],
      gradeId:"-1",
      tableData:[],
      total: 0,
      categoryList:[],
      brandList:[],
      content:'',
		}
  },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
  },
	activated() {
    this.getParams();
    this.$api.get(this, 'admin/u/po/productline/list', { page: 1, count: 10000 }).then(res => { // 分类列表
      let ary = [];
      res.productlines.forEach(item => {
        if(item.isOpen) {
          ary.push(item)
        }
      })
      this.categoryList = ary;
    })
    getBrandPoList(this).then(res => {//品牌列表
      let ary = [];
      res.tableData.forEach(item => {
        if(item.isOpen) {
          ary.push(item)
        }
      })
      this.brandList = ary;
    })
	},
	methods: {
    timeFormat(row, col, val) { // 时间戳转换
      if(val === 0){
        return '-'
      }else{
        return monthDay(val)
      }
    },
		getParams(){
      getGoodGrade(this,{status:1}).then(res =>{
        this.grades = []
        this.pages = []
        if(res.goodGrades !== undefined){
          this.grades = res.goodGrades
          this.grades.splice(0,0,{
            id:-1,
            gradeName:'全部'
          })//全部
        }
        this.grades.forEach(item =>{
          this.pages.push(1)
        })
        this.getGoodGradeList()
      })
    },

		handleChangeGrade(){ // 调整等级操作
			this.$router.push({name: 'goodChangeGrade'})
    },

    handleHistory(row){ // 查看历史等级操作
      this.$router.push({name: 'goodhistoryGrade',params:{id:row.id}})
    },

    filter(){ // 搜索操作
      this.pageInfo.content = this.content
    },
    
    getGoodGradeList(){ // 货品等级列表数据
      this.loading = true;
      if(this.gradeId === "-1"){
        this.pageInfo.gradeId = undefined
      }else{
        this.pageInfo.gradeId = this.gradeId
      }
      getGoodGradeList(this,this.pageInfo).then(res =>{
        this.tableData = res.tableData;
        this.total = res.total;
        res.code == 0 ? this.loading = false : this.loading = false
      })
    },

    sizeChange(size){
      this.pageInfo.count = size
      this.pageInfo.page = 1
      this.changeOPage()
    },

    currentChange(page){
      this.pageInfo.page = page;
      this.changeOPage()
    },

    changeOPage(){
      for(let i=0;i<this.grades.length;i++){
        if(this.grades[i].id === this.gradeId){
          this.pages[i] = this.pageInfo.page
        }
      }
    },
    changePage(){
      for(let i=0;i<this.grades.length;i++){
        if(this.grades[i].id === this.gradeId){
          this.pageInfo.page = this.pages[i]
        }
      }
    },

    formatData(row, col, val){ // 存销比 / 产品等级转换
      if(val === undefined || val === null || val === ''){
        return '-'
      }else{
        return val
      }
    }
  },
  watch: {
    pageInfo: {
      handler(){
        this.getGoodGradeList()
      },
      deep: true
    },
    gradeId: {
      handler(){
        this.changePage()
        this.pageInfo.page = 1
        this.getGoodGradeList()
      },
      deep: true
    },
  }
}

</script>
<style rel="stylesheet/scss" lang="scss" scoped>
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
    background-color: white;
    .choose {
      position: relative;
      top: 1px;
    }
    .search {
      float: right;
      display: inline-flex;
    }
    .box-search {
      width: 140px;
    }
    .Fheader {
      width: 98%;
      margin: 10px auto;
      margin-top: 0;
      margin-left: 0;
    }
  }
	.el-tabs__nav{
		margin-left: 30px;
		margin-right: 30px;
	}
}

</style>
