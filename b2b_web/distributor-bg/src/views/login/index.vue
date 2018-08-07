<template>
  <div class="login-container">
    <el-form autoComplete="on" :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left" label-width="0px" class="card-box login-form" v-if="!firstTime">
      <h3 class="title">B2B</h3>
      <el-form-item prop="userName">
        <span class="svg-container svg-container_login">
        <svg-icon icon-class="user" />
      </span>
        <el-input name="userName" type="text" v-model="loginForm.userName" autoComplete="on" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item prop="password">
        <span class="svg-container">
        <svg-icon icon-class="password"></svg-icon>
      </span>
        <el-input name="password" id="demo_input" :type="pwdType" @keyup.enter.native="handleLogin" v-model="loginForm.password" autoComplete="on" placeholder="请输入密码"></el-input>
        <span class="show-pwd" id="demo_img" @mousedown.left="mouseDownHold" @mouseup="mouseHoldStop" @click="passwordShow"><svg-icon icon-class="eye" /></span>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" style="width:100%;" :loading="loading" @click.native.prevent="handleLogin">
          登录
        </el-button>
      </el-form-item>
      <div class="remember">
        <el-checkbox v-model="checkbox">记住账号</el-checkbox>
      </div>
    </el-form>
    <el-form autoComplete="on" :model="passwordForm" :rules="modifyRules" ref="passwordForm" label-position="left" label-width="0px" class="card-box login-form" v-if="firstTime">
      <div class="first-time-context">
        为了您的账户安全，首次登陆需重新设置密码
      </div>
       <el-form-item prop="oldPwd">
        <span class="svg-container ">
        <svg-icon icon-class="password" />
      </span>
        <el-input name="oldPwd" :type="pwdType1" @keyup.enter.native="handleLogin" v-model="passwordForm.oldPwd" autoComplete="on" placeholder="输入原密码"></el-input>
        <span class="show-pwd" @mousedown.left="mouseDownHold" @mouseup="mouseHoldStop" @click="passwordShow1"><svg-icon icon-class="eye" /></span>
      </el-form-item>
      <el-form-item prop="newPwd">
        <span class="svg-container ">
        <svg-icon icon-class="password" />
      </span>
        <el-input name="newPwd" :type="pwdType2" @keyup.enter.native="handleLogin" v-model="passwordForm.newPwd" autoComplete="on" placeholder="设置新密码"></el-input>
        <span class="show-pwd" @mousedown.left="mouseDownHold" @mouseup="mouseHoldStop" @click="passwordShow2"><svg-icon icon-class="eye" /></span>
      </el-form-item>
      <el-form-item prop="confirmPwd">
        <span class="svg-container">
        <svg-icon icon-class="password"></svg-icon>
      </span>
        <el-input name="confirmPwd" :type="pwdType3" @keyup.enter.native="handleLogin" v-model="passwordForm.confirmPwd" autoComplete="on" placeholder="新密码确认"></el-input>
        <span class="show-pwd" @mousedown.left="mouseDownHold" @mouseup="mouseHoldStop" @click="passwordShow3"><svg-icon icon-class="eye" /></span>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" style="width:100%;" :loading="loading" @click.native.prevent="updatePwd">
          更新密码
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { isvalidUsername } from '@/utils/validate'
import md5 from 'js-md5'
import Cookies from 'js-cookie'
import store from '@/store'
import { getToken, getId } from '@/utils/auth'
import axios from 'axios'
export default {
  name: 'login',
  data() {
    // const validateUsername = (rule, value, callback) => {
    //   if (!isvalidUsername(value)) {
    //     callback(new Error('请输入正确的用户名'))
    //   } else {
    //     callback()
    //   }
    // }
    const validatePass = (rule, value, callback) => {
      if(value.length < 5) {
        callback(new Error('密码不能小于5位'))
      } else {
        callback()
      }
    }
    const validateConfirm = (rule, value, callback) => {
      if(value !== this.passwordForm.newPwd) {
        callback(new Error('两次密码不一致'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        userName: '',
        password: ''
      },
      loginRules: {
        userName: [{
          required: true,
          trigger: 'blur',
          message: '用户名不能为空'
          // validator: validateUsername
       }],
        password: [{ required: true, trigger: 'blur', validator: validatePass }]
      },
      modifyRules: {
        oldPwd: [{ required: true, trigger: 'blur', validator: validatePass }],
        newPwd: [{ required: true, trigger: 'blur', validator: validatePass }],
        confirmPwd: [{ required: true, trigger: 'blur', validator: validateConfirm }]
      },
      loading: false,
      pwdType: 'password',
      pwdType1: 'password',
      pwdType2: 'password',
      pwdType3: 'password',
      checkbox: false,
      firstTime: false,
      passwordForm: {
        oldPwd:'',
        newPwd: '',
        confirmPwd: ''
      },
      holdTime: 0,
      mouseHold: '',

    }
  },
  computed: {
    loginInfo() {
      return {
        userName: this.loginForm.userName,
        password: md5(this.loginForm.password)
      }
    },
    cookieName() {
      return this.loginForm.userName
    }
  },
  watch: {
    cookieName(val) {
      if(val) {
        if(Cookies.get(val)) {
          this.loginForm.password = Cookies.get(val)
        }
      }

    }
  },
  methods: {
    passwordShow() { // 单击密码显示
      if (this.pwdType == "password") {
        this.pwdType = "text";
      }else {
        this.pwdType = "password";
      }
    },
    passwordShow1() { // 单击密码显示（输入原密码）
      if (this.pwdType1 == "password") {
        this.pwdType1 = "text";
      }else {
        this.pwdType1 = "password";
      }
    },
    passwordShow2() { // 单击密码显示（设置新密码）
      if (this.pwdType2 == "password") {
        this.pwdType2 = "text";
      }else {
        this.pwdType2 = "password";
      }
    },
    passwordShow3() { // 单击密码显示（新密码确认）
      if (this.pwdType3 == "password") {
        this.pwdType3 = "text";
      }else {
        this.pwdType3 = "password";
      }
    },

    mouseHoldStop() {
      // clearInterval(this.mouseHold)
      // this.holdTime = 0;
      // this.pwdType = 'password'
    },
    mouseDownHold() {
      // this.mouseHold = setInterval(() => {
      //   this.holdTime++
      //     if(this.holdTime > 5) {
      //       this.pwdType = ''
      //       clearInterval(this.mouseHold)
      //     }
      // }, 100)
    },
    updatePwd() { // 首次登陆更新密码
      this.$refs.passwordForm.validate(valid => {
        if(valid) {
          this.loading = true;
          this.$api.put(this, 'admin/u/admin/password', { oldPassword: md5(this.passwordForm.oldPwd), newPassword: md5(this.passwordForm.newPwd) }).then(res => {
            if(res.code == 0) {
              this.$message({
                message: "更改成功",
                type: "success",
                // duration: 3 * 1000,
                onClose: () => {}
              })
              this.loading = false;
              Cookies.remove(this.loginForm.userName)
              this.firstTime = false // 更新完密码后返回登录页面
            }else {
              this.loading = false;
            }
          })
        } else {
          this.loading = false;
          return false
        }
      })
      setTimeout(() => { // 无法收到反馈则6S结束动画
				this.loading = false;
			}, 4000);
    },
    handleLogin() {

      this.$refs.loginForm.validate(valid => {
        if(valid) {
          let tenantHost=localStorage.getItem('tenantHost');
          if(tenantHost==undefined||tenantHost==null||tenantHost==''){
              let host = window.location.host.split(":")[0];
              let tenantNo=this.getUrlCode().tenantNo;
              let params={
                gainUrlType: 6,
                host:host,
                qryUrlType: 1,
              }
              if(tenantNo&&tenantNo!=''){
                params={...params,...{tenantNo:this.getUrlCode().tenantNo}} 
              }
              
              let tenantUrl = localStorage.getItem('tenantUrl');
              axios.get(tenantUrl + '/' +'platform/v1/web/tenant/url',{
                params:params
              }).then(res=>{
                  console.log('后台配置接口地址：',res);
                  if(res.data.success){
                    let data=res.data.data;
                    localStorage.setItem('tenantId',data.tenantId);
                    localStorage.setItem('tenantNo',data.tenantNo);
                    localStorage.setItem('tenantHost',data.host);
                    localStorage.setItem('tenantUrl',data.url);
                  }else{
                    localStorage.removeItem('tenantId');
                    localStorage.removeItem('tenantNo');
                    localStorage.removeItem('tenantHost');
                  }
              })
          }
          this.loading = true
          this.$store.dispatch('Login', this.loginInfo).then((res) => {
            if(this.checkbox) {
              Cookies.set(this.loginForm.userName, this.loginForm.password, { expires: 7 })
            }
            // if(store.getters.logintime) {
            //   this.firstTime = true;
            //   this.loading = false
            // } else {
              this.loading = false
              if (res.adminType === 1) {
                this.$router.push({ name: 'dashboard' })
              } else {
                this.$router.push({ name: 'goodslist' })
              }
            // }
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
      //获取地址栏方法
    getUrlCode() {
      let url = location.href;
       
       url=url.split('?')[1];
       console.log(url);
      /* eslint-disable */
      let theRequest = new Object();
     
      // if (url.indexOf("?") !== -1) {
        let str = url;
        console.log(str);
        let strs = str.split("&");
        for (let i = 0; i < strs.length; i++) {
          theRequest[strs[i].split("=")[0]] = strs[i].split("=")[1];
        }
        return theRequest;
      // }
    },
  }
}

</script>
<style rel="stylesheet/scss" lang="scss">
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  position: fixed;
  height: 100%;
  width: 100%;
  background-color: $bg;
  input:-webkit-autofill {
    -webkit-box-shadow: 0 0 0px 1000px #293444 inset !important;
    -webkit-text-fill-color: #fff !important;
  }
  .first-time-context {
    color: white;
    margin-bottom: 20px;
  }
  input {
    background: transparent;
    border: 0px;
    -webkit-appearance: none;
    border-radius: 0px;
    padding: 12px 5px 12px 15px;
    color: $light_gray;
    height: 47px;
  }
  .remember {
    float: right;
    margin-bottom: 20px;
  }
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;
  }
  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;
  }
  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
    &_login {
      font-size: 20px;
    }
  }
  .title {
    font-size: 26px;
    font-weight: 400;
    color: $light_gray;
    margin: 0px auto 40px auto;
    text-align: center;
    font-weight: bold;
  }
  .login-form {
    position: absolute;
    left: 0;
    right: 0;
    width: 400px;
    padding: 35px 35px 15px 35px;
    margin: 120px auto;
  }
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
  .thirdparty-button {
    position: absolute;
    right: 35px;
    bottom: 28px;
  }
}

</style>
