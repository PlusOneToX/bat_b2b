<template>
  <div class="check-goods">
    <div class="nav-bar">
      <el-select
        style="width:120px"
        size="mini"
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
      <!-- <el-select
        v-if="conFlag === 'exchangeActivity'"
        style="width:120px"
        size="mini"
        v-model="pageInfo.goodsType"
        placeholder="商品类型"
        clearable
      >
        <el-option label="新定制商品" :value="3"></el-option>
      </el-select> -->
      <el-button
        class="mini-search-btn"
        size="mini"
        @click="filter()"
        style="float: right;margin-left:5px;"
      >搜索</el-button>
      <el-input
        size="mini"
        style="float: right;width:250px;"
        placeholder="货品名称/货品编号/货品条形码"
        v-model.trim="pageInfo.content"
      ></el-input>
    </div>

    <div>
      <el-table
        ref="multipleSelect"
        :data="tableData"
        tooltip-effect="dark"
        @select="select"
        @select-all="selectAll"
        border
        header-row-class-name="header-row"
        @selection-change="handleSelectionChange"
        :class="{'select-all': conFlag === 'exchangeActivity'}"
      >
        <el-table-column align="center" type="selection" :selectable="checkboxT" width="55"></el-table-column>
        <el-table-column align="center" label="商品编号" prop="goodsNo"></el-table-column>
        <el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column>
        <el-table-column align="center" label="货号" prop="itemCode"></el-table-column>
        <el-table-column align="center" label="货品名称" prop="itemName"></el-table-column>
        <el-table-column align="center" label="上架状态" prop="saleStatus" :formatter="formatStatus"></el-table-column>
      </el-table>
      <page
        :page="pageInfo.page"
        :total="total"
        @sizeChange="sizeChange"
        @currentChange="currentChange"
      ></page>
    </div>
    <div class="foot-btn">
      <el-button class="mini-search-btn" style="margin-left:47%;" @click="handleSubmit()">确定</el-button>
      <el-button size="mini" @click="handleCancel">返回</el-button>
    </div>
  </div>
