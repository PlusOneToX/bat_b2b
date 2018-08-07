<template>
  <div>
    <div class="login rl-margin-zero rl-bg-white">
      <div
        class="login-head rl-padding-horizontal-lllg rl-padding-top-lllg rl-padding-bottom-default rl-clear"
      >
        <div class="logo rl-fl">
          <router-link to="/Index">
            <img
              class="logo-img"
              src="../../assets/images/index/logo.png"
            />
          </router-link>
          <div class="lang-box" v-if="useLang">
            <el-select v-model="lang" @change="fLangChange" :placeholder="$t('P.Select')">
              <el-option
                v-for="item in langList"
                :key="item.id"
                :label="item.name"
                :value="item.value"
              ></el-option>
            </el-select>
          </div>
        </div>
        <!--<div class="rl-fl words rl-text-lg rl-margin-left-lllg rl-text-gray">欢迎登录</div>-->
      </div>
      <div class="main">
        <div class="content rl-margin-zero">
          <div class="login-enroll rl-fr rl-bg-white">
            <div class="head">
              <span>{{($i18n.locale === 'zh') ? '账号登录' : $t('P.Login')}}</span>
            </div>
            <div class="inner rl-padding-top-lllg">
              <form action class="rl-relative">
                <div class="input-box">
                  <input
                    type="text"
                    v-model="loginForm.userName"
                    class="rl-padding-left-mid"
                    :placeholder="$t('P.Username') + '/' + $t('P.Mobile')"
                  />
                </div>
                <div class="input-box">
                  <input
                    type="password"
                    v-model="loginForm.password"
                    @keydown="show($event)"
                    class="rl-padding-left-mid"
                    :placeholder="$t('P.Password')"
                  />
                </div>
                <el-button
                  class="login-btn rl-text-white"
                  :loading="loading"
                  type="primary"
                  @click.native.prevent="submit"
                >{{$t('P.Login')}}</el-button>
                <div class="regis-wrap rl-margin-top-default">
<!--                  <span
                    class="link"
                    @click="showIntroDialog = true"
                  >{{($i18n.locale === 'zh') ? '立即注册' : $t('P.Register')}}</span>
                  <span class="line">|</span> -->
                  <router-link class="link" to="/Retrieval">{{$t('P.ForgotPassword')}}?</router-link>
                </div>
              </form>
            </div>
            <div
              class="service rl-margin-top-double"
              :class="{'service-en': ($i18n.locale === 'en')}"
            >
              <div class="center-top">
                <span>{{$t('P.CustomerService')}}</span>
              </div>
              <div class="center-bottom rl-padding-top-default rl-clear">
                <div class="qrcode rl-fl">
                  <img
                    src="../../assets/images/login/qrcode.jpg"
                  />
                </div>
                <div class="info rl-fl">
                  <p class="rl-text-xxxs">{{$t('P.WeChatOA')}}：BAT</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <RegisterDialog ref="userAgreement" :showOpen="showOpen" :select="true" @close="closeDialog"></RegisterDialog>
      </div>
      <!--公共底部-->
      <Footer></Footer>

      <!-- 产品介绍 -->
      <productIntro v-show="showIntroDialog" @closeIntro="closeProductIntro"></productIntro>
    </div>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
