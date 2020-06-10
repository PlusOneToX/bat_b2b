<!--
 * @Author: yaowei
 * @Date: 2019-05-15 14:05:07
 * @LastEditors: yaowei
 * @LastEditTime: 2019-12-04 10:17:15
-->
<template>
  <div class="code-wrap" :class="{ hiddenHeader: isMiniProgram }">
    <THeader
      class="header-wrap"
      :title="'卡包中心'"
      :hasBg="true"
      @back="goBack"
    ></THeader>
    <div class="content">
      <ul class="code-title">
        <li
          :class="{ active: curTab === index }"
          v-for="(code, index) in codeTitle"
          :key="index"
          @click="handleClickCode(index, code)"
        >
          {{ code.name }}
        </li>
      </ul>

      <van-pull-refresh
        v-model="isLoading"
        @refresh="onRefresh"
        v-show="codeList.length > 0"
      >
        <van-list
          v-model="loadingMore"
          :finished="finishedAll"
          :immediate-check="false"
          @load="onLoadMore"
        >
          <ul class="code-list">
            <li
              v-for="(code, index) in codeList"
              :key="index"
              :class="{ used: code.status === 2, expired: code.status === 3 }"
            >
              <div class="card-t">
                <img :src="code.headImg" :alt="code.codeName" />
              </div>
              <p class="code">
                <span>{{ code.secretCode }}</span>
                <span
                  class="mail-fee"
                  v-if="Number(code.mailSetting) === 2 && code.mailFee"
                >
                  （需支付 {{ code.mailFee }} 元运费）
                </span>
              </p>
              <div class="code-b">
                <span class="time"
                  >有效期至：{{ code.endTime | formatDate }}</span
                >
                <span
                  v-show="code.status === 1"
                  class="btn confirm-btn"
                  @click="goPath('custom', code)"
                  >立即定制</span
                >
              </div>
            </li>
          </ul>
        </van-list>
      </van-pull-refresh>

      <!-- 底线 -->
      <Divider
        v-show="finishedAll && codeList.length > 0"
        class="divider-wrap"
        :text="'你看到我的底线啦'"
      ></Divider>

      <div class="no-data-wrap" v-show="codeList.length <= 0">
        <NoData :flagType="'no-order'" :showBtn="false"></NoData>
      </div>
    </div>

    <div class="btn-wrap">
      <p class="btn confirm-btn" @click="handleShowDialog">绑定兑换卡</p>
    </div>

    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script>
// 组件
import THeader from "components/tHeader/tHeader";
import NoData from "components/noData/noData";
import Divider from "components/divider/divider";
import Loading from "components/loading/loading";
// js
import { formatDate } from "common/js/common";
import wx from "weixin-js-sdk";
// api
import api from "api/allApi.js";