</template>
<script>
import page from "@/components/pagination/index";
export default {
  props: ["selectItemsData", "goodsType", "conFlag", "relevanceMaterialFlag"],
  data() {
    return {
      categoryId: "",
      brandId: "",
      categorylist: [],
      brandlist: [],
      pageInfo: {
        page: 1,
        size: 10,
        saleStatus: 3,
        content: "",
        goodsType: this.goodsType,
        relevanceMaterialFlag: this.relevanceMaterialFlag // 是否关联材质 1-是 2-否
      },
      tableData: [],
      total: 0,
      page: 1,
      loading: false,
      multipleSelect: [],
      selected: [],
      isSelect: false,
      saleStatusList: [
        { saleStatus: 3, saleStatusName: "已上架" },
        { saleStatus: 1, saleStatusName: "已下架" },
        { saleStatus: 2, saleStatusName: "审批中" }
      ]
    };
  },
  components: { page },
  created() {
    this.multipleSelect = [];
    if (this.selectItemsData !== undefined && this.selectItemsData.length > 0) {
      this.multipleSelect = this.multipleSelect.concat(this.selectItemsData); // 第一次进入时
    }
    this.dataForamt();
    // if (this.conFlag === "exchangeActivity") {
    //   this.pageInfo.goodsType = 3;
    // }
  },
  methods: {
    selectRow() {
      // 商品请求数据变化时，重新选择行（如，删除、数据变化）
      this.$refs.multipleSelect.clearSelection();
      this.multipleSelect.forEach((row1) => {
        this.tableData.forEach((row2) => {
          if (row1.itemCode === row2.itemCode) {
            this.$refs.multipleSelect.toggleRowSelection(row2);
            if(row1.id != null){
              this.$set(row2, "status", 1);
            }
          }
        });
      });
    },
    dataForamt() {
       this.$http.getGoodsItemPoList(this, this.pageInfo)
        .then((res) => {
          if (res.success) {
            this.isSelect = false;
            this.total = res.data.total;
            this.multipleSelect.forEach((row1) => {
              // 重新获取数据时，判断哪些选中了
              res.data.list.forEach((row2) => {
                if (row1.itemCode === row2.itemCode) {
                  this.$refs.multipleSelect.toggleRowSelection(row2);
                  this.selected.push(row2);
                  if(row1.id != null){
                    this.$set(row2, "status", 1);
                  }
                }
              });
            });
            this.tableData = res.data.list;
          }
        });
    },
    select(selection, row) {
      // 单选时调用
      this.isSelect = true;
      let d = false;
      if (this.multipleSelect.length !== 0) {
        for (let i = 0; i < this.multipleSelect.length; i++) {
          if (this.multipleSelect[i].itemCode === row.itemCode) {
            this.multipleSelect.splice(i, 1);
            d = true;
            break;
          }
        }
      }
      if (this.multipleSelect.length === 0) {
        this.tableData.forEach((item) => {
          if (item.goodsId !== row.goodsId) {
            this.$set(item, "status", "");
          }
        });
      }
      if (!d) {
        this.multipleSelect.push(row);
        this.multipleSelect = this.setArr(this.multipleSelect);
      }
    },
    selectAll(selection) {
      // 全选时调用
      this.isSelect = true;
      if (selection.length === 0) {
        this.tableData.forEach((row) => {
          for (let i = 0; i < this.multipleSelect.length; i++) {
            if (this.multipleSelect[i].itemCode === row.itemCode) {
              this.multipleSelect.splice(i, 1);
              break;
            }
          }
        });
      } else {
        this.multipleSelect = this.setArr(
          this.multipleSelect.concat(selection)
        );
      }
    },
    setArr(arr) {
      // 去重
      const obj = {};
      const temp = [];
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].itemCode); // 不加类型 分不清 1 '1'
        if (!obj[arr[i].itemCode + type]) {
          temp.push(arr[i]);
          obj[arr[i].itemCode + type] = true; // 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp;
    },

    sizeChange(size) {
      // 分页页数
      this.pageInfo.size = size;
      this.pageInfo.page = 1;
      this.dataForamt();
    },

    currentChange(page) {
      // 分页总数
      this.pageInfo.page = page;
      this.dataForamt();
    },

    checkboxT(row, index) {
      // if (this.conFlag === "exchangeActivity") {
      //   return true;
      // } else {
        if (row.status === 1) {
          return false;
        } else {
          return true;
        }
      // }
    },

    handleSelectionChange(val) {
      // 当切换页面时的作用
      if (
        val.length === 0 &&
        this.multipleSelect.length !== 0 &&
        !this.isSelect
      ) {
        this.multipleSelect.forEach((row1) => {
          this.tableData.forEach((row2) => {
            if (row1.itemCode === row2.itemCode) {
              this.$refs.multipleSelect.toggleRowSelection(row2);
            }
          });
        });
        this.isSelect = true;
      }
    },

    formatStatus(row, col, val) {
      // 上架状态值
      switch (val) {
        case 1:
          return "已下架";
          // eslint-disable-next-line no-unreachable
          break;
        case 2:
          return "审批中";
          // eslint-disable-next-line no-unreachable
          break;
        case 3:
          return "已上架";
          // eslint-disable-next-line no-unreachable
          break;
      }
    },

    filter() {
      // 搜索操作
      this.dataForamt();
    },

    handleSubmit() {
      console.log(this.multipleSelect)
      this.multipleSelect.forEach((item) => {
        if (item.id) {
          this.$set(item, "itemId", item.id);
        }
      })
      // 确定操作，给父页面监听
      this.$emit("submit", this.multipleSelect);
    },

    handleCancel() {
      // 返回操作，给父页面监听
      this.$emit("cancel");
    },
  },
  watch: {
    selectItemsData: {
      // 请求的分销商数据变化时
      handler() {
        this.multipleSelect = []
        this.multipleSelect = this.multipleSelect.concat(this.selectItemsData)
        this.multipleSelect = this.setArr(this.multipleSelect);
        this.selectRow();
      },
      deep: true,
    },
    "pageInfo.saleStatus": {
      handler() {
        this.pageInfo.page = 1;
        this.dataForamt();
      },
      deep: true,
    },
    "pageInfo.goodsType": {
      handler() {
        this.pageInfo.page = 1;
        this.dataForamt();
      },
      deep: true,
    }
  },
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell {
  overflow: hidden;
  white-space: nowrap;
}
.check-goods {
  background-color: white;
  .nav-bar {
    padding: 10px 0;
  }
  .commodityTable {
    text-align: center;
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
    &.select-all {
      .el-table__header-wrapper .el-checkbox {
        display: inline-block;
      }
    }
  }
}
</style>
