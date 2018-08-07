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
            <h6 class="user-right-title">{{ $t("UserCenter.VoucherDetails") }}</h6>

            <div class="total-amount">
              {{ $t("UserCenter.VoucherTotal") }}:
              <span class="amount"
                >{{
                  $i18n.locale === "en" && $root.currency === "USD"
                    ? "$"
                    : "￥"
                }}{{voucherAmount}}</span
              >
            </div>

            <!-- tab选项 -->
            <div class="nav rl-clear rl-margin-top-default">
              <ul class="rl-fl" :class="{'navEn': $i18n.locale === 'en' && $root.currency === 'USD'}">
                <li
                  :class="voucherType === 1 ? 'current' : ''"
                  @click="changeState(1)"
                >
                  {{ $t("UserCenter.VoucherUsable") }}({{validCount}})
                </li>
                <li
                  :class="voucherType === 2 ? 'current' : ''"
                  @click="changeState(2)"
                >
                  {{ $t("UserCenter.VoucherUnusable") }}
                </li>
              </ul>
            </div>
            <!-- 列表 -->
            <ul class="voucher-list" v-show="voucherList.length > 0">
              <li class="voucher-list-item" v-for="item in voucherList"
                :key="item.id">
                <div class="voucher-list-inner" :class="{disabled: item.voucherStatus === 7 || item.voucherStatus === 9}">
                  <div class="item-t">
                    <span class="value"
                      >{{ $t("UserCenter.VoucherValue") }}:
                      {{
                        $i18n.locale === "en" && $root.currency === "USD"
                          ? "$"
                          : "￥"
                      }}{{item.faceValue}}</span
                    >
                    <span class="history" @click="handleHistory(item.id)">
                      {{ $t("UserCenter.VoucherUseHistory") }}
                      <i class="el-icon-arrow-right"></i>
                    </span>
                  </div>
                  <div class="item-b">
                    <p class="name">{{item.name}}</p>
                    <p class="no">{{ $t("UserCenter.VoucherNo") }}: {{item.voucherNo}}</p>
                    <p class="tips">{{ $t("UserCenter.VoucherTips") }}</p>
                    <p class="time">{{ $t("UserCenter.VoucherValid") }}: <span>{{item.startTime | formatDate }}</span> 至 <span>{{item.endTime | formatDate}}</span></p>
                    <p class="amount">
                      {{
                        $i18n.locale === "en" && $root.currency === "USD"
                          ? "$"
                          : "￥"
                      }}<span>{{item.balance}}</span>
                    </p>
                    <!-- 立即使用 -->
                    <span class="btn" v-if="voucherType === 1" @click="goIndex">{{
                      $t("UserCenter.VoucherUseNow")
                    }}</span>
                    <!-- 已用完 -->
                    <p class="status" v-if="voucherType === 2 && item.voucherStatus === 9" :class="{'usedEn': $i18n.locale === 'en' && $root.currency === 'USD'}">{{ $t("UserCenter.VoucherUsed") }}</p>
                    <!-- 已过期 -->
                    <p class="status" v-if="voucherType === 2 && item.voucherStatus === 7" :class="{'expiredEn': $i18n.locale === 'en' && $root.currency === 'USD'}">
                      {{ $t("UserCenter.VoucherExpired") }}
                    </p>
                  </div>
                </div>
              </li>
            </ul>
            <!-- 暂无数据 -->
            <div class="no-voucher" v-show="voucherList.length <= 0">
              <img class="img" width="100%" src="../../assets/images/goods-empty.png" alt />
              <p class="text">{{ $t("P.NoData") }}</p>
            </div>
          </div>
          <div class="rl-tr rl-margin-top-default">
            <el-pagination
              v-if="paginationShow"
              background
              :current-page.sync="cur_page"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              layout="prev, pager, next, jumper"
              :page-size="pagesize"
              :total="totalCount"
            ></el-pagination>
          </div>

          <!-- 使用记录 -->
          <el-dialog
            :close-on-click-modal="false"
            title="使用记录"
            :visible.sync="historyDialogVisible"
            class="history-dialog"
          >
            <table class="table">
              <tr>
                <th>{{ $t("UserCenter.Time") }}</th>
                <th>{{ $t("UserCenter.OrderNo") }}</th>
                <th>{{ $t("UserCenter.VoucherUsedAmount") }}</th>
                <th>{{ $t("UserCenter.VoucherBalance") }}</th>
              </tr>
              <tr
                v-for="item in historyData"
                :key="item.id"
              >
                <td>{{ item.useTime }}</td>
                <!-- 订单号 -->
                <td>{{ item.orderNo }}</td>
                <!-- 使用金额 -->
                <td>
                  {{ item.amountFlag }}{{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? "$"
                      : "￥"
                  }}{{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? (Number(item.useAmount) * exchange).toFixed(2)
                      : item.useAmount
                  }}{{ item.amountFlag === '+' ? '(退还)' : ''}}
                </td>
                <!-- 金额 -->
                <td>
                  {{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? "$"
                      : "￥"
                  }}{{
                    $i18n.locale === "en" && $root.currency === "USD"
                      ? (Number(item.balance) * exchange).toFixed(2)
                      : item.balance
                  }}
                </td>
              </tr>
            </table>
            <div class="rl-tr rl-margin-top-default">
              <el-pagination
                v-if="paginationShow"
                small
                background
                :current-page.sync="historyInfo.page"
                @current-change="historyCurrentChange"
                @size-change="historySizeChange"
                layout="prev, pager, next, jumper"
                :page-size="historyInfo.size"
                :total="historyTotal"
              ></el-pagination>
            </div>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import GD from "@/assets/js/globalData";
