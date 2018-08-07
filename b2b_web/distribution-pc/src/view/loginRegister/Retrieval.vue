<template>
  <div>
    <div class="index rl-margin-zero">
      <div class="register-head rl-bg-white rl-margin-bottom-xxxs">
        <div class="login-head rl-padding-horizontal-lllg rl-padding-top-lllg rl-padding-bottom-default rl-clear">
          <div class="logo rl-fl">
            <router-link to="/Index">
              <img class="logo-img" src="../../assets/images/index/logo.png" alt="BAT商城" />
            </router-link>
          </div>
          <div class="lang-box" v-if="useLang">
            <el-select v-model="lang"
                       @change="fLangChange"
                       :placeholder="$t('P.Select')">
              <el-option
                v-for="item in langList"
                :key="item.id"
                :label="item.name"
                :value="item.value">
              </el-option>
            </el-select>
          </div>
<!--          <div class="rl-fl words rl-text-lg rl-margin-left-lllg rl-text-gray rl-margin-top-xxxs" v-if="this.times !== 1">欢迎注册</div>-->
<!--          <div class="rl-fl word-spe rl-text-lg rl-margin-top-xxxs" v-if="this.times === 1">首次登录需进行信息修改</div>-->
          <div class="rl-fr logo-right rl-text-default rl-margin-right-default rl-margin-top-xxxs">
            <span class="rl-text-gray">{{$t('Register.AlreadyAccount')}}</span>
            <span @click="$router.push({name: 'Login', params: {key: '-1'}})" class="rl-text-orange-sm cursor-pointer">{{$t('P.Login')}}</span>
          </div>
        </div>
      </div>
      <!--主内容-->
      <div class="main rl-margin-zero rl-padding-top-default rl-bg-white">
        <div class="code-head rl-margin-zero rl-tc rl-text-default rl-padding-top-double rl-padding-bottom-double">
          <span v-if="$i18n.locale === 'zh'">找回密码</span>
          <span v-else>Reset your Forgotten Password</span>
        </div>
        <div class="inner rl-margin-zero">
          <form action="">
            <div class="item rl-clear rl-relative" v-if="$i18n.locale === 'zh'">
              <div class="box-left rl-fl rl-tc"><span>手机号</span></div>
              <div class="box-right rl-fl"><input v-model="mobile" class="rl-padding-left-default" type="text" placeholder="请输入注册手机号" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
              <!--<p class="prompt rl-text-xxss rl-text-red-sm">格式错误</p>-->
              <p class="prompt rl-text-xxs rl-text-orange-sm" v-show="mobileCode === false">请输入手机号</p>
            </div>
            <div class="item rl-clear rl-relative" v-if="$i18n.locale === 'en'">
              <div class="box-left rl-fl rl-tc"><span>E-mail</span></div>
              <div class="box-right rl-fl"><input v-model="emails" class="rl-padding-left-default" type="text" placeholder="Enter Email adress"></div>
              <!--<p class="prompt rl-text-xxss rl-text-red-sm">格式错误</p>-->
              <p class="prompt rl-text-xxs rl-text-orange-sm" v-show="mobileCode === false">Please enter your email address</p>
            </div>
            <div class="item rl-clear rl-margin-top-lllg  rl-relative">
              <div class="box-right rl-fl"><input v-model="authCode" class="rl-padding-left-default" type="text" :placeholder="$t('Register.EnterPhoneCode')" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
              <div @click="sendCode" class="box-left rl-fl rl-tc rl-bg-blue-xs rl-text-white cursor-pointer"><span>{{btnSendCodeTitle}}</span></div>
              <p class="prompt rl-text-xxs rl-text-orange-sm" v-show="code === false">
                <i v-if="$i18n.locale === 'zh'">验证码已过期或错误，请重新获取验证码</i>
                <i v-else>Please enter the right verification code</i>
              </p>
            </div>
            <!-- 账号类型 -->
            <div class="item rl-clear rl-margin-top-lllg rl-relative" v-if="hasClickVerify">
              <el-radio-group  v-model="ownerFlag" class="Am-el-radio" @change="handleChangeAccount">
                <el-radio :label="1" value="1">{{ $i18n.locale === "zh" ? "主账号" : "Primary account" }}</el-radio>
                <el-radio :label="0" value="0">{{ $i18n.locale === "zh" ? "子账号" : "Subsidiary account" }}</el-radio>
              </el-radio-group>
            </div>
            <div @click="nextStep" class="next-step rl-tc cursor-pointer rl-bg-blue-xs rl-text-white"><span v-if="$i18n.locale === 'zh'">下一步</span><span v-else>Continue</span></div>
          </form>
        </div>
      </div>
      <!--公共底部-->
      <Footer></Footer>
    </div>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue'
