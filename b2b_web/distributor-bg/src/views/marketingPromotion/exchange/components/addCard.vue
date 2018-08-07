<!--
 * @Author: yaowei
 * @Date: 2018-05-21 16:43:35
 * @LastEditors: litian
 * @LastEditTime: 2018-07-01 14:00:33
-->
<template>
  <div>
    <el-form-item label="兑换卡片关联:" prop="exchangeCard">
      <el-button
        class="mini-search-btn"
        @click="handleInitMaterialData"
        v-if="!isDisabled"
        >添加卡片</el-button
      >

      <el-table
        v-if="cardItems.length > 0"
        :data="cardItems"
        ref="multipleGoodsTable"
        border
        header-row-class-name="header-row"
        class="tableCenter goods-table"
        style="width: 100%"
      >
        <el-table-column
          align="center"
          label="商品编码"
          prop="goodsNo"
        ></el-table-column>
        <el-table-column
          align="center"
          label="商品名称"
          prop="goodsName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="货品编码"
          prop="itemCode"
        ></el-table-column>
        <el-table-column
          align="center"
          label="货品名称"
          prop="itemName"
        ></el-table-column>
        <el-table-column
          width="80"
          align="center"
          label="操作"
          v-if="!isDisabled"
        >
          <template slot-scope="scope">
            <el-button
              class="mini-delete-btn"
              @click="handleDeleteGood(scope.$index)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-form-item>

    <!-- 关联卡片 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="showDialog"
      width="80%"
      :before-close="closeItemsDialog"
    >
      <div class="check-goods">
        <div class="nav-bar">
          <el-input
            size="mini"
            style="width: 250px"
            placeholder="商品编码/商品名称/货品编码/货品名称"
            v-model.trim="pageInfo.content"
          ></el-input>
          <el-button
            class="mini-search-btn"
            size="mini"
            @click="filter()"
            style="margin-left: 5px"
            >搜索</el-button
          >
        </div>

        <div>
          <el-table
            ref="singleSelect"
            :data="tableData"
            v-loading="loading"
            tooltip-effect="dark"
            border
            header-row-class-name="header-row"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              align="center"
              type="selection"
              width="55"
            ></el-table-column>
            <el-table-column
              align="center"
              label="商品编码"
              prop="goodsNo"
            ></el-table-column>
            <el-table-column
              align="center"
              label="商品名称"
              prop="goodsName"
            ></el-table-column>
            <el-table-column
              align="center"
              label="货品编码"
              prop="itemCode"
            ></el-table-column>
            <el-table-column
              align="center"
              label="货品名称"
              prop="itemName"
            ></el-table-column>
          </el-table>
          <page
            :page="pageInfo.page"
            :total="total"
            @sizeChange="sizeChange"
            @currentChange="currentChange"
          ></page>
        </div>
        <div class="foot-btn">
          <el-button
            class="mini-search-btn"
            style="margin-left: 47%"
            @click="handleSubmit()"
            >确定</el-button
          >
          <el-button size="mini" @click="handleCancel">返回</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import page from "@/components/pagination/index";
export default {
  name: "addCard",
  props: [
    "exchangeCard",
    "isDisabled",
    "initCardItems",
    "editStatus",
    "distributorId",
    "distriStatus"
  ],
  components: { page },
  data() {
    return {
      loading: false,
      showDialog: false,
      pageInfo: {
        distributorId: "",
        page: 1,
        size: 10,
        content: ""
      },
      tableData: [],
      total: 0,
      singleSelect: [],
      isSelect: false,
      cardItems: [],
      curCode: "",
    };
  },
  methods: {
    // 搜索添加兑换卡片列表
    filter() {
      this.dataForamt();
    },
    // 添加兑换卡片
    handleInitMaterialData() {
      this.showDialog = true;
      this.dataForamt();
    },
    // 获取兑换卡片列表
    dataForamt() {
      this.loading = true
      if (this.distriStatus === 1) {
        // 全部分销商
        this.$http.exchangeItemList(this, {
          page:this.pageInfo.page,
          size:this.pageInfo.size,
          content:this.pageInfo.content
        }).then(res => {
          if (res.success) {
            this.isSelect = false;
            this.tableData = res.data.list;
            this.total = res.data.total;
            // 重新获取数据时，判断哪些选中了
            if (this.singleSelect) {
              this.singleSelect.forEach((row1) => {
                this.tableData.forEach((row2) => {
                  if (row1.itemId === row2.itemId) {
                    this.$nextTick(() => {
                      this.$refs.singleSelect.toggleRowSelection(row2, true);
                    });
                  }
                });
              });
            }
          }
          res.success ? this.loading = false : this.loading = false
        })
      } else {
        // 指定分销商
        this.pageInfo.distributorId = this.distributorId
        ? Number(this.distributorId)
        : "";
        this.$http.goodsByDistributorId(this, this.pageInfo)  
          .then((res) => {
            if (res.success) {
              this.isSelect = false;
              this.tableData = res.data.list;
              this.total = res.data.total;
              // 重新获取数据时，判断哪些选中了
              if (this.singleSelect) {
                this.singleSelect.forEach((row1) => {
                  this.tableData.forEach((row2) => {
                    if (row1.itemId === row2.itemId) {
                      this.$nextTick(() => {
                        this.$refs.singleSelect.toggleRowSelection(row2, true);
                      });
                    }
                  });
                });
              }
            }
            res.success ? this.loading = false : this.loading = false
          });
      }
    },
    // 删除兑换卡片
    handleDeleteGood(index) {
      this.cardItems.splice(index, 1);
      this.singleSelect = this.cardItems;
      this.$emit("getCardData", this.cardItems);
    },
    closeItemsDialog() {
      this.showDialog = false;
    },
    sizeChange(size) {
      // 分页页数
      this.pageInfo.count = size;
      this.pageInfo.page = 1;
      this.dataForamt();
    },
    currentChange(page) {
      // 分页总数
      this.pageInfo.page = page;
      this.dataForamt();
    },
    handleSelectionChange(val) {
      // 当切换页面时的作用
      if (val.length > 1) {
        this.$nextTick(() => {
          this.$refs.singleSelect.clearSelection();
          this.$refs.singleSelect.toggleRowSelection(val[val.length - 1]);
        });
      }
      this.singleSelect = val[val.length - 1];
    },
    handleSubmit() {
      // 确定
      this.cardItems = [];
      if (this.singleSelect) {
        this.cardItems = this.cardItems.concat(this.singleSelect);
      }
      this.showDialog = false;
      this.singleSelect = this.cardItems;
      this.$emit("getCardData", this.cardItems);
    },
    handleCancel() {
      // 取消
      this.singleSelect = this.cardItems;
      this.showDialog = false;
      this.$emit("getCardData", this.singleSelect);
    },
  },
  watch: {
    editStatus: {
      handler(val) {
        if (val) {
          // 查看详情
          this.cardItems = this.initCardItems;
          this.singleSelect = this.initCardItems;
        }
      },
      deep: true
    }
  }
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
    text-align: right;
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
  }
}
</style>