import { voucherList, voucherAmount, getVoucherUsedList } from "@/apiService/api";
import { formatDate } from "@/assets/js/common.js";
export default {
  name: "MoneyDetails",
  components: {
    Header,
    Left,
  },
  data() {
    return {
      userState: 2,
      totalCount: 0,
      cur_page: 1,
      pagesize: 10,
      voucherAmount: 0, // 代金券总额
      voucherList: [],
      voucherType: 1, // 状态（可用 1，不可用 2）
      voucherStatus: "5", // 代金券状态
      validCount: 0, // 可用数量
      paginationShow: true, // 分页控制
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      exchange: 1, //汇率
      // 消费记录
      historyDialogVisible: false, // 使用记录弹窗
      historyData: [],
      historyTotal: 0,
      historyInfo: {
        rebateVoucherId: "",
        page: 1,
        size: 10,
      },
    };
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd");
    },
  },
  methods: {
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    getStatus(row) {
      // 事件状态
      switch (row) {
        case 1:
          return this.$t("UserCenter.Recharge"); //充值
        case 2:
          return this.$t("UserCenter.Cash"); //提现
        case 3:
          return this.$t("P.Payments"); //支付
        case 4:
          return this.$t("UserCenter.ExpenditureAmount"); //退款
      }
    },
    // 当前页码
    handleCurrentChange(val) {
      this.cur_page = val;
      this.getListData();
    },
    // 每页条数
    handleSizeChange(val) {
      this.pagesize = val;
      this.getListData();
    },
    // 明细列表
    getListData() {
      let userId = localStorage.getItem("userId");
      let parmas = {
        page: this.cur_page,
        size: this.pagesize,
        voucherStatusStr: this.voucherStatus,
        distributorId: userId,
      };
      voucherList(parmas).then((res) => {
        if (res.success) {
          this.voucherList = res.data.list;
          this.totalCount = res.data.total;
          if (this.voucherType === 1) {
            this.validCount = this.totalCount;
          }
        }
      });
    },
    // 代金券总额
    getVoucherAmount() {
      let userId = localStorage.getItem("userId");
      voucherAmount({
        distributorId: userId
      }).then((res) => {
        if (res.success) {
          this.voucherAmount = res.data
        }
      });
    },
    // 切换tab状态
    changeState(type) {
      this.paginationShow = false;
      this.voucherType = type;
      if (type === 1) {
        this.voucherStatus = "5"
      } else if (type === 2) {
        this.voucherStatus = "7,9"
      }
      this.cur_page = 1;
      this.getListData();
      this.$nextTick(function () {
        this.paginationShow = true;
      });
    },
    // 立即使用
    goIndex() {
      // 跳转首页
      this.$router.push({name: 'Index'});
    },
    // 消费记录
    handleHistory(id) {
      this.historyDialogVisible = true;
      this.historyInfo.rebateVoucherId = id;
      this.handleHistoryData(true);
    },
    handleHistoryData(type) {
      getVoucherUsedList(this.historyInfo).then((res) => {
        if (res.success) {
          if (res.data.list && res.data.list.length > 0) {
            let historyData = res.data.list;
            historyData.forEach((item) => {
              this.$set(item, "amountFlag", "+")
              if (item.useAmount < 0) {
                this.$set(item, "amountFlag", "-")
                this.$set(item, "useAmount", -item.useAmount)
              }
            })

            this.historyData = historyData;
            this.historyTotal = res.data.total;
            if (type) {
              // 点击使用记录按钮时，弹窗显示
              this.historyDialogVisible = true;
            }
          } else {
            this.$message({
              type: "info",
              message: "暂无使用记录~",
            });
          }
        }
      });
    },
    historySizeChange(size) {
      this.historyInfo.size = size;
      this.historyInfo.page = 1;
      this.handleHistoryData();
    },
    historyCurrentChange(page) {
      this.historyInfo.page = page;
      this.handleHistoryData();
    },
  },
  created() {
    this.exchange = Number(localStorage.getItem("exchange"));
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    this.getListData();
    this.getVoucherAmount(); // 代金券总额
  },
};
</script>

