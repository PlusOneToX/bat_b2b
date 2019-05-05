<template>
  <view class="priceLevel">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>价格等级设置</view>
      </view>
    </view>

    <view class="priceLevel-box">
      <view class="priceLevel-list">
        <view class="priceLevel-line">
          <view class="priceLevel-list-Lf">
            <text>默认等级价</text>
            <text>(使用中)</text>
          </view>
          <view class="priceLevel-list-RG">
            <text>成本价</text>
          </view>
        </view>
      </view>
      <view class="priceLevel-list">
        <view
          class="priceLevel-line"
          v-for="item in priceLevelList"
          :key="item.id"
          @click="topage(item.id)"
        >
          <view class="priceLevel-list-Lf">
            <text>{{ item.name }}</text>
            <text>{{ item.openFlag == 1 ? "使用中" : "已停用" }}</text>
          </view>
          <view class="priceLevel-list-RG">
            <text>
              成本价 {{ item.arithmeticName }} {{ item.arithmeticNum }}
            </text>
            <image
              src="../../static/imgs/personalMore.png"
              class="toIcon"
            ></image>
          </view>
        </view>
      </view>
    </view>

    <view class="brandVisual-save">
      <text @click="addPriceLevel">新增</text>
    </view>
  </view>
</template>

<script>
import { scalepriceList } from "../../common/api.js";
export default {
  data() {
    return {
      priceLevelList: [{}],
    };
  },
  onLoad(option) {
    this.getScalepriceList();
  },
  onShow() {},
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },

    // 获取价格等级
    getScalepriceList() {
      let id = uni.getStorageSync("userId");
      let that = this;
      scalepriceList({ distributorId: id }).then((res) => {
        if (res.success) {
          let list = res.data;
          list.forEach((item) => {
            if (item.arithmeticType == 1) {
              item.arithmeticName = "*";
            } else if (item.arithmeticType == 2) {
              item.arithmeticName = "+";
            } else if (item.arithmeticType == 3) {
              item.arithmeticName = "/";
            } else if (item.arithmeticType == 4) {
              item.arithmeticName = "-";
            }
          });
          that.priceLevelList = list;
        }
      });
    },

    // 详情
    topage(id) {
      uni.navigateTo({
        url: "priceLevelDetails?id=" + id,
      });
    },
    addPriceLevel() {
      uni.navigateTo({
        url: "addPriceLevel",
      });
    },
  },
};
</script>

<style lang="scss">
.priceLevel-box {
  font-size: 28rpx;
  padding-top: calc(136rpx + var(--status-bar-height));
  // #ifdef H5
  padding-top: 124rpx;
  // #endif

  .priceLevel-list {
    .priceLevel-line {
      background: #fff;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx;
      border-bottom: 1rpx solid $opacity-border;
      .priceLevel-list-Lf {
        text:nth-child(2) {
          font-size: 24rpx;
          color: #999;
          margin-left: 15rpx;
        }
      }
      .priceLevel-list-RG {
        font-size: 26rpx;
        color: #999;
        text {
          margin-right: 15rpx;
        }
        .toIcon {
          width: 24rpx;
          height: 24rpx;
        }
      }
    }
  }
}
.brandVisual-save {
  position: fixed;
  bottom: 0;
  background: #fff;
  width: 100%;
  padding: 25rpx 0;
  text {
    display: block;
    width: 600rpx;
    text-align: center;
    background: $base-color1;
    color: #fff;
    height: 80rpx;
    line-height: 80rpx;
    border-radius: 40rpx;
    margin: 0 auto;
  }
}
</style>
