<template>
  <view class="brandVisual">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>设置品牌可视</view>
      </view>
    </view>

    <view class="brandVisual-box">
      <view class="brandVisual-all" @click="allcheckFun">
        <text
          class="iconfont"
          :class="allCheck ? 'icon-Checkthe itemHover' : 'icon-uncheck'"
        ></text>
        <text>全选</text>
      </view>
      <view class="brandVisual-list">
        <view
          class="brandVisual-item"
          v-for="item in barndList"
          :key="item.id"
          @click="choickItem(item)"
        >
          <text
            class="iconfont"
            :class="
              item.isVisual == true ? 'icon-Checkthe itemHover' : 'icon-uncheck'
            "
          ></text>
          <text>{{ item.name }}</text>
        </view>
      </view>
    </view>

    <view class="brandVisual-save"><text @click="setBrand">保存</text></view>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { nobrandList, brandList } from "../../common/api.js";
export default {
  data() {
    return {
      id: "",
      barndList: [], //可视品牌
      noBrandList: [], //不可视品牌
      dataList: [
        { id: 1, name: "BAT" },
        { id: 2, name: "BAT2" },
      ],
      checkList: [],
      allCheck: false, //是否全选
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad(option) {
    this.id = option.id;
    this.getDataList(option.id);
  },
  onShow() {},
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
      let list = this.barndList;
      let flag = true;

      list.forEach((items) => {
        if (items.id == item.id) {
          let ischeck = !items.isVisual;
          that.$set(items, "isVisual", ischeck);
        }
        if (!items.isVisual) {
          that.checkList.push(items.id);
        }
        if (!items.isVisual) {
          flag = false;
        }
      });
      this.allCheck = flag;
      console.log(that.checkList);
      console.log(this.barndList);
    },

    // 全选
    allcheckFun() {
      this.checkList = [];
      this.allCheck = !this.allCheck;
      this.barndList.forEach((items) => {
        items.isVisual = this.allCheck;
      });
    },

    // 设置品牌
    setBrand() {
      console.log("---", this.barndList);
      this.checkList = [];
      let list = this.barndList;
      for (let i = 0; i < list.length; i++) {
        if (!list[i].isVisual) {
          this.checkList.push(list[i].id);
        }
      }
      let that = this;
      let params = {
        brandIds: this.checkList,
        distributorId: this.id,
      };
      nobrandList("POST", params).then((res) => {
        if (res.success) {
          that.tipFun("设置品牌成功");
          that.getDataList(that.id);
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },

    // 获取品牌
    getBrandList(list) {
      let that = this;
      let id = uni.getStorageSync("userId");
      brandList({ distributorId: id }).then((res) => {
        that.barndList = res.data;
        that.barndList.forEach((item) => {
          that.$set(item, "isVisual", true);
          if (list.length == 0) {
            that.$set(item, "isVisual", true);
            that.allCheck = true;
          }
          for (let i = 0; i < list.length; i++) {
            if (item.id == list[i]) {
              console.log(list[i]);
              that.$set(item, "isVisual", false);
            }
          }
        });
        console.log(that.barndList);
      });
    },

    // 获取下级不可视品牌
    getDataList(id) {
      let that = this;
      nobrandList("GET", { distributorId: id }).then((res) => {
        if (res.success) {
          this.noBrandList = res.data;
          console.log("不可视：", res.data);
          this.getBrandList(res.data);
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
    text {
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
    text {
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
