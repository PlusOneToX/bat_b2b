<template>
  <div class="saleman">
    <div class="top-tip">
      <p class="distributor">{{ distributorName }}</p>
      <div class="remark">
        <p>请确认绑定的分销商无误后</p>
        <p>输入您的手机号进行绑定</p>
      </div>
    </div>
    <div class="saleman-module">
      <div class="item-module">
        <span>手机号</span>
        <input
          type=""
          v-model="phone"
          placeholder="请输入手机号码"
          maxlength="11"
        />
      </div>
      <div class="item-module">
        <span>验证码</span>
        <input
          class="code"
          type="text"
          v-model="code"
          placeholder="手机验证码"
        />
        <span class="tip" v-if="times == 60" @click="getCode">获取验证码</span>
        <span class="tip" v-if="times < 60">{{ times }}S重新获取</span>
      </div>
    </div>
    <div class="btn-submit" @click="confirmClick">确认并绑定</div>
  </div>
</template>

<script>
import api from "common/js/allApi.js";

export default {
  data() {
    return {
      providerList: [],
      platform: "",
      distributorId: "",
      distributorName: "",
      openId: "",
      phone: "",
      code: "", //获取的验证码
      times: 60,
      tipTextShow: false,
      tipText: "",
      appId: "",
      webViewUrl: "",
    };
  },
  mounted() {
    // 判断是否从第三方进入,获取平台和分销商ID
    let plat = this.$route.query.platform;
    let distrId = this.$route.query.distributorId;
    let orderSource = this.$route.query.orderSource;
    let platform = localStorage.getItem("platform");
    let distributorId = localStorage.getItem("distributorId");
    if (distrId && (plat || orderSource)) {
      this.platform = plat || orderSource;
      this.distributorId = distrId;
    } else if (platform && distributorId) {
      this.platform = platform;
      this.distributorId = distributorId;
    }
    localStorage.setItem("platform", this.platform);
    localStorage.setItem("distributorId", this.distributorId);
    if (orderSource) {
      localStorage.setItem("orderSource", orderSource);
    } else {
      localStorage.setItem("orderSource", this.platform);
    }

    if (this.platform === "1") {
      // 微信公众号登录授权
      if (this.$route.query.appid && !sessionStorage.getItem("hasAuth")) {
        this.weixinLogin();
      }
    }
  },
  methods: {
    // 微信授权获取openId、accessToken
    weixinLogin() {
      let APPID;
      if (this.$route.query.appid) {
        // 判断缓存ID是否与
        if (
          localStorage.getItem("appid") &&
          localStorage.getItem("appid") === this.$route.query.appid
        ) {
          APPID = localStorage.getItem("appid");
        } else {
          APPID = this.$route.query.appid;
          localStorage.setItem("appid", APPID);
        }
      } else {
        APPID = localStorage.getItem("appid");
      }
      let code = this.getUrlCode() ? this.getUrlCode().code : "";
      if (!code) {
        let scope = "snsapi_base";
        // 未授权，需要重新授权获取code
        let authUrl =
          "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
          APPID +
          "&redirect_uri=" +
          encodeURIComponent(window.location.href) +
          "&response_type=code&scope=" +
          scope +
          "&state=STATE&connect_redirect=1#wechat_redirect";
        window.location.replace(authUrl);
      } else {
        // 通过code换取网页授权access_token
        this.$api
          .post(this, api.wxLogin, {
            appId: APPID,
            code: code,
          })
          .then((res) => {
            if (res.success) {
              if (res.data) {
                // 根据分销商id获取分销商信息
                this.getInfo(this.distributorId);

                this.openId = res.data;
                localStorage.setItem("openId", res.data.openId);
                sessionStorage.setItem("hasAuth", true);
              }
            } else {
              this.$toast(res.errMessage);
            }
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    getUrlCode() {
      let url = location.search;
      /* eslint-disable */
      let theRequest = new Object();
      if (url.indexOf("?") !== -1) {
        let str = url.substr(1);
        let strs = str.split("&");
        for (let i = 0; i < strs.length; i++) {
          theRequest[strs[i].split("=")[0]] = strs[i].split("=")[1];
        }
        return theRequest;
      }
    },
    // 根据分销商id获取分销商信息
    getInfo(distributorId) {
      this.isComit = distributorId;
      this.$api
        .get(this, api.getInfoByDistributorId, { id: distributorId })
        .then((res) => {
          this.datall = JSON.stringify(res);
          if (res.success) {
            this.distributorName = res.data.name;
          }
        });
    },
    // 确认并绑定
    confirmClick() {
      let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      let phone = this.phone;
      let code = this.code;
      if (phone == "") {
        this.$toast("请输入手机号");
        return;
      }
      if (phone != "" && !myreg.test(phone)) {
        this.$toast("手机号格式不正确");
        return;
      }
      if (code == "") {
        this.$toast("验证码不能为空");
        return;
      }
      // 获取openid
      let openId = localStorage.getItem("openId");
      this.$api
        .put(this, api.bindWechat, {
          openId: openId,
          codeType: 6,
          phone: phone,
          code: code,
          distributorId: this.distributorId,
        })
        .then((res) => {
          if (res.success) {
            let username = res.data;
            this.$router.push({
              path: "/bindSuccess",
              query: {
                distributor: this.distributorName,
                phone: phone,
                username: username,
              },
            });
          } else {
            this.$toast(res.errMessage);
          }
        });
    },
    // 获取验证码
    getCode() {
      this.times = 60;
      let phone = this.phone;
      let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      if (phone == "") {
        this.$toast("请输入手机号");
        return;
      }
      if (phone != "" && !myreg.test(phone)) {
        this.$toast("手机号格式不正确");
        return;
      }
      this.$api
        .post(this, api.phoneVerify, {
          codeType: 6,
          phone: phone,
          skipCheck: true,
        })
        .then((res) => {
          if (res.success) {
            this.code = res.data.code;
            let times = setInterval(() => {
              this.times--;
              if (this.times == 0) {
                clearInterval(times);
                this.times = 60;
              }
            }, 1000);
          } else {
            this.$toast(res.errMessage);
          }
        });
    },
  },
};
</script>

<style lang="stylus" scoped>
$white = #fff;
$dark = #333;
$darkGray = #666;
$blue = #0076A5;
$opacity-border = rgba(241, 242, 249, 0.5);

.saleman {
  font-size: 13px;

  .top-tip {
    padding-bottom: 30px;
    text-align: center;
    background-color: $white;

    .distributor {
      padding: 30px 0;
      font-size: 15px;
      color: $dark;
    }

    .remark {
      color: $darkGray;
      line-height: 1.5;
    }
  }

  .saleman-module {
    margin-top: 10px;
    font-size: 14px;
    background-color: $white;

    input {
      padding: 12px 15px;
    }

    .item-module {
      position: relative;
      display: flex;
      align-items: center;
      padding: 0 15px;

      & + .item-module {
        border-top: 1px solid $opacity-border;
      }

      span {
        color: $dark;
        width: 60px;
      }

      input {
        padding-left: 25px;
      }

      .code {
        flex: 1;
      }

      .tip {
        position: absolute;
        right: 15px;
        width: 88px;
        color: $blue;
        font-size: 12px;
        text-align: right;
      }
    }
  }

  .btn-submit {
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

