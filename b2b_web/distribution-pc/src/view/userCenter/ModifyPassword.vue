<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div class="user-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
          <div class="content register-content">
            <h6 class="user-right-title">{{$t('UserCenter.Password')}}</h6>
            <div class="info rl-margin-zero">
              <div class="info-base rl-padding-top-default rl-padding-bottom-default">
                <div class="item rl-clear">
                  <span class="rl-fl enterLeft rl-tr">{{$t('UserCenter.OriginalPassword')}}</span>
                  <div class="enterInput rl-fl">
                    <input
                      class="common-input"
                      type="password"
                      maxlength="16"
                      v-model="oldPassword"
                      :placeholder="$t('P.PleaseEnter')"
                    />
                  </div>
                </div>
                <div class="item rl-clear rl-relative">
                  <span class="rl-fl enterLeft rl-tr">{{$t('UserCenter.NewPassword')}}</span>
                  <div class="enterInput rl-fl">
                    <input
                      @blur="schoolTwo"
                      class="common-input"
                      type="password"
                      maxlength="16"
                      v-model="newPassword"
                      :placeholder="$t('P.PleaseEnter')"
                    />
                  </div>
                  <p class="prompt" v-show="$i18n.locale === 'zh'">密码长度为6-16个字符，可为英文、数字、符号组合，区分大小写</p>
                  <p
                    class="prompt en"
                    v-show="$i18n.locale === 'en'"
                  >The password is composed of 6-16 characters, and is case-sensitive</p>
                </div>
                <div class="item rl-clear rl-relative">
                  <span class="rl-fl enterLeft rl-tr">{{$t('UserCenter.ConfirmPassword')}}</span>
                  <div class="enterInput rl-fl">
                    <input
                      @blur="schoolThr"
                      class="common-input"
                      type="password"
                      maxlength="16"
                      v-model="confirmPassword"
                      :placeholder="$t('P.PleaseEnter')"
                    />
                  </div>
                  <p class="prompt" v-show="$i18n.locale === 'zh'">请再次输入密码，确认密码必须与密码一致</p>
                  <p
                    class="prompt en"
                    v-show="$i18n.locale === 'en'"
                  >Please enter password again to confirm that the password must be identical.</p>
                </div>
              </div>
              <div class="submit rl-tc">
                <span
                  @click="modifyPassword"
                  class="rl-bg-blue-xs rl-tc rl-text-white cursor-pointer"
                >{{$t('UserCenter.ConfirmModify')}}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import md5 from "js-md5";

