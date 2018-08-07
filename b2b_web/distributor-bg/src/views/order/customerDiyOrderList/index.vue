<template>
  <div class="order-list-wrap">
    <header>
      <h4>柔性定制订单列表</h4>
    </header>
    <div class="order-list">
      <el-tabs v-model="pageInfo.frontOrderStatus">
        <el-tab-pane label="全部" name="0"></el-tab-pane>
        <el-tab-pane label="待确认" name="1"></el-tab-pane>
        <el-tab-pane label="待发货" name="2"></el-tab-pane>
        <el-tab-pane label="出库中" name="9"></el-tab-pane>
        <el-tab-pane label="部分发货" name="3"></el-tab-pane>
        <el-tab-pane label="待收货" name="4"></el-tab-pane>
        <el-tab-pane label="已关闭" name="5"></el-tab-pane>
        <el-tab-pane label="已完成" name="6"></el-tab-pane>
      </el-tabs>
      <div class="order-list-fun">
        <div class="order-list-search">
          <div class="order-list-header">
            <div>
              <el-button
                class="mini-batch-btn"
                size="mini"
                @click.prevent="handleInputSyncLogistics()"
              >
                手动同步物流
              </el-button>
              <el-select
                size="mini"
                v-model="pageInfo.payStatus"
                placeholder="付款状态"
                style="width: 100px"
                clearable
              >
                <el-option label="未付款" value="1"></el-option>
                <el-option label="部分付款" value="2"></el-option>
                <el-option label="已付款" value="3"></el-option>
                <el-option label="部分退款" value="4"></el-option>
                <el-option label="退款申请中" value="5"></el-option>
                <el-option label="已退款" value="6"></el-option>
              </el-select>
              <el-date-picker
                size="mini"
                v-model="pageInfo.time"
                style="width: 330px"
                type="datetimerange"
                value-format="timestamp"
                range-separator="至"
                start-placeholder="下单开始日期"
                end-placeholder="下单结束日期"
              ></el-date-picker>
            </div>
            <div class="order-list-sear">
              <el-select
                size="mini"
                v-model="pageInfo.contentType"
                placeholder="类型"
                style="width: 160px"
                clearable
              >
                <el-option label="分销商用户名" value="1"></el-option>
                <el-option label="门店编码" value="3"></el-option>
                <el-option label="收货人" value="4"></el-option>
                <el-option label="订单号" value="5"></el-option>
                <el-option label="订单ID" value="6"></el-option>
                <el-option label="手机号" value="7"></el-option>
              </el-select>
              <el-input
                placeholder="请输入搜索关键字"
                @change="contentChange"
                @keyup.enter.native="onSearch()"
                clearable
                v-model.trim="pageInfo.content"
                size="mini"
                class="box-search"
              ></el-input>
              <button
                class="mini-search-btn btn-box"
                @click.prevent="onSearch()"
              >
                搜索
              </button>
            </div>
          </div>
        </div>
      </div>

      <el-table
        :data="tableData"
        border
        header-row-class-name="header-row"
        @selection-change="onSelectionChange"
        class="tableCenter"
        v-loading="loading"
        :height="tableHeight"
      >
        <el-table-column
          label="ID"
          align="center"
          prop="id"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="订单号"
          align="center"
          prop="orderNo"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="下单时间"
          align="center"
          :formatter="timeFormatter"
          :min-width="160"
        ></el-table-column>
        <el-table-column
          label="分销商用户"
          show-overflow-tooltip
          align="center"
          prop="distributorName"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="来源平台名称"
          show-overflow-tooltip
          align="center"
          prop="userSourceName"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="店铺编码"
          show-overflow-tooltip
          align="center"
          prop="shopCode"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="店铺名称"
          show-overflow-tooltip
          align="center"
          prop="shopName"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="收货人"
          align="center"
          prop="userName"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="收货人手机号"
          align="center"
          prop="mobile"
          :min-width="120"
        ></el-table-column>
        <el-table-column label="订单总额" align="center" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd" v-show="scope.row.currencyType === 'CNY'"
              >￥:&nbsp;</i
            >
            <i class="asmd" v-show="scope.row.currencyType === 'USD'"
              >$:&nbsp;</i
            >
            {{ scope.row.payAmount | NumFormat }}
          </template>
        </el-table-column>
        <el-table-column
          label="留言"
          align="center"
          prop="remark"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="支付方式"
          align="center"
          :formatter="orderPaymentFormatter"
          :min-width="100"
        ></el-table-column>
        <el-table-column
          label="订单状态"
          align="center"
          :formatter="orderStatusFormatter"
          :min-width="100"
        ></el-table-column>
        <el-table-column label="操作" :min-width="180" align="center">
          <template slot-scope="scope">
            <el-button
              class="mini-search-btn"
              @click="onEdit(scope.row, scope.$index)"
              >查看</el-button
            >
            <el-button
              v-if="scope.row.frontOrderStatus === 2"
              class="mini-tableadd-btn"
              @click="handleSyncLogisticsByOrderId(scope.row.id)"
              >同步物流</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="total"
        :page="pageInfo.page"
        @sizeChange="onSizeCHange"
        @currentChange="onCurrentChange"
      ></pagination>
    </div>

    <!-- 手动同步物流 -->
    <el-dialog
      :title="'同步物流信息'"
      :modal-append-to-body="false"
      :close-on-click-modal="false"
      :visible="syncDialog"
      width="500px"
      :before-close="closeSyncDialog"
    >
      <el-form
        :model="syncInfo"
        :rules="rules"
        label-width="120px"
        ref="syncInfo"
      >
        <el-form-item label="订单号" prop="orderNo">
          <el-input
            v-model="syncInfo.orderNo"
            size="mini"
            maxlength="20"
            clearable
          />
        </el-form-item>
        <el-form-item label="快递公司名称" prop="expressName">
          <el-input
            v-model="syncInfo.expressName"
            size="mini"
            maxlength="20"
            clearable
          />
        </el-form-item>
        <el-form-item label="快递公司编码" prop="expressCode">
          <el-input
            v-model="syncInfo.expressCode"
            size="mini"
            maxlength="20"
            clearable
          />
        </el-form-item>
        <el-form-item label="快递单号" prop="expressNo">
          <el-input
            v-model="syncInfo.expressNo"
            size="mini"
            maxlength="20"
            clearable
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="check_back_btn" size="mini" @click="closeSyncDialog()"
          >取消</el-button
        >
        <el-button
          class="mini-search-btn check_btn"
          @click="confirmSyncDialog()"
          >确定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import eventBus from "@/views/order/eventBus";
