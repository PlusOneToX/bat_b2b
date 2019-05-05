<template>
  <view class="set">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>设置</view>
      </view>
    </view>
    <view class="set-box">
      <view class="set-module">
        <view
          class="set-module-line"
          @click="toAgreement(item.id, item.name)"
          v-for="item in agreementNameList"
          :key="item.id"
        >
          <view class="set-line-lf">{{ item.name }}</view>
          <view class="set-line-rg">
            <image src="../../static/img/auditTo_icon.png"></image>
          </view>
        </view>
      </view>

      <view class="set-module">
        <view class="set-module-line" @click="toModifyPhone">
          <view class="set-line-lf">手机号码</view>
          <view class="set-line-rg">
            <text>{{ phone }}</text>
            <image src="../../static/img/auditTo_icon.png"></image>
          </view>
        </view>
        <view class="set-module-line" @click="toPassword">
          <view class="set-line-lf">登录密码</view>
          <view class="set-line-rg">
            <text>修改密码</text>
            <image src="../../static/img/auditTo_icon.png"></image>
          </view>
        </view>
        <view class="set-module-line" @click="toAddress">
          <view class="set-line-lf">收货地址</view>
          <view class="set-line-rg">
            <text>管理地址</text>
            <image src="../../static/img/auditTo_icon.png"></image>
          </view>
        </view>
      </view>

      <!-- <view class="set-module">
				<view class="set-module-line">
					<view class="set-line-lf">意见反馈</view>
					<view class="set-line-rg"><image src="../../static/img/auditTo_icon.png"></image></view>
				</view>
			</view> -->

      <!-- <view class="set-module">
				<view class="set-module-line">
					<view class="set-line-lf">清除缓存</view>
					<view class="set-line-rg"><text>2.13M</text><image src="../../static/img/auditTo_icon.png"></image></view>
				</view>
				<view class="set-module-line">
					<view class="set-line-lf">当前版本</view>
					<view class="set-line-rg"><text>2.5.6</text><image src="../../static/img/auditTo_icon.png"></image></view>
				</view>
			</view> -->
    </view>
    <view class="set-loginOut" @click="loginOut">退出登录</view>
  </view>
</template>

<script>
import {
  agreementSettingList,
  agreementSettingId,
  loginOut,
} from "../../common/api.js";
export default {
  data() {
    return {
      phone: "",
      agreementNameList: [],
    };
  },
  onLoad() {
    this.phone = uni.getStorageSync("userPhone");
    this.agreementSettingListFun();
  },
  methods: {
    //返回
    toback() {
      uni.switchTab({
        url: "/pages/personalCenter/personalCenter",
      });
    },
    // 协议
    agreementSettingListFun() {
      this.getAgreementId(2);
      this.getAgreementId(1);
    },

    // 查协议id
    getAgreementId(type) {
      let that = this;
      let params = {
        agreementArea: 0, //0:国内  ，1：国外
        page: 1,
        size: 100,
        type: type, //1:品牌协议 2：用户协议
      };
      agreementSettingList(params).then((res) => {
        if (res.success) {
          console.log("协议列表：", res.data.list);
          that.agreementNameList = [
            ...that.agreementNameList,
            ...res.data.list,
          ];
        }
      });
    },

    // 退出登录
    loginOut() {
      uni.showModal({
        title: "提示",
        content: "此操作将退出, 是否继续?",
        cancelText: "取消", // 取消按钮的文字
        confirmText: "确定", // 确认按钮文字
        success: (res) => {
          if (res.confirm) {
            let id = uni.getStorageSync("contactId");
            if (id != "") {
              loginOut({ id: id }).then((loginRes) => {});
            }
            uni.removeStorageSync("name");
            uni.removeStorageSync("userId");
            uni.removeStorageSync("userName");
            uni.removeStorageSync("gradeId");
            uni.removeStorageSync("capitalStatus");
            uni.removeStorageSync("onWayFlag");
            uni.removeStorageSync("loginStatus");
            uni.removeStorageSync("freezeStatus");
            uni.removeStorageSync("shopOrderList");
            uni.removeStorageSync("orderParams");
            uni.removeStorageSync("autoDelivery");
            uni.removeStorageSync("orderDeliveryTrace");
            uni.removeStorageSync("userPhone");
            uni.removeStorageSync("orderAddress");
            uni.removeStorageSync("Font-Token");
            uni.removeStorageSync("distributionFlag");
            uni.removeStorageSync("companyName");
            uni.removeStorageSync("openId");
            uni.removeStorageSync("indexGetPrice");
            // uni.removeStorageSync('contactId');
            uni.navigateTo({
              url: "/pages/login/login",
            });
          } else {
          }
        },
      });
    },
    // 协议
    toAgreement(id, name) {
      uni.navigateTo({
        url: "agreement?id=" + id + "&name=" + name,
      });
    },

    // 修改手机号
    toModifyPhone() {
      uni.navigateTo({
        url: "modifyPhone",
      });
    },
    // 修改密码
    toPassword() {
      uni.navigateTo({
        url: "modifyPassword",
      });
    },
    // 管理地址
    toAddress() {
      uni.navigateTo({
        url: "addressList",
      });
    },
  },
};
</script>

<style lang="scss">
.set {
  padding-bottom: 120rpx;
  .set-box {
    padding-top: calc(116rpx + var(--status-bar-height));
    // #ifdef H5
    padding-top: 104rpx;
    // #endif
    padding-bottom: 130rpx;
  }
  .set-module {
    margin-top: 20rpx;
    .set-module-line {
      display: flex;
      align-items: center;
      justify-content: space-between;
      background: #fff;
      padding: 30rpx;
      border-bottom: 1rpx solid $opacity-border;
      font-size: 28rpx;
      .set-line-lf {
        font-weight: 400;
      }
      .set-line-rg {
        color: #999;
        image {
          width: 12rpx;
          height: 21rpx;
          margin-left: 15rpx;
        }
      }
    }
    .set-module-line:last-child {
      border: none;
    }
  }
  .set-loginOut {
    width: 86%;
    height: 100rpx;
    background: #b9b9b9;
    line-height: 90rpx;
    text-align: center;
    font-size: 36rpx;
    color: #fff;
    left: 7%;
    bottom: 100rpx;
    position: fixed;
  }
}
</style>
