<template>
  <view class="myQrCode">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>专属二维码</view>
      </view>
    </view>

    <view class="myQrCode-module">
      <view class="myQrCode-name">
        <image src="../../static/img/heahericon.png"></image>
        <text>{{ name }}</text>
      </view>
      <image :src="qrcodeImg" class="qrcodeImg" @click="saveImg"></image>
      <view class="myQrCode-tip">
        <text>扫描上方二维码</text>
        <text>下级分销即可进入您专属分销平台</text>
        <text class="mt">长按二维码保存本地</text>
      </view>
    </view>
    <!-- <view class="myQrCode-btn" @click="saveQrcode">保存二维码</view> -->

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { distributorQrcode } from "../../common/api.js";
export default {
  data() {
    return {
      qrcodeImg: "",
      name: "",
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad() {
    this.getQrcode();
    this.name = uni.getStorageSync("userName");
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
    // tab选择
    tabClick(item) {
      this.tabIndex = item.id;
    },
    // 保存
    saveQrcode() {
      let that = this;
      uni.downloadFile({
        url: that.qrcodeImg,
        success: (res) => {
          console.log(res);
          if (res.statusCode == 200) {
            that.tipFun("保存成功");
          }
        },
      });
    },
    saveImg() {
      const that = this;
      console.log(that.qrcodeImg);
      uni.downloadFile({
        url: that.qrcodeImg, //可以写网络图片
        success: (res) => {
          if (res.statusCode === 200) {
            console.log(res);
            uni.showModal({
              title: "提示",
              content: "是否保存到相册？",
              cancelText: "取消", // 取消按钮的文字
              confirmText: "确定", // 确认按钮文字
              showCancel: true, // 是否显示取消按钮，默认为 true
              confirmColor: "#09D5EE",
              cancelColor: "#999999",
              success: (res2) => {
                if (res2.confirm) {
                  console.log(res.tempFilePath);
                  uni.saveImageToPhotosAlbum({
                    filePath: res.tempFilePath,
                    success: function () {
                      that.tipFun("保存成功");
                    },
                    fail: function () {
                      that.tipFun("保存失败请稍后再试!");
                    },
                  });
                }
              },
            });
          } else {
            that.tipFun("下载失败!");
          }
        },
      });
    },

    // 获取二维码
    getQrcode() {
      let that = this;
      let id = uni.getStorageSync("userId");
      distributorQrcode({ distributorId: id }).then((res) => {
        if (res.success) {
          console.log(res.data);
          that.qrcodeImg = res.data.qrCodeUrl;
        }
      });
    },
  },
};
</script>

<style lang="scss">
.myQrCode {
  .myQrCode-module {
    width: 560rpx;
    background: #fff;
    border-radius: 10rpx;
    position: absolute;
    top: 230rpx;
    left: 50%;
    margin-left: -280rpx;
    .myQrCode-name {
      font-size: 32rpx;
      display: flex;
      align-items: center;
      padding-top: 50rpx;
      margin-left: 80rpx;
      image {
        width: 100rpx;
        height: 100rpx;
        margin-right: 40rpx;
      }
    }
    .qrcodeImg {
      width: 500rpx;
      height: 500rpx;
      margin-left: 30rpx;
    }
    .myQrCode-tip {
      color: #999;
      font-size: 24rpx;
      text-align: center;
      padding-bottom: 80rpx;
      text {
        display: block;
        margin-top: 5rpx;

        &.mt {
          margin-top: 20rpx;
        }
      }
    }
  }
  .myQrCode-btn {
    width: 500rpx;
    color: #fff;
    background: $base-color1;
    text-align: center;
    border-radius: 10rpx;
    position: absolute;
    bottom: 180rpx;
    margin-left: 125rpx;
    padding: 20rpx 0;
  }
}
</style>
