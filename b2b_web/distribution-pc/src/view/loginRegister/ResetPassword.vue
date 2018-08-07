<template>
  <div>
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
        <div class="rl-fr logo-right rl-text-default rl-margin-right-default rl-margin-top-xxxs" v-if="this.times !== 1">
          <span class="rl-text-gray">{{$t('Register.AlreadyAccount')}}</span>
          <span @click="$router.push({name: 'Login', params: {key: '-1'}})" class="rl-text-orange-sm cursor-pointer">{{$t('P.Login')}}</span>
        </div>
      </div>
    </div>
    <div class="index rl-margin-zero">
      <!--主内容-->
      <div class="main rl-margin-zero rl-padding-top-default rl-bg-white">
        <div class="yuan rl-clear rl-margin-zero">
          <div class="all rl-relative rl-fl">
            <div class="num-spe">1</div>
            <div class="word-spe rl-tc">{{$t('Register.Step.OneOther')}}</div>
            <div class="img"></div>
          </div>
          <div class="all rl-relative rl-fl">
            <div class="num">2</div>
            <div class="word rl-tc">{{$t('Register.Step.Two')}}</div>
            <div class="img"></div>
          </div>
          <div class="all rl-relative rl-fl">
            <div class="num">3</div>
            <div class="word rl-tc">{{$t('Register.Step.Three')}}</div>
            <div class="img"></div>
          </div>
          <div class="all rl-relative rl-fl">
            <div class="num">4</div>
            <div class="word rl-tc">{{$t('Register.Step.FourOther')}}</div>
          </div>
        </div>
        <div class="code-head rl-margin-zero rl-tc rl-text-default rl-padding-top-double rl-padding-bottom-double" v-if="this.times !== 1">{{$t('Register.Step.OneOther')}}</div>
        <div class="inner rl-margin-zero">
          <form action="">
            <div class="item rl-clear rl-relative">
              <div class="box-left rl-fl rl-tc"><span>{{$t('UserCenter.NewPassword')}}</span></div>
              <div class="box-right rl-fl"><input @blur="schoolOne" v-model="password" class="rl-padding-left-default" maxlength="16" type="password" :placeholder="$t('P.PleaseEnter')"></div>
              <!--<p class="prompt rl-text-xxss rl-text-red-sm">格式错误</p>-->
              <p class="prompt rl-text-xxs rl-text-orange-sm" v-show="$i18n.locale === 'zh'">密码长度为6-16个字符，可为英文、数字组合，区分大小写</p>
              <p class="prompt rl-text-xxs rl-text-orange-sm" v-show="$i18n.locale === 'en'">The password is composed of 6-16 characters, and is case-sensitive</p>
            </div>
            <div class="item rl-clear rl-margin-top-lllg  rl-relative">
              <div class="box-left rl-fl rl-tc cursor-pointer"><span>{{$t('UserCenter.ConfirmPassword')}}</span></div>
              <div class="box-right rl-fl"><input @blur="schoolTwo" v-model="newPassword" class="rl-padding-left-default" maxlength="16"  type="password" :placeholder="$t('P.PleaseEnter')"></div>
              <p class="prompt rl-text-xxs rl-text-orange-sm" v-show="$i18n.locale === 'zh'">请再次输入密码，确认密码必须和密码一致</p>
              <p class="prompt rl-text-xxs rl-text-orange-sm" v-show="$i18n.locale === 'en'">Please enter password again to confirm that the password must be identical.</p>
            </div>
            <div @click="submitSend" class="next-step rl-tc cursor-pointer" v-if="this.times !== 1">{{$t('P.OK')}}</div>
            <div @click="nextStep" class="next-step rl-tc cursor-pointer" v-if="this.times === 1">
              <span v-if="$i18n.locale === 'zh'">下一步</span><span v-else>Continue</span>
            </div>
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
import Header from '@/components/Header.vue'
import md5 from 'js-md5'
import GD from '@/assets/js/globalData'
import { userPassword} from '@/apiService/api'
export default {
  name: 'ResetPassword',
  components: {
    Header,
    Footer
  },
  data () {
    return {
      times: 0,
      mobile: '',
      authCode: '',
      password: '',
      newPassword: '',
      useLang: false, // 是否启用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-CNY' // 语种
    }
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('_')[0];
      this.btnSendCodeTitle = this.$t('Register.GetVerificationCode')
    },
    schoolOne () {
      var reg = /^[A-Za-z0-9]+$/
      if (!reg.test(this.password)) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('密码长度为6-16个字符，可为英文、数字组合，区分大小写！')
        } else { this.$message.warning('The password is composed of 6-16 characters, and is case-sensitive.') }
        this.password = ''
      } else if (this.password.length < 6 || this.password.length > 16) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('密码长度为6-16个字符，可为英文、数字组合，区分大小写！')
        } else { this.$message.warning('The password is composed of 6-16 characters, and is case-sensitive.') }
        this.password = ''
      }
    },
    schoolTwo () {
      var reg = /^[A-Za-z0-9]+$/
      if (!reg.test(this.newPassword)) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('密码长度为6-16个字符，可为英文、数字组合，区分大小写！')
        } else { this.$message.warning('The password is composed of 6-16 characters, and is case-sensitive.') }
        this.newPassword = ''
      } else if (this.newPassword.length < 6 || this.newPassword.length > 16) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('密码长度为6-16个字符，可为英文、数字组合，区分大小写！')
        } else { this.$message.warning('The password is composed of 6-16 characters, and is case-sensitive.') }
        this.newPassword = ''
      }
    },
    nextStep () {
      if (this.password === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入新密码！')
        } else { this.$message.warning('Please enter new password.') }
        return false
      }
      if (this.newPassword === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请再次输入新密码！')
        } else { this.$message.warning('Please enter the new password again.') }
        return false
      }
      if (this.password !== this.newPassword) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('两次输入的密码不一致！')
        } else { this.$message.warning('The password entered in the first time is inconsistent with that entered in the second time.') }
        return false
      }
      window.localStorage.setItem('password', this.password)
      this.$router.push({name: 'Register', params: {password: this.password}})
    },
    submitSend () {
      if (this.password === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入新密码！')
        } else { this.$message.warning('Please enter new password.') }
        return false
      }
      if (this.newPassword === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请再次输入新密码！')
        } else { this.$message.warning('Please enter the new password again.') }
        return false
      }
      if (this.password !== this.newPassword) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('两次输入的密码不一致！')
        } else { this.$message.warning('The password entered in the first time is inconsistent with that entered in the second time.') }
        return false
      }
      let id=localStorage.getItem('userId');
      let accountType=localStorage.getItem('accountType');
      let params={
        accountType:accountType,
        changeType:2,
        // id:id,
        codeType:2,
        code:this.$route.params.authCode,
        phone:this.$route.params.mobile,
        newPassword:md5(this.newPassword),
      }
      userPassword(params).then(res => {
        if (res.success) {
          if (this.times === 1) {
            this.$router.push({name: 'Register'})
          } else {
            window.localStorage.removeItem('retMobile')
            window.localStorage.removeItem('authCode')
            if (this.$i18n.locale === 'zh') {
              this.$message.success('密码重置成功！')
            } else { this.$message.success('Reset the password successfully.') }
            this.$router.push({name: 'Login'})
          }
        }
      })
    }
  },
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh_CNY';
  },
  mounted () {
    var time = window.localStorage.getItem('times')
    this.times = Number(time)
    if (this.times !== 1) {
      var biles = window.localStorage.getItem('retMobile')
      var auth = window.localStorage.getItem('authCode')
      this.mobile = biles
      this.authCode = auth
    }
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
  .yuan{
    padding-left: 20px;
    padding-top: 40px;
    padding-bottom: 40px;
    width: 600px;
    .all{
      width: 120px;
      margin-right: 30px;
      .num{
        margin: 0 auto;
        width: 20px;
        height: 20px;
        line-height: 20px;
        text-align: center;
        border: 1px solid #C9C9C9;
        border-radius: 50%;
        color: #C9C9C9;
        font-size: 14px;
      }
      .word{
        margin-top: 5px;
        font-size: 14px;
        color: #C9C9C9;
      }
      .img{
        position: absolute;
        top: 8px;
        left: 85px;
        width: 95px;
        height: 6px;
        background: url("../../assets/images/step/bg-yuan.png") center center no-repeat;
      }
      .num-spe{
        margin: 0 auto;
        width: 20px;
        height: 20px;
        line-height: 20px;
        text-align: center;
        background-color: #00c9dc;
        border-radius: 50%;
        color: #fff;
        font-size: 14px;
      }
      .word-spe{
        margin-top: 5px;
        font-size: 14px;
        color: #00c9dc;
      }
      .img-spe{
        position: absolute;
        top: 8px;
        left: 85px;
        width: 95px;
        height: 6px;
        background: url("../../assets/images/step/bg-yuan1.png") center center no-repeat;
      }
    }
  }
  .main {
    .code-head{
      width: 100%;
    }
    .inner{
      padding-bottom: 80px;
      width: 600px;
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
          border: 1px solid #ccc;
          input{
            width: 298px;
            line-height: 45px;
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