import GD from "@/assets/js/globalData";
// liu--
import { userPassword} from '@/apiService/api'
export default {
  name: "ModifyPassword",
  components: {
    Header,
    Left,
  },
  data() {
    return {
      userState: 2,
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
    };
  },
  created() {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    
  },
  methods: {
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    schoolOne() {
      var reg = /^(?!([a-zA-Z]+|\d+)$)[a-zA-Z\d]{6,16}$/;
      if (!reg.test(this.oldPassword)) {
        if (this.$i18n.locale === "zh") {
          this.$message.warning(
            "密码长度为6-16个字符，可为英文、数字组合，区分大小写！"
          );
        } else {
          this.$message.warning(
            "The password is composed of 6-16 characters, and is case-sensitive."
          );
        }
        this.oldPassword = "";
      }
    },
    schoolTwo() {
      var reg = /^[A-Za-z0-9]+$/;
      if (!reg.test(this.newPassword)) {
        if (this.$i18n.locale === "zh") {
          this.$message.warning(
            "密码长度为6-16个字符，可为英文、数字组合，区分大小写！"
          );
        } else {
          this.$message.warning(
            "The password is composed of 6-16 characters, and is case-sensitive."
          );
        }
        this.newPassword = "";
      } else if (this.newPassword.length < 6 || this.newPassword.length > 16) {
        if (this.$i18n.locale === "zh") {
          this.$message.warning(
            "密码长度为6-16个字符，可为英文、数字组合，区分大小写！"
          );
        } else {
          this.$message.warning(
            "The password is composed of 6-16 characters, and is case-sensitive."
          );
        }
        this.newPassword = "";
      }
    },
    schoolThr() {
      var reg = /^[A-Za-z0-9]+$/;
      if (!reg.test(this.confirmPassword)) {
        if (this.$i18n.locale === "zh") {
          this.$message.warning(
            "密码长度为6-16个字符，可为英文、数字组合，区分大小写！"
          );
        } else {
          this.$message.warning(
            "The password is composed of 6-16 characters, and is case-sensitive."
          );
        }
        this.confirmPassword = "";
      } else if (
        this.confirmPassword.length < 6 ||
        this.confirmPassword.length > 16
      ) {
        if (this.$i18n.locale === "zh") {
          this.$message.warning(
            "密码长度为6-16个字符，可为英文、数字组合，区分大小写！"
          );
        } else {
          this.$message.warning(
            "The password is composed of 6-16 characters, and is case-sensitive."
          );
        }
        this.confirmPassword = "";
      }
    },
    // 密码修改
    modifyPassword() {
      if (this.oldPassword === "") {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("请输入原密码！");
        } else {
          this.$message.warning("Please enter original password.");
        }
        return false;
      }
      if (this.newPassword === "") {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("请输入新密码！");
        } else {
          this.$message.warning("Please enter new password.");
        }
        return false;
      }
      if (this.confirmPassword === "") {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("请再次输入新密码！");
        } else {
          this.$message.warning("Please enter the new password again.");
        }
        return false;
      }
      console.log(this.newPassword);
      console.log(this.confirmPassword);
      if (this.newPassword != this.confirmPassword) {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("两次输入的密码不一致！");
        } else {
          this.$message.warning(
            "The password entered in the first time is inconsistent with that entered in the second time."
          );
        }
        return false;
      }
      let id=localStorage.getItem('userId');
      let accountType=localStorage.getItem('accountType');
      let params={
        accountType:accountType,
        changeType:1, //修改方式：1 原密码修改 2 验证码修改
        id:id,
        newPassword:md5(this.confirmPassword),
        oldPassword:md5(this.oldPassword),
      }
      userPassword(params).then(res=>{
           if (res.success) {
            // this.$store.dispatch("LogOut").then(() => {
              window.localStorage.removeItem("name");
              window.localStorage.removeItem("userId");
              window.localStorage.removeItem("gradeId");
              window.localStorage.removeItem("capitalStatus");
              window.localStorage.removeItem("freezeStatus");
              this.$router.push({ name: "Login" });
            // });
          } else{
       
            this.$message(res.errMessage);
          }
      })
    },
  },
  
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
  }
  .content {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
  }
  .submit {
    width: 100%;
    padding: 30px 0;
    span {
      display: inline-block;
      margin: 0 10px;
      min-width: 100px;
      height: 40px;
      line-height: 40px;
      border-radius: 4px;
      padding: 0 20px;
      box-sizing: border-box;
    }
  }
}
.register-content {
  .info {
    width: 800px;
    .info-base {
      .item {
        height: 30px;
        line-height: 30px;
        & + .item {
          margin-top: 15px;
        }
        .enterLeft {
          width: 180px;
          display: block;
          font-size: 12px;
          color: @lighterBlack;
        }
        .enterInput {
          margin-left: 15px;
          width: 190px;
          height: 30px;
          .common-input {
            padding-left: 10px;
            width: 100%;
            height: 30px;
            line-height: 30px;
            border: 1px solid @bdLightColor;
          }
        }
        .prompt {
          position: absolute;
          top: 50%;
          left: 410px;
          font-size: 12px;
          color: @lighterGray;
          transform: translateY(-50%);
          &.en {
            line-height: 15px;
          }
        }
      }
    }
  }
}
</style>
