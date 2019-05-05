<template>
  <view class="priceLevelDetails">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>特殊公式的设置</view>
      </view>
    </view>

    <view class="priceLevelDetails-list">
      <view class="priceLevelDetails-line2"><text>价格设置</text></view>
      <view class="priceLevelDetails-line3">
        <text>等级价格 = 成本价 </text>
        <picker
          @change="bindPickerChange"
          :value="index"
          :range="symbolList"
          class="pld-picker"
        >
          <view class="pld-uni-upBox">
            <text>{{ arithmeticName }}</text>
            <text class="iconfont icon-Packup"></text>
          </view>
        </picker>
        <input
          type="number"
          placeholder="请输入数值"
          v-model="formInfo.arithmeticNum"
        />
      </view>
      <view class="priceLevelDetails-line2">
        <text>选择品牌</text>
        <text class="formula-required">(必选)</text>
      </view>
      <view class="priceLevelDetails-list2">
        <view
          class="priceLevelDetails-item"
          v-for="item in barndList"
          :key="item.id"
          @click="choickItem(item)"
        >
          <text
            class="iconfont"
            :class="
              item.isVisual == 1 ? 'icon-Checkthe itemHover' : 'icon-uncheck'
            "
          ></text>
          <text>{{ item.name }}</text>
        </view>
      </view>
    </view>

    <view class="formula-btn2" v-if="type == 2">
      <text @click="saveFun('PUT')" class="formula-save">保存</text>
      <text @click="deleteFun" class="formula-delete">删除</text>
    </view>
    <view class="formula-btn1" v-if="type == 1">
      <text @click="saveFun('POST')">添加</text>
    </view>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { scaleprice, brandList, scalepriceSpecial } from "../../common/api.js";
