<template>
  <view class="priceLevelDetails">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>新增价格等级</view>
      </view>
    </view>

    <view class="priceLevelDetails-list">
      <view class="priceLevelDetails-line">
        <text class="priceLevelDetails-textTitle">等级名称</text>
        <input placeholder="请输入等级名称" v-model="formInfo.name" />
      </view>
      <view class="priceLevelDetails-line">
        <text class="priceLevelDetails-textTitle">特殊公式</text>
        <view class="pld-checkGroud">
          <view>
            <text
              class="iconfont"
              :class="
                formInfo.specialFlag == 0
                  ? 'icon-Checkthe pldHover'
                  : 'icon-uncheck'
              "
              @click="choiceFun(1, 0)"
            >
            </text>
            <text>不启用</text>
          </view>
          <view>
            <text
              class="iconfont"
              :class="
                formInfo.specialFlag == 1
                  ? 'icon-Checkthe pldHover'
                  : 'icon-uncheck'
              "
              @click="choiceFun(1, 1)"
            ></text>
            <text>启用</text>
          </view>
        </view>
      </view>
      <view class="priceLevelDetails-line">
        <text class="priceLevelDetails-textTitle">状态</text>
        <view class="pld-checkGroud">
          <view>
            <text
              class="iconfont"
              :class="
                formInfo.openFlag == 0
                  ? 'icon-Checkthe pldHover'
                  : 'icon-uncheck'
              "
              @click="choiceFun(2, 0)"
            >
            </text>
            <text>停用</text>
          </view>
          <view>
            <text
              class="iconfont"
              :class="
                formInfo.openFlag == 1
                  ? 'icon-Checkthe pldHover'
                  : 'icon-uncheck'
              "
              @click="choiceFun(2, 1)"
            >
            </text>
            <text>启用</text>
          </view>
        </view>
      </view>
      <view class="priceLevelDetails-line2"><text>通用公式</text></view>
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
    </view>

    <view class="brandVisual-save"><text @click="saveFun">添加</text></view>
    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { scaleprice } from "../../common/api.js";
export default {
  data() {
    return {
      type: "", //1:新增  2：详情编辑
      symbolList: ["*", "+"],
      index: 0,
      formInfo: {
        distributorId: "",
        name: "",
        openFlag: 1, //状态, 1启用,0停用
        arithmeticType: "", //1 乘 2 加 3 除 4 减
        arithmeticNum: "", //参加运算的数值
        specialFlag: 1, //是否启用特殊公式, 1是,0否
      },
      arithmeticName: "请选择",
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad() {},
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
    // 选择特殊公式
    choiceFun(type, status) {
      //type:1:
      if (type == 1) {
        this.formInfo.specialFlag = status;
      } else {
        this.formInfo.openFlag = status;
      }
    },

    // 选择运算符
    bindPickerChange(e) {
      console.log(e.target.value);
      this.formInfo.arithmeticType = e.target.value + 1;
      this.arithmeticName = this.symbolList[e.target.value];
    },

    // 保存
    saveFun() {
      let that = this;
      this.formInfo.distributorId = uni.getStorageSync("userId");
      let info = this.formInfo;
      if (info.name == "") {
        this.tipFun("请输入等级名称");
        return;
      }
      if (info.arithmeticType != "" && info.arithmeticNum == "") {
        this.tipFun("请输入数值");
        return;
      }
      scaleprice("POST", this.formInfo).then((res) => {
        if (res.success) {
          that.tipFun("新增公式成功！");
          setTimeout(() => {
            uni.navigateTo({
              url: "priceLevel",
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
  padding-top: calc(136rpx + var(--status-bar-height));
  // #ifdef H5
  padding-top: 124rpx;
  // #endif
  font-size: 28rpx;

  .priceLevelDetails-line {
    background: #fff;
    display: flex;
    padding: 30rpx;
    border-bottom: 1rpx solid $opacity-border;
    .priceLevelDetails-textTitle {
      display: block;
      width: 180rpx;
    }
    input {
      font-size: 28rpx;
    }

    .pld-checkGroud {
      display: flex;
      align-items: center;
      color: #999;
      view {
        display: flex;
        align-items: center;
        width: 180rpx;
        text:nth-child(1) {
          font-size: 34rpx;
          margin-right: 15rpx;
        }
      }
    }
    .pldHover {
      color: $base-color1;
    }
  }
  .priceLevelDetails-line2 {
    padding: 30rpx;
    color: #999;
    display: flex;
    justify-content: space-between;
    text:nth-child(2) {
      color: $base-color1;
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
  .priceLevelDetails-line4 {
    padding: 30rpx;
    border-bottom: 1rpx solid $opacity-border;
    display: flex;
    justify-content: space-between;
    background: #fff;
    view:nth-child(2) {
      color: #999;
      text {
        margin-right: 15rpx;
      }
      icon {
        font-size: 20rpx;
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
    margin-left: 75rpx;
  }
}
</style>