import pagination from "@/components/pagination/index";
import { parseTime } from "@/utils/index";
import {
  orderStatusFormatter,
  payStatusFormatter,
  orderPaymentFormatter,
  category,
} from "@/views/order/orderUtils"; // 格式化引入

export default {
  components: { pagination },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight * 0.75 - 100); // 计算表高度，固定表头
  },
  activated() {
    eventBus.$on("shouldUpdateData", (_) => {
      // 生成发货单后更新页面数据
      this.updateMainData();
    });
    this.updateMainData();
  },
  beforeDestroy() {
    // 实例销毁之前执行的钩子 (此处用来销毁eventbus的创建)
    eventBus.$off("shouldUpdateData");
  },
  data() {
    return {
      tableHeight: 600, // 给表格高度一个默认高度，以防没有计算到表格高度
      loading: false,
      chosenOrders: [], // 表格中被勾选的订单
      tableData: [],
      orderTypes: [], //订单类型
      orderSources: [],
      pageInfo: {
        frontOrderStatus: 0, // 前端订单状态
        searchType: 4, // 柔性定制订单
        page: 1,
        size: 10,
      },
      total: 1,
      recordImg: "", // 支付凭证图片
      deliveryShow: false,
      curOrderId: "",
      curProductOrderNo: "",
      // 手动输入同步物流
      syncInfo: {
        manualFlag: true,
        orderNo: "",
        expressName: "",
        expressCode: "",
        expressNo: "",
      },
      syncDialog: false,
      rules: {
        orderNo: [{ required: true, message: "请输入订单号", trigger: "blur" }],
        expressName: [
          { required: true, message: "请输入快递公司名称", trigger: "blur" },
        ],
        expressCode: [
          { required: true, message: "请输入快递公司编码", trigger: "blur" },
        ],
        expressNo: [
          { required: true, message: "请输入快递单号", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.onSearch();
      }
    },
    // ======== 操作 ========
    onSearch() {
      // 搜索操作
      this.pageInfo.page = 1;
      this.updateMainData();
    },

    onEdit(chosenOne, index) {
      // 查看操作
      this.$router.push({
        name: "orderDetail",
        query: {
          orderId: chosenOne.id,
          orderCustG: true,
          type: 3,
        },
      });
    },

    // ================== 数据 ==================
    updateMainData() {
      // ..柔性定制订单列表
      this.loading = true;
      this.$http.customerDiyOrderList(this, this.pageInfo).then((res) => {
        this.tableData = res.data.list;
        this.total = res.data.total;
        res.success ? (this.loading = false) : (this.loading = false);
      });
    },

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

    onSelectionChange(val) {
      // 表格勾选中的值
      this.chosenOrders = val;
    },

    // ======== 状态转换 ========
    payStatusFormatter(row) {
      // 付款状态
      return payStatusFormatter(row.payStatus);
    },

    orderStatusFormatter(row) {
      // 订单状态
      return orderStatusFormatter(row.frontOrderStatus);
    },

    orderPaymentFormatter(row) {
      // 支付方式
      return orderPaymentFormatter(row.payWay);
    },

    timeFormatter(row, column, cellValue) {
      // 时间格式化
      return parseTime(row.createTime);
    },

    category(row) {
      // 订单类别
      return category(row.category);
    },
    // 查看支付凭证
    showRecord(img) {
      this.showRecordModel = true;
      this.recordImg = img;
    },
    // 发货
    handleDelivery(row, index) {
      this.curOrderId = row.id; // 获取当前操作订单号
      this.curProductOrderNo = row.productOrderNo; // 获取当前操作工厂订单号
      this.deliveryShow = true;
    },
    // 手动同步物流
    handleInputSyncLogistics() {
      this.syncDialog = true;
    },
    closeSyncDialog() {
      this.syncDialog = false;
      this.expressNo = "";
      this.expressCode = "";
      this.expressName = "";
      this.orderNo = "";
      this.$nextTick(() => {
        this.$refs.syncInfo.clearValidate();
      });
    },
    confirmSyncDialog() {
      this.$refs["syncInfo"].validate((valid) => {
        if (valid) {
          this.$http.handleSyncLogistics(this, this.syncInfo).then((res) => {
            if (res.success) {
              this.$message({
                message: "同步成功",
                type: "success",
                duration: 3 * 1000,
              });
            }
            this.syncDialog = false;
            this.loading = false;
          });
        }
      });
    },
    // 同步物流 - 根据订单 ID
    handleSyncLogisticsByOrderId(id) {
      this.loading = true;
      this.$http.handleSyncLogisticsById(this, { id: id }).then((res) => {
        if (res.success) {
          this.$message({
            message: "同步成功",
            type: "success",
            duration: 3 * 1000,
          });
        }
        this.loading = false;
      });
    },
  },
  watch: {
    "pageInfo.frontOrderStatus": {
      // 前端订单状态
      handler() {
        this.pageInfo.page = 1;
        this.updateMainData();
      },
      deep: true,
    },
    "pageInfo.payStatus": {
      // 付款状态
      handler() {
        this.pageInfo.page = 1;
        this.updateMainData();
      },
      deep: true,
    },
    "pageInfo.time": {
      handler(val) {
        if (val) {
          this.pageInfo.startTime = parseTime(val[0]);
          this.pageInfo.endTime = parseTime(val[1]);
        } else {
          this.pageInfo.startTime = undefined;
          this.pageInfo.endTime = undefined;
        }
        this.pageInfo.page = 1;
        this.updateMainData();
      },
      deep: true,
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
  }
  .order-list {
    background-color: white;
    height: 100%;
    .order-list-fun {
      padding: 0px 10px 10px 10px;
      overflow: hidden;
      .order-list-search {
        overflow: hidden;
        .order-list-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          .el-select {
            margin-bottom: 10px;
          }
          .order-list-sear {
            .box-search {
              width: 220px;
            }
            .btn-box {
              position: relative;
              top: -1px;
            }
          }
        }
      }
    }
  }
}

// 支付凭证
.record-wrap {
  .record-img {
    text-align: center;
    img {
      max-width: 100%;
      max-height: 100%;
    }
  }
}
</style>
