<template>
  <div class="category-goods-list">
    <header>
      <h4>冻结商品列表</h4>
    </header>
    <div class="nav clearfix">
      <div class="Fheader">
        <el-select placeholder="商品分类" clearable size="mini" v-model.trim="pageInfo.classifyId" style="margin-left:10px;width:130px;">
          <el-option :key="item.id" :label="item.name" :value="item.id" v-for="item in categoryList">
            <div v-if="item.parentId === 0">{{item.name}}</div>
            <div v-else>&nbsp;&nbsp;&nbsp;├{{item.name}}</div>
          </el-option>
        </el-select>

        <el-select placeholder="品牌" clearable size="mini" v-model="pageInfo.brandId" style="width:130px;">
          <el-option :key="item.id" :label="item.name" :value="item.id" v-for="item in brandList">
          </el-option>
        </el-select>

         <el-select placeholder="品类" clearable size="mini" v-model="pageInfo.categoryId" style="width:130px;">
          <el-option :key="item.id" :label="item.name" :value="item.id" v-for="item in productlineList">
          </el-option>
        </el-select>
        <!------商品类型------->
        <el-select
          placeholder="商品类型"
          size="mini"
          v-model="pageInfo.goodsType"
          style="width:130px;"
          clearable
        >
          <el-option
            v-for="item in goodsTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>

        <el-button type="info" class="mini-search-btn"  @click.prevent="filter()" style="float: right;margin-left:5px;margin-right:10px;">搜索</el-button>
        <el-input class="box_input" size="mini" @change="changeContent" @keyup.enter.native="filter()" placeholder="商品名称/商品编号/存货编号" v-model.trim="content"></el-input>
        <el-select
          class="content_select"
          placeholder="选择类型"
          size="mini"
          v-model="contentType"
          clearable
        >
          <el-option
            v-for="item in contentTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </div>
    </div>
    <!---未解冻商品列表--->
    <div>
      <el-table :data="tableData" @selection-change="handleSelectionChange" border header-row-class-name="header-row" ref="multipleSelect" class="tableCenter" v-loading="loading" :height="tableHeight">
        <!-- <el-table-column type="selection" :width="55"></el-table-column> -->
        <el-table-column  label="商品编号" align="center" :min-width="120" prop="goodsNo"> </el-table-column>
        <el-table-column label="商品名称" align="center" :min-width="150" prop="goodsName" show-overflow-tooltip> </el-table-column>
        <el-table-column label="商品分类" align="center" :min-width="120" prop="categoryName"> </el-table-column>
        <el-table-column label="品牌" prop="brandName" align="center" :min-width="120"> </el-table-column>
        <el-table-column label="操作" align="center" :width="210">
          <template slot-scope="scope">
            <el-button @click="handleEdit(scope.row)" class="mini-search-btn"> 查看 </el-button>
            <el-button @click="handleFreeze(scope.row)" class="mini-freeze-btn"> 解冻 </el-button>
            <el-button @click="handleDelete(scope.row)" class="mini-delete-btn"> 删除 </el-button>
          </template>
        </el-table-column>
      </el-table>
      <page :page="pageInfo.page" :total="total" @currentChange="currentChange" @sizeChange="sizeChange">
      </page>
    </div>
  </div>
