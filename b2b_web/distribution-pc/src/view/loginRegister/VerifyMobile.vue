<template>
  <div>
    <div class="register-head rl-bg-white rl-margin-bottom-xxxs">
      <div class="login-head rl-padding-horizontal-lllg rl-padding-top-lllg rl-padding-bottom-default rl-clear">
        <div class="logo rl-fl">
          <router-link to="/Index">
            <img class="logo-img" src="../../assets/images/index/logo.png" alt="BAT商城" />
          </router-link>
        </div>
        <!-- 施义煌 - 语言切换 -->
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
        <div class="rl-fr logo-right rl-text-default rl-margin-right-default rl-margin-top-xxxs">
          <span class="rl-text-gray">{{$t('Register.AlreadyAccount')}}</span>
          <span class="rl-text-orange-sm cursor-pointer" @click="$router.push({name: 'Login', params: {key: '-1'}})">
            {{$t('P.Login')}}
            <i class="el-icon-d-arrow-right"></i>
          </span>
        </div>
      </div>
    </div>
    <div class="register-phone rl-bg-white">
      <div class="yuan rl-clear rl-margin-zero">
        <div class="all rl-relative rl-fl">
          <div class="num-spe">1</div>
          <div class="word-spe rl-tc">{{$t('Register.Step.One')}}</div>
          <div class="img"></div>
        </div>
        <div class="all rl-relative rl-fl">
          <div class="num">2</div>
          <div class="word rl-tc">{{$t('Register.Step.Two')}}</div>
          <div class="img"></div>
        </div>
       <!-- <div class="all rl-relative rl-fl">
          <div class="num">3</div>
          <div class="word rl-tc">{{$t('Register.Step.Three')}}</div>
          <div class="img"></div>
        </div>-->
        <div class="all rl-relative rl-fl">
          <div class="num">3</div>
          <div class="word rl-tc">{{$t('Register.Step.Four')}}</div>
        </div>
      </div>
      <div class="infos rl-margin-zero">
        <form>
          <div class="item rl-clear rl-relative" v-if="$i18n.locale === 'zh'">
            <div class="box-left rl-fl rl-tc">
              <span>中国 0086</span>
            </div>
            <div class="box-right rl-fl">
              <input v-model="mobile"
                     class="rl-padding-left-default"
                     type="text"
                     placeholder="请输入手机号"
                     onkeyup="this.value=this.value.replace(/\D/g,'')">
            </div>
          </div>
          <div class="item rl-clear rl-relative" v-else>
            <div class="box-left rl-fl rl-tc">
              <span>E-mail</span>
            </div>
            <div class="box-right rl-fl">
              <input v-model="emails"
                     class="rl-padding-left-default"
                     type="text"
                     placeholder="Enter E-mail Address">
            </div>
          </div>
          <div class="item rl-clear rl-margin-top-lllg  rl-relative">
            <div class="box-right rl-fl">
              <input v-model="authCode"
                     class="rl-padding-left-default"
                     type="text"
                     :placeholder="$t('Register.EnterPhoneCode')"
                     onkeyup="this.value=this.value.replace(/\D/g,'')">
            </div>
            <div @click="sendCode"
                 class="box-left rl-fl rl-tc cursor-pointer rl-text-white"
                 :class="{'rl-bg-blue-xs':sendCodes === false}"
                 v-if="this.sendCodes === false">
              <span>{{btnSendCodeTitle}}</span>
            </div>
            <div class="box-left rl-fl rl-tc cursor-pointer rl-text-white"
                 :class="{'rl-bg-gray-sm':sendCodes === true}"
                 v-if="this.sendCodes === true">
              <span class="rl-text-xxs">{{btnSendCodeTitle}}</span>
            </div>
            <p class="prompt rl-text-xxs rl-text-orange-sm" v-show="code === false">验证码已过期或错误，请重新获取验证码</p>
          </div>
          <div @click="nextStep" class="next-step rl-tc cursor-pointer rl-bg-blue-xs rl-text-white">{{$t('P.Next')}}</div>
        </form>
      </div>
    </div>
    <!--验证手机号弹框-->
    <div class="cover" v-if="showcov === true"></div>
    <div class="pro-cover cover-box rl-padding-bottom-lllg rl-relative" v-if="showcov === true">
      <div class="img rl-margin-zero">
        <img src="../../assets/images/phone-shut.png" alt="">
      </div>
      <div class="words">手机号{{mobile}}已注册了账号，您可直接使用该手机号登录，或更换手机号继续注册.</div>
      <div class="address-info rl-bg-white rl-padding-left-default rl-clear rl-margin-zero">
        <div @click="goLogin" class="rl-fl rl-tc all rl-bg-gray-mm cursor-pointer rl-margin-top-default rl-text-gray">登录该账号</div>
        <div @click="shutLog" class="rl-fl rl-tc all right rl-margin-left-default cursor-pointer rl-margin-top-default rl-bg-blue-mm rl-text-white">继续注册</div>
      </div>
    </div>
    <!--注册协议弹框-->
    <RegisterDialog :showOpen="showOpen" @close="agreeGoon"></RegisterDialog>
  </div>
</template>