export default {
  data() {
    return {
      type: "", //1:新增  2：详情编辑
      symbolList: ["*", "+"],
      index: 0,
      formInfo: {
        arithmeticType: "", //1 乘 2 加 3 除 4 减
        arithmeticNum: "", //参加运算的数值
        nextScalePriceId: "", //分销商价格等级id
        brandCategorys: [], //分销商品牌id
        id: "",
      },
      arithmeticName: "请选择",
      barndList: [], //品牌列表
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad(option) {
    this.type = option.type;
    let that = this;
    if (option.type == 2) {
      let objs = JSON.parse(option.item);
      this.formInfo.arithmeticType = objs.arithmeticType;
      this.formInfo.arithmeticNum = objs.arithmeticNum;
      this.formInfo.nextScalePriceId = objs.nextScalePriceId;
      this.formInfo.id = objs.id;
      objs.brandCategorys.forEach((item) => {
        let objStr = { brandId: item.brandId };
        that.formInfo.brandCategorys.push(objStr);
      });
      this.arithmeticName = this.symbolList[this.formInfo.arithmeticType - 1];
    } else {
      this.formInfo.nextScalePriceId = option.priceId;
    }

    this.getBrandList();
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

    // 选择运算符
    bindPickerChange(e) {
      console.log(e.target.value);
      this.formInfo.arithmeticType = e.target.value + 1;
      this.arithmeticName = this.symbolList[e.target.value];
    },
    // 获取品牌
    getBrandList() {
      let that = this;
      let id = uni.getStorageSync("userId");
      brandList({ distributorId: id }).then((res) => {
        that.barndList = res.data;
        console.log("获取品牌：", res.data);
        that.barndList.forEach((item) => {
          item.isVisual = 0;
          if (that.type == 2) {
            that.formInfo.brandCategorys.forEach((items) => {
              if (items.brandId == item.id) {
                item.isVisual = 1;
              }
            });
          }
        });
        console.log(that.barndList);
      });
    },
    // 选择品牌
    choickItem(arr) {
      let that = this;
      this.formInfo.brandCategorys = [];
      let list = JSON.parse(JSON.stringify(this.barndList));
      list.forEach((item) => {
        if (item.id == arr.id) {
          if (item.isVisual == 1) {
            item.isVisual = 0;
          } else {
            item.isVisual = 1;
          }
        }
        if (item.isVisual == 1) {
          let objs = { brandId: item.id };
          that.formInfo.brandCategorys.push(objs);
        }
      });
      that.barndList = list;
    },

    // 保存
    saveFun(methods) {
      let that = this;
      let info = this.formInfo;
      console.log("56666", this.formInfo);
      if (info.arithmeticType == "") {
        this.tipFun("请选择运算符");
        return;
      }
      if (info.arithmeticType != "" && info.arithmeticNum == "") {
        this.tipFun("请输入数值");
        return;
      }
      if (info.brandCategorys.length == 0) {
        this.tipFun("请选择至少一个品牌");
        return;
      }
      scalepriceSpecial(methods, that.formInfo).then((res) => {
        if (res.success) {
          that.tipFun("设置特殊公式成功！");
          setTimeout(() => {
            uni.navigateTo({
              url: "priceLevelDetails?id=" + that.formInfo.nextScalePriceId,
            });
          }, 500);
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },

    // 删除
    deleteFun() {
      let that = this;
      scalepriceSpecial("DELETE", { id: that.formInfo.id }).then((res) => {
        if (res.success) {
          that.tipFun("删除成功");
          setTimeout(() => {
            uni.navigateTo({
              url: "priceLevelDetails?id=" + that.formInfo.nextScalePriceId,
            });
          }, 500);
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },
  },
};
</script>

<style lang="scss">
.pld-picker {
  width: 150rpx;
}
.priceLevelDetails-list {
  padding-top: calc(116rpx + var(--status-bar-height));
  // #ifdef H5
  padding-top: 104rpx;
  // #endif
  padding-bottom: 150rpx;
  font-size: 28rpx;

  .priceLevelDetails-line2 {
    padding: 30rpx;
    color: #999;
    display: flex;

    .formula-required {
      font-size: 24rpx;
      margin-left: 15rpx;
    }
  }
  .priceLevelDetails-line3 {
    display: flex;
    align-items: center;
    background: #fff;
    padding: 20rpx 30rpx;
    .pld-uni-upBox {
      width: 160rpx;
      border: 1rpx solid $opacity-border;
      display: flex;
      align-items: center;
      border-radius: 10rpx;
      padding: 10rpx 0;
      margin-left: 30rpx;
      text:nth-child(1) {
        display: block;
        width: 120rpx;
        text-align: center;
        font-size: 28rpx;
        font-weight: bold;
        color: #333;
      }
      text:nth-child(2) {
        font-size: 20rpx;
        color: #999;
      }
    }
    input {
      width: 200rpx;
      font-size: 28rpx;
      margin-left: 70rpx;
    }
  }

  .priceLevelDetails-item {
    display: flex;
    align-items: center;
    padding: 30rpx 30rpx;
    background: #fff;
    & + .priceLevelDetails-item {
      border-top: 1px solid $opacity-border;
    }
    text:nth-child(1) {
      margin-right: 30rpx;
      color: #999;
    }
  }
  .itemHover {
    color: $base-color1 !important;
  }
}
.formula-btn1 {
  position: fixed;
  bottom: 0;
  width: 100%;
  padding: 25rpx 0;
  background: #fff;
  text {
    display: block;
    width: 600rpx;
    text-align: center;
    background: $base-color1;
    color: #fff;
    padding: 20rpx 0;
    border-radius: 40rpx;
    margin: 0 auto;
  }
}
.formula-btn2 {
  position: fixed;
  bottom: 0;
  width: 100%;
  padding: 25rpx 0;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  text {
    display: block;
    width: 300rpx;
    text-align: center;
    padding: 20rpx 0;
    border-radius: 40rpx;
  }
  .formula-save {
    background: $base-color1;
    color: #fff;
  }
  .formula-delete {
    background: $opacity-border;
    color: #333;
    margin-left: 55rpx;
  }
}
</style>
