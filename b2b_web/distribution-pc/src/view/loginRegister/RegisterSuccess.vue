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
<!--        <div class="rl-fl words rl-text-lg rl-margin-left-lllg rl-text-gray rl-margin-top-xxxs" v-if="this.times !== 1">欢迎注册</div>-->
<!--        <div class="rl-fl word-spe rl-text-lg rl-margin-top-xxxs" v-if="this.times === 1">首次登录需进行信息修改</div>-->
        <div class="rl-fr logo-right rl-text-default rl-margin-right-default rl-margin-top-xxxs" v-if="this.times !== 1">
          <span class="rl-text-gray">{{$t('Register.AlreadyAccount')}}</span>
          <span @click="$router.push({name: 'Login', params: {key: '-1'}})" class="rl-text-orange-sm cursor-pointer">{{$t('P.Login')}}》</span>
        </div>
      </div>
    </div>
    <div class="success rl-padding-top-lllg">
      <div class="yuan rl-clear rl-margin-zero  rl-bg-white">
        <div class="all rl-relative rl-fl">
          <div class="num-spe">1</div>
          <div class="word-spe rl-tc" v-if="this.times === 1">{{$t('Register.Step.OneOther')}}</div>
          <div class="word-spe rl-tc" v-else>{{$t('Register.Step.One')}}</div>
          <div class="img-spe"></div>
        </div>
        <div class="all rl-relative rl-fl">
          <div class="num-spe">2</div>
          <div class="word-spe rl-tc">{{$t('Register.Step.Two')}}</div>
          <div class="img-spe"></div>
        </div>
        <!--<div class="all rl-relative rl-fl">
          <div class="num-spe">3</div>
          <div class="word-spe rl-tc">{{$t('Register.Step.Three')}}</div>
          <div class="img-spe"></div>
        </div>-->
        <div class="all rl-relative rl-fl">
          <div class="num-spe">3</div>
          <div class="word-spe rl-tc" v-if="this.times === 1">{{$t('Register.Step.FourOther')}}</div>
          <div class="word-spe rl-tc" v-else>{{$t('Register.Step.Four')}}</div>
        </div>
      </div>
      <div class="success" v-show="$i18n.locale === 'zh'">
        <div class="content rl-bg-white rl-margin-zero rl-text-gray" v-if="this.times === 1">
          <div class="rl-margin-bottom-default">您需要重新登录，重新登录成功后，即可正常下单购买</div>
          <div>页面将在<span class="rl-text-orange-sm rl-text-sm">{{seconds}}</span>秒后自动跳转回登录页面>></div>
        </div>
        <div class="content rl-bg-white rl-margin-zero rl-text-gray" v-else>
          <div class="rl-margin-bottom-default">您已注册成功！正在等待后台审核，请您耐心等待。</div>
          <div class="rl-margin-bottom-default">审核通过后您将成为本站会员，可以立即正常登录下单。审核未通过时，您无法下单。</div>
          <div>页面将在<span class="rl-text-orange-sm rl-text-sm">{{seconds}}</span>秒后自动跳转回首页>></div>
        </div>
      </div>
      <div class="success" v-show="$i18n.locale === 'en'">
        <div class="content rl-bg-white rl-margin-zero rl-text-gray" v-if="this.times === 1">
          <div class="rl-margin-bottom-default">You need to log in again. After successful login again, you can order and purchase normally.</div>
          <div>The page will automatically jump back to the login page in <span class="rl-text-orange-sm rl-text-sm">{{seconds}}</span> seconds>></div>
        </div>
        <div class="content rl-bg-white rl-margin-zero rl-text-gray" v-else>
          <div class="rl-margin-bottom-default">You have registered successfully! Waiting for background review. Please wait patiently</div>
          <div class="rl-margin-bottom-default">After the review, you will become a member of the site and can normally log in and place an order immediately. In case of review failure, you cannot place an order.</div>
          <div>The page will automatically jump back to the login page in <span class="rl-text-orange-sm rl-text-sm">{{seconds}}</span> seconds>></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import GD from '@/assets/js/globalData'
export default {
  name: 'RegisterSuccess',
  data () {
    return {
      times: 0,
      seconds: 5,
      type: true,
      useLang: false, // 是否启用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-CNY' // 语种
    }
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('_')[0];
    },
    goLogin () {
      this.type = false
      if (this.times === 1) {
        this.$store.dispatch('LogOut').then(() => {
          window.localStorage.removeItem('name')
          window.localStorage.removeItem('userId')
          window.localStorage.removeItem('gradeId')
          window.localStorage.removeItem('capitalStatus')
          window.localStorage.removeItem('freezeStatus')
          this.$router.push({name: 'Login'})
        })
      } else {
        this.$router.push({name: 'Login'})
      }
    },
    code () {
      var secondsArea = 5
      this.intervalid = setInterval(() => {
        secondsArea--
        this.seconds = secondsArea
        if (this.type === true) {
          if (secondsArea === 0) {
            this.seconds = secondsArea
            if (this.times === 1) {
              this.$store.dispatch('LogOut').then(() => {
                window.localStorage.removeItem('name')
                window.localStorage.removeItem('userId')
                window.localStorage.removeItem('gradeId')
                window.localStorage.removeItem('capitalStatus')
                window.localStorage.removeItem('freezeStatus')
                window.localStorage.removeItem('times')
                this.$router.push({name: 'Login'})
              })
            } else {
              this.$router.push({name: 'Index'})
            }
            clearInterval(this.intervalid)
          }
        }
      }, 1000)
    }
  },
  created () {
    // 施义煌
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh_CNY';
    var time = window.localStorage.getItem('times')
    this.times = Number(time)
    this.code()
  }
}
</script>

<style scoped="scoped" lang='less'>
  .yuan{
    padding-left: 80px;
    padding-top: 80px;
    width: 705px;
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
      }
      .logo-right{

      }
      .word-spe{
        margin-left: 220px;
      }
    }
  }
  .success{
    width: 100%;
    .content{
      padding-left: 100px;
      padding-top: 90px;
      width: 685px;
      height: 210px;
    }
  }

  // logo
  .logo-img {
    width: 177px;
  }
</style>