import GD from '@/assets/js/globalData'
// liu
import { phoneVerifyApi, contactApi} from '@/apiService/api'
export default {
  name: 'Retrieval',
  components: {
    Footer
  },
  data () {
    return {
      mobile: '',
      emails: '',
      mobileCode: true,
      code: true,
      btnSendCodeTitle: this.$t('Register.GetVerificationCode'),
      authCode: '',
      useLang: false, // 是否启用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-CNY', // 语种
      ownerFlag: 1, // 是否主账号: 1 是/0 否
      hasClickVerify: false, // 是否点击获取验证码
    }
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('_')[0];
      this.btnSendCodeTitle = this.$t('Register.GetVerificationCode')
    },
    // 获取验证码
    sendCode () {
      let code = ''
      if (this.$i18n.locale === 'zh') {
        if (this.btnSendCodeTitle !== '获取验证码') {
          return false
        }
        var pattern = /^1[3456789]\d{9}$/
        code = this.mobile
        if (this.mobile === '') {
          this.mobileCode = false
          this.$message.warning('请输入的手机号！')
          return false
        }
        if (this.mobile.length !== 11) {
          this.$message.warning('请输入正确的手机号！')
          return false
        }
        if (!pattern.test(this.mobile)) {
          this.$message.warning('请输入正确的手机号！')
          return false
        }
      } else {
        if (this.btnSendCodeTitle !== 'Get verification code') {
          return false
        }
        code = this.emails
        if (this.emails === '') {
          this.$message.warning('Please enter e-mail Address.')
          return false
        }
      }
     
      phoneVerifyApi('POST',{codeType:2,phone:code}).then(res => { 
        if (res.success) {
          if (this.$i18n.locale === 'zh') {
            this.$message.success('短信已发送，请注意查收！')
            this.btnSendCodeTitle = '等待 60 秒'
          } else {
            this.$message.success('Message has been sent, please check it.')
            this.btnSendCodeTitle = 'Waiting for 60 seconds'
          }
          var secondsArea = 60
          this.intervalid = setInterval(() => {
            secondsArea--
            if (this.$i18n.locale === 'zh') {
              this.btnSendCodeTitle = '等待' + secondsArea + '秒'
            } else { this.btnSendCodeTitle = 'Waiting for ' + secondsArea + ' seconds' }
            if (secondsArea === 0) {
              if (this.$i18n.locale === 'zh') {
                this.btnSendCodeTitle = '获取验证码'
              } else {
                this.btnSendCodeTitle = 'Get verification code'
              }
              clearInterval(this.intervalid)
            }
          }, 1000)
          this.getDistriInfo(this.mobile, this.emails)
        }
      })
    },
    // 获取账号类型
    getDistriInfo(phone, emails) {
      contactApi("GET", {
        phone: phone ? phone : undefined,
        emails: emails ? emails : undefined
      }).then(res=>{
        if(res.success){
          // 判断是否是主账号
          if (res.data.ownerFlag) {
            // 是，显示选择主账号/子账号
            this.hasClickVerify = true;
            localStorage.setItem("accountType", 1);
          } else {
            // 否，子账号
            this.hasClickVerify = false;
            localStorage.setItem("accountType", 2);
          }
        } else {
          that.$message.error(res.errMessage);
        }
      })
    },
    // 账号类型切换
    handleChangeAccount(val) {
      if (val === 1) {
        localStorage.setItem("accountType", 1);
      } else {
        localStorage.setItem("accountType", 2);
      }
    },
    nextStep () {
      let code = ''
      if (this.$i18n.locale === 'zh') {
        if (this.mobile === '') {
          this.mobileCode = false
          this.$message.warning('请输入的手机号！')
          return false
        }
        code = this.mobile
        var pattern = /^1[3456789]\d{9}$/
        if (this.mobile.length !== 11) {
          this.$message.warning('请输入正确的手机号！')
          return false
        }
        if (!pattern.test(this.mobile)) {
          this.$message.warning('请输入正确的手机号！')
          return false
        }
      } else {
        if (this.emails === '') {
          this.$message.warning('Please enter e-mail Address.')
          return false
        }
        code = this.emails
      }
      if (this.authCode === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入手机验证码！')
        } else { this.$message.warning('Please enter the mobile verification code！') }
        return false
      }
      phoneVerifyApi('PUT',{codeType:2,phone:code,code:this.authCode}).then(res => {
        if (res.success) {
          if (this.$i18n.locale === 'zh') {
            this.$message.success('短信验证码验证成功！')
          } else { this.$message.warning('Verify message verification code successfully！') }
          window.localStorage.setItem('authCode', this.authCode)
          window.localStorage.setItem('retMobile', code)
          this.$router.push({name: 'ResetPassword', params: {authCode: this.authCode, mobile: code}})
        }else{
          this.$message(res.errMessage);
        }
      })
    }
  },
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh_CNY';
  }
}
</script>

<style scoped="scoped" lang='less'>
  .index{
    width: 100%;
  }
  .lang-box{
    display: inline-block;
    width: 140px;
    margin-left: 20px;
  }
  .register-head{
    width: 100%;
    .login-head{
      width: 1190px;
      margin: 0 auto;
      .logo{
        display:flex;
        align-items: center;
        img{
          border:0px;
        }
      }
      .word-spe{
        margin-left: 220px;
      }
    }
  }
  .main {
    .code-head{
      width: 100%;
    }
    .inner{
      padding-bottom: 80px;
      width: 470px;
      .item{
        .box-left{
          width: 148px;
          height: 45px;
          line-height: 45px;
          border: 1px solid #ccc;
        }
        .box-right{
          width: 318px;
          height: 45px;
          line-height: 45px;
          border: 1px solid #ccc;
          input{
            width: 298px;
          }
        }
        .prompt{
          position: absolute;
          left: 10px;
          bottom:-20px;
        }
      }
      .next-step{
        margin-top: 60px;
        width: 100%;
        height: 45px;
        line-height: 45px;
        border: 1px solid #ccc;
        letter-spacing: 2px;
      }
    }
  }

  .logo-img {
    width: 117px;
  }
</style>