<style scoped="scoped" lang='less'>
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

  .total-amount {
    margin-top: 30px;
    font-size: 14px;
    color: @lighterBlack;
    line-height: 22px;

    .amount {
      font-size: 16px;
      color: @redLabel;
      font-weight: bold;
    }
  }

  .content {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
  }
  .nav {
    margin-bottom: 15px;
    width: 100%;
    height: 40px;
    line-height: 40px;
    ul {
      overflow: hidden;
      background-color: @bdLightColor;
      border-radius: 4px;
      &.navEn {
        li {
          width: auto;
          padding: 0 12px;
          font-size: 13px;
        }
      }
      li {
        float: left;
        width: 95px;
        cursor: pointer;
        font-size: 14px;
        color: @lighterBlack;
        text-align: center;
        &:hover,
        &.current {
          color: @white;
          background-color: @blue;
        }
      }
    }
  }

  .history-dialog {
    /deep/ .el-dialog__body {
      padding: 0 15px 30px;
    }
  }

  .table {
    width: 100%;
    word-wrap: break-word;
    word-break: break-all;
    border-collapse: collapse;
    tr {
      &:hover {
        background-color: @lightGrayBg;
      }
      & + tr {
        border-top: 1px dashed @bdLighterColor;
      }
      th {
        height: 30px;
        line-height: 30px;
        text-align: center;
        background-color: @bdLightColor;
        font-size: 12px;
        color: @gray;
        font-weight: normal;
      }
      td {
        height: 50px;
        text-align: center;
        font-size: 12px;
        color: @lightBlack;
      }
    }
  }

  .voucher-list {
    padding: 14px 69px;
    box-sizing: border-box;
    overflow: hidden;

    .voucher-list-item {
      float: left;
      width: 50%;
      padding: 6px;
      box-sizing: border-box;
      overflow: hidden;
    }

    .voucher-list-inner {
      border: 1px solid #f16e7b;

      &.disabled {
        border-color: #dedede;

        .item-t {
          background-color: #dddddd;

          .history {
            color: #01cbdd;
          }
        }

        .item-b {
          .amount {
            color: #b1b1b1;
          }
        }
      }

      .item-t {
        padding: 10px 15px;
        font-size: 12px;
        color: @white;
        line-height: 17px;
        background-color: @redLabel;

        .history {
          float: right;
          cursor: pointer;
        }
      }

      .item-b {
        position: relative;
        padding: 22px 23px 28px;
        font-size: 12px;
        color: @lightGray;
        line-height: 16px;
        overflow: hidden;

        .name {
          font-size: 18px;
          color: @lightBlack;
          line-height: 22px;
        }

        .no {
          margin-top: 8px;
          font-size: 11px;
          color: @darkGray;
        }

        .tips {
          margin-top: 16px;
        }

        .time {
          margin-top: 8px;
        }

        .amount {
          position: absolute;
          top: 29px;
          right: 23px;
          font-size: 12px;
          color: @redLabel;

          span {
            font-size: 30px;
          }
        }

        .btn {
          display: inline-block;
          position: absolute;
          right: 23px;
          bottom: 22px;
          padding: 6px 16px;
          color: @white;
          background-color: @redLabel;
          border-radius: 40px;
          cursor: pointer;
        }

        .status {
          position: absolute;
          right: -10px;
          bottom: -11px;
          width: 68px;
          height: 68px;
          font-size: 14px;
          color: #9c9c9c;
          padding-left: 11px;
          line-height: 68px;
          background-color: #e6e6e6;
          border-radius: 50%;
          transform: rotate(40deg);
          box-sizing: border-box;

          // 已用完
          &.usedEn {
            padding-left: 6px;
          }
          // 已过期
          &.expiredEn {
            padding-left: 8px;
          }
        }
      }
    }
  }
}

.no-voucher {
  padding: 50px 0;
  text-align: center;

  .img {
    width: 120px;
  }

  .text {
    margin-top: 15px;
  }
}
</style>
