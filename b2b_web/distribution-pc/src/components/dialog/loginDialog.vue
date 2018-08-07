<!--
 * @Author: yaowei
 * @Date: 2018-05-20 09:03:32
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-14 17:26:49
-->
<template>
  <div class="login-dialog-wrap">
    <div class="login-content">
      <img
        class="login-logo"
        src="../../assets/images/login/login-logo.png"
        :alt="$i18n.locale === 'zh' ? 'BAT商城' : 'BAT'"
      />
      <span class="el-icon-close" @click="handleClose()"></span>
      <div class="content">
        <p class="blue title">欢迎来到BAT商城</p>
        <p class="blue title-en">Welcome to BAT</p>
        <p class="con rl-margin-top-lllg">
          全球领先的数码生活产品解决方案供应商
        </p>
        <p class="con con-en">
          The world's leading brand for digital life gadget solutions
        </p>
      </div>
      <div class="btn-wrap">
        <span class="blue-btn" @click="handleLogin()">{{ $t("P.Login") }}</span>
        <span class="gray-btn" @click="handleRegister()">{{
          $t("P.Register")
        }}</span>
      </div>

      <ul class="tips-list" v-show="showChoose">
        <li @click="handleInterval(1)">
          {{
            $i18n.locale === "zh"
              ? "稍后提示（5分钟）"
              : "Remind me 5 minutes later"
          }}
        </li>
        <li @click="handleInterval(0)">
          {{ $i18n.locale === "zh" ? "不再提示" : "Never ask me again" }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "loginDialog",
  data() {
    return {
      showChoose: false,
    };
  },
  methods: {
    // 关闭弹窗
    handleClose() {
      this.showChoose = true;
    },
    // 登录
    handleLogin() {
      this.$emit("handleLogin");
    },
    // 注册
    handleRegister() {
      this.$parent.showIntroDialog = true;
      this.$parent.showLoginDialog = false;
    },
    // 强制登陆定时器
    handleInterval(time) {
      // time：0 不再提示，1 5分钟后
      if (time === 0) {
        clearInterval(this.$parent.loginTimer);
        sessionStorage.setItem("clearInterval", "loginTimer");
      } else if (time === 1) {
        this.$parent.setTimer(300000);
      }
      this.showChoose = false;
      this.$emit("handleClose");
    },
  },
};
</script>

<style lang="less" scoped>
@import url("../../assets/less/variable.less");
.login-dialog-wrap {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 100;
  .login-content {
    position: absolute;
    top: 50%;
    left: 50%;
    padding-bottom: 50px;
    width: 548px;
    background-color: @white;
    border-radius: 8px;
    transform: translate(-50%, -50%);
    box-shadow: 0px 6px 17px 0px rgba(255, 255, 255, 0.5);
    .el-icon-close {
      position: absolute;
      top: 10px;
      right: 15px;
      font-size: 30px;
      color: @lighterGray;
      cursor: pointer;
      z-index: 111;
    }
    .login-logo {
      position: absolute;
      top: -65px;
      left: 50%;
      transform: translateX(-50%);
      z-index: 111;
    }
    .content {
      padding-top: 73px;
      padding-bottom: 60px;
      text-align: center;
      .blue {
        color: @lightBlue;
        &.title {
          font-size: 24px;
        }
        &.title-en {
          margin-top: 5px;
          font-size: 14px;
        }
      }
      .con {
        font-size: 16px;
        color: @lightBlack;
        line-height: 22px;
        &.con-en {
          margin-top: 10px;
          font-size: 14px;
          line-height: 20px;
        }
      }
    }
    .btn-wrap {
      font-size: 18px;
      text-align: center;
      span {
        display: inline-block;
        width: 180px;
        height: 50px;
        line-height: 50px;
        border-radius: 4px;
        cursor: pointer;
        & + span {
          margin-left: 15px;
        }
        &.gray-btn {
          color: @lightBlack;
          background-color: @entryBd;
        }
        &.blue-btn {
          color: @white;
          background-color: @blue;
        }
        &:hover {
          opacity: 0.8;
        }
      }
    }
    .tips-list {
      position: absolute;
      top: 50px;
      left: 100%;
      margin-left: -50px;
      padding: 10px 20px;
      background-color: @white;
      border-radius: 4px;
      border: 1px solid @bdColor;
      box-shadow: 0px 6px 17px 0px rgba(255, 255, 255, 0.5);
      li {
        white-space: nowrap;
        line-height: 30px;
        text-align: center;
        cursor: pointer;
        &:hover {
          color: @blue;
        }
      }
    }
  }
}
</style>