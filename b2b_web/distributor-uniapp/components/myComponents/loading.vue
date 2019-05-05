<template>
  <view class="loading">
    <view class="spinner">
      <view class="spinner-container container1">
        <view class="circle1"></view>
        <view class="circle2"></view>
        <view class="circle3"></view>
        <view class="circle4"></view>
      </view>
      <view class="spinner-container container2">
        <view class="circle1"></view>
        <view class="circle2"></view>
        <view class="circle3"></view>
        <view class="circle4"></view>
      </view>
      <view class="spinner-container container3">
        <view class="circle1"></view>
        <view class="circle2"></view>
        <view class="circle3"></view>
        <view class="circle4"></view>
      </view>
    </view>
    <view class="text">{{ message }}</view>
  </view>
</template>

<script type="text/ecmascript-6">
export default {
  name: "loading",
  data() {
    return {};
  },
  props: {
    message: {
      type: String,
      default: "",
    },
  },
  watch: {
    message(value) {
      this.message = value;
    },
  },
};
</script>

<style lang="less" scoped>

.loading {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate3d(-50%, -50%, 0);
  padding: 30px 30px 20px;
  border-radius: 10px;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  pointer-events: none;

  .text {
    display: block;
    margin-top: 30px;
    font-size:30rpx;
    color:#fff;
  }

  .spinner {
    display: block;
    width: 30px;
    height: 30px;
    position: relative;
  }

  .container1 > view, .container2 > view, .container3 > view {
    width: 6px;
    height: 6px;
    background-color: #fff;
    border-radius: 100%;
    position: absolute;
    -webkit-animation: bouncedelay 1.2s infinite ease-in-out;
    animation: bouncedelay 1.2s infinite ease-in-out;
    -webkit-animation-fill-mode: both;
    animation-fill-mode: both;
  }

  .spinner .spinner-container {
    position: absolute;
    width: 100%;
    height: 100%;
  }

  .container2 {
    -webkit-transform: rotateZ(45deg);
    transform: rotateZ(45deg);
  }

  .container3 {
    -webkit-transform: rotateZ(90deg);
    transform: rotateZ(90deg);
  }

  .circle1 {
    top: 0;
    left: 0;
  }

  .circle2 {
    top: 0;
    right: 0;
  }

  .circle3 {
    right: 0;
    bottom: 0;
  }

  .circle4 {
    left: 0;
    bottom: 0;
  }

  .container2 .circle1 {
    -webkit-animation-delay: -1.1s;
    animation-delay: -1.1s;
  }

  .container3 .circle1 {
    -webkit-animation-delay: -1s;
    animation-delay: -1s;
  }

  .container1 .circle2 {
    -webkit-animation-delay: -0.9s;
    animation-delay: -0.9s;
  }

  .container2 .circle2 {
    -webkit-animation-delay: -0.8s;
    animation-delay: -0.8s;
  }

  .container3 .circle2 {
    -webkit-animation-delay: -0.7s;
    animation-delay: -0.7s;
  }

  .container1 .circle3 {
    -webkit-animation-delay: -0.6s;
    animation-delay: -0.6s;
  }

  .container2 .circle3 {
    -webkit-animation-delay: -0.5s;
    animation-delay: -0.5s;
  }

  .container3 .circle3 {
    -webkit-animation-delay: -0.4s;
    animation-delay: -0.4s;
  }

  .container1 .circle4 {
    -webkit-animation-delay: -0.3s;
    animation-delay: -0.3s;
  }

  .container2 .circle4 {
    -webkit-animation-delay: -0.2s;
    animation-delay: -0.2s;
  }

  .container3 .circle4 {
    -webkit-animation-delay: -0.1s;
    animation-delay: -0.1s;
  }

  @keyframes bouncedelay {
    0%, 80%, 100% {
      -webkit-transform: scale(0);
    }

    40% {
      -webkit-transform: scale(1);
    }
  }

  @keyframes bouncedelay {
    0%, 80%, 100% {
      transform: scale(0);
      -webkit-transform: scale(0);
    }

    40% {
      transform: scale(1);
      -webkit-transform: scale(1);
    }
  }
}
</style>
