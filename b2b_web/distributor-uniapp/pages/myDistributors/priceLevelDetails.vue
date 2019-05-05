<template>
  <view class="priceLevelDetails">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>价格等级详情</view>
      </view>
    </view>

    <view class="priceLevelDetails-list">
      <view class="priceLevelDetails-line">
        <text>等级名称</text>
        <input placeholder="请输入等级名称" v-model="formInfo.name" />
      </view>
      <view class="priceLevelDetails-line">
        <text>特殊公式</text>
        <view class="pld-checkGroud">
          <view>
            <text
              class="iconfont"
              :class="
                formInfo.specialFlag == 0
                  ? 'icon-icon_selected pldHover'
                  : 'icon-icon_selected_def'
              "
              @click="choiceFun(1, 0)"
            ></text>
            <text>不启用</text>
          </view>
          <view>
            <text
              class="iconfont"
              :class="
                formInfo.specialFlag == 1
                  ? 'icon-icon_selected pldHover'
                  : 'icon-icon_selected_def'
              "
              @click="choiceFun(1, 1)"
            ></text>
            <text>启用</text>
          </view>
        </view>
      </view>
      <view class="priceLevelDetails-line">
        <text>状态</text>
        <view class="pld-checkGroud">
          <view>
            <text
              class="iconfont"
              :class="
                formInfo.openFlag == 0
                  ? 'icon-icon_selected pldHover'
                  : 'icon-icon_selected_def'
              "
              @click="choiceFun(2, 0)"
            ></text>
            <text>停用</text>
          </view>
          <view>
            <text
              class="iconfont"
              :class="
                formInfo.openFlag == 1
                  ? 'icon-icon_selected pldHover'
                  : 'icon-icon_selected_def'
              "
              @click="choiceFun(2, 1)"
            ></text>
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
      <view class="priceLevelDetails-line2">
        <text>特殊公式</text><text @click="addFormula(1)">新增</text>
      </view>
      <view
        class="priceLevelDetails-line4"
        @click="addFormula(2, item)"
        v-for="item in formInfo.scalePriceSpecials"
        :key="item.id"
      >
        <view>特殊公式{{ item.id }}</view>
        <view>
          <text>成本价 {{ item.arithmeticName }} {{ item.arithmeticNum }}</text>
          <text class="iconfont icon-more"></text>
        </view>
      </view>
    </view>

    <view class="formula-btn2">
      <text @click="saveFun" class="formula-save">保存</text>
      <text @click="deleteFun" class="formula-delete">删除</text>
    </view>

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
        id: "",
        name: "",
        openFlag: 1, //状态, 1启用,0停用
        arithmeticType: "", //1 乘 2 加 3 除 4 减
        arithmeticNum: "", //参加运算的数值
        specialFlag: 1, //是否有特殊公式, 1是,0否
        scalePriceSpecials: [], //特殊公式
      },
      arithmeticName: "请选择",
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad(option) {
    this.getDataDetails(option.id);
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
      let value = Number(e.target.value);
      this.formInfo.arithmeticType = value + 1;
      this.arithmeticName = this.symbolList[value];
    },

    // 获取价格详情
    getDataDetails(id) {
      let that = this;
      scaleprice("GET", { id: id }).then((res) => {
        if (res.success) {
          let list = res.data;
          that.arithmeticName =
            that.symbolList[Number(list.arithmeticType) - 1];

          that.formInfo = res.data;
          if (
            that.formInfo.scalePriceSpecials &&
            that.formInfo.scalePriceSpecials.length > 0
          ) {
            that.formInfo.scalePriceSpecials.forEach((item) => {
              // item.arithmeticName=that.symbolList[item.arithmeticType-1];
              that.$set(
                item,
                "arithmeticName",
                that.symbolList[Number(item.arithmeticType) - 1]
              );
              console.log("特殊公式：", item.arithmeticType);
            });
          }
        } else {
          that.tipFun(res.errMessage);
        }
      });
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
      scaleprice("PUT", this.formInfo).then((res) => {
        if (res.success) {
          that.tipFun("修改公式成功！");
          uni.navigateTo({
            url: "priceLevel",
          });
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },

    //调整特殊公式
    addFormula(type, item) {
      let url = "";
      if (type == 1) {
        url = "formulaSet?type=" + type + "&priceId=" + this.formInfo.id;
      } else {
        url = "formulaSet?type=" + type + "&item=" + JSON.stringify(item);
      }

      uni.navigateTo({
        url: url,
      });
    },

    // 删除价格等级
    deleteFun() {
      let that = this;
      scaleprice("DELETE", { id: this.formInfo.id }).then((res) => {
        if (res.success) {
          that.tipFun("删除成功");
          setTimeout(() => {
            uni.navigateTo({
              url: "myDistributors",
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
    text {
      width: 150rpx;
    }
    input {
      font-size: 28rpx;
    }
    .iconfont {
      font-size: 34rpx;
      margin-right: 15rpx;
    }
    .pld-checkGroud {
      display: flex;
      align-items: center;
      color: #999;
      view {
        position: relative;
        padding-left: 42rpx;
        height: 34rpx;
        display: flex;
        align-items: center;
      }
      .iconfont {
        display: inherit;
        position: absolute;
        top: 50%;
        left: 0;
        transform: translateY(-50%);
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
      text:nth-child(1) {
        margin-right: 15rpx;
      }
      text:nth-child(2) {
        font-size: 20rpx;
      }
    }
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
