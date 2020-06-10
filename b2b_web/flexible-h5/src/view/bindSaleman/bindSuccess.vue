<template>
  <div class="bindSuccess">
    <div class="bS-img">
      <img src="../../assets/images/success_icon.png" />
    </div>
    <p class="bS-tip">绑定成功</p>
    <p class="bS-name">分销商：{{ distributor }}</p>
    <p class="bS-name">姓名：{{ username }}</p>
    <p class="bS-name">手机号：{{ phone }}</p>
    <p class="bS-btn" @click="handleClose">关闭</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      distributor: "",
      phone: "",
      username: "",
    };
  },
  mounted() {
    this.distributor = this.$route.query.distributor;
    this.phone = this.$route.query.phone;
    this.username = this.$route.query.username;
  },
  methods: {
    // 关闭
    handleClose() {
      let browser = navigator.userAgent.toLowerCase();
      if (browser.match(/MicroMessenger/i) == "micromessenger") {
        // 微信浏览器
        document.addEventListener("WeixinJSBridgeReady", function () {
            WeixinJSBridge.call("closeWindow");
          },
          false
        );
        WeixinJSBridge.call("closeWindow");
      } else {
        window.opener = null;
        window.open('', '_self', '');
        window.close();
      }
    },
  },
};
</script>

<style lang="stylus" scoped>
$white = #fff;
$dark = #333;
$gray = #999;
$blue = #0076A5;

.bindSuccess {
  text-align: center;

  .bS-img {
    padding-top: 65px;

    img {
      width: 100px;
      height: 100px;
    }
  }

  .bS-name {
    color: $dark;
    font-size: 14px;
    margin: 10px 0;
  }

  .bS-tip {
    font-size: 12px;
    color: $gray;
    margin-top: 5px;
    margin-bottom: 30px;
  }

  .bS-btn {
    margin: 50px auto;
    width: 88%;
    height: 50px;
    line-height: 50px;
    font-size: 16px;
    color: $white;
    text-align: center;
    background: $blue;
    border-radius: 50px;
  }
}
</style>