import md5 from 'js-md5';
import Cookies from 'js-cookie';
import { mapGetters } from 'vuex';
import GD from '@/assets/js/globalData';
import RegisterDialog from '@/components/RegisterDialog';
import productIntro from '@/components/dialog/productIntro';
// liu
import {agreementSignStatus,signAgreement,agreementSignId,agreementSignList} from  '@/apiService/api'
import {removeToken } from '@/utils/auth'
export default {
  name: 'Login',
  components: {
    Footer,
    RegisterDialog,
    productIntro
  },
  computed: {
    loginInfo () {
      return {
        userName: this.loginForm.userName,
        password: md5(this.loginForm.password)
      };
    },
    cookieName () {
      return this.loginForm.userName;
    },
    ...mapGetters({
      code: 'getCode',
      times: 'getTimes'
    })
  },
  watch: {
    cookieName (val) {
      if (val) {
        if (Cookies.get(val)) {
          this.loginForm.password = Cookies.get(val);
        }
      }
    }
  },
  data () {
    return {
      loginState: this.$route.query.login_state,
      loading: false,
      loginForm: {
        userName: '',
        password: ''
      },
      userState: 1,
      prompt: 0, // 控制提示
      firstEnter: false,
      showOpen: false,
      useLang: false, // 是否使用多语种 - 施义煌
      langList: GD.langList, // 语种列表 - 施义煌
      lang: 'zh-CNY', // 语种 - 施义煌
      showIntroDialog: false,// 产品介绍
      agreementId:'',  //协议id
    };
  },
  mounted () {
    // var names = window.localStorage.getItem('loginName')
    // this.loginForm.userName = names
    this.clearCookie();
    this.getCookie();
    this.oldName = window.localStorage.getItem('userName');
    
    
  },
  created () {
    // 施义煌
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang')
      ? window.localStorage.getItem('bLang')
      : 'zh_CNY';
    localStorage.removeItem('name')
    localStorage.removeItem('userName')
    localStorage.removeItem('userId');
    localStorage.removeItem('shopOrderList');
    removeToken();
    let tenantNo=localStorage.getItem('tenantNo');

    if(tenantNo&&tenantNo!=null&&tenantNo!=undefined&&tenantNo!=''){
       this.getAgreementSignId();
    }
  },
  methods: {
    /**
     * 施义煌
     * 更换语种
     */
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      window.localStorage.setItem('currency', value.slice(3, 6));
      this.lang = value;
      this.$root.currency = value.slice(3, 6);
      this.$i18n.locale = value.split('_')[0];
      this.$i18n.curren = value.slice(3, 6);
      location.reload();
    },
    show (ev) {
      if (ev.keyCode === 13) {
        // enter
        this.submit();
      }
    },
    // 获取用户协议id
    getAgreementSignId(){
        let agreementArea='';
        if(this.$i18n.locale === 'zh'){
             agreementArea=0;
        }else{
             agreementArea=1;
        }
        agreementSignList({page:1,size:100,agreementArea :agreementArea,type:2}).then(res=>{
          this.agreementId=res.data.list[0].id;
        })
    },
    submit () {
      
      // 登录
      if (this.loginForm.userName === '') {
        this.$message({
          type: 'warning',
          message: this.$t('FormCheck.MobileEmpty') + '！'
        });
        this.prompt = 3;
        return false;
      }
      if (this.loginForm.password === '') {
        this.$message({
          type: 'warning',
          message: this.$t('FormCheck.PasswordEmpty') + '！'
        });
        this.prompt = 4;
        return false;
      }
      if (this.loginForm.password.length < 6) {
        this.$message({
          type: 'warning',
          message: this.$t('FormCheck.PasswordLength') + '！'
        });
        return false;
      }
      this.loading = true;
      this.$store
        .dispatch('Login', this.loginInfo)
        .then(()=> {
              window.localStorage.setItem('name', this.loginForm.userName);
              var exdate = new Date(); // 获取时间
              exdate.setTime(exdate.getTime() + 24 * 60 * 60 * 1000 * 7); // 保存的天数(7天)
              window.document.cookie =
                'loginName' +
                '=' +
                this.loginForm.userName +
                ';path=/;expires=' +
                exdate.toGMTString();
              this.loading = false;

              // this.$router.push({name: 'Index', params: {user_state: this.userState}})
              
            
            // 提示所有用户协议更新弹框
            let brandIds = []
            brandIds.push(100)
            // this.$api.post(this, 'user/brand/agreement/status', {brandIds: brandIds})
            let userId=localStorage.getItem('userId');
            let agreementId=this.agreementId;
            agreementSignStatus({distributorId:userId,agreementId:agreementId}).then(res => {
              if (res.success) {
               
                
                if(!res.data.sign){
                  this.showOpen = true
                }else{
                  if (this.oldName !== this.loginForm.userName || this.$route.params.key === '-1' || this.firstEnter) {
                    this.$router.push({name: 'Index', params: {user_state: this.userState}})
                  } else {
                    this.$router.go(-1)
                  }
                }
                // if (res.distributorBrandAgreementList.length === 0 || (res.distributorBrandAgreementList[0].brandId === 100 && res.distributorBrandAgreementList[0].status === 1)) {
                //   this.showOpen = true
                // } else {
                //   if (this.oldName !== this.loginForm.userName || this.$route.params.key === '-1' || this.firstEnter) {
                //     this.$router.push({name: 'Index', params: {user_state: this.userState}})
                //   } else {
                //     this.$router.go(-1)
                //   }
                // }
              }
            })
        
      }).catch(() => {
        this.loading = false
        if (this.code === 302) {
          this.prompt = 2
        } else {
          this.prompt = 1
        }
      })
      
    },
    closeDialog () {
      // 关闭协议弹出框
      this.showOpen = false;
      let userId=localStorage.getItem('userId');
      let agreementId=this.agreementId;
        signAgreement({distributorId:userId,agreementId:agreementId}).then((res) => {
          if (res.success) {
            this.$router.push({
              name: 'Index',
              params: { user_state: this.userState }
            });
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    clearCookie () {
      // 登录之前先清空cookie缓存token值
      if (document.cookie.length > 0) {
        var arr = document.cookie.split('; '); // 这里显示的格式需要切割一下自己可输出看下
        for (var i = 0; i < arr.length; i++) {
          var arr2 = arr[i].split('='); // 再次切割
          if (arr2[0] === 'Font-Token') {
            // this.$store.dispatch('LogOut').then(() => {});
          }
        }
      }
    },
    // 读取cookie
    getCookie: function () {
      if (document.cookie.length > 0) {
        var arr = document.cookie.split('; '); // 这里显示的格式需要切割一下自己可输出看下
        for (var i = 0; i < arr.length; i++) {
          var arr2 = arr[i].split('='); // 再次切割
          // 判断查找相对应的值
          if (arr2[0] === 'loginName') {
            this.loginForm.userName = arr2[1]; // 保存到保存数据的地方
          }
        }
      }
    },
    // 产品介绍 - 关闭
    closeProductIntro () {
      this.showIntroDialog = false;
    }
  },
  beforeRouteEnter (to, from, next) {
    if (from.name == null || to.params.key === '-1') {
      next((vm) => {
        vm.firstEnter = true;
      });
    } else {
      next((vm) => {
        vm.firstEnter = false;
      });
    }
  },
  
};
</script>
<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.login {
  width: 100%;
  .login-head {
    width: 1200px;
    margin: 0 auto;
    .logo {
      display: flex;
      align-items: center;
      img {
        border: 0px;
      }
    }
  }
}
.main {
  width: 100%;
  height: 580px;
  background: url("../../assets/images/login/login-banner.jpg") no-repeat center
    center;
  .content {
    padding-top: 30px;
    width: 1200px;
    height: 580px;
    .login-enroll {
      padding: 0 34px;
      width: 413px;
      height: 520px;
      box-sizing: border-box;
      border-radius: 8px;
      box-shadow: 0px 3px 7px 4px rgba(231, 231, 231, 0.5);
      .head {
        position: relative;
        padding-top: 54px;
        border-bottom: 1px solid @bat_logo;
        span {
          display: inline-block;
          position: absolute;
          bottom: -15px;
          left: 50%;
          padding: 0 16px;
          height: 32px;
          line-height: 32px;
          font-size: 24px;
          color: @bat_logo;
          text-align: center;
          background-color: @white;
          transform: translateX(-50%);
        }
      }
      .inner {
        margin-top: 4px;
        .input-box {
          width: 100%;
          height: 48px;
          line-height: 48px;
          border: 1px solid @bdLighterColor;
          border-radius: 4px;
          overflow: hidden;
          & + .input-box {
            margin-top: 10px;
          }
          input {
            height: 48px;
            box-sizing: border-box;
            &:-webkit-autofill {
              -webkit-box-shadow: 0 0 0 1000px @white inset !important;
            }
          }
        }
        .login-btn {
          margin-top: 26px;
          width: 100%;
          height: 48px;
          font-size: 18px;
          border-color: @bat_logo;
          background-color: @bat_logo;
          border-radius: 4px;
          &:hover {
            background-color: @bat_logo;
			opacity: 0.7;
          }
        }
        .regis-wrap {
          color: @sLightGray;
          text-align: center;
          .link {
            color: @sLightGray;
            cursor: pointer;
            &:hover {
              color: @bat_logo;
            }
          }
          .line {
            margin: 0 2px;
          }
        }
      }
      .service {
        .center-top {
          position: relative;
          padding-top: 12px;
          border-bottom: 1px solid @bat_logo;
          span {
            display: inline-block;
            position: absolute;
            bottom: -10px;
            left: 50%;
            padding: 0 16px;
            height: 22px;
            line-height: 22px;
            font-size: 16px;
            color: @bat_logo;
            text-align: center;
            background-color: @white;
            transform: translateX(-50%);
          }
        }
        .center-bottom {
          padding-left: 36px;
          .qrcode {
            width: 95px;
            img {
              width: 100%;
            }
          }
          .info {
            padding-top: 24px;
            padding-left: 13px;
            p {
              font-size: 14px;
              color: @sLightGray;
              line-height: 24px;
            }
          }
        }
        &.service-en {
          .center-bottom {
            padding-left: 8px;
            .info {
              p {
                font-size: 12px;
              }
            }
          }
        }
      }
    }
  }
}
.lang-box {
  display: inline-block;
  width: 140px;
  margin-left: 20px;
}

.logo-img {
  width: 117px;
}
.tips{
  margin-top:10px;
}
</style>