export default {
  name: "CodeList",
  data() {
    return {
      curTab: 0, // 当前tab
      // 卡包状态
      codeTitle: [
        {
          name: "未使用",
          id: 1,
        },
        {
          name: "已使用",
          id: 2,
        },
        {
          name: "已过期",
          id: 3,
        },
      ],
      curId: 1, // 当前卡包状态id
      userId: "", // 用户id
      page: 1,
      size: 10,
      codeList: [], // 卡包数据
      isLoading: true, // 下拉刷新加载
      loadingMore: false, // 加载更多
      finishedAll: false, // 加载完成
      showMask: false, // 蒙版
      isLoading: true, // 加载
      message: "数据加载中", // 加载内容
      isMiniProgram: false, // 是否是小程序
    };
  },
  filters: {
    formatDate(time) {
      if (time) {
        let date = new Date(time);
        return formatDate(date, "yyyy-MM-dd");
      } else {
        return "";
      }
    },
  },
  created() {
    var enterFlag = this.getQueryVariable("enterFlag");
    if (enterFlag === "codeList") {
      // 小程序进入
      var params = this.getQueryVariable("enterParams");
      if (params) {
        var enterParams = JSON.parse(params);
        localStorage.setItem("userId", enterParams.userId);
        localStorage.setItem("phone", enterParams.phone);
        localStorage.setItem("userNo", enterParams.userNo);
        localStorage.setItem("auth", enterParams.auth);
        localStorage.setItem("openId", enterParams.openid);
        localStorage.setItem("distributorId", enterParams.distributorId);
        localStorage.setItem("exchangeId", enterParams.exchangeId);
        localStorage.setItem("platform", enterParams.platform);
        localStorage.setItem("orderSource", enterParams.orderSource);

        this.isMiniProgram = true;
        sessionStorage.setItem("isMiniProgram", this.isMiniProgram);
      }
    }

    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    this.userId = localStorage.getItem("userId");

    // 获取卡包列表
    this.getCodeListData(1);
  },
  methods: {
    getQueryVariable(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      } else {
        return null;
      }
    },
    // 获取卡包列表
    getCodeListData(status) {
      this.$api
        .get(this, api.getCodeList, {
          userId: this.userId,
          page: this.page,
          size: this.size,
          status: status,
        })
        .then((res) => {
          if (res.success) {
            this.isLoading = false;
            this.message = "";
            this.loadingMore = false;

            let rows = res.data.list;
            if (rows == null || rows.length === 0) {
              // 加载结束
              this.finishedAll = true;
              return;
            }

            if (this.page === 1) {
              this.codeList = [];
            }
            // 将新数据与老数据进行合并
            this.codeList = this.codeList.concat(rows);

            // 如果列表数据条数>=总条数，不再触发滚动加载
            let total = res.data.total;
            if (this.codeList.length >= total) {
              this.finishedAll = true;
            }
          } else {
            this.isLoading = false;
            this.message = "";
          }
        });
    },
    // 卡包状态切换
    handleClickCode(index, code) {
      this.curTab = index;
      // 获取卡包列表
      this.curId = code.id;
      this.codeList = [];
      this.isLoading = true;
      this.message = "数据加载中";
      this.getCodeListData(this.curId);
    },
    // 下拉刷新
    onRefresh() {
      this.page = 1;
      setTimeout(() => {
        this.getCodeListData(this.curId);
        this.isLoading = false;
        this.message = "";
      }, 1000);
    },
    // 加载更多
    onLoadMore() {
      this.page++;
      this.getCodeListData(this.curId);
    },
    // 页面跳转
    goPath(name, code) {
      // 获取默认 exchangeId
      this.initExchangeId(name, code.exchangeId);
    },
    // 获取默认 exchangeId
    initExchangeId(name, exchangeId) {
      this.$api
        .get(this, api.getDefaultExchangeId, {
          id: exchangeId,
        })
        .then((res) => {
          if (res.success) {
            this.exchangeId = res.data.exchangeId;
            if (res.data.distributorId) {
              this.distributorId = res.data.distributorId;
            } else {
              this.distributorId = 2601;
            }
            localStorage.setItem("distributorId", this.distributorId);
            localStorage.setItem("exchangeId", res.data.exchangeId);

            // 根据兑换码id获取可兑换材质
            this.$api
              .get(this, api.getMaterialByExchangeId, {
                id: exchangeId,
              })
              .then((res) => {
                if (res.success) {
                  if (res.data && res.data.length > 0) {
                    // 默认取第一个id
                    this.$router.push({
                      name: name,
                      query: {
                        materialId: res.data[0],
                      },
                    });
                  } else {
                    this.$router.push({ name: name });
                  }
                  this.isLoading = false;
                  this.message = "";
                } else {
                  this.isLoading = false;
                  this.message = "";
                  this.$toast.fail(res.errMessage);
                }
              });
          } else {
            this.isLoading = false;
            this.message = "";
            this.$toast.fail(res.errMessage);
          }
        });
    },
    handleShowDialog() {
      let platform = localStorage.getItem("platform");
      if (platform === "GF60006") {
        // 字节小程序
        tt.miniProgram.navigateTo({
          url:
            "/pages/code/bindCode/bindCode?enterFlag=codeList&userId=" +
            this.userId,
        });
      } else if (platform === "GF60007" || platform === "GF60008") {
        // 支付宝小程序
        my.navigateTo({
          url:
            "/pages/code/bindCode/bindCode?enterFlag=codeList&userId=" +
            this.userId,
        });
      } else {
        wx.miniProgram.navigateTo({
          url:
            "/pages/code/bindCode/bindCode?enterFlag=codeList&userId=" +
            this.userId,
        });
      }
    },
    // 返回首页
    goBack() {
      this.$router.replace("/index");
    },
  },
  components: {
    THeader,
    NoData,
    Divider,
    Loading,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.code-wrap {
  position: fixed;
  top: 44px;
  bottom: 0;
  width: 100%;
  background-color: $color-bg-white;

  &.hiddenHeader {
    top: 0;

    .header-wrap {
      display: none;
    }

    .code-title {
      top: 0;
    }
  }

  .content {
    position: relative;
    width: 100%;
    height: 100%;
    padding: 0 38px 65px;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }
  }

  .code-title {
    position: fixed;
    top: 44px;
    left: 0;
    width: 100%;
    display: flex;
    justify-content: space-between;
    padding: $spacing-lg 40px;
    background-color: $color-bg-white;
    z-index: 10;

    li {
      font-size: $font-xm;
      color: $color-font-grey;

      &.active {
        position: relative;
        font-size: $font-lg;
        color: $color-font-base;
        font-weight: 500;

        &::after {
          content: '';
          position: absolute;
          bottom: -10px;
          left: 50%;
          width: 20px;
          height: 10px;
          bg-image('home-tab');
          background-size: 100% 100%;
          transform: translateX(-50%);
        }
      }

      i {
        position: relative;
        font-style: normal;
        z-index: 2;
      }
    }
  }

  .code-list {
    position: relative;
    padding-top: 61px;
    height: 100%;

    li {
      padding-bottom: 8px;
      background-color: rgba(183, 183, 183, 0.1);
      border-radius: $radius-xm;
      overflow: hidden;

      & + li {
        margin-top: $spacing-lg;
      }

      &.used, &.expired {
        position: relative;
        background-color: rgba(246, 246, 246, 0.9);
        border-radius: $radius-xm;
        overflow: hidden;

        .card-t {
          img {
            -webkit-filter: grayscale(100%);
            -moz-filter: grayscale(100%);
            -ms-filter: grayscale(100%);
            -o-filter: grayscale(100%);
            filter: grayscale(100%);
            filter: $color-font-grey;
            border-radius: $radius-xm $radius-xm 0 0;
          }
        }

        &::after {
          content: '';
          position: absolute;
          right: 12px;
          bottom: 12px;
          width: 48px;
          height: 48px;
          background-size: 100% 100%;
        }
      }

      &.used {
        &::after {
          bg-image('used');
        }
      }

      &.expired {
        &::after {
          bg-image('expired');
        }
      }
    }

    .code {
      margin-top: 24px;
      font-size: $font-lg;
      line-height: 1;
      align(center);
    }

    .card-t {
      img {
        width: 100%;
      }
    }

    .mail-fee {
      position: relative;
      top: -1px;
      left: -4px;
      display: inline-block;
      font-size: $font-sm;
      color: $color-font-grey;
      align(center);
      transform: scale(0.95);
    }

    .code-b {
      margin-top: 18px;
      padding: 0 $spacing-base;
      lineHeight(30px);
      font-size: $font-sm;

      .btn {
        float: right;
        padding: 0 $spacing-lg;
        border-radius: $radius-sm;
      }
    }
  }

  .divider-wrap {
    padding-top: 12px;
    padding-bottom: 32px;

    .van-divider {
      margin: 0;
    }

    >>>.text {
      font-size: $font-sm;
      color: $color-font-grey;
      transform: scale(0.9);
    }
  }

  .no-data-wrap {
    position: fixed;
    width: 100%;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .btn-wrap {
    position: fixed;
    right: 0;
    bottom: 0;
    left: 0;
    display: flex;
    padding: $spacing-sm $spacing-base;
    background-color: $color-bg-white;
    box-shadow: $color-bt-shadow;
    z-index: 20;

    .btn {
      flex: 1;
      lineHeight(45px);
      font-size: $font-lg;
      align(center);
      border-radius: 45px;
    }
  }
}
</style>