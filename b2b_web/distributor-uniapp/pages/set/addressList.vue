<template>
  <view class="addressList">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>收货地址</view>
      </view>
    </view>

    <view class="address-list" v-if="addressList.length > 0">
      <view
        class="address-list-line"
        v-for="item in addressList"
        :key="item.id"
      >
        <view
          class="address-list-lineBox"
          v-if="isParams"
          @click="selectAddress(item)"
        >
          <view class="address-list-lineTop">
            <view class="address-provinces">
              {{ item.countryName }}
              {{ item.provinceName ? item.provinceName : "" }}
              {{ item.cityName ? item.cityName : "" }}
              {{ item.districtName ? item.districtName : "" }}</view
            >
            <view class="address-icon">
              <image
                src="../../static/img/detele_icon.png"
                @click.stop="deleteAdress(item)"
              ></image>
              <image
                src="../../static/img/edit_icon.png"
                @click.stop="addressEdit(2, item)"
              ></image>
            </view>
          </view>
          <view class="address-detail">{{ item.address }}</view>
          <view class="address-list-lineBtm">
            <view>
              <text>{{ item.userName }}</text>
              <text>{{ item.phone }}</text>
            </view>
          </view>
        </view>
        <view class="address-list-lineBox" v-else>
          <view class="address-list-lineTop">
            <view class="address-provinces">
              {{ item.countryName }}
              {{ item.provinceName ? item.provinceName : "" }}
              {{ item.cityName ? item.cityName : "" }}
              {{ item.districtName ? item.districtName : "" }}
            </view>
            <view class="address-icon">
              <view>
                <image
                  src="../../static/img/detele_icon.png"
                  @click.stop="deleteAdress(item)"
                ></image>
              </view>
              <view>
                <image
                  src="../../static/img/edit_icon.png"
                  @click.stop="addressEdit(2, item)"
                ></image>
              </view>
            </view>
          </view>
          <view class="address-detail">{{ item.address }}</view>
          <view class="address-list-lineBtm">
            <view>
              <text>{{ item.userName }}</text>
              <text>{{ item.phone }}</text>
            </view>
          </view>
        </view>
        <view class="address-defaultIcon" @click="setDefault(item)">
          <text
            class="iconfont"
            :class="
              item.defaultFlag == 1
                ? 'icon-Checkthe choice-icon'
                : 'icon-uncheck'
            "
          ></text>
          <text>设为默认地址</text>
        </view>
      </view>
    </view>

    <view class="address-noData" v-if="addressList.length == 0">
      <image src="../../static/img/noAddress_img.png"></image>
      <view>暂无收货地址</view>
    </view>

    <view class="add-address" @click="addressEdit(1)">
      <view>新增收货地址</view>
    </view>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import {
  addressList,
  addressSet,
  addressDefault,
  region,
} from "../../common/api.js";
export default {
  data() {
    return {
      addressList: [],
      isTwoWay: false,
      isParams: false,
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad(option) {
    this.getAddressList();
    this.isTwoWay = option.isTwoWay ? option.isTwoWay : false;
    this.isParams = option.isTwoWay ? true : false;
  },
  onShow() {},
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        url: "set",
      });
    },
    // 轻提示弹框
    tipFun(text) {
      let that = this;
      this.tipText = text;
      this.tipTextShow = true;
      setTimeout(function () {
        that.tipTextShow = false;
      }, 3000);
    },
    // 获取地址列表
    getAddressList() {
      let that = this;
      let id = uni.getStorageSync("userId");
      let params = {
        id: id,
        page: 1,
        size: 10,
      };
      addressList(params).then((res) => {
        if (res.success) {
          let list = res.data.list;
          list.forEach((item) => {
            region({ id: item.countryId }).then((res) => {
              if (res.success) {
                that.$set(item, "countryName", res.data.regionName);
              }
            });
            region({ id: item.provinceId }).then((res) => {
              if (res.success) {
                that.$set(item, "provinceName", res.data.regionName);
              }
            });
            region({ id: item.cityId }).then((res) => {
              if (res.success) {
                that.$set(item, "cityName", res.data.regionName);
                item.haveNext = res.data.haveNext;
                if (res.data.haveNext == 1) {
                  region({ id: item.districtId }).then((res) => {
                    if (res.success) {
                      that.$set(item, "districtName", res.data.regionName);
                    }
                  });
                }
              }
            });
          });
          that.addressList = list;
          console.log("地址列表", list);
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },

    // 删除地址
    deleteAdress(item) {
      let that = this;
      let id = uni.getStorageSync("userId");
      addressSet("DELETE", { id: item.id }).then((res) => {
        if (res.success) {
          uni.removeStorageSync("orderAddress");
          that.tipFun("删除成功");
          this.getAddressList();
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },
    // 协议
    toAgreement(type) {
      uni.navigateTo({
        url: "agreement?type=" + type,
      });
    },
    // 设为默认地址
    setDefault(item) {
      let that = this;
      let list = this.addressList;
      addressDefault({ id: item.id }).then((res) => {
        if (res.success) {
          that.tipFun("设置成功");
          this.getAddressList();
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },

    // 新增or编辑收货地址
    addressEdit(type, item) {
      let info = JSON.stringify(item);
      console.log(item);
      uni.navigateTo({
        url: "addressEdit?type=" + type + "&info=" + info,
      });
    },

    // 订单过来选择地址
    selectAddress(item) {
      uni.setStorageSync("orderAddress", item);
      uni.navigateTo({
        url: "/pages/shoppingCart/confirmOrder?isTwoWay=" + this.isTwoWay,
      });
    },
  },
};
</script>

<style lang="scss">

.addressList {
  .address-noData {
    font-size: 30rpx;
    text-align: center;
    padding-top: 300rpx;
    image {
      width: 268rpx;
      height: 258rpx;
      margin-bottom: 20rpx;
      color: #666;
    }
  }
  .address-list {
    padding-top: calc(116rpx + var(--status-bar-height));
    height: calc(100vh - 276rpx - var(--status-bar-height));
    // #ifdef H5
    padding-top: 104rpx;
    height: calc(100vh - 152rpx - var(--window-bottom));
    // #endif
    overflow-y: auto;
    .address-list-line {
      background: #fff;
      margin-top: 20rpx;
      padding: 30rpx;
      position: relative;
      .address-defaultIcon {
        position: absolute;
        right: 30rpx;
        bottom: 30rpx;
        font-size: 24rpx;
        color: #999;
        display: flex;
        align-items: center;
        .iconfont {
          position: relative;
          top: 3rpx;
          font-size: 28rpx;
        }

        text {
          margin-right: 5rpx;
        }
        .icon-uncheck {
          color: #999;
        }
        .icon-Checkthe {
          color: $base-color1;
        }
      }
      .address-list-lineTop {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .address-provinces {
          color: #999;
          font-size: 26rpx;
        }
        .address-icon {
          display: flex;
          align-items: center;
          view {
            width: 50rpx;
            text-align: center;
            cursor: pointer;
          }
          image:nth-child(1) {
            width: 28rpx;
            height: 27rpx;
          }
          image:nth-child(2) {
            width: 30rpx;
            height: 26rpx;
            margin-left: 30rpx;
          }
        }
      }
      .address-detail {
        font-size: 28rpx;
        color: #333;
        font-weight: 400;
        margin-top: 18rpx;
      }
      .address-list-lineBtm {
        color: #666;
        font-size: 26rpx;
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-top: 20rpx;
        view:nth-child(1) {
          text:nth-child(1) {
            margin-right: 20rpx;
          }
        }
        view:nth-child(2) {
          display: flex;
          align-items: center;
          icon {
            font-size: 36rpx;
            margin-right: 10rpx;
          }
          .choice-icon {
            color: $base-color1;
          }
        }
      }
    }
  }
  .add-address {
    background: #fff;
    position: fixed;
    bottom: 0;
    width: 100%;
    padding: 30rpx;
    box-sizing: border-box;
    view {
      width: 100%;
      height: 80rpx;
      line-height: 80rpx;
      text-align: center;
      color: #fff;
      font-size: 28rpx;
      background: $bg-gradient-btn;
      border-radius: 40rpx;
    }
  }
}
</style>
