<template>
    <div class="check-goods" v-loading="loading">
        <div class="nav-bar">
          <el-select size="mini" v-model="pageInfo.classifyId" clearable placeholder="选择分类" style="width:180px">
            <el-option v-for="item in categoryList" :key="item.id" :value="item.id" :label="item.label" >
              <div v-if="item.parentId === 0">{{item.label}}</div>
              <div v-else>&nbsp;&nbsp;&nbsp;├{{item.label}}</div>
            </el-option>
          </el-select>
          <el-select size="mini" v-model="pageInfo.brandId" clearable placeholder="选择品牌" style="width:160px">
            <el-option v-for="item in brandlist" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
          <el-select size="mini" style="width:100px" v-model="pageInfo.saleStatus" clearable placeholder="选择状态">
            <el-option v-for="item in saleStatusList" :key="item.value" :value="item.value" :label="item.label"></el-option>
          </el-select>
          <el-button class="mini-search-btn" @click="filter" style="float: right;margin-left:5px;">搜索</el-button>
          <el-input class="box_input" size="mini" style="float: right;width:200px;" placeholder="商品编号/商品名称/存货编码/存货名称" v-model.trim="content" />
          <el-select
          class="content_select"
          placeholder="选择类型"
          size="mini"
          style="width:100px;float: right;"
          v-model="contentType"
          clearable
        >
          <el-option label="商品名称" :value="1"></el-option>
          <el-option label="商品编号" :value="2"></el-option>
          <el-option label="货品编号" :value="3"></el-option>
          <el-option label="条形码" :value="4"></el-option>
        </el-select>
        </div>

        <el-table
          ref="multipleSelect"
          :data="tableData"
          tooltip-effect="dark"
          @select="select"
          @select-all="selectAll"
          style="text-align:center;"
          border header-row-class-name="header-row" 
          @selection-change="handleSelectionChange">
          <el-table-column align="center" type="selection" width="55"></el-table-column>
          <el-table-column align="center" label="商品编号" prop="goodsNo"></el-table-column>
          <el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column>
          <el-table-column align="center" label="商品分类" prop="categoryName"></el-table-column>
          <el-table-column align="center" label="商品上架状态" prop="saleStatus" :formatter="formatStatus"></el-table-column>
          <el-table-column align="center" label="品牌" prop="brandName"></el-table-column>
        </el-table>
        <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
        <div class="foot-btn">
			<el-button class="mini-search-btn" style="margin-left:47%;" @click="handleSubmit">确定</el-button>
			<el-button size="mini" @click="handleCancel">返回</el-button>
		</div>
    </div>