<script>
  import GD from '@/assets/js/globalData'
  import RegisterDialog from '@/components/RegisterDialog'
  import cutFilmMachineDialog from '@/components/cutFilmMachineDialog'
  import proCustomizeDialog from '@/components/proCustomizeDialog'
  import { phoneVerifyApi} from '@/apiService/api'
  export default {
    name: 'VerifyMobile',
    data () {
      return {
        mobile: '',
        emails: '',
        code: true,
        sendCodes: false,
        btnSendCodeTitle: this.$t('Register.GetVerificationCode'),
        authCode: '',
        showcov: false, // 是否显示弹框
        showOpen: true,
        useLang: false, // 是否启用多语种 - 施义煌
        langList: GD.langList, // 语种列表 - 施义煌
        lang: 'zh-CNY' // 语种 - 施义煌
      }
    },
    created () {
      // 施义煌
      this.useLang = this.$b2bConfig.lang;
      this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh_CNY';
    },
    methods: {
      /**
       * 施义煌
       * 更换语种
       */
      fLangChange (value) {
        window.localStorage.setItem('bLang', value);
        this.$i18n.locale = value.split('_')[0];
        this.btnSendCodeTitle = this.$t('Register.GetVerificationCode')
      },
      shutLog () {
        this.showcov = false
      },
      goBack () {
        this.$router.go(-1)
      },
      agreeGoon () {
        this.showOpen = false
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
        this.sendCodes = true
        phoneVerifyApi('POST',{codeType:1,phone:code}).then(res=>{
            if (res.success) {
              if (this.$i18n.locale === 'zh') {
                this.$message.success('短信已发送，请注意查收！')
                this.btnSendCodeTitle = '等待 60 秒'
              } else {
                this.$message.success('Message has been sent, please check it.')
                this.btnSendCodeTitle = 'Waiting for 60 S'
              }
              var secondsArea = 60
              this.intervalid = setInterval(() => {
                secondsArea--
                if (this.$i18n.locale === 'zh') {
                  this.btnSendCodeTitle = '等待' + secondsArea + '秒'
                } else { this.btnSendCodeTitle = 'Waiting for ' + secondsArea + ' S' }
                if (secondsArea === 0) {
                  if (this.$i18n.locale === 'zh') {
                    this.btnSendCodeTitle = '获取验证码'
                  } else {
                    this.btnSendCodeTitle = 'Get verification code'
                  }
                  this.sendCodes = false
                  clearInterval(this.intervalid)
                }
              }, 1000)
            } 
            // else if (res.code === 903) {
            //   this.showcov = true
            // } 
            else { 
              this.sendCodes = false;
              this.$message(res.errMessage);
            }
        })
       
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
          } else { this.$message.warning('Please enter the verification code.') }
          return false
        }
        phoneVerifyApi('PUT',{codeType:1,phone:code,code:this.authCode}).then(res=>{
            if (res.success) {
              if (this.$i18n.locale === 'zh') {
                this.$message.success('短信验证码验证成功！')
              } else { this.$message.warning('Verify message verification code successfully！') }
              window.localStorage.setItem('mobile', code)
              this.$router.push({name: 'Register', params: {mobile: code}})
              clearInterval(this.intervalid)
            } else {
              this.code = false
            }
        })

        
      },
      goLogin () {
        this.$router.push({name: 'Login'})
      }
    },
    mounted () {
      window.localStorage.removeItem('times')
    },
    components: {
      RegisterDialog,
      cutFilmMachineDialog,
      proCustomizeDialog
    }
  }
</script>

<style scoped="scoped" lang='less'>
  .yuan{
    padding-left: 20px;
    padding-top: 80px;
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
      .words{
      }
    }
  }
  .register-phone{
    padding-bottom: 300px;
    width: 100%;
    .infos{
      padding-top: 80px;
      width: 470px;
      .item{
        .box-left{
          margin-left: -1px;
          width: 148px;
          height: 45px;
          line-height: 45px;
          border: 1px solid #ccc;
        }
        .box-right{
          margin-left: -1px;
          width: 318px;
          height: 45px;
          border: 1px solid #ccc;
          input{
            padding-top: 13px;
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
  /*弹框*/
  .cover{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: #000;
    z-index: 99;
    opacity: 0.5;
  }
  .pro-cover{
    padding-top: 30px;
    width: 470px;
    height: 260px;
    border: 1px solid #ccc;
    border-radius: 5px;
    z-index: 99;
    background: #fefefe;
    .img{width: 80px}
    .words{
      padding-top: 20px;
      padding-bottom: 10px;
      margin: 0 auto;
      width: 285px;
      text-align: center;
    }
  }
  .prote-cover{
    width: 845px;
    height: 705px;
    border: 1px solid #ccc;
    border-radius: 5px;
    z-index: 99;
    background: #fefefe;
    box-shadow: 0 1px 10px 5px rgba(0,0,0,0.3);
  }
  .cover-box{
    box-sizing: border-box;
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    margin: auto;
    z-index: 99;
    .shut{
      position: absolute;
      top:-8px;
      right:-8px;
      display: block;
      width: 18px;
      height: 18px;
      background: url("../../assets/images/shut.png") no-repeat center center;
    }
    .address-info{
      width: 250px;
      .all{
        width: 100px;
        height: 30px;
        line-height: 30px;
        border-radius: 5px;
      }
      .right{
        height: 28px;
        line-height: 28px;
        border: 1px solid #ccc
      }
    }
    .address-infos{
      .pop-box{
        padding-left: 20px;
        width: 775px;
        .box{
          width: 775px;
          height: 470px;
          overflow-y: auto;
          scrollbar-width: none;
          -ms-overflow-style: none;
          &::-webkit-scrollbar {
            display: none;
          }
          .box-word{
            h1{
              margin: 30px 0 10px;
              line-height:2;
            }
            p{
              margin-bottom: 14px;
              line-height: 1.8;
              text-indent: 28px;
              text-align: justify;
            }
          }
        }
        .box-foot{
          width: 775px;
          span{
            display: inline-block;
            width: 140px;
            height: 40px;
            line-height: 40px;
          }
        }
      }
    }
  }
  .lang-box{
    display: inline-block;
    width: 140px;
    margin-left: 20px;
  }

  // logo
  .logo-img {
    width: 177px;
  }
</style>
