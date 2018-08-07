<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div
          class="
            user-right
            rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double
          "
        >
          <div class="content">
            <h6 class="user-right-title">{{ $t("UserCenter.BatchImport") }}</h6>
            <div class="top-box rl-padding-top-default">
              <el-tabs v-model="importOrderStatus">
                <el-tab-pane
                  :label="$t('versionsps.TobeOrder')"
                  name="2"
                ></el-tab-pane>
                <el-tab-pane
                  :label="$t('versionsps.Ordered')"
                  name="1"
                ></el-tab-pane>
              </el-tabs>
            </div>
            <div class="query">
              <!-- <el-select class="custom_select" :placeholder="$t('UserCenter.Type')" size="mini" v-model="pageInfo.brandId" clearable>
                  <el-option :key="item.id" :label="($i18n.locale === 'zh' || !item.titleEn == true ? item.title:item.titleEn)" :value="item.id" v-for="item in orderTypeList"> </el-option>
              </el-select>-->
              <!-- <el-select v-model="pageInfo.orderTypeValue"  class="custom_select" :placeholder="$t('UserCenter.Type')" size="mini" clearable>
                  <el-option value="XSDD01_SYS" label="标准销售订单"> </el-option>
                  <el-option value="XSDD04_SYS" label="直运销售订单"> </el-option>
                  <el-option value="XSDD10_SYS" label="mto订单类型"> </el-option>
                  <el-option value="DIY_SYS" label="定制订单"> </el-option>
              </el-select>-->
              <el-select
                class="custom_select"
                :placeholder="$t('UserCenter.Currency')"
                size="mini"
                v-model="pageInfo.currencyType"
                clearable
              >
                <el-option
                  v-for="item in currencyTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                ></el-option>
              </el-select>
              <el-input
                style="width: 240px"
                size="mini"
                :placeholder="$t('versionsps.CodeNameTel')"
                type="text"
                clearable
                @change="changeContent"
                v-model.trim="pageInfo.content"
              ></el-input>
              <el-button
                class="mini-search-btn"
                type="info"
                @click="search"
                size="mini"
                >{{ $t("P.Search") }}</el-button
              >

              <div class="handle-wrap rl-tr">
                <span
                  v-show="checkImportOrderStatus == 2"
                  class="mini-del cursor-pointer"
                  @click.prevent="handleDeletes()"
                >
                  <i class="iconfont icon-delete"></i>
                  {{ $t("ShopCarts.Delete") }}
                </span>
                <span
                  v-show="checkImportOrderStatus == 2"
                  class="mini-submit cursor-pointer"
                  @click.prevent="handleSubmits()"
                >
                  <i class="iconfont icon-check-square"></i>
                  {{ $t("ShopCarts.SubmitOrder") }}
                </span>
                <router-link class="mini-export" to="/ImportOrder">
                  <i class="iconfont icon-import"></i>
                  {{ $t("UserCenter.ImportOrder") }}
                </router-link>
              </div>
            </div>
            <div class="table">
              <el-table
                :data="tableData"
                @selection-change="handleSelectionChange"
                v-loading="loading"
              >
                <el-table-column
                  align="center"
                  type="selection"
                  key="1"
                  v-if="checkImportOrderStatus == 2"
                  :selectable="checkSelectable"
                ></el-table-column>
                <el-table-column
                  align="center"
                  :label="$t('versionsps.No')"
                  prop="id"
                  width="56"
                  v-if="checkImportOrderStatus == 2"
                ></el-table-column>
                <el-table-column
                  align="center"
                  :label="$t('versionsps.OrderNo')"
                  prop="orderId"
                  :min-width="70"
                  v-if="checkImportOrderStatus == 1"
                ></el-table-column>
                <!-- <el-table-column align="center" label="订单类型" key="3" prop="orderTypeName" :min-width="80" > </el-table-column> -->
                <!-- <el-table-column align="center" label="订单状态" key="4" prop="handleFlag" :formatter="orderStatusFormatter" :min-width="80" > </el-table-column> -->
                <el-table-column
                  align="center"
                  :label="$t('versionsps.ConsigneeName')"
                  prop="userName"
                  :min-width="80"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  align="center"
                  :label="$t('UserCenter.Tel')"
                  prop="mobile"
                  :min-width="100"
                ></el-table-column>
                <el-table-column
                  align="center"
                  :label="$t('Register.Address')"
                  prop="address"
                  :max-width="90"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  align="center"
                  :label="$t('UserCenter.Currency')"
                  prop="currencyType"
                  :min-width="76"
                ></el-table-column>
                <el-table-column
                  align="center"
                  :label="$t('ShopCarts.Quantity')"
                  prop="countSum"
                  :min-width="76"
                ></el-table-column>
                <el-table-column
                  align="center"
                  :label="$t('P.Prompt')"
                  prop="remind"
                  :max-width="100"
                  v-if="checkImportOrderStatus == 2"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  align="center"
                  :label="$t('versionsps.SubmitStatus')"
                  prop="submitStatus"
                  :formatter="submitStatusFormatter"
                  :min-width="80"
                  v-if="checkImportOrderStatus == 2"
                ></el-table-column>
                <el-table-column
                  align="center"
                  key="5"
                  :label="$t('UserCenter.TotalAmount')"
                  :min-width="90"
                >
                  <template slot-scope="scope">
                    <i class="asmd">{{
                      scope.row.currencyType === "CNY" ? "￥" : "$"
                    }}</i>
                    {{ scope.row.amountSum | keepTwoNum }}
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  key="11"
                  :label="$t('UserCenter.OrderTime')"
                  prop="orderCreateTime"
                  :formatter="formatDate"
                  show-overflow-tooltip
                  v-if="checkImportOrderStatus == 1"
                  :min-width="80"
                ></el-table-column>
                <el-table-column
                  align="center"
                  key="12"
                  :label="$t('ShopCarts.Operation')"
                  :min-width="100"
                >
                  <template slot-scope="scope">
                    <span
                      class="log rl-tc cursor-pointer"
                      @click="onEdit(scope.row, scope.$index)"
                      >{{ $t("P.Detail") }}</span
                    >
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
          <div class="rl-tr rl-margin-top-default" v-if="totalCount > 0">
            <el-pagination
              background
              :current-page="pageInfo.page"
              :page-sizes="[10, 20, 30]"
              :page-size="pageInfo.count"
              @size-change="onSizeChange"
              @current-change="onCurrentChange"
              layout=" prev, pager, next , total , sizes , jumper"
              :total="totalCount"
              class="page"
            ></el-pagination>
          </div>
        </div>
      </div>
      <cutFilmMachineDialog
        :showOpen="showOpenCut"
        :select="true"
        @close="closeUserProtocol(2)"
      ></cutFilmMachineDialog>
      <proCustomizeDialog
        :showOpen="showOpenPro"
        :select="true"
        @close="closeUserProtocol(3)"
      ></proCustomizeDialog>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import { formatDate, orderHint } from "@/assets/js/common.js";
