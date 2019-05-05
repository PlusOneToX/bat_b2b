<template>
  <view class="brandVisual">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>设置商品可视</view>
      </view>
    </view>

    <view class="brandVisual-box">
      <view class="brandVisual-all" @click="allcheckFun">
        <text
          class="iconfont"
          :class="allCheck == 1 ? 'icon-Checkthe itemHover' : 'icon-uncheck'"
        >
        </text>
        <text>全选</text>
      </view>
      <view class="brandVisual-list">
        <view
          class="brandVisual-item"
          v-for="item in goodList"
          :key="item.id"
          @click="choickItem(item)"
        >
          <text
            class="iconfont"
            :class="
              item.isVisual == 1 ? 'icon-Checkthe itemHover' : 'icon-uncheck'
            "
          ></text>
          <text>{{ item.goodsName }}</text>
        </view>
      </view>
    </view>

    <view class="brandVisual-save"><text @click="setBrand">保存</text></view>
    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { goodsList, nogoodsList } from "../../common/api.js";
export default {
  data() {
    return {
      id: "",
      goodList: [], //可视品牌
      noGoodList: [], //不可视品牌
      checkList: [],
      allCheck: 0, //是否全选
      page: 1,
      total: 0,
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad(option) {
    this.id = option.id;
    this.getDataList(option.id);
  },
  onShow() {},
  onPullDownRefresh() {
    this.page += 1;
    if (this.page < this.total) {
      this.page += 1;
      this.getDataList(this.id);
    }
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
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

    // 单选品牌
    choickItem(item) {
      this.checkList = [];
      let that = this;
      let list = this.goodList;
      list.forEach((items) => {
        if (items.id == item.id) {
          if (items.isVisual == 1) {
            items.isVisual = 0;
          } else {
            items.isVisual = 1;
          }
        }
        if (items.isVisual == 0) {
          that.checkList.push(item.id);
        }
        if (that.checkList.length == 0) {
          this.allCheck = 1;
        } else {
          this.allCheck = 0;
        }
      });
      console.log(that.checkList);
    },

    // 全选
    allcheckFun() {
      this.checkList = [];
      if (this.allCheck == 1) {
        this.allCheck = 0;
      } else {
        this.allCheck = 1;
      }
      let list = this.goodList;
      list.forEach((items) => {
        items.isVisual = this.allCheck;
      });
    },

    // 设置品牌
    setBrand() {
      this.page = 1;
      let that = this;
      let params = {
        goodsIds: this.checkList,
        distributorId: this.id,
      };
      nogoodsList("POST", params).then((res) => {
        if (res.success) {
          that.tipFun("设置商品可视成功");
          this.getDataList(this.id);
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },

    // 获取商品
    getGoodsList(list) {
      let that = this;
      let id = uni.getStorageSync("userId");

      let params = {
        distributorId: id,
        page: this.page,
        size: 14,
      };
      goodsList(params).then((res) => {
        that.total = res.data.pages;
        that.goodList = res.data.list;
        that.goodList.forEach((item) => {
          if (list.length == 0) {
            item.isVisual = 1;
            that.allCheck = 1;
          }
          for (let i = 0; i < list.length; i++) {
            if (item.id == list[i]) {
              item.isVisual = 0;
            } else {
              item.isVisual = 1;
            }
          }
        });
        console.log(that.goodList);
      });
    },

    // 获取下级不可视品牌
    getDataList(id) {
      let that = this;
      nogoodsList("GET", { distributorId: id }).then((res) => {
        if (res.success) {
          that.noGoodList = res.data;
          that.getGoodsList(res.data);
        }
      });
    },
  },
};
</script>

<style lang="scss">
.brandVisual-box {
  font-size: 28rpx;
  padding-top: calc(116rpx + var(--status-bar-height));
  // #ifdef H5
  padding-top: 104rpx;
  // #endif
  padding-bottom: 150rpx;
  .brandVisual-all {
    display: flex;
    align-items: center;
    padding: 25rpx 30rpx;
    .iconfont {
      margin-right: 30rpx;
      font-size: 28rpx;
      color: $base-color4;
    }
  }
  .brandVisual-item {
    display: flex;
    align-items: center;
    padding: 30rpx 30rpx;
    background: #fff;
    border-bottom: 1rpx solid $opacity-border;
    .iconfont {
      margin-right: 30rpx;
      font-size: 28rpx;
      color: $base-color4;
    }
  }
  .itemHover {
    color: $base-color1 !important;
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
    background: $bg-gradient-btn;
    font-size: 32rpx;
    color: #fff;
    height: 80rpx;
    line-height: 80rpx;
    border-radius: 100rpx;
    margin: 0 auto;
  }
}
</style>