</template>
<script>
import page from '@/components/pagination'
import { timeFormat } from "@/utils/timeFormat"
export default {
  name: 'goods_list',
  components: { page },
  data() {
    return {
      tableHeight: 600,
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        classifyId:-1,
        brandId:-1,
        categoryId:-1,
        freezeStatus: 2 // 冻结
      },
      page:1,
      total: 0,
      classifyId: '',
      categoryList: [],
      brandId: '',
      brandList: [],
      categoryId: '',
      productlineList: [],
      contentType: '',
      content: '',
      tableData: [],
      total: 0,
      multipleSelect: [],
      priceData: [],
      priceShow: false,
      warehouseData: [],
      warehouseShow: false,
      contentTypes: [
        { value: 1, label: '商品名称' },
        { value: 2, label: '商品编号' },
        { value: 3, label: '货品编号' },
        { value: 4, label: '条形码' }
      ],
      goodsTypes: [
        { value: 1, label: "普通" },
        { value: 2, label: "新定制" }
      ]
    }
  },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
  },
  activated() {
    this.dataFot()
  },
  methods: {
    dataFot () {
      // 获取所有分类级联
      this.$http.getClassifyPoList(this, {page:1, size:10000, openFlag: 1}).then(res => {
        this.categoryList = res.tableData
        this.loading = false;
      })
     // 获取所有品类
      this.$http.getCategoryPoList(this, {page:1, size:10000, openFlag: 1}).then(res => {
        if (res.success) {
          this.productlineList = res.data.list
        }
      })
      // 获取所有品牌列表
      this.$http.getBrandPoList(this, {page:1, size:10000, openFlag: 1}).then(res => {
        if (res.success) {
          this.brandList = res.data.list
        }
      })
      // 获取所有冻结商品列表
      this.getTableData()
    },
    changeContent(val) {
      if (val === undefined || val === "" || val === null) {
        this.filter();
      }
    },
    filter() { // 搜索
      if(this.page === this.pageInfo.page){
        this.pageInfo.page = 1
      }
      this.page = this.pageInfo.page
      this.getTableData()
    },

    closePriceShow() {
      this.priceShow = false;
    },

    closeWarehouseShow() {
      this.warehouseShow = false;
    },
    handleDelete(row) { // 删除操作
      this.$confirm('此操作将永久删除该商品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.deleteGoods(this, { id: row.id }).then(res => {  
          if (res.success) {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1000,
            })
            this.getTableData()
          }
        })
      })
    },
    handleEdit(row) { // 查看操作
      this.$router.push({name:'addgoods',query:{id:row.id, freezeStatus: row.freezeStatus}})
    },

    handleFreeze(row) { // 解冻操作
      this.$confirm('此操作将改变该商品的冻结状态, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.freezeStatus(this, { id: row.id, freezeStatus: 1 }).then(res => {  
          if(res.success){
            this.$message({
              message: '商品解冻成功',
              type: 'success',
              duration: 3 * 1000,
            })
            this.getTableData()
          }
        })
      })
    },
    // ======== 数据 ========
    getTableData() { // 主数据
      this.loading = true;
      // 分类
      if(this.pageInfo.classifyId === -1 || this.pageInfo.classifyId === ''){
          this.pageInfo.classifyId = undefined;
      }
      if(this.pageInfo.brandId === -1 || this.pageInfo.brandId === ''){
          this.pageInfo.brandId = undefined;
      }
      // 品类
      if(this.pageInfo.categoryId === -1 || this.pageInfo.categoryId === ''){
          this.pageInfo.categoryId = undefined;
      }
      this.pageInfo.contentType = this.contentType;
      this.pageInfo.content = this.content;
       this.$http.getGoodsListP(this, this.pageInfo).then(res => {    
        this.tableData = res.tableData.list;
        this.total = res.tableData.total;
        res.success ? this.loading = false : this.loading = false
      })
    },

    handleSelectionChange(val) { // 勾选中的值
      this.multipleSelect = val;
    },

    sizeChange(size) { // 总条数
      this.pageInfo.size = size;
    },

    currentChange(page) { // 分页
      this.pageInfo.page = page;
    },
    
    timeFormat(row, col, val) { // 时间格式化
      return timeFormat(val)
    },

    formatStatus(row, col, val) { // 上架状态
      switch(val) {
        case 3:
          return "已上架";
          break;
        case 1:
          return "已下架";
          break;
        case 2:
          return "上架审批中";
          break;
        case 4:
          return "下架审批中";
          break;
      }
    }
  },
  watch: {
    pageInfo: {
      handler() {
        this.filter()
      },
      deep: true
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell{
  overflow: hidden; 
  white-space:nowrap; 
}
.category-goods-list {
  background-color: white;
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
  .header {
    background-color: $lakeBlue;
    color: white;
    padding-left: 20px;
  }
  .nav {
    background-color: white;
    .choose {
      position: relative;
      top: 1px;
    }
    .search {
      float: right;
    }
    .box-search {
      width: 120px;
    }
    .Fheader {
      margin: 10px auto;
      margin-left: 0;
      overflow: hidden;
    }
    .content_select{
      float:right;
      width: 120px;
      /deep/.el-input{
        .el-input__inner{
          border-top-right-radius: 0;
          border-bottom-right-radius: 0;
        }
      }
    }
    .box_input{
      float: right;
      width: 200px;
      /deep/.el-input__inner{
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
        border-left: none;
      }
    }
}
  .header-row {
    @include table-header-color;
    min-width:100px;

  }
  .dialog-table {
    width: 100%;
  }
  .cell-center {
    text-align: center;
  }
}

</style>