import GD from "@/assets/js/globalData";
import cutFilmMachineDialog from "@/components/cutFilmMachineDialog";
import proCustomizeDialog from "@/components/proCustomizeDialog";

import {
  getImportOrder,
  deleteImportOrder,
  ordersImportOrder,
} from "@/apiService/api";

export default {
  components: {
    Header,
    Left,
    cutFilmMachineDialog,
    proCustomizeDialog,
  },
  data() {
    return {
      importOrderStatus: "2", // 订单状态
      userState: 2,
      canExportPrice: "2",
      loading: false,
      categoryList: [],
      multipleSelection: [],
      currencyTypes: [
        { value: 1, label: "USD" },
        { value: 2, label: "CNY" },
      ],
      pageInfo: {
        page: 1,
        size: 10,
        orderTypeValue: "", // 订单类型
        currencyType: "", // 币种
        content: "",
      },
      exportPriceItemCount: 0,
      tableData: [],
      totalCount: 0,
      ids: [],
      showOpenCut: false, // 膜切机弹框
      showOpenPro: false, // 定制弹框
      brandIds: [], // 当前提交商品品牌，判断协议弹框
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
    };
  },
  filters: {
    keepTwoNum(value) {
      value = Number(value);
      return value.toFixed(2);
    },
  },
  computed: {
    checkImportOrderStatus() {
      // 1 已下单 2 未下单
      return this.importOrderStatus;
    },
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
            h("span", "总金额"),
            h(
              "el-tooltip",
              {
                props: {
                  content: "注意：总金额不包含配送费",
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
    formatDate(row, col, val) {
      var date = new Date(val);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
    onEdit(row, index) {
      // 查看操作
      this.$router.push({
        name: "batchChannelDetail",
        query: { id: row.id, handleFlag: parseInt(this.importOrderStatus) },
      });
    },
    // 筛选可选范围
    checkSelectable(row) {
      return row.submitStatus !== 2;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    search() {
      this.searchData();
    },
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    searchData() {
      this.loading = true;
      this.pageInfo.importOrderStatus = parseInt(this.importOrderStatus);
      this.getData();
    },
    getData() {
      this.loading = true;
      this.pageInfo.importOrderStatus = parseInt(this.importOrderStatus);
      getImportOrder(this.pageInfo).then((res) => {
        this.loading = false;
        if (res.success) {
          this.tableData = res.data.list;
          this.totalCount = res.data.total;
        }
      });
    },
    changeContent(val) {
      if (val === undefined || val === "" || val === null) {
        this.searchData();
      }
    },
    onSizeChange(val) {
      // 分页方法
      this.pageInfo.count = val;
      this.getData();
    },
    onCurrentChange(val) {
      // 分页方法
      this.pageInfo.page = val;
      this.getData();
    },
    handleDeletes() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning(this.$t("versionsps.Del"));
        return;
      }
      let ids = [];
      this.multipleSelection.forEach((item) => {
        ids.push(item.id);
      });
      this.$confirm(this.$t("versionsps.RemindDelMes"), this.$t("P.Prompt"), {
        confirmButtonText: this.$t("Message.Confirm"),
        cancelButtonText: this.$t("Message.Cancel"),
        type: "warning",
        center: true,
      })
        .then((_) => {
          deleteImportOrder({
            ids: ids.join(",").toString(),
          }).then((res) => {
            if (res.success) {
              this.$message.success({
                message: this.$t("versionsps.batchDelMes"),
                duration: 1 * 1000,
              });
              this.pageInfo.page = 1;
              this.getData();
            }
          });
        })
        .catch((_) => {
          this.$message({
            type: "info",
            message: this.$t("versionsps.CancelDelMes"),
          });
        });
    },
    getProtocolStatus() {
      this.$api
        .post(this, "user/brand/agreement/status", { brandIds: this.brandIds })
        .then((res) => {
          if (res.code === 0) {
            let brandarr = [];
            // 是否存在膜切机
            res.distributorBrandAgreementList.forEach((item) => {
              // status 2 未弹出
              brandarr.push(item.brandId);
              // 7-膜切机  33-定制（正式）42-定制（测试）
              if (item.brandId === 7 && item.status === 2) {
                // 膜切机
                this.showOpenCut = true;
              }
              if (item.brandId === 33 && item.status === 2) {
                // 定制
                this.showOpenPro = true;
              }
            });
            if (brandarr.indexOf(7) === -1 && this.brandIds.indexOf(7) > -1) {
              // 膜切机
              this.showOpenCut = true;
            }
            if (brandarr.indexOf(33) === -1 && this.brandIds.indexOf(33) > -1) {
              // 定制
              this.showOpenPro = true;
            }
            if (!this.showOpenCut && !this.showOpenPro) {
              this.orderSubmit();
            }
          }
        });
    },
    closeUserProtocol(value) {
      this.$api
        .post(this, "user/brand/agreement", {
          status: 1,
          brandIds: this.brandIds,
        })
        .then((res) => {
          if (res.code === 0) {
            if (value === 2) {
              // 膜切机
              this.showOpenCut = false;
            } else if (value === 3) {
              // 定制
              this.showOpenPro = false;
            }
            this.orderSubmit();
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleSubmits() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning(this.$t("versionsps.RemindSubMes"));
        return;
      }
      // let ids = []
      this.multipleSelection.forEach((item) => {
        this.ids.push(item.id);
        this.brandIds.push(item.brands[0]);
      });
      this.$confirm(
        this.$t("versionsps.RemindImportMes"),
        this.$t("P.Prompt"),
        {
          confirmButtonText: this.$t("Message.Confirm"),
          cancelButtonText: this.$t("Message.Cancel"),
          type: "warning",
          center: true,
        }
      ).then((_) => {
        this.orderSubmit();
        // // 判断协议弹框
        // this.getProtocolStatus();
      });
    },
    orderSubmit() {
      orderHint().then((res) => {
        if (res) {
          this.submitLoading = true;
          ordersImportOrder({
            ids: this.ids.join(",").toString(),
          })
            .then((res) => {
              this.multipleSelection.forEach((item) => {
                item.submitStatus = 2;
              });
              if (res.success) {
                this.$message.success({
                  message: this.$t("versionsps.BatchSucMes"),
                  duration: 1 * 1000,
                });
                this.pageInfo.page = 1;
                this.getData();
              }
              this.submitLoading = false;
            })
            .catch((_) => {
              this.$message({
                type: "info",
                message: this.$t("versionsps.CancelSubMes"),
              });
            });
        }
      });
    },
  },
  created() {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    this.getData();
  },
  watch: {
    importOrderStatus(val) {
      this.pageInfo.page = 1;
      this.pageInfo.orderTypeValue = "";
      this.pageInfo.currencyType = "";
      this.getData();
    },
    "pageInfo.orderTypeValue": {
      handler() {
        this.pageInfo.page = 1;
        this.searchData();
      },
      deep: true,
    },
    "pageInfo.currencyType": {
      handler() {
        this.pageInfo.page = 1;
        this.searchData();
      },
      deep: true,
    },
  },
};
</script>
<style lang="less" scoped>
@import url("../../assets/less/variable.less");
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
  }
  .content {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
    .query {
      position: relative;
      padding-bottom: 15px;
      /deep/ .el-input {
        width: 140px;
        margin-right: 5px;
        font-size: 12px;
        box-sizing: border-box;
        border-radius: 4px;
      }
      /deep/ .el-input--mini .el-input__inner {
        height: 38px;
        line-height: 38px;
        border: 1px solid @bdLighterColor;
      }
      .el-button {
        padding: 0 12px;
        height: 38px;
        line-height: 38px;
        box-sizing: border-box;
        border-radius: 4px;
      }
      .mini-search-btn {
        background-color: @blue;
      }
      .handle-wrap {
        position: absolute;
        right: 0;
        bottom: 15px;
        font-size: 14px;
        color: @blue;
        line-height: 1;
        .iconfont {
          margin-right: 2px;
          color: @lighterGray;
        }
      }
    }
    .table {
      width: 100%;
      margin-top: 5px;
      margin-bottom: 30px;
      /deep/ .el-table {
        width: 100%;
        word-wrap: break-word;
        word-break: break-all;
        border-collapse: collapse;
        tr {
          &:hover {
            background-color: @lightGrayBg;
          }
          th {
            padding: 0;
            height: 32px;
            line-height: 32px;
            text-align: center;
            background-color: @bdLightColor;
            font-size: 12px;
            color: @gray;
            font-weight: normal;
          }
          td {
            height: 53px;
            text-align: center;
            font-size: 12px;
            color: @lightBlack;
            border: none;
            border-top: 1px dashed @bdLighterColor;
            .log {
              color: @blue;
              &:hover {
                opacity: 0.6;
              }
            }
          }
        }
      }
    }
    .top-box {
      position: relative;
      /deep/.el-tabs {
        .el-tabs__nav {
          overflow: hidden;
          background-color: @bdLightColor;
          border-radius: 4px;
        }
        .el-tabs__nav-wrap::after {
          background-color: transparent;
        }
        .el-tabs__item {
          min-width: 93px;
          padding: 0 12px;
          text-align: center;
          &:hover,
          &.is-active {
            color: @white !important;
            background-color: @blue;
          }
        }
      }
    }
    .mini-del {
      margin-right: 20px;
      color: @red;
      &:hover {
        opacity: 0.6;
      }
    }
    .mini-submit {
      margin-right: 20px;
      &:hover {
        opacity: 0.6;
      }
      &.disable {
        pointer-events: none;
        cursor: default;
        opacity: 0.6;
      }
    }
    .mini-export {
      color: @blue;
      &:hover {
        opacity: 0.6;
      }
    }
  }
}
</style>