</template>
<script>
import { getGoodsList } from '@/views/goods/goodslist/data/goodslistManage.js'
import page from "@/components/pagination/index"
export default {
  props: ['saleStatus', 'goodsType', 'isNew', 'selectGoodsData'],
  data() {
    return {
      categoryId: '',
      brandId: '',
      contentType: '',
      content: '',
      categoryList: [],
      brandlist: [],
      pageInfo: {
        page: 1,
        size: 10,
        saleStatus:-1,
        classifyId:-1,
        brandId:-1,
        contentType: ''
      },
      page:1,
      tableData: [],
      total: 0,
      loading: false,
      multipleSelect:[],
      saleStatusList:[
        { value: 1, label: "未上架" },
        { value: 3, label: "已上架" },
        { value: 2, label: "审批中" }
      ],
      selected:[],
      isSelect:false
    }
  },
  components: { page },
  created() {
    this.pageInfo.saleStatus = this.saleStatus
    if(this.goodsType !== undefined && this.goodsType !== ''){
			this.pageInfo.goodsType = this.goodsType
		}
    this.multipleSelect = []
    if (this.selectGoodsData) {
      this.multipleSelect = this.multipleSelect.concat(this.selectGoodsData)//第一次进入时
    }
  
    this.getTableData()
  },
  methods: {
    selectRow(){//商品请求数据变化时，重新选择行（如，删除、数据变化）
      // this.$refs.multipleSelect.clearSelection();
      
      this.multipleSelect.forEach(row1 => {
        this.tableData.forEach(row2 => {
          if(row1.id === row2.id){
            this.$refs.multipleSelect.toggleRowSelection(row2);
          }
        })
      });
    
    },
    getTableData(){
      //初始化分类名称显示 (获取所有分类级联)
      this.$http.getAllClassifyPoList(this).then(res => {
        let arr = []
        for(let i = 0;i<res.length;i++){
          arr.push(res[i])
          if(res[i].children != undefined && res[i].children.length >0){
            res[i].children.forEach(item => {
              arr.push(item)
            })
          }
        }
        this.categoryList = arr;
      })
      // 获取所有品牌列表
      this.$http.getBrandPoList(this, { page: 1, size: 10000, openFlag:1}).then(res => {  
        this.brandlist = res.data.list;
      })
      getGoodsList(this, this.pageInfo).then(res => {
        this.isSelect = false
        this.tableData = res.tableData.list;
        this.total = res.tableData.total;
        if (this.multipleSelect.length > 0) {
          this.multipleSelect.forEach(row1 => {//重新获取数据时，判断哪些选中了
            this.tableData.forEach(row2 => {
              if(row1.id === row2.id){
                this.$refs.multipleSelect.toggleRowSelection(row2);
                this.selected.push(row2)
              }
            })
          });
        }
      })
    },

    sizeChange(size) {
      this.pageInfo.size = size
    },

    currentChange(page) {
      this.pageInfo.page = page
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

    setArr(arr){ //去重
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
          this.tableData.forEach(row2 => {
            if(row1.id === row2.id){
              this.$refs.multipleSelect.toggleRowSelection(row2);
            }
          })
        });
        this.isSelect = true
      }
    },
    
    formatStatus(row,col,val){
      switch(val) {
        case '1':
          return "未上架";
          break;
        case '2':
          return "审批中";
          break;
        case '3':
          return "已上架";
          break;
      }
    },

    filter() {
      if(this.pageInfo.page === this.page){
        this.pageInfo.page = 1
      }
      this.page = this.pageInfo.page
      if(this.pageInfo.classifyId === -1 || this.pageInfo.classifyId === ''){
        this.pageInfo.classifyId = undefined;
      }
      if(this.pageInfo.brandId === -1 || this.pageInfo.brandId === ''){
        this.pageInfo.brandId = undefined;
      }
      if(this.pageInfo.saleStatus === -1 || this.pageInfo.saleStatus === ''){
        this.pageInfo.saleStatus = undefined;
      }
      this.pageInfo.contentType = this.contentType;
      this.pageInfo.content = this.content;
      this.loading = true
      getGoodsList(this, this.pageInfo).then(res => {
        this.isSelect = false
        this.tableData = res.tableData.list;
        this.total = res.tableData.total;
        if (this.multipleSelect.length > 0) {
          this.multipleSelect.forEach(row1 => {//重新获取数据时，判断哪些选中了
            this.tableData.forEach(row2 => {
              if(row1.id === row2.id){
                this.$refs.multipleSelect.toggleRowSelection(row2);
                this.selected.push(row2)
              }
            })
          });
        }
        res.success ? this.loading = false : this.loading = false
      })
    },

    handleSubmit() { // 确定操作
      // this.multipleSelect.reverse()
      console.log('caozz----',this.multipleSelect)
      this.multipleSelect=this.setArr(this.multipleSelect)
      this.$emit('submit',this.multipleSelect);
    },

    handleCancel() { // 返回操作
      // this.multipleSelect = []
      this.multipleSelect = this.multipleSelect.concat(this.selectGoodsData)
      this.$emit('cancel')
      // this.selectRow()
      
    }
  },
  watch: {
    pageInfo: {
      handler() {
        this.filter()
      },
      deep: true
    },
    selectGoodsData: {//请求的分销商数据变化时
      handler() {
        // this.multipleSelect = []
        this.multipleSelect = this.multipleSelect.concat(this.selectGoodsData)
        this.selectRow()
      },
      deep: true
    }
  }
}

</script>
<style lang="scss" scoped>
.check-goods {
  background-color: white;
  .nav-bar {
    padding: 10px 0;
  }
  .header-row {
    @include table-header-color;
  }
  .foot-btn {
      margin-top:10px;
  }
  .content_select{
    /deep/.el-input{
      .el-input__inner{
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
      }
    }
  }
  .box_input{
    /deep/.el-input__inner{
      border-top-left-radius: 0;
      border-bottom-left-radius: 0;
      border-left: none;
    }
  }
}
</style>
