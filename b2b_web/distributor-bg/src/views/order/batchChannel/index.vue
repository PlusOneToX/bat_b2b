<template>
  <div class="order-list-wrap">
    <header>
      <h4>批量导入订单列表</h4>
      <el-button class="mini-add-btn btn-home" @click="addpuls"
        >导入订单</el-button
      >
    </header>
    <div class="order-list">
      <div>
        <el-tabs v-model="importOrderStatus">
          <el-tab-pane label="未下单" name="2"> </el-tab-pane>
          <el-tab-pane label="已下单" name="1"> </el-tab-pane>
        </el-tabs>
      </div>
      <div
        class="order-list-fun"
        v-show="checkImportOrderStatus == 2 && tableData.length > 0"
      >
        <el-button
          class="mini-search-btn btn-box"
          :loading="submitLoading"
          @click.prevent="handleSubmits()"
          >批量提交</el-button
        >
        <el-button
          class="mini-delete-btn btn-box"
          :loading="submitLoading"
          @click.prevent="handleDeletes()"
          >批量删除</el-button
        >
      </div>
      <el-table
        ref="mytable"
        :data="tableData"
        @selection-change="handleSelectionChange"
        border
        max-height="700"
        style="width: 100%"
        header-row-class-name="header-row"
        class="tableCenter"
        v-loading="loading"
      >
        <el-table-column
          align="center"
          type="selection"
          width="55"
          key="1"
          v-if="checkImportOrderStatus == 2"
          :selectable="checkSelectable"
        ></el-table-column>
        <el-table-column align="center" type="index" key="2" width="50">
        </el-table-column>
        <el-table-column
          align="center"
          label="分销商用户名"
          key="3"
          prop="distributorName"
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          align="center"
          label="收货人"
          key="4"
          prop="userName"
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          align="center"
          label="总数量"
          prop="countSum"
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          align="center"
          key="5"
          v-if="checkImportOrderStatus == 2"
          label="商品总额"
          :render-header="renderHeader"
          :min-width="130"
        >
          <template slot-scope="scope">
            <i class="asmd">{{
              scope.row.currencyType === "CNY" ? "￥:" : "$:"
            }}</i>
            {{ scope.row.amountSum | NumFormat }}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          key="6"
          v-if="checkImportOrderStatus == 1"
          label="订单总额"
          :min-width="120"
        >
          <template slot-scope="scope">
            <i class="asmd">{{
              scope.row.currencyType === "CNY" ? "￥:" : "$:"
            }}</i>
            {{ scope.row.amountSum | NumFormat }}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          key="7"
          label="提交状态"
          prop="submitStatus"
          :formatter="submitStatusFormatter"
          v-if="checkImportOrderStatus == 2"
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          align="center"
          key="8"
          label="提示"
          prop="remark"
          v-if="checkImportOrderStatus == 2"
          :min-width="120"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          align="center"
          key="9"
          label="订单号"
          prop="orderId"
          v-if="checkImportOrderStatus == 1"
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          align="center"
          key="10"
          label="ERP订单号"
          prop="orderNo"
          v-if="checkImportOrderStatus == 1"
          :min-width="120"
        >
          <template slot-scope="scope">
            <span
              >{{ scope.row.orderNo == "" ? "-" : scope.row.orderNo }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          key="11"
          label="下单时间"
          prop="orderCreateTime"
          :formatter="timeFormatter"
          v-if="checkImportOrderStatus == 1"
          :min-width="120"
        >
        </el-table-column>
        <el-table-column align="center" key="12" label="操作" :min-width="120">
          <template slot-scope="scope">
            <el-button
              class="mini-search-btn"
              @click="onEdit(scope.row, scope.$index)"
            >
              查看
            </el-button>
            <el-button
              v-if="checkImportOrderStatus == 2"
              class="mini-delete-btn"
              @click="onDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        :total="total"
        @sizeChange="onSizeCHange"
        @currentChange="onCurrentChange"
      ></pagination>
    </div>
  </div>
</template>

<script>
import pagination from "@/components/pagination/index";
import { parseTime } from "@/utils/index";

export default {
  name: "batchChannel",
  components: { pagination },
  activated() {
    this.updateMainData();
  },
  computed: {
    checkHandleFlag() {
      for (var i = 0; i < this.tableData.length; i++) {
        return this.tableData[i].handleFlag;
      }
    },
    checkImportOrderStatus() {
      // 1 已下单 2 未下单
      return this.importOrderStatus;
    },
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      FirstReq: true,
      importOrderStatus: "2", //订单状态
      search: {
        content: "", // 搜索用关键词
      },
      pageInfo: {
        page: 1,
        size: 10,
      },
      total: 1,
      tableData: [],
      already: false,
      noAlready: true,
      multipleSelection: [],
    };
  },
  methods: {
    submitStatusFormatter(row, col, val) {
      switch (val) {
        case 1:
          return "未提交";
          break;
        case 2:
          return "提交中";
          break;
        case 3:
          return "提交成功";
          break;
        case 4:
          return "提交失败";
          break;
      }
    },
    renderHeader(h, col) {
      switch (col.$index) {
        case 5:
          return h("div", [
            h("span", "商品总额"),
            h(
              "el-tooltip",
              {
                props: {
                  content: "注意：商品总额不包含配送费",
                  effect: "light",
                  placement: "top",
                },
              },
              [h("span", { class: "el-icon-question question-color" })]
            ),
          ]);
          break;
      }
    },
    // 筛选可选范围
    checkSelectable(row) {
      return row.submitStatus !== 2;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleDeletes() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning("请先选择需要删除的订单！");
        return;
      }
      let ids = [];
      this.multipleSelection.forEach((item) => {
        ids.push(item.id);
      });
      this.$confirm("此操作将永久删除已选择的导入订单，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.$http
            .deleteImportOrder(this, { ids: ids.join(",") })
            .then((res) => {
              if (res.success) {
                this.$message.success({
                  message: "批量删除成功",
                  duration: 1 * 1000,
                });
                this.updateMainData();
              }
            });
        })
        .catch((_) => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    handleSubmits() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning("请先选择需要提交的订单！");
        return;
      }
      let ids = [];
      this.multipleSelection.forEach((item) => {
        ids.push(item.id);
      });
      this.$confirm("此操作将批量提交已选择的导入订单，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.submitLoading = true;
          this.$http
            .ordersImportOrder(this, { ids: ids.join(",") })
            .then((res) => {
              this.multipleSelection.forEach((item) => {
                item.submitStatus = 2;
              });
              if (res.success) {
                this.$message.success({
                  message: "批量提交成功，请稍后刷新列表，查看提交结果！",
                  duration: 1 * 1000,
                });
              }
              this.$refs.mytable.clearSelection();
              this.submitLoading = false;
            });
        })
        .catch((_) => {
          this.$message({
            type: "info",
            message: "已取消提交",
          });
        });
    },
    // ================== 操作部分 ==================
    addpuls() {
      // 导入操作
      this.$router.push({ name: "batchChannelLead" });
    },
    onEdit(row, index) {
      // 查看操作
      this.$router.push({
        name: "batchChannelRedact",
        query: { id: row.id, handleFlag: this.importOrderStatus },
      });
    },
    onDelete(row) {
      this.$confirm("此操作将永久删除该导入订单，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.submitLoading = true;
          this.$http.deleteImportOrder(this, { ids: row.id }).then((res) => {
            this.submitLoading = false;
            if (res.success) {
              this.$message.success({
                message: "删除成功",
                duration: 1 * 1000,
              });
              this.updateMainData();
            }
          });
        })
        .catch((_) => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    cancelOrder() {
      // 取消操作
      this.cancelOrder = this.operateOrderBy(cancelOrder); // 本行只在本函数第一次触发时运行
      return this.cancelOrder();
    },
    confirmOrder() {
      // 确认操作
      this.confirmOrder = this.operateOrderBy(confirmOrder);
      return this.confirmOrder();
    },
    // ================== End ==================

    // ================== 状态转换 ==================
    orderStatusFormatter(row) {
      // 订单状态
      return orderStatusFormatter(row.importOrderStatus);
    },
    orderPaymentFormatter(row) {
      // 支付方式
      return orderPaymentFormatter(row.payWay);
    },
    timeFormatter(row, column, cellValue) {
      // 时间格式化
      return parseTime(row.orderCreateTime);
    },
    // ================== End ==================

    // ================== 数据部分 ==================
    updateMainData() {
      this.loading = true;
      this.pageInfo.importOrderStatus = parseInt(this.importOrderStatus);
      this.$http.getImportOrder(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.loading = false;
        } else {
          this.looking = false;
        }
        if (this.importOrderStatus == 1) {
          // 已下单
          this.already = true;
          this.noAlready = false;
        } else if (this.importOrderStatus == 2) {
          // 未下单
          this.already = false;
          this.noAlready = true;
        }
      });
    },
    // ================== End ==================

    onSizeCHange(val) {
      // 分页方法
      this.pageInfo.size = val;
      this.updateMainData();
    },
    onCurrentChange(val) {
      // 分页方法
      this.pageInfo.page = val;
      this.updateMainData();
    },
  },
  watch: {
    importOrderStatus(val) {
      this.updateMainData();
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.order-list-wrap {
  height: 100%;
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
    .btn-home {
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
  .order-list {
    background-color: white;
    height: 100%;
    .order-list-fun {
      padding: 0px 16px 14px 16px;
      overflow: hidden;
    }
  }
}
</style>
