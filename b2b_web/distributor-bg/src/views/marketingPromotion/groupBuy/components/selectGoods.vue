<template>
  <div class="check-goods" v-loading="loading">
    <div class="nav-bar">
      <!------商品分类------->
      <el-select size="mini" style="width:140px" v-model="pageInfo.classifyId" clearable placeholder="选择分类">
        <el-option v-for="item in categoryList" :key="item.id" :value="item.id" :label="item.name">
          <div v-if="item.parentId === 0">{{item.name}}</div>
          <div v-else>&nbsp;&nbsp;&nbsp;├{{item.name}}</div>
        </el-option>
      </el-select>
      <!------品牌------->
      <el-select
          class="custom_select"
          placeholder="品牌"
          size="mini"
          style="width:140px"
          v-model="pageInfo.brandId"
          clearable
        >
          <el-option
            :key="item.id"
            :label="item.name"
            :value="item.id"
            v-for="item in brandList"
          ></el-option>
        </el-select>
       <!------商品状态------->
      <el-select
        size="mini"
        style="width:100px"
        v-model="pageInfo.saleStatus"
        clearable
        placeholder="选择状态"
      >
        <el-option
          v-for="item in saleStatusList"
          :key="item.saleStatus"
          :value="item.saleStatus"
          :label="item.saleStatusName"
        ></el-option>
      </el-select>
      <!------商品类型------->
      <el-select
        class="custom_select"
        placeholder="商品类型"
        size="mini"
        style="width:100px"
        v-model="pageInfo.goodsType"
        clearable
      >
        <el-option
          v-for="item in goodsTypes"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
      <el-button class="mini-search-btn" @click="filter" style="float: right;margin-left:5px;">搜索</el-button>
      <el-input
        size="mini"
        style="float: right;width:160px;"
        placeholder="商品名称/商品编号"
        v-model.trim="content"
      />
      <el-select
        class="content_select"
        placeholder="选择类型"
        size="mini"
        style="float: right;width:120px"
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

    <el-table
      ref="singleSelect"
      :data="tableData"
      tooltip-effect="dark"
      style="text-align:center;"
      border
      header-row-class-name="header-row"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="商品编号" prop="goodsNo"></el-table-column>
      <el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column>
      <el-table-column align="center" label="商品分类" prop="categoryName"></el-table-column>
      <el-table-column align="center" label="商品上架状态" prop="saleStatus" :formatter="formatStatus"></el-table-column>
      <el-table-column align="center" label="品牌" prop="brandName"></el-table-column>
    </el-table>
    <page
      :page="pageInfo.page"
      :total="total"
      @sizeChange="sizeChange"
      @currentChange="currentChange"
    ></page>
    <div class="foot-btn">
      <el-button class="mini-search-btn" style="margin-left:47%;" @click="handleSubmit">确定</el-button>
      <el-button size="mini" @click="handleCancel">返回</el-button>
    </div>
  </div>
</template>
<script>
import page from "@/components/pagination/index";
export default {
  props: {
    saleStatus: {
      type: Number,
    },
    // goodsType: {
    //   type: Number,
    // },
    isNew: true,
    selectGoodsData: Object,
  },
  data() {
    return {
      categoryId: "",
      brandId: "",
      contentType: 2,
      content: "",
      categoryList: [],
      brandlist: [],
      pageInfo: {
        page: 1,
        size: 10,
        freezeStatus: 1,
        saleStatus: 3,
        classifyId: -1,
        brandId: -1,
        contentType: "",
        content: ""
      },
      page: 1,
      tableData: [],
      total: 0,
      loading: false,
      singleSelect: [],
      goodsTypes: [
        { value: 1, label: "普通" },
        { value: 2, label: "新定制" }
      ],
      contentTypes: [
        { value: 1, label: '商品名称' },
        { value: 2, label: '商品编号' },
        { value: 3, label: '货品编号' },
        { value: 4, label: '条形码' }
      ],
      saleStatusList: [
        {
          saleStatus: 3,
          saleStatusName: "已上架",
        },
        {
          saleStatus: 1,
          saleStatusName: "已下架",
        },
        {
          saleStatus: 2,
          saleStatusName: "审批中",
        }
      ],
      isSelect: false,
    };
  },
  components: { page },
  created() {
    this.pageInfo.saleStatus = this.saleStatus;
    // if (this.goodsType !== undefined && this.goodsType !== "") {
    //   this.pageInfo.goodsType = this.goodsType;
    // }
    if (this.selectGoodsData) {
      this.singleSelect = this.selectGoodsData;
    }
     // 获取所有分类级联
    this.$http.getClassifyPoList(this, {page:1, size:10000, openFlag: 1}).then(res => {
      this.categoryList = res.tableData
    })
    // 获取所有品牌列表
    this.$http.getBrandPoList(this, {page:1, size:10000, openFlag: 1}).then(res => {
      if (res.success) {
        this.brandList = res.data.list
      }
    })
    this.getTableData();
  },
  methods: {
    getTableData() {
      // 获取所有未冻结商品列表
      this.$http.getGoodsListP(this, this.pageInfo).then(res => {      
          this.isSelect = false;
          this.total = res.tableData.total;
          //重新获取数据时，判断哪些选中了
          res.tableData.list.forEach((row2) => {
            if (this.singleSelect.goodsId === row2.id) {
              this.$refs.singleSelect.toggleRowSelection(row2, true);
            }
          });
          this.tableData = res.tableData.list;
          res.success ? (this.loading = false) : (this.loading = false);
        });
    },
    sizeChange(size) {
      this.pageInfo.size = size;
    },

    currentChange(page) {
      this.pageInfo.page = page;
    },

    handleSelectionChange(selection) {
      this.singleSelect = selection[0];
      if (selection.length > 1) {
        this.$refs.singleSelect.clearSelection();
        this.$refs.singleSelect.toggleRowSelection(selection.pop());
      }
    },

    formatStatus(row, col, val) {
      switch (val) {
        case '3':
          return "已上架";
          break;
        case '1':
          return "已下架";
          break;
        case '2':
          return "审批中";
          break
      }
    },

    filter() {
      if (this.pageInfo.page === this.page) {
        this.pageInfo.page = 1;
      }
      this.page = this.pageInfo.page;
      if (this.pageInfo.classifyId === -1 || this.pageInfo.classifyId === "") {
        this.pageInfo.classifyId = undefined;
      }
      if (this.pageInfo.brandId === -1 || this.pageInfo.brandId === "") {
        this.pageInfo.brandId = undefined;
      }
      if (this.pageInfo.saleStatus === -1 || this.pageInfo.saleStatus === "") {
        this.pageInfo.saleStatus = undefined;
      }
      this.pageInfo.contentType = this.contentType;
      this.pageInfo.content = this.content;
      this.loading = true;
      this.getTableData()
    },

    handleSubmit() {
      // 确定操作
      this.$emit("submit", this.singleSelect);
    },

    handleCancel() {
      // 返回操作
      this.$emit("cancel");
    },
  },
  watch: {
    pageInfo: {
      handler() {
        this.filter();
      },
      deep: true,
    },
  },
};
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
    margin-top: 10px;
  }
  /deep/ .el-table {
    .el-table__header-wrapper .el-checkbox {
      //找到表头那一行，然后把里面的复选框隐藏掉
      display: none;
    }
  }
}
</style>
